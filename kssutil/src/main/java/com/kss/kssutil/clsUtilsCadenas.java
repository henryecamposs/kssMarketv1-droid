package com.kss.kssutil;

/**
 * Created by KSS on 01/07/2015.
 */
public class clsUtilsCadenas {
    /**
     * Rellenar espacios a la derecha
     * @param s
     * @param n
     * @return
     */
    public static String padRight(String s, int n) {
        return String.format("%1$-" + n + "s", s);
    }

    /**
     * Rellenar espacios a la Izquierda
     * @param s
     * @param n
     * @return
     */
    public static String padLeft(String s, int n) {
        return String.format("%1$" + n + "s", s);
    }
}
