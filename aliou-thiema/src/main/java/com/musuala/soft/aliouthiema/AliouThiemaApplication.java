package com.musuala.soft.aliouthiema;

import com.musuala.soft.aliouthiema.helpers.FileStorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({
		FileStorageProperties.class
})
public class AliouThiemaApplication {

	public static void main(String[] args) {
		SpringApplication.run(AliouThiemaApplication.class, args);
	}

}
