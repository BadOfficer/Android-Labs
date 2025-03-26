package com.example.labs.db;

import com.example.labs.db.dao.TextStyleEntity;

import java.util.List;

public interface TextStyleRepository {
    boolean createTextStyleEntry(TextStyleEntity textStyleEntity);

    List<TextStyleEntity> getAllTextStyles();

    void deleteAllTextStyles();

    void deleteTextStyleById(long id);
}
