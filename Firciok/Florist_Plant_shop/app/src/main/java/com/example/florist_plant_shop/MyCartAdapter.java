package com.example.florist_plant_shop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class MyCartAdapter extends RecyclerView.Adapter<MyCartAdapter.ViewHolder> {

    FirebaseFirestore firestore;
    FirebaseAuth auth;
    Context context;
    List<MyCartModel> cartModelList;
    int totalPrice=0;
    int Quantity=0;

    public MyCartAdapter(Context context, List<MyCartModel> cartModelList) {
        this.context = context;
        this.cartModelList = cartModelList;
        firestore=FirebaseFirestore.getInstance();
        auth=FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public MyCartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyCartAdapter.ViewHolder holder, int position) {
        Glide.with(context).load(cartModelList.get(position).getProductImg()).into(holder.Product_img);
        holder.name.setText(cartModelList.get(position).getProductName());
        holder.price.setText(cartModelList.get(position).getProductPrice());
        holder.date.setText(cartModelList.get(position).getCurrentDate());
        holder.quantity.setText(cartModelList.get(position).getTotalQuantity());
        holder.totalPrice.setText(String.valueOf(cartModelList.get(position).getTotalPrice()));

        holder.Delete_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usernameUser= UserDataSingleton.getInstance().getEmail();
                firestore.collection("CurrentUser").document(usernameUser).collection("AddToCart").document(cartModelList.get(holder.getAdapterPosition())
                        .getDocumentId()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            cartModelList.remove(cartModelList.get(holder.getAdapterPosition()));
                            notifyDataSetChanged();
                            Toast.makeText(context,"Product deleted",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(context,"Error"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return cartModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView Product_img,Delete_item;
        TextView name,price,date,quantity,totalPrice;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Delete_item=itemView.findViewById(R.id.Delete_item);
            Product_img=itemView.findViewById(R.id.Product_Img);
            name=itemView.findViewById(R.id.Product_name);
            price=itemView.findViewById(R.id.Product_price);
            date=itemView.findViewById(R.id.Current_date);
            quantity=itemView.findViewById(R.id.Total_Quantity);
            totalPrice=itemView.findViewById(R.id.Total_Price);
        }
    }
}
