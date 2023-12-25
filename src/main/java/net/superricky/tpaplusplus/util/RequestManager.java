package net.superricky.tpaplusplus.util;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.superricky.tpaplusplus.Messages;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class RequestManager {
    public static final Set<Request> requestSet = new HashSet<>();

    public static boolean isPlayerIdentical(ServerPlayer player1, ServerPlayer player2) {
        return player1.getUUID().equals(player2.getUUID());
    }


    // Send command is run by the sender, hence why its in the sender's point of view
    public static void sendTeleportRequest(ServerPlayer sender, ServerPlayer receiver, boolean isHereRequest) {
        if (isPlayerIdentical(sender, receiver)) {
            sender.sendSystemMessage(Component.literal(Messages.ERR_NO_SELF_TELEPORT.get()));
            return;
        }

        if (isHereRequest) {
            Request request = new Request(sender, receiver, true);
            sender.sendSystemMessage(Component.literal(String.format(Messages.SENDER_SENT_TPAHERE.get(), receiver.getName().getString())));
            receiver.sendSystemMessage(Component.literal(String.format(Messages.RECEIVER_GOT_TPAHERE.get(), sender.getName().getString())));

            requestSet.add(request);

            AsyncTaskManager.scheduleTeleportTimeout(request);
            return;
        }

        Request request = new Request(sender, receiver, false);

        sender.sendSystemMessage(Component.literal(String.format(Messages.SENDER_SENT_TPA.get(), receiver.getName().getString())));
        receiver.sendSystemMessage(Component.literal(String.format(Messages.RECEIVER_GOT_TPA.get(), sender.getName().getString())));

        requestSet.add(request);

        AsyncTaskManager.scheduleTeleportTimeout(request);
    }

    // Deny command is run by the receiver, hence why it's in the receiver's point of view.
    private static void denyFunctionality(Request request, ServerPlayer receiver) {
        if (Objects.isNull(request)) {
            receiver.sendSystemMessage(Component.literal(Messages.ERR_REQUEST_NOT_FOUND.get()));
            return;
        }

        request.getReceiver().sendSystemMessage(Component.literal(String.format(Messages.RECEIVER_DENIES_TPA.get(), request.getSender().getName().getString())));
        request.getSender().sendSystemMessage(Component.literal(String.format(Messages.SENDER_GOT_DENIED_TPA.get(), request.getReceiver().getName().getString())));

        requestSet.remove(request);
    }
    public static void denyTeleportRequest(ServerPlayer receiver) {
        Request request = RequestGrabUtil.getReceiverRequest(receiver);
        denyFunctionality(request, receiver);
    }

    // Deny command is run by the receiver, hence why it's in the receiver's point of view.
    public static void denyTeleportRequest(ServerPlayer receiver, ServerPlayer sender) {
        Request request = RequestGrabUtil.getReceiverRequest(receiver, sender);
        denyFunctionality(request, receiver);
    }

    // Cancel command is run by the sender, hence why it's in the sender's point of view.
    private static void cancelFunctionality(Request request, ServerPlayer sender) {
        if (Objects.isNull(request)) {
            sender.sendSystemMessage(Component.literal(Messages.ERR_REQUEST_NOT_FOUND.get()));
            return;
        }

        request.getSender().sendSystemMessage(Component.literal(String.format(Messages.SENDER_CANCELS_TPA.get(), request.getReceiver().getName().getString())));
        request.getReceiver().sendSystemMessage(Component.literal(String.format(Messages.RECEIVER_GOT_CANCELLED_TPA.get(), request.getSender().getName().getString())));

        requestSet.remove(request);
    }
    public static void cancelTeleportRequest(ServerPlayer sender) {
        Request request = RequestGrabUtil.getSenderRequest(sender);
        cancelFunctionality(request, sender);
    }

    public static void cancelTeleportRequest(ServerPlayer sender, ServerPlayer receiver) {
        Request request = RequestGrabUtil.getSenderRequest(sender, receiver);
        cancelFunctionality(request, sender);
    }

    // Accept command is run by the sender, hence why it's in the sender's point of view.
    private static void acceptFunctionality(Request request, ServerPlayer receiver) {
        if (Objects.isNull(request)) {
            receiver.sendSystemMessage(Component.literal(Messages.ERR_REQUEST_NOT_FOUND.get()));
            return;
        }

        receiver.sendSystemMessage(Component.literal(String.format(Messages.RECEIVER_ACCEPTS_TPA.get(), request.getSender().getName().getString())));
        request.getSender().sendSystemMessage(Component.literal(String.format(Messages.SENDER_GOT_ACCEPTED_TPA.get(), request.getSender().getName().getString())));

        teleport(request);

        requestSet.remove(request);
    }
    public static void acceptTeleportRequest(ServerPlayer receiver) {
        Request request = RequestGrabUtil.getReceiverRequest(receiver);
        acceptFunctionality(request, receiver);
    }

    public static void acceptTeleportRequest(ServerPlayer receiver, ServerPlayer sender) {
        Request request = RequestGrabUtil.getReceiverRequest(receiver, sender);
        acceptFunctionality(request, receiver);
    }

    public static void teleport(Request request) {
        ServerPlayer sender = request.getSender();
        ServerPlayer receiver = request.getReceiver();

        // /tpahere
        if (request.isHereRequest()) {
            receiver.teleportTo(sender.serverLevel(), sender.getX(), sender.getY(), sender.getZ(), sender.getYRot(), sender.getXRot());
        }

        // /tpa
        sender.teleportTo(receiver.serverLevel(), receiver.getX(), receiver.getY(), receiver.getZ(), receiver.getYRot(), receiver.getXRot());
    }

    /**
     * A utility class used inside the request manager for grabbing teleport requests from the requestSet, based on the players point of view.
     * This is important as since we have commands that the receiver runs, that also have to grab the same teleport request that was sent by the sender,
     * and vice-versa, meaning that we have to use something like this since there is no one-size-fits-all solution here.
     */
    private static class RequestGrabUtil {
        @Nullable
        public static Request getSenderRequest(ServerPlayer sender) {
            for (Request request : RequestManager.requestSet) {
                if (isPlayerIdentical(request.getSender(), sender)) {
                    return request;
                }
            }
            return null;
        }

        @Nullable
        public static Request getSenderRequest(ServerPlayer sender, ServerPlayer receiver) {
            for (Request request : RequestManager.requestSet) {
                if (isPlayerIdentical(request.getSender(), sender) &&
                        isPlayerIdentical(request.getReceiver(), receiver)) {
                    return request;
                }
            }
            return null;
        }

        @Nullable
        public static Request getReceiverRequest(ServerPlayer receiver) {
            for (Request request : RequestManager.requestSet) {
                if (isPlayerIdentical(request.getReceiver(), receiver)) {
                    return request;
                }
            }
            return null;
        }

        @Nullable
        public static Request getReceiverRequest(ServerPlayer receiver, ServerPlayer sender) {
            for (Request request : RequestManager.requestSet) {
                if (isPlayerIdentical(request.getReceiver(), receiver) &&
                        isPlayerIdentical(request.getSender(), sender)) {
                    return request;
                }
            }
            return null;
        }
    }
}