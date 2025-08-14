package lol.vifez.electron.util.command.data;

import lol.vifez.electron.util.command.parameter.ParameterType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

/**
 * @author Emilxyz (langgezockt@gmail.com)
 * 03.06.2020 / 10:29
 * iLib / lol.vifez.electron.util.commandapi.parameter
 */

@Getter
@AllArgsConstructor
public class ParameterData implements Data {

    private final String name;
    private final String defaultValue;
    private final Class<?> type;
    private final boolean wildCard;
    private final ParameterType parameterType;
    private final List<String> completionFlags;
    private final int index;

}
