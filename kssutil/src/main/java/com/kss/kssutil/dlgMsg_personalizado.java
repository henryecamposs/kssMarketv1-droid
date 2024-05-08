package com.kss.kssutil;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;


/**
 * Created by HENRY on 12/02/2015.
 */
public class dlgMsg_personalizado {
    Context context;

    public  dlgMsg_personalizado(Context context) {
        this.context = context;
    }

    public void crearDialog(String Mensaje, String Titulo) {
        new AlertDialog.Builder(this.context)
                .setTitle(Titulo.equals("")? this.context.getString(R.string.kssHaOcurridoError): Titulo)
                .setMessage(Mensaje)
                .setIcon(R.drawable.ic_launcher)
                        //	.setIcon(R.drawable.ICONO) //anadir icono
                .setPositiveButton(this.context.getString(R.string.kssAceptar), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("AlertDialog", "Aceptar");
                        return; //cierra el dialogo. Siempre puedes hacerle hacer algo.
                    }
                })
                .show();
    }
}
