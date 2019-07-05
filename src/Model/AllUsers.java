package Model;





import java.io.*;
import java.util.ArrayList;
import java.util.List;
public class AllUsers implements Serializable{
    public static final String FILEFROMSERVER = "C:\\Users\\asus\\Desktop\\GmailMainProject\\FileFromServeSide\\users.txt";
    static final long serialVersionUID = 1222L;
    public static FileInputStream fileIn;
    public static FileOutputStream fileOut;
    public static ObjectInputStream in;
    public static ObjectOutputStream out;

    public static List<User> All_ONLINE_USERS = new ArrayList<>();
    public static List<User> All_REGISTERD_USER = new ArrayList<>();

    public static void updateList() throws IOException, ClassNotFoundException {
        boolean emptyList = false;
        File file = new File(FILEFROMSERVER);
        if (file.length() == 0) emptyList = true;

        if (!emptyList) {
            fileIn = new FileInputStream(FILEFROMSERVER);
            in = new ObjectInputStream(fileIn);

            All_ONLINE_USERS = (List<User>) in.readObject();


            fileIn.close();
            in.close();

        }


    }

    public static void addUser(User user) throws IOException, ClassNotFoundException {

        updateList();
        boolean beenInTheList = false;
        fileOut = new FileOutputStream(FILEFROMSERVER);
        out = new ObjectOutputStream(fileOut);


        for (User check : All_ONLINE_USERS) {
            if (user.getUsername().toLowerCase().equals(check.getUsername().toLowerCase()))
                beenInTheList = true;
        }

        if (!
                beenInTheList)
            All_ONLINE_USERS.add(user);


        out.writeObject(All_ONLINE_USERS);
        out.flush();

        fileOut.close();
        out.close();

    }

}