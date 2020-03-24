/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fcv.pulsera.inteligente.Formbean;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;
import org.fcv.pulsera.inteligente.BaseDatos.Tradicional.Model.UtilModel;
import org.fcv.pulsera.inteligente.Dto.Database.UserSession;
import org.fcv.pulsera.inteligente.Util.SessionUtils;

/**
 *
 * @author williamrodriguez
 */
@ManagedBean
@ViewScoped
public class TemplateFormbean {
    
    private static String rutaPrincipal = "../index.xhtml";

    //<editor-fold defaultstate="collapsed" desc="Variables"> 
    private static String rutaArchivoProperties = "properties/proyectproperties.properties";
    public String titulo = "";
    private MenuModel menuModel;
    private String nombreUsuario;
    private UserSession dataUser;
    //</editor-fold>

    public TemplateFormbean() {
        //<editor-fold defaultstate="collapsed" desc="get Perfil Usuario"> 
        menuModel = new DefaultMenuModel();

        try {
            titulo = UtilModel.getProperties(rutaArchivoProperties, "nameProject");
        } catch (Exception ex) {
            Logger.getLogger(TemplateFormbean.class.getName()).log(Level.SEVERE, null, ex);
        }

        HttpSession session = SessionUtils.getSession();
        dataUser = (UserSession) session.getAttribute("dataUser");
        if (dataUser != null) {
            nombreUsuario = dataUser.getUsuario();
        }else{
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect(rutaPrincipal);
            } catch (IOException ex) {
                Logger.getLogger(TemplateFormbean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="Usuario"> 
        if (dataUser.getIdRolUser() == 2 || dataUser.getIdRolUser() == 3) {
            DefaultSubMenu subMenu = new DefaultSubMenu("User");
            subMenu.addElement(createNuevoItem("Create", "#", true, "home"));
            subMenu.addElement(createNuevoItem("Edit", "#", true, "home"));
            subMenu.addElement(createNuevoItem("Read", "#", true, "home"));
            menuModel.addElement(subMenu);
        }
        //</editor-fold>

        if (dataUser.getIdRolUser() == 2 || dataUser.getIdRolUser() == 3 || dataUser.getIdRolUser() == 4 || dataUser.getIdRolUser() == 5) {
            DefaultSubMenu subMenu = new DefaultSubMenu("Config");
            subMenu.addElement(createNuevoItem("Ubicación", "../faces/vistas/ubicacion.xhtml", true, "home"));
            subMenu.addElement(createNuevoItem("Baliza", "../faces/vistas/baliza.xhtml", true, "home"));
            subMenu.addElement(createNuevoItem("Manilla", "../faces/vistas/manilla.xhtml", true, "home"));
                        
            /*subMenu.addElement(createOptionsManilla());
            if (dataUser.getIdRolUser() < 5) {
                subMenu.addElement(createOptionsUbications());
            }*/
            menuModel.addElement(subMenu);
        }

        //<editor-fold defaultstate="collapsed" desc="Reportes"> 
        if (dataUser.getIdRolUser() > 1) {
            DefaultSubMenu subMenu = new DefaultSubMenu("Report");
            subMenu.addElement(createNuevoItem("Ubication", "#", true, "home"));
            subMenu.addElement(createNuevoItem("Sensors", "#", true, "home"));
            subMenu.addElement(createNuevoItem("Alerts", "#", true, "home"));
            menuModel.addElement(subMenu);
        }
        //</editor-fold>
    }

    //<editor-fold defaultstate="collapsed" desc="Menu, submenu e items"> 
    private DefaultMenuItem createNuevoItem(String nombreItem, String redirect, boolean isUrl, String icono) {
        DefaultMenuItem item = new DefaultMenuItem(nombreItem);

        if (isUrl) {
            item.setUrl(redirect);
        } else {
            item.setCommand(redirect);
        }

        switch (icono) {
            case "home": {
                item.setIcon("ui-icon-home");
            }
            break;
        }

        return item;
    }

    private DefaultSubMenu createOptionsManilla() {
        //entran los perfiles 2, 3, 4 y 5
        DefaultSubMenu subMenu = new DefaultSubMenu("Manilla");

        if (dataUser.getIdRolUser() < 5) {
            subMenu.addElement(createNuevoItem("Create", "#", true, "home"));
            if (dataUser.getIdRolUser() == 2 || dataUser.getIdRolUser() == 3) {
                subMenu.addElement(createNuevoItem("Edit", "#", true, "home"));
                subMenu.addElement(createNuevoItem("Enable / Disable", "#", true, "home"));
            }
        }

        if (dataUser.getIdRolUser() <= 5 && dataUser.getIdRolUser() != 4) {
            //entran los perfiles 2, 3 y 5
            subMenu.addElement(createNuevoItem("Relation to patient", "#", true, "home"));
        }

        return subMenu;
    }

    private DefaultSubMenu createOptionsUbications() {
        DefaultSubMenu subMenu = new DefaultSubMenu("Ubications");        
        subMenu.addElement(createOptionsBaliza());
        subMenu.addElement(createOptionsSede());
        subMenu.addElement(createOptionsPisos());
        subMenu.addElement(createOptionsAreas());
        return subMenu;
    }

    

    private DefaultSubMenu createOptionsBaliza() {
        //entra 2, 3 y 4
        DefaultSubMenu subMenu = new DefaultSubMenu("Baliza");
        subMenu.addElement(createNuevoItem("Create", "#", true, "home"));
        subMenu.addElement(createNuevoItem("Enable / Disable", "#", true, "home"));
        subMenu.addElement(createNuevoItem("Edit Ubication", "#", true, "home"));

        if (dataUser.getIdRolUser() < 4) {
            subMenu.addElement(createNuevoItem("Edit data manufacturing", "#", true, "home"));
        }

        return subMenu;
    }

    private DefaultSubMenu createOptionsSede() {
        //entra 2, 3 y 4
        DefaultSubMenu subMenu = new DefaultSubMenu("Sede");
        if (dataUser.getIdRolUser() == 2) {
            subMenu.addElement(createNuevoItem("New", "#", true, "home"));
            subMenu.addElement(createNuevoItem("Enable / Disable", "#", true, "home"));
        }

        if (dataUser.getIdRolUser() < 4) {
            subMenu.addElement(createNuevoItem("Edit", "#", true, "home"));
        }

        return subMenu;
    }

    private DefaultSubMenu createOptionsPisos() {
        //entra 2, 3 y 4
        DefaultSubMenu subMenu = new DefaultSubMenu("Pisos");
        if (dataUser.getIdRolUser() < 4) {
            subMenu.addElement(createNuevoItem("New", "#", true, "home"));
        }        
        
        subMenu.addElement(createNuevoItem("Edit", "#", true, "home"));
        
        if (dataUser.getIdRolUser() < 4) {
            subMenu.addElement(createNuevoItem("Enable / Disable", "#", true, "home"));
        }        
        return subMenu;
    }
    
    private DefaultSubMenu createOptionsAreas() {
        //entra 2, 3 y 4
        DefaultSubMenu subMenu = new DefaultSubMenu("Areas");
        if (dataUser.getIdRolUser() < 4) {
            subMenu.addElement(createNuevoItem("New", "#", true, "home"));
        }        
        subMenu.addElement(createNuevoItem("Edit", "#", true, "home"));
        if (dataUser.getIdRolUser() < 4) {
            subMenu.addElement(createNuevoItem("Enable / Disable", "#", true, "home"));
        }        
        return subMenu;
    }

    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Get y Set"> 
    public MenuModel getMenuModel() {
        return menuModel;
    }

    public void setMenuModel(MenuModel menuModel) {
        this.menuModel = menuModel;
    }

    public static String getRutaArchivoProperties() {
        return rutaArchivoProperties;
    }

    public static void setRutaArchivoProperties(String rutaArchivoProperties) {
        TemplateFormbean.rutaArchivoProperties = rutaArchivoProperties;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getTitulo() {
        return titulo;
    }

    public void cerrarSesion() throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().redirect(rutaPrincipal);
    }

    //</editor-fold>
}
