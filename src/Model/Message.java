package Model;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
public class Message implements Serializable {
    static final long serialVersionUID = 42L;
    private String Sender;
    private String reciever;
    private String MessageText;
    private UserActions userActions;
    private User user;
    private List<User> AllUSer=new ArrayList<>();
    private Data date;

    public Data getDate() {
        return date;
    }

    public List<User> getAllUSer() {
        return AllUSer;
    }

    public void setAllUSer(List<User> allUSer) {
        AllUSer = allUSer;
    }

    public Message(String sender, String reciever, String messageText,User user) {
        Sender = sender;
        this.reciever = reciever;
        MessageText = messageText;
        this.user=user;
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

    public Message(String sender, String reciever, String messageText, UserActions userActions, User user) {
        Sender = sender;
        this.reciever = reciever;
        MessageText = messageText;
        this.userActions = userActions;
        this.user = user;
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
