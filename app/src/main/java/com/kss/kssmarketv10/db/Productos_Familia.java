package com.kss.kssmarketv10.db;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table "PRODUCTOS__FAMILIA".
 */
public class Productos_Familia {

    private Long id;
    /** Not-null value. */
    private String FamiliaProducto;

    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public Productos_Familia() {
    }

    public Productos_Familia(Long id) {
        this.id = id;
    }

    public Productos_Familia(Long id, String FamiliaProducto) {
        this.id = id;
        this.FamiliaProducto = FamiliaProducto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /** Not-null value. */
    public String getFamiliaProducto() {
        return FamiliaProducto;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setFamiliaProducto(String FamiliaProducto) {
        this.FamiliaProducto = FamiliaProducto;
    }

    // KEEP METHODS - put your custom methods here
    // KEEP METHODS END

}
