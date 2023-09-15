package com.legendaryrealms.shop.PluginCommand.SubCommands;

import com.legendaryrealms.shop.LegendaryDailyShop;
import com.legendaryrealms.shop.PluginCommand.ShopCommand;
import com.legendaryrealms.shop.Shop.Shop;
import com.legendaryrealms.shop.Shop.ShopRarity;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddCommand extends ShopCommand {
    public AddCommand() {
        super("add", "LegendaryDailyShop.admin", 8);
    }

    @Override
    public void reslove(CommandSender sender, String[] args) {
        if (sender instanceof Player)
        {
            String shopId=args[1];
            Shop shop=LegendaryDailyShop.getInstance().getShopManager().getShop(shopId);
            if (shop == null)
            {
                sender.sendMessage(LegendaryDailyShop.getInstance().getConfigManager().plugin+LegendaryDailyShop.getInstance().getConfigManager().noshop);
                return;
            }
            ShopRarity rarity=LegendaryDailyShop.getInstance().getShopRarityManager().getRarity(args[2]);
            if (rarity == null)
            {
                sender.sendMessage(LegendaryDailyShop.getInstance().getConfigManager().plugin+LegendaryDailyShop.getInstance().getConfigManager().norarity);
                return;
            }
            if (!shop.getRarities().contains(rarity.getId()))
            {
                sender.sendMessage(LegendaryDailyShop.getInstance().getConfigManager().plugin+LegendaryDailyShop.getInstance().getConfigManager().norarityInShop.replace("%shop%",shopId+".yml"));
                return;
            }
            String minStr=args[3];String maxStr=args[4];
            String limit=args[5];
            if (checkIsNumber(minStr) && checkIsNumber(maxStr) && checkIsNumber(limit.replace("-","")))
            {
                String currency=args[6];
                String type=args[7];
                if (currency.equalsIgnoreCase("vault") || currency.equalsIgnoreCase("playerpoints")) {
                    if (type.equalsIgnoreCase("buy") || type.equalsIgnoreCase("sell")) {
                        Player p = (Player) sender;
                        ItemStack i = p.getInventory().getItemInMainHand().clone();
                        i.setAmount(1);
                        if (i == null) {
                            p.sendMessage(LegendaryDailyShop.getInstance().getConfigManager().plugin + LegendaryDailyShop.getInstance().getConfigManager().itemnull);
                            return;
                        }
                        shop.addItem(UUID.randomUUID(), i, Double.parseDouble(minStr), Double.parseDouble(maxStr), Integer.parseInt(limit), rarity.getId() ,LegendaryDailyShop.getInstance().getShopManager().getCurrency(currency),LegendaryDailyShop.getInstance().getShopManager().getProductType(type));
                        LegendaryDailyShop.getInstance().getShopManager().saveShop(shop);
                        String name = i.getType().name();
                        if (i.getItemMeta().hasDisplayName()) {
                            name = i.getItemMeta().getDisplayName();
                        }
                        p.sendMessage(LegendaryDailyShop.getInstance().getConfigManager().plugin + LegendaryDailyShop.getInstance().getConfigManager().add.replace("%shop%", shopId).replace("%rarity%", rarity.getDisplay()).replace("%item%", name));
                        return;
                    }
                    sender.sendMessage(LegendaryDailyShop.getInstance().getConfigManager().plugin+LegendaryDailyShop.getInstance().getConfigManager().unknown_type);
                    return;
                }
                sender.sendMessage(LegendaryDailyShop.getInstance().getConfigManager().plugin+LegendaryDailyShop.getInstance().getConfigManager().unknown_currency);
                return;
            }
            sender.sendMessage(LegendaryDailyShop.getInstance().getConfigManager().plugin+LegendaryDailyShop.getInstance().getConfigManager().notnumber);
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
        if (args.length==4)
        {
            return Arrays.asList("最低价格");
        }
        if (args.length==5)
        {
            return Arrays.asList("最高价格");
        }
        if (args.length==6)
        {
            return Arrays.asList("每日限购次数（-1无限）");
        }
        if (args.length==7)
        {
            return Arrays.asList("vault","playerpoints");
        }
        if (args.length==8)
        {
            return Arrays.asList("buy","sell");
        }
        return new ArrayList<>();
    }


}
