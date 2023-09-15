package com.legendaryrealms.shop.API;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class ShopBuyEvent extends Event {
    private Player player;

    private static final HandlerList HANDLERS = new HandlerList();

    private boolean isCancelled;

    private String shopId;
    private String ratity;
    private int amount;
    public ShopBuyEvent(Player player, String shopId,String rarity,int amount) {
        this.player = player;
        this.shopId=shopId;
        this.amount=amount;
        this.ratity=rarity;
        this.isCancelled = false;
    }

    public String getShopId() {
        return shopId;
    }

    public String getRatity() {
        return ratity;
    }

    public int getAmount() {
        return amount;
    }

    @NotNull
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }



    public Player getPlayer() {
        return this.player;
    }
}
