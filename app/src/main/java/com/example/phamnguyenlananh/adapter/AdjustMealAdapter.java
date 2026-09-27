package com.example.phamnguyenlananh.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.phamnguyenlananh.R;
import com.example.phamnguyenlananh.model.MealItem;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class AdjustMealAdapter extends RecyclerView.Adapter<AdjustMealAdapter.ViewHolder> {
    private List<MealItem> mealItems;
    private OnSwapClickListener listener;

    public interface OnSwapClickListener {
        void onSwapClick(MealItem item, int position);
    }

    public AdjustMealAdapter(List<MealItem> mealItems, OnSwapClickListener listener) {
        this.mealItems = mealItems;
        this.listener = listener;
    }

    public void updateData(List<MealItem> items) {
        this.mealItems = items;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_adjust_meal, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MealItem item = mealItems.get(position);
        holder.bind(item, position, listener);
    }

    @Override
    public int getItemCount() {
        return mealItems != null ? mealItems.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgAdjustDish;
        TextView tvAdjustMealType, tvAdjustDishName, tvAdjustStats, tvAdjustCost;
        Button btnSwapMeal;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAdjustDish = itemView.findViewById(R.id.imgAdjustDish);
            tvAdjustMealType = itemView.findViewById(R.id.tvAdjustMealType);
            tvAdjustDishName = itemView.findViewById(R.id.tvAdjustDishName);
            tvAdjustStats = itemView.findViewById(R.id.tvAdjustStats);
            tvAdjustCost = itemView.findViewById(R.id.tvAdjustCost);
            btnSwapMeal = itemView.findViewById(R.id.btnSwapMeal);
        }

        public void bind(MealItem item, int position, OnSwapClickListener listener) {
            if (item.getImageRes() != 0) {
                imgAdjustDish.setImageResource(item.getImageRes());
            }
            tvAdjustMealType.setText(item.getMealType());
            tvAdjustDishName.setText(item.getDishName());
            tvAdjustStats.setText(item.getCalories() + " kcal • " + item.getProtein() + "g Đạm");

            NumberFormat currencyFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
            tvAdjustCost.setText(currencyFormat.format(item.getCost()) + "đ");

            btnSwapMeal.setOnClickListener(v -> {
                if (listener != null) listener.onSwapClick(item, position);
            });
        }
    }
}