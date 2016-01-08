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
public class Server1{
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
        Server1 server = new Server1();
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
        public String clientIP;
        public boolean connStatus = false;
        private Socket mSocket;
        private DataInputStream serin = null;
        public Socket getSocket(){
            return mSocket;
        }
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

//        public void sendmsg(String msg) {
//            if(connStatus) {
//                PrintWriter pout = null;
//                try {
//                    pout = new PrintWriter(new BufferedWriter(
//                            new OutputStreamWriter(getSocket().getOutputStream())), true);
//                    pout.println(msg);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
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

        @Override
        public void run() {
            try {
                while (true) {
                    String msg="";
                    List<Byte> bytearr= new ArrayList<Byte>();
                    int b = serin.read();
                    while(true){
                        if (b == '\n' || b == '\r' || b== -1){
                            break;
                        }else{
                            System.out.println(b);
                            bytearr.add((byte) b);
                        }
                        b = serin.read();
                    }
                    byte[] orgb = new byte[bytearr.size()];
                    for(int i=0;i<bytearr.size();i++ ){
                        orgb[i]=bytearr.get(i);
                    }
                    ProtocolUtils.printByte(orgb);
                    WhmBean bean = WhmBean.parse(ProtocolUtils.
                            hexStringToBytes("68 02 00 00 00 10 20 68 91 18 33 32 34 33 67 5C 33 33 99 3A 33 33 48 39 33 33 B3 37 33 33 A4 43 33 33 5B 16".replace(" ","")));
                                            //68 02 00 00 00 10 20 68 91 18 33 32 34 33 67 5c 33 33 99 3a 33 33 48 39 33 33 b3 37 33 33 a4 43 33 33 5b 16
                    sendmsg(ProtocolUtils.hexStringToBytes(bean.toString()));

//                    if ((msg = serin.readLine()) != null) {
////                        System.out.println(" by hua client info is "+msg);
//                        byte[] b11 = ProtocolUtils.hexStringToBytes("68 02 00 00 00 10 20 68 11 04 33 32 34 33 e1 16".replace(" ",""));
//                        //                                           68 02 00 00 00 10 20 68 11 04 33 32 34 33 ef bf bd 16
//                        String sendMsg="";
//                        if(new String(b11).equals(msg)){
//                            WhmBean bean = WhmBean.parse(ProtocolUtils.hexStringToBytes("68 02 00 00 00 10 20 68 91 18 33 32 34 33 67 5C 33 33 99 3A 33 33 48 39 33 33 B3 37 33 33 A4 43 33 33 5B 16".replace(" ","")));
//
//                            sendMsg = new String(ProtocolUtils.hexStringToBytes(bean.toString()));
//                        }else{
//                            System.out.println("false");
//
//                        }
////                        "68 02 00 00 00 10 20 68 11 04 33 32 34 33 e1 16".getBytes();
////                        if (msg.equals("0x68 0x16")) {
////                            msg = "0x16 0x68";
////                        } else {
////                            msg = "ERRCODE";
////                        }
//                        sendmsg(sendMsg);
//                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
