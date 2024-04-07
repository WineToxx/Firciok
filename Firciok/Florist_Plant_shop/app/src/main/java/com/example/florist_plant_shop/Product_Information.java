package com.example.florist_plant_shop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class Product_Information extends AppCompatActivity {
    ImageView detailedImg,detailed_rate_img;
    TextView detailed_rating,detailed_name,detailed_price,detailed_maintenance,detailed_description,quantity;
    Button add_to_cart_button;

    ImageView back_home_button;
    ImageView add_item,remove_item;

    ViewAllModel viewAllModel = null;
    RecommendedModel recommendedModel=null;
    FirebaseFirestore firestore;
    FirebaseAuth auth;
    int FinalQuantity=1;
    int TotalPrice=0;
    int Product_Price;
    String Product_name,Product_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_information);
        detailed_rate_img=findViewById(R.id.detailed_rate_img);
        detailedImg=findViewById(R.id.detailed_img);
        detailed_rating=findViewById(R.id.detailed_rating);
        detailed_name=findViewById(R.id.detailed_name);
        detailed_price=findViewById(R.id.detailed_price);
        detailed_maintenance= findViewById(R.id.detailed_maintenance);
        detailed_description=findViewById(R.id.detailed_description);
        add_item=findViewById(R.id.add_item);
        remove_item=findViewById(R.id.remove_item);
        add_to_cart_button=findViewById(R.id.add_to_cart_button);
        back_home_button=findViewById(R.id.back_home_button);
        quantity=findViewById(R.id.quantity);

        firestore=FirebaseFirestore.getInstance();
        auth=FirebaseAuth.getInstance();
        //--------------------x-------------------------x-----------------------x----------------------x--------------------x------------------x----------------------//
        //BACK BUTTON
        back_home_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //--------------------x-------------------------x-----------------------x----------------------x--------------------x------------------x----------------------//
        //DISPLAYING PRODUCT INFORMATION - VIEW ALL PRODUCTS & RECOMMENDED
        final Object object=getIntent().getSerializableExtra("detail");
        if (object instanceof ViewAllModel){
            viewAllModel=(ViewAllModel) object;
        }

        if (object instanceof RecommendedModel){
            recommendedModel=(RecommendedModel) object;
        }

        if (viewAllModel!=null){
            Glide.with(getApplicationContext()).load(viewAllModel.getImg_url()).into(detailedImg);
            Glide.with(getApplicationContext()).load(viewAllModel.getRate_img()).into(detailed_rate_img);
            detailed_rating.setText(viewAllModel.getRating());
            detailed_name.setText(viewAllModel.getName());
            detailed_price.setText(String.valueOf(viewAllModel.getPrice()));
            detailed_maintenance.setText(viewAllModel.getMaintenance());
            detailed_description.setText(viewAllModel.getDescription());
            Product_img=viewAllModel.getImg_url();
            Product_name=viewAllModel.getName();
            Product_Price=viewAllModel.getPrice();
        }

        if (recommendedModel!=null){
            Glide.with(getApplicationContext()).load(recommendedModel.getImg_url()).into(detailedImg);
            Glide.with(getApplicationContext()).load(recommendedModel.getRate_img()).into(detailed_rate_img);
            detailed_rating.setText(recommendedModel.getRating());
            detailed_name.setText(recommendedModel.getName());
            detailed_price.setText(String.valueOf(recommendedModel.getPrice()));
            detailed_maintenance.setText(recommendedModel.getMaintenance());
            detailed_description.setText(recommendedModel.getDescription());
            Product_img=recommendedModel.getImg_url();
            Product_name=recommendedModel.getName();
            Product_Price=recommendedModel.getPrice();
        }

        //--------------------x-------------------------x-----------------------x----------------------x--------------------x------------------x----------------------//
        //ENABLING USER'S TO BE ABLE TO CHOOSE THEIR DESIRED QUANTITY OF THE PRODUCTS
        add_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FinalQuantity<10){
                    FinalQuantity++;
                    quantity.setText(String.valueOf(FinalQuantity));
                    TotalPrice=Product_Price*FinalQuantity;
                }
            }
        });

        remove_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FinalQuantity>0){
                    FinalQuantity--;
                    if (FinalQuantity==0) finish();
                    quantity.setText(String.valueOf(FinalQuantity));
                    TotalPrice=Product_Price*FinalQuantity;
                }
            }
        });

        //--------------------x-------------------------x-----------------------x----------------------x--------------------x------------------x----------------------//
        //ADD TO CART BUTTON
        add_to_cart_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (UserDataSingleton.getInstance().getEmail()!=null) {
                    addedtocart();
                }
                else {
                    startActivity(new Intent(Product_Information.this,Login.class));
                }
            }
        });

    }

    private void addedtocart() {
        String saveCurrentDate,saveCurrentTime;
        Calendar calforDate=Calendar.getInstance();

        SimpleDateFormat currentDate=new SimpleDateFormat("MM dd,yyyy");
        saveCurrentDate=currentDate.format(calforDate.getTime());

        SimpleDateFormat currentTime=new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calforDate.getTime());

        final HashMap<String,Object> cartMap = new HashMap<>();


        cartMap.put("ProductImg",Product_img);
        cartMap.put("ProductName",Product_name);
        cartMap.put("ProductPrice",String.valueOf(Product_Price));
        cartMap.put("currentDate",saveCurrentDate);
        cartMap.put("currentTime",saveCurrentTime);
        cartMap.put("totalQuantity",quantity.getText().toString());
        cartMap.put("totalPrice",TotalPrice);

        String usernameUser= UserDataSingleton.getInstance().getEmail();
        firestore.collection("CurrentUser").document(usernameUser).collection("AddToCart").add(cartMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                Toast.makeText(Product_Information.this,"Added To Cart",Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

}