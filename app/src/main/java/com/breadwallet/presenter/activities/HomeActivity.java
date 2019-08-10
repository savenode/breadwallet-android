/**
 * BreadWallet
 * <p/>
 * Created by byfieldj on <jade@breadwallet.com> 1/17/18.
 * Copyright (c) 2019 breadwallet LLC
 * <p/>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p/>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p/>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.breadwallet.presenter.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.breadwallet.R;
import com.breadwallet.model.Wallet;
import com.breadwallet.presenter.activities.intro.WriteDownActivity;
import com.breadwallet.presenter.activities.settings.SettingsActivity;
import com.breadwallet.presenter.activities.util.BRActivity;
import com.breadwallet.presenter.customviews.BRButton;
import com.breadwallet.presenter.customviews.BRNotificationBar;
import com.breadwallet.presenter.customviews.BaseTextView;
import com.breadwallet.presenter.interfaces.BasePromptActions;
import com.breadwallet.presenter.viewmodels.MainViewModel;
import com.breadwallet.sharedPrefsPackageNew.CoinAdapter;
import com.breadwallet.sharedPrefsPackageNew.CoinModel;
import com.breadwallet.sharedPrefsPackageNew.ConstantsNew;
import com.breadwallet.sharedPrefsPackageNew.SharedPrefsNew;
import com.breadwallet.sharedPrefsPackageNew.VolleySingleton;
import com.breadwallet.tools.adapter.WalletListAdapter;
import com.breadwallet.tools.animation.UiUtils;
import com.breadwallet.tools.listeners.RecyclerItemClickListener;
import com.breadwallet.tools.manager.AppEntryPointHandler;
import com.breadwallet.tools.manager.BRSharedPrefs;
import com.breadwallet.tools.manager.InternetManager;
import com.breadwallet.tools.manager.PromptManager;
import com.breadwallet.tools.util.BRConstants;
import com.breadwallet.tools.util.CurrencyUtils;
import com.breadwallet.tools.util.EventUtils;
import com.breadwallet.tools.util.Utils;
import com.breadwallet.ui.wallet.WalletActivity;
import com.breadwallet.wallet.wallets.bitcoin.WalletBitcoinManager;
import com.breadwallet.wallet.wallets.ethereum.WalletTokenManager;
import com.platform.APIClient;
import com.platform.HTTPServer;

import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by byfieldj on 1/17/18.
 * <p>
 * Home activity that will show a list of a user's wallets
 */

public class HomeActivity extends BRActivity implements InternetManager.ConnectionReceiverListener, BasePromptActions {
    private static final String TAG = HomeActivity.class.getSimpleName();
    public static final String EXTRA_DATA = "com.breadwallet.presenter.activities.WalletActivity.EXTRA_DATA";
    public static final int MAX_NUMBER_OF_CHILDREN = 2;

    private RecyclerView mWalletRecycler;
    private WalletListAdapter mAdapter;
    private BaseTextView mFiatTotal;
    private BRNotificationBar mNotificationBar;
    private ConstraintLayout mBuyLayout;
    private LinearLayout mTradeLayout;
    private LinearLayout mMenuLayout;
//    private LinearLayout mListGroupLayout;
    private RelativeLayout mListGroupLayout;
    private MainViewModel mViewModel;


    // Declarations by VAIVAL's developer

    RecyclerView walletListRecView;
    CoinAdapter coinAdapter;
    List<CoinModel> coinModelList;

    BaseTextView walletName,walletTradePrice,walletBalance,walletBalanceUsd,walletSyncLabel;
    RelativeLayout walletBg;
    ProgressBar syncSNO;

    CoinModel model = new CoinModel();

    RelativeLayout savenodeWalletCard;

    private PromptManager.PromptItem mCurrentPrompt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mWalletRecycler = findViewById(R.id.rv_wallet_list);
        mFiatTotal = findViewById(R.id.total_assets_usd);
        mNotificationBar = findViewById(R.id.notification_bar);
        mBuyLayout = findViewById(R.id.buy_layout);
        mTradeLayout = findViewById(R.id.trade_layout);
        mMenuLayout = findViewById(R.id.menu_layout);
//        mListGroupLayout = findViewById(R.id.list_group_layout);
        mListGroupLayout = findViewById(R.id.layoutSNO);




        // worked by VAIVAL's developer

        PromptManager.basePromptActions = this;

        walletBg = findViewById(R.id.icon_background_wallet);
        walletName = findViewById(R.id.wallet_name_sno);
        walletTradePrice = findViewById(R.id.wallet_trade_price_sno);
        walletBalanceUsd = findViewById(R.id.wallet_balance_fiat_sno);
        walletBalance = findViewById(R.id.wallet_balance_currency_sno);
        syncSNO = findViewById(R.id.sync_progress_sno);
        walletSyncLabel = findViewById(R.id.syncing_label_sno);

        savenodeWalletCard = (RelativeLayout) findViewById(R.id.wallet_card);


        SharedPrefsNew.putString(HomeActivity.this, ConstantsNew.BALANCE_SAVE_NODE_WALLET,"0.0000");
        SharedPrefsNew.putString(HomeActivity.this,ConstantsNew.USD_VALUE,"0.00");
        SharedPrefsNew.putString(HomeActivity.this,ConstantsNew.USD_BALANCE,"0.00");



        mListGroupLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(HomeActivity.this,WalletActivity.class);
                intent.putExtra("SNO","SNO");
                startActivity(intent);
            }
        });





        coinModelList = new ArrayList<>();

        walletListRecView = findViewById(R.id.walletList);

        coinAdapter = new CoinAdapter(HomeActivity.this,coinModelList);

        walletListRecView.setAdapter(coinAdapter);

        walletListRecView.setLayoutManager(new LinearLayoutManager(HomeActivity.this,RecyclerView.VERTICAL,false));

        model.name = "SaveNode";
        model.colorId = ContextCompat.getColor(HomeActivity.this,R.color.saveNode);
        //  model.balanceCurrency= getSaveNodeBalanceAmount();
        model.symbol = "SNO";
        model.drawableId = R.drawable.savenodes;
        // model.usdPrice = getBalanceUsd();
        // model.usdBalance = "$"+ Float.parseFloat(model.balanceCurrency) * Float.parseFloat(model.usdPrice);

        model.publicAddress= SharedPrefsNew.getStrings(HomeActivity.this,ConstantsNew.SAVE_NODE_WALLET_ADDRESS);
        model.walletObject = getValueFromWalletResponse();


        createWallets();


        //////////////////// Work ended by VAIVAL's developer //////////////////////////

        mBuyLayout.setOnClickListener(view -> {
            String url = String.format(BRConstants.CURRENCY_PARAMETER_STRING_FORMAT,
                    HTTPServer.getPlatformUrl(HTTPServer.URL_BUY),
                    WalletBitcoinManager.getInstance(HomeActivity.this).getCurrencyCode());
            UiUtils.startWebActivity(HomeActivity.this, url);
        });
        mTradeLayout.setOnClickListener(view -> UiUtils.startWebActivity(HomeActivity.this, HTTPServer.getPlatformUrl(HTTPServer.URL_TRADE)));
        mMenuLayout.setOnClickListener(view -> {
            Intent intent = new Intent(HomeActivity.this, SettingsActivity.class);
            intent.putExtra(SettingsActivity.EXTRA_MODE, SettingsActivity.MODE_SETTINGS);
            startActivity(intent);
            overridePendingTransition(R.anim.enter_from_bottom, R.anim.empty_300);
        });
        mWalletRecycler.setLayoutManager(new LinearLayoutManager(this));
        mWalletRecycler.addOnItemTouchListener(new RecyclerItemClickListener(this, mWalletRecycler, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, float x, float y) {
                if (position >= mAdapter.getItemCount() || position < 0) {
                    return;
                }
                if (mAdapter.getItemViewType(position) == 0) {
                    String currencyCode = mAdapter.getItemAt(position).getCurrencyCode();
                    BRSharedPrefs.putCurrentWalletCurrencyCode(HomeActivity.this, currencyCode);
                    // Use BrdWalletActivity to show rewards view and animation if BRD and not shown yet.
                    if (WalletTokenManager.BRD_CURRENCY_CODE.equalsIgnoreCase(currencyCode)) {
                        if (!BRSharedPrefs.getRewardsAnimationShown(HomeActivity.this)) {
                            Map<String, String> attributes = new HashMap<>();
                            attributes.put(EventUtils.EVENT_ATTRIBUTE_CURRENCY, WalletTokenManager.BRD_CURRENCY_CODE);
                            EventUtils.pushEvent(EventUtils.EVENT_REWARDS_OPEN_WALLET, attributes);
                        }
                        BrdWalletActivity.start(HomeActivity.this, currencyCode);
                    } else {
                        WalletActivity.start(HomeActivity.this, currencyCode);
                    }
                    overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                } else {
                    Intent intent = new Intent(HomeActivity.this, AddWalletsActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
                }
            }

            @Override
            public void onLongItemClick(View view, int position) {
            }
        }));
        processIntentData(getIntent());

        ImageView buyBell = findViewById(R.id.buy_bell);
        boolean isBellNeeded = BRSharedPrefs.getFeatureEnabled(this, APIClient.FeatureFlags.BUY_NOTIFICATION.toString())
                && CurrencyUtils.isBuyNotificationNeeded(this);
        buyBell.setVisibility(isBellNeeded ? View.VISIBLE : View.INVISIBLE);

        mAdapter = new WalletListAdapter(this);
        mWalletRecycler.setAdapter(mAdapter);

        // Get ViewModel, observe updates to Wallet and aggregated balance data
        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mViewModel.getWallets().observe(this, wallets -> mAdapter.setWallets(wallets));

        mViewModel.getAggregatedFiatBalance().observe(this, aggregatedFiatBalance -> {
            if (aggregatedFiatBalance == null) {
                Log.e(TAG, "fiatTotalAmount is null");
                return;
            }
            mFiatTotal.setText(CurrencyUtils.getFormattedAmount(HomeActivity.this,
                    BRSharedPrefs.getPreferredFiatIso(HomeActivity.this), aggregatedFiatBalance));
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        processIntentData(intent);
    }

    private synchronized void processIntentData(Intent intent) {
        String data = intent.getStringExtra(EXTRA_DATA);
        if (Utils.isNullOrEmpty(data)) {
            data = intent.getDataString();
        }
        if (data != null) {
            AppEntryPointHandler.processDeepLink(this, data);
        }
    }

    private void showNextPromptIfNeeded() {
        PromptManager.PromptItem toShow = PromptManager.nextPrompt(this);
        if (toShow != null) {
            View promptView = PromptManager.promptInfo(this, toShow);

            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)savenodeWalletCard.getLayoutParams();
            if (toShow == PromptManager.PromptItem.PAPER_KEY) {
                if (mListGroupLayout.getChildCount() >= MAX_NUMBER_OF_CHILDREN) {
                    mListGroupLayout.removeViewAt(0);
                }
                mListGroupLayout.addView(promptView, 0);

                params.setMargins(16, 525, 16, 8); //substitute parameters for left, top, right, bottom
            }
            else {
                params.setMargins(16, 8, 16, 8); //substitute parameters for left, top, right, bottom
            }
            savenodeWalletCard.setLayoutParams(params);

            EventUtils.pushEvent(EventUtils.EVENT_PROMPT_PREFIX
                    + PromptManager.getPromptName(toShow) + EventUtils.EVENT_PROMPT_SUFFIX_DISPLAYED);
        } else {
            Log.i(TAG, "showNextPrompt: nothing to show");
        }
    }



    @Override
    protected void onResume() {
        super.onResume();
        showNextPromptIfNeeded();
        InternetManager.registerConnectionReceiver(this, this);
        onConnectionChanged(InternetManager.getInstance().isConnected(this));
        mViewModel.refreshWallets();
    }

    @Override
    protected void onPause() {
        super.onPause();
        InternetManager.unregisterConnectionReceiver(this, this);
    }

    @Override
    public void onConnectionChanged(boolean isConnected) {
        Log.d(TAG, "onConnectionChanged: isConnected: " + isConnected);
        if (isConnected) {
            if (mNotificationBar != null) {
                mNotificationBar.setVisibility(View.GONE);
            }
        } else {
            if (mNotificationBar != null) {
                mNotificationBar.setVisibility(View.VISIBLE);
                mNotificationBar.bringToFront();
            }
        }
    }

    public void closeNotificationBar() {
        mNotificationBar.setVisibility(View.INVISIBLE);
    }

    void createWallets(){
        Log.d(TAG, "IN CREATE WALLETS");
//        Log.d(TAG, SharedPrefsNew.getStrings(IntroActivity.this,ConstantsNew.WALLET_CREATED_KEY));
        try{
            if(!SharedPrefsNew.getStrings(HomeActivity.this, ConstantsNew.WALLET_CREATED_KEY).equals(ConstantsNew.CREATED)){
                Log.d(TAG, "IN CREATE WALLETS");
                createWalletSaveNode();
                // SharedPrefsNew.putString(IntroActivity.this,ConstantsNew.WALLET_CREATED_KEY,ConstantsNew.CREATED);
            }else{
                getBalances();
            }
        }catch (NullPointerException e){
            Log.d(TAG, "IN CREATE WALLETS");
            createWalletSaveNode();

        }

    }

    void createWalletSaveNode() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, ConstantsNew.CREATE_SAVE_NODE_WALLET_URL,
            new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "SNO: " + response);
                        SharedPrefsNew.putString(HomeActivity.this, ConstantsNew.SAVE_NODE_WALLET_OBJECT_KEY, response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject dataObject = jsonObject.getJSONObject("data");
                            SharedPrefsNew.putString(HomeActivity.this, ConstantsNew.SAVE_NODE_WALLET_ADDRESS, dataObject.optString("address"));
                            Log.d(TAG, "SAVENODE: "+"Address: " + SharedPrefsNew.getStrings(HomeActivity.this, ConstantsNew.SAVE_NODE_WALLET_ADDRESS));
                            SharedPrefsNew.putString(HomeActivity.this,ConstantsNew.WALLET_CREATED_KEY,ConstantsNew.CREATED);
                            getBalances();

                        } catch (Exception e) {

                        }
//                        startActivity(new Intent(InputPinActivity.this, MainActivity.class));
//                        finish();
                        // createWalletBTC();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, error.toString());
                        Toast.makeText(HomeActivity.this,"Error creating wallet",Toast.LENGTH_SHORT).show();
//                        Log.d(TAG, String.valueOf(error.networkResponse.statusCode));
                    }
                });
        stringRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 8000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 8000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {
                Log.d(TAG, String.valueOf(error.networkResponse));
            }
        });
        VolleySingleton.getInstance(HomeActivity.this).addToRequestQueue(stringRequest);
    }


    void getBalances() {
        getSaveNodebalance();
        // getBCHBalance();
        // getBTCbalance();
    }

    void getSaveNodebalance() {
//        "SPaWzGJgQ6ThNH71GF1V2qTtRdTdxVV1JG"
        StringRequest stringRequest = new StringRequest(Request.Method.GET, ConstantsNew.GET_BALANCE_SAVE_NODE + SharedPrefsNew.getStrings(HomeActivity.this,ConstantsNew.SAVE_NODE_WALLET_ADDRESS),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "SAVE NODE RESPONSE: " + response);

                        SharedPrefsNew.putString(HomeActivity.this, ConstantsNew.SAVE_NODE_WALLET_BALANCE_KEY, response);
                        model.balanceCurrency= getSaveNodeBalanceAmount();
                        walletBalanceUsd.setText(getSaveNodeBalanceAmount());
                        getBalanceUsd();


                        //getBTCbalance();
//                        try{
//                            JSONObject jsonObject = new JSONObject(response);
//                            JSONObject dataObject = jsonObject.getJSONObject("data");
//                            //SharedPrefsNew.putString(HomeActivity.this,ConstantsNew.SAVE_NODE_WALLET_ADDRESS,dataObject.optString("address"));
//                           // Log.d(TAG, "Address: "+ SharedPrefsWallet.getStrings(context,Constants.SAVE_NODE_WALLET_ADDRESS));
//                        } catch (Exception e){
//
//                        }
//                        startActivity(new Intent(InputPinActivity.this,HomeActivity.class));
//                        finish();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, error.toString());
                        Toast.makeText(HomeActivity.this,"error finding balance", Toast.LENGTH_SHORT).show();

//                        Log.d(TAG, String.valueOf(error.networkResponse.statusCode));
                    }
                });
        stringRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        VolleySingleton.getInstance(HomeActivity.this).addToRequestQueue(stringRequest);
    }

    String getSaveNodeBalanceAmount(){
        String balanceObject = SharedPrefsNew.getStrings(HomeActivity.this,ConstantsNew.SAVE_NODE_WALLET_BALANCE_KEY);
        try{
            JSONObject jsonObject = new JSONObject(balanceObject);
            JSONObject data = jsonObject.getJSONObject("data");
            if(data.getString("error").equals("address not found") || data == (null)){
                Toast.makeText(HomeActivity.this,"Save node api error", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "balance save node: 0");
                SharedPrefsNew.putString(HomeActivity.this,ConstantsNew.BALANCE_SAVE_NODE_WALLET,"0.0000");
                return "0.0000";
            }
            JSONObject result = data.optJSONObject("result");
            if(result == null){
                SharedPrefsNew.putString(HomeActivity.this,ConstantsNew.BALANCE_SAVE_NODE_WALLET,"0.0000");
                Log.d(TAG, "BALANCE SAVE NODE: " + "0");
                return "0.0000";
            }else{
                String balance = result.optString("balance");
                SharedPrefsNew.putString(HomeActivity.this,ConstantsNew.BALANCE_SAVE_NODE_WALLET,balance);
                Log.d(TAG, "BALANCE SAVE NODE Result: " + balance);
                return balance;

            }
        }catch (Exception e){
            Log.d(TAG, e.getMessage());
        }
        SharedPrefsNew.putString(HomeActivity.this,ConstantsNew.BALANCE_SAVE_NODE_WALLET,"0.0000");
        return "0";
    }

    String getValueFromWalletResponse(){
        String object = SharedPrefsNew.getStrings(HomeActivity.this,ConstantsNew.SAVE_NODE_WALLET_OBJECT_KEY);
        try{
            JSONObject jsonObject = new JSONObject(object);
            JSONObject data = jsonObject.getJSONObject("data");
            JSONObject secret = data.getJSONObject("secret");
            if(secret != null){
                Log.d(TAG, "SECRET: " + secret.toString());
                /*** secret ko shared prefs m store karwao ***/
                SharedPrefsNew.putString(HomeActivity.this,ConstantsNew.SECRET,secret.toString());
                return secret.toString(); //actual account


                //for testing purpose
                //return "{\"address\":\"7a723ea4e922073af0398be9d47c9ae8477b6d21\",\"crypto\":{\"cipher\":\"aes-128-ctr\",\"ciphertext\":\"f8f55af6ac35cfd8583292a130123f9497b455ffccec990e1c8e49e8f569d848\",\"cipherparams\":{\"iv\":\"03dd0c57839da6f32f7fbd9f164ede4b\"},\"mac\":\"08a43ac0567e5ca230a62371dcef8befaedd57df03d88486403ba50afa252ba8\",\"kdf\":\"pbkdf2\",\"kdfparams\":{\"c\":262144,\"dklen\":32,\"prf\":\"hmac-sha256\",\"salt\":\"39527e6498b5ea3bde7dd7a6921575b51c9857052ecb94989c00568ff90fb0ee\"}},\"id\":\"e24c7d82-6d3b-4bfa-bf6d-615cbeb52c79\",\"version\":3}";
            }else{
                return "";
            }
        }catch (Exception e){
            Log.d(TAG, e.getMessage());
        }
        return  "";
    }

    String getBalanceUsd(){
        final String[] usdArray = {"0"};
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "https://api.coingecko.com/api/v3/coins/savenode/tickers",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "SAVE NODE RESPONSE: " + response);
//                       fakeLayout.setVisibility(View.GONE);
//                       SharedPrefsNew.putString(HomeActivity.this, ConstantsNew.SAVE_NODE_WALLET_BALANCE_KEY, response);
//                       CoinModel model;
//                       model = new CoinModel();
//                       model.name = "SaveNode";
//                       model.colorId = ContextCompat.getColor(HomeActivity.this,R.color.saveNode);
//                       model.balanceCurrency= getSaveNodeBalanceAmount();
//                       model.symbol = "SNO";
//                       // model.publicAddress = "Sca3sbLJV8kJbRceKyTivaPVjiWaq3BgCd";
//                       model.drawableId = R.drawable.savenodes;
//
//                       model.publicAddress= SharedPrefsNew.getStrings(HomeActivity.this,ConstantsNew.SAVE_NODE_WALLET_ADDRESS);
//                       model.walletObject = getValueFromWalletResponse();
//                       coinModelList.add(model);
//                       coinAdapter.notifyDataSetChanged();
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray tickers = jsonObject.getJSONArray("tickers");
                            JSONObject priceObject = tickers.getJSONObject(1);
                            JSONObject convertedLast = priceObject.getJSONObject("converted_last");
                            String usd = convertedLast.getString("usd");
                            usdArray[0] = usd;
                            model.usdPrice= usd;


                            walletTradePrice.setText(usd);


                            model.usdBalance = "$"+ Float.parseFloat(model.balanceCurrency) * Float.parseFloat(model.usdPrice);


                            walletBalanceUsd.setText("$"+ Float.parseFloat(model.balanceCurrency) * Float.parseFloat(model.usdPrice));
                            SharedPrefsNew.putString(HomeActivity.this,ConstantsNew.USD_VALUE,usd);
                            SharedPrefsNew.putString(HomeActivity.this,ConstantsNew.USD_BALANCE,model.usdBalance);

                            //  fakeLayout.setVisibility(View.GONE);
                            //  coinModelList.add(model);
                            syncSNO.setVisibility(View.GONE);
                            walletSyncLabel.setVisibility(View.GONE);
                            coinAdapter.notifyDataSetChanged();
                            Log.d(TAG, "usdBalance: " + model.usdBalance + " usdPrice: " + model.usdPrice);
                            Log.d(TAG, "USD: " + usd);





                        }catch (Exception e){
                            Toast.makeText(HomeActivity.this,"error finding balance", Toast.LENGTH_SHORT).show();
                        }

                        //getBTCbalance();
//                        try{
//                            JSONObject jsonObject = new JSONObject(response);
//                            JSONObject dataObject = jsonObject.getJSONObject("data");
//                            //SharedPrefsNew.putString(HomeActivity.this,ConstantsNew.SAVE_NODE_WALLET_ADDRESS,dataObject.optString("address"));
//                           // Log.d(TAG, "Address: "+ SharedPrefsWallet.getStrings(context,Constants.SAVE_NODE_WALLET_ADDRESS));
//                        } catch (Exception e){
//
//                        }
//                        startActivity(new Intent(InputPinActivity.this,HomeActivity.class));
//                        finish();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                       Log.d(TAG, error.toString());
//                       CoinModel model;
//                       model = new CoinModel();
//                       model.name = "Save Node";
//                       model.colorId = ContextCompat.getColor(HomeActivity.this,R.color.saveNode);
//                       model.balanceCurrency= getSaveNodeBalanceAmount();
//                       model.symbol = "SNO";
//                       // model.publicAddress = "Sca3sbLJV8kJbRceKyTivaPVjiWaq3BgCd";
//                       model.drawableId = R.drawable.savenodes;
//
//                       model.publicAddress= SharedPrefsNew.getStrings(HomeActivity.this,ConstantsNew.SAVE_NODE_WALLET_ADDRESS);
//                       model.walletObject = getValueFromWalletResponse();
//                       coinModelList.add(model);
//                       coinAdapter.notifyDataSetChanged();
//                        Log.d(TAG, String.valueOf(error.networkResponse.statusCode));
                    }
                });
        stringRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        VolleySingleton.getInstance(HomeActivity.this).addToRequestQueue(stringRequest);

        return usdArray[0];
    }

    @Override
    public void didPressContinueButton() {}

    @Override
    public void didPressDismissButton() {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)savenodeWalletCard.getLayoutParams();
        params.setMargins(16, 8, 16, 8); //substitute parameters for left, top, right, bottom
        savenodeWalletCard.setLayoutParams(params);
    }
}
