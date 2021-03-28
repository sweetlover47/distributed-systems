package ru.nsu.lebedeva;

import org.apache.commons.compress.compressors.CompressorException;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import ru.nsu.lebedeva.dao.NodeDAO;
import ru.nsu.lebedeva.dao.TagDAO;
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

public class OSMReader {
    public void readArchivedOsmFile(String fileName) throws XMLStreamException {
        XMLStreamReader reader = null;
        try (CompressorInputStream compressorInputStream = new CompressorStreamFactory()
                .createCompressorInputStream(new BufferedInputStream(new FileInputStream(fileName)))) {
            reader = new StAXReader(compressorInputStream).getReader();
            JAXBContext jaxbContext = JAXBContext.newInstance(Node.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            NodeDAO nodeDao = new NodeDAO();
            TagDAO tagDao = new TagDAO();
            NodeRepository nodeRepository = new NodeRepository(nodeDao, tagDao);
            int i = 0;
            long sum = 0L;
            while (reader.hasNext()) {
                int event = reader.next();
                if (event == START_ELEMENT && reader.getLocalName().equals("node")) {
                    Node node = (Node) unmarshaller.unmarshal(reader);
                    NodeEntity nodeEntity = NodeEntity.convert(node);
                    long cur = System.currentTimeMillis();
                    nodeRepository.putNode(nodeEntity); //////////////////////// change to other type of insert
                    cur = System.currentTimeMillis() - cur;
                    sum += cur;
                    if (i == 100000)
                        break;
                    i++;
                }
            }
            System.out.println("Время выполнения: " + sum / 1000);
            assert i > 0;
            double perOne = (double) sum / i;
            System.out.println("Объектов в секунду: " + 1000 / perOne);
        } catch (XMLStreamException | IOException | CompressorException | JAXBException e) {
            e.printStackTrace();
        } finally {
            if (reader != null)
                reader.close();
        }

    }
}
