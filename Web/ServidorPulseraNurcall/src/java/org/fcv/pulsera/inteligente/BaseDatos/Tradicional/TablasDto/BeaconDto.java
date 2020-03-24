/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fcv.pulsera.inteligente.BaseDatos.Tradicional.TablasDto;

import java.sql.Date;

/**
 *
 * @author williamrodriguez
 */
public class BeaconDto {

    private int idBeacon;
    private String macDispositivo;
    private int major;
    private int minor;
    private int txPower;
    private boolean indHabilitado;
    private int idUsuarioRegistro;
    private Date fechaRegistro;

    public int getIdBeacon() {
        return idBeacon;
    }

    public void setIdBeacon(int idBeacon) {
        this.idBeacon = idBeacon;
    }

    public String getMacDispositivo() {
        return macDispositivo;
    }

    public void setMacDispositivo(String macDispositivo) {
        this.macDispositivo = macDispositivo;
    }

    public int getMajor() {
        return major;
    }

    public void setMajor(int major) {
        this.major = major;
    }

    public int getMinor() {
        return minor;
    }

    public void setMinor(int minor) {
        this.minor = minor;
    }

    public int getTxPower() {
        return txPower;
    }

    public void setTxPower(int txPower) {
        this.txPower = txPower;
    }

    public boolean isIndHabilitado() {
        return indHabilitado;
    }

    public void setIndHabilitado(boolean indHabilitado) {
        this.indHabilitado = indHabilitado;
    }

    public int getIdUsuarioRegistro() {
        return idUsuarioRegistro;
    }

    public void setIdUsuarioRegistro(int idUsuarioRegistro) {
        this.idUsuarioRegistro = idUsuarioRegistro;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

}
