package lol.vifez.electron.leaderboard;

import lol.vifez.electron.kit.Kit;
import lol.vifez.electron.profile.Profile;
import lol.vifez.electron.profile.ProfileManager;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

// NOTE: This class is an old class so if you want to recode, be my guest!
public class Leaderboard {

    private final ProfileManager profileManager;

    public Leaderboard(ProfileManager profileManager) {
        this.profileManager = profileManager;
    }

    public List<Profile> getLeaderboard(Kit kit) {
        List<Profile> allProfiles = new ArrayList<>(profileManager.getProfiles().values());
        allProfiles.sort(Comparator.comparingInt((Profile o) -> o.getElo(kit)).reversed());

        return allProfiles.stream()
                .limit(10)
                .collect(Collectors.toList());
    }

    public String[] getLeaderboardLayout(Kit kit) {
        List<Profile> leaderboard = getLeaderboard(kit);
        String[] leaderboardLayout = new String[11];

        leaderboardLayout[0] = "&r";

        for (int i = 0; i < leaderboard.size(); i++) {
            Profile profile = leaderboard.get(i);

            if (profile == null) {
                leaderboardLayout[i] = "&7&l" + (i + 1) + ". &bN/A" + " &7- &b0";
            } else {
                leaderboardLayout[i] = "&7&l" + (i + 1) + ". &b" + profile.getName() + " &7- &b" + profile.getElo(kit);
            }
        }

        return leaderboardLayout;
    }
}