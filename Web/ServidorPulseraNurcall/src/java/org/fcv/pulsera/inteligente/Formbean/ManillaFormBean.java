package org.fcv.pulsera.inteligente.Formbean;

import java.io.Serializable;
import java.util.Date;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import org.fcv.pulsera.inteligente.BaseDatos.Tradicional.Model.Model;
import org.fcv.pulsera.inteligente.BaseDatos.Tradicional.Model.UtilModel;
import org.fcv.pulsera.inteligente.BaseDatos.Tradicional.TablasDto.UnionManillaDto;
import org.fcv.pulsera.inteligente.Dto.Database.UserSession;
import org.fcv.pulsera.inteligente.Util.SessionUtils;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.RowEditEvent;

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
public class ManillaFormBean implements Serializable {

    private Model model;
    private UserSession dataUser;

    private List<UnionManillaDto> listManillas;
    private UnionManillaDto datosCrearManilla;
    private boolean botonGuardarDeshabilitado = true;

    public ManillaFormBean() {
        HttpSession session = SessionUtils.getSession();
        dataUser = (UserSession) session.getAttribute("dataUser");

        listManillas = new ArrayList<>();
        datosCrearManilla = new UnionManillaDto();
        datosCrearManilla.setFechaRegistro(new Date());
        datosCrearManilla.setUsuario(Integer.toString(dataUser.getIdUsuario()));

        try {
            model = new Model();
        } catch (Exception ex) {
            Logger.getLogger(ManillaFormBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        getListaManillas("");
    }

    private void getListaManillas(String idManilla) {
        if (model != null) {
            listManillas = model.getListManillas(idManilla);
            if (listManillas == null) {
                listManillas = new ArrayList<>();
            }
        }
    }

    public List<UnionManillaDto> getListManillas() {
        return listManillas;
    }

    public void onRowEdit(RowEditEvent event) {
        UnionManillaDto unionManillaDto = (UnionManillaDto) event.getObject();
        FacesMessage msg = new FacesMessage("Manilla Editada", Integer.toString(unionManillaDto.getIdManilla()));
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void onRowCancel(RowEditEvent event) {
        UnionManillaDto unionManillaDto = (UnionManillaDto) event.getObject();
        FacesMessage msg = new FacesMessage("Edición manilla Cancelada", Integer.toString(unionManillaDto.getIdManilla()));
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void onCellEdit(CellEditEvent event) {
        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();

        if (newValue != null && !newValue.equals(oldValue)) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cell Changed", "Old: " + oldValue + ", New:" + newValue);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    public UnionManillaDto getDatosCrearManilla() {
        return datosCrearManilla;
    }

    public void setDatosCrearManilla(UnionManillaDto datosCrearManilla) {
        this.datosCrearManilla = datosCrearManilla;
    }

    private boolean validarSiExistenDatosDuplicados(boolean nuevoLote) {
        boolean yaExiste = false;
        boolean superoPrevalidacion = true;
        if (datosCrearManilla.getBeacon().length() == 17) {
            if (datosCrearManilla.getEddystone().length() == 17) {
                if (!UtilModel.ValidarMAC(datosCrearManilla.getBeacon())) {
                    addMessage("La MAC de iBeacon no es valida, recuerde usar los caracteres ':'.");
                    superoPrevalidacion = false;
                } else if (!UtilModel.ValidarMAC(datosCrearManilla.getEddystone())) {
                    addMessage("La MAC de Eddystone no es valida, recuerde usar los caracteres ':'.");
                    superoPrevalidacion = false;
                } else {
                    datosCrearManilla.setBeacon(datosCrearManilla.getBeacon().toUpperCase());
                    datosCrearManilla.setEddystone(datosCrearManilla.getEddystone().toUpperCase());
                
                    if (datosCrearManilla.getMajor() == 0) {
                        superoPrevalidacion = false;
                        addMessage("El valor MAJOR no puede ser cero, por favor coloque un valor correcto, recuerde que este es la cantidad de unidades producidas.");
                    } else if (datosCrearManilla.getMinor() == 0) {
                        addMessage("El valor MINOR no puede ser cero, por favor coloque un valor correcto, recuerde que el serial de este lote, iniciando desde uno.");
                        superoPrevalidacion = false;
                    } else if (datosCrearManilla.getTxPower() == -150) {
                        addMessage("TxPower no puede ser '-150', mida el RSSI a un metro de distancia de la manilla y anote el valor proporcionado, este dato es muy importante para poder hallar correctamente la localización.");
                        superoPrevalidacion = false;
                    } else if (datosCrearManilla.getTxPower() > 0) {
                        addMessage("TxPower debe ser negativo, mida el RSSI a un metro de distancia de la manilla y anote el valor proporcionado, este dato es muy importante para poder hallar correctamente la localización.");
                        superoPrevalidacion = false;
                    } else if (datosCrearManilla.getMaxCardio() == 0) {
                        addMessage("El valor máximo del pulso cardiaco no puede ser cero, por favor anote un umbral máximo correcto, recuerde que este valor determinará cuando se evidencie una alarma cardiaca al médico y a la enfermera.");
                        superoPrevalidacion = false;
                    } else if (datosCrearManilla.getMinCardio() == 0) {
                        addMessage("El valor mínimo del pulso cardiaco no puede ser cero, por favor anote un umbral minimo correcto, recuerde que este valor determinará cuando se evidencie una alarma cardiaca (ejemplo un paro cardiaco) al médico y a la enfermera.");
                        superoPrevalidacion = false;
                    } else if (datosCrearManilla.getMinTemp() == 0) {
                        addMessage("El valor mínimo de temperatura no puede ser cero, por favor ingrese un valor valido, recuerde que este dato determinará la temperatura baja del paciente y generará una alarma cuando la temperatura baje del umbral establecido.");
                        superoPrevalidacion = false;
                    } else if (datosCrearManilla.getMaxTemp() == 0) {
                        addMessage("El valor máximo de temperatura no puede ser cero, por favor ingrese un valor valido, recuerde que este dato determinará la temperatura alta del paciente (ejemplo fiebre) y generará una alarma cuando la temperatura suba del umbral establecido. ");
                        superoPrevalidacion = false;
                    }
                    
                    if (superoPrevalidacion) {
                        if(datosCrearManilla.getMinor() > datosCrearManilla.getMajor()){
                            superoPrevalidacion = false;
                            addMessage("El valor MINOR no puede ser mayor al MAJOR, si no tiene MINOR disponibles, habra un nuevo lote.");
                        }
                    }

                    if (superoPrevalidacion) {
                        boolean loteDiferenteCreados = true;
                        for (Iterator<UnionManillaDto> iterator = listManillas.iterator(); iterator.hasNext();) {
                            UnionManillaDto listManilla = iterator.next();   
                            if (listManilla.getBeacon().equals(datosCrearManilla.getBeacon())) {
                                addMessage("La MAC de iBeacon ya esta registrada.");
                                yaExiste = true;
                                break;
                            } else if (listManilla.getEddystone().equals(datosCrearManilla.getEddystone())) {
                                addMessage("La MAC de Eddystone ya esta registrada.");
                                yaExiste = true;
                                break;
                            } else if (listManilla.getMinor() == datosCrearManilla.getMinor() 
                                    && listManilla.getMajor() == datosCrearManilla.getMajor()) {
                                yaExiste = true;
                                addMessage("El valor MINOR de iBeacon ya existe en el lote de " + Integer.toString(datosCrearManilla.getMajor()) + " unidades.");
                                break;
                            }else{
                                if(listManilla.getMajor() == datosCrearManilla.getMajor()){
                                    loteDiferenteCreados = false;
                                }                                
                            }
                        }
                        if (loteDiferenteCreados && !yaExiste) {
                            addMessage("Esta a un click de crear un nuevo lote de producción al utilizar un valor de MAJOR distinto a los actuales, para continuar oprima el boton 'GUARDAR'");
                        }
                    }

                    if (superoPrevalidacion) {
                        if (!yaExiste && nuevoLote) {
                            //no existe, se habilita el boton de guardar
                            datosCrearManilla.setHabilitadoManilla(true);
                        }
                        botonGuardarDeshabilitado = yaExiste;
                    }
                }

            } else {
                superoPrevalidacion = false;
                addMessage("Por favor ingrese una dirección MAC valida para el Eddystone.");
            }
        } else {
            superoPrevalidacion = false;
            addMessage("Por favor ingrese una dirección MAC valida para el iBeacon.");
        }

        if (!yaExiste && superoPrevalidacion && !nuevoLote) {
            addMessage("La validación ha sido correcta, por favor oprima el boton 'GUARDAR'");
        }
        return yaExiste;
    }

    public String buttonValidar() {
        botonGuardarDeshabilitado = true;
        validarSiExistenDatosDuplicados(false);
        return "";
    }

    public String buttonGuardar() {
        botonGuardarDeshabilitado = true;
        boolean yaExiste = validarSiExistenDatosDuplicados(true);
        if (!yaExiste) {
            int rtaCrearManilla = model.InsertarNuevaManilla(datosCrearManilla);
            if (rtaCrearManilla > 0) {
                getListaManillas("");
                addMessage("Manilla registrada correctamente.");
                datosCrearManilla = new UnionManillaDto();
            } else {
                addMessage("Hubo un error al insertar la manilla, por favor intentelo nuevamente.");
            }
        }

        return "";
    }

    public boolean isBotonGuardarHabilitado() {
        return botonGuardarDeshabilitado;
    }

    public void addMessage(String summary) {
        FacesMessage message = new FacesMessage("Info", summary);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
}
