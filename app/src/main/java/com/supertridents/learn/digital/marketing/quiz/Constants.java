package com.supertridents.learn.digital.marketing.quiz;

import android.content.Context;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

public class Constants {
    public static RewardedAd rewardedAd;

    public static void loadRewardedAd(Context context){
        rewardedAd = new RewardedAd(context,"ca-app-pub-3940256099942544/5224354917");
        rewardedAd.loadAd(new AdRequest.Builder().build(), new RewardedAdLoadCallback(){
            @Override
            public void onRewardedAdFailedToLoad(LoadAdError loadAdError) {
                super.onRewardedAdFailedToLoad(loadAdError);
                loadRewardedAd(context);
            }
        });
    }
}
