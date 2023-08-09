package com.legendaryrealms.shop.Manager;

import com.legendaryrealms.shop.Events.InvEvent;
import com.legendaryrealms.shop.Events.JoinEvent;
import com.legendaryrealms.shop.Events.QuitEvent;
import com.legendaryrealms.shop.LegendaryDailyShop;
import org.bukkit.Bukkit;

public class ListenManager {

    public ListenManager()
    {

        Bukkit.getPluginManager().registerEvents(new JoinEvent(), LegendaryDailyShop.getInstance());
        Bukkit.getPluginManager().registerEvents(new QuitEvent(),LegendaryDailyShop.getInstance());
        Bukkit.getPluginManager().registerEvents(new InvEvent(),LegendaryDailyShop.getInstance());

    }

}
