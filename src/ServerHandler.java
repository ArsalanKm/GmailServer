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
 * Created by ArsalanKarimzad
 * ServerHandler class usese for Handle User APIS
 *
 * @throws IOException
 * @throws ClassNotFoundException
 * @throws InterruptedException
 */

public class ServerHandler {
    /**
     * List of Logged users persons for save them
     */
    public static List<User> LoggedInUsers = new ArrayList<>();
    /**
     * List of all Users that registered  to our application
     */
    public static List<User> RegisteredUsers = new ArrayList<>();


    private static List<OnlineUsers> onlineUsers = new ArrayList<>();
    private static List<Data> MessagesList = new ArrayList<>();
    private final String IMAGES_DIRECTORY = "C:\\Users\\asus\\Desktop\\Server\\src\\UsersImage\\";

    /**
     * Socket and inputStream and outputstream for writing over sockets
     */
    private Socket socket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private User TempUser;

    /**
     * This is a constructor that initialize socket ,inputstream,outputstream
     *
     * @param socket
     * @param inputStream
     * @param outputStream
     */

    public ServerHandler(Socket socket, ObjectInputStream inputStream, ObjectOutputStream outputStream) {
        this.socket = socket;
        this.inputStream = inputStream;
        this.outputStream = outputStream;
    }
    public ServerHandler(){}

    /**
     * To set input stream
     *
     * @return
     */
    public ObjectInputStream getInputStream() {
        return inputStream;
    }

    /**
     * a method that get message as a parameter and maybe throw these Exception
     *
     * @param message
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws InterruptedException
     */
    public void handle(Message message) throws IOException, ClassNotFoundException, InterruptedException {


        switch (message.getUserActions()) {
            //when users Register to application
            /**
             * Register API that server Handle this when users registers
             *
             */
            case Register: {
                synchronized (RegisteredUsers) {
                    RegisteredUsers.add(message.getUser());

                }
                Thread.sleep(2000);
                ByteArrayInputStream bis = new ByteArrayInputStream(message.getUser().UserImageFile);
                BufferedImage bImage2 = ImageIO.read(bis);
                ImageIO.write(bImage2, "png", new File(IMAGES_DIRECTORY + message.getUser().getName()));

                synchronized (AllUsers.All_REGISTERD_USER) {
                    AllUsers.All_REGISTERD_USER.add(message.getUser());
                }

                System.out.println(message.getUser().getUsername() + " register" + message.getUser().getImageUrl());
                System.out.println("time :" + message.getUser().getCurrentTimeAndDate());
                break;
            }
            //when users Sign in to application
            /**
             * SIGN IN API Server Handle this situation when users Logged in
             */
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


                break;

            }
            //showing forwarded messages information
            /**
             * Forward API : Server Handle this situation when a message forwarded to another user
             */
            case Forward: {
//                System.out.println(message.getUser().getUsername() + "  forward");
//                System.out.println("message:  " + message.getData().getSubject() + "  " + message.getData().getSendingFileName() + " from" + message.getData().getSender() + " TO" + message.getData().getReciever());
//                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
//                LocalDateTime now = LocalDateTime.now();
//                System.out.println(dtf.format(now));
                Forward(message);
                break;

            }
            //showing Replied messages informations
            /**
             * ReplyMessage that Server Handle this situation when Messages Reply
             */
            case ReplyMessage: {
//                System.out.println(message.getUser().getUsername() + "  Reply");
//                System.out.println("message: " + message.getData().getSubject() + "  " + message.getData().getSendingFileName() + "  to " + message.getData().getReciever());
//
//                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
//
//                LocalDateTime now = LocalDateTime.now();
//                System.out.println(dtf.format(now));
//
                ReplyMessage(message);
                break;
            }
            //Sending Registration list to client side to check new Logged in users
            /**
             * when we want to check the list of all users in registration panel and Sign in panel
             */
            case Reply: {
                outputStream.writeObject(new Message("", "", "", UserActions.Reply, RegisteredUsers));
                outputStream.flush();
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
                            break;
                        }

                    }
                }

                break;
            }
            //Change Setting protocole
            /**
             * this API update changes in user profile
             */
            case Change_Setting: {
//                for (User user : RegisteredUsers) {
//                    if (user.getUsername().equals(message.getUser().getUsername())) {
//                        user.setName(message.getNewuser().getName());
//                        user.setLastname(message.getNewuser().getLastname());
//                        user.setPassword(message.getNewuser().getPassword());
//                        user.setUserImageFile(message.getNewuser().getUserImageFile());
//                        user.setImageUrl(message.getNewuser().getImageUrl());
//
//                        outputStream.writeObject(new Message(user, UserActions.Change_Setting));
//                        outputStream.flush();
//                        ByteArrayInputStream bis = new ByteArrayInputStream(message.getNewuser().UserImageFile);
//                        BufferedImage bImage2 = ImageIO.read(bis);
//                        ImageIO.write(bImage2, "png", new File(IMAGES_DIRECTORY + "New_Image" + message.getUser().getName()));
//
//                        break;
//                    }
                ChangeSetting(message);

                break;
            }
            //chat things


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

                        System.out.println("request sender: " + message.getUser().getUsername());
                        System.out.println(onlineUser.getUsername());
                        OnlineUsers.ONLINE_USERS.remove(onlineUser);
                        break;
                    }
                }
                semaphore.release();

                break;

            }
            //Refresh Btm that send 4 users List to them and refresh them
            /**
             * Refresh API that send updated List when refresh Btm selected
             */
            case Refresh: {
//                for (User user : RegisteredUsers) {
//                    if (user.equals(message.getUser())) {
//                        outputStream.writeObject(new Message(UserActions.Refresh, user.Inbox, user.ConversationsList, user.Blocked_Users, user.Outbox, user.FavoritesBox));
//                        outputStream.flush();
//                    }
//                }
                Refresh(message);
                break;
            }

            /**
             * Block ApI that block selected users  and add them in to blocked list
             *
             */
            case Block: {

//                User TempUser = null;
//                for (User user : RegisteredUsers) {
//
//                    if (user.getUsername().equals(message.getData().getSender())) {
//                        user.setBlock(true);
//                        System.out.println(message.getUser().getUsername() + "  " + "Block" + message.getData().getSender());
//                        message.getUser().getBlocked_Users().add(user);
//                        TempUser = user;
//                    }
//                }
//                for (User user : RegisteredUsers) {
//                    if (user.getUsername().equals(message.getUser().getUsername())) {
//                        user.Blocked_Users.add(TempUser);
//                    }
//                    System.out.println(user.Blocked_Users.size());
//                }
//                for (User user : RegisteredUsers) {
//                    for (Data data : user.Inbox) {
//                        if (data.getSender().equals(message.getData().getSender())) {
//                            data.SenderIsBlocked = true;
//                        }
//                    }
//                }
                Block(message);
                break;
            }
            //UnBlocking Users
            /**
             * API that Unblock block users and return then in users panel
             */
            case UnBlock: {
//                User TempUser = null;
//                List<User> UnbLockList = new ArrayList<>();
//                for (User user : RegisteredUsers) {
//                    if (user.getUsername().equals(message.getUser().getUsername())) {
//                        for (User Blocked : user.Blocked_Users) {
//                            if (Blocked.getUsername().equals(message.getSender())) {
//                                TempUser = Blocked;
////                                user.Blocked_Users.remove(Blocked);
//                                Blocked.setBlock(false);
//                                UnbLockList.add(Blocked);
//                                System.out.println(user.getUsername() + "  UnBlocked  " + Blocked.getUsername());
//                                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
//                                LocalDateTime now = LocalDateTime.now();
//                                System.out.println(dtf.format(now));
//
//                            }
//                        }
//                        user.Blocked_Users.removeAll(UnbLockList);
//                    }
//
//                }
//                for (User user : RegisteredUsers) {
//                    if (user.getUsername().equals(message.getUser().getUsername())) {
//                        for (Data data : user.Inbox) {
//                            if (data.getSender().equals(message.getSender())) {
//                                data.SenderIsBlocked = false;
//                            }
//                        }
//                    }
//                }
                UnBlock(message);
                break;
            }
            /**
             * delete selected message from favorite list
             *
             */
            case DeleteFavoite: {
//                for (User user : RegisteredUsers) {
//                    if (user.getUsername().equals(message.getUser().getUsername())) {
//                        user.FavoritesBox.remove(message.getData());
//                        System.out.println(message.getUser() + " unimportant");
//                        System.out.println("message : " + message.getData().getSubject() + "  " + message.getData().getSender() + " as  unimportant ");
//                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
//                        LocalDateTime now = LocalDateTime.now();
//                        System.out.println(dtf.format(now));
//
//                    }
//                }
                DiFavoite(message);
                break;
            }
            /**
             * will add selected message to favorite messages list
             */
            case Add_Favorite: {
//                for (User user : RegisteredUsers) {
//                    if (user.getUsername().equals(message.getUser().getUsername())) {
//                        user.FavoritesBox.add(message.getData());
//                        System.out.println(message.getUser() + " important");
//                        System.out.println("message : " + message.getData().getSubject() + "  " + message.getData().getSender() + " as  important ");
//                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
//                        LocalDateTime now = LocalDateTime.now();
//                        System.out.println(dtf.format(now));
//                    }
//                }
                Add_Favorite(message);
                break;
            }
            //Removing Message
            /**
             * API Remove Each message that selected from users
             */
            case RemoveMsg: {
//                List<Data> Want_Remove = new ArrayList<>();
//                for (User user : RegisteredUsers) {
//                    if (user.getUsername().equals(message.getUser().getUsername())) {
//                        for (Data data : user.Inbox) {
//                            if (data.equals(message.getData())) {
//                                Want_Remove.add(data);
//                            }
//
//                        }
//                        user.Inbox.removeAll(Want_Remove);
//                        user.Blocked_Users.removeAll(Want_Remove);
//                        user.ConversationsList.removeAll(Want_Remove);
//                    }
//
//                }
                RemoveMsg(message);
                break;
            }
            /**
             * API that Remove selected conversation
             */
            case RemoveConv: {
//                String reciever = "";
//                List<Data> DeletFormSentMessages = new ArrayList<>();
//                List<Data> DeletFormtInboxMessages = new ArrayList<>();
//                for (Data data : message.getUser().wantToDeletConversation) {
//
//                    for (Data sent : message.getUser().Outbox) {
//                        if (sent.getReciever().equals(data.getReciever())) {
//                            DeletFormSentMessages.add(sent);
//                            reciever = data.getReciever();
//                        }
//                    }
//                    for (Data inbox : message.getUser().Inbox) {
//                        if (inbox.getSender().equals(data.getSender())) {
//                            DeletFormtInboxMessages.add(inbox);
//                            reciever = data.getSender();
//                        }
//                    }
//
//                }
//                for (User user : RegisteredUsers) {
//                    if (message.getUser().getUsername().equals(user.getUsername())) {
//                        user.Inbox.removeAll(DeletFormtInboxMessages);
//                        user.Outbox.removeAll(DeletFormSentMessages);
//                        user.ConversationsList.removeAll(message.getUser().wantToDeletConversation);
//                    }
//                }
//                System.out.println(message.getUser().getUsername() + " DeleteConversation   " + reciever);
//                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
//                LocalDateTime now = LocalDateTime.now();
//                System.out.println(dtf.format(now));
                RemoveConv(message);
                break;
            }
            /**
             * when user Read Messages
             */
            case Read: {
//                System.out.println(message.getUser() + " Read");
//                System.out.println("message : " + message.getData().getSubject() + "  " + message.getData().getSender() + " as  Read ");
//                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
//                LocalDateTime now = LocalDateTime.now();
//                System.out.println(dtf.format(now));
                Read(message);
                break;
            }
            /**
             * When in Conversation Panel user make Messages Unseen
             */
            case UnRead: {
//                System.out.println(message.getUser() + " UnRead");
//                System.out.println("message : " + message.getData().getSubject() + "  " + message.getData().getSender() + " as  UnRead ");
//                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
//                LocalDateTime now = LocalDateTime.now();
//                System.out.println(dtf.format(now));
                UnReade(message);

                break;
            }
            /**
             * Archieved Messages
             */
            case Archieve:{
                Archieve(message);
                break;
            }
            case Recover:{
                Recover(message);
                break;
            }
        }

    }

    /**
     * method that handle all send messages stuff like valid or invalid situation online or not online situation
     *
     * @param message
     * @param outputStream
     * @throws IOException
     */
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
                    System.out.println(user.getUsername() + "  " + "recieve");
                    System.out.println("message:  " + data.getSubject() + data.getSendingFileName());
                    System.out.println(dtf.format(now));


                }

            }


            mailboxes.remove(message.getData());


        }  // sending the message to the online user  .

        else if (ReciverIsOnline) {
            Reciver.getUser().getOutputStream().writeObject(new Message(message.getData()));

            for (User user : RegisteredUsers) {
                if (user.getUsername().equals(message.getData().getReciever())) {
                    user.Inbox.add(message.getData());
                    user.ConversationsList.add(message.getData());
                    System.out.println(user.getUsername() + "  " + "recieve");
                    System.out.println("message:  " + message.getData().getSubject() + message.getData().getSendingFileName());
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

                    LocalDateTime now = LocalDateTime.now();
                    System.out.println(dtf.format(now));

                }
                if (user.getUsername().equals(message.getData().getSender())) {
                    user.ConversationsList.add(message.getData());
                }

            }
            for (User user : RegisteredUsers) {
                if (user.getUsername().equals(message.getData().getSender())) {
                    user.Outbox.add(message.getData());
                }
            }

            MailBox.remove(message.getData());
        }

        // keep the message in mail box till the user become online  .

        else if (!ReciverIsOnline) {

            for (User user : RegisteredUsers) {
                if (user.getUsername().equals(message.getData().getReciever())) {
                    user.Inbox.add(message.getData());
                    user.ConversationsList.add(message.getData());
                    System.out.println(user.getUsername() + "  " + "recieve");
                    System.out.println("message:  " + message.getData().getSubject() + message.getData().getSendingFileName());
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

                    LocalDateTime now = LocalDateTime.now();
                    System.out.println(dtf.format(now));

                }
                if (user.getUsername().equals(message.getData().getSender())) {
                    user.ConversationsList.add(message.getData());
                }

            }
            for (User user : RegisteredUsers) {
                if (user.getUsername().equals(message.getData().getSender())) {
                    System.out.println(user.getUsername() + "  " + "send");
                    System.out.println("message:  " + message.getData().getSubject() + "   " + message.getData().getSendingFileName() + " to " + message.getData().getReciever());
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

                    LocalDateTime now = LocalDateTime.now();
                    System.out.println(dtf.format(now));

                    user.Outbox.add(message.getData());
                }
            }
            for (User user : RegisteredUsers) {

            }


        }


    }

    public  void Block(Message message) {

        User TempUser = null;
        for (User user : RegisteredUsers) {

            if (user.getUsername().equals(message.getData().getSender())) {
                user.setBlock(true);
                System.out.println(message.getUser().getUsername() + "  " + "Block" + message.getData().getSender());
                message.getUser().getBlocked_Users().add(user);
                TempUser = user;
            }
        }
        for (User user : RegisteredUsers) {
            if (user.getUsername().equals(message.getUser().getUsername())) {
                user.Blocked_Users.add(TempUser);
            }
            System.out.println(user.Blocked_Users.size());
        }
        for (User user : RegisteredUsers) {
            for (Data data : user.Inbox) {
                if (data.getSender().equals(message.getData().getSender())) {
                    data.SenderIsBlocked = true;
                }
            }
        }

    }

    public  void UnBlock(Message message) {
        User TempUser = null;
        List<User> UnbLockList = new ArrayList<>();
        for (User user : RegisteredUsers) {
            if (user.getUsername().equals(message.getUser().getUsername())) {
                for (User Blocked : user.Blocked_Users) {
                    try {


                        if (Blocked.getUsername().equals(message.getSender())) {
                            TempUser = Blocked;
//                                user.Blocked_Users.remove(Blocked);
                            Blocked.setBlock(false);
                            UnbLockList.add(Blocked);
                            System.out.println(user.getUsername() + "  UnBlocked  " + Blocked.getUsername());
                            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                            LocalDateTime now = LocalDateTime.now();
                            System.out.println(dtf.format(now));

                        }
                    }catch (NullPointerException ex){ }
                }
                user.Blocked_Users.removeAll(UnbLockList);
            }

        }
        for (User user : RegisteredUsers) {
            if (user.getUsername().equals(message.getUser().getUsername())) {
                for (Data data : user.Inbox) {
                    if (data.getSender().equals(message.getSender())) {
                        data.SenderIsBlocked = false;
                    }
                }
            }
        }
    }

    public  void Refresh(Message message) throws IOException {
        for (User user : RegisteredUsers) {
            if (user.equals(message.getUser())) {
                outputStream.writeObject(new Message(UserActions.Refresh, user.Inbox, user.ConversationsList, user.Blocked_Users, user.Outbox, user.FavoritesBox));
                outputStream.flush();
            }
        }

    }

    public  void ChangeSetting(Message message) throws IOException {
        for (User user : RegisteredUsers) {
            if (user.getUsername().equals(message.getUser().getUsername())) {
                user.setName(message.getNewuser().getName());
                user.setLastname(message.getNewuser().getLastname());
                user.setPassword(message.getNewuser().getPassword());
                user.setUserImageFile(message.getNewuser().getUserImageFile());
                user.setImageUrl(message.getNewuser().getImageUrl());
try {


    outputStream.writeObject(new Message(user, UserActions.Change_Setting));
    outputStream.flush();
}catch (NullPointerException e){

}
                ByteArrayInputStream bis = new ByteArrayInputStream(message.getNewuser().UserImageFile);
                BufferedImage bImage2 = ImageIO.read(bis);
                ImageIO.write(bImage2, "png", new File(IMAGES_DIRECTORY + "New_Image" + message.getUser().getName()));

                break;
            }

        }
    }
    public void ReplyMessage(Message message ){
        System.out.println(message.getUser().getUsername() + "  Reply");
        System.out.println("message: " + message.getData().getSubject() + "  " + message.getData().getSendingFileName() + "  to " + message.getData().getSender());

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

        LocalDateTime now = LocalDateTime.now();
        System.out.println(dtf.format(now));
    }
    public void Forward (Message message){
        System.out.println(message.getUser().getUsername() + "  forward");
        System.out.println("message:  " + message.getData().getSubject() + "  " + message.getData().getSendingFileName() + " from" + message.getData().getReciever() + " TO" + message.getData().getReciever());
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        System.out.println(dtf.format(now));
    }
    public void DiFavoite(Message message){
        for (User user : RegisteredUsers) {
            if (user.getUsername().equals(message.getUser().getUsername())) {
                user.FavoritesBox.remove(message.getData());
                System.out.println(message.getUser() + " unimportant");
                System.out.println("message : " + message.getData().getSubject() + "  " + message.getData().getSender() + " as  unimportant ");
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                LocalDateTime now = LocalDateTime.now();
                System.out.println(dtf.format(now));

            }
        }
    }
    public void Add_Favorite(Message message){
        for (User user : RegisteredUsers) {
            if (user.getUsername().equals(message.getUser().getUsername())) {
                user.FavoritesBox.add(message.getData());
                System.out.println(message.getUser() + " important");
                System.out.println("message : " + message.getData().getSubject() + "  " + message.getData().getSender() + " as  important ");
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                LocalDateTime now = LocalDateTime.now();
                System.out.println(dtf.format(now));
            }
        }
    }
    public  void RemoveMsg(Message message){
        List<Data> Want_Remove = new ArrayList<>();
        for (User user : RegisteredUsers) {
            if (user.getUsername().equals(message.getUser().getUsername())) {
                for (Data data : user.Inbox) {
                    if (data.equals(message.getData())) {
                        Want_Remove.add(data);
                        user.DeletedMessages.add(data);

                    }

                }
                user.Inbox.removeAll(Want_Remove);
                user.Blocked_Users.removeAll(Want_Remove);
                user.ConversationsList.removeAll(Want_Remove);
            }

        }
    }
    public void RemoveConv(Message message){
        String reciever = "";
        List<Data> DeletFormSentMessages = new ArrayList<>();
        List<Data> DeletFormtInboxMessages = new ArrayList<>();
        for (Data data : message.getUser().wantToDeletConversation) {

            for (Data sent : message.getUser().Outbox) {
                if (sent.getReciever().equals(data.getReciever())) {
                    DeletFormSentMessages.add(sent);
                    reciever = data.getReciever();
                }
            }
            for (Data inbox : message.getUser().Inbox) {
                if (inbox.getSender().equals(data.getSender())) {
                    DeletFormtInboxMessages.add(inbox);
                    reciever = data.getSender();
                }
            }

        }
        for (User user : RegisteredUsers) {
            if (message.getUser().getUsername().equals(user.getUsername())) {
                user.Inbox.removeAll(DeletFormtInboxMessages);
                user.Outbox.removeAll(DeletFormSentMessages);
                user.ConversationsList.removeAll(message.getUser().wantToDeletConversation);
            }
        }
        System.out.println(message.getUser().getUsername() + " DeleteConversation   " + reciever);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        System.out.println(dtf.format(now));
    }
    public void Read (Message message){
        System.out.println(message.getUser() + " Read");
        System.out.println("message : " + message.getData().getSubject() + "  " + message.getData().getSender() + " as  Read ");
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        System.out.println(dtf.format(now));
    }
    public void UnReade(Message message){
        System.out.println(message.getUser() + " UnRead");
        System.out.println("message : " + message.getData().getSubject() + "  " + message.getData().getSender() + " as  UnRead ");
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        System.out.println(dtf.format(now));

    }
    public void Archieve(Message message) throws IOException {
        for (User user :RegisteredUsers) {
            if(user.getUsername().equals(message.getUser().getUsername())){
                user.ArhcievedData.add(message.getData());
                try {


                    outputStream.writeObject(new Message(user, UserActions.Archieve, user.ArhcievedData));
                    outputStream.flush();
                }catch (NullPointerException e){

                }
            }
        }
    }
    public void Recover(Message message) throws IOException {
        for (User user :RegisteredUsers) {
           if(user.getUsername().equals(message.getUser().getUsername())){
               user.DeletedMessages.remove(message.getData());
               user.Inbox.add(message.getData());
               user.ConversationsList.add(message.getData());
               outputStream.writeObject(new Message(UserActions.Recover,user,user.DeletedMessages));
               outputStream.flush();
           }
        }
    }
}
