import Model.Message;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable {
    public static final int requestPort = 8888;
    public static final String serverIp = "localhost";
    private static ServerSocket requestServerSocket;

    public static void main(String[] args) throws IOException {
        Server.start();
    }

    public static void start() throws IOException {

        requestServerSocket = new ServerSocket(requestPort);
        Thread serverThread = new Thread(new Server(), "ServerThread");
        serverThread.start();
    }
    @Override
    public void run() {
        while (!requestServerSocket.isClosed()) {
            try {
                new Thread(new ServerRunner(requestServerSocket.accept())).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

class ServerRunner implements Runnable {

    private Socket serverSocket;
    private ServerHandler serverHandler;

    public ServerRunner(Socket serverSocket) {
        this.serverSocket = serverSocket;
    }

    @Override
    public void run() {
        Message message;
        try {
            serverHandler = new ServerHandler(serverSocket,
                    new ObjectInputStream(serverSocket.getInputStream()),
                    new ObjectOutputStream(serverSocket.getOutputStream()));
            while (true) {
                message = (Message) serverHandler.getInputStream().readObject();
                serverHandler.handle(message);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}


