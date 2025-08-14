package lol.vifez.electron.profile.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Optional;
import lol.vifez.electron.util.CC;
import lombok.RequiredArgsConstructor;
import lol.vifez.electron.profile.Profile;
import lol.vifez.electron.profile.menu.ProfileMenu;
import lol.vifez.electron.profile.ProfileManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
@CommandAlias("profile")
public class ProfileCommand extends BaseCommand {

    private final ProfileManager profileManager;

    @Default
    public void onProfileCommand(Player player, @Optional String targetName) {
        Profile targetProfile;

        if (targetName == null) {
            targetProfile = profileManager.getProfile(player.getName());
        } else {
            Player targetPlayer = Bukkit.getPlayer(targetName);
            if (targetPlayer == null) {
                player.sendMessage(CC.translate("&cPlayer not found!"));
                return;
            }
            targetProfile = profileManager.getProfile(targetPlayer.getName());
        }

        if (targetProfile == null) {
            player.sendMessage(CC.translate("&cProfile not found!"));
            return;
        }

        new ProfileMenu(targetProfile).openMenu(player);
    }
}