package com.legendaryrealms.shop.Manager;

import com.legendaryrealms.shop.LegendaryDailyShop;
import com.legendaryrealms.shop.Utils.MsgUtils;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.List;

public class ConfigManager {

    public boolean Mysql;
    public YamlConfiguration yml;
    public String nopermission;
    public List<String> command_player;
    public List<String> command_op;
    public String noshop;
    public String norarity;
    public String norarityInShop;
    public String notnumber;
    public String itemnull;
    public String delete;
    public String add;
    public String plugin;
    public String unknown_currency;
    public String unknown_type;
    public int times;
    public String max;
    public String vault_notenough;
    public String playerpoints_notenough;
    public String vault_buy;
    public String playerpoints_buy;
    public String item_sell_vault;
    public String item_sell_playerpoints;
    public String item_notenough;
    public String random;
    public String random_cant;
    public String points_give;
    public String points_recive;
    public String random_admin;
    public int auto_save;
    public String noplayer;
    public String notonline;

    public ConfigManager()
    {
        File file=new File("./plugins/LegendaryDailyShop","config.yml");
        if (!file.exists())
        {
            LegendaryDailyShop.getInstance().saveResource("config.yml",false);
            LegendaryDailyShop.getInstance().getLogger().fine("生成 config.yml 配置文件");
        }
        yml=YamlConfiguration.loadConfiguration(file);
    }

    public void reload()
    {
        Mysql = yml.getString("Store","Yaml").equals("Mysql");
        auto_save = yml.getInt("settings.auto-save",600);
        command_player = MsgUtils.msg(yml.getStringList("message.command_player"));
        command_op = MsgUtils.msg(yml.getStringList("message.command_op"));
        nopermission = MsgUtils.msg(yml.getString("message.nopermission","&c你没有该权限."));
        noshop = MsgUtils.msg(yml.getString("message.noshop","&c该商店不存在！"));
        norarity = MsgUtils.msg(yml.getString("message.norarity","&c该品质ID不存在！"));
        norarityInShop = MsgUtils.msg(yml.getString("message.norarityInShop","&c该商店配置内没有添加该品质ID，请打开 &eShop/%shop% &c在其 &erarities &c下添加该品质ID"));
        notnumber= MsgUtils.msg(yml.getString("message.notnumber","&c请输入一个正确的数字!"));
        itemnull= MsgUtils.msg(yml.getString("message.itemnull","&c请手持一个需要上架的物品！"));
        add= MsgUtils.msg(yml.getString("message.add","&a成功添加 &f%item% &a到商店 &f%shop% &a的 %rarity% &a随机池内."));
        delete=MsgUtils.msg(yml.getString("message.delete","&c成功将 &f%item% &c从商店 &f%shop% &a的 %ratity% &c奖池中删去"));
        plugin = MsgUtils.msg(yml.getString("message.plugin","&7[&3&lLegendary&b&lDailyShop&7] "));
        times = yml.getInt("settings.random.times",600);
        unknown_currency = MsgUtils.msg(yml.getString("message.unknown_currency","&c未知的货币类型.请输入 &evault / playerpoints"));
        unknown_type = MsgUtils.msg(yml.getString("message.unknown_type","&c未知的商品类型.请输入 &ebuy（购买） / sell（收购）"));
        max = MsgUtils.msg(yml.getString("message.max","&c该商品已经不能再购买或者出售了！"));
        vault_notenough = MsgUtils.msg(yml.getString("message.vault_notenough","&c你的游戏币不足 &e%price%"));
        playerpoints_notenough = MsgUtils.msg(yml.getString("message.playerpoints_notenough","&c你的点券不足 &e%price%"));
        vault_buy = MsgUtils.msg(yml.getString("message.vault_buy","&e你花费了 &a%price% &e游戏币购买了 &f%item% &7×%amount%"));
        playerpoints_buy = MsgUtils.msg(yml.getString("message.playerpoints_buy","&e你花费了 &a%price% &e点券购买了 &f%item% &7×%amount%"));
        item_sell_vault = MsgUtils.msg(yml.getString("message.item_sell_vault","&e你出售了 &f%item% &7×%amount% &e获得了 &a%price% &e游戏币"));
        item_sell_playerpoints = MsgUtils.msg(yml.getString("message.item_sell_playerpoints","&e你出售了 &f%item% &7×%amount% &e获得了 &a%price% &e点券"));
        item_notenough = MsgUtils.msg(yml.getString("message.item_notenough","&c你缺少物品 &f%item% &7×%amount%"));
        random = MsgUtils.msg(yml.getString("message.random","&e成功随机该商店，目前该商店还剩余 &a%amount% &e次随机点"));
        random_cant = MsgUtils.msg(yml.getString("message.random_cant","&c该商店且缺少随机点数！"));
        points_give = MsgUtils.msg(yml.getString("message.points_give","&e你给与玩家 &f%player% &a%amount%点 &f%shop% &e的随机点数"));
        points_recive = MsgUtils.msg(yml.getString("message.points_recive","&a你获得了 &e%amount%点 %shop% &a的随机点数"));
        random_admin = MsgUtils.msg(yml.getString("message.random_admin","&e你将玩家&f %player% &e的 &a%shop% &e商品刷新了"));
        noplayer = MsgUtils.msg(yml.getString("message.noplayer","&c该玩家不存在！"));
        notonline = MsgUtils.msg(yml.getString("message.notonline","&c该玩家不在你当前的区服或者不在线！"));

    }



}
