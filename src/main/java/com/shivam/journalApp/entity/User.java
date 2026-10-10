package com.shivam.journalApp.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@Document(collection = "users")
public class User {

    @Id
    private String id;

    private String userName;

    private String password;

    private List<String> roles;

    private List<JournalEntry> journalEntries = new ArrayList<>();
}