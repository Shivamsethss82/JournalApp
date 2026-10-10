package com.shivam.journalApp.controller;

import com.shivam.journalApp.entity.JournalEntry;
import com.shivam.journalApp.entity.User;
import com.shivam.journalApp.service.JournalEntryService;
import com.shivam.journalApp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/journal")
public class JournalEntryController {

    private final JournalEntryService journalEntryService;
    private final UserService userService;

    public JournalEntryController(JournalEntryService journalEntryService,
                                  UserService userService) {
        this.journalEntryService = journalEntryService;
        this.userService = userService;
    }

    // Username of the logged-in user (from Basic Auth, cannot be faked via URL)
    private String currentUserName() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    // Does this entry belong to this user?
    private boolean ownsEntry(User user, ObjectId entryId) {
        return user.getJournalEntries().stream()
                .anyMatch(x -> entryId.equals(x.getId()));
    }

    // GET all MY entries
    @GetMapping
    public ResponseEntity<?> getMyEntries() {
        User user = userService.findByUserName(currentUserName());
        return new ResponseEntity<>(user.getJournalEntries(), HttpStatus.OK);
    }

    // CREATE an entry for me
    @PostMapping
    public ResponseEntity<?> createEntry(@RequestBody JournalEntry myEntry) {
        try {
            journalEntryService.saveEntry(myEntry, currentUserName());
            return new ResponseEntity<>(myEntry, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // GET one of MY entries
    @GetMapping("/id/{myId}")
    public ResponseEntity<?> getEntryById(@PathVariable ObjectId myId) {
        User user = userService.findByUserName(currentUserName());
        if (ownsEntry(user, myId)) {
            Optional<JournalEntry> entry = journalEntryService.findById(myId);
            if (entry.isPresent()) {
                return new ResponseEntity<>(entry.get(), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // UPDATE one of MY entries
    @PutMapping("/id/{myId}")
    public ResponseEntity<?> updateEntry(@PathVariable ObjectId myId,
                                         @RequestBody JournalEntry newEntry) {
        User user = userService.findByUserName(currentUserName());
        if (!ownsEntry(user, myId)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Optional<JournalEntry> found = journalEntryService.findById(myId);
        if (found.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        JournalEntry old = found.get();
        if (newEntry.getTitle() != null && !newEntry.getTitle().isEmpty()) {
            old.setTitle(newEntry.getTitle());
        }
        if (newEntry.getContent() != null && !newEntry.getContent().isEmpty()) {
            old.setContent(newEntry.getContent());
        }
        return new ResponseEntity<>(journalEntryService.updateEntry(old), HttpStatus.OK);
    }

    // DELETE one of MY entries
    @DeleteMapping("/id/{myId}")
    public ResponseEntity<?> deleteEntry(@PathVariable ObjectId myId) {
        boolean removed = journalEntryService.deleteById(myId, currentUserName());
        return removed ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}