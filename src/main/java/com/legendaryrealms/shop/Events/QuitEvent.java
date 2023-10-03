package com.legendaryrealms.shop.Events;

import com.legendaryrealms.shop.Data.Player.PlayerData;
import com.legendaryrealms.shop.LegendaryDailyShop;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuitEvent implements Listener {
    @EventHandler
    public void onQuit(PlayerQuitEvent e)
    {
        Player p=e.getPlayer();
        LegendaryDailyShop.getInstance().getPlayerDataManager().saveAndRemve(p.getName());
    }
}
