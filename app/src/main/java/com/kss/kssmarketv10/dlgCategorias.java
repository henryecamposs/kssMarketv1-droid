package com.kss.kssmarketv10;

import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabaseCorruptException;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kss.kssmarketv10.db.Productos_Familia;
import com.kss.kssmarketv10.db.dao.DaoMaster;
import com.kss.kssmarketv10.db.dao.DaoSession;
import com.kss.kssmarketv10.db.dao.Productos_FamiliaDao;
import com.kss.kssutil.ToastManager;
import com.kss.kssutil.enuStatusRegistro;
import com.kss.kssutil.enuToastIcons;

import java.util.List;

import de.greenrobot.dao.query.Query;

import static com.kss.kssutil.enuStatusRegistro.ACTUALIZANDO;
import static com.kss.kssutil.enuStatusRegistro.AGREGANDO;
import static com.kss.kssutil.enuStatusRegistro.CONSULTANDO;
import static com.kss.kssutil.enuStatusRegistro.EDITANDO;
import static com.kss.kssutil.enuStatusRegistro.ELIMINANDO;
import static com.kss.kssutil.enuStatusRegistro.NONE;

public class dlgCategorias extends AppCompatActivity {

    private kssSettings setting;
    //Variables propias
    private EditText nombreCategoria;
    private ImageButton ibFirst;
    private ImageButton ibPrev;
    private ImageButton ibNext;
    private ImageButton ibLast;
    private TextView tvNumReg_Cat;
    private LinearLayout llSaveCancel;
    private GridLayout glbtnMover;
    private ImageView ivAdd;
    private ImageView ivDel;
    private ImageView ivEdit;
    private ImageView ivBuscar;
    private AutoCompleteTextView etBuscar;
    private LinearLayout llTopBar;

    //Base de datos
    //Variables relativas a Green Dao Database
    public DaoSession daoSession;
    public DaoMaster daoMaster;
    private Productos_FamiliaDao categoriasDao;
    private SQLiteDatabase db;
    private long proximoID;
    private long maxRegistros;
    private long _position;

    private Productos_Familia _CatActual;
    private AutoCompleteTextView acBuscar_Cat;
    private List<Productos_Familia> categoriaList_result;

    //Respuestas msgs
    enum Answer {
        YES, NO, ERROR
    }

    static dlgClientes.Answer choice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dlg_categorias);

        //Obtiene el texto del EditText
        nombreCategoria = (EditText) findViewById(R.id.etNombre_Cat);
        tvNumReg_Cat = (TextView) findViewById(R.id.tvNumReg_Cat);

        glbtnMover = (GridLayout) findViewById(R.id.glbtnMover_Cat);
        etBuscar = (AutoCompleteTextView) findViewById(R.id.acBuscar_Cat);
        llTopBar = (LinearLayout) findViewById(R.id.lltopBar_Cat);

        final TextView tvSave = (TextView) findViewById(R.id.tvSave_Cat);
        final TextView tvcancel = (TextView) findViewById(R.id.tvCancel_Cat);
        ivAdd = (ImageView) findViewById(R.id.ivAdd_Cat);
        ivDel = (ImageView) findViewById(R.id.ivDel_Cat);
        ivEdit = (ImageView) findViewById(R.id.ivEdit_Cat);
        ivBuscar = (ImageView) findViewById(R.id.ivBuscar_Cat);
        tvcancel.setOnClickListener(OnClickListener);
        tvSave.setOnClickListener(OnClickListener);
        ivAdd.setOnClickListener(OnClickListener);
        ivDel.setOnClickListener(OnClickListener);
        ivEdit.setOnClickListener(OnClickListener);
        ivBuscar.setOnClickListener(OnClickListener);
        //Oculatr Editores
        ivEdit.setVisibility(View.GONE);
        ivDel.setVisibility(View.GONE);
        ibFirst = (ImageButton) findViewById(R.id.ibFirst_Cat);
        ibPrev = (ImageButton) findViewById(R.id.ibPrev_Cat);
        ibNext = (ImageButton) findViewById(R.id.ibNext_Cat);
        ibLast = (ImageButton) findViewById(R.id.ibLast_Cat);
        ibFirst.setOnClickListener(OnClickListener);
        ibPrev.setOnClickListener(OnClickListener);
        ibNext.setOnClickListener(OnClickListener);
        ibLast.setOnClickListener(OnClickListener);
        llSaveCancel = (LinearLayout) findViewById(R.id.llSaveCancel_Cat);
        acBuscar_Cat = (AutoCompleteTextView) findViewById(R.id.acBuscar_Cat);
        final ImageView ivExit = (ImageView) findViewById(R.id.ivExit_Cat);
        final ImageButton ibHome = (ImageButton) findViewById(R.id.ibHome_Cat);
        ivExit.setOnClickListener(OnClickListener);
        ibHome.setOnClickListener(OnClickListener);

        setting = kssSettings.getInstance(this);
        //Inicializa las operaciones en la Base de Datos
        categoriasDao = setupDb();
        setenuStatus(NONE);
        setPosition(0);
        //mediante su ID recibida mediante startActivityForResut()
        Bundle extras = getIntent().getExtras();
        List<Productos_Familia> ClientesList = cargarTodos();
        if (ClientesList != null) {
            if (extras != null) {
                long id = extras.getLong("ID");
                setCategoriaActual(categoriasDao.load(id));
            } else {
                setCategoriaActual(ClientesList.get(0));
            }
            setenuStatus(CONSULTANDO);
        } else {
            setenuStatus(NONE);
            ToastManager.show(this, "No existen datos!", enuToastIcons.INFORMATION, 10);
            glbtnMover.setVisibility(View.INVISIBLE);
            tvNumReg_Cat.setText("0 de 0");
        }
        loadAcCategorias();
    }

    private long getPosition() {
        return _position;
    }

    private void setPosition(long value) {
        _position = value;
        ibFirst.setVisibility(value == 0 ? View.INVISIBLE : View.VISIBLE);
        ibPrev.setVisibility(value == 0 ? View.INVISIBLE : View.VISIBLE);
        ibNext.setVisibility(value == maxRegistros - 1 ? View.INVISIBLE : View.VISIBLE);
        ibLast.setVisibility(value == maxRegistros - 1 ? View.INVISIBLE : View.VISIBLE);
        tvNumReg_Cat.setText(String.format("%1$d de %2$d", value + 1, maxRegistros));
    }

    private void loadAcCategorias() {
        try {
            if (daoMaster != null) {
                DaoSession daoSessionCategorias1 = daoMaster.newSession();
                Productos_FamiliaDao categoriaDao = daoSessionCategorias1.getProductos_FamiliaDao();
                List<Productos_Familia> CategoriasList = categoriaDao.queryBuilder().orderAsc(Productos_FamiliaDao.Properties.Id).build().list();
                String[] sCategorias = new String[CategoriasList.size()];
                int i = 0;
                for (Productos_Familia categorias : CategoriasList) {
                    sCategorias[i] = categorias.getFamiliaProducto();
                    i++;
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, sCategorias);
                this.etBuscar.setAdapter(adapter);
            } else
                Toast.makeText(this, "Master DB no iniciada", Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Toast.makeText(this, this.getString(R.string.kssEx) + ex.toString(), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Cliente
     *
     * @return
     */
    private Productos_Familia getCategoriaActual() {
        return _CatActual;
    }

    private void setCategoriaActual(Productos_Familia value) {
        _CatActual = value;
        List<Productos_Familia> ClientesList = cargarTodos();
        if (value != null) {
            setPosition(ClientesList.indexOf(value));
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
            if (getCategoriaActual() != null) {
                ivEdit.setVisibility(View.VISIBLE);
                ivDel.setVisibility(View.VISIBLE);
            }
        } else {
            ivEdit.setVisibility(View.GONE);
            ivDel.setVisibility(View.GONE);
        }


    }

    /**
     * Inicia la Sesion de Trabajo con la base de datos
     *
     * @return
     */
    private Productos_FamiliaDao setupDb() {
        DaoMaster.DevOpenHelper masterHelper = new DaoMaster.DevOpenHelper(this, "kssmarket_db", null); //create database db file if not exist
        SQLiteDatabase db = masterHelper.getWritableDatabase();  //get the created database db file
        daoMaster = new DaoMaster(db);//create masterDao
        daoSession = daoMaster.newSession(); //Creates Session session
        return daoSession.getProductos_FamiliaDao();
    }

    /**
     * Mover registros
     *
     * @param v
     */
    private void moverRegistros(View v) {
        List<Productos_Familia> ClientesList = cargarTodos();

        if (ClientesList != null) {
            {
                glbtnMover.setVisibility(View.VISIBLE);
                switch (v.getId()) {
                    case R.id.ibFirst_Cat:
                        setPosition(0);
                        break;
                    case R.id.ibPrev_Cat:
                        setPosition(getPosition() - 1 < 0 ? 0 : getPosition() - 1);
                        break;
                    case R.id.ibNext_Cat:
                        setPosition(getPosition() + 1 > ClientesList.size() - 1 ? ClientesList.size() - 1 : getPosition() + 1);
                        break;
                    case R.id.ibLast_Cat:
                        setPosition(ClientesList.size() - 1);
                        break;
                }
                setCategoriaActual(ClientesList.get((int) getPosition()));
                LiberarControles(false);
            }
        } else
            glbtnMover.setVisibility(View.INVISIBLE);
    }

    /**
     * Activa o desactiva los controles segun el status del registro
     *
     * @param esActivar
     */
    private void LiberarControles(boolean esActivar) {
        nombreCategoria.setEnabled(esActivar);

        llSaveCancel.setVisibility(!esActivar ? View.INVISIBLE : View.VISIBLE);

        if (llSaveCancel.getVisibility() == View.VISIBLE) {
            llTopBar.setEnabled(false);
            ivAdd.setVisibility(View.GONE);
            ivBuscar.setVisibility(View.GONE);
            etBuscar.setVisibility(View.GONE);
        } else {
            llTopBar.setEnabled(true);
            ivAdd.setVisibility(View.VISIBLE);
            ivBuscar.setVisibility(View.VISIBLE);
            etBuscar.setVisibility(View.VISIBLE);
        }

        if (llSaveCancel.getVisibility() == View.VISIBLE)
            glbtnMover.setVisibility(View.INVISIBLE);
        else {
            if (categoriasDao.count() > 0)
                glbtnMover.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Limpieza de controles
     */
    private void Reset() {
        nombreCategoria.setText("");
    }

    /**
     * Se asegura que los datos requeridos esten completos
     *
     * @return
     */
    private Boolean esDatosCompletos() {
        if (nombreCategoria.getText().toString().trim().length() == 0) return false;
        return true;
    }

    /**
     * Obtiene el categoria first
     *
     * @return
     */
    private List<Productos_Familia> cargarTodos() {
        try {
            List<Productos_Familia> clientesList = categoriasDao.queryBuilder().orderAsc(Productos_FamiliaDao.Properties.Id).build().list();
            if (clientesList.size() > 0) {
                maxRegistros = clientesList.size();
                return clientesList;
            }
            setenuStatus(CONSULTANDO);
            return null;
        } catch (SQLiteDatabaseCorruptException sqlex) {
            Toast.makeText(this, this.getString(R.string.kssEx) + sqlex.toString(), Toast.LENGTH_SHORT).show();
            return null;
        } catch (Exception ex) {
            Toast.makeText(this, this.getString(R.string.kssEx) + ex.toString(), Toast.LENGTH_SHORT).show();
            return null;
        } finally {
        }
    }

    private void buscarDatos() {
        String sCategoria=acBuscar_Cat.getText().toString().trim();
        if (sCategoria.length() > 0) {
            //Buscar por Categoria
            Query<Productos_Familia> query = categoriasDao.queryBuilder().where(
                    Productos_FamiliaDao.Properties.FamiliaProducto.eq(sCategoria.trim())
            ).build();
            categoriaList_result = query.list();
            if (categoriaList_result.size() > 0) {
                setCategoriaActual(categoriaList_result.get(0));
            } else
                ToastManager.show(this, "No existe la Categoria Seleccionada.\nPor favor Seleccione la Correcta!", enuToastIcons.ERROR, 10);
        } else
            ToastManager.show(this, "Por favor indique la categor�a.", enuToastIcons.INFORMATION, 10);

    }

    /**
     * Guardar Datos en la base de Datos
     */
    public long guardarDatos() {
        if (esDatosCompletos()) {
            //Se crea nuevo Cliente
            Productos_Familia categoria = new Productos_Familia();
            if (getenuStatusregistro() == AGREGANDO)
                categoria.setId(proximoID);
            else
                categoria.setId(getCategoriaActual().getId());
            categoria.setFamiliaProducto(nombreCategoria.getText().toString());
            return SaveToSQL(categoria, getenuStatusregistro());
        }
        ToastManager.show(this, "Datos Incompletos!.\nPor favor verifique e intente de nuevo.", enuToastIcons.ERROR, 10);
        return 0;
    }

    /**
     * Salvar insertar Datos
     *
     * @param entidad
     * @return
     */
    private Long SaveToSQL(Productos_Familia entidad, enuStatusRegistro enuAciion) {
        try {
            long IDActual;
            if (enuAciion == AGREGANDO)
                IDActual = categoriasDao.insert(entidad);
            else {
                categoriasDao.update(entidad);
                IDActual = entidad.getId();
            }
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
     * @param categoria
     * @return
     */
    private Boolean eliminarDatos(final Productos_Familia categoria) {
        try {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle(this.getString(R.string.kssEliminarRegistro));
            alertDialogBuilder
                    .setIcon(R.drawable.ic_launcher)
                    .setMessage("Realmente desea eliminar: " + categoria.getFamiliaProducto().toUpperCase())
                    .setCancelable(false)//Disable back button action
                    .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            categoriasDao.delete(categoria);
                            moverRegistros(ibPrev);
                            // Toast.makeText(this,  getString(R.string.kssRegistroEliminado), Toast.LENGTH_SHORT).show();
                            choice = dlgClientes.Answer.YES;
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //Toast.makeText(this, this.getString(R.string.ksscancelar), Toast.LENGTH_SHORT).show();
                            choice = dlgClientes.Answer.NO;
                            dialog.cancel();
                        }
                    });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
            return choice == dlgClientes.Answer.YES ? true : false;
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
        if (categoriasDao.count() > 0) {
            List<Productos_Familia> maxPostIdRow = categoriasDao.queryBuilder().where(Productos_FamiliaDao.Properties.Id.isNotNull()).orderDesc(Productos_FamiliaDao.Properties.Id).limit(1).list();
            maxPostId = maxPostIdRow.get(0).getId();
        } else
            maxPostId = (long) 0;
        return maxPostId;
    }

    /**
     * Rellena los campos del Formulario Activity
     */
    private void fillForm(Productos_Familia clienteActual) {
        if (clienteActual != null) {
            nombreCategoria.setText(clienteActual.getFamiliaProducto());
        }
    }

    /**
     * Acciones de Botones
     */
    private View.OnClickListener OnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tvSave_Cat:
                    long IDActual;
                    if ((IDActual = guardarDatos()) > 0) {
                        setCategoriaActual(categoriasDao.load(IDActual));
                        String Datos = getCategoriaActual().getFamiliaProducto();
                        Toast.makeText(v.getContext(),
                                getenuStatusregistro() == ACTUALIZANDO ? v.getContext().getString(R.string.kssMsjDatosActualizados) + Datos.toUpperCase() :
                                        v.getContext().getString(R.string.kssMsjDatosADD) + Datos.toUpperCase(),
                                Toast.LENGTH_SHORT).show();
                        setenuStatus(CONSULTANDO);
                    }
                    break;
                case R.id.tvCancel_Cat:
                    if (categoriasDao.count() > 0) {
                        if (getenuStatusregistro() == AGREGANDO)
                            moverRegistros(ibPrev);
                        setCategoriaActual(cargarTodos().get(((int) getPosition())));
                        Toast.makeText(v.getContext(), v.getContext().getString(R.string.kssMsjCancelEdit), Toast.LENGTH_SHORT).show();
                    }
                    setenuStatus(CONSULTANDO);
                    break;
                case R.id.ivAdd_Cat:
                    setenuStatus(AGREGANDO);
                    Reset();
                    proximoID = getMaxId() + 1;
                    nombreCategoria.requestFocus();
                    break;
                case R.id.ivEdit_Cat:
                    if (getCategoriaActual() != null) {
                        setenuStatus(EDITANDO);
                        nombreCategoria.requestFocus();
                    }
                    break;
                case R.id.ivDel_Cat:
                    if (getCategoriaActual() != null) {
                        if (eliminarDatos(getCategoriaActual())) {
                            setenuStatus(ELIMINANDO);
                            moverRegistros(ibPrev);
                            setCategoriaActual(cargarTodos().get(((int) getPosition())));
                        }
                    }
                    setenuStatus(CONSULTANDO);
                    break;
                case R.id.ibFirst_Cat:
                case R.id.ibPrev_Cat:
                case R.id.ibNext_Cat:
                case R.id.ibLast_Cat:
                    setenuStatus(CONSULTANDO);
                    moverRegistros(v);
                    break;
                case R.id.ivBuscar_Cat:
                    setenuStatus(CONSULTANDO);
                    buscarDatos();
                    break;
                case R.id.ivExit_Cat:
                    finish();
                    break;
                case R.id.ibHome_Cat:
                    finish();
                    break;
            }
        }
    };
}
