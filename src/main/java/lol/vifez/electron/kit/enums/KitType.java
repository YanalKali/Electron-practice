package lol.vifez.electron.kit.enums;

/*
 * Copyright (c) 2025 Vifez. All rights reserved.
 * Unauthorized use or distribution is prohibited.
 */

public enum KitType {
    REGULAR,
    BUILD,
    BED_FIGHT,
    BOXING,
    WATER_KILL;

    public KitType[] getAll() {
        return new KitType[] {REGULAR, BUILD, BED_FIGHT, BOXING, WATER_KILL};
    }
}