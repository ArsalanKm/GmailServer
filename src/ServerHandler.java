import Model.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import static Model.MailBox.mailboxes;
import static Model.UserActions.Not_Online;

/**
 * @throws IOException
 * @throws ClassNotFoundException
 * @throws InterruptedException
 */

public class ServerHandler {
    private static List<User> LoggedInUsers = new ArrayList<>();
    private static List<User> RegisteredUsers = new ArrayList<>();
    private static List<OnlineUsers> onlineUsers = new ArrayList<>();
    private static List<Data> MessagesList = new ArrayList<>();
    private final String IMAGES_DIRECTORY = "C:\\Users\\asus\\Desktop\\Server\\src\\UsersImage\\";
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
            //when users Register to application
            case Register: {
                synchronized (RegisteredUsers) {
                    RegisteredUsers.add(message.getUser());

                }
                Thread.sleep(2000);
                ByteArrayInputStream bis = new ByteArrayInputStream(message.getUser().UserImageFile);
                BufferedImage bImage2 = ImageIO.read(bis);
                ImageIO.write(bImage2, "png", new File(IMAGES_DIRECTORY + message.getUser().getName()));
                System.out.println("image created");
                synchronized (AllUsers.All_REGISTERD_USER) {
                    AllUsers.All_REGISTERD_USER.add(message.getUser());
                }

                System.out.println(message.getUser().getUsername() + " register" + message.getUser().getImageUrl());
                System.out.println("time :" + message.getUser().getCurrentTimeAndDate());
                break;
            }
            //when users Sign in to application
            case SignIn: {
                synchronized (LoggedInUsers) {
                    LoggedInUsers.add(message.getUser());
                }
                OnlineUsers LoggedUser = new OnlineUsers(message.getUser(), outputStream, inputStream);
                LoggedUser.setUsername(message.getUser().getUsername());
                System.out.println(message.getUser().getUsername() + " " + message.getUser().getUserActions());
                System.out.println(message.getUser().getCurrentTimeAndDate());

                onlineUsers.add(LoggedUser);
                OnlineUsers.ONLINE_USERS.add(LoggedUser);
                System.out.println("online users size: " + OnlineUsers.ONLINE_USERS.size());


//                for (Data mailbox : mailboxes) {
//                for(int i=mailboxes.size()-1;i>=0;i--){
//                    if (mailboxes.get(i).getReciever().equals(message.getUser().getUsername())) {
//                        System.out.println("Reciever become online");
//                        LoggedUser.getUser().getOutputStream().writeObject(new Message(mailboxes.get(i), UserActions.Message));
//                       MessagesList.add(mailboxes.get(i));
//                        System.out.println("MAil Sender: " + mailboxes.get(i).getSender());
//                        System.out.println("Mail Reciever: " + mailboxes.get(i).getReciever());
//                        System.out.println("Mail Texr: " + mailboxes.get(i).getText());
//                        System.out.println("Message sent to user");
//                        MailBox.remove(mailboxes.get(i));
//
//                    }
//                }
//
//                mailboxes.removeAll(MessagesList);

                System.out.println("after remove all : mailboxe size: " + mailboxes.size());
                for (Data inbox : LoggedUser.getUser().Inbox) {
                    System.out.println(inbox.getText());
                }

                break;

            }
            //porotocol that checking occurences of users
            case Reply: {
                outputStream.writeObject(new Message("", "", "", UserActions.Reply, RegisteredUsers));
                outputStream.flush();
                System.out.println("Registerd List sent");
                break;
            }
            case Text: {
                boolean isOnline = false;
                boolean HasRegisterd = false;
                for (User user : AllUsers.All_ONLINE_USERS) {
                    if (user.getUsername().equals(message.getUser().getUsername())) {
                        user.getInbox().add(message.getData());
                        isOnline = true;
                        break;
                    }

                }
                if (isOnline == false) {

                    for (User user : AllUsers.All_REGISTERD_USER) {
                        if (user.getUsername().equals(message.getUser().getUsername())) {
                            user.getInbox().add(message.getData());
                            outputStream.writeObject(new Message(message.getSender(), message.getReciever(), "", Not_Online, message.getUser()));
                            outputStream.flush();
                            System.out.println("new Message that show user it is not online");
                            break;
                        }

                    }
                }


            }
            //Change Setting protocole
            case Change_Setting: {
                for (User user : RegisteredUsers) {
                    if (user.getUsername().equals(message.getUser().getUsername())) {
                        user = message.getUser();
                        ByteArrayInputStream bis = new ByteArrayInputStream(message.getUser().UserImageFile);
                        BufferedImage bImage2 = ImageIO.read(bis);
                        ImageIO.write(bImage2, "png", new File(IMAGES_DIRECTORY + "New_Image" + message.getUser().getName()));
                        System.out.println("image created");
                        break;
                    }

                }
            }
            //Chat Protocole
            case Message: {
                message(message, outputStream);
                break;
            }
            case Log_Out: {
                // This semaphore is for controlling the access to the online users list .
                Semaphore semaphore = new Semaphore(1);
                semaphore.acquire();


                for (OnlineUsers onlineUser : OnlineUsers.ONLINE_USERS) {
                    if (message.getUser().getUsername().equals(onlineUser.getUsername())) {
                        int index = OnlineUsers.ONLINE_USERS.indexOf(onlineUser);
                        System.out.println(index);
                        System.out.println("request sender: " + message.getUser().getUsername());
                        System.out.println(onlineUser.getUsername());
                        OnlineUsers.ONLINE_USERS.remove(onlineUser);
                        break;
                    }
                }
                semaphore.release();

                System.out.println("Online users size in logout side  : " + OnlineUsers.ONLINE_USERS.size());

            }
            case Refresh:{
                for (User user:RegisteredUsers) {
                    if(user.equals(message.getUser())){
                        outputStream.writeObject(new Message (UserActions.Refresh, user.Inbox,user.ConversationsList));
                   outputStream.flush();
                    }
                }
            }
        }
    }

    public void message(Message message, ObjectOutputStream outputStream) throws IOException {
        boolean ReciverIsOnline = false;
        boolean ReciverIsValid = false;
        OnlineUsers Sender = null;
        OnlineUsers Reciver = null;

        mailboxes.add(message.getData());
        //finding sender
        for (OnlineUsers onlineUser : OnlineUsers.ONLINE_USERS) {
            if (onlineUser.getUsername().equals(message.getData().getSender())) {
                Sender = onlineUser;
            }
        }
        //finding reciever
        for (OnlineUsers onlineUser : OnlineUsers.ONLINE_USERS) {
            if (onlineUser.getUsername().equals(message.getData().getReciever())) {
                Reciver = onlineUser;
                ReciverIsOnline = true;
                ReciverIsValid = true;
            }
        }
        //checking valid

        for (User allUser : RegisteredUsers) {
            if (allUser.getUsername().equals(message.getData().getReciever())) {
                ReciverIsValid = true;
            }
        }
        if (!ReciverIsValid) {

            BufferedImage image = ImageIO.read(new File("C:\\Users\\asus\\Desktop\\Server\\src\\Recources\\UserIcon.png"));
            ByteArrayOutputStream outputStream1 = new ByteArrayOutputStream();
            ImageIO.write(image, "png", outputStream1);
            byte[] bufferedImage = outputStream1.toByteArray();
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

            LocalDateTime now = LocalDateTime.now();
            Data data = new Data("mailerdaemon@googlemail.com", message.getData().getSender(), "Mail Delivery Subsystem", "your target account doesnt exit dear", dtf.format(now), bufferedImage);
            for (User user : RegisteredUsers) {
                if (user.getUsername().equals(message.getData().getSender())) {
                    user.Inbox.add(data);
                    user.ConversationsList.add(data);

                }

            }


            mailboxes.remove(message.getData());
            System.out.println("Message sent from mail delivery system . ");
            System.out.println("Mail box size : " + mailboxes.size());

        }  // sending the message to the online user  .

        else if (ReciverIsOnline) {
            System.out.println("user was online and message sent to the user successfully . ");
            Reciver.getUser().getOutputStream().writeObject(new Message(message.getData()));

            for (User user : RegisteredUsers) {
                if (user.getUsername().equals(message.getData().getReciever())) {
                    user.Inbox.add(message.getData());
                    user.ConversationsList.add(message.getData());
                }

            }

            MailBox.remove(message.getData());
            System.out.println("recieve is online : mail box size: " + mailboxes.size());
        }

        // keep the message in mail box till the user become online  .

        else if (!ReciverIsOnline) {

            for (User user : RegisteredUsers) {
                if (user.getUsername().equals(message.getData().getReciever())) {
                    user.Inbox.add(message.getData());
                    user.ConversationsList.add(message.getData());
                }

            }
            for (User user : RegisteredUsers) {
                System.out.println(user.Inbox.size());
                System.out.println("Registerd user Conversation size :  1" + user.ConversationsList.size());

            }
            System.out.println("User is not online and server will keep the message till user become online . ");
            System.out.println("MailBox size : " + MailBox.mailboxes.size());

        }


    }
}
