// src/main/java/com/example/examplemod/cities/commands/CommandKick.java
package com.example.examplemod.cities.commands;

import com.example.examplemod.cities.City;
import com.example.examplemod.cities.CityManager;
import com.example.examplemod.cities.CityRole;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

public class CommandKick extends CommandBase {
    private final CityManager cityManager;

    public CommandKick(CityManager cityManager) {
        this.cityManager = cityManager;
    }

    @Override
    public String getName() {
        return "kick";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/kick <cityName> <playerName>";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2; // Требует уровня OP 2 (можно изменить по необходимости)
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        if (args.length != 2) {
            sender.sendMessage(new TextComponentString("Использование: /kick <cityName> <playerName>"));
            return;
        }

        String cityName = args[0];
        String playerName = args[1];

        City city = cityManager.getCity(cityName);
        if (city == null) {
            sender.sendMessage(new TextComponentString("Город \"" + cityName + "\" не найден."));
            return;
        }

        // Проверяем, является ли отправитель команды лидером или офицером города
        String senderName = sender.getName();
        if (!city.isMember(senderName)) {
            sender.sendMessage(new TextComponentString("Вы не являетесь участником города \"" + cityName + "\"."));
            return;
        }

        CityRole senderRole = city.getMemberRole(senderName);
        if (senderRole != CityRole.LEADER && senderRole != CityRole.OFFICER) {
            sender.sendMessage(new TextComponentString("У вас нет прав для исключения участников из города."));
            return;
        }

        // Проверяем, является ли целевой игрок участником города
        if (!city.isMember(playerName)) {
            sender.sendMessage(new TextComponentString("Игрок \"" + playerName + "\" не является участником города \"" + cityName + "\"."));
            return;
        }

        // Нельзя исключить лидера города
        CityRole targetRole = city.getMemberRole(playerName);
        if (targetRole == CityRole.LEADER) {
            sender.sendMessage(new TextComponentString("Нельзя исключить лидера города."));
            return;
        }

        // Исключаем игрока из города
        if (cityManager.removeMember(cityName, playerName)) {
            sender.sendMessage(new TextComponentString("Игрок \"" + playerName + "\" был исключён из города \"" + cityName + "\"."));
            // Можно отправить сообщение исключённому игроку, если доступно
            // Например, через серверные сообщения или систему уведомлений
        } else {
            sender.sendMessage(new TextComponentString("Не удалось исключить игрока \"" + playerName + "\" из города \"" + cityName + "\"."));
        }
    }
}
