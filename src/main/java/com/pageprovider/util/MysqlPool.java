package com.pageprovider.util;

import com.pageprovider.util.exceptions.ConnectionException;
import org.apache.commons.pool.BasePoolableObjectFactory;
import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.PoolableObjectFactory;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.commons.pool.impl.GenericObjectPoolFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by antoniop on 25/02/15.
 */
public class MysqlPool {

    private static ObjectPool pool;

    private final static Logger LOG = Logger.getLogger(MysqlPool.class.getName());

    public static void init(String host, int port, String schema, String user, String password) {

        if(pool == null) {
            PoolableObjectFactory mySqlPoolableObjectFactory = new MySqlPoolableObjectFactory(host, port, schema, user, password);
            GenericObjectPool.Config config         = new GenericObjectPool.Config();
            config.maxActive                        = 10;
            config.testOnBorrow                     = true;
            config.testWhileIdle                    = true;
            config.timeBetweenEvictionRunsMillis    = 10000;
            config.minEvictableIdleTimeMillis       = 60000;

            GenericObjectPoolFactory genericObjectPoolFactory = new GenericObjectPoolFactory(mySqlPoolableObjectFactory, config);
            pool = genericObjectPoolFactory.createPool();
        }
    }

    public static void init(String host, int port, String schema, String user, String password, int maxActive, boolean testOnBorrow, boolean testWhileIdle, int timeBetweenEvictionRunsMillis, int minEvictableIdleTimeMillis ) {

        if(pool == null) {
            PoolableObjectFactory mySqlPoolableObjectFactory = new MySqlPoolableObjectFactory(host, port, schema, user, password);

            GenericObjectPool.Config config         = new GenericObjectPool.Config();
            config.maxActive                        = maxActive;
            config.testOnBorrow                     = testWhileIdle;
            config.testWhileIdle                    = testWhileIdle;
            config.timeBetweenEvictionRunsMillis    = timeBetweenEvictionRunsMillis;
            config.minEvictableIdleTimeMillis       = minEvictableIdleTimeMillis;

            GenericObjectPoolFactory genericObjectPoolFactory = new GenericObjectPoolFactory(mySqlPoolableObjectFactory, config);
            pool = genericObjectPoolFactory.createPool();
        }
    }


    public static Connection getConnection() throws ConnectionException{
        try{
            return (Connection)pool.borrowObject();
        }catch (Exception ex){
            throw new ConnectionException(ex);
        }
    }

    public static void closeConnection(Connection conn){
        try{
            pool.returnObject(conn);
        }catch(Exception ex){
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }


    private static class MySqlPoolableObjectFactory extends BasePoolableObjectFactory {
        private String  host;
        private int     port;
        private String  schema;
        private String  user;
        private String  password;

        public MySqlPoolableObjectFactory(String host, int port, String schema,
                                          String user, String password) {
            this.host = host;
            this.port = port;
            this.schema = schema;
            this.user = user;
            this.password = password;
        }

        @Override
        public Object makeObject() throws Exception {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            String url = "jdbc:mysql://" + host + ":" + port + "/"
                    + schema + "?autoReconnectForPools=true";
            return DriverManager.getConnection(url, user, password);
        }
    }
}
