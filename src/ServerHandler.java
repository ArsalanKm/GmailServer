


import Model.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import static Model.UserActions.*;

public class ServerHandler {
    private static List<User> LoggedInUsers = new ArrayList<>();
    private static List<User> RegisteredUsers = new ArrayList<>();
private final String IMAGES_DIRECTORY="C:\\Users\\asus\\Desktop\\Server\\src\\UsersImage\\";
    private Socket socket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private User TempUser;

    public ServerHandler(Socket socket, ObjectInputStream inputStream, ObjectOutputStream outputStream) {
        this.socket = socket;
        this.inputStream = inputStream;
        this.outputStream = outputStream;
    }

    public ObjectInputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(ObjectInputStream inputStream) {
        this.inputStream = inputStream;
    }

    public ObjectOutputStream getOutputStream() {
        return outputStream;
    }

    public void setOutputStream(ObjectOutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public void handle(Message message) throws IOException, ClassNotFoundException, InterruptedException {


        switch (message.getUserActions()) {
            case Register: {
                synchronized (RegisteredUsers) {
                    RegisteredUsers.add(message.getUser());

                }
                Thread.sleep(2000);
                ByteArrayInputStream bis = new ByteArrayInputStream(message.getUser().UserImageFile);
                BufferedImage bImage2 = ImageIO.read(bis);
                ImageIO.write(bImage2, "png", new File(IMAGES_DIRECTORY+message.getUser().getName()) );
                System.out.println("image created");
                synchronized (AllUsers.All_REGISTERD_USER) {
                    AllUsers.All_REGISTERD_USER.add(message.getUser());
                }

                System.out.println(message.getUser().getUsername() + " register" + message.getUser().getImageUrl());
                System.out.println("time :" + message.getUser().getCurrentTimeAndDate());
                break;
            }
            case SignIn: {
                synchronized (LoggedInUsers) {
                    LoggedInUsers.add(message.getUser());
                }
                synchronized (AllUsers.All_ONLINE_USERS) {
                    AllUsers.updateList();

                    AllUsers.addUser(message.getUser());
                }
                OnlineUsers onlineUser=new OnlineUsers(message.getUser());
                System.out.println(message.getUser().getUsername() + " " + message.getUser().getUserActions());
                System.out.println(message.getUser().getCurrentTimeAndDate());
                break;

            }
            case Reply: {
                outputStream.writeObject(new Message("", "", "", UserActions.Reply, RegisteredUsers));
                outputStream.flush();
                System.out.println("Registerd List sent");
                break;
            }
            case Text: {
                boolean isOnline = false;
                boolean HasRegisterd=false;
                for (User user : AllUsers.All_ONLINE_USERS) {
                    if (user.getUsername().equals(message.getUser().getUsername())) {
                        user.getInbox().add(message.getDate());
                        isOnline = true;
                        break;
                    }

                }
                if (isOnline == false) {

                    for (User user : AllUsers.All_REGISTERD_USER) {
                        if (user.getUsername().equals(message.getUser().getUsername())) {
                            user.getInbox().add(message.getDate());
                            outputStream.writeObject(new Message(message.getSender(), message.getReciever(), "", Not_Online, message.getUser()));
                            outputStream.flush();
                            System.out.println("new Message that show user it is not online");
                            break;
                        }

                    }
                }


            }
            case Change_Setting:{
                for(User user :RegisteredUsers){
                    if(user.getUsername().equals(message.getUser().getUsername())){
                      user=message.getUser();
                        ByteArrayInputStream bis = new ByteArrayInputStream(message.getUser().UserImageFile);
                        BufferedImage bImage2 = ImageIO.read(bis);
                        ImageIO.write(bImage2, "png", new File(IMAGES_DIRECTORY+"New_Image"+message.getUser().getName()) );
                        System.out.println("image created");
                    }
                    break;
                }
            }
        }
    }
}
