package com.legendaryrealms.shop.Menu.MenuUI;

import com.legendaryrealms.shop.LegendaryDailyShop;
import com.legendaryrealms.shop.Menu.MenuConfiguration;
import com.legendaryrealms.shop.Shop.Shop;
import com.legendaryrealms.shop.Utils.MsgUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class DeleteMenu implements InventoryHolder {
    private Inventory inv;
    private Player p;
    private String shopId;
    private int page;
    private String rarity;
    private final MenuConfiguration read= LegendaryDailyShop.getInstance().getMenusManager().getMenu("DeleteMenu");
    private HashMap<Integer,UUID> uuids;
    private HashMap<Integer,String> fuction;
    public DeleteMenu(Player p, String shop, int page, String rarity) {
        this.p = p;
        this.shopId = shop;
        this.page = page;
        this.rarity = rarity;
        this.fuction=new HashMap<>();
        this.uuids=new HashMap<>();
        this.inv = Bukkit.createInventory(this,read.getSize(),read.getTitle().replace("%shop%",shop).replace("%rarity%",rarity));
    }

    public void loadMenu()
    {
        long time=System.currentTimeMillis();
        for (String key:read.getKeys())
        {
            for (int slot:read.getCustomItem_Slot(key))
            {
                inv.setItem(slot,read.getCustomItem(key));
                fuction.put(slot,read.getFuction(key));
            }
        }

        Shop shop=LegendaryDailyShop.getInstance().getShopManager().getShop(shopId);
        int a=0;
        for (UUID uuid:getUUIDs(page))
        {
            ItemStack i=shop.getItems().get(uuid).clone();
            ItemMeta id=i.getItemMeta();
            List<String> lore=id.hasLore() ? id.getLore() : new ArrayList<>();
            lore.add(" ");
            lore.add(MsgUtils.msg("&f[ &c右键删除该商品 &f]"));
            id.setLore(lore);
            i.setItemMeta(id);
            inv.setItem(read.getLayout().get(a),i);
            uuids.put(read.getLayout().get(a),uuid);
            fuction.put(read.getLayout().get(a),"items");
            a++;
        }
    }

    public void open()
    {
        p.openInventory(inv);
    }
    public List<UUID> getUUIDs(int page)
    {
        Shop shop=LegendaryDailyShop.getInstance().getShopManager().getShop(shopId);

        int start= 0 + (page-1)*read.getLayout().size();
        int end = read.getLayout().size() + (page-1)*read.getLayout().size();
        List<UUID> list=new ArrayList<>();
        List<UUID> old=shop.getRarityItems(rarity);

        if (shop != null &&  old != null) {
            for (int s = start; s < end; s++) {
                if (old.size() > s)
                {
                    list.add(old.get(s));
                }
            }
        }
        return list;
    }
    public String getFuction(int slot)
    {
        return fuction.get(slot);
    }

    public Player getPlayer() {
        return p;
    }

    public String getShopId() {
        return shopId;
    }

    public int getPage() {
        return page;
    }

    public String getRarity() {
        return rarity;
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inv;
    }
    public void dealEvent(InventoryClickEvent e)
    {
        e.setCancelled(true);
        if (e.getRawSlot() >=0 && e.getRawSlot() < read.getSize())
        {
            if (e.getCurrentItem() != null || !e.getCurrentItem().getType().equals(Material.AIR))
            {
                String fuction=getFuction(e.getRawSlot()) != null ? getFuction(e.getRawSlot()) : "null";
                switch (fuction)
                {
                    case "null":
                        break;
                    case "pre":
                        if (page > 1)
                        {
                            DeleteMenu menu=new DeleteMenu(p,shopId,(page-1),rarity);
                            menu.loadMenu();
                            menu.open();
                        }
                        break;
                    case "next":
                        if (!getUUIDs((page+1)).isEmpty())
                        {
                            DeleteMenu menu=new DeleteMenu(p,shopId,(page+1),rarity);
                            menu.loadMenu();
                            menu.open();
                        }
                        break;
                    case "close":
                        p.closeInventory();
                        break;
                    case "items":
                        UUID uuid=uuids.get(e.getRawSlot());
                        if (e.isRightClick()){
                            if (e.getCurrentItem()!=null || !e.getCurrentItem().getType().equals(Material.AIR)) {
                                Shop shop = LegendaryDailyShop.getInstance().getShopManager().getShop(shopId);
                                shop.deleteItem(p, uuid);
                                LegendaryDailyShop.getInstance().getShopManager().saveShop(shop);

                                DeleteMenu menu = new DeleteMenu(p, shopId, page, rarity);
                                menu.loadMenu();
                                menu.open();
                            }
                        }
                        break;
                }


            }
        }
    }
}
