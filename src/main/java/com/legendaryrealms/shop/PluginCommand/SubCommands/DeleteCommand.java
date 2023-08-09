package com.legendaryrealms.shop.PluginCommand.SubCommands;

import com.legendaryrealms.shop.LegendaryDailyShop;
import com.legendaryrealms.shop.Menu.MenuUI.DeleteMenu;
import com.legendaryrealms.shop.PluginCommand.ShopCommand;
import com.legendaryrealms.shop.Shop.Shop;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DeleteCommand extends ShopCommand {
    public DeleteCommand() {
        super("delete", "LegendaryDailyShop.admin", 3);
    }

    @Override
    public void reslove(CommandSender sender, String[] args) {
        if (sender instanceof Player)
        {
            String shopId=args[1];
            String rarity=args[2];
            if (LegendaryDailyShop.getInstance().getShopManager().getShop(shopId) == null)
            {
                sender.sendMessage(LegendaryDailyShop.getInstance().getConfigManager().plugin+LegendaryDailyShop.getInstance().getConfigManager().noshop);
                return;
            }
            if (!LegendaryDailyShop.getInstance().getShopRarityManager().getRarities().contains(rarity))
            {
                sender.sendMessage(LegendaryDailyShop.getInstance().getConfigManager().plugin+LegendaryDailyShop.getInstance().getConfigManager().norarity);
                return;
            }
            Shop shop=LegendaryDailyShop.getInstance().getShopManager().getShop(shopId);
            if (!shop.getRarities().contains(rarity))
            {
                sender.sendMessage(LegendaryDailyShop.getInstance().getConfigManager().plugin+LegendaryDailyShop.getInstance().getConfigManager().norarityInShop);
                return;
            }
            DeleteMenu menu=new DeleteMenu((Player) sender,args[1],1,rarity);
            menu.loadMenu();
            menu.open();
            return;
        }
    }

    @Override
    public List<String> complete(CommandSender sender, String[] args) {
        if (args.length==2)
        {
            return LegendaryDailyShop.getInstance().getShopManager().getShops();
        }
        if (args.length==3)
        {
            String shopId=args[1];
            Shop shop=LegendaryDailyShop.getInstance().getShopManager().getShop(shopId);
            if (shop!=null)
            {
                return shop.getRarities();
            }
            return Arrays.asList("未发现该商店");
        }
        return new ArrayList<>();
    }
}
