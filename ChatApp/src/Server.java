import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.*;

public class Server {
    private static final int PORT = 12345;
    private static final String URL = "jdbc:mysql://localhost:3306/school";
    private static final String USER = "root";
    private static final String PASS = "";
    
    private static final Set<ClientHandler> clients = Collections.synchronizedSet(new HashSet<>());
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            Class.forName("com.mysql.cj.jdbc.Driver");
            while (true) {
                Socket socket = serverSocket.accept();
                ClientHandler handler = new ClientHandler(socket);
                clients.add(handler);
                handler.start(); 
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void broadcast(String message, String username, boolean isFile) {
        try{
            Connection conn = DriverManager.getConnection(URL, USER, PASS);
            
            PreparedStatement pstmt = conn.prepareStatement("INSERT INTO messages (username, message_content, is_file) VALUES (?, ?, ?)");
            pstmt.setString(1, username);
            pstmt.setString(2, message);
            pstmt.setInt(3, isFile ? 1 : 0);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String file = (isFile ? "FILE" : "MSG") + "|" + username + "|" + message;
        synchronized (clients) {
            for (ClientHandler client : clients) {
                client.sendMessage(file);
            }
        }
    }

    private static class ClientHandler extends Thread {
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;
        private String username;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }
        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);
                username = in.readLine();
                sendChatHistory();
                String input;
                while ((input = in.readLine()) != null) {
                    if (input.startsWith("MSG|")) {
                        broadcast(input.substring(4), username, false);
                    } else if (input.startsWith("FILE|")) {
                        broadcast(input.substring(5), username, true);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                clients.remove(this);
                try { socket.close(); } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private void sendChatHistory() {
            String query = "SELECT username, message_content, is_file FROM messages ORDER BY timestamp ASC";
            try {
                Connection conn = DriverManager.getConnection(URL, USER, PASS);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query);

                while (rs.next()) {
                    String histUser = rs.getString("username");
                    String content = rs.getString("message_content");
                    boolean isFile = rs.getInt("is_file") == 1;
                    String prefix = isFile ? "HIST_FILE" : "HIST_MSG";
                    sendMessage(prefix + "|" + histUser + "|" + content);
                }
                conn.close();
            }catch (SQLException e) {
                e.printStackTrace();
            }
        }

        public void sendMessage(String packet) {
            if (out != null) out.println(packet);
        }
    }
}

// java --module-path "C:\javafx-sdk-21.0.2\lib" --add-modules javafx.controls -cp ".;lib/mysql-connector.jar" GUI
