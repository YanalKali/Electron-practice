package lol.vifez.electron.util.command;

import lol.vifez.electron.Practice;
import lol.vifez.electron.util.CC;
import lol.vifez.electron.util.command.annotation.Command;
import lol.vifez.electron.util.command.annotation.CommandCooldown;
import lol.vifez.electron.util.command.annotation.Flag;
import lol.vifez.electron.util.command.annotation.Param;
import lol.vifez.electron.util.command.bukkit.BukkitCommandNode;
import lol.vifez.electron.util.command.data.Data;
import lol.vifez.electron.util.command.data.FlagData;
import lol.vifez.electron.util.command.data.ParameterData;
import lol.vifez.electron.util.command.parameter.ParameterType;
import lol.vifez.electron.util.command.parameter.defaults.*;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;

import java.lang.reflect.*;
import java.util.*;

/**
 * @author Emilxyz (langgezockt@gmail.com)
 * 15.01.2020 / 22:50
 * iLib / lol.vifez.electron.util.commandapi
 */

public class CommandService {

    public static String NO_PERMISSION_MESSAGE = CC.RED + "You don't have permission to execute this command.";
    public static final Map<String, CommandNode> REGISTERED_COMMANDS = new HashMap<>();

    private static final Map<Class<?>, ParameterType<?>> PARAMETER_MAP = new HashMap<>();
    private static SimpleCommandMap COMMAND_MAP;

    static {
        ParameterType<?> type;
        registerParameter(Boolean.TYPE, type = new BooleanParameter());
        registerParameter(Boolean.class, type);
        registerParameter(Double.TYPE, type = new DoubleParameter());
        registerParameter(Double.class, type);
        registerParameter(Float.TYPE, type = new FloatParameter());
        registerParameter(Float.class, type);
        registerParameter(Integer.TYPE, type = new IntegerParameter());
        registerParameter(Integer.class, type);
        registerParameter(Long.TYPE, type = new LongParameter());
        registerParameter(Long.class, type);
        registerParameter(Short.TYPE, type = new ShortParameter());
        registerParameter(Short.class, type);
        registerParameter(String.class, new StringParameter());
        registerParameter(Duration.class, new Duration.Type());
        registerParameter(World.class, new WorldParameter());
        registerParameter(GameMode.class, new GameModeParameter());
        registerParameter(EntityType.class, new EntityTypeParameter());
        registerParameter(Sound.class, new SoundParameter());
        registerParameter(Player.class, new PlayerParameter());
        registerParameter(PotionEffectType.class, new PotionEffectTypeParameter());
        registerParameter(World.Environment.class, new EnvironmentParameter());

        try {
            Field commandMapField = Bukkit.getPluginManager().getClass().getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
            COMMAND_MAP = (SimpleCommandMap) commandMapField.get(Bukkit.getPluginManager());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    public static void registerParameter(Class<?> clazz, ParameterType<?> parameterType) {
        PARAMETER_MAP.put(clazz, parameterType);
    }

    public static <T> ParameterType<T> getParameter(Class<? extends T> clazz) {
        return (ParameterType<T>) PARAMETER_MAP.get(clazz);
    }

    public static void register(JavaPlugin plugin, Object... objects) {
        for (Object object : objects) {
            register(object, plugin);
        }
    }

    private static void register(Object object, JavaPlugin plugin) {
        for (Method method : object.getClass().getDeclaredMethods()) {
            if ((method.getParameterCount() < 1) || (!method.isAnnotationPresent(Command.class)) || (!CommandSender.class.isAssignableFrom(method.getParameterTypes()[0]))) {
                continue;
            }

            CommandCooldown commandCooldown = null;
            if (method.isAnnotationPresent(CommandCooldown.class)) {
                commandCooldown = method.getAnnotation(CommandCooldown.class);
            }

            Command command = method.getAnnotation(Command.class);
            List<String> flags = new ArrayList<>();
            List<Data> parameters = new ArrayList<>();
            if (method.getParameterCount() > 1) {
                for (int i = 1; i < method.getParameterCount(); i++) {
                    Parameter parameter = method.getParameters()[i];
                    if (parameter.isAnnotationPresent(Param.class)) {
                        Param param = parameter.getAnnotation(Param.class);
                        parameters.add(
                                new ParameterData(
                                        param.name(),
                                        param.defaultValue(),
                                        parameter.getType(),
                                        param.wildcard(),
                                        PARAMETER_MAP.get(parameter.getType()),
                                        Arrays.asList(param.completionFlags()),
                                        i - 1
                                )
                        );
                    } else if (parameter.isAnnotationPresent(Flag.class)) {
                        Flag flag = parameter.getAnnotation(Flag.class);
                        parameters.add(
                                new FlagData(
                                        Arrays.asList(flag.names()),
                                        flag.defaultValue(),
                                        flag.description(),
                                        flag.hidden(),
                                        i
                                )
                        );
                        flags.addAll(Arrays.asList(flag.names()));
                    } else {
                        Practice.getInstance().getLogger().warning("Failed to register command " + command.names()[0] +
                                ". Every parameter (except the CommandSender) must either have the @Flag or @Param " +
                                "annotation (" +
                                method.getDeclaringClass().getName() + ":" + method.getName() + ")");
                    }
                }
            }

            CommandNode node = null;
            CommandNode child = null;

            for (String name : command.names()) {
                if (!name.contains(" ")) {
                    if (node == null) {
                        if (REGISTERED_COMMANDS.containsKey(name.toLowerCase()))
                            node = REGISTERED_COMMANDS.get(name.toLowerCase());
                        else {
                            node = new CommandNode();
                            node.setLabel(name.toLowerCase());
                            registerBukkitCommand(node, plugin);
                        }
                    } else {
                        if (!node.getAliases().contains(name.toLowerCase())
                                && !node.getLabel().equalsIgnoreCase(name)) {
                            node.getAliases().add(name.toLowerCase());
                            registerBukkitCommand(node, plugin);
                        }
                    }

                    if (node.getMethod() == null) {
                        node.setMethod(method);
                        node.setObject(object);
                        node.setAsync(command.async());
                        node.setHidden(command.hidden());
                        node.setPermission(command.permission());
                        node.setPlayerOnly(command.playerOnly());
                        node.setDescription(command.description());
                        node.setParameters(parameters);
                        node.setFlags(flags);
                        node.setCommandCooldown(commandCooldown);
                    }

                    REGISTERED_COMMANDS.put(name.toLowerCase(), node);
                    continue;
                }

                String[] args = name.split(" ");

                if (node == null) {
                    if (REGISTERED_COMMANDS.containsKey(args[0].toLowerCase()))
                        node = REGISTERED_COMMANDS.get(args[0].toLowerCase());
                    else {
                        node = new CommandNode();
                        node.setLabel(args[0].toLowerCase());
                        registerBukkitCommand(node, plugin);
                    }
                } else if (!node.getAliases().contains(args[0].toLowerCase())
                        && !node.getLabel().equalsIgnoreCase(args[0])) {
                    node.getAliases().add(args[0].toLowerCase());
                    registerBukkitCommand(node, plugin);
                }

                for (int i = 1; i < args.length; i++) {
                    String subCommand = args[i];

                    if (child == null) {
                        if (node.getChild(subCommand.toLowerCase()) != null)
                            child = node.getChild(subCommand.toLowerCase());
                        else {
                            child = new CommandNode();
                            child.setLabel(subCommand.toLowerCase());
                            node.registerChild(child);
                        }
                    } else if (!child.getAliases().contains(subCommand.toLowerCase())
                            && !child.getLabel().equalsIgnoreCase(subCommand))
                        child.getAliases().add(subCommand.toLowerCase());

                    if (i == args.length - 1) {
                        child.setMethod(method);
                        child.setObject(object);
                        child.setAsync(command.async());
                        child.setHidden(command.hidden());
                        child.setPermission(command.permission());
                        child.setPlayerOnly(command.playerOnly());
                        child.setDescription(command.description());
                        child.setParameters(parameters);
                        child.setCommandCooldown(commandCooldown);
                        child.setFlags(flags);
                    } else {
                        node = child;
                        child = null;
                    }
                }

            }

        }
    }

    private static void registerBukkitCommand(CommandNode node, JavaPlugin plugin) {
        BukkitCommandNode bukkitNode = new BukkitCommandNode(node, plugin);

        REGISTERED_COMMANDS.put(node.getLabel().toLowerCase(), node);
        node.getAliases().forEach(alias -> REGISTERED_COMMANDS.put(alias.toLowerCase(), node));

        PluginCommand pluginCommand = getPluginCommmand(bukkitNode, plugin);
        if (pluginCommand != null) {
            pluginCommand.setExecutor(bukkitNode);
            pluginCommand.setTabCompleter(bukkitNode);
            setFieldValue(pluginCommand, "aliases", node.getAliases());
            setFieldValue(pluginCommand, "activeAliases", node.getAliases());
            setFieldValue(pluginCommand, "usageMessage", "");
            setFieldValue(pluginCommand, "description", node.getDescription());
            if (COMMAND_MAP.register(plugin.getName(), pluginCommand))
                Practice.getInstance().getLogger().info("Registered command " + node.getFullLabel());
        } else {
            Practice.getInstance().getLogger().warning("Failed to register command " + node.getFullLabel());
        }
    }

    private static PluginCommand getPluginCommmand(BukkitCommandNode command, JavaPlugin plugin) {
        try {
            Constructor<PluginCommand> constructor = PluginCommand.class.getDeclaredConstructor(String.class,
                    Plugin.class);
            constructor.setAccessible(true);
            return constructor.newInstance(command.getNode().getLabel(), plugin);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void setFieldValue(PluginCommand pluginCommand, String fieldName, Object value) {
        try {
            Field field = pluginCommand.getClass().getSuperclass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(pluginCommand, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
