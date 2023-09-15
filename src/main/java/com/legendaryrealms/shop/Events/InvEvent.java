package com.legendaryrealms.shop.Events;

import com.legendaryrealms.shop.Menu.MenuUI.DeleteMenu;
import com.legendaryrealms.shop.Menu.MenuUI.ShopMenu;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;

public class InvEvent implements Listener {
    @EventHandler
    public void onInvenEvent(InventoryClickEvent e)
    {
        Player p=(Player) e.getWhoClicked();
        InventoryHolder holder=e.getInventory().getHolder();
        if (holder instanceof DeleteMenu)
        {
            DeleteMenu menu=(DeleteMenu) holder;
            menu.dealEvent(e);
        }
        if (holder instanceof ShopMenu)
        {
            ShopMenu menu=(ShopMenu) holder;
            menu.OnClick(e);
        }
    }
}
