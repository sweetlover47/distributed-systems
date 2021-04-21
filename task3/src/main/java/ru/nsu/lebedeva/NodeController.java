package ru.nsu.lebedeva;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.nsu.lebedeva.entity.NodeEntity;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/node")
public class NodeController {

    @Autowired
    OSMReader osmReader;
    @Autowired
    NodeService nodeProcessor;

    @GetMapping("/{id}")
    public ResponseEntity<NodeDto> getNode(@PathVariable("id") Long id) {
        NodeEntity nodeEntity = nodeProcessor.getNode(id);
        if (nodeEntity != null) {
            return new ResponseEntity<>(NodeDto.convert(nodeEntity), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") long id) {
        nodeProcessor.delete(id);
    }

    @PutMapping("/update")
    public ResponseEntity<NodeDto> updateNode(
            @Valid @RequestBody NodeDto node) {
        try {
            NodeEntity nodeEntity = nodeProcessor.update(NodeEntity.convert(node));
            return new ResponseEntity<>(NodeDto.convert(nodeEntity), HttpStatus.OK);
        } catch (NullPointerException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @PutMapping("/save")
    public ResponseEntity<NodeDto> saveNode(@Valid @RequestBody NodeDto nodeDto) {
        return new ResponseEntity<>(NodeDto.convert(nodeProcessor.save(NodeEntity.convert(nodeDto))), HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<NodeDto>> searchNodes(@Valid @RequestBody SearchDataDto searchDataDto) {
        return new ResponseEntity<>(
                nodeProcessor.findNodesInRadius(searchDataDto.getLatitude(), searchDataDto.getLongitude(), searchDataDto.getRadius()).stream()
                        .map(NodeDto::convert)
                        .collect(Collectors.toList()),
                HttpStatus.OK);
    }

    @GetMapping("/readOSM")
    public void readOSM() throws Exception {
        osmReader.readArchivedOsmFile("src\\main\\resources\\RU-NVS.osm.bz2");
    }
}