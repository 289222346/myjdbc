package com.myjdbc.web.socket;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 陈文
 */
public class ServerSocketThread extends Thread {
    private static final Logger logger = LoggerFactory.getLogger(ServerSocketThread.class);

    /**
     * 客户端连接
     */
    private Socket socket;
    /**
     * 推送数据
     */
    private PrintWriter pw;
    /**
     * 输入流
     */
    private InputStream is = null;
    /**
     * 输出流
     */
    private OutputStream os = null;

    ServerSocketThread(Socket socket) {
        this.socket = socket;
        try {
            socket.setSoTimeout(60000);
            os = socket.getOutputStream();
            is = socket.getInputStream();
            pw = new PrintWriter(os, true);
            this.start();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public void send(String msg) {
        pw.println(msg);
        pw.flush(); // 强制送出数据
    }

    @Override
    public void run() {
        byte[] buff = new byte[4096];
        List<byte[]> buffList = new ArrayList<>();
        StringBuffer rcvMsg = new StringBuffer();
        int rcvLen;
        boolean flag = false;

        // 读取客户端数据
        BufferedReader input = new BufferedReader(new InputStreamReader(is));
        try {
            //公钥|传输类别|传输长度高位|传输长度低位|校验位|正文
            int publicKey = input.read();
            int type = input.read();
            int lengthHigh = input.read();
            int lengthLow = input.read();
            long lengthMax = lengthHigh * 65535 + lengthLow;
            int check = input.read();

            String str = input.readLine();

            System.out.println("check=" + check);
            System.out.println("publicKey=" + publicKey);
            System.out.println("type=" + type);
            System.out.println("lengthMax=" + lengthMax);
            System.out.println(str);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //发送接收成功字符串
        send("ok");

        try {
            socket.close();
            socket = null;
        } catch (IOException e) {
            logger.error("Socket发生错误：" + e.getMessage());
        }

    }

}