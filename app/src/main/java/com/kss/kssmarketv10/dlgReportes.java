package com.kss.kssmarketv10;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabaseCorruptException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.itextpdf.text.DocumentException;
import com.kss.kssmarketv10.kssSettings.enuTipoTabla;
import com.kss.kssmarketv10.db.Clientes;
import com.kss.kssmarketv10.db.Productos;
import com.kss.kssmarketv10.db.Productos_Familia;
import com.kss.kssmarketv10.db.dao.ClientesDao;
import com.kss.kssmarketv10.db.dao.DaoMaster;
import com.kss.kssmarketv10.db.dao.DaoSession;
import com.kss.kssmarketv10.db.dao.ProductosDao;
import com.kss.kssmarketv10.db.dao.Productos_FamiliaDao;
import com.kss.kssmarketv10.email.EMail;
import com.kss.kssmarketv10.email.SendMailTask;
import com.kss.kssmarketv10.reports.reportePDF_ListConImages;
import com.kss.kssutil.ToastManager;
import com.kss.kssutil.enuToastIcons;

import java.io.IOException;
import java.util.List;
import java.util.StringTokenizer;

import de.greenrobot.dao.query.Query;

public class dlgReportes extends AppCompatActivity {

    private static final String TAG = "Creando Reporte PDF";
    private kssSettings settings;
    //Variables relativas a Green Dao Database
    private DaoSession daoSession;
    private DaoMaster daoMaster;
    private ProductosDao productosDao;
    private Productos_FamiliaDao categoriasDao;
    private ClientesDao clientesDao;
    private SQLiteDatabase db;

    public List<Productos> get_ProductosList() {
        return _ProductosList;
    }

    public void set_ProductosList(List<Productos> _ProductosList) {
        if (_ProductosList == null) {
            llSave_Rep.setVisibility(View.INVISIBLE);
        } else {
            if (_ProductosList.size() == 0)
                llSave_Rep.setVisibility(View.INVISIBLE);
            else
                llSave_Rep.setVisibility(View.VISIBLE);
        }
        this._ProductosList = _ProductosList;
    }

    public List<Productos_Familia> get_CategoriasList() {
        return _CategoriasList;
    }

    public void set_CategoriasList(List<Productos_Familia> _CategoriasList) {
        this._CategoriasList = _CategoriasList;
    }

    public List<Clientes> get_ClientesList() {
        return _ClientesList;
    }

    public void set_ClientesList(List<Clientes> _ClientesList) {
        if (_ClientesList == null) {
            llSave_Rep.setVisibility(View.INVISIBLE);
        } else {
            if (_ClientesList.size() == 0)
                llSave_Rep.setVisibility(View.INVISIBLE);
            else
                llSave_Rep.setVisibility(View.VISIBLE);
        }
        this._ClientesList = _ClientesList;
    }

    private List<Productos> _ProductosList;
    private List<Productos_Familia> _CategoriasList;
    private List<Clientes> _ClientesList;

    //Controles
    private AutoCompleteTextView acClientes;
    private AutoCompleteTextView acCategorias;
    private boolean esCatTodos;
    private boolean esClieTodos;
    EditText tvAsunto_Rep;
    TextView tvNombreClie_Rep;
    EditText tvMensaje_Rep;
    private TextView tvEmailClie_Rep;
    private GridLayout glDatosCliente_Rep;
    private TextView tvCategorias_Conf;
    private EMail eMail;
    private String AsuntoPredet =
            "Cat?logo de Precios con Fotos por ";
    private final String MensajePredet =
            "A continuaci?n enviamos un Cat?logo de Precios con fotos de los productos.";
    private LinearLayout llSave_Rep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dlg_reportes);
        final ImageView ivExit = (ImageView) findViewById(R.id.ivExit_Rep);
        final ImageButton ibHome = (ImageButton) findViewById(R.id.ibHome_Rep);
        final TextView tvEnviar = (TextView) findViewById(R.id.tvEnviar_Rep);
        acClientes = (AutoCompleteTextView) findViewById(R.id.acClientes_Rep);
        acCategorias = (AutoCompleteTextView) findViewById(R.id.acCategorias_Rep);
        final RadioGroup rgCat = (RadioGroup) findViewById(R.id.rgCategorias_Rep);
        final RadioGroup rgClie = (RadioGroup) findViewById(R.id.rgClientes_Rep);
        final ImageView ivBuscar_Rep = (ImageView) findViewById(R.id.ivBuscar_Rep);
        glDatosCliente_Rep = (GridLayout) findViewById(R.id.glDatosCliente_Rep);
        tvAsunto_Rep = (EditText) findViewById(R.id.tvAsunto_Rep);
        tvMensaje_Rep = (EditText) findViewById(R.id.tvMensaje_Rep);
        tvNombreClie_Rep = (TextView) findViewById(R.id.tvNombreClie_Rep);
        tvEmailClie_Rep = (TextView) findViewById(R.id.tvEmailClie_Rep);
        tvCategorias_Conf = (TextView) findViewById(R.id.tvCategorias_Rep);
        llSave_Rep = (LinearLayout) findViewById(R.id.llSave_Rep);

        ivExit.setOnClickListener(OnClickListener);
        ibHome.setOnClickListener(OnClickListener);
        tvEnviar.setOnClickListener(OnClickListener);
        ivBuscar_Rep.setOnClickListener(OnClickListener);
        //cargar valores Globales
        settings = kssSettings.getInstance(this);
        AsuntoPredet += settings.getEmpresaNombre();
        //Ejecutar metodos
        setupDb();
        loadAcCategorias();
        loadAcClientes();
        selCategoriasAll(true);
        selClienteAll(true);
        buscarDatos(false);

        tvAsunto_Rep.setText(AsuntoPredet);
        tvMensaje_Rep.setText(MensajePredet);

        rgCat.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                View radioButton = rgCat.findViewById(checkedId);
                int index = rgCat.indexOfChild(radioButton);
                switch (index) {
                    case 0: // first button
                        selCategoriasAll(true);
                        break;
                    case 1: // secondbutton
                        selCategoriasAll(false);
                        break;
                }
            }
        });

        rgClie.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                View radioButton = rgClie.findViewById(checkedId);
                int index = rgClie.indexOfChild(radioButton);
                switch (index) {
                    case 0:
                        selClienteAll(true);
                        break;
                    case 1: // secondbutton
                        selClienteAll(false);
                        break;
                }
            }
        });
        cargarTodos(enuTipoTabla.productos);
    }

    //region Base de Datos
    void selCategoriasAll(Boolean value) {
        esCatTodos = value;
        acCategorias.setEnabled(!value);
        acCategorias.setText("");
        tvCategorias_Conf.setText("");
        if (value)
            cargarTodos(enuTipoTabla.categorias);
        else
            set_CategoriasList(null);
    }

    void selClienteAll(Boolean value) {
        esClieTodos = value;
        acClientes.setEnabled(!value);
        glDatosCliente_Rep.setVisibility(View.GONE);
        acClientes.setText("");
        if (value)
            cargarTodos(enuTipoTabla.clientes);
        else
            set_ClientesList(null);
    }

    /**
     * Inicia la Sesion de Trabajo con la base de datos
     *
     * @return
     */
    private void setupDb() {
        DaoMaster.DevOpenHelper masterHelper = new DaoMaster.DevOpenHelper(this, "kssmarket_db", null); //create database db file if not exist
        SQLiteDatabase db = masterHelper.getWritableDatabase();  //get the created database db file
        daoMaster = new DaoMaster(db);//create masterDao
        daoSession = daoMaster.newSession(); //Creates Session session
        productosDao = daoSession.getProductosDao();
        categoriasDao = daoSession.getProductos_FamiliaDao();
        clientesDao = daoSession.getClientesDao();
    }


    private void cargarTodos(enuTipoTabla enTabla) {
        try {
            if (enTabla == enuTipoTabla.productos)
                set_ProductosList(productosDao.queryBuilder().orderAsc(ProductosDao.Properties.Id).build().list());

            if (enTabla == enuTipoTabla.categorias)
                set_CategoriasList(categoriasDao.queryBuilder().orderAsc(Productos_FamiliaDao.Properties.Id).build().list());

            if (enTabla == enuTipoTabla.clientes)
                set_ClientesList(clientesDao.queryBuilder().orderAsc(ClientesDao.Properties.Id).build().list());

        } catch (SQLiteDatabaseCorruptException sqlex) {
            Toast.makeText(this, this.getString(R.string.kssEx) + sqlex.toString(), Toast.LENGTH_SHORT).show();
        } catch (Exception ex) {
            Toast.makeText(this, this.getString(R.string.kssEx) + ex.toString(), Toast.LENGTH_SHORT).show();
        } finally {
        }
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
                this.acCategorias.setAdapter(adapter);
            } else
                Toast.makeText(this, "Master DB no iniciada", Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Toast.makeText(this, this.getString(R.string.kssEx) + ex.toString(), Toast.LENGTH_LONG).show();
        }
    }

    private void loadAcClientes() {
        try {
            if (daoMaster != null) {
                DaoSession daoSessionClientes1 = daoMaster.newSession();
                ClientesDao clientesDao1 = daoSessionClientes1.getClientesDao();
                List<Clientes> clientesList1 = clientesDao1.queryBuilder().orderAsc(ClientesDao.Properties.Id).build().list();
                String[] sClientes1 = new String[clientesList1.size()];
                int i = 0;
                for (Clientes cliente1 : clientesList1) {
                    sClientes1[i] = cliente1.getNombres() + " | " + cliente1.getCI();
                    i++;
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, sClientes1);
                this.acClientes.setAdapter(adapter);
            } else
                Toast.makeText(this, "Master DB no iniciada", Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Toast.makeText(this, this.getString(R.string.kssEx) + ex.toString(), Toast.LENGTH_LONG).show();
        }
    }

    private void buscarDatos(Boolean esEnviarEMail) {

        List<Productos_Familia> categoriaList_result = null;

        if (esCatTodos) {
            cargarTodos(enuTipoTabla.categorias);
            if (get_CategoriasList() != null)
                if (get_CategoriasList().size() == 0)
                    ToastManager.show(this, "No se encontraron Categor?as.", enuToastIcons.WARNING, 10);
                else
                    cargarTodos(enuTipoTabla.productos);
        } else {
            //Busca productos por Categoria
            if (acCategorias.getText().toString().trim().length() > 0) {
                //Buscar por Categoria
                String sCategoria = acCategorias.getText().toString().trim();
                Query<Productos_Familia> query = categoriasDao.queryBuilder().where(
                        Productos_FamiliaDao.Properties.FamiliaProducto.eq(sCategoria.trim())
                ).build();
                categoriaList_result = query.list();
                if (categoriaList_result.size() > 0) {
                    tvCategorias_Conf.setText(sCategoria);
                    Query<Productos> query1 = productosDao.queryBuilder().where(
                            ProductosDao.Properties.Id_familiaProducto.eq(categoriaList_result.get(0).getId())
                    ).build();
                    set_ProductosList(query1.list());
                } else {
                    ToastManager.show(this, "No existe la Categoria Seleccionada.\nPor favor Seleccione la Correcta!", enuToastIcons.ERROR, 10);
                    return;
                }
            } else {
                ToastManager.show(this, "Por favor indique la categor?a.", enuToastIcons.INFORMATION, 10);
                return;
            }
        }
        if (esClieTodos) {
            cargarTodos(enuTipoTabla.clientes);

        } else {
            //Buscar por Cliente
            glDatosCliente_Rep.setVisibility(View.GONE);
            if (acClientes.getText().toString().trim().length() > 0) {
                StringTokenizer st = new StringTokenizer(acClientes.getText().toString(), "|");
                String Nombre = st.nextToken();
                String Cedula;
                if (st.hasMoreElements()) {
                    Cedula = st.nextToken();
                } else
                    Cedula = "0";
                Query<Clientes> query = clientesDao.queryBuilder().where(
                        ClientesDao.Properties.Nombres.eq(Nombre.trim()),
                        ClientesDao.Properties.CI.eq(Cedula.trim())
                ).build();
                set_ClientesList(query.list());
                if (get_ClientesList().size() > 0) {
                    tvNombreClie_Rep.setText(get_ClientesList().get(0).getNombres());
                    tvEmailClie_Rep.setText(get_ClientesList().get(0).getEmail());
                    glDatosCliente_Rep.setVisibility(View.VISIBLE);
                } else {
                    ToastManager.show(this, "No existe el Cliente Seleccionado.\nPor favor Seleccione el Correcto!", enuToastIcons.ERROR, 10);
                    return;
                }
            } else {
                ToastManager.show(this, "Por favor indique el Cliente.", enuToastIcons.INFORMATION, 10);
                return;
            }
        }

        //Mostrar Msj de encontrados
        if (get_ProductosList() != null)
            if (get_ProductosList().size() > 0) {
                String sMensaje = "Se hallaron: " + String.valueOf(get_ProductosList().size()) + " Productos.";
                int Categorias = categoriaList_result != null ?
                        (categoriaList_result.size() > 0 ? categoriaList_result.size() : get_CategoriasList().size()) : get_CategoriasList().size();
                sMensaje += Categorias > 0 ? "\nCategor?as: " + String.valueOf(Categorias) : "";
                int Clientes = get_ClientesList() != null ?
                        (get_ClientesList().size() > 0 ? get_ClientesList().size() : get_ClientesList().size()) : get_ClientesList().size();
                sMensaje += Clientes > 0 ? "\nClientes:  " + String.valueOf(Clientes) : "";
                ToastManager.show(this, sMensaje, enuToastIcons.INFORMATION, 20);
            } else {
                ToastManager.show(this, "No se han cargado Productos para el Reporte.\nIntente de nuevo", enuToastIcons.WARNING, 50);
            }

        if (esEnviarEMail) {
            if (get_ClientesList() != null && get_ProductosList() != null)
                if (get_ClientesList().size() > 0 && get_ProductosList().size() > 0)
                    try {
                        new reportePDF_ListConImages().crearReporte(this,
                                settings.getRutaDocs() + "Reporte.pdf",
                                settings.getRutaMedia(),
                                get_ProductosList(),
                                settings);
                        eMail = new EMail(settings.getEmpresaEmailGmail(), settings.getEmpresaPassWordGmail());
                        String[] arrClientesEmail ;
                        int i = 0;
                        arrClientesEmail = new String[get_ClientesList().size()];
                        for (Clientes cliente1 : get_ClientesList()) {
                            arrClientesEmail[i] = cliente1.getEmail();
                            i++;
                        }
                        sendEmail(arrClientesEmail);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e(TAG, "exportarPDF: " + e.toString(), e);
                    } catch (DocumentException e) {
                        e.printStackTrace();
                        Log.e(TAG, "exportarPDF: " + e.toString(), e);
                    }
                else
                    ToastManager.show(this, "No se cargaron " + ( get_ClientesList()==null ?"CLIENTES" : "PRODUCTOS" ), enuToastIcons.ERROR, 10);

        }

    }

    public void sendEmail(String[] arrClientesEmail) {
        //Todos los clientes


        if (arrClientesEmail != null)
            if (arrClientesEmail.length > 0) {
                String[] toArr = arrClientesEmail; // This is an array, you can add more emails, just separate them with a coma
                eMail.setTo(toArr); // load array to setTo function
                eMail.setFrom(settings.getEmpresaEmailGmail()); // who is sending the email
                eMail.setSubject(tvAsunto_Rep.getText().toString().trim().length() > 0 ?
                        tvAsunto_Rep.getText().toString() : AsuntoPredet);
                eMail.setBody(tvMensaje_Rep.getText().toString().length() > 0 ?
                        tvMensaje_Rep.getText().toString() : MensajePredet);
                try {
                    eMail.addAttachment(settings.getRutaDocs() + "Reporte.pdf");  // path to file you want to attach
                    new SendMailTask(this).execute(eMail);
                } catch (Exception e) {
                    // some other problem
                    Toast.makeText(this, "Existe un Problema enviando Email.\n" + e.toString(), Toast.LENGTH_LONG).show();
                }
            } else
                ToastManager.show(this, "No existen Remitentes para enviar Emails.", enuToastIcons.OK, 50);

    }

    private View.OnClickListener OnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ivExit_Rep:
                    finish();
                    break;
                case R.id.ibHome_Rep:
                    finish();
                    break;
                case R.id.ivBuscar_Rep:
                    buscarDatos(false);
                    break;
                case R.id.tvEnviar_Rep:
                    buscarDatos(true);
                    break;

            }
        }
    };


}
