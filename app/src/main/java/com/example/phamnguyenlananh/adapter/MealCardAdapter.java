package com.example.phamnguyenlananh.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.phamnguyenlananh.R;
import com.example.phamnguyenlananh.model.MealPlan;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class MealCardAdapter extends RecyclerView.Adapter<MealCardAdapter.ViewHolder> {
    private List<MealPlan> mealPlans;
    private OnMealPlanClickListener listener;

    public interface OnMealPlanClickListener {
        void onMealPlanClick(MealPlan plan);
    }

    public MealCardAdapter(List<MealPlan> mealPlans, OnMealPlanClickListener listener) {
        this.mealPlans = mealPlans;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_meal_plan_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MealPlan plan = mealPlans.get(position);
        holder.bind(plan, listener);
    }

    @Override
    public int getItemCount() {
        return mealPlans != null ? mealPlans.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgMealPlan;
        TextView tvMatchRate, tvSavings, tvPlanTitle, tvPlanSubtitle;
        TextView tvPlanCalories, tvPlanProtein, tvPlanCost;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgMealPlan = itemView.findViewById(R.id.imgMealPlan);
            tvMatchRate = itemView.findViewById(R.id.tvMatchRate);
            tvSavings = itemView.findViewById(R.id.tvSavings);
            tvPlanTitle = itemView.findViewById(R.id.tvPlanTitle);
            tvPlanSubtitle = itemView.findViewById(R.id.tvPlanSubtitle);
            tvPlanCalories = itemView.findViewById(R.id.tvPlanCalories);
            tvPlanProtein = itemView.findViewById(R.id.tvPlanProtein);
            tvPlanCost = itemView.findViewById(R.id.tvPlanCost);
        }

        public void bind(MealPlan plan, OnMealPlanClickListener listener) {
            if (plan.getImageRes() != 0) {
                imgMealPlan.setImageResource(plan.getImageRes());
            }
            tvMatchRate.setText("Phù hợp " + plan.getMatchRate() + "%");
            if (plan.getSavingsAmount() > 0) {
                tvSavings.setVisibility(View.VISIBLE);
                tvSavings.setText("Tiết kiệm " + (plan.getSavingsAmount() / 1000) + "k");
            } else {
                tvSavings.setVisibility(View.GONE);
            }
            tvPlanTitle.setText(plan.getTitle());
            tvPlanSubtitle.setText(plan.getSubtitle());
            tvPlanCalories.setText(String.format(Locale.getDefault(), "%,d kcal", plan.getTotalCalories()));
            tvPlanProtein.setText(plan.getTotalProtein() + "g");

            NumberFormat currencyFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
            tvPlanCost.setText(currencyFormat.format(plan.getTotalCost()) + "đ/ngày");

            itemView.setOnClickListener(v -> {
                if (listener != null) listener.onMealPlanClick(plan);
            });
        }
    }
}