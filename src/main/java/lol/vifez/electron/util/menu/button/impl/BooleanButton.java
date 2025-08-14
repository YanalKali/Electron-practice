package lol.vifez.electron.util.menu.button.impl;

import lol.vifez.electron.util.ItemBuilder;
import lol.vifez.electron.util.menu.button.Button;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public class BooleanButton<T> extends Button {
    private ItemStack item;
    private final T target;
    private final String trait;
    private final BiConsumer<T, Boolean> writeFunction;
    private final Function<T, Boolean> readFunction;
    private final Consumer<T> saveFunction;

    public BooleanButton(ItemStack item, T target, String trait, BiConsumer<T, Boolean> writeFunction, Function<T, Boolean> readFunction) {
        this(item, target, trait, writeFunction, readFunction, (i) -> {});
    }

    public BooleanButton(T target, String trait, BiConsumer<T, Boolean> writeFunction, Function<T, Boolean> readFunction) {
        this(target, trait, writeFunction, readFunction, i -> {});
    }

    public BooleanButton(T target, String trait, BiConsumer<T, Boolean> toDo, Function<T, Boolean> getFrom, Consumer<T> saveFunction) {
        this.target = target;
        this.trait = trait;
        this.writeFunction = toDo;
        this.readFunction = getFrom;
        this.saveFunction = saveFunction;
    }

    public BooleanButton(ItemStack item, T target, String trait, BiConsumer<T, Boolean> toDo, Function<T, Boolean> getFrom, Consumer<T> saveFunction) {
        this.item = item;
        this.target = target;
        this.trait = trait;
        this.writeFunction = toDo;
        this.readFunction = getFrom;
        this.saveFunction = saveFunction;
    }


    @Override
    public ItemStack getItem(Player p0) {
        boolean current = this.readFunction.apply(this.target);
        return item == null ? new ItemBuilder(Material.PAPER)
                .name((this.readFunction.apply(this.target) ? "&a" : "&c") + this.trait)
                .lore(Arrays.asList("",
                                current ? "&a✔ Enabled"
                                        : "&7Enabled",
                                !current ? "&c✘ Disabled"
                                        : "&7Disabled"))
                .build() :
                new ItemBuilder(item)
                        .lore(Arrays.asList("",
                                current ? "&a✔ Enabled"
                                        : "&7Enabled",
                                !current ? "&c✘ Disabled"
                                        : "&7Disabled"
                        )).build();
    }

    @Override
    public void onClick(final Player player, final int i, final ClickType clickType) {
        boolean current = this.readFunction.apply(this.target);
        this.writeFunction.accept(this.target, !current);
        this.saveFunction.accept(this.target);
    }

    @Override
    public boolean cancelEvent(Player player, int slot, ClickType clickType) {
        return true;
    }
}