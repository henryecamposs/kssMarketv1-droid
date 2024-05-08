package com.kss.kssutil;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;

/**
 * Created by HENRY on 27/02/2015.
 */
public class clsUtil_Net {
    public static boolean esWSActivo(final Context context, String sUrl) {
        try {
            if (checkWifiState(context)) {
                URL url = null;
                url = new URL(sUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setConnectTimeout(60000);
                conn.setDoOutput(true);
                conn.setDoInput(true);
                OutputStreamWriter w = new OutputStreamWriter(conn.getOutputStream());
                return true;
            }
        } catch (final Exception e) {
            e.printStackTrace();
            ToastManager.show(context, e.getLocalizedMessage(), enuToastIcons.ERROR, 0);
           /* Handler handler=new Handler();
            handler=  new Handler(context.getMainLooper());
            handler.post( new Runnable(){
                public void run(){
                    ToastManager.show(context, e.getLocalizedMessage(), enuToastIcons.ERROR, 0);
                }
            });*/
            return false;
        }
        return false;
    }

    /**
     * Hacer Ping a URL
     *
     * @param URL Direccion del WebService
     * @return Positivo si existe
     */
    public static boolean PingURL(String URL) {
        int timeOut = 5000;
        boolean connected = false;
        InetAddress inet;
        try {
            inet = InetAddress.getByName(URL);
            //println(inet);
            connected = inet.isReachable(timeOut);
            if (connected == true) {
                return connected;
            } else {
                return connected;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Verifica si el dispositivo WIFI est� encendido
     *
     * @param context Contexto de la pantalla
     * @return Positivo si esta encendido
     */
    public static boolean checkWifiState(final Context context) {
        try {
            WifiManager mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = mWifiManager.getConnectionInfo();
            if (!mWifiManager.isWifiEnabled() || wifiInfo.getSSID() == null) {
                new AlertDialog.Builder(context)
                        .setTitle(context.getString(R.string.kssHaOcurridoError))
                        .setMessage(context.getString(R.string.kssDeseaActivarlo))
                        .setIcon(R.drawable.ic_launcher)
                        .setCancelable(false)
                        .setPositiveButton(context.getString(R.string.kssAceptar), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                enableWiFi(context, true);
                                return; //cierra el di�logo. Siempre puedes hacerle hacer algo.
                            }
                        })
                        .setNegativeButton(context.getString(R.string.kssCancelar), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                enableWiFi(context, false);
                                return; //cierra el di�logo. Siempre puedes hacerle hacer algo.
                            }
                        })
                        .show();
                return false;
            }
            return true;
        } catch (final Exception e) {
            e.printStackTrace();
            ToastManager.show(context, e.getLocalizedMessage(), enuToastIcons.ERROR, 0);
            return false;
        }
    }

    /**
     * activa o desactiva el dispositivo widi
     *
     * @param context Contexto d ela aplicacion
     * @param enable  Accion a realizar.
     */
    public static void enableWiFi(final Context context, boolean enable) {
        try {
            if (enable) {
                ToastManager.show(context, context.getString(R.string.kssWIFI_activo),  enuToastIcons.INFORMATION, 0);
            } else {
                ToastManager.show(context, context.getString(R.string.kssWIFI_Inactivo), enuToastIcons.INFORMATION, 0);
            }
            WifiManager mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            mWifiManager.setWifiEnabled(enable);
        } catch (final Exception e) {
            e.printStackTrace();
            ToastManager.show(context, e.getLocalizedMessage(), enuToastIcons.ERROR, 0);
        }
    }

    /**
     * Selecciona la direcci�n MAC del dispositivo WIFI
     *
     * @param context Contexto d ela aplicaci�n
     * @return String con la direcci�n MAC
     */
    public static String getIDByMAC(final Context context) {
        String str = null;
        try {
            WifiManager localWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo localWifiInfo = localWifiManager.getConnectionInfo();
            str = localWifiInfo.getMacAddress();
        } catch (final Exception e) {
            e.printStackTrace();
            ToastManager.show(context, e.getLocalizedMessage(), enuToastIcons.ERROR, 0);
            Log.d("No WIFI conexi�n", "Could not read MAC, forget to include ACCESS_WIFI_STATE permission?", e);
        }
        return str;
    }
   /* public static byte[] getBroadcastIPAddressRaw(final Context pContext) throws WifiException {
        final WifiManager wifiManager=WifiUtils.getWifiManager(pContext);
        final DhcpInfo dhcp=wifiManager.getDhcpInfo();
        final int broadcast=(dhcp.ipAddress & dhcp.netmask) | ~dhcp.netmask;
        final byte[] broadcastIP=new byte[4];
        for (int k=0; k < 4; k++) {
            broadcastIP[k]=(byte)((broadcast >> k * 8) & 0xFF);
        }
        return broadcastIP;
    }
*/


}

