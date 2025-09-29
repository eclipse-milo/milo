package org.eclipse.milo.opcua.sdk.client.subscriptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import org.eclipse.milo.opcua.sdk.test.AbstractClientServerTest;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.junit.jupiter.api.Test;

public class PublishingManagerTest extends AbstractClientServerTest {

  @Test
  void serviceFaultListenersReceivePublishFaults() throws Exception {
    var serviceFaultReceivedLatch = new CountDownLatch(1);

    client.addFaultListener(
        serviceFault -> {
          assertEquals(
              StatusCodes.Bad_NoSubscription,
              serviceFault.getResponseHeader().getServiceResult().value());
          serviceFaultReceivedLatch.countDown();
        });

    PublishingManager publishingManager = client.getPublishingManager();

    publishingManager.sendPublishRequest(client.getSession(), new AtomicLong(1));

    assertTrue(serviceFaultReceivedLatch.await(5, TimeUnit.SECONDS));
  }
}
