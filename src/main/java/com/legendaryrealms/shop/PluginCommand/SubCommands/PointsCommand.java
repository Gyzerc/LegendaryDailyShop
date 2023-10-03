package com.legendaryrealms.shop.PluginCommand.SubCommands;

import com.legendaryrealms.shop.Data.Player.PlayerData;
import com.legendaryrealms.shop.Data.Player.ShopData;
import com.legendaryrealms.shop.LegendaryDailyShop;
import com.legendaryrealms.shop.PluginCommand.ShopCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class PointsCommand extends ShopCommand {
    public PointsCommand() {
        super("points", "LegendaryDailyShop.admin", 4);
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
         if (!checkIsNumber(args[3]))
         {
             sender.sendMessage(LegendaryDailyShop.getInstance().getConfigManager().plugin+LegendaryDailyShop.getInstance().getConfigManager().notnumber);
             return;
         }
         int a=Integer.parseInt(args[3]);
         Player p=Bukkit.getPlayer(player);
         PlayerData data=LegendaryDailyShop.getInstance().getPlayerDataManager().getData(p.getName());
         ShopData shopData=data.getShopData(shop)!=null ? data.getShopData(shop) : new ShopData(shop,new ArrayList<>(),new HashMap<>(),new HashMap<>(),0);
         shopData.setRoll(shopData.getRoll()+a);
         data.setShop(shop,shopData);

         String display=LegendaryDailyShop.getInstance().getShopManager().getShop(shop).getDisplay();
         p.sendMessage(LegendaryDailyShop.getInstance().getConfigManager().plugin+LegendaryDailyShop.getInstance().getConfigManager().points_recive.replace("%amount%",""+a).replace("%shop%",display));
         sender.sendMessage(LegendaryDailyShop.getInstance().getConfigManager().plugin+LegendaryDailyShop.getInstance().getConfigManager().points_give.replace("%player%",player).replace("%amount%",""+a).replace("%shop%",display));
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
        if (args.length==4)
        {
            return Arrays.asList("数量");
        }
        return new ArrayList<>();
    }
}
