package gr.atrifyllis.devassignment.support;

import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Base class for all tests.
 * Cleans up database before every test.
 * All common functionality should go here.
 */
public abstract class CommonBase {

    @Autowired
    protected JdbcTemplate jdbcTemplate;

    @Before
    public void cleanUpDatabase() {
        TestUtils.cleanUpDb(jdbcTemplate);
    }
}
