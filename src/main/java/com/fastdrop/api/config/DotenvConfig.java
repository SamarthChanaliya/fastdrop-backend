package com.fastdrop.api.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DotenvConfig {

    static {
        Dotenv dotenv = Dotenv.configure()
                .ignoreIfMissing()
                .load();

        dotenv.entries().forEach(entry ->{
            System.setProperty(entry.getKey(), entry.getValue());
//            System.out.println("✅ Loaded ENV: " + entry.getKey());
        }


        );

    }
}
