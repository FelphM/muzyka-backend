package com.alice.muzyka.config

import org.springframework.boot.SpringApplication
import org.springframework.boot.env.EnvironmentPostProcessor
import org.springframework.core.env.ConfigurableEnvironment
import org.springframework.core.env.MapPropertySource

class DynamicTNSConfig : EnvironmentPostProcessor {
    override fun postProcessEnvironment(environment: ConfigurableEnvironment, application: SpringApplication) {
            
        val osName = System.getProperty("os.name").toLowerCase()
        val tnsAdminPath: String

        if (osName.contains("win")) {
            tnsAdminPath = "C:/oracle/muzyka/"
        } else {
            tnsAdminPath = "/opt/oracle/muzyka/"
        }

        val props = mutableMapOf<String, Any>()
        

        val dbService = "muzyka_high" 
        
        props["spring.datasource.url"] = "jdbc:oracle:thin:@$dbService?TNS_ADMIN=$tnsAdminPath"
        
        environment.propertySources.addFirst(MapPropertySource("osBasedTNSProps", props))
    }
}
