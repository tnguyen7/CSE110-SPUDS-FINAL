package com.spuds.eventapp.Firebase;

/**
 * Created by Arjun on 5/5/16.
 */

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.Map;

/**
 * Created by Arjun on 5/5/16.
 */
public class AccountFirebase {
    public void createAccount(String email, String password) {


        Firebase ref = new Firebase("https://eventory.firebaseio.com");
        ref.createUser(email, password, new Firebase.ValueResultHandler<Map<String, Object>>()

                {
                    @Override
                    public void onSuccess (Map < String, Object > result){
                        System.out.println("Successfully created user account with uid: " + result.get("uid"));
                    }

                    @Override
                    public void onError (FirebaseError firebaseError){
                        // there was an error
                    }
                }

        );
    }
    public void logIn() {
        Firebase ref = new Firebase("https://eventory.firebaseio.com");
        ref.authWithPassword("bobtony@firebase.com", "correcthorsebatterystaple", new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {
                System.out.println("User ID: " + authData.getUid() + ", Provider: " + authData.getProvider());
            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                // there was an error
            }
        });
    }
    void changePass() {
        Firebase ref = new Firebase("https://eventory.firebaseio.com");
        ref.changePassword("bobtony@firebase.com", "correcthorsebatterystaple", "securenewpassword", new Firebase.ResultHandler() {
            @Override
            public void onSuccess() {
                // password changed
            }

            @Override
            public void onError(FirebaseError firebaseError) {
                // error encountered
            }
        });
    }
    void resetPass() {
        Firebase ref = new Firebase("https://eventory.firebaseio.com");
        ref.resetPassword("bobtony@firebase.com", new Firebase.ResultHandler() {
            @Override
            public void onSuccess() {
                // password reset email sent
            }

            @Override
            public void onError(FirebaseError firebaseError) {
                // error encountered
            }
        });
    }
    public void removingAccount() {
        Firebase ref = new Firebase("https://eventory.firebaseio.com");
        ref.removeUser("bobtony@firebase.com", "correcthorsebatterystaple", new Firebase.ResultHandler() {
            @Override
            public void onSuccess() {
                // user removed
            }

            @Override
            public void onError(FirebaseError firebaseError) {
                // error encountered
            }
        });
    }
}
