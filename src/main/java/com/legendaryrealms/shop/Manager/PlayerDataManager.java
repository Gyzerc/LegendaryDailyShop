package com.legendaryrealms.shop.Manager;

import com.legendaryrealms.shop.Data.Player.PlayerData;
import com.legendaryrealms.shop.LegendaryDailyShop;

import java.util.HashMap;
import java.util.Map;

public class PlayerDataManager {

    private HashMap<String, PlayerData> cache;
    public PlayerDataManager(){
        cache=new HashMap<>();
    }
    public PlayerData getData(String player){
        if (cache.containsKey(player)) {
            return cache.get(player);
        }
        PlayerData data= LegendaryDailyShop.getInstance().getDataProvider().loadData(player);
        cache.put(player,data);
        return data;
    }

    public void saveAndRemve(String player){
        if (cache.containsKey(player)){
            PlayerData data=cache.remove(player);
            LegendaryDailyShop.getInstance().getDataProvider().saveData(data);
        }
    }
    public void saveAll()
    {
        int a=0;
        for (Map.Entry<String,PlayerData> entry:cache.entrySet()){
            PlayerData data=entry.getValue();
            LegendaryDailyShop.getInstance().getDataProvider().saveData(data);
            a++;
        }
        System.out.println("成功保存 "+a+" 个玩家数据");
    }
}
