package com.legendaryrealms.shop.Data.Database;

import com.legendaryrealms.shop.LegendaryDailyShop;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.hibernate.HikariConfigurationUtil;
import org.bukkit.configuration.ConfigurationSection;

import java.sql.*;

public class MysqlManager {

    private static HikariDataSource connectPool;

    public static void setConnectPool()
    {
        HikariConfig hikariConfig=new HikariConfig();
        hikariConfig.setDriverClassName("com.mysql.jdbc.Driver");
        ConfigurationSection section= LegendaryDailyShop.getInstance().getConfigManager().yml.getConfigurationSection("HikariCP");
        hikariConfig.setConnectionTimeout(section.getLong("connectionTimeout"));
        hikariConfig.setMinimumIdle(section.getInt("minimumIdle"));
        hikariConfig.setMaximumPoolSize(section.getInt("maximumPoolSize"));
        section= LegendaryDailyShop.getInstance().getConfigManager().yml.getConfigurationSection("Mysql");
        String url="jdbc:mysql://"+
                section.getString("address")+":"+
                section.getString("port")+"/"+
                section.getString("database")+"?useUnicode=true&characterEncoding=UTF-8&useSSL=false&serverTimezone=Asia/Shanghai";
        hikariConfig.setJdbcUrl(url);
        hikariConfig.setUsername(section.getString("user"));
        hikariConfig.setPassword(section.getString("password"));
        hikariConfig.setAutoCommit(true);
        connectPool=new HikariDataSource(hikariConfig);
        enableMysql();
    }



    public static void enableMysql()
    {
        Connection connection=null;
        Statement statement=null;
        try {
            connection= connectPool.getConnection();
            statement = connection.createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS `LegendaryDailyShop_player` (" +
                    "`player` VARCHAR(100) NOT NULL," +
                    "`data` TEXT DEFAULT NULL," +
                    "PRIMARY KEY (`player`))");
            LegendaryDailyShop.getInstance().getLogger().info("成功与数据库建立连接！");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
                if (statement != null)
                {
                    statement.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }


    }

    public static boolean hasPlayerData(String player) {
        Connection connection=null;
        PreparedStatement preparedStatement=null;
        ResultSet rs=null;
        try {
            connection = connectPool.getConnection();
            preparedStatement = connection.prepareStatement("SELECT `data` FROM `LegendaryDailyShop_player` WHERE player=?;", ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            String res="";
            preparedStatement.setString(1, player);
            rs = preparedStatement.executeQuery();
            if (rs.next()) {
                return true;
            }
            return false;
        } catch (SQLException e) {

            e.printStackTrace();
            return false;
        } finally {
                try {
                    if (connection != null) {
                        connection.close();
                    }
                    if (preparedStatement != null)
                    {
                        preparedStatement.close();
                    }
                    if (rs != null)
                    {
                        rs.close();
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

        }
    }


    public static String getPlayerStringData(String player) {
        Connection connection=null;
        PreparedStatement preparedStatement=null;
        ResultSet rs=null;
        try {
            connection = connectPool.getConnection();
            preparedStatement = connection.prepareStatement("SELECT `data` FROM `LegendaryDailyShop_player` WHERE player=?;",ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            String res="";
            preparedStatement.setString(1, player);
            rs = preparedStatement.executeQuery();
            if (rs.next()) {
                rs.first();
                res = rs.getString("data");
            }
            return res;
        } catch (SQLException e) {
            e.printStackTrace();
            return "";
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
                if (preparedStatement != null)
                {
                    preparedStatement.close();
                }
                if (rs != null)
                {
                    rs.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }
    }

    public static void setPlayerString(String player, String data) {
        Connection connection=null;
        PreparedStatement preparedStatement=null;
        try {
            connection = connectPool.getConnection();
            preparedStatement = connection.prepareStatement("INSERT INTO `LegendaryDailyShop_player` " +
                    "(`player`, `data`)" +
                    "VALUES (?, ?) ON DUPLICATE KEY UPDATE `data`=?;",ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
            preparedStatement.setString(1, player);
            preparedStatement.setString(2, data);
            preparedStatement.setString(3, data);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
                if (preparedStatement != null)
                {
                    preparedStatement.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }
    }


}
