package com.kss.kssmarketv10;

import android.content.Context;
import android.content.SharedPreferences;

import com.kss.kssutil.clsUtil_Files;
import com.kss.kssutil.enuStatusRegistro;

import static com.kss.kssutil.clsUtil_Errores.ManejadorErrores;

/**
 * Created by KSS on 22/3/2017.
 */

public class kssSettings {


    private String empresaNombre;
    private String empresaCIRIF;
    private String empresaEmail;
    private String empresaTelf;
    private String empresaDireccion;
    private String empresaContacto;
    private String iva1;
    private String iva2;
    private String iva3;
    private String Ganancia;
    public String RutaBackupBD;
    private String rutaMedia;
    private String rutaDocs;
    private String nombreBD;
    private String EmpresaPassWordGmail;
    public enuStatusRegistro enuStatusregistro;
    public long IDActual;
    private Boolean esConfigCargada = false;
    private String DropBoxEmail;
    private String DropBoxUser;
    private String DropBox_AuthToken;
    final public String DropBox_APP_KEY = "967316iyu30ugsr";
    final public String DropBox_APP_SECRET = "6edjqeuupgvcpsr";
    final public String DropBox_ROOT_DIR = "kssmarket";

    private String DropBox_AcountType;
    private SharedPreferences prefs;
    private static kssSettings instance = null;

    public static void setContext(Context context) {
        kssSettings.context = context;
    }

    private static Context context;

    public Boolean getEsPrefGuardadas() {
        return esPrefGuardadas;
    }

    public void setEsPrefGuardadas(Boolean esPrefGuardadas) {
        this.esPrefGuardadas = esPrefGuardadas;
    }

    private Boolean esPrefGuardadas;


    //region Iniciar Clase
    public kssSettings(SharedPreferences prefs) {
        this.prefs = prefs;
        setGanancia(prefs.getString("Ganancia_Conf", "30"));
        setRutaBackupBD(prefs.getString("URIBackup_Conf", clsUtil_Files.getExternalSdCardPath() + "/kssmarket/database/"));
        setEmpresaCIRIF(prefs.getString("CIRIF_Conf", ""));
        setEmpresaNombre(prefs.getString("Nombre_Conf", ""));
        setEmpresaContacto(prefs.getString("Contacto_Conf", ""));
        setEmpresaDireccion(prefs.getString("Direccion_Conf", ""));
        setEmpresaEmailGmail(prefs.getString("Email_Conf", ""));
        setEmpresaPassWordGmail(prefs.getString("Password_Conf", ""));
        setEmpresaTelf(prefs.getString("Telf_Conf", ""));
        setIva1(prefs.getString("IVA1_Conf", ""));
        setIva2(prefs.getString("IVA2_Conf", ""));
        setIva3(prefs.getString("IVA3_Conf", ""));
        setRutaDocs(prefs.getString("rutaDocs", clsUtil_Files.getExternalSdCardPath() + "/kssmarket/doc/"));
        setRutaMedia(prefs.getString("rutaFotos", clsUtil_Files.getExternalSdCardPath() + "/kssmarket/media/"));
        setNombreBD(prefs.getString("etBD_Conf", "kssmarket_db"));
        setDropBox_AuthToken(prefs.getString("DropBox_AuthToken", ""));
        setDropBoxEmail(prefs.getString("DropBoxEmail", ""));
        setDropBoxUser(prefs.getString("DropBoxUser", ""));
        setDropBox_AcountType(prefs.getString("DropBox_AcountType", ""));
        setEsPrefGuardadas(prefs.getBoolean("EsPrefGuardadas", false));

        setEsConfigCargada(true);
    }

    public Boolean guardarCambios() {
        try {
            SharedPreferences.Editor edit = prefs.edit();
            edit.putString("Ganancia_Conf", getGanancia());
            edit.putString("URIBackup_Conf", getRutaBackupBD());
            edit.putString("Nombre_Conf", getEmpresaNombre());
            edit.putString("CIRIF_Conf", getEmpresaCIRIF());
            edit.putString("Contacto_Conf", getEmpresaContacto());
            edit.putString("Direccion_Conf", getEmpresaDireccion());
            edit.putString("Email_Conf", getEmpresaEmailGmail());
            edit.putString("Password_Conf", getEmpresaPassWordGmail());
            edit.putString("Telf_Conf", getEmpresaTelf());
            edit.putString("IVA1_Conf", getIva1());
            edit.putString("IVA2_Conf", getIva2());
            edit.putString("IVA3_Conf", getIva3());
            edit.putString("rutaDocs", getRutaDocs());
            edit.putString("rutaFotos", getRutaMedia());
            edit.putString("etBD_Conf", getNombreBD());
            edit.putString("DropBox_AuthToken", getDropBox_AuthToken());
            edit.putString("DropBoxEmail", getDropBoxEmail());
            edit.putString("DropBoxUser", getDropBoxUser());
            edit.putString("DropBox_AcountType", getDropBox_AcountType());
            edit.putBoolean("EsPrefGuardadas", getEsPrefGuardadas());
            edit.putString("rutaFotos", getRutaMedia());
            edit.putString("rutaDocs", getRutaDocs());

            edit.commit();
            return true;
        } catch (Exception e) {
            ManejadorErrores(context, e, true, true);
            return false;
        }
    }

    public static kssSettings initSettings(SharedPreferences prefs) {
        instance = new kssSettings(prefs);
        return instance;
    }

    public static kssSettings getInstance(Context context) {
        if (instance == null) {
            throw new IllegalStateException("You must first call initSettings on AndroidSettings.");
        } else
            kssSettings.context = context;
        return instance;
    }
//endregion


    public String getDropBox_AcountType() {
        return DropBox_AcountType;
    }

    public void setDropBox_AcountType(String dropBox_AcountType) {
        DropBox_AcountType = dropBox_AcountType;
    }

    public String getDropBox_ClienteDir() {
        String result = getEmpresaNombre();
        if (result.trim().length() == 0)
            setEmpresaNombre("Empresa Demo");
        result = "/" + getEmpresaNombre() + "/";
        return result;
    }

    public boolean esDropBoxAuthToken() {
        return getDropBox_AuthToken().equals("") ? false : true;
    }

    public String getDropBox_AuthToken() {
        return DropBox_AuthToken;
    }

    public void setDropBox_AuthToken(String dropboxAuthToken) {
        DropBox_AuthToken = dropboxAuthToken;
    }

    public String getGanancia() {
        return Ganancia;
    }

    public void setGanancia(String ganancia) {
        Ganancia = ganancia;
    }

    public String getDropBoxEmail() {
        return DropBoxEmail;
    }

    public void setDropBoxEmail(String dropBoxEmail) {
        DropBoxEmail = dropBoxEmail;
    }

    public String getDropBoxUser() {
        return DropBoxUser;
    }

    public void setDropBoxUser(String dropBoxUser) {
        DropBoxUser = dropBoxUser;
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

    @Override
    public String toString() {
        return "Nombre='" + empresaNombre + '\'' +
                ", Rif='" + empresaCIRIF + '\'' +
                ", Telefono='" + empresaTelf + '\''
                ;
    }
}
