package com.kss.kssutil;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.BatteryManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Created by HENRY on 27/02/2015.
 *
 * @author HENRY
 */
public  class clsUtil_Utilidades {

    /**
     * Retorna el Item Seleccionado
     * @param pos
     * @param listView
     * @return
     */
    public View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }

    /**
     * Enviar notificaciones y luces
     *
     * @param context Contesto de aplicacion
     * @param msg     mensaje
     * @param extras  Extras
     */
    private void sendNotification(Context context, String msg, Bundle extras) {
       /* NotificationManager mNotificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent notifyMainIntent = buildIntentForMainActifity(extras);

        // Figure out how to add extras for the URL and message
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                notifyMainIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle("Home Monitor Notification")
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(msg))
                        .setExtras(extras)
                        .setContentText(msg);

        mBuilder.setContentIntent(contentIntent);
        mBuilder.setPriority(Notification.PRIORITY_MAX);
        mBuilder.setLights(0xFF0000, 500, 500);
        long[] pattern = new long[]{0, 500, 0, 500, 0, 500, 0, 500, 0, 500};
        mBuilder.setVibrate(pattern);
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        mBuilder.setSound(soundUri);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());*/
    }

    /**
     * Escucha eventos generados al tocar la pantalla
     *
     * @param event
     * @return
     */
    public boolean onTouchEvent(MotionEvent event) {
        int eventaction = event.getAction();

        switch (eventaction) {
            case MotionEvent.ACTION_DOWN:
                // finger touches the screen
                break;

            case MotionEvent.ACTION_MOVE:
                // finger moves on the screen
                break;

            case MotionEvent.ACTION_UP:
                // finger leaves the screen
                break;
        }

        // tell the system that we handled the event and no further processing is required
        return true;
    }

    /**
     * Convertir fecha string a fecha tipo DATE
     *
     * @param sFecha string
     * @return fecha en tipo Date
     * @throws ParseException
     */
    public static Date stringToFecha(String sFecha) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        Date date = format.parse(sFecha);
        return date;
    }

    /**
     * Mostrar Dialogo Alerta
     *
     * @param context
     * @param message
     * @param status
     * @param nombreActivity
     */
    public static void showAlertDialog(Context context, String message, Boolean status, String nombreActivity) {
  /*      AlertDialog.Builder builder = new AlertDialog.Builder(context);
        try {

            String[] aux = source.split(" ### ");
            message = aux[0];
            nombreActivity = aux[1];
            builder.setMessage(message)
                    .setCancelable(false)
                    .setNegativeButton("go to url", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(nombreActivity));
                            startActivity(browserIntent);
                        }
                    })
                    .setPositiveButton("cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                            mTimer = new MyTimmer(200, 200);
                            mTimer.start();
                        }
                    });
        } catch (Exception ex) {
            builder.setMessage(source)
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                            mTimer = new MyTimmer(200, 200);
                            mTimer.start();
                        }
                    });
        }
        AlertDialog alert = builder.create();
        alert.show();*/
    }

    /**
     * Guardar las veces que se ha usado d eesta aplicacion
     *
     * @param context
     * @param launched_times
     */
    public static void guardarNroVecesAppIniciaada(Context context, int launched_times) {
        SharedPreferences prefs1 = context.getSharedPreferences("PREFS", 0);

        launched_times = prefs1.getInt("launched_times", 0);

        Log.i("_launched times_", String.valueOf(launched_times));

        SharedPreferences.Editor editor = prefs1.edit();

        editor.putInt("launched_times", launched_times + 1);

        editor.commit();
    }

    /**
     * Seleccionar el ID del Dispositivo
     *
     * @param context Contexti de la aplicacion
     * @return
     */
    public static String getIDDispoditivo(Context context) {
        final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        final String tmDevice, tmSerial, tmPhone, androidId;
        try {
            tmDevice = "" + tm.getDeviceId();
            tmSerial = "" + tm.getSimSerialNumber();
            androidId = "" + android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
            UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
            String deviceId = deviceUuid.toString();
            return deviceId;
        } catch (Exception ex) {
            return "nodeviceid";
        }

    }

    /**
     * Lista de archivos de un directorio
     *
     * @param directoryName Nombre del directorio
     * @return
     */
    public static File[] listarArchivos(String directoryName) {
        File dir = new File(directoryName);
        String[] children = dir.list();
        if (children == null) {
            // Either dir does not exist or is not a directory
        } else {
            for (int i = 0; i < children.length; i++) {
                // Get filename of file or directory
                String filename = children[i];
            }
        }
        // It is also possible to filter the list of returned files.
        // This example does not return any files that start with `.'.
        FilenameFilter filter = new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return !name.startsWith(".");
            }
        };
        children = dir.list(filter);
        // The list of files can also be retrieved as File objects
        File[] files = dir.listFiles();
        // This filter only returns directories
        FileFilter fileFilter = new FileFilter() {
            public boolean accept(File file) {
                return file.isDirectory();
            }
        };
        files = dir.listFiles(fileFilter);
        return files;
    }

    /**
     * Saber estado bateria
     */
    private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context ctx, Intent intent) {
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
            Log.i("Battery Level", String.valueOf(level) + "%");
        }

    };
    public static String FechaActualToString(String Formato){
        SimpleDateFormat dateformat = new SimpleDateFormat(Formato==null ? "dd-MM-yyyy HH:mm:ss" : Formato);
        Date date = new Date();
        String datetime = dateformat.format(date);
        return datetime;
    }

    /** Configurar formato Decimal
     * Configurar el formato decimal
     * @param pattern
     * @param value
     */
    static public String customFormat(String pattern, double value ) {
        DecimalFormat myFormatter = new DecimalFormat(pattern);
        String output = myFormatter.format(value);
        return output;
        //Ejemplo de uso
        /* static public void main(String[] args) {
            customFormat("###,###.###", 123456.789);
            customFormat("###.##", 123456.789);
            customFormat("000000.000", 123.78);
            customFormat("$###,###.###", 12345.67);
        }*/
    }

}
