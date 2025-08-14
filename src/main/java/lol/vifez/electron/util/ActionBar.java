package lol.vifez.electron.util;

import net.minecraft.server.v1_8_R3.ChatComponentText;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

/*
 * Copyright (c) 2025 Vifez. All rights reserved.
 * Unauthorized use or distribution is prohibited.
 */

public class ActionBar {
    public static void sendActionBar(Player player, String message) {
        if (player == null) return;
        ChatComponentText chatComponent = new ChatComponentText(message);
        PacketPlayOutChat packet = new PacketPlayOutChat(chatComponent, (byte) 2);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }
}
