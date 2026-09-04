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

  private static <T> List<List<T>> singletonPartitions(List<T> values) {
    return values.stream().map(List::of).toList();
  }

  private static <T> List<List<T>> wrapEach(List<T> values) {
    return singletonPartitions(values);
  }
}
