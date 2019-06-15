package com.example.roy.cryptofolio;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

@Entity(tableName = "portfolio")
public class PortfolioDbObject implements Parcelable {
    @PrimaryKey
    @NonNull
    private String id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "marketcap")
    private Float marketcap;

    @ColumnInfo(name = "amount")
    private String amount;

    public PortfolioDbObject(String id, String name, Float marketcap, String amount) {
        this.id = id;
        this.name = name;
        this.marketcap = marketcap;
        this.amount = amount;
    }
    protected PortfolioDbObject(Parcel in){
        id = in.readString();
        name = in.readString();
        marketcap = in.readFloat();
        amount = in.readString();
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeFloat(marketcap);
        dest.writeString(amount);
    }
    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PortfolioDbObject> CREATOR = new Creator<PortfolioDbObject>() {
        @Override
        public PortfolioDbObject createFromParcel(Parcel source) {
            return new PortfolioDbObject(source);
        }

        @Override
        public PortfolioDbObject[] newArray(int size) {
            return new PortfolioDbObject[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getMarketcap() {
        return marketcap;
    }

    public void setMarketcap(Float marketcap) {
        this.marketcap = marketcap;
    }

    public String getAmount() {
        return amount;
    }
    public void setAmount(String amount) {
        this.amount = amount;
    }
}
