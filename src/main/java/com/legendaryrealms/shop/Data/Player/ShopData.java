package com.legendaryrealms.shop.Data.Player;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ShopData {

    private String shop;
    private List<UUID> items;
    private Map<UUID,Double> price;
    private Map<UUID,Integer> buy;
    private int roll;
    public ShopData(String shop, List<UUID> items, Map<UUID, Double> price, Map<UUID, Integer> buy,int roll) {
        this.shop = shop;
        this.items = items;
        this.price = price;
        this.buy = buy;
        this.roll = roll;
    }

    public int getRoll() {
        return roll;
    }

    public void setRoll(int roll) {
        this.roll = roll;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public void setItems(List<UUID> items) {
        this.items = items;
    }

    public void setPrice(Map<UUID, Double> price) {
        this.price = price;
    }

    public void setBuy(Map<UUID, Integer> buy) {
        this.buy = buy;
    }

    public String getShop() {
        return shop;
    }

    public List<UUID> getItems() {
        return items;
    }

    public Map<UUID, Double> getPrice() {
        return price;
    }

    public Map<UUID, Integer> getBuy() {
        return buy;
    }

    public int getBuyAmount(UUID uuid)
    {
        return buy.containsKey(uuid) ? buy.get(uuid) : 0;
    }
    public void addBuyAmount(UUID uuid,int amount) {
        int buy=getBuyAmount(uuid);
        this.buy.put(uuid,(buy+amount));

    }

}
