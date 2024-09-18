// src/main/java/com/example/examplemod/cities/commands/CommandDeleteCity.java
package com.example.examplemod.cities.commands;

import com.example.examplemod.cities.CityManager;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

public class CommandDeleteCity extends CommandBase {
    private final CityManager cityManager;

    public CommandDeleteCity(CityManager cityManager) {
        this.cityManager = cityManager;
    }

    @Override
    public String getName() {
        return "deletecity";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/deletecity <cityName>";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2; // Требует уровня OP 2 (можно изменить по необходимости)
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        if (args.length != 1) {
            sender.sendMessage(new TextComponentString("Use: /deletecity <cityName>"));
            return;
        }

        String cityName = args[0];
        if (cityManager.deleteCity(cityName)) {
            sender.sendMessage(new TextComponentString("City \"" + cityName + "\" delete!"));
        } else {
            sender.sendMessage(new TextComponentString("The city with the name \"" + cityName + "\" not find."));
        }
    }
}
