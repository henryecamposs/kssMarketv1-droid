package com.kss.kssmarketv10.DropBox.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.dropbox.core.android.Auth;
import com.dropbox.core.v2.users.FullAccount;
import com.kss.kssmarketv10.DropBox.DbxClient.DropboxClientFactory;
import com.kss.kssmarketv10.R;
import com.kss.kssmarketv10.DropBox.asynctasks.GetUserAccountDetailsTask;
import com.kss.kssmarketv10.DropBox.base.DropboxActivity;
import com.kss.kssmarketv10.DropBox.interfaces.AccountDetailCallback;

/**
 * Created by jamwal on 30/12/16.
 */

public class DropboxLoginActivity extends DropboxActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Auth.startOAuth2Authentication(DropboxLoginActivity.this, getString(R.string.dropbox_app_key));
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void loadData() {
        new GetUserAccountDetailsTask(DropboxClientFactory.getClient(), new AccountDetailCallback() {
            @Override
            public void onAccountDetailSuccess(FullAccount account) {
                SharedPreferences prefs = getSharedPreferences("dropbox-sample", MODE_PRIVATE);
                prefs.edit().putString("user-name", account.getName().getDisplayName())
                        .putString("user-email", account.getEmail())
                        .putString("account-type", account.getAccountType().name()).apply();
                setResult(RESULT_OK, new Intent().putExtra("loginType", "DROPBOX"));
                finish();
            }

            @Override
            public void onAccountDetailFailure(Exception e) {
                setResult(RESULT_CANCELED);
                finish();
                Log.e(getClass().getName(), "Failed to get account details.", e);
            }
        });
    }
}
