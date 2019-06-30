package Model;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Objects;

public class Data implements Serializable {
    public static final Long serialVersionUID=123L;
    public static final DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    private String sender;
    private String reciever;

    private String subject;
    private String Text;
    private Calendar calendar;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    private String time;
    private byte[] file;
    private MessageType messageType;

    public Data(String sender, String receiver, String subject, String text, String time, byte[] file) {
        this.sender = sender;
        this.reciever = receiver;
        this.subject = subject;
        Text = text;
        this.time = time;
        this.file = file;
    }

    public Data(String sender, String reciever, String Text, MessageType messageType) {
        this.sender = sender;
        this.reciever = reciever;
        this.Text = Text;
        this.messageType = messageType;
    }

    public Data(String sender, String reciever, String subject, String text, byte[] file) {
        this.sender = sender;
        this.reciever = reciever;
        this.subject = subject;
        Text = text;
        this.file = file;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public void setMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReciever() {
        return reciever;
    }

    public void setReciever(String reciever) {
        this.reciever = reciever;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getText() {
        return Text;
    }

    public void setText(String text) {
        Text = text;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    @Override
    public String toString() {
        return "Data{" +
                "sender='" + sender + '\'' +
                ", receiver='" + reciever + '\'' +
                ", messageText='" + Text + '\'' +
                ", messageType=" + messageType +
                '}';
    }

    @Override
    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
        Data data = (Data) o;
        if( data.getSender().equals(sender))
            return true;
        return false;

    }

    @Override
    public int hashCode() {
        int result = Objects.hash(sender, reciever, subject, Text, calendar, messageType);
        result = 31 * result + Arrays.hashCode(file);
        return result;
    }
}
