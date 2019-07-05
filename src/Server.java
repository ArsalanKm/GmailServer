import Model.Message;
import Model.User;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class Server implements Runnable {
    public static final int requestPort = 8888;
    public static final String serverIp = "localhost";
    private static ServerSocket requestServerSocket;
    public static final String SERVER_DBROOT="C:\\Users\\asus\\Desktop\\Server\\src\\Database\\Users.ser";
    public static void updateUsernames() throws IOException, ClassNotFoundException {
        Object MUTEX_USERNAME = new Object();
        synchronized (MUTEX_USERNAME) {


            FileInputStream fileInputStream = new FileInputStream(new File("src\\Database\\Username.ser"));
            File file=new File("src\\Database\\Username.ser");
            if(file.length()>0) {
              ObjectInputStream  objectInputStream = new ObjectInputStream(fileInputStream);

                ServerHandler.USERNAME_LIST = (List<String>) objectInputStream.readObject();

                fileInputStream.close();
                objectInputStream.close();
            }
        }
    }
    public static boolean UpdateAllusersList() throws IOException, ClassNotFoundException {

        if(AllUsers.USERNAME_LIST.size() == 0) return true ;
        for (String username : AllUsers.USERNAME_LIST) {
            int split = username.lastIndexOf("@");
            String directoryPath =  username.substring(0 , split);

            FileInputStream inputStream = new FileInputStream(new File( SERVER_DBROOT+ directoryPath + "\\" + directoryPath +  ".ser"));
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

            ServerHandler.RegisteredUsers.add((User) objectInputStream.readObject());
            System.out.println("All users : " + ServerHandler.RegisteredUsers);

            inputStream.close();
            objectInputStream.close();


        }

        return  true ;
    }
    public static void main(String[] args) throws IOException, ClassNotFoundException {
//        updateUsernames();
//        UpdateAllusersList();
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
//            }
        } catch (EOFException e) {
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}


