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

public class CurrencyInfo extends AppCompatActivity{
    private TextView currencyDescription;
    private RequestQueue queue;
    private ImageView currencyImgView;
    private TextView currencyMcap;
    private TextView currencyRank;
    private TextView currencyCircSupply;
    private TextView high24h;
    private TextView currencyATH;
    private TextView currencyTotalVolume;
    private TextView currencyPriceChangeDollar;
    private Button addToPortfolioBtn;
    private Button delFromPortfolioBtn;
    private ProgressBar progressBar;
    private TextView currencyPrice;
    private static final int DEFAULT_ZERO = 0;

    public static final int DELETE_CRYPTO = 1;
    public static final int ADD_CRYPTO = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency_info);


        currencyDescription = findViewById(R.id.currencyDescription);
        currencyImgView = findViewById(R.id.currencyImgView);
        currencyMcap = findViewById(R.id.currencyMcap);
        currencyRank = findViewById(R.id.currencyRank);
        currencyPrice = findViewById(R.id.currencyPrice);
        currencyCircSupply = findViewById(R.id.circSupply);
        high24h = findViewById(R.id.high24h);
        currencyTotalVolume = findViewById(R.id.currencyTotalVolume);
        currencyPriceChangeDollar = findViewById(R.id.priceChangeDollar);
        currencyATH = findViewById(R.id.currencyATH);

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

        //Set the textviews
        currencyRank.setText("Rank: " + String.valueOf(marketCapRank));
        //Displays circ supply compared to the total supply
        currencyCircSupply.setText(getString(R.string.circ_supply) + "\n" + String.valueOf(circSupplyValue) + "/" + String.valueOf(totalSupplyValue));
        //Price change in percentage and dollar
        currencyPriceChangeDollar.setText("Price change: \n" + "$" + priceChange24hValue + " | " + String.valueOf(priceChangePct) + "%");
        currencyPrice.setText("$" + String.valueOf(price));
        currencyMcap.setText("MarketCap: \n" + "$" + String.valueOf(mCapValue));
        //ath and percentage removed from ath
        currencyATH.setText("ATH:  \n$" + String.valueOf(ath) + " | " + pctFromAthValue + "%");
        //24 High and 24 Low
        high24h.setText("24h low / " + "24h high:" + "\n" + "$" + String.valueOf(low24) + " / $" + String.valueOf(high24));
        currencyTotalVolume.setText("Total volume: \n" + totalVolumeValue);


        //request description and large image
        queue = Volley.newRequestQueue(this);
        jsonParse(id);

        for (int i = 0; i < db.cryptoDao().getPortfolio().size(); i++) {
            if (db.cryptoDao().getPortfolio().get(i).getId().equals(id)) {
                addToPortfolioBtn.setVisibility(View.GONE);
                delFromPortfolioBtn.setVisibility(View.VISIBLE);
            }
        }
        //Button sends intent to add values to the database
        addToPortfolioBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("name", getIntent().getStringExtra("name"));
                intent.putExtra("id", getIntent().getStringExtra("id"));
                intent.putExtra("marketcap", getIntent().getFloatExtra("marketCap", DEFAULT_ZERO));
                setResult(ADD_CRYPTO, intent);
                finish();

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
}
