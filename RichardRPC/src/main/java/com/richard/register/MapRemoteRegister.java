package com.richard.register;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapRemoteRegister {
    private static Map<String, List<URL>> map = new HashMap<>();

    public static void regist(String interfaceName, URL url) {
        if (!map.containsKey(interfaceName)) {
            List<URL> urls = new ArrayList<>();
            urls.add(url);
            map.put(interfaceName, urls);
        }
        else {
            map.get(interfaceName).add(url);
        }
    }

    public static List<URL> get(String interfaceName) {
        return map.get(interfaceName);
    }
}
