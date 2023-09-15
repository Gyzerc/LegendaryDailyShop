package com.legendaryrealms.shop.Utils;

import com.legendaryrealms.shop.LegendaryDailyShop;
import com.legendaryrealms.shop.Shop.ShopCurrency;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ItemUtils {


    public static int hasItem(ItemStack item, Player p)
    {
        int a=0;
        for (ItemStack i:p.getInventory().getContents())
        {
            if (i!=null) {
                ItemStack check = i.clone();
                check.setAmount(1);
                if (item.equals(check)) {
                    a += i.getAmount();
                }
            }
        }
        return a;
    }

    public static void takeItem(Player p,ItemStack tar,int amount)
    {
        int last=amount;
        for (ItemStack i:p.getInventory().getContents())
        {
            if (i!=null)
            {
                ItemStack i2=i.clone();
                i2.setAmount(1);
                if (i2.equals(tar))
                {
                    if (i.getAmount() >= amount)
                    {
                        i.setAmount(i.getAmount()-amount);
                        return;
                    }
                    else {
                        last+=i.getAmount();
                        i.setAmount(0);
                    }
                }
            }
        }
    }

}
