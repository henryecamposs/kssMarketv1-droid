package com.kss.kssmarketv10.DropBox;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.WriteMode;
import com.kss.kssutil.ToastManager;
import com.kss.kssutil.enuToastIcons;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by KSS on 1/4/2017.
 */

public class UploadTask extends AsyncTask {
    private DbxClientV2 dbxClient;
    private File file;
    private Context context;

    private ProgressDialog statusDialog;
    private Activity sendMailActivity;
    private Boolean esArchivoEnviado;
    private String DirectorioEmpresa;

    UploadTask(DbxClientV2 dbxClient, File file, Activity activity, String DirectorioEmpresa) {
        this.dbxClient = dbxClient;
        this.file = file;
        this.context = activity.getBaseContext();
        sendMailActivity= activity;
        this.DirectorioEmpresa= DirectorioEmpresa;
        if(this.DirectorioEmpresa.trim().length()==0)
            this.DirectorioEmpresa="/";
    }

    protected void onPreExecute() {
        statusDialog = new ProgressDialog(sendMailActivity);
        statusDialog.setMessage("Iniciando envio...");
        statusDialog.setIndeterminate(false);
        statusDialog.setCancelable(true);
         statusDialog.show();
    }

    @Override
    public void onProgressUpdate(Object... values) {
        statusDialog.setMessage(values[0].toString());
    }
    @Override
    protected Object doInBackground(Object[] params) {
        try {
            // Upload to Dropbox
            publishProgress("Espere un momento...." +
                    "\nEsta operación puede tardar tiempo según su conexión a internet" +
                    "\nRespaldando Archivos en DropBox");
            InputStream inputStream = new FileInputStream(file);
            dbxClient.files().uploadBuilder(DirectorioEmpresa + file.getName()) //Path in the user's Dropbox to save the file.
                    .withMode(WriteMode.OVERWRITE) //always overwrite existing file
                    .uploadAndFinish(inputStream);
            Log.d("Upload Status", "Success");
            publishProgress("Respaldo Satisfactorio!");
            Thread.sleep(2000);
            esArchivoEnviado=true;
        } catch (DbxException e) {
            e.printStackTrace();
            publishProgress("Excepcion DropBox:\n" + e.getMessage());
            esArchivoEnviado=false;
        } catch (IOException e) {
            e.printStackTrace();
            publishProgress("Excepcion:\n" + e.getMessage());
            esArchivoEnviado=false;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            Thread.currentThread().interrupt();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
            if (esArchivoEnviado)
                ToastManager.show(sendMailActivity, "Respaldo enviado.", enuToastIcons.OK, 50);
            else
                ToastManager.show(sendMailActivity, "Respaldo No Enviado", enuToastIcons.ERROR, 50);
            statusDialog.dismiss();
    }
}
