package com.carpooling.ums.services;

import com.carpooling.ums.entities.RefreshableProperty;
import com.carpooling.ums.repositories.RefreshablePropertyDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RefreshablePropertyService {

    private final RefreshablePropertyDao refreshablePropertyDao;
    private final Map<String, String> propertiesMap = new HashMap<>();

    @Autowired
    public RefreshablePropertyService(RefreshablePropertyDao refreshablePropertyDao) {
        this.refreshablePropertyDao = refreshablePropertyDao;
    }

    @PostConstruct
    private void loadProperties() {
        List<RefreshableProperty> properties = refreshablePropertyDao.findAll();
        for (RefreshableProperty property : properties) {
            if (property.getActive() != null && property.getActive()) {
                propertiesMap.put(property.getKey(), property.getValue());
            }
        }
    }

    public String getProperty(String key) {
        return propertiesMap.get(key);
    }
}
