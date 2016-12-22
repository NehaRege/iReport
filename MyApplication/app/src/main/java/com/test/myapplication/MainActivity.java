package com.test.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "MainActivity";
    //private static final int REQUEST_SIGNUP = 0;
    private static final int RC_SIGN_IN_GOOGLE = 9001;

    LoginButton fbLoginButton;
    CallbackManager callbackManager;

    SignInButton googleSignInButton;
    Button signOutButton;
    Button disconnectButton;
    Button signOutButtonFb;
    Button loginButton;

    //TextView textViewSignUp;

    EditText editTextEmail;
    EditText editTextPassword;

    String fb_email;
    String gmail;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    private GoogleApiClient googleApiClient;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.activity_main);

        Intent intent = getIntent();

        googleSignInAndApiClientSetUp();

        initializeViews();

        firebaseAuth = FirebaseAuth.getInstance();

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null) {

                    String name = firebaseUser.getDisplayName();
                    fb_email = firebaseUser.getEmail();
                    Uri photoUrl = firebaseUser.getPhotoUrl();

                    // The user's ID, unique to the Firebase project. Do NOT use this value to
                    // authenticate with your backend server, if you have one. Use
                    // FirebaseUser.getToken() instead.

                    String uid = firebaseUser.getUid();

                    Toast.makeText(MainActivity.this, "Logged in via Facebook as: "+firebaseUser.getEmail(), Toast.LENGTH_SHORT).show();

                    Log.i(TAG, "************************************************************************");
                    Log.i(TAG, "onAuthStateChanged: fb email = "+ fb_email);
                    Log.i(TAG, "onAuthStateChanged: fb name = "+ name);
                    Log.i(TAG, "************************************************************************");

                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("fb_address",fb_email);
                    editor.commit();

                    Intent intent = new Intent(MainActivity.this,HomePage.class);
                    startActivity(intent);

                } else {

                    Log.i(TAG, "onAuthStateChanged: logged_out");

                }

//                updateUIFb(firebaseUser);


            }
        };


        AppEventsLogger.activateApp(this);

        hashKeyGenerationFacebook();

        callbackManager = CallbackManager.Factory.create();
        fbLoginButton.setReadPermissions("email", "public_profile");
        fbLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.i(TAG, "onCancel: ");
//                updateUIFb(null);


            }

            @Override
            public void onError(FacebookException error) {
                Log.i(TAG, "onError: ");
//                updateUIFb(null);


            }
        });



        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = editTextEmail.getText().toString();
                //String password = editTextPassword.getText().toString();

                if (email.equals("")) {

                    editTextEmail.setError("This field is required!");

                    final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Alert!")
                            .setMessage("Please enter a valid Email Address")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    builder.create().dismiss();
                                }
                            });

                    AlertDialog dialog = builder.create();
                    dialog.show();

                }  else {

                    Log.i(TAG, "onClick: email = " + email);


                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("login_address",email);
                    editor.commit();

                    Intent intent = new Intent(MainActivity.this,HomePage.class);
                    intent.putExtra("login",email);
                    startActivity(intent);

                }
            }
        });




        googleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInGoogle();
            }
        });

        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOutGoogle();
            }
        });

        disconnectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                revokeAccessGoogle();
            }
        });

        signOutButtonFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOutFacebook();
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authStateListener != null) {
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        callbackManager.onActivityResult(requestCode,resultCode,data);

        if (requestCode == RC_SIGN_IN_GOOGLE) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleGoogleSignInResult(result);
        }

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(TAG, "onConnectionFailed: ");
    }


    private void handleFacebookAccessToken(AccessToken token) {

        Log.i(TAG, "handleFacebookAccessToken: "+token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());

        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {

                            Log.w(TAG, "signInWithCredential", task.getException());

                            Toast.makeText(MainActivity.this, "Authentication failed.",Toast.LENGTH_SHORT).show();

                        }
                    }
                });

    }

    public void signOutFacebook() {
        firebaseAuth.signOut();
        LoginManager.getInstance().logOut();

    }


    private void handleGoogleSignInResult(GoogleSignInResult result) {

        if (result.isSuccess()) {

            GoogleSignInAccount googleAcc = result.getSignInAccount();

            if(googleAcc!=null) {

                // google sign in successful !

                gmail=googleAcc.getEmail();

                Log.i(TAG, "handleGoogleSignInResult: gmail display name  = " + googleAcc.getDisplayName());
                Log.i(TAG, "handleGoogleSignInResult: gmail = "+ googleAcc.getEmail());


                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("gmail_address",gmail);
                editor.commit();

                Toast.makeText(MainActivity.this, "Signed in using google as: "+ googleAcc.getEmail(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this,OfficialActivity.class);
                startActivity(intent);

            }

            updateUI(true);

        } else {

            updateUI(false);

            Log.i(TAG, "handleGoogleSignInResult: gmail login failed !");
        }
    }


    private void signInGoogle() {
        Intent googleSignInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(googleSignInIntent,RC_SIGN_IN_GOOGLE);
    }

    private void signOutGoogle() {
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        updateUI(false);
                    }
                });
    }

    private void revokeAccessGoogle() {
        Auth.GoogleSignInApi.revokeAccess(googleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        updateUI(false);
                    }
                });
    }

    private void updateUI(boolean signedIn) {
        if (signedIn) {
            googleSignInButton.setVisibility(View.GONE);
            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.VISIBLE);
        } else {

            googleSignInButton.setVisibility(View.VISIBLE);
            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.GONE);
        }
    }

    private void googleSignInAndApiClientSetUp() {

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */,this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    private void initializeViews() {

        googleSignInButton = (SignInButton) findViewById(R.id.sign_in_button_google);
        googleSignInButton.setSize(SignInButton.SIZE_STANDARD);
        signOutButton = (Button) findViewById(R.id.sign_out_button);
        disconnectButton = (Button) findViewById(R.id.disconnect_button);
        fbLoginButton = (LoginButton) findViewById(R.id.login_button_fb);
        signOutButtonFb = (Button) findViewById(R.id.sign_out_button_fb);
        loginButton = (Button) findViewById(R.id.btn_login);

        editTextEmail = (EditText) findViewById(R.id.input_email);
        //editTextPassword = (EditText) findViewById(R.id.input_password);

        //textViewSignUp = (TextView) findViewById(R.id.link_signup);

    }

    private void hashKeyGenerationFacebook() {
        PackageInfo info;
        try {
            info = getPackageManager().getPackageInfo("com.test.myapplication", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                Log.e("hash key", something);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("no such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("exception", e.toString());
        }
    }

}
