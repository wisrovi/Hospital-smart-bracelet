/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fcv.pulsera.inteligente.Dto.Database;

import java.sql.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.fcv.pulsera.inteligente.BaseDatos.Tradicional.Model.Model;
import org.fcv.pulsera.inteligente.Util.SessionUtils;

/**
 *
 * @author williamrodriguez
 */
@ManagedBean
@SessionScoped
public class UserSession {

    private static final long serialVersionUID = 1094801825228386363L;

    private int idUsuario;
    private String usuario;
    private int idRolUser;
    private String docUsuario;
    private Date fechaRegistro;
    private int idUsuarioRegistro;
    private int idSede;
    private boolean indHabilitado;

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public int getIdRolUser() {
        return idRolUser;
    }

    public void setIdRolUser(int idRolUser) {
        this.idRolUser = idRolUser;
    }

    public String getDocUsuario() {
        return docUsuario;
    }

    public void setDocUsuario(String docUsuario) {
        this.docUsuario = docUsuario;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public int getIdUsuarioRegistro() {
        return idUsuarioRegistro;
    }

    public void setIdUsuarioRegistro(int idUsuarioRegistro) {
        this.idUsuarioRegistro = idUsuarioRegistro;
    }

    public int getIdSede() {
        return idSede;
    }

    public void setIdSede(int idSede) {
        this.idSede = idSede;
    }

    public boolean isIndHabilitado() {
        return indHabilitado;
    }

    public void setIndHabilitado(boolean indHabilitado) {
        this.indHabilitado = indHabilitado;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    //validate login
    public String validateUsernamePassword() {
        String pageOK = "./dashboard.xhtml";
        String pageBad = "./index.xhtml";

        boolean valid = false;
        try {
            Model model = new Model();
            UserSession dataUser = model.getDataUser(usuario, docUsuario).get(0);
            valid = dataUser.isIndHabilitado();
            if (valid) {
                HttpSession session = SessionUtils.getSession();
                session.setAttribute("dataUser", dataUser);
                FacesContext.getCurrentInstance().getExternalContext().redirect(pageOK);
                return pageOK;
            } else {
                FacesContext.getCurrentInstance().addMessage(
                        null,
                        new FacesMessage(FacesMessage.SEVERITY_WARN,
                                "Incorrect Username and Passowrd",
                                "Please enter correct username and Password"));
                return pageBad;
            }
        } catch (Exception ex) {
            Logger.getLogger(UserSession.class.getName()).log(Level.SEVERE, null, ex);
            return pageBad;
        }
    }

    //logout event, invalidate session
    public String logout() {
        HttpSession session = SessionUtils.getSession();
        session.invalidate();
        return "login";
    }
}
