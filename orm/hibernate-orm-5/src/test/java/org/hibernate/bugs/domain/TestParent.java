package org.hibernate.bugs.domain;

import javax.persistence.*;
import java.util.List;

@Table(name = "TEST_PARENT")
@Entity
public class TestParent {

    @Id
    @Column(name = "ID")
    private Long id;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "PARENT_ID")
    private List<TestChild> children;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public List<TestChild> getChildren() {
        return children;
    }

    public void setChildren(final List<TestChild> children) {
        this.children = children;
    }
}
