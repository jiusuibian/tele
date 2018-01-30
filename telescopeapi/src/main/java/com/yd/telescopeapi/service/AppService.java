package com.yd.telescopeapi.service;

import java.util.Map;

public interface AppService {
    Map<String, String> getAppStatus();

    Map<String, String> getAppKey();
}
