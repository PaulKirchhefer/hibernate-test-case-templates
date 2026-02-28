package org.hibernate.envers.bugs;

import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.Configuration;
import org.junit.Test;

public class EnversUnitTestCase extends AbstractEnversTestCase {

    @Override
    protected Class[] getAnnotatedClasses() {
        return new Class[]{
                TestEntity.class
        };
    }

    @Override
    protected String[] getMappings() {
        return new String[]{
        };
    }

    @Override
    protected String getBaseForMappings() {
        return "org/hibernate/test/";
    }

    @Override
    protected void configure(Configuration configuration) {
        super.configure(configuration);

        configuration.setProperty(AvailableSettings.SHOW_SQL, Boolean.TRUE.toString());
        configuration.setProperty(AvailableSettings.FORMAT_SQL, Boolean.TRUE.toString());
    }

    @Test
    public void hhh20217Test() throws Exception {
        // should have no issue - but initialization fails with ClassCastException
    }
}
