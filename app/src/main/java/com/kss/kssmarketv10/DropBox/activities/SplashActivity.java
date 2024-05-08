package com.kss.kssmarketv10.DropBox.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.kss.kssmarketv10.DropBox.DbxClient.DropboxClientFactory;
import com.kss.kssmarketv10.DropBox.DbxClient.PicassoClient;


public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = getSharedPreferences("dropbox-sample", MODE_PRIVATE);

        String accessToken = prefs.getString("access-token", null);
        if (accessToken == null) {
            startActivity(new Intent(this, LoginActivity.class));
        } else {
            DropboxClientFactory.init(accessToken);
            PicassoClient.init(getApplicationContext(), DropboxClientFactory.getClient());
            startActivity(new Intent(this, FilesActivity.class));
        }
        finish();
    }
}
