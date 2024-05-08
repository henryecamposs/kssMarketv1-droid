package com.kss.kssmarketv10;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kss.kssutil.ToastManager;
import com.kss.kssutil.clsUtil_Files;
import com.kss.kssutil.enuToastIcons;

import static com.kss.kssutil.BitmapUtils.showBitmapFromFile;
import static com.kss.kssutil.clsUtil_Errores.ManejadorErrores;

public class MainActivity extends AppCompatActivity {
    Context context;
    public kssSettings setting ;

    //Respuestas msgs
    enum Answer {
        YES, NO, ERROR
    }

    static Answer choice;

    @Override
    public void onResume() {
        super.onResume();
        cargarConfig();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final ImageButton ibClientes = (ImageButton) findViewById(R.id.ibClientesMain);
        ibClientes.setOnClickListener(OnClickListener);
        final ImageButton ibProductos = (ImageButton) findViewById(R.id.ibProductosMain);
        ibProductos.setOnClickListener(OnClickListener);
        final ImageButton ibConfigMain = (ImageButton) findViewById(R.id.ibConfigMain);
        ibConfigMain.setOnClickListener(OnClickListener);
        final ImageButton ibAyudaMain = (ImageButton) findViewById(R.id.ibAyudaMain);
        ibAyudaMain.setOnClickListener(OnClickListener);
        final ImageButton ibReportesMain = (ImageButton) findViewById(R.id.ibReportesMain);
        ibReportesMain.setOnClickListener(OnClickListener);
        final ImageButton ibBuscarMain = (ImageButton) findViewById(R.id.ibBuscarMain);
        ibBuscarMain.setOnClickListener(OnClickListener);
        final ImageView ivExit = (ImageView) findViewById(R.id.ivExit_Main);
        ivExit.setOnClickListener(OnClickListener);
        SharedPreferences prefs = getSharedPreferences(getApplicationContext().getPackageName(), Context.MODE_PRIVATE);
        setting = setting.initSettings(prefs);
        context = this;
        cargarConfig();
        crearDirsDefault();
    }


    private void cargarConfig() {
        //Cargar Confi
        final ImageView ivTakeImage_Main = (ImageView) findViewById(R.id.ivTakeImage_Main);
        final TextView tvEmpresa_Main = (TextView) findViewById(R.id.tvEmpresa_Main);
        try {
            tvEmpresa_Main.setText(setting.getEmpresaNombre());
            Bitmap bitmap = showBitmapFromFile(this, setting.getRutaMedia() + "logoempresa.jpg");
            if (bitmap != null)
                ivTakeImage_Main.setImageBitmap(bitmap);
            else
                ivTakeImage_Main.setImageResource(R.drawable.kss_camara);
        } catch (Exception e) {
            ManejadorErrores(this,e,true,true);
        }
    }



    void crearDirsDefault() {
        int i = 0;
        try {
            clsUtil_Files. verifyStoragePermissions( this);
            if (!clsUtil_Files.createDir(this, setting.getRutaBackupBD())) {
                {
                    Toast.makeText(this, "No se pudo crear dir: " + setting.getRutaBackupBD(), Toast.LENGTH_LONG);
                    i++;
                }

                if (!clsUtil_Files.createDir(this, setting.getRutaBackupBD())) {
                    Toast.makeText(this, "No se pudo crear dir: " + setting.getRutaDocs(), Toast.LENGTH_LONG);
                    i++;
                }
                if (!clsUtil_Files.createDir(this, setting.getRutaBackupBD())) {
                    Toast.makeText(this, "No se pudo crear dir: " + setting.getRutaMedia(), Toast.LENGTH_LONG);
                    i++;
                }
                if (i > 0) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                    alertDialogBuilder.setTitle(this.getString(R.string.kssEliminarRegistro));
                    alertDialogBuilder
                            .setIcon(R.drawable.ic_launcher)
                            .setMessage("Algunos directoris no se han creado correctamente!.\n" +
                                    "¿Desea Continuar o cerra la Aplicación para crearlos Manualmente?")
                            .setCancelable(false)//Disable back button action
                            .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    ToastManager.show(context,
                                            String.format("Asegurase de tener acceso a la carpeta %s/kssmarket.\n" +
                                                    "En caso contrario debe crear la carpeta (kssmarket) y abrir la aplicación de nuevo.\n" +
                                                    "Si el problema persiste contacte el fabricante de software.", clsUtil_Files.getExternalSdCardPath()),
                                            enuToastIcons.INFORMATION, 40);
                                    finish();
                                    choice = Answer.YES;
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Toast.makeText(context, "Eligio emitir excepciones", Toast.LENGTH_SHORT).show();
                                    choice = Answer.NO;
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
            }
        } catch (Exception e) {
            ManejadorErrores(this,e,true,true);
        }
    }

    /**
     * Evento se dispara al hacr click en el boton
     */
    private View.OnClickListener OnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
//            buttonEffect(v);
            try {
                switch (v.getId()) {
                    case R.id.ibClientesMain:
                        try {
                            final Intent i = new Intent(v.getContext(), dlgClientes.class);
                            startActivity(i);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(v.getContext(), context.getString(R.string.kssEx) + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case R.id.ibProductosMain:
                        try {
                            final Intent j = new Intent(v.getContext(), dlgProductos.class);
                            startActivity(j);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(v.getContext(), context.getString(R.string.kssEx) + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case R.id.ibConfigMain:
                        try {
                            Intent k = new Intent(v.getContext(), dlgConfig.class);
                            startActivity(k);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(v.getContext(), context.getString(R.string.kssEx) + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case R.id.ibAyudaMain:
                        try {
                            Intent l = new Intent(v.getContext(), dlgAcercade.class);
                            startActivity(l);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(v.getContext(), context.getString(R.string.kssEx) + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case R.id.ibReportesMain:
                        try {
                            Intent m = new Intent(v.getContext(), dlgReportes.class);
                            startActivity(m);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(v.getContext(), context.getString(R.string.kssEx) + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case R.id.ibBuscarMain:
                        try {
                            Intent n = new Intent(v.getContext(), dlgBuscar.class);
                            startActivity(n);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(v.getContext(), context.getString(R.string.kssEx) + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case R.id.ivExit_Main:
                        finish();
                        break;
                }
            } catch (Exception e) {
                ManejadorErrores(v.getContext(),e,true,true);
            }
        }
    };
}
//TODO Direccion Base de datos: /data/data/com.kss.kssmarketv10/databases/kssmarket_db