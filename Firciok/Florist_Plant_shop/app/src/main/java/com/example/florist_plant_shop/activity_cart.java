package com.example.florist_plant_shop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class activity_cart extends AppCompatActivity {
    FirebaseFirestore db;
    FirebaseAuth auth;
    ImageView Empty_Cart_pic;
    FrameLayout buy_now;
    TextView Empty_Cart_text,Overall_Amount;
    BottomNavigationView bottom_navigation;
    RecyclerView recyclerView;
    MyCartAdapter myCartAdapter;
    List<MyCartModel> cartModelList;
    int OverAll_Bill=0;
    int OverAll_Quantity=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        db=FirebaseFirestore.getInstance();
        auth=FirebaseAuth.getInstance();
        bottom_navigation=findViewById(R.id.bottom_navigation_menu);
        recyclerView=findViewById(R.id.recycler_view);
        Empty_Cart_pic=findViewById(R.id.Empty_Cart_Pic);
        Empty_Cart_text=findViewById(R.id.Empty_Cart_Text);
        Overall_Amount=findViewById(R.id.OverAll_Amount);
        buy_now=findViewById(R.id.buy_now);


        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        cartModelList=new ArrayList<>();
        myCartAdapter=new MyCartAdapter(this,cartModelList);
        recyclerView.setAdapter(myCartAdapter);

        String usernameUser= UserDataSingleton.getInstance().getEmail();
        if (usernameUser!=null || auth.getCurrentUser()!=null){
            db.collection("CurrentUser").document(usernameUser).collection("AddToCart").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()){
                        for (DocumentSnapshot documentSnapshot:task.getResult().getDocuments()){
                            String documentId=documentSnapshot.getId();
                            MyCartModel cartModel=documentSnapshot.toObject(MyCartModel.class);
                            cartModel.setDocumentId(documentId);
                            cartModelList.add(cartModel);
                            myCartAdapter.notifyDataSetChanged();
                        }

                        calTotalAmountQuantity(cartModelList);
                    }
                }
            });
        }
        else {
            Empty_Cart_pic.setVisibility(View.VISIBLE);
            Empty_Cart_text.setVisibility(View.VISIBLE);
        }


        String emailUser= UserDataSingleton.getInstance().getEmail();
        buy_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(OverAll_Bill>0) {
                    if (emailUser != null) {
                        Intent intent = new Intent(activity_cart.this, CheckOut_Activity.class);
                        intent.putExtra("total_bill", String.valueOf(OverAll_Bill));
                        intent.putExtra("total_quantity", String.valueOf(OverAll_Quantity));
                        intent.putExtra("itemList", (Serializable) cartModelList);
                        startActivity(intent);
                    } else {
                        startActivity(new Intent(activity_cart.this, Login.class));
                    }
                }
                else {
                    Toast.makeText(activity_cart.this,"No items found",Toast.LENGTH_SHORT).show();
                }
            }
        });

        //--------------------x-------------------------x-----------------------x----------------------x--------------------x------------------x----------------------//
        //Set Selected item
        bottom_navigation.setSelectedItemId(R.id.cart);
        //Perform item selected listener
        bottom_navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId==R.id.cart) return true;
                if (itemId==R.id.Catalogue){
                    startActivity(new Intent(getApplicationContext(), activity_catalogue.class));
                    overridePendingTransition(0, 0);
                    return true;
                }
                if (itemId==R.id.home){
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    overridePendingTransition(0,0);
                    return true;
                }
                if (itemId==R.id.account){
                    startActivity(new Intent(getApplicationContext(), activity_account.class));
                    overridePendingTransition(0,0);
                    return true;
                }
                return false;
            }
        });
    }

    private void calTotalAmountQuantity(List<MyCartModel> cartModelList) {
        for(MyCartModel myCartModel:cartModelList){
            OverAll_Bill+=myCartModel.getTotalPrice();
            OverAll_Quantity+=Integer.parseInt(myCartModel.getTotalQuantity());
        }
        Overall_Amount.setText(String.valueOf(OverAll_Bill));
    }

}