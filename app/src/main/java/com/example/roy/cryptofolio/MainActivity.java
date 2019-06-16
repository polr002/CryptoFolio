package com.example.roy.cryptofolio;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.JsonObject;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Path;

public class MainActivity extends AppCompatActivity implements CurrencyAdapter.CardClickListener, SwipeRefreshLayout.OnRefreshListener {

    private CmcApiService service;
    private RecyclerView recyclerView;
    private List<CryptoCurrency> cryptoCurrency;
    private CurrencyAdapter mAdapter;
    private EditText searchBar;
    private ProgressBar progressBar;
    private ArrayList<CryptoCurrency> portfolio;
    private SwipeRefreshLayout swipeRefreshLayout;

    public static final String CLICKED_CRYPTO = "crypto";

    static CryptoDatabase db;

    public final static int TASK_GET_ALL_CRYPTO = 0;
    public final static int TASK_DELETE_CRYPTO = 1;
    public final static int TASK_INSERT_CRYPTO = 2;

    public static final int BACK_BUTTON = 0;
    public static final int ADD_CRYPTO = 1;
    public static final int DELETE_CRYPTO = 2;

    public static final Double NO_LIMIT = 0.0;
    int position;

    public final static int CURRENCY_INFO = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = CryptoDatabase.getsInstance(this);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_favorites:
                        requestPortfolio();
                        break;
                    case R.id.action_recents:
                        mAdapter.swapList(cryptoCurrency);
                        break;
                }
                return true;
            }
        });



        recyclerView = findViewById(R.id.recyclerView);
        searchBar = findViewById(R.id.searchBar);
        progressBar = findViewById(R.id.progressBar);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);

        cryptoCurrency = new ArrayList<>();

        swipeRefreshLayout.setOnRefreshListener(this);

        RecyclerView.LayoutManager mLayoutManager = new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);


        //initiating service with api call and requesting data
        service = CmcApiService.retrofit.create(CmcApiService.class);

        requestCryptoCurrency(2, 250);
        requestCryptoCurrency(1, 250);


        //Search functionality
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });

    }

    private void filter(String text) {
        ArrayList<CryptoCurrency> filteredList = new ArrayList<>();
        for (CryptoCurrency name : cryptoCurrency) {
            if (name.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(name);
            }
        }
        mAdapter.filterList(filteredList);
    }

    //load items that are in the db into portfolio list and swap new list into adapter
    private void requestPortfolio() {
        portfolio = new ArrayList<>();

        for (int i = 0; i < db.cryptoDao().getPortfolio().size(); i++) {
            String id = db.cryptoDao().getPortfolio().get(i).getId();
            for (int j = 0; j < cryptoCurrency.size(); j++) {
                if (id.equals(cryptoCurrency.get(j).getId())) {
                    portfolio.add(cryptoCurrency.get(j));
                    break;
                }
            }
        }
        mAdapter.swapList(portfolio);
    }


    //API call using Retrofit
    private void requestCryptoCurrency(int page, int perPage) {
        Call<List<CryptoCurrency>> call = service.getCryptoCurrency(perPage, page);

        call.enqueue(new Callback<List<CryptoCurrency>>() {
            @Override
            public void onResponse(Call<List<CryptoCurrency>> call, Response<List<CryptoCurrency>> response) {
                Context context = getApplicationContext();
                String toastText = "Succes!";

                cryptoCurrency.addAll(response.body());

                Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show();
                updateView();
                progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<List<CryptoCurrency>> call, Throwable t) {
                Log.d("error", t.toString());
            }
        });

    }

    public void updateView() {
        if (mAdapter == null) {
            mAdapter = new CurrencyAdapter(getApplicationContext(), cryptoCurrency, this);
            recyclerView.setAdapter(mAdapter);
        } else {

            mAdapter.swapList(cryptoCurrency);
        }

    }

    @Override
    public void cardOnClick(int i) {
        Intent intent = new Intent(MainActivity.this, CurrencyInfo.class);
        position = i;
        //some currencies have unlimited supply, the API returns null in that case so we set them
        //to zero
        if (mAdapter.currencyList.get(i).getTotalSupply()== null) {
            mAdapter.currencyList.get(i).setTotalSupply(NO_LIMIT);
        }

        CryptoCurrency clickedCurrency = mAdapter.currencyList.get(i);
        intent.putExtra("name", clickedCurrency.getName());
        intent.putExtra("id", clickedCurrency.getId());
        intent.putExtra("marketCap", clickedCurrency.getMarketCap());
        intent.putExtra("marketCapRank", clickedCurrency.getMarketCapRank());
        intent.putExtra("currentPrice", clickedCurrency.getCurrentPrice());
        intent.putExtra("totalVolume", clickedCurrency.getTotalVolume());
        intent.putExtra("high24h", clickedCurrency.getHigh24h());
        intent.putExtra("low24h", clickedCurrency.getLow24h());
        intent.putExtra("priceChange24H", clickedCurrency.getPriceChange24h());
        intent.putExtra("priceChangePct", clickedCurrency.getPriceChangePercentage24h());
        intent.putExtra("circSupply", clickedCurrency.getCirculatingSupply());
        intent.putExtra("totalSupply", clickedCurrency.getTotalSupply());
        intent.putExtra("ath", clickedCurrency.getAth());
        intent.putExtra("athChangePct", clickedCurrency.getAthChangePercentage());


        startActivityForResult(intent, CURRENCY_INFO);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == ADD_CRYPTO) {
            String id = data.getStringExtra("id");
            String name = data.getStringExtra("name");
            Float marketcap = data.getFloatExtra("marketcap", 0);
            String amount = data.getStringExtra("amount");
            new CryptoAsyncTask(TASK_INSERT_CRYPTO).execute(new PortfolioDbObject(id, name, marketcap, amount));
        } else if (resultCode == DELETE_CRYPTO) {
            String id = data.getStringExtra("id");
            String name = data.getStringExtra("name");
            Float marketcap = data.getFloatExtra("marketcap", 0);
            String amount = data.getStringExtra("amount");
            new CryptoAsyncTask(TASK_DELETE_CRYPTO).execute(new PortfolioDbObject(id, name, marketcap, amount));
        }
    }

    public class CryptoAsyncTask extends AsyncTask<PortfolioDbObject, Void, List> {

        private int taskCode;

        public CryptoAsyncTask(int taskCode) {
            this.taskCode = taskCode;
        }

        @Override
        protected List doInBackground(PortfolioDbObject... currencies) {
            switch (taskCode) {
                case TASK_DELETE_CRYPTO:
                    db.cryptoDao().deleteCurrency(currencies[0].getId());
                    break;

                case TASK_INSERT_CRYPTO:
                    db.cryptoDao().insertCurrency(currencies[0]);
                    break;
            }

            //To return a new list with the updated data, we get all the data from the database again.
            return db.cryptoDao().getPortfolio();
        }

        @Override
        protected void onPostExecute(List list) {
            super.onPostExecute(list);
            if (taskCode == TASK_DELETE_CRYPTO) {
                requestPortfolio();
            }
        }
    }

    @Override
    public void onRefresh() {
            cryptoCurrency.clear();

            requestCryptoCurrency(2, 250);
            requestCryptoCurrency(1, 250);
            swipeRefreshLayout.setRefreshing(false);

    }

}
