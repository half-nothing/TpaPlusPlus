package net.superricky.tpaplusplus.command.commands

import net.minecraft.server.command.CommandManager.literal
import net.minecraft.text.Text
import net.superricky.tpaplusplus.TpaPlusPlus
import net.superricky.tpaplusplus.async.*
import net.superricky.tpaplusplus.command.BuildableCommand
import net.superricky.tpaplusplus.command.CommandResult
import net.superricky.tpaplusplus.config.Config
import net.superricky.tpaplusplus.config.command.CommandCooldownSpec
import net.superricky.tpaplusplus.config.command.CommandDelaySpec
import net.superricky.tpaplusplus.config.command.CommandDistanceSpec
import net.superricky.tpaplusplus.config.command.CommandNameSpec
import net.superricky.tpaplusplus.utility.*

object BackCommand : AsyncCommand(), BuildableCommand {
    init {
        commandName = Config.getConfig()[CommandNameSpec.backCommand]
    }

    override fun build(): LiteralNode =
        literal(commandName)
            .executes { backRequest(it) }
            .build()

    override fun getCooldownTime(): Double = Config.getConfig()[CommandCooldownSpec.backCooldown]

    override fun getDelayTime(): Double = Config.getConfig()[CommandDelaySpec.backDelay]

    override fun getMinDistance(): Double = Config.getConfig()[CommandDistanceSpec.backDistance]

    private fun backRequest(context: Context): Int {
        val source = context.source
        val sender = source.player
        sender ?: return CommandResult.SENDER_NOT_EXIST.status
        val playerData = TpaPlusPlus.dataService.getPlayerData(sender)
        val lastDeathPos = playerData.lastDeathPos
        if (lastDeathPos.backed) {
            sender.sendMessage(
                Text.translatable("command.back.death_not_found").setStyle(TextColorPallet.error)
            )
            return CommandResult.NORMAL.status
        }
        if (!LimitationHelper.checkDimensionLimitation(sender, lastDeathPos.world.getWorld())) {
            sender.sendMessage(
                Text.translatable(
                    "command.error.cross_dim",
                    sender.getDimension().value.toString().literal().setStyle(TextColorPallet.errorVariant),
                    lastDeathPos.world.getWorld().value.toString().literal().setStyle(TextColorPallet.errorVariant)
                ).setStyle(TextColorPallet.error)
            )
            return CommandResult.NORMAL.status
        }
        AsyncCommandHelper.schedule(
            AsyncCommandData(
                AsyncRequest(AsyncCommandType.BACK, sender, null, sender),
                LevelBoundVec3(sender.getDimension(), sender.pos),
                AsyncCommandEventFactory
                    .addListener(AsyncCommandEvent.REQUEST_AFTER_DELAY) {
                        sender.sendMessage(
                            Text.translatable("command.back.teleporting").setStyle(TextColorPallet.primary)
                        )
                        AsyncCommandHelper.teleport(it)
                    }
            )
        )
        return CommandResult.NORMAL.status
    }
}
