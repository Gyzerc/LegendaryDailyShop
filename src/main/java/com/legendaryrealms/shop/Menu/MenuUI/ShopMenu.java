package com.legendaryrealms.shop.Menu.MenuUI;

import com.legendaryrealms.shop.API.ShopBuyEvent;
import com.legendaryrealms.shop.API.ShopSellEvent;
import com.legendaryrealms.shop.Data.Player.PlayerData;
import com.legendaryrealms.shop.Data.Player.ShopData;
import com.legendaryrealms.shop.LegendaryDailyShop;
import com.legendaryrealms.shop.Menu.MenuConfiguration;
import com.legendaryrealms.shop.Shop.ProductType;
import com.legendaryrealms.shop.Shop.Shop;
import com.legendaryrealms.shop.Shop.ShopCurrency;
import com.legendaryrealms.shop.Shop.ShopRarity;
import com.legendaryrealms.shop.Utils.ItemUtils;
import com.legendaryrealms.shop.Utils.RandomUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ShopMenu implements InventoryHolder {
    private Inventory inv;
    private Player p;
    private String shopId;
    private final MenuConfiguration read= LegendaryDailyShop.getInstance().getMenusManager().getMenu("ShopMenu");
    private Shop shop;
    private HashMap<Integer, UUID> uuids;
    private HashMap<Integer,String> fuction;

    public ShopMenu(Player p, String shopId) {

        this.p = p;
        this.shopId = shopId;
        this.shop = LegendaryDailyShop.getInstance().getShopManager().getShop(shopId);
        this.inv = Bukkit.createInventory(this,read.getSize(),read.getTitle().replace("%shop%",shop.getDisplay()));
        this.uuids=new HashMap<>();
        this.fuction=new HashMap<>();
    }

    public void loadMenu()
    {

        PlayerData data=LegendaryDailyShop.getInstance().getPlayerDataManager().getData(p.getName());
        ShopData shopData=data.getShopData(shopId);
        for (String key:read.getKeys())
        {
            for (int slot:read.getCustomItem_Slot(key))
            {
                ItemStack i=read.getCustomItem(key).clone();
                if (i.getItemMeta().hasLore())
                {
                    ItemMeta id=i.getItemMeta();
                    int sl=shopData!=null ? shopData.getRoll() : 0;
                    List<String> l=id.getLore();
                    l.replaceAll(x->x.replace("%amount%",""+sl));
                    id.setLore(l);
                    i.setItemMeta(id);
                }
                inv.setItem(slot,i);
                fuction.put(slot,read.getFuction(key));
            }
        }


        if (shopData!=null) {
            int a=0;
            for (UUID uuid : shopData.getItems()) {
                if (shop.getItems().get(uuid) == null)
                {
                    if (a < read.getLayout().size()) {
                        inv.setItem(read.getLayout().get(a), read.getUnknown());
                        fuction.put(read.getLayout().get(a), "null");
                        a++;
                        continue;
                    }
                }
                ItemStack i = shop.getItems().get(uuid).clone();
                ItemMeta id=i.getItemMeta();
                List<String> oldlore=id.hasLore() ? id.getLore() : new ArrayList<>();
                List<String> format=new ArrayList<>(read.getLore_format());
                ArrayList lore=new ArrayList();

                String rarity=shop.getRarity().get(uuid);
                String rarityDisplay="Unknown";
                if (rarity != null) {
                    ShopRarity shopRarity = LegendaryDailyShop.getInstance().getShopRarityManager().getRarity(rarity);
                    rarityDisplay=shopRarity.getDisplay();
                }

                for (String l:format)
                {
                    l=l.replace("%price%",""+shopData.getPrice().get(uuid)).replace("%rarity%",rarityDisplay);
                    switch (l) {
                        case "%lore%":
                            lore.addAll(oldlore);
                            break;
                        case "%placeholder_limit%":
                            if (shop.getLimitAmount(uuid) > 0) {
                                String limit = read.getPlaceholder("limit").replace("%buy%", "" + shopData.getBuyAmount(uuid)).replace("%max%", "" + shop.getLimitAmount(uuid));
                                lore.add(limit);
                                break;
                            }
                            break;
                        case "%placeholder_sellOrBuy%":
                            if (shop.getType().get(uuid).equals(ProductType.BUY))
                            {
                                lore.add(read.getPlaceholder("buy"));
                                break;
                            }
                            lore.add(read.getPlaceholder("sell"));
                            break;
                        default:
                            lore.add(l);
                            break;
                    }
                }
                id.setLore(lore);
                i.setItemMeta(id);
                inv.setItem(read.getLayout().get(a),i);
                uuids.put(read.getLayout().get(a),uuid);
                fuction.put(read.getLayout().get(a),"items");
                a++;
            }
            for (int in=a;in < read.getLayout().size();in++) {
                if (a < read.getLayout().size()) {
                    inv.setItem(read.getLayout().get(in), read.getEmpty());
                    fuction.put(read.getLayout().get(in), "null");
                }
            }
        }
        else {
            for (int a=0;a<read.getLayout().size();a++)
            {
                if (a < read.getLayout().size()) {
                    inv.setItem(read.getLayout().get(a), read.getEmpty());
                    fuction.put(read.getLayout().get(a), "null");
                }
            }
        }
    }


    public void OnClick(InventoryClickEvent  e)
    {
        e.setCancelled(true);
        if (e.getRawSlot() >= 0 && e.getRawSlot() < read.getSize())
        {
            String fuction=this.fuction.get(e.getRawSlot()) != null ? this.fuction.get(e.getRawSlot()) : "null";
            switch (fuction)
            {
                case "refresh":
                    PlayerData data=LegendaryDailyShop.getInstance().getPlayerDataManager().getData(p.getName());
                    ShopData shopData=data.getShopData(shopId);
                    if (shopData.getRoll() < 1)
                    {
                        p.sendMessage(LegendaryDailyShop.getInstance().getConfigManager().plugin+LegendaryDailyShop.getInstance().getConfigManager().random_cant);
                        break;
                    }
                    int roll=shopData.getRoll()-1;
                    shopData.setRoll(roll);
                    data.setShop(shopId,shopData);

                    p.sendMessage(LegendaryDailyShop.getInstance().getConfigManager().plugin+LegendaryDailyShop.getInstance().getConfigManager().random.replace("%amount%",""+roll));
                    new RandomUtils().random(p,shopId);
                    ShopMenu menu = new ShopMenu(p, shopId);
                    menu.loadMenu();
                    menu.open();
                    break;
                case "close":
                    break;
                case "items":
                    UUID uuid=uuids.get(e.getRawSlot());
                    data=LegendaryDailyShop.getInstance().getPlayerDataManager().getData(p.getName());
                    shopData=data.getShopData(shopId);
                    if (uuid != null)
                    {
                        int amount=LegendaryDailyShop.getInstance().getConfigManager().click_left_amount;
                        if (e.isLeftClick())
                        {
                            if (e.isShiftClick())
                            {
                                amount=LegendaryDailyShop.getInstance().getConfigManager().click_shift_left_amount;
                            }
                        }
                        if (e.isRightClick())
                        {
                            if (e.isShiftClick())
                            {
                                amount = LegendaryDailyShop.getInstance().getConfigManager().click_shift_right_amount;
                            }
                            else {
                                amount=LegendaryDailyShop.getInstance().getConfigManager().click_right_amount;
                            }
                        }
                        if (amount == -1){
                            if (shop.getType().get(uuid).equals(ProductType.BUY))
                            {
                                amount=canBuyMax(shopData.getPrice().get(uuid),shop.getCurrency().get(uuid),p);
                            }
                            else {
                                amount= ItemUtils.hasItem(shop.getItems().get(uuid),p);
                            }
                        }
                        if (amount == 0)
                        {
                            amount=1;
                        }

                        if (shop.getLimitAmount(uuid) > 0) {
                            int limit = shop.getLimitAmount(uuid);
                            int buy=shopData.getBuyAmount(uuid);
                            if (buy >= limit)
                            {
                                p.sendMessage(LegendaryDailyShop.getInstance().getConfigManager().plugin + LegendaryDailyShop.getInstance().getConfigManager().max);
                                return;
                            }
                            if (limit-buy < amount)
                            {
                                amount= limit-buy;
                            }
                        }
                        if (shop.getType().get(uuid).equals(ProductType.BUY)) {
                            if (shop.canBuy(uuid, p, amount)) {
                                shop.dealPrice(uuid, p, amount);
                                if (shop.getLimitAmount(uuid) > 0) {
                                    shopData.addBuyAmount(uuid, amount);
                                    data.setShop(shopId,shopData);

                                    //LegendaryDailyShop.getInstance().getDataProvider().saveData(data);
                                }
                                Bukkit.getPluginManager().callEvent(new ShopBuyEvent(p,shopId,shop.getRarity().get(uuid),amount));
                                menu = new ShopMenu(p, shopId);
                                menu.loadMenu();
                                menu.open();
                                break;
                            }
                        }
                        else {
                            String name=shop.getItems().get(uuid).getItemMeta().hasDisplayName() ? shop.getItems().get(uuid).getItemMeta().getDisplayName() : shop.getItems().get(uuid).getType().name();
                            if (ItemUtils.hasItem(shop.getItems().get(uuid), p) >= amount)
                            {
                                ItemUtils.takeItem(p,shop.getItems().get(uuid),amount);
                                double earn=amount * shopData.getPrice().get(uuid);
                                DecimalFormat df=new DecimalFormat("#.00");
                                earn=Double.parseDouble(df.format(earn));
                                if (shop.getCurrency().get(uuid).equals(ShopCurrency.Vault)) {
                                    p.sendMessage(LegendaryDailyShop.getInstance().getConfigManager().plugin + LegendaryDailyShop.getInstance().getConfigManager().item_sell_vault.replace("%amount%", "" + amount).replace("%item%", name).replace("%price%",earn+""));
                                    LegendaryDailyShop.getInstance().getEco().depositPlayer(p,earn);
                                }
                                else {
                                    p.sendMessage(LegendaryDailyShop.getInstance().getConfigManager().plugin + LegendaryDailyShop.getInstance().getConfigManager().item_sell_vault.replace("%amount%", "" + amount).replace("%item%", name).replace("%price%",earn+""));
                                    LegendaryDailyShop.getInstance().getPlayerPointsAPI().getAPI().give(p.getUniqueId(), (int) earn);
                                }

                                if (shop.getLimitAmount(uuid) > 0) {
                                    shopData.addBuyAmount(uuid, amount);
                                    data.setShop(shopId,shopData);

                                    //LegendaryDailyShop.getInstance().getDataProvider().saveData(data);
                                }

                                Bukkit.getPluginManager().callEvent(new ShopSellEvent(p,shopId,shop.getRarity().get(uuid),amount));
                                menu = new ShopMenu(p, shopId);
                                menu.loadMenu();
                                menu.open();
                                break;
                            }
                            p.sendMessage(LegendaryDailyShop.getInstance().getConfigManager().plugin+LegendaryDailyShop.getInstance().getConfigManager().item_notenough.replace("%amount%",""+amount).replace("%item%",name));
                            break;
                        }
                    }
                    break;
            }


        }

    }
    public static int canBuyMax(double singlePrice, ShopCurrency currency, Player p)
    {
        if (currency.equals(ShopCurrency.Vault))
        {
            double has= LegendaryDailyShop.getInstance().getEco().getBalance(p);
            return (int) (has/singlePrice);
        }
        else {
            double has= LegendaryDailyShop.getInstance().getPlayerPointsAPI().getAPI().look(p.getUniqueId());
            return (int) (has/singlePrice);
        }
    }
    public void open()
    {
        p.openInventory(inv);
    }

    public Player getPlayer() {
        return p;
    }

    public String getShopId() {
        return shopId;
    }

    @Override
    public Inventory getInventory() {
        return inv;
    }
}
