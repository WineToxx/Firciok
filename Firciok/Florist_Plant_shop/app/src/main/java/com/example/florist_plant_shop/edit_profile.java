package com.example.florist_plant_shop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class edit_profile extends AppCompatActivity {
    TextView Fixed_Username;
    TextInputEditText Edit_Name,Edit_Email,Edit_Password,Edit_Number;
    MaterialButton Update_Button;
    String nameUser,emailUser,passwordUser,numberUser,usernameUser;
    DatabaseReference reference;
    FirebaseAuth firebaseAuth;
    TextInputLayout Password_Heading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        Edit_Name=findViewById(R.id.Edit_Name);
        Edit_Email=findViewById(R.id.Edit_Email);
        Edit_Password=findViewById(R.id.Edit_Password);
        Edit_Number=findViewById(R.id.Edit_Number);
        Update_Button=findViewById(R.id.Update_Button);
        Fixed_Username=findViewById(R.id.Fixed_Username);
        Password_Heading=findViewById(R.id.Password_Layout);
        firebaseAuth = FirebaseAuth.getInstance();
        // Initialize firebase user
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        reference= FirebaseDatabase.getInstance().getReference("users");

        showData();

        Update_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = Edit_Name.getText().toString();
                String email = Edit_Email.getText().toString();
                String number = Edit_Number.getText().toString();

                if (validateName(name) && validateEmail(email) && validateNumber(number)) {
                    if (firebaseUser == null) {
                        if (isNameChanged() || isPasswordChanged() || isEmailChanged() || isNumberChanged()) {

                            UserDataSingleton userData = UserDataSingleton.getInstance();
                            userData.setName(Edit_Name.getText().toString());
                            userData.setEmail(Edit_Email.getText().toString());
                            userData.setPassword(Edit_Password.getText().toString());
                            userData.setNumber(Edit_Number.getText().toString());

                            Toast.makeText(edit_profile.this, "Saved", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(edit_profile.this, activity_account.class));
                        } else {
                            Toast.makeText(edit_profile.this, "No Changes found", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        if (validateName(name) && validateEmail(email) && validateNumber(number)) {
                            UserDataSingleton userData = UserDataSingleton.getInstance();
                            userData.setName(name);
                            userData.setEmail(email);
                            userData.setNumber(number);
                            userData.setUsername(firebaseUser.getEmail());
                            Toast.makeText(edit_profile.this, "Saved", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(edit_profile.this, activity_account.class));
                        }
                    }
                }
            }
        });
    }

    public boolean isNameChanged(){
        if (!nameUser.equals(Edit_Name.getText().toString())){
            reference.child(usernameUser).child("name").setValue(Edit_Name.getText().toString());
            nameUser = Edit_Name.getText().toString();
            return true;
        }else return false;
    }


    public boolean isEmailChanged(){
        if (!emailUser.equals(Edit_Email.getText().toString())){
            reference.child(usernameUser).child("email").setValue(Edit_Email.getText().toString());
            emailUser = Edit_Email.getText().toString();
            return true;
        }else return false;
    }

    public boolean isPasswordChanged(){
        if (!passwordUser.equals(Edit_Password.getText().toString())){
            reference.child(usernameUser).child("password").setValue(Edit_Password.getText().toString());
            passwordUser = Edit_Password.getText().toString();
            return true;
        }else return false;
    }

    public boolean isNumberChanged(){
        if (!numberUser.equals(Edit_Number.getText().toString())){
            reference.child(usernameUser).child("number").setValue(Edit_Number.getText().toString());
            numberUser = Edit_Number.getText().toString();
            return true;
        }else return false;
    }

    public void showData(){
        firebaseAuth = FirebaseAuth.getInstance();
        // Initialize firebase user
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        UserDataSingleton userData = UserDataSingleton.getInstance();

        if (firebaseUser==null){
            Intent intent=getIntent();
            nameUser=intent.getStringExtra("name");
            usernameUser=intent.getStringExtra("username");
            emailUser=intent.getStringExtra("email");
            passwordUser=intent.getStringExtra("password");
            numberUser=intent.getStringExtra("number");

            Fixed_Username.setText(usernameUser);
            Edit_Name.setText(nameUser);
            Edit_Email.setText(emailUser);
            Edit_Password.setText(passwordUser);
            Edit_Number.setText(numberUser);
        }
        else {
            Intent intent=getIntent();

            Password_Heading.setVisibility(View.GONE);
            nameUser=intent.getStringExtra("name");
            emailUser=intent.getStringExtra("email");
            numberUser=intent.getStringExtra("number");

            if (userData.getEmail()==null){Edit_Email.setText(firebaseUser.getEmail());}
            else Edit_Email.setText(emailUser);

            Edit_Name.setText(nameUser);
            Edit_Number.setText(numberUser);

            Fixed_Username.setText(firebaseUser.getEmail());

        }

    }

    private boolean validateName(String name) {
        if (!name.matches("^[a-zA-Z]+$") ) {
            Edit_Name.setError("Name can only contain text ");
            return false;
        } else {
            Edit_Name.setError(null);
            return true;
        }
    }

    private boolean validateEmail(String email){
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Edit_Email.setError("example@email.com");
            return false;
        } else {
            Edit_Email.setError(null);
            return true;
        }
    }

    private boolean validateNumber(String number){
        if (!number.matches("^971\\d{9}$")) {
            Edit_Number.setError("Number must start with 971 and have 12digits");
            return false;
        } else {
            Edit_Number.setError(null);
            return true;
        }
    }

}