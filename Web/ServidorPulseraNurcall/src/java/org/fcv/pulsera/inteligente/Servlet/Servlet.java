/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fcv.pulsera.inteligente.Servlet;

import org.fcv.pulsera.inteligente.Dto.Manilla.BalizaDto;
import org.fcv.pulsera.inteligente.Dto.Manilla.BalizasQueVieronUnaManillaDto;
import org.fcv.pulsera.inteligente.Dto.Manilla.EddystoneDto;
import org.fcv.pulsera.inteligente.Dto.Manilla.ManillaDto;
import org.fcv.pulsera.inteligente.Dto.Manilla.ReceivedBalizaDto;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import static java.lang.Math.pow;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.fcv.pulsera.inteligente.BaseDatos.Tradicional.Model.Model;
import org.fcv.pulsera.inteligente.BaseDatos.Tradicional.TablasDto.BeaconDto;

/**
 *
 * @author williamrodriguez
 */
@WebServlet(name = "Servlet", urlPatterns = {"/Servlet"})
public class Servlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    HashMap<String, ManillaDto> datosBalizas;
    Gson gson;
    Model model;

    public Servlet() {
        datosBalizas = new HashMap<>();
        gson = new Gson();
        try {
            model = new Model();
        } catch (Exception ex) {
            model = null;
        }
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
//            out.println("<!DOCTYPE html>");
//            out.println("<html>");
//            out.println("<head>");
//            out.println("<title>Servlet Servlet</title>");            
//            out.println("</head>");
//            out.println("<body>");
//            out.println("<h1>Servlet Servlet at " + request.getContextPath() + "</h1>");
//            out.println("</body>");
//            out.println("</html>");
            String parameterVer = request.getParameter("ver");
            if (parameterVer != null) {
                if (parameterVer.equals("1")) {
                    out.println("*******************************************************************\n");
                    out.println(datosBalizas.size() + "\n");
                    out.println("*******************************************************************\n");
                    if (datosBalizas.isEmpty()) {
                        out.println("Hahmap vacio.\n");
                    } else {
                        out.println(datosBalizas + "\n");
                    }

                    out.println("*******************************************************************\n");
                }
            }
            out.println("OK");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        boolean posiblePostESP32 = true;
        Map<String, String[]> parameterMap = request.getParameterMap();
        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            String key = entry.getKey();
            String[] value = entry.getValue();
            System.out.println("key: " + key);
            System.out.println("value: " + value);
            posiblePostESP32 = false;
        }

        if (posiblePostESP32) {
            String datos = "";
            ReceivedBalizaDto datosReceived = null;

            try {
                datos = httpServletRequestToString(((HttpServletRequest) request));
                datosReceived = gson.fromJson(datos, ReceivedBalizaDto.class);
            } catch (Exception ex) {
                Logger.getLogger(Servlet.class.getName()).log(Level.SEVERE, null, ex);
            }

            if (datosReceived != null) {
                String baliza = datosReceived.getBaliza();

                List<BalizaDto> beacon = datosReceived.getBeacon();
                if (beacon.size() > 0) {
                    ProcesarBeacon(baliza, beacon);
                }

                List<EddystoneDto> eddystone = datosReceived.getEddystone();
                if (eddystone.size() > 0) {
                    ProcesarEddystone(baliza, eddystone);
                }
            }

            //System.out.println(datos);
        }
        processRequest(request, response);
    }

    void ProcesarEddystone(String baliza, List<EddystoneDto> eddystone) {

        for (Iterator<EddystoneDto> iterator = eddystone.iterator(); iterator.hasNext();) {
            EddystoneDto next = iterator.next();

            String address = next.getAddress();
            String temp = next.getTemp();
            String time = next.getTime();
            String voltajeBateria = next.getVolt();
            int datosCodificados = Integer.parseInt(next.getCode());
            /*
            1)Leer los umbrales de los sensores
             */
        }
    }

    void ProcesarBeacon(String baliza, List<BalizaDto> beacon) {
        //constante para no usar, en la triangulación, registros muy viejos, solo los actuales con "tiempoSeparacionMuestras" segundos atras
        byte tiempoSeparacionMuestras = 40;
        byte minimaCantidadMuestrasUsarParaTriangulacion = 3;

        for (Iterator<BalizaDto> iterator = beacon.iterator(); iterator.hasNext();) {
            BalizaDto next = iterator.next();
            Date fechaActual = new Date();

            String manillaBeacon = next.getAddress();
            String major = next.getMajor();
            String minor = next.getMinor();
            String rssi = next.getRssi();
            String txPower = next.getTxPower();

            float distanciaEstimada = CalculateAccuracy(Integer.parseInt(rssi), Integer.parseInt(txPower));

            if (datosBalizas == null) {
                datosBalizas = new HashMap<>();
            }

            BalizasQueVieronUnaManillaDto balizasQueVieronUnaManillaDto
                    = new BalizasQueVieronUnaManillaDto(
                            baliza,
                            distanciaEstimada,
                            fechaActual,
                            Integer.parseInt(rssi)
                    );

            if (datosBalizas.containsKey(manillaBeacon)) {
                ManillaDto manillaDto = datosBalizas.get(manillaBeacon);

                List<BalizasQueVieronUnaManillaDto> listaBalizasQueLoHanVisto = manillaDto.getListaBalizasQueLoHanVisto();
                listaBalizasQueLoHanVisto.add(balizasQueVieronUnaManillaDto);

                listaBalizasQueLoHanVisto.sort((o2, o1) -> o1.getFechaVisto().compareTo(o2.getFechaVisto()));

                boolean thereOldData = false;
                if (listaBalizasQueLoHanVisto.size() >= 3) {
                    HashMap<String, BalizasQueVieronUnaManillaDto> balizasEvaluar = new HashMap<>();
                    for (Iterator<BalizasQueVieronUnaManillaDto> iterator1 = listaBalizasQueLoHanVisto.iterator(); iterator1.hasNext();) {
                        BalizasQueVieronUnaManillaDto next1 = iterator1.next();
                        if (!balizasEvaluar.containsKey(next1.getMacBaliza())) {
                            balizasEvaluar.put(next1.getMacBaliza(), next1);
                        }
                    }

                    if (balizasEvaluar.size() >= 2) {
                        ArrayList<String> keys = new ArrayList<>(balizasEvaluar.keySet());
                        Date fechaMayor = balizasEvaluar.get(keys.get(keys.size() - 1)).getFechaVisto();
                        long tiempoMayor = fechaMayor.getTime();
                        for (int i = keys.size() - 2; i >= 0; i--) {
                            BalizasQueVieronUnaManillaDto get = balizasEvaluar.get(keys.get(i));
                            Date fecha = get.getFechaVisto();
                            long tiempo = fecha.getTime();
                            int segundos = Math.abs((int) (tiempoMayor - tiempo) / 1000);
                            if (segundos > tiempoSeparacionMuestras) {
                                balizasEvaluar.remove(keys.get(i));
                                thereOldData = true;
                            }
                            //System.out.println(segundos);
                        }
                    }

                    System.err.println("*************************************");
                    System.err.println("**                                 **");
                    System.err.println("**             Manilla             **");
                    System.err.println("**        " + manillaBeacon + "        **");
                    System.err.println("**                                 **");
                    System.err.println("*************************************");
                    for (Map.Entry<String, BalizasQueVieronUnaManillaDto> entry : balizasEvaluar.entrySet()) {
                        String key = entry.getKey();
                        BalizasQueVieronUnaManillaDto value = entry.getValue();

                        System.err.println("Baliza: " + key);
                        System.err.println("Distancia (metros): " + value.getDistanciaEstimada());
                        System.err.println("Tx: " + txPower);
                        System.err.println("Rssi: " + value.getRssi());
                        System.err.println(value.getFechaVisto());
                        System.err.println("____________________________");
                    }
                    System.err.println("********************************* ");

                    boolean datosListosEnviar = false;
                    if (thereOldData && balizasEvaluar.size() >= minimaCantidadMuestrasUsarParaTriangulacion) {
                        datosListosEnviar = true;
                    } else if (balizasEvaluar.size() > minimaCantidadMuestrasUsarParaTriangulacion) {
                        datosListosEnviar = true;
                    }

                    if (datosListosEnviar) {  //con esto garantizo que tenga registros de varias balizas suficientes, incluso mas de tres
                        System.err.println("Registros que tenia: " + listaBalizasQueLoHanVisto.size());
                        System.err.println("Registros que serán usados para la triangulación: " + balizasEvaluar.size());
                        System.err.println("Tengo las balizas suficientes para hallar la localización, ahora envio datos al servicio para que halle la triangulación.");
                        //hago procesos de triangulacion

                        manillaDto.setListaBalizasQueLoHanVisto(new ArrayList<BalizasQueVieronUnaManillaDto>()); //limpiar lista
                    }
                }
            } else {
                ManillaDto manillaDto = new ManillaDto();

                List<BalizasQueVieronUnaManillaDto> balizasQueVieronUnaManillaDtos = new ArrayList<>();
                balizasQueVieronUnaManillaDtos.add(balizasQueVieronUnaManillaDto);

                manillaDto.setListaBalizasQueLoHanVisto(balizasQueVieronUnaManillaDtos);
                manillaDto.setFechaCreacion(fechaActual);
                manillaDto.setTxPower(Integer.parseInt(txPower));
                manillaDto.setMajor(Integer.parseInt(major));
                manillaDto.setMinor(Integer.parseInt(minor));
                                                               

                if (model != null) {
                    //buscar el beacon de la base de datos con la MAC, esto para acelerar futuros procesos futuros
                    //solo se busca la primera vez cuando la manilla aparece por primera vez en este servlet
                    List<BeaconDto> beaconSearch = model.getBeacon(manillaBeacon);
                    manillaDto.setBeacon(beaconSearch.get(0));                    
                }        
                
                datosBalizas.put(manillaBeacon, manillaDto);
            }
        }
    }

    float CalculateAccuracy(int rssi, int txPower) {
        float a = (float) 0.89976;
        float b = (float) 7.7095;
        float c = (float) 0.111;

        float ratio = (float) (rssi * 1.0 / txPower);
        if (ratio < 1.0) {
            return (float) pow(ratio, 10);
        } else {
            float accuaricy = (float) (a * pow(ratio, b) + c);
            return accuaricy;
        }
    }

    String httpServletRequestToString(HttpServletRequest request) throws Exception {

        ServletInputStream mServletInputStream = request.getInputStream();
        byte[] httpInData = new byte[request.getContentLength()];
        int retVal = -1;
        StringBuilder stringBuilder = new StringBuilder();

        try {
            while ((retVal = mServletInputStream.read(httpInData)) != -1) {
                for (int i = 0; i < retVal; i++) {
                    stringBuilder.append(Character.toString((char) httpInData[i]));
                }
            }
        } catch (Exception e) {
        }

        return stringBuilder.toString();
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
