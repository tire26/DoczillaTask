package org.ru;

import java.util.Date;

public class Task {
    private String id;
    private String name;
    private String shortDesc;
    private String fullDesc;
    private Date date;
    private boolean status;

    public Task() {
    }

    public Task(String id, String name, String shortDesc, String fullDesc, Date date, boolean status) {
        this.id = id;
        this.name = name;
        this.shortDesc = shortDesc;
        this.fullDesc = fullDesc;
        this.date = date;
        this.status = status;
    }

    // Геттеры и сеттеры для каждого поля

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public String getFullDesc() {
        return fullDesc;
    }

    public void setFullDesc(String fullDesc) {
        this.fullDesc = fullDesc;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}