package com.kss.kssmarketv10.DropBox.asynctasks;

import android.os.AsyncTask;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.sharing.SharedLinkMetadata;
import com.kss.kssmarketv10.DropBox.interfaces.SharedLinkCallback;

import java.util.List;

public class SharedLinkTask extends AsyncTask<String, Void, List<SharedLinkMetadata>> {

    private final DbxClientV2 mDbxClient;
    private final SharedLinkCallback mCallback;
    private Exception mException;

    public SharedLinkTask(DbxClientV2 dbxClient, SharedLinkCallback callback) {
        mDbxClient = dbxClient;
        mCallback = callback;
    }

    @Override
    protected void onPostExecute(List<SharedLinkMetadata> result) {
        super.onPostExecute(result);
        if (mException != null) {
            mCallback.onError(mException);
        } else if (result == null) {
            mCallback.onError(null);
        } else {
            mCallback.onSharedLinkGenerated(result);
        }
    }

    @Override
    protected List<SharedLinkMetadata> doInBackground(String... params) {
        try {
            return mDbxClient.sharing().listSharedLinksBuilder()
                    .withPath(params[0]).withDirectOnly(true).start().getLinks();
        } catch (DbxException ex) {
            mException = ex;
        }
        return null;
    }

}
