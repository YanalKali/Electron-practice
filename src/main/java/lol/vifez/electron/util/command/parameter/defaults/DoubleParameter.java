package lol.vifez.electron.util.command.parameter.defaults;

import lol.vifez.electron.util.CC;
import lol.vifez.electron.util.command.parameter.ParameterType;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Emilxyz (langgezockt@gmail.com)
 * 15.01.2020 / 22:45
 * iLib / lol.vifez.electron.util.commandapi.parameter.defaults
 */

public class DoubleParameter implements ParameterType<Double> {

    @Override
    public Double parse(CommandSender sender, String source) {
        Double value;
        try {
            value = Double.parseDouble(source);
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
