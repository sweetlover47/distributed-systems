package ru.nsu.lebedeva;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.xml.stream.XMLStreamException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Main {
    public static final String URL = "jdbc:postgresql://localhost:5432/osm";
    public static final String USERNAME = "nastya";
    public static final String PASSWORD = "123456";
    public static Connection connection = null;

    public static void main(String[] args) throws SQLException {
        Logger logger = LogManager.getRootLogger();
        try {
            File file = new File("src\\main\\resources\\initDB.sql");
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                StringBuffer stringBuffer = new StringBuffer("");
                while (reader.ready()) {
                    stringBuffer.append(reader.readLine());
                }
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                Statement statement = connection.createStatement();
                statement.execute(new String(stringBuffer));
            }
            logger.info("Start read osm file");
            new OSMReader().readArchivedOsmFile("src\\main\\resources\\RU-NVS.osm.bz2");
            logger.info("End read osm file");
        } catch (XMLStreamException | SQLException | IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }
}
