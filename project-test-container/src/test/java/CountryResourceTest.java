package eti.query.demonstration.country.control;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.wait.strategy.Wait;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.function.Function;

public class CountryResourceTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(CountryResourceTest.class);

    private Function<String,byte[]> war = (path) -> {
        try {
            return Files.readAllBytes(Paths.get(path));
        } catch (IOException e) {
            LOGGER.error(e.getLocalizedMessage(), e);
            throw new RuntimeException(e);
        }
    };

    @Rule
    public Network network = Network.newNetwork();

    @Rule
    public GenericContainer oracle = new GenericContainer<>("acsdev00/oracle-18-4-0-xe")
            .withEnv("ORACLE_BASE","/opt/oracle")
            .withEnv("ORACLE_SID","XE")
            .withEnv("ORACLE_PWD","oracle")
            .withFileSystemBind("~/Dev/Docker/Volume/oracle-18c-xe","/opt/oracle/oradata", BindMode.READ_WRITE)
            .withPrivilegedMode(true)
            .withExposedPorts(1521)
            .withLogConsumer(new Slf4jLogConsumer(LOGGER))
            .withNetwork(network);

    @Rule
    public GenericContainer payara = new GenericContainer<>("payara/micro:latest")
            .waitingFor(Wait.forHttp("/demo"))
            .withExposedPorts(8080)
            .withLogConsumer(new Slf4jLogConsumer(LOGGER))
            .withNetwork(network);

    public CountryResourceTest() throws IOException {
    }

    @Before
    public void before() {
        String containerIpAddress = payara.getContainerIpAddress();
        Integer firstMappedPort = payara.getFirstMappedPort();
        System.out.print(containerIpAddress + ":" +firstMappedPort);
    }

    @Test
    public void testGetCountry() {

    }
}