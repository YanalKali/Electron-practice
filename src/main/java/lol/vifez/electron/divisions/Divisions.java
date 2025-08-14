package lol.vifez.electron.divisions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Divisions {
    SILVER_I("&7&lSilver I", 0),
    SILVER_II("&7&lSilver II", 300),
    SILVER_III("&7&lSilver III", 500),
    SILVER_IV("&7&lSilver IV", 700),
    SILVER_V("&7&lSilver V", 900),

    IRON_I("&f&lIron I", 1000),
    IRON_II("&f&lIron II", 1050),
    IRON_III("&f&lIron III", 1100),
    IRON_IV("&f&lIron IV", 1150),
    IRON_V("&f&lIron V", 1200),

    GOLD_I("&6&lGold I", 1225),
    GOLD_II("&6&lGold II", 1250),
    GOLD_III("&6&lGold III", 1275),
    GOLD_IV("&6&lGold IV", 1300),
    GOLD_V("&6&lGold V", 1350),

    DIAMOND_I("&b&lDiamond I", 1375),
    DIAMOND_II("&b&lDiamond II", 1400),
    DIAMOND_III("&b&lDiamond III", 1425),
    DIAMOND_IV("&b&lDiamond IV", 1450),
    DIAMOND_V("&b&lDiamond V", 1475),

    EMERALD_I("&a&lEmerald I", 1500),
    EMERALD_II("&a&lEmerald II", 1525),
    EMERALD_III("&a&lEmerald III", 1550),
    EMERALD_IV("&a&lEmerald IV", 1575),
    EMERALD_V("&a&lEmerald V", 1600),

    MASTER_I("&2&lMaster I", 1625),
    MASTER_II("&2&lMaster II", 1650),
    MASTER_III("&2&lMaster III", 1675),
    MASTER_IV("&2&lMaster IV", 1700),
    MASTER_V("&2&lMaster V", 1750),

    GRANDMASTER("&5&lGrand Master", 1800),
    GRANDMASTER_I("&5&lGrand Master I", 2000),

    LEGEND("&A&lLegend", 2300),
    SUPER_LEGEND("&A&lSuper Legend", 3000);

    private final String prettyName;
    private final int minimumElo;
}