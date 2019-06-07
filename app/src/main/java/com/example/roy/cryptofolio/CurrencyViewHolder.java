package com.example.roy.cryptofolio;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class CurrencyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    private CurrencyAdapter.CardClickListener cardClickListener;

    public TextView currencyRank;
    public TextView currencyName;
    public TextView currencyPrice;
    public TextView currencyChange;
    public TextView currencyMcap;
    public TextView currencyCircSupply;
    public TextView currencySymbol;
    public ImageView currencyImgView;
    public View view;

    public CurrencyViewHolder(View itemView, CurrencyAdapter.CardClickListener cardClickListener) {
        super(itemView);
        currencyRank = itemView.findViewById(R.id.currencyRank);
        currencyName = itemView.findViewById(R.id.currencyName);
        currencyImgView = itemView.findViewById(R.id.currencyImgView);
        currencyPrice = itemView.findViewById(R.id.currencyPrice);
        currencyChange = itemView.findViewById(R.id.currencyChange);
        currencyMcap = itemView.findViewById(R.id.currencyMcap);
        currencyCircSupply = itemView.findViewById(R.id.currencyCircSupply);
        currencySymbol = itemView.findViewById(R.id.currencySymbol);
        view = itemView;

        this.cardClickListener = cardClickListener;
        itemView.setOnClickListener(this);
    }
    @Override
    public void onClick(View view){
        int clickedPosition = getAdapterPosition();
        cardClickListener.cardOnClick(clickedPosition);
    }
}
