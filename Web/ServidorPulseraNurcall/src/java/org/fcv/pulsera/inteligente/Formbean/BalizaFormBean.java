package org.fcv.pulsera.inteligente.Formbean;

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
import org.fcv.pulsera.inteligente.BaseDatos.Tradicional.TablasDto.Area_DB_Dto;
import org.fcv.pulsera.inteligente.BaseDatos.Tradicional.TablasDto.Baliza_DB_Dto;
import org.fcv.pulsera.inteligente.BaseDatos.Tradicional.TablasDto.Piso_DB_Dto;
import org.fcv.pulsera.inteligente.BaseDatos.Tradicional.TablasDto.Sede_DB_Dto;
import org.fcv.pulsera.inteligente.Dto.Database.UserSession;
import org.fcv.pulsera.inteligente.Util.SessionUtils;
import org.primefaces.event.RowEditEvent;
import org.primefaces.event.SlideEndEvent;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BubbleChartModel;
import org.primefaces.model.chart.BubbleChartSeries;

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
public class BalizaFormBean implements Serializable {

    private Model model;
    private UserSession dataUser;
    private Baliza_DB_Dto baliza_DB_Dto;

    private List<Baliza_DB_Dto> balizaDtos;
    private List<Sede_DB_Dto> sedesInstaladas;
    private List<Piso_DB_Dto> pisosDisponibles;
    private List<Area_DB_Dto> areasDisponibles;
    private Area_DB_Dto areaElegida;
    private List<Baliza_DB_Dto> baliza_DB_Dtos;
    private BubbleChartModel burbujasTodasBalizasInstaladas;

    private boolean botonGuardarDeshabilitado = true;
    private String mostrarCrearBaliza;
    private String mostrarEditarBaliza;

    private final static List<String> opcionesBooleanas;

    static {
        opcionesBooleanas = new ArrayList();
        opcionesBooleanas.add("true");
        opcionesBooleanas.add("false");
    }

    public List<String> getOpcionesBooleanas() {
        return opcionesBooleanas;
    }

    public BalizaFormBean() {
        HttpSession session = SessionUtils.getSession();
        dataUser = (UserSession) session.getAttribute("dataUser");

        baliza_DB_Dto = new Baliza_DB_Dto();
        baliza_DB_Dto.setIdUsuario(dataUser.getIdUsuario());
        balizaDtos = new ArrayList<>();

        try {
            model = new Model();
        } catch (Exception ex) {
            Logger.getLogger(BalizaFormBean.class.getName()).log(Level.SEVERE, null, ex);
        }

        areaElegida = new Area_DB_Dto();

        if (dataUser.getIdRolUser() == 2 || dataUser.getIdRolUser() == 3 || dataUser.getIdRolUser() == 4) {
            mostrarCrearBaliza = Boolean.toString(true);
            mostrarEditarBaliza = Boolean.toString(true);
        } else {
            mostrarCrearBaliza = Boolean.toString(false);
            mostrarEditarBaliza = Boolean.toString(false);
        }

        getListaSedes();
        getListaPisos();
        getListaAreas();
        getListaBalizas();
    }

    private void getListaSedes() {
        if (model != null) {
            sedesInstaladas = model.ProcessSede(new Sede_DB_Dto(), AccionEnum.READ);
            if (sedesInstaladas == null) {
                sedesInstaladas = new ArrayList<>();
            }
        } else {
            sedesInstaladas = new ArrayList<>();
        }
    }

    public List<String> getSedesInstaladas() {
        List<String> listado = new ArrayList<>();
        for (Iterator<Sede_DB_Dto> iterator = sedesInstaladas.iterator(); iterator.hasNext();) {
            Sede_DB_Dto next = iterator.next();
            listado.add(next.getNomSede());
        }
        return listado;
    }

    public void refreshPiso() {
        int idSedeElegida = 0;
        for (Iterator<Sede_DB_Dto> iterator = sedesInstaladas.iterator(); iterator.hasNext();) {
            Sede_DB_Dto next = iterator.next();
            if (next.getNomSede().equals(baliza_DB_Dto.getSede())) {
                idSedeElegida = next.getIdSede();
                break;
            }
        }
        baliza_DB_Dto.setIdSede(idSedeElegida);
        baliza_DB_Dto.setIdArea(0);
        baliza_DB_Dto.setIdPiso(0);
        areaElegida.setIdArea(0);
        areaElegida.setIdSede(0);
        areaElegida.setIdZ(0);
        baliza_DB_Dto.setArea("");
        baliza_DB_Dto.setPiso("");
        baliza_DB_Dto.setSede("");
    }

    private void getListaPisos() {
        if (model != null) {
            pisosDisponibles = model.ProcessPiso(new Piso_DB_Dto(), AccionEnum.READ);
            if (pisosDisponibles == null) {
                pisosDisponibles = new ArrayList<>();
            }
        } else {
            pisosDisponibles = new ArrayList<>();
        }
    }

    public List<String> getPisosInstaladadosPorSede() {
        List<String> listado = new ArrayList<>();
        for (Iterator<Piso_DB_Dto> iterator = pisosDisponibles.iterator(); iterator.hasNext();) {
            Piso_DB_Dto next = iterator.next();
            if (next.getIdSede() == baliza_DB_Dto.getIdSede()) {
                listado.add(next.getPiso());
            }
        }
        return listado;
    }

    public void refreshArea() {
        int idPisoElegida = 0;
        for (Iterator<Piso_DB_Dto> iterator = pisosDisponibles.iterator(); iterator.hasNext();) {
            Piso_DB_Dto next = iterator.next();
            if (next.getPiso().equals(baliza_DB_Dto.getPiso())
                    && next.getIdSede() == baliza_DB_Dto.getIdSede()) {
                idPisoElegida = next.getIdZ();
                break;
            }
        }
        baliza_DB_Dto.setIdPiso(idPisoElegida);
        baliza_DB_Dto.setIdArea(0);
        areaElegida.setIdZ(0);
        areaElegida.setIdArea(0);
        baliza_DB_Dto.setPiso("");
        baliza_DB_Dto.setArea("");
    }

    private void getListaAreas() {
        if (model != null) {
            areasDisponibles = model.ProcessArea(new Area_DB_Dto(), AccionEnum.READ);
            if (areasDisponibles == null) {
                areasDisponibles = new ArrayList<>();
            }
        } else {
            areasDisponibles = new ArrayList<>();
        }
    }

    public List<String> getAreasInstaladadssPorPiso() {
        List<String> listado = new ArrayList<>();
        for (Iterator<Area_DB_Dto> iterator = areasDisponibles.iterator(); iterator.hasNext();) {
            Area_DB_Dto next = iterator.next();
            if (next.getIdZ() == baliza_DB_Dto.getIdPiso()) {
                listado.add(next.getArea());
            }
        }
        return listado;
    }

    public void refreshGraficaElegirUbicacion() {
        for (Iterator<Area_DB_Dto> iterator = areasDisponibles.iterator(); iterator.hasNext();) {
            Area_DB_Dto next = iterator.next();
            if (next.getArea().equals(baliza_DB_Dto.getArea())) {
                areaElegida = next;
                baliza_DB_Dto.setIdArea(areaElegida.getIdArea());
                baliza_DB_Dto.setXplano(areaElegida.getxInicial());
                baliza_DB_Dto.setYplano(areaElegida.getyInicial());
                break;
            }
        }
    }

    private void getListaBalizas() {
        if (model != null) {
            baliza_DB_Dtos = model.ProcessBaliza(new Baliza_DB_Dto(), AccionEnum.READ);
            if (baliza_DB_Dtos == null) {
                baliza_DB_Dtos = new ArrayList<>();
            }
        } else {
            baliza_DB_Dtos = new ArrayList<>();
        }
    }

    public List<Baliza_DB_Dto> getBaliza_DB_Dtos() {
        return baliza_DB_Dtos;
    }

    private BubbleChartSeries CrearPuntoGrafica(String nombrePunto, int x, int y, int radioPuntoCircular) {
        return new BubbleChartSeries(nombrePunto, x, y, radioPuntoCircular * 10);
    }

    private void crearGraficoBusbuja() {
        burbujasTodasBalizasInstaladas = new BubbleChartModel();
        burbujasTodasBalizasInstaladas.setTitle("Balizas instaladas " + areaElegida.getArea());
        burbujasTodasBalizasInstaladas.setShadow(false);
        burbujasTodasBalizasInstaladas.setBubbleGradients(true);
        burbujasTodasBalizasInstaladas.setBubbleAlpha(0.8);
        burbujasTodasBalizasInstaladas.getAxis(AxisType.X).setTickAngle(-50);

        int sizePropagation = 5;
        Area_DB_Dto basic = new Area_DB_Dto();
        if ((basic != areaElegida && areaElegida.getyFinal() != 0 && areaElegida.getxFinal() != 0)) {
            for (Iterator<Baliza_DB_Dto> iterator = baliza_DB_Dtos.iterator(); iterator.hasNext();) {
                Baliza_DB_Dto next = iterator.next();
                BubbleChartSeries puntoGrafica = CrearPuntoGrafica(Integer.toString(next.getIdBaliza()), next.getXplano(), next.getYplano(), sizePropagation);

                burbujasTodasBalizasInstaladas.add(puntoGrafica);
            }

            Axis yAxis = burbujasTodasBalizasInstaladas.getAxis(AxisType.Y);
            yAxis = burbujasTodasBalizasInstaladas.getAxis(AxisType.Y);
            yAxis.setMin(areaElegida.getyFinal());
            yAxis.setMax(areaElegida.getyInicial());
            yAxis.setTickAngle(50);
            yAxis.setLabel("Y");

            Axis xAxis = burbujasTodasBalizasInstaladas.getAxis(AxisType.X);
            xAxis = burbujasTodasBalizasInstaladas.getAxis(AxisType.X);
            xAxis.setMin(areaElegida.getxInicial());
            xAxis.setMax(areaElegida.getxFinal());
            xAxis.setTickAngle(-50);
            xAxis.setLabel("X");
        }
    }

    public void onSlideEndX(SlideEndEvent event) {
        baliza_DB_Dto.setXplano((int) event.getValue());
    }

    public void onSlideEndY(SlideEndEvent event) {
        baliza_DB_Dto.setYplano((int) event.getValue());
    }

    public Area_DB_Dto getAreaElegida() {
        return areaElegida;
    }

    public BubbleChartModel getBubbleByArea() {
        BubbleChartModel burbujasBalizasInstaladasPorArea = new BubbleChartModel();
        burbujasBalizasInstaladasPorArea.setTitle("Balizas instaladas " + areaElegida.getArea());
        burbujasBalizasInstaladasPorArea.setShadow(false);
        burbujasBalizasInstaladasPorArea.setBubbleGradients(true);
        burbujasBalizasInstaladasPorArea.setBubbleAlpha(0.8);
        burbujasBalizasInstaladasPorArea.getAxis(AxisType.X).setTickAngle(-50);

        int sizePropagation = 5;
        Area_DB_Dto basic = new Area_DB_Dto();
        if ((basic != areaElegida && areaElegida.getyFinal() != 0 && areaElegida.getxFinal() != 0)) {
            int idMayor = 0;
            for (Iterator<Baliza_DB_Dto> iterator = baliza_DB_Dtos.iterator(); iterator.hasNext();) {
                Baliza_DB_Dto next = iterator.next();
                BubbleChartSeries puntoGrafica = CrearPuntoGrafica(Integer.toString(next.getIdBaliza()), next.getXplano(), next.getYplano(), sizePropagation);
                if (next.getIdArea() == areaElegida.getIdArea()) {
                    burbujasBalizasInstaladasPorArea.add(puntoGrafica);
                }
                if (idMayor < next.getIdArea()) {
                    idMayor = next.getIdArea();
                }
            }
            burbujasBalizasInstaladasPorArea.add(CrearPuntoGrafica(Integer.toString(idMayor + 1), baliza_DB_Dto.getXplano(), baliza_DB_Dto.getYplano(), sizePropagation));

            Axis yAxis = burbujasBalizasInstaladasPorArea.getAxis(AxisType.Y);
            yAxis = burbujasBalizasInstaladasPorArea.getAxis(AxisType.Y);
            yAxis.setMin(areaElegida.getyFinal());
            yAxis.setMax(areaElegida.getyInicial());
            yAxis.setTickAngle(50);
            yAxis.setLabel("Y");

            Axis xAxis = burbujasBalizasInstaladasPorArea.getAxis(AxisType.X);
            xAxis = burbujasBalizasInstaladasPorArea.getAxis(AxisType.X);
            xAxis.setMin(areaElegida.getxInicial());
            xAxis.setMax(areaElegida.getxFinal());
            xAxis.setTickAngle(-50);
            xAxis.setLabel("X");
        }
        return burbujasBalizasInstaladasPorArea;
    }

    public BubbleChartModel getAllBubbles() {
        crearGraficoBusbuja();
        return burbujasTodasBalizasInstaladas;
    }

    private boolean validarSiExistenDatosDuplicados() {
        Baliza_DB_Dto b_Dto = new Baliza_DB_Dto();

        if (baliza_DB_Dto.getMacBaliza().length() != 17) {
            addMessage("Por favor digite una mac correcta");
            return true;
        }

        if (baliza_DB_Dto.getSede().length() < 3) {
            addMessage("Por favor elija una sede");
            return true;
        }

        if (baliza_DB_Dto.getPiso().length() < 3) {
            addMessage("Por favor elija un piso");
            return true;
        }

        if (baliza_DB_Dto.getArea().length() < 3) {
            addMessage("Por favor elija un area");
            return true;
        }

        if (baliza_DB_Dto.getXplano() < 0) {
            addMessage("Por favor elija una pocisión en X");
            return true;
        }

        if (baliza_DB_Dto.getYplano() < 0) {
            addMessage("Por favor elija una pocisión en Y");
            return true;
        }

        for (Iterator<Baliza_DB_Dto> iterator = baliza_DB_Dtos.iterator(); iterator.hasNext();) {
            Baliza_DB_Dto next = iterator.next();
            if (baliza_DB_Dto.getIdBaliza() != next.getIdBaliza()) {
                if (baliza_DB_Dto.getMacBaliza().equals(next.getMacBaliza())) {
                    addMessage("La dirección MAC ya esta registrada, por favor revice los datos ingresados.");
                    return true;
                }
                if (next.getXplano() == baliza_DB_Dto.getXplano() && baliza_DB_Dto.getYplano() == next.getYplano()) {
                    addMessage("En esa ubicación ya existe una baliza instalada");
                    return true;
                }
            }
        }

        return false;
    }

    public String buttonValidar() {
        boolean validacion = validarSiExistenDatosDuplicados();
        if (!validacion) {
            botonGuardarDeshabilitado = false;
            addMessage("Validacion correcta, para continuar confirme que todos los datos estan correctos y oprima el boton 'GUARDAR'.");
        } else {
            botonGuardarDeshabilitado = true;
        }
        return "";
    }

    public String buttonGuardar() {
        boolean yaExiste = validarSiExistenDatosDuplicados();
        if (!yaExiste) {
            List<Baliza_DB_Dto> processBaliza = model.ProcessBaliza(baliza_DB_Dto, AccionEnum.WRITE);
            if (processBaliza.size() > baliza_DB_Dtos.size()) {
                getListaBalizas();
                addMessage("Baliza registrada correctamente.");
                baliza_DB_Dto = new Baliza_DB_Dto();
            }
        } else {
            addMessage("Hubo un error al insertar la nueva Baliza, por favor verifique los datos e intentelo nuevamente.");
        }
        return "";
    }

    public void addMessage(String summary) {
        FacesMessage message = new FacesMessage("Info", summary);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public void onRowEdit(RowEditEvent event) {
        Baliza_DB_Dto baliza_DB_Dto = (Baliza_DB_Dto) event.getObject();

        boolean yaExiste = false;
        boolean encontreAnterior = false;
        Baliza_DB_Dto datoAnterior = null;
        List<Baliza_DB_Dto> processBaliza = model.ProcessBaliza(new Baliza_DB_Dto(), AccionEnum.READ);
        for (Iterator<Baliza_DB_Dto> iterator = processBaliza.iterator(); iterator.hasNext();) {
            Baliza_DB_Dto next = iterator.next();
            if (next.getIdBaliza() == baliza_DB_Dto.getIdBaliza()) {
                datoAnterior = next;
                encontreAnterior = true;
            } else {
                if (next.getMacBaliza().equals(baliza_DB_Dto.getMacBaliza())) {
                    addMessage("La Mac ya existe.");
                    yaExiste = true;
                }
                if (next.getXplano() == baliza_DB_Dto.getXplano() || baliza_DB_Dto.getYplano() == next.getYplano()) {
                    addMessage("Ya existe una baliza en la misma ubicación.");
                    yaExiste = true;
                }
            }

            if (encontreAnterior && yaExiste) {
                break;
            }
        }

        if (yaExiste) {
            addMessage("La edición no se puede realizar.");
            baliza_DB_Dto.setMacBaliza(datoAnterior.getMacBaliza());
            baliza_DB_Dto.setIndHabilitado(datoAnterior.isIndHabilitado());
            baliza_DB_Dto.setXplano(datoAnterior.getXplano());
            baliza_DB_Dto.setYplano(datoAnterior.getYplano());
        } else {
            List<Baliza_DB_Dto> procesoBaliza = model.ProcessBaliza(baliza_DB_Dto, AccionEnum.UPDATE);
            if (processBaliza != null) {
                if(processBaliza.size()>0){
                    addMessage("Baliza Editada " + Integer.toString(baliza_DB_Dto.getIdBaliza()));
                }else{
                    addMessage("Error al editar la Baliza.");
                }                
            }
        }
    }

    public void onRowCancel(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Edición Cancelada");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public boolean isBotonGuardarDeshabilitado() {
        return botonGuardarDeshabilitado;
    }

    public Baliza_DB_Dto getBaliza_DB_Dto() {
        return baliza_DB_Dto;
    }

    public void setBaliza_DB_Dto(Baliza_DB_Dto baliza_DB_Dto) {
        this.baliza_DB_Dto = baliza_DB_Dto;
    }

    public List<Baliza_DB_Dto> getBalizaDtos() {
        return balizaDtos;
    }

    public String getMostrarCrearBaliza() {
        return mostrarCrearBaliza;
    }

    public void setMostrarCrearBaliza(String mostrarCrearBaliza) {
        this.mostrarCrearBaliza = mostrarCrearBaliza;
    }

    public String getMostrarEditarBaliza() {
        return mostrarEditarBaliza;
    }

    public void setMostrarEditarBaliza(String mostrarEditarBaliza) {
        this.mostrarEditarBaliza = mostrarEditarBaliza;
    }

}
