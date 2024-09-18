// src/main/java/com/example/examplemod/cities/commands/CommandCreateCity.java
package com.example.examplemod.cities.commands;

import com.example.examplemod.cities.CityManager;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.server.command.CommandTreeBase;
import net.minecraftforge.server.command.CommandTreeHelp;

public class CommandCreateCity extends CommandBase {
    private final CityManager cityManager;

    public CommandCreateCity(CityManager cityManager) {
        this.cityManager = cityManager;
    }

    @Override
    public String getName() {
        return "createcity";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/createcity <cityName>";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2; // Требует уровня OP 2 (можно изменить по необходимости)
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        if (args.length != 1) {
            sender.sendMessage(new TextComponentString("Use: /createcity <cityName>"));
            return;
        }

        String cityName = args[0];
        String ownerName = sender.getName(); // Получаем имя отправителя команды

        if (cityManager.createCity(cityName, ownerName)) {
            sender.sendMessage(new TextComponentString("City \"" + cityName + "\" successfully created!"));
            // Дополнительно можно уведомить владельца или других участников
        } else {
            sender.sendMessage(new TextComponentString("The city with the name \"" + cityName + "\" already exists."));
        }
    }
}
