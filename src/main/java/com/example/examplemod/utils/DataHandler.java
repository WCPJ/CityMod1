// src/main/java/com/example/examplemod/utils/DataHandler.java
package com.example.examplemod.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import java.io.*;
import java.lang.reflect.Type;

public class DataHandler<T> {
    private String fileName;
    private Type type;
    private Gson gson;

    public DataHandler(String fileName, Type type) {
        this.fileName = fileName;
        this.type = type;
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    public T loadData(T defaultData) {
        File file = new File(fileName);
        if (!file.exists()) {
            saveData(defaultData);
            return defaultData;
        }
        try (Reader reader = new FileReader(file)) {
            T data = gson.fromJson(reader, type);
            if (data == null) {
                return defaultData;
            }
            return data;
        } catch (JsonSyntaxException | JsonIOException | IOException e) {
            e.printStackTrace();
            return defaultData;
        }
    }

    public void saveData(T data) {
        try (Writer writer = new FileWriter(fileName)) {
            gson.toJson(data, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
