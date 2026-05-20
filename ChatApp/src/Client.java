import java.io.*;
import java.net.*;

public class Client {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private Message message; 

    public void connect(String host, int port, String username, Message message) throws IOException {
        this.message = message;
        this.socket = new Socket(host, port);
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(socket.getOutputStream(), true);
        out.println(username);
        new Thread(() -> {
            try {
                String response;
                while ((response = in.readLine()) != null) {
                    if (message != null) {
                        message.onMessageReceived(response);
                    }
                }
            } catch (IOException e) {
            
            }
        }).start();
    }

    public void sendTextMessage(String msg) {
        if (out != null) {
            out.println("MSG|" + msg);
        }
    }

    public void sendFileNotification(String fileName) {
        if (out != null) {
            out.println("FILE|" + fileName);
        }
    }

    public void disconnect() {
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            
        }
    }
}