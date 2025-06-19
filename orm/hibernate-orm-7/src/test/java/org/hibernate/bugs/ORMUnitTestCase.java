package org.hibernate.bugs;

import org.hibernate.bugs.domain.TestChild;
import org.hibernate.bugs.domain.TestParent;
import org.hibernate.bugs.domain.TestReference;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.testing.orm.junit.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@DomainModel(annotatedClasses = {TestChild.class, TestParent.class, TestReference.class})
@ServiceRegistry(settings = {@Setting(name = AvailableSettings.SHOW_SQL, value = "true"), @Setting(name = AvailableSettings.FORMAT_SQL, value = "true")})
@SessionFactory
class ORMUnitTestCase {
    static {
        ORMUnitTestCase.class.getClassLoader().setClassAssertionStatus("org.hibernate.sql.results.graph.entity.internal.EntityInitializerImpl", false);
        ORMUnitTestCase.class.getClassLoader().setClassAssertionStatus("org.hibernate.engine.internal.StatefulPersistenceContext", false);
    }

    @Test
    void hhh123Test(SessionFactoryScope scope) throws Exception {
        scope.inTransaction(session -> {
            TestReference reference1 = new TestReference();
            reference1.setId(1L);
            session.persist(reference1);

            TestChild child1 = new TestChild();
            child1.setId(1L);
            TestChild child2 = new TestChild();
            child2.setId(2L);

            TestParent parent = new TestParent();
            parent.setId(1L);
            parent.setChildren(List.of(child1, child2));
            session.persist(parent);
        });

        scope.inTransaction(session -> {
            var children = List.of(session.find(TestChild.class, 1L), session.find(TestChild.class, 2L));
            for (var child : children) {
                TestReference unmanagedReference = new TestReference();
                unmanagedReference.setId(1L);
                child.setReference(unmanagedReference);
            }
            session.flush();

            TestParent parent = session.find(TestParent.class, 1L);
            assertDoesNotThrow(() -> parent.getChildren().size());
        });
    }
}
