package eti.query.demonstration.country.control;

import oracle.jdbc.pool.OracleDataSource;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.Container;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.startupcheck.StartupCheckStrategy;
import org.testcontainers.containers.wait.strategy.DockerHealthcheckWaitStrategy;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.containers.wait.strategy.WaitAllStrategy;
import org.testcontainers.containers.wait.strategy.WaitStrategy;
import org.testcontainers.utility.MountableFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAmount;
import java.time.temporal.TemporalUnit;
import java.util.function.Function;

/**
 * For this classes works it necessary to have the docker images in your local registry
 * docker pull acsdev00/oracle-18-4-0-xe
 * docker payara/micro:latest
 */
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

    @ClassRule
    public static Network network = Network.newNetwork();

    @ClassRule
    public static GenericContainer oracle = new GenericContainer<>("acsdev00/oracle-18-4-0-xe")
            .withEnv("ORACLE_SID","XE")
            .withEnv("ORACLE_PWD","oracle")
            .withFileSystemBind("/home/ctw00723/Dev/Docker/Volume/oracle-18c-xe","/opt/oracle/oradata", BindMode.READ_WRITE)
            .withPrivilegedMode(true)
            .withExposedPorts(1521, 8080, 5500)
            .withLogConsumer(new Slf4jLogConsumer(LOGGER))
            .withNetwork(network)
            .waitingFor(Wait.forHealthcheck().withStartupTimeout(Duration.ofMinutes(2)));


    @ClassRule
    public static GenericContainer payara = new GenericContainer<>("payara/micro:latest")
            .withExposedPorts(8080)
            .withLogConsumer(new Slf4jLogConsumer(LOGGER))
            .withNetwork(network)
            .waitingFor(Wait.forListeningPort());

    @Before
    public void before() throws IOException, InterruptedException, SQLException {

        String url =  String.format("jdbc:oracle:thin:system/oracle@%s:%s:XE",
                oracle.getContainerIpAddress(),
                oracle.getFirstMappedPort());

        OracleDataSource dataSource = new OracleDataSource();
        dataSource.setURL(url);
        dataSource.setUser("system");
        dataSource.setPassword("oracle");
        dataSource.setDriverType("ojdbc7");
        dataSource.getConnection();
        System.out.println(dataSource.getDatabaseName());
        System.out.println(dataSource.getServiceName());
        System.out.println(dataSource.getParentLogger());


        // docker exec -it oracle-18c-xe sqlplus system/oracle@XE @/home/oracle/test.sql
        // inside docker run script file
        //
        // select sysdate from dual;
        // quit;
        // /.
//        String containerIpAddress = payara.getContainerIpAddress();
//        Integer firstMappedPort = payara.getFirstMappedPort();
//        System.out.println(containerIpAddress + ":" +firstMappedPort);
//
//        oracle.copyFileToContainer(MountableFile.forClasspathResource("sql/create.sql"), "/home/oracle/create.sql");
//        oracle.copyFileToContainer(MountableFile.forClasspathResource("sql/load.sql"), "/home/oracle/load.sql");
//        String containerIpAddress1 = oracle.getContainerIpAddress();
//
//        String url = "system/oracle@".concat(containerIpAddress1).concat(":1521/XE");
//
//        Container.ExecResult execResult = oracle.execInContainer("sqlplus", url, "@/home/oracle/create.sql");
//        System.out.println(execResult.getExitCode());
//        System.out.println(execResult.getStdout());
//        System.err.println(execResult.getStderr());
//
//        Container.ExecResult execResult1 = oracle.execInContainer("sqlplus", url, "@/home/oracle/load.sql");
//        System.out.println(execResult1.getExitCode());
//        System.out.println(execResult1.getStdout());
//        System.err.println(execResult1.getStderr());
    }

    @Test
    public void testGetCountry() {

    }
}