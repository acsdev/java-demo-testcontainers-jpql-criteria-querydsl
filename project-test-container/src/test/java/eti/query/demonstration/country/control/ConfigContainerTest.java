package eti.query.demonstration.country.control;

import oracle.jdbc.pool.OracleDataSource;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.junit.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.output.OutputFrame;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.output.ToStringConsumer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.MountableFile;

import javax.ws.rs.core.UriBuilder;
import java.io.FileReader;
import java.net.URI;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Optional;

/**
 * For this classes works it necessary to have the docker images in your local registry
 * docker pull acsdev00/oracle-18-4-0-xe
 * execute docker-run-oracle.sh once, to prepare docker oracle volume
 * docker payara/micro:latest
 */
public abstract class ConfigContainerTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigContainerTest.class);

    private static String getOracleVolume() {
        //TODO, GET FROM ENVIROMENT
        return "/home/allansantos/Dev/Docker/Volume/oracle-18c-xe";
    }

    private static String getPayaraDeployDir() {
        return System.getProperty("user.dir").concat("/payara/deployments");
    }

    private static GenericContainer oracleGC;

    private static GenericContainer payaraGC;

    @BeforeClass
    public static void before() throws Exception {

        Network network = Network.newNetwork();

        oracleGC = new GenericContainer<>("acsdev00/oracle-18-4-0-xe")
                .withEnv("ORACLE_SID", "XE")
                .withEnv("ORACLE_PWD", "oracle")
                .withFileSystemBind(getOracleVolume(), "/opt/oracle/oradata", BindMode.READ_WRITE)
                .withPrivilegedMode(true)
                .withExposedPorts(1521)
                .withNetwork(network)
                .withNetworkAliases("oracledatabase")
                .withLogConsumer(new Slf4jLogConsumer(LOGGER))
                .waitingFor(
                        Wait.forLogMessage(".*DATABASE IS READY TO USE!.*", 1)
                );
        oracleGC.start();

        payaraGC = new GenericContainer<>("payara/micro:latest")
                .withFileSystemBind(getPayaraDeployDir(), "/opt/payara/deployments", BindMode.READ_WRITE)
                .withExposedPorts(8080)
                .withNetwork(network)
                .withNetworkAliases("payara")
                .withLogConsumer(new Slf4jLogConsumer(LOGGER))
                .waitingFor(Wait.forListeningPort());
        payaraGC.start();


//        String url = String.format("jdbc:oracle:thin:system/oracle@%s:%s:XE",
//                oracle.getContainerIpAddress(),
//                oracle.getFirstMappedPort());

//        OracleDataSource dataSource = new OracleDataSource();
//        dataSource.setURL(url);
//        dataSource.setUser("system");
//        dataSource.setPassword("oracle");
//        dataSource.setDriverType("ojdbc7");
//
//        ScriptRunner runner = new ScriptRunner(dataSource.getConnection());
//
//        FileReader create = new FileReader
//                (Thread.currentThread().getContextClassLoader().getResource("sql/create.sql").getFile());
//        runner.runScript(create);
//
//        FileReader load = new FileReader
//                (Thread.currentThread().getContextClassLoader().getResource("sql/load.sql").getFile());
//        runner.runScript(load);

        payaraGC.followOutput(new ToStringConsumer(), OutputFrame.OutputType.STDOUT);
    }

    @After
    public void after() {
        payaraGC.followOutput(new ToStringConsumer(), OutputFrame.OutputType.STDOUT);
    }

    protected String getAppServerURLBase() {
        return String.format("http://%s:%s", payaraGC.getContainerIpAddress(), payaraGC.getFirstMappedPort());
    }
}