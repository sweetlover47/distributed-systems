package ru.nsu.lebedeva;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.nsu.lebedeva.entity.NodeEntity;
import ru.nsu.lebedeva.entity.TagEntity;

import java.util.Map;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class NodeDto {
    private Long id;
    private String user;
    private Double longitude;
    private Double latitude;
    private Map<String, String> tags;

    public static NodeDto convert(NodeEntity nodeEntity) {
        Map<String, String> tags = nodeEntity.getTagList().stream().collect(Collectors.toMap(TagEntity::getKey, TagEntity::getValue));
        return new NodeDto(nodeEntity.getId(), nodeEntity.getUser(), nodeEntity.getLongitude(), nodeEntity.getLatitude(), tags);
    }
}