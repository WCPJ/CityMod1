package com.example.examplemod;

import com.example.examplemod.cities.CityManager;
import com.example.examplemod.cities.commands.*;
import com.example.examplemod.events.ChunkEventHandler;
import com.example.examplemod.events.ServerChunkEventHandler;
import com.example.examplemod.cities.render.CityBorderRenderer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.*;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

@Mod(modid = ExampleMod.MODID, name = ExampleMod.NAME, version = ExampleMod.VERSION)
public class ExampleMod {
    public static final String MODID = "examplemod";
    public static final String NAME = "Example Mod";
    public static final String VERSION = "1.0";

    // Логгер
    public static final Logger LOGGER = LogManager.getLogger(NAME);

    // Общие настройки
    public static final int MAX_CHUNKS_PER_CITY = 16;

    // Менеджер городов
    private CityManager cityManager;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        LOGGER.info("Initialization CityManager");
        // Инициализация CityManager
        cityManager = new CityManager(event, MAX_CHUNKS_PER_CITY);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        LOGGER.info("Registration of event handlers in the \"CityMod\" mod");
        // Регистрация обработчиков событий
        registerEventHandlers();
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        LOGGER.info("Commands registration");
        // Регистрация команд
        event.registerServerCommand(new CommandCreateCity(cityManager));
        event.registerServerCommand(new CommandDeleteCity(cityManager));
        event.registerServerCommand(new CommandAddChunk(cityManager));
        event.registerServerCommand(new CommandRemoveChunk(cityManager));
        event.registerServerCommand(new CommandListCities(cityManager));
        event.registerServerCommand(new CommandInviteMember(cityManager));
        event.registerServerCommand(new CommandRemoveMember(cityManager));
        event.registerServerCommand(new CommandSetRole(cityManager));
        event.registerServerCommand(new CommandKick(cityManager));
    }

    @Mod.EventHandler
    public void serverStopping(FMLServerStoppingEvent event) {
        LOGGER.info("Saving city data");
        // Сохранение данных при остановке сервера
        if (cityManager != null) {
            cityManager.saveData();
        }
    }

    private void registerEventHandlers() {
        // Регистрация обработчиков событий
        if (cityManager != null) {
            // Эти обработчики могут быть зарегистрированы на сервере
            MinecraftForge.EVENT_BUS.register(new ChunkEventHandler(cityManager));
            MinecraftForge.EVENT_BUS.register(new ServerChunkEventHandler(cityManager));
        }

        // Регистрация клиентских обработчиков событий
        // Важно: Этот обработчик должен быть зарегистрирован только на клиенте
        // Переместите этот код в клиентскую часть вашего мода
        if (FMLCommonHandler.instance().getEffectiveSide().isClient()) {
            MinecraftForge.EVENT_BUS.register(new CityBorderRenderer(cityManager));
        }
    }
}
