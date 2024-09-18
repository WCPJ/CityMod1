package com.example.examplemod.cities;

import com.example.examplemod.ExampleMod;
import com.example.examplemod.events.ServerChunkEventHandler;
import com.example.examplemod.utils.DataHandler;
import com.google.gson.reflect.TypeToken;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.util.HashMap;
import java.util.Map;

public class CityManager {
    private static CityManager instance; // Singleton

    private Map<String, City> cities;
    private DataHandler<Map<String, City>> dataHandler;
    private static final String DATA_FILE = "cities.json";
    private int maxChunksPerCity;

    public CityManager(FMLPreInitializationEvent event, int maxChunksPerCity) {
        this.maxChunksPerCity = maxChunksPerCity;
        // Инициализация DataHandler с правильным параметром типа
        dataHandler = new DataHandler<>(DATA_FILE, new TypeToken<Map<String, City>>() {
        }.getType());
        cities = dataHandler.loadData(new HashMap<>());

        ExampleMod.LOGGER.info("Cities are loaded: " + cities.size());

        // Регистрация серверных обработчиков событий
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new ServerChunkEventHandler(this));

        instance = this; // Устанавливаем экземпляр
    }

    public static CityManager getInstance() {
        return instance;
    }

    // Методы управления городами
    public boolean createCity(String name, String ownerName) {
        if (cities.containsKey(name.toLowerCase())) {
            ExampleMod.LOGGER.warn("City \"" + name + "\" already exist.");
            return false; // Город с таким именем уже существует
        }
        // Проверка, не состоит ли игрок уже в другом городе
        for (City city : cities.values()) {
            if (city.isMember(ownerName)) {
                ExampleMod.LOGGER.warn("Player \"" + ownerName + "\" is already a member of another city.");
                return false; // Игрок уже состоит в городе
            }
        }

        City city = new City(name);
        city.addMember(ownerName, CityRole.LEADER);
        cities.put(name.toLowerCase(), city);
        ExampleMod.LOGGER.info("City \"" + name + "\" created.");
        saveData();
        return true;
    }

    public boolean deleteCity(String name) {
        if (!cities.containsKey(name.toLowerCase())) {
            ExampleMod.LOGGER.warn("City \"" + name + "\" not found.");
            return false; // Город не найден
        }
        cities.remove(name.toLowerCase());
        ExampleMod.LOGGER.info("City \"" + name + "\" deleted.");
        saveData();
        return true;
    }

    public City getCity(String name) {
        return cities.get(name.toLowerCase());
    }

    public boolean addChunkToCity(String cityName, World world, int x, int z) {
        City city = cities.get(cityName.toLowerCase());
        if (city == null) {
            ExampleMod.LOGGER.warn("City \"" + cityName + "\" not found.");
            return false; // Город не найден
        }
        if (city.getChunkCount() >= maxChunksPerCity) {
            ExampleMod.LOGGER.warn("The maximum number of chunks for the city has been reached \"" + cityName + "\".");
            return false; // Достигнуто максимальное количество чанков
        }
        String key = generateKey(world.provider.getDimension(), x, z);
        if (isChunkClaimed(world, x, z)) {
            ExampleMod.LOGGER.warn("Чанк (" + x + ", " + z + ") уже принадлежит другому городу.");
            return false; // Чанк уже принадлежит другому городу
        }
        city.addChunk(key);
        ExampleMod.LOGGER.info("Чанк (" + x + ", " + z + ") добавлен в город \"" + cityName + "\".");
        saveData();
        return true;
    }

    public boolean removeChunkFromCity(String cityName, World world, int x, int z) {
        City city = cities.get(cityName.toLowerCase());
        if (city == null) {
            ExampleMod.LOGGER.warn("Город \"" + cityName + "\" не найден.");
            return false; // Город не найден
        }
        String key = generateKey(world.provider.getDimension(), x, z);
        if (!city.hasChunk(key)) {
            ExampleMod.LOGGER.warn("Чанк (" + x + ", " + z + ") не принадлежит городу \"" + cityName + "\".");
            return false; // Чанк не принадлежит этому городу
        }
        city.removeChunk(key);
        ExampleMod.LOGGER.info("Чанк (" + x + ", " + z + ") удалён из города \"" + cityName + "\".");
        saveData();
        return true;
    }

    public boolean isChunkClaimed(World world, int x, int z) {
        String key = generateKey(world.provider.getDimension(), x, z);
        for (City city : cities.values()) {
            if (city.hasChunk(key)) {
                return true;
            }
        }
        return false;
    }

    public String getCityByChunk(World world, int x, int z) {
        String key = generateKey(world.provider.getDimension(), x, z);
        for (City city : cities.values()) {
            if (city.hasChunk(key)) {
                return city.getName();
            }
        }
        return null; // Чанк не принадлежит ни одному городу
    }

    private String generateKey(int dimension, int x, int z) {
        return dimension + ":" + x + ":" + z;
    }

    public void saveData() {
        dataHandler.saveData(cities);
        ExampleMod.LOGGER.info("City data is saved.");
    }

    public Map<String, City> getCities() {
        return cities;
    }

    public int getMaxChunksPerCity() {
        return maxChunksPerCity;
    }

    // Методы для управления участниками города
    public boolean inviteMember(String cityName, String playerName) {
        City city = cities.get(cityName.toLowerCase());
        if (city == null) {
            ExampleMod.LOGGER.warn("Город \"" + cityName + "\" не найден.");
            return false; // Город не найден
        }
        if (city.isMember(playerName)) {
            ExampleMod.LOGGER.warn("Игрок \"" + playerName + "\" уже является участником города \"" + cityName + "\".");
            return false; // Игрок уже является участником
        }
        city.addMember(playerName, CityRole.MEMBER);
        ExampleMod.LOGGER.info("Игрок \"" + playerName + "\" приглашён в город \"" + cityName + "\".");
        saveData();
        return true;
    }

    public boolean removeMember(String cityName, String playerName) {
        City city = cities.get(cityName.toLowerCase());
        if (city == null) {
            ExampleMod.LOGGER.warn("Город \"" + cityName + "\" не найден.");
            return false; // Город не найден
        }
        if (!city.isMember(playerName)) {
            ExampleMod.LOGGER.warn("Игрок \"" + playerName + "\" не является участником города \"" + cityName + "\".");
            return false; // Игрок не является участником
        }
        city.removeMember(playerName);
        ExampleMod.LOGGER.info("Игрок \"" + playerName + "\" удалён из города \"" + cityName + "\".");
        saveData();
        return true;
    }

    public boolean setMemberRole(String cityName, String playerName, CityRole role) {
        City city = cities.get(cityName.toLowerCase());
        if (city == null) {
            ExampleMod.LOGGER.warn("Город \"" + cityName + "\" не найден.");
            return false; // Город не найден
        }
        if (!city.isMember(playerName)) {
            ExampleMod.LOGGER.warn("Игрок \"" + playerName + "\" не является участником города \"" + cityName + "\".");
            return false; // Игрок не является участником
        }


        return false;
    }
}


