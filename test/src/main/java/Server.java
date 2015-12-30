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
                PrintWriter pout = null;
                try {
                    pout = new PrintWriter(new BufferedWriter(
                            new OutputStreamWriter(getSocket().getOutputStream())), true);
                    pout.println(msg);
                } catch (IOException e) {
                    e.printStackTrace();
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
