package com.legendaryrealms.shop.Shop;

import java.util.HashMap;

public class ShopRarity {

    private String id;
    private String display;
    private double chance;
    private int max;

    public ShopRarity(String id, String display, double chance, int max) {
        this.id = id;
        this.display = display;
        this.chance = chance;
        this.max = max;
    }

    public String getId() {
        return id;
    }

    public String getDisplay() {
        return display;
    }

    public double getChance() {
        return chance;
    }

    public int getMax() {
        return max;
    }

}
