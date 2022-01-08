package kz.toko.app;


import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.jdbc.SqlMergeMode;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static org.springframework.test.context.jdbc.SqlMergeMode.MergeMode.MERGE;

@SpringBootTest
@ActiveProfiles("it")
@TestMethodOrder(MethodOrderer.MethodName.class)
@SqlMergeMode(MERGE)
@SqlGroup({
        @Sql(value = "classpath:/db.scripts/add_one_user.sql", executionPhase = BEFORE_TEST_METHOD),

        @Sql(value = "classpath:/db.scripts/delete_all_records.sql", executionPhase = AFTER_TEST_METHOD),
})
public abstract class IntegrationTest {

    /**
     * Supplies JWT token for valid user of Adam Smith. <br/>
     * Can be used in combination with {@code add_one_user.sql} that adds this user into database
     *
     * @return JwtRequestPostProcessor configured to existing user token
     * @see SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor
     */
    protected SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor validJwtToken() {
        return jwt()
                .jwt(jwt -> jwt
                        .claim("sub", "adam.smith")
                        .claim("email", "adam.smith@example.com")
                        .claim("given_name", "Adam")
                        .claim("family_name", "Smith")
                );
    }
}
