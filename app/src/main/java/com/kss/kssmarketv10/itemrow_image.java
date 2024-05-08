package com.kss.kssmarketv10;

import android.graphics.Bitmap;

/**
 * Created by KSS on 30/3/2017.
 */

public class itemrow_image {
    private String itemTitulo;
    private Double itemValue;
    private Bitmap itemImage;
    private Long ID;

    public itemrow_image(String itemTitulo, Double itemValue, Bitmap itemImage, Long ID) {
        this.itemTitulo = itemTitulo;
        this.itemValue = itemValue;
        this.itemImage = itemImage;
        this.ID = ID;
    }

    public String getItemTitulo() {
        return itemTitulo;
    }

    public void setItemTitulo(String itemTitulo) {
        this.itemTitulo = itemTitulo;
    }

    public Double getItemValue() {
        return itemValue;
    }

    public void setItemValue(Double itemValue) {
        this.itemValue = itemValue;
    }

    public Bitmap getItemImage() {
        return itemImage;
    }

    public void setItemImage(Bitmap itemImage) {
        this.itemImage = itemImage;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public String toString() {
        return this.getItemTitulo().toString();
    }
}
