package no.home.automation.ws;

import static spark.Spark.before;
import static spark.Spark.exception;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.SparkBase.setPort;

import java.net.URLEncoder;
import java.util.Base64;

import no.home.automation.model.JsonTransformer;
import no.home.automation.service.RfxcomBusImpl;
import no.home.automation.ws.action.ListDevicesAction;
import no.home.automation.ws.action.ListRulesAction;
import no.home.automation.ws.action.LoginAction;
import no.home.automation.ws.action.SearchDevicesAction;
import no.home.automation.ws.action.SendCommandAction;
import no.home.automation.ws.action.UpdateDeviceAction;
import no.home.automation.ws.action.UpdateRuleAction;

import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.PropertyConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

public class SentimentWs
{
	private static final Logger	logger	= LoggerFactory.getLogger("stdoutLogger");

	@Parameter(names = "-port", description = "Webservice port")
	private int					port	= 5300;

	public static void main(String[] args) throws Exception
	{
		PropertyConfigurator.configure("log4j.properties");
		new SentimentWs(args);
	}

	public SentimentWs()
	{

	}

	public SentimentWs(String[] args) throws Exception
	{
		new JCommander(this, args);

		startServer();
	}

	public void startServer() throws Exception
	{
		logger.info("Starting server on port {}", port);
		setPort(port);

		get("/ping", (request, response) -> {
			return "pong";
		}, new JsonTransformer());

		before((request, response) -> {
			boolean authenticated = false;

			String authHeader = request.headers("Authorization");
			String username = getUsername(authHeader);
			String password = getUsername(authHeader);

			if (!StringUtils.isEmpty(username) && !StringUtils.isEmpty(password))
				authenticated = true;

			if (!authenticated)
			{
				// halt(401, "You are not authorized");
			}
		});

		RfxcomBusImpl bus = new RfxcomBusImpl();
		// bus.startBus("COM7");

		XMLConfiguration config = new XMLConfiguration("config.xml");

		String databaseHost = config.getString("database.host", "");
		String databaseName = config.getString("database.name", "");
		String databaseUsername = config.getString("database.username", "");
		String databasePassword = config.getString("database.password", "");

		String connectionString = "jdbc:mysql://" + databaseHost + "/" + databaseName + "?user=" + URLEncoder.encode(databaseUsername, "utf-8")
				+ "&password=" + URLEncoder.encode(databasePassword, "utf-8") + "&zeroDateTimeBehavior=convertToNull";
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("com.mysql.jdbc.Driver");
		dataSource.setUrl(connectionString);

		DataSourceTransactionManager txManager = new DataSourceTransactionManager(dataSource);
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

		post("/login", "application/json", new LoginAction(false), new JsonTransformer());

		post("/device/list", "application/json", new ListDevicesAction(false, jdbcTemplate), new JsonTransformer());
		post("/device/update", "application/json", new UpdateDeviceAction(false, jdbcTemplate), new JsonTransformer());
		post("/device/search", "application/json", new SearchDevicesAction(false, bus), new JsonTransformer());
		post("/device/command", "application/json", new SendCommandAction(false, bus, jdbcTemplate), new JsonTransformer());

		post("/rule/list", "application/json", new ListRulesAction(false, jdbcTemplate), new JsonTransformer());
		post("/rule/update", "application/json", new UpdateRuleAction(false, txManager), new JsonTransformer());

		get("/throwexception", (request, response) -> {
			throw new RuntimeException();
		});

		exception(Exception.class, (ex, request, response) -> {
			ex.printStackTrace();
			response.status(404);
			response.body("Resource not found");
		});

		logger.info("Server started");
	}

	String getUsername(String auth)
	{
		try
		{
			String authHeader = StringUtils.substringAfter(auth, " ");

			byte[] decoded = Base64.getMimeDecoder().decode(authHeader);
			String authString = new String(decoded, "UTF-8");

			return StringUtils.substringBefore(authString, ":");
		}
		catch (Exception ex)
		{
			return null;
		}
	}

	String getPassword(String auth)
	{
		try
		{
			String authHeader = StringUtils.substringAfter(auth, " ");

			byte[] decoded = Base64.getMimeDecoder().decode(authHeader);
			String authString = new String(decoded, "UTF-8");

			return StringUtils.substringAfter(authString, ":");
		}
		catch (Exception ex)
		{
			return null;
		}
	}
}
