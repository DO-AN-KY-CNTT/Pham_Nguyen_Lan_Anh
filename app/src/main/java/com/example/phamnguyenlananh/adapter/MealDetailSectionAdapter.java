package com.example.phamnguyenlananh.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.phamnguyenlananh.R;
import com.example.phamnguyenlananh.model.MealItem;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class MealDetailSectionAdapter extends RecyclerView.Adapter<MealDetailSectionAdapter.ViewHolder> {
    private List<MealItem> mealItems;

    public MealDetailSectionAdapter(List<MealItem> mealItems) {
        this.mealItems = mealItems;
    }

    public void updateData(List<MealItem> items) {
        this.mealItems = items;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_meal_detail, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MealItem item = mealItems.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return mealItems != null ? mealItems.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgDetailDish;
        TextView tvDetailMealType, tvDetailCalories, tvDetailDishName, tvDetailPortion, tvDetailCost;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgDetailDish = itemView.findViewById(R.id.imgDetailDish);
            tvDetailMealType = itemView.findViewById(R.id.tvDetailMealType);
            tvDetailCalories = itemView.findViewById(R.id.tvDetailCalories);
            tvDetailDishName = itemView.findViewById(R.id.tvDetailDishName);
            tvDetailPortion = itemView.findViewById(R.id.tvDetailPortion);
            tvDetailCost = itemView.findViewById(R.id.tvDetailCost);
        }

        public void bind(MealItem item) {
            if (item.getImageRes() != 0) {
                imgDetailDish.setImageResource(item.getImageRes());
            }
            tvDetailMealType.setText(item.getMealType());
            tvDetailCalories.setText(item.getCalories() + " kcal");
            tvDetailDishName.setText(item.getDishName());
            tvDetailPortion.setText(item.getPortion());

            NumberFormat currencyFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
            tvDetailCost.setText(currencyFormat.format(item.getCost()) + "đ");
        }
    }
}