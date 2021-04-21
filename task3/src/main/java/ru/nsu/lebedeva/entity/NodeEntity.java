package ru.nsu.lebedeva.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.nsu.lebedeva.NodeDto;
import ru.nsu.lebedeva.entity.generated.Node;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "nodes")
public class NodeEntity {
    @Id
    private long id;

    @Column(name = "username")
    private String user;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "latitude")
    private Double latitude;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "node", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<TagEntity> tagList;

    public NodeEntity(long id, String user, Double longitude, Double latitude) {
        this.id = id;
        this.user = user;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public static NodeEntity convert(Node node) {
        return new NodeEntity(
                node.getId().longValue(),
                node.getUser(),
                node.getLon(),
                node.getLat(),
                node.getTag().stream()
                        .map(t -> TagEntity.convert(t, node.getId().longValue()))
                        .collect(Collectors.toList())
        );
    }

    public static NodeEntity convert(NodeDto node) {
        List<TagEntity> tags = node.getTags().entrySet().stream()
                .map(tag -> TagEntity.convert(tag.getKey(), tag.getValue(), node.getId()))
                .collect(Collectors.toList());
        return new NodeEntity(node.getId(), node.getUser(), node.getLongitude(), node.getLatitude(), tags);
    }
}
