package lol.vifez.electron.chat;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import lol.vifez.electron.Practice;
import lol.vifez.electron.profile.Profile;
import lol.vifez.electron.util.CC;
import org.bukkit.entity.Player;

/*
 * Copyright (c) 2025 Vifez. All rights reserved.
 * Unauthorized use or distribution is prohibited.
 */

@CommandAlias("message|msg")
@Description("Send a private message to another player.")
public class MessageCommand extends BaseCommand {

    @Default
    public void onMessageCommand(Player sender, @Flags("other") Player recipient, String message) {
        if (recipient == null) {
            sender.sendMessage(CC.translate("&cPlayer not found"));
            return;
        }

        if (sender.equals(recipient)) {
            sender.sendMessage(CC.translate("&cYou cannot message yourself."));
            return;
        }

        Profile senderProfile = Practice.get().getProfileManager().getProfile(sender.getUniqueId());

        if (!senderProfile.isPrivateMessagingEnabled()) {
            sender.sendMessage(CC.translate("&cPrivate messaging is disabled."));
            return;
        }

        senderProfile.setLastMessagedPlayer(recipient);

        recipient.sendMessage(CC.translate("&7(From &b" + sender.getName() + "&7) &f" + message));
        sender.sendMessage(CC.translate("&7(To &b" + recipient.getName() + "&7) &f" + message));
    }
}