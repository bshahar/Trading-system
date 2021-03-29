
import java.net.*;
import java.io.*;

class Server {

    private ServerSocket serverSocket;
    private static TradingSystem tradingSystem;

    public void start(int port) {
        tradingSystem = new TradingSystem(null);
        try {
            System.out.println("server started");
            serverSocket = new ServerSocket(port);
            while (true)
                new ClientHandler(serverSocket.accept()).start();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            stop();
        }

    }

    public void stop() {
        try {

            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static class ClientHandler extends Thread {
        private Socket clientSocket;
        private PrintWriter out;
        private BufferedReader in;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        public void run() {
            try {
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    String action = inputLine.substring(0, inputLine.indexOf(' '));
                    switch (action)
                    {
                        case "LOGIN":
                            tradingSystem.login(inputLine);
                            break;
                        case "REGISTER":
                            tradingSystem.register(inputLine);
                            break;
                    }
                    if (".".equals(inputLine)) {
                        out.println("bye");
                        break;
                    }
                    System.out.println(inputLine);
                    out.println(inputLine);
                }

                in.close();
                out.close();
                clientSocket.close();

            } catch (IOException e) {

            }
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.start(5555);
    }

}