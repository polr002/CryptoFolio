package com.example.roy.cryptofolio;


import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import android.os.Parcelable;
import android.os.Parcel;



public class CryptoCurrency implements Parcelable{


    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("symbol")
    @Expose
    private String symbol;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("current_price")
    @Expose
    private Double currentPrice;
    @SerializedName("market_cap")
    @Expose
    private Float marketCap;
    @SerializedName("market_cap_rank")
    @Expose
    private Integer marketCapRank;
    @SerializedName("total_volume")
    @Expose
    private Double totalVolume;
    @SerializedName("high_24h")
    @Expose
    private Double high24h;
    @SerializedName("low_24h")
    @Expose
    private Double low24h;
    @SerializedName("price_change_24h")
    @Expose
    private Double priceChange24h;
    @SerializedName("price_change_percentage_24h")
    @Expose
    private Double priceChangePercentage24h;
    @SerializedName("market_cap_change_24h")
    @Expose
    private Double marketCapChange24h;
    @SerializedName("market_cap_change_percentage_24h")
    @Expose
    private Double marketCapChangePercentage24h;
    @SerializedName("circulating_supply")
    @Expose
    private Float circulatingSupply;
    @SerializedName("total_supply")
    @Expose
    private Double totalSupply;
    @SerializedName("ath")
    @Expose
    private Double ath;
    @SerializedName("ath_change_percentage")
    @Expose
    private Double athChangePercentage;
    @SerializedName("ath_date")
    @Expose
    private String athDate;
    @SerializedName("roi")
    @Expose
    private Object roi;
    @SerializedName("last_updated")
    @Expose
    private String lastUpdated;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Double getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(Double currentPrice) {
        this.currentPrice = currentPrice;
    }

    public Float getMarketCap() {
        return marketCap;
    }

    public void setMarketCap(Float marketCap) {
        this.marketCap = marketCap;
    }

    public Integer getMarketCapRank() {
        return marketCapRank;
    }

    public void setMarketCapRank(Integer marketCapRank) {
        this.marketCapRank = marketCapRank;
    }

    public Double getTotalVolume() {
        return totalVolume;
    }

    public void setTotalVolume(Double totalVolume) {
        this.totalVolume = totalVolume;
    }

    public Double getHigh24h() {
        return high24h;
    }

    public void setHigh24h(Double high24h) {
        this.high24h = high24h;
    }

    public Double getLow24h() {
        return low24h;
    }

    public void setLow24h(Double low24h) {
        this.low24h = low24h;
    }

    public Double getPriceChange24h() {
        return priceChange24h;
    }

    public void setPriceChange24h(Double priceChange24h) {
        this.priceChange24h = priceChange24h;
    }

    public Double getPriceChangePercentage24h() {
        return priceChangePercentage24h;
    }

    public void setPriceChangePercentage24h(Double priceChangePercentage24h) {
        this.priceChangePercentage24h = priceChangePercentage24h;
    }

    public Double getMarketCapChange24h() {
        return marketCapChange24h;
    }

    public void setMarketCapChange24h(Double marketCapChange24h) {
        this.marketCapChange24h = marketCapChange24h;
    }

    public Double getMarketCapChangePercentage24h() {
        return marketCapChangePercentage24h;
    }

    public void setMarketCapChangePercentage24h(Double marketCapChangePercentage24h) {
        this.marketCapChangePercentage24h = marketCapChangePercentage24h;
    }

    public Float getCirculatingSupply() {
        return circulatingSupply;
    }

    public void setCirculatingSupply(Float circulatingSupply) {
        this.circulatingSupply = circulatingSupply;
    }

    public Double getTotalSupply() {
        return totalSupply;
    }

    public void setTotalSupply(Double totalSupply) {
        this.totalSupply = totalSupply;
    }

    public Double getAth() {
        return ath;
    }

    public void setAth(Double ath) {
        this.ath = ath;
    }

    public Double getAthChangePercentage() {
        return athChangePercentage;
    }

    public void setAthChangePercentage(Double athChangePercentage) {
        this.athChangePercentage = athChangePercentage;
    }

    public String getAthDate() {
        return athDate;
    }

    public void setAthDate(String athDate) {
        this.athDate = athDate;
    }

    public Object getRoi() {
        return roi;
    }

    public void setRoi(Object roi) {
        this.roi = roi;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }


    protected CryptoCurrency(Parcel in) {
        id = in.readString();
        marketCap = in.readFloat();
        marketCapRank = in.readInt();
        currentPrice = in.readDouble();
        totalVolume = in.readDouble();
        priceChange24h = in.readDouble();
        priceChangePercentage24h = in.readDouble();
        circulatingSupply = in.readFloat();
        totalSupply = in.readDouble();
        high24h = in.readDouble();
        low24h = in.readDouble();
        ath = in.readDouble();
        athChangePercentage = in.readDouble();


    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeFloat(marketCap);
        dest.writeInt(marketCapRank);
        dest.writeDouble(currentPrice);
        dest.writeDouble(totalVolume);
        dest.writeDouble(priceChange24h);
        dest.writeDouble(priceChangePercentage24h);
        dest.writeDouble(circulatingSupply);
        dest.writeDouble(totalSupply);
        dest.writeString(high24h.toString());
        dest.writeDouble(low24h);
        dest.writeDouble(ath);
        dest.writeDouble(athChangePercentage);


    }

    @Override
    public int describeContents() {
        return hashCode();
    }

    public static final Creator<CryptoCurrency> CREATOR = new Creator<CryptoCurrency>() {
        @Override
        public CryptoCurrency createFromParcel(Parcel in) {
            return new CryptoCurrency(in);
        }

        @Override
        public CryptoCurrency[] newArray(int size) {
            return new CryptoCurrency[size];
        }
    };

}