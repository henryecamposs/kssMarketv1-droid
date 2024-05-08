package com.kss.kssmarketv10.DropBox.interfaces;

/**
 * Created by jamwal on 20/01/17.
 */

public interface CreateSharedLinkCallback {
    void onSharedLinkCreated(String result);

    void onError(Exception e);
}
