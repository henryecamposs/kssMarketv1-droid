package com.kss.kssmarketv10;

import android.app.Application;

import com.kss.kssutil.enuStatusRegistro;

/**
 * Created by KSS on 22/3/2017.
 */

public class GlobalClass extends Application {


    private String empresaNombre;
    private String empresaCIRIF;
    private String empresaEmail;
    private String empresaTelf;
    private String empresaDireccion;
    private String empresaContacto;
    private String iva1;
    private String iva2;
    private String iva3;
    private String RutaBackupBD;
    private String rutaMedia;
    private String rutaDocs;
    private String nombreBD;
    private String EmpresaPassWordGmail;
    public enuStatusRegistro enuStatusregistro;
    public long IDActual;
    private Boolean esConfigCargada=false;
    final public String DropBox_APP_KEY = "INSERT_APP_KEY";
    final public String DropBox_APP_SECRET = "INSERT_APP_SECRET";

    public GlobalClass() {
        getEsConfigCargada();
    }

    @Override
    public String toString() {
        return "Nombre='" + empresaNombre + '\'' +
                ", Rif='" + empresaCIRIF + '\'' +
                ", Telefono='" + empresaTelf + '\''
                ;
    }

    public String getEmpresaNombre() {
        return empresaNombre;
    }

    public void setEmpresaNombre(String empresaNombre) {
        this.empresaNombre = empresaNombre;
    }

    public String getEmpresaCIRIF() {
        return empresaCIRIF;
    }

    public void setEmpresaCIRIF(String empresaCIRIF) {
        this.empresaCIRIF = empresaCIRIF;
    }

    public String getEmpresaEmailGmail() {
        return empresaEmail;
    }

    public void setEmpresaEmailGmail(String empresaEmail) {
        this.empresaEmail = empresaEmail;
    }


    public String getEmpresaPassWordGmail() {
        return EmpresaPassWordGmail;
    }

    public void setEmpresaPassWordGmail(String empresaPassWordGmail) {
        EmpresaPassWordGmail = empresaPassWordGmail;
    }

    public Boolean getEsConfigCargada() {
        return esConfigCargada;
    }

    public void setEsConfigCargada(Boolean esConfigCargada) {
        this.esConfigCargada = esConfigCargada;
    }

    public enum enuTipoTabla {
        productos, categorias, clientes
    }

    public String getEmpresaTelf() {
        return empresaTelf;
    }

    public void setEmpresaTelf(String empresaTelf) {
        this.empresaTelf = empresaTelf;
    }

    public String getEmpresaDireccion() {
        return empresaDireccion;
    }

    public void setEmpresaDireccion(String empresaDireccion) {
        this.empresaDireccion = empresaDireccion;
    }

    public String getEmpresaContacto() {
        return empresaContacto;
    }

    public void setEmpresaContacto(String empresaContacto) {
        this.empresaContacto = empresaContacto;
    }

    public String getIva1() {
        return iva1;
    }

    public void setIva1(String iva1) {
        this.iva1 = iva1;
    }

    public String getIva2() {
        return iva2;
    }

    public void setIva2(String iva2) {
        this.iva2 = iva2;
    }

    public String getIva3() {
        return iva3;
    }

    public void setIva3(String iva3) {
        this.iva3 = iva3;
    }

    public String getEmpresaArchivoLogo() {
        return "logoempresa";
    }

    public String getRutaBackupBD() {
        return RutaBackupBD;
    }

    public void setRutaBackupBD(String rutaBd) {
        this.RutaBackupBD = rutaBd;
    }

    public String getRutaMedia() {
        return rutaMedia;
    }

    public void setRutaMedia(String rutaMedia) {
        this.rutaMedia = rutaMedia;
    }

    public String getRutaDocs() {
        return rutaDocs;
    }

    public void setRutaDocs(String rutaDocs) {
        this.rutaDocs = rutaDocs;
    }

    public String getNombreBD() {
        return nombreBD;
    }

    public void setNombreBD(String nombreBD) {
        this.nombreBD = nombreBD;
    }

    public String getRutaBD() {
        return "/data/data/com.kss.kssmarketv10/databases/";
    }
}
