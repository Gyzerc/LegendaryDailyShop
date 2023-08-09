package com.legendaryrealms.shop.PluginCommand;

import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class ShopCommand {
    private String sub;
    private String permission;
    private int length=-1;

    public ShopCommand(String sub, String permission) {
        this.sub = sub;
        this.permission = permission;
    }

    public ShopCommand(String sub, String permission, int length) {
        this.sub = sub;
        this.permission = permission;
        this.length= length;
    }
    public void runCommand(CommandSender sender,String[] args)
    {
        if (length > -1 && length != args.length)
        {
            return;
        }
        if (sender.hasPermission(permission))
        {
            reslove(sender,args);
            return;
        }
        //sender.sendMessage(LangFile.plugin+LangFile.nopermission);

    }
    public boolean checkIsNumber(String arg)
    {
        Pattern pattern = Pattern.compile("[0-9]+[.]{0,1}[0-9]*[dD]{0,1}");
        Matcher isNum = pattern.matcher(arg);
        return isNum.matches();
    }
    public abstract void reslove(CommandSender sender,String[] args);

    public abstract List<String> complete(CommandSender sender, String[] args);

    public String getSub() {
        return sub;
    }

    public String getPermission() {
        return permission;
    }

}
