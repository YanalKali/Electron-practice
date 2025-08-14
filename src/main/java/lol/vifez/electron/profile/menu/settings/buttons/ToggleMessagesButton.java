package lol.vifez.electron.profile.menu.settings.buttons;

import lol.vifez.electron.Practice;
import lol.vifez.electron.profile.Profile;
import lol.vifez.electron.util.CC;
import lol.vifez.electron.util.ItemBuilder;
import lol.vifez.electron.util.menu.button.Button;
import lol.vifez.electron.util.menu.button.impl.EasyButton;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class ToggleMessagesButton {

    public static Button createToggleMessagesButton(Profile profile, Practice instance) {
        boolean privateMessagingEnabled = profile.isPrivateMessagingEnabled();
        ItemStack messagesItem = new ItemBuilder(Material.BOOK_AND_QUILL)
                .name(CC.translate("&ePrivate Messages"))
                .lore(Arrays.asList(
                        CC.translate("&7Toggle private messages."),
                        CC.translate("&7Current value: " + (privateMessagingEnabled ? "&aEnabled" : "&cDisabled")),
                        CC.translate("&r"),
                        CC.translate("&eClick to change!")
                ))
                .build();

        return new EasyButton(messagesItem, true, false, () -> {
            profile.setPrivateMessagingEnabled(!profile.isPrivateMessagingEnabled());
            Practice.getInstance().getProfileManager().save(profile);

            Player player = profile.getPlayer();
            if (profile.isPrivateMessagingEnabled()) {
                player.sendMessage(CC.translate("&aNow receiving private messages."));
            } else {
                player.sendMessage(CC.translate("&cNo longer receiving private messages."));
            }
        });
    }
}