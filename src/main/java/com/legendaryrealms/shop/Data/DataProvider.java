package com.legendaryrealms.shop.Data;

import com.legendaryrealms.shop.Data.Player.PlayerData;

public abstract class DataProvider {

   public abstract void saveData(PlayerData data);
   public abstract PlayerData loadData(String player);
   public abstract boolean isExist(String player);
}
