package ru.nsu.lebedeva;

import org.apache.commons.compress.compressors.CompressorException;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static javax.xml.stream.XMLStreamConstants.END_ELEMENT;
import static javax.xml.stream.XMLStreamConstants.START_ELEMENT;

public class OSMReader {
    private Map<String, Integer> tagCount = new HashMap<>();
    private Map<String, Integer> userNodes = new HashMap<>();

    private static final int USER_ATTRIBUTE = 4;
    private static final Logger LOGGER = LogManager.getRootLogger();

    public void readArchivedOsmFile(String fileName) throws XMLStreamException {
        XMLStreamReader reader = null;
        try (CompressorInputStream compressorInputStream = new CompressorStreamFactory()
                .createCompressorInputStream(new BufferedInputStream(new FileInputStream(fileName)))) {
            reader = new StAXReader(compressorInputStream).getReader();
            String userName = "";
            int nodes = 0;
            boolean isInNode = false;
            LOGGER.info("Start reading");
            while (reader.hasNext()) {
                int eventType = reader.next();
                if (eventType == START_ELEMENT || eventType == END_ELEMENT) {
                    String localName = reader.getLocalName();
                    if (eventType == START_ELEMENT && localName.equals("tag") && isInNode) {
                        String tagName = reader.getAttributeValue(0);
                        if (tagCount.containsKey(tagName)) {
                            tagCount.put(tagName, tagCount.get(tagName) + 1);
                        } else {
                            tagCount.put(tagName, 1);
                        }
                    }
                    if (eventType == START_ELEMENT && localName.equals("node")) {
                        isInNode = true;
                        if (userName.isEmpty()) {
                            userName = reader.getAttributeValue(USER_ATTRIBUTE);
                        } else if (!userName.equals(reader.getAttributeValue(USER_ATTRIBUTE))) {
                            if (userNodes.containsKey(userName)) {
                                userNodes.put(userName, userNodes.get(userName) + nodes);
                            } else {
                                userNodes.put(userName, nodes);
                            }
                            userName = reader.getAttributeValue(USER_ATTRIBUTE);
                            nodes = 0;
                        }
                        nodes++;
                    }
                    if (eventType == END_ELEMENT && localName.equals("node")) {
                        isInNode = false;
                    }
                }
            }
            LOGGER.info("Reading complete");
            printUserNodes();
            printTagsCount();
        } catch (XMLStreamException | IOException | CompressorException e) {
            e.printStackTrace();
        } finally {
            if (reader != null)
                reader.close();
        }

    }

    private void printUserNodes() {
        LOGGER.info("Print userNodes to file");
        userNodes = userNodes.entrySet().stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));
        try (FileWriter writer = new FileWriter("src\\main\\resources\\user_nodes.txt")) {
            for (Map.Entry<String, Integer> entry : userNodes.entrySet())
                writer.write(entry.getKey() + " - " + entry.getValue() + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        LOGGER.info("Complete printing userNodes to file");
    }

    private void printTagsCount() {
        LOGGER.info("Print tagCount to file");
        tagCount = tagCount.entrySet().stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));

        try (FileWriter writer = new FileWriter("src\\main\\resources\\tag_count.txt")) {
            for (Map.Entry<String, Integer> entry : tagCount.entrySet())
                writer.write(entry.getKey() + " - " + entry.getValue() + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        LOGGER.info("Complete printing tagCount to file");
    }
}
