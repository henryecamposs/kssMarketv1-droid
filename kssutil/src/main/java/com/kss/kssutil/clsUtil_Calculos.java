package com.kss.kssutil;

/**
 * Created by KSS on 01/07/2015.
 */
public class clsUtil_Calculos {


    /**
     * Convertir Tasa en multiplo directo (12% -> 1.2)
     * @param tasa tasa porcentual a convertir
     * @return
     */
    static public Double convertirTasa(double tasa){
        return (tasa / 100) +1 ;
    }

    /**
     * Calcular el porcentaje de un Valor Base
     * @param Tasa
     * @param ValorBase
     * @return
     */
    static public Double calcularPorcentaje(Double Tasa, Double ValorBase){
        return (convertirTasa(Tasa) *    ValorBase) -ValorBase;
    }

    /**
     * Calcular el Total de un valor base mas su porcentaje
     * @param Tasa
     * @param ValorBase
     * @return
     */
    static public Double calcularValorFinalConPorcentaje(Double Tasa, Double ValorBase){
        return (convertirTasa(Tasa) *    ValorBase);
    }
}
