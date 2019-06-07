package com.example.roy.cryptofolio;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.squareup.picasso.Picasso;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class CurrencyAdapter extends RecyclerView.Adapter<CurrencyViewHolder> {

    private final CardClickListener cardClickListener;
    private Context context;
    public List<CryptoCurrency> currencyList;


    public interface CardClickListener {
        void cardOnClick (int i);
    }

    public CurrencyAdapter(Context context, List<CryptoCurrency> currencyList, CardClickListener cardClickListener) {
        this.context = context;
        this.currencyList = currencyList;
        this.cardClickListener = cardClickListener;
    }

    @Override
    public CurrencyViewHolder onCreateViewHolder(ViewGroup parent, int ViewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row, parent, false);
        return new CurrencyViewHolder(view, cardClickListener);
    }

    @Override
    public void onBindViewHolder(final CurrencyViewHolder holder, final int position) {

        ImageView currencyImgView = holder.currencyImgView;

        int rank = currencyList.get(position).getMarketCapRank();
        double price = currencyList.get(position).getCurrentPrice();
        double change = currencyList.get(position).getPriceChangePercentage24h();
        Float mCap = currencyList.get(position).getMarketCap();
        Float circSupply = currencyList.get(position).getCirculatingSupply();
        String name = currencyList.get(position).getName();
        String currencyImg = (currencyList.get(position).getImage());
        String currencySymbol = currencyList.get(position).getSymbol();

        //String rank1 = context.getResources().getString(R.string.rank, rank);



        //format to 2 decimals
        change = (double) Math.round(change * 100)/100;

        //format float to non exponential values
        DecimalFormat decim = new DecimalFormat("###,###.##", new DecimalFormatSymbols());
        String mCapValue = decim.format(mCap);
        String circSupplyValue = decim.format(circSupply);

        String mCapString = context.getResources().getString(R.string.mcap) + mCapValue;
        String circSupplyString = context.getResources().getString(R.string.circ_supply) + circSupplyValue;

        holder.currencyCircSupply.setText(circSupplyString);
        holder.currencyMcap.setText(mCapString);
        holder.currencyRank.setText(String.valueOf(rank));
        holder.currencyName.setText(name);
        holder.currencyPrice.setText("$" + String.valueOf(price));
        holder.currencyChange.setText(String.valueOf(change) + "%");
        holder.currencySymbol.setText(currencySymbol);
        Picasso.get().load(currencyImg).into(currencyImgView);

        if (change > 0){
            holder.currencyChange.setTextColor(ContextCompat.getColor(context, R.color.increaseColor));
        } else if (change < 0) {
            holder.currencyChange.setTextColor(ContextCompat.getColor(context, R.color.decreaseColor));
        }

    }

    @Override
    public int getItemCount(){

        return currencyList.size();
    }
    public void filterList(ArrayList<CryptoCurrency> filteredList){
        currencyList = filteredList;
        notifyDataSetChanged();
    }
    public void swapList (List<CryptoCurrency> newList) {
        if (newList != null) {
            currencyList = newList;

            //Sort the list on marketcaprank
            Collections.sort(currencyList, new Comparator<CryptoCurrency>() {
                @Override
                public int compare(CryptoCurrency self, CryptoCurrency other) {
                    return self.getMarketCapRank().compareTo(other.getMarketCapRank());
                }
            });
            notifyDataSetChanged();
        }
    }
}
