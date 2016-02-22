package com.example.mina.test3;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.channels.SocketChannel;

/**
 * Created by hua on 2/22/16.
 */
public class Client2 {
    /**
     * 非NIO client 连接  Mina NIO server
     */
    public static void main(String[] args) {

    }
    public static void clienttow(){
        try {
            SocketChannel socketChannel = SocketChannel.open();
            InetSocketAddress ia = new InetSocketAddress("127.0.0.1",20087);
            socketChannel.connect(ia);
            System.out.println("与服务器的连接建立成功");
            Socket socket = socketChannel.socket();
            if(socket.isConnected() && !socket.isClosed()){
                OutputStream os = socket.getOutputStream();
                DataOutputStream dos = new DataOutputStream(os);
                dos.write("hello world2".getBytes());
                dos.flush();

                System.out.println("C->S: 发送完毕");

                InputStream is = socket.getInputStream();
                DataInputStream dis = new DataInputStream(is);
                while(true){
                    byte[] bb = new byte[6];
                    dis.readFully(bb);
                    System.out.println("C<-S = "+new String(bb));
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public static void clientone(){
        try {
            InetAddress ipAddress  = InetAddress.getByName("127.0.0.1");
            Socket socket = new Socket(ipAddress, 20087);
            if(socket.isConnected() && !socket.isClosed()){
                OutputStream os = socket.getOutputStream();
                DataOutputStream dos = new DataOutputStream(os);
                dos.write("hello world2".getBytes());
                dos.flush();

                System.out.println("C->S: 发送完毕");

                InputStream is = socket.getInputStream();
                DataInputStream dis = new DataInputStream(is);
                while(true){
                    byte[] bb = new byte[6];
                    dis.readFully(bb);
                    System.out.println("C<-S = "+new String(bb));
                }
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
