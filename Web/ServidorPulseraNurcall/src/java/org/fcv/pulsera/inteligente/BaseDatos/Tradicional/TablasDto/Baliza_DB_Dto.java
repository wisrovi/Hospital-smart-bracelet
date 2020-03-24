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
public class Baliza_DB_Dto {

    private int idBaliza;
    private String macBaliza;
    private String ipBaliza;
    private String sede;
    private String area;
    private String piso;
    private int xplano = -1;
    private int yplano = -1;
    private boolean indHabilitado;
    private String usuario;
    private int idUsuario;
    private Date fechaRegistro;
    private int idSede;
    private int idPiso;
    private int idArea;

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

    public String getIpBaliza() {
        return ipBaliza;
    }

    public void setIpBaliza(String ipBaliza) {
        this.ipBaliza = ipBaliza;
    }

    public String getSede() {
        return sede;
    }

    public void setSede(String sede) {
        this.sede = sede;
    }

    public int getIdSede() {
        return idSede;
    }

    public void setIdSede(int idSede) {
        this.idSede = idSede;
    }

    public String getPiso() {
        return piso;
    }

    public void setPiso(String piso) {
        this.piso = piso;
    }

    public int getIdPiso() {
        return idPiso;
    }

    public void setIdPiso(int idPiso) {
        this.idPiso = idPiso;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public int getIdArea() {
        return idArea;
    }

    public void setIdArea(int idArea) {
        this.idArea = idArea;
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

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public int getXplano() {
        return xplano;
    }

    public void setXplano(int xplano) {
        this.xplano = xplano;
    }

    public int getYplano() {
        return yplano;
    }

    public void setYplano(int yplano) {
        this.yplano = yplano;
    }

}
