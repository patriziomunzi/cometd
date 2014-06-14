package org.cometd.oort;

import java.util.Properties;

public interface OortConfig {
	public void configureCloud(Properties properties, Oort oort) throws OortConfigException;
	public void destroyCloud() throws OortConfigException;
}
