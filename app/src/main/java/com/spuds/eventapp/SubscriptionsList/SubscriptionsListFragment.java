package com.spuds.eventapp.SubscriptionsList;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.spuds.eventapp.Firebase.UserFirebase;
import com.spuds.eventapp.R;
import com.spuds.eventapp.Shared.MainActivity;
import com.spuds.eventapp.Shared.Subscription;

import java.util.ArrayList;


public class SubscriptionsListFragment extends Fragment {
    private ArrayList<Subscription> subscriptions;
    public SubscriptionsListRVAdapter adapter;
    UserFirebase userFirebase;

    public SubscriptionsListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userFirebase = new UserFirebase();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.recycler, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Subscriptions");
        final RecyclerView rv=(RecyclerView) v.findViewById(R.id.rv);
        subscriptions = new ArrayList<>();

        userFirebase.getSubscriptions(subscriptions);

        LinearLayoutManager llm = new LinearLayoutManager(v.getContext());
        rv.setLayoutManager(llm);


        adapter = new SubscriptionsListRVAdapter(subscriptions, this);
        rv.setAdapter(adapter);

        new Thread(new Runnable() {

            @Override
            public void run() {
                while (userFirebase.numSubscriptions > subscriptions.size() || !userFirebase.getSubscriptionsThreadCheck) {
                    //("sublist", "numsubs" + userFirebase.numSubscriptions);
                    //("sublist", "subscriptions size" + subscriptions.size());
                    try {
                        Thread.sleep(70);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        rv.setAdapter(adapter);
                    }
                });
            }
        }).start();

        refreshing(v);

        return v;
    }

    public void refreshing(View view) {
        final SwipeRefreshLayout mySwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        mySwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        subscriptions.clear();

                        userFirebase.getSubscriptions(subscriptions);

                        //("refresh", "here");
                        new Thread(new Runnable() {

                            @Override
                            public void run() {
                                //("refresh", "hereherehere");

                                while (userFirebase.numSubscriptions > subscriptions.size() || !userFirebase.getSubscriptionsThreadCheck) {
                                    ////("refresh", "size: " + events.size());
                                    try {
                                        Thread.sleep(70);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }

                                //("refresh", "here2");
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run()
                                    {
                                        //("refresh", "here3");

                                        adapter.notifyDataSetChanged();
                                        mySwipeRefreshLayout.setRefreshing(false);

                                    }
                                });
                            }
                        }).start();
                    }
                }
        );
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)getActivity()).addSearchToolbar();
        ((MainActivity)getActivity()).searchType = getString(R.string.fragment_my_sub);
    }

}
