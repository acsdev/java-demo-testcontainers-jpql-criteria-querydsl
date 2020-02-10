package eti.query.demonstration;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import oracle.jdbc.pool.OracleDataSource;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.wait.strategy.Wait;

import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

/**
 * For this classes works it necessary to have the docker images in your local registry
 * docker pull acsdev00/oracle-18-4-0-xe
 * execute docker-run-oracle.sh once, to prepare docker oracle volume
 * docker payara/micro:latest
 */
public abstract class ConfigContainerTest {

    private static final Logger LOGGER = ConfigContainerTest.createLogger();

    private static GenericContainer oracleGC;

    private static GenericContainer payaraGC;

    /**
     * Create custom logger at Runtime
     *
     * @return
     */
    private static Logger createLogger() {
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        PatternLayoutEncoder ple = new PatternLayoutEncoder();
        ple.setPattern("%msg%n");
        ple.setContext(lc);
        ple.start();

        ConsoleAppender<ILoggingEvent> consoleAppender = new ConsoleAppender<>();
        consoleAppender.setEncoder(ple);
        consoleAppender.setContext(lc);
        consoleAppender.start();

        ch.qos.logback.classic.Logger logger =
                (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(ConfigContainerTest.class);

        logger.addAppender(consoleAppender);
        logger.setAdditive(false);
        return logger;
    }

    private static String getOracleVolume() {
        //TODO, GET FROM ENVIROMENT
        String volumeForOracleContainer = null;
        return Optional.ofNullable(volumeForOracleContainer)
                .orElseThrow(() -> new RuntimeException("Directory to use as volume for oracle container not found"));
    }

    private static String getPayaraDeployDir() {

        return System.getProperty("user.dir").concat("/payara/deployments");
    }

    private static void prepareOracleTable() throws IOException, SQLException {
        String url = String.format("jdbc:oracle:thin:system/oracle@%s:%s:XE", oracleGC.getContainerIpAddress(),
                oracleGC.getFirstMappedPort());

        OracleDataSource dataSource = new OracleDataSource();
        dataSource.setURL(url);
        dataSource.setUser("system");
        dataSource.setPassword("oracle");
        dataSource.setDriverType("ojdbc7");

        ScriptRunner runner = new ScriptRunner(dataSource.getConnection());

        FileReader create =
                new FileReader(Thread.currentThread().getContextClassLoader().getResource("sql/create.sql").getFile());
        runner.runScript(create);

        FileReader load =
                new FileReader(Thread.currentThread().getContextClassLoader().getResource("sql/load.sql").getFile());
        runner.runScript(load);
    }

    protected String getAppServerURLBase() {
        return String.format("http://%s:%s", payaraGC.getContainerIpAddress(), payaraGC.getFirstMappedPort());
    }

    @BeforeClass
    public static void before() throws Exception {

        Network network = Network.newNetwork();

        oracleGC = new GenericContainer<>("acsdev00/oracle-18-4-0-xe").withEnv("ORACLE_SID", "XE")
                .withEnv("ORACLE_PWD", "oracle")
                .withFileSystemBind(getOracleVolume(), "/opt/oracle/oradata", BindMode.READ_WRITE)
                .withPrivilegedMode(true)
                .withExposedPorts(1521)
                .withNetwork(network)
                .withNetworkAliases("oracledatabase")
                .withLogConsumer(new Slf4jLogConsumer(LOGGER))
                .waitingFor(Wait.forLogMessage(".*DATABASE IS READY TO USE!.*", 1));
        oracleGC.start();

        prepareOracleTable();

        payaraGC = new GenericContainer<>("payara/micro:latest").withFileSystemBind(getPayaraDeployDir(),
                "/opt/payara/deployments", BindMode.READ_WRITE)
                .withExposedPorts(8080)
                .withNetwork(network)
                .withNetworkAliases("payara")
                .withLogConsumer(new Slf4jLogConsumer(LOGGER))
                .waitingFor(Wait.forHttp("/demo/application.wadl"));

        payaraGC.start();
    }

    @AfterClass
    public static void after() throws Exception {
        payaraGC.stop();

        oracleGC.stop();
    }
}