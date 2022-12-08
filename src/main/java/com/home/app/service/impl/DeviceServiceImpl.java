package com.home.app.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.print.attribute.standard.Fidelity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.api.client.json.Json;
import com.home.app.AppApplication;
import com.home.app.model.Layout;
import com.home.app.model.LayoutElement;
import com.home.app.model.Light;
import com.home.app.model.Lights;
import com.home.app.model.Open;
import com.home.app.model.Opens;

import se.michaelthelin.spotify.model_objects.miscellaneous.Device;

public class DeviceServiceImpl {
    private final Logger logger = LoggerFactory.getLogger(AppApplication.class);

    public Lights returnLights() {

        Lights emp = new Lights();
        try {
            // read json file data to String
            byte[] jsonData = Files.readAllBytes(Paths.get("C:/Users/Kleis/Dev/Home-Deck/Lights.json"));

            // create ObjectMapper instance
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, String> myMap = new HashMap<String, String>();
            myMap = objectMapper.readValue(jsonData, HashMap.class);

            emp = objectMapper.convertValue(myMap, Lights.class);

            logger.info("Devolviendo datos de luces");

            return emp;

        } catch (StreamReadException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (DatabindException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return emp;
    }

    public ArrayList<Light> returnLightsFromRoom(String RoomID) {

        Lights lightsData = new Lights();
        ArrayList<Light> response = new ArrayList<Light>();
        Light aux = new Light();

        try {
            // read json file data to String
            byte[] jsonData = Files.readAllBytes(Paths.get("C:/Users/Kleis/Dev/Home-Deck/Lights.json"));

            // create ObjectMapper instance
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, String> myMap = new HashMap<String, String>();
            myMap = objectMapper.readValue(jsonData, HashMap.class);

            lightsData = objectMapper.convertValue(myMap, Lights.class);

            logger.info("Devolviendo datos de Luces");

            for (Light o : lightsData.getLights()) {
                if (RoomID.equals(o.getRoomId())) {
                    response.add(o);
                }
            }

            return response;

        } catch (StreamReadException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (DatabindException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

    public Light modifyLightValues(Integer DeviceID, String field, String value) {
        File LightsData = new File("C:/Users/Kleis/Dev/Home-Deck/Lights.json");

        logger.info("Modificando el dispositivo: " + DeviceID);
        logger.info("Campo: " + field + " = " + value);
        Lights lights = new Lights();
        Light lght = new Light();
        try {
            byte[] jsonData = Files.readAllBytes(Paths.get("C:/Users/Kleis/Dev/Home-Deck/Lights.json"));
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, String> myMap = new HashMap<String, String>();
            myMap = objectMapper.readValue(jsonData, HashMap.class);
            lights = objectMapper.convertValue(myMap, Lights.class);

            for (Light l : lights.getLights()) {
                if (l.getID() == DeviceID) {
                    switch (field) {
                        case "id":
                            l.setID(Integer.parseInt(value));
                            lght = l;
                            break;
                        case "name":
                            l.setName(value);
                            lght = l;

                            break;
                        case "gridSite":
                            l.setGridSite(value);
                            lght = l;

                            break;
                        case "lightBrightness":
                            l.setLightBrightness(Integer.parseInt(value));
                            lght = l;

                            break;
                        case "state":
                            logger.info("entra aca: " + l.toString());

                            l.setState(Boolean.valueOf(value));
                            lght = l;

                            break;

                    }
                }
            }

            Map<String, Object> map = objectMapper.convertValue(lights, Map.class);
            objectMapper.writeValue(LightsData, map);
            return lght;
        } catch (StreamReadException e) {
            e.printStackTrace();
        } catch (DatabindException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return lght;
    }

    public Light createLightDevice(Integer DeviceID, String RoomID, String Name, Boolean State) {
        File LightsData = new File("C:/Users/Kleis/Dev/Home-Deck/Lights.json");

        Lights lights = new Lights();
        Light lght = new Light();

        System.out.println("Creando Light Device");

        try {
            byte[] jsonData = Files.readAllBytes(Paths.get("C:/Users/Kleis/Dev/Home-Deck/Lights.json"));
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, String> myMap = new HashMap<String, String>();
            myMap = objectMapper.readValue(jsonData, HashMap.class);
            lights = objectMapper.convertValue(myMap, Lights.class);

            lght.setID(DeviceID);
            lght.setName(Name);
            lght.setRoomId(RoomID);
            lght.setState(State);
            lights.getLights().add(lght);

            Map<String, Object> map = objectMapper.convertValue(lights, Map.class);
            objectMapper.writeValue(LightsData, map);
            return lght;
        } catch (StreamReadException e) {
            e.printStackTrace();
        } catch (DatabindException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return lght;
    }

    public Opens returnOpens() {

        Opens opensData = new Opens();
        try {
            // read json file data to String
            byte[] jsonData = Files.readAllBytes(Paths.get("C:/Users/Kleis/Dev/Home-Deck/Opens.json"));

            // create ObjectMapper instance
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, String> myMap = new HashMap<String, String>();
            myMap = objectMapper.readValue(jsonData, HashMap.class);

            opensData = objectMapper.convertValue(myMap, Opens.class);

            logger.info("Devolviendo datos de Aperturas");

            return opensData;

        } catch (StreamReadException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (DatabindException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return opensData;
    }

    public ArrayList<Open> returnOpen(String RoomID) {

        Opens opensData = new Opens();
        ArrayList<Open> response = new ArrayList<Open>();
        Open aux = new Open();

        try {
            // read json file data to String
            byte[] jsonData = Files.readAllBytes(Paths.get("C:/Users/Kleis/Dev/Home-Deck/Opens.json"));

            // create ObjectMapper instance
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, String> myMap = new HashMap<String, String>();
            myMap = objectMapper.readValue(jsonData, HashMap.class);

            opensData = objectMapper.convertValue(myMap, Opens.class);

            logger.info("Devolviendo datos de Aperturas");

            for (Open o : opensData.getOpens()) {
                if (RoomID.equals(o.getRoomId())) {
                    response.add(o);
                }
            }

            return response;

        } catch (StreamReadException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (DatabindException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

    public Open modifyOpenDevice(Integer DeviceID, String field, String value) {
        File LightsData = new File("C:/Users/Kleis/Dev/Home-Deck/Opens.json");

        logger.info("Modificando el dispositivo: " + DeviceID);
        logger.info("Campo: " + field + " = " + value);
        Opens opens = new Opens();
        Open open = new Open();
        try {
            byte[] jsonData = Files.readAllBytes(Paths.get("C:/Users/Kleis/Dev/Home-Deck/Opens.json"));
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, String> myMap = new HashMap<String, String>();
            myMap = objectMapper.readValue(jsonData, HashMap.class);
            opens = objectMapper.convertValue(myMap, Opens.class);

            for (Open l : opens.getOpens()) {
                if (l.getId() == DeviceID) {
                    switch (field) {
                        case "id":
                            l.setID(Integer.parseInt(value));
                            open = l;
                            break;
                        case "name":
                            l.setName(value);
                            open = l;

                            break;
                        case "gridSite":
                            l.setGridSite(value);
                            open = l;

                            break;

                        case "state":
                            logger.info("entra aca: " + l.toString());

                            l.setState(Boolean.valueOf(value));
                            open = l;

                            break;

                    }
                }
            }

            Map<String, Object> map = objectMapper.convertValue(opens, Map.class);
            objectMapper.writeValue(LightsData, map);
            return open;
        } catch (StreamReadException e) {
            e.printStackTrace();
        } catch (DatabindException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return open;
    }

    public String returnRoomLayout(String RoomID) {

        Layout opensData = new Layout();
        String response = "";

        try {
            // read json file data to String
            byte[] jsonData = Files.readAllBytes(Paths.get("C:/Users/Kleis/Dev/Home-Deck/Layout.json"));

            // create ObjectMapper instance
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, String> myMap = new HashMap<String, String>();
            myMap = objectMapper.readValue(jsonData, HashMap.class);

            opensData = objectMapper.convertValue(myMap, Layout.class);

            logger.info("Devolviendo datos de layouts");

            for (LayoutElement o : opensData.getLayouts()) {
                if (RoomID.equals(o.getRoomId())) {
                    response = o.getLayout();
                }
            }

            return response;

        } catch (StreamReadException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (DatabindException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

    public LayoutElement setRoomLayout(String RoomID, String layout) {
        File LayoutData = new File("C:/Users/Kleis/Dev/Home-Deck/Layout.json");

        Layout layoutAux = new Layout();
        LayoutElement layoutElement = new LayoutElement();
        try {
            byte[] jsonData = Files.readAllBytes(Paths.get("C:/Users/Kleis/Dev/Home-Deck/Layout.json"));
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, String> myMap = new HashMap<String, String>();
            myMap = objectMapper.readValue(jsonData, HashMap.class);
            layoutAux = objectMapper.convertValue(myMap, Layout.class);

            for (LayoutElement l : layoutAux.getLayouts()) {
                if (l.getRoomId().equals(RoomID)) {
                    objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
                    objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

                    l.setLayout(layout);
                    layoutElement = l;
                }
            }

            Map<String, Object> map = objectMapper.convertValue(layoutAux, Map.class);
            objectMapper.writeValue(LayoutData, map);
            return layoutElement;
        } catch (StreamReadException e) {
            e.printStackTrace();
        } catch (DatabindException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return layoutElement;
    }

}
