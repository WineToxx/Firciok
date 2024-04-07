package com.example.florist_plant_shop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class CheckOut_Activity extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseFirestore firestore;
    ImageView back_home_button;
    TextView Display_Cart_Total,Display_Cart_Quantity;
    MaterialButton Back_To_Home_Button;
    LinearLayout after_checkout,Checkout_Bill,Checkout_Card_Detail;
    MaterialButton Checkout_Save_Card_Button,Checkout_Back_to_home;
    FrameLayout Checkout_Buy_Button;
    TextView Checkout_Delivery_Emirate, Checkout_Delivery_Date,Checkout_Card_Number,User_name;

    TextInputEditText Checkout_Card_Expiry,Checkout_Delivery_Address,Checkout_Card_CVV;
    RadioGroup RG_payment_Group;
    //Checkout Confirmation
    TextView Checkout_Con_Delivery_Date,Checkout_Con_Quantity,Checkout_Con_COD,Checkout_Con_Del_Charge,Checkout_Con_Service,Checkout_Con_Cart_Amount,Checkout_Con_Final_Amount;

    String Entered_Expiry,Entered_cvv;
    TextView Checkout_cvv_Number;
    RadioButton Checked_button;
    int COD=0;
    int Checkout_Amount=20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);
        auth=FirebaseAuth.getInstance();
        firestore=FirebaseFirestore.getInstance();
        Display_Cart_Total=findViewById(R.id.Display_Cart_Total);
        Display_Cart_Quantity=findViewById(R.id.Display_Cart_Quantity);
        Back_To_Home_Button=findViewById(R.id.Back_To_Home_Button);
        after_checkout=findViewById(R.id.after_checkout);
        Checkout_Bill=findViewById(R.id.Checkout_Bill);
        Checkout_Card_Detail=findViewById(R.id.Checkout_Card_Detail);
        back_home_button=findViewById(R.id.back_home_button);
        Checkout_Buy_Button=findViewById(R.id.Checkout_Buy);
        Checkout_Delivery_Emirate=findViewById(R.id.Checkout_Delivery_Emirate);
        Checkout_Delivery_Date=findViewById(R.id.Checkout_Delivery_Date);
        Checkout_Card_Number=findViewById(R.id.Checkout_Card_Number);
        Checkout_Card_Expiry=findViewById(R.id.Checkout_Card_Expiry);
        Checkout_Delivery_Address=findViewById(R.id.Checkout_Delivery_Address);
        Checkout_Card_Expiry=findViewById(R.id.Checkout_Card_Expiry);
        Checkout_Card_CVV=findViewById(R.id.Checkout_Card_CVV);
        RG_payment_Group=findViewById(R.id.RG_payment_Group);
        Checkout_Con_Delivery_Date=findViewById(R.id.Checkout_Con_Delivery_Date);
        Checkout_Con_Quantity=findViewById(R.id.Checkout_Con_Quantity);
        Checkout_Con_COD=findViewById(R.id.Checkout_Con_COD);
        Checkout_Con_Del_Charge=findViewById(R.id.Checkout_Con_Del_Charge);
        Checkout_Con_Service=findViewById(R.id.Checkout_Con_Service);
        Checkout_Con_Cart_Amount=findViewById(R.id.Checkout_Con_Cart_Amount);
        Checkout_Con_Final_Amount=findViewById(R.id.Checkout_Con_Final_Amount);
        Checkout_Save_Card_Button=findViewById(R.id.Checkout_Save_Card);
        Checkout_Back_to_home=findViewById(R.id.Back_To_Home_Button);
        User_name=findViewById(R.id.User_Name);

        Checkout_Card_Detail.setVisibility(View.GONE);
        loadCartData();

        back_home_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        loadDeliveryDate();

        RG_payment_Group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = findViewById(checkedId);
                Checked_button=rb;
                if (checkedId==R.id.rb_COD){
                    COD+=3;
                    Checkout_Con_COD.setText(String.valueOf(COD));
                    Checkout_Amount+=COD;
                    Checkout_Con_Final_Amount.setText(String.valueOf(Checkout_Amount));
                }
                if (checkedId == R.id.rb_Card) {
                    Checkout_Con_COD.setText(String.valueOf(COD));
                    if (COD==3){
                        COD=0;
                        Checkout_Con_COD.setText(String.valueOf(COD));
                        Checkout_Amount-=3;
                        Checkout_Con_Final_Amount.setText(String.valueOf(Checkout_Amount));
                    }
                    Checkout_Card_Detail.setVisibility(View.VISIBLE);
                    Checkout_Card_Number.setText(UserDataSingleton.getInstance().getCardNumber());
                }
            }
        });

        Checkout_Save_Card_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CheckOut_Activity.this,"Please Proceed to Checkout",Toast.LENGTH_SHORT).show();
            }
        });

        Checkout_Buy_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Saved_Expiry = UserDataSingleton.getInstance().getExpiryDate();
                String Saved_CVV = UserDataSingleton.getInstance().getCvv();

                if (Checked_button==null){
                    Toast.makeText(CheckOut_Activity.this,"Kindly select a payment method",Toast.LENGTH_SHORT).show();
                }
                if (CheckAddress()&&CheckCardCvv()&&CheckCardExpiry()) {
                    if (Saved_Expiry.equals(Entered_Expiry) && Saved_CVV.equals(Entered_cvv)) {
                        List<MyCartModel> list = (ArrayList<MyCartModel>) getIntent().getSerializableExtra("itemList");
                        if (list != null && list.size() > 0) {
                            for (MyCartModel model : list) {
                                final HashMap<String, Object> cartMap = new HashMap<>();


                                cartMap.put("ProductImg", model.getProductImg());
                                cartMap.put("ProductName", model.getProductName());
                                cartMap.put("ProductPrice", model.getProductPrice());
                                cartMap.put("currentDate", model.getCurrentDate());
                                cartMap.put("totalQuantity", model.getTotalQuantity());
                                cartMap.put("totalPrice", Checkout_Amount);

                                String usernameUser = UserDataSingleton.getInstance().getEmail();
                                firestore.collection("CurrentUser").document(usernameUser).collection("OrderPlaced").add(cartMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentReference> task) {
                                        Toast.makeText(CheckOut_Activity.this, "Order_Placed", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                });
                            }
                        }
                        User_name.setText(UserDataSingleton.getInstance().getName());
                        Checkout_Bill.setVisibility(View.GONE);
                        after_checkout.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        Back_To_Home_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CheckOut_Activity.this, MainActivity.class));
            }
        });
    }


    public boolean CheckCardExpiry(){
        String expiry=Checkout_Card_Expiry.getText().toString();
        if (expiry.length() != 5 || !expiry.matches("\\d{2}/\\d{2}")) {
            Checkout_Card_Expiry.setError("Enter the Expiry Date in the format MM/YY");
            return false;
        } else {
            Checkout_Card_Expiry.setError(null);
            Entered_Expiry=Checkout_Card_Expiry.getText().toString();
            return true;
        }
    }

    public boolean CheckCardCvv(){
        String cvv=Checkout_Card_CVV.getText().toString().trim();
        if (cvv.length() != 3 || !cvv.matches("\\d{3}")) {
            Checkout_Card_CVV.setError("Enter in format: nnn");
            return false;
        } else {
            Checkout_Card_CVV.setError(null);
            Entered_cvv=Checkout_Card_CVV.getText().toString();
            return true;
        }
    }

    public boolean CheckAddress(){
        String address=Checkout_Delivery_Address.getText().toString();
        if (address.length()<15 ) {
            Checkout_Delivery_Address.setError("Enter A Valid Delivery Address");
            return false;
        } else {
            Checkout_Delivery_Address.setError(null);
            return true;
        }
    }
    private void loadDeliveryDate() {
        SimpleDateFormat dateFormat= new SimpleDateFormat("EEEE dd.MM.yyyy");
        Calendar currentCal = Calendar.getInstance();
        String currentdate=dateFormat.format(currentCal.getTime());
        currentCal.add(Calendar.DATE, 7);
        String dateAfter7Days = dateFormat.format(currentCal.getTime());
        Checkout_Delivery_Date.setText(dateAfter7Days);
        Checkout_Con_Delivery_Date.setText(dateAfter7Days);
    }
    private void loadCartData() {
        Intent intent = getIntent();
        String Cart_amount = intent.getStringExtra("total_bill");
        String total_quantity = intent.getStringExtra("total_quantity");

        Display_Cart_Quantity.setText(total_quantity);
        Display_Cart_Total.setText(Cart_amount);
        Checkout_Con_Cart_Amount.setText(Cart_amount);
        Checkout_Con_Quantity.setText(total_quantity);
        Checkout_Con_COD.setText(String.valueOf(COD));
        Checkout_Delivery_Address.setText(UserDataSingleton.getInstance().getAddress());
        Checkout_Delivery_Emirate.setText(UserDataSingleton.getInstance().getEmirate());

        Checkout_Amount+=Integer.parseInt(Cart_amount);
        Checkout_Con_Final_Amount.setText(String.valueOf(Checkout_Amount));
    }

}