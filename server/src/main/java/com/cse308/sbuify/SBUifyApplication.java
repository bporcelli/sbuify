package com.cse308.sbuify;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.cse308.sbuify.album.AlbumProperties;
import com.cse308.sbuify.customer.CustomerProperties;
import com.cse308.sbuify.customer.LibraryProperties;
import com.cse308.sbuify.image.ImageProperties;
import com.cse308.sbuify.image.StorageService;
import com.cse308.sbuify.playlist.PlaylistProperties;
import com.cse308.sbuify.reports.ReportProperties;

@SpringBootApplication
@EnableConfigurationProperties({ImageProperties.class, PlaylistProperties.class, AlbumProperties.class, LibraryProperties.class, CustomerProperties.class, ReportProperties.class})
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
