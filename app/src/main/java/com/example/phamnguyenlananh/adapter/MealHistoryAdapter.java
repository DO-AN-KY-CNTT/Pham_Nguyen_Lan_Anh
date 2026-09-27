package com.example.phamnguyenlananh.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.phamnguyenlananh.R;
import com.example.phamnguyenlananh.data.api.model.MenuHistoryResponse;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class MealHistoryAdapter extends RecyclerView.Adapter<MealHistoryAdapter.ViewHolder> {
    private List<MenuHistoryResponse> historyList;
    private OnHistoryItemClickListener listener;

    public interface OnHistoryItemClickListener {
        void onHistoryItemClick(MenuHistoryResponse historyItem);
    }

    public MealHistoryAdapter(List<MenuHistoryResponse> historyList, OnHistoryItemClickListener listener) {
        this.historyList = historyList;
        this.listener = listener;
    }

    public void updateData(List<MenuHistoryResponse> list) {
        this.historyList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_meal_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MenuHistoryResponse item = historyList.get(position);
        holder.bind(item, listener);
    }

    @Override
    public int getItemCount() {
        return historyList != null ? historyList.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgHistoryDish;
        TextView tvHistoryDate, tvHistoryStatus, tvHistoryTitle, tvHistoryNote, tvHistoryCalories, tvHistoryCost;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgHistoryDish = itemView.findViewById(R.id.imgHistoryDish);
            tvHistoryDate = itemView.findViewById(R.id.tvHistoryDate);
            tvHistoryStatus = itemView.findViewById(R.id.tvHistoryStatus);
            tvHistoryTitle = itemView.findViewById(R.id.tvHistoryTitle);
            tvHistoryNote = itemView.findViewById(R.id.tvHistoryNote);
            tvHistoryCalories = itemView.findViewById(R.id.tvHistoryCalories);
            tvHistoryCost = itemView.findViewById(R.id.tvHistoryCost);
        }

        public void bind(MenuHistoryResponse item, OnHistoryItemClickListener listener) {
            // 1. Tên thực đơn
            String title = item.getTenThucDon();
            if (title == null || title.trim().isEmpty()) {
                title = "Thực đơn #" + (item.getThucDonId() != null ? item.getThucDonId() : item.getId());
            }
            tvHistoryTitle.setText(title);

            // 2. Thời gian thực hiện (VD: 27/09/2026 - 10:30)
            String formattedTime = formatDateTime(item.getThoiGian());
            tvHistoryDate.setText(formattedTime);

            // 3. Loại thao tác (TAO_MOI -> Đã tạo thực đơn, CAP_NHAT -> Đã cập nhật thực đơn, DOI_MON -> Đã thay đổi món ăn)
            String action = item.getHanhDong() != null ? item.getHanhDong().toUpperCase() : "TAO_MOI";
            String actionLabel;
            if (action.contains("DOI_MON")) {
                actionLabel = "Đã thay đổi món ăn";
                tvHistoryStatus.setText("ĐỔI MÓN");
                tvHistoryStatus.setBackgroundResource(R.drawable.bg_badge_amber);
                tvHistoryStatus.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.badge_amber_text));
            } else if (action.contains("CAP_NHAT")) {
                actionLabel = "Đã cập nhật thực đơn";
                tvHistoryStatus.setText("CẬP NHẬT");
                tvHistoryStatus.setBackgroundResource(R.drawable.bg_badge_gray);
                tvHistoryStatus.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.on_surface_variant));
            } else {
                actionLabel = "Đã tạo thực đơn";
                tvHistoryStatus.setText("TẠO MỚI");
                tvHistoryStatus.setBackgroundResource(R.drawable.bg_badge_green);
                tvHistoryStatus.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.badge_green_text));
            }

            // 4. Nội dung / mô tả thao tác
            String note = item.getGhiChu();
            if (note != null && !note.trim().isEmpty()) {
                tvHistoryNote.setText(actionLabel + " • " + note);
            } else {
                tvHistoryNote.setText(actionLabel);
            }

            // 5. Tổng Calo
            double calo = item.getTongCalo() != null ? item.getTongCalo() : 0;
            tvHistoryCalories.setText(String.format(Locale.getDefault(), "%,.0f kcal", calo));

            // 6. Tổng Chi phí
            NumberFormat nf = NumberFormat.getInstance(new Locale("vi", "VN"));
            double cost = item.getTongChiPhi() != null ? item.getTongChiPhi().doubleValue() : 0;
            tvHistoryCost.setText(nf.format(cost) + "đ");

            // 7. Ảnh minh họa
            int[] dishImages = {
                    R.drawable.img_dish_pork,
                    R.drawable.img_dish_bento,
                    R.drawable.img_dish_chicken,
                    R.drawable.img_dish_vietnamese_lunch,
                    R.drawable.img_dish_fish
            };
            int imgIdx = (int) (Math.abs(item.getId() != null ? item.getId() : 0) % dishImages.length);
            imgHistoryDish.setImageResource(dishImages[imgIdx]);

            // 8. Click listener
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onHistoryItemClick(item);
                }
            });
        }

        private String formatDateTime(String timeIso) {
            if (timeIso == null || timeIso.trim().isEmpty()) return "Hôm nay";
            try {
                String clean = timeIso.replace("Z", "");
                if (clean.contains("T")) {
                    String[] parts = clean.split("T");
                    String[] dateParts = parts[0].split("-");
                    String timePart = parts[1].split("\\.")[0];
                    if (timePart.length() >= 5) {
                        timePart = timePart.substring(0, 5); // HH:mm
                    }
                    if (dateParts.length == 3) {
                        return dateParts[2] + "/" + dateParts[1] + "/" + dateParts[0] + " - " + timePart;
                    }
                }
                return timeIso;
            } catch (Exception e) {
                return timeIso;
            }
        }
    }
}
