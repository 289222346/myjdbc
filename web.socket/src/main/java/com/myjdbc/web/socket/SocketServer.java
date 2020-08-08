package com.myjdbc.web.socket;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


/**
 * @author 陈文
 */
public class SocketServer {
    private static final Logger logger = LoggerFactory.getLogger(SocketServer.class);

    private int port = 4306;
    private int timeout = 30000;
    /**
     * 线程监听标志位
     */
    private boolean isListen = true;


    public SocketServer() {
    }

    public SocketServer(int port) {
        this.port = port;
    }

    /**
     * 开启监听
     */
    public void startListening() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            //5秒监听不到，则视为监听失败
            serverSocket.setSoTimeout(timeout);
            while (isListen) {
                Socket socket = getSocket(serverSocket);
                if (socket != null) {
                    try {
                        new ServerSocketThread(socket);
                    } catch (Exception e) {
                        logger.error(e.getMessage());
                    }
                }
            }
            serverSocket.close();
        } catch (IOException e) {
            //奔溃继续监听
            startListening();
            logger.error(e.getMessage());
        }
    }

    /**
     * 接收到客户端请求
     *
     * @param serverSocket
     * @return
     */
    private Socket getSocket(ServerSocket serverSocket) {
        try {
            return serverSocket.accept();
        } catch (IOException e) {
            logger.error(e.getMessage());
            return null;
        }
    }

}
