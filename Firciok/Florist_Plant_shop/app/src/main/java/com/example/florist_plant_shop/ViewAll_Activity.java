package com.example.florist_plant_shop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ViewAll_Activity extends AppCompatActivity {
    FirebaseFirestore firestore;
    ImageView back_home_button;
    RecyclerView recyclerView;
    List<ViewAllModel> viewAllModelList;
    ViewAllAdapter viewAllAdapter;
    ProgressBar progress_bar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all);
        back_home_button=findViewById(R.id.back_home_button);
        progress_bar=findViewById(R.id.progress_bar);
        recyclerView=findViewById(R.id.view_all_rec);
        firestore=FirebaseFirestore.getInstance();
        String type=getIntent().getStringExtra("type");

        recyclerView.setVisibility(View.GONE);
        progress_bar.setVisibility(View.VISIBLE);

        viewAllModelList=new ArrayList<>();
        viewAllAdapter=new ViewAllAdapter(this,viewAllModelList);
        recyclerView.setAdapter(viewAllAdapter);

        //--------------------x-------------------------x-----------------------x----------------------x--------------------x------------------x----------------------//
        if (type != null && type.equalsIgnoreCase("pink")){
            firestore.collection("AllProducts").whereEqualTo("color","pink").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {

                    for (DocumentSnapshot documentSnapshot:task.getResult().getDocuments()){
                        ViewAllModel viewAllModel = documentSnapshot.toObject(ViewAllModel.class);
                        viewAllModelList.add(viewAllModel);
                        viewAllAdapter.notifyDataSetChanged();
                        recyclerView.setVisibility(View.VISIBLE);
                        progress_bar.setVisibility(View.GONE);
                    }

                }
            });
        }
        //--------------------x-------------------------x-----------------------x----------------------x--------------------x------------------x----------------------//
        if (type != null && type.equalsIgnoreCase("purple")){
            firestore.collection("AllProducts").whereEqualTo("color","purple").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {

                    for (DocumentSnapshot documentSnapshot:task.getResult().getDocuments()){
                        ViewAllModel viewAllModel = documentSnapshot.toObject(ViewAllModel.class);
                        viewAllModelList.add(viewAllModel);
                        viewAllAdapter.notifyDataSetChanged();
                        recyclerView.setVisibility(View.VISIBLE);
                        progress_bar.setVisibility(View.GONE);
                    }

                }
            });
        }
        //--------------------x-------------------------x-----------------------x----------------------x--------------------x------------------x----------------------//
        if (type != null && type.equalsIgnoreCase("green")){
            firestore.collection("AllProducts").whereEqualTo("color","green").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {

                    for (DocumentSnapshot documentSnapshot:task.getResult().getDocuments()){
                        ViewAllModel viewAllModel = documentSnapshot.toObject(ViewAllModel.class);
                        viewAllModelList.add(viewAllModel);
                        viewAllAdapter.notifyDataSetChanged();
                        recyclerView.setVisibility(View.VISIBLE);
                        progress_bar.setVisibility(View.GONE);
                    }

                }
            });
        }
        //--------------------x-------------------------x-----------------------x----------------------x--------------------x------------------x----------------------//
        if (type != null && type.equalsIgnoreCase("blue")){
            firestore.collection("AllProducts").whereEqualTo("color","blue").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {

                    for (DocumentSnapshot documentSnapshot:task.getResult().getDocuments()){
                        ViewAllModel viewAllModel = documentSnapshot.toObject(ViewAllModel.class);
                        viewAllModelList.add(viewAllModel);
                        viewAllAdapter.notifyDataSetChanged();
                        recyclerView.setVisibility(View.VISIBLE);
                        progress_bar.setVisibility(View.GONE);

                    }

                }
            });
        }
        //--------------------x-------------------------x-----------------------x----------------------x--------------------x------------------x----------------------//
        if (type != null && type.equalsIgnoreCase("white")){
            firestore.collection("AllProducts").whereEqualTo("color","white").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {

                    for (DocumentSnapshot documentSnapshot:task.getResult().getDocuments()){
                        ViewAllModel viewAllModel = documentSnapshot.toObject(ViewAllModel.class);
                        viewAllModelList.add(viewAllModel);
                        viewAllAdapter.notifyDataSetChanged();
                        recyclerView.setVisibility(View.VISIBLE);
                        progress_bar.setVisibility(View.GONE);
                    }

                }
            });
        }
        //--------------------x-------------------------x-----------------------x----------------------x--------------------x------------------x----------------------//
        if (type != null && type.equalsIgnoreCase("red")){
            firestore.collection("AllProducts").whereEqualTo("color","red").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {

                    for (DocumentSnapshot documentSnapshot:task.getResult().getDocuments()){
                        ViewAllModel viewAllModel = documentSnapshot.toObject(ViewAllModel.class);
                        viewAllModelList.add(viewAllModel);
                        viewAllAdapter.notifyDataSetChanged();
                        recyclerView.setVisibility(View.VISIBLE);
                        progress_bar.setVisibility(View.GONE);
                    }

                }
            });
        }
        //--------------------x-------------------------x-----------------------x----------------------x--------------------x------------------x----------------------//
         // BACK BUTTON
        back_home_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //--------------------x-------------------------x-----------------------x----------------------x--------------------x------------------x----------------------//
        // GETTING ALL PRODUCTS
        if (type != null && type.equalsIgnoreCase("All")){
            firestore.collection("AllProducts").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {

                    for (DocumentSnapshot documentSnapshot:task.getResult().getDocuments()){
                        ViewAllModel viewAllModel = documentSnapshot.toObject(ViewAllModel.class);
                        viewAllModelList.add(viewAllModel);
                        viewAllAdapter.notifyDataSetChanged();
                        recyclerView.setVisibility(View.VISIBLE);
                        progress_bar.setVisibility(View.GONE);
                    }

                }
            });
        }
        //--------------------x-------------------------x-----------------------x----------------------x--------------------x------------------x----------------------//
        //GETTING FLOWERS
        if (type != null && type.equalsIgnoreCase("Flower")){
            firestore.collection("AllProducts").whereEqualTo("type","Flower").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {

                    for (DocumentSnapshot documentSnapshot:task.getResult().getDocuments()){
                        ViewAllModel viewAllModel = documentSnapshot.toObject(ViewAllModel.class);
                        viewAllModelList.add(viewAllModel);
                        viewAllAdapter.notifyDataSetChanged();
                        recyclerView.setVisibility(View.VISIBLE);
                        progress_bar.setVisibility(View.GONE);
                    }

                }
            });
        }
        //--------------------x-------------------------x-----------------------x----------------------x--------------------x------------------x----------------------//
        //GETTING PLANTS
        if(type != null && type.equalsIgnoreCase("Plant")){
            firestore.collection("AllProducts").whereEqualTo("type","Plant").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {

                    for (DocumentSnapshot documentSnapshot:task.getResult().getDocuments()){
                        ViewAllModel viewAllModel = documentSnapshot.toObject(ViewAllModel.class);
                        viewAllModelList.add(viewAllModel);
                        viewAllAdapter.notifyDataSetChanged();
                        recyclerView.setVisibility(View.VISIBLE);
                        progress_bar.setVisibility(View.GONE);
                    }

                }
            });
        }
        //--------------------x-------------------------x-----------------------x----------------------x--------------------x------------------x----------------------//
        //GETTING BOUQUET
        if (type != null && type.equalsIgnoreCase("Bouquet")){
            firestore.collection("AllProducts").whereEqualTo("type","Bouquet").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {

                    for (DocumentSnapshot documentSnapshot:task.getResult().getDocuments()){
                        ViewAllModel viewAllModel = documentSnapshot.toObject(ViewAllModel.class);
                        viewAllModelList.add(viewAllModel);
                        viewAllAdapter.notifyDataSetChanged();
                        recyclerView.setVisibility(View.VISIBLE);
                        progress_bar.setVisibility(View.GONE);
                    }

                }
            });
        }

    }

}