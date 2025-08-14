package lol.vifez.electron.util.adapter;

import com.google.gson.*;
import lol.vifez.electron.util.SerializationUtil;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Type;

/*
 * Copyright (c) 2025 Vifez. All rights reserved.
 * Unauthorized use or distribution is prohibited.
 */

public class ItemStackArrayTypeAdapter implements JsonSerializer<ItemStack[]>, JsonDeserializer<ItemStack[]> {

    @Override
    public ItemStack[] deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return SerializationUtil.deserializeItemStackArray(jsonElement.getAsString());
    }

    @Override
    public JsonElement serialize(ItemStack[] itemStacks, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(SerializationUtil.serializeItemStackArray(itemStacks));
    }
}
