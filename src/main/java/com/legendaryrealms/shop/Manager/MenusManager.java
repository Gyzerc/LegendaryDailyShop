package com.legendaryrealms.shop.Manager;

import com.legendaryrealms.shop.Menu.MenuConfiguration;

import java.util.HashMap;

public class MenusManager {

    private static HashMap<String, MenuConfiguration> cache;
    public MenusManager()
    {
        cache=new HashMap<>();
    }

    public MenuConfiguration getMenu(String menu)
    {
        return cache.get(menu);
    }

    public void reload()
    {
        //MenuConfiguration menuConfiguration =new MenuConfiguration("ShopMenu");
        //cache.put("ShopMenu", menuConfiguration);

        MenuConfiguration menuConfiguration = new MenuConfiguration("DeleteMenu");
        cache.put("DeleteMenu",menuConfiguration);
        menuConfiguration = new MenuConfiguration("ShopMenu");
        cache.put("ShopMenu",menuConfiguration);
    }
}
