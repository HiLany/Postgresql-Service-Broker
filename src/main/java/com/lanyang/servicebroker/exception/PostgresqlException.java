package com.lanyang.servicebroker.exception;

import org.springframework.cloud.servicebroker.exception.ServiceBrokerException;

/**
 *
 * Exception thrown when issues with the underlying postgres service occur.
 * Created by Ly on 3/5/18.
 */
public class PostgresqlException extends ServiceBrokerException {

    private static final long serialVersionUID = 8667141725171626000L;

    public PostgresqlException(String message) {
        super(message);
    }
}
