package com.github.larryr1.verilock.data.database;

import com.github.larryr1.verilock.Verilock;
import com.github.larryr1.verilock.data.PlayerIdentity;
import com.github.larryr1.verilock.data.VerificationPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.sql.*;
import java.util.UUID;

public class VerificationDatabase implements DatabaseInterface {

    private final Connection connection;

    public VerificationDatabase(String path) throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:" + path);

        // Create tables
        try (Statement statement = connection.createStatement()) {
            statement.execute(Statements.Tables.CreateIdentitiesTable);
            statement.execute(Statements.Tables.CreatePlayersTable);
        }
    }

    /**
     * Close the database connection.
     * @throws SQLException An SQLException indicating a database error.
     */
    public void CloseConnection() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    @Override
    public void CreateIdentity(PlayerIdentity identity) throws SQLException {

        if (IdentityExistsById(identity.getIdNumber())) {
            throw new RuntimeException("The specified identity's ID number already exists in the database. id_number: " + identity.getIdNumber());
        }

        // Create identity in database
        try (PreparedStatement statement = connection.prepareStatement(Statements.Queries.CreateIdentity)) {
            statement.setString(1, identity.getIdNumber());
            statement.setString(2, identity.getBirthday());
            statement.setString(3, identity.getFirstName());
            statement.setString(4, identity.getLastName());
            statement.setInt(5, identity.getAge());
            statement.setInt(6, identity.getGrade());
            statement.setString(7, identity.getHouse());
            int result = statement.executeUpdate();
            Verilock.getInstance().logger.info("Inserted a record. Result is: " + result);
        }
    }

    @Override
    public boolean DeleteIdentity(PlayerIdentity identity) {
        return false;
    }

    @Override
    public void LinkPlayerIdentity(UUID uuid, PlayerIdentity identity) {
        try (PreparedStatement statement = connection.prepareStatement(Statements.Queries.LinkPlayerIdentity)) {
            statement.setString(1, identity.getIdNumber());
            statement.setString(2, uuid.toString());
            statement.executeUpdate();

            Player player = Bukkit.getPlayer(uuid);
            if (player.isOnline()) {
                player.sendMessage(ChatColor.GOLD + "You have been linked to IDENTITY #" + identity.getIdNumber());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Nullable
    public PlayerIdentity GetPlayerIdentity(String idNumber) {
        try (PreparedStatement statement = connection.prepareStatement(Statements.Queries.SelectIdentityById)) {
            statement.setString(1, idNumber);
            ResultSet result = statement.executeQuery();

            boolean isNext = result.next();
            if (!isNext) return null;

            return createIdentityFromResult(result);

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public boolean UnlinkPlayerIdentity(UUID uuid) {
        try (PreparedStatement statement = connection.prepareStatement(Statements.Queries.LinkPlayerIdentity)) {
            statement.setNull(1, Types.VARCHAR);
            statement.setString(2, uuid.toString());
            int rowsChanged = statement.executeUpdate();

            Player player = Bukkit.getPlayer(uuid);
            if (player.isOnline()) {
                player.sendMessage(ChatColor.GOLD + "The server has unlinked your identity. You will be required to re-verify the next time you join the server.");
            }

            return (rowsChanged > 0);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    @Nullable
    public PlayerIdentity VerifyIdentity(String idNumber, String birthday) {
        try (PreparedStatement statement = connection.prepareStatement(Statements.Queries.VerifyIdentity)) {
            statement.setString(1, idNumber);
            statement.setString(2, birthday);
            ResultSet result = statement.executeQuery();

            boolean isNext = result.next();
            if (!isNext) return null;

            return createIdentityFromResult(result);

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public PlayerIdentity GetIdentityByIdNumber(String idNumber) {
        return null;
    }

    @Override
    public boolean IdentityExistsById(String idNumber) {
        try (PreparedStatement statement = connection.prepareStatement(Statements.Queries.SelectIdentityById)) {
            statement.setString(1, idNumber);
            ResultSet result = statement.executeQuery();
            return result.next() ? true : false;
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public ResultSet ExecuteQuery(String query) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            return statement.executeQuery(query);
        }
    }

    private PlayerIdentity createIdentityFromResult(ResultSet result) throws SQLException {
        return new PlayerIdentity(
                result.getString("first_name"),
                result.getString("last_name"),
                result.getString("id_number"),
                result.getString("birthday"),
                result.getInt("age"),
                result.getInt("grade"),
                result.getString("house")
        );
    }

    public VerificationPlayer getPlayer(UUID uuid) {

        // Statement
        try (PreparedStatement statement = connection.prepareStatement(Statements.Queries.Players.SelectPlayerByUUID)) {

            // Set values in statement
            statement.setString(1, uuid.toString());
            ResultSet result = statement.executeQuery();

            // Check if statement returned a result
            boolean isNext = result.next();
            if (!isNext) return null;

            return createVerificationPlayerFromResult(result);

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public VerificationPlayer createPlayer(UUID uuid) throws SQLException {

        // Check if player already exists
        if (getPlayer(uuid) != null) {
            throw new RuntimeException("The specified player's UUID already exists in the database. uuid: " + uuid);
        }

        // Create player in database with prepared statement
        try (PreparedStatement statement = connection.prepareStatement(Statements.Queries.Players.CreatePlayer)) {

            // Set value in statement
            statement.setString(1, uuid.toString());

            // Execute statements
            int result = statement.executeUpdate();

            // Return newly constructed player
            return new VerificationPlayer(uuid, null);
        }
    }

    @Override
    public VerificationPlayer GetOrCreatePlayer(UUID uuid) throws SQLException {
        VerificationPlayer player = getPlayer(uuid);
        if (player != null) return player;

        return createPlayer(uuid);
    }

    private VerificationPlayer createVerificationPlayerFromResult(ResultSet result) throws SQLException {

        // Construct object from result
        return new VerificationPlayer(
                UUID.fromString(result.getString("uuid")),
                result.getString("identity_id")
        );
    }

    @Override
    public VerificationPlayer GetPlayerByIdentityId(String idNumber) {

        // Statement
        try (PreparedStatement statement = connection.prepareStatement(Statements.Queries.Players.SelectPlayerByIdentityId)) {

            // Set values in statement
            statement.setString(1, idNumber);
            ResultSet result = statement.executeQuery();

            // Check if statement returned a result
            boolean isNext = result.next();
            if (!isNext) return null;

            return createVerificationPlayerFromResult(result);

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }
}
