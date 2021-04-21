package ru.nsu.lebedeva.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.nsu.lebedeva.entity.generated.Node;

import javax.persistence.*;

@Data
@Entity
@Table(name = "tags")
@AllArgsConstructor
@NoArgsConstructor
@IdClass(TagComplexKey.class)
public class TagEntity {
    @Id
    private long id;

    @Id
    private String key;

    @Column(name = "value")
    private String value;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "node_id", insertable = false, updatable = false)
    private NodeEntity node;

    public TagEntity(long nodeId, String k, String v) {
        this.id = nodeId;
        this.key = k;
        this.value = v;
    }

    public static TagEntity convert(Node.Tag tag, long nodeId) {
        return new TagEntity(nodeId, tag.getK(), tag.getV());
    }

    public static TagEntity convert(String key, String value, long nodeId) {
        return new TagEntity(nodeId, key, value);
    }
}