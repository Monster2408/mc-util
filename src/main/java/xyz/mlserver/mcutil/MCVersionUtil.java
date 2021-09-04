package xyz.mlserver.mcutil;

import com.viaversion.viaversion.api.Via;
import org.bukkit.entity.Player;
import xyz.mlserver.mcutil.version.MCVersion;
import xyz.mlserver.util.ICollectionList;

import java.util.Objects;

public class MCVersionUtil {

    /**
     *
     * @param protocolVersion
     * @return
     */
    public static String getByProtocolVersion(int protocolVersion) {
        return MCVersion.getByProtocolVersion(protocolVersion).get(0).getName();
    }

    public static String getByProtocolVersion(Player player) {
        return getReleaseVersionIfPossible(Via.getAPI().getPlayerVersion(player.getUniqueId())).getName();
    }

    public static MCVersion getReleaseVersionIfPossible(int protocolVersion) {
        ICollectionList<MCVersion> list = ICollectionList.asList(MCVersion.getByProtocolVersion(protocolVersion));
        return list.filter(v -> !v.isSnapshot()).size() == 0 // if non-snapshot version wasn't found
                ? Objects.requireNonNull(list.first()) // return the last version anyway
                : Objects.requireNonNull(list.filter(v -> !v.isSnapshot()).first()); // return non-snapshot version instead
    }

}
