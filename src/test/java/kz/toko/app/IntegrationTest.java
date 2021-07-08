package kz.toko.app;


import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("it")
@TestMethodOrder(MethodOrderer.MethodName.class)
public class IntegrationTest {
}
