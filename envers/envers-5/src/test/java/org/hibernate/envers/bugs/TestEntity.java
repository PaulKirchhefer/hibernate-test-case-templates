package org.hibernate.envers.bugs;

import org.hibernate.envers.Audited;

import javax.persistence.Entity;
import javax.persistence.Id;

@Audited
@Entity
public class TestEntity {

    @Id
    private Long id;

    private Long originalId;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Long getOriginalId() {
        return originalId;
    }

    public void setOriginalId(final Long originalId) {
        this.originalId = originalId;
    }
}
