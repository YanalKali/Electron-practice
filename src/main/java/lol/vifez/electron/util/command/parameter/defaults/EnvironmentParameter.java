package lol.vifez.electron.util.command.parameter.defaults;

import lol.vifez.electron.util.CC;
import lol.vifez.electron.util.command.parameter.ParameterType;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Emilxyz (langgezockt@gmail.com)
 * 15.01.2020 / 22:43
 * iLib / lol.vifez.electron.util.commandapi.parameter.defaults
 */

public class EnvironmentParameter implements ParameterType<World.Environment> {

    @Override
    public World.Environment parse(CommandSender sender, String source) {
        World.Environment environment;
        try {
            environment = World.Environment.valueOf(source);

        } catch (IllegalArgumentException exception) {
            sender.sendMessage(CC.RED + "Environment " + CC.YELLOW + source + CC.RED + " not found.");
            return null;
        }

        return environment;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, List<String> flags) {
        List<String> completions = new ArrayList<>();

        for (World.Environment value : World.Environment.values())
            completions.add(value.name());

        return completions;
    }
}
