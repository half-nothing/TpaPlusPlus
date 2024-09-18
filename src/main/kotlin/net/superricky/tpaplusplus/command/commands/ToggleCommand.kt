package net.superricky.tpaplusplus.command.commands

import kotlinx.coroutines.launch
import net.minecraft.server.command.CommandManager.literal
import net.minecraft.text.Text
import net.superricky.tpaplusplus.TpaPlusPlus
import net.superricky.tpaplusplus.async.AsyncCommandData
import net.superricky.tpaplusplus.command.AsyncCommand
import net.superricky.tpaplusplus.command.BuildableCommand
import net.superricky.tpaplusplus.command.CommandHelper
import net.superricky.tpaplusplus.config.Config
import net.superricky.tpaplusplus.config.command.CommandDistanceSpec
import net.superricky.tpaplusplus.config.command.CommandNameSpec
import net.superricky.tpaplusplus.database.DatabaseManager
import net.superricky.tpaplusplus.utility.*

object ToggleCommand : BuildableCommand, AsyncCommand {
    override fun build(): LiteralNode =
        literal(Config.getConfig()[CommandNameSpec.tpatoggleCommand])
            .then(
                literal("on")
                    .executes { switchToggle(it, true) }
            )
            .then(
                literal("off")
                    .executes { switchToggle(it, false) }
            )
            .executes { switchToggle(it) }
            .build()

    override fun checkWindupDistance(asyncCommandData: AsyncCommandData): Boolean =
        checkWindupDistance(
            asyncCommandData,
            ::getSenderDistance,
            Config.getConfig()[CommandDistanceSpec.toggleDistance]
        )

    private fun switchToggle(context: Context): Int {
        val source = context.source
        val (result, sender) = CommandHelper.checkSenderReceiver(context, CommandHelper::checkSender)
        if (result != CommandResult.NORMAL) return result.status
        sender!!
        TpaPlusPlus.launch {
            val blocked = DatabaseManager.playerSwitchBlock(sender.uuid)
            if (blocked) {
                source.sendFeedback({ Text.translatable("command.toggle.success.on") }, false)
            } else {
                source.sendFeedback({ Text.translatable("command.toggle.success.off") }, false)
            }
        }
        return CommandResult.NORMAL.status
    }

    private fun switchToggle(context: Context, blocked: Boolean): Int {
        val source = context.source
        val (result, sender) = CommandHelper.checkSenderReceiver(context, CommandHelper::checkSender)
        if (result != CommandResult.NORMAL) return result.status
        sender!!
        TpaPlusPlus.launch {
            if (blocked) {
                sender.toggleOn()
                source.sendFeedback({ Text.translatable("command.toggle.success.on") }, false)
            } else {
                sender.toggleOff()
                source.sendFeedback({ Text.translatable("command.toggle.success.off") }, false)
            }
        }
        return CommandResult.NORMAL.status
    }
}
