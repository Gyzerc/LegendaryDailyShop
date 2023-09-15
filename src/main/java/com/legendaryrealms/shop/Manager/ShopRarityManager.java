package com.legendaryrealms.shop.Manager;

import com.legendaryrealms.shop.LegendaryDailyShop;
import com.legendaryrealms.shop.Shop.ShopRarity;
import com.legendaryrealms.shop.Utils.MsgUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ShopRarityManager {
    private static HashMap<String,ShopRarity> cache;
    private YamlConfiguration yml;
    private List<String> rarities;
    public ShopRarityManager()
    {
        File file=new File("./plugins/LegendaryDailyShop","rarities.yml");
        if (!file.exists())
        {
            LegendaryDailyShop.getInstance().saveResource("rarities.yml",false);
            Bukkit.getConsoleSender().sendMessage("[LegendaryDailyShop] 成功生成 rarities.yml");
        }
        cache=new HashMap<>();
        yml=YamlConfiguration.loadConfiguration(file);
        rarities=new ArrayList<>();
    }

    public void reload()
    {
        ConfigurationSection section=yml.getConfigurationSection("rarities");
        if (section != null)
        {
            int a=0;
            for (String id:section.getKeys(false))
            {
                ShopRarity rarity=new ShopRarity(id, MsgUtils.msg(yml.getString("rarities."+id+".display","未知品质")), yml.getDouble("rarities."+id+".chance",0.5),yml.getInt("rarities."+id+".max",5));
                rarities.add(id);
                cache.put(id,rarity);
                a++;
            }

            Bukkit.getConsoleSender().sendMessage("[LegendaryDailyShop] 共加载 "+a+" 个品质");
        }
    }

    public List<String> getRarities() {
        return rarities;
    }
    public ShopRarity getRarity(String id)
    {
        return cache.get(id);
    }
}
