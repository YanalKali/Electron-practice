package lol.vifez.electron.util.command.parameter.defaults;

import lol.vifez.electron.util.CC;
import lol.vifez.electron.util.command.parameter.ParameterType;
import org.bukkit.command.CommandSender;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class PotionEffectTypeParameter implements ParameterType<PotionEffectType> {

    @Override
    public PotionEffectType parse(CommandSender sender, String source) {
        PotionEffectType type = PotionEffectType.getByName(source);

        if (type == null)
            sender.sendMessage(CC.format("&cPotion effect &e%s &cnot found.", source));

        return type;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, List<String> flags) {
        List<String> completions = new ArrayList<>();
        for (PotionEffectType type : PotionEffectType.values()) {
            if (type == null || type.getName() == null)
                continue;

            completions.add(type.getName());
        }
        return completions;
    }
}
