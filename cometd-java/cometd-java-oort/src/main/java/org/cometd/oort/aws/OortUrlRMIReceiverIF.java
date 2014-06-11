package org.cometd.oort.aws;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface OortUrlRMIReceiverIF extends Remote {

	public void registerCometUrl(String cometUrl) throws RemoteException;

    String getUrl() throws RemoteException;

}
