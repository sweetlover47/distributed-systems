package ru.nsu.lebedeva.entity;

import lombok.Data;
import ru.nsu.lebedeva.entity.generated.Node;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class NodeEntity {
    private long id;
    private String user;
    private Double longitude;
    private Double latitude;
    private List<TagEntity> tagList;

    public NodeEntity(long id, String user, Double longitude, Double latitude, List<TagEntity> tagList) {
        this.id = id;
        this.user = user;
        this.longitude = longitude;
        this.latitude = latitude;
        this.tagList = tagList;
    }

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

    public List<TagEntity> getTags() {
        return tagList;
    }
}
