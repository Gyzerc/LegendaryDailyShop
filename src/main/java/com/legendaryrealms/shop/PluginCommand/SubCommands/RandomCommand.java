package com.legendaryrealms.shop.PluginCommand.SubCommands;

import com.legendaryrealms.shop.Data.Player.PlayerData;
import com.legendaryrealms.shop.Data.Player.ShopData;
import com.legendaryrealms.shop.LegendaryDailyShop;
import com.legendaryrealms.shop.PluginCommand.ShopCommand;
import com.legendaryrealms.shop.Utils.RandomUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class RandomCommand extends ShopCommand {
    public RandomCommand() {
        super("random", "LegendaryDailyShop.admin", 3);
    }

    @Override
    public void reslove(CommandSender sender, String[] args) {
        String player=args[1];
        String shop=args[2];
        if (!LegendaryDailyShop.getInstance().getDataProvider().isExist(args[1]))
        {
            sender.sendMessage(LegendaryDailyShop.getInstance().getConfigManager().plugin+LegendaryDailyShop.getInstance().getConfigManager().noplayer);
            return;
        }
        if (!LegendaryDailyShop.getInstance().getShopManager().getShops().contains(shop))
        {
            sender.sendMessage(LegendaryDailyShop.getInstance().getConfigManager().plugin+LegendaryDailyShop.getInstance().getConfigManager().noshop);
            return;
        }
        if (Bukkit.getPlayer(player)==null)
        {
            sender.sendMessage(LegendaryDailyShop.getInstance().getConfigManager().plugin+LegendaryDailyShop.getInstance().getConfigManager().notonline);
            return;
        }
        Player p=Bukkit.getPlayer(player);
        new RandomUtils().random(p,shop);
        sender.sendMessage(LegendaryDailyShop.getInstance().getConfigManager().plugin+LegendaryDailyShop.getInstance().getConfigManager().random_admin.replace("%player%",player).replace("%shop%",LegendaryDailyShop.getInstance().getShopManager().getShop(shop).getDisplay()));
    }

    @Override
    public List<String> complete(CommandSender sender, String[] args) {
        if (args.length==2)
        {
            List<String> list=new ArrayList<>();
            for (Player p:Bukkit.getOnlinePlayers())
            {
                list.add(p.getName());
            }
            return list;
        }
        if (args.length==3)
        {
            return LegendaryDailyShop.getInstance().getShopManager().getShops();
        }
        return new ArrayList<>();
    }
}
