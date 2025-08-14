package lol.vifez.electron.kit.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import lol.vifez.electron.Practice;
import lol.vifez.electron.kit.menu.kiteditor.KitSelectionMenu;
import lol.vifez.electron.util.CC;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
@CommandAlias("kiteditor")
@CommandPermission("electron.kit.editor")
public class KitEditorCommand extends BaseCommand {

    private final Practice instance;

    @Default
    public void onKitEditorCommand(CommandSender sender) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            openKitEditor(player);
        } else {
            CC.sendMessage(sender, "&cThis command can only be run by a player.");
        }
    }

    private void openKitEditor(Player player) {
        KitSelectionMenu KitSelectionMenuMenu = new KitSelectionMenu(instance);

        KitSelectionMenuMenu.openMenu(player);
    }
}