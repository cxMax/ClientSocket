package com.cxmax.clientsocket;

import java.util.List;

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
    public List<Phone> phones;

    public ContactBean(String id, String name, List<Phone> phones) {
        this.id = id;
        this.name = name;
        this.phones = phones;
    }

    static class Phone {
        int type;
        String number;
    }

    /**
     * log for toString
     */
    private String getPhones() {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for (Phone p : phones) {
            sb.append(p.toString());
            sb.append(',');
        }
        if (!phones.isEmpty()) {
            sb.deleteCharAt(sb.length() - 1);
        }
        sb.append(']');
        return sb.toString();
    }
}
