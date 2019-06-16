package com.example.roy.cryptofolio;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;

import static com.example.roy.cryptofolio.MainActivity.ADD_CRYPTO;
import static com.example.roy.cryptofolio.MainActivity.DELETE_CRYPTO;
import static com.example.roy.cryptofolio.MainActivity.db;

public class CurrencyInfo extends AppCompatActivity implements CurrencyDialog.CurrencyDialogListener{
    private TextView currencyDescription;
    private RequestQueue queue;
    private ImageView currencyImgView;
    private TextView currencyMcap;
    private TextView currencyRank;
    private TextView currencyCircSupply;
    private TextView currencyHighLow;
    private TextView currencyAth;
    private TextView currencyTotalVolume;
    private TextView currencyPriceChangeDollar;
    private Button addToPortfolioBtn;
    private Button delFromPortfolioBtn;
    private ProgressBar progressBar;
    private TextView currencyPrice;
    private TextView currencyAmount;
    private View divider;
    private static final int DEFAULT_ZERO = 0;

    public static final int DELETE_CRYPTO = 2;
    public static final int ADD_CRYPTO = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency_info);

        divider = findViewById(R.id.divider);
        currencyDescription = findViewById(R.id.currencyDescription);
        currencyImgView = findViewById(R.id.currencyImgView);
        currencyMcap = findViewById(R.id.currencyMcap);
        currencyRank = findViewById(R.id.currencyRank);
        currencyPrice = findViewById(R.id.currencyPrice);
        currencyCircSupply = findViewById(R.id.circSupply);
        currencyHighLow = findViewById(R.id.high24h);
        currencyTotalVolume = findViewById(R.id.currencyTotalVolume);
        currencyPriceChangeDollar = findViewById(R.id.priceChangeDollar);
        currencyAth = findViewById(R.id.currencyATH);
        currencyAmount = findViewById(R.id.currencyAmount);

        addToPortfolioBtn = findViewById(R.id.addToPortfolioBtn);
        delFromPortfolioBtn = findViewById(R.id.delFromPortfolioBtn);
        progressBar = findViewById(R.id.progressBar);



        //Get intent variables
        String id = getIntent().getStringExtra("id");
        double high24 = getIntent().getDoubleExtra("high24h", DEFAULT_ZERO);
        double price = getIntent().getDoubleExtra("currentPrice", DEFAULT_ZERO);
        float marketCap = getIntent().getFloatExtra("marketCap", DEFAULT_ZERO);
        int marketCapRank = getIntent().getIntExtra("marketCapRank", DEFAULT_ZERO);
        double totalVolume = getIntent().getDoubleExtra("totalVolume", DEFAULT_ZERO);
        double priceChange24h = getIntent().getDoubleExtra("priceChange24H", DEFAULT_ZERO);
        double priceChangePct = getIntent().getDoubleExtra("priceChangePct", DEFAULT_ZERO);
        float circulatingSupply = getIntent().getFloatExtra("circSupply", DEFAULT_ZERO);
        double totalSupply = getIntent().getDoubleExtra("totalSupply", DEFAULT_ZERO);
        double low24 = getIntent().getDoubleExtra("low24h", DEFAULT_ZERO);
        double ath = getIntent().getDoubleExtra("ath", DEFAULT_ZERO);
        double pctFromAth = getIntent().getDoubleExtra("athChangePct", DEFAULT_ZERO);



        //Some values require formatting
        DecimalFormat decim = new DecimalFormat("###,###.##", new DecimalFormatSymbols());
        String mCapValue = decim.format(marketCap);
        String circSupplyValue = decim.format(circulatingSupply);
        String totalSupplyValue = decim.format(totalSupply);
        String totalVolumeValue  = decim.format(totalVolume);

        DecimalFormat max5Digits = new DecimalFormat("#.####");
        String priceChange24hValue = max5Digits.format(priceChange24h);

        DecimalFormat max2Digits = new DecimalFormat("#.##");
        String pctFromAthValue = max2Digits.format(pctFromAth);
        String priceChangePctValue = max2Digits.format(priceChangePct);

        //Set the textviews
        String currencyRankValue = String.format(getResources().getString(R.string.currencyRankStr), marketCapRank);
        currencyRank.setText(currencyRankValue);

        //Displays circ supply compared to the total supply
        String currencySupplyValue = String.format(getResources().getString(R.string.currencySupplyStr), circSupplyValue, totalSupplyValue);
        currencyCircSupply.setText(currencySupplyValue);

        //Price change in percentage and dollar
        String currencyPriceChangeValue = String.format(getResources().getString(R.string.currencyPriceChangeStr), priceChange24hValue, priceChangePctValue);
        currencyPriceChangeDollar.setText(currencyPriceChangeValue);

        String currencyPriceValue = String.format(getResources().getString(R.string.currencyPriceStr), String.valueOf(price));
        currencyPrice.setText(currencyPriceValue);

        String currencyMarketCapValue = String.format(getResources().getString(R.string.currencyMarketCapStr), mCapValue);
        currencyMcap.setText(currencyMarketCapValue);

        //ath and percentage removed from ath
        String currencyAthValue = String.format(getResources().getString(R.string.currencyAthStr), String.valueOf(ath), pctFromAthValue);
        currencyAth.setText(currencyAthValue);

        //24 High and 24 Low
        String currencyHighLowValue = String.format(getResources().getString(R.string.currencyHighLowStr), String.valueOf(low24), String.valueOf(high24));
        currencyHighLow.setText(currencyHighLowValue);

        //Total volume
        String currencyTotalVolumeValue = String.format(getResources().getString(R.string.currencyTotalVolumeStr), totalVolumeValue);
        currencyTotalVolume.setText(currencyTotalVolumeValue);


        //request description and large image
        queue = Volley.newRequestQueue(this);
        jsonParse(id);

        //Checks if the currency is in the portfolio database and sets the add and delete btn accordingly
        //If its in the db we can also request the amount and display it
        for (int i = 0; i < db.cryptoDao().getPortfolio().size(); i++) {
            if (db.cryptoDao().getPortfolio().get(i).getId().equals(id)) {
                addToPortfolioBtn.setVisibility(View.GONE);
                delFromPortfolioBtn.setVisibility(View.VISIBLE);

                String amount = String.format(getResources().getString(R.string.currencyAmountStr), db.cryptoDao().getCurrencyAmount(id));

                currencyAmount.setText(amount);

                currencyAmount.setVisibility(View.VISIBLE);
            }
        }
        //Button opens the amount dialog
        addToPortfolioBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();

            }
        });
        delFromPortfolioBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("name", getIntent().getStringExtra("name"));
                intent.putExtra("id", getIntent().getStringExtra("id"));
                intent.putExtra("marketcap", getIntent().getFloatExtra("marketCap", DEFAULT_ZERO));
                setResult(DELETE_CRYPTO, intent);
                finish();
            }
        });
    }
    private void jsonParse(String cryptoCurrency) {
        String url = ("https://api.coingecko.com/api/v3/coins/"+ cryptoCurrency +"?localization=false&community_data=false&market_data=true&developer_data=false&sparkline=true&tickers=false");
        final List<View> viewsList = new ArrayList<>();

        //Add views to a list to make them visible when the loading is done
        viewsList.add(currencyDescription);
        viewsList.add(currencyImgView);
        viewsList.add(currencyMcap);
        viewsList.add(currencyRank);
        viewsList.add(currencyPrice);
        viewsList.add(currencyCircSupply);
        viewsList.add(currencyHighLow);
        viewsList.add(currencyTotalVolume);
        viewsList.add(currencyPriceChangeDollar);
        viewsList.add(currencyAth);
        viewsList.add(currencyAmount);
        viewsList.add(divider);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //Get the needed Json objects from the response
                            JSONObject jsonDescription = response.getJSONObject("description");
                            JSONObject jsonImage = response.getJSONObject("image");

                            //Get the data from the Json objects
                            String description = jsonDescription.getString("en");
                            String currencyImg = jsonImage.getString("large");

                            currencyDescription.setText(Html.fromHtml(description));
                            Picasso.get().load(currencyImg).into(currencyImgView);

                            progressBar.setVisibility(View.GONE);
                            changeVisibility(viewsList, View.VISIBLE);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        queue.add(request);
    }
    private void changeVisibility(List<View> views, int visibility) {
        for (View view : views) {
            view.setVisibility(visibility);
        }
    }
    public void openDialog() {
        CurrencyDialog dialog = new CurrencyDialog();
        dialog.show(getSupportFragmentManager(), "dialog");
    }
    @Override
    public void addToPortfolio(String amount) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("name", getIntent().getStringExtra("name"));
        intent.putExtra("id", getIntent().getStringExtra("id"));
        intent.putExtra("marketcap", getIntent().getFloatExtra("marketCap", DEFAULT_ZERO));
        intent.putExtra("amount", amount);
        setResult(ADD_CRYPTO, intent);
        finish();
    }

}
