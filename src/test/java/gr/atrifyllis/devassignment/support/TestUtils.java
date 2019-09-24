package gr.atrifyllis.devassignment.support;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;

class TestUtils {
    static void cleanUpDb(JdbcTemplate jdbcTemplate) {
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "order_line", "placed_order", "product");
    }
}
