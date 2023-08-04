package com.dtu.serverside;

import com.dtu.Shared.model.FieldAction;
import com.dtu.Shared.model.fileaccess.FieldActionTypeAdapter;
import org.springframework.boot.autoconfigure.gson.GsonBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


// This class is used to configure the GsonBuilder
@Configuration
public class GsonConfig {
    @Bean
    public GsonBuilderCustomizer typeAdapter() {
        return gsonBuilder -> {
            gsonBuilder.registerTypeAdapter(FieldAction.class, new FieldActionTypeAdapter());
        };
    }
}
