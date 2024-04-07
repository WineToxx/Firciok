package com.example.florist_plant_shop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Create_Account extends AppCompatActivity {

    TextView button_Login;
    private FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference reference;

    TextInputEditText SignUp_Name,SignUp_Username,SignUp_Password,SignUp_Number,SignUp_Email;
    MaterialButton SignUp_Button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        auth=FirebaseAuth.getInstance();
        SignUp_Name=findViewById(R.id.SignUp_Name);
        SignUp_Email=findViewById(R.id.SignUp_Email);
        SignUp_Username=findViewById(R.id.SignUp_Username);
        SignUp_Password=findViewById(R.id.SignUp_Password);
        SignUp_Number=findViewById(R.id.SignUp_Number);
        SignUp_Button=findViewById(R.id.SignUp_Button);
        button_Login=findViewById(R.id.button_Login);

        button_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(getApplicationContext(), Login.class);
                startActivity(i);
            }
        });

        SignUp_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database=FirebaseDatabase.getInstance();
                reference=database.getReference("users");
                String name=SignUp_Name.getText().toString();
                String username=SignUp_Username.getText().toString();
                String email=SignUp_Email.getText().toString();
                String number=SignUp_Number.getText().toString();
                String password=SignUp_Password.getText().toString();

                if (validateName(name) && validateUsername(username) && validateEmail(email) && validateNumber(number) && validatePassword(password)){
                    HelperClass helperClass=new HelperClass(name,username,email,number,password);
                    reference.child(username).setValue(helperClass);

                    auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Toast.makeText(Create_Account.this,"you have signed up successfully",Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(Create_Account.this,Login.class);
                            startActivity(intent);
                        }
                    });

                }
            }
        });

    }

    private boolean validatePassword(String password) {
        if (password.length() < 4) {
            SignUp_Password.setError("Password must be at least 4 characters");
            return false;
        } else {
            SignUp_Password.setError(null);
            return true;
        }
    }
    private boolean validateUsername(String username) {
        if (username.length() < 5  || !username.matches("^[a-zA-Z0-9]+$")) {
            SignUp_Username.setError("> 5 characters with only letters and numbers");
            return false;
        } else {
            SignUp_Username.setError(null);
            return true;
        }
    }

    private boolean validateName(String name) {
        if (!name.matches("^[a-zA-Z]+$") ) {
            SignUp_Name.setError("Name can only contain text ");
            return false;
        } else {
            SignUp_Name.setError(null);
            return true;
        }
    }

    private boolean validateEmail(String email){
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            SignUp_Email.setError("example@email.com");
            return false;
        } else {
            SignUp_Email.setError(null);
            return true;
        }
    }

    private boolean validateNumber(String number){
        if (!number.matches("^971\\d{9}$")) {
            SignUp_Number.setError("Number must start with 971 ");
            return false;
        } else {
            SignUp_Number.setError(null);
            return true;
        }
    }
}


