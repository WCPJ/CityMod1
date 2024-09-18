// src/main/java/com/example/examplemod/cities/commands/CommandInviteMember.java
package com.example.examplemod.cities.commands;

import com.example.examplemod.cities.CityManager;
import com.example.examplemod.cities.City;
import com.example.examplemod.cities.CityRole;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

public class CommandInviteMember extends CommandBase {
    private final CityManager cityManager;

    public CommandInviteMember(CityManager cityManager) {
        this.cityManager = cityManager;
    }

    @Override
    public String getName() {
        return "invite";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/invite <cityName> <playerName>";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2; // Требует уровня OP 2 (можно изменить по необходимости)
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        if (args.length != 2) {
            sender.sendMessage(new TextComponentString("Use: /invite <cityName> <playerName>"));
            return;
        }

        String cityName = args[0].toLowerCase();
        String playerName = args[1].toLowerCase();

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
        if (senderRole != CityRole.LEADER && senderRole != CityRole.OFFICER) {
            sender.sendMessage(new TextComponentString("You do not have the right to invite participants to the city \"" + cityName + "\"."));
            return;
        }

        if (city.isMember(playerName)) {
            sender.sendMessage(new TextComponentString("Player \"" + playerName + "\" is already a member of the city \"" + cityName + "\"."));
            return;
        }

        if (cityManager.inviteMember(cityName, playerName)) {
            sender.sendMessage(new TextComponentString("Player \"" + playerName + "\" invited to the city \"" + cityName + "\"."));
        } else {
            sender.sendMessage(new TextComponentString("Failed to invite a player \"" + playerName + "\" in the city \"" + cityName + "\"."));
        }
    }
}
