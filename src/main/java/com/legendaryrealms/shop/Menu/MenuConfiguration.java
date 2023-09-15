package com.legendaryrealms.shop.Menu;

import com.legendaryrealms.shop.LegendaryDailyShop;
import com.legendaryrealms.shop.Utils.MsgUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MenuConfiguration {
    private String menu;
    private HashMap<String, ItemStack> customItem_item;
    private HashMap<String, List<Integer>> customItem_slot;
    private HashMap<String, String> fuction;
    private HashMap<String, String> placeholder;
    private ItemStack empty;
    private ItemStack unknown;
    private List<Integer> layout;
    private List<String> keys;
    private List<String> lore_format;
    private int size;
    private String title;

    public MenuConfiguration(String menu) {
        this.menu = menu;
        File file=new File("./plugins/LegendaryDailyShop/Menus",menu+".yml");
        if (!file.exists())
        {
            LegendaryDailyShop.getInstance().saveResource("Menus/"+menu+".yml",false);
            Bukkit.getConsoleSender().sendMessage(LegendaryDailyShop.getInstance().getConfigManager().plugin+"成功生成 "+menu+".yml");
        }
        if (layout == null) {
            YamlConfiguration yml = YamlConfiguration.loadConfiguration(file);
            keys = new ArrayList<>();
            layout = yml.getIntegerList("layout");
            title = MsgUtils.msg(yml.getString("title"," "));
            size = yml.getInt("size",54);
            customItem_item = new HashMap<>();
            customItem_slot = new HashMap<>();
            fuction = new HashMap<>();
            placeholder = new HashMap<>();
            ConfigurationSection sec=yml.getConfigurationSection("customItems");
            if (sec != null)
            {
                for (String key:sec.getKeys(false))
                {
                    ItemStack i=new ItemStack(getMaterial(yml.getString("customItems."+key+".material","STONE")),yml.getInt("customItems."+key+".amount",1),(short)yml.getInt("customItems."+key+".data",0));
                    ItemMeta id=i.getItemMeta();
                    id.setDisplayName(MsgUtils.msg(yml.getString("customItems."+key+".display","&f ")));
                    id.setLore(MsgUtils.msg(yml.getStringList("customItems."+key+".lore")));
                    if (LegendaryDailyShop.getInstance().high_version){
                        id.setCustomModelData(yml.getInt("customItems."+key+".model",0));
                    }
                    i.setItemMeta(id);
                    customItem_item.put(key,i);
                    customItem_slot.put(key,yml.getIntegerList("customItems."+key+".slot"));
                    fuction.put(key,yml.getString("customItems."+key+".fuction.type","null"));
                    keys.add(key);

                }
            }
            if (yml.contains("empty"))
            {
                String key="empty";
                ItemStack i=new ItemStack(getMaterial(yml.getString("empty.material","STONE")),yml.getInt("empty.amount",1),(short)yml.getInt("empty.data",0));
                ItemMeta id=i.getItemMeta();
                id.setDisplayName(MsgUtils.msg(yml.getString("empty.display","&f ")));
                id.setLore(MsgUtils.msg(yml.getStringList("empty.lore")));
                if (LegendaryDailyShop.getInstance().high_version){
                    id.setCustomModelData(yml.getInt("empty.model",0));
                }
                i.setItemMeta(id);
                empty=i;
            }
            if (yml.contains("unknown"))
            {
                String key="unknown";
                ItemStack i=new ItemStack(getMaterial(yml.getString("unknown.material","STONE")),yml.getInt("unknown.amount",1),(short)yml.getInt("unknown.data",0));
                ItemMeta id=i.getItemMeta();
                id.setDisplayName(MsgUtils.msg(yml.getString("unknown.display","&f ")));
                id.setLore(MsgUtils.msg(yml.getStringList("unknown.lore")));
                if (LegendaryDailyShop.getInstance().high_version){
                    id.setCustomModelData(yml.getInt("unknown.model",0));
                }
                i.setItemMeta(id);
                unknown=i;
            }
            if (yml.contains("placeholder")){
                for (String placeholder:yml.getConfigurationSection("placeholder").getKeys(false))
                {
                    this.placeholder.put(placeholder,MsgUtils.msg(yml.getString("placeholder."+placeholder," ")));
                }
            }
            if (yml.contains("items_lore"))
            {
                lore_format=MsgUtils.msg(yml.getStringList("items_lore"));
            }
            Bukkit.getConsoleSender().sendMessage(LegendaryDailyShop.getInstance().getConfigManager().plugin+"成功读取 "+menu+".yml");
        }
    }

    public List<String> getLore_format() {
        return lore_format;
    }

    public ItemStack getUnknown() {
        return unknown;
    }

    public ItemStack getEmpty() {
        return empty;
    }

    public String getPlaceholder(String placeholder)
    {
        return this.placeholder.get(placeholder) != null ? this.placeholder.get(placeholder) : " ";
    }
    public int getSize() {
        return size;
    }

    public String getTitle() {
        return title;
    }

    public List<String> getKeys()
    {
        return keys;
    }

    public ItemStack getCustomItem(String id)
    {
        return customItem_item.get(id);
    }

    public List<Integer> getCustomItem_Slot(String id)
    {
        return customItem_slot.get(id);
    }

    public String getFuction(String id)
    {
        return fuction.get(id) !=null ? fuction.get(id) : "null";
    }
    public Material getMaterial(String id)
    {
        return Material.getMaterial(id) != null ? Material.getMaterial(id) : Material.STONE;
    }

    public List<Integer> getLayout() {
        return layout;
    }
}
