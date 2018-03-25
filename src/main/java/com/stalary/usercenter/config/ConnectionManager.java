package com.stalary.usercenter.config;

import org.apache.http.conn.HttpClientConnectionManager;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * ConnectionManage
 *
 * @author lirongqian
 * @since 2018/03/25
 */
@Component
public class ConnectionManager extends Thread {

    @Resource
    private HttpClientConnectionManager connMgr;

    private volatile boolean shutdown;

    public ConnectionManager() {
        super();
        super.start();
    }

    @Override
    public void run() {
        try {
            while (!shutdown) {
                synchronized (this) {
                    wait(5000);
                    // 关闭失效的连接
                    connMgr.closeExpiredConnections();
                }
            }
        } catch (InterruptedException ex) {
            // 结束
        }
    }

    //关闭清理无效连接的线程
    public void shutdown() {
        shutdown = true;
        synchronized (this) {
            notifyAll();
        }
    }
}