package com.home.app.configurations;

import org.eclipse.paho.client.mqttv3.IMqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.internal.wire.MqttConnect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.ClientManager;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.core.Mqttv5ClientManager;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.home.app.AppApplication;
import com.home.app.model.Light;
import com.home.app.model.Lights;

@Configuration
public class MqttBeans {
    
    private static final Logger logger = LoggerFactory.getLogger(AppApplication.class);
    private File LightsData = new File("C:/Users/Kleis/Dev/Home-Deck/Lights.json");

    @Bean
    MqttPahoClientFactory mqttClientFactory() {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        MqttConnectOptions options = new MqttConnectOptions();

        options.setServerURIs(new String[] { "tcp://localhost:1833" });
        options.setUserName("haas");
        String pass = "test";
        options.setPassword(pass.toCharArray());
        options.setCleanSession(true);

        factory.setConnectionOptions(options);

        return factory;

    }

    @Bean
    MessageChannel mqttInputChannel() {
        return new DirectChannel();
    }

    @Bean
    MessageProducer inbound() {
        MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter("serverIn",
                mqttClientFactory(), "#");

        adapter.setCompletionTimeout(5000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(2);
        adapter.setOutputChannel(mqttInputChannel());
        return adapter;
    }

    @Bean
    @ServiceActivator(inputChannel = "mqttInputChannel")
    MessageHandler handler() {
        return new MessageHandler() {

            @Override
            public void handleMessage(Message<?> message) throws MessagingException {
                String topic = message.getHeaders().get(MqttHeaders.RECEIVED_TOPIC).toString();
                if (topic.equals("myTopic")) {
                    System.out.println("This is the topic");
                }
                System.out.println(message.getHeaders());
                System.out.println(message.getPayload());
                String clientID = message.getPayload().toString();

                if (topic.equals("device/register")) {
                    logger.info("REGISTRANDO CLIENTE " + clientID);
                    LightsData = new File("C:/Users/Kleis/Dev/Home-Deck/Lights.json");

                    Lights lights = new Lights();
                    Light lght = new Light();

                    try {
                        byte[] jsonData = Files.readAllBytes(Paths.get("C:/Users/Kleis/Dev/Home-Deck/Lights.json"));
                        ObjectMapper objectMapper = new ObjectMapper();
                        Map<String, String> myMap = new HashMap<String, String>();
                        myMap = objectMapper.readValue(jsonData, HashMap.class);
                        lights = objectMapper.convertValue(myMap, Lights.class);
                        lght.setID(0);
                        lght.setName(clientID);
                        lght.setRoomId("0");
                        lght.setState(false);
                        lght.setType("Lamp");
                        lights.getLights().add(lght);
                        Map<String, Object> map = objectMapper.convertValue(lights, Map.class);
                        objectMapper.writeValue(LightsData, map);
                    } catch (StreamReadException e) {
                        e.printStackTrace();
                    } catch (DatabindException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }

        };

    }

    @Bean
    MessageChannel mqttOutboundChannel() {
        return new DirectChannel();
    }

    @Bean
    @ServiceActivator(inputChannel = "mqttOutboundChannel")
    MessageHandler mqttOutbound() {
        // clientId is generated using a random number
        MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler("serverOut", mqttClientFactory());
        messageHandler.setAsync(true);
        messageHandler.setDefaultTopic("#");
        messageHandler.setDefaultRetained(false);
        return messageHandler;
    }

}
