package com.shivam.journalApp.service;

import com.shivam.journalApp.entity.JournalEntry;
import com.shivam.journalApp.entity.User;
import com.shivam.journalApp.repository.JournalEntryRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class JournalEntryService {

    private final JournalEntryRepository journalEntryRepository;
    private final UserService userService;

    public JournalEntryService(JournalEntryRepository journalEntryRepository,
                               UserService userService) {
        this.journalEntryRepository = journalEntryRepository;
        this.userService = userService;
    }

    @Transactional
    public void saveEntry(JournalEntry journalEntry, String userName) {
        boolean removed = false;
        try {
            User user = userService.findByUserName(userName);
            if (user == null) {
                throw new IllegalArgumentException("User not found: " + userName);
            }
            journalEntry.setDate(LocalDateTime.now());
            JournalEntry saved = journalEntryRepository.save(journalEntry);
            user.getJournalEntries().add(saved);
            userService.saveUser(user);
        }
        catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public JournalEntry updateEntry(JournalEntry journalEntry) {
        return journalEntryRepository.save(journalEntry);
    }

    public List<JournalEntry> getAll() {
        return journalEntryRepository.findAll();
    }

    public Optional<JournalEntry> findById(ObjectId id) {
        return journalEntryRepository.findById(id);
    }

    @Transactional
    public boolean deleteById(ObjectId id, String userName) {
        User user = userService.findByUserName(userName);
        if (user == null) {
            return false;
        }
        boolean removed = user.getJournalEntries().removeIf(x -> id.equals(x.getId()));
        if (removed) {
            userService.saveUser(user);
            journalEntryRepository.deleteById(id);
        }
        return removed;
    }
}