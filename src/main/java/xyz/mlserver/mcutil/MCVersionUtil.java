package xyz.mlserver.mcutil;

import xyz.acrylicstyle.mcutil.lang.MCVersion;

public class MCVersionUtil {

    public static String getByProtocolVersion(int protocolVersion) {
        return MCVersion.getByProtocolVersion(protocolVersion).get(0).getName();
    }

}
