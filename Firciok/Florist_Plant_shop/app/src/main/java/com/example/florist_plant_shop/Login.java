package com.example.florist_plant_shop;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {
    EditText SignIn_Username, SignIn_Password;
    TextView Create_Account_button,Forgot_Password_Text;
    SignInButton button_SignIn;
    MaterialButton button_Login;
    GoogleSignInClient googleSignInClient;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Assign variable
        firebaseAuth = FirebaseAuth.getInstance();
        button_SignIn = findViewById(R.id.button_SignIn);
        Create_Account_button = findViewById(R.id.Create_Account_button);
        SignIn_Username = findViewById(R.id.SignIn_Username);
        SignIn_Password = findViewById(R.id.SignIn_Password);
        button_Login = findViewById(R.id.button_Login);
        Forgot_Password_Text=findViewById(R.id.Forgot_Password_Text);

        //-------------------x--------------------------x------------------------x------------------------x-------------------------x------------------------x---------------//
        // RESET PASSWORD
        Forgot_Password_Text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(Login.this);
                View dialogView=getLayoutInflater().inflate(R.layout.dialog_forgot,null);
                EditText emailBox=dialogView.findViewById(R.id.emailBox);
                EditText nameBox=dialogView.findViewById(R.id.name_box);
                builder.setView(dialogView);
                AlertDialog dialog=builder.create();
                dialogView.findViewById(R.id.btnReset).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String userEmail=emailBox.getText().toString();
                        String userName=nameBox.getText().toString();
                        if (TextUtils.isEmpty(userName)){
                            Toast.makeText(Login.this,"Enter your registered Username",Toast.LENGTH_SHORT).show();
                        }
                        if (TextUtils.isEmpty(userEmail) && !Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()){
                            Toast.makeText(Login.this,"Enter your registered email id",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
                        Query checkUserDatabase = reference.orderByChild("username").equalTo(userName);
                        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()){
                                    nameBox.setError(null);
                                    String usernameFromDB=snapshot.child(userName).child("username").getValue(String.class);
                                    if (usernameFromDB.equals(userName)){
                                        firebaseAuth.sendPasswordResetEmail(userEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    Toast.makeText(Login.this,"Check your email",Toast.LENGTH_SHORT).show();
                                                    dialog.dismiss();
                                                }
                                                else {
                                                    Toast.makeText(Login.this,"Unable to send,failed",Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }
                                }
                                else {
                                    Toast.makeText(Login.this,"Invalid credentials",Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }
                });
                dialogView.findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                if (dialog.getWindow()!=null){
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                }
                dialog.show();
            }
        });

        //-------------------x--------------------------x------------------------x------------------------x-------------------------x------------------------x---------------//
        button_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!validateUsername() | !validatePassword()){
                }
                else {
                    checkUser();
                }
            }
        });

        //-------------------x--------------------------x------------------------x------------------------x-------------------------x------------------------x---------------//

        Create_Account_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Create_Account.class);
                startActivity(i);
            }
        });

        //-------------------x--------------------------x------------------------x------------------------x-------------------------x------------------------x---------------//

        // Initialize sign in options the client-id is copied form google-services.json file
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Initialize sign in client
        googleSignInClient = GoogleSignIn.getClient(Login.this, googleSignInOptions);

        button_SignIn.setOnClickListener((View.OnClickListener) view -> {
            // Initialize sign in intent
            Intent intent = googleSignInClient.getSignInIntent();
            // Start activity for result
            startActivityForResult(intent, 100);
        });

        // Initialize firebase auth
        firebaseAuth = FirebaseAuth.getInstance();
        // Initialize firebase user
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        // Check condition
        if (firebaseUser != null) {
            // When user already sign in redirect to profile activity
            startActivity(new Intent(Login.this, activity_account.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Check condition
        if (requestCode == 100) {
            // When request code is equal to 100 initialize task
            Task<GoogleSignInAccount> signInAccountTask = GoogleSignIn.getSignedInAccountFromIntent(data);
            // check condition
            if (signInAccountTask.isSuccessful()) {
                // When google sign in successful initialize string
                String s = "Google sign in successful";
                // Display Toast
                displayToast(s);
                // Initialize sign in account
                try {
                    // Initialize sign in account
                    GoogleSignInAccount googleSignInAccount = signInAccountTask.getResult(ApiException.class);
                    // Check condition
                    if (googleSignInAccount != null) {
                        // When sign in account is not equal to null initialize auth credential
                        AuthCredential authCredential = GoogleAuthProvider.getCredential(googleSignInAccount.getIdToken(), null);
                        // Check credential
                        firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // Check condition
                                if (task.isSuccessful()) {
                                    // When task is successful redirect to profile activity display Toast
                                    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                                    String userEmail=firebaseUser.getEmail();
//                                    UserDataSingleton.getInstance().setName(userEmail);
                                    startActivity(new Intent(Login.this, edit_profile.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                                    displayToast("Firebase authentication successful");
                                } else {
                                    // When task is unsuccessful display Toast
                                    displayToast("Authentication Failed :" + task.getException().getMessage());
                                }
                            }
                        });
                    }
                } catch (ApiException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void displayToast(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
    }

    public boolean validateUsername(){
        String val = SignIn_Username.getText().toString();
        if (val.isEmpty()){
            SignIn_Username.setError("Username cannot be empty");
            return false;
        }else {
            SignIn_Username.setError(null);
            return true;
        }
    }

    public boolean validatePassword() {
        String val = SignIn_Password.getText().toString();
        if (val.isEmpty()){
            SignIn_Password.setError("Password cannot be empty");
            return false;
        }else {
            SignIn_Password.setError(null);
            return true;
        }
    }

    public void checkUser(){
        String userUsername = SignIn_Username.getText().toString().trim();
        String userPassword = SignIn_Password.getText().toString().trim();


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUserDatabase = reference.orderByChild("username").equalTo(userUsername);

        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    if (snapshot.exists()){

                        SignIn_Username.setError(null);
                        String passwordFromDB = snapshot.child(userUsername).child("password").getValue(String.class);

                        if (passwordFromDB.equals(userPassword)){
                            SignIn_Username.setError(null);

                            String nameFromDB = snapshot.child(userUsername).child("name").getValue(String.class);
                            String usernameFromDB=snapshot.child(userUsername).child("username").getValue(String.class);
                            String emailFromDB=snapshot.child(userUsername).child("email").getValue(String.class);
                            String numberFromDB=snapshot.child(userUsername).child("number").getValue(String.class);
                            String Logout="Logout";

                            UserDataSingleton.getInstance().setName(nameFromDB);
                            UserDataSingleton.getInstance().setEmail(emailFromDB);
                            UserDataSingleton.getInstance().setUsername(usernameFromDB);
                            UserDataSingleton.getInstance().setNumber(numberFromDB);
                            UserDataSingleton.getInstance().setPassword(passwordFromDB);
                            Toast.makeText(Login.this,"you have signed in successfully",Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(Login.this,activity_account.class);
                            intent.putExtra("name",nameFromDB);
                            intent.putExtra("password",passwordFromDB);
                            intent.putExtra("username",usernameFromDB);
                            intent.putExtra("email",emailFromDB);
                            intent.putExtra("number",numberFromDB);
//                            intent.putExtra("button",Logout);
                            startActivity(intent);
                        }else {
                            SignIn_Password.setError("Inavlid credentials");
                            SignIn_Password.requestFocus();
                        }
                    }else {
                        SignIn_Username.setError("User cannot be empty");
                        SignIn_Username.requestFocus();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}





