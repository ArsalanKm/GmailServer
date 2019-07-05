package Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Message implements Serializable {
    static final long serialVersionUID = 42L;
    public List<Data> ConversationsList = new ArrayList<>();
    public List<Data> OUTBOX = new ArrayList<>();
    public List<Data> ListOfConversation = new ArrayList<>();
    public List<Data> FavoritBox=new ArrayList<>();
    private User newuser = new User();
    private String Sender;
    private String reciever;
    private String MessageText;
    private UserActions userActions;
    private User user;
    private List<User> AllUSer = new ArrayList<>();
    private List<Data> Mydata = new ArrayList<>();
    private Data data;
    private List<User> Blocked_User = new ArrayList<>();
public List<Data> ArchievedList=new ArrayList<>();
public List<Data> DeletedMessageList=new ArrayList<>();
    public Message(User user, Data data) {
        this.user = user;
        this.data = data;
    }
public Message (UserActions userActions,User user, List<Data> DeletedMessages){
        this.user=user;
this.userActions=userActions;
        this.DeletedMessageList=DeletedMessages;
}
    public Message(User user, Data data, UserActions userActions) {
        this.user = user;
        this.data = data;
        this.userActions = userActions;
    }

    public Message (User user, UserActions userActions,List<Data> ArchievedList){
        this.userActions=userActions;
        this.user=user;
        this.ArchievedList=ArchievedList;

    }
    public Message(User user, List<Data> chatList, UserActions userActions) {
        this.ListOfConversation = chatList;
        this.user = user;
        this.userActions = userActions;
    }


    public Message(List<Data> mydata, UserActions userActions) {
        this.userActions = userActions;
        Mydata = mydata;
    }

    public Message(Data data) {
        this.data = data;
    }

    public Message(Data mailbox, UserActions message) {
        this.data = mailbox;
        this.userActions = message;
    }

    public Message(UserActions refresh, List<Data> inbox, List<Data> ConversationsList, List<User> Blocked_Users, List<Data> outbox,List<Data> FAvoriteBox) {
        this.userActions = refresh;
        this.Mydata = inbox;
        this.ConversationsList = ConversationsList;
        this.Blocked_User = Blocked_Users;
        this.OUTBOX = outbox;
        this.FavoritBox=FAvoriteBox;
    }

    public Message(String sender, String reciever, String messageText, User user) {
        Sender = sender;
        this.reciever = reciever;
        MessageText = messageText;
        this.user = user;
    }

    public Message(String sender, String reciever, String messageText, UserActions userActions) {
        Sender = sender;
        this.reciever = reciever;
        MessageText = messageText;
        this.setUserActions(userActions);
    }

    public Message(String sender, String reciever, String messageText, UserActions userActions, List<User> allUSer) {
        Sender = sender;
        this.reciever = reciever;
        MessageText = messageText;
        this.userActions = userActions;
        AllUSer = allUSer;
    }

    public Message(String sender, String reciever, UserActions userActions, Data data) {
        Sender = sender;
        this.reciever = reciever;
        this.userActions = userActions;
        this.data = data;
    }


    public Message(String sender, String reciever, String messageText, UserActions userActions, User user) {
        Sender = sender;
        this.reciever = reciever;
        MessageText = messageText;
        this.userActions = userActions;
        this.user = user;
    }

    public User getNewuser() {
        return newuser;
    }

    public void setNewuser(User newuser) {
        this.newuser = newuser;
    }
    public Message(User user, UserActions userActions){
        this.user=user;
        this.userActions=userActions;
    }

    public Message(User user, User newuser, UserActions change_setting) {
        this.user = user;
        this.userActions = change_setting;
        this.newuser = newuser;
    }

    public List<User> getBlocked_User() {
        return Blocked_User;
    }

    public void setBlocked_User(List<User> blocked_User) {
        Blocked_User = blocked_User;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public List<User> getAllUSer() {
        return AllUSer;
    }

    public void setAllUSer(List<User> allUSer) {
        AllUSer = allUSer;
    }

    public String getSender() {
        return Sender;
    }

    public void setSender(String sender) {
        Sender = sender;
    }

    public String getReciever() {
        return reciever;
    }

    public void setReciever(String reciever) {
        this.reciever = reciever;
    }

    public String getMessageText() {
        return MessageText;
    }

    public void setMessageText(String messageText) {
        MessageText = messageText;
    }

    public UserActions getUserActions() {
        return userActions;
    }

    public void setUserActions(UserActions userActions) {
        this.userActions = userActions;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
