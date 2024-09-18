package net.superricky.tpaplusplus.command

import net.minecraft.command.argument.EntityArgumentType
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text
import net.superricky.tpaplusplus.utility.Context
import net.superricky.tpaplusplus.utility.TextColorPallet

object CommandHelper {

    fun requestNotFound(sender: ServerPlayerEntity) =
        sender.sendMessage(Text.translatable("command.error.request.notfound.all").setStyle(TextColorPallet.error))

    fun requestNotFound(sender: ServerPlayerEntity, receiver: ServerPlayerEntity) =
        sender.sendMessage(
            Text.translatable("command.error.request.notfound.target", receiver.name).setStyle(TextColorPallet.error)
        )

    fun checkSenderReceiver(context: Context): Triple<CommandResult, ServerPlayerEntity?, ServerPlayerEntity?> {
        val source = context.source
        val sender = source.player
        val receiver = EntityArgumentType.getPlayer(context, "player")
        val result = checkSenderReceiver(sender, receiver)
        return when (result) {
            CommandResult.SELF_CHECK_ERROR -> {
                source.sendError(Text.translatable("command.error.self"))
                Triple(CommandResult.SELF_CHECK_ERROR, null, null)
            }

            CommandResult.SENDER_NOT_EXIST -> {
                source.sendError(Text.translatable("command.error.sender.not_exist"))
                Triple(CommandResult.SENDER_NOT_EXIST, null, null)
            }

            CommandResult.RECEIVER_NOT_EXIST -> {
                source.sendError(Text.translatable("command.error.target.not_exist"))
                Triple(CommandResult.RECEIVER_NOT_EXIST, null, null)
            }

            CommandResult.NORMAL -> {
                Triple(CommandResult.NORMAL, sender, receiver)
            }
        }
    }

    fun checkSenderReceiver(sender: ServerPlayerEntity?, receiver: ServerPlayerEntity?): CommandResult {
        sender ?: return CommandResult.SENDER_NOT_EXIST
        receiver ?: return CommandResult.RECEIVER_NOT_EXIST
        if (sender == receiver) {
            return CommandResult.SELF_CHECK_ERROR
        }
        return CommandResult.NORMAL
    }
}
