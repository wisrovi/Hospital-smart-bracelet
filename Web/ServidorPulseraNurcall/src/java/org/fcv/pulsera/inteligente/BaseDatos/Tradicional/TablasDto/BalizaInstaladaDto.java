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
public class BalizaInstaladaDto {

    private int idBaliza;
    private String macBaliza;
    private String sede;
    private String piso;
    private String area;
    private boolean indHabilitado;
    private String usuario;
    private Date fechaRegistro;
    private int idSede;
    private int idPiso;
    private int idArea;
    private int xFinal;
    private int yFinal;

    public int getIdBaliza() {
        return idBaliza;
    }

    public void setIdBaliza(int idBaliza) {
        this.idBaliza = idBaliza;
    }

    public String getMacBaliza() {
        return macBaliza;
    }

    public void setMacBaliza(String macBaliza) {
        this.macBaliza = macBaliza;
    }

    public String getSede() {
        return sede;
    }

    public void setSede(String sede) {
        this.sede = sede;
    }

    public String getPiso() {
        return piso;
    }

    public void setPiso(String piso) {
        this.piso = piso;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public boolean isIndHabilitado() {
        return indHabilitado;
    }

    public void setIndHabilitado(boolean indHabilitado) {
        this.indHabilitado = indHabilitado;
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
