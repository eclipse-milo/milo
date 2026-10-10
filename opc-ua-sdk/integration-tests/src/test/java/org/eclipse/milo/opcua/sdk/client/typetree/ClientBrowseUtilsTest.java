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

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.OperationLimits;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseNextResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseResult;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId;
import org.eclipse.milo.opcua.stack.core.types.structured.ReferenceDescription;
import org.jspecify.annotations.Nullable;
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
    var continuationPoint = ByteString.of(new byte[] {1});
    when(client.browseNext(false, List.of(continuationPoint)))
        .thenReturn(
            browseNextResponse(
                new BrowseResult(StatusCode.GOOD, ByteString.of(new byte[0]), null)));

    assertEquals(List.of(), ClientBrowseUtils.maybeBrowseNext(client, continuationPoint));

    verify(client).browseNext(false, List.of(continuationPoint));
    verifyNoMoreInteractions(client);
  }

  @Test
  void missingReadLimitUsesSingletonRequests() throws UaException {
    assertReadLimitUsesSingletonRequests(null);
  }

  @Test
  void zeroReadLimitUsesSingletonRequests() throws UaException {
    assertReadLimitUsesSingletonRequests(uint(0));
  }

  private static void assertReadLimitUsesSingletonRequests(@Nullable UInteger operationLimit)
      throws UaException {

    var client = mock(OpcUaClient.class);
    List<ReadValueId> requests = readRequests(3);
    List<DataValue> values = dataValues(3);
    var calls = new ArrayList<List<ReadValueId>>();

    when(client.read(eq(0.0), eq(TimestampsToReturn.Neither), anyList()))
        .thenAnswer(
            invocation -> {
              List<ReadValueId> partition = invocation.getArgument(2);
              calls.add(List.copyOf(partition));
              return readResponse(partition, requests, values);
            });

    List<DataValue> result =
        ClientBrowseUtils.readWithOperationLimits(
            client, requests, operationLimits(operationLimit, null));

    assertEquals(values, result);
    assertEquals(singletonPartitions(requests), calls);
  }

  @Test
  void missingBrowseLimitUsesSingletonRequests() throws UaException {
    assertBrowseLimitUsesSingletonRequests(null);
  }

  @Test
  void zeroBrowseLimitUsesSingletonRequests() throws UaException {
    assertBrowseLimitUsesSingletonRequests(uint(0));
  }

  private static void assertBrowseLimitUsesSingletonRequests(@Nullable UInteger operationLimit)
      throws UaException {

    var client = mock(OpcUaClient.class);
    List<BrowseDescription> requests = browseRequests(3);
    List<ReferenceDescription> references = references(3);
    var calls = new ArrayList<List<BrowseDescription>>();

    when(client.browse(anyList()))
        .thenAnswer(
            invocation -> {
              List<BrowseDescription> partition = invocation.getArgument(0);
              calls.add(List.copyOf(partition));
              return browseResults(partition, requests, references);
            });

    List<List<ReferenceDescription>> result =
        ClientBrowseUtils.browseWithOperationLimits(
            client, requests, operationLimits(null, operationLimit));

    assertEquals(wrapEach(references), result);
    assertEquals(singletonPartitions(requests), calls);
  }

  // UInt32 limits above Integer.MAX_VALUE remain valid and must not overflow into an invalid
  // partition size.
  @Test
  void maximumUnsignedReadLimitUsesOnePositivePartition() throws UaException {
    var client = mock(OpcUaClient.class);
    List<ReadValueId> requests = readRequests(3);
    List<DataValue> values = dataValues(3);
    var calls = new ArrayList<List<ReadValueId>>();

    when(client.read(eq(0.0), eq(TimestampsToReturn.Neither), anyList()))
        .thenAnswer(
            invocation -> {
              List<ReadValueId> partition = invocation.getArgument(2);
              calls.add(List.copyOf(partition));
              return readResponse(partition, requests, values);
            });

    List<DataValue> result =
        ClientBrowseUtils.readWithOperationLimits(
            client, requests, operationLimits(UInteger.MAX, null));

    assertEquals(values, result);
    assertEquals(List.of(requests), calls);
  }

  // UInt32 limits above Integer.MAX_VALUE remain valid and must not overflow into an invalid
  // partition size.
  @Test
  void maximumUnsignedBrowseLimitUsesOnePositivePartition() throws UaException {
    var client = mock(OpcUaClient.class);
    List<BrowseDescription> requests = browseRequests(3);
    List<ReferenceDescription> references = references(3);
    var calls = new ArrayList<List<BrowseDescription>>();

    when(client.browse(anyList()))
        .thenAnswer(
            invocation -> {
              List<BrowseDescription> partition = invocation.getArgument(0);
              calls.add(List.copyOf(partition));
              return browseResults(partition, requests, references);
            });

    List<List<ReferenceDescription>> result =
        ClientBrowseUtils.browseWithOperationLimits(
            client, requests, operationLimits(null, UInteger.MAX));

    assertEquals(wrapEach(references), result);
    assertEquals(List.of(requests), calls);
  }

  // A stale advertised limit may reject one batch, but retrying it and the remaining Reads as
  // singletons must retain the original result order.
  @Test
  void rejectedReadPartitionFallsBackToOrderedSingletonRequests() throws UaException {
    var client = mock(OpcUaClient.class);
    List<ReadValueId> requests = readRequests(5);
    List<DataValue> values = dataValues(5);
    var calls = new ArrayList<List<ReadValueId>>();

    when(client.read(eq(0.0), eq(TimestampsToReturn.Neither), anyList()))
        .thenAnswer(
            invocation -> {
              List<ReadValueId> partition = invocation.getArgument(2);
              calls.add(List.copyOf(partition));
              if (partition.size() > 1) {
                throw new UaException(StatusCodes.Bad_TooManyOperations);
              }
              return readResponse(partition, requests, values);
            });

    List<DataValue> result =
        ClientBrowseUtils.readWithOperationLimits(client, requests, operationLimits(uint(3), null));

    var expectedCalls = new ArrayList<List<ReadValueId>>();
    expectedCalls.add(List.copyOf(requests.subList(0, 3)));
    expectedCalls.addAll(singletonPartitions(requests));

    assertEquals(values, result);
    assertEquals(expectedCalls, calls);
  }

  // A stale advertised limit may reject one batch, but retrying it and the remaining Browses as
  // singletons must retain the original result order and one result per input.
  @Test
  void rejectedBrowsePartitionFallsBackToOrderedSingletonRequests() throws UaException {
    var client = mock(OpcUaClient.class);
    List<BrowseDescription> requests = browseRequests(5);
    List<ReferenceDescription> references = references(5);
    var calls = new ArrayList<List<BrowseDescription>>();

    when(client.browse(anyList()))
        .thenAnswer(
            invocation -> {
              List<BrowseDescription> partition = invocation.getArgument(0);
              calls.add(List.copyOf(partition));
              if (partition.size() > 1) {
                throw new UaException(StatusCodes.Bad_TooManyOperations);
              }
              return browseResults(partition, requests, references);
            });

    List<List<ReferenceDescription>> result =
        ClientBrowseUtils.browseWithOperationLimits(
            client, requests, operationLimits(null, uint(3)));

    var expectedCalls = new ArrayList<List<BrowseDescription>>();
    expectedCalls.add(List.copyOf(requests.subList(0, 3)));
    expectedCalls.addAll(singletonPartitions(requests));

    assertEquals(wrapEach(references), result);
    assertEquals(expectedCalls, calls);
  }

  // Only Bad_TooManyOperations identifies an oversized request; another Read service failure must
  // propagate without replaying any operation.
  @Test
  void nonRetryableReadFailurePropagatesWithoutRetry() throws UaException {
    var client = mock(OpcUaClient.class);
    List<ReadValueId> requests = readRequests(3);
    var calls = new ArrayList<List<ReadValueId>>();
    var failure = new UaException(StatusCodes.Bad_Timeout);

    when(client.read(eq(0.0), eq(TimestampsToReturn.Neither), anyList()))
        .thenAnswer(
            invocation -> {
              List<ReadValueId> partition = invocation.getArgument(2);
              calls.add(List.copyOf(partition));
              throw failure;
            });

    UaException thrown =
        assertThrows(
            UaException.class,
            () ->
                ClientBrowseUtils.readWithOperationLimits(
                    client, requests, operationLimits(uint(2), null)));

    assertSame(failure, thrown);
    assertEquals(List.of(requests.subList(0, 2)), calls);
  }

  // Only Bad_TooManyOperations identifies an oversized request; another Browse service failure
  // must propagate without replaying any operation.
  @Test
  void nonRetryableBrowseFailurePropagatesWithoutRetry() throws UaException {
    var client = mock(OpcUaClient.class);
    List<BrowseDescription> requests = browseRequests(3);
    var calls = new ArrayList<List<BrowseDescription>>();
    var failure = new UaException(StatusCodes.Bad_Timeout);

    when(client.browse(anyList()))
        .thenAnswer(
            invocation -> {
              List<BrowseDescription> partition = invocation.getArgument(0);
              calls.add(List.copyOf(partition));
              throw failure;
            });

    UaException thrown =
        assertThrows(
            UaException.class,
            () ->
                ClientBrowseUtils.browseWithOperationLimits(
                    client, requests, operationLimits(null, uint(2))));

    assertSame(failure, thrown);
    assertEquals(List.of(requests.subList(0, 2)), calls);
  }

  // BrowseNext already sends one continuation point per request. Its failure must not replay the
  // successful multi-node Browse request that produced the continuation point.
  @Test
  void browseNextTooManyOperationsDoesNotRetryTheInitialBrowse() throws UaException {
    var client = mock(OpcUaClient.class);
    List<BrowseDescription> requests = browseRequests(2);
    var continuationPoint = ByteString.of(new byte[] {1});
    var failure = new UaException(StatusCodes.Bad_TooManyOperations);

    when(client.browse(requests))
        .thenReturn(
            List.of(
                new BrowseResult(StatusCode.GOOD, continuationPoint, null),
                new BrowseResult(StatusCode.GOOD, ByteString.NULL_VALUE, null)));
    when(client.browseNext(false, List.of(continuationPoint))).thenThrow(failure);

    UaException thrown =
        assertThrows(
            UaException.class,
            () ->
                ClientBrowseUtils.browseWithOperationLimits(
                    client, requests, operationLimits(null, uint(2))));

    assertSame(failure, thrown);
    verify(client).browse(requests);
  }

  // A singleton Bad_TooManyOperations cannot be reduced further and must not cause an infinite
  // retry loop.
  @Test
  void singletonReadRejectionPropagatesWithoutRetry() throws UaException {
    var client = mock(OpcUaClient.class);
    List<ReadValueId> requests = readRequests(2);
    var calls = new ArrayList<List<ReadValueId>>();
    var failure = new UaException(StatusCodes.Bad_TooManyOperations);

    when(client.read(eq(0.0), eq(TimestampsToReturn.Neither), anyList()))
        .thenAnswer(
            invocation -> {
              List<ReadValueId> partition = invocation.getArgument(2);
              calls.add(List.copyOf(partition));
              throw failure;
            });

    UaException thrown =
        assertThrows(
            UaException.class,
            () ->
                ClientBrowseUtils.readWithOperationLimits(
                    client, requests, operationLimits(null, null)));

    assertSame(failure, thrown);
    assertEquals(List.of(List.of(requests.get(0))), calls);
  }

  // A singleton Bad_TooManyOperations cannot be reduced further and must not cause an infinite
  // retry loop.
  @Test
  void singletonBrowseRejectionPropagatesWithoutRetry() throws UaException {
    var client = mock(OpcUaClient.class);
    List<BrowseDescription> requests = browseRequests(2);
    var calls = new ArrayList<List<BrowseDescription>>();
    var failure = new UaException(StatusCodes.Bad_TooManyOperations);

    when(client.browse(anyList()))
        .thenAnswer(
            invocation -> {
              List<BrowseDescription> partition = invocation.getArgument(0);
              calls.add(List.copyOf(partition));
              throw failure;
            });

    UaException thrown =
        assertThrows(
            UaException.class,
            () ->
                ClientBrowseUtils.browseWithOperationLimits(
                    client, requests, operationLimits(null, null)));

    assertSame(failure, thrown);
    assertEquals(List.of(List.of(requests.get(0))), calls);
  }

  @Test
  void releasesContinuationPointWhenBrowseNextLimitIsReached() throws UaException {
    var client = mock(OpcUaClient.class);
    var continuationPoint = ByteString.of(new byte[] {1, 2, 3, 4});

    when(client.browseNext(false, List.of(continuationPoint)))
        .thenReturn(browseNextResponse(new BrowseResult(StatusCode.GOOD, continuationPoint, null)));

    assertThrows(
        UaException.class, () -> ClientBrowseUtils.maybeBrowseNext(client, continuationPoint));

    verify(client, times(1000)).browseNext(false, List.of(continuationPoint));
    verify(client).browseNext(true, List.of(continuationPoint));
  }

  /**
   * Part 4 §7.9: a Server frees a Session's ContinuationPoints when another request on the Session
   * needs them, and answers Bad_ContinuationPointInvalid when the Client then uses one. Returning
   * the first page would let a type tree build cache it as the Node's complete references.
   */
  @Test
  void badBrowseNextResultFailsTheBrowse() throws UaException {
    var client = mock(OpcUaClient.class);
    List<BrowseDescription> requests = browseRequests(1);
    List<ReferenceDescription> references = references(1);
    var continuationPoint = ByteString.of(new byte[] {1});

    when(client.browse(requests))
        .thenReturn(
            List.of(
                new BrowseResult(
                    StatusCode.GOOD,
                    continuationPoint,
                    new ReferenceDescription[] {references.get(0)})));
    when(client.browseNext(false, List.of(continuationPoint)))
        .thenReturn(
            browseNextResponse(
                new BrowseResult(
                    new StatusCode(StatusCodes.Bad_ContinuationPointInvalid),
                    ByteString.NULL_VALUE,
                    null)));

    UaException thrown =
        assertThrows(
            UaException.class,
            () ->
                ClientBrowseUtils.browseWithOperationLimits(
                    client, requests, operationLimits(null, null)));

    assertEquals(StatusCodes.Bad_ContinuationPointInvalid, thrown.getStatusCode().value());
    verify(client).browse(requests);
    verify(client).browseNext(false, List.of(continuationPoint));
    verifyNoMoreInteractions(client);
  }

  /**
   * Part 4 §7.9: a ContinuationPoint stays active until the Client releases it or the Session
   * closes. A Bad BrowseNext result that still carries one is released before the browse fails.
   */
  @Test
  void badBrowseNextResultWithContinuationPointFailsAndReleasesIt() throws UaException {
    var client = mock(OpcUaClient.class);
    var continuationPoint = ByteString.of(new byte[] {1});
    var nextContinuationPoint = ByteString.of(new byte[] {2});

    when(client.browseNext(false, List.of(continuationPoint)))
        .thenReturn(
            browseNextResponse(
                new BrowseResult(
                    new StatusCode(StatusCodes.Bad_NodeNotInView), nextContinuationPoint, null)));

    UaException thrown =
        assertThrows(
            UaException.class, () -> ClientBrowseUtils.maybeBrowseNext(client, continuationPoint));

    assertEquals(StatusCodes.Bad_NodeNotInView, thrown.getStatusCode().value());
    verify(client).browseNext(true, List.of(nextContinuationPoint));
  }

  /**
   * Part 4 §5.9.2.4: Uncertain_NotAllNodesAvailable means the results may be incomplete, not that
   * they are wrong. The first page's references are kept and its ContinuationPoint is followed, the
   * same as an Uncertain BrowseNext page.
   */
  @Test
  void uncertainFirstBrowseResultKeepsReferencesAndFollowsContinuationPoint() throws UaException {

    var client = mock(OpcUaClient.class);
    List<BrowseDescription> requests = browseRequests(1);
    List<ReferenceDescription> references = references(2);
    var continuationPoint = ByteString.of(new byte[] {1});

    when(client.browse(requests))
        .thenReturn(
            List.of(
                new BrowseResult(
                    new StatusCode(StatusCodes.Uncertain_NotAllNodesAvailable),
                    continuationPoint,
                    new ReferenceDescription[] {references.get(0)})));
    when(client.browseNext(false, List.of(continuationPoint)))
        .thenReturn(
            browseNextResponse(
                new BrowseResult(
                    StatusCode.GOOD,
                    ByteString.NULL_VALUE,
                    new ReferenceDescription[] {references.get(1)})));

    List<List<ReferenceDescription>> result =
        ClientBrowseUtils.browseWithOperationLimits(client, requests, operationLimits(null, null));

    assertEquals(List.of(references), result);
  }

  /**
   * A Bad first result is an operation-level failure that yields no references for that Node only.
   * Part 4 §7.6 gives a Bad result no ContinuationPoint, but if a Server sends one anyway it is
   * released rather than held until the Session closes.
   */
  @Test
  void badFirstBrowseResultYieldsEmptyListAndReleasesAnyContinuationPoint() throws UaException {
    var client = mock(OpcUaClient.class);
    List<BrowseDescription> requests = browseRequests(3);
    List<ReferenceDescription> references = references(1);
    var continuationPoint = ByteString.of(new byte[] {1});
    var badStatus = new StatusCode(StatusCodes.Bad_NodeIdUnknown);

    when(client.browse(requests))
        .thenReturn(
            List.of(
                new BrowseResult(badStatus, ByteString.NULL_VALUE, null),
                new BrowseResult(badStatus, continuationPoint, null),
                new BrowseResult(
                    StatusCode.GOOD,
                    ByteString.NULL_VALUE,
                    new ReferenceDescription[] {references.get(0)})));

    List<List<ReferenceDescription>> result =
        ClientBrowseUtils.browseWithOperationLimits(
            client, requests, operationLimits(null, uint(3)));

    assertEquals(List.of(List.of(), List.of(), references), result);
    verify(client).browse(requests);
    verify(client).browseNext(true, List.of(continuationPoint));
    verifyNoMoreInteractions(client);
  }

  /**
   * Each result in a multi-node Browse response can carry its own ContinuationPoint. When paging
   * one Node fails, the ContinuationPoints of the Nodes not yet paged are released rather than held
   * by the Server until the Session closes (Part 4 §7.9).
   */
  @Test
  void failedBrowseNextReleasesContinuationPointsNotYetFollowed() throws UaException {
    var client = mock(OpcUaClient.class);
    List<BrowseDescription> requests = browseRequests(3);
    var continuationPoint0 = ByteString.of(new byte[] {0});
    var continuationPoint1 = ByteString.of(new byte[] {1});

    when(client.browse(requests))
        .thenReturn(
            List.of(
                new BrowseResult(StatusCode.GOOD, continuationPoint0, null),
                new BrowseResult(StatusCode.GOOD, continuationPoint1, null),
                new BrowseResult(StatusCode.GOOD, ByteString.NULL_VALUE, null)));
    when(client.browseNext(false, List.of(continuationPoint0)))
        .thenReturn(
            browseNextResponse(
                new BrowseResult(
                    new StatusCode(StatusCodes.Bad_ContinuationPointInvalid),
                    ByteString.NULL_VALUE,
                    null)));

    UaException thrown =
        assertThrows(
            UaException.class,
            () ->
                ClientBrowseUtils.browseWithOperationLimits(
                    client, requests, operationLimits(null, uint(3))));

    assertEquals(StatusCodes.Bad_ContinuationPointInvalid, thrown.getStatusCode().value());
    verify(client).browse(requests);
    verify(client).browseNext(false, List.of(continuationPoint0));
    verify(client).browseNext(true, List.of(continuationPoint1));
    verifyNoMoreInteractions(client);
  }

  // Releasing ContinuationPoints is cleanup. If it fails, callers still need the Bad BrowseNext
  // status that ended the browse, with the release failures attached as suppressed exceptions.
  @Test
  void failedReleaseKeepsTheBadBrowseNextStatus() throws UaException {
    var client = mock(OpcUaClient.class);
    List<BrowseDescription> requests = browseRequests(2);
    var continuationPoint0 = ByteString.of(new byte[] {0});
    var continuationPoint1 = ByteString.of(new byte[] {1});
    var nextContinuationPoint0 = ByteString.of(new byte[] {2});
    var releaseFailure = new UaException(StatusCodes.Bad_Timeout);

    when(client.browse(requests))
        .thenReturn(
            List.of(
                new BrowseResult(StatusCode.GOOD, continuationPoint0, null),
                new BrowseResult(StatusCode.GOOD, continuationPoint1, null)));
    when(client.browseNext(false, List.of(continuationPoint0)))
        .thenReturn(
            browseNextResponse(
                new BrowseResult(
                    new StatusCode(StatusCodes.Bad_NodeNotInView), nextContinuationPoint0, null)));
    when(client.browseNext(eq(true), anyList())).thenThrow(releaseFailure);

    UaException thrown =
        assertThrows(
            UaException.class,
            () ->
                ClientBrowseUtils.browseWithOperationLimits(
                    client, requests, operationLimits(null, uint(2))));

    assertEquals(StatusCodes.Bad_NodeNotInView, thrown.getStatusCode().value());
    assertEquals(List.of(releaseFailure, releaseFailure), List.of(thrown.getSuppressed()));
    verify(client).browseNext(true, List.of(nextContinuationPoint0));
    verify(client).browseNext(true, List.of(continuationPoint1));
  }

  private static OperationLimits operationLimits(
      @Nullable UInteger maxNodesPerRead, @Nullable UInteger maxNodesPerBrowse) {

    return new OperationLimits(
        maxNodesPerRead,
        null,
        null,
        maxNodesPerBrowse,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null);
  }

  private static List<ReadValueId> readRequests(int count) {
    return IntStream.range(0, count).mapToObj(i -> mock(ReadValueId.class, "read-" + i)).toList();
  }

  private static List<DataValue> dataValues(int count) {
    return IntStream.range(0, count).mapToObj(i -> DataValue.valueOnly(new Variant(i))).toList();
  }

  private static ReadResponse readResponse(
      List<ReadValueId> partition, List<ReadValueId> requests, List<DataValue> values) {

    DataValue[] results =
        partition.stream()
            .map(request -> values.get(requests.indexOf(request)))
            .toArray(DataValue[]::new);

    return new ReadResponse(null, results, null);
  }

  private static List<BrowseDescription> browseRequests(int count) {
    return IntStream.range(0, count)
        .mapToObj(i -> mock(BrowseDescription.class, "browse-" + i))
        .toList();
  }

  private static List<ReferenceDescription> references(int count) {
    return IntStream.range(0, count)
        .mapToObj(i -> mock(ReferenceDescription.class, "reference-" + i))
        .toList();
  }

  private static List<BrowseResult> browseResults(
      List<BrowseDescription> partition,
      List<BrowseDescription> requests,
      List<ReferenceDescription> references) {

    return partition.stream()
        .map(
            request ->
                new BrowseResult(
                    StatusCode.GOOD,
                    ByteString.NULL_VALUE,
                    new ReferenceDescription[] {references.get(requests.indexOf(request))}))
        .toList();
  }

  private static BrowseNextResponse browseNextResponse(BrowseResult result) {
    return new BrowseNextResponse(null, new BrowseResult[] {result}, null);
  }

  private static <T> List<List<T>> singletonPartitions(List<T> values) {
    return values.stream().map(List::of).toList();
  }

  private static <T> List<List<T>> wrapEach(List<T> values) {
    return singletonPartitions(values);
  }
}
