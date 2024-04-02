package no.uit.syntHIR.syntheticFHIRData.util;

public class BasicConstants {

	// Synthetic Data Generator API related constants

	public static final String GRETEL_CLI_CREATE_MODEL = "gretel models create ";
	public static final String GRETEL_CLI_CREATE_MODEL_CONFIG_FILE_PATH = "--config ";
	public static final String GRETEL_CLI_CREATE_MODEL_NAME = "--name ";
	public static final String GRETEL_CLI_CREATE_MODEL_OUTPUT_DIRECTORY = "--output ";
	public static final String GRETEL_CLI_CREATE_MODEL_INPUT_DIRECTORY = "--in-data ";
	public static final String GRETEL_CLI_CREATE_MODEL_PROJECT_NAME = "--project ";

	public static final String GRETEL_CLI_GENERATE_SYNTHETIC_RECORDS = "gretel records generate --runner local ";
	public static final String GRETEL_CLI_GENERATE_SYNTHETIC_RECORDS_MODEL_ID = "--model-id ";
	public static final String GRETEL_CLI_GENERATE_SYNTHETIC_RECORDS_MODEL_PATH = "--model-path ";
	public static final String GRETEL_CLI_GENERATE_SYNTHETIC_RECORDS_PARAM_NUMBER_OF_RECORDS = "--num-records ";
	public static final String GRETEL_CLI_GENERATE_SYNTHETIC_RECORDS_OUTPUT_DIRECTORY = "--output ";

	// public static final String GRETEL_MODEL_CONFIG_FILE_PATH =
	// "~/.gretel/config-tabular-lstm-registry.yml";
	// public static final String GRETEL_PROJECT_NAME =
	// "nor-registry-synthetic-data";

	// Dataset file storage directory paths

	// Local server
	public static final String LOCAL_INPUT_REAL_DATA_DIRECTORY_PATH = "src/main/resources/Datasets/input";
	public static final String LOCAL_OUTPUT_SYNTHETIC_DATA_DIRECTORY_PATH = "src/main/resources/Datasets/output";

	// Gretel cloud server
	public static final String GRETEL_SERVER_INPUT_REAL_DATA_DIRECTORY_PATH = "/home/pch026/input-data-synthir";
	public static final String GRETEL_SERVER_OUTPUT_SYNTHETIC_DATA_DIRECTORY_PATH = "/home/pch026/output-data-synthir";

	public static final String APPLICATION_CONFIG_KEY_NAME_GRETEL_SERVER_INPUT_REAL_DATA_DIRECTORY_PATH = "gretel.server.real.data.input.directory.path";
	public static final String APPLICATION_CONFIG_KEY_NAME_GRETEL_SERVER_OUTPUT_SYNTHETIC_DATA_DIRECTORY_PATH = "gretel.server.synthetic.data.output.directory.path";

	public static final String APPLICATION_CONFIG_KEY_NAME_GRETEL_CLOUD_HOST_NAME = "gretel.server.host.name";
	public static final String APPLICATION_CONFIG_KEY_NAME_GRETEL_CLOUD_USER_NAME = "gretel.server.user.name";
	public static final String APPLICATION_CONFIG_KEY_NAME_GRETEL_CLOUD_PASSWORD = "gretel.server.password";
	public static final String APPLICATION_CONFIG_KEY_NAME_GRETEL_CLOUD_PRIVATE_KEY = "gretel.server.openssh.private.key.path";
	public static final String APPLICATION_CONFIG_KEY_NAME_GRETEL_MODEL_CONFIG_FILE_PATH = "gretel.server.model.path";
	public static final String APPLICATION_CONFIG_KEY_NAME_GRETEL_PROJECT_NAME = "gretel.project.name";

}
