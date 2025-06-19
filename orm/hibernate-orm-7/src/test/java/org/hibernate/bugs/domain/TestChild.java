package org.hibernate.bugs.domain;

import jakarta.persistence.*;

@Table(name = "TEST_CHILD")
@Entity
public class TestChild {

    @Id
    @Column(name = "ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "REFERENCE_ID")
    private TestReference reference;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public TestReference getReference() {
        return reference;
    }

    public void setReference(final TestReference reference) {
        this.reference = reference;
    }
}
