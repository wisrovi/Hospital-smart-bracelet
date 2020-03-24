/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.fcv.pulsera.inteligente.MQTT;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;



/**
 *
 * @author williamrodriguez
 */
public class Subscriber {

    public String BROKER_URL = null;
    public String GeneralTopic = null;
    private MqttClient client;

    public String getBROKER_URL() {
        return BROKER_URL;
    }

    public void setBROKER_URL(String BROKER_URL) {
        this.BROKER_URL = BROKER_URL;
    }

    public String getGeneralTopic() {
        return GeneralTopic;
    }

    public void setGeneralTopic(String GeneralTopic) {
        this.GeneralTopic = GeneralTopic;
    }    
    
    
    public boolean ConfigureClient(){
        if (BROKER_URL != null) {
            String clientId = "demonio";
            try {
                client = new MqttClient(BROKER_URL, clientId);
                return true;
            } catch (MqttException e) {
                e.printStackTrace();
                //System.exit(1);
                return false;
            }
        }else{
            return false;
        }
    }    
    

    public void start() {
        try {
            client.setCallback(new SubscribeCallback());
            client.connect();
            client.subscribe("/" + GeneralTopic + "/#");
        } catch (MqttException e) {
            e.printStackTrace();
            System.exit(1);
        }

    }

}
