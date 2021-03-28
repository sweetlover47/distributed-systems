package ru.nsu.lebedeva.entity;

import lombok.Data;
import ru.nsu.lebedeva.entity.generated.Node;


@Data
public class TagEntity {
    private long nodeId;
    private String key;
    private String value;

    public TagEntity(long nodeId, String k, String v) {
        this.nodeId = nodeId;
        this.key = k;
        this.value = v;
    }

    public static TagEntity convert(Node.Tag tag, long nodeId) {
        return new TagEntity(nodeId, tag.getK(), tag.getV());
    }
}
