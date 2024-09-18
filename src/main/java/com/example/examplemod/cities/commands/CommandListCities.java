// src/main/java/com/example/examplemod/cities/commands/CommandListCities.java
package com.example.examplemod.cities.commands;

import com.example.examplemod.cities.City;
import com.example.examplemod.cities.CityManager;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

import java.util.Map;
import java.util.Set;

public class CommandListCities extends CommandBase {
    private final CityManager cityManager;

    public CommandListCities(CityManager cityManager) {
        this.cityManager = cityManager;
    }

    @Override
    public String getName() {
        return "listcities";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/listcities";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0; // Доступно всем игрокам
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        Map<String, City> cities = cityManager.getCities();
        if (cities.isEmpty()) {
            sender.sendMessage(new TextComponentString("There are no cities available."));
            return;
        }

        StringBuilder message = new StringBuilder("Available cities:\n");
        for (Map.Entry<String, City> entry : cities.entrySet()) {
            String cityName = entry.getKey();
            City city = entry.getValue();
            message.append("- ").append(cityName)
                    .append(" (Chunks: ").append(city.getChunkCount())
                    .append("/").append(cityManager.getMaxChunksPerCity())
                    .append(")\n");
        }

        sender.sendMessage(new TextComponentString(message.toString()));
    }
}
