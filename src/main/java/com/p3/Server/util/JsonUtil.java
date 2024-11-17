package com.p3.Server.util;

import java.util.HashMap;
import java.util.Map;

public class JsonUtil {

    public static <T> Map<String, T> singleJsonResponse(String key, T value){
        Map<String, T> response = new HashMap<>();
        response.put(key, value);
        return response;
    }


}
