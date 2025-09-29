package org.eclipse.milo.opcua.sdk.client.subscriptions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import org.eclipse.milo.opcua.sdk.test.AbstractClientServerTest;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.junit.jupiter.api.Test;

public class PublishingManagerTest extends AbstractClientServerTest {

  @Test
  void serviceFaultListenersReceivePublishFaults() throws Exception {
    var statusCodeQueue = new LinkedBlockingQueue<StatusCode>();

    client.addFaultListener(
        serviceFault -> statusCodeQueue.add(serviceFault.getResponseHeader().getServiceResult()));

    PublishingManager publishingManager = client.getPublishingManager();

    publishingManager.sendPublishRequest(client.getSession(), new AtomicLong(1));

    StatusCode statusCode = statusCodeQueue.poll(5, TimeUnit.SECONDS);

    assertEquals(new StatusCode(StatusCodes.Bad_NoSubscription), statusCode);
  }
}
