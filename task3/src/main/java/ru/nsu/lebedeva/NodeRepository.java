package ru.nsu.lebedeva;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.nsu.lebedeva.entity.NodeEntity;

import java.util.List;

@Repository
public interface NodeRepository extends JpaRepository<NodeEntity, Long> {
    @Query(value = "SELECT * FROM nodes WHERE earth_distance(ll_to_earth(?1, ?2), ll_to_earth(nodes.latitude, nodes.longitude)) < ?3",
            nativeQuery = true)
    List<NodeEntity> getNodesInRadius(double lat, double lon, double r);
}
