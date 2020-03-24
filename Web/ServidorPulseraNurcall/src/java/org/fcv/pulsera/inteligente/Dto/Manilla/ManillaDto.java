/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fcv.pulsera.inteligente.Dto.Manilla;

import java.util.Date;
import java.util.List;
import org.fcv.pulsera.inteligente.BaseDatos.Tradicional.TablasDto.BeaconDto;

/**
 *
 * @author williamrodriguez
 */
public class ManillaDto {

    public List<BalizasQueVieronUnaManillaDto> listaBalizasQueLoHanVisto;
    public Date FechaCreacion;
    public int txPower;
    public int major;
    public int minor;
    private BeaconDto beacon;

    public ManillaDto() {
    }

    public ManillaDto(List<BalizasQueVieronUnaManillaDto> listaBalizasQueLoHanVisto, Date FechaCreacion, int txPower, int major, int minor, BeaconDto beacon) {
        this.listaBalizasQueLoHanVisto = listaBalizasQueLoHanVisto;
        this.FechaCreacion = FechaCreacion;
        this.txPower = txPower;
        this.major = major;
        this.minor = minor;
        this.beacon = beacon;
    }

    public BeaconDto getBeacon() {
        return beacon;
    }

    public void setBeacon(BeaconDto beacon) {
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

    public List<BalizasQueVieronUnaManillaDto> getListaBalizasQueLoHanVisto() {
        return listaBalizasQueLoHanVisto;
    }

    public void setListaBalizasQueLoHanVisto(List<BalizasQueVieronUnaManillaDto> listaBalizasQueLoHanVisto) {
        this.listaBalizasQueLoHanVisto = listaBalizasQueLoHanVisto;
    }

    public Date getFechaCreacion() {
        return FechaCreacion;
    }

    public void setFechaCreacion(Date FechaCreacion) {
        this.FechaCreacion = FechaCreacion;
    }

}
