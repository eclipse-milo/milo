/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.concurrent.ExecutionException;
import org.eclipse.milo.opcua.sdk.test.AbstractClientServerTest;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.channel.SecureChannel;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.ContentFilter;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.RequestHeader;
import org.eclipse.milo.opcua.stack.core.types.structured.ViewDescription;
import org.eclipse.milo.opcua.stack.transport.server.ServiceRequestContext;
import org.junit.jupiter.api.Test;

public class QueryServiceUnsupportedTest extends AbstractClientServerTest {

  @Test
  void queryFirstReturnsServiceUnsupported() {
    UaException exception =
        assertThrows(
            UaException.class,
            () ->
                client.queryFirst(
                    new ViewDescription(NodeId.NULL_VALUE, DateTime.NULL_VALUE, uint(0)),
                    List.of(),
                    new ContentFilter(null),
                    uint(0),
                    uint(0)));

    assertEquals(StatusCodes.Bad_ServiceUnsupported, exception.getStatusCode().getValue());
  }

  @Test
  void queryNextReturnsServiceUnsupported() {
    UaException exception =
        assertThrows(UaException.class, () -> client.queryNext(false, ByteString.NULL_VALUE));

    assertEquals(StatusCodes.Bad_ServiceUnsupported, exception.getStatusCode().getValue());
  }

  @Test
  void missingNonQueryHandlerReturnsNotImplemented() {
    var request = new ReadRequest(newRequestHeader(), 0.0, TimestampsToReturn.Neither, null);

    ExecutionException exception =
        assertThrows(
            ExecutionException.class,
            () ->
                server
                    .getApplicationContext()
                    .handleServiceRequest(unregisteredServiceContext(), request)
                    .get());

    UaException uaException = UaException.extract(exception).orElseThrow();

    assertEquals(StatusCodes.Bad_NotImplemented, uaException.getStatusCode().getValue());
  }

  private static ServiceRequestContext unregisteredServiceContext() {
    ServiceRequestContext context = mock(ServiceRequestContext.class);
    SecureChannel secureChannel = mock(SecureChannel.class);

    when(context.getEndpointUrl()).thenReturn("opc.tcp://localhost:4840/unregistered");
    when(context.getSecureChannel()).thenReturn(secureChannel);
    when(secureChannel.getSecurityPolicy()).thenReturn(SecurityPolicy.Basic256Sha256);

    return context;
  }

  private static RequestHeader newRequestHeader() {
    return new RequestHeader(
        NodeId.NULL_VALUE, DateTime.now(), uint(1), uint(0), null, uint(0), null);
  }
}
