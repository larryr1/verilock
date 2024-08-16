package com.github.larryr1.verilock.data.database;

public class Statements {

    public static class Tables {

        public static String CreateIdentitiesTable = """
                CREATE TABLE IF NOT EXISTS identities (
                id_number TEXT PRIMARY KEY,
                birthday TEXT NOT NULL,
                first_name TEXT,
                last_name TEXT,
                age INTEGER,
                grade INTEGER,
                house TEXT)
                """;

        public static String CreatePlayersTable = """
                CREATE TABLE IF NOT EXISTS players (
                uuid TEXT PRIMARY KEY,
                identity_id TEXT,
                FOREIGN KEY(identity_id) REFERENCES identities(id_number))
                """;
    }

    public static class Queries {

        public static String CreateIdentity = "INSERT INTO identities (id_number, birthday, first_name, last_name, age, grade, house) VALUES (?, ?, ?, ?, ?, ?, ?)";
        public static String GetLinkedPlayer = """
            SELECT *
            FROM players
            WHERE uuid = ?
            AND identity_id IS NOT NULL
            """;

        public static String SelectIdentityById = "SELECT * FROM identities WHERE id_number = ? LIMIT 1";

        public static String VerifyIdentity = "SELECT * FROM identities WHERE id_number = ? AND birthday = ? LIMIT 1";
        public static String LinkPlayerIdentity = "UPDATE players SET identity_id = ? WHERE uuid = ?";

        public static class Players {

            public static String SelectPlayerByUUID = "SELECT * FROM players WHERE uuid = ? LIMIT 1";
            public static String SelectPlayerByIdentityId = "SELECT * FROM players WHERE identity_id = ?";
            public static String CreatePlayer = "INSERT INTO players (uuid, identity_id) VALUES (?, ?)";

        }
    }
}
