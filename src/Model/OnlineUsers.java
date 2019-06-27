package Model;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class OnlineUsers {
    private User user;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
 public OnlineUsers(User user){
     this.inputStream=user.getInputStream();
     this.outputStream=user.getOutputStream();
     this.user=user;

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
