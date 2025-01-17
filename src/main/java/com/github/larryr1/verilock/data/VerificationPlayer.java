package com.github.larryr1.verilock.data;

import java.util.UUID;

public class VerificationPlayer {
    private final UUID uuid;
    private final String identityId;

    public VerificationPlayer(UUID uuid, String identityId) {
        this.uuid = uuid;
        this.identityId = identityId;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getIdentityId() {
        return identityId;
    }
}