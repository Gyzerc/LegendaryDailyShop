package com.legendaryrealms.shop;

import com.legendaryrealms.shop.Data.DataProvider;
import com.legendaryrealms.shop.Data.Database.MysqlManager;
import com.legendaryrealms.shop.Data.Database.MysqlStore;
import com.legendaryrealms.shop.Data.Database.YamlStore;
import com.legendaryrealms.shop.Data.Player.PlayerData;
import com.legendaryrealms.shop.Events.JoinEvent;
import com.legendaryrealms.shop.Events.QuitEvent;
import com.legendaryrealms.shop.Manager.*;
import com.legendaryrealms.shop.PluginCommand.PluginCommand;
import com.legendaryrealms.shop.Utils.MsgUtils;
import net.milkbowl.vault.economy.Economy;
import org.black_ixx.playerpoints.PlayerPoints;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class LegendaryDailyShop extends JavaPlugin {

    private DataProvider dataProvider;
    private ConfigManager configManager;
    private ShopRarityManager shopRarityManager;
    private ShopManager shopManager;
    private MenusManager menusManager;
    private Economy eco;
    private PlayerPoints pp;
    private static LegendaryDailyShop legendaryDailyShop;
    public boolean high_version;


    @Override
    public void onEnable() {



        legendaryDailyShop=this;
        high_version=BukkitVersionHigh();

        reload();
        if (!Bukkit.getPluginManager().isPluginEnabled("Vault"))
        {
            Bukkit.getConsoleSender().sendMessage(configManager.plugin+MsgUtils.msg("&4can't find Vault."));
            this.getPluginLoader().disablePlugin(this);
        }
        if (!Bukkit.getPluginManager().isPluginEnabled("PlayerPoints"))
        {
            Bukkit.getConsoleSender().sendMessage(configManager.plugin+ MsgUtils.msg("&4can't find PlayerPoints"));
            this.getPluginLoader().disablePlugin(this);
        }
        setDepend();


        if (configManager.Mysql)
        {
            dataProvider = new MysqlStore();
            Bukkit.getConsoleSender().sendMessage("[LegendaryDailyShop] 正在连接 Mysql 数据库.");
            MysqlManager.setConnectPool();
        }
        else {
            dataProvider = new YamlStore();
            Bukkit.getConsoleSender().sendMessage("[LegendaryDailyShop] 正在使用 Yaml 存储方式.");
        }




        new ListenManager();

        Bukkit.getPluginCommand("LegendaryDailyShop").setExecutor(new PluginCommand());
        Bukkit.getPluginCommand("LegendaryDailyShop").setTabCompleter(new PluginCommand());
        PluginCommand.registerCommands();

        new BukkitRunnable()
        {
            @Override
            public void run() {
                PlayerData.saveAllToLocal();
            }
        }.runTaskTimerAsynchronously(this,20*configManager.auto_save,20*configManager.auto_save);
    }

    public void setDepend()
    {
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        eco = rsp.getProvider();
        pp = PlayerPoints.class.cast(Bukkit.getServer().getPluginManager().getPlugin("PlayerPoints"));

    }

    @Override
    public void onDisable() {
        PlayerData.saveAllToLocal();
    }

    public void reload()
    {
        configManager=new ConfigManager();
        configManager.reload();

        shopRarityManager=new ShopRarityManager();
        shopRarityManager.reload();

        shopManager=new ShopManager();
        shopManager.reload();

        menusManager=new MenusManager();
        menusManager.reload();
    }

    public DataProvider getDataProvider() {return dataProvider;}
    public ConfigManager getConfigManager() {return configManager;}
    public ShopRarityManager getShopRarityManager() {return shopRarityManager;}
    public ShopManager getShopManager(){return shopManager;}
    public MenusManager getMenusManager(){return menusManager;}
    public Economy getEco(){return eco;}
    public PlayerPoints getPlayerPointsAPI(){return pp;}

    public static LegendaryDailyShop getInstance() {return legendaryDailyShop;}
    public boolean BukkitVersionHigh()
    {
        String name=Bukkit.getServer().getClass().getPackage().getName();
        name=name.substring(name.lastIndexOf(".")+1);
        int version=Integer.parseInt(name.substring(name.indexOf("_")+1,name.indexOf("R")-1));
        return version >=13;
    }

    public double getJavaVersion()
    {
       double version=Double.parseDouble(System.getProperty("java.specification.version"));
       return version;
    }
}
