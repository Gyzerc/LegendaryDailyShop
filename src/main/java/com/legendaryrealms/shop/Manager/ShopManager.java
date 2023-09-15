package com.legendaryrealms.shop.Manager;

import com.legendaryrealms.shop.Shop.ProductType;
import com.legendaryrealms.shop.Shop.Shop;
import com.legendaryrealms.shop.Shop.ShopCurrency;
import com.legendaryrealms.shop.Utils.MsgUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class ShopManager {

    private HashMap<String, Shop> cache;
    private List<String> shops;
    public ShopManager()
    {
        shops=new ArrayList<>();
        cache=new HashMap<>();
    }
    public void reload()
    {

        File fileParent = new File("./plugins/LegendaryDailyShop/Shops");
        int a=0;
        if (fileParent.exists()) {
            for (File file : fileParent.listFiles()) {
                String shopName = file.getName().replace(".yml", "");
                YamlConfiguration yml = YamlConfiguration.loadConfiguration(file);
                ConfigurationSection sec = yml.getConfigurationSection("items");
                Shop shop = new Shop(shopName);
                if (sec != null) {
                    for (String uuid : sec.getKeys(false)) {
                        shop.addItem(UUID.fromString(uuid), yml.getItemStack("items." + uuid + ".item"), yml.getDouble("items." + uuid + ".min"), yml.getDouble("items." + uuid + ".max"), yml.getInt("items." + uuid + ".limit"), yml.getString("items." + uuid + ".rarity"),getCurrency(yml.getString("items."+uuid+".currency","vault")),getProductType(yml.getString("items."+uuid+".type","buy")));
                    }
                }
                List<String> list = yml.getStringList("rarities") != null ? yml.getStringList("rarities") : new ArrayList<>();
                shop.setRarities(list);
                shop.setDisplay(MsgUtils.msg(yml.getString("display", "&f每日商店（请在Shops/" + shopName + ".yml的 display 修改）")));

                cache.put(shopName, shop);
                shops.add(shopName);
                a++;
            }
        }
        Bukkit.getConsoleSender().sendMessage("[LegendaryDailyShop] 共加载 "+a+" 个商店");

    }

    public void saveShop(Shop shop) {
        File fileParent = new File("./plugins/LegendaryDailyShop/Shops",shop.getId()+".yml");
        YamlConfiguration yml=YamlConfiguration.loadConfiguration(fileParent);
        yml.set("display",shop.getDisplay());
        yml.set("rarities",shop.getRarities());
        yml.set("items",null);
        for (Map.Entry<UUID,ItemStack> map:shop.getItems().entrySet())
        {
            UUID uuid=map.getKey();
            String path="items."+map.getKey()+".";
            yml.set(path+"item",map.getValue());
            yml.set(path+"min",shop.getMin_price().get(uuid));
            yml.set(path+"max",shop.getMax_price().get(uuid));
            yml.set(path+"limit",shop.getLimit().get(uuid));
            yml.set(path+"rarity",shop.getRarity().get(uuid));
            yml.set(path+"currency",shop.getCurrency().get(uuid).toString());
            yml.set(path+"type",shop.getType().get(uuid).toString());
        }
        try {
            yml.save(fileParent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Bukkit.getConsoleSender().sendMessage("[LegendaryDailyShop] 成功保存商店 "+shop.getId());
    }

    public Shop getShop(String id)
    {
        return cache.get(id);
    }

    public List<String> getShops() {
        return shops;
    }

    public ShopCurrency getCurrency(String currency)
    {
        if (currency.equalsIgnoreCase("vault")){
            return ShopCurrency.Vault;
        }
        return ShopCurrency.PlayerPoints;
    }
    public ProductType getProductType(String type)
    {
        if (type.equalsIgnoreCase("buy"))
        {
            return ProductType.BUY;
        }
        return ProductType.SELL;
    }
}
