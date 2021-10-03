package xyz.mlserver.mc.util.version;

import com.viaversion.viaversion.api.Via;
import org.bukkit.entity.Player;
import xyz.mlserver.mc.util.version.MCVersion;
import xyz.mlserver.util.ICollectionList;

import java.util.List;
import java.util.Objects;

public class MCVersionUtil {

    /**
     *
     * @param protocolVersion
     * @return
     */
    public static String getByProtocolVersion(int protocolVersion) {
        List<MCVersion> versions = MCVersion.getByProtocolVersion(protocolVersion);
        if (versions.get(0) == null) return MCVersion.UNKNOWN.getName();
        return versions.get(0).getName();
    }

    public static String getByProtocolVersion(Player player) {
        return getByProtocolVersion(Via.getAPI().getPlayerVersion(player.getUniqueId()));
    }

    public static MCVersion getReleaseVersionIfPossible(int protocolVersion) {
        ICollectionList<MCVersion> list = ICollectionList.asList(MCVersion.getByProtocolVersion(protocolVersion));
        return list.filter(v -> !v.isSnapshot()).size() == 0 // if non-snapshot version wasn't found
                ? Objects.requireNonNull(list.first()) // return the last version anyway
                : Objects.requireNonNull(list.filter(v -> !v.isSnapshot()).first()); // return non-snapshot version instead
    }

}
