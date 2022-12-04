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

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.home.app.AppApplication;
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

}
