package ru.nsu.lebedeva;


import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.nsu.lebedeva.entity.NodeEntity;

import java.util.List;

@AllArgsConstructor
@Service
public class NodeService {
    @Autowired
    private final NodeRepository nodeRepository;

    public NodeEntity save(NodeEntity node) {
        return nodeRepository.save(node);
    }

    public NodeEntity getNode(Long id) {
        return nodeRepository.findById(id).orElse(null);
    }

    public NodeEntity update(NodeEntity node) {
        nodeRepository.findById(node.getId()).orElseThrow(NullPointerException::new);
        return nodeRepository.save(node);
    }

    public void delete(long id) {
        NodeEntity node = nodeRepository.findById(id).orElseThrow(NullPointerException::new);
        nodeRepository.delete(node);
    }

    public List<NodeEntity> findNodesInRadius(Double latitude, Double longitude, Double radius) {
        return nodeRepository.getNodesInRadius(latitude, longitude, radius);
    }
}