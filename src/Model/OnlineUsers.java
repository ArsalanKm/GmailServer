package Model;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class OnlineUsers {
    public static List<OnlineUsers> ONLINE_USERS = new ArrayList<>();
    private User user;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private String username;

    public OnlineUsers(User user, ObjectOutputStream outputStream, ObjectInputStream inputStream) {
        this.inputStream = inputStream;
        this.outputStream = outputStream;
        this.user = user;
        this.user.setOutputStream(outputStream);
        this.user.setInputStream(inputStream);

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
}
