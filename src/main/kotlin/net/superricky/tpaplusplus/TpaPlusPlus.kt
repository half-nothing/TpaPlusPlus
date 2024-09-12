package net.superricky.tpaplusplus

import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.server.MinecraftServer
import net.superricky.tpaplusplus.Const.CONFIG_FILE_NAME
import net.superricky.tpaplusplus.Const.CONFIG_FILE_PATH
import net.superricky.tpaplusplus.Const.CONFIG_FOLDER_PATH
import net.superricky.tpaplusplus.Const.MOD_ID
import net.superricky.tpaplusplus.Const.logger
import net.superricky.tpaplusplus.config.Config
import java.nio.file.Files

class TpaPlusPlus : ModInitializer {

    override fun onInitialize() {
        val version = FabricLoader.getInstance().getModContainer(MOD_ID).get().metadata.version
        logger.info("Initializing TPA++ ${version.friendlyString}")

        if (!Files.isDirectory(FabricLoader.getInstance().configDir.resolve(CONFIG_FOLDER_PATH))) {
            logger.info("Config folder not exist, Creating.")
            Files.createDirectories(FabricLoader.getInstance().configDir.resolve(CONFIG_FOLDER_PATH))
        }

        if (!Files.exists(FabricLoader.getInstance().configDir.resolve(CONFIG_FILE_PATH))) {
            logger.info("No config file, Creating")
            Files.copy(
                FabricLoader.getInstance().getModContainer(MOD_ID).get().findPath(CONFIG_FILE_NAME).get(),
                FabricLoader.getInstance().configDir.resolve(CONFIG_FILE_PATH)
            )
        }
        logger.info("Loading config file...")
        try {
            Config.loadAndVerifyConfig()
            logger.info("Config file loaded.")
        } catch (e: Exception) {
            logger.error("Error while loading config file", e)
            return
        }

        ServerLifecycleEvents.SERVER_STARTING.register(::serverStarting)
        ServerLifecycleEvents.SERVER_STOPPED.register(::serverStopped)
    }

    private fun serverStarting(server: MinecraftServer) {
        logger.info("Starting TPA++ server")
    }

    private fun serverStopped(server: MinecraftServer) {
        logger.info("Shutting down TPA++")
    }
}
