package com.cse308.sbuify;

import com.cse308.sbuify.customer.CustomerProperties;
import com.cse308.sbuify.image.ImageProperties;
import com.cse308.sbuify.image.StorageService;
import com.cse308.sbuify.playlist.PlaylistProperties;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
@EnableConfigurationProperties({ImageProperties.class, PlaylistProperties.class, CustomerProperties.class})
public class SBUifyApplication {

	public static void main(String[] args) {
	    SpringApplication.run(SBUifyApplication.class, args);
	}

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    CommandLineRunner init(StorageService storageService) {
          return (args) -> {
                storageService.init();
          };
    }
}
