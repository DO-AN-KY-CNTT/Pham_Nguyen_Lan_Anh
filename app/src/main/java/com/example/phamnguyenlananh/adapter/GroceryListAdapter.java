package com.example.phamnguyenlananh.adapter;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.phamnguyenlananh.R;
import com.example.phamnguyenlananh.data.api.model.IngredientItemResponse;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class GroceryListAdapter extends RecyclerView.Adapter<GroceryListAdapter.ViewHolder> {
    private List<IngredientItemResponse> ingredients;
    private OnItemCheckChangeListener listener;

    public interface OnItemCheckChangeListener {
        void onItemCheckChanged(IngredientItemResponse item, int position, boolean isChecked);
    }

    public GroceryListAdapter(List<IngredientItemResponse> ingredients, OnItemCheckChangeListener listener) {
        this.ingredients = ingredients != null ? ingredients : new ArrayList<>();
        this.listener = listener;
    }

    public void updateData(List<IngredientItemResponse> list) {
        this.ingredients = list != null ? list : new ArrayList<>();
        notifyDataSetChanged();
    }

    public List<IngredientItemResponse> getIngredients() {
        return ingredients;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grocery, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        IngredientItemResponse item = ingredients.get(position);
        holder.bind(item, position, listener);
    }

    @Override
    public int getItemCount() {
        return ingredients != null ? ingredients.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox cbGrocery;
        TextView tvGroceryName, tvGroceryCategory, tvGroceryQuantity, tvGroceryPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cbGrocery = itemView.findViewById(R.id.cbGrocery);
            tvGroceryName = itemView.findViewById(R.id.tvGroceryName);
            tvGroceryCategory = itemView.findViewById(R.id.tvGroceryCategory);
            tvGroceryQuantity = itemView.findViewById(R.id.tvGroceryQuantity);
            tvGroceryPrice = itemView.findViewById(R.id.tvGroceryPrice);
        }

        public void bind(IngredientItemResponse item, int position, OnItemCheckChangeListener listener) {
            cbGrocery.setOnCheckedChangeListener(null);
            cbGrocery.setChecked(item.isChecked());

            tvGroceryName.setText(item.getTenNguyenLieu());

            NumberFormat currencyFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
            String donGiaStr = currencyFormat.format(item.getDonGia()) + "đ/" + item.getDonVi();
            tvGroceryCategory.setText(donGiaStr);

            double qty = item.getSoLuong();
            String qtyStr = (qty == Math.floor(qty)) ? String.format(Locale.US, "%.0f", qty) : String.format(Locale.US, "%.2f", qty);
            tvGroceryQuantity.setText(qtyStr + " " + item.getDonVi());

            tvGroceryPrice.setText(currencyFormat.format(item.getThanhTien()) + "đ");

            updateStrikeThrough(item.isChecked());

            cbGrocery.setOnCheckedChangeListener((buttonView, isChecked) -> {
                item.setChecked(isChecked);
                updateStrikeThrough(isChecked);
                if (listener != null) listener.onItemCheckChanged(item, position, isChecked);
            });

            itemView.setOnClickListener(v -> cbGrocery.setChecked(!cbGrocery.isChecked()));
        }

        private void updateStrikeThrough(boolean isChecked) {
            if (isChecked) {
                tvGroceryName.setPaintFlags(tvGroceryName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                tvGroceryName.setTextColor(itemView.getContext().getResources().getColor(R.color.on_surface_variant));
            } else {
                tvGroceryName.setPaintFlags(tvGroceryName.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                tvGroceryName.setTextColor(itemView.getContext().getResources().getColor(R.color.on_surface));
            }
        }
    }
}
