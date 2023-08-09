package com.legendaryrealms.shop.Utils;

import com.legendaryrealms.shop.LegendaryDailyShop;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MsgUtils {


    public static String msg(String msg)
    {
        return tm(msg);
    }

    public static List<String> msg(List<String> msg)
    {
        List<String> lore=new ArrayList<>();
        for (String l:msg)
        {
            lore.add(tm(l));
        }
        return lore;
    }



    public static String tm( String textToColor) {
        if (LegendaryDailyShop.getInstance().high_version) {
            Pattern HEX_PATTERN = Pattern.compile("&#([a-fA-F0-9]{6}|[a-fA-F0-9]{3})");
            Matcher matcher = HEX_PATTERN.matcher(textToColor);
            StringBuffer buffer = new StringBuffer(textToColor.length() + 32);

            while (matcher.find()) {
                String group = matcher.group(1);

                if (group.length() == 6) {
                    matcher.appendReplacement(buffer, ChatColor.COLOR_CHAR + "x"
                            + ChatColor.COLOR_CHAR + group.charAt(0) + ChatColor.COLOR_CHAR + group.charAt(1)
                            + ChatColor.COLOR_CHAR + group.charAt(2) + ChatColor.COLOR_CHAR + group.charAt(3)
                            + ChatColor.COLOR_CHAR + group.charAt(4) + ChatColor.COLOR_CHAR + group.charAt(5));
                } else {
                    matcher.appendReplacement(buffer, ChatColor.COLOR_CHAR + "x"
                            + ChatColor.COLOR_CHAR + group.charAt(0) + ChatColor.COLOR_CHAR + group.charAt(0)
                            + ChatColor.COLOR_CHAR + group.charAt(1) + ChatColor.COLOR_CHAR + group.charAt(1)
                            + ChatColor.COLOR_CHAR + group.charAt(2) + ChatColor.COLOR_CHAR + group.charAt(2));
                }
            }

            return stripSpaceAfterColorCodes(
                    ChatColor.translateAlternateColorCodes('&', matcher.appendTail(buffer).toString()));
        }
        return textToColor.replace("&","§");
    }
    public static String stripSpaceAfterColorCodes( String textToStrip) {

        textToStrip = textToStrip.replaceAll("(" + ChatColor.COLOR_CHAR + ".)[\\s]", "$1");
        return textToStrip;
    }
}
