package Model;

import java.util.ArrayList;
import java.util.List;

public class MailBox {
    public static List<Data> mailboxes= new ArrayList<>();

    public static List<Data> getMailboxes() {
        return mailboxes;
    }

    public static void setMailboxes(List<Data> mailboxes) {
        MailBox.mailboxes = mailboxes;
    }
    public static void remove(Data data){
        synchronized (mailboxes){
            mailboxes.remove(data);
        }
    }
}
