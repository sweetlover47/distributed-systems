package ru.nsu.lebedeva;

import org.apache.commons.compress.compressors.CompressorException;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.nsu.lebedeva.entity.NodeEntity;
import ru.nsu.lebedeva.entity.generated.Node;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;

import static javax.xml.stream.XMLStreamConstants.START_ELEMENT;

@Service
public class OSMReader {
    @Autowired
    NodeService nodeProcessor;

    public void readArchivedOsmFile(String fileName) throws XMLStreamException, JAXBException, IOException, CompressorException {
        XMLStreamReader reader;
        try (CompressorInputStream compressorInputStream = new CompressorStreamFactory()
                .createCompressorInputStream(new BufferedInputStream(new FileInputStream(fileName)))) {
            JAXBContext jaxbContext = JAXBContext.newInstance(Node.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            reader = new StAXReader(compressorInputStream).getReader();
            int i = 0;
            while (reader.hasNext()) {
                int event = reader.next();
                if (event == START_ELEMENT && reader.getLocalName().equals("node")) {
                    Node node = (Node) unmarshaller.unmarshal(reader);
                    NodeEntity nodeEntity = NodeEntity.convert(node);
                    nodeProcessor.save(nodeEntity);
                    if (i == 10000)
                        break;
                    i++;
                }
            }
        }

    }
}
