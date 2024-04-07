package com.example.florist_plant_shop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class activity_account extends AppCompatActivity {
    TextView tvLogin,Display_UserName;

    TextView Display_Name,Display_Email,Display_Password,Display_Number,Display_SavedUsername;
    TextView Card_Name,Card_Number,Card_Expiry_Date,Card_cvv;
    TextInputEditText Entered_Delivery_Address;

    FrameLayout Expansion_Profile,Expansion_Card_detail,Expansion_Delivery_Address,Expansion_Terms_Conditions;
    CardView My_profile_details,Card_details;
    TextInputLayout Delivery_Address,Terms_Conditions;
    ImageView ProfileImg;
    MaterialButton button_SignInUp,Update_Profile_Button,Card_Save_Button,Save_Address_Button;
    BottomNavigationView bottom_navigation;
    FirebaseAuth firebaseAuth;
    LinearLayout Password_Layout;

    GoogleSignInClient googleSignInClient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        firebaseAuth = FirebaseAuth.getInstance();
        // Initialize firebase user
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        bottom_navigation=findViewById(R.id.bottom_navigation_menu);
        Display_UserName = findViewById(R.id.Display_UserName);
        ProfileImg=findViewById(R.id.ProfileImg);
        button_SignInUp = findViewById(R.id.button_Login);
        Expansion_Profile=findViewById(R.id.Expansion_Profile);
        Expansion_Card_detail=findViewById(R.id.Expansion_Card_detail);
        Expansion_Delivery_Address=findViewById(R.id.Expansion_Delivery_Address);
        Expansion_Terms_Conditions=findViewById(R.id.Expansion_Terms_Conditions);
        My_profile_details=findViewById(R.id.My_profile_details);
        Card_details=findViewById(R.id.Card_details);
        Delivery_Address=findViewById(R.id.Delivery_Address);
        Terms_Conditions=findViewById(R.id.Terms_Conditions);
        Update_Profile_Button=findViewById(R.id.Update_Profile_Button);
        Display_Name=findViewById(R.id.Display_name);
        Display_Email=findViewById(R.id.Display_email);
        Display_Password=findViewById(R.id.Display_password);
        Display_Number=findViewById(R.id.Display_number);
        Display_SavedUsername=findViewById(R.id.Display_SavedUsername);
        Card_Name=findViewById(R.id.Card_Name);
        Card_Number=findViewById(R.id.Card_Number);
        Card_Expiry_Date=findViewById(R.id.Card_Expiry_Date);
        Card_cvv=findViewById(R.id.Card_cvv);
        Card_Save_Button=findViewById(R.id.Card_Save_Button);
        Entered_Delivery_Address=findViewById(R.id.Entered_Delivery_Address);
        Save_Address_Button=findViewById(R.id.Save_Address);
        Password_Layout=findViewById(R.id.Password_Layout);

        loadUserData();

        //--------------------x-------------------------x-----------------------x----------------------x--------------------x------------------x----------------------//
        //DELIVERY ADDRESS
        Save_Address_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailUser= UserDataSingleton.getInstance().getEmail();
                if (emailUser!=null) {
                    if (button_SignInUp.getText().equals("Logout")) {
                        if (CheckAddress()) {
                            Entered_Delivery_Address.setText(Entered_Delivery_Address.getText().toString());
                            UserDataSingleton userData = UserDataSingleton.getInstance();
                            userData.setAddress(Entered_Delivery_Address.getText().toString());
                            Toast.makeText(activity_account.this,"Saved",Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Intent intent = new Intent(activity_account.this, Login.class);
                        startActivity(intent);
                    }
                }
                else{
                    startActivity(new Intent(activity_account.this,Login.class));
                }
            }
        });

        //--------------------x-------------------------x-----------------------x----------------------x--------------------x------------------x----------------------//
        //SAVE CARD DETAILS
        Card_Save_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailUser= UserDataSingleton.getInstance().getEmail();
                if (emailUser!=null) {
                    if (button_SignInUp.getText().equals("Logout")) {
                        if (CheckCardNumber() && CheckCardName() && CheckCardCvv() && CheckCardExpiry()) {
                            Card_Name.setText(Card_Name.getText().toString());
                            Card_Expiry_Date.setText(Card_Expiry_Date.getText().toString().trim());
                            Card_cvv.setText(Card_cvv.getText().toString());
                            Card_Number.setText(Card_Number.getText().toString());

                            UserDataSingleton userData = UserDataSingleton.getInstance();
                            userData.setCardName(Card_Name.getText().toString());
                            userData.setCardNumber(Card_Number.getText().toString());
                            userData.setCvv(Card_cvv.getText().toString());
                            userData.setExpiryDate(Card_Expiry_Date.getText().toString());

                            Toast.makeText(activity_account.this,"Saved",Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Intent intent = new Intent(activity_account.this, Login.class);
                        startActivity(intent);
                    }
                }
                else {
                    startActivity(new Intent(activity_account.this,Login.class));
                }
            }
        });

        //--------------------x-------------------------x-----------------------x----------------------x--------------------x------------------x----------------------//
        //EDIT PROFILE
        Update_Profile_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (button_SignInUp.getText().equals("Logout")){
                    passUserData();
                }
                else {
                    Intent intent=new Intent(activity_account.this,Login.class);
                    startActivity(intent);
                }
            }
        });
        //--------------------x-------------------------x-----------------------x----------------------x--------------------x------------------x----------------------//

        //Set Selected item
        bottom_navigation.setSelectedItemId(R.id.account);
        //Perform item selected listener
        bottom_navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (firebaseUser!=null || UserDataSingleton.getInstance().getEmail()!=null){
                    if (CheckCardExpiry()&&CheckAddress()&&CheckCardName()&&CheckCardCvv()&&CheckCardCvv()){
                        int itemId = item.getItemId();
                        if (itemId==R.id.account) return true;
                        if (itemId==R.id.Catalogue){
                            startActivity(new Intent(getApplicationContext(), activity_catalogue.class));
                            overridePendingTransition(0, 0);
                            return true;
                        }
                        if (itemId==R.id.cart){
                            startActivity(new Intent(getApplicationContext(),activity_cart.class));
                            overridePendingTransition(0,0);
                            return true;
                        }
                        if (itemId==R.id.home){
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            overridePendingTransition(0,0);
                            return true;
                        }
                    }
                    else {
                        Toast.makeText(activity_account.this,"Kindly complete your profile",Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    if (CheckCardExpiry()&&CheckAddress()&&CheckCardName()&&CheckCardCvv()&&CheckCardCvv()){}
                    int itemId = item.getItemId();
                    if (itemId==R.id.account) return true;
                    if (itemId==R.id.Catalogue){
                        startActivity(new Intent(getApplicationContext(), activity_catalogue.class));
                        overridePendingTransition(0, 0);
                        return true;
                    }
                    if (itemId==R.id.cart){
                        startActivity(new Intent(getApplicationContext(),activity_cart.class));
                        overridePendingTransition(0,0);
                        return true;
                    }
                    if (itemId==R.id.home){
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    }
                }
                return false;
            }
        });

        //--------------------x-------------------------x-----------------------x----------------------x--------------------x------------------x----------------------//

        // LOGIN / LOGOUT


        String emailUser= UserDataSingleton.getInstance().getEmail();
        if (emailUser!=null) button_SignInUp.setText("Logout");

        // Check condition
        if (firebaseUser != null ) {
            button_SignInUp.setText("Logout");
            ProfileImg.setVisibility(View.VISIBLE);
            // When firebase user is not equal to null set image on image view
            Glide.with(activity_account.this).load(firebaseUser.getPhotoUrl()).into(ProfileImg);
            // set name on text view
            Display_Name.setText(UserDataSingleton.getInstance().getName());
        }

        googleSignInClient = GoogleSignIn.getClient(activity_account.this, GoogleSignInOptions.DEFAULT_SIGN_IN);

        button_SignInUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (button_SignInUp.getText()=="Logout"){
                    if (emailUser!=null){
                        clearUserData();
                        loadUserData();
                    }
                    // Sign out from google
                    googleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            // Check condition
                            if (task.isSuccessful()) {
                                // When task is successful sign out from firebase
                                firebaseAuth.signOut();
                                clearUserData();
                                loadUserData();
                                // Display Toast
                                Toast.makeText(getApplicationContext(), "Logout successful", Toast.LENGTH_SHORT).show();
                                button_SignInUp.setText("Sign In or Sign Up");
                                Display_Name.setVisibility(View.GONE);
                                ProfileImg.setImageResource(R.drawable.profile);
                            }
                        }
                    });
                }
                else {
                    Intent i= new Intent(getApplicationContext(), Login.class);
                    startActivity(i);}
            }
        });

        //--------------------x-------------------------x-----------------------x----------------------x--------------------x------------------x----------------------//
        //DETAIL EXPANSION

        Expansion_Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (My_profile_details.getVisibility()==View.GONE){
                    My_profile_details.setVisibility(View.VISIBLE);
                }else My_profile_details.setVisibility(View.GONE);
            }
        });

        Expansion_Card_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Card_details.getVisibility()==View.GONE){
                    Card_details.setVisibility(View.VISIBLE);
                }else Card_details.setVisibility(View.GONE);
            }
        });

        Expansion_Delivery_Address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Delivery_Address.getVisibility()==View.GONE){
                    Delivery_Address.setVisibility(View.VISIBLE);
                }else Delivery_Address.setVisibility(View.GONE);
            }
        });

        Expansion_Terms_Conditions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Terms_Conditions.getVisibility()==View.GONE){
                    Terms_Conditions.setVisibility(View.VISIBLE);
                }else Terms_Conditions.setVisibility(View.GONE);
            }
        });


    }

    private void loadUserData() {
        firebaseAuth = FirebaseAuth.getInstance();
        // Initialize firebase user
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        UserDataSingleton userData = UserDataSingleton.getInstance();
        // Set retrieved data to UI elements

        Card_Name.setText(userData.getCardName());
        Card_Number.setText(userData.getCardNumber());
        Card_cvv.setText(userData.getCvv());
        Card_Expiry_Date.setText(userData.getExpiryDate());
        Entered_Delivery_Address.setText(userData.getAddress());
        Display_Name.setText(userData.getName());
        Display_UserName.setVisibility(View.VISIBLE);
        Display_UserName.setText(userData.getName());
        Display_SavedUsername.setText(userData.getUsername());
        Display_Email.setText(userData.getEmail());
        if (firebaseUser==null){Display_Password.setText(userData.getPassword());}
        else {
            Password_Layout.setVisibility(View.GONE);
        }
        Display_Number.setText(userData.getNumber());
    }

    private void saveUserData(String name, String username, String email, String password, String number) {
        // Save user data to UserDataSingleton
        UserDataSingleton userData = UserDataSingleton.getInstance();
        userData.setName(name);
        userData.setUsername(username);
        userData.setEmail(email);
        userData.setPassword(password);
        userData.setNumber(number);
    }

    private void clearUserData() {
        firebaseAuth = FirebaseAuth.getInstance();
        // Initialize firebase user
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        // Clear user data in UserDataSingleton
        UserDataSingleton.getInstance().setName(null);
        UserDataSingleton.getInstance().setUsername(null);
        UserDataSingleton.getInstance().setEmail(null);
        if (firebaseUser==null)UserDataSingleton.getInstance().setPassword(null);
        UserDataSingleton.getInstance().setNumber(null);
        UserDataSingleton.getInstance().setCardName(null);
        UserDataSingleton.getInstance().setCardNumber(null);
        UserDataSingleton.getInstance().setCvv(null);
        UserDataSingleton.getInstance().setExpiryDate(null);
        UserDataSingleton.getInstance().setAddress(null);
    }

    public void passUserData(){
        firebaseAuth = FirebaseAuth.getInstance();
        // Initialize firebase user
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        if (firebaseUser==null) {
            String userUsername = Display_SavedUsername.getText().toString().trim();
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
            Query checkUserDatabase = reference.orderByChild("username").equalTo(userUsername);

            checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String nameFromDB = snapshot.child(userUsername).child("name").getValue(String.class);
                    String emailFromDB = snapshot.child(userUsername).child("email").getValue(String.class);
                    String passwordFromDB = snapshot.child(userUsername).child("password").getValue(String.class);
                    String numberFromDB = snapshot.child(userUsername).child("number").getValue(String.class);
                    String usernameFromDB = snapshot.child(userUsername).child("username").getValue(String.class);

                    Intent intent = new Intent(activity_account.this, edit_profile.class);

                    intent.putExtra("name", nameFromDB);
                    intent.putExtra("email", emailFromDB);
                    intent.putExtra("password", passwordFromDB);
                    intent.putExtra("number", numberFromDB);
                    intent.putExtra("username", usernameFromDB);
                    startActivity(intent);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        else {
            UserDataSingleton userData = UserDataSingleton.getInstance();
            Intent intent = new Intent(activity_account.this, edit_profile.class);
            intent.putExtra("name",userData.getName());
            intent.putExtra("email",userData.getEmail());
            intent.putExtra("number",userData.getNumber());
            intent.putExtra("username",firebaseUser.getEmail());
            startActivity(intent);
        }

    }

    public boolean CheckCardNumber(){
        String name=Card_Number.getText().toString().trim().replaceAll("\\s", "");
        if (!name.matches("^\\d{12}$")) {
            Card_Number.setError("Card Number Must be of 12 digits");
            return false;
        } else {
            Card_Number.setError(null);
            return true;
        }
    }

    public boolean CheckCardCvv(){
        String cvv=Card_cvv.getText().toString().trim();
        if (cvv.length() != 3 || !cvv.matches("\\d{3}")) {
            Card_cvv.setError("Enter Card cvv");
            return false;
        } else {
            Card_cvv.setError(null);
            return true;
        }
    }

    public boolean CheckCardExpiry(){
        String expiry=Card_Expiry_Date.getText().toString().trim();
        if (expiry.length() != 5 || !expiry.matches("\\d{2}/\\d{2}")) {
            Card_Expiry_Date.setError("Enter the Expiry Date in the format MM/YY");
            return false;
        } else {
            Card_Expiry_Date.setError(null);
            return true;
        }
    }

    public boolean CheckCardName(){
        String name=Card_Name.getText().toString().trim();
        if (!name.matches("^[a-zA-Z ]+$")) {
            Card_Name.setError("Name can only contain text");
            return false;
        } else {
            Card_Name.setError(null);
            return true;
        }
    }

    public boolean CheckAddress(){
        String address=Entered_Delivery_Address.getText().toString();
        if (address.length()<15 ) {
            Entered_Delivery_Address.setError("Enter A Valid Delivery Address");
            return false;
        } else {
            Entered_Delivery_Address.setError(null);
            return true;
        }
    }
}