import java.io.*;
import java.util.ArrayList;
import java.util.List;

import Model.User;

import javax.swing.*;

public class AllUsers implements Serializable {
    public static final String FILEFROMSERVER = "C:\\Users\\asus\\Desktop\\Server\\src\\users.txt";
    static final long serialVersionUID = 1222L;
    public static FileInputStream fileIn;
    public static FileOutputStream fileOut;
    public static ObjectInputStream in;
    public static ObjectOutputStream out;
    public static final String SERVER_DBROOT = "C:\\Users\\asus\\Desktop\\Server\\src\\Database\\Users.ser";
public static List<String> USERNAME_LIST=new ArrayList<>();
    public static List<User> All_ONLINE_USERS = new ArrayList<>();
    public static List<User> All_REGISTERD_USER = new ArrayList<>();

    public static void updateList() throws IOException, ClassNotFoundException {
//        synchronized (MUTE){
        boolean emptyList = false;
        File file = new File(SERVER_DBROOT);
        if (file.length() == 0) emptyList = true;

        if (!emptyList) {
            fileIn = new FileInputStream(SERVER_DBROOT);
            in = new ObjectInputStream(fileIn);

            All_REGISTERD_USER = (List<User>) in.readObject();
            System.out.println(All_REGISTERD_USER.size());

            fileIn.close();
            in.close();

        }

//        }
        System.out.println("ALL USERS LIST updated successfully .");


    }

    public static void saveUsers() throws IOException {
//        synchronized (MUTE){
        FileOutputStream fileIn = new FileOutputStream(SERVER_DBROOT);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileIn);
        objectOutputStream.writeObject(ServerHandler.RegisteredUsers);
        objectOutputStream.flush();

        fileIn.close();
        objectOutputStream.close();


        System.out.println("Users saved successfully");
    }

//    }



}