package cz.engeto.ja.genesisResources.util;

/**
 * Utility class for storing application settings.
 */
public class Settings {
    // Database connection settings
    private static final String DB_HOST = "localhost";
    private static final int DB_PORT = 3306;
    private static final String DB_NAME = "genesisResources_db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Genesis-2024";

    /**
     * JDBC connection string for connecting to the database.
     */
    public static final String CONNECTION_STRING = "jdbc:mysql://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME + "?user=" + DB_USER + "&password=" + DB_PASSWORD;

    /**
     * File name for storing person IDs.
     */
    public static final String PERSON_ID_FILE = "dataPersonId.txt";
}
