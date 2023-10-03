package com.legendaryrealms.shop.Utils;

import com.legendaryrealms.shop.Data.Player.PlayerData;
import com.legendaryrealms.shop.Data.Player.ShopData;
import com.legendaryrealms.shop.LegendaryDailyShop;
import com.legendaryrealms.shop.Shop.Shop;
import com.legendaryrealms.shop.Shop.ShopRarity;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import java.util.*;


public class RandomUtils {

    public void refreshCheckDate(Player p)
    {
        PlayerData data=LegendaryDailyShop.getInstance().getPlayerDataManager().getData(p.getName());
        Calendar calendar=Calendar.getInstance();
        if (data.getDate() != calendar.get(Calendar.DATE)) {
            data.setDate(calendar.get(Calendar.DATE));
            for (String shop : LegendaryDailyShop.getInstance().getShopManager().getShops()) {
                Bukkit.getScheduler().runTaskAsynchronously(LegendaryDailyShop.getInstance(), () -> {
                    random(p, shop);
                    Bukkit.getConsoleSender().sendMessage(LegendaryDailyShop.getInstance().getConfigManager().plugin + "refresh shop: " + shop);
                });
            }
        }
    }

    public void randomAll(Player p)
    {
        for (String shop : LegendaryDailyShop.getInstance().getShopManager().getShops()) {
            Bukkit.getScheduler().runTaskAsynchronously(LegendaryDailyShop.getInstance(), () -> {
                random(p, shop);
                Bukkit.getConsoleSender().sendMessage(LegendaryDailyShop.getInstance().getConfigManager().plugin + "refresh shop: " + shop);
            });
        }
    }
    public void random(Player p,String shopId)
    {
        PlayerData data=LegendaryDailyShop.getInstance().getPlayerDataManager().getData(p.getName());
        ShopData shopData=data.getShopData(shopId) != null ? data.getShopData(shopId) : new ShopData(shopId,new ArrayList<>(),new HashMap<>(),new HashMap<>(),0);
        List<UUID> uuids=RandomUUIDs(shopId);
        shopData.setItems(uuids);
        shopData.setPrice(RandomPrice(shopId,uuids));
        shopData.setBuy(new HashMap<>());
        data.setShop(shopId,shopData);
    }

    private Map<UUID,Double> RandomPrice(String shopId,List<UUID> uuids)
    {
        Shop shop=LegendaryDailyShop.getInstance().getShopManager().getShop(shopId);
        Map<UUID,Double> map=new HashMap<>();
        uuids.forEach(uuid -> {

            double min=(shop.getMin_price().get(uuid) !=null ? shop.getMin_price().get(uuid) : 0.0 ) * 100;
            double max=(shop.getMax_price().get(uuid) !=null ? shop.getMax_price().get(uuid) : 50.0 ) * 100;
            if (max == min)
            {
                map.put(uuid,min/100);
            }
            else {
                double next=(new Random()).nextInt((int) (max-min)) + min;
                next /= 100;
                map.put(uuid,next);
            }
        });
        return map;
    }

    private List<UUID> RandomUUIDs(String shopId)
    {
        List<UUID> list=new ArrayList<>();
        Shop shop= LegendaryDailyShop.getInstance().getShopManager().getShop(shopId);
        int max=LegendaryDailyShop.getInstance().getMenusManager().getMenu("ShopMenu").getLayout().size();
        int times=LegendaryDailyShop.getInstance().getConfigManager().times;
        while (times>0)
        {
            if (max == 0)
            {
                break;
            }
            for (String rarityId:shop.getRarities())
            {
                int has=0;
                for (UUID uuid:list)
                {
                    if (shop.getRarity().get(uuid).equals(rarityId))
                    {
                        has++;
                    }
                }
                if (has >= LegendaryDailyShop.getInstance().getShopRarityManager().getRarity(rarityId).getMax())
                {
                    continue;
                }
                if (shop.getRarityItems(rarityId).isEmpty())
                {
                    continue;
                }
                ShopRarity rarity=LegendaryDailyShop.getInstance().getShopRarityManager().getRarity(rarityId);
                int chance= (int) (rarity.getChance() * 100);
                if ((new Random()).nextInt(101) <= chance)
                {
                    List<UUID> uuids=shop.getRarityItems(rarityId);
                    UUID uuid=uuids.get((new Random()).nextInt(uuids.size()));
                    if (list.contains(uuid))
                    {
                        UUID other=getOther(list,uuids,uuid);
                        if (other != null) {
                            list.add(other);
                        }
                        else {
                            continue;
                        }
                    }
                    else {
                        list.add(uuid);
                    }
                    max--;
                }
            }
            times--;
        }

        return list;
    }

    private UUID getOther(List<UUID> now,List<UUID> uuids,UUID old)
    {

        List<UUID> list=new ArrayList<>(uuids);
        list.remove(old);
        for (UUID uuid:uuids)
        {
            if (now.contains(uuid))
            {
                list.remove(uuid);
            }
        }

        if (list.size() == 0)
        {
            return null;
        }
        return list.get((new Random()).nextInt(list.size()));
    }
}
