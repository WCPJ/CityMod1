// src/main/java/com/example/examplemod/cities/City.java
package com.example.examplemod.cities;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class City implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private Set<String> chunks; // Формат "dimension:x:z"
    private Map<String, CityRole> members; // Ключ: имя игрока, Значение: роль

    public City(String name) {
        this.name = name;
        this.chunks = new HashSet<>();
        this.members = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public void addChunk(String chunkKey) {
        chunks.add(chunkKey);
    }

    public void removeChunk(String chunkKey) {
        chunks.remove(chunkKey);
    }

    public boolean hasChunk(String chunkKey) {
        return chunks.contains(chunkKey);
    }

    public Set<String> getChunks() {
        return chunks;
    }

    public void addMember(String playerName, CityRole role) {
        members.put(playerName.toLowerCase(), role);
    }

    public void removeMember(String playerName) {
        members.remove(playerName.toLowerCase());
    }

    public boolean isMember(String playerName) {
        return members.containsKey(playerName.toLowerCase());
    }

    public CityRole getMemberRole(String playerName) {
        return members.get(playerName.toLowerCase());
    }

    public Map<String, CityRole> getMembers() {
        return members;
    }

    public int getChunkCount() {
        return chunks.size();
    }
}
