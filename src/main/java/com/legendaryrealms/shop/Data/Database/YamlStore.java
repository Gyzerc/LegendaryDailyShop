package com.legendaryrealms.shop.Data.Database;

import com.legendaryrealms.shop.Data.DataProvider;
import com.legendaryrealms.shop.Data.Player.PlayerData;
import com.legendaryrealms.shop.Data.Player.ShopData;
import com.legendaryrealms.shop.LegendaryDailyShop;
import com.legendaryrealms.shop.Shop.Shop;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class YamlStore extends DataProvider {
    @Override
    public void saveData(PlayerData data) {
        File file=new File("./plugins/LegendaryDailyShop/Data",data.getPlayer()+".yml");
        YamlConfiguration yml=YamlConfiguration.loadConfiguration(file);
        yml.set("date",data.getDate());
        yml.set("tip",data.isTip());
        for (Map.Entry<String, ShopData> shopMap:data.getData().entrySet())
        {
            String shop=shopMap.getKey();
            ShopData shopData=shopMap.getValue();
            yml.set("data."+shop+".items",toStringList(shopData.getItems()));
            yml.set("data."+shop+".roll",shopData.getRoll());
            for (Map.Entry<UUID, Integer> uuid:shopData.getBuy().entrySet()){
                yml.set("data."+shop+".buy."+uuid.getKey(),uuid.getValue());

            }
            for (Map.Entry<UUID, Double> uuid:shopData.getPrice().entrySet()){
                yml.set("data."+shop+".price."+uuid.getKey(),uuid.getValue());
            }
        }
        try {
            yml.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Bukkit.getLogger().fine("成功保存玩家 "+data.getPlayer()+" 数据");
    }

    public List<String> toStringList(List<UUID> list)
    {
        List<String> l=new ArrayList<>();
        list.forEach(uuid -> {
            l.add(uuid.toString());
        });
        return l;
    }

    @Override
    public PlayerData loadData(String player) {
        File file=new File("./plugins/LegendaryDailyShop/Data",player+".yml");
        YamlConfiguration yml=YamlConfiguration.loadConfiguration(file);
        if (!file.exists())
        {
            Calendar calendar=Calendar.getInstance();
            return new PlayerData(new HashMap<>(),player,calendar.get(Calendar.DATE),false);
        }
        HashMap<String,ShopData> data=new HashMap<>();
            if (yml.getConfigurationSection("data") != null) {
                for (String shop : yml.getConfigurationSection("data").getKeys(false)) {
                    List<UUID> items=toList(yml.getStringList("data." + shop + ".items"));
                    Map<UUID, Double> price = new HashMap<>();
                    if (yml.getConfigurationSection("data."+shop+".price")!=null) {
                        for (String uuid : yml.getConfigurationSection("data." + shop + ".price").getKeys(false)) {
                            price.put(UUID.fromString(uuid), yml.getDouble("data." + shop + ".price." + uuid));
                        }
                    }
                    Map<UUID, Integer> buy = new HashMap<>();
                    if (yml.getConfigurationSection("data."+shop+".buy")!=null) {
                        for (String uuid : yml.getConfigurationSection("data." + shop + ".buy").getKeys(false)) {
                            buy.put(UUID.fromString(uuid), yml.getInt("data." + shop + ".buy." + uuid));
                        }
                    }
                    ShopData shopData = new ShopData(shop, items, price, buy , yml.getInt("data."+shop+".roll",0));
                    data.put(shop, check(shopData));
                }
        }
        return new PlayerData(data,player, yml.getInt("date"),yml.getBoolean("tip"));
    }


    public ShopData check(ShopData data)
    {
        ShopData shopData=data;
        for (UUID uuid:data.getItems())
        {
            if (data.getPrice().get(uuid) == null)
            {
                Map<UUID, Double> prices=data.getPrice();
                Shop shop=LegendaryDailyShop.getInstance().getShopManager().getShop(data.getShop());
                double min=(shop.getMin_price().get(uuid) !=null ? shop.getMin_price().get(uuid) : 0.0 ) * 100;
                double max=(shop.getMax_price().get(uuid) !=null ? shop.getMax_price().get(uuid) : 50.0 ) * 100;
                if (max == min)
                {
                    prices.put(uuid,min);
                }
                else {
                    double next=(new Random()).nextInt((int) (max-min)) + min;
                    next /= 100;
                    prices.put(uuid,next);
                }
                data.setPrice(prices);
            }
        }
        return shopData;
    }

    @Override
    public boolean isExist(String player) {
        File file=new File("./plugins/LegendaryDailyShop/Data",player+".yml");
        return file.exists();
    }



    public List<UUID> toList(List<String> list)
    {
        List<UUID> l=new ArrayList<>();
        list.forEach(u->{
            l.add(UUID.fromString(u));
        });
        return l;
    }
}
