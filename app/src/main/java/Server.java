import com.sansheng.testcenter.bean.A;
import com.sansheng.testcenter.bean.C;
import com.sansheng.testcenter.bean.UserData;
import com.sansheng.testcenter.tools.protocol.TerProtocolCreater;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hua on 12/18/15.
 */
public class Server {
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
                        sendmsg(socket.getInetAddress().toString(),msg,true);
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
    public void sendmsg(String ip,String msg,boolean all) {
        for(ConnClient client : clientList){
            if(all || (client.connStatus && equals(client.clientIP))) {
                client.sendmsg(msg);
            }
        }
    }
    public static class ConnClient implements Runnable{
        public String clientName;
        public String clientIP;
        public boolean connStatus = false;
        private Socket mSocket;
        private BufferedReader serin = null;
        public Socket getSocket(){
            return mSocket;
        }
        public ConnClient(Socket socket){
            mSocket = socket;
            connStatus = true;
            try {
                serin = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
                connStatus = false;
            }
        }

        public void sendmsg(String msg) {
            if(connStatus) {
                BufferedOutputStream pout = null;
                try {

                    pout = new BufferedOutputStream(getSocket().getOutputStream());
                    TerProtocolCreater creater = new TerProtocolCreater();
                    while(true) {
                        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                        String str = br.readLine();
                        System.out.println("you input cmd is "+str);
                        if(!str.equalsIgnoreCase("end")) {
                            TerProtocolCreater clazz = new TerProtocolCreater();
                            //68 49 00 49 00 68
                            // C: 4A
                            // A : 10 12 64 00 02
                            // AFN:0C
                            // SEQ:F0
                            // DA : 00 00
                            // DT:01 00
                            // suerdata:00 35 24 09 25 00
                            // 56 16
                            C c =new C(false,true,false,false,10);
                            A a = new A(4114,25600,true,1);
                            UserData u = new UserData();
                            u.setAFN("0C");
                            u.setSEQ("F0");
                            u.setDataUnitTip_DA1("00");
                            u.setDataUnitTip_DA2("00");
                            u.setDataUnitTip_DT1("01");
                            u.setDataUnitTip_DT2("00");
                            u.setDataUnit("00 35 24 09 25 00".replace(" ",""));
                            byte[] data = clazz.makeCommand(a,c ,u);
                            pout.write(data);
                            pout.flush();
                        }else{
                            System.out.println("we will quit ====> ...");
                            System.exit(0);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                }
            }
        }

        @Override
        public void run() {
            try {
                while (true) {
                    String msg="";
                    if ((msg = serin.readLine()) != null) {
                        System.out.println(" by hua client info is "+msg);
                        if (msg.equals("0x68 0x16")) {
                            msg = "0x16 0x68";
                        } else {
                            msg = "ERRCODE";
                        }
                        sendmsg(msg);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
