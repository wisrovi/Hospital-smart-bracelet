package org.fcv.pulsera.inteligente.Formbean;

import java.io.IOException;
import java.io.Serializable;
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
import org.fcv.pulsera.inteligente.BaseDatos.Tradicional.Model.AccionEnum;
import org.fcv.pulsera.inteligente.BaseDatos.Tradicional.Model.Model;
import org.fcv.pulsera.inteligente.BaseDatos.Tradicional.TablasDto.Piso_DB_Dto;
import org.fcv.pulsera.inteligente.BaseDatos.Tradicional.TablasDto.Sede_DB_Dto;
import org.fcv.pulsera.inteligente.Dto.Database.UserSession;
import org.fcv.pulsera.inteligente.Util.SessionUtils;
import org.primefaces.event.RowEditEvent;
import java.util.Date;
import org.fcv.pulsera.inteligente.BaseDatos.Tradicional.TablasDto.Area_DB_Dto;

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
public class UbicacionFormBean implements Serializable {

    private Model model;
    private UserSession dataUser;

    private String mostrarCrearSede;
    private String mostrarEditarSede;

    private String mostrarCrearUbicacion;
    private String mostrarEditarUbicacion;

    private String mostrarCrearArea;
    private String mostrarEditarArea;

    public UbicacionFormBean() {
        HttpSession session = SessionUtils.getSession();
        dataUser = (UserSession) session.getAttribute("dataUser");

        if (dataUser == null) {
            try {
                FacesContext.getCurrentInstance().getExternalContext().redirect("./index.xhtml");
            } catch (IOException ex) {
                Logger.getLogger(TemplateFormbean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (dataUser.getIdRolUser() == 2) {
            mostrarCrearSede = Boolean.toString(true);
        } else {
            mostrarCrearSede = Boolean.toString(false);
        }

        if (dataUser.getIdRolUser() == 2 || dataUser.getIdRolUser() == 3) {
            mostrarEditarSede = Boolean.toString(true);
            mostrarCrearUbicacion = Boolean.toString(true);
            mostrarCrearArea = Boolean.toString(true);
            mostrarEditarUbicacion = Boolean.toString(true);
        } else {
            mostrarEditarSede = Boolean.toString(false);
            mostrarCrearUbicacion = Boolean.toString(false);
            mostrarCrearArea = Boolean.toString(false);
            mostrarEditarUbicacion = Boolean.toString(false);
        }

        if (dataUser.getIdRolUser() == 2 || dataUser.getIdRolUser() == 3 || dataUser.getIdRolUser() == 4) {
            mostrarEditarArea = Boolean.toString(true);
        } else {
            mostrarEditarArea = Boolean.toString(false);
        }

        try {
            model = new Model();
        } catch (Exception ex) {
            Logger.getLogger(UbicacionFormBean.class.getName()).log(Level.SEVERE, null, ex);
        }

        initSede();
        initPiso();
        initArea();
    }

    //<editor-fold defaultstate="collapsed" desc="Funciones Sede"> 
    private List<Sede_DB_Dto> sedesInstaladas;
    private Sede_DB_Dto sede_DB_Dto;

    private void initSede() {
        sedesInstaladas = new ArrayList<>();
        sede_DB_Dto = new Sede_DB_Dto();
        sede_DB_Dto.setUsuario(Integer.toString(dataUser.getIdUsuario()));
        getListaSedes();
    }

    private void getListaSedes() {
        if (model != null) {
            sedesInstaladas = model.ProcessSede(new Sede_DB_Dto(), AccionEnum.READ);
            if (sedesInstaladas == null) {
                sedesInstaladas = new ArrayList<>();
            }
        }
    }

    public List<Sede_DB_Dto> getListSedes() {
        return sedesInstaladas;
    }

    public void onRowEditSede(RowEditEvent event) {
        Sede_DB_Dto sedeDto = (Sede_DB_Dto) event.getObject();
        boolean yaExiste = false;
        boolean encontreAnterior = false;
        Sede_DB_Dto datoAnterior = null;
        List<Sede_DB_Dto> listReal = model.ProcessSede(new Sede_DB_Dto(), AccionEnum.READ);
        for (Iterator<Sede_DB_Dto> iterator = listReal.iterator(); iterator.hasNext();) {
            Sede_DB_Dto next = iterator.next();
            if (next.getIdSede() == sedeDto.getIdSede()) {
                datoAnterior = next;
                encontreAnterior = true;
            }
            if (next.getNomSede().equals(sedeDto.getNomSede())) {
                yaExiste = true;
            }
            if (encontreAnterior && yaExiste) {
                break;
            }
        }
        if (yaExiste) {
            addMessage("La edición no se puede realizar.");
            sedeDto.setNomSede(datoAnterior.getNomSede());
            sedeDto.setIndHabilitado(datoAnterior.isIndHabilitado());
        } else {
            List<Sede_DB_Dto> processSede = model.ProcessSede(sedeDto, AccionEnum.UPDATE);
            if (processSede != null) {
                addMessage("Sede Editada " + Integer.toString(sedeDto.getIdSede()));
            }
        }
    }

    private boolean validarSiExistenDatosDuplicadosSede() {
        if (sede_DB_Dto.getNomSede().length() > 2) {
            for (Iterator<Sede_DB_Dto> iterator = sedesInstaladas.iterator(); iterator.hasNext();) {
                Sede_DB_Dto next = iterator.next();
                if (next.getNomSede().equals(sede_DB_Dto.getNomSede())) {
                    return true;
                }
            }
        } else {
            return true;
        }
        return false;
    }

    public String buttonGuardarSede() {
        boolean yaExiste = validarSiExistenDatosDuplicadosSede();
        if (!yaExiste) {
            List<Sede_DB_Dto> processSede = model.ProcessSede(sede_DB_Dto, AccionEnum.WRITE);
            if (processSede.size() > sedesInstaladas.size()) {
                getListaSedes();
                addMessage("Sede registrada correctamente.");
                sede_DB_Dto = new Sede_DB_Dto();
            }
        } else {
            addMessage("Hubo un error al insertar la nueva Sede, por favor verifique los datos e intentelo nuevamente.");
        }
        return "";
    }

    public Sede_DB_Dto getSede_DB_Dto() {
        return sede_DB_Dto;
    }

    public void setSede_DB_Dto(Sede_DB_Dto sede_DB_Dto) {
        this.sede_DB_Dto = sede_DB_Dto;
    }

    public String getMostrarCrearSede() {
        return mostrarCrearSede;
    }

    public String getMostrarEditarSede() {
        return mostrarEditarSede;
    }

    public List<String> getSedesInstaladas() {
        List<String> listado = new ArrayList<>();
        for (Iterator<Sede_DB_Dto> iterator = sedesInstaladas.iterator(); iterator.hasNext();) {
            Sede_DB_Dto next = iterator.next();
            listado.add(next.getNomSede());
        }
        return listado;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Funciones Piso">
    private List<Piso_DB_Dto> pisosDisponibles;
    private Piso_DB_Dto piso_DB_Dto;

    private void initPiso() {
        pisosDisponibles = new ArrayList<>();
        piso_DB_Dto = new Piso_DB_Dto();
        piso_DB_Dto.setIdUsuarioRegistro(dataUser.getIdUsuario());
        piso_DB_Dto.setUsuario(dataUser.getUsuario());
        piso_DB_Dto.setFecharegistro(new Date());
        getListaPisos();
    }

    private void getListaPisos() {
        if (model != null) {
            pisosDisponibles = model.ProcessPiso(new Piso_DB_Dto(), AccionEnum.READ);
            if (pisosDisponibles == null) {
                pisosDisponibles = new ArrayList<>();
            }
        }
    }

    private boolean validarSiExistenDatosDuplicadosUbicacion() {
        if (piso_DB_Dto.getPiso() != null) {
            if (piso_DB_Dto.getPiso().length() > 3) {
                for (Iterator<Piso_DB_Dto> iterator = pisosDisponibles.iterator(); iterator.hasNext();) {
                    Piso_DB_Dto next = iterator.next();
                    if (next.getPiso().equals(piso_DB_Dto.getPiso()) && next.getSede().equals(piso_DB_Dto.getSede())) {
                        addMessage("El piso ya existe para esta sede.");
                        return true;
                    }
                }
            } else {
                addMessage("El nombre del piso debe contener minimo 4 caracteres");
                return true;
            }
        } else {
            addMessage("Escriba un nombre para el piso, no puede estar vacio");
            return true;
        }
        return false;
    }

    public String buttonGuardarUbicacion() {
        for (Iterator<Sede_DB_Dto> iterator = sedesInstaladas.iterator(); iterator.hasNext();) {
            Sede_DB_Dto next = iterator.next();
            if (next.getNomSede().equals(piso_DB_Dto.getSede())) {
                piso_DB_Dto.setIdSede(next.getIdSede());
                break;
            }
        }

        boolean yaExiste = validarSiExistenDatosDuplicadosUbicacion();
        if (!yaExiste) {
            List<Piso_DB_Dto> processUbicacion = model.ProcessPiso(piso_DB_Dto, AccionEnum.WRITE);
            if (processUbicacion.size() > pisosDisponibles.size()) {
                pisosDisponibles = processUbicacion;
                addMessage("Piso registrado correctamente.");
                piso_DB_Dto = new Piso_DB_Dto();
            }
        } else {
            addMessage("Hubo un error al insertar la nueva ubicación, por favor verifique los datos e intentelo nuevamente.");
        }

        return "";
    }

    public String getMostrarCrearUbicacion() {
        return mostrarCrearUbicacion;
    }

    public String getMostrarEditarUbicacion() {
        return mostrarEditarUbicacion;
    }

    public List<Piso_DB_Dto> getPisosDisponibles() {
        return pisosDisponibles;
    }

    public Piso_DB_Dto getPiso_DB_Dto() {
        return piso_DB_Dto;
    }

    public void setPiso_DB_Dto(Piso_DB_Dto piso_DB_Dto) {
        this.piso_DB_Dto = piso_DB_Dto;
    }

    public void onRowEditUbicacion(RowEditEvent event) {
        Piso_DB_Dto pisoDto = (Piso_DB_Dto) event.getObject();

        for (Iterator<Sede_DB_Dto> iterator = sedesInstaladas.iterator(); iterator.hasNext();) {
            Sede_DB_Dto next = iterator.next();
            if (next.getNomSede().equals(pisoDto.getSede())) {
                pisoDto.setIdSede(next.getIdSede());
                break;
            }
        }

        boolean yaExiste = false;
        boolean encontreAnterior = false;
        Piso_DB_Dto datoAnterior = null;
        List<Piso_DB_Dto> listReal = model.ProcessPiso(new Piso_DB_Dto(), AccionEnum.READ);
        for (Iterator<Piso_DB_Dto> iterator = listReal.iterator(); iterator.hasNext();) {
            Piso_DB_Dto next = iterator.next();
            if (next.getIdZ() == pisoDto.getIdZ()) {
                datoAnterior = next;
                encontreAnterior = true;
            }
            if (next.getIdSede() == pisoDto.getIdSede()) {
                if (next.getPiso().equals(pisoDto.getPiso())) {
                    addMessage("Ya existe este piso para esta sede");
                    yaExiste = true;
                }
            }
            if (encontreAnterior && yaExiste) {
                break;
            }
        }
        if (yaExiste) {
            addMessage("La edición no se puede realizar.");
            pisoDto.setSede(datoAnterior.getSede());
            pisoDto.setPiso(datoAnterior.getPiso());
            pisoDto.setIndHabilitado(datoAnterior.isIndHabilitado());
        } else {
            List<Piso_DB_Dto> processSede = model.ProcessPiso(pisoDto, AccionEnum.UPDATE);
            if (processSede != null) {
                addMessage("Ubicacion Editada para " + Integer.toString(pisoDto.getIdZ()) + " en la sede " + pisoDto.getSede());
            } else {
                addMessage("La edición no se puede realizar.");
            }
        }
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Funciones Area">
    private List<Area_DB_Dto> areasDisponibles;
    private Area_DB_Dto area_DB_Dto;

    private void initArea() {
        areasDisponibles = new ArrayList<>();
        area_DB_Dto = new Area_DB_Dto();
        area_DB_Dto.setIdUsuarioRegistro(dataUser.getIdUsuario());
        area_DB_Dto.setUsuario(dataUser.getUsuario());
        area_DB_Dto.setFechaRegistro(new Date());
        getListaAreas();
    }

    private boolean validarSiExistenDatosDuplicadosArea(List<Area_DB_Dto> areaDisponiblesEvaluar, int idArea) {
        Area_DB_Dto basicConfig = new Area_DB_Dto();
        if (basicConfig == area_DB_Dto) {
            return true;
        }

        if (basicConfig.getIdSede() == area_DB_Dto.getIdSede()) {
            addMessage("Debe elegir una sede");
            return true;
        }

        if (basicConfig.getIdZ() == area_DB_Dto.getIdZ()) {
            addMessage("Debe elegir un piso");
            return true;
        }

        if (area_DB_Dto.getArea().length() < 3) {
            addMessage("Debe escribir un nombre para el area");
            return true;
        }

        if (basicConfig.getxFinal() == area_DB_Dto.getxFinal()
                || basicConfig.getyFinal() == area_DB_Dto.getyFinal()) {
            addMessage("Debe diligenciar las coordenadas finales donde estará el area a crear");
            return true;
        }

        if (area_DB_Dto.getxFinal() == area_DB_Dto.getxInicial()
                || area_DB_Dto.getyFinal() == area_DB_Dto.getyInicial()) {
            addMessage("Los puntos inicial y final no pueden ser los mismos");
            return true;
        }

        if (area_DB_Dto.getyInicial() < 0 || area_DB_Dto.getyFinal() < 0
                || area_DB_Dto.getxInicial() < 0 || area_DB_Dto.getxFinal() < 0) {
            addMessage("Debe diligenciar unas coordenadas positivas");
            return true;
        }

        for (Iterator<Area_DB_Dto> iterator = areaDisponiblesEvaluar.iterator(); iterator.hasNext();) {
            Area_DB_Dto next = iterator.next();
            if (next.getIdSede() == area_DB_Dto.getIdSede()
                    && next.getIdZ() == area_DB_Dto.getIdZ()) {

                if (next.getArea().equals(area_DB_Dto.getArea()) && idArea == 0) {
                    addMessage("Ya hay un area registrada con el nombre diligenciado.");
                    return true;
                }

                if (idArea != next.getIdArea()) {
                    int oldXinicial = next.getxInicial();
                    int oldXfinal = next.getxFinal();
                    int oldYinicial = next.getyInicial();
                    int oldYfinal = next.getyFinal();

                    int newXinicial = area_DB_Dto.getxInicial();
                    int newXfinal = area_DB_Dto.getxFinal();
                    int newYinicial = area_DB_Dto.getyInicial();
                    int newYfinal = area_DB_Dto.getyFinal();

                    int sizeX = newXfinal + oldXfinal;
                    int sizeY = newYfinal + oldYfinal;

                    int oldFigure[][] = new int[sizeX][sizeY];;
                    for (int x = oldXinicial; x < oldXfinal; x++) {
                        for (int y = oldYinicial; y < oldYfinal; y++) {
                            oldFigure[x][y] = 1;
                        }
                    }

                    /*System.err.println("Matrix vieja");
                    for (int x = 0; x < sizeX; x++) {
                        String fila = "";
                        for (int y = 0; y < sizeY; y++) {
                            if(oldFigure[x][y]==1){
                                fila = fila + "*";
                            }else{
                                fila = fila + ".";
                            }
                        }
                        System.err.println(fila);
                    }*/
                    int newFigure[][] = new int[sizeX][sizeY];;
                    for (int x = newXinicial; x < newXfinal; x++) {
                        for (int y = newYinicial; y < newYfinal; y++) {
                            newFigure[x][y] = 1;
                        }
                    }

                    /*System.err.println("Matrix nueva");
                    for (int x = 0; x < sizeX; x++) {
                        String fila = "";
                        for (int y = 0; y < sizeY; y++) {
                            if(newFigure[x][y]==1){
                                fila = fila + "*";
                            }else{
                                fila = fila + ".";
                            }
                        }
                        System.err.println(fila);
                    }*/
                    boolean hayIntercepcion = false;
                    for (int x = 0; x < sizeX; x++) {
                        for (int y = 0; y < sizeY; y++) {
                            int puntoXevaluar = x;
                            int puntoYevaluar = y;
                            if (newFigure[x][y] == 1 && oldFigure[x][y] == 1) {
                                hayIntercepcion = true;
                                break;
                            }
                        }
                        if (hayIntercepcion) {
                            break;
                        }
                    }
                    if (hayIntercepcion) {
                        System.err.println("Se tocan las areas...");
                        addMessage("Esta intentando crear un area encima del area " + next.getArea() + " , por revise los puntos de 'X' y 'Y'");
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void refreshPiso() {
        int idSedeElegida = 0;
        for (Iterator<Sede_DB_Dto> iterator = sedesInstaladas.iterator(); iterator.hasNext();) {
            Sede_DB_Dto next = iterator.next();
            if (next.getNomSede().equals(area_DB_Dto.getSede())) {
                idSedeElegida = next.getIdSede();
                break;
            }
        }

        area_DB_Dto.setIdSede(idSedeElegida);
    }

    public List<String> getPisosInstaladadosPorSede() {
        List<Piso_DB_Dto> todosPisosInstalados = model.ProcessPiso(new Piso_DB_Dto(), AccionEnum.READ);
        List<String> listado = new ArrayList<>();
        for (Iterator<Piso_DB_Dto> iterator = todosPisosInstalados.iterator(); iterator.hasNext();) {
            Piso_DB_Dto next = iterator.next();
            if (next.getIdSede() == area_DB_Dto.getIdSede()) {
                listado.add(next.getPiso());
            }
        }
        return listado;
    }

    public String buttonGuardarArea() {
        List<Piso_DB_Dto> todosPisosInstalados = model.ProcessPiso(new Piso_DB_Dto(), AccionEnum.READ);
        for (Iterator<Piso_DB_Dto> iterator = todosPisosInstalados.iterator(); iterator.hasNext();) {
            Piso_DB_Dto next = iterator.next();
            if (next.getPiso().equals(area_DB_Dto.getPiso())) {
                area_DB_Dto.setIdZ(next.getIdZ());
            }
        }

        boolean yaExiste = validarSiExistenDatosDuplicadosArea(areasDisponibles, 0);
        if (!yaExiste) {
            List<Area_DB_Dto> processArea = model.ProcessArea(area_DB_Dto, AccionEnum.WRITE);
            if (processArea.size() > areasDisponibles.size()) {
                areasDisponibles = processArea;
                addMessage("Area registrada correctamente.");
                area_DB_Dto = new Area_DB_Dto();
            }
        } else {
            addMessage("Hubo un error al insertar la nueva Area, por favor verifique los datos e intentelo nuevamente.");
        }

        return "";
    }

    public String getMostrarCrearArea() {
        return mostrarCrearArea;
    }

    public String getMostrarEditarArea() {
        return mostrarEditarArea;
    }

    public Area_DB_Dto getArea_DB_Dto() {
        return area_DB_Dto;
    }

    public void setArea_DB_Dto(Area_DB_Dto area_DB_Dto) {
        this.area_DB_Dto = area_DB_Dto;
    }

    private void getListaAreas() {
        if (model != null) {
            areasDisponibles = model.ProcessArea(new Area_DB_Dto(), AccionEnum.READ);
            if (areasDisponibles == null) {
                areasDisponibles = new ArrayList<>();
            }
        }
    }

    public List<Area_DB_Dto> getAreasDisponibles() {
        return areasDisponibles;
    }

    public void onRowEditArea(RowEditEvent event) {
        Area_DB_Dto areaDto = (Area_DB_Dto) event.getObject();

        for (Iterator<Area_DB_Dto> iterator = areasDisponibles.iterator(); iterator.hasNext();) {
            Area_DB_Dto next = iterator.next();
            if (next.getSede().equals(areaDto.getSede())) {
                areaDto.setIdSede(next.getIdSede());
                break;
            }
        }

        for (Iterator<Area_DB_Dto> iterator = areasDisponibles.iterator(); iterator.hasNext();) {
            Area_DB_Dto next = iterator.next();
            if (next.getArea().equals(areaDto.getArea())) {
                areaDto.setIdArea(next.getIdArea());
                break;
            }
        }

        boolean yaExiste = false;
        Area_DB_Dto datoAnterior = null;
        List<Area_DB_Dto> listReal = model.ProcessArea(new Area_DB_Dto(), AccionEnum.READ);
        for (Iterator<Area_DB_Dto> iterator = listReal.iterator(); iterator.hasNext();) {
            Area_DB_Dto next = iterator.next();
            if (next.getIdArea() == areaDto.getIdArea()) {
                datoAnterior = next;
                break;
            }
        }

        area_DB_Dto = areaDto;
        yaExiste = validarSiExistenDatosDuplicadosArea(listReal, areaDto.getIdArea());

        if (yaExiste) {
            addMessage("La edición no se puede realizar.");
            areaDto.setSede(datoAnterior.getPiso());
            areaDto.setPiso(datoAnterior.getArea());
            areaDto.setxInicial(datoAnterior.getxInicial());
            areaDto.setxFinal(datoAnterior.getxFinal());
            areaDto.setyInicial(datoAnterior.getyInicial());
            areaDto.setyFinal(datoAnterior.getyFinal());
            areaDto.setIndHabilitado(datoAnterior.isIndHabilitado());
        } else {
            List<Area_DB_Dto> processSede = model.ProcessArea(areaDto, AccionEnum.UPDATE);
            if (processSede != null) {
                addMessage("Ubicacion Editada para " + Integer.toString(areaDto.getIdArea()) + " en la sede " + areaDto.getSede() + " y piso " + areaDto.getPiso());
            } else {
                addMessage("La edición no se puede realizar.");
            }
        }
        area_DB_Dto = new Area_DB_Dto();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Funciones Generales">    
    private final static List<String> opcionesBooleanas;

    static {
        opcionesBooleanas = new ArrayList();
        opcionesBooleanas.add("true");
        opcionesBooleanas.add("false");
    }

    public List<String> getOpcionesBooleanas() {
        return opcionesBooleanas;
    }

    public void addMessage(String summary) {
        FacesMessage message = new FacesMessage("Info", summary);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public void onRowCancel(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Edición Cancelada");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }
    //</editor-fold>

}
