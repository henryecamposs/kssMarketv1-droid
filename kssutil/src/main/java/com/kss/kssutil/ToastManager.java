package com.kss.kssutil;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;



/**
 * Created by HENRY on 17/02/2015.
 * Enviar un Mostrar un Toast mensaje en un tiempo determinado
 *
 */
public class ToastManager {




    /**
     * Funcion para mostrar Toast en pantalla.
     * @param context Contexto de la aplicacion
     * @param msj mensaje de toast
     * @param tipoMSJ tipo de mensaje
     * @param tiempoMSJ duraci�n del mensaje
     */
    public static void show(Context context, String msj,
                            enuToastIcons tipoMSJ, int tiempoMSJ) {

        LayoutInflater inflater =
                (LayoutInflater) context.getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);

        View layout = inflater.inflate(R.layout.toast_layout, null);

        TextView tv = (TextView) layout.findViewById(R.id.tvTexto);
        tv.setText(msj);

        LinearLayout llRoot =
                (LinearLayout) layout.findViewById(R.id.llRoot);

        Drawable img;
        int bg;

        switch (tipoMSJ) {
            case WARNING:
                img = context.getResources().getDrawable(R.drawable.btn_warning);
                bg = R.drawable.toast_background_yellow;
                break;
            case ERROR:
                img = context.getResources().getDrawable(R.drawable.btn_error);
                bg = R.drawable.toast_background_red;
                break;
            case ADD:
                img = context.getResources().getDrawable(R.drawable.btn_circ_plusv);
                bg = R.drawable.toast_background_green;
                break;
            case OK:
                img = context.getResources().getDrawable(R.drawable.btn_circ_okv);
                bg = R.drawable.toast_background_green;
                break;
            case DEL:
                img = context.getResources().getDrawable(R.drawable.btn_circ_minusr);
                bg = R.drawable.toast_background_red;
                break;
            case BLACK:
                img = context.getResources().getDrawable(R.drawable.ksslogo_opaco);
                bg = R.drawable.toast_background_black;
                break;
            case WHITE:
                img = context.getResources().getDrawable(R.drawable.ksslogo_opaco);
                bg = R.drawable.toast_background_white;
                break;
            case GRAY:
                img = context.getResources().getDrawable(R.drawable.ksslogo_opaco);
                bg = R.drawable.toast_background_gray;
                break;
            default:
                img = context.getResources().getDrawable(R.drawable.btn_info);
                bg = R.drawable.toast_background_blue;
                break;
        }
        tv.setCompoundDrawablesWithIntrinsicBounds(img, null, null, null);
        llRoot.setBackgroundResource(bg);

        Toast toast = new Toast(context);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(tiempoMSJ);
        toast.setView(layout);
        toast.show();
    }

}
