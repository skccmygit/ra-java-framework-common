package com.skcc.ra.common.config;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.support.TransactionSynchronizationManager;

// routing database 설정
public class RoutingDataSource extends AbstractRoutingDataSource {
    @Override
    protected Object determineCurrentLookupKey() {
        return (TransactionSynchronizationManager.isCurrentTransactionReadOnly()) ? "slave" : "master";
    }
}