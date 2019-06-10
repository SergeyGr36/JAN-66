package com.ra.janus.developersteam.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
public class TechnicalTask implements BaseEntity {
    private long id;
    private String title;
    private String description;

    public TechnicalTask(long id, TechnicalTask task) {
        this(id, task.getTitle(), task.getDescription());
    }

}
