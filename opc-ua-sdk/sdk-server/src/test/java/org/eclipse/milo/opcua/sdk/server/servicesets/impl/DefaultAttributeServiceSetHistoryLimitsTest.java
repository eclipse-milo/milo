/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.servicesets.impl;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import org.eclipse.milo.opcua.sdk.server.AddressSpaceManager;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.OpcUaServerConfig;
import org.eclipse.milo.opcua.sdk.server.OpcUaServerConfigLimits;
import org.eclipse.milo.opcua.sdk.server.Session;
import org.eclipse.milo.opcua.sdk.server.SessionManager;
import org.eclipse.milo.opcua.sdk.server.diagnostics.SessionDiagnostics;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.channel.EncodingLimits;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.PerformUpdateType;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.ContentFilter;
import org.eclipse.milo.opcua.stack.core.types.structured.DeleteEventDetails;
import org.eclipse.milo.opcua.stack.core.types.structured.DeleteRawModifiedDetails;
import org.eclipse.milo.opcua.stack.core.types.structured.EventFilter;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryReadDetails;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryReadRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryReadResult;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryReadValueId;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryUpdateDetails;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryUpdateRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryUpdateResult;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadEventDetails;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadRawModifiedDetails;
import org.eclipse.milo.opcua.stack.core.types.structured.RequestHeader;
import org.eclipse.milo.opcua.stack.core.types.structured.UpdateEventDetails;
import org.eclipse.milo.opcua.stack.transport.server.ServiceRequestContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * OPC 10000-5 6.3.11: the history services are limited by the four MaxNodesPerHistory* operation
 * limits the server publishes, not by the much larger Read and Write limits.
 *
 * <p>The data and event limits differ here, and both sit far below the default Read and Write
 * limits, so each case shows which limit the service applied.
 */
class DefaultAttributeServiceSetHistoryLimitsTest {

  private static final int MAX_DATA = 2;
  private static final int MAX_EVENTS = 3;

  private static final EncodingContext ENCODING_CONTEXT = DefaultEncodingContext.INSTANCE;

  private static final RequestHeader REQUEST_HEADER =
      new RequestHeader(NodeId.NULL_VALUE, DateTime.now(), uint(1), uint(0), null, uint(0), null);

  private final ServiceRequestContext context = mock(ServiceRequestContext.class);

  private DefaultAttributeServiceSet serviceSet;

  @BeforeEach
  void setUp() throws Exception {
    OpcUaServer server = mock(OpcUaServer.class);
    OpcUaServerConfig config = mock(OpcUaServerConfig.class);
    SessionManager sessionManager = mock(SessionManager.class);
    Session session = mock(Session.class);
    AddressSpaceManager addressSpaceManager = mock(AddressSpaceManager.class);

    when(server.getConfig()).thenReturn(config);
    when(config.getLimits()).thenReturn(new HistoryLimits());
    when(config.getEncodingLimits()).thenReturn(EncodingLimits.DEFAULT);
    when(server.getStaticEncodingContext()).thenReturn(ENCODING_CONTEXT);
    when(server.getSessionManager()).thenReturn(sessionManager);
    when(sessionManager.getSession(context, REQUEST_HEADER)).thenReturn(session);
    when(session.getSessionDiagnostics()).thenReturn(new SessionDiagnostics(session));
    when(server.getAddressSpaceManager()).thenReturn(addressSpaceManager);

    when(addressSpaceManager.historyRead(any(), any(), any(), any()))
        .thenAnswer(
            invocation -> {
              List<HistoryReadValueId> nodesToRead = invocation.getArgument(3);

              return Collections.nCopies(
                  nodesToRead.size(), new HistoryReadResult(StatusCode.GOOD, null, null));
            });

    when(addressSpaceManager.historyUpdate(any(), any()))
        .thenAnswer(
            invocation -> {
              List<HistoryUpdateDetails> details = invocation.getArgument(1);

              return Collections.nCopies(
                  details.size(), new HistoryUpdateResult(StatusCode.GOOD, null, null));
            });

    serviceSet = new DefaultAttributeServiceSet(server);
  }

  @Nested
  class HistoryRead {

    @ParameterizedTest(name = "{0} nodes with {1}")
    @MethodSource(
        "org.eclipse.milo.opcua.sdk.server.servicesets.impl."
            + "DefaultAttributeServiceSetHistoryLimitsTest#historyReadsWithinLimit")
    void readWithinTheLimitForItsDetailsTypeIsAccepted(int nodeCount, HistoryReadDetails details)
        throws Exception {

      var response = serviceSet.onHistoryRead(context, historyReadRequest(details, nodeCount));

      assertEquals(nodeCount, response.getResults().length);
    }

    @ParameterizedTest(name = "{0} nodes with {1}")
    @MethodSource(
        "org.eclipse.milo.opcua.sdk.server.servicesets.impl."
            + "DefaultAttributeServiceSetHistoryLimitsTest#historyReadsOverLimit")
    void readOverTheLimitForItsDetailsTypeIsRejected(int nodeCount, HistoryReadDetails details) {
      UaException e =
          assertThrows(
              UaException.class,
              () -> serviceSet.onHistoryRead(context, historyReadRequest(details, nodeCount)));

      assertEquals(StatusCodes.Bad_TooManyOperations, e.getStatusCode().value());
    }
  }

  @Nested
  class HistoryUpdate {

    @ParameterizedTest(name = "{0} data entries and {1} event entries")
    @MethodSource(
        "org.eclipse.milo.opcua.sdk.server.servicesets.impl."
            + "DefaultAttributeServiceSetHistoryLimitsTest#historyUpdatesWithinLimits")
    void updateWithinBothLimitsIsAccepted(int dataCount, int eventCount) throws Exception {
      var response =
          serviceSet.onHistoryUpdate(context, historyUpdateRequest(dataCount, eventCount));

      assertEquals(dataCount + eventCount, response.getResults().length);
    }

    // Data and event entries count against separate limits. An array short enough to pass one
    // limit must still be rejected when the entries of the other kind exceed their own limit.
    @ParameterizedTest(name = "{0} data entries and {1} event entries")
    @MethodSource(
        "org.eclipse.milo.opcua.sdk.server.servicesets.impl."
            + "DefaultAttributeServiceSetHistoryLimitsTest#historyUpdatesOverALimit")
    void updateOverEitherLimitIsRejected(int dataCount, int eventCount) {
      UaException e =
          assertThrows(
              UaException.class,
              () ->
                  serviceSet.onHistoryUpdate(context, historyUpdateRequest(dataCount, eventCount)));

      assertEquals(StatusCodes.Bad_TooManyOperations, e.getStatusCode().value());
    }

    // Both event details types count against the events limit. MAX_EVENTS is above the data limit,
    // so an entry mistaken for a data entry would be rejected here.
    @ParameterizedTest
    @MethodSource(
        "org.eclipse.milo.opcua.sdk.server.servicesets.impl."
            + "DefaultAttributeServiceSetHistoryLimitsTest#eventUpdateDetails")
    void everyEventDetailsTypeCountsAgainstTheEventsLimit(HistoryUpdateDetails eventDetails)
        throws Exception {

      var response =
          serviceSet.onHistoryUpdate(
              context, historyUpdateRequest(Collections.nCopies(MAX_EVENTS, eventDetails)));

      assertEquals(MAX_EVENTS, response.getResults().length);
    }

    // The limit also bounds the work spent on a request. An array longer than any valid request
    // must be rejected without decoding its entries, so undecodable entries never get the chance
    // to fail first.
    @Test
    void oversizedUpdateIsRejectedBeforeItsEntriesAreDecoded() {
      ExtensionObject undecodable =
          ExtensionObject.of(ByteString.of(new byte[] {1, 2, 3}), new NodeId(1, "unknown"));

      var oversized = new ExtensionObject[Math.max(MAX_DATA, MAX_EVENTS) + 1];
      Arrays.fill(oversized, undecodable);

      UaException e =
          assertThrows(
              UaException.class,
              () ->
                  serviceSet.onHistoryUpdate(
                      context, new HistoryUpdateRequest(REQUEST_HEADER, oversized)));

      assertEquals(StatusCodes.Bad_TooManyOperations, e.getStatusCode().value());
    }
  }

  static Stream<Arguments> historyReadsWithinLimit() {
    return Stream.of(
        Arguments.of(MAX_DATA, rawDetails()), Arguments.of(MAX_EVENTS, eventDetails()));
  }

  static Stream<Arguments> historyReadsOverLimit() {
    return Stream.of(
        Arguments.of(MAX_DATA + 1, rawDetails()), Arguments.of(MAX_EVENTS + 1, eventDetails()));
  }

  static Stream<Arguments> historyUpdatesWithinLimits() {
    return Stream.of(
        Arguments.of(MAX_DATA, 0), Arguments.of(0, MAX_EVENTS), Arguments.of(MAX_DATA, 1));
  }

  static Stream<Arguments> historyUpdatesOverALimit() {
    return Stream.of(
        Arguments.of(MAX_DATA + 1, 0),
        Arguments.of(0, MAX_EVENTS + 1),
        Arguments.of(1, MAX_EVENTS),
        Arguments.of(MAX_DATA, MAX_EVENTS));
  }

  static Stream<HistoryUpdateDetails> eventUpdateDetails() {
    var filter = new EventFilter(null, new ContentFilter(null));

    return Stream.of(
        new DeleteEventDetails(new NodeId(1, "event"), null),
        new UpdateEventDetails(new NodeId(1, "event"), PerformUpdateType.Insert, filter, null));
  }

  private static HistoryReadDetails rawDetails() {
    return new ReadRawModifiedDetails(
        false, DateTime.MIN_VALUE, DateTime.MIN_VALUE, uint(0), false);
  }

  private static HistoryReadDetails eventDetails() {
    return new ReadEventDetails(
        uint(0),
        DateTime.MIN_VALUE,
        DateTime.MIN_VALUE,
        new EventFilter(null, new ContentFilter(null)));
  }

  private static HistoryReadRequest historyReadRequest(HistoryReadDetails details, int nodeCount) {
    var nodesToRead = new HistoryReadValueId[nodeCount];

    for (int i = 0; i < nodeCount; i++) {
      nodesToRead[i] = new HistoryReadValueId(new NodeId(1, i), null, null, null);
    }

    return new HistoryReadRequest(
        REQUEST_HEADER,
        ExtensionObject.encode(ENCODING_CONTEXT, details),
        TimestampsToReturn.Both,
        false,
        nodesToRead);
  }

  private static HistoryUpdateRequest historyUpdateRequest(int dataCount, int eventCount) {
    var data =
        new DeleteRawModifiedDetails(
            new NodeId(1, "data"), false, DateTime.MIN_VALUE, DateTime.MIN_VALUE);
    var event = new DeleteEventDetails(new NodeId(1, "event"), null);

    return historyUpdateRequest(
        Stream.concat(
                Collections.nCopies(dataCount, data).stream(),
                Collections.nCopies(eventCount, event).stream())
            .toList());
  }

  private static HistoryUpdateRequest historyUpdateRequest(List<HistoryUpdateDetails> details) {
    ExtensionObject[] historyUpdateDetails =
        details.stream()
            .map(entry -> ExtensionObject.encode(ENCODING_CONTEXT, entry))
            .toArray(ExtensionObject[]::new);

    return new HistoryUpdateRequest(REQUEST_HEADER, historyUpdateDetails);
  }

  /** Publishes small, distinct history limits and keeps the default Read and Write limits. */
  private static class HistoryLimits implements OpcUaServerConfigLimits {

    @Override
    public UInteger getMaxNodesPerHistoryReadData() {
      return uint(MAX_DATA);
    }

    @Override
    public UInteger getMaxNodesPerHistoryReadEvents() {
      return uint(MAX_EVENTS);
    }

    @Override
    public UInteger getMaxNodesPerHistoryUpdateData() {
      return uint(MAX_DATA);
    }

    @Override
    public UInteger getMaxNodesPerHistoryUpdateEvents() {
      return uint(MAX_EVENTS);
    }
  }
}
