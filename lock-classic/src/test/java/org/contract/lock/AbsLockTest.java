package org.contract.lock;

import org.contract.common.InitException;
import org.contract.common.PropertiesUtils;
import org.contract.config.Config;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import static org.mockito.Mockito.when;

class AbsLockTest {

    Config config;

    String fileName;

    @BeforeEach
    void setUp() throws InitException {
        this.config = Mockito.mock(Config.class);
        when(config.getConfig()).thenReturn(getMap());
    }

    @AfterEach
    void tearDown() {
        this.fileName = null;
        this.config = null;
    }

    public Map getMap() {
        Properties properties = null;
        try {
            properties = PropertiesUtils.getProperties(fileName);
        } catch (IOException e) {
            // ignore
        }
        Map map = new HashMap();
        Iterator<Map.Entry<Object, Object>> iterator = properties.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Object, Object> next = iterator.next();
            map.put(next.getKey(), next.getValue());
        }
        return map;
    }
}