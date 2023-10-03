package com.legendaryrealms.shop.PluginCommand;

import com.legendaryrealms.shop.LegendaryDailyShop;
import com.legendaryrealms.shop.Manager.ConfigManager;
import com.legendaryrealms.shop.Menu.MenuUI.ShopMenu;
import com.legendaryrealms.shop.PluginCommand.SubCommands.*;
import com.legendaryrealms.shop.Utils.RandomUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PluginCommand implements CommandExecutor, TabCompleter {

    public static HashMap<String,ShopCommand> commands=new HashMap<>();
    public static void registerCommands()
    {

        commands.put("open",new OpenCommand());
        commands.put("add",new AddCommand());
        commands.put("reload",new ReloadCommand());
        commands.put("delete",new DeleteCommand());
        commands.put("random",new RandomCommand());
        commands.put("points",new PointsCommand());
    }

    @Override
    public boolean onCommand( CommandSender sender,  Command command,  String s,  String[] args) {
        if (args.length==0)
        {

            for (String msg: LegendaryDailyShop.getInstance().getConfigManager().command_player)
            {
                sender.sendMessage(msg);
            }
            if (sender.isOp())
            {
                for (String msg: LegendaryDailyShop.getInstance().getConfigManager().command_op)
                {
                    sender.sendMessage(msg);
                }
            }
            return true;
        }
        else {
            String sub=args[0];
            ShopCommand shopCommand=commands.get(sub);
            if (shopCommand!=null)
            {
                shopCommand.runCommand(sender,args);
            }
        }
        return true;
    }

    @Override
    public  List<String> onTabComplete( CommandSender commandSender,  Command command,  String s,  String[] args) {
        List<String> list=new ArrayList<>();

        if (args.length==1)
        {
            for (String name:commands.keySet())
            {
                list.add(name);
            }
            return list;
        }
        else if (args.length > 1){
            String sub=args[0];
            ShopCommand shopCommand=commands.get(sub);
            return shopCommand!=null ? shopCommand.complete(commandSender,args) : new ArrayList<>();
        }
        return new ArrayList<>();
    }
}
