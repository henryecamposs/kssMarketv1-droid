package com.kss.greendao;


import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;
import de.greenrobot.daogenerator.Property;


/**
 * Created by KSS on 19/3/2017.
 */

public class MainGenerator {
    private static final String PROJECT_DIR = System.getProperty("user.dir");

    public static void main(String[] args) {
        Schema schema = new Schema(1, "com.kss.kssmarketv10.db");
        schema.enableKeepSectionsByDefault();
        schema.setDefaultJavaPackageDao("com.kss.kssmarketv10.db.ado");
        //Agregar las tablas (entidades)
        addTables(schema);
        try {
            /* Use forward slashes if you're on macOS or Unix, i.e. "/app/src/main/java"  */
            new DaoGenerator().generateAll(schema, PROJECT_DIR + "/app/src/main/java");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void addTables(final Schema schema) {
       final Entity Productos = addProductos(schema);
        final Entity Clientes = addClientes(schema);
        final Entity Empresa = addEmpresa(schema);
        final Entity Productos_Familias = addProductos_Familia(schema);
        final Entity Licencias = addLicencias(schema);
    }

    private static Entity addProductos(final Schema schema) {
        Entity Entidad = schema.addEntity("Productos");

        Entidad.addIdProperty().primaryKey().autoincrement().primaryKey().notNull();
                                                                    
        Entidad.addStringProperty("CodigoAdicional");                        
        Entidad.addStringProperty("CodigoBarras").notNull();
        Entidad.addStringProperty("CodigoInterno").notNull() ;
        Entidad.addStringProperty("Producto").notNull() ;
        Entidad.addStringProperty("Producto_DescripcionLarga") ;
        Entidad.addStringProperty("Producto_LinkWeb") ;
        Entidad.addDoubleProperty("cantidadUnidadVenta");
        Entidad.addDoubleProperty("MargenGananciaPrecio1");                     
        Entidad.addDoubleProperty("MargenGananciaPrecio2");                     
        Entidad.addDoubleProperty("MargenGananciaPrecio3");                     
        Entidad.addDoubleProperty("MedidaAlto");                                
        Entidad.addDoubleProperty("MedidaAncho");                               
        Entidad.addDoubleProperty("MedidaLargo");                               
        Entidad.addDoubleProperty("MontoCostoPromedio");                        
        Entidad.addDoubleProperty("MontoPrecio1").notNull();
        Entidad.addDoubleProperty("MontoPrecio2");                              
        Entidad.addDoubleProperty("MontoPrecio3");                              
        Entidad.addDoubleProperty("MontoUltimoCosto");                              
        Entidad.addBooleanProperty("esActivo");
        Entidad.addBooleanProperty("esCodigoAlterno");                                                
        Entidad.addBooleanProperty("esCompuesto");                                                    
        Entidad.addBooleanProperty("esFavorito");                                                     
        Entidad.addBooleanProperty("esGarantia");                                                     
        Entidad.addBooleanProperty("esImportado");                                                    
        Entidad.addBooleanProperty("esInventario");                                                   
        Entidad.addBooleanProperty("esPideComentario");                                               
        Entidad.addBooleanProperty("esPidePeso");                                                     
        Entidad.addBooleanProperty("esPidePrecio");                                                   
        Entidad.addBooleanProperty("esPideTipoProducto");                                             
        Entidad.addBooleanProperty("esUsoInterno");                                                   
        Entidad.addIntProperty("id_familiaProducto").notNull();
        Entidad.addIntProperty("id_Marca");
        Entidad.addIntProperty("id_Impuesto");
        Entidad.addIntProperty("id_unidadMedida");
        Entidad.addDateProperty("FechaCreacion");
        Entidad.addDateProperty("FechaUltimaCompra");                        
        Entidad.addDateProperty("FechaUltimaModificacion");                  
        Entidad.addDateProperty("FechaUltimaVenta");                         

        return Entidad;
    }

    private static Entity addProductos_Familia(final Schema schema) {
        Entity Entidad = schema.addEntity("Productos_Familia");
        Entidad.addIdProperty().primaryKey().autoincrement();
        Entidad.addStringProperty("FamiliaProducto").notNull();
       
        return Entidad;
    }

    private static Entity addLicencias(final Schema schema) {
        Entity Entidad = schema.addEntity("Licencias");
        Entidad.addIdProperty().primaryKey().autoincrement().primaryKey().notNull();
        Entidad.addStringProperty("CodigoActivacion");
        Entidad.addStringProperty("CodigoRequerido");                                                          
        Entidad.addStringProperty("CodigoSerial").notNull() ;
        Entidad.addStringProperty("MotherBoard_Serial") ;                                                         
        Entidad.addStringProperty("NombreEquipo") ;                                                    
        Entidad.addStringProperty("SistemaOperativo") ;                                                                  
        Entidad.addStringProperty("SO_Version") ;                                                                  
        Entidad.addStringProperty("hdd_Serial") ;                                                                  
        Entidad.addBooleanProperty("esActivo");
        Entidad.addBooleanProperty("esOnline");        

        return Entidad;
    }

    private static Entity addEmpresa(final Schema schema) {
        Entity Entidad = schema.addEntity("Empresa");
        Entidad.addIdProperty().primaryKey().autoincrement().primaryKey().notNull();

       Entidad.addStringProperty("Contacto");                          
       Entidad.addStringProperty("direccion").notNull();
       Entidad.addStringProperty("empresa").notNull();
       Entidad.addStringProperty("ImagenArchivo");                          
       Entidad.addStringProperty("nit");                          
       Entidad.addStringProperty("rif").notNull();
       Entidad.addStringProperty("telefonos").notNull();
       Entidad.addStringProperty("telefonosContacto");                           
       Entidad.addBooleanProperty("esActivo");
       Entidad.addIntProperty("codigoPostal");
       Entidad.addIntProperty("Id_Ciudad");
       Entidad.addIntProperty("id_Estado");
       Entidad.addIntProperty("id_idioma");
       Entidad.addIntProperty("id_Municipio");
       Entidad.addIntProperty("id_Pais");
       Entidad.addIntProperty("Id_Parroquia");
       Entidad.addIntProperty("ID_tipoComercio");
        return Entidad;
    }

    private static Entity addClientes(final Schema schema) {
        Entity Entidad = schema.addEntity("Clientes");
        Entidad.addIdProperty().primaryKey().autoincrement().primaryKey().notNull();
        Entidad.addStringProperty("CI").notNull();
        Entidad.addStringProperty("Nombres").notNull();
        Entidad.addStringProperty("Categorias") ;
        Entidad.addStringProperty("Pasaporte") ;                  
        Entidad.addStringProperty("telefono") ;                   
        Entidad.addStringProperty("telefono_movil") ;             
        Entidad.addStringProperty("direccion").notNull() ;
        Entidad.addStringProperty("Apellidos") ;                      
        Entidad.addStringProperty("email").notNull() ;
        Entidad.addBooleanProperty("esActivo");
        Entidad.addBooleanProperty("esEmpresa");                    
        Entidad.addIntProperty("id_estado");
        Entidad.addIntProperty("id_municipio");
        Entidad.addIntProperty("id_nacionalidad");
        Entidad.addIntProperty("id_parroquia");
        Entidad.addIntProperty("id_profesion");
        Entidad.addIntProperty("codigoPostal");
        Entidad.addIntProperty("id_ciudad");
        Entidad.addIntProperty("id_tipoPago");
        return Entidad;
    }
}

 //Campos Palntilla para generar las entidades
 /* Entidad.addIdProperty().primaryKey().autoincrement();
        Entidad.addStringProperty("").notNull();
        Entidad.addShortProperty("");
        Entidad.addBooleanProperty("");
        Entidad.addDoubleProperty("");
        Entidad.addIntProperty("");
        Entidad.addDateProperty("");
        Entidad.addIdProperty().primaryKey().autoincrement();

        Entidad.addStringProperty("");
        Entidad.addStringProperty("");
        Entidad.addStringProperty("") ;
        Entidad.addStringProperty("") ;
        Entidad.addStringProperty("") ;
        Entidad.addStringProperty("") ;

        Entidad.addDoubleProperty("");
        Entidad.addDoubleProperty("");
        Entidad.addDoubleProperty("");
        Entidad.addDoubleProperty("");
        Entidad.addDoubleProperty("");
        Entidad.addDoubleProperty("");
        Entidad.addDoubleProperty("");
        Entidad.addDoubleProperty("");
        Entidad.addDoubleProperty("");
        Entidad.addDoubleProperty("");
        Entidad.addDoubleProperty("");

        Entidad.addBooleanProperty("");
        Entidad.addBooleanProperty("");
        Entidad.addBooleanProperty("");
        Entidad.addBooleanProperty("");
        Entidad.addBooleanProperty("");
        Entidad.addBooleanProperty("");
        Entidad.addBooleanProperty("");
        Entidad.addBooleanProperty("");
        Entidad.addBooleanProperty("");
        Entidad.addBooleanProperty("");
        Entidad.addBooleanProperty("");
        Entidad.addBooleanProperty("");
        Entidad.addBooleanProperty("");
        Entidad.addBooleanProperty("");
        Entidad.addBooleanProperty("");
        Entidad.addBooleanProperty("");
        Entidad.addBooleanProperty("");
        Entidad.addBooleanProperty("");
        Entidad.addBooleanProperty("");
        Entidad.addBooleanProperty("");
        Entidad.addBooleanProperty("");
        Entidad.addBooleanProperty("");
        Entidad.addBooleanProperty("");

        Entidad.addIntProperty("");
        Entidad.addIntProperty("");
        Entidad.addIntProperty("");
        Entidad.addIntProperty("");

        Entidad.addDateProperty("");
        Entidad.addDateProperty("");
        Entidad.addDateProperty("");
        Entidad.addDateProperty("");
        Entidad.addDateProperty("");

        Entidad.addShortProperty("");*/