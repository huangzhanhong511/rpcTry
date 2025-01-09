package com.richard.loadBalance;

import java.net.URL;
import java.util.List;
import java.util.Random;

public class LoadBalance {

    public static URL randomUrl(List<URL> urls) {
        Random random = new Random();
        int index = random.nextInt(urls.size());
        return urls.get(index);
    }
}
