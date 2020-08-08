package com.myjdbc.web.socket;

import com.myjdbc.core.util.SecretUtil;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;

/**
 * 监听中心
 *
 * @author game
 */
public class MonitoringCenter {
    private static final Logger logger = LoggerFactory.getLogger(MonitoringCenter.class);

    private static final int BYTE_SIZE = 65535;
    private static final int MAX_CONNECTION = 100;

    /**
     * 开启监听
     */
    public static void startListening() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                SocketServer socketServer = new SocketServer();
                socketServer.startListening();
            }
        };
        thread.start();
        logger.info("MonitoringCenter监听中心已开启");
    }


    @Test
    public void test() {
        String address = "127.0.0.1";
        int port = 4306;


        //校验位|公钥|传输类别|传输长度高位|传输长度低位|正文
        //公钥
        char publicKey = (char) (10000 + new Random().nextInt(55536));
        //传输类别
        char type = 3000;
        //传输长度高位
        char lengthHigh;
        //传输长度低位
        char lengthLow;
        //校验位
        char check;

//        try {
            String text = "abc";
            long length = text.length();
            lengthHigh = (char) ((int) (length / BYTE_SIZE));
            lengthLow = (char) ((int) (length % BYTE_SIZE));

            String value = "" + type + lengthHigh + lengthLow;
            String s = SecretUtil.encryption(value, publicKey + "");
            System.out.println(s);
//            Socket socket = new Socket(address, port);
//            // 输入流
//            InputStreamReader socIn = new InputStreamReader(socket.getInputStream(), "UTF-8");
//            BufferedReader socBuf = new BufferedReader(socIn);
//            PrintWriter socOut = new PrintWriter(socket.getOutputStream());
//            socOut.print(check);
//            socOut.print(publicKey);
//            socOut.print(type);
//            socOut.print(lengthHigh);
//            socOut.print(lengthLow);
//            socOut.println(text);
//            socOut.flush();
//            socOut.close();
//            socIn.close();
//            socket.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

}
