/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fcv.pulsera.inteligente.BaseDatos.Tradicional.Model;

import org.fcv.pulsera.inteligente.BaseDatos.Tradicional.SQL.Sql;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.fcv.pulsera.inteligente.BaseDatos.Tradicional.TablasDto.Area_DB_Dto;
import org.fcv.pulsera.inteligente.BaseDatos.Tradicional.TablasDto.Baliza_DB_Dto;
import org.fcv.pulsera.inteligente.BaseDatos.Tradicional.TablasDto.BeaconDto;
import org.fcv.pulsera.inteligente.BaseDatos.Tradicional.TablasDto.Piso_DB_Dto;
import org.fcv.pulsera.inteligente.BaseDatos.Tradicional.TablasDto.Sede_DB_Dto;
import org.fcv.pulsera.inteligente.BaseDatos.Tradicional.TablasDto.UnionManillaDto;
import org.fcv.pulsera.inteligente.Dto.Database.UserSession;

/**
 *
 * @author williamrodriguez
 */
public class Model extends Sql {

    private static String rutaArchivoProperties = "properties/proyectproperties.properties";

    //String InsertarDatos(String comandos);
    //String LeerUnRecuadroEnDataBase(String consulta, String nombreColumna);
    //List<String> LeerUnaListaDataBase(String consulta, String nombreColumna);
    //<editor-fold defaultstate="collapsed" desc="Funciones Básicas conexión SQL"> 
//preparando variables necesarias
    private PreparedStatement s;
    protected PreparedStatement ps;
    protected String bd;
    protected Connection cn;
    protected Statement st;
    protected String sql;
    protected ResultSet rs;
    private String DatoFinales = "";
    private String driverDataBase = "";
    private Sql claseSql = new Sql();
    private boolean sesionAbierta = false;

    public Model() throws Exception {
        String userProperties = UtilModel.getProperties(rutaArchivoProperties, "user");
        String passwordProperties = UtilModel.getProperties(rutaArchivoProperties, "password");
        String driverDataBaseProperties = UtilModel.getProperties(rutaArchivoProperties, "driverDataBase");;

        String sqlserverProperties;
        String applicationNameProperties;
        String databaseNameProperties;

        sqlserverProperties = UtilModel.getProperties(rutaArchivoProperties, "sqlserver_pasteur");
        applicationNameProperties = UtilModel.getProperties(rutaArchivoProperties, "applicationName");
        databaseNameProperties = UtilModel.getProperties(rutaArchivoProperties, "databaseName");

        DatoFinales += "jdbc:sqlserver:"
                + sqlserverProperties
                + ";applicationName="
                + applicationNameProperties
                + ";databaseName="
                + databaseNameProperties
                + ";user="
                + userProperties
                + ";password="
                + passwordProperties;

        driverDataBase = driverDataBaseProperties;
    }

    private void AbrirConexionBaseDatos() {
        try {
            Class.forName(driverDataBase); //inicializo el driver
            cn = DriverManager.getConnection(DatoFinales, new Properties()); //enlazo la tabla
            sesionAbierta = true;
        } catch (ClassNotFoundException | SQLException e) {
            UtilModel.Exception(e);
        }
    }

    private void CerrarConexionBaseDatos() throws SQLException {
        try {
            s.close();
        } catch (Exception e) {
            UtilModel.Exception(e);
        }
    }

    public String InsertarDatos(String comandos) throws Exception {
        String mensajeRetorno = "0";
        AbrirConexionBaseDatos();
        try {
            s = cn.prepareStatement(comandos, Statement.RETURN_GENERATED_KEYS);//guardo los comandos a ejecutar
            int i = s.executeUpdate(); //ejecuto los comandos guardados

            Integer risultato = -1;
            ResultSet rs = s.getGeneratedKeys();
            if (rs.next()) {
                risultato = rs.getInt(1);
                if (risultato == 0) {
                    risultato = 1;
                }
            }

            if (i > 0) {
                //System.out.println("dato insertado o actualizado");
                mensajeRetorno = "" + risultato;
            } else {
                System.out.println("problema en el insertado o actualizado");
            }
            CerrarConexionBaseDatos();
        } catch (SQLException e1) {
            UtilModel.Exception(e1);
        }
        return mensajeRetorno;
    }

    public String LeerUnRecuadroEnDataBase(String consulta, String nombreColumna) {
        String respuesta = "0";
        AbrirConexionBaseDatos();
        try {
            s = cn.prepareStatement(consulta);//guardo los comandos a ejecutar

            String Datorespuesta = "";
            ResultSet resultSet = s.executeQuery(); //ejecuto los comandos guardados
            if (resultSet.next()) {
                do {
                    Datorespuesta = resultSet.getString(nombreColumna);
                } while (resultSet.next());
            }
            respuesta = Datorespuesta;
            CerrarConexionBaseDatos();
        } catch (SQLException e1) {
            respuesta = "Q";
            UtilModel.Exception(e1);
        }
        return respuesta;
    }

    public List<String> LeerUnaListaDataBase(String consulta, String nombreColumna) {
        List<String> lista = new ArrayList<String>();
        AbrirConexionBaseDatos();
        try {
            s = cn.prepareStatement(consulta);//guardo los comandos a ejecutar

            String Datorespuesta = "1";
            ResultSet resultSet = s.executeQuery(); //ejecuto los comandos guardados
            if (resultSet.next()) {
                do {
                    Datorespuesta = resultSet.getString(nombreColumna);
                    lista.add(Datorespuesta);
                } while (resultSet.next());
            }
            CerrarConexionBaseDatos();
        } catch (SQLException e1) {
            UtilModel.Exception(e1);
        }
        return lista;
    }
//</editor-fold>

    public String getIdbeacon(String mac) {
        if (mac != null && !mac.equals("")) {

            String comandoEjecutar = claseSql.comandoLeerIdbeaconSegunMAC;
            comandoEjecutar = UtilModel.ReemplaceEnString(comandoEjecutar, "?", mac);

            String idBeacon = LeerUnRecuadroEnDataBase(comandoEjecutar, "idBeacon");
            return idBeacon;
        } else {
            return null;
        }
    }

    public List<BeaconDto> getBeacon(String mac) {
        List<BeaconDto> tablaLeida = null;
        if (mac != null && !mac.equals("")) {
            AbrirConexionBaseDatos();
            try {
                String comandoEjecutar = claseSql.comandoLeerBeaconSegunMAC;
                comandoEjecutar = UtilModel.ReemplaceEnString(comandoEjecutar, "?", mac);

                if (sesionAbierta) {
                    s = cn.prepareStatement(comandoEjecutar);//guardo los comandos a ejecutar
                    ResultSet resultSet = s.executeQuery(); //ejecuto los comandos guardados
                    if (resultSet.next()) {
                        tablaLeida = new ArrayList<>();
                        do {
                            BeaconDto auxiliar = new BeaconDto();

                            auxiliar.setIdBeacon(resultSet.getInt("idBeacon"));
                            auxiliar.setMacDispositivo(resultSet.getString("macDispositivo"));
                            auxiliar.setMajor(resultSet.getInt("major"));
                            auxiliar.setMinor(resultSet.getInt("minor"));
                            auxiliar.setTxPower(resultSet.getInt("txPower"));
                            auxiliar.setIndHabilitado(resultSet.getBoolean("indHabilitado"));
                            auxiliar.setIdUsuarioRegistro(resultSet.getInt("idUsuarioRegistro"));

                            if (resultSet.getTimestamp("fechaRegistro") != null) {
                                auxiliar.setFechaRegistro(new Date(resultSet.getTimestamp("fechaRegistro").getTime()));
                            }
                            tablaLeida.add(auxiliar);
                        } while (resultSet.next());
                    }
                    CerrarConexionBaseDatos();
                }
            } catch (Exception e) {
                UtilModel.Exception(e);
            }
        }
        return tablaLeida;
    }

    public List<UserSession> getDataUser(String user, String document) {
        List<UserSession> tablaLeida = null;
        if (user != null && !user.equals("") && document != null && !document.equals("")) {
            AbrirConexionBaseDatos();
            try {
                String comandoEjecutar = claseSql.comandoLeerDatosUsuario;
                comandoEjecutar = UtilModel.ReemplaceEnString(comandoEjecutar, "?", user);
                comandoEjecutar = UtilModel.ReemplaceEnString(comandoEjecutar, "?", document);

                if (sesionAbierta) {
                    s = cn.prepareStatement(comandoEjecutar);//guardo los comandos a ejecutar
                    ResultSet resultSet = s.executeQuery(); //ejecuto los comandos guardados
                    if (resultSet.next()) {
                        tablaLeida = new ArrayList<>();
                        do {
                            UserSession auxiliar = new UserSession();

                            auxiliar.setIdUsuario(resultSet.getInt("idUsuario"));
                            auxiliar.setUsuario(resultSet.getString("usuario"));
                            auxiliar.setIdRolUser(resultSet.getInt("idRolUsuario"));
                            auxiliar.setDocUsuario(resultSet.getString("docUsuario"));

                            if (resultSet.getTimestamp("fechaRegistro") != null) {
                                auxiliar.setFechaRegistro(new Date(resultSet.getTimestamp("fechaRegistro").getTime()));
                            }
                            auxiliar.setIdUsuarioRegistro(resultSet.getInt("idUsuarioRegistro"));
                            auxiliar.setIdSede(resultSet.getInt("idSede"));
                            auxiliar.setIndHabilitado(resultSet.getBoolean("indHabilitado"));
                            tablaLeida.add(auxiliar);
                        } while (resultSet.next());
                    }
                    CerrarConexionBaseDatos();
                }

            } catch (Exception e) {
                UtilModel.Exception(e);
            }
        }
        return tablaLeida;
    }

    public List<UnionManillaDto> getListManillas(String idManilla) {
        List<UnionManillaDto> tablaLeida = null;
        if (idManilla != null) {
            AbrirConexionBaseDatos();
            try {
                String comandoEjecutar = null;
                if (idManilla.equals("")) {
                    comandoEjecutar = claseSql.comandoLeerTodosDatosManillas;
                } else {
                    comandoEjecutar = claseSql.comandoLeerDatosManilla;
                    comandoEjecutar = UtilModel.ReemplaceEnString(comandoEjecutar, "?", idManilla);
                }

                if (sesionAbierta) {
                    s = cn.prepareStatement(comandoEjecutar);//guardo los comandos a ejecutar
                    ResultSet resultSet = s.executeQuery(); //ejecuto los comandos guardados
                    if (resultSet.next()) {
                        tablaLeida = new ArrayList<>();
                        do {
                            UnionManillaDto auxiliar = new UnionManillaDto();

                            auxiliar.setIdManilla(resultSet.getInt("idManilla"));
                            auxiliar.setBeacon(resultSet.getString("beacon"));
                            auxiliar.setTxPower(resultSet.getInt("txPower"));
                            auxiliar.setMajor(resultSet.getInt("major"));
                            auxiliar.setMinor(resultSet.getInt("minor"));
                            auxiliar.setEddystone(resultSet.getString("eddystone"));
                            auxiliar.setMinTemp(resultSet.getInt("minTemp"));
                            auxiliar.setMaxTemp(resultSet.getInt("maxTemp"));
                            auxiliar.setMinCardio(resultSet.getInt("minCardio"));
                            auxiliar.setMaxCardio(resultSet.getInt("maxCardio"));
                            auxiliar.setHabilitadoManilla(resultSet.getBoolean("habilitadoManilla"));
                            auxiliar.setUsuario(resultSet.getString("usuario"));

                            if (resultSet.getTimestamp("fechaRegistro") != null) {
                                auxiliar.setFechaRegistro(new Date(resultSet.getTimestamp("fechaRegistro").getTime()));
                            }

                            /*auxiliar.setIdDato(resultSet.getInt("idDato"));
                        auxiliar.setDocumento(resultSet.getString("documento"));
                        auxiliar.setFechaRegistro(new Date(resultSet.getTimestamp("fechaRegistro").getTime()));
                        if (resultSet.getTimestamp("fechaModificacion") != null) {
                            auxiliar.setFechaModificacion(new Date(resultSet.getTimestamp("fechaModificacion").getTime()));
                        }                        
                        auxiliar.setHabilitado(resultSet.getBoolean("habilitado"));*/
                            tablaLeida.add(auxiliar);
                        } while (resultSet.next());
                    }
                    CerrarConexionBaseDatos();
                }

            } catch (Exception e) {
                UtilModel.Exception(e);
            }
        }
        return tablaLeida;
    }

    public int InsertarNuevaManilla(UnionManillaDto datosCrearManilla) {
        int datoRetornar = 0;
        if (datosCrearManilla != null) {
            int rtaInsertBeacon = 0;
            int rtaInsertEddystone = 0;
            int rtaInsertUnionManilla = 0;

            String rtaInsert = null;
            String comandoEjecutar = null;

            {   //insertar nuevo Beacon
                comandoEjecutar = claseSql.comandoInsertarNuevoBeacon;
                comandoEjecutar = UtilModel.ReemplaceEnString(comandoEjecutar, "?", datosCrearManilla.getBeacon());
                comandoEjecutar = UtilModel.ReemplaceEnString(comandoEjecutar, "?", Integer.toString(datosCrearManilla.getMajor()));
                comandoEjecutar = UtilModel.ReemplaceEnString(comandoEjecutar, "?", Integer.toString(datosCrearManilla.getMinor()));
                comandoEjecutar = UtilModel.ReemplaceEnString(comandoEjecutar, "?", Integer.toString(datosCrearManilla.getTxPower()));
                comandoEjecutar = UtilModel.ReemplaceEnString(comandoEjecutar, "?", datosCrearManilla.getUsuario());

                try {
                    rtaInsert = InsertarDatos(comandoEjecutar);
                } catch (Exception ex) {
                    Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            if (rtaInsert != null) {
                rtaInsertBeacon = Integer.parseInt(rtaInsert);
                if (rtaInsertBeacon > 0) {
                    {   //insertar nuevo Eddystone
                        comandoEjecutar = claseSql.comandoInsertarNuevoEddystone;
                        comandoEjecutar = UtilModel.ReemplaceEnString(comandoEjecutar, "?", datosCrearManilla.getEddystone());
                        comandoEjecutar = UtilModel.ReemplaceEnString(comandoEjecutar, "?", datosCrearManilla.getUsuario());
                        try {
                            rtaInsert = InsertarDatos(comandoEjecutar);
                        } catch (Exception ex) {
                            Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }

                    if (rtaInsert != null) {
                        rtaInsertEddystone = Integer.parseInt(rtaInsert);
                        if (rtaInsertEddystone > 0) {
                            {   //insertar nueva union manilla
                                comandoEjecutar = claseSql.comandoInsertarNuevaUnionManilla;
                                comandoEjecutar = UtilModel.ReemplaceEnString(comandoEjecutar, "?", Integer.toString(rtaInsertBeacon));
                                comandoEjecutar = UtilModel.ReemplaceEnString(comandoEjecutar, "?", Integer.toString(rtaInsertEddystone));
                                comandoEjecutar = UtilModel.ReemplaceEnString(comandoEjecutar, "?", datosCrearManilla.getUsuario());
                                try {
                                    rtaInsert = InsertarDatos(comandoEjecutar);
                                } catch (Exception ex) {
                                    Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                            if (rtaInsert != null) {
                                rtaInsertUnionManilla = Integer.parseInt(rtaInsert);
                                {   //inertar nuevos umbrales manilla
                                    comandoEjecutar = claseSql.comandoInsertarNuevosUmbrales;
                                    comandoEjecutar = UtilModel.ReemplaceEnString(comandoEjecutar, "?", Integer.toString(rtaInsertUnionManilla));
                                    comandoEjecutar = UtilModel.ReemplaceEnString(comandoEjecutar, "?", Integer.toString(datosCrearManilla.getMinTemp()));
                                    comandoEjecutar = UtilModel.ReemplaceEnString(comandoEjecutar, "?", Integer.toString(datosCrearManilla.getMaxTemp()));
                                    comandoEjecutar = UtilModel.ReemplaceEnString(comandoEjecutar, "?", Integer.toString(datosCrearManilla.getMinCardio()));
                                    comandoEjecutar = UtilModel.ReemplaceEnString(comandoEjecutar, "?", Integer.toString(datosCrearManilla.getMaxCardio()));
                                    comandoEjecutar = UtilModel.ReemplaceEnString(comandoEjecutar, "?", datosCrearManilla.getUsuario());
                                    try {
                                        rtaInsert = InsertarDatos(comandoEjecutar);
                                    } catch (Exception ex) {
                                        Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }
                                if (rtaInsert != null) {
                                    {   //inserto nueva pulsera paciente
                                        comandoEjecutar = claseSql.comandoInsertarNuevaPulseraPaciente;
                                        comandoEjecutar = UtilModel.ReemplaceEnString(comandoEjecutar, "?", Integer.toString(rtaInsertUnionManilla));
                                        comandoEjecutar = UtilModel.ReemplaceEnString(comandoEjecutar, "?", datosCrearManilla.getUsuario());
                                        try {
                                            rtaInsert = InsertarDatos(comandoEjecutar);
                                        } catch (Exception ex) {
                                            Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                    }
                                    if (rtaInsert != null) {
                                        datoRetornar = rtaInsertUnionManilla;  //para retornar el id manilla registrada
                                    }
                                }
                            }
                        }
                    }
                }

            }
        }
        return datoRetornar;
    }

    private List<Sede_DB_Dto> getListSedes() {
        List<Sede_DB_Dto> tablaLeida = new ArrayList<>();
        AbrirConexionBaseDatos();

        if (sesionAbierta) {
            try {
                String comandoEjecutar = null;
                comandoEjecutar = claseSql.comandoLeerTodasSedes;
                s = cn.prepareStatement(comandoEjecutar);//guardo los comandos a ejecutar
                ResultSet resultSet = s.executeQuery(); //ejecuto los comandos guardados
                if (resultSet.next()) {
                    tablaLeida = new ArrayList<>();
                    do {
                        Sede_DB_Dto auxiliar = new Sede_DB_Dto();

                        auxiliar.setIdSede(resultSet.getInt("idSede"));
                        auxiliar.setNomSede(resultSet.getString("Sede"));
                        auxiliar.setIndHabilitado(resultSet.getBoolean("indHabilitado"));
                        auxiliar.setUsuario(Integer.toString(resultSet.getInt("idUsuarioRegistro")));
                        if (resultSet.getTimestamp("fechaRegistro") != null) {
                            auxiliar.setFechaRegistro(new Date(resultSet.getTimestamp("fechaRegistro").getTime()));
                        }
                        tablaLeida.add(auxiliar);
                    } while (resultSet.next());
                }
                CerrarConexionBaseDatos();
            } catch (SQLException e) {
                UtilModel.Exception(e);
            }
        }
        return tablaLeida;
    }

    public List<Sede_DB_Dto> ProcessSede(Sede_DB_Dto sede_DB_Dto, AccionEnum accionEnum) {
        List<Sede_DB_Dto> tablaLeida = null;
        if (sede_DB_Dto != null) {
            try {
                String comandoEjecutar = null;
                switch (accionEnum) {
                    case READ: {
                        tablaLeida = getListSedes();
                    }
                    break;
                    case UPDATE: {
                        comandoEjecutar = claseSql.comandoUpdateSede;
                        comandoEjecutar = UtilModel.ReemplaceEnString(comandoEjecutar, "?", sede_DB_Dto.getNomSede());
                        comandoEjecutar = UtilModel.ReemplaceEnString(comandoEjecutar, "?", Boolean.toString(sede_DB_Dto.isIndHabilitado()));
                        comandoEjecutar = UtilModel.ReemplaceEnString(comandoEjecutar, "?", sede_DB_Dto.getUsuario());
                        comandoEjecutar = UtilModel.ReemplaceEnString(comandoEjecutar, "?", Integer.toString(sede_DB_Dto.getIdSede()));
                        String InsertarDatos = InsertarDatos(comandoEjecutar);
                        if (!InsertarDatos.equals("0")) {
                            tablaLeida = getListSedes();
                        }
                    }
                    break;
                    case WRITE: {
                        comandoEjecutar = claseSql.comandoInsertarNuevaSede;
                        comandoEjecutar = UtilModel.ReemplaceEnString(comandoEjecutar, "?", sede_DB_Dto.getNomSede());
                        comandoEjecutar = UtilModel.ReemplaceEnString(comandoEjecutar, "?", sede_DB_Dto.getUsuario());
                        String InsertarDatos = InsertarDatos(comandoEjecutar);
                        tablaLeida = getListSedes();
                    }
                    break;
                }
            } catch (Exception e) {
                UtilModel.Exception(e);
            }
        }
        return tablaLeida;
    }

    private List<Piso_DB_Dto> getListPisos() {
        List<Piso_DB_Dto> tablaLeida = new ArrayList<>();
        AbrirConexionBaseDatos();

        if (sesionAbierta) {
            try {
                String comandoEjecutar = null;
                comandoEjecutar = claseSql.comandoLeerTodosPisos;
                s = cn.prepareStatement(comandoEjecutar);//guardo los comandos a ejecutar
                ResultSet resultSet = s.executeQuery(); //ejecuto los comandos guardados
                if (resultSet.next()) {
                    tablaLeida = new ArrayList<>();
                    do {
                        Piso_DB_Dto auxiliar = new Piso_DB_Dto();

                        auxiliar.setIdZ(resultSet.getInt("idZ"));
                        auxiliar.setIdSede(resultSet.getInt("idSede"));
                        auxiliar.setPiso(resultSet.getString("piso"));
                        auxiliar.setIndHabilitado(resultSet.getBoolean("indHabilitado"));
                        auxiliar.setIdUsuarioRegistro(resultSet.getInt("idUsuarioRegistro"));
                        if (resultSet.getTimestamp("fechaRegistro") != null) {
                            auxiliar.setFecharegistro(new Date(resultSet.getTimestamp("fechaRegistro").getTime()));
                        }
                        auxiliar.setSede(resultSet.getString("Sede"));
                        auxiliar.setUsuario(resultSet.getString("usuario"));
                        tablaLeida.add(auxiliar);
                    } while (resultSet.next());
                }
                CerrarConexionBaseDatos();
            } catch (SQLException e) {
                UtilModel.Exception(e);
            }
        }
        return tablaLeida;
    }

    public List<Piso_DB_Dto> ProcessPiso(Piso_DB_Dto piso_DB_Dto, AccionEnum accionEnum) {
        List<Piso_DB_Dto> tablaLeida = null;
        if (piso_DB_Dto != null) {
            try {
                String comandoEjecutar = null;
                switch (accionEnum) {
                    case READ: {
                        tablaLeida = getListPisos();
                    }
                    break;
                    case UPDATE: {
                        comandoEjecutar = claseSql.comandoUpdatePiso;
                        comandoEjecutar = UtilModel.ReemplaceEnString(comandoEjecutar, "?", Integer.toString(piso_DB_Dto.getIdSede()));
                        comandoEjecutar = UtilModel.ReemplaceEnString(comandoEjecutar, "?", piso_DB_Dto.getPiso());
                        comandoEjecutar = UtilModel.ReemplaceEnString(comandoEjecutar, "?", Boolean.toString(piso_DB_Dto.isIndHabilitado()));
                        comandoEjecutar = UtilModel.ReemplaceEnString(comandoEjecutar, "?", Integer.toString(piso_DB_Dto.getIdUsuarioRegistro()));
                        comandoEjecutar = UtilModel.ReemplaceEnString(comandoEjecutar, "?", Integer.toString(piso_DB_Dto.getIdZ()));
                        String InsertarDatos = InsertarDatos(comandoEjecutar);
                        if (!InsertarDatos.equals("0")) {
                            tablaLeida = getListPisos();
                        } else {
                            tablaLeida = null;
                        }
                    }
                    break;
                    case WRITE: {
                        comandoEjecutar = claseSql.comandoInsertarNuevoPiso;
                        comandoEjecutar = UtilModel.ReemplaceEnString(comandoEjecutar, "?", Integer.toString(piso_DB_Dto.getIdSede()));
                        comandoEjecutar = UtilModel.ReemplaceEnString(comandoEjecutar, "?", piso_DB_Dto.getPiso());
                        comandoEjecutar = UtilModel.ReemplaceEnString(comandoEjecutar, "?", Integer.toString(piso_DB_Dto.getIdUsuarioRegistro()));
                        String InsertarDatos = InsertarDatos(comandoEjecutar);
                        if (!InsertarDatos.equals("0")) {
                            tablaLeida = getListPisos();
                        }
                    }
                    break;
                }
            } catch (Exception e) {
                UtilModel.Exception(e);
            }
        }
        if (tablaLeida == null) {
            tablaLeida = new ArrayList<>();
        }
        return tablaLeida;
    }

    private List<Area_DB_Dto> getListAreas() {
        List<Area_DB_Dto> tablaLeida = new ArrayList<>();
        AbrirConexionBaseDatos();

        if (sesionAbierta) {
            try {
                String comandoEjecutar = null;
                comandoEjecutar = claseSql.comandoLeerTodasAreas;
                s = cn.prepareStatement(comandoEjecutar);//guardo los comandos a ejecutar
                ResultSet resultSet = s.executeQuery(); //ejecuto los comandos guardados
                if (resultSet.next()) {
                    tablaLeida = new ArrayList<>();
                    do {
                        Area_DB_Dto auxiliar = new Area_DB_Dto();

                        auxiliar.setIdArea(resultSet.getInt("idArea"));
                        auxiliar.setSede(resultSet.getString("Sede"));
                        auxiliar.setPiso(resultSet.getString("piso"));
                        auxiliar.setArea(resultSet.getString("area"));
                        auxiliar.setxInicial(resultSet.getInt("xInicial"));
                        auxiliar.setxFinal(resultSet.getInt("xFinal"));
                        auxiliar.setyInicial(resultSet.getInt("yInicial"));
                        auxiliar.setyFinal(resultSet.getInt("yFinal"));
                        auxiliar.setIndHabilitado(resultSet.getBoolean("indHabilitado"));
                        auxiliar.setIdUsuarioRegistro(resultSet.getInt("idUsuarioRegistro"));
                        if (resultSet.getTimestamp("fechaRegistro") != null) {
                            auxiliar.setFechaRegistro(new Date(resultSet.getTimestamp("fechaRegistro").getTime()));
                        }
                        auxiliar.setIdSede(resultSet.getInt("idSede"));
                        auxiliar.setIdZ(resultSet.getInt("idZ"));

                        tablaLeida.add(auxiliar);
                    } while (resultSet.next());
                }
                CerrarConexionBaseDatos();
            } catch (SQLException e) {
                UtilModel.Exception(e);
            }
        }
        return tablaLeida;
    }

    public List<Area_DB_Dto> ProcessArea(Area_DB_Dto area_DB_Dto, AccionEnum accionEnum) {
        List<Area_DB_Dto> tablaLeida = null;
        if (area_DB_Dto != null) {
            try {
                String comandoEjecutar = null;
                switch (accionEnum) {
                    case READ: {
                        tablaLeida = getListAreas();
                    }
                    break;
                    case UPDATE: {
                        comandoEjecutar = claseSql.comandoUpdateArea;
                        comandoEjecutar = UtilModel.ReemplaceEnString(comandoEjecutar, "?", area_DB_Dto.getArea());
                        comandoEjecutar = UtilModel.ReemplaceEnString(comandoEjecutar, "?", Integer.toString(area_DB_Dto.getxInicial()));
                        comandoEjecutar = UtilModel.ReemplaceEnString(comandoEjecutar, "?", Integer.toString(area_DB_Dto.getxFinal()));
                        comandoEjecutar = UtilModel.ReemplaceEnString(comandoEjecutar, "?", Integer.toString(area_DB_Dto.getyInicial()));
                        comandoEjecutar = UtilModel.ReemplaceEnString(comandoEjecutar, "?", Integer.toString(area_DB_Dto.getyFinal()));
                        comandoEjecutar = UtilModel.ReemplaceEnString(comandoEjecutar, "?", Integer.toString(area_DB_Dto.getIdZ()));
                        comandoEjecutar = UtilModel.ReemplaceEnString(comandoEjecutar, "?", Boolean.toString(area_DB_Dto.isIndHabilitado()));
                        comandoEjecutar = UtilModel.ReemplaceEnString(comandoEjecutar, "?", Integer.toString(area_DB_Dto.getIdArea()));
                        String InsertarDatos = InsertarDatos(comandoEjecutar);
                        if (!InsertarDatos.equals("0")) {
                            tablaLeida = getListAreas();
                        } else {
                            tablaLeida = null;
                        }
                    }
                    break;
                    case WRITE: {
                        comandoEjecutar = claseSql.comandoInsertarNuevaArea;
                        comandoEjecutar = UtilModel.ReemplaceEnString(comandoEjecutar, "?", area_DB_Dto.getArea());
                        comandoEjecutar = UtilModel.ReemplaceEnString(comandoEjecutar, "?", Integer.toString(area_DB_Dto.getxInicial()));
                        comandoEjecutar = UtilModel.ReemplaceEnString(comandoEjecutar, "?", Integer.toString(area_DB_Dto.getxFinal()));
                        comandoEjecutar = UtilModel.ReemplaceEnString(comandoEjecutar, "?", Integer.toString(area_DB_Dto.getyInicial()));
                        comandoEjecutar = UtilModel.ReemplaceEnString(comandoEjecutar, "?", Integer.toString(area_DB_Dto.getyFinal()));
                        comandoEjecutar = UtilModel.ReemplaceEnString(comandoEjecutar, "?", Integer.toString(area_DB_Dto.getIdZ()));
                        comandoEjecutar = UtilModel.ReemplaceEnString(comandoEjecutar, "?", Integer.toString(area_DB_Dto.getIdUsuarioRegistro()));
                        String InsertarDatos = InsertarDatos(comandoEjecutar);
                        if (!InsertarDatos.equals("0")) {
                            tablaLeida = getListAreas();
                        } else {
                            tablaLeida = null;
                        }
                    }
                    break;
                }
            } catch (Exception e) {
                UtilModel.Exception(e);
            }
        }
        if (tablaLeida == null) {
            tablaLeida = new ArrayList<>();
        }
        return tablaLeida;
    }

    private List<Baliza_DB_Dto> getListBalizas() {
        List<Baliza_DB_Dto> tablaLeida = new ArrayList<>();
        AbrirConexionBaseDatos();

        if (sesionAbierta) {
            try {
                String comandoEjecutar = null;
                comandoEjecutar = claseSql.comandoVerBalizasInstaladas;
                s = cn.prepareStatement(comandoEjecutar);//guardo los comandos a ejecutar
                ResultSet resultSet = s.executeQuery(); //ejecuto los comandos guardados
                if (resultSet.next()) {
                    tablaLeida = new ArrayList<>();
                    do {
                        Baliza_DB_Dto auxiliar = new Baliza_DB_Dto();

                        auxiliar.setIdBaliza(resultSet.getInt("idBaliza"));
                        auxiliar.setMacBaliza(resultSet.getString("macDispositivo"));
                        auxiliar.setIpBaliza(resultSet.getString("ipBaliza"));
                        auxiliar.setSede(resultSet.getString("sede"));
                        auxiliar.setArea(resultSet.getString("area"));
                        auxiliar.setPiso(resultSet.getString("piso"));
                        auxiliar.setXplano(resultSet.getInt("xplano"));
                        auxiliar.setYplano(resultSet.getInt("yplano"));
                        auxiliar.setIndHabilitado(resultSet.getBoolean("indHabilitado"));
                        auxiliar.setUsuario(resultSet.getString("usuario"));
                        auxiliar.setIdUsuario(resultSet.getInt("idUsuario"));
                        if (resultSet.getTimestamp("fechaRegistro") != null) {
                            auxiliar.setFechaRegistro(new Date(resultSet.getTimestamp("fechaRegistro").getTime()));
                        }
                        auxiliar.setIdPiso(resultSet.getInt("idZ"));
                        auxiliar.setIdSede(resultSet.getInt("idSede"));
                        auxiliar.setIdArea(resultSet.getInt("idArea"));

                        tablaLeida.add(auxiliar);
                    } while (resultSet.next());
                }
                CerrarConexionBaseDatos();
            } catch (SQLException e) {
                UtilModel.Exception(e);
            }
        }
        return tablaLeida;
    }

    public List<Baliza_DB_Dto> ProcessBaliza(Baliza_DB_Dto baliza_DB_Dto, AccionEnum accionEnum) {
        List<Baliza_DB_Dto> tablaLeida = null;
        if (baliza_DB_Dto != null) {
            try {
                String comandoEjecutar = null;
                switch (accionEnum) {
                    case READ: {
                        tablaLeida = getListBalizas();
                    }
                    break;
                    case UPDATE: {
                        comandoEjecutar = claseSql.comandoUpdateBaliza;
                        comandoEjecutar = UtilModel.ReemplaceEnString(comandoEjecutar, "?", Integer.toString(baliza_DB_Dto.getXplano()));
                        comandoEjecutar = UtilModel.ReemplaceEnString(comandoEjecutar, "?", Integer.toString(baliza_DB_Dto.getYplano()));
                        comandoEjecutar = UtilModel.ReemplaceEnString(comandoEjecutar, "?", Boolean.toString(baliza_DB_Dto.isIndHabilitado()));
                        comandoEjecutar = UtilModel.ReemplaceEnString(comandoEjecutar, "?", Integer.toString(baliza_DB_Dto.getIdBaliza()));
                        String InsertarDatos = InsertarDatos(comandoEjecutar);
                        if (!InsertarDatos.equals("0")) {
                            tablaLeida = getListBalizas();
                        } else {
                            tablaLeida = null;
                        }
                    }
                    break;
                    case WRITE: {
                        comandoEjecutar = claseSql.comandoInsertarNuevaBaliza;
                        comandoEjecutar = UtilModel.ReemplaceEnString(comandoEjecutar, "?", baliza_DB_Dto.getMacBaliza());
                        comandoEjecutar = UtilModel.ReemplaceEnString(comandoEjecutar, "?", Integer.toString(baliza_DB_Dto.getIdUsuario()));
                        String InsertarDatos = InsertarDatos(comandoEjecutar);
                        if (!InsertarDatos.equals("0")) {
                            comandoEjecutar = claseSql.comandoInsertarInstalacionBaliza;
                            comandoEjecutar = UtilModel.ReemplaceEnString(comandoEjecutar, "?", InsertarDatos);
                            comandoEjecutar = UtilModel.ReemplaceEnString(comandoEjecutar, "?", Integer.toString(baliza_DB_Dto.getXplano()));
                            comandoEjecutar = UtilModel.ReemplaceEnString(comandoEjecutar, "?", Integer.toString(baliza_DB_Dto.getYplano()));
                            comandoEjecutar = UtilModel.ReemplaceEnString(comandoEjecutar, "?", Integer.toString(baliza_DB_Dto.getIdArea()));
                            comandoEjecutar = UtilModel.ReemplaceEnString(comandoEjecutar, "?", Integer.toString(baliza_DB_Dto.getIdUsuario()));
                            InsertarDatos = InsertarDatos(comandoEjecutar);
                            if (!InsertarDatos.equals("0")) {
                                tablaLeida = getListBalizas();
                            }
                        } else {
                            tablaLeida = null;
                        }
                    }
                    break;
                }
            } catch (Exception e) {
                UtilModel.Exception(e);
            }
        }
        if (tablaLeida == null) {
            tablaLeida = new ArrayList<>();
        }
        return tablaLeida;
    }

    /*auxiliar.setIdDato(resultSet.getInt("idDato"));
    auxiliar.setDocumento(resultSet.getString("documento"));
    auxiliar.setFechaRegistro(new Date(resultSet.getTimestamp("fechaRegistro").getTime()));
    if (resultSet.getTimestamp("fechaModificacion") != null) {
        auxiliar.setFechaModificacion(new Date(resultSet.getTimestamp("fechaModificacion").getTime()));
    }                        
    auxiliar.setHabilitado(resultSet.getBoolean("habilitado"));*/
}
