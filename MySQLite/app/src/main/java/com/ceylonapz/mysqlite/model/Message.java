package com.ceylonapz.mysqlite.model;

import java.io.Serializable;

/**
 * Created by ceylonApz on 2018-03-14
 */
public class Message implements Serializable {

    private int msgId;
    private String msgTitle;
    private String msgBody;
    private String msgDate;
    private int msgIsRead;

    public Message(int msgId, String msgTitle, String msgBody, String msgDate, int msgIsRead) {
        this.msgId = msgId;
        this.msgTitle = msgTitle;
        this.msgBody = msgBody;
        this.msgDate = msgDate;
        this.msgIsRead = msgIsRead;
    }

    public Message(String msgTitle, String msgBody, String msgDate, int msgIsRead) {
        this.msgTitle = msgTitle;
        this.msgBody = msgBody;
        this.msgDate = msgDate;
        this.msgIsRead = msgIsRead;
    }

    public int getMsgId() {
        return msgId;
    }

    public String getMsgTitle() {
        return msgTitle;
    }

    public String getMsgBody() {
        return msgBody;
    }

    public String getMsgDate() {
        return msgDate;
    }

    public int getMsgIsRead() {
        return msgIsRead;
    }
}
