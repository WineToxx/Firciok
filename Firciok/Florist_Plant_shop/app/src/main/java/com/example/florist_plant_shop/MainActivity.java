package com.example.florist_plant_shop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottom_navigation;
    Spinner Emirates_Spinner;
    RecyclerView popularRec,homeCatRec,recommendedRec;
    FirebaseFirestore db;
    //popular items
    PopularAdapter popularAdapter;
    List<PopularModel> popularModelList;

    LinearLayout Scroll_view;
    ProgressBar progressBar;
    //explore items
    List<HomeCategory> categoryList;
    HomeAdapter homeAdapter;

    //Recommended items
    List<RecommendedModel> recommendedModelList;
    RecommendedAdapter recommendedAdapter;

    ///////// SEARCH BOX
    TextInputEditText Search_Box;
    private RecyclerView Search_rec;
    private List<ViewAllModel> viewAllModelList;
    private ViewAllAdapter viewAllAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = FirebaseFirestore.getInstance();
        popularRec=findViewById(R.id.pop_rec);
        homeCatRec=findViewById(R.id.explore_rec);
        recommendedRec=findViewById(R.id.recommended_rec);
        Scroll_view=findViewById(R.id.Scroll_view);
        progressBar=findViewById(R.id.Progressbar);
        Search_Box=findViewById(R.id.Search_Box);
        Search_rec=findViewById(R.id.search_rec);

        progressBar.setVisibility(View.VISIBLE);
        Scroll_view.setVisibility(View.GONE);

        viewAllModelList=new ArrayList<>();
        viewAllAdapter=new ViewAllAdapter(this,viewAllModelList);
        Search_rec.setAdapter(viewAllAdapter);
        Search_rec.setHasFixedSize(true);
        Search_Box.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()){
                    viewAllModelList.clear();
                    viewAllAdapter.notifyDataSetChanged();
                    Search_rec.setVisibility(View.GONE);
                }
                else {
                    Search_rec.setVisibility(View.VISIBLE);
                    searchProduct(s.toString());
                }
            }
        });

        //--------------------x-------------------------x-----------------------x----------------------x--------------------x------------------x----------------------//
        //POPULAR POSTER LIST
        popularRec.setLayoutManager(new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false));
        popularModelList = new ArrayList<>();
        popularAdapter = new PopularAdapter(this,popularModelList);
        popularRec.setAdapter(popularAdapter);

        db.collection("PopularProducts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                PopularModel popularModel = document.toObject(PopularModel.class);
                                popularModelList.add(popularModel);
                                popularAdapter.notifyDataSetChanged();
                                progressBar.setVisibility(View.GONE);
                                Scroll_view.setVisibility(View.VISIBLE);
                            }
                        } else {
                            Log.e("Firestore Error","Error getting documents: ",task.getException());
                            Toast.makeText(MainActivity.this,"Error"+task.getException(),Toast.LENGTH_LONG).show();
                        }
                    }
                });

        //--------------------x-------------------------x-----------------------x----------------------x--------------------x------------------x----------------------//
        //EXPLORE ITEMS

        homeCatRec.setLayoutManager(new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false));
        categoryList = new ArrayList<>();
        homeAdapter = new HomeAdapter(this,categoryList);
        homeCatRec.setAdapter(homeAdapter);

        db.collection("HomeCategory")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                HomeCategory homeCategory = document.toObject(HomeCategory.class);
                                categoryList.add(homeCategory);
                                homeAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Log.e("Firestore Error","Error getting documents: ",task.getException());
                            Toast.makeText(MainActivity.this,"Error"+task.getException(),Toast.LENGTH_LONG).show();
                        }
                    }
                });

        //--------------------x-------------------------x-----------------------x----------------------x--------------------x------------------x----------------------//
        //RECOMMENDED ITEMS

        recommendedModelList = new ArrayList<>();
        recommendedAdapter = new RecommendedAdapter(this,recommendedModelList);
        recommendedRec.setAdapter(recommendedAdapter);

        db.collection("Recommended")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                RecommendedModel recommendedModel = document.toObject(RecommendedModel.class);
                                recommendedModelList.add(recommendedModel);
                                recommendedAdapter.notifyDataSetChanged();
                            }
                        } else {
                            Log.e("Firestore Error","Error getting documents: ",task.getException());
                            Toast.makeText(MainActivity.this,"Error"+task.getException(),Toast.LENGTH_LONG).show();
                        }
                    }
                });


        //--------------------x-------------------------x-----------------------x----------------------x--------------------x------------------x----------------------//
        //EMIRATES CHOOSER
        Emirates_Spinner = findViewById(R.id.Emirates_Spinner);
        final String[] Emirates = {"Dubai", "Abu Dhabi", "Sharjah", "Ajman", "Umm Al Quwain","Fujairah","Ras Al Khaimah"};

        ArrayAdapter<String> a = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, Emirates);
        a.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Emirates_Spinner.setAdapter(a);

        Emirates_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                UserDataSingleton.getInstance().setEmirate(Emirates_Spinner.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //--------------------x-------------------------x-----------------------x----------------------x--------------------x------------------x----------------------//
        // BOTTOM NAVIGATION
        bottom_navigation=findViewById(R.id.bottom_navigation_menu);
        //Set Selected item
        bottom_navigation.setSelectedItemId(R.id.home);
        //Perform item selected listener
        bottom_navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId==R.id.home) return true;
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
                if (itemId==R.id.account){
                    startActivity(new Intent(getApplicationContext(), activity_account.class));
                    overridePendingTransition(0,0);
                    return true;
                }
                return false;
            }
        });


        //--------------------x-------------------------x-----------------------x----------------------x--------------------x------------------x----------------------//


    }

    private void searchProduct(String type){
        if (!type.isEmpty()){
            if (type.equalsIgnoreCase("Flower")||type.equalsIgnoreCase("Plant")||type.equalsIgnoreCase("Bouquet")) {
                db.collection("AllProducts").whereEqualTo("type", capitalizeFirstLetter(type)).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            viewAllModelList.clear();
                            viewAllAdapter.notifyDataSetChanged();
                            for (DocumentSnapshot doc : task.getResult().getDocuments()) {
                                ViewAllModel viewAllModel = doc.toObject(ViewAllModel.class);
                                viewAllModelList.add(viewAllModel);
                                viewAllAdapter.notifyDataSetChanged();

                            }
                        }
                    }
                });
            }
            else {
                db.collection("AllProducts").whereGreaterThanOrEqualTo("name",capitalizeFirstLetter(type)).whereLessThanOrEqualTo("name",type + "\uf8ff").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            viewAllModelList.clear();
                            viewAllAdapter.notifyDataSetChanged();
                            for (DocumentSnapshot doc : task.getResult().getDocuments()) {
                                ViewAllModel viewAllModel = doc.toObject(ViewAllModel.class);
                                viewAllModelList.add(viewAllModel);
                                viewAllAdapter.notifyDataSetChanged();

                            }
                        }
                    }
                });
            }
        }
    }

    public String capitalizeFirstLetter(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
    }
}