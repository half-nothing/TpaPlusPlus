package net.superricky.tpaplusplus.modevents;

import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.common.Mod;

@Cancelable
@Mod.EventBusSubscriber
public class TeleportRequestAcceptEvent extends Event {
    private ServerPlayer sender;
    private ServerPlayer receiver;

    public TeleportRequestAcceptEvent(ServerPlayer sender, ServerPlayer receiver) {
        this.sender = sender;
        this.receiver = receiver;
    }


    public ServerPlayer getSender() {
        return sender;
    }

    public ServerPlayer getReceiver() {
        return receiver;
    }
}