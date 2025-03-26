package com.example.labs.db.dao;

public class TextStyleEntity {
    private Long id;
    private String text;
    private int fontId;

    public TextStyleEntity(String text, int fontId) {
        this.text = text;
        this.fontId = fontId;
    }

    public TextStyleEntity(Long id, String text, int fontId) {
        this.id = id;
        this.text = text;
        this.fontId = fontId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getFontId() {
        return fontId;
    }

    public void setFontId(int fontId) {
        this.fontId = fontId;
    }
}
