package com.yukikase.framework.orm.proxy;

import com.yukikase.framework.orm.DatabaseConnector;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;

public class ConnectionProxy {
    public static Connection wrap(Connection delegate, DatabaseConnector pool) {
        return (Connection) Proxy.newProxyInstance(
                Connection.class.getClassLoader(),
                new Class<?>[]{Connection.class},
                new ConnectionHandler(delegate, pool)
        );
    }

    private static class ConnectionHandler implements InvocationHandler {

        private final Connection delegate;
        private final DatabaseConnector pool;
        private boolean closed;

        ConnectionHandler(Connection delegate, DatabaseConnector pool) {
            this.delegate = delegate;
            this.pool = pool;
            this.closed = false;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            String methodName = method.getName();

            if (methodName.equals("close")) {
                if (!closed) {
                    closed = true;
                    pool.returnConnection(delegate);
                }

                return null;
            }

            if (methodName.equals("isClosed")) {
                return closed || delegate.isClosed();
            }

            return method.invoke(delegate, args);
        }
    }
}

