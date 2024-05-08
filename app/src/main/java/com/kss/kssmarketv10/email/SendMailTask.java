package com.kss.kssmarketv10.email;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.kss.kssutil.ToastManager;
import com.kss.kssutil.enuToastIcons;

import javax.mail.AuthenticationFailedException;
import javax.mail.MessagingException;

public class SendMailTask extends AsyncTask {

    private ProgressDialog statusDialog;
    private Activity sendMailActivity;
    public Boolean esEmailEnviado;

    public SendMailTask(Activity activity) {
        sendMailActivity = activity;
        esEmailEnviado = false;
    }

    protected void onPreExecute() {
        statusDialog = new ProgressDialog(sendMailActivity);
        statusDialog.setMessage("Iniciando envio...");
        statusDialog.setIndeterminate(false);
        statusDialog.setCancelable(false);
        statusDialog.show();
    }

    @Override
    protected Boolean doInBackground(Object... args) {
        EMail eMail = (EMail) args[0];

        try {
            publishProgress("Enviando Email!");
            Thread.sleep(1000);
            if (esEmailEnviado = eMail.send()) {
                publishProgress("Email Enviado!!.");
                Thread.sleep(5000);
                Log.i("SendMailTask", "Mail Sent.");
                return esEmailEnviado;
            } else
                return esEmailEnviado;
        } catch (AuthenticationFailedException e) {
            try {
                publishProgress("Error de autenticación.\n" + e.getMessage());
                Thread.sleep(1000);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            Log.e(SendMailTask.class.getName(), "Error de autenticación.");
            e.printStackTrace();
            return esEmailEnviado;
        } catch (MessagingException e) {
            try {
                publishProgress("Error de Mensaje.\n" + e.getMessage());
                Thread.sleep(1000);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
            return esEmailEnviado;
        } catch (Exception e) {
            try {
                publishProgress("Mensaje no Enviado.\n" + e.getMessage());
                Thread.sleep(1000);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            Log.e("SendMailTask", e.getMessage(), e);
        } finally {
            Thread.currentThread().interrupt();
        }
        return esEmailEnviado;
    }

    @Override
    public void onProgressUpdate(Object... values) {
        statusDialog.setMessage(values[0].toString());
    }

    @Override
    public void onPostExecute(Object result) {
        try {
            if (esEmailEnviado)
                ToastManager.show(sendMailActivity, "Email Enviado", enuToastIcons.OK, 50);
            else
                ToastManager.show(sendMailActivity, "Email NO Enviado", enuToastIcons.ERROR, 50);
            Thread.sleep(1000);
            statusDialog.dismiss();
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }

    }

}
