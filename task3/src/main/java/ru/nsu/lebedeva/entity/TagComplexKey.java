package ru.nsu.lebedeva.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.io.Serializable;
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class TagComplexKey implements Serializable {

    @Column(name = "node_id")
    private long id;

    @Column(name = "key")
    private String key;
}

