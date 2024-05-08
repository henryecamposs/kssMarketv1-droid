package com.kss.kssmarketv10.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.kss.kssmarketv10.db.Licencias;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "LICENCIAS".
*/
public class LicenciasDao extends AbstractDao<Licencias, Long> {

    public static final String TABLENAME = "LICENCIAS";

    /**
     * Properties of entity Licencias.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, long.class, "id", true, "_id");
        public final static Property CodigoActivacion = new Property(1, String.class, "CodigoActivacion", false, "CODIGO_ACTIVACION");
        public final static Property CodigoRequerido = new Property(2, String.class, "CodigoRequerido", false, "CODIGO_REQUERIDO");
        public final static Property CodigoSerial = new Property(3, String.class, "CodigoSerial", false, "CODIGO_SERIAL");
        public final static Property MotherBoard_Serial = new Property(4, String.class, "MotherBoard_Serial", false, "MOTHER_BOARD__SERIAL");
        public final static Property NombreEquipo = new Property(5, String.class, "NombreEquipo", false, "NOMBRE_EQUIPO");
        public final static Property SistemaOperativo = new Property(6, String.class, "SistemaOperativo", false, "SISTEMA_OPERATIVO");
        public final static Property SO_Version = new Property(7, String.class, "SO_Version", false, "SO__VERSION");
        public final static Property Hdd_Serial = new Property(8, String.class, "hdd_Serial", false, "HDD__SERIAL");
        public final static Property EsActivo = new Property(9, Boolean.class, "esActivo", false, "ES_ACTIVO");
        public final static Property EsOnline = new Property(10, Boolean.class, "esOnline", false, "ES_ONLINE");
    };


    public LicenciasDao(DaoConfig config) {
        super(config);
    }
    
    public LicenciasDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"LICENCIAS\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL ," + // 0: id
                "\"CODIGO_ACTIVACION\" TEXT," + // 1: CodigoActivacion
                "\"CODIGO_REQUERIDO\" TEXT," + // 2: CodigoRequerido
                "\"CODIGO_SERIAL\" TEXT NOT NULL ," + // 3: CodigoSerial
                "\"MOTHER_BOARD__SERIAL\" TEXT," + // 4: MotherBoard_Serial
                "\"NOMBRE_EQUIPO\" TEXT," + // 5: NombreEquipo
                "\"SISTEMA_OPERATIVO\" TEXT," + // 6: SistemaOperativo
                "\"SO__VERSION\" TEXT," + // 7: SO_Version
                "\"HDD__SERIAL\" TEXT," + // 8: hdd_Serial
                "\"ES_ACTIVO\" INTEGER," + // 9: esActivo
                "\"ES_ONLINE\" INTEGER);"); // 10: esOnline
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"LICENCIAS\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Licencias entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getId());
 
        String CodigoActivacion = entity.getCodigoActivacion();
        if (CodigoActivacion != null) {
            stmt.bindString(2, CodigoActivacion);
        }
 
        String CodigoRequerido = entity.getCodigoRequerido();
        if (CodigoRequerido != null) {
            stmt.bindString(3, CodigoRequerido);
        }
        stmt.bindString(4, entity.getCodigoSerial());
 
        String MotherBoard_Serial = entity.getMotherBoard_Serial();
        if (MotherBoard_Serial != null) {
            stmt.bindString(5, MotherBoard_Serial);
        }
 
        String NombreEquipo = entity.getNombreEquipo();
        if (NombreEquipo != null) {
            stmt.bindString(6, NombreEquipo);
        }
 
        String SistemaOperativo = entity.getSistemaOperativo();
        if (SistemaOperativo != null) {
            stmt.bindString(7, SistemaOperativo);
        }
 
        String SO_Version = entity.getSO_Version();
        if (SO_Version != null) {
            stmt.bindString(8, SO_Version);
        }
 
        String hdd_Serial = entity.getHdd_Serial();
        if (hdd_Serial != null) {
            stmt.bindString(9, hdd_Serial);
        }
 
        Boolean esActivo = entity.getEsActivo();
        if (esActivo != null) {
            stmt.bindLong(10, esActivo ? 1L: 0L);
        }
 
        Boolean esOnline = entity.getEsOnline();
        if (esOnline != null) {
            stmt.bindLong(11, esOnline ? 1L: 0L);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public Licencias readEntity(Cursor cursor, int offset) {
        Licencias entity = new Licencias( //
            cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // CodigoActivacion
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // CodigoRequerido
            cursor.getString(offset + 3), // CodigoSerial
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // MotherBoard_Serial
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // NombreEquipo
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // SistemaOperativo
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // SO_Version
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // hdd_Serial
            cursor.isNull(offset + 9) ? null : cursor.getShort(offset + 9) != 0, // esActivo
            cursor.isNull(offset + 10) ? null : cursor.getShort(offset + 10) != 0 // esOnline
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Licencias entity, int offset) {
        entity.setId(cursor.getLong(offset + 0));
        entity.setCodigoActivacion(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setCodigoRequerido(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setCodigoSerial(cursor.getString(offset + 3));
        entity.setMotherBoard_Serial(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setNombreEquipo(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setSistemaOperativo(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setSO_Version(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setHdd_Serial(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setEsActivo(cursor.isNull(offset + 9) ? null : cursor.getShort(offset + 9) != 0);
        entity.setEsOnline(cursor.isNull(offset + 10) ? null : cursor.getShort(offset + 10) != 0);
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(Licencias entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(Licencias entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
