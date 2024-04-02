package no.uit.syntHIR.syntheticFHIRData.engine;

public interface SyntheticFHIRDataEngine {

	public byte[] generateSyntheticDataUsingGretel(String modelConfigurationFilePath, String inputDataFilePath,
			String gretelProjectName, String numberOfSyntheticRecords, String outputDataDirectory);
}
