package com.github.larryr1.verilock.data.database;

import com.github.larryr1.verilock.data.PlayerIdentity;
import com.github.larryr1.verilock.data.VerificationPlayer;

import java.sql.SQLException;
import java.util.UUID;

public interface DatabaseInterface {
    void CreateIdentity(PlayerIdentity identity) throws SQLException;
    boolean DeleteIdentity(PlayerIdentity identity);
    void LinkPlayerIdentity(UUID uuid, PlayerIdentity identity);
    PlayerIdentity GetPlayerIdentity(String idNumber);
    boolean UnlinkPlayerIdentity(UUID uuid);
    PlayerIdentity VerifyIdentity(String idNumber, String birthday);
    PlayerIdentity GetIdentityByIdNumber(String idNumber);
    boolean IdentityExistsById(String idNumber);
    VerificationPlayer GetOrCreatePlayer(UUID uuid) throws SQLException;
    VerificationPlayer GetPlayerByIdentityId(String idNumber);
}
