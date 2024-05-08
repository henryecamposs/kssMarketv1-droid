package com.kss.kssutil;

/**
 * Created by HENRY on 05/03/2015.
 */

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class BitmapUtils {




    //region Controller Imagenes a Drawable
    public static  void showImage(Context context, String archivo) {
        Dialog builder = new Dialog(context);
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        builder.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                //nothing;
            }
        });

        //
        ImageView imageView = new ImageView(context);
        Bitmap bitmap = showBitmapFromFile(context,archivo);
        imageView.setImageBitmap(bitmap);
        imageView.setMinimumHeight(400);
        imageView.setMinimumWidth(400);
        builder.addContentView(imageView, new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        builder.show();
    }



    /**
     * Salvar Drawable de View en Archivo fisico
     *
     * @param imageView
     * @param Nombrearchivo
     * @throws FileNotFoundException
     */
    public static  void saveDrawable(ImageView imageView, String Nombrearchivo, String rutaMedia) throws FileNotFoundException {
        Drawable drawable = imageView.getDrawable();
        Rect bounds = drawable.getBounds();
        Bitmap bitmap = Bitmap.createBitmap(bounds.width(), bounds.height(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.draw(canvas);
        OutputStream fOut = null;
        try {
            new File(rutaMedia).mkdir();
            fOut = new FileOutputStream(rutaMedia + Nombrearchivo + ".jpg");
            bitmap.compress(Bitmap.CompressFormat.JPEG, 95, fOut);
        } catch (Exception ex) {
            Toast.makeText(imageView.getContext(), "Ha ocurrido una excepción: " + ex.toString(), Toast.LENGTH_SHORT).show();
        } finally {
            if (fOut != null) {
                try {
                    fOut.close();
                } catch (IOException e) {
                    Toast.makeText(imageView.getContext(), "Ha ocurrido una excepción: "  + e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    /**
     * Mostrar Imagen Guardada
     *
     * @param file_path
     * @return
     */
    public static  Bitmap showBitmapFromFile(Context context,String file_path) {
        try {
            File imgFile = new File(file_path);
            if (imgFile.exists()) {
                Bitmap pic_Bitmap = decodeFile(file_path);
                return pic_Bitmap;
            }
        } catch (Exception e) {
            Toast.makeText(context, "Ha ocurrido una excepción: "  + e.toString(), Toast.LENGTH_SHORT).show();
            return null;
        }
        return null;
    }

    /**
     * Decodificar archivo Imagen externo
     *
     * @param path
     * @return
     */
    public static Bitmap decodeFile(String path) {
        Bitmap b = null;
        File f = new File(path);
        // Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;

        FileInputStream fis = null;
        try {
            fis = new FileInputStream(f);
            BitmapFactory.decodeStream(fis, null, o);
            fis.close();

            int IMAGE_MAX_SIZE = 1024; // maximum dimension limit
            int scale = 1;
            if (o.outHeight > IMAGE_MAX_SIZE || o.outWidth > IMAGE_MAX_SIZE) {
                scale = (int) Math.pow(2, (int) Math.round(Math.log(IMAGE_MAX_SIZE / (double) Math.max(o.outHeight, o.outWidth)) / Math.log(0.5)));
            }
            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;

            fis = new FileInputStream(f);
            b = BitmapFactory.decodeStream(fis, null, o2);
            fis.close();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return b;
    }
    //endregion


    void set(InputStream is) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(is, null, options);
        int imageHeight = options.outHeight;
        int imageWidth = options.outWidth;
        String imageType = options.outMimeType;


    }

    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float) height / (float) reqHeight);
            } else {
                inSampleSize = Math.round((float) width / (float) reqWidth);
            }
        }
        return inSampleSize;
    }

      public static Bitmap decodeSampledBitmapFromResource(Resources res,
                                                           int resId, int reqWidth, int reqHeight) {

              // First decode with inJustDecodeBounds=true to check dimensions
              final BitmapFactory.Options options = new BitmapFactory.Options();
              options.inJustDecodeBounds = true;
              BitmapFactory.decodeResource(res, resId, options);

              // Calculate inSampleSize
              options.inSampleSize = calculateInSampleSize(options, reqWidth,
                              reqHeight);

              // Decode bitmap with inSampleSize set
              options.inJustDecodeBounds = false;
              return BitmapFactory.decodeResource(res, resId, options);
      }

//      public void foo() {
//              mImageView.setImageBitmap(decodeSampledBitmapFromResource(
//                              getResources(), R.id.myimage, 100, 100));
//      }

    public void foo()
    {
//              BufferedInputStream bis = new BufferedInputStream(is);
//
//        final ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
//        BufferedOutputStream bos = new BufferedOutputStream(dataStream);
//        try {
//                      int a = bis.available();
//
//                      while (bis.available() > 0)
//                      {
//                              bos.write(bis.read());
//                      }
//                      bos.flush();
//              } catch (IOException e) {
//                      // TODO Auto-generated catch block
//                      e.printStackTrace();
//              }

//        final byte[] data = dataStream.toByteArray();
//        BitmapFactory.Options options = new BitmapFactory.Options();
        //options.inSampleSize = 1;

//        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length,options);

//              imageView.setImageBitmap(bitmap);

    }
}