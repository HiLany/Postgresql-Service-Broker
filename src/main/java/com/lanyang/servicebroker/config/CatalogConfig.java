package com.lanyang.servicebroker.config;

import org.springframework.cloud.servicebroker.config.BrokerApiVersionConfig;
import org.springframework.cloud.servicebroker.model.Catalog;
import org.springframework.cloud.servicebroker.model.Plan;
import org.springframework.cloud.servicebroker.model.ServiceDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

import java.io.IOException;
import java.util.*;

/**
 * Created by Ly on 3/5/18.
 */
@Configuration
@ComponentScan(basePackages = "com.lanyang.servicebroker", excludeFilters = { @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = BrokerApiVersionConfig.class) })
public class CatalogConfig {

    @Bean
    public Catalog catalog() throws IOException {
        ServiceDefinition serviceDefinition = new ServiceDefinition("pg", "PostgreSQL", "PostgreSQL on shared instance.",
                true, false, getPlans(), getTags(), getServiceDefinitionMetadata(), Arrays.asList("syslog_drain"), null);
        return new Catalog(Arrays.asList(serviceDefinition));
    }

    private static List<String> getTags() {
        return Arrays.asList("PostgreSQL", "Database storage");
    }

    private static Map<String, Object> getServiceDefinitionMetadata() {
        Map<String, Object> sdMetadata = new HashMap<String, Object>();
        sdMetadata.put("displayName", "PostgreSQL");
        sdMetadata.put("imageUrl", "https://wiki.postgresql.org/images/3/30/PostgreSQL_logo.3colors.120x120.png");
        sdMetadata.put("longDescription", "PostgreSQL Service");
        sdMetadata.put("providerDisplayName", "PostgreSQL");
        sdMetadata.put("documentationUrl", "http://mendix.com/postgresql");
        sdMetadata.put("supportUrl", "https://support.mendix.com");
        return sdMetadata;
    }

    private static List<Plan> getPlans() {
        Plan basic = new Plan("postgresql-basic-plan", "standard",
                "A PG plan providing a single database on a shared instance with limited storage.", getBasicPlanMetadata());
        return Arrays.asList(basic);
    }

    private static Map<String, Object> getBasicPlanMetadata() {
        Map<String, Object> planMetadata = new HashMap<String, Object>();
        planMetadata.put("bullets", getBasicPlanBullets());
        return planMetadata;
    }

    private static List<String> getBasicPlanBullets() {
        return Arrays.asList("Single PG database", "Limited storage", "Shared instance");
    }

}
