package com.ylfin.spider.utils;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

public class RemoteShellTool {

    private Connection conn;
    private String ipAddr;
    private String charset = Charset.defaultCharset().toString();
    private String userName;
    private String password;

    private  Session session;

    public RemoteShellTool(String ipAddr, String userName, String password,
                           String charset) {
        this.ipAddr = ipAddr;
        this.userName = userName;
        this.password = password;
        if (charset != null) {
            this.charset = charset;
        }
    }

    public boolean login() throws IOException {
        conn = new Connection(ipAddr);
        conn.connect(); // 连接
        // 认证
        return conn.authenticateWithPassword(userName, password);
    }
//
//    public String execing(String cmds){
//        String result = "";
//        try {
//            if (this.login()) {
//                // 打开一个会话
//                Session session = conn.openSession();
//                session.execCommand(cmds);
//
//                in = session.getStdout();
//                result = this.processStdout(in, this.charset);
//                session.close();
//                conn.close();
//            }
//        } catch (IOException e1) {
//            e1.printStackTrace();
//        }
//        return result;
//    }

    public String exec(String cmds) {
        InputStream in = null;
        String result = "";
        try {
            if (this.login()) {
                // 打开一个会话
                Session session = conn.openSession();
                session.execCommand(cmds);
                in  = new StreamGobbler(session.getStdout());
                InputStream error  = new StreamGobbler(session.getStderr());
                result = this.processStdout(in, this.charset);
                String errorStr = this.processStdout(error, this.charset);
                System.out.println(errorStr);
                session.close();
                conn.close();
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return result;
    }

    public String processStdout(InputStream in, String charset) throws IOException {

        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String line;
        while (true)
        {
             line = br.readLine();
            if (line == null){
                break;
            }

            System.out.println(line);
        }

        return line;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {

    }
}
