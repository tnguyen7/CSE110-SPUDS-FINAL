package com.spuds.eventapp.SubscriptionsList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.spuds.eventapp.Firebase.UserFirebase;
import com.spuds.eventapp.Profile.ProfileFragment;
import com.spuds.eventapp.R;
import com.spuds.eventapp.Shared.MainActivity;
import com.spuds.eventapp.Shared.Subscription;

import java.util.List;


/**
 * Created by David on 4/22/16.
 */
public class SubscriptionsListRVAdapter extends RecyclerView.Adapter<SubscriptionsListRVAdapter.SubViewHolder>{

    public Fragment currentFragment;
    List<Subscription> subscriptions;

    public static class SubViewHolder extends RecyclerView.ViewHolder {
        CardView card;
        TextView subName;
        ImageView subPhoto;
        Button toggleFollow;

        SubViewHolder(View itemView) {
            super(itemView);
            card = (CardView)itemView.findViewById(R.id.cv);
            subName = (TextView)itemView.findViewById(R.id.sub_name);
            subPhoto = (ImageView)itemView.findViewById(R.id.sub_photo);
            toggleFollow = (Button)itemView.findViewById(R.id.follow_toggle);
        }
    }



    public SubscriptionsListRVAdapter(List<Subscription> subscriptions, Fragment currentFragment){
        this.subscriptions = subscriptions;
        this.currentFragment = currentFragment;
    }


    @Override
    public int getItemCount() {
        return subscriptions.size();
    }

    @Override
    public SubViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_my_subscriptions, viewGroup, false);
        overrideFonts(v.getContext(),v);
        SubViewHolder svh = new SubViewHolder(v);
        return svh;
    }

    @Override
    public void onBindViewHolder(SubViewHolder subViewHolder, int i) {
        final Subscription currentSub = subscriptions.get(i);
        subViewHolder.subName.setText(subscriptions.get(i).name);

        subViewHolder.subPhoto.setImageResource(subscriptions.get(i).photoId);


        Bitmap src = BitmapFactory.decodeResource(currentFragment.getResources(), R.drawable.arjun);
        RoundedBitmapDrawable dr =
                RoundedBitmapDrawableFactory.create(currentFragment.getResources(), src);
        dr.setCornerRadius(Math.max(src.getWidth(), src.getHeight()) / 2.0f);
        subViewHolder.subPhoto.setImageDrawable(dr);


        subViewHolder.toggleFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // already following this user
                if (currentSub.follow){
                    currentSub.follow = false;
                    // TODO (V): Add image for "Follow" button
                    //currentSubViewHolder.toggleFollow.setImageResource(R.drawable.button_not_subscribed);
                    // TODO (M): Update database for follow boolean
                }
                else{
                    currentSub.follow = true;
                    // TODO (V): Add image for "Unfollow" button
                    //currentSubViewHolder.toggleFollow.setImageResource(R.drawable.button_subscribed);
                    // TODO (M): Update database for follow boolean

                }
            }
        });

        subViewHolder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final UserFirebase userFirebase = new UserFirebase();
                userFirebase.getAnotherUser(currentSub.userId);

                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        while (!userFirebase.threadCheckAnotherUser) {
                            try {
                                Thread.sleep(77);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                        }

                        startProfileFragment(userFirebase);

                    }
                }).start();


            }
        });
    }

    private void startProfileFragment(final UserFirebase userFirebase) {

        Fragment profileFragment = new ProfileFragment();

        Bundle bundle = new Bundle();
        bundle.putString(currentFragment.getString(R.string.profile_type),
                currentFragment.getString(R.string.profile_type_other));


        bundle.putSerializable(currentFragment.getString(R.string.user_details), userFirebase.anotherUser);

        profileFragment.setArguments(bundle);

        currentFragment.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((MainActivity)currentFragment.getActivity()).removeSearchToolbar();
            }
        });
        // Add Event Details Fragment to fragment manager
        currentFragment.getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_frame_layout, profileFragment)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .addToBackStack(currentFragment.getString(R.string.fragment_profile))
                .commit();



    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    private void overrideFonts(final Context context, final View v) {
        try {
            if (v instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) v;
                for (int i = 0; i < vg.getChildCount(); i++) {
                    View child = vg.getChildAt(i);
                    overrideFonts(context, child);
                }
            } else if (v instanceof TextView ) {
                ((TextView) v).setTypeface(Typeface.createFromAsset(context.getAssets(), "Raleway-Medium.ttf"));
            }
        }
        catch (Exception e) {
        }
    }

}
