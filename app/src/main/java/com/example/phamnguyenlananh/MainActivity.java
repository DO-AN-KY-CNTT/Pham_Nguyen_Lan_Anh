package com.example.phamnguyenlananh;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.phamnguyenlananh.data.NutriBudgetRepository;
import com.example.phamnguyenlananh.ui.main.HistoryFragment;
import com.example.phamnguyenlananh.ui.main.HomeFragment;
import com.example.phamnguyenlananh.ui.main.MealFragment;
import com.example.phamnguyenlananh.ui.main.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;

    private final Fragment homeFragment = new HomeFragment();
    private final Fragment mealFragment = new MealFragment();
    private final Fragment historyFragment = new HistoryFragment();
    private final Fragment profileFragment = new ProfileFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Sync fresh data from REST API in background
        NutriBudgetRepository.getInstance().syncAllData(this, null);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        if (savedInstanceState == null) {
            loadFragment(homeFragment);
        }

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_home) {
                loadFragment(homeFragment);
                return true;
            } else if (itemId == R.id.navigation_meal) {
                loadFragment(mealFragment);
                return true;
            } else if (itemId == R.id.navigation_history) {
                loadFragment(historyFragment);
                return true;
            } else if (itemId == R.id.navigation_profile) {
                loadFragment(profileFragment);
                return true;
            }
            return false;
        });
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_container, fragment);
        ft.commit();
    }

    public void navigateToTab(int itemId) {
        if (bottomNavigationView != null) {
            bottomNavigationView.setSelectedItemId(itemId);
        }
    }
}