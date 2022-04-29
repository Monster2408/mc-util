package xyz.mlserver.mls;

import java.util.UUID;

public enum MLSMember {
    GINGER_ALE10("Ginger_Ale10", UUID.fromString("95593263edef4f07a6bbefd7a05e2652"), "ぎんあれ"),
    MONSTER2408("Monster2408", UUID.fromString("1c2b6991e8ce4e5db4d8ec3f0cdc5f8e"), "もんすたぁ"),
    KAIJI_("kaiji_", UUID.fromString("bf72eed1c52249c4ace3975eb806471b"), "かいじ"),
    ;
    private final String id;
    private final UUID uuid;
    private final String name;

    /**
     * 撮影メンバー
     * @param id String MCID
     * @param uuid {@link UUID} UUID
     * @param name String 名前
     */
    MLSMember(String id, UUID uuid, String name) {
        this.id = id;
        this.uuid = uuid;
        this.name = name;
    }

    /**
     * 撮影メンバーの名前
     * @return String 名前
     */
    public String getName() {
        return name;
    }

    /**
     * 撮影メンバーのMCID
     * @return String MCID
     */
    public String getId() {
        return id;
    }

    /**
     * 撮影メンバーのUUID
     * @return {@link UUID} UUID

     */
    public UUID getUuid() {
        return uuid;
    }
}
