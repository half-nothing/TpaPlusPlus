package net.superricky.tpaplusplus.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;
import net.superricky.tpaplusplus.util.manager.PlayerBlockingManager;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public class TPBlockCommand {
    private TPBlockCommand() {
    }

    private static int blockPlayer(CommandSourceStack source, ServerPlayer blockedPlayer) throws CommandSyntaxException {
        PlayerBlockingManager.blockPlayer(source.getPlayerOrException(), blockedPlayer);
        return 1;
    }

    public static void onRegisterCommandEvent(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext registry, Commands.CommandSelection selection) {
        dispatcher.register(literal("tpblock")
                .then(argument("player", EntityArgument.player())
                        .executes(context -> blockPlayer(context.getSource(), EntityArgument.getPlayer(context, "player")))));
    }
}