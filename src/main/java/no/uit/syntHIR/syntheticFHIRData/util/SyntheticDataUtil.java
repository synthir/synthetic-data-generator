package no.uit.syntHIR.syntheticFHIRData.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.zip.GZIPInputStream;

import org.apache.sshd.client.SshClient;
import org.apache.sshd.client.channel.ClientChannel;
import org.apache.sshd.client.channel.ClientChannelEvent;
import org.apache.sshd.client.session.ClientSession;
import org.apache.sshd.common.channel.Channel;
import org.apache.sshd.common.config.keys.FilePasswordProvider;
import org.apache.sshd.common.keyprovider.FileKeyPairProvider;
import org.apache.sshd.common.signature.BuiltinSignatures;
import org.apache.sshd.core.CoreModuleProperties;
import org.apache.sshd.scp.client.ScpClient;
import org.apache.sshd.scp.client.ScpClientCreator;

public class SyntheticDataUtil {

	/**
	 * Establish connection with a server on
	 * which Gretel is installed using the
	 * server credentials and openssh private key
	 * Once the connection is established, execute
	 * Gretel commands using Gretel CLI to create model
	 * and then generate synthetic data using the model
	 * 
	 * @param userName
	 * @param hostName
	 * @param cloudPrivateKeyLocation
	 * @param cloudPassword
	 * @param commandToExecute
	 * @return text file containing the output of the command executed using Gretel
	 *         CLI
	 */
	public static String establishConnectionAndExecuteCommandWithGretelCloud(String userName, String hostName,
			String cloudPrivateKeyLocation, String cloudPassword, String commandToExecute) {

		String commandOutputFile = "gretel-command-response.txt";

		System.out.println("Command to execute on server: " + commandToExecute);
		int defaultTimeoutMinutes = 60;
		SshClient client = SshClient.setUpDefaultClient();
		client.setSignatureFactories(Arrays.asList(BuiltinSignatures.ed25519, BuiltinSignatures.ed25519_cert,
				BuiltinSignatures.sk_ssh_ed25519));
		client.start();

		Duration HEARTBEAT = Duration.ofSeconds(95L);
		Duration TIMEOUT = HEARTBEAT.multipliedBy(10L);

		CoreModuleProperties.IDLE_TIMEOUT.set(client, TIMEOUT);

		try (ClientSession session = client
				.connect(userName, hostName, 22)
				.verify(defaultTimeoutMinutes, TimeUnit.MINUTES).getSession()) {

			FileKeyPairProvider provider = new FileKeyPairProvider(Paths.get(cloudPrivateKeyLocation));
			provider.setPasswordFinder(FilePasswordProvider.of(cloudPassword));
			session.setKeyIdentityProvider(provider);
			session.auth().verify(defaultTimeoutMinutes, TimeUnit.MINUTES);

			System.out.println("IDLE TIMEOUT:===" + session.getIdleTimeout());

			try (ByteArrayOutputStream responseStream = new ByteArrayOutputStream();
					ClientChannel channel = session.createChannel(Channel.CHANNEL_SHELL)) {
				channel.setOut(responseStream);

				try {
					channel.open().verify(defaultTimeoutMinutes, TimeUnit.MINUTES);

					try (OutputStream pipedIn = channel.getInvertedIn()) {

						pipedIn.write(commandToExecute.getBytes());
						pipedIn.flush();

					}

					channel.waitFor(EnumSet.of(ClientChannelEvent.CLOSED),
							TimeUnit.MINUTES.toMillis(defaultTimeoutMinutes));

					try (OutputStream outputStream = new FileOutputStream(commandOutputFile)) {
						responseStream.writeTo(outputStream);
						outputStream.close();
					}

					String responseString = new String(responseStream.toByteArray());
					System.out.println("response String==" + responseString);

				} finally {
					channel.close(false);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			client.stop();
		}
		return commandOutputFile;
	}

	public static String searchForTextInFile(String filePath, String searchText) {

		String textToReturn = null;
		File file = new File(filePath);

		try {
			Scanner scanner = new Scanner(file);
			// now read the file line by line...
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine().toLowerCase().toString();
				if (line.contains(searchText.toLowerCase().toString())) {
					// System.out.println("Search Text line: " +line);
					textToReturn = line;
				}
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			// handle this
		}
		return textToReturn;
	}

	public static String uploadDataToCloudServer(String localDataFileDirectoryPath, String serverDataDirectoryPath,
			String gretelCloudUserName, String gretelCloudHostName,
			String gretelCloudPassword, String gretelCloudOpenSshKey) {

		int defaultTimeoutMinutes = 60;
		SshClient client = SshClient.setUpDefaultClient();
		client.setSignatureFactories(Arrays.asList(BuiltinSignatures.ed25519, BuiltinSignatures.ed25519_cert,
				BuiltinSignatures.sk_ssh_ed25519));
		client.start();

		Duration HEARTBEAT = Duration.ofSeconds(95L);
		Duration TIMEOUT = HEARTBEAT.multipliedBy(10L);

		CoreModuleProperties.IDLE_TIMEOUT.set(client, TIMEOUT);

		try (ClientSession session = client
				.connect(gretelCloudUserName, gretelCloudHostName, 22)
				.verify(defaultTimeoutMinutes, TimeUnit.MINUTES).getSession()) {

			FileKeyPairProvider provider = new FileKeyPairProvider(Paths.get(gretelCloudOpenSshKey));
			provider.setPasswordFinder(FilePasswordProvider.of(gretelCloudPassword));
			session.setKeyIdentityProvider(provider);
			session.auth().verify(defaultTimeoutMinutes, TimeUnit.MINUTES);

			System.out.println("IDLE TIMEOUT:===" + session.getIdleTimeout());

			ScpClientCreator creator = ScpClientCreator.instance();
			ScpClient scpClient = creator.createScpClient(session);
			scpClient.upload(
					Paths.get(localDataFileDirectoryPath),
					serverDataDirectoryPath,
					ScpClient.Option.Recursive,
					ScpClient.Option.PreserveAttributes,
					ScpClient.Option.TargetIsDirectory);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return serverDataDirectoryPath + APIConstants.REQUEST_SEPARATOR
				+ new File(localDataFileDirectoryPath).getName();
	}

	/**
	 * To download file from a server
	 * 
	 * @param serverDataFileDirectoryPath
	 * @param localDataFileDirectoryPath
	 * @param gretelCloudUserName
	 * @param gretelCloudHostName
	 * @param gretelCloudPassword
	 * @param gretelCloudOpenSshKey
	 * @return
	 */
	public static String downloadDataFromCloudServer(String serverDataFileDirectoryPath,
			String localDataFileDirectoryPath, String gretelCloudUserName, String gretelCloudHostName,
			String gretelCloudPassword, String gretelCloudOpenSshKey) {

		int defaultTimeoutMinutes = 60;
		SshClient client = SshClient.setUpDefaultClient();
		client.setSignatureFactories(Arrays.asList(BuiltinSignatures.ed25519, BuiltinSignatures.ed25519_cert,
				BuiltinSignatures.sk_ssh_ed25519));
		client.start();

		Duration HEARTBEAT = Duration.ofSeconds(95L);
		Duration TIMEOUT = HEARTBEAT.multipliedBy(10L);

		CoreModuleProperties.IDLE_TIMEOUT.set(client, TIMEOUT);

		try (ClientSession session = client
				.connect(gretelCloudUserName, gretelCloudHostName, 22)
				.verify(defaultTimeoutMinutes, TimeUnit.MINUTES).getSession()) {

			FileKeyPairProvider provider = new FileKeyPairProvider(Paths.get(gretelCloudOpenSshKey));
			provider.setPasswordFinder(FilePasswordProvider.of(gretelCloudPassword));
			session.setKeyIdentityProvider(provider);
			session.auth().verify(defaultTimeoutMinutes, TimeUnit.MINUTES);

			System.out.println("IDLE TIMEOUT:===" + session.getIdleTimeout());

			ScpClientCreator creator = ScpClientCreator.instance();
			ScpClient scpClient = creator.createScpClient(session);
			scpClient.download(serverDataFileDirectoryPath,
					Paths.get(
							localDataFileDirectoryPath),
					ScpClient.Option.Recursive,
					ScpClient.Option.PreserveAttributes,
					ScpClient.Option.TargetIsDirectory);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return localDataFileDirectoryPath;
	}

	/**
	 * To unzip a file
	 * 
	 * @param zipFilePath
	 * @return byte[] of the decompressed file
	 */
	public static byte[] unzipSyntheticDataFromGretel(String zipFilePath) {

		Path source = Paths.get(zipFilePath);
		ByteArrayOutputStream output = new ByteArrayOutputStream();

		try (GZIPInputStream gis = new GZIPInputStream(
				new FileInputStream(source.toFile()))) {

			// copy GZIPInputStream to ByteArrayOutputStream
			byte[] buffer = new byte[1024];
			int len;
			while ((len = gis.read(buffer)) > 0) {
				output.write(buffer, 0, len);
			}

			// byte[] bytes = output.toByteArray();
			// convert byte[] to string for display
			// System.out.println(new String(bytes, StandardCharsets.UTF_8));

		} catch (IOException e) {
			e.printStackTrace();
		}

		return output.toByteArray();
	}

}
