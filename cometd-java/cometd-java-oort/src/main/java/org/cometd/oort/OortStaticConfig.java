package org.cometd.oort;

import java.util.Properties;

import org.cometd.client.BayeuxClient;

public class OortStaticConfig implements OortConfig {

    public final static String OORT_CLOUD_PARAM = "oort.cloud";

	@Override
	public void configureCloud(Properties properties, Oort oort) throws OortConfigException {
        String cloud = properties.getProperty(OORT_CLOUD_PARAM);
        if (cloud != null && cloud.length() > 0)
        {
            String[] urls = cloud.split(",");
            for (String comet : urls)
            {
                comet = comet.trim();
                if (comet.length() > 0)
                {
                    OortComet oortComet = oort.observeComet(comet);
                    if (oortComet == null)
                        throw new IllegalArgumentException("Invalid value for " + OORT_CLOUD_PARAM);
                    oortComet.waitFor(1000, BayeuxClient.State.CONNECTED, BayeuxClient.State.DISCONNECTED);
                }
            }
        }
	}

	@Override
	public void destroyCloud() {}
}
