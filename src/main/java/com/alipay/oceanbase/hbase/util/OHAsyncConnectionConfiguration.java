package com.alipay.oceanbase.hbase.util;

import com.alipay.oceanbase.rpc.property.Property;
import org.apache.hadoop.classification.InterfaceAudience;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HConstants;

import java.util.Properties;
import java.util.concurrent.TimeUnit;

import static com.alipay.oceanbase.hbase.constants.OHConstants.*;
import static com.alipay.oceanbase.hbase.constants.OHConstants.MAX_KEYVALUE_SIZE_DEFAULT;
import static com.alipay.oceanbase.hbase.constants.OHConstants.MAX_KEYVALUE_SIZE_KEY;
import static com.alipay.oceanbase.hbase.constants.OHConstants.WRITE_BUFFER_SIZE_DEFAULT;
import static org.apache.commons.lang.StringUtils.isBlank;
import static org.apache.hadoop.hbase.HConstants.*;
import static org.apache.hadoop.hbase.HConstants.DEFAULT_HBASE_CLIENT_RETRIES_NUMBER;
import static org.apache.hadoop.hbase.client.ConnectionConfiguration.WRITE_BUFFER_PERIODIC_FLUSH_TIMEOUT_MS;
import static org.apache.hadoop.hbase.client.ConnectionConfiguration.WRITE_BUFFER_PERIODIC_FLUSH_TIMEOUT_MS_DEFAULT;
import static org.apache.hadoop.hbase.ipc.RpcClient.DEFAULT_SOCKET_TIMEOUT_CONNECT;
import static org.apache.hadoop.hbase.ipc.RpcClient.SOCKET_TIMEOUT_CONNECT;

@InterfaceAudience.Private
public class OHAsyncConnectionConfiguration {
    private String           paramUrl;
    private String           database;
    private final Properties properties;
    private final String     fullUsername;
    private final String     password;
    private final String     sysUsername;
    private final String     sysPassword;
    private final String     odpAddr;
    private final int        odpPort;
    private final boolean    odpMode;
    private final long       writeBufferSize;
    private final int        operationTimeout;
    private final int        metaOperationTimeout;
    private final int        scannerCaching;
    private final int        metaScannerCaching;
    private final long       scannerMaxResultSize;
    private final int        maxKeyValueSize;
    private final int        rpcTimeout;
    private final int        readRpcTimeout;
    private final int        writeRpcTimeout;
    private final int        rpcConnectTimeout;
    private final int        maxRetries;
    private final long       writeBufferPeriodicFlushTimeoutNs;

    public OHAsyncConnectionConfiguration(Configuration conf) {
        this.paramUrl = conf.get(HBASE_OCEANBASE_PARAM_URL);
        this.fullUsername = conf.get(HBASE_OCEANBASE_FULL_USER_NAME);
        this.password = conf.get(HBASE_OCEANBASE_PASSWORD);
        this.sysUsername = conf.get(HBASE_OCEANBASE_SYS_USER_NAME);
        this.sysPassword = conf.get(HBASE_OCEANBASE_SYS_PASSWORD);
        this.odpAddr = conf.get(HBASE_OCEANBASE_ODP_ADDR);
        this.odpPort = conf.getInt(HBASE_OCEANBASE_ODP_PORT, -1);
        this.odpMode = conf.getBoolean(HBASE_OCEANBASE_ODP_MODE, false);
        String database = conf.get(HBASE_OCEANBASE_DATABASE);
        if (isBlank(database)) {
            database = "default";
        }
        this.database = database;
        this.writeBufferSize = conf.getLong(WRITE_BUFFER_SIZE_KEY, WRITE_BUFFER_SIZE_DEFAULT);
        this.metaOperationTimeout = conf.getInt(HConstants.HBASE_CLIENT_META_OPERATION_TIMEOUT,
                HConstants.DEFAULT_HBASE_CLIENT_OPERATION_TIMEOUT);
        this.operationTimeout = conf.getInt(HConstants.HBASE_CLIENT_OPERATION_TIMEOUT,
                HConstants.DEFAULT_HBASE_CLIENT_OPERATION_TIMEOUT);
        this.rpcTimeout = conf.getInt(HConstants.HBASE_RPC_TIMEOUT_KEY,
                HConstants.DEFAULT_HBASE_RPC_TIMEOUT);
        this.readRpcTimeout = conf.getInt(HConstants.HBASE_RPC_READ_TIMEOUT_KEY,
                HConstants.DEFAULT_HBASE_RPC_TIMEOUT);
        this.writeRpcTimeout = conf.getInt(HConstants.HBASE_RPC_WRITE_TIMEOUT_KEY,
                HConstants.DEFAULT_HBASE_RPC_TIMEOUT);
        int rpcConnectTimeout = -1;
        if (conf.get(SOCKET_TIMEOUT_CONNECT) != null) {
            rpcConnectTimeout = conf.getInt(SOCKET_TIMEOUT_CONNECT, DEFAULT_SOCKET_TIMEOUT_CONNECT);
        } else {
            if (conf.get(SOCKET_TIMEOUT) != null) {
                rpcConnectTimeout = conf.getInt(SOCKET_TIMEOUT, DEFAULT_SOCKET_TIMEOUT);
            } else {
                rpcConnectTimeout = conf.getInt(SOCKET_TIMEOUT_CONNECT,
                        DEFAULT_SOCKET_TIMEOUT_CONNECT);
            }
        }
        this.rpcConnectTimeout = rpcConnectTimeout;
        this.maxRetries = conf.getInt(HBASE_CLIENT_RETRIES_NUMBER, DEFAULT_HBASE_CLIENT_RETRIES_NUMBER);
        this.scannerCaching = conf.getInt(HConstants.HBASE_CLIENT_SCANNER_CACHING,
                Integer.MAX_VALUE);
        this.metaScannerCaching =
                conf.getInt(HBASE_META_SCANNER_CACHING, DEFAULT_HBASE_META_SCANNER_CACHING);
        this.scannerMaxResultSize = conf.getLong(
                HConstants.HBASE_CLIENT_SCANNER_MAX_RESULT_SIZE_KEY, WRITE_BUFFER_SIZE_DEFAULT);
        this.maxKeyValueSize = conf.getInt(MAX_KEYVALUE_SIZE_KEY, MAX_KEYVALUE_SIZE_DEFAULT);
        this.writeBufferPeriodicFlushTimeoutNs =
                TimeUnit.MILLISECONDS.toNanos(conf.getLong(WRITE_BUFFER_PERIODIC_FLUSH_TIMEOUT_MS,
                        WRITE_BUFFER_PERIODIC_FLUSH_TIMEOUT_MS_DEFAULT));
        properties = new Properties();
        for (Property property : Property.values()) {
            String value = conf.get(property.getKey());
            if (value != null) {
                properties.put(property.getKey(), value);
            }
        }
    }

    public void setParamUrl(String paramUrl) {
        this.paramUrl = paramUrl;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public long getWriteBufferSize() {
        return this.writeBufferSize;
    }

    public int getMetaOperationTimeout() {
        return this.metaOperationTimeout;
    }

    public int getOperationTimeout() {
        return this.operationTimeout;
    }

    public int getScannerCaching() {
        return this.scannerCaching;
    }

    public int getMaxKeyValueSize() {
        return this.maxKeyValueSize;
    }

    public int getRpcTimeout() {
        return this.rpcTimeout;
    }

    public int getReadRpcTimeout() {
        return this.readRpcTimeout;
    }

    public int getWriteRpcTimeout() {
        return this.writeRpcTimeout;
    }

    public int getRpcConnectTimeout() {
        return this.rpcConnectTimeout;
    }

    public long getScannerMaxResultSize() {
        return this.scannerMaxResultSize;
    }

    public Properties getProperties() {
        return this.properties;
    }

    public int getOdpPort() {
        return this.odpPort;
    }

    public String getSysPassword() {
        return this.sysPassword;
    }

    public String getSysUsername() {
        return this.sysUsername;
    }

    public String getPassword() {
        return this.password;
    }

    public String getFullUsername() {
        return this.fullUsername;
    }

    public String getParamUrl() {
        return this.paramUrl;
    }

    public String getOdpAddr() {
        return this.odpAddr;
    }

    public boolean isOdpMode() {
        return this.odpMode;
    }

    public String getDatabase() {
        return this.database;
    }

    int getMaxRetries() {
        return maxRetries;
    }

    int getMetaScannerCaching() {
        return metaScannerCaching;
    }

    long getWriteBufferPeriodicFlushTimeoutNs() {
        return writeBufferPeriodicFlushTimeoutNs;
    }
}
