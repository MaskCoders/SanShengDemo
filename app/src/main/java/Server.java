import com.sansheng.testcenter.bean.WhmBean;
import com.sansheng.testcenter.tools.protocol.ProtocolUtils;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hua on 12/18/15.
 */
public class Server{
    private Socket socket = null;
    private BufferedReader in = null;
    private PrintWriter out = null;
    private ServerSocket server = null;
    static final int PORT = 8001;
    static final String ERRCODE = "ERRCODE";
    static final String CONN_SUCCESS = "CONN_SUCCESS";
    static final String HOST = "127.0.0.1";

    static final int CONN_SER_CLS = -2;
    static final int CONN_ERR = -1;
    static final int CONN_OK = 0;
    static final int RECV_MSG = 1;
    static final int INPUT_ERR = -3;

    //    Socket client = null;
    private ArrayList<ConnClient> clientList = new ArrayList<ConnClient>();

    public static final void main(String[] args){
        Server server = new Server();
        server.startServer();
    }
    public void startServer() {
        System.out.println(" by hua start server ... ");
//        if(server != null && server.isBound() ){
//            Toast.makeText(this, this.getResources().getString(R.string.start_ser_tip3), 0).show();
//        }
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    server = new ServerSocket(PORT);

                    while (true) {
                        Socket socket = server.accept();
                        ConnClient client = new ConnClient(socket);
                        clientList.add(client);
                        System.out.println(" by hua client conn ... ");
                        String msg = CONN_SUCCESS + socket.getInetAddress();
                        sendmsg(socket.getInetAddress().toString(),msg.getBytes(),true);
                        (new Thread(client)).start();

                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();


    }
    public List<ConnClient> getClients(){
        return clientList;
    }
    public void sendmsg(String ip,byte[] data,boolean all) {
        for(ConnClient client : clientList){
            if(all || (client.connStatus && equals(client.clientIP))) {
                client.sendmsg(data);
            }
        }
    }
    private static class ConnClient implements Runnable{
        public String clientName;
        private long receiveTimeDelay=61000;
        public String clientIP;
        public boolean connStatus = false;
        private Socket mSocket;
        private DataInputStream serin = null;
        public Socket getSocket(){
            return mSocket;
        }
        boolean run=true;
        long lastReceiveTime = System.currentTimeMillis();
        public ConnClient(Socket socket){
            mSocket = socket;
            connStatus = true;
            try {
                serin = new DataInputStream(mSocket.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
                connStatus = false;
            }
        }

        public void sendmsg(byte[] data) {
            if(connStatus) {
                DataOutputStream pout = null;
                try {
                    pout = new DataOutputStream(getSocket().getOutputStream());
                    try {
                        pout.write(data);
                        pout.write('\n');
                        pout.flush();
                    } catch (Exception e) {
                        e.printStackTrace();
                        overThis();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    overThis();

                }
            }
        }

        private void overThis() {
            if(run)run=false;
            if(mSocket!=null){
                try {
                    mSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("关闭："+mSocket.getRemoteSocketAddress());
        }
        @Override
        public void run() {
            try {
                while (run) {
                    long time = System.currentTimeMillis()-lastReceiveTime;
                    lastReceiveTime = System.currentTimeMillis();
                    if(time>receiveTimeDelay){
                        System.out.println(" over times "+time);
                        overThis();
                    }else {
                        System.out.println(" read data ,,, ");
                        String msg = "";
                        List<Byte> bytearr = new ArrayList<Byte>();
                        int b = serin.read();
                        while (true) {
                            if (b == '\n' || b == '\r' || b == -1) {
                                break;
                            } else {
                                bytearr.add((byte) b);
                            }
                            b = serin.read();
                        }
                        byte[] orgb = new byte[bytearr.size()];
                        for (int i = 0; i < bytearr.size(); i++) {
                            orgb[i] = bytearr.get(i);
                        }
                        String recvCmd = ProtocolUtils.printByte(orgb);
                        String sendCmd = "";
                        if (recvCmd.trim().equalsIgnoreCase("68 12 00 00 00 10 20 68 11 04 66 65 67 66 bf 16".trim())) {
                            sendCmd = "68 12 00 00 00 10 20 68 91 18 33 32 34 33 67 5C 33 33 99 3A 33 33 48 39 33 33 B3 37 33 33 A4 43 33 33 5D 16".replace(" ", "");
                        }
                        else if (recvCmd.trim().equalsIgnoreCase("68 AA AA AA AA AA AA 68 13 00 DF 16".trim())) {
                            sendCmd = "68 12 00 00 00 10 20 68 93 06 45 33 33 33 43 53 1F 16".replace(" ", "");
                        }
                        else if (recvCmd.trim().equalsIgnoreCase("68 12 00 00 00 10 20 68 11 04 33 32 34 33 F3 16".trim())) {
                            //正向有功电能 1.29, 0.62, 0.6, 0.03, 0.02
                            sendCmd = "68 12 00 00 00 10 20 68 91 18 33 32 34 33 5C 34 33 33 95 33 33 33 93 33 33 33 36 33 33 33 35 33 33 33 74 16".replace(" ", "");
                        }
                        else if (recvCmd.trim().equalsIgnoreCase("68 12 00 00 00 10 20 68 11 04 33 32 34 35 F5 16".trim())) {
                            //三相电压: 225.6, -1, -1
                            sendCmd = "68 12 00 00 00 10 20 68 91 0A 33 32 34 35 89 55 32 32 32 32 21 16".replace(" ", "");
                        }
                        else if (recvCmd.trim().equalsIgnoreCase("68 12 00 00 00 10 20 68 11 04 34 34 33 37 F9 16".trim())) {
                            //日期: 2016-01-11 星期01
                            sendCmd = "68 12 00 00 00 10 20 68 91 08 34 34 33 37 34 44 34 49 72 16".replace(" ", "");
                        }
                        else if (recvCmd.trim().equalsIgnoreCase("68 12 00 00 00 10 20 68 11 04 35 34 33 37 FA 16".trim())) {
                            //时间: 21:53:55
                            sendCmd = "68 12 00 00 00 10 20 68 91 07 35 34 33 37 88 86 54 DF 16".replace(" ", "");
                        }
                        else if (recvCmd.trim().equalsIgnoreCase("68 12 00 00 00 10 20 68 11 04 34 33 39 38 FF 16".trim())) {
                            //上2次日冻结时间: 2015-10-9 0:00:00
                            sendCmd = "68 12 00 00 00 10 20 68 91 09 34 33 39 38 33 33 3C 43 48 B1 16".replace(" ", "");
                        }
                        else if (recvCmd.trim().equalsIgnoreCase("68 12 00 00 00 10 20 68 11 04 33 35 C3 33 85 16".trim())) {
                            //剩余金额: 198.56
                            sendCmd = "68 12 00 00 00 10 20 68 91 08 33 35 C3 33 89 CB 34 33 C4 16".replace(" ", "");
                        }
                        else if (recvCmd.trim().equalsIgnoreCase("68 12 00 00 00 10 20 68 11 04 33 33 34 36 F7 16".trim())) {
                            //ERR2 无请求数据
                            sendCmd = "68 12 00 00 00 10 20 68 D1 01 35 19 16".replace(" ", "");
                        }
                        else if (recvCmd.trim().equalsIgnoreCase("68 12 00 00 00 10 20 68 11 04 33 40 63 36 33 16".trim())) {
                            //开盖次数: 27
                            sendCmd = "68 12 00 00 00 10 20 68 91 07 33 40 63 36 5A 33 33 76 16".replace(" ", "");
                        }
                        else if (recvCmd.trim().equalsIgnoreCase("68 12 00 00 00 10 20 68 11 04 34 33 33 50 11 16".trim())) {
                            //跳闸次数: 0
                            sendCmd = "68 12 00 00 00 10 20 68 91 07 34 33 33 50 33 33 33 2D 16".replace(" ", "");
                        }
                        else if (new String(orgb).contains("keepalive")) {
                            System.out.println("keepalive");
                            sendmsg("keepalive".getBytes());
                            continue;
                        } else {
                            sendmsg("can not read cmd".getBytes());
                            continue ;
                        }
                        //68 02 00 00 00 10 20 68 11 04 66 65 67 66 af 16
                        WhmBean bean = WhmBean.parse(ProtocolUtils.
                                hexStringToBytes(sendCmd));

                        sendmsg(ProtocolUtils.hexStringToBytes(bean.toString()));
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
                overThis();
            }
        }
    }
}
