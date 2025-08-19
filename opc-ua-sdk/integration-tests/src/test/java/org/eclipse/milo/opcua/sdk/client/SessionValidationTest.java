package org.eclipse.milo.opcua.sdk.client;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.eclipse.milo.opcua.sdk.test.AbstractClientServerTest;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.junit.jupiter.api.Test;

public class SessionValidationTest extends AbstractClientServerTest {

  @Override
  protected void customizeClientConfig(OpcUaClientConfigBuilder configBuilder) {
    configBuilder.setSessionEndpointValidationEnabled(true);
  }

  @Test
  void clientConnectsWithSessionValidationEnabled() throws UaException {
    assertNotNull(client.connect());
    assertNotNull(client.getSession());
  }
}
