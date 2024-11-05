package com.alipay.oceanbase.hbase.util;

import com.alipay.oceanbase.hbase.exception.FeatureNotSupportedException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.ServerName;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.security.User;
import org.slf4j.Logger;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

public class OHAsyncConnectionImpl implements AsyncConnection {
    private static final Logger LOGGER               = TableHBaseLoggerFactory
            .getLogger(OHConnectionImpl.class);

    private final Configuration conf;

    private final OHAsyncConnectionConfiguration asyncConnectionConfig;


    public OHAsyncConnectionImpl(Configuration conf, Closeable registry, String clusterId,
                                 User user) throws IOException {
        this.conf = conf;
        this.asyncConnectionConfig = new OHAsyncConnectionConfiguration(conf);
    }

    @Override
    public Configuration getConfiguration() {
        return this.conf;
    }

    public OHAsyncConnectionConfiguration getOHAsyncConnectionConfiguration() {
        return this.asyncConnectionConfig;
    }

    @Override
    public void close() {
    }

    @Override
    public AsyncTableRegionLocator getRegionLocator(TableName tableName) {
        throw new FeatureNotSupportedException("not supported yet.");
    }

    @Override
    public AsyncTableBuilder<AdvancedScanResultConsumer> getTableBuilder(TableName tableName) {
        throw new FeatureNotSupportedException("not supported yet.");
    }

    @Override
    public AsyncTableBuilder<ScanResultConsumer> getTableBuilder(TableName tableName,
                                                                 ExecutorService pool) {
        throw new FeatureNotSupportedException("not supported yet.");
    }

    @Override
    public AsyncAdminBuilder getAdminBuilder() {
        throw new FeatureNotSupportedException("admin is not supported yet.");
    }

    @Override
    public AsyncAdminBuilder getAdminBuilder(ExecutorService executorService) {
        throw new FeatureNotSupportedException("admin is not supported yet.");
    }

    @Override
    public AsyncBufferedMutatorBuilder getBufferedMutatorBuilder(TableName tableName) {
        // TODO: AsyncBufferedMutatorBuilderImpl
        throw new FeatureNotSupportedException("not support yet.");
    }

    @Override
    public AsyncBufferedMutatorBuilder getBufferedMutatorBuilder(TableName tableName, ExecutorService pool) {
        // TODO: AsyncBufferedMutatorBuilderImpl
        throw new FeatureNotSupportedException("not support yet.");
    }

    @Override
    public CompletableFuture<Hbck> getHbck() {
        throw new FeatureNotSupportedException("not support yet.");
    }

    @Override
    public Hbck getHbck(ServerName masterServer) throws IOException {
        throw new FeatureNotSupportedException("not support yet.");
    }

}
