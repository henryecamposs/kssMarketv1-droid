package com.kss.kssmarketv10;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabaseCorruptException;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.DocumentException;
import com.kss.kssmarketv10.kssSettings.enuTipoTabla;
import com.kss.kssmarketv10.db.Clientes;
import com.kss.kssmarketv10.db.Productos;
import com.kss.kssmarketv10.db.dao.ClientesDao;
import com.kss.kssmarketv10.db.dao.DaoMaster;
import com.kss.kssmarketv10.db.dao.DaoSession;
import com.kss.kssmarketv10.db.dao.ProductosDao;
import com.kss.kssmarketv10.reports.reportePDF_ListConImages;
import com.kss.kssutil.ToastManager;
import com.kss.kssutil.enuToastIcons;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.greenrobot.dao.query.Query;

import static com.kss.kssutil.BitmapUtils.showBitmapFromFile;
import static com.kss.kssutil.clsUtil_Errores.ManejadorErrores;


public class dlgBuscar extends AppCompatActivity {
    public kssSettings setting;
    //Variables relativas a Green Dao Database
    private DaoSession daoSession;
    private DaoMaster daoMaster;
    private ProductosDao productosDao;
    private ClientesDao clientesDao;
    private SQLiteDatabase db;

    private AutoCompleteTextView acBuscar_Bus;
    private boolean esProductos;
    private boolean esTodos;
    //Adaptador
    ListViewAdapter listViewAdapter;

    //Listas d eProductos
    private List<Productos> _ProductosList;
    private SearchItemArrayAdapter searchItemArrayAdapter = null;

    public List<Productos> get_ProductosList() {
        return _ProductosList;
    }

    public void set_ProductosList(List<Productos> _ProductosList) {
        this._ProductosList = _ProductosList;
    }

    private List<Clientes> _ClientesList;

    public List<Clientes> get_ClientesList() {
        return _ClientesList;
    }

    public void set_ClientesList(List<Clientes> _ClientesList) {
        this._ClientesList = _ClientesList;
    }

    private ListView ListaEncontrados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dlg_buscar);

        final ImageView ivExit = (ImageView) findViewById(R.id.ivExit_Bus);
        final ImageButton ibHome = (ImageButton) findViewById(R.id.ibHome_Bus);
        final TextView tvEnviar = (TextView) findViewById(R.id.tvEnviar_Bus);
        acBuscar_Bus = (AutoCompleteTextView) findViewById(R.id.acBuscar_Bus);
        final RadioGroup rgTabla_bus = (RadioGroup) findViewById(R.id.rgTabla_bus);
        final RadioGroup rgAllOnly_Bus = (RadioGroup) findViewById(R.id.rgAllOnly_Bus);
        final ImageView ivBuscar_Bus = (ImageView) findViewById(R.id.ivBuscar_Bus);
        ListaEncontrados = (ListView) findViewById(R.id.lvListItems_Bus);


        try {
            ivExit.setOnClickListener(OnClickListener);
            ibHome.setOnClickListener(OnClickListener);
            tvEnviar.setOnClickListener(OnClickListener);
            ivBuscar_Bus.setOnClickListener(OnClickListener);
            //cargar valores Globales
            setting = kssSettings.getInstance(this);

            //Ejecutar metodos
            setupDb();
            acBuscar_Bus.setHint("Buscar Productos");
            acBuscar_Bus.setEnabled(false);
            esTodos = true;
            esProductos = true;
            buscarDatos();
            loadAcProductos();

            //Evita encoger la pantalla al aparecer teclado
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

            rgTabla_bus.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    View radioButton = rgTabla_bus.findViewById(checkedId);
                    int index = rgTabla_bus.indexOfChild(radioButton);
                    acBuscar_Bus.setText("");
                    switch (index) {
                        case 0: // first button
                            esProductos = true;
                            try {
                                buscarDatos();
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (BadElementException e) {
                                e.printStackTrace();
                            }
                            loadAcProductos();
                            acBuscar_Bus.setHint("Buscar Producto");
                            break;
                        case 1: // secondbutton
                            esProductos = false;
                            try {
                                buscarDatos();
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (BadElementException e) {
                                e.printStackTrace();
                            }
                            loadAcClientes();
                            acBuscar_Bus.setHint("Buscar Cliente");
                            break;
                    }
                }
            });

            rgAllOnly_Bus.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    View radioButton = rgAllOnly_Bus.findViewById(checkedId);
                    int index = rgAllOnly_Bus.indexOfChild(radioButton);
                    acBuscar_Bus.setText("");
                    switch (index) {
                        case 0: // first button
                            esTodos = true;
                            acBuscar_Bus.setEnabled(false);
                            break;
                        case 1: // secondbutton
                            esTodos = false;
                            acBuscar_Bus.setEnabled(true);
                            break;
                    }
                }
            });

            //ListView Imagenes
            ListaEncontrados.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView adapterView, View view, int i, long l) {
                    if (esProductos) {
                        if (get_ProductosList()!=null)
                        showProducto(view.getContext(), get_ProductosList().get(i));
                    } else {
                        if(get_ClientesList()!=null)
                        showCLiente(view.getContext(),get_ClientesList().get(i));
                    }
                }
            });
            ListaEncontrados.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView adapterView, View view, int i, long l) {
                    //Mostrar Producto directamente
                    Intent intentDlgProductos = null;
                    long ID = 0;
                    if (esProductos) {
                        if (get_ProductosList() != null)
                            if (get_ProductosList().size() > 0) {
                                Toast.makeText(getApplicationContext(), "Cargando Producto...\n" +
                                                get_ProductosList().get(i).getProducto(),
                                        Toast.LENGTH_SHORT).show();
                                ID = get_ProductosList().get(i).getId();
                                intentDlgProductos = new Intent(view.getContext(), dlgProductos.class);
                            }
                    } else {
                        if (get_ClientesList() != null)
                            if (get_ClientesList().size() > 0) {
                                Toast.makeText(getApplicationContext(), "Cargando Cliente...\n" +
                                                get_ClientesList().get(i).getNombres() + " " +
                                                get_ClientesList().get(i).getCI(),
                                        Toast.LENGTH_SHORT).show();
                                ID = get_ClientesList().get(i).getId();
                                intentDlgProductos = new Intent(view.getContext(), dlgClientes.class);
                            }
                    }
                    intentDlgProductos.putExtra("ID", ID);
                    startActivity(intentDlgProductos);
                    return false;
                }
            });
            //<-Fin ListView
            //AUtoComplete
            acBuscar_Bus.setThreshold(2);
            acBuscar_Bus.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    acBuscar_Bus.showDropDown();
                    acBuscar_Bus.setDropDownWidth(400);
                    return false;
                }
            });
            acBuscar_Bus.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long rowId) {
                    String selection = parent.getItemAtPosition(position).toString();
                    Toast.makeText(parent.getContext(), "" + selection, Toast.LENGTH_SHORT).show();
                    try {
                        buscarDatos();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (BadElementException e) {
                        e.printStackTrace();
                    }
                }
            });
            acBuscar_Bus.addTextChangedListener(new TextWatcher() {
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
            ManejadorErrores(this,e,true,true);
        }
    }

    /**
     * Obtiene el Producto first
     *
     * @return
     */
    private void cargarTodos(enuTipoTabla enTabla) {
        try {
            if (enTabla == enuTipoTabla.productos)
                set_ProductosList(productosDao.queryBuilder().orderAsc(ProductosDao.Properties.Id).build().list());

            if (enTabla == enuTipoTabla.clientes)
                set_ClientesList(clientesDao.queryBuilder().orderAsc(ClientesDao.Properties.Id).build().list());

        } catch (SQLiteDatabaseCorruptException sqlex) {
            Toast.makeText(this, this.getString(R.string.kssEx) + sqlex.toString(), Toast.LENGTH_SHORT).show();
        } catch (Exception ex) {
            ManejadorErrores(this,ex,true,true);
        } finally {
        }
    }

    private void buscarDatos() throws IOException, BadElementException {
        try {
            if (!esProductos)
                //Es Clientes
                if (esTodos) {
                    cargarTodos(enuTipoTabla.clientes);
                    if (get_ClientesList().size() > 0) {
                        //Rellenar Lista
                    }
                } else {
                    //Buscar por Cliente
                    if (acBuscar_Bus.getText().toString().trim().length() > 0) {
                        // Split texto con separadores
                        /*  StringTokenizer st = new StringTokenizer(acBuscar_Bus.getText().toString(), "|");
                        String Nombre = st.nextToken();
                        String Cedula;
                        if (st.hasMoreElements()) {
                            Cedula = st.nextToken();
                        } else
                            Cedula = "0";*/
                        String Nombre = acBuscar_Bus.getText().toString();
                        Query<Clientes> query = clientesDao.queryBuilder().where(
                                ClientesDao.Properties.Nombres.eq(Nombre.trim())
                        ).build();
                        set_ClientesList(query.list());

                        if (get_ClientesList().size() > 0) {
                            //Rellenar Lista
                        } else {
                            //Busqueda: contiene
                            query = clientesDao.queryBuilder().where(
                                    ClientesDao.Properties.Nombres.like("%" + Nombre.trim() + "%")
                            ).build();
                            set_ClientesList(query.list());
                            if (get_ClientesList().size() == 0) {
                                ToastManager.show(this, "No existe el Cliente indicado.", enuToastIcons.ERROR, 10);
                                return;
                            }
                        }
                    } else {
                        ToastManager.show(this, "Por favor indique el Cliente.", enuToastIcons.INFORMATION, 10);
                        return;
                    }
                }
            else
                //es Productos
                if (esTodos) {
                    cargarTodos(enuTipoTabla.productos);
                    if (get_ProductosList().size() > 0) {
                        //Rellenar List
                    }
                } else {
                    //Buscar por Cliente
                    String sProducto = acBuscar_Bus.getText().toString().trim();
                    if (sProducto.length() > 0) {
                        //Busqueda: es Igual
                        Query<Productos> query = productosDao.queryBuilder().where(
                                ProductosDao.Properties.Producto.eq(sProducto)
                        ).build();
                        set_ProductosList(query.list());
                        if (get_ProductosList().size() > 0) {
                            //Rellenar List

                        } else {
                            //Busqueda: contiene
                            query = productosDao.queryBuilder()
                                    .where(ProductosDao.Properties.Producto.like("%" + sProducto + "%")
                                    ).build();
                            set_ProductosList(query.list());
                            if (get_ProductosList().size() == 0) {
                                ToastManager.show(this, "No existe el producto indicado.", enuToastIcons.ERROR, 10);
                                return;
                            }
                        }
                    } else
                        ToastManager.show(this, "Por favor indique el producto.", enuToastIcons.INFORMATION, 10);
                }

            //Mostrar Msj de encontrados
            try {
                if (get_ProductosList() != null || get_ClientesList() != null) {
                    //Rellenar Adapter
                    String[] titulo;
                    String[] titulo2;
                    Bitmap[] imagenes;
                    String sMensaje = "";
                    if (esProductos) {
                        if (get_ProductosList().size() > 0) {
                            sMensaje = "Se hallaron: " + String.valueOf(get_ProductosList().size()) + " Productos.";
                            int i = 0;
                            imagenes = new Bitmap[get_ProductosList().size()];
                            titulo = new String[get_ProductosList().size()];
                            titulo2 = new String[get_ProductosList().size()];
                            for (Productos Producto : get_ProductosList()
                                    ) {
                                String sImagen = setting.getRutaMedia() + Producto.getId() + ".jpg";
                                File file = new File(sImagen);
                                Boolean Existe = file.exists();
                                if (!Existe)
                                    sImagen = setting.getRutaMedia() + "logoempresa.jpg";
                                imagenes[i] = showBitmapFromFile(this, sImagen);
                                titulo[i] = Producto.getProducto().toUpperCase();
                                titulo2[i] = "Bs. " + String.valueOf(Producto.getMontoPrecio1());
                                i++;
                            }
                            listViewAdapter = new ListViewAdapter(this, titulo, titulo2, imagenes);
                        }
                    } else {
                        if (get_ClientesList().size() > 0) {
                            sMensaje = "Se hallaron: " + String.valueOf(get_ClientesList().size()) + " Clientes.";
                            int i = 0;
                            imagenes = new Bitmap[get_ClientesList().size()];
                            titulo = new String[get_ClientesList().size()];
                            titulo2 = new String[get_ClientesList().size()];
                            for (Clientes Cliente : get_ClientesList()
                                    ) {
                                String sImagen = setting.getRutaMedia() + Cliente.getId() + ".jpg";
                                imagenes[i] = null;
                                titulo[i] = Cliente.getNombres().toUpperCase();
                                titulo2[i] = Cliente.getCI();
                                i++;
                            }
                            listViewAdapter = new ListViewAdapter(this, titulo, titulo2, imagenes);
                        }
                    }
                    ListaEncontrados.setAdapter(listViewAdapter);
                    ToastManager.show(this, sMensaje.length() > 0 ? sMensaje : "No existen Datos que mostrar.", enuToastIcons.INFORMATION, 4);
                }
            } catch (Exception e) {
                ManejadorErrores(this,e,true,true);
            }
        } catch (Exception e) {
            ManejadorErrores(this,e,true,true);
        } finally {

        }
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
        clientesDao = daoSession.getClientesDao();
    }

    private ArrayList<itemrow_image> populateClientesData(List<Clientes> rows) {
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
                    searchItemArrayAdapter = new SearchItemArrayAdapter(this, R.layout.searchitem_image, populateClientesData(get_ClientesList()));
                    acBuscar_Bus.setAdapter(searchItemArrayAdapter);
                    set_ProductosList(null);
                }
            } else
                Toast.makeText(this, "Master DB no iniciada", Toast.LENGTH_LONG).show();

        } catch (Exception ex) {
            ManejadorErrores(this,ex,true,true);
        }
    }

    private ArrayList<itemrow_image> populateProductosRowData(List<Productos> rows) {
        ArrayList<itemrow_image> itemrow_images = new ArrayList<>();
        for (Productos item : rows
                ) {
            String sImagen = setting.getRutaMedia() + item.getId() + ".jpg";
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
                    acBuscar_Bus.setAdapter(searchItemArrayAdapter);
                    set_ClientesList(null);
                }
            } else
                Toast.makeText(this, "Master DB no iniciada", Toast.LENGTH_LONG).show();

        } catch (Exception ex) {
            ManejadorErrores(this,ex,true,true);
        }
    }

    //region Controller Imagenes a Drawable
    public void showCLiente(Context context, Clientes clientes) {
        try {
            final Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.dialog_cliente);
            dialog.setTitle("Detalles de Cliente");
            dialog.getWindow().setBackgroundDrawable(
                    new ColorDrawable(android.graphics.Color.TRANSPARENT));
            // set the custom dialog components - text, image and button
            TextView tvNombre_dc = (TextView) dialog.findViewById(R.id.tvNombre_dc);
            tvNombre_dc.setText(clientes.getNombres());
            TextView tvCI_dc = (TextView) dialog.findViewById(R.id.tvCI_dc);
            tvCI_dc.setText(String.valueOf(clientes.getCI()));
            TextView tvEmail_dc = (TextView) dialog.findViewById(R.id.tvEmail_dc);
            tvEmail_dc.setText(String.valueOf(clientes.getEmail()));
            TextView tvTelf_dc = (TextView) dialog.findViewById(R.id.tvTelf_dc);
            tvTelf_dc.setText(String.valueOf(clientes.getTelefono()));
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    //nothing;
                }
            });
        /*
        Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });*/

            dialog.show();
        } catch (Exception e) {
            ManejadorErrores(context,e,true,true);
        }
    }

    //region Controller Imagenes a Drawable
    public void showProducto(Context context, Productos producto) {
        try {
            final Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.dialog_producto);
            dialog.setTitle("Detalles de Producto");
            dialog.getWindow().setBackgroundDrawable(
                    new ColorDrawable(android.graphics.Color.TRANSPARENT));
            // set the custom dialog components - text, image and button
            TextView tvNombre = (TextView) dialog.findViewById(R.id.tvNombre_dp);
            tvNombre.setText(producto.getProducto());
            TextView tvprecio_dp = (TextView) dialog.findViewById(R.id.tvprecio_dp);
            tvprecio_dp.setText(String.valueOf(producto.getMontoPrecio1()));
            ImageView image = (ImageView) dialog.findViewById(R.id.ivImage_dp);
            String sImagen = setting.getRutaMedia() + producto.getId() + ".jpg";
            image.setImageBitmap(showBitmapFromFile(this, sImagen));
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    //nothing;
                }
            });
        /*
        Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });*/

            dialog.show();
        } catch (Exception e) {
            ManejadorErrores(context,e,true,true);

        }
    }

    private void exportarPDF(View v, Boolean esEnviarEmail) {
        try {
            if (get_ProductosList() != null) {
                new reportePDF_ListConImages().crearReporte(v.getContext(),
                        setting.getRutaDocs() + "Reporte.pdf",
                        setting.getRutaMedia(),
                        get_ProductosList(),
                        setting);
                if (esEnviarEmail) {
                    ToastManager.show(v.getContext(), "Programar Envio Email", enuToastIcons.GRAY, 10);
                }
            } else
                ToastManager.show(v.getContext(), "No se cargaron productos!", enuToastIcons.ERROR, 10);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    private View.OnClickListener OnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ivExit_Bus:
                    finish();
                    break;
                case R.id.ibHome_Bus:
                    finish();
                    break;
                case R.id.ivBuscar_Bus:
                    try {
                        buscarDatos();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (BadElementException e) {
                        e.printStackTrace();
                    }
                    break;
                case R.id.tvEnviar_Bus:
                    exportarPDF(v, true);
                    break;
                case R.id.tvExportarPDF_Bus:
                    exportarPDF(v, false);
                    break;

                case R.id.tvExportarExcel_Bus:
                    ToastManager.show(v.getContext(), "No existe Programacion", enuToastIcons.GRAY, 10);
                    break;
            }
        }
    };


}
