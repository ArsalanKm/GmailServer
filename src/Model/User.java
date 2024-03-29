package Model;

import javafx.scene.image.Image;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {
    // 7589968655528222888
    public static final long serialVersionUID = 12;
    public static List<User> GmailUsers = new ArrayList<>();
    public static List<User> RegisterdUser = new ArrayList<>();
    public static List<User> xx = new ArrayList<>();
    public List<Data> Inbox = new ArrayList<>();
    public List<Data> Outbox = new ArrayList<>();
    public byte[] UserImageFile = new byte[1024];
    public List<Data> ConversationsList = new ArrayList<>();
    public List<Data> FavoritesBox = new ArrayList<>();
    private String ImageUrl = "C:\\Users\\asus\\Desktop\\GmailMainProject\\src\\Recources\\";
    private String name;
    private String Lastname;
    private String Username;
    private String CurrentTimeAndDate;
    private String Password;
    private String BirthDate;
    private String SavedLocation;
    private UserActions userActions;
    private long Phonenumber;
    private transient Image image;
    private Gender gender;
    private transient ObjectOutputStream outputStream;
    private transient ObjectInputStream inputStream;
    private List<Data> inbox = new ArrayList<>();
    private List<Data> DeliveryBox = new ArrayList<>();
    private List<Data> outbox = new ArrayList<>();
    public List<User> Blocked_Users = new ArrayList<>();
    public List<Data> wantToDeletConversation=new ArrayList<>();
    private boolean IsBlock=false;
    public List<Data> ArhcievedData=new ArrayList<>();
    public List<Data> DeletedMessages=new ArrayList<>();
    public boolean isBlock() {
        return IsBlock;
    }

    public void setBlock(boolean block) {
        IsBlock = block;
    }

    public User() {
    }

    public User(String name, String lastname, String username, String password, String birthDate) {
        this.name = name;
        Lastname = lastname;
        Username = username;
        Password = password;
        BirthDate = birthDate;
    }

    public User(long phonenumber, Image image, Gender gender) {
        super();
        Phonenumber = phonenumber;
        this.image = image;
        this.gender = gender;
    }

    public User(String username, ObjectOutputStream outputStream, ObjectInputStream inputStream) {
        Username = username;
        this.outputStream = outputStream;
        this.inputStream = inputStream;
    }

    public static List<User> getGmailUsers() {
        return GmailUsers;
    }

    public List<User> getBlocked_Users() {
        return Blocked_Users;
    }

    public void setBlocked_Users(List<User> blocked_Users) {
        Blocked_Users = blocked_Users;
    }

    public List<Data> getFavoritesBox() {
        return FavoritesBox;
    }

    public byte[] getUserImageFile() {
        return UserImageFile;
    }

    public void setUserImageFile(byte[] userImageFile) {
        UserImageFile = userImageFile;
    }

    public List<Data> getDeliveryBox() {
        return DeliveryBox;
    }

    public void setDeliveryBox(List<Data> deliveryBox) {
        DeliveryBox = deliveryBox;
    }

    public List<Data> getOutbox() {
        return outbox;
    }

    public void setOutbox(List<Data> outbox) {
        this.outbox = outbox;
    }

    public List<Data> getInbox() {
        return inbox;
    }

    public void setInbox(List<Data> inbox) {
        this.inbox = inbox;
    }

    public ObjectOutputStream getOutputStream() {
        return outputStream;
    }

    public void setOutputStream(ObjectOutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public ObjectInputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(ObjectInputStream inputStream) {
        this.inputStream = inputStream;
    }

    public String getCurrentTimeAndDate() {
        return CurrentTimeAndDate;
    }

    public void setCurrentTimeAndDate(String currentTimeAndDate) {
        CurrentTimeAndDate = currentTimeAndDate;
    }

    public UserActions getUserActions() {
        return userActions;
    }

    public void setUserActions(UserActions userActions) {
        this.userActions = userActions;
    }


    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public String getSavedLocation() {
        return SavedLocation;
    }

    public void setSavedLocation(String savedLocation) {
        SavedLocation = savedLocation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return Lastname;
    }

    public void setLastname(String lastname) {
        Lastname = lastname;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getBirthDate() {
        return BirthDate;
    }

    public void setBirthDate(String birthDate) {
        BirthDate = birthDate;
    }

    public long getPhonenumber() {
        return Phonenumber;
    }

    public void setPhonenumber(long phonenumber) {
        Phonenumber = phonenumber;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;


        User user = (User) o;
        return this.getPassword().equals(user.getPassword()) && user.getUsername().equals(this.getUsername());

    }

}
