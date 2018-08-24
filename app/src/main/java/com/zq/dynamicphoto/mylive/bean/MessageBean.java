package com.zq.dynamicphoto.mylive.bean;

/**
 * Created by Administrator on 2018/5/10.
 */

public class MessageBean {
    private String nick;

    private String msg;

    public MessageBean(String nick, String msg) {
        this.nick = nick;
        this.msg = msg;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
