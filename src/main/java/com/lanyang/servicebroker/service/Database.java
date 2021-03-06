package com.lanyang.servicebroker.service;

import com.lanyang.servicebroker.Model.ServiceInstance;
import com.lanyang.servicebroker.common.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.servicebroker.model.CreateServiceInstanceRequest;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Ly on 3/5/18.
 */
@Component
public class Database {

    private static final Logger logger = LoggerFactory.getLogger(Database.class);

    public void createDatabaseForInstance(String instanceId, String serviceId, String planId, String organizationGuid, String spaceGuid) throws SQLException {
        Utils.checkValidUUID(instanceId);
        PostgreSQLDatabase.executeUpdate("CREATE DATABASE \"" + instanceId + "\" ENCODING 'UTF8'");
        PostgreSQLDatabase.executeUpdate("REVOKE all on database \"" + instanceId + "\" from public");

        Map<Integer, String> parameterMap = new HashMap<Integer, String>();
        parameterMap.put(1, instanceId);
        parameterMap.put(2, serviceId);
        parameterMap.put(3, planId);
        parameterMap.put(4, organizationGuid);
        parameterMap.put(5, spaceGuid);

        PostgreSQLDatabase.executePreparedUpdate("INSERT INTO service (serviceinstanceid, servicedefinitionid, planid, organizationguid, spaceguid) VALUES (?, ?, ?, ?, ?)", parameterMap);
    }

    public void deleteDatabase(String instanceId) throws SQLException {
        Utils.checkValidUUID(instanceId);

        Map<Integer, String> parameterMap = new HashMap<Integer, String>();
        parameterMap.put(1, instanceId);

        Map<String, String> result = PostgreSQLDatabase.executeSelect("SELECT current_user");
        String currentUser = null;

        if(result != null) {
            currentUser = result.get("current_user");
        }

        if(currentUser == null) {
            logger.error("Current user for instance '" + instanceId + "' could not be found");
        }

        PostgreSQLDatabase.executePreparedSelect("SELECT pg_terminate_backend(pg_stat_activity.pid) FROM pg_stat_activity WHERE pg_stat_activity.datname = ? AND pid <> pg_backend_pid()", parameterMap);
        PostgreSQLDatabase.executeUpdate("ALTER DATABASE \"" + instanceId + "\" OWNER TO \"" + currentUser + "\"");
        PostgreSQLDatabase.executeUpdate("DROP DATABASE IF EXISTS \"" + instanceId + "\"");
        PostgreSQLDatabase.executePreparedUpdate("DELETE FROM service WHERE serviceinstanceid=?", parameterMap);
    }

    public ServiceInstance findServiceInstance(String instanceId) throws SQLException {
        Utils.checkValidUUID(instanceId);

        Map<Integer, String> parameterMap = new HashMap<Integer, String>();
        parameterMap.put(1, instanceId);

        Map<String, String> result = PostgreSQLDatabase.executePreparedSelect("SELECT * FROM service WHERE serviceinstanceid = ?", parameterMap);

        String serviceDefinitionId = result.get("servicedefinitionid");
        String organizationGuid = result.get("organizationguid");
        String planId = result.get("planid");
        String spaceGuid = result.get("spaceguid");

        CreateServiceInstanceRequest wrapper = new CreateServiceInstanceRequest(serviceDefinitionId, planId, organizationGuid, spaceGuid).withServiceInstanceId(instanceId);
        return new ServiceInstance(wrapper);
    }

    // TODO needs to be implemented
    public List<ServiceInstance> getAllServiceInstances() {
        return Collections.emptyList();
    }

}
