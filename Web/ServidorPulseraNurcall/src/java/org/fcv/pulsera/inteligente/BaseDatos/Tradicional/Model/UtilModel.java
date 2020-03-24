/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fcv.pulsera.inteligente.BaseDatos.Tradicional.Model;

import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;

/**
 *
 * @author williamrodriguez
 */
public class UtilModel {

    public static String getProperties(String properties, String llave) throws Exception {
        String extencionProperties = "/org/fcv/pulsera/inteligente/";

        Properties props;
        InputStream fis;
        String key;
        props = new Properties();
        fis = UtilModel.class.getResourceAsStream(extencionProperties + properties);
        props.load(fis);
        fis.close();
        for (Enumeration ruta2 = props.keys(); ruta2.hasMoreElements();) {
            key = (String) ruta2.nextElement();
            if (key.equals(llave)) {
                return props.getProperty(key);
            }
        }
        return null;
    }

    public static void Exception(Exception e) {
        e.printStackTrace();
    }

    public static String ReemplaceEnString(String cadena, String caracterBuscar, String stringCambio) {
        int indice = cadena.indexOf(caracterBuscar);
        String loro = cadena.substring(0, indice + 1);
        loro = loro.replace(caracterBuscar, stringCambio);
        cadena = loro + cadena.substring(indice + 1, cadena.length());
        return cadena;
    }

    public static boolean ValidarMAC(String mac) {
        char[] Arraycadena = mac.toCharArray();
        int conteo = 0;
        for (int i = 0; i < Arraycadena.length; i++) {
            char caracter = Arraycadena[i];
            if (caracter == ':') {
                conteo++;
            }
        }
        if(conteo == 5){
            return true;
        }else{
            return false;
        }
    }
}
