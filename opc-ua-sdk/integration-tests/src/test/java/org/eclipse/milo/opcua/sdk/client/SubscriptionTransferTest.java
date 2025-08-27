package org.eclipse.milo.opcua.sdk.client;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.eclipse.milo.opcua.sdk.client.subscriptions.OpcUaSubscription;
import org.eclipse.milo.opcua.sdk.test.AbstractClientServerTest;
import org.eclipse.milo.opcua.sdk.test.TestClient;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.structured.TransferResult;
import org.eclipse.milo.opcua.stack.core.types.structured.TransferSubscriptionsResponse;
import org.junit.jupiter.api.Test;

public class SubscriptionTransferTest extends AbstractClientServerTest {

  @Test
  void unsecureTransferBetweenAnonymousIdentitiesFails() throws UaException {
    var subscription = new OpcUaSubscription(client);
    subscription.create();

    var client2 = TestClient.create(server, cfg -> {});
    client2.connect();

    TransferSubscriptionsResponse response =
        client2.transferSubscriptions(
            List.of(subscription.getSubscriptionId().orElseThrow()), false);

    TransferResult result = requireNonNull(response.getResults())[0];

    assertEquals(StatusCodes.Bad_UserAccessDenied, result.getStatusCode().getValue());
  }
}
