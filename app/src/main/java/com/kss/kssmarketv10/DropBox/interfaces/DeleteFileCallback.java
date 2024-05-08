package com.kss.kssmarketv10.DropBox.interfaces;

/**
 * Created by jamwal on 09/01/17.
 */

public interface DeleteFileCallback {

    void onDeleteComplete();

    void onError(Exception e);
}
