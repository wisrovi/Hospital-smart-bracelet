package org.fcv.pulsera.inteligente.Formbean;

import org.fcv.pulsera.inteligente.Dto.Database.UserSession;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author williamrodriguez
 */
@ManagedBean
@ViewScoped
public class IndexFormBean {

    private UserSession user;

    public IndexFormBean() {
        user = new UserSession();
    }

    public String startSesion() {
        user.validateUsernamePassword();
        return "";
    }

    public UserSession getUser() {
        return user;
    }

    public void setUser(UserSession user) {
        this.user = user;
    }

}
