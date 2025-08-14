package lol.vifez.electron.util.command.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/**
 * @author Emilxyz (langgezockt@gmail.com)
 * 03.06.2020 / 11:23
 * iLib / lol.vifez.electron.util.commandapi.data
 */

@Getter
@AllArgsConstructor
public class FlagData implements Data {

    private final List<String> names;
    private final boolean defaultValue;
    private final String description;
    private final boolean hidden;
    private final int index;

}
