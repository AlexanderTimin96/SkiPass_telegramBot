package ru.timin.telegramBot.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

import java.io.Serializable;

@Entity
public class Client implements Serializable {
    @Id
    private Long chatId;
    private String firstName;
    private String lastName;
    private String userName;
    private String phone;

    @OneToOne
    private SkiPass skiPass;

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public SkiPass getSkiPass() {
        return skiPass;
    }

    public void setSkiPass(SkiPass skiPass) {
        this.skiPass = skiPass;
    }
}