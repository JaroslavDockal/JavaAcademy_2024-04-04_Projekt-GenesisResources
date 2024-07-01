package cz.engeto.ja.genesisResources.util;

public class Settings {
    private static final String DB_HOST = "localhost";
    private static final int DB_PORT = 3306;
    private static final String DB_NAME = "genesisResources_db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Genesis-2024";

    public static final String CONNECTION_STRING = "jdbc:mysql://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME + "?user=" + DB_USER + "&password=" + DB_PASSWORD;

    public static final String PERSON_ID_FILE = "dataPersonId.txt";
}
