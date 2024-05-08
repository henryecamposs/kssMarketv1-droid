package com.kss.kssutil;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by KSS on 31/3/2017.
 */

public class clsUtil_Errores {

    /**
     * Manejador de Errores con mensajes de fallos de excepciones
     *
     * @param context
     * @param ex
     */
    public static void ManejadorErrores(Context context, Exception ex, Boolean esMostrarMsj, Boolean eskssDialogMsj) {

        ex.printStackTrace();
        Log.e(ex.getStackTrace()[0].getClassName(), ex.getStackTrace()[0].getMethodName() + ": " + ex.toString(), ex);
        if (esMostrarMsj)
            if (!eskssDialogMsj) {
                Toast.makeText(context,
                        ex.getStackTrace()[0].getClassName() + "." +
                                ex.getStackTrace()[0].getMethodName() +
                                "[" + ex.getStackTrace()[0].getLineNumber() + "]" +
                                "\nExcepcion :" +
                                ex.toString(), Toast.LENGTH_LONG).show();
            } else
                ToastManager.show(context,ex.getStackTrace()[0].getClassName() + "." +
                        ex.getStackTrace()[0].getMethodName() +
                        "[" + ex.getStackTrace()[0].getLineNumber() + "]" +
                        "\nExcepcion :" +
                        ex.toString(),enuToastIcons.ERROR, 70);


    }
}
