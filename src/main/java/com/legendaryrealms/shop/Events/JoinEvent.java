package com.legendaryrealms.shop.Events;

import com.legendaryrealms.shop.Data.Player.PlayerData;
import com.legendaryrealms.shop.LegendaryDailyShop;
import com.legendaryrealms.shop.Utils.RandomUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Calendar;
import java.util.HashMap;

public class JoinEvent implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent e)
    {
        Player p=e.getPlayer();
        if (LegendaryDailyShop.getInstance().getDataProvider().isExist(p.getName()))
        {
            PlayerData.getPlayerData(p);
            new RandomUtils().refreshCheckDate(p);
            return;
        }
        Calendar calendar=Calendar.getInstance();
        new PlayerData(new HashMap<>(),p.getName(),calendar.get(Calendar.DATE),false);
        new RandomUtils().randomAll(p);
        Bukkit.getLogger().fine("成功创建玩家 "+p.getName()+" 数据");
    }
}
