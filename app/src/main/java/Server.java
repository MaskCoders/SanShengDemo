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
                    }
                } catch (Exception e) {
                    e.printStackTrace();
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
                                System.out.println(b);
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
                        if (recvCmd.trim().equalsIgnoreCase("68 02 00 00 00 10 20 68 11 04 66 65 67 66 af 16".trim())) {
                            sendCmd = "68 02 00 00 00 10 20 68 91 18 33 32 34 33 67 5C 33 33 99 3A 33 33 48 39 33 33 B3 37 33 33 A4 43 33 33 5D 16".replace(" ", "");
                            String st = new String(orgb);
                        }else if (new String(orgb).contains("keepalive")) {
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
