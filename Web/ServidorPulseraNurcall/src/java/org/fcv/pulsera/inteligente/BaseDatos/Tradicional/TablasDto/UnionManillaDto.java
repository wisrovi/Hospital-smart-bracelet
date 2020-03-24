/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fcv.pulsera.inteligente.BaseDatos.Tradicional.TablasDto;

import java.util.Date;

/**
 *
 * @author williamrodriguez
 */
public class UnionManillaDto {
    private int idManilla;
    private String beacon;
    private int txPower = -150;
    private int major;
    private int minor;
    private String eddystone;
    private int minTemp;
    private int maxTemp;
    private int minCardio;
    private int maxCardio;
    private boolean habilitadoManilla;
    private String usuario;
    private Date fechaRegistro;

    public int getIdManilla() {
        return idManilla;
    }

    public void setIdManilla(int idManilla) {
        this.idManilla = idManilla;
    }

    public String getBeacon() {
        return beacon;
    }

    public void setBeacon(String beacon) {
        this.beacon = beacon;
    }

    public int getTxPower() {
        return txPower;
    }

    public void setTxPower(int txPower) {
        this.txPower = txPower;
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

    public String getEddystone() {
        return eddystone;
    }

    public void setEddystone(String eddystone) {
        this.eddystone = eddystone;
    }

    public int getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(int minTemp) {
        this.minTemp = minTemp;
    }

    public int getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(int maxTemp) {
        this.maxTemp = maxTemp;
    }

    public int getMinCardio() {
        return minCardio;
    }

    public void setMinCardio(int minCardio) {
        this.minCardio = minCardio;
    }

    public int getMaxCardio() {
        return maxCardio;
    }

    public void setMaxCardio(int maxCardio) {
        this.maxCardio = maxCardio;
    }

    public boolean isHabilitadoManilla() {
        return habilitadoManilla;
    }

    public void setHabilitadoManilla(boolean habilitadoManilla) {
        this.habilitadoManilla = habilitadoManilla;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }
    
    
}
