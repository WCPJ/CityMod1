// src/main/java/com/example/examplemod/cities/commands/CommandAddChunk.java
package com.example.examplemod.cities.commands;

import com.example.examplemod.cities.CityManager;
import com.example.examplemod.cities.City;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CommandAddChunk extends CommandBase {
    private CityManager cityManager;

    public CommandAddChunk(CityManager cityManager) {
        this.cityManager = cityManager;
    }

    @Override
    public String getName() {
        return "addchunk";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/addchunk <cityName>";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        if (args.length != 1) {
            sender.sendMessage(new TextComponentString("Use: /addchunk <cityName>"));
            return;
        }

        String cityName = args[0];
        World world = sender.getEntityWorld();
        BlockPos pos = sender.getPosition();
        int x = pos.getX() >> 4;
        int z = pos.getZ() >> 4;

        if (cityManager.addChunkToCity(cityName, world, x, z)) {
            sender.sendMessage(new TextComponentString("Chunk added to the city " + cityName + "."));
        } else {
            if (cityManager.getCity(cityName) == null) {
                sender.sendMessage(new TextComponentString("City " + cityName + " does not exitst."));
            } else if (cityManager.getCity(cityName).getChunkCount() >= cityManager.getMaxChunksPerCity()) {
                sender.sendMessage(new TextComponentString("The maximum number of chunks for the city has been reached " + cityName + "."));
            } else {
                sender.sendMessage(new TextComponentString("This chunk already belongs to another city."));
            }
        }
    }

    // Метод для подсчёта чанков в городе не требуется, т.к. уже есть getChunkCount()
}
