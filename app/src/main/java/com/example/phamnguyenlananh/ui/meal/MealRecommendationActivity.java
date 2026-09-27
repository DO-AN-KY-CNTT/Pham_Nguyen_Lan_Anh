package com.example.phamnguyenlananh.ui.meal;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.phamnguyenlananh.R;
import com.example.phamnguyenlananh.ui.main.MealFragment;

public class MealRecommendationActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new MealFragment())
                    .commit();
        }
    }
}