package lol.vifez.electron.util;

import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Credits to RevereInc, Alley
 * Original source: https://github.com/RevereInc/alley-practice/blob/master/src/main/java/dev/revere/alley/common/serializer/Serializer.java
 */

@UtilityClass
public class SerializationUtil {

    public String serializeLocation(Location location) {
        if (location == null || location.getWorld() == null) return null;
        return String.join(", ",
                location.getWorld().getName(),
                String.valueOf(location.getX()),
                String.valueOf(location.getY()),
                String.valueOf(location.getZ()),
                String.valueOf(location.getYaw()),
                String.valueOf(location.getPitch())
        );
    }

    public Location deserializeLocation(String data) {
        if (data == null || data.isEmpty()) return null;

        String[] parts = data.split(", ");
        if (parts.length < 6) return null;

        World world = Bukkit.getWorld(parts[0]);
        if (world == null) return null;

        return new Location(
                world,
                Double.parseDouble(parts[1]),
                Double.parseDouble(parts[2]),
                Double.parseDouble(parts[3]),
                Float.parseFloat(parts[4]),
                Float.parseFloat(parts[5])
        );
    }

    public String serializeItemStackArray(ItemStack[] items) {
        if (items == null) return "";
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream)) {

            dataOutput.writeInt(items.length);
            for (ItemStack item : items) {
                dataOutput.writeObject(item);
            }

            dataOutput.flush();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public ItemStack[] deserializeItemStackArray(String data) {
        if (data == null || data.isEmpty()) return new ItemStack[0];
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
             BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream)) {

            ItemStack[] items = new ItemStack[dataInput.readInt()];
            for (int i = 0; i < items.length; i++) {
                items[i] = (ItemStack) dataInput.readObject();
            }

            return items;
        } catch (Exception e) {
            e.printStackTrace();
            return new ItemStack[0];
        }
    }

    public String serializeBlockLocation(Location location) {
        if (location == null || location.getWorld() == null) return null;
        return String.join(", ",
                location.getWorld().getName(),
                String.valueOf(location.getBlockX()),
                String.valueOf(location.getBlockY()),
                String.valueOf(location.getBlockZ())
        );
    }

    public Location deserializeBlockLocation(String data) {
        if (data == null || data.isEmpty()) return null;

        String[] parts = data.split(", ");
        if (parts.length < 4) return null;

        World world = Bukkit.getWorld(parts[0]);
        if (world == null) return null;

        return new Location(
                world,
                Double.parseDouble(parts[1]),
                Double.parseDouble(parts[2]),
                Double.parseDouble(parts[3])
        );
    }

    public List<String> serializeLocations(List<Location> locations) {
        if (locations == null) return null;
        List<String> result = new ArrayList<>();
        for (Location loc : locations) {
            result.add(serializeBlockLocation(loc));
        }
        return result;
    }

    public List<Location> deserializeLocations(List<String> data) {
        if (data == null) return null;
        List<Location> result = new ArrayList<>();
        for (String s : data) {
            result.add(deserializeBlockLocation(s));
        }
        return result;
    }
}