package com.mossonthetree.codegenerator.view;

public class QuizEntry {
    private String word;
    private String plural;
    private String article;
    private String translation;

    public QuizEntry() {
    }

    public QuizEntry(String word, String plural, String article, String translation) {
        this.word = word;
        this.plural = plural;
        this.article = article;
        this.translation = translation;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getPlural() {
        return plural;
    }

    public void setPlural(String plural) {
        this.plural = plural;
    }

    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }
}
