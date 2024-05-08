package com.kss.kssmarketv10;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.kss.kssmarketv10.DropBox.DropboxLogin;
import com.kss.kssutil.ToastManager;
import com.kss.kssutil.enuToastIcons;

import java.io.IOException;

import static com.kss.kssutil.BitmapUtils.saveDrawable;
import static com.kss.kssutil.BitmapUtils.showBitmapFromFile;
import static com.kss.kssutil.BitmapUtils.showImage;
import static com.kss.kssutil.clsUtil_Errores.ManejadorErrores;

public class dlgConfig extends AppCompatActivity {
    private boolean preferenciasGuardadas = false;
    private kssSettings setting;

    protected Context context;

    private ImageView ivTakeImage_Conf;
    private EditText etCIRIF_Conf;
    private EditText etNombre_Conf;
    private EditText etContacto_Conf;
    private EditText etEmail_Conf;
    private EditText etDireccion_Conf;
    private EditText etTelf_Conf;
    private TextView tvZoom_Conf;
    private EditText etPassword_Conf;
    private EditText etIVA1_Conf;
    private EditText etIVA2_Conf;
    private EditText etIVA3_Conf;
    private EditText etGanancia_Conf;
    private EditText etBD_Conf;
    private EditText etURIBackup_Conf;
    private ImageButton ibHome_Conf;
    private ImageView ivExit_Conf;
    private TextView tvSave_Conf;
    private TextView tvCancel_Conf;
    private static final int CAMERA_REQUEST = 1888;
    private boolean esTomandoFoto;
    private TextView tvBackup_Conf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //tab1
        setContentView(R.layout.activity_dlg_config);
        setting = kssSettings.getInstance(this);
        context = this;
        ivTakeImage_Conf = (ImageView) findViewById(R.id.ivTakeImage_Conf);
        etCIRIF_Conf = (EditText) findViewById(R.id.etCIRIF_Conf);
        etNombre_Conf = (EditText) findViewById(R.id.etNombre_Conf);
        etContacto_Conf = (EditText) findViewById(R.id.etContacto_Conf);
        etEmail_Conf = (EditText) findViewById(R.id.etEmail_Conf);
        etDireccion_Conf = (EditText) findViewById(R.id.tvDireccion_Conf);
        etTelf_Conf = (EditText) findViewById(R.id.etTelf_Conf);
        tvZoom_Conf = (TextView) findViewById(R.id.tvZoom_Conf);
        etPassword_Conf = (EditText) findViewById(R.id.etPassword_Conf);
        //Tab2
        etIVA1_Conf = (EditText) findViewById(R.id.etIVA1_Conf);
        etIVA2_Conf = (EditText) findViewById(R.id.etIVA2_Conf);
        etIVA3_Conf = (EditText) findViewById(R.id.etIVA3_Conf);
        etGanancia_Conf = (EditText) findViewById(R.id.etGanancia_Conf);
        //Tab3
        etBD_Conf = (EditText) findViewById(R.id.etBD_Conf);
        etURIBackup_Conf = (EditText) findViewById(R.id.etURIBacxup_Conf);
        tvBackup_Conf = (TextView) findViewById(R.id.tvBackup_Conf);

        //Form
        ibHome_Conf = (ImageButton) findViewById(R.id.ibHome_Conf);
        ivExit_Conf = (ImageView) findViewById(R.id.ivExit_Conf);
        tvSave_Conf = (TextView) findViewById(R.id.tvSave_Conf);
        tvCancel_Conf = (TextView) findViewById(R.id.tvCancel_Conf);

        //Eventos Click
        tvSave_Conf.setOnClickListener(OnClickListener);
        tvCancel_Conf.setOnClickListener(OnClickListener);

        final TextView tvRIF_conf = (TextView) findViewById(R.id.tvRIF_conf);
        final TextView tvEmpresa_conf = (TextView) findViewById(R.id.tvEmpresa_conf);
        final TextView tvBaseD_Conf = (TextView) findViewById(R.id.tvBaseD_Conf);
        final TextView tvURIBackup_Conf = (TextView) findViewById(R.id.tvURIBackup_Conf);

        final TextView tvCategorias_Conf = (TextView) findViewById(R.id.tvCategorias_Conf);
        tvCategorias_Conf.setOnClickListener(OnClickListener);
        asignTabHost();

        ivTakeImage_Conf.setOnClickListener(OnClickListener);
        tvZoom_Conf.setOnClickListener(OnClickListener);
        ivExit_Conf.setOnClickListener(OnClickListener);
        ibHome_Conf.setOnClickListener(OnClickListener);
        tvBackup_Conf.setOnClickListener(OnClickListener);

        tvEmpresa_conf.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                etNombre_Conf.setEnabled(true);
                return true;
            }
        });
        tvRIF_conf.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                etCIRIF_Conf.setEnabled(true);
                return true;
            }
        });

        tvBaseD_Conf.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                etBD_Conf.setEnabled(true);
                return true;
            }
        });

        tvURIBackup_Conf.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                etURIBackup_Conf.setEnabled(true);
                return true;
            }
        });

    }

    private void asignTabHost() {
        //TabHost
        TabHost host = (TabHost) findViewById(R.id.thConfig);
        host.setup();
        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec("Empresa");
        spec.setContent(R.id.tbEmpresa_Conf);
        spec.setIndicator("Empresa");
        host.addTab(spec);
        //Tab 2
        spec = host.newTabSpec("Impuestos");
        spec.setContent(R.id.tbIVA_Conf);
        spec.setIndicator("Impuestos");
        host.addTab(spec);
        //Tab 3
        spec = host.newTabSpec("Sistema");
        spec.setContent(R.id.tbSistema_Conf);
        spec.setIndicator("Sistema");
        host.addTab(spec);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (esTomandoFoto) {
            esTomandoFoto = false;
        }

    }

    private Boolean Backup() throws IOException {
        //TODO Eliminar: Hacer un Backup
        try {
            Intent intent = new Intent(dlgConfig.this, DropboxLogin.class);
            startActivity(intent);
            return true;
        } catch (Exception ex) {
            ToastManager.show(context, ex.getMessage().toString(), enuToastIcons.ERROR, 0);
            return false;
        }
    }

    // al abrir la aplicaci&#xfffd;n, guardamos preferencias
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    // al abrir la aplicaci&#xfffd;n, cargamos preferencias
    @Override
    protected void onStart() {
        super.onStart();
        setting = setting == null ? kssSettings.getInstance(this) : setting;
        if (setting != null)
            if (setting.getEsPrefGuardadas()) {
                try {
                    etCIRIF_Conf.setText(setting.getEmpresaCIRIF());
                    etCIRIF_Conf.setEnabled(false);
                    etNombre_Conf.setText(setting.getEmpresaNombre());
                    etNombre_Conf.setEnabled(false);
                    etContacto_Conf.setText(setting.getEmpresaContacto());
                    etEmail_Conf.setText(setting.getEmpresaEmailGmail());
                    etTelf_Conf.setText(setting.getEmpresaTelf());
                    etPassword_Conf.setText(setting.getEmpresaPassWordGmail());
                    etIVA1_Conf.setText(setting.getIva1());
                    etIVA2_Conf.setText(setting.getIva2());
                    etIVA3_Conf.setText(setting.getIva3());
                    etGanancia_Conf.setText(setting.getGanancia());
                    etBD_Conf.setText(setting.getNombreBD());
                    etBD_Conf.setEnabled(false);
                    etDireccion_Conf.setText(setting.getEmpresaDireccion());
                    etURIBackup_Conf.setText(setting.getRutaBackupBD());
                    etURIBackup_Conf.setEnabled(false);
                    tvBackup_Conf = (TextView) findViewById(R.id.tvBackup_Conf);
                    //Imagen Logotipo
                    Bitmap bitmap = showBitmapFromFile(this, setting.getRutaMedia() + "logoempresa.jpg");
                    if (bitmap != null)
                        ivTakeImage_Conf.setImageBitmap(bitmap);
                    else
                        ivTakeImage_Conf.setImageResource(R.drawable.kss_camara);
                } catch (Exception ex) {
                    ManejadorErrores(this, ex, true, true);
                }
            } else {
                //Cargar Predeterminadas
                ToastManager.show(context, "Se van a Cargar datos Predeterminados.", enuToastIcons.INFORMATION, 0);
                etIVA1_Conf.setText("12.0");
                etIVA2_Conf.setText("8.0");
                etIVA3_Conf.setText("22.0");
                etGanancia_Conf.setText("30");
                etBD_Conf.setText("kssmarket_db");
                etNombre_Conf.setText("Empresa Demo");
                etCIRIF_Conf.setText("J-00000000-0");
            }
    }

    boolean esDatosCompletos() {
        if (etNombre_Conf.getText().toString().trim().length() == 0) {
            etNombre_Conf.requestFocus();
            return false;
        }
        if (etCIRIF_Conf.getText().toString().trim().length() == 0) {
            etCIRIF_Conf.requestFocus();
            return false;
        }
        if (etTelf_Conf.getText().toString().trim().length() == 0) {
            etTelf_Conf.requestFocus();
            return false;
        }
        if (etEmail_Conf.getText().toString().trim().length() == 0) {
            etEmail_Conf.requestFocus();
            return false;
        }
        if (etPassword_Conf.getText().toString().trim().length() == 0) {
            etPassword_Conf.requestFocus();
            return false;
        }
        if (etDireccion_Conf.getText().toString().trim().length() == 0) {
            etDireccion_Conf.requestFocus();
            return false;
        }
        if (etIVA1_Conf.getText().toString().trim().length() == 0)
            etIVA1_Conf.setText("12.0");

        if (etBD_Conf.getText().toString().trim().length() == 0)
            etBD_Conf.setText("kssmarket_db");

        if (etURIBackup_Conf.getText().toString().trim().length() == 0)
            etURIBackup_Conf.setText(setting.getRutaBackupBD());
        return true;
    }

    //region Tomar foto
    public void tomarFoto(View view) {
        Intent intent = new Intent(
                android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, CAMERA_REQUEST);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    final Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            ivTakeImage_Conf.setImageDrawable(null);
            ivTakeImage_Conf.destroyDrawingCache();
            Bundle extras = data.getExtras();
            Bitmap imagebitmap = (Bitmap) extras.get("data");
            ivTakeImage_Conf.setImageBitmap(imagebitmap);
            esTomandoFoto = true;
        }
    }

    //endregion


    /**
     * Salvar Configuracion
     *
     * @param v
     */
    void guardardatos(View v) {
        Boolean result = false;
        try {
            if (esDatosCompletos()) {
                setting.setEmpresaCIRIF(etCIRIF_Conf.getText().toString());
                setting.setEmpresaNombre(etNombre_Conf.getText().toString());
                setting.setEmpresaDireccion(etDireccion_Conf.getText().toString());
                setting.setEmpresaContacto(etContacto_Conf.getText().toString());
                setting.setEmpresaPassWordGmail(etPassword_Conf.getText().toString());
                setting.setEmpresaEmailGmail(etEmail_Conf.getText().toString());
                setting.setEmpresaTelf(etTelf_Conf.getText().toString());
                setting.setEmpresaPassWordGmail(etPassword_Conf.getText().toString());
                setting.setIva1(etIVA1_Conf.getText().toString());
                setting.setIva2(etIVA2_Conf.getText().toString());
                setting.setIva3(etIVA3_Conf.getText().toString());
                setting.setGanancia(etGanancia_Conf.getText().toString());
                setting.setNombreBD(etBD_Conf.getText().toString());
                setting.setRutaBackupBD(etURIBackup_Conf.getText().toString());
                saveDrawable(ivTakeImage_Conf, "logoempresa", setting.getRutaMedia());
                result = true;
                finish();
            } else {
                ToastManager.show(this, "Datos incompletos!.\nVerifique e intente d enuevo.", enuToastIcons.INFORMATION, 0);
            }
        } catch (Exception ex) {
            ManejadorErrores(this,ex,true,true);
        } finally {
            setting.setEsPrefGuardadas(result);
            setting.guardarCambios();
        }
    }

    void Cerrar(View v) {
        //Cerrar
        finish();
    }

    private View.OnClickListener OnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tvSave_Conf:
                    guardardatos(v);
                    break;
                case R.id.tvCancel_Conf:
                    finish();
                    break;
                case R.id.tvZoom_Conf:
                    showImage(v.getContext(), setting.getRutaMedia() + "logoempresa.jpg");
                    break;
                case R.id.ivTakeImage_Conf:
                    tomarFoto(v);
                    break;
                case R.id.tvBackup_Conf:
                    try {
                        Backup();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case R.id.tvCategorias_Conf:
                    Intent n = new Intent(v.getContext(), dlgCategorias.class);
                    startActivity(n);
                    break;
                case R.id.ivExit_Conf:
                case R.id.ibHome_Conf:
                    finish();
                    break;

            }
        }
    };

}
