package com.legendaryrealms.shop.Shop;

import com.legendaryrealms.shop.Data.Player.PlayerData;
import com.legendaryrealms.shop.Data.Player.ShopData;
import com.legendaryrealms.shop.LegendaryDailyShop;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class Shop {

    private String id;
    private String display;
    private Map<UUID, ItemStack> items;
    private Map<UUID, Double> min_price;
    private Map<UUID, Double> max_price;
    private Map<UUID, Integer> limit;
    private Map<UUID, String> rarity;
    private Map<UUID, ShopCurrency> currency;
    private Map<UUID, ProductType> type;
    private List<String> rarities;
    private HashMap<String,List<UUID>> rarityToItems;

    public Shop(String id) {
        this.id = id;
        this.items = new HashMap<>();
        this.min_price = new HashMap<>();
        this.max_price = new HashMap<>();
        this.limit = new HashMap<>();
        this.rarities = new ArrayList<>();
        this.rarity = new HashMap<>();
        this.rarityToItems=new HashMap<>();
        this.currency=new HashMap<>();
        this.type=new HashMap<>();
    }


    public void setRarities(List<String> rarities) {
        this.rarities = rarities;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public void addItem(UUID uuid, ItemStack i, double min, double max, int buy, String rarity,ShopCurrency currency,ProductType type)
    {
        items.put(uuid,i);
        min_price.put(uuid,min);
        max_price.put(uuid,max);
        limit.put(uuid,buy);
        this.rarity.put(uuid,rarity);
        List<UUID> list=rarityToItems.get(rarity) != null ? rarityToItems.get(rarity) : new ArrayList<>();
        list.add(uuid);
        rarityToItems.put(rarity,list);
        this.currency.put(uuid,currency);
        this.type.put(uuid,type);
    }

    public void deleteItem(Player p,UUID uuid)
    {
        ItemStack i=items.remove(uuid).clone();
        min_price.remove(uuid);
        max_price.remove(uuid);
        limit.remove(uuid);
        String ra=rarity.remove(uuid);
        List<UUID> list=rarityToItems.get(ra) != null ? rarityToItems.get(ra) : new ArrayList<>();
        if (list.contains(uuid))
        {
            list.remove(list.indexOf(uuid));
        }
        rarityToItems.put(ra,list);
        currency.remove(uuid);
        type.remove(uuid);

        if (p!=null)
        {
            String name=i.getType().name();
            if (i.getItemMeta().hasDisplayName())
            {
                name=i.getItemMeta().getDisplayName();
            }
            p.sendMessage(LegendaryDailyShop.getInstance().getConfigManager().plugin+LegendaryDailyShop.getInstance().getConfigManager().delete.replace("%shop%",id).replace("%rarity%",ra).replace("%item%",name));
        }
    }


    public String getDisplay() {
        return display;
    }

    public List<UUID> getRarityItems(String rarity)
    {
        return rarityToItems.get(rarity)!=null ? rarityToItems.get(rarity) : new ArrayList<>();
    }

    public Map<UUID, String> getRarity() {
        return rarity;
    }

    public List<String> getRarities() {
        return rarities;
    }

    public String getId() {
        return id;
    }

    public Map<UUID, ShopCurrency> getCurrency() {
        return currency;
    }

    public Map<UUID, ProductType> getType() {
        return type;
    }

    public Map<UUID, ItemStack> getItems() {
        return items;
    }

    public Map<UUID, Double> getMin_price() {
        return min_price;
    }

    public Map<UUID, Double> getMax_price() {
        return max_price;
    }

    public Map<UUID, Integer> getLimit() {
        return limit;
    }
    public int getLimitAmount(UUID uuid)
    {
        return limit.containsKey(uuid) ? limit.get(uuid) : -1;
    }

    public boolean canBuy(UUID uuid,Player p,int amount)
    {
        PlayerData data=PlayerData.getPlayerData(p);
        ShopData shopData=data.getShopData(id);
        if (shopData == null)
        {
            return false;
        }
        int max=limit.get(uuid);
        if (max > 0)
        {
            int buy=shopData.getBuyAmount(uuid);
            if (buy >= max)
            {
                p.sendMessage(LegendaryDailyShop.getInstance().getConfigManager().plugin+LegendaryDailyShop.getInstance().getConfigManager().max);
                return false;
            }
        }
        double price=shopData.getPrice().get(uuid);
        if (getCurrency().get(uuid).equals(ShopCurrency.Vault))
        {
            if (LegendaryDailyShop.getInstance().getEco().getBalance(p) >= price*amount)
            {
                return true;
            }
            p.sendMessage(LegendaryDailyShop.getInstance().getConfigManager().plugin+LegendaryDailyShop.getInstance().getConfigManager().vault_notenough.replace("%price%",""+price*amount));
            return false;
        }
        else {
            if (LegendaryDailyShop.getInstance().getPlayerPointsAPI().getAPI().look(p.getUniqueId()) >= price*amount)
            {
                return true;
            }
            p.sendMessage(LegendaryDailyShop.getInstance().getConfigManager().plugin+LegendaryDailyShop.getInstance().getConfigManager().playerpoints_notenough.replace("%price%",""+price*amount));
            return false;
        }
    }

    public void dealPrice(UUID uuid,Player p,int amount)
    {
        PlayerData data=PlayerData.getPlayerData(p);
        ShopData shopData=data.getShopData(id);
        if (shopData == null)
        {
            return;
        }
        double price=shopData.getPrice().get(uuid)*amount;
        ItemStack i=items.get(uuid).clone();
        i.setAmount(amount);
        String name=i.getType().name();
        if (i.getItemMeta().hasDisplayName())
        {
            name=i.getItemMeta().getDisplayName();
        }

        if (getCurrency().get(uuid).equals(ShopCurrency.Vault))
        {
            LegendaryDailyShop.getInstance().getEco().withdrawPlayer(p,price);
            p.sendMessage(LegendaryDailyShop.getInstance().getConfigManager().plugin+LegendaryDailyShop.getInstance().getConfigManager().vault_buy.replace("%item%",name).replace("%price%",""+price).replace("%amount%",""+amount));
        }
        else {
            LegendaryDailyShop.getInstance().getPlayerPointsAPI().getAPI().take(p.getUniqueId(), (int) price);
            p.sendMessage(LegendaryDailyShop.getInstance().getConfigManager().plugin+LegendaryDailyShop.getInstance().getConfigManager().playerpoints_buy.replace("%item%",name).replace("%price%",""+price).replace("%amount%",""+amount));
        }
        p.getInventory().addItem(i);
    }

}
