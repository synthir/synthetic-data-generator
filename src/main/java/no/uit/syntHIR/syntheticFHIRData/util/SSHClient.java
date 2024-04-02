package no.uit.syntHIR.syntheticFHIRData.util;

import java.io.IOException;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import org.apache.sshd.client.SshClient;
import org.apache.sshd.client.session.ClientSession;
import org.apache.sshd.common.config.keys.FilePasswordProvider;
import org.apache.sshd.common.keyprovider.FileKeyPairProvider;
import org.apache.sshd.common.signature.BuiltinSignatures;
import org.apache.sshd.core.CoreModuleProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SSHClient {

    private SshClient sshClient;
    private String username;
    private String cloudPrivateKeyLocation;
    private String cloudPassword;
    private String host;
    private int port;
    private int defaultTimeoutMinutes;
    
    private static final Logger logger = LoggerFactory.getLogger(SSHClient.class);

    public SSHClient(String username, String host, int port, String cloudPrivateKeyLocation, String cloudPassword) {
    	
    	SshClient client = SshClient.setUpDefaultClient();
		client.setSignatureFactories(Arrays.asList(BuiltinSignatures.ed25519, BuiltinSignatures.ed25519_cert,
				BuiltinSignatures.sk_ssh_ed25519));
		client.start();
		
        this.username = username;
        this.host = host;
        this.port = port;
        this.sshClient = client;
        this.cloudPrivateKeyLocation = cloudPrivateKeyLocation;
        this.cloudPassword = cloudPassword;
    }

    public ClientSession connect()  throws IOException {
    	
    	Duration HEARTBEAT = Duration.ofSeconds(95L);
		Duration TIMEOUT = HEARTBEAT.multipliedBy(10L);
		
		CoreModuleProperties.IDLE_TIMEOUT.set(sshClient, TIMEOUT);
    	
		ClientSession clientSession = sshClient
				.connect(username, host, port)
				.verify(defaultTimeoutMinutes, TimeUnit.MINUTES).getSession();
		
    	//Authenticate and verify
		FileKeyPairProvider provider = new FileKeyPairProvider(Paths.get(cloudPrivateKeyLocation));
		provider.setPasswordFinder(FilePasswordProvider.of(cloudPassword));
		clientSession.setKeyIdentityProvider(provider);
		clientSession.auth().verify(defaultTimeoutMinutes, TimeUnit.MINUTES);
		
 
        logger.debug("SSHClient is connected: {}", clientSession.getConnectionContext());
        
        return clientSession;
    }

    public void startClient() {
        sshClient.start();
        logger.debug("SSHClient is started...");
    }

    public void stopClient() {
        sshClient.stop();
        logger.debug("SSHClient is stopped...");
    }
}
