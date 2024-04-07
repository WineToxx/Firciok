package com.example.florist_plant_shop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class activity_catalogue extends AppCompatActivity {
    FirebaseFirestore db;
    BottomNavigationView bottom_navigation;
    TextView all_items;
    RecyclerView recyclerView,Catalogue_color_rec;

    List<NavCategoryModel> categoryColorModelList;
    NavCategoryAdapter navColorCategoryAdapter;
    List<NavCategoryModel> categoryModelList;
    NavCategoryAdapter navCategoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalogue);
        recyclerView=findViewById(R.id.catalogue_type_rec);
        bottom_navigation=findViewById(R.id.bottom_navigation_menu);
        Catalogue_color_rec=findViewById(R.id.catalogue_color_rec);
        all_items=findViewById(R.id.All_items);
        db = FirebaseFirestore.getInstance();


        //--------------------x-------------------------x-----------------------x----------------------x--------------------x------------------x----------------------//
        //CATALOGUE ALL

        all_items.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(activity_catalogue.this,ViewAll_Activity.class);
                intent.putExtra("type","All");
                startActivity(intent);
            }
        });
        //--------------------x-------------------------x-----------------------x----------------------x--------------------x------------------x----------------------//
        // CATALGOUE COLORS

        Catalogue_color_rec.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));
        categoryColorModelList = new ArrayList<>();
        navColorCategoryAdapter = new NavCategoryAdapter(this,categoryColorModelList);
        Catalogue_color_rec.setAdapter(navColorCategoryAdapter);

        db.collection("NavCategory").document("UYO4kky6jotSCicwJoUq").collection("Color")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                NavCategoryModel navCategoryModel= document.toObject(NavCategoryModel.class);
                                categoryColorModelList.add(navCategoryModel);
                                navColorCategoryAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Log.e("Firestore Error","Error getting documents: ",task.getException());
                            Toast.makeText(activity_catalogue.this,"Error"+task.getException(),Toast.LENGTH_LONG).show();
                        }
                    }
                });
        //--------------------x-------------------------x-----------------------x----------------------x--------------------x------------------x----------------------//
        //CATALOGUE TYPES
        recyclerView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));
        categoryModelList = new ArrayList<>();
        navCategoryAdapter = new NavCategoryAdapter(this,categoryModelList);
        recyclerView.setAdapter(navCategoryAdapter);

        db.collection("NavCategory").document("UYO4kky6jotSCicwJoUq").collection("Types")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                NavCategoryModel navCategoryModel= document.toObject(NavCategoryModel.class);
                                categoryModelList.add(navCategoryModel);
                                navCategoryAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Log.e("Firestore Error","Error getting documents: ",task.getException());
                            Toast.makeText(activity_catalogue.this,"Error"+task.getException(),Toast.LENGTH_LONG).show();
                        }
                    }
                });

        //--------------------x-------------------------x-----------------------x----------------------x--------------------x------------------x----------------------//
        //BOTTOM NAVIGATION
        //Set Selected item
        bottom_navigation.setSelectedItemId(R.id.Catalogue);
        //Perform item selected listener
        bottom_navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId==R.id.Catalogue) return true;
                if (itemId==R.id.home){
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    overridePendingTransition(0, 0);
                    return true;
                }
                if (itemId==R.id.cart){
                    startActivity(new Intent(getApplicationContext(),activity_cart.class));
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
}