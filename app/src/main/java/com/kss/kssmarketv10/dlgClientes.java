package com.kss.kssmarketv10;

import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabaseCorruptException;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kss.kssmarketv10.db.Clientes;
import com.kss.kssmarketv10.db.dao.ClientesDao;
import com.kss.kssmarketv10.db.dao.DaoMaster;
import com.kss.kssmarketv10.db.dao.DaoSession;
import com.kss.kssutil.ToastManager;
import com.kss.kssutil.enuStatusRegistro;
import com.kss.kssutil.enuToastIcons;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.dao.query.Query;

import static com.kss.kssutil.enuStatusRegistro.ACTUALIZANDO;
import static com.kss.kssutil.enuStatusRegistro.AGREGANDO;
import static com.kss.kssutil.enuStatusRegistro.CONSULTANDO;
import static com.kss.kssutil.enuStatusRegistro.EDITANDO;
import static com.kss.kssutil.enuStatusRegistro.ELIMINANDO;
import static com.kss.kssutil.enuStatusRegistro.NONE;

public class dlgClientes extends AppCompatActivity {

    private kssSettings setting;

    //Variables propias
    private EditText nombreclie;
    private EditText cirif;
    private EditText numTelf;
    private EditText contacto;
    private EditText email;
    private EditText categorias;
    private ImageButton ibFirst;
    private ImageButton ibPrev;
    private ImageButton ibNext;
    private ImageButton ibLast;
    private TextView tvRegistros;
    private LinearLayout llSaveCancel;
    private GridLayout glbtnMover;
    private ImageView ivAdd;
    private ImageView ivDel;
    private ImageView ivEdit;
    private ImageView ivBuscar;
    private AutoCompleteTextView acBuscar;
    private LinearLayout llTopBar;

    //Base de datos
    //Variables relativas a Green Dao Database
    public DaoSession daoSession;
    public DaoMaster daoMaster;
    private ClientesDao clientesDao;
    private SQLiteDatabase db;
    private long proximoID;
    private long maxRegistros;
    private long _position;

    private Clientes _clienteActual;
    private List<Clientes> _ClientesList;
    private SearchItemArrayAdapter searchItemArrayAdapter = null;

    public List<Clientes> get_ClientesList() {
        return _ClientesList;
    }
    public void set_ClientesList(List<Clientes> _ClientesList) {
        this._ClientesList = _ClientesList;
    }

    //Respuestas msgs
    enum Answer {
        YES, NO, ERROR
    }
    static Answer choice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dlg_clientes);

        //Obtiene el texto del EditText
        nombreclie = (EditText) findViewById(R.id.etNombre_Clie);
        cirif = (EditText) findViewById(R.id.etCIRIF_Clie);
        numTelf = (EditText) findViewById(R.id.etNumTelf_Clie);
        contacto = (EditText) findViewById(R.id.etContacto_Clie);
        email = (EditText) findViewById(R.id.etEmail_Clie);
        categorias = (EditText) findViewById(R.id.etCategorias);
        tvRegistros = (TextView) findViewById(R.id.tvNumReg_Clie);
        glbtnMover = (GridLayout) findViewById(R.id.glbtnMover_Clie);
        acBuscar = (AutoCompleteTextView) findViewById(R.id.acBuscar_Clie);
        llTopBar = (LinearLayout) findViewById(R.id.lltopBar_Clie);
        final TextView tvSave = (TextView) findViewById(R.id.tvSave_Clie);
        final TextView tvcancel = (TextView) findViewById(R.id.tvCancel_Clie);
        ivAdd = (ImageView) findViewById(R.id.ivAdd_Clie);
        ivDel = (ImageView) findViewById(R.id.ivDel_Clie);
        ivEdit = (ImageView) findViewById(R.id.ivEdit_Clie);
        ivBuscar = (ImageView) findViewById(R.id.ivBuscar_Clie);
        ibFirst = (ImageButton) findViewById(R.id.ibFirst_Clie);
        ibPrev = (ImageButton) findViewById(R.id.ibPrev_Clie);
        ibNext = (ImageButton) findViewById(R.id.ibNext_Clie);
        ibLast = (ImageButton) findViewById(R.id.ibLast_Clie);
        llSaveCancel = (LinearLayout) findViewById(R.id.llSaveCancel_Clie);
        final ImageView ivExit = (ImageView) findViewById(R.id.ivExit_Clie);
        final ImageButton ibHome = (ImageButton) findViewById(R.id.ibHome_Clie);
        try {
            ivExit.setOnClickListener(OnClickListener);
            ibHome.setOnClickListener(OnClickListener);
            ibFirst.setOnClickListener(OnClickListener);
            ibPrev.setOnClickListener(OnClickListener);
            ibNext.setOnClickListener(OnClickListener);
            ibLast.setOnClickListener(OnClickListener);
            tvcancel.setOnClickListener(OnClickListener);
            tvSave.setOnClickListener(OnClickListener);
            ivAdd.setOnClickListener(OnClickListener);
            ivDel.setOnClickListener(OnClickListener);
            ivEdit.setOnClickListener(OnClickListener);
            ivBuscar.setOnClickListener(OnClickListener);

            setting = kssSettings.getInstance(this);
            ivEdit.setVisibility(View.GONE);
            ivDel.setVisibility(View.GONE);
            clientesDao = setupDb();
            cargarTodos();
            loadAcClientes();
            setenuStatus(NONE);
            setPosition(0);

            Bundle extras = getIntent().getExtras();
            if (get_ClientesList() != null) {
                if (extras != null) {
                    long id = extras.getLong("ID");
                    setClienteActual(clientesDao.load(id));
                } else {
                    setClienteActual(get_ClientesList().get(0));
                }
                setenuStatus(CONSULTANDO);
            } else {
                setenuStatus(NONE);
                ToastManager.show(this, "No existen datos!", enuToastIcons.INFORMATION, 10);
                glbtnMover.setVisibility(View.INVISIBLE);
                tvRegistros.setText("0 de 0");
            }

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

    private long getPosition() {
        return _position;
    }

    private void setPosition(long value) {
        _position = value;
        ibFirst.setVisibility(value == 0 ? View.INVISIBLE : View.VISIBLE);
        ibPrev.setVisibility(value == 0 ? View.INVISIBLE : View.VISIBLE);
        ibNext.setVisibility(value == maxRegistros - 1 ? View.INVISIBLE : View.VISIBLE);
        ibLast.setVisibility(value == maxRegistros - 1 ? View.INVISIBLE : View.VISIBLE);
        tvRegistros.setText(String.format("%1$d de %2$d", value + 1, maxRegistros));
    }

    /**
     * Cliente
     *
     * @return
     */
    private Clientes getclienteActual() {
        return _clienteActual;
    }

    private void setClienteActual(Clientes value) {
        _clienteActual = value;
         cargarTodos();
        if (value != null) {
            setPosition(get_ClientesList().indexOf(value));
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
            if (getclienteActual() != null) {
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
    private ClientesDao setupDb() {
        DaoMaster.DevOpenHelper masterHelper = new DaoMaster.DevOpenHelper(this, setting.getNombreBD(), null); //create database db file if not exist
        SQLiteDatabase db = masterHelper.getWritableDatabase();  //get the created database db file
        daoMaster = new DaoMaster(db);//create masterDao
        daoSession = daoMaster.newSession(); //Creates Session session
        return daoSession.getClientesDao();
    }

    /**
     * Mover registros
     *
     * @param v
     */
    private void moverRegistros(View v) {
         cargarTodos();

        if (get_ClientesList() != null) {
            {
                glbtnMover.setVisibility(View.VISIBLE);
                switch (v.getId()) {
                    case R.id.ibFirst_Clie:
                        setPosition(0);
                        break;
                    case R.id.ibPrev_Clie:
                        setPosition(getPosition() - 1 < 0 ? 0 : getPosition() - 1);
                        break;
                    case R.id.ibNext_Clie:
                        setPosition(getPosition() + 1 > get_ClientesList().size() - 1 ? get_ClientesList().size() - 1 : getPosition() + 1);
                        break;
                    case R.id.ibLast_Clie:
                        setPosition(get_ClientesList().size() - 1);
                        break;
                }
                setClienteActual(get_ClientesList().get((int) getPosition()));
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
        nombreclie.setEnabled(esActivar);
        cirif.setEnabled(esActivar);
        numTelf.setEnabled(esActivar);
        contacto.setEnabled(esActivar);
        email.setEnabled(esActivar);
        categorias.setEnabled(esActivar);
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
            if (clientesDao.count() > 0)
                glbtnMover.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Limpieza de controles
     */
    private void Reset() {
        nombreclie.setText("");
        cirif.setText("");
        numTelf.setText("");
        contacto.setText("");
        email.setText("");
        categorias.setText("");
    }

    /**
     * Se asegura que los datos requeridos esten completos
     *
     * @return
     */
    private Boolean esDatosCompletos() {
        if (cirif.getText().toString().trim().length() == 0) return false;
        if (nombreclie.getText().toString().trim().length() == 0) return false;
        if (numTelf.getText().toString().trim().length() == 0) return false;
        if (contacto.getText().toString().trim().length() == 0) return false;
        if (email.getText().toString().trim().length() == 0) return false;
        if (categorias.getText().toString().trim().length() == 0) return false;
        if (email.getText().toString().trim().length() == 0) return false;
        return true;
    }

    private ArrayList<itemrow_image> populateProductosRowData(List<Clientes> rows) {
        ArrayList<itemrow_image> itemrow_images = new ArrayList<>();
        for (Clientes item : rows
                ) {
            itemrow_images.add(new itemrow_image(item.getNombres(),
                    0.00,
                    null,
                    item.getId()));
        }
        return itemrow_images;
    }

    private void loadAcClientes() {
        try {
            if (daoMaster != null) {
                if (get_ClientesList().size() > 0) {
                    searchItemArrayAdapter = new SearchItemArrayAdapter(this, R.layout.searchitem_image, populateProductosRowData(get_ClientesList()));
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
     * Obtiene el cliente first
     *
     * @return
     */
    private  void cargarTodos() {
        try {
            set_ClientesList( clientesDao.queryBuilder().orderAsc(ClientesDao.Properties.Id).build().list());
            if (get_ClientesList().size() > 0) {
                maxRegistros = get_ClientesList().size();
            }
            setenuStatus(CONSULTANDO);
        } catch (SQLiteDatabaseCorruptException sqlex) {
            Toast.makeText(this, this.getString(R.string.kssEx) + sqlex.toString(), Toast.LENGTH_SHORT).show();
        } catch (Exception ex) {
            Toast.makeText(this, this.getString(R.string.kssEx) + ex.toString(), Toast.LENGTH_SHORT).show();
        } finally {
        }
    }

    private void buscarDatos() {
        //Buscar por Cliente
        String sCliente = acBuscar.getText().toString().trim();
        if (sCliente.length() > 0) {
            Query<Clientes> query = clientesDao.queryBuilder().where(
                    ClientesDao.Properties.Nombres.eq(sCliente.trim())
            ).build();
            set_ClientesList(query.list());
            if (get_ClientesList().size() > 0) {
                setClienteActual(get_ClientesList().get(0));
            } else {
                //Busqueda: contiene
                query = clientesDao.queryBuilder().where(
                        ClientesDao.Properties.Nombres.like("%" + sCliente + "%")
                ).build();
                set_ClientesList(query.list());
                if (get_ClientesList().size() == 0) {
                    ToastManager.show(this, "No existe el Cliente indicado.", enuToastIcons.ERROR, 10);
                    return;
                }
            }
        } else
            ToastManager.show(this, "Por favor indique el Cliente.", enuToastIcons.INFORMATION, 10);
    }

    /**
     * Guardar Datos en la base de Datos
     */
    public long guardarDatos() {
        if (esDatosCompletos()) {
            //Se crea nuevo Cliente
            Clientes cliente = new Clientes();
            if (getenuStatusregistro() == AGREGANDO)
                cliente.setId(proximoID);
            else
                cliente.setId(getclienteActual().getId());
            cliente.setCI(cirif.getText().toString());
            cliente.setNombres(nombreclie.getText().toString());
            cliente.setTelefono(numTelf.getText().toString());
            cliente.setApellidos(contacto.getText().toString());
            cliente.setEmail(email.getText().toString());
            cliente.setCategorias(categorias.getText().toString());
            cliente.setDireccion(email.getText().toString());
            return SaveToSQL(cliente, getenuStatusregistro());
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
    private Long SaveToSQL(Clientes entidad, enuStatusRegistro enuAciion) {
        try {
            long IDActual;
            if (enuAciion == AGREGANDO)
                IDActual = clientesDao.insert(entidad);
            else {
                clientesDao.update(entidad);
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
     * @param cliente
     * @return
     */
    private Boolean eliminarDatos(final Clientes cliente) {
        try {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle(this.getString(R.string.kssEliminarRegistro));
            alertDialogBuilder
                    .setIcon(R.drawable.ic_launcher)
                    .setMessage("Realmente desea eliminar: " + cliente.getNombres().toUpperCase())
                    .setCancelable(false)//Disable back button action
                    .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            clientesDao.delete(cliente);
                            moverRegistros(ibPrev);
                            // Toast.makeText(this,  getString(R.string.kssRegistroEliminado), Toast.LENGTH_SHORT).show();
                            choice = Answer.YES;
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //Toast.makeText(this, this.getString(R.string.ksscancelar), Toast.LENGTH_SHORT).show();
                            choice = Answer.NO;
                            dialog.cancel();
                        }
                    });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
            return choice == Answer.YES ? true : false;
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
        if (clientesDao.count() > 0) {
            List<Clientes> maxPostIdRow = clientesDao.queryBuilder().where(ClientesDao.Properties.Id.isNotNull()).orderDesc(ClientesDao.Properties.Id).limit(1).list();
            maxPostId = maxPostIdRow.get(0).getId();
        } else
            maxPostId = (long) 0;
        return maxPostId;
    }

    /**
     * Rellena los campos del Formulario Activity
     */
    private void fillForm(Clientes clienteActual) {
        if (clienteActual != null) {
            nombreclie.setText(clienteActual.getNombres());
            cirif.setText(clienteActual.getCI());
            numTelf.setText(clienteActual.getTelefono());
            contacto.setText(clienteActual.getApellidos());
            email.setText(clienteActual.getEmail());
            categorias.setText(clienteActual.getCategorias());
        }
    }

    /**
     * Acciones de Botones
     */
    private View.OnClickListener OnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tvSave_Clie:
                    long IDActual;
                    if ((IDActual = guardarDatos()) > 0) {
                        setClienteActual(clientesDao.load(IDActual));
                        String Datos = "[" + getclienteActual().getCI() + "]" + getclienteActual().getNombres();
                        Toast.makeText(v.getContext(),
                                getenuStatusregistro() == ACTUALIZANDO ? v.getContext().getString(R.string.kssMsjDatosActualizados) + Datos.toUpperCase() :
                                        v.getContext().getString(R.string.kssMsjDatosADD) + Datos.toUpperCase(),
                                Toast.LENGTH_SHORT).show();
                        setenuStatus(CONSULTANDO);
                    }
                    break;
                case R.id.tvCancel_Clie:
                    if (clientesDao.count() > 0) {
                        if (getenuStatusregistro() == AGREGANDO)
                            moverRegistros(ibPrev);
                        setClienteActual(get_ClientesList().get(((int) getPosition())));
                        Toast.makeText(v.getContext(), v.getContext().getString(R.string.kssMsjCancelEdit), Toast.LENGTH_SHORT).show();
                    }
                    setenuStatus(CONSULTANDO);
                    break;
                case R.id.ivAdd_Clie:
                    setenuStatus(AGREGANDO);
                    Reset();
                    proximoID = getMaxId() + 1;
                    cirif.requestFocus();
                    break;
                case R.id.ivEdit_Clie:
                    if (getclienteActual() != null) {
                        setenuStatus(EDITANDO);
                        cirif.requestFocus();
                    }
                    break;
                case R.id.ivDel_Clie:
                    if (getclienteActual() != null) {
                        if (eliminarDatos(getclienteActual())) {
                            setenuStatus(ELIMINANDO);
                            moverRegistros(ibPrev);
                            setClienteActual(get_ClientesList().get(((int) getPosition())));
                        }
                    }
                    setenuStatus(CONSULTANDO);
                    break;
                case R.id.ibFirst_Clie:
                case R.id.ibPrev_Clie:
                case R.id.ibNext_Clie:
                case R.id.ibLast_Clie:
                    setenuStatus(CONSULTANDO);
                    moverRegistros(v);
                    break;
                case R.id.ivBuscar_Clie:
                    setenuStatus(CONSULTANDO);
                    buscarDatos();
                    break;
                case R.id.ivExit_Clie:
                    finish();
                    break;
                case R.id.ibHome_Clie:
                    finish();
                    break;
            }
        }
    };

}

/*private ClientesDao clientesDao= new ClientesDao(){
        @Override
        public long insert(Clientes entity) {
            long ret ;
            try {
                ret = super.insert(entity);
            } catch (Exception e) {
                ret = 0;
                Log.e("LOG", "fail to insert!!!");
            }
            return ret;
        }
    };*/