package org.RestAssured.example;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Utils {
    public static Map<String, Object> readJSONFile(String path) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            File file = new File(path);
            ObjectMapper mapper = new ObjectMapper();
            result = new ObjectMapper().readValue(file, HashMap.class);
        }  catch (IOException ex) {
            ex.printStackTrace();
        }
        return  result;
    }

    public static PlayerRq readJSONFileAsPlayer(String path) {
        PlayerRq result = new PlayerRq();
        try {
            File file = new File(path);
            ObjectMapper mapper = new ObjectMapper();
            result = new ObjectMapper().readValue(file, PlayerRq.class);
        }  catch (IOException ex) {
            ex.printStackTrace();
        }
        return  result;
    }
}
