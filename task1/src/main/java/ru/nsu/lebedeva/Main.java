package ru.nsu.lebedeva;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.xml.stream.XMLStreamException;

public class Main {
    public static void main(String[] args) {
        Logger logger = LogManager.getRootLogger();
        System.out.println("Hello, World!");
        logger.info("Printed message");
        OSMReader osmReader = new OSMReader();
        try {
            osmReader.readArchivedOsmFile("src\\main\\resources\\RU-NVS.osm.bz2");
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }

    }
}
