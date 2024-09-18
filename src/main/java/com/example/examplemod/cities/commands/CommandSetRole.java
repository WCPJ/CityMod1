// src/main/java/com/example/examplemod/cities/commands/CommandSetRole.java
package com.example.examplemod.cities.commands;

import com.example.examplemod.cities.CityManager;
import com.example.examplemod.cities.City;
import com.example.examplemod.cities.CityRole;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

public class CommandSetRole extends CommandBase {
    private final CityManager cityManager;

    public CommandSetRole(CityManager cityManager) {
        this.cityManager = cityManager;
    }

    @Override
    public String getName() {
        return "setrole";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/setrole <cityName> <playerName> <role>";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2; // Требует уровня OP 2 (можно изменить по необходимости)
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        if (args.length != 3) {
            sender.sendMessage(new TextComponentString("Use: /setrole <cityName> <playerName> <role>"));
            return;
        }

        String cityName = args[0].toLowerCase();
        String playerName = args[1].toLowerCase();
        String roleStr = args[2].toUpperCase();

        CityRole role;
        try {
            role = CityRole.valueOf(roleStr);
        } catch (IllegalArgumentException e) {
            sender.sendMessage(new TextComponentString("The role type is incorrect. Available roles: LEADER, OFFICER, MEMBER."));
            return;
        }

        City city = cityManager.getCity(cityName);
        if (city == null) {
            sender.sendMessage(new TextComponentString("City \"" + cityName + "\" does not exist."));
            return;
        }

        // Проверка роли отправителя команды
        String senderName = sender.getName().toLowerCase();
        if (!city.isMember(senderName)) {
            sender.sendMessage(new TextComponentString("You are not a member of the city \"" + cityName + "\"."));
            return;
        }

        CityRole senderRole = city.getMemberRole(senderName);
        if (senderRole != CityRole.LEADER) {
            sender.sendMessage(new TextComponentString("Only the city leader can assign roles."));
            return;
        }

        if (!city.isMember(playerName)) {
            sender.sendMessage(new TextComponentString("Player \"" + playerName + "\" is not a member of the city \"" + cityName + "\"."));
            return;
        }

        if (cityManager.setMemberRole(cityName, playerName, role)) {
            sender.sendMessage(new TextComponentString("Players role \"" + playerName + "\" in city \"" + cityName + "\" installed on \"" + role + "\"."));
        } else {
            sender.sendMessage(new TextComponentString("The role of the player could not be set \"" + playerName + "\" in the town \"" + cityName + "\"."));
        }
    }
}
