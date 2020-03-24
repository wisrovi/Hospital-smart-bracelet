/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fcv.pulsera.inteligente.Dto.Manilla;

import java.util.Date;

/**
 *
 * @author williamrodriguez
 */
public class BalizasQueVieronUnaManillaDto {

    public String MacBaliza;
    public float distanciaEstimada;
    public Date FechaVisto;
    public int rssi;

    public BalizasQueVieronUnaManillaDto(String MacBaliza, float distanciaEstimada, Date FechaVisto, int rssi) {
        this.MacBaliza = MacBaliza;
        this.distanciaEstimada = distanciaEstimada;
        this.FechaVisto = FechaVisto;
        this.rssi = rssi;
    }

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    public String getMacBaliza() {
        return MacBaliza;
    }

    public void setMacBaliza(String MacBaliza) {
        this.MacBaliza = MacBaliza;
    }

    public float getDistanciaEstimada() {
        return distanciaEstimada;
    }

    public void setDistanciaEstimada(float distanciaEstimada) {
        this.distanciaEstimada = distanciaEstimada;
    }

    public Date getFechaVisto() {
        return FechaVisto;
    }

    public void setFechaVisto(Date FechaVisto) {
        this.FechaVisto = FechaVisto;
    }

}
