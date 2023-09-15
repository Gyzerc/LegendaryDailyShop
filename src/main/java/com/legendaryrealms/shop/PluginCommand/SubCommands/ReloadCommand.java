package com.legendaryrealms.shop.PluginCommand.SubCommands;

import com.legendaryrealms.shop.LegendaryDailyShop;
import com.legendaryrealms.shop.PluginCommand.ShopCommand;
import com.legendaryrealms.shop.Utils.MsgUtils;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class ReloadCommand extends ShopCommand {
    public ReloadCommand( ) {
        super("reload", "LegendaryDailyShop.admin", 1);
    }

    @Override
    public void reslove(CommandSender sender, String[] args) {
        long mill=System.currentTimeMillis();
        LegendaryDailyShop.getInstance().reload();
        sender.sendMessage(MsgUtils.msg("&7[&3&lLegendary&b&lDailyShop&7] &aReload successful! Time:&6 "+(System.currentTimeMillis()-mill)+"ms"));
    }

    @Override
    public List<String> complete(CommandSender sender, String[] args) {
        return new ArrayList<>();
    }
}
