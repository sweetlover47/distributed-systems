package ru.nsu.lebedeva;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.InputStream;

public class StAXReader {
    private static final XMLInputFactory XML_INPUT_FACTORY = XMLInputFactory.newInstance();

    private final XMLStreamReader reader;

    public StAXReader(InputStream inputStream) throws XMLStreamException {
        this.reader = XML_INPUT_FACTORY.createXMLStreamReader(inputStream);
    }

    public XMLStreamReader getReader() {
        return reader;
    }

}
