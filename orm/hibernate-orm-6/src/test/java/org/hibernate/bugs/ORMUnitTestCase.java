/*
 * Copyright 2014 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.hibernate.bugs;

import org.hibernate.bugs.domain.TestChild;
import org.hibernate.bugs.domain.TestParent;
import org.hibernate.bugs.domain.TestReference;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.testing.orm.junit.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

/**
 * This template demonstrates how to develop a test case for Hibernate ORM, using its built-in unit test framework.
 * Although ORMStandaloneTestCase is perfectly acceptable as a reproducer, usage of this class is much preferred.
 * Since we nearly always include a regression test with bug fixes, providing your reproducer using this method
 * simplifies the process.
 * <p>
 * What's even better?  Fork hibernate-orm itself, add your test case directly to a module's unit tests, then
 * submit it as a PR!
 */
@DomainModel(
        annotatedClasses = {
                TestChild.class,
                TestParent.class,
                TestReference.class
        }
)
@ServiceRegistry(
        settings = {
                @Setting(name = AvailableSettings.SHOW_SQL, value = "true"),
                @Setting(name = AvailableSettings.FORMAT_SQL, value = "true")
        }
)
@SessionFactory
class ORMUnitTestCase {
    static {
        ORMUnitTestCase.class.getClassLoader()
                .setClassAssertionStatus("org.hibernate.sql.results.graph.entity.internal.EntityInitializerImpl", false);
        ORMUnitTestCase.class.getClassLoader()
                .setClassAssertionStatus("org.hibernate.engine.internal.StatefulPersistenceContext", false);
    }

    @Test
    void hhh19562Test(SessionFactoryScope scope) throws Exception {
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
