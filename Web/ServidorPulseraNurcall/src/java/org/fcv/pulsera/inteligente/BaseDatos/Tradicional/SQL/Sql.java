/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fcv.pulsera.inteligente.BaseDatos.Tradicional.SQL;

import java.io.Serializable;

/**
 *
 * @author williamrodriguez
 */
public class Sql implements Serializable{
    public String comandoSqlEjecutar = "<todo comando es valido, si algun comando requiere reemplazar valores lo pongo como '?' y uso la funcion 'ReemplaceEnString'>";
    
    public String comandoLeerIdbeaconSegunMAC = ""
            + "SELECT idBeacon FROM pulseraBeacon "
            + "WHERE macDispositivo = '?'";
    
    public String comandoLeerBeaconSegunMAC = ""
            + "SELECT idBeacon FROM pulseraBeacon "
            + "WHERE macDispositivo = '?'";
    
    public String comandoLeerDatosUsuario = ""
            + "SELECT * FROM pulseraUsuario "
            + "WHERE usuario='?'  "
            + "AND docUsuario ='?'";
    
    public String comandoLeerTodosDatosManillas = "SELECT "
            + "pUM.idManilla, "
            + "pBea.macDispositivo as 'beacon', "
            + "pBea.txPower_1metro as 'txPower', "
            + "pBea.major, pBea.minor , "
            + "pES.macDispositivo as 'eddystone', "
            + "pUmb.minTemp, "
            + "pUmb.maxTemp, "
            + "pUmb.minCardio, "
            + "pUmb.maxCardio , "
            + "pUM.indHabilitado as 'habilitadoManilla' ,"
            + "pUser.usuario ,"
            + "pUM.fechaRegistro "
            + "FROM pulseraUnionManilla pUM "
            + "INNER JOIN pulseraEddystone pES ON pES.idEddystone = pUM.idEddystone "
            + "INNER JOIN pulseraBeacon pBea ON pBea.idBeacon = pUM.idBeacon "
            + "INNER JOIN pulseraPaciente pPacient ON pPacient.idManilla = pUM.idManilla "
            + "INNER JOIN pulseraUmbrales pUmb ON pUmb.idManilla = pUM.idManilla "
            + "INNER JOIN pulseraUsuario pUser ON pUser.idUsuario = pUM.idUsuarioRegistro "
            + "ORDER BY  pUM.fechaRegistro, pPacient.idAtencionPaciente, pUM.indHabilitado, pUM.idUsuarioRegistro ";
    
    public String comandoLeerDatosManilla = "SELECT "
            + "pUM.idManilla, "
            + "pBea.macDispositivo as 'beacon', "
            + "pBea.txPower_1metro as 'txPower', "
            + "pBea.major, pBea.minor , "
            + "pES.macDispositivo as 'eddystone', "
            + "pUmb.minTemp, "
            + "pUmb.maxTemp, "
            + "pUmb.minCardio, "
            + "pUmb.maxCardio , "
            + "pUM.indHabilitado as 'habilitadoManilla' ,"
            + "pUser.usuario ,"
            + "pUM.fechaRegistro "
            + "FROM pulseraUnionManilla pUM "
            + "INNER JOIN pulseraEddystone pES ON pES.idEddystone = pUM.idEddystone "
            + "INNER JOIN pulseraBeacon pBea ON pBea.idBeacon = pUM.idBeacon "
            + "INNER JOIN pulseraPaciente pPacient ON pPacient.idManilla = pUM.idManilla "
            + "INNER JOIN pulseraUmbrales pUmb ON pUmb.idManilla = pUM.idManilla "
            + "INNER JOIN pulseraUsuario pUser ON pUser.idUsuario = pUM.idUsuarioRegistro "
            + "WHERE pUM.idManilla LIKE '?' "
            + "ORDER BY  pUM.fechaRegistro, pPacient.idAtencionPaciente, pUM.indHabilitado, pUM.idUsuarioRegistro ";
    
    public String comandoInsertarNuevoBeacon = "INSERT INTO pulseraBeacon "
            + "(macDispositivo, major, minor, txPower_1metro, idUsuarioRegistro) "
            + "VALUES ('?', '?', '?', '?', '?')";
        
    public String comandoInsertarNuevoEddystone = "INSERT INTO pulseraEddystone "
            + "(macDispositivo, idUsuarioRegistro) "
            + "VALUES ('?', '?')";
        
    public String comandoInsertarNuevaUnionManilla = "INSERT INTO pulseraUnionManilla "
            + "(idBeacon, idEddystone, idUsuarioRegistro) "
            + "VALUES ('?','?','?')";
        
    public String comandoInsertarNuevosUmbrales = "INSERT INTO pulseraUmbrales "
            + "(idManilla, minTemp, maxTemp, minCardio, maxCardio, idUsuarioRegistro) "
            + "VALUES ('?', '?', '?', '?', '?', '?')";
           
    public String comandoInsertarNuevaPulseraPaciente = "INSERT INTO pulseraPaciente "
            + "(idManilla, idUsuarioRegistro) "
            + "VALUES ('?', '?')";
    
    public String comandoInsertarNuevaSede = "INSERT INTO pulseraSede (Sede, idUsuarioRegistro) VALUES ('?', '?')";
    
    public String comandoLeerTodasSedes = "SELECT idSede,  Sede, indHabilitado, idUsuarioRegistro, fechaRegistro FROM pulseraSede";
    
    public String comandoUpdateSede = "UPDATE pulseraSede SET Sede = '?', indHabilitado = '?', idUsuarioRegistro = '?' WHERE idSede = '?'";
    

    public String comandoInsertarNuevoPiso = "INSERT INTO pulseraZ (idSede, piso, idUsuarioRegistro) VALUES ('?', '?', '?')";
    
    public String comandoLeerTodosPisos = ""
            + "SELECT pZ.idZ, pZ.idSede, pZ.piso, pZ.indHabilitado, "
            + "pZ.idUsuarioRegistro , pZ.fechaRegistro, pSede.Sede, pUser.usuario "
            + "FROM pulseraZ pZ "
            + "INNER JOIN pulseraUsuario pUser ON pUser.idUsuario = pZ.idUsuarioRegistro "
            + "INNER JOIN pulseraSede pSede ON pSede.idSede = pZ.idSede  "
            + "ORDER BY pZ.idZ, pSede.idSede, pZ.indHabilitado, pZ.piso";
    
    public String comandoUpdatePiso = "UPDATE pulseraZ SET idSede = '?', piso = '?', "
            + "indHabilitado = '?', idUsuarioRegistro = '?' WHERE idZ = '?'";
    
    
    
    
    
    public String comandoInsertarNuevaArea = "INSERT INTO "
            + "pulseraArea (area, xInicial, xFinal, yInicial, yFinal, idZ, idUsuarioRegistro) "
            + "VALUES ('?', '?', '?', '?', '?', '?', '?')";
    
    public String comandoLeerTodasAreas = "SELECT "
            + "pArea.idArea, pSede.Sede, pZ.piso, pArea.area, "
            + "pArea.xInicial, pArea.xFinal, pArea.yInicial, "
            + "pArea.yFinal, pArea.indHabilitado, pArea.idUsuarioRegistro , "
            + "pArea.fechaRegistro, pSede.idSede, pZ.idZ "
            + "FROM pulseraArea pArea "
            + "INNER JOIN pulseraZ pZ ON pArea.idZ = pZ.idZ "
            + "INNER JOIN pulseraSede pSede ON pSede.idSede = pZ.idSede ";
    
    public String comandoUpdateArea = "UPDATE pulseraArea "
            + " SET "
            + " area='?', "
            + " xInicial='?',"
            + " xFinal='?',"
            + " yInicial='?',"
            + " yFinal='?',"
            + " idZ='?',"
            + " indHabilitado='?' "
            + " WHERE idArea='?'";

    public String comandoVerBalizasInstaladas = "SELECT "
            + "pBaliza.idBaliza, pBaliza.macDispositivo, pBaliza.ipBaliza, "
            + "pSede.Sede as sede, pArea.area, pZ.piso, pInstalacionBaliza.Xplano as xplano, "
            + "pInstalacionBaliza.Yplano as yplano, pBaliza.indHabilitado, pUsuario.usuario, "
            + "pUsuario.idUsuario, pBaliza.fechaRegistro, pZ.idZ, pSede.idSede, pArea.idArea "
            + "FROM pulseraInstalacionBaliza pInstalacionBaliza "
            + "INNER JOIN pulseraBalizas pBaliza ON pBaliza.idBaliza = pInstalacionBaliza.idBaliza "
            + "INNER JOIN pulseraArea pArea ON pArea.idArea = pInstalacionBaliza.idArea "
            + "INNER JOIN pulseraZ pZ ON pArea.idZ = pZ.idZ "
            + "INNER JOIN pulseraSede pSede ON pSede.idSede = pZ.idSede "
            + "INNER JOIN pulseraUsuario pUsuario ON pUsuario.idUsuario = pBaliza.idUsuarioRegistro "
            + "ORDER BY pSede.idSede, pZ.idZ, pArea.idArea, pBaliza.indHabilitado, pArea.idUsuarioRegistro, pArea.fechaRegistro ";
    
    public String comandoUpdateBaliza = "UPDATE pulseraInstalacionBaliza "
            + "SET Xplano='?', Yplano='?', indHabilitado='?' "
            + "WHERE idBaliza = '?'";
    
    public String comandoInsertarNuevaBaliza = "INSERT INTO pulseraBalizas (macDispositivo, idUsuarioRegistro) VALUES ('?', '?')";
    
    public String comandoInsertarInstalacionBaliza = "INSERT INTO pulseraInstalacionBaliza (idBaliza, Xplano, Yplano, idArea, idUsuarioRegistro) VALUES ('?', '?', '?', '?', '?')";
}
