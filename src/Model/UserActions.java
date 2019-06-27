package Model;

import java.io.Serializable;

public enum UserActions implements Serializable {
    SignIn,
    Connect,
    Register,
    Receive,
    Send,
    Reply,
    Forward,
    Read,Unread,Important,Unimportant,
    Block,
    UnBlock,
    RemoveMsg,
    RemoveConv,
    Text,
    Not_Online,
    Change_Setting

}
