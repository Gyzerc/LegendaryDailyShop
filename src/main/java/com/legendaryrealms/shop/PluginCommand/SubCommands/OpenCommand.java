package com.legendaryrealms.shop.PluginCommand.SubCommands;

import com.legendaryrealms.shop.LegendaryDailyShop;
import com.legendaryrealms.shop.Manager.ShopManager;
import com.legendaryrealms.shop.Menu.MenuUI.ShopMenu;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class OpenCommand extends com.legendaryrealms.shop.PluginCommand.ShopCommand {
    public OpenCommand() {
        super("open", "", 2);
    }

    @Override
    public void reslove(CommandSender sender,String[] args) {
        if (LegendaryDailyShop.getInstance().getShopManager().getShop(args[1])!=null)
        {
            String shop=args[1];
            if (sender.hasPermission("LegendaryDailyShop.open."+shop))
            {
                ShopMenu shopMenu=new ShopMenu((Player) sender,shop);
                shopMenu.loadMenu();
                shopMenu.open();
                return;
            }
            sender.sendMessage(LegendaryDailyShop.getInstance().getConfigManager().plugin+LegendaryDailyShop.getInstance().getConfigManager().nopermission);
            return;
        }
        sender.sendMessage(LegendaryDailyShop.getInstance().getConfigManager().noshop);
    }

    @Override
    public List<String> complete(CommandSender sender, String[] args) {
        if (args.length==2)
        {
            return LegendaryDailyShop.getInstance().getShopManager().getShops();
        }
        return new ArrayList<>();
    }
}
