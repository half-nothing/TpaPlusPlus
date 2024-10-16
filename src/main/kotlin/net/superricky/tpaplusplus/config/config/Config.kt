package net.superricky.tpaplusplus.config.config

import com.uchuhimo.konf.Config
import com.uchuhimo.konf.RequiredItem
import com.uchuhimo.konf.source.Source
import com.uchuhimo.konf.source.toml
import dev.architectury.platform.Platform
import net.minecraft.util.WorldSavePath
import net.superricky.tpaplusplus.GlobalConst
import net.superricky.tpaplusplus.TpaPlusPlus
import net.superricky.tpaplusplus.config.config.command.*
import java.nio.file.Path

object Config {
    private val config: Config = Config {
        addSpec(CommonSpec)
        addSpec(ColorSpec)
        addSpec(AdvancedSpec)
        addSpec(DatabaseSpec)
        addSpec(CommandEnableSpec)
        addSpec(CommandNameSpec)
        addSpec(CommandDelaySpec)
        addSpec(CommandCooldownSpec)
        addSpec(CommandDistanceSpec)
        addSpec(CommandLimitationsSpec)
    }
        .from.toml.resource(GlobalConst.CONFIG_FILE_NAME)
        .from.toml.watchFile(Platform.getConfigFolder().resolve(GlobalConst.CONFIG_FILE_PATH).toFile())
        .from.env()

    /**
     * Load and check config file.
     * Please call this function before use config.
     */
    fun loadAndVerifyConfig() {
        config.validateRequired()
        @Suppress("UNCHECKED_CAST")
        // CommandNameSpec are all RequiredItem<String> type, so this cast is safe
        CommandNameSpec.items.forEach { replaceCommand(it as? RequiredItem<String> ?: return@forEach) }
    }

    fun addLoadListener(listener: Function1<Source, Unit>) {
        config.beforeLoad { listener(it) }
    }

    /**
     * Replace / before command
     */
    private fun replaceCommand(item: RequiredItem<String>) {
        if (config[item].startsWith("/")) {
            config[item] = config[item].replace("/", "")
        }
    }

    fun getDatabasePath(): Path {
        val location = config[DatabaseSpec.location]
        return if (location != null) {
            Path.of(location)
        } else {
            TpaPlusPlus.server.getSavePath(WorldSavePath.ROOT)
        }
    }

    fun getTickRate(): Double {
        return if (AdvancedSpec.unblockingTickLoop.get()) {
            AdvancedSpec.asyncLoopRate.get().toDouble()
        } else {
            GlobalConst.SERVER_TICK_RATE
        }
    }

    fun <T> RequiredItem<T>.get(): T {
        return config[this]
    }
}
