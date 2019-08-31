package com.cxmax.clientsocket;

/**
 * ClientSocket.com.cxmax.clientsocket
 *
 * @ClassName: ContactBean
 * @Description:
 * @Author: tanlin
 * @Date: 2019-08-31
 * @Version: 1.0
 */
public class ContactBean {
    public String id;
    public String name;
    public String number;

    public ContactBean(String id, String name, String phone) {
        this.id = id;
        this.name = name;
        this.number = phone;
    }
}
