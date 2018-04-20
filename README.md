## About
This project is referred to [postgresql-cf-service-broker](!https://github.com/cloudfoundry-community/postgresql-cf-service-broker) and [cloudfoundry-service-broker](!https://github.com/spring-cloud-samples/cloudfoundry-service-broker).

### In Cloud Foundry

Find out the database subnet and create a security group rule (postgresql.json):
```
[{"protocol":"tcp","destination":"10.10.8.0/24","ports":"5432"}]
```

import this into CF with:
```
cf create-security-group postgresql-service postgresql.json
```

Bind to the full cf install:
```
cf bind-running-security-group postgresql-service
```


Build the package with `mvn package` then push it out:
```
cf push postgresql-cf-service-broker -p target/postgresql-cf-service-broker-2.4.0-SNAPSHOT.jar --no-start
```

Export the following environment variables:

```
cf set-env postgresql-cf-service-broker MASTER_JDBC_URL "jdbcurl"
cf set-env postgresql-cf-service-broker JAVA_OPTS "-Dsecurity.user.password=mysecret"
```

Start the service broker:
```
cf start postgresql-cf-service-broker
```

Create Cloud Foundry service broker:
```
cf create-service-broker postgresql-cf-service-broker user mysecret http://postgresql-cf-service-broker.cfapps.io
```

Add service broker to Cloud Foundry Marketplace:

```
cf enable-service-access PostgreSQL -p "Basic PostgreSQL Plan" -o ORG
```

## Registering a Broker with the Cloud Controller

See [Managing Service Brokers](http://docs.cloudfoundry.org/services/managing-service-brokers.html).