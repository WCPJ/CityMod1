// src/main/java/com/example/examplemod/events/ChunkEventHandler.java
package com.example.examplemod.events;

import com.example.examplemod.cities.CityManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class ChunkEventHandler {
    private final CityManager cityManager;

    public ChunkEventHandler(CityManager cityManager) {
        this.cityManager = cityManager;
    }

    @SubscribeEvent
    public void onBlockInteract(PlayerInteractEvent.RightClickBlock event) {
        net.minecraft.world.World world = event.getWorld();
        net.minecraft.util.math.BlockPos pos = event.getPos();
        EntityPlayer player = event.getEntityPlayer();
        int x = pos.getX() >> 4;
        int z = pos.getZ() >> 4;

        String cityName = cityManager.getCityByChunk(world, x, z);
        if (cityName != null) {
            // Здесь можно добавить проверку прав игрока
            // Для примера, будем запрещать всем, кроме операторов сервера
            if (!player.canUseCommand(2, "")) {
                event.setCanceled(true);
                player.sendMessage(new TextComponentString("You can't interact with this chunk, it belongs to the city " + cityName + "."));
            }
        }
    }
}
