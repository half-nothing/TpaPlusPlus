package net.superricky.tpaplusplus.command.commands

import net.minecraft.command.argument.EntityArgumentType
import net.minecraft.server.command.CommandManager.argument
import net.minecraft.server.command.CommandManager.literal
import net.superricky.tpaplusplus.async.*
import net.superricky.tpaplusplus.command.BuildableCommand
import net.superricky.tpaplusplus.command.CommandHelper
import net.superricky.tpaplusplus.command.CommandHelper.checkSenderReceiver
import net.superricky.tpaplusplus.command.CommandResult
import net.superricky.tpaplusplus.config.config.Config.get
import net.superricky.tpaplusplus.config.config.command.CommandCooldownSpec
import net.superricky.tpaplusplus.config.config.command.CommandDelaySpec
import net.superricky.tpaplusplus.config.config.command.CommandDistanceSpec
import net.superricky.tpaplusplus.config.config.command.CommandNameSpec
import net.superricky.tpaplusplus.utility.Context
import net.superricky.tpaplusplus.utility.LevelBoundVec3
import net.superricky.tpaplusplus.utility.LiteralNode
import net.superricky.tpaplusplus.utility.getDimension

object AcceptCommand : AsyncCommand(), BuildableCommand {
    init {
        commandName = CommandNameSpec.tpacceptCommand.get()
    }

    override fun build(): LiteralNode =
        literal(commandName)
            .then(
                argument("player", EntityArgumentType.player())
                    .executes { acceptCommandWithTarget(it) }
            )
            .executes { acceptCommand(it) }
            .build()

    override fun getCooldownTime(): Double = CommandCooldownSpec.acceptCooldown.get()

    override fun getDelayTime(): Double = CommandDelaySpec.acceptDelay.get()

    override fun getMinDistance(): Double = CommandDistanceSpec.acceptDistance.get()

    private fun acceptCommandWithTarget(context: Context): Int {
        fun asyncCommandCallback(result: AsyncCommandEvent, asyncCommandData: AsyncCommandData) {
            val asyncRequest = asyncCommandData.getRequest()
            if (result == AsyncCommandEvent.REQUEST_AFTER_DELAY) {
                val sender = asyncRequest.sender
                val receiver = asyncRequest.receiver!!
                if (AsyncCommandHelper.acceptRequest(sender, receiver) == AsyncCommandEvent.REQUEST_NOT_FOUND) {
                    CommandHelper.requestNotFound(sender, receiver)
                }
            }
        }

        val source = context.source
        val (result, sender, receiver) = checkSenderReceiver(context)
        if (result != CommandResult.NORMAL) return result.status
        sender!!
        receiver!!
        AsyncCommandHelper.schedule(
            AsyncCommandData(
                AsyncRequest(AsyncCommandType.ACCEPT, sender, receiver),
                LevelBoundVec3(sender.getDimension(), sender.pos),
                AsyncCommandEventFactory.addListener(AsyncCommandEvent.REQUEST_AFTER_DELAY) {
                    if (AsyncCommandHelper.acceptRequest(sender, receiver) == AsyncCommandEvent.REQUEST_NOT_FOUND) {
                        CommandHelper.requestNotFound(sender, receiver)
                    }
                }
            )
        )
        return CommandResult.NORMAL.status
    }

    private fun acceptCommand(context: Context): Int {
        val source = context.source
        val sender = source.player
        sender ?: return CommandResult.SENDER_NOT_EXIST.status
        AsyncCommandHelper.schedule(
            AsyncCommandData(
                AsyncRequest(AsyncCommandType.ACCEPT, sender),
                LevelBoundVec3(sender.getDimension(), sender.pos),
                AsyncCommandEventFactory.addListener(AsyncCommandEvent.REQUEST_AFTER_DELAY) {
                    if (AsyncCommandHelper.acceptRequest(sender) == AsyncCommandEvent.REQUEST_NOT_FOUND) {
                        CommandHelper.requestNotFound(sender)
                    }
                }
            )
        )
        return CommandResult.NORMAL.status
    }
}
