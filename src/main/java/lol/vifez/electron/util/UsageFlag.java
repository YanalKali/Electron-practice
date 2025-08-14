package lol.vifez.electron.util;

import lombok.Getter;

public class UsageFlag {

    @Getter private String flag;
    @Getter private String description;

    public UsageFlag(String flag, String description) {
        this.flag = flag;
        this.description = description;
    }
}
