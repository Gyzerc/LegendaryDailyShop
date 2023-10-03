package com.legendaryrealms.shop.Utils;

import com.legendaryrealms.shop.Data.Player.PlayerData;
import com.legendaryrealms.shop.Data.Player.ShopData;
import com.legendaryrealms.shop.LegendaryDailyShop;
import com.legendaryrealms.shop.Shop.Shop;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ShopPAPI extends PlaceholderExpansion {
    public String getIdentifier() {
        return "LegendaryDailyShop";
    }

    public String getAuthor() {
        return "Gyzer";
    }

    public String getVersion() {
        return "2.0.5";
    }

    @Override
    public String onRequest(OfflinePlayer player, String params) {
        if (params.contains("amount_")) {
            String[] args = params.split("_");
            if (args.length == 3) {
                String shopId = args[1];
                String rarity = args[2];
                PlayerData data = LegendaryDailyShop.getInstance().getPlayerDataManager().getData(player.getName());
                ShopData shopData = data.getShopData(shopId);
                if (shopData != null) {
                    int a = 0;
                    Shop shop = LegendaryDailyShop.getInstance().getShopManager().getShop(shopId);
                    Map<UUID, String> rarities = shop.getRarity();
                    for (UUID uuid : shopData.getItems()) {
                        String itemRarity = rarities.containsKey(uuid) ? rarities.get(uuid) : "无";
                        if (itemRarity.equals(rarity)) {
                            a++;
                        }
                    }
                    return a + "";
                }
            }
        }
        return "0";
    }
}
