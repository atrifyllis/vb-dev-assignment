package gr.atrifyllis.devassignment.support;

import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Base class for Repository (JPA) tests.
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public abstract class JpaTestBase extends CommonBase {


}
