package com.shivam.journalApp.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@Document(collection = "users")
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class User {

    @Id
    private String id;

    @NonNull
    private String userName;

    @NonNull
    private String password;

    private String email;

    private boolean sentimentAnalysis;

    private List<String> roles;

    private List<JournalEntry> journalEntries = new ArrayList<>();
}