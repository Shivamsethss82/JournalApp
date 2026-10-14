package com.shivam.journalApp.cache;

import com.shivam.journalApp.entity.ConfigJournalAppEntity;
import com.shivam.journalApp.repository.ConfigJournalAppRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class AppCache {

    public enum keys{
        weather_api;
    }

    @Autowired
    private ConfigJournalAppRepository configJournalAppRepository;

    public Map<String, String> APPCACHE = new HashMap<>();

    @PostConstruct
    public void init() {
        List<ConfigJournalAppEntity> all = configJournalAppRepository.findAll();
        all.forEach(entity -> APPCACHE.put(entity.getKey(), entity.getValue()));
    }
}