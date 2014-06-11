package org.cometd.oort;

import org.cometd.oort.aws.OortAwsConfig;

public class OortConfigFactory {

	private static final String PEER_DISCOVERY_STATIC = "static";
	private static final String PEER_DISCOVERY_MULTICAST = "multicast";
	private static final String PEER_DISCOVERY_AWS = "aws";
	
	public static OortConfig createOortConfigurator(String peerDiscoveryType) throws OortConfigException {
		if(peerDiscoveryType == null) {
			throw new OortConfigException("peerDiscoveryType is null");
		}
		
		if(peerDiscoveryType.equals(PEER_DISCOVERY_STATIC)) {
			return new OortStaticConfig();
		}
		if(peerDiscoveryType.equals(PEER_DISCOVERY_MULTICAST)) {
			return new OortMulticastConfig();
		}
		if(peerDiscoveryType.equals(PEER_DISCOVERY_AWS)) {
			return new OortAwsConfig();
		}

		throw new OortConfigException("Not found peerDiscoveryType");
	}
}
