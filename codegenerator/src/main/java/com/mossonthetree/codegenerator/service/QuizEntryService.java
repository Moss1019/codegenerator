package com.mossonthetree.codegenerator.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mossonthetree.codegenerator.view.QuizEntry;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Service
public class QuizEntryService {
    private List<QuizEntry> entries = null;

    public List<QuizEntry> getQuizEntries() {
        if (entries == null) {
            entries = loadEntries();
        }
        return entries;
    }

    private List<QuizEntry> loadEntries() {
        ObjectMapper objMapper = new ObjectMapper();
        URL fileUrl = this.getClass().getResource("/quiz_entries/entries_01.json");
        try {
            return objMapper.readValue(fileUrl, List.class);
        } catch (Exception ignored) {

        }
        return new ArrayList<>();
    }
}
