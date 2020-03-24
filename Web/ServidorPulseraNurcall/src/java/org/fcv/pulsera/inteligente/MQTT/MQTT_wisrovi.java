/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fcv.pulsera.inteligente.MQTT;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;




/**
 *
 * @author williamrodriguez
 */
public class MQTT_wisrovi {

    public MQTT_wisrovi() {
        try {
            String generateClientId = MqttClient.generateClientId();

            System.err.println("MQTT");

            //MqttClient client = new MqttClient("tcp://172.16.66.84", "pahomqttpublish1");
            MqttClient client = new MqttClient("tcp://172.30.19.88", "/NURCALL");
            client.connect();
            MqttMessage message = new MqttMessage();
            message.setPayload("A single message".getBytes());
            client.publish("/test", message);
            client.disconnect();
            System.out.println("OK");

        } catch (Exception e) {
            System.err.println("error:" + e);
            System.out.println("no se pudo enviar...");
        }

    }

    

    
}
