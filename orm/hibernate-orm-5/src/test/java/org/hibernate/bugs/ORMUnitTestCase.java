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

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.bugs.domain.TestChild;
import org.hibernate.bugs.domain.TestParent;
import org.hibernate.bugs.domain.TestReference;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.Configuration;
import org.hibernate.testing.junit4.BaseCoreFunctionalTestCase;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class ORMUnitTestCase extends BaseCoreFunctionalTestCase {

    @Override
    protected Class[] getAnnotatedClasses() {
        return new Class[]{
                TestChild.class,
                TestParent.class,
                TestReference.class
        };
    }

    @Override
    protected void configure(Configuration configuration) {
        super.configure(configuration);

        configuration.setProperty(AvailableSettings.SHOW_SQL, Boolean.TRUE.toString());
        configuration.setProperty(AvailableSettings.FORMAT_SQL, Boolean.TRUE.toString());
    }

    void setUpData(Session session) {
        Transaction tx = session.beginTransaction();
        TestReference reference1 = new TestReference();
        reference1.setId(1L);

        session.persist(reference1);

        TestChild child1 = new TestChild();
        child1.setId(1L);
        TestChild child2 = new TestChild();
        child2.setId(2L);

        TestParent parent = new TestParent();
        parent.setId(1L);
        parent.setChildren(new ArrayList<>());
        parent.getChildren().add(child1);
        parent.getChildren().add(child2);
        session.persist(parent);
        tx.commit();
    }

    @Test
    public void hhh123Test() throws Exception {
        Session s = openSession();
        setUpData(s);
        s.clear();

        Transaction tx = session.beginTransaction();
        List<TestChild> children = new ArrayList<>();
        children.add(session.find(TestChild.class, 1L));
        children.add(session.find(TestChild.class, 2L));
        for (TestChild child : children) {
            TestReference unmanagedReference = new TestReference();
            unmanagedReference.setId(1L);
            child.setReference(unmanagedReference);
        }
        session.flush();

        TestParent parent = session.find(TestParent.class, 1L);
        assertDoesNotThrow(() -> parent.getChildren().size());

        tx.commit();
        s.close();
    }
}
