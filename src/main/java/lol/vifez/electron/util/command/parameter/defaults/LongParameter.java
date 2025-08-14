package lol.vifez.electron.util.command.parameter.defaults;

import lol.vifez.electron.util.CC;
import lol.vifez.electron.util.command.parameter.ParameterType;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Emilxyz (langgezockt@gmail.com)
 * 15.01.2020 / 22:49
 * iLib / lol.vifez.electron.util.commandapi.parameter.defaults
 */

public class LongParameter implements ParameterType<Long> {

    @Override
    public Long parse(CommandSender sender, String source) {
        Long value;
        try {
            value = Long.parseLong(source);
        } catch (NumberFormatException e) {
            sender.sendMessage(CC.YELLOW + source + CC.RED + " is not a valid number.");
            return null;
        }
        return value;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, List<String> flags) {
        return new ArrayList<>();
    }
}
