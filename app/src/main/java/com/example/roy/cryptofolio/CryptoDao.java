package com.example.roy.cryptofolio;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface CryptoDao {

    @Query("SELECT * FROM portfolio")
    public List<PortfolioDbObject> getPortfolio();

    @Insert
    public void insertCurrency (PortfolioDbObject cryptoCurrency);

    //@Delete
    //public void deleteCurrency (PortfolioDbObject cryptoCurrency);

    @Query("DELETE FROM portfolio WHERE id == :id")
    void deleteCurrency(String id);

    @Query("SELECT amount FROM portfolio WHERE id == :id")
    public String getCurrencyAmount(String id);


}
