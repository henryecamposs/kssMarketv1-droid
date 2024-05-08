package com.kss.kssmarketv10;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabaseCorruptException;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.kss.kssmarketv10.db.Productos;
import com.kss.kssmarketv10.db.Productos_Familia;
import com.kss.kssmarketv10.db.dao.DaoMaster;
import com.kss.kssmarketv10.db.dao.DaoSession;
import com.kss.kssmarketv10.db.dao.ProductosDao;
import com.kss.kssmarketv10.db.dao.Productos_FamiliaDao;
import com.kss.kssutil.ToastManager;
import com.kss.kssutil.enuStatusRegistro;
import com.kss.kssutil.enuToastIcons;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.dao.query.Query;

import static com.kss.kssutil.BitmapUtils.saveDrawable;
import static com.kss.kssutil.BitmapUtils.showBitmapFromFile;
import static com.kss.kssutil.BitmapUtils.showImage;
import static com.kss.kssutil.enuStatusRegistro.ACTUALIZANDO;
import static com.kss.kssutil.enuStatusRegistro.AGREGANDO;
import static com.kss.kssutil.enuStatusRegistro.CONSULTANDO;
import static com.kss.kssutil.enuStatusRegistro.EDITANDO;
import static com.kss.kssutil.enuStatusRegistro.ELIMINANDO;
import static com.kss.kssutil.enuStatusRegistro.NONE;

public class dlgProductos extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private static final int CAMERA_REQUEST = 1888;
    private kssSettings settings;
    //Variables propias
    private EditText etNombre_Prod;
    private EditText etDesc_Prod;
    private EditText etPrecioC_Prod;
    private EditText etPrecioV_Prod;
    private TextView tvZoom_Prod;
    private Spinner spCategorias_Prod;
    private ImageButton ibFirst;
    private ImageButton ibPrev;
    private ImageButton ibNext;
    private ImageButton ibLast;
    private Spinner spImpuestos_Prod;
    private LinearLayout llSaveCancel;
    private GridLayout glbtnMover;
    private ImageView ivAdd;
    private ImageView ivDel;
    private ImageView ivEdit;
    private ImageView ivBuscar;
    private AutoCompleteTextView acBuscar;
    private LinearLayout llTopBar;
    private TextView tvCodigo_Prod;
    ImageView ivTakeImage_Prod;
    TextView tvNumReg_Prod;

    //Base de datos
    //Variables relativas a Green Dao Database
    public DaoSession daoSession;
    public DaoMaster daoMaster;
    private ProductosDao productoDao;
    private SQLiteDatabase db;
    private long proximoID;
    private long maxRegistros;
    private long _position;
    ArrayList<itemrow_image> productosRow = null;
    private Boolean esTomandoFoto = false;
    private Productos _ProductoActual;
    private String rutaFotos;
    private List<Productos> _ProductosList;
    SearchItemArrayAdapter searchItemArrayAdapter = null;

    public List<Productos> get_ProductosList() {
        return _ProductosList;
    }

    public void set_ProductosList(List<Productos> _ProductosList) {
        this._ProductosList = _ProductosList;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    //Respuestas msgs
    enum Answer {
        YES, NO, ERROR
    }

    static dlgProductos.Answer choice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dlg_productos);

        //Obtiene el texto del EditText
        etNombre_Prod = (EditText) findViewById(R.id.etNombre_Prod);
        etDesc_Prod = (EditText) findViewById(R.id.etDesc_Prod);
        etPrecioC_Prod = (EditText) findViewById(R.id.etPrecioC_Prod);
        etPrecioV_Prod = (EditText) findViewById(R.id.etPrecioV_Prod);
        tvZoom_Prod = (TextView) findViewById(R.id.tvZoom_Prod);
        spCategorias_Prod = (Spinner) findViewById(R.id.spCategorias_Prod);
        spImpuestos_Prod = (Spinner) findViewById(R.id.spImpuestos_Prod);
        tvCodigo_Prod = (TextView) findViewById(R.id.tvCodigo_Prod);
        llSaveCancel = (LinearLayout) findViewById(R.id.llSaveCancel_Prod);
        tvNumReg_Prod = (TextView) findViewById(R.id.tvNumReg_Prod);
        glbtnMover = (GridLayout) findViewById(R.id.glbtnMover_Prod);
        acBuscar = (AutoCompleteTextView) findViewById(R.id.acBuscar_Prod);
        llTopBar = (LinearLayout) findViewById(R.id.lltopBar_Prod);
        final TextView tvSave = (TextView) findViewById(R.id.tvSave_Prod);
        final TextView tvcancel = (TextView) findViewById(R.id.tvCancel_Prod);
        ivAdd = (ImageView) findViewById(R.id.ivAdd_Prod);
        ivDel = (ImageView) findViewById(R.id.ivDel_Prod);
        ivEdit = (ImageView) findViewById(R.id.ivEdit_Prod);
        ivBuscar = (ImageView) findViewById(R.id.ivBuscar_Prod);
        ivTakeImage_Prod = (ImageView) findViewById(R.id.ivTakeImage_Prod);
        ibFirst = (ImageButton) findViewById(R.id.ibFirst_Prod);
        ibPrev = (ImageButton) findViewById(R.id.ibPrev_Prod);
        ibNext = (ImageButton) findViewById(R.id.ibNext_Prod);
        ibLast = (ImageButton) findViewById(R.id.ibLast_Prod);
        final ImageView ivExit = (ImageView) findViewById(R.id.ivExit_Prod);
        final ImageButton ibHome = (ImageButton) findViewById(R.id.ibHome_Prod);

        try {
            tvcancel.setOnClickListener(OnClickListener);
            tvSave.setOnClickListener(OnClickListener);
            ivAdd.setOnClickListener(OnClickListener);
            ivDel.setOnClickListener(OnClickListener);
            ivEdit.setOnClickListener(OnClickListener);
            ivBuscar.setOnClickListener(OnClickListener);
            ibFirst.setOnClickListener(OnClickListener);
            ibPrev.setOnClickListener(OnClickListener);
            ibNext.setOnClickListener(OnClickListener);
            ibLast.setOnClickListener(OnClickListener);
            ivTakeImage_Prod.setOnClickListener(OnClickListener);
            tvZoom_Prod.setOnClickListener(OnClickListener);
            ivExit.setOnClickListener(OnClickListener);
            ibHome.setOnClickListener(OnClickListener);

            settings = kssSettings.getInstance(this);
            ivEdit.setVisibility(View.GONE);
            ivDel.setVisibility(View.GONE);
            rutaFotos = settings.getRutaMedia();
            productoDao = setupDb();
            cargarTodos();
            loadAcProductos();
            setenuStatus(NONE);
            setPosition(0);
            Bundle extras = getIntent().getExtras();
            loadSpinnerImpuestos();
            loadSpinnercategorias();
            if (get_ProductosList() != null) {
                if (extras != null) {
                    long id = extras.getLong("ID");
                    setProductoActual(productoDao.load(id));
                } else {
                    setProductoActual(get_ProductosList().get(0));
                }
                setenuStatus(CONSULTANDO);
            } else {
                setenuStatus(NONE);
                ToastManager.show(this, "No existen datos!", enuToastIcons.INFORMATION, 10);
                glbtnMover.setVisibility(View.INVISIBLE);
                tvNumReg_Prod.setText("0 de 0");
            }
            //Texto Productos
            etNombre_Prod.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                }

                @Override
                public void beforeTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
                }

                @Override
                public void afterTextChanged(Editable arg0) {
                    if (getenuStatusregistro() == AGREGANDO)
                        etDesc_Prod.setText(etNombre_Prod.getText());
                }
            });

            //AUtoComplete
            acBuscar.setThreshold(2);
            acBuscar.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    acBuscar.showDropDown();
                    acBuscar.setDropDownWidth(400);
                    return false;
                }
            });
            acBuscar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long rowId) {
                    String selection = parent.getItemAtPosition(position).toString();
                    Toast.makeText(parent.getContext(), "" + selection, Toast.LENGTH_SHORT).show();
                    buscarDatos();
                }
            });
            acBuscar.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count,
                                              int after) {
                    searchItemArrayAdapter.getFilter().filter(s);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, this.getString(R.string.kssEx) + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getProductoActual() != null && esTomandoFoto) {
            //setenuStatus(enuStatusRegistro.ACTUALIZANDO);
            esTomandoFoto = false;
        }
    }

    /**
     * Retorno de tomar foto
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Bitmap mphoto = (Bitmap) data.getExtras().get("data");
            setenuStatus(settings.enuStatusregistro);
            if (getenuStatusregistro() == enuStatusRegistro.AGREGANDO)
                proximoID = settings.IDActual;
            else
                setProductoActual(productoDao.load(settings.IDActual));
            ivTakeImage_Prod.setImageBitmap(mphoto);
            esTomandoFoto = true;
        }
    }

    //Tomar foto
    public void tomarFoto(View view) {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        settings.enuStatusregistro = getenuStatusregistro();
        settings.IDActual = getenuStatusregistro() == enuStatusRegistro.AGREGANDO ? proximoID : getProductoActual().getId();
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    //region Controller Base datos

    private long getPosition() {
        return _position;
    }

    private void setPosition(long value) {
        _position = value;
        ibFirst.setVisibility(value == 0 ? View.INVISIBLE : View.VISIBLE);
        ibPrev.setVisibility(value == 0 ? View.INVISIBLE : View.VISIBLE);
        ibNext.setVisibility(value == maxRegistros - 1 ? View.INVISIBLE : View.VISIBLE);
        ibLast.setVisibility(value == maxRegistros - 1 ? View.INVISIBLE : View.VISIBLE);
        tvNumReg_Prod.setText(String.format("%1$d de %2$d", value + 1, maxRegistros));
        if (getProductoActual() == null)
            tvZoom_Prod.setEnabled(false);
        else
            tvZoom_Prod.setEnabled(true);
    }

    /**
     * Producto
     *
     * @return
     */
    private Productos getProductoActual() {
        return _ProductoActual;
    }

    private void setProductoActual(Productos value) {
        _ProductoActual = value;
        cargarTodos();
        if (value != null) {
            setPosition(get_ProductosList().indexOf(value));
            fillForm(value);
        } else {
            setPosition(-1);
            Reset();
        }
    }

    private enuStatusRegistro _enuStatusregistro;

    private enuStatusRegistro getenuStatusregistro() {
        return _enuStatusregistro;
    }

    private void setenuStatus(enuStatusRegistro value) {
        _enuStatusregistro = value;
        switch (value) {
            case EDITANDO:
                LiberarControles(true);
                break;
            case ELIMINANDO:
                LiberarControles(false);
                break;
            case ACTUALIZANDO:
                LiberarControles(true);
                break;
            case CONSULTANDO:
                LiberarControles(false);
                break;
            case AGREGANDO:
                LiberarControles(true);
                break;
            case NONE:
                LiberarControles(false);
                break;
        }
        if (getenuStatusregistro() == CONSULTANDO || getenuStatusregistro() == NONE) {
            if (getProductoActual() != null) {
                ivEdit.setVisibility(View.VISIBLE);
                ivDel.setVisibility(View.VISIBLE);
            }
        } else {
            ivEdit.setVisibility(View.GONE);
            ivDel.setVisibility(View.GONE);
        }
    }

    /**
     * Se asegura que los datos requeridos esten completos
     *
     * @return
     */
    private Boolean esDatosCompletos() {
        //if (etDesc_Prod.getText().toString().trim().length() == 0) return false;
        if (etNombre_Prod.getText().toString().trim().length() == 0) return false;
        // if (etPrecioC_Prod.getText().toString().trim().length() == 0) return false;
        if (etPrecioV_Prod.getText().toString().trim().length() == 0) return false;
        return true;
    }

    private ArrayList<itemrow_image> populateProductosRowData(List<Productos> rows) {
        ArrayList<itemrow_image> itemrow_images = new ArrayList<>();
        for (Productos item : rows
                ) {
            String sImagen = settings.getRutaMedia() + item.getId() + ".jpg";
            itemrow_images.add(new itemrow_image(item.getProducto(),
                    item.getMontoPrecio1(),
                    showBitmapFromFile(this, sImagen),
                    item.getId()));
        }
        return itemrow_images;
    }

    private void loadAcProductos() {
        try {
            if (daoMaster != null) {
                if (get_ProductosList().size() > 0) {
                    searchItemArrayAdapter = new SearchItemArrayAdapter(this, R.layout.searchitem_image, populateProductosRowData(get_ProductosList()));
                    acBuscar.setAdapter(searchItemArrayAdapter);
                }
            } else
                Toast.makeText(this, "Master DB no iniciada", Toast.LENGTH_LONG).show();

        } catch (Exception ex) {
            ex.printStackTrace();
            Toast.makeText(this, this.getString(R.string.kssEx) + ex.toString(), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Obtiene el Producto first
     *
     * @return
     */
    private void cargarTodos() {
        try {
            set_ProductosList(productoDao.queryBuilder().orderAsc(ProductosDao.Properties.Id).build().list());
            if (get_ProductosList().size() > 0) {
                maxRegistros = get_ProductosList().size();
            }
            setenuStatus(CONSULTANDO);
        } catch (SQLiteDatabaseCorruptException sqlex) {
            Toast.makeText(this, this.getString(R.string.kssEx) + sqlex.toString(), Toast.LENGTH_SHORT).show();
        } catch (Exception ex) {
            Toast.makeText(this, this.getString(R.string.kssEx) + ex.toString(), Toast.LENGTH_SHORT).show();
        } finally {
        }
    }

    /**
     * Guardar Datos en la base de Datos
     */
    public long guardarDatos() {
        if (esDatosCompletos()) {
            //Se crea nuevo Producto
            Productos Producto = new Productos();
            if (getenuStatusregistro() == AGREGANDO)
                Producto.setId(proximoID);
            else
                Producto.setId(getProductoActual().getId());
            Producto.setCodigoBarras(String.valueOf(Producto.getId()));
            Producto.setCodigoInterno(String.valueOf(Producto.getId()));
            Producto.setProducto_DescripcionLarga(etDesc_Prod.getText().toString());
            Producto.setProducto(etNombre_Prod.getText().toString());
            Producto.setMontoUltimoCosto(Double.parseDouble(etPrecioC_Prod.getText().toString()));
            Producto.setMontoPrecio1(Double.parseDouble(etPrecioV_Prod.getText().toString()));
            //Salvar Impuesto y Categoria
            if (spCategorias_Prod.getAdapter().getCount() > 0 && spCategorias_Prod.getSelectedItemId() >= 0)
                Producto.setId_familiaProducto((int) spCategorias_Prod.getSelectedItemId() + 1);
            if (spImpuestos_Prod.getAdapter().getCount() > 0 && spImpuestos_Prod.getSelectedItemId() >= 0)
                Producto.setId_Impuesto((int) spImpuestos_Prod.getSelectedItemId());
            return SaveToSQL(Producto, getenuStatusregistro());
        }
        ToastManager.show(this, "Datos Incompletos!.\nPor favor verifique e intente de nuevo.", enuToastIcons.ERROR, 10);
        return 0;
    }


    private void buscarDatos() {
        //Buscar por Cliente
        String sProducto = acBuscar.getText().toString().trim();
        if (sProducto.length() > 0) {
            Query<Productos> query = productoDao.queryBuilder().where(
                    ProductosDao.Properties.Producto.eq(sProducto)
            ).build();
            set_ProductosList(query.list());
            if (get_ProductosList().size() > 0) {
                setProductoActual(get_ProductosList().get(0));
            } else {
                //Busqueda: contiene
                query = productoDao.queryBuilder().where(
                        ProductosDao.Properties.Producto.like("%" + sProducto + "%")
                ).build();
                set_ProductosList(query.list());
                if (get_ProductosList().size() == 0) {
                    ToastManager.show(this, "No existe el Producto indicado.", enuToastIcons.ERROR, 10);
                    return;
                }
            }
        } else
            ToastManager.show(this, "Por favor indique el producto.", enuToastIcons.INFORMATION, 10);
    }

    /**
     * Salvar insertar Datos
     *
     * @param entidad
     * @return
     */
    private Long SaveToSQL(Productos entidad, enuStatusRegistro enuAciion) {
        try {
            long IDActual;
            if (enuAciion == AGREGANDO)
                IDActual = productoDao.insert(entidad);
            else {
                productoDao.update(entidad);
                IDActual = entidad.getId();
            }
            saveDrawable(ivTakeImage_Prod, String.valueOf(entidad.getId()), rutaFotos);
            return IDActual;
        } catch (SQLiteDatabaseCorruptException sqlex) {
            Toast.makeText(this, this.getString(R.string.kssEx) + sqlex.toString(), Toast.LENGTH_SHORT).show();
            return (long) 0;
        } catch (Exception ex) {
            Toast.makeText(this, this.getString(R.string.kssEx) + ex.toString(), Toast.LENGTH_SHORT).show();
            return (long) 0;
        } finally {
        }
    }

    /**
     * Eliminar Registro
     *
     * @param Producto
     * @return
     */
    private Boolean eliminarDatos(final Productos Producto) {
        try {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle(this.getString(R.string.kssEliminarRegistro));
            alertDialogBuilder
                    .setIcon(R.drawable.ic_launcher)
                    .setMessage("Realmente desea eliminar: " + Producto.getProducto().toUpperCase())
                    .setCancelable(false)//Disable back button action
                    .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            productoDao.delete(Producto);
                            moverRegistros(ibPrev);
                            // Toast.makeText(this,  getString(R.string.kssRegistroEliminado), Toast.LENGTH_SHORT).show();
                            choice = dlgProductos.Answer.YES;
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //Toast.makeText(this, this.getString(R.string.ksscancelar), Toast.LENGTH_SHORT).show();
                            choice = dlgProductos.Answer.NO;
                            dialog.cancel();
                        }
                    });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
            return choice == dlgProductos.Answer.YES ? true : false;
        } catch (SQLiteDatabaseCorruptException sqlex) {
            Toast.makeText(this, this.getString(R.string.kssEx) + sqlex.toString(), Toast.LENGTH_SHORT).show();
            return false;
        } catch (Exception ex) {
            Toast.makeText(this, this.getString(R.string.kssEx) + ex.toString(), Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    /**
     * Devuelve Ultimo ID
     *
     * @return
     */
    public Long getMaxId() {
        Long maxPostId;
        if (productoDao.count() > 0) {
            List<Productos> maxPostIdRow = productoDao.queryBuilder().where(ProductosDao.Properties.Id.isNotNull()).orderDesc(ProductosDao.Properties.Id).limit(1).list();
            maxPostId = maxPostIdRow.get(0).getId();
        } else
            maxPostId = (long) 0;
        return maxPostId;
    }


    /**
     * Inicia la Sesion de Trabajo con la base de datos
     *
     * @return
     */
    private ProductosDao setupDb() {
        DaoMaster.DevOpenHelper masterHelper = new DaoMaster.DevOpenHelper(this, settings.getNombreBD(), null); //create database db file if not exist
        SQLiteDatabase db = masterHelper.getWritableDatabase();  //get the created database db file
        daoMaster = new DaoMaster(db);//create masterDao
        daoSession = daoMaster.newSession(); //Creates Session session
        return daoSession.getProductosDao();
    }

    /**
     * Mover registros
     *
     * @param v
     */
    private void moverRegistros(View v) {
        cargarTodos();
        if (get_ProductosList() != null) {
            {
                glbtnMover.setVisibility(View.VISIBLE);
                switch (v.getId()) {
                    case R.id.ibFirst_Prod:
                        setPosition(0);
                        break;
                    case R.id.ibPrev_Prod:
                        setPosition(getPosition() - 1 < 0 ? 0 : getPosition() - 1);
                        break;
                    case R.id.ibNext_Prod:
                        setPosition(getPosition() + 1 > get_ProductosList().size() - 1 ? get_ProductosList().size() - 1 : getPosition() + 1);
                        break;
                    case R.id.ibLast_Prod:
                        setPosition(get_ProductosList().size() - 1);
                        break;
                }
                setProductoActual(get_ProductosList().get((int) getPosition()));
                LiberarControles(false);
            }
        } else
            glbtnMover.setVisibility(View.INVISIBLE);
    }

    //endregion

    private void loadSpinnerImpuestos() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.kssImpuestos, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.spImpuestos_Prod.setAdapter(adapter);
        this.spImpuestos_Prod.setOnItemSelectedListener(this);
    }

    private void loadSpinnercategorias() {
        try {
            if (daoMaster != null) {
                DaoSession daoSessionCategorias = daoMaster.newSession();
                Productos_FamiliaDao categoriaDao = daoSessionCategorias.getProductos_FamiliaDao();
                List<Productos_Familia> CategoriasList = categoriaDao.queryBuilder().orderAsc(Productos_FamiliaDao.Properties.Id).build().list();
                if (CategoriasList != null)
                    if (CategoriasList.size() > 0) {
                        String[] sCategorias = new String[CategoriasList.size()];
                        int i = 0;
                        for (Productos_Familia categorias : CategoriasList) {
                            sCategorias[i] = categorias.getFamiliaProducto();
                            i++;
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sCategorias);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        this.spCategorias_Prod.setAdapter(adapter);
                        this.spCategorias_Prod.setOnItemSelectedListener(this);
                    } else {
                        ToastManager.show(this, "No existen Categorias para crear un Producto.\n" +
                                        "Por favor dirijase a: Configuración > Categorías y debe crear algunas para continuar.",
                                enuToastIcons.ERROR, 50);
                    }
            } else
                Toast.makeText(this, "Master DB no iniciada", Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Toast.makeText(this, this.getString(R.string.kssEx) + ex.toString(), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Activa o desactiva los controles segun el status del registro
     *
     * @param esActivar
     */
    private void LiberarControles(boolean esActivar) {
        etNombre_Prod.setEnabled(esActivar);
        etDesc_Prod.setEnabled(esActivar);
        etPrecioC_Prod.setEnabled(esActivar);
        etPrecioV_Prod.setEnabled(esActivar);
        spCategorias_Prod.setEnabled(esActivar);
        spImpuestos_Prod.setEnabled(esActivar);
        ivTakeImage_Prod.setEnabled(esActivar);
        llSaveCancel.setVisibility(!esActivar ? View.INVISIBLE : View.VISIBLE);

        if (llSaveCancel.getVisibility() == View.VISIBLE) {
            llTopBar.setEnabled(false);
            ivAdd.setVisibility(View.GONE);
            ivBuscar.setVisibility(View.GONE);
            acBuscar.setVisibility(View.GONE);
        } else {
            llTopBar.setEnabled(true);
            ivAdd.setVisibility(View.VISIBLE);
            ivBuscar.setVisibility(View.VISIBLE);
            acBuscar.setVisibility(View.VISIBLE);
        }

        if (llSaveCancel.getVisibility() == View.VISIBLE)
            glbtnMover.setVisibility(View.INVISIBLE);
        else {
            if (productoDao.count() > 0)
                glbtnMover.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Limpieza de controles
     */
    private void Reset() {
        etNombre_Prod.setText("");
        etDesc_Prod.setText("");
        etPrecioC_Prod.setText("");
        etPrecioV_Prod.setText("");
        tvCodigo_Prod.setText(String.format("Código: %03d", proximoID));
        ivTakeImage_Prod.setImageResource(R.drawable.kss_camara);
    }

    /**
     * Rellena los campos del Formulario Activity
     */
    private void fillForm(Productos ProductoActual) {
        try {
            if (ProductoActual != null) {
                etNombre_Prod.setText(ProductoActual.getProducto());
                etDesc_Prod.setText(ProductoActual.getProducto_DescripcionLarga());
                etPrecioC_Prod.setText(String.valueOf(ProductoActual.getMontoUltimoCosto()));
                etPrecioV_Prod.setText(String.valueOf(ProductoActual.getMontoPrecio1()));
                spCategorias_Prod.setSelection(ProductoActual.getId_familiaProducto() - 1);
                spImpuestos_Prod.setSelection(ProductoActual.getId_Impuesto() != null ? ProductoActual.getId_Impuesto() : 1);
                tvCodigo_Prod.setText(String.format("Código: %03d", ProductoActual.getId()));
                Bitmap bitmap = showBitmapFromFile(this, rutaFotos + String.valueOf(ProductoActual.getId()) + ".jpg");
                if (bitmap != null)
                    ivTakeImage_Prod.setImageBitmap(bitmap);
                else
                    ivTakeImage_Prod.setImageResource(R.drawable.kss_camara);
            }
        } catch (Exception ex) {
            Toast.makeText(this, this.getString(R.string.kssEx) + ex.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Acciones de Botones
     */
    private View.OnClickListener OnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tvSave_Prod:
                    long IDActual;
                    if ((IDActual = guardarDatos()) > 0) {
                        setProductoActual(productoDao.load(IDActual));
                        String Datos = "[" + getProductoActual().getId() + "]" + getProductoActual().getProducto();
                        Toast.makeText(v.getContext(),
                                getenuStatusregistro() == ACTUALIZANDO ? v.getContext().getString(R.string.kssMsjDatosActualizados) + Datos.toUpperCase() :
                                        v.getContext().getString(R.string.kssMsjDatosADD) + Datos.toUpperCase(),
                                Toast.LENGTH_SHORT).show();
                        setenuStatus(CONSULTANDO);
                    }
                    break;
                case R.id.tvCancel_Prod:
                    if (productoDao.count() > 0) {
                        if (getenuStatusregistro() == AGREGANDO)
                            moverRegistros(ibPrev);
                        cargarTodos();
                        setProductoActual(get_ProductosList().get(((int) getPosition())));
                        Toast.makeText(v.getContext(), v.getContext().getString(R.string.kssMsjCancelEdit), Toast.LENGTH_SHORT).show();
                    }
                    setenuStatus(CONSULTANDO);
                    break;
                case R.id.ivAdd_Prod:
                    setenuStatus(AGREGANDO);
                    spCategorias_Prod.setSelection(0);
                    spImpuestos_Prod.setSelection(1);
                    Reset();
                    proximoID = getMaxId() + 1;
                    etNombre_Prod.requestFocus();
                    break;
                case R.id.ivEdit_Prod:
                    if (getProductoActual() != null) {
                        setenuStatus(EDITANDO);
                        etNombre_Prod.requestFocus();
                    }
                    break;
                case R.id.ivDel_Prod:
                    if (getProductoActual() != null) {
                        if (eliminarDatos(getProductoActual())) {
                            setenuStatus(ELIMINANDO);
                            moverRegistros(ibPrev);
                            cargarTodos();
                            setProductoActual(get_ProductosList().get(((int) getPosition())));
                        }
                    }
                    setenuStatus(CONSULTANDO);
                    break;
                case R.id.ibFirst_Prod:
                case R.id.ibPrev_Prod:
                case R.id.ibNext_Prod:
                case R.id.ibLast_Prod:
                    setenuStatus(CONSULTANDO);
                    moverRegistros(v);
                    break;
                case R.id.ivBuscar_Prod:
                    setenuStatus(CONSULTANDO);
                    buscarDatos();
                    break;
                case R.id.ivTakeImage_Prod:
                    tomarFoto(v);
                    setenuStatus(CONSULTANDO);
                    break;
                case R.id.tvZoom_Prod:
                    if (getProductoActual() != null) {
                        String ruta = rutaFotos + String.valueOf(getProductoActual().getId()) + ".jpg";
                        showImage(v.getContext(), ruta);
                    }
                    break;
                case R.id.ivExit_Prod:
                    finish();
                    break;
                case R.id.ibHome_Prod:
                    finish();
                    break;
            }
        }
    };

}
