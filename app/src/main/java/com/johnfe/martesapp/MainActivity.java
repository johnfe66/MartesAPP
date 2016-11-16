package com.johnfe.martesapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private Button btnSignUp;
    private Button btnLogIn;
    private EditText txtEmail;
    private EditText txtPassword;

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();

        btnLogIn= (Button) findViewById(R.id.btnLogIn);
        btnSignUp= (Button) findViewById(R.id.btnSingUp);
        txtEmail= (EditText) findViewById(R.id.txtEmail);
        txtPassword= (EditText) findViewById(R.id.txtPassword);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("Main activity", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d("Main Activity", "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.createUserWithEmailAndPassword(txtEmail.getText().toString().trim(), txtPassword.getText().toString().trim())
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Log.d("", "createUserWithEmail:onComplete:" + task.isSuccessful());

                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    Toast.makeText(MainActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(MainActivity.this, "Authentication OK.",
                                            Toast.LENGTH_SHORT).show();
                                    mAuth.getCurrentUser().sendEmailVerification()
                                            .addOnCompleteListener(MainActivity.this, new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        Toast.makeText(MainActivity.this, "Email enviado OK.",
                                                                Toast.LENGTH_SHORT).show();

                                                    }else{
                                                        Toast.makeText(MainActivity.this, "Email no enviado",
                                                                Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });

                                }

                                // ...
                            }
                        });
            }
        });

        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mAuth.signInWithEmailAndPassword(txtEmail.getText().toString().trim(), txtPassword.getText().toString().trim())
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Log.d("Main activity - login", "signInWithEmail:onComplete:" + task.isSuccessful());

                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    Log.w("Main activity - login", "signInWithEmail", task.getException());
                                    Toast.makeText(MainActivity.this, "login failed.",
                                            Toast.LENGTH_SHORT).show();
                                }else{
                                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                            .setDisplayName("nombre")
                                            .setPhotoUri(Uri.parse("https://fbcdn-sphotos-e-a.akamaihd.net/hphotos-ak-xaf1/v/t1.0-9/30382_10151143006550458_1277185577_n.jpg?oh=75fd5ad8033c23b7deb3f30fbec965c1&oe=58CC7890&__gda__=1490377027_e3d19a221bbcaded909296e7fab737fe"))
                                            .build();
                                    mAuth.getCurrentUser().updateProfile(profileUpdates);
                                    Log.w("Main activity - login", "signInWithEmail", task.getException());
                                    Toast.makeText(MainActivity.this, "Bienvenido "+ mAuth.getInstance().getCurrentUser().getDisplayName(),
                                            Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(MainActivity.this,BaseDatos.class);
                                    //intent.putExtra("USER", (Parcelable) mAuth);
                                    startActivity(intent);

                                }

                                // ...
                            }
                        });

            }
        });


    }
}
