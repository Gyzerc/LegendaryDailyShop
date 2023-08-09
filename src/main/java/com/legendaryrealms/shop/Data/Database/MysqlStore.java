package com.legendaryrealms.shop.Data.Database;

import com.google.gson.Gson;
import com.legendaryrealms.shop.Data.DataProvider;
import com.legendaryrealms.shop.Data.Player.PlayerData;
import org.bukkit.Bukkit;

import java.util.Calendar;
import java.util.HashMap;

public class MysqlStore extends DataProvider {
    @Override
    public void saveData(PlayerData data) {
        Gson gson=new Gson();
        MysqlManager.setPlayerString(data.getPlayer(),gson.toJson(data,PlayerData.class));
        Bukkit.getLogger().fine("成功保存玩家 "+data.getPlayer()+" 数据");
    }

    @Override
    public PlayerData loadData(String player) {
        if (isExist(player)) {
            Gson gson = new Gson();
            PlayerData old = gson.fromJson(MysqlManager.getPlayerStringData(player), PlayerData.class);
            PlayerData data = new PlayerData(old.getData(), player, old.getDate(), old.isTip());
            return data;
        }
        Calendar calendar=Calendar.getInstance();
        return new PlayerData(new HashMap<>(),player,calendar.get(Calendar.DATE),false);
    }

    @Override
    public boolean isExist(String player) {
        return MysqlManager.hasPlayerData(player);
    }
}
