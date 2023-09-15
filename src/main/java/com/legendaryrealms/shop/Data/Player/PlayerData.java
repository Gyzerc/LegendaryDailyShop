package com.legendaryrealms.shop.Data.Player;

import com.legendaryrealms.shop.LegendaryDailyShop;
import com.legendaryrealms.shop.Utils.RandomUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

public class PlayerData {

    private static HashMap<String,PlayerData> cache=new HashMap<>();
    private Map<String, ShopData> data;
    private String p;
    private int date;
    private boolean tip;

    public PlayerData(Map<String, ShopData> data, String p, int date, boolean tip) {
        this.data = data;
        this.p = p;
        this.date = date;
        this.tip = tip;
        cache.put(p,this);
    }

    public Map<String, ShopData> getData() {
        return data;
    }

    public List<String> getShops()
    {
        List<String> list=new ArrayList<>();
        for (String shop:data.keySet())
        {
            list.add(shop);
        }
        return list;
    }
    public ShopData getShopData(String shop)
    {
        return data.get(shop);
    }

    public String getPlayer() {
        return p;
    }

    public int getDate() {
        return date;
    }

    public boolean isTip() {
        return tip;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public void setTip(boolean tip) {
        this.tip = tip;
    }

    public static PlayerData getPlayerData(Player p)
    {
        if (cache.containsKey(p.getName()))
        {
            return cache.get(p.getName());
        }
        PlayerData playerData=LegendaryDailyShop.getInstance().getDataProvider().loadData(p.getName());
        cache.put(p.getName(),playerData);
        return playerData;
    }

    public static void updata(PlayerData data)
    {
        cache.put(data.p,data);
    }


    public void setShop(String shopId,ShopData shopData)
    {
        data.put(shopId,shopData);
    }

    public static void saveAllToLocal()
    {
        if (cache.isEmpty())
        {
            return;
        }
        for (String player:cache.keySet())
        {
            PlayerData data=LegendaryDailyShop.getInstance().getDataProvider().loadData(player);
            LegendaryDailyShop.getInstance().getDataProvider().saveData(data);
        }
    }

}
