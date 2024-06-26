package com.kss.kssmarketv10.DropBox;

import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;

/**
 * Created by KSS on 31/3/2017.
 */

public class DropboxClient {
    public static DbxClientV2 getClient(String ACCESS_TOKEN) {
        // Create Dropbox client
        DbxRequestConfig config = new DbxRequestConfig("dropbox/KalixtoMarket", "en_US");
        DbxClientV2 client = new DbxClientV2(config, ACCESS_TOKEN);
        return client;
    }
}
