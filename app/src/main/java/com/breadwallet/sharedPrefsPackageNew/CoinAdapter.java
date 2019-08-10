package com.breadwallet.sharedPrefsPackageNew;


import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;

import com.breadwallet.R;
import com.breadwallet.presenter.customviews.BaseTextView;
import com.breadwallet.ui.wallet.WalletActivity;

import java.util.ArrayList;
import java.util.List;

public class CoinAdapter extends RecyclerView.Adapter<CoinAdapter.CoinViewHolder> {



    ArrayList<CoinModel> list;
    Context context;

    public CoinAdapter(Context context,List<CoinModel> list){
        this.context = context;
        this.list = (ArrayList<CoinModel>) list;
    }

    @NonNull
    @Override
    public CoinViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CoinViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.wallet_list_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull CoinViewHolder holder, final int position) {
        holder.background.setBackgroundColor(list.get(position).colorId);
        Log.d("REC VIEW", list.get(position).name);
        holder.walletName.setText(list.get(position).name);
        //holder.walletCurrencyBalance.setText(list.get(position).balanceCurrency + " " + list.get(position).symbol);
        holder.fiatCurrencyBalance.setText(list.get(position).usdBalance);
        holder.imageCurrency.setImageDrawable(ContextCompat.getDrawable(context,list.get(position).drawableId));
        holder.exchangePrice.setText("$"+list.get(position).usdPrice);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, WalletActivity.class);
                intent.putExtra("SNO","SNO");
                context.startActivity(intent);
            }
        });
//        holder.name.setText(list.get(position).name);
//
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                // temporary to avoid crashes
//                if((list.get(position).name.equals("SAVE NODE")) || (list.get(position).name.equals("BITCOIN")) ||(list.get(position).name.equals("BCH"))){
//                    Intent intent = new Intent(context,WalletActivity.class);
//                    intent.putExtra(Constants.WALLET_NAME_KEY,list.get(position).name);
//                    intent.putExtra(Constants.COlOR_ID,list.get(position).colorId);
//                    intent.putExtra(Constants.BALANCE,list.get(position).balance);
////                    Log.d("BALANCE IN ADAPTER", list.get(position).balance);
//                    intent.putExtra(Constants.ADDRESS,list.get(position).address);
//                    intent.putExtra(Constants.TRANSACTION_OBJECT,list.get(position).walletJsonObject);
//                    context.startActivity(intent);
//                }
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class CoinViewHolder extends RecyclerView.ViewHolder{

        RelativeLayout background;
        ImageView imageCurrency;
        BaseTextView walletName, exchangePrice, fiatCurrencyBalance, walletCurrencyBalance;

        public CoinViewHolder(@NonNull View itemView) {
            super(itemView);
            background = itemView.findViewById(R.id.wallet_card);
            imageCurrency = itemView.findViewById(R.id.currency_icon_white);
            walletName = itemView.findViewById(R.id.wallet_name);
            exchangePrice = itemView.findViewById(R.id.wallet_trade_price);
            fiatCurrencyBalance = itemView.findViewById(R.id.wallet_balance_fiat);
            walletCurrencyBalance = itemView.findViewById(R.id.wallet_balance_currency);
        }
    }
}
