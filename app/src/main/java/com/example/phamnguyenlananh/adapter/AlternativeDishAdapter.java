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
import com.example.phamnguyenlananh.model.AlternativeDish;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AlternativeDishAdapter extends RecyclerView.Adapter<AlternativeDishAdapter.ViewHolder> {
    private List<AlternativeDish> dishes;
    private OnDishSelectListener listener;

    public interface OnDishSelectListener {
        void onDishSelect(AlternativeDish dish);
    }

    public AlternativeDishAdapter(List<AlternativeDish> dishes, OnDishSelectListener listener) {
        this.dishes = dishes != null ? dishes : new ArrayList<>();
        this.listener = listener;
    }

    public void updateData(List<AlternativeDish> newDishes) {
        this.dishes = newDishes != null ? newDishes : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_alternative_dish, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AlternativeDish dish = dishes.get(position);
        holder.bind(dish, listener);
    }

    @Override
    public int getItemCount() {
        return dishes != null ? dishes.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgAltDish;
        TextView tvAltSavings, tvAltMatchTag, tvAltDishName, tvAltStats, tvAltCost;
        Button btnSelectAltDish;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAltDish = itemView.findViewById(R.id.imgAltDish);
            tvAltSavings = itemView.findViewById(R.id.tvAltSavings);
            tvAltMatchTag = itemView.findViewById(R.id.tvAltMatchTag);
            tvAltDishName = itemView.findViewById(R.id.tvAltDishName);
            tvAltStats = itemView.findViewById(R.id.tvAltStats);
            tvAltCost = itemView.findViewById(R.id.tvAltCost);
            btnSelectAltDish = itemView.findViewById(R.id.btnSelectAltDish);
        }

        public void bind(AlternativeDish dish, OnDishSelectListener listener) {
            if (dish.getImageRes() != 0) {
                imgAltDish.setImageResource(dish.getImageRes());
            } else {
                imgAltDish.setImageResource(R.drawable.img_dish_pork);
            }
            tvAltSavings.setText(dish.getSavingsOrExtra());
            tvAltMatchTag.setText(dish.getMatchTag());
            tvAltDishName.setText(dish.getDishName());
            tvAltStats.setText(dish.getCalories() + " kcal • " + dish.getProtein() + "g Đạm");

            NumberFormat currencyFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
            tvAltCost.setText(currencyFormat.format(dish.getCost()) + "đ");

            btnSelectAltDish.setOnClickListener(v -> {
                if (listener != null) listener.onDishSelect(dish);
            });
        }
    }
}
