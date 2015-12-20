
import java.io.*;
import java.net.Socket;

/**
 * Created by hua on 15-12-20.
 */
public class Client {
        private Socket socket = null;
        private BufferedReader in = null;
        private PrintWriter out = null;

        static final int PORT = 8001;
        static final String ERRCODE = "ERRCODE";
        static final String CONN_SUCCESS = "CONN_SUCCESS";
        static final String HOST = "127.0.0.1";

        static final int CONN_SER_CLS = -2;
        static final int CONN_ERR = -1;
        static final int CONN_OK = 0;
        static final int RECV_MSG = 1;
        static final int INPUT_ERR = -3;


        public final static void main(String[] args){
            Client client = new Client();
            client.startClient();
        }

        public void sendMessage(String msg){
            if (socket != null && socket.isConnected()) {
                if (!socket.isOutputShutdown()) {
                    out.println(msg);
                }
            }
        }



        public void startClient(){
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (socket == null || socket.isClosed()) {
                            socket = new Socket(HOST, PORT);
                            in = new BufferedReader(new InputStreamReader(socket
                                    .getInputStream()));
                            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
                                    socket.getOutputStream())), true);
                        }

                        while (true) {
                            if (!socket.isClosed()) {
                                if (socket.isConnected()) {
                                    if (!socket.isInputShutdown()) {
                                        String content = null;
                                        if ((content = in.readLine()) != null) {
                                            content += "\n";
                                        } else {

                                        }
                                    }
                                }
                            }
                        }

                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            });
            thread.start();
        }

}
