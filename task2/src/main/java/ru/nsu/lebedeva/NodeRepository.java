package ru.nsu.lebedeva;

import ru.nsu.lebedeva.dao.NodeDAO;
import ru.nsu.lebedeva.dao.TagDAO;
import ru.nsu.lebedeva.entity.NodeEntity;
import ru.nsu.lebedeva.entity.TagEntity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class NodeRepository {
    private static final int BATCH_SIZE = 5000;

    private NodeDAO nodeDao;
    private TagDAO tagDao;
    private final List<NodeEntity> nodeBuffer = new ArrayList<>();

    public NodeRepository(NodeDAO nodeDao, TagDAO tagDao) {
        this.nodeDao = nodeDao;
        this.tagDao = tagDao;
    }

    public void putNodePrepared(NodeEntity node) {
        try {
            nodeDao.insertPreparedNode(node);
            for (TagEntity tag : node.getTags()) {
                tagDao.insertPreparedTag(tag);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void putNode(NodeEntity node) {
        try {
            nodeDao.insertNode(node);
            for (TagEntity tag : node.getTags()) {
                tagDao.insertTag(tag);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void putNodeBuffered(NodeEntity node) {
        nodeBuffer.add(node);
        if (nodeBuffer.size() == BATCH_SIZE) {
            flush();
        }
    }

    public void flush() {
        try {
            nodeDao.batchInsertNodes(nodeBuffer);
            List<TagEntity> tags = nodeBuffer.stream()
                    .flatMap(node -> node.getTags().stream())
                    .collect(Collectors.toList());
            tagDao.batchInsertTags(tags);
            nodeBuffer.clear();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
