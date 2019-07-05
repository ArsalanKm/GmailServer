import Model.Data;
import Model.Message;
import Model.User;
import Model.UserActions;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * j unit class created by Arsalan karimzad
 * we Test that Whether Client APIS work Correct or not here
 *
 * @Param user1
 * @Param user2
 * @Param user3
 * @Param data1
 * @Param data2
 * @Param data3
 */
public class junit {

    private User user2;
    private User user1;
    private User user3;
    private Data data;
    private Data data1;
    private Data data2;
    private ServerHandler serverHandler;


    @Before


    public void setup() {
        serverHandler = new ServerHandler();
        user2 = new User();
        user1 = new User();
        user3 = new User();
        user1.setName("user1");
        user2.setName("user2");
        user3.setName("user3");
        user1.setUsername("user1@gmail.com");
        user2.setUsername("user2@gmail.com");
        user3.setUsername("user3@gmail.com");
        user2.setPassword("user2");
        user1.setPassword("user1");
        user3.setPassword("user3");
        data = new Data("user1", "user2", "firstMessage", "first test Message ", "", null);
        data1 = new Data("user2", "user1", "Second ", "Second Message", null);
        data2 = new Data("user3", "user 2", "Third ", "Third Message", null);
    }

    @Test
    public void testRegisterdUsers() {
        assertTrue(ServerHandler.RegisteredUsers.isEmpty());
        assertEquals(0, ServerHandler.RegisteredUsers.size());
        ServerHandler.RegisteredUsers.add(user1);
        assertEquals(1, ServerHandler.RegisteredUsers.size());
        ServerHandler.RegisteredUsers.remove(user1);
        assertTrue(ServerHandler.RegisteredUsers.isEmpty());


    }

    @Test
    public void testLoggedinUsers() {
        assertNotNull(user1);
        assertNotNull(user2);
        assertTrue(ServerHandler.RegisteredUsers.isEmpty());
        ServerHandler.LoggedInUsers.add(user1);
        assertEquals(1, ServerHandler.LoggedInUsers.size());
        ServerHandler.RegisteredUsers.remove(user2);
        assertEquals(0, ServerHandler.RegisteredUsers.size());

    }

    @Test
    public void checkBlockList() {
        ServerHandler.RegisteredUsers.add(user2);
        ServerHandler.RegisteredUsers.add(user1);
        user1.Inbox.add(data1);
        user1.Inbox.add(data2);
        Message message = new Message(user1, data, UserActions.Block);
        Message message1 = new Message(user1, data2, UserActions.Block);

        serverHandler.Block(message);
        assertEquals(1, user1.Blocked_Users.size());
        serverHandler.Block(message1);
        assertEquals(2, user1.Blocked_Users.size());
        user3.Inbox.add(data);


        ServerHandler.RegisteredUsers.clear();
    }


    @Test
    public void CheckRemoveMsg() {

        user1.Inbox.add(data);
        user1.Inbox.add(data1);
        ServerHandler.RegisteredUsers.add(user1);
        ServerHandler.RegisteredUsers.add(user2);
        user2.Inbox.add(data);
        Message API1 = new Message(user1, data);
        Message API2 = new Message(user2, data);
        serverHandler.RemoveMsg(API1);
        serverHandler.RemoveMsg(API2);
        assertEquals(1, user1.Inbox.size());
        assertEquals(0, user2.Inbox.size());
        ServerHandler.RegisteredUsers.clear();

    }

    @Test
    public void CheckRemoveConv() {

        user1.ConversationsList.add(data);
        user1.ConversationsList.add(data1);
        user1.wantToDeletConversation.add(data);
        user1.wantToDeletConversation.add(data1);

        assertEquals(2, user1.ConversationsList.size());
        ServerHandler.RegisteredUsers.add(user1);
        ;

        Message message = new Message(user1, user1.wantToDeletConversation, UserActions.RemoveConv);
        serverHandler.RemoveConv(message);
        assertEquals(0, user1.ConversationsList.size());
        ServerHandler.RegisteredUsers.clear();
    }

    @Test
    public void CheckUnBlock() {
        ServerHandler.RegisteredUsers.add(user1);
        user1.Inbox.add(data1);
        Message message = new Message(user1, data1, UserActions.Block);
        serverHandler.Block(message);
        assertEquals(1, user1.Blocked_Users.size());
        Message message1 = new Message("user2", "", "", UserActions.UnBlock, user1);
        serverHandler.UnBlock(message1);
        ServerHandler.RegisteredUsers.clear();
    }

    @Test
    public void AddTOFavorites() {
        ServerHandler.RegisteredUsers.add(user1);
        user1.Inbox.add(data);
        user1.Inbox.add(data1);
        user1.Inbox.add(data2);
        Message message = new Message(user1, data, UserActions.Add_Favorite);
        serverHandler.Add_Favorite(message);
        assertEquals(1, user1.FavoritesBox.size());
        Message message1 = new Message(user1, data1, UserActions.Add_Favorite);
        serverHandler.Add_Favorite(message);
        Message message2 = new Message(user1, data2, UserActions.Add_Favorite);
        serverHandler.Add_Favorite(message);
        assertEquals(3, user1.FavoritesBox.size());
        ServerHandler.RegisteredUsers.clear();

    }

    @Test
    public void DeleteFromFavorite() {
        ServerHandler.RegisteredUsers.add(user1);
        user1.Inbox.add(data);
        user1.Inbox.add(data1);
        user1.Inbox.add(data2);
        Message message = new Message(user1, data, UserActions.Add_Favorite);
        serverHandler.Add_Favorite(message);
        Message message1 = new Message(user1, data, UserActions.DeleteFavoite);
        serverHandler.DiFavoite(message1);
        assertEquals(0, user1.FavoritesBox.size());
        ServerHandler.RegisteredUsers.clear();
    }

    @Test
    public void CheckRead() {
        user1.Inbox.add(data1);
        Message message = new Message(user1, data1, UserActions.Read);
        ServerHandler.RegisteredUsers.add(user1);
        assertEquals(1, ServerHandler.RegisteredUsers.size());
        serverHandler.Read(message);
        ServerHandler.RegisteredUsers.clear();

    }

    @Test
    public void CheckUnRead() {
        user1.Inbox.add(data1);
        ServerHandler.RegisteredUsers.add(user1);
        Message message = new Message(user1, data1, UserActions.UnRead);
        serverHandler.UnReade(message);
        assertEquals(1, ServerHandler.RegisteredUsers.size());
        ServerHandler.RegisteredUsers.clear();
    }

    @Test
    public void CheckForward() {
        user1.Inbox.add(data1);
        user1.Inbox.add(data2);
        ServerHandler.RegisteredUsers.add(user1);
        assertEquals(1, ServerHandler.RegisteredUsers.size());
        Message message = new Message(user1, data1, UserActions.Forward);
        serverHandler.Forward(message);
    }

    @Test
    public void CheckReply() {
        user1.Inbox.add(data2);
        user2.Inbox.add(data2);

        ServerHandler.RegisteredUsers.add(user1);
        ServerHandler.RegisteredUsers.add(user2);
        Message message = new Message(user1, data2, UserActions.Reply);
        assertNotNull(message);
        Message message1 = new Message(user2, data2, UserActions.Reply);
        assertNotNull(message1);
        serverHandler.ReplyMessage(message);
        serverHandler.ReplyMessage(message1);
    }

    @Test
    public void ArchievedCHeck() throws IOException {
        user1.Inbox.add(data);
        user2.Inbox.add(data2);
        ServerHandler.RegisteredUsers.add(user1);
        ServerHandler.RegisteredUsers.add(user2);
        Message message = new Message(user1, data, UserActions.Archieve);
        Message message1 = new Message(user2, data2, UserActions.Archieve);
        serverHandler.Archieve(message);
        serverHandler.Archieve(message1);
        assertEquals(1, user1.ArhcievedData.size());
        assertEquals(1, user2.ArhcievedData.size());


    }

    @Test
    public void DeltedMEssaesCheck() {
        user1.Inbox.add(data);
        user1.Inbox.add(data1);
        ServerHandler.RegisteredUsers.add(user1);
        ServerHandler.RegisteredUsers.add(user2);
        user2.Inbox.add(data);
        Message API1 = new Message(user1, data);
        Message API2 = new Message(user2, data);
        serverHandler.RemoveMsg(API1);
        serverHandler.RemoveMsg(API2);
        assertEquals(1, user1.DeletedMessages.size());
        assertEquals(1, user2.DeletedMessages.size());
        assertEquals(1, user1.Inbox.size());
        assertEquals(0, user2.Inbox.size());
        ServerHandler.RegisteredUsers.clear();
    }
}
