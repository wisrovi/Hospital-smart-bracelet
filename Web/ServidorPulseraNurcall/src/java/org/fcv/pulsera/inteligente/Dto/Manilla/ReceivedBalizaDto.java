/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fcv.pulsera.inteligente.Dto.Manilla;

import java.util.List;

/**
 *
 * @author williamrodriguez
 */
public class ReceivedBalizaDto {
    public String baliza;
    public List<BalizaDto> beacon;
    public List<EddystoneDto> eddystone;
    
    public String getBaliza() {
        return baliza;
    }

    public void setBaliza(String baliza) {
        this.baliza = baliza;
    }

    public List<BalizaDto> getBeacon() {
        return beacon;
    }

    public void setBeacon(List<BalizaDto> beacon) {
        this.beacon = beacon;
    }

    public List<EddystoneDto> getEddystone() {
        return eddystone;
    }

    public void setEddystone(List<EddystoneDto> eddystone) {
        this.eddystone = eddystone;
    }
    
}
