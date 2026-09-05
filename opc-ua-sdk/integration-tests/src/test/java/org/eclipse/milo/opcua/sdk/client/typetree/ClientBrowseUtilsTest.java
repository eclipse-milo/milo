/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.typetree;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseNextResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseResult;
import org.junit.jupiter.api.Test;

class ClientBrowseUtilsTest {

  // Empty and null tokens both mean Browse is complete and must never reach BrowseNext.
  @Test
  void terminalContinuationPointsDoNotSendAnotherRequest() throws UaException {
    var client = mock(OpcUaClient.class);

    assertEquals(List.of(), ClientBrowseUtils.maybeBrowseNext(client, null));
    assertEquals(List.of(), ClientBrowseUtils.maybeBrowseNext(client, ByteString.NULL_VALUE));
    assertEquals(List.of(), ClientBrowseUtils.maybeBrowseNext(client, ByteString.of(new byte[0])));
    verifyNoInteractions(client);
  }

  // A final empty token must stop the loop after the valid continuation request.
  @Test
  void emptyContinuationPointInResponseFinishesBrowse() throws UaException {
    var client = mock(OpcUaClient.class);
    var response = mock(BrowseNextResponse.class);
    var result = mock(BrowseResult.class);
    var continuationPoint = ByteString.of(new byte[] {1});
    when(client.browseNext(false, List.of(continuationPoint))).thenReturn(response);
    when(response.getResults()).thenReturn(new BrowseResult[] {result});
    when(result.getContinuationPoint()).thenReturn(ByteString.of(new byte[0]));

    assertEquals(List.of(), ClientBrowseUtils.maybeBrowseNext(client, continuationPoint));

    verify(client).browseNext(false, List.of(continuationPoint));
    verifyNoMoreInteractions(client);
  }

  @Test
  void releasesContinuationPointWhenBrowseNextLimitIsReached() throws UaException {
    var client = mock(OpcUaClient.class);
    var response = mock(BrowseNextResponse.class);
    var result = mock(BrowseResult.class);
    var continuationPoint = ByteString.of(new byte[] {1, 2, 3, 4});

    when(client.browseNext(false, List.of(continuationPoint))).thenReturn(response);
    when(response.getResults()).thenReturn(new BrowseResult[] {result});
    when(result.getContinuationPoint()).thenReturn(continuationPoint);

    assertThrows(
        UaException.class, () -> ClientBrowseUtils.maybeBrowseNext(client, continuationPoint));

    verify(client, times(1000)).browseNext(false, List.of(continuationPoint));
    verify(client).browseNext(true, List.of(continuationPoint));
  }
}
