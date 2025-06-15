package tech.best;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@Import(ContainerConfig.class)
public class ImportConfigTest {



    @Test
    void contextLoad() {

    }
}