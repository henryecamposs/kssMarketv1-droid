package com.kss.kssutil;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by KSS on 20/3/2017.
 */

public class clsUtil_Files {
    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public static void displaypdf(Context context, String sArchivo) {

        File file = null;
        file = new File(sArchivo);
        Toast.makeText(context, file.toString() , Toast.LENGTH_LONG).show();
        if(file.exists()) {
            Intent target = new Intent(Intent.ACTION_VIEW);
            target.setDataAndType(Uri.fromFile(file), "application/pdf");
            target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

            Intent intent = Intent.createChooser(target, "Open File");
            try {
                //todo revisar
                //startActivity(intent);
            } catch (ActivityNotFoundException e) {
                // Instruct the user to install a PDF reader here, or something
            }
        }
        else
            Toast.makeText(context, "File path is incorrect." , Toast.LENGTH_LONG).show();
    }

    /**
     * Checks if the app has permission to write to device storage
     *
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }
    public static String getDominioApp(View v) {
        return v.getContext().getPackageName();
    }

    /**
     * obtiene la direccion de la memoria externa SD
     *
     * @return
     */
    public static String getExternalSdCardPath() {
        String path = null;

        File sdCardFile = null;
        List<String> sdCardPossiblePath = Arrays.asList("external_sd", "ext_sd", "external", "extSdCard");

        for (String sdPath : sdCardPossiblePath) {
            File file = new File("/mnt/", sdPath);

            if (file.isDirectory() && file.canWrite()) {
                path = file.getAbsolutePath();
                String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date());
                File testWritable = new File(path, "test_" + timeStamp);

                if (testWritable.mkdirs()) {
                    testWritable.delete();
                } else {
                    path = null;
                }
            }
        }
        if (path != null) {
            sdCardFile = new File(path);
        } else {
            sdCardFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
        }
        return sdCardFile.getAbsolutePath();
    }

    public static class BDbackup_return {
        private Boolean result;
        private String nombreArchivo_result;
        private String pathArchivo_result;

        public BDbackup_return(Boolean result, String nombreArchivo_result, String pathArchivo_result) {
            this.result = result;
            this.nombreArchivo_result = nombreArchivo_result;
            this.pathArchivo_result = pathArchivo_result;
        }

        public Boolean getResult() {
            return result;
        }

        public void setResult(Boolean result) {
            this.result = result;
        }

        public String getNombreArchivo_result() {
            return nombreArchivo_result;
        }

        public void setNombreArchivo_result(String nombreArchivo_result) {
            this.nombreArchivo_result = nombreArchivo_result;
        }

        public String getPathArchivo_result() {
            return pathArchivo_result;
        }

        public void setPathArchivo_result(String pathArchivo_result) {
            this.pathArchivo_result = pathArchivo_result;
        }
    }

    public static BDbackup_return BD_backup(Context context, String dataBaseName, String dirDestinoBackup, String patchOrigenBD) throws IOException {
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmmss").format(new Date());
        String outFileName = "";
        try {
            final String inFileName = patchOrigenBD + dataBaseName;
            File dbFile = new File(inFileName);
            FileInputStream fis;

            fis = new FileInputStream(dbFile);
            File d = new File(dirDestinoBackup);
            if (!d.exists())
                d.mkdir();

            outFileName = dirDestinoBackup + "/" + dataBaseName + timeStamp;
            OutputStream output = new FileOutputStream(outFileName);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0)
                output.write(buffer, 0, length);

            output.flush();
            output.close();
            fis.close();
            return new BDbackup_return(true, dataBaseName + timeStamp, outFileName);
        } catch (FileNotFoundException fnfe1) {
            Log.e("tag", fnfe1.getMessage());
            Toast.makeText(context, fnfe1.getMessage(), Toast.LENGTH_LONG).show();
            return new BDbackup_return(false, dataBaseName, outFileName);
        } catch (Exception e) {
            Log.e("tag", e.getMessage());
            return new BDbackup_return(false, dataBaseName, outFileName);
        } finally {

        }
    }

    /**
     * Crear Directorio
     *
     * @param rutaDir
     */
    public static Boolean createDir(Context context, String rutaDir) {
        //create output directory if it doxesn't exist
        try {
            File dir = new File(rutaDir);
            if (!dir.exists())
                dir.mkdirs();
            return true;
        } catch (Exception ex) {
            ToastManager.show(context, "Se ha generado una excepcion" + ex.toString(), enuToastIcons.ERROR, 20);
            return false;
        }
    }

    /**
     * Copiar Archivos
     *
     * @param inputPath
     * @param inputFile
     * @param outputPath
     */
    private void copyFile(String inputPath, String inputFile, String outputPath) {

        InputStream in = null;
        OutputStream out = null;
        try {
            //create output directory if it doesn't exist
            File dir = new File(outputPath);
            if (!dir.exists())
                dir.mkdirs();

            in = new FileInputStream(inputPath + inputFile);
            out = new FileOutputStream(outputPath + inputFile);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1)
                out.write(buffer, 0, read);
            in.close();
            out.flush();
            out.close();
            out = null;
            in = null;

        } catch (FileNotFoundException fnfe1) {
            Log.e("tag", fnfe1.getMessage());
        } catch (Exception e) {
            Log.e("tag", e.getMessage());
        }
    }

    /**
     * Eliminar Archivo
     *
     * @param inputPath
     * @param inputFile
     */
    private void deleteFile(String inputPath, String inputFile) {
        try {
            // delete the original file
            new File(inputPath + inputFile).delete();
        } catch (Exception e) {
            Log.e("tag", e.getMessage());
        }
    }

    /**
     * Mover Archivo
     *
     * @param inputPath
     * @param inputFile
     * @param outputPath
     */
    private void moveFile(String inputPath, String inputFile, String outputPath) {

        InputStream in = null;
        OutputStream out = null;
        try {

            //create output directory if it doesn't exist
            File dir = new File(outputPath);
            if (!dir.exists())
                dir.mkdirs();

            in = new FileInputStream(inputPath + inputFile);
            out = new FileOutputStream(outputPath + inputFile);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1)
                out.write(buffer, 0, read);
            in.close();
            out.flush();
            out.close();
            in = null;
            out = null;

            // delete the original file
            new File(inputPath + inputFile).delete();

        } catch (FileNotFoundException fnfe1) {
            Log.e("tag", fnfe1.getMessage());
        } catch (Exception e) {
            Log.e("tag", e.getMessage());
        }

    }

    /**
     * Funcion para Colocar Imagen a un ImageView desde un SD
     *
     * @param fileName Nombre del Archivo
     * @param pic      Objeto Imageview al que se le coloca la imagen
     */
    public static void ShowPicture(String fileName, ImageView pic) {
        File f = new File(Environment.getExternalStorageDirectory(), fileName);
        FileInputStream is = null;
        try {
            is = new FileInputStream(f);
        } catch (FileNotFoundException e) {
            Log.d("error: ", String.format("ShowPicture.java file[%s]Not Found", fileName));
            return;
        }
        Bitmap bm = BitmapFactory.decodeStream(is, null, null);
        pic.setImageBitmap(bm);
    }
}
