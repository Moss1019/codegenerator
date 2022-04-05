package com.mossonthetree.codegenerator.services;

import com.mossonthetree.codegenerator.service.QuizEntryService;
import com.mossonthetree.codegenerator.view.QuizEntry;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

import java.util.List;

public class QuizEntryServiceTests {
    @Test
    public void testSpringBootGenerator_callGenerate_returnsBytes() {
        // Arrange
        QuizEntryService sut = new QuizEntryService();

        // Act
        List<QuizEntry> entries = sut.getQuizEntries();

        // Assert
        Assert.isTrue(entries.size() > 0, String.format("%d entries", entries.size()));
    }
}
