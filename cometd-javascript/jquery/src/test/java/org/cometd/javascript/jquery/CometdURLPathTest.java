package org.cometd.javascript.jquery;

import javax.servlet.http.HttpServletRequest;

import org.cometd.Bayeux;
import org.cometd.Client;
import org.cometd.Extension;
import org.cometd.Message;
import org.cometd.javascript.Latch;
import org.cometd.server.AbstractBayeux;

/**
 * @version $Revision$ $Date$
 */
public class CometdURLPathTest extends AbstractCometdJQueryTest
{
    @Override
    protected void customizeBayeux(AbstractBayeux bayeux)
    {
        bayeux.addExtension(new BayeuxURLExtension(bayeux));
    }

    public void testURLPath() throws Exception
    {
        defineClass(Latch.class);
        evaluateScript("var connectLatch = new Latch(1);");
        Latch connectLatch = get("connectLatch");
        evaluateScript("var handshake = undefined;");
        evaluateScript("var connect = undefined;");
        evaluateScript("$.cometd.addListener('/meta/handshake', function(message) { handshake = message; });");
        evaluateScript("$.cometd.addListener('/meta/connect', function(message) { connect = message; connectLatch.countDown(); });");
        evaluateScript("$.cometd.init({url: '" + cometdURL + "/', logLevel: 'debug'})");
        assertTrue(connectLatch.await(1000));

        evaluateScript("window.assert(handshake !== undefined, 'handshake is undefined');");
        evaluateScript("window.assert(handshake.ext !== undefined, 'handshake without ext');");
        String handshakeURI = evaluateScript("handshake.ext.uri");
        assertTrue(handshakeURI.endsWith("/handshake"));

        evaluateScript("window.assert(connect !== undefined, 'connect is undefined');");
        evaluateScript("window.assert(connect.ext !== undefined, 'connect without ext');");
        String connectURI = evaluateScript("connect.ext.uri");
        assertTrue(connectURI.endsWith("/connect"));

        evaluateScript("var disconnectLatch = new Latch(1);");
        Latch disconnectLatch = get("disconnectLatch");
        evaluateScript("var disconnect = undefined;");
        evaluateScript("$.cometd.addListener('/meta/disconnect', function(message) { disconnect = message; disconnectLatch.countDown(); });");
        evaluateScript("$.cometd.disconnect();");
        assertTrue(disconnectLatch.await(1000));

        evaluateScript("window.assert(disconnect !== undefined, 'disconnect is undefined');");
        evaluateScript("window.assert(disconnect.ext !== undefined, 'disconnect without ext');");
        String disconnectURI = evaluateScript("disconnect.ext.uri");
        assertTrue(disconnectURI.endsWith("/disconnect"));
    }

    public static class BayeuxURLExtension implements Extension
    {
        private final Bayeux bayeux;

        public BayeuxURLExtension(Bayeux bayeux)
        {
            this.bayeux = bayeux;
        }

        public Message rcv(Client from, Message message)
        {
            return message;
        }

        public Message rcvMeta(Client from, Message message)
        {
            return message;
        }

        public Message send(Client from, Message message)
        {
            return message;
        }

        public Message sendMeta(Client from, Message message)
        {
            if (Bayeux.META_HANDSHAKE.equals(message.getChannel()) ||
                    Bayeux.META_CONNECT.equals(message.getChannel()) ||
                    Bayeux.META_DISCONNECT.equals(message.getChannel()))
            {
                HttpServletRequest request = bayeux.getCurrentRequest();
                String uri = request.getRequestURI();
                message.getExt(true).put("uri", uri);
            }
            return message;
        }
    }
}
