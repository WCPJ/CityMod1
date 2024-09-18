// src/main/java/com/example/examplemod/cities/commands/CommandRemoveChunk.java
package com.example.examplemod.cities.commands;

import com.example.examplemod.cities.CityManager;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class CommandRemoveChunk extends CommandBase {
    private final CityManager cityManager;

    public CommandRemoveChunk(CityManager cityManager) {
        this.cityManager = cityManager;
    }

    @Override
    public String getName() {
        return "removechunk";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/removechunk <cityName>";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2; // Требует уровня OP 2 (можно изменить по необходимости)
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        if (args.length != 1) {
            sender.sendMessage(new TextComponentString("Use: /removechunk <cityName>"));
            return;
        }

        String cityName = args[0];
        World world = sender.getEntityWorld();
        BlockPos pos = sender.getPosition();
        int x = pos.getX() >> 4;
        int z = pos.getZ() >> 4;

        if (cityManager.getCity(cityName) == null) {
            sender.sendMessage(new TextComponentString("City \"" + cityName + "\" does not exist."));
            return;
        }

        if (!cityManager.isChunkClaimed(world, x, z)) {
            sender.sendMessage(new TextComponentString("This chunk does not belong to any city."));
            return;
        }

        String existingCity = cityManager.getCityByChunk(world, x, z);
        if (!existingCity.equalsIgnoreCase(cityName)) {
            sender.sendMessage(new TextComponentString("This site belongs to the city \"" + existingCity + "\"."));
            return;
        }

        if (cityManager.removeChunkFromCity(cityName, world, x, z)) {
            sender.sendMessage(new TextComponentString("The chunk was successfully deleted from the city \"" + cityName + "\"."));
        } else {
            sender.sendMessage(new TextComponentString("Couldn't delete a chunk from the city \"" + cityName + "\"."));
        }
    }
}
