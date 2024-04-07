package com.example.florist_plant_shop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class OnBoarding extends AppCompatActivity {
    private FragmentManager fragmentManager;
    CardView NextCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding);
        NextCard=findViewById(R.id.NextCard);

    }

    public void onClick(View v) {
        startActivity(new Intent(OnBoarding.this, MainActivity.class));
    }


}