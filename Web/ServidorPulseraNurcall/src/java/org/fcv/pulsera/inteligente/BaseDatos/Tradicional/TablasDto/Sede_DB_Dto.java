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
public class Sede_DB_Dto {

    private int idSede;
    private String nomSede;
    private boolean indHabilitado;
    private String usuario;
    private Date fechaRegistro;

    public int getIdSede() {
        return idSede;
    }

    public void setIdSede(int idSede) {
        this.idSede = idSede;
    }

    public String getNomSede() {
        return nomSede;
    }

    public void setNomSede(String nomSede) {
        this.nomSede = nomSede;
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
