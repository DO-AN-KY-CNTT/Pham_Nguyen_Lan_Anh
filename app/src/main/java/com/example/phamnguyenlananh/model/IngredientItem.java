package com.example.phamnguyenlananh.model;

import java.io.Serializable;

public class IngredientItem implements Serializable {
    private String id;
    private String name;
    private String quantity;
    private int unitPrice;
    private String category;
    private boolean checked;

    public IngredientItem(String id, String name, String quantity, int unitPrice, String category, boolean checked) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.category = category;
        this.checked = checked;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getQuantity() { return quantity; }
    public void setQuantity(String quantity) { this.quantity = quantity; }
    public int getUnitPrice() { return unitPrice; }
    public void setUnitPrice(int unitPrice) { this.unitPrice = unitPrice; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public boolean isChecked() { return checked; }
    public void setChecked(boolean checked) { this.checked = checked; }
}