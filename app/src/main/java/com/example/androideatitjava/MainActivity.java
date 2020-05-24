package com.example.androideatitjava;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.androideatitjava.Common.Common;
import com.example.androideatitjava.Remote.ICloudFunctions;
import com.example.androideatitjava.Remote.RetrofitCloudClient;
import com.facebook.accountkit.AccessToken;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;
import com.google.android.gms.dynamic.IFragmentWrapper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import dmax.dialog.SpotsDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class MainActivity extends AppCompatActivity {
    private static int APP_REQUEST_CODE = 7171; //any number
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener listener;
    private AlertDialog dialog;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private ICloudFunctions cloudFunctions;

    private DatabaseReference userRef;

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(listener);
    }

    @Override
    protected void onStop() {
        if (listener != null)
            firebaseAuth.addAuthStateListener(listener);
            compositeDisposable.clear();
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


      init();
    }

    private void init() {
        userRef = FirebaseDatabase.getInstance().getReference(Common.USER_REFERENCES);
        firebaseAuth = FirebaseAuth.getInstance();
        dialog = new SpotsDialog.Builder().setCancelable(false).setContext(this).build();
        cloudFunctions = RetrofitCloudClient.getInstance().create(ICloudFunctions.class);
        listener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){
                    //Already login
                    Toast.makeText(MainActivity.this, "already logged in", Toast.LENGTH_SHORT).show();

                }


                else{

                    Toast.makeText(MainActivity.this, "not logged in", Toast.LENGTH_SHORT).show();

                }
            }
        };
    }

    private void checkUserFromFirebase(String uid) {
        userRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    Toast.makeText(MainActivity.this, "You already registerd", Toast.LENGTH_SHORT).show();

                }
                else{
                         showRegisterDialog(uid );
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, ""+ databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showRegisterDialog(String uid) {

    }
}

   /* private void phoneLogin() {
        Intent intent = new Intent(MainActivity.this , AccountKitActivity.class);
        AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder = new AccountKitConfiguration.AccountKitConfigurationBuilder(LoginType.PHONE,
              AccountKitActivity.ResponseType.TOKEN );
        intent.putExtra(AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION, configurationBuilder.build());
        startActivityForResult(intent , APP_REQUEST_CODE);

    } */

/*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == APP_REQUEST_CODE)
            handleFacebookLoginResult(resultCode , data);

    }



    private void handleFacebookLoginResult(int resultCode, Intent data) {
        AccountKitLoginResult result = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);
        if (result.getError() != null)
        {
            Toast.makeText(this, result.getError().getUserFacingMessage(), Toast.LENGTH_SHORT).show();
        }
        else if(result.wasCancelled() || resultCode ==  RESULT_CANCELED){
            finish();
        }
        else{

            if (result.getAccessToken() != null){
                getCustomToken(result.getAccessToken());
                Toast.makeText(this, "Sign in OK", Toast.LENGTH_SHORT).show();
            }

        }

    }


    private void getCustomToken(AccessToken accessToken) {
      dialog.show();
      compositeDisposable.add(cloudFunctions.getCustomtToken(accessToken.getToken())
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(responseBody -> {

          String customerToken =  responseBody.toString();
          signInWithCustomToken(customerToken);

      }, throwable -> {
          dialog.dismiss();
          Toast.makeText(this,  ""+ throwable.getMessage(), Toast.LENGTH_SHORT).show();

      }
      ));
    }

    private void signInWithCustomToken(String customerToken) {
         dialog.dismiss();
         firebaseAuth.signInWithCustomToken(customerToken)
                 .addOnCompleteListener(task -> {

                     Toast.makeText(this , "Authentication Failed " , Toast.LENGTH_SHORT).show();



                 });
    }

}
*/
