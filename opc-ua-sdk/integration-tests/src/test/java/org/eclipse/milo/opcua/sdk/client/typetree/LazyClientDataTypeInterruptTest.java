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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.LockSupport;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.OpcUaSession;
import org.eclipse.milo.opcua.sdk.client.OperationLimit;
import org.eclipse.milo.opcua.sdk.client.OperationLimits;
import org.eclipse.milo.opcua.sdk.core.types.codec.DynamicCodecFactory;
import org.eclipse.milo.opcua.sdk.core.types.codec.DynamicStructCodec;
import org.eclipse.milo.opcua.sdk.core.typetree.DataType;
import org.eclipse.milo.opcua.sdk.core.typetree.DataTypeTree;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.UaSerializationException;
import org.eclipse.milo.opcua.stack.core.encoding.DataTypeCodec;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.types.DataTypeEncoding;
import org.eclipse.milo.opcua.stack.core.types.DataTypeManager;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrowseDirection;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseResult;
import org.eclipse.milo.opcua.stack.core.types.structured.DataTypeDefinition;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId;
import org.eclipse.milo.opcua.stack.core.types.structured.ReferenceDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.ResponseHeader;
import org.eclipse.milo.opcua.stack.core.types.structured.StructureDefinition;
import org.eclipse.milo.opcua.stack.core.types.structured.StructureField;
import org.eclipse.milo.opcua.stack.core.util.Tree;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class LazyClientDataTypeInterruptTest {

  private static final String CUSTOM_NAMESPACE_URI = "urn:test:lazy-datatype-interrupt";

  private static final NodeId CUSTOM_STRUCT_ID = new NodeId(1, "SampleStruct");
  private static final NodeId CUSTOM_STRUCT_BINARY_ENCODING_ID =
      new NodeId(1, "SampleStruct.BinaryEncoding");
  private static final NodeId CUSTOM_STRUCT_XML_ENCODING_ID =
      new NodeId(1, "SampleStruct.XmlEncoding");
  private static final NodeId CUSTOM_STRUCT_JSON_ENCODING_ID =
      new NodeId(1, "SampleStruct.JsonEncoding");

  private static final NodeId SUB_STRUCT_ID = new NodeId(1, "SubSampleStruct");
  private static final NodeId SUB_STRUCT_BINARY_ENCODING_ID =
      new NodeId(1, "SubSampleStruct.BinaryEncoding");

  private static final NodeId OTHER_STRUCT_ID = new NodeId(1, "OtherStruct");
  private static final NodeId OTHER_STRUCT_BINARY_ENCODING_ID =
      new NodeId(1, "OtherStruct.BinaryEncoding");

  private static final NodeId MISSING_TYPE_ID = new NodeId(1, "NoSuchDataType");
  private static final NodeId MISSING_ENCODING_ID = new NodeId(1, "NoSuchEncoding");

  @Nested
  class LazyTree {

    // A caller that is already interrupted must not issue I/O or record a negative cache entry
    // that would break later lookups on healthy threads.
    @Test
    void preInterruptedLookupSkipsResolutionWithoutCachingFailure() throws Exception {
      var fixture = new MockClientFixture();
      var tree = new LazyClientDataTypeTree(fixture.client);

      WorkerOutcome<DataType> outcome =
          runOnPreInterruptedThread(() -> tree.getDataType(CUSTOM_STRUCT_ID));

      assertNull(outcome.value(), "pre-interrupted lookup must return null for unresolved type");
      assertTrue(outcome.interrupted(), "calling thread's interrupt status must be preserved");
      assertFalse(tree.isResolved(CUSTOM_STRUCT_ID));
      assertEquals(
          0,
          fixture.singleBrowseCalls.get()
              + fixture.batchBrowseCalls.get()
              + fixture.readCalls.get(),
          "pre-interrupted slow path should not issue server requests");

      DataType resolved = tree.getDataType(CUSTOM_STRUCT_ID);
      assertNotNull(resolved, "subsequent healthy lookup should resolve the type");
      assertEquals("SampleStruct", resolved.getBrowseName().name());
      assertEquals(CUSTOM_STRUCT_BINARY_ENCODING_ID, resolved.getBinaryEncodingId());
      assertTrue(tree.isResolved(CUSTOM_STRUCT_ID));
    }

    // Already-cached types should remain readable by an interrupted thread without clearing
    // its interrupt status or hitting the server.
    @Test
    void preInterruptedLookupStillReturnsAlreadyResolvedType() throws Exception {
      var fixture = new MockClientFixture();
      var tree = new LazyClientDataTypeTree(fixture.client);

      DataType expected = tree.getDataType(CUSTOM_STRUCT_ID);
      assertNotNull(expected);
      int callsBefore = fixture.totalClientCalls();

      WorkerOutcome<DataType> outcome =
          runOnPreInterruptedThread(() -> tree.getDataType(CUSTOM_STRUCT_ID));

      assertSame(expected, outcome.value());
      assertTrue(outcome.interrupted(), "calling thread's interrupt status must be preserved");
      assertEquals(callsBefore, fixture.totalClientCalls());
    }

    // browseInverseParent() swallows UaException internally; an interrupt during inverse subtype
    // browsing must still leave attemptedResolution clean so healthy threads can resolve later.
    @Test
    void interruptDuringInverseParentBrowseDoesNotPoisonCache() throws Exception {
      var fixture = new MockClientFixture();
      var gate = new InterruptGate();
      fixture.singleBrowseGate.set(
          bd ->
              bd.getNodeId().equals(CUSTOM_STRUCT_ID)
                  && bd.getReferenceTypeId().equals(NodeIds.HasSubtype));
      fixture.gate = gate;

      var tree = new LazyClientDataTypeTree(fixture.client);

      WorkerOutcome<DataType> outcome =
          runAndInterruptAtGate(gate, () -> tree.getDataType(CUSTOM_STRUCT_ID));

      assertNull(outcome.value());
      assertTrue(outcome.interrupted(), "interrupted thread's interrupt flag must stay set");
      assertFalse(tree.isResolved(CUSTOM_STRUCT_ID));

      DataType resolved = tree.getDataType(CUSTOM_STRUCT_ID);
      assertNotNull(resolved, "healthy lookup after interrupted inverse browse must succeed");
      assertEquals("SampleStruct", resolved.getBrowseName().name());
      assertTrue(tree.isResolved(CUSTOM_STRUCT_ID));
    }

    // Interrupting a later hop in a multi-hop inverse subtype chain must not poison the leaf type.
    @Test
    void interruptDuringSecondInverseBrowseHopDoesNotPoisonCache() throws Exception {
      var fixture = new MockClientFixture();
      var gate = new InterruptGate();
      fixture.singleBrowseGate.set(
          bd ->
              bd.getNodeId().equals(CUSTOM_STRUCT_ID)
                  && bd.getReferenceTypeId().equals(NodeIds.HasSubtype));
      fixture.gate = gate;

      var tree = new LazyClientDataTypeTree(fixture.client);

      WorkerOutcome<DataType> outcome =
          runAndInterruptAtGate(gate, () -> tree.getDataType(SUB_STRUCT_ID));

      assertNull(outcome.value());
      assertTrue(outcome.interrupted(), "interrupted thread's interrupt flag must stay set");
      assertFalse(tree.isResolved(SUB_STRUCT_ID));
      assertFalse(tree.isResolved(CUSTOM_STRUCT_ID));

      DataType resolved = tree.getDataType(SUB_STRUCT_ID);
      assertNotNull(resolved, "healthy lookup after mid-chain interrupt must succeed");
      assertEquals("SubSampleStruct", resolved.getBrowseName().name());
      assertTrue(tree.isResolved(SUB_STRUCT_ID));
      assertTrue(tree.isResolved(CUSTOM_STRUCT_ID));
      assertTrue(tree.isResolved(NodeIds.Structure));
    }

    // An interrupt during batch attribute reads (BrowseName, IsAbstract, DataTypeDefinition)
    // throws UaException out of resolvePath(); attemptedResolution must not retain the failed id.
    @Test
    void interruptDuringAttributeReadDoesNotPoisonCache() throws Exception {
      var fixture = new MockClientFixture();
      var gate = new InterruptGate();
      fixture.readGate.set(true);
      fixture.gate = gate;

      var tree = new LazyClientDataTypeTree(fixture.client);

      WorkerOutcome<DataType> outcome =
          runAndInterruptAtGate(gate, () -> tree.getDataType(CUSTOM_STRUCT_ID));

      assertNull(outcome.value());
      assertTrue(outcome.interrupted(), "interrupted thread's interrupt flag must stay set");
      assertFalse(tree.isResolved(CUSTOM_STRUCT_ID));

      DataType resolved = tree.getDataType(CUSTOM_STRUCT_ID);
      assertNotNull(resolved, "healthy lookup after interrupted attribute read must succeed");
      assertEquals("SampleStruct", resolved.getBrowseName().name());
      assertNotNull(resolved.getDataTypeDefinition());
    }

    // An interrupt during forward HasEncoding browsing inside fetchDataTypeInfoBatch() must not
    // poison attemptedResolution.
    @Test
    void interruptDuringEncodingBrowseDoesNotPoisonCache() throws Exception {
      var fixture = new MockClientFixture();
      var gate = new InterruptGate();
      fixture.batchBrowseGate.set(true);
      fixture.gate = gate;

      var tree = new LazyClientDataTypeTree(fixture.client);

      WorkerOutcome<DataType> outcome =
          runAndInterruptAtGate(gate, () -> tree.getDataType(CUSTOM_STRUCT_ID));

      assertNull(outcome.value());
      assertTrue(outcome.interrupted(), "interrupted thread's interrupt flag must stay set");
      assertFalse(tree.isResolved(CUSTOM_STRUCT_ID));

      DataType resolved = tree.getDataType(CUSTOM_STRUCT_ID);
      assertNotNull(resolved, "healthy lookup after interrupted encoding browse must succeed");
      assertEquals(CUSTOM_STRUCT_BINARY_ENCODING_ID, resolved.getBinaryEncodingId());
    }

    // A thread interrupted while queued behind another thread's active resolution must not start
    // I/O or poison attemptedResolution once the lock becomes available.
    @Test
    void interruptWhileBlockedOnConcurrentResolutionDoesNotPoisonCache() throws Exception {
      var fixture = new MockClientFixture();
      var holderEntered = new CountDownLatch(1);
      var releaseHolder = new CountDownLatch(1);
      fixture.singleBrowseHook =
          bd -> {
            if (bd.getNodeId().equals(OTHER_STRUCT_ID)
                && bd.getReferenceTypeId().equals(NodeIds.HasSubtype)) {
              holderEntered.countDown();
              try {
                if (!releaseHolder.await(5, TimeUnit.SECONDS)) {
                  throw new AssertionError("Timed out waiting to release lock holder");
                }
              } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new UaException(StatusCodes.Bad_UnexpectedError, e);
              }
            }
          };

      var tree = new LazyClientDataTypeTree(fixture.client);

      var holderResult = new CompletableFuture<DataType>();
      Thread holder =
          new Thread(
              () -> {
                try {
                  holderResult.complete(tree.getDataType(OTHER_STRUCT_ID));
                } catch (Throwable t) {
                  holderResult.completeExceptionally(t);
                }
              },
              "holder-resolution");
      holder.setDaemon(true);
      holder.start();

      assertTrue(holderEntered.await(5, TimeUnit.SECONDS));

      var waiterStarted = new CountDownLatch(1);
      var waiterOutcome = new CompletableFuture<WorkerOutcome<DataType>>();
      Thread waiter =
          new Thread(
              () -> {
                try {
                  waiterStarted.countDown();
                  DataType value = tree.getDataType(CUSTOM_STRUCT_ID);
                  waiterOutcome.complete(
                      new WorkerOutcome<>(value, Thread.currentThread().isInterrupted()));
                } catch (Throwable t) {
                  waiterOutcome.completeExceptionally(t);
                }
              },
              "waiter-interrupted");
      waiter.setDaemon(true);
      waiter.start();

      assertTrue(waiterStarted.await(5, TimeUnit.SECONDS));
      assertTrue(
          awaitWaiting(waiter, TimeUnit.SECONDS.toNanos(5)),
          "waiter should block behind holder's write lock");

      waiter.interrupt();
      releaseHolder.countDown();

      assertNotNull(holderResult.get(5, TimeUnit.SECONDS));
      WorkerOutcome<DataType> outcome = waiterOutcome.get(5, TimeUnit.SECONDS);

      assertNull(outcome.value());
      assertTrue(outcome.interrupted(), "waiter's interrupt status must remain set");
      assertFalse(tree.isResolved(CUSTOM_STRUCT_ID));
      assertEquals(
          0,
          fixture.inverseSubtypeCallsFor(CUSTOM_STRUCT_ID),
          "interrupted waiter must not start resolution for its target type");

      DataType resolved = tree.getDataType(CUSTOM_STRUCT_ID);
      assertNotNull(resolved, "healthy lookup after interrupted waiter must resolve normally");
      assertEquals("SampleStruct", resolved.getBrowseName().name());
    }

    // If decoding a DataTypeDefinition ExtensionObject fails due to interruption, the tree must
    // abort before publishing the type with a null definition (a positive cache entry that
    // clearFailedResolutions() could not repair) and must not negatively cache the type either.
    @Test
    void interruptDuringDataTypeDefinitionDecodeDoesNotCacheIncompleteType() throws Exception {
      var fixture = new MockClientFixture();
      fixture.encodeDefinitionsAsExtensionObjects = true;

      var decodeCalls = new AtomicInteger();
      EncodingContext interruptingContext =
          new DefaultEncodingContext() {
            @Override
            public DataTypeManager getDataTypeManager() {
              if (decodeCalls.incrementAndGet() == 1) {
                throw new UaSerializationException(
                    StatusCodes.Bad_UnexpectedError,
                    new InterruptedException("interrupted during DataTypeDefinition decode"));
              }
              return super.getDataTypeManager();
            }
          };
      when(fixture.client.getStaticEncodingContext()).thenReturn(interruptingContext);

      var tree = new LazyClientDataTypeTree(fixture.client);

      WorkerOutcome<DataType> outcome = runOnWorkerThread(() -> tree.getDataType(CUSTOM_STRUCT_ID));

      assertNull(outcome.value(), "interrupted definition decode must not return a partial type");
      assertFalse(
          outcome.interrupted(),
          "unwrapping an InterruptedException cause must not interrupt a healthy caller");
      assertFalse(
          tree.isResolved(CUSTOM_STRUCT_ID),
          "type must not be published into the tree without its DataTypeDefinition");
      assertFalse(
          tree.isResolved(NodeIds.Structure),
          "aborted batch must not publish partial ancestor state");

      DataType resolved = tree.getDataType(CUSTOM_STRUCT_ID);
      assertNotNull(resolved, "healthy lookup after interrupted definition decode must succeed");
      assertInstanceOf(
          StructureDefinition.class,
          resolved.getDataTypeDefinition(),
          "healthy retry must populate the decoded DataTypeDefinition");
      assertTrue(tree.isResolved(CUSTOM_STRUCT_ID));
    }

    // If checkSessionUnchanged() encounters a CompletionException wrapping InterruptedException
    // from getSessionAsync().getNow(null), it inspected an async future without blocking the
    // calling thread: it must leave attemptedResolution clean without interrupting the healthy
    // caller.
    @Test
    void interruptWrappedInSessionCheckDoesNotPoisonCacheOrInterruptHealthyCaller()
        throws Exception {
      var fixture = new MockClientFixture();
      var sessionAsyncCalls = new AtomicInteger();
      when(fixture.client.getSessionAsync())
          .thenAnswer(
              invocation -> {
                if (sessionAsyncCalls.incrementAndGet() == 1) {
                  return CompletableFuture.failedFuture(
                      new InterruptedException("session check interrupted"));
                }
                return CompletableFuture.completedFuture(fixture.session);
              });

      var tree = new LazyClientDataTypeTree(fixture.client);

      WorkerOutcome<DataType> outcome = runOnWorkerThread(() -> tree.getDataType(CUSTOM_STRUCT_ID));

      assertNull(outcome.value());
      assertFalse(
          outcome.interrupted(),
          "inspecting a session future failed with InterruptedException via getNow() must not"
              + " interrupt a healthy caller");
      assertFalse(tree.isResolved(CUSTOM_STRUCT_ID));

      DataType resolved = tree.getDataType(CUSTOM_STRUCT_ID);
      assertNotNull(resolved, "healthy lookup after interrupted session check must succeed");
      assertEquals("SampleStruct", resolved.getBrowseName().name());
    }

    // When a blocking call unwraps an already-failed async future whose cause is
    // InterruptedException (for example OpcUaClient.getSession() catching ExecutionException),
    // the calling thread itself was not interrupted: the tree must skip negative caching while
    // leaving the healthy caller's interrupt flag clear.
    @ParameterizedTest(name = "{0}")
    @MethodSource("clearedFlagInterruptTreePaths")
    void wrappedInterruptedExceptionCauseSkipsNegativeCacheWithoutInterruptingHealthyCaller(
        String path, Consumer<MockClientFixture> arm) throws Exception {

      var fixture = new MockClientFixture();
      arm.accept(fixture);

      var tree = new LazyClientDataTypeTree(fixture.client);

      WorkerOutcome<DataType> outcome = runOnWorkerThread(() -> tree.getDataType(CUSTOM_STRUCT_ID));

      assertNull(outcome.value());
      assertFalse(
          outcome.interrupted(),
          () ->
              path
                  + " must not interrupt a healthy caller when unwrapping an InterruptedException"
                  + " cause");
      assertFalse(tree.isResolved(CUSTOM_STRUCT_ID));

      DataType resolved = tree.getDataType(CUSTOM_STRUCT_ID);
      assertNotNull(resolved, () -> "healthy lookup after " + path + " must succeed");
      assertEquals("SampleStruct", resolved.getBrowseName().name());
    }

    static Stream<Arguments> clearedFlagInterruptTreePaths() {
      return Stream.of(
          Arguments.of(
              "initial getSession()",
              (Consumer<MockClientFixture>)
                  fixture -> {
                    var failedOnce = new AtomicInteger();
                    try {
                      when(fixture.client.getSession())
                          .thenAnswer(
                              invocation -> {
                                if (failedOnce.incrementAndGet() == 1) {
                                  // Mirrors OpcUaClient.getSession() catching ExecutionException
                                  // from an already-failed getSessionAsync() future.
                                  throw new UaException(
                                      new InterruptedException("producer session failed"));
                                }
                                return fixture.session;
                              });
                    } catch (UaException e) {
                      throw new AssertionError(e);
                    }
                  }),
          Arguments.of(
              "browseInverseParent",
              (Consumer<MockClientFixture>)
                  fixture -> {
                    var failedOnce = new AtomicInteger();
                    fixture.singleBrowseHook =
                        bd -> {
                          if (bd.getNodeId().equals(CUSTOM_STRUCT_ID)
                              && bd.getReferenceTypeId().equals(NodeIds.HasSubtype)
                              && failedOnce.incrementAndGet() == 1) {
                            throw new UaException(
                                StatusCodes.Bad_UnexpectedError,
                                new InterruptedException("browse interrupted with flag cleared"));
                          }
                        };
                  }),
          Arguments.of(
              "fetchDataTypeInfoBatch read",
              (Consumer<MockClientFixture>)
                  fixture -> {
                    var failedOnce = new AtomicInteger();
                    fixture.readHook =
                        readValueIds -> {
                          if (failedOnce.incrementAndGet() == 1) {
                            throw new UaException(
                                StatusCodes.Bad_UnexpectedError,
                                new InterruptedException("read interrupted with flag cleared"));
                          }
                        };
                  }));
    }

    // Walking a cyclic exception cause chain must terminate instead of spinning forever while
    // holding the write lock.
    @Test
    void cyclicExceptionCauseChainDoesNotHangResolution() throws Exception {
      var fixture = new MockClientFixture();
      Throwable cyclicCause = createCyclicCauseChain();
      fixture.singleBrowseHook =
          bd -> {
            if (bd.getNodeId().equals(CUSTOM_STRUCT_ID)) {
              throw new UaException(StatusCodes.Bad_UnexpectedError, cyclicCause);
            }
          };

      var tree = new LazyClientDataTypeTree(fixture.client);

      WorkerOutcome<DataType> outcome = runOnWorkerThread(() -> tree.getDataType(CUSTOM_STRUCT_ID));

      assertNull(outcome.value());
      assertFalse(outcome.interrupted(), "non-interrupt cyclic cause must not set interrupt flag");
      assertEquals(1, fixture.inverseSubtypeCallsFor(CUSTOM_STRUCT_ID));

      // Non-interrupt failure should still be negatively cached.
      assertNull(tree.getDataType(CUSTOM_STRUCT_ID));
      assertEquals(1, fixture.inverseSubtypeCallsFor(CUSTOM_STRUCT_ID));
    }

    // A reentrant lookup for the NodeId currently being resolved (for example from a
    // DataTypeDefinition decoder callback) must not recurse or append duplicate child nodes
    // to the tree snapshot.
    @Test
    void reentrantLookupForSameIdDuringDecodeDoesNotInsertDuplicateTreeNodes() throws Exception {
      var fixture = new MockClientFixture();
      fixture.encodeDefinitionsAsExtensionObjects = true;

      var tree = new LazyClientDataTypeTree(fixture.client);
      var reentered = new AtomicBoolean();
      var reentrantResult = new AtomicReference<@Nullable DataType>();

      EncodingContext reentrantContext =
          new DefaultEncodingContext() {
            @Override
            public DataTypeManager getDataTypeManager() {
              if (reentered.compareAndSet(false, true)) {
                reentrantResult.set(tree.getDataType(CUSTOM_STRUCT_ID));
              }
              return super.getDataTypeManager();
            }
          };
      when(fixture.client.getStaticEncodingContext()).thenReturn(reentrantContext);

      DataType resolved = tree.getDataType(CUSTOM_STRUCT_ID);

      assertTrue(reentered.get(), "reentrant decode callback should have executed");
      assertNull(
          reentrantResult.get(),
          "reentrant lookup for an in-progress type must not start a nested resolution");
      assertNotNull(resolved);
      assertInstanceOf(StructureDefinition.class, resolved.getDataTypeDefinition());
      assertEquals(
          1,
          fixture.inverseSubtypeCallsFor(CUSTOM_STRUCT_ID),
          "reentrant lookup must not trigger a second inverse browse");
      assertEquals(
          1,
          countOccurrencesInSnapshot(tree, NodeIds.Structure),
          "Structure must appear exactly once in the tree snapshot");
      assertEquals(
          1,
          countOccurrencesInSnapshot(tree, CUSTOM_STRUCT_ID),
          "SampleStruct must appear exactly once in the tree snapshot");
    }

    // A reentrant lookup for an ancestor NodeId while resolving a subtype must not cause the
    // outer resolution to append duplicate ancestor nodes when publishing the remaining path.
    @Test
    void reentrantLookupForAncestorDuringDecodeDoesNotInsertDuplicateTreeNodes() throws Exception {
      var fixture = new MockClientFixture();
      fixture.encodeDefinitionsAsExtensionObjects = true;

      var tree = new LazyClientDataTypeTree(fixture.client);
      var reentered = new AtomicBoolean();
      var ancestorFromCallback = new AtomicReference<@Nullable DataType>();

      EncodingContext reentrantContext =
          new DefaultEncodingContext() {
            @Override
            public DataTypeManager getDataTypeManager() {
              if (reentered.compareAndSet(false, true)) {
                ancestorFromCallback.set(tree.getDataType(CUSTOM_STRUCT_ID));
              }
              return super.getDataTypeManager();
            }
          };
      when(fixture.client.getStaticEncodingContext()).thenReturn(reentrantContext);

      DataType resolved = tree.getDataType(SUB_STRUCT_ID);

      assertTrue(reentered.get(), "reentrant decode callback should have executed");
      assertNotNull(ancestorFromCallback.get(), "reentrant ancestor lookup should succeed");
      assertNotNull(resolved);
      assertInstanceOf(StructureDefinition.class, resolved.getDataTypeDefinition());
      assertTrue(tree.isResolved(SUB_STRUCT_ID));
      assertTrue(tree.isResolved(CUSTOM_STRUCT_ID));
      assertEquals(
          1,
          countOccurrencesInSnapshot(tree, NodeIds.Structure),
          "Structure must appear exactly once in the tree snapshot");
      assertEquals(
          1,
          countOccurrencesInSnapshot(tree, CUSTOM_STRUCT_ID),
          "SampleStruct must appear exactly once in the tree snapshot");
      assertEquals(
          1,
          countOccurrencesInSnapshot(tree, SUB_STRUCT_ID),
          "SubSampleStruct must appear exactly once in the tree snapshot");
    }

    // A decoder callback that queries a dependency DataType which fails once with a wrapped
    // InterruptedException and then throws a fresh exception (without an InterruptedException
    // cause) must abort the batch rather than permanently caching the outer type with a null
    // definition.
    @Test
    void decoderDependencyWrappedInterruptThrowingFreshExceptionDoesNotCacheNullDefinition()
        throws Exception {
      var fixture = new MockClientFixture();
      fixture.encodeDefinitionsAsExtensionObjects = true;

      var depFailedOnce = new AtomicInteger();
      fixture.singleBrowseHook =
          bd -> {
            if (bd.getNodeId().equals(OTHER_STRUCT_ID)
                && bd.getReferenceTypeId().equals(NodeIds.HasSubtype)
                && depFailedOnce.incrementAndGet() == 1) {
              throw new UaException(
                  StatusCodes.Bad_UnexpectedError,
                  new InterruptedException("decoder dependency interrupted"));
            }
          };

      var tree = new LazyClientDataTypeTree(fixture.client);
      var checkedDependency = new AtomicBoolean();

      EncodingContext dependentContext =
          new DefaultEncodingContext() {
            @Override
            public DataTypeManager getDataTypeManager() {
              if (checkedDependency.compareAndSet(false, true)) {
                try {
                  DataType dependency = tree.getDataType(OTHER_STRUCT_ID);
                  if (dependency == null) {
                    throw new IllegalStateException("missing dependency DataType during decode");
                  }
                } finally {
                  checkedDependency.set(false);
                }
              }
              return super.getDataTypeManager();
            }
          };
      when(fixture.client.getStaticEncodingContext()).thenReturn(dependentContext);

      WorkerOutcome<DataType> outcome = runOnWorkerThread(() -> tree.getDataType(CUSTOM_STRUCT_ID));

      assertNull(outcome.value(), "interrupted dependency decode must abort outer resolution");
      assertFalse(
          outcome.interrupted(),
          "wrapped InterruptedException in decoder dependency must not interrupt healthy caller");
      assertFalse(
          tree.isResolved(CUSTOM_STRUCT_ID),
          "outer type must not be permanently cached with a null definition");
      assertFalse(tree.isResolved(OTHER_STRUCT_ID));

      DataType recovered = tree.getDataType(CUSTOM_STRUCT_ID);
      assertNotNull(
          recovered,
          "healthy retry must resolve outer type without calling clearFailedResolutions");
      assertInstanceOf(
          StructureDefinition.class,
          recovered.getDataTypeDefinition(),
          "healthy retry must publish the real DataTypeDefinition");
      assertTrue(tree.isResolved(CUSTOM_STRUCT_ID));
      assertTrue(tree.isResolved(OTHER_STRUCT_ID));
    }

    // A DataTypeDefinition decoder that calls manager.getCodec(B) where B is already loaded in the
    // tree and B's codecFactory fails once with a wrapped InterruptedException, then throws a fresh
    // exception on null, must propagate the manager abort into the tree's RetryScope so A is not
    // permanently cached with a null definition.
    @Test
    void decoderManagerLookupWrappedInterruptThrowingFreshExceptionDoesNotCacheNullDefinition()
        throws Exception {
      var fixture = new MockClientFixture();
      fixture.encodeDefinitionsAsExtensionObjects = true;

      var tree = new LazyClientDataTypeTree(fixture.client);
      // Preload B's metadata in the tree so manager.getCodec(B) does not touch the tree's slow
      // path and fails strictly inside B's codecFactory.
      assertNotNull(tree.getDataType(OTHER_STRUCT_ID));
      assertTrue(tree.isResolved(OTHER_STRUCT_ID));

      var depFactoryCalls = new AtomicInteger();
      var manager =
          new LazyClientDataTypeManager(
              fixture.client,
              fixture.namespaceTable,
              tree,
              (dataType, dataTypeTree) -> {
                if (OTHER_STRUCT_ID.equals(dataType.getNodeId())
                    && depFactoryCalls.incrementAndGet() == 1) {
                  throw new RuntimeException(
                      new InterruptedException("nested codecFactory B interrupted"));
                }
                return DynamicCodecFactory.create(dataType, dataTypeTree);
              });

      var checkedManager = new AtomicBoolean();
      EncodingContext dependentContext =
          new DefaultEncodingContext() {
            @Override
            public DataTypeManager getDataTypeManager() {
              if (checkedManager.compareAndSet(false, true)) {
                try {
                  DataTypeCodec depCodec = manager.getCodec(OTHER_STRUCT_ID);
                  if (depCodec == null) {
                    throw new IllegalStateException("missing dependency codec B during decode");
                  }
                } finally {
                  checkedManager.set(false);
                }
              }
              return super.getDataTypeManager();
            }
          };
      when(fixture.client.getStaticEncodingContext()).thenReturn(dependentContext);

      WorkerOutcome<DataType> outcome = runOnWorkerThread(() -> tree.getDataType(CUSTOM_STRUCT_ID));

      assertNull(outcome.value(), "interrupted manager codec lookup must abort outer resolution");
      assertFalse(
          outcome.interrupted(),
          "wrapped InterruptedException in manager codecFactory must not interrupt healthy caller");
      assertEquals(1, depFactoryCalls.get());
      assertFalse(
          tree.isResolved(CUSTOM_STRUCT_ID),
          "outer type A must not be permanently cached with a null definition");

      DataType recovered = tree.getDataType(CUSTOM_STRUCT_ID);
      assertNotNull(
          recovered, "healthy retry must resolve A without calling clearFailedResolutions()");
      assertInstanceOf(
          StructureDefinition.class,
          recovered.getDataTypeDefinition(),
          "healthy retry must publish A with its full DataTypeDefinition");
      assertTrue(tree.isResolved(CUSTOM_STRUCT_ID));
      assertEquals(2, depFactoryCalls.get());
      assertInstanceOf(DynamicStructCodec.class, manager.getCodec(OTHER_STRUCT_ID));
    }

    // Control assertion: genuine non-interrupt definition decode failures (no interrupt and no
    // retryable dependency miss) must preserve existing behavior and publish the type with a null
    // definition.
    @Test
    void genuineMalformedDefinitionDecodeFailureStillCachesTypeWithNullDefinition()
        throws Exception {
      var fixture = new MockClientFixture();
      fixture.encodeDefinitionsAsExtensionObjects = true;

      EncodingContext failingContext =
          new DefaultEncodingContext() {
            @Override
            public DataTypeManager getDataTypeManager() {
              throw new IllegalStateException("corrupt DataTypeDefinition payload");
            }
          };
      when(fixture.client.getStaticEncodingContext()).thenReturn(failingContext);

      var tree = new LazyClientDataTypeTree(fixture.client);

      DataType resolved = tree.getDataType(CUSTOM_STRUCT_ID);
      assertNotNull(
          resolved, "genuine malformed definition decode failure should still publish the type");
      assertNull(
          resolved.getDataTypeDefinition(),
          "genuine malformed definition decode failure should leave definition null");
      assertTrue(tree.isResolved(CUSTOM_STRUCT_ID));
    }

    // A reentrant tree.clearFailedResolutions() inside a DataTypeDefinition decoder followed by a
    // session-change failure must not re-insert the DataType into attemptedResolution and undo the
    // clear.
    @Test
    void reentrantClearFailedResolutionsDuringDecodeIsNotUndoneBySessionChangeFailure()
        throws Exception {
      var fixture = new MockClientFixture();
      fixture.encodeDefinitionsAsExtensionObjects = true;

      var tree = new LazyClientDataTypeTree(fixture.client);
      var clearedOnce = new AtomicBoolean(false);

      OpcUaSession newSession = mock(OpcUaSession.class);
      when(newSession.getSessionId()).thenReturn(new NodeId(0, 2));

      EncodingContext clearingContext =
          new DefaultEncodingContext() {
            @Override
            public DataTypeManager getDataTypeManager() {
              if (clearedOnce.compareAndSet(false, true)) {
                tree.clearFailedResolutions();
                // Advance the client's active session so checkSessionUnchanged() fails after
                // decoding completes.
                when(fixture.client.getSessionAsync())
                    .thenReturn(CompletableFuture.completedFuture(newSession));
              }
              return super.getDataTypeManager();
            }
          };
      when(fixture.client.getStaticEncodingContext()).thenReturn(clearingContext);

      assertNull(
          tree.getDataType(CUSTOM_STRUCT_ID),
          "session change after decode must abort without publishing CUSTOM_STRUCT_ID");
      assertTrue(clearedOnce.get(), "decoder callback should have called clearFailedResolutions()");
      assertFalse(tree.isResolved(CUSTOM_STRUCT_ID));

      // Align getSession() with the active newSession and retry WITHOUT another clear.
      when(fixture.client.getSession()).thenReturn(newSession);

      DataType recovered = tree.getDataType(CUSTOM_STRUCT_ID);
      assertNotNull(
          recovered, "next healthy lookup must resolve without a second clearFailedResolutions()");
      assertInstanceOf(StructureDefinition.class, recovered.getDataTypeDefinition());
      assertTrue(tree.isResolved(CUSTOM_STRUCT_ID));
    }

    // Control assertion: a genuinely missing NodeId on an un-interrupted thread must still be
    // negatively cached so repeated lookups do not hammer the server, until cleared.
    @Test
    void uninterruptedMissingTypeIsStillNegativelyCached() throws Exception {
      var fixture = new MockClientFixture();
      var tree = new LazyClientDataTypeTree(fixture.client);

      assertNull(tree.getDataType(MISSING_TYPE_ID));
      assertEquals(1, fixture.inverseSubtypeCallsFor(MISSING_TYPE_ID));

      assertNull(tree.getDataType(MISSING_TYPE_ID));
      assertEquals(
          1,
          fixture.inverseSubtypeCallsFor(MISSING_TYPE_ID),
          "second lookup of missing type should hit attemptedResolution without browsing again");

      tree.clearFailedResolutions();

      assertNull(tree.getDataType(MISSING_TYPE_ID));
      assertEquals(
          2,
          fixture.inverseSubtypeCallsFor(MISSING_TYPE_ID),
          "clearFailedResolutions() should allow retrying a previously failed lookup");
    }
  }

  @Nested
  class LazyManagerWithEagerTree {

    // Pre-interrupted lookups on LazyClientDataTypeManager must return null without performing
    // resolution or caching the failure in attemptedResolution.
    @ParameterizedTest(name = "{0}")
    @MethodSource("eagerLookupCases")
    void preInterruptedLookupSkipsResolutionWithoutCachingFailure(
        String label,
        Function<LazyClientDataTypeManager, @Nullable Object> lookup,
        @Nullable Object expectedNonNullValue)
        throws Exception {

      var fixture = new MockClientFixture();
      DataTypeTree eagerTree = fixture.createEagerTree();
      var codecFactoryCalls = new AtomicInteger();
      var manager =
          new LazyClientDataTypeManager(
              fixture.client,
              fixture.namespaceTable,
              eagerTree,
              (dataType, tree) -> {
                codecFactoryCalls.incrementAndGet();
                return DynamicCodecFactory.create(dataType, tree);
              });

      WorkerOutcome<Object> outcome = runOnPreInterruptedThread(() -> lookup.apply(manager));

      assertNull(outcome.value(), () -> label + " should return null when pre-interrupted");
      assertTrue(outcome.interrupted(), () -> label + " must preserve interrupt status");
      assertEquals(0, fixture.totalClientCalls(), () -> label + " should not browse on server");
      assertEquals(0, codecFactoryCalls.get(), () -> label + " should not construct codecs");

      Object resolved = lookup.apply(manager);
      assertNotNull(resolved, () -> label + " must succeed on a healthy thread afterward");
      if (expectedNonNullValue != null) {
        assertEquals(expectedNonNullValue, resolved);
      } else {
        assertInstanceOf(DynamicStructCodec.class, resolved);
      }
    }

    static Stream<Arguments> eagerLookupCases() {
      return Stream.of(
          Arguments.of(
              "getCodec(encodingId)",
              (Function<LazyClientDataTypeManager, @Nullable Object>)
                  m -> m.getCodec(CUSTOM_STRUCT_BINARY_ENCODING_ID),
              null),
          Arguments.of(
              "getCodec(dataTypeId)",
              (Function<LazyClientDataTypeManager, @Nullable Object>)
                  m -> m.getCodec(CUSTOM_STRUCT_ID),
              null),
          Arguments.of(
              "getBinaryEncodingId(dataTypeId)",
              (Function<LazyClientDataTypeManager, @Nullable Object>)
                  m -> m.getBinaryEncodingId(CUSTOM_STRUCT_ID),
              CUSTOM_STRUCT_BINARY_ENCODING_ID),
          Arguments.of(
              "getXmlEncodingId(dataTypeId)",
              (Function<LazyClientDataTypeManager, @Nullable Object>)
                  m -> m.getXmlEncodingId(CUSTOM_STRUCT_ID),
              CUSTOM_STRUCT_XML_ENCODING_ID),
          Arguments.of(
              "getJsonEncodingId(dataTypeId)",
              (Function<LazyClientDataTypeManager, @Nullable Object>)
                  m -> m.getJsonEncodingId(CUSTOM_STRUCT_ID),
              CUSTOM_STRUCT_JSON_ENCODING_ID));
    }

    // Even with an eager DataTypeTree, getCodec(encodingId) browses inverse HasEncoding; an
    // interrupt caught by browseDataTypeIdForEncoding() must not poison attemptedResolution.
    @Test
    void interruptDuringEncodingBrowseInGetCodecDoesNotPoisonCache() throws Exception {
      var fixture = new MockClientFixture();
      var gate = new InterruptGate();
      fixture.singleBrowseGate.set(
          bd ->
              bd.getNodeId().equals(CUSTOM_STRUCT_BINARY_ENCODING_ID)
                  && bd.getReferenceTypeId().equals(NodeIds.HasEncoding));
      fixture.gate = gate;

      DataTypeTree eagerTree = fixture.createEagerTree();
      var manager =
          new LazyClientDataTypeManager(fixture.client, fixture.namespaceTable, eagerTree);

      WorkerOutcome<DataTypeCodec> outcome =
          runAndInterruptAtGate(gate, () -> manager.getCodec(CUSTOM_STRUCT_BINARY_ENCODING_ID));

      assertNull(outcome.value());
      assertTrue(outcome.interrupted(), "interrupted thread's interrupt flag must stay set");

      DataTypeCodec codec = manager.getCodec(CUSTOM_STRUCT_BINARY_ENCODING_ID);
      assertNotNull(codec, "healthy lookup after interrupted encoding browse must succeed");
      assertInstanceOf(DynamicStructCodec.class, codec);
    }

    // When an async browse or codecFactory wraps an InterruptedException without interrupting the
    // calling thread itself, LazyClientDataTypeManager must leave attemptedResolution clean while
    // keeping the healthy caller's interrupt flag clear.
    @Test
    void wrappedInterruptedExceptionCauseSkipsNegativeCacheWithoutInterruptingHealthyCaller()
        throws Exception {

      var fixture = new MockClientFixture();
      var browseFailedOnce = new AtomicInteger();
      fixture.singleBrowseHook =
          bd -> {
            if (bd.getNodeId().equals(CUSTOM_STRUCT_BINARY_ENCODING_ID)
                && bd.getReferenceTypeId().equals(NodeIds.HasEncoding)
                && browseFailedOnce.incrementAndGet() == 1) {
              throw new UaException(
                  StatusCodes.Bad_UnexpectedError,
                  new InterruptedException("encoding browse interrupted with flag cleared"));
            }
          };

      var factoryFailedOnce = new AtomicInteger();
      DataTypeTree eagerTree = fixture.createEagerTree();
      var manager =
          new LazyClientDataTypeManager(
              fixture.client,
              fixture.namespaceTable,
              eagerTree,
              (dataType, tree) -> {
                if (dataType.getNodeId().equals(OTHER_STRUCT_ID)
                    && factoryFailedOnce.incrementAndGet() == 1) {
                  throw new RuntimeException(
                      "codec factory interrupted",
                      new InterruptedException("factory interrupted with flag cleared"));
                }
                return DynamicCodecFactory.create(dataType, tree);
              });

      WorkerOutcome<DataTypeCodec> encodingOutcome =
          runOnWorkerThread(() -> manager.getCodec(CUSTOM_STRUCT_BINARY_ENCODING_ID));
      assertNull(encodingOutcome.value());
      assertFalse(
          encodingOutcome.interrupted(),
          "browseDataTypeIdForEncoding() must not spuriously interrupt a healthy caller");
      assertInstanceOf(
          DynamicStructCodec.class,
          manager.getCodec(CUSTOM_STRUCT_BINARY_ENCODING_ID),
          "healthy retry after wrapped encoding browse interrupt must succeed");

      WorkerOutcome<DataTypeCodec> factoryOutcome =
          runOnWorkerThread(() -> manager.getCodec(OTHER_STRUCT_ID));
      assertNull(factoryOutcome.value());
      assertFalse(
          factoryOutcome.interrupted(),
          "resolveAndRegisterCodec() must not spuriously interrupt a healthy caller");
      assertInstanceOf(
          DynamicStructCodec.class,
          manager.getCodec(OTHER_STRUCT_ID),
          "healthy retry after wrapped codec factory interrupt must succeed");
    }

    // A cyclic exception cause chain from a failing codec factory must not hang resolution while
    // holding the manager's write lock.
    @Test
    void cyclicExceptionCauseChainDoesNotHangResolution() throws Exception {
      var fixture = new MockClientFixture();
      Throwable cyclicCause = createCyclicCauseChain();
      DataTypeTree eagerTree = fixture.createEagerTree();
      var manager =
          new LazyClientDataTypeManager(
              fixture.client,
              fixture.namespaceTable,
              eagerTree,
              (dataType, tree) -> {
                throw new RuntimeException("codec failure", cyclicCause);
              });

      WorkerOutcome<DataTypeCodec> outcome =
          runOnWorkerThread(() -> manager.getCodec(CUSTOM_STRUCT_ID));

      assertNull(outcome.value());
      assertFalse(outcome.interrupted(), "non-interrupt cyclic cause must not set interrupt flag");
    }

    // A custom codecFactory that reentrantly queries either the encoding id or the DataType id
    // for the same canonical DataType must not invoke codecFactory twice or record an in-progress
    // id in attemptedResolution, regardless of which id started the outer lookup.
    @ParameterizedTest(name = "outer lookup by {0}")
    @MethodSource("reentrantCodecOuterIds")
    void reentrantCodecFactoryLookupDoesNotRecurseOrPoisonCache(String label, NodeId outerLookupId)
        throws Exception {
      var fixture = new MockClientFixture();
      DataTypeTree eagerTree = fixture.createEagerTree();
      var factoryCalls = new AtomicInteger();
      var managerRef = new AtomicReference<LazyClientDataTypeManager>();
      var reentrantByBinaryEncoding = new AtomicReference<@Nullable DataTypeCodec>();
      var reentrantByXmlEncoding = new AtomicReference<@Nullable DataTypeCodec>();
      var reentrantByDataType = new AtomicReference<@Nullable DataTypeCodec>();
      var reentrantBinaryId = new AtomicReference<@Nullable NodeId>();

      var manager =
          new LazyClientDataTypeManager(
              fixture.client,
              fixture.namespaceTable,
              eagerTree,
              (dataType, tree) -> {
                if (factoryCalls.incrementAndGet() == 1) {
                  LazyClientDataTypeManager active = managerRef.get();
                  reentrantByBinaryEncoding.set(active.getCodec(CUSTOM_STRUCT_BINARY_ENCODING_ID));
                  reentrantByXmlEncoding.set(active.getCodec(CUSTOM_STRUCT_XML_ENCODING_ID));
                  reentrantByDataType.set(active.getCodec(CUSTOM_STRUCT_ID));
                  reentrantBinaryId.set(active.getBinaryEncodingId(CUSTOM_STRUCT_ID));
                }
                return DynamicCodecFactory.create(dataType, tree);
              });
      managerRef.set(manager);

      DataTypeCodec codec = manager.getCodec(outerLookupId);

      assertNotNull(codec);
      assertInstanceOf(DynamicStructCodec.class, codec);
      assertEquals(
          1,
          factoryCalls.get(),
          () -> "reentrant lookups during " + label + " must not re-invoke codecFactory");
      assertNull(reentrantByBinaryEncoding.get());
      assertNull(reentrantByXmlEncoding.get());
      assertNull(reentrantByDataType.get());
      assertNull(reentrantBinaryId.get());
      assertSame(codec, manager.getCodec(CUSTOM_STRUCT_BINARY_ENCODING_ID));
      assertSame(codec, manager.getCodec(CUSTOM_STRUCT_XML_ENCODING_ID));
      assertSame(codec, manager.getCodec(CUSTOM_STRUCT_ID));
      assertEquals(CUSTOM_STRUCT_BINARY_ENCODING_ID, manager.getBinaryEncodingId(CUSTOM_STRUCT_ID));
    }

    static Stream<Arguments> reentrantCodecOuterIds() {
      return Stream.of(
          Arguments.of("binaryEncodingId", CUSTOM_STRUCT_BINARY_ENCODING_ID),
          Arguments.of("dataTypeId", CUSTOM_STRUCT_ID));
    }

    // If a reentrant getCodec(binaryEncodingId) happens during getCodec(dataTypeId) and the outer
    // factory attempt then aborts due to interruption, the reentrant encoding lookup must not have
    // poisoned attemptedResolution for the encoding id.
    @Test
    void reentrantEncodingLookupDuringInterruptedFactoryDoesNotPoisonEncodingCache()
        throws Exception {
      var fixture = new MockClientFixture();
      DataTypeTree eagerTree = fixture.createEagerTree();
      var factoryCalls = new AtomicInteger();
      var managerRef = new AtomicReference<LazyClientDataTypeManager>();
      var reentrantByBinaryEncoding = new AtomicReference<@Nullable DataTypeCodec>();

      var manager =
          new LazyClientDataTypeManager(
              fixture.client,
              fixture.namespaceTable,
              eagerTree,
              (dataType, tree) -> {
                if (factoryCalls.incrementAndGet() == 1) {
                  reentrantByBinaryEncoding.set(
                      managerRef.get().getCodec(CUSTOM_STRUCT_BINARY_ENCODING_ID));
                  Thread.currentThread().interrupt();
                  throw new RuntimeException(
                      "interrupted during factory",
                      new InterruptedException("factory interrupted"));
                }
                return DynamicCodecFactory.create(dataType, tree);
              });
      managerRef.set(manager);

      WorkerOutcome<DataTypeCodec> outcome =
          runOnWorkerThread(() -> manager.getCodec(CUSTOM_STRUCT_ID));

      assertNull(outcome.value());
      assertTrue(outcome.interrupted());
      assertNull(reentrantByBinaryEncoding.get());
      assertEquals(1, factoryCalls.get(), "reentrant encoding lookup must not invoke factory");

      DataTypeCodec codec = manager.getCodec(CUSTOM_STRUCT_BINARY_ENCODING_ID);
      assertNotNull(
          codec, "reentrant encoding lookup must not have negatively cached binaryEncodingId");
      assertInstanceOf(DynamicStructCodec.class, codec);
      assertEquals(2, factoryCalls.get());
    }

    // The canonical inProgressCodecs guard must be claimed before calling
    // dataType.getDataTypeDefinition() and stay active until super.registerType() finishes, so a
    // custom DataType whose definition or encoding-ID getter reenters getCodec(dataTypeId) cannot
    // invoke codecFactory a second time and register a duplicate codec before the outer
    // registration.
    @ParameterizedTest(name = "reentrant getter: {0}")
    @MethodSource("reentrantDataTypeGetters")
    void reentrantDataTypeGetterDuringEncodingLookupDoesNotInvokeFactoryTwice(
        String getterName, boolean reenterInDefinitionGetter) throws Exception {
      var fixture = new MockClientFixture();
      DataType delegate = fixture.createEagerTree().getDataType(CUSTOM_STRUCT_ID);
      assertNotNull(delegate);

      var managerRef = new AtomicReference<LazyClientDataTypeManager>();
      var getterReentered = new AtomicBoolean();
      var reentrantByDataType = new AtomicReference<@Nullable DataTypeCodec>();
      var reentrantByXmlEncoding = new AtomicReference<@Nullable DataTypeCodec>();

      Runnable triggerReentry =
          () -> {
            if (getterReentered.compareAndSet(false, true)) {
              LazyClientDataTypeManager active = managerRef.get();
              reentrantByDataType.set(active.getCodec(CUSTOM_STRUCT_ID));
              reentrantByXmlEncoding.set(active.getCodec(CUSTOM_STRUCT_XML_ENCODING_ID));
            }
          };

      DataType reentrantDataType =
          new DataType() {
            @Override
            public QualifiedName getBrowseName() {
              return delegate.getBrowseName();
            }

            @Override
            public NodeId getNodeId() {
              return delegate.getNodeId();
            }

            @Override
            public @Nullable NodeId getBinaryEncodingId() {
              if (!reenterInDefinitionGetter) {
                triggerReentry.run();
              }
              return delegate.getBinaryEncodingId();
            }

            @Override
            public @Nullable NodeId getXmlEncodingId() {
              return delegate.getXmlEncodingId();
            }

            @Override
            public @Nullable NodeId getJsonEncodingId() {
              return delegate.getJsonEncodingId();
            }

            @Override
            public @Nullable DataTypeDefinition getDataTypeDefinition() {
              if (reenterInDefinitionGetter) {
                triggerReentry.run();
              }
              return delegate.getDataTypeDefinition();
            }

            @Override
            public Boolean isAbstract() {
              return delegate.isAbstract();
            }
          };

      Tree<DataType> root =
          new Tree<>(
              null,
              new ClientDataType(
                  new QualifiedName(0, "BaseDataType"),
                  NodeIds.BaseDataType,
                  null,
                  null,
                  null,
                  null,
                  true));
      Tree<DataType> structure =
          root.addChild(
              new ClientDataType(
                  new QualifiedName(0, "Structure"),
                  NodeIds.Structure,
                  null,
                  null,
                  null,
                  null,
                  true));
      structure.addChild(reentrantDataType);

      var factoryCalls = new AtomicInteger();
      var manager =
          new LazyClientDataTypeManager(
              fixture.client,
              fixture.namespaceTable,
              new DataTypeTree(root),
              (dataType, tree) -> {
                factoryCalls.incrementAndGet();
                return DynamicCodecFactory.create(dataType, tree);
              });
      managerRef.set(manager);

      DataTypeCodec codec = manager.getCodec(CUSTOM_STRUCT_BINARY_ENCODING_ID);

      assertTrue(getterReentered.get(), () -> "reentrant " + getterName + " should have executed");
      assertNotNull(codec);
      assertInstanceOf(DynamicStructCodec.class, codec);
      assertEquals(
          1,
          factoryCalls.get(),
          () ->
              "reentrant "
                  + getterName
                  + " during encoding lookup must not invoke codecFactory twice");
      assertNull(reentrantByDataType.get());
      assertNull(reentrantByXmlEncoding.get());
      assertSame(codec, manager.getCodec(CUSTOM_STRUCT_ID));
      assertSame(codec, manager.getCodec(CUSTOM_STRUCT_BINARY_ENCODING_ID));
      assertSame(codec, manager.getCodec(CUSTOM_STRUCT_XML_ENCODING_ID));
      assertEquals(CUSTOM_STRUCT_BINARY_ENCODING_ID, manager.getBinaryEncodingId(CUSTOM_STRUCT_ID));
    }

    static Stream<Arguments> reentrantDataTypeGetters() {
      return Stream.of(
          Arguments.of("getBinaryEncodingId()", false),
          Arguments.of("getDataTypeDefinition()", true));
    }

    // If a custom encoding-ID getter interrupts the current thread and returns null after
    // codecFactory has run, LazyClientDataTypeManager must abort before calling
    // super.registerType() so partial metadata (for example binary ID without XML ID) is not
    // permanently committed.
    @Test
    void interruptInEncodingIdGetterDoesNotRegisterPartialMetadata() throws Exception {
      var fixture = new MockClientFixture();
      DataType delegate = fixture.createEagerTree().getDataType(CUSTOM_STRUCT_ID);
      assertNotNull(delegate);

      var xmlGetterCalls = new AtomicInteger();
      DataType interruptingXmlDataType =
          new DataType() {
            @Override
            public QualifiedName getBrowseName() {
              return delegate.getBrowseName();
            }

            @Override
            public NodeId getNodeId() {
              return delegate.getNodeId();
            }

            @Override
            public @Nullable NodeId getBinaryEncodingId() {
              return delegate.getBinaryEncodingId();
            }

            @Override
            public @Nullable NodeId getXmlEncodingId() {
              if (xmlGetterCalls.incrementAndGet() == 1) {
                Thread.currentThread().interrupt();
                return null;
              }
              return delegate.getXmlEncodingId();
            }

            @Override
            public @Nullable NodeId getJsonEncodingId() {
              return delegate.getJsonEncodingId();
            }

            @Override
            public @Nullable DataTypeDefinition getDataTypeDefinition() {
              return delegate.getDataTypeDefinition();
            }

            @Override
            public Boolean isAbstract() {
              return delegate.isAbstract();
            }
          };

      Tree<DataType> root =
          new Tree<>(
              null,
              new ClientDataType(
                  new QualifiedName(0, "BaseDataType"),
                  NodeIds.BaseDataType,
                  null,
                  null,
                  null,
                  null,
                  true));
      Tree<DataType> structure =
          root.addChild(
              new ClientDataType(
                  new QualifiedName(0, "Structure"),
                  NodeIds.Structure,
                  null,
                  null,
                  null,
                  null,
                  true));
      structure.addChild(interruptingXmlDataType);

      var factoryCalls = new AtomicInteger();
      var manager =
          new LazyClientDataTypeManager(
              fixture.client,
              fixture.namespaceTable,
              new DataTypeTree(root),
              (dataType, tree) -> {
                factoryCalls.incrementAndGet();
                return DynamicCodecFactory.create(dataType, tree);
              });

      WorkerOutcome<NodeId> outcome =
          runOnWorkerThread(() -> manager.getXmlEncodingId(CUSTOM_STRUCT_ID));

      assertNull(outcome.value());
      assertTrue(outcome.interrupted(), "interrupt status set by getter must be preserved");
      assertEquals(1, factoryCalls.get());

      // A healthy retry must register all encoding IDs cleanly rather than seeing a partial
      // binary-only registration or a negative-cache entry.
      assertEquals(
          CUSTOM_STRUCT_XML_ENCODING_ID,
          manager.getXmlEncodingId(CUSTOM_STRUCT_ID),
          "healthy retry must return the XML encoding ID after an interrupted getter");
      assertEquals(
          2,
          factoryCalls.get(),
          "interrupted first attempt must not have committed a partial registration");
      assertEquals(CUSTOM_STRUCT_BINARY_ENCODING_ID, manager.getBinaryEncodingId(CUSTOM_STRUCT_ID));
      assertEquals(CUSTOM_STRUCT_JSON_ENCODING_ID, manager.getJsonEncodingId(CUSTOM_STRUCT_ID));
    }

    // Even with an eager DataTypeTree, if outer factory A calls Objects.requireNonNull(
    // manager.getCodec(B)) and B's factory fails once with a wrapped InterruptedException, the
    // resulting NullPointerException in A's factory must not negatively cache A.
    @Test
    void nestedManagerLookupWrappedInterruptDoesNotPoisonOuterNegativeCache() throws Exception {
      var fixture = new MockClientFixture();
      DataTypeTree eagerTree = fixture.createEagerTree();

      var outerFactoryCalls = new AtomicInteger();
      var innerFactoryCalls = new AtomicInteger();
      var managerRef = new AtomicReference<LazyClientDataTypeManager>();

      var manager =
          new LazyClientDataTypeManager(
              fixture.client,
              fixture.namespaceTable,
              eagerTree,
              (dataType, tree) -> {
                if (CUSTOM_STRUCT_ID.equals(dataType.getNodeId())) {
                  outerFactoryCalls.incrementAndGet();
                  Objects.requireNonNull(
                      managerRef.get().getCodec(OTHER_STRUCT_ID), "dependency codec B missing");
                  return DynamicCodecFactory.create(dataType, tree);
                }
                if (OTHER_STRUCT_ID.equals(dataType.getNodeId())) {
                  if (innerFactoryCalls.incrementAndGet() == 1) {
                    throw new RuntimeException(
                        new InterruptedException("nested factory B interrupted"));
                  }
                  return DynamicCodecFactory.create(dataType, tree);
                }
                return DynamicCodecFactory.create(dataType, tree);
              });
      managerRef.set(manager);

      WorkerOutcome<DataTypeCodec> outcome =
          runOnWorkerThread(() -> manager.getCodec(CUSTOM_STRUCT_ID));

      assertNull(outcome.value());
      assertFalse(
          outcome.interrupted(),
          "wrapped InterruptedException in nested factory must not interrupt healthy caller");
      assertEquals(1, outerFactoryCalls.get());
      assertEquals(1, innerFactoryCalls.get());

      DataTypeCodec recovered = manager.getCodec(CUSTOM_STRUCT_ID);
      assertNotNull(
          recovered, "healthy retry of A must succeed without calling clearFailedResolutions()");
      assertInstanceOf(DynamicStructCodec.class, recovered);
      assertEquals(2, outerFactoryCalls.get());
      assertEquals(2, innerFactoryCalls.get());
      assertInstanceOf(DynamicStructCodec.class, manager.getCodec(OTHER_STRUCT_ID));
    }

    // Even when outer factory A does not throw after a nested manager.getCodec(B) aborts due to a
    // wrapped InterruptedException, A must not publish partial codec state and must retry cleanly.
    @Test
    void nestedManagerLookupWrappedInterruptDoesNotPublishPartialOuterCodec() throws Exception {
      var fixture = new MockClientFixture();
      DataTypeTree eagerTree = fixture.createEagerTree();

      var outerFactoryCalls = new AtomicInteger();
      var innerFactoryCalls = new AtomicInteger();
      var managerRef = new AtomicReference<LazyClientDataTypeManager>();
      DataTypeCodec fallbackCodec = mock(DataTypeCodec.class);

      var manager =
          new LazyClientDataTypeManager(
              fixture.client,
              fixture.namespaceTable,
              eagerTree,
              (dataType, tree) -> {
                if (CUSTOM_STRUCT_ID.equals(dataType.getNodeId())) {
                  outerFactoryCalls.incrementAndGet();
                  DataTypeCodec depCodec = managerRef.get().getCodec(OTHER_STRUCT_ID);
                  return depCodec == null
                      ? fallbackCodec
                      : DynamicCodecFactory.create(dataType, tree);
                }
                if (OTHER_STRUCT_ID.equals(dataType.getNodeId())) {
                  if (innerFactoryCalls.incrementAndGet() == 1) {
                    throw new RuntimeException(
                        new InterruptedException("nested factory B interrupted"));
                  }
                  return DynamicCodecFactory.create(dataType, tree);
                }
                return DynamicCodecFactory.create(dataType, tree);
              });
      managerRef.set(manager);

      WorkerOutcome<DataTypeCodec> outcome =
          runOnWorkerThread(() -> manager.getCodec(CUSTOM_STRUCT_ID));

      assertNull(
          outcome.value(),
          "outer resolution must abort before publishing fallback codec after nested abort");
      assertFalse(outcome.interrupted());
      assertEquals(1, outerFactoryCalls.get());
      assertEquals(1, innerFactoryCalls.get());

      DataTypeCodec recovered = manager.getCodec(CUSTOM_STRUCT_ID);
      assertNotNull(recovered);
      assertInstanceOf(
          DynamicStructCodec.class,
          recovered,
          "healthy retry must build and register the full codec rather than a partial fallback");
      assertEquals(2, outerFactoryCalls.get());
      assertEquals(2, innerFactoryCalls.get());
    }

    // When outer factory A looks up B, and B's XML encoding-ID getter reentrantly calls
    // manager.getCodec(A) and returns a missing encoding ID (null or NodeId.NULL_VALUE) because A
    // is in progress, B must not be committed without its XML encoding ID; once A is registered,
    // looking up B must resolve and register B with its XML encoding ID intact.
    @ParameterizedTest(name = "missing XML encoding returned as {0}")
    @MethodSource("missingEncodingIds")
    void reentrantEncodingIdGetterReturningMissingIdDefersRegistrationInsteadOfOmittingMapping(
        String label, @Nullable NodeId missingValue) throws Exception {
      var fixture = new MockClientFixture();
      DataTypeTree baseTree = fixture.createEagerTree();
      DataType delegateB = baseTree.getDataType(CUSTOM_STRUCT_ID);
      assertNotNull(delegateB);

      var managerRef = new AtomicReference<LazyClientDataTypeManager>();
      var bFactoryCalls = new AtomicInteger();
      var bXmlGetterCalls = new AtomicInteger();
      var nestedBLookupDuringA = new AtomicReference<@Nullable DataTypeCodec>();

      DataTypeTree dependentTree =
          new DataTypeTree(baseTree.getRoot()) {
            @Override
            public @Nullable DataType getDataType(NodeId dataTypeId) {
              if (CUSTOM_STRUCT_ID.equals(dataTypeId)) {
                return new DataType() {
                  @Override
                  public QualifiedName getBrowseName() {
                    return delegateB.getBrowseName();
                  }

                  @Override
                  public NodeId getNodeId() {
                    return delegateB.getNodeId();
                  }

                  @Override
                  public @Nullable NodeId getBinaryEncodingId() {
                    return delegateB.getBinaryEncodingId();
                  }

                  @Override
                  public @Nullable NodeId getXmlEncodingId() {
                    bXmlGetterCalls.incrementAndGet();
                    DataTypeCodec codecA = managerRef.get().getCodec(OTHER_STRUCT_ID);
                    return codecA == null ? missingValue : delegateB.getXmlEncodingId();
                  }

                  @Override
                  public @Nullable NodeId getJsonEncodingId() {
                    return delegateB.getJsonEncodingId();
                  }

                  @Override
                  public @Nullable DataTypeDefinition getDataTypeDefinition() {
                    return delegateB.getDataTypeDefinition();
                  }

                  @Override
                  public Boolean isAbstract() {
                    return delegateB.isAbstract();
                  }
                };
              }
              return super.getDataType(dataTypeId);
            }
          };

      var manager =
          new LazyClientDataTypeManager(
              fixture.client,
              fixture.namespaceTable,
              dependentTree,
              (dataType, tree) -> {
                if (OTHER_STRUCT_ID.equals(dataType.getNodeId())) {
                  nestedBLookupDuringA.set(managerRef.get().getCodec(CUSTOM_STRUCT_ID));
                  return DynamicCodecFactory.create(dataType, tree);
                }
                if (CUSTOM_STRUCT_ID.equals(dataType.getNodeId())) {
                  bFactoryCalls.incrementAndGet();
                }
                return DynamicCodecFactory.create(dataType, tree);
              });
      managerRef.set(manager);

      DataTypeCodec codecA = manager.getCodec(OTHER_STRUCT_ID);
      assertNotNull(codecA, () -> "outer A should register cleanly when B defers (" + label + ")");
      assertInstanceOf(DynamicStructCodec.class, codecA);
      assertNull(
          nestedBLookupDuringA.get(),
          () -> "B must defer registration while A is in progress and XML getter returns " + label);
      assertEquals(1, bFactoryCalls.get());
      assertEquals(1, bXmlGetterCalls.get());

      // Now that A is registered, resolving B (via getXmlEncodingId and getCodec) must re-run B's
      // XML getter and register B with all encoding IDs intact.
      assertEquals(
          CUSTOM_STRUCT_XML_ENCODING_ID,
          manager.getXmlEncodingId(CUSTOM_STRUCT_ID),
          () -> "B must not have been committed without its XML encoding ID (" + label + ")");
      assertEquals(2, bFactoryCalls.get());
      assertEquals(2, bXmlGetterCalls.get());
      DataTypeCodec codecB = manager.getCodec(CUSTOM_STRUCT_ID);
      assertInstanceOf(DynamicStructCodec.class, codecB);
      assertSame(codecB, manager.getCodec(CUSTOM_STRUCT_XML_ENCODING_ID));
      assertEquals(CUSTOM_STRUCT_BINARY_ENCODING_ID, manager.getBinaryEncodingId(CUSTOM_STRUCT_ID));
      assertEquals(CUSTOM_STRUCT_JSON_ENCODING_ID, manager.getJsonEncodingId(CUSTOM_STRUCT_ID));
    }

    static Stream<Arguments> missingEncodingIds() {
      return Stream.of(
          Arguments.of("null", null), Arguments.of("NodeId.NULL_VALUE", NodeId.NULL_VALUE));
    }

    // When the tree already identified a DataType with a DataTypeDefinition and its codecFactory
    // fails with an ordinary exception or returns null, LazyClientDataTypeManager must finish the
    // lookup without issuing an unnecessary inverse HasEncoding browse to the server.
    @ParameterizedTest(name = "{0}")
    @MethodSource("ordinaryFactoryFailures")
    void ordinaryCodecFactoryFailureDoesNotTriggerInverseEncodingBrowse(
        String label, BiFunction<DataType, DataTypeTree, @Nullable DataTypeCodec> failingFactory)
        throws Exception {
      var fixture = new MockClientFixture();
      DataTypeTree eagerTree = fixture.createEagerTree();
      var factoryCalls = new AtomicInteger();

      var manager =
          new LazyClientDataTypeManager(
              fixture.client,
              fixture.namespaceTable,
              eagerTree,
              (dataType, tree) -> {
                factoryCalls.incrementAndGet();
                return failingFactory.apply(dataType, tree);
              });

      assertNull(
          manager.getCodec(CUSTOM_STRUCT_ID),
          () -> label + " should return null on ordinary factory failure");
      assertEquals(1, factoryCalls.get());
      assertEquals(
          0,
          fixture.inverseEncodingCallsFor(CUSTOM_STRUCT_ID),
          () -> label + " must not fall through to an inverse HasEncoding browse");
      assertEquals(0, fixture.totalClientCalls());

      // The ordinary failure should still be negatively cached.
      assertNull(manager.getCodec(CUSTOM_STRUCT_ID));
      assertEquals(1, factoryCalls.get());
      assertEquals(0, fixture.inverseEncodingCallsFor(CUSTOM_STRUCT_ID));
    }

    static Stream<Arguments> ordinaryFactoryFailures() {
      return Stream.of(
          Arguments.of(
              "throwing factory",
              (BiFunction<DataType, DataTypeTree, @Nullable DataTypeCodec>)
                  (dataType, tree) -> {
                    throw new IllegalStateException("ordinary factory failure");
                  }),
          Arguments.of(
              "null-returning factory",
              (BiFunction<DataType, DataTypeTree, @Nullable DataTypeCodec>)
                  (dataType, tree) -> null));
    }

    // Control assertion: genuinely missing encodings and data types on un-interrupted threads
    // must still be cached in attemptedResolution until clearFailedResolutions() is called.
    @Test
    void uninterruptedMissingEncodingAndDataTypeAreStillNegativelyCached() throws Exception {
      var fixture = new MockClientFixture();
      var treeLookups = new AtomicInteger();
      DataTypeTree countingTree =
          new DataTypeTree(fixture.createEagerTree().getRoot()) {
            @Override
            public @Nullable DataType getDataType(NodeId dataTypeId) {
              treeLookups.incrementAndGet();
              return super.getDataType(dataTypeId);
            }
          };
      var manager =
          new LazyClientDataTypeManager(fixture.client, fixture.namespaceTable, countingTree);

      assertNull(manager.getCodec(MISSING_ENCODING_ID));
      assertEquals(1, fixture.inverseEncodingCallsFor(MISSING_ENCODING_ID));

      assertNull(manager.getCodec(MISSING_ENCODING_ID));
      assertEquals(
          1,
          fixture.inverseEncodingCallsFor(MISSING_ENCODING_ID),
          "second getCodec() for missing encoding should hit attemptedResolution");

      treeLookups.set(0);
      assertNull(manager.getBinaryEncodingId(MISSING_TYPE_ID));
      assertEquals(1, treeLookups.get());

      assertNull(manager.getBinaryEncodingId(MISSING_TYPE_ID));
      assertEquals(
          1,
          treeLookups.get(),
          "second getBinaryEncodingId() for missing type should hit attemptedResolution");

      manager.clearFailedResolutions();

      assertNull(manager.getCodec(MISSING_ENCODING_ID));
      assertEquals(
          2,
          fixture.inverseEncodingCallsFor(MISSING_ENCODING_ID),
          "clearFailedResolutions() should allow retrying a previously failed encoding lookup");
    }
  }

  @Nested
  class LazyManagerWithLazyTree {

    // When LazyClientDataTypeManager is paired with LazyClientDataTypeTree, an interrupt during
    // getCodec(dataTypeId) must not poison either cache and must not fall through to an extra
    // inverse HasEncoding browse on the already-interrupted thread.
    @ParameterizedTest(name = "interrupt during {0}")
    @MethodSource("treeInterruptStages")
    void interruptDuringGetCodecByDataTypeIdDoesNotPoisonTreeOrManager(
        String stage, InterruptStage configurer) throws Exception {

      var fixture = new MockClientFixture();
      var gate = new InterruptGate();
      configurer.arm(fixture, gate);

      var tree = new LazyClientDataTypeTree(fixture.client);
      var manager = new LazyClientDataTypeManager(fixture.client, fixture.namespaceTable, tree);

      WorkerOutcome<DataTypeCodec> outcome =
          runAndInterruptAtGate(gate, () -> manager.getCodec(CUSTOM_STRUCT_ID));

      assertNull(outcome.value());
      assertTrue(outcome.interrupted(), "interrupted thread's interrupt flag must stay set");
      assertFalse(tree.isResolved(CUSTOM_STRUCT_ID));
      assertEquals(
          0,
          fixture.inverseEncodingCallsFor(CUSTOM_STRUCT_ID),
          () ->
              "manager must not send inverse HasEncoding browse after tree interrupt during "
                  + stage);

      DataTypeCodec codec = manager.getCodec(CUSTOM_STRUCT_ID);
      assertNotNull(codec, "healthy lookup must succeed without calling clearFailedResolutions()");
      assertInstanceOf(DynamicStructCodec.class, codec);
      assertTrue(tree.isResolved(CUSTOM_STRUCT_ID));
      assertEquals(CUSTOM_STRUCT_BINARY_ENCODING_ID, manager.getBinaryEncodingId(CUSTOM_STRUCT_ID));
    }

    static Stream<Arguments> treeInterruptStages() {
      return Stream.of(
          Arguments.of(
              "inverse HasSubtype browse",
              (InterruptStage)
                  (fixture, gate) -> {
                    fixture.singleBrowseGate.set(
                        bd ->
                            bd.getNodeId().equals(CUSTOM_STRUCT_ID)
                                && bd.getReferenceTypeId().equals(NodeIds.HasSubtype));
                    fixture.gate = gate;
                  }),
          Arguments.of(
              "attribute reads",
              (InterruptStage)
                  (fixture, gate) -> {
                    fixture.readGate.set(true);
                    fixture.gate = gate;
                  }));
    }

    // getCodec(encodingId) with a lazy tree has two network steps after confirming the id is not a
    // DataType: inverse HasEncoding browse and lazy tree resolution of the discovered DataType.
    // An interrupt in either step must leave both caches clean.
    @ParameterizedTest(name = "interrupt during {0}")
    @MethodSource("encodingCodecInterruptStages")
    void interruptDuringGetCodecByEncodingIdDoesNotPoisonTreeOrManager(
        String stage, InterruptStage configurer) throws Exception {

      var fixture = new MockClientFixture();
      var gate = new InterruptGate();
      configurer.arm(fixture, gate);

      var tree = new LazyClientDataTypeTree(fixture.client);
      var manager = new LazyClientDataTypeManager(fixture.client, fixture.namespaceTable, tree);

      WorkerOutcome<DataTypeCodec> outcome =
          runAndInterruptAtGate(gate, () -> manager.getCodec(CUSTOM_STRUCT_BINARY_ENCODING_ID));

      assertNull(outcome.value());
      assertTrue(outcome.interrupted(), "interrupted thread's interrupt flag must stay set");
      assertFalse(tree.isResolved(CUSTOM_STRUCT_ID));

      DataTypeCodec codec = manager.getCodec(CUSTOM_STRUCT_BINARY_ENCODING_ID);
      assertNotNull(
          codec, () -> "healthy lookup after interrupt during " + stage + " must succeed");
      assertInstanceOf(DynamicStructCodec.class, codec);
      assertTrue(tree.isResolved(CUSTOM_STRUCT_ID));
    }

    static Stream<Arguments> encodingCodecInterruptStages() {
      return Stream.of(
          Arguments.of(
              "inverse HasEncoding browse",
              (InterruptStage)
                  (fixture, gate) -> {
                    fixture.singleBrowseGate.set(
                        bd ->
                            bd.getNodeId().equals(CUSTOM_STRUCT_BINARY_ENCODING_ID)
                                && bd.getReferenceTypeId().equals(NodeIds.HasEncoding));
                    fixture.gate = gate;
                  }),
          Arguments.of(
              "lazy tree resolution of discovered DataType",
              (InterruptStage)
                  (fixture, gate) -> {
                    fixture.singleBrowseGate.set(
                        bd ->
                            bd.getNodeId().equals(CUSTOM_STRUCT_ID)
                                && bd.getReferenceTypeId().equals(NodeIds.HasSubtype));
                    fixture.gate = gate;
                  }));
    }

    // getBinaryEncodingId/getXmlEncodingId/getJsonEncodingId go through
    // ensureRegisteredForDataType(); an interrupt during lazy tree resolution must not poison
    // either cache.
    @ParameterizedTest(name = "{0}")
    @MethodSource("encodingIdGetters")
    void interruptDuringEncodingIdLookupDoesNotPoisonTreeOrManager(
        String methodName,
        Function<LazyClientDataTypeManager, @Nullable NodeId> getter,
        NodeId expectedEncodingId)
        throws Exception {

      var fixture = new MockClientFixture();
      var gate = new InterruptGate();
      fixture.singleBrowseGate.set(
          bd ->
              bd.getNodeId().equals(CUSTOM_STRUCT_ID)
                  && bd.getReferenceTypeId().equals(NodeIds.HasSubtype));
      fixture.gate = gate;

      var tree = new LazyClientDataTypeTree(fixture.client);
      var manager = new LazyClientDataTypeManager(fixture.client, fixture.namespaceTable, tree);

      WorkerOutcome<NodeId> outcome = runAndInterruptAtGate(gate, () -> getter.apply(manager));

      assertNull(outcome.value());
      assertTrue(outcome.interrupted(), () -> methodName + " must preserve interrupt status");
      assertFalse(tree.isResolved(CUSTOM_STRUCT_ID));

      NodeId resolvedEncodingId = getter.apply(manager);
      assertEquals(
          expectedEncodingId,
          resolvedEncodingId,
          () -> methodName + " must succeed on a healthy thread without clearFailedResolutions()");
    }

    // When LazyClientDataTypeTree aborts because either the initial getSession() or the final
    // checkSessionUnchanged() observes a session future failed with InterruptedException, the
    // healthy caller must not be interrupted and the paired LazyClientDataTypeManager must also
    // treat the miss as retryable so subsequent getCodec() and get*EncodingId() calls recover
    // without calling clearFailedResolutions() on either cache.
    @ParameterizedTest(name = "{0} with {1}")
    @MethodSource("pairedSessionInterruptCases")
    void sessionInterruptedFutureDoesNotPoisonPairedManagerOrInterruptHealthyCaller(
        String failurePoint,
        String lookupName,
        Consumer<MockClientFixture> armSessionFailure,
        Function<LazyClientDataTypeManager, @Nullable Object> lookup,
        @Nullable Object expectedNonNullValue)
        throws Exception {

      var fixture = new MockClientFixture();
      armSessionFailure.accept(fixture);

      var tree = new LazyClientDataTypeTree(fixture.client);
      var manager = new LazyClientDataTypeManager(fixture.client, fixture.namespaceTable, tree);

      WorkerOutcome<Object> outcome = runOnWorkerThread(() -> lookup.apply(manager));

      assertNull(outcome.value(), () -> lookupName + " should return null on failed session");
      assertFalse(
          outcome.interrupted(),
          () -> failurePoint + " must not interrupt a healthy caller during paired " + lookupName);
      assertFalse(tree.isResolved(CUSTOM_STRUCT_ID));

      Object recovered = lookup.apply(manager);
      assertNotNull(
          recovered,
          () ->
              lookupName
                  + " on the same LazyClientDataTypeManager must recover after "
                  + failurePoint
                  + " without clearing caches");
      if (expectedNonNullValue != null) {
        assertEquals(expectedNonNullValue, recovered);
      } else {
        assertInstanceOf(DynamicStructCodec.class, recovered);
      }
      assertTrue(tree.isResolved(CUSTOM_STRUCT_ID));
    }

    static Stream<Arguments> pairedSessionInterruptCases() {
      Consumer<MockClientFixture> failFinalSessionCheck =
          fixture -> {
            var calls = new AtomicInteger();
            when(fixture.client.getSessionAsync())
                .thenAnswer(
                    invocation -> {
                      if (calls.incrementAndGet() == 1) {
                        return CompletableFuture.failedFuture(
                            new InterruptedException("session future failed"));
                      }
                      return CompletableFuture.completedFuture(fixture.session);
                    });
          };

      Consumer<MockClientFixture> failInitialGetSession =
          fixture -> {
            var calls = new AtomicInteger();
            try {
              when(fixture.client.getSession())
                  .thenAnswer(
                      invocation -> {
                        if (calls.incrementAndGet() == 1) {
                          throw new UaException(
                              new InterruptedException("initial session future failed"));
                        }
                        return fixture.session;
                      });
            } catch (UaException e) {
              throw new AssertionError(e);
            }
          };

      List<Arguments> cases = new ArrayList<>();
      for (Object[] failure :
          new Object[][] {
            {"final checkSessionUnchanged()", failFinalSessionCheck},
            {"initial getSession()", failInitialGetSession}
          }) {
        String failurePoint = (String) failure[0];
        @SuppressWarnings("unchecked")
        Consumer<MockClientFixture> arm = (Consumer<MockClientFixture>) failure[1];

        cases.add(
            Arguments.of(
                failurePoint,
                "getCodec(dataTypeId)",
                arm,
                (Function<LazyClientDataTypeManager, @Nullable Object>)
                    m -> m.getCodec(CUSTOM_STRUCT_ID),
                null));
        cases.add(
            Arguments.of(
                failurePoint,
                "getBinaryEncodingId(dataTypeId)",
                arm,
                (Function<LazyClientDataTypeManager, @Nullable Object>)
                    m -> m.getBinaryEncodingId(CUSTOM_STRUCT_ID),
                CUSTOM_STRUCT_BINARY_ENCODING_ID));
        cases.add(
            Arguments.of(
                failurePoint,
                "getCodec(binaryEncodingId)",
                arm,
                (Function<LazyClientDataTypeManager, @Nullable Object>)
                    m -> m.getCodec(CUSTOM_STRUCT_BINARY_ENCODING_ID),
                null));
      }

      return cases.stream();
    }

    // LazyClientDataTypeManager's package-private retryability integration with
    // LazyClientDataTypeTree must still dispatch through virtual getDataType()/getType() so
    // subclass overrides are honored without browsing the server or negatively caching a miss.
    @ParameterizedTest(name = "subclass overrides {0}")
    @MethodSource("lazyTreeSubclassOverrideCases")
    void lazyTreeSubclassOverridesAreHonoredByManager(String overrideName, boolean overrideGetType)
        throws Exception {

      var fixture = new MockClientFixture();
      DataType customDataType = fixture.createEagerTree().getDataType(CUSTOM_STRUCT_ID);
      assertNotNull(customDataType);

      var overrideCalls = new AtomicInteger();
      LazyClientDataTypeTree subclassTree =
          overrideGetType
              ? new LazyClientDataTypeTree(fixture.client) {
                @Override
                public @Nullable DataType getType(NodeId nodeId) {
                  if (CUSTOM_STRUCT_ID.equals(nodeId)) {
                    overrideCalls.incrementAndGet();
                    return customDataType;
                  }
                  return super.getType(nodeId);
                }
              }
              : new LazyClientDataTypeTree(fixture.client) {
                @Override
                public @Nullable DataType getDataType(NodeId dataTypeId) {
                  if (CUSTOM_STRUCT_ID.equals(dataTypeId)) {
                    overrideCalls.incrementAndGet();
                    return customDataType;
                  }
                  return super.getDataType(dataTypeId);
                }
              };

      var managerForEncoding =
          new LazyClientDataTypeManager(fixture.client, fixture.namespaceTable, subclassTree);
      assertEquals(
          CUSTOM_STRUCT_BINARY_ENCODING_ID,
          managerForEncoding.getBinaryEncodingId(CUSTOM_STRUCT_ID),
          () -> "getBinaryEncodingId() must honor subclass override of " + overrideName);
      assertEquals(
          0,
          fixture.totalClientCalls(),
          () -> "getBinaryEncodingId() must not browse when " + overrideName + " supplies type");

      var managerForCodec =
          new LazyClientDataTypeManager(fixture.client, fixture.namespaceTable, subclassTree);
      DataTypeCodec codec = managerForCodec.getCodec(CUSTOM_STRUCT_ID);
      assertNotNull(codec, () -> "getCodec() must honor subclass override of " + overrideName);
      assertInstanceOf(DynamicStructCodec.class, codec);
      assertEquals(
          0,
          fixture.totalClientCalls(),
          () -> "getCodec() must not browse when " + overrideName + " supplies type");
      assertEquals(2, overrideCalls.get());
    }

    static Stream<Arguments> lazyTreeSubclassOverrideCases() {
      return Stream.of(
          Arguments.of("getType(NodeId)", true), Arguments.of("getDataType(NodeId)", false));
    }

    // A LazyClientDataTypeTree subclass that delegates getDataType(alias) to a different canonical
    // NodeId must still propagate retryable misses so LazyClientDataTypeManager does not
    // permanently negative-cache the alias id.
    @Test
    void subclassDelegatingLookupToDifferentCanonicalIdPropagatesRetryability() throws Exception {
      var fixture = new MockClientFixture();
      var failedOnce = new AtomicInteger();
      fixture.singleBrowseHook =
          bd -> {
            if (bd.getNodeId().equals(CUSTOM_STRUCT_ID)
                && bd.getReferenceTypeId().equals(NodeIds.HasSubtype)
                && failedOnce.incrementAndGet() == 1) {
              throw new UaException(
                  StatusCodes.Bad_UnexpectedError,
                  new InterruptedException("canonical lookup failed with interrupt cause"));
            }
          };

      NodeId aliasId = new NodeId(1, "SampleStructAlias");
      LazyClientDataTypeTree aliasTree =
          new LazyClientDataTypeTree(fixture.client) {
            @Override
            public @Nullable DataType getDataType(NodeId dataTypeId) {
              if (aliasId.equals(dataTypeId)) {
                return super.getDataType(CUSTOM_STRUCT_ID);
              }
              return super.getDataType(dataTypeId);
            }
          };

      var manager =
          new LazyClientDataTypeManager(fixture.client, fixture.namespaceTable, aliasTree);

      WorkerOutcome<DataTypeCodec> outcome = runOnWorkerThread(() -> manager.getCodec(aliasId));

      assertNull(outcome.value());
      assertFalse(
          outcome.interrupted(),
          "unwrapping an InterruptedException cause must not interrupt a healthy caller");
      assertFalse(aliasTree.isResolved(CUSTOM_STRUCT_ID));

      DataTypeCodec codec = manager.getCodec(aliasId);
      assertNotNull(
          codec,
          "subsequent lookup for alias must succeed without calling clearFailedResolutions()");
      assertInstanceOf(DynamicStructCodec.class, codec);
      assertTrue(aliasTree.isResolved(CUSTOM_STRUCT_ID));

      // Calling getBinaryEncodingId(alias) resolves and registers canonicalId rather than alias;
      // it must not record alias in attemptedResolution and break a subsequent getCodec(alias).
      var freshManager =
          new LazyClientDataTypeManager(fixture.client, fixture.namespaceTable, aliasTree);
      assertNull(
          freshManager.getBinaryEncodingId(aliasId),
          "binary encoding is registered under canonicalId rather than alias");
      DataTypeCodec codecAfterEncodingLookup = freshManager.getCodec(aliasId);
      assertNotNull(
          codecAfterEncodingLookup,
          "getBinaryEncodingId(alias) must not negatively cache a valid alias for getCodec(alias)");
      assertInstanceOf(DynamicStructCodec.class, codecAfterEncodingLookup);
    }

    // If a custom codecFactory resolves a dependency via LazyClientDataTypeTree and fails (for
    // example with NullPointerException from Objects.requireNonNull) because the dependency lookup
    // hit a wrapped InterruptedException, LazyClientDataTypeManager must recognize the retryable
    // tree miss and leave attemptedResolution clean.
    @Test
    void interruptedDependencyLookupInsideCodecFactoryDoesNotPoisonCache() throws Exception {
      var fixture = new MockClientFixture();
      var depFailedOnce = new AtomicInteger();
      fixture.singleBrowseHook =
          bd -> {
            if (bd.getNodeId().equals(OTHER_STRUCT_ID)
                && bd.getReferenceTypeId().equals(NodeIds.HasSubtype)
                && depFailedOnce.incrementAndGet() == 1) {
              throw new UaException(
                  StatusCodes.Bad_UnexpectedError,
                  new InterruptedException("dependency lookup interrupted"));
            }
          };

      var tree = new LazyClientDataTypeTree(fixture.client);
      var factoryCalls = new AtomicInteger();
      var manager =
          new LazyClientDataTypeManager(
              fixture.client,
              fixture.namespaceTable,
              tree,
              (dataType, dataTypeTree) -> {
                factoryCalls.incrementAndGet();
                Objects.requireNonNull(
                    dataTypeTree.getDataType(OTHER_STRUCT_ID), "dependency missing");
                return DynamicCodecFactory.create(dataType, dataTypeTree);
              });

      WorkerOutcome<DataTypeCodec> outcome =
          runOnWorkerThread(() -> manager.getCodec(CUSTOM_STRUCT_ID));

      assertNull(outcome.value());
      assertFalse(
          outcome.interrupted(),
          "unwrapping an InterruptedException cause in dependency lookup must not interrupt a"
              + " healthy caller");
      assertEquals(1, factoryCalls.get());
      assertFalse(tree.isResolved(OTHER_STRUCT_ID));

      DataTypeCodec recovered = manager.getCodec(CUSTOM_STRUCT_ID);
      assertNotNull(
          recovered,
          "healthy retry after interrupted dependency lookup in codecFactory must succeed without"
              + " clearing caches");
      assertInstanceOf(DynamicStructCodec.class, recovered);
      assertEquals(2, factoryCalls.get());
      assertTrue(tree.isResolved(OTHER_STRUCT_ID));
    }

    // If codecFactory calls acquireType(id, tempCodec, ...) and keeps the handle open, the outer
    // lazy registration must not overwrite tempCodec while the handle is active; once the handle
    // is closed, subsequent lookups must resolve cleanly without being negatively cached.
    @Test
    void codecFactoryCallingAcquireTypeIsNotOverwrittenWhileHandleIsActive() throws Exception {
      var fixture = new MockClientFixture();
      var tree = new LazyClientDataTypeTree(fixture.client);
      var managerRef = new AtomicReference<LazyClientDataTypeManager>();
      var handleRef = new AtomicReference<DataTypeManager.RegistrationHandle>();
      var factoryCalls = new AtomicInteger();
      DataTypeCodec tempCodec = mock(DataTypeCodec.class);

      var manager =
          new LazyClientDataTypeManager(
              fixture.client,
              fixture.namespaceTable,
              tree,
              (dataType, dataTypeTree) -> {
                if (factoryCalls.incrementAndGet() == 1) {
                  handleRef.set(
                      managerRef.get().acquireType(CUSTOM_STRUCT_ID, tempCodec, null, null, null));
                }
                return DynamicCodecFactory.create(dataType, dataTypeTree);
              });
      managerRef.set(manager);

      DataTypeCodec first = manager.getCodec(CUSTOM_STRUCT_ID);
      DataTypeManager.RegistrationHandle handle = handleRef.get();
      assertNotNull(handle);

      try {
        assertSame(
            tempCodec,
            first,
            "lazy registration must return the active acquireType() codec instead of replacing it");
        assertSame(
            tempCodec,
            manager.getCodec(CUSTOM_STRUCT_ID),
            "tempCodec must remain active while its RegistrationHandle is open");
      } finally {
        handle.close();
      }

      DataTypeCodec afterRelease = manager.getCodec(CUSTOM_STRUCT_ID);
      assertNotNull(afterRelease, "after closing handle, getCodec() must lazily register codec");
      assertInstanceOf(DynamicStructCodec.class, afterRelease);
      assertEquals(2, factoryCalls.get());
      assertEquals(CUSTOM_STRUCT_BINARY_ENCODING_ID, manager.getBinaryEncodingId(CUSTOM_STRUCT_ID));
    }

    // If a custom encoding-ID getter delegates to LazyClientDataTypeTree and returns null after a
    // wrapped InterruptedException on a healthy caller thread, RetryScope must still abort
    // registration before super.registerType() so partial metadata is not committed.
    @Test
    void wrappedInterruptInEncodingIdGetterDependencyDoesNotRegisterPartialMetadata()
        throws Exception {
      var fixture = new MockClientFixture();
      var depFailedOnce = new AtomicInteger();
      fixture.singleBrowseHook =
          bd -> {
            if (bd.getNodeId().equals(OTHER_STRUCT_ID)
                && bd.getReferenceTypeId().equals(NodeIds.HasSubtype)
                && depFailedOnce.incrementAndGet() == 1) {
              throw new UaException(
                  StatusCodes.Bad_UnexpectedError,
                  new InterruptedException("xml getter dependency interrupted"));
            }
          };

      DataType delegate = fixture.createEagerTree().getDataType(CUSTOM_STRUCT_ID);
      assertNotNull(delegate);

      LazyClientDataTypeTree treeWithDependentGetter =
          new LazyClientDataTypeTree(fixture.client) {
            @Override
            public @Nullable DataType getDataType(NodeId dataTypeId) {
              if (CUSTOM_STRUCT_ID.equals(dataTypeId)) {
                return new DataType() {
                  @Override
                  public QualifiedName getBrowseName() {
                    return delegate.getBrowseName();
                  }

                  @Override
                  public NodeId getNodeId() {
                    return delegate.getNodeId();
                  }

                  @Override
                  public @Nullable NodeId getBinaryEncodingId() {
                    return delegate.getBinaryEncodingId();
                  }

                  @Override
                  public @Nullable NodeId getXmlEncodingId() {
                    DataType dependency = getDataType(OTHER_STRUCT_ID);
                    return dependency == null ? null : delegate.getXmlEncodingId();
                  }

                  @Override
                  public @Nullable NodeId getJsonEncodingId() {
                    return delegate.getJsonEncodingId();
                  }

                  @Override
                  public @Nullable DataTypeDefinition getDataTypeDefinition() {
                    return delegate.getDataTypeDefinition();
                  }

                  @Override
                  public Boolean isAbstract() {
                    return delegate.isAbstract();
                  }
                };
              }
              return super.getDataType(dataTypeId);
            }
          };

      var factoryCalls = new AtomicInteger();
      var manager =
          new LazyClientDataTypeManager(
              fixture.client,
              fixture.namespaceTable,
              treeWithDependentGetter,
              (dataType, dataTypeTree) -> {
                factoryCalls.incrementAndGet();
                return DynamicCodecFactory.create(dataType, dataTypeTree);
              });

      WorkerOutcome<NodeId> outcome =
          runOnWorkerThread(() -> manager.getXmlEncodingId(CUSTOM_STRUCT_ID));

      assertNull(outcome.value());
      assertFalse(
          outcome.interrupted(),
          "wrapped InterruptedException in getter dependency must not interrupt healthy caller");
      assertEquals(1, factoryCalls.get());

      assertEquals(
          CUSTOM_STRUCT_XML_ENCODING_ID,
          manager.getXmlEncodingId(CUSTOM_STRUCT_ID),
          "healthy retry must return XML encoding ID without partial binary-only registration");
      assertEquals(
          2, factoryCalls.get(), "first attempt must have aborted before super.registerType()");
      assertEquals(CUSTOM_STRUCT_BINARY_ENCODING_ID, manager.getBinaryEncodingId(CUSTOM_STRUCT_ID));
      assertEquals(CUSTOM_STRUCT_JSON_ENCODING_ID, manager.getJsonEncodingId(CUSTOM_STRUCT_ID));
    }

    // If a LazyClientDataTypeTree subclass overrides getDataType(alias) with
    // Objects.requireNonNull(super.getDataType(canonicalId)), throwing NullPointerException after
    // an interrupted canonical miss must still propagate retryability so alias is not negatively
    // cached.
    @Test
    void throwingSubclassOverrideAfterInterruptedCanonicalLookupPropagatesRetryability()
        throws Exception {
      var fixture = new MockClientFixture();
      var failedOnce = new AtomicInteger();
      fixture.singleBrowseHook =
          bd -> {
            if (bd.getNodeId().equals(CUSTOM_STRUCT_ID)
                && bd.getReferenceTypeId().equals(NodeIds.HasSubtype)
                && failedOnce.incrementAndGet() == 1) {
              throw new UaException(
                  StatusCodes.Bad_UnexpectedError,
                  new InterruptedException("canonical lookup interrupted before NPE"));
            }
          };

      NodeId aliasId = new NodeId(1, "ThrowingSampleStructAlias");
      LazyClientDataTypeTree throwingAliasTree =
          new LazyClientDataTypeTree(fixture.client) {
            @Override
            public @Nullable DataType getDataType(NodeId dataTypeId) {
              if (aliasId.equals(dataTypeId)) {
                return Objects.requireNonNull(
                    super.getDataType(CUSTOM_STRUCT_ID), "canonical DataType missing");
              }
              return super.getDataType(dataTypeId);
            }
          };

      var manager =
          new LazyClientDataTypeManager(fixture.client, fixture.namespaceTable, throwingAliasTree);

      WorkerOutcome<DataTypeCodec> outcome = runOnWorkerThread(() -> manager.getCodec(aliasId));

      assertNull(outcome.value());
      assertFalse(
          outcome.interrupted(),
          "unwrapping an InterruptedException cause must not interrupt a healthy caller");
      assertFalse(throwingAliasTree.isResolved(CUSTOM_STRUCT_ID));

      DataTypeCodec recovered = manager.getCodec(aliasId);
      assertNotNull(
          recovered,
          "healthy retry after throwing subclass override must succeed without clearing caches");
      assertInstanceOf(DynamicStructCodec.class, recovered);
      assertTrue(throwingAliasTree.isResolved(CUSTOM_STRUCT_ID));
    }

    // Lazy registration must not overwrite an encoding-ID mapping owned by an active acquireType()
    // handle for another type, and must leave the colliding type retryable once the handle closes.
    @Test
    void lazyRegistrationDoesNotOverwriteActiveAcquireTypeEncodingId() throws Exception {
      var fixture = new MockClientFixture();
      var tree = new LazyClientDataTypeTree(fixture.client);
      var manager = new LazyClientDataTypeManager(fixture.client, fixture.namespaceTable, tree);

      DataTypeCodec tempCodec = mock(DataTypeCodec.class);
      try (DataTypeManager.RegistrationHandle ignored =
          manager.acquireType(
              OTHER_STRUCT_ID, tempCodec, CUSTOM_STRUCT_BINARY_ENCODING_ID, null, null)) {

        assertNull(
            manager.getCodec(CUSTOM_STRUCT_ID),
            "lazy registration for colliding type must defer while active handle owns encoding ID");
        assertSame(
            tempCodec,
            manager.getCodec(CUSTOM_STRUCT_BINARY_ENCODING_ID),
            "active handle's encoding-ID lookup must not be overwritten");
        assertSame(tempCodec, manager.getCodec(OTHER_STRUCT_ID));
      }

      DataTypeCodec resolvedByEncoding = manager.getCodec(CUSTOM_STRUCT_BINARY_ENCODING_ID);
      assertNotNull(
          resolvedByEncoding,
          "after closing active handle, encoding lookup must resolve SampleStruct cleanly");
      assertInstanceOf(DynamicStructCodec.class, resolvedByEncoding);
      assertSame(resolvedByEncoding, manager.getCodec(CUSTOM_STRUCT_ID));
      assertEquals(CUSTOM_STRUCT_BINARY_ENCODING_ID, manager.getBinaryEncodingId(CUSTOM_STRUCT_ID));
    }

    // When an active local handle registers canonical DataTypeId T as an encoding ID for another
    // type U, getCodec(E) for T's server encoding E must not return U's codec or negatively cache
    // E, and must resolve T's codec cleanly once U's handle is released.
    @Test
    void encodingLookupDoesNotReturnOtherTypesCodecWhenDataTypeIdIsUsedAsEncodingId()
        throws Exception {
      var fixture = new MockClientFixture();
      var tree = new LazyClientDataTypeTree(fixture.client);
      var manager = new LazyClientDataTypeManager(fixture.client, fixture.namespaceTable, tree);

      DataTypeCodec otherCodec = mock(DataTypeCodec.class);
      try (DataTypeManager.RegistrationHandle ignored =
          manager.acquireType(OTHER_STRUCT_ID, otherCodec, CUSTOM_STRUCT_ID, null, null)) {

        assertNull(
            manager.getCodec(CUSTOM_STRUCT_BINARY_ENCODING_ID),
            "getCodec(E) must not return U's codec when T is only registered as U's encoding ID");
        assertNull(
            manager.getBinaryEncodingId(CUSTOM_STRUCT_ID),
            "encoding-ID lookup for T must defer without negatively caching T during collision");
        assertSame(
            otherCodec,
            manager.getCodec(CUSTOM_STRUCT_ID),
            "active handle's encoding lookup for T must remain undisturbed");
        assertSame(otherCodec, manager.getCodec(OTHER_STRUCT_ID));
      }

      DataTypeCodec resolved = manager.getCodec(CUSTOM_STRUCT_BINARY_ENCODING_ID);
      assertNotNull(
          resolved,
          "after releasing U's handle, getCodec(E) must resolve T's codec without clearing caches");
      assertInstanceOf(DynamicStructCodec.class, resolved);
      assertSame(resolved, manager.getCodec(CUSTOM_STRUCT_ID));
      assertEquals(CUSTOM_STRUCT_BINARY_ENCODING_ID, manager.getBinaryEncodingId(CUSTOM_STRUCT_ID));
    }

    // Thread A holding LazyClientDataTypeTree's write lock during DataTypeDefinition decoding and
    // calling manager.getCodec(B) concurrently with Thread B holding LazyClientDataTypeManager's
    // write lock and querying the tree (even for preloaded metadata B) must never deadlock, must
    // not cache A with a null definition, and must allow A to resolve cleanly afterward.
    @Test
    void decoderManagerLookupAndConcurrentCodecLookupDoNotDeadlock() throws Exception {
      var fixture = new MockClientFixture();
      fixture.encodeDefinitionsAsExtensionObjects = true;

      var concurrentPhase = new AtomicBoolean(false);
      var threadAInDecode = new CountDownLatch(1);
      var threadBInManager = new CountDownLatch(1);

      LazyClientDataTypeTree tree =
          new LazyClientDataTypeTree(fixture.client) {
            @Override
            public @Nullable DataType getDataType(NodeId dataTypeId) {
              if (concurrentPhase.get()
                  && OTHER_STRUCT_ID.equals(dataTypeId)
                  && threadBInManager.getCount() > 0) {
                threadBInManager.countDown();
                try {
                  if (!threadAInDecode.await(5, TimeUnit.SECONDS)) {
                    throw new AssertionError("Timed out waiting for Thread A to enter decode");
                  }
                } catch (InterruptedException e) {
                  Thread.currentThread().interrupt();
                }
              }
              return super.getDataType(dataTypeId);
            }
          };

      // Preload B's metadata in the tree; even a tree cache hit acquires tree.lock.readLock().
      assertNotNull(tree.getDataType(OTHER_STRUCT_ID));
      assertTrue(tree.isResolved(OTHER_STRUCT_ID));

      var manager = new LazyClientDataTypeManager(fixture.client, fixture.namespaceTable, tree);

      var decoderActive = new AtomicBoolean(false);
      EncodingContext decodingContext =
          new DefaultEncodingContext() {
            @Override
            public DataTypeManager getDataTypeManager() {
              if (decoderActive.compareAndSet(false, true)) {
                try {
                  if (concurrentPhase.get()) {
                    threadAInDecode.countDown();
                    if (!threadBInManager.await(5, TimeUnit.SECONDS)) {
                      throw new AssertionError(
                          "Timed out waiting for Thread B to acquire manager write lock");
                    }
                  }
                  DataTypeCodec depCodec = manager.getCodec(OTHER_STRUCT_ID);
                  if (depCodec == null) {
                    throw new IllegalStateException("dependency codec B unavailable during decode");
                  }
                } catch (InterruptedException e) {
                  Thread.currentThread().interrupt();
                } finally {
                  decoderActive.set(false);
                }
              }
              return super.getDataTypeManager();
            }
          };
      when(fixture.client.getStaticEncodingContext()).thenReturn(decodingContext);

      concurrentPhase.set(true);

      var futureA = new CompletableFuture<@Nullable DataType>();
      Thread threadA =
          new Thread(
              () -> {
                try {
                  futureA.complete(tree.getDataType(CUSTOM_STRUCT_ID));
                } catch (Throwable t) {
                  futureA.completeExceptionally(t);
                }
              },
              "decoder-thread-A");
      threadA.setDaemon(true);

      var futureB = new CompletableFuture<@Nullable DataTypeCodec>();
      Thread threadB =
          new Thread(
              () -> {
                try {
                  futureB.complete(manager.getCodec(OTHER_STRUCT_ID));
                } catch (Throwable t) {
                  futureB.completeExceptionally(t);
                }
              },
              "manager-thread-B");
      threadB.setDaemon(true);

      threadA.start();
      threadB.start();

      DataType resolvedA = futureA.get(5, TimeUnit.SECONDS);
      DataTypeCodec codecB = futureB.get(5, TimeUnit.SECONDS);

      assertNotNull(codecB, "Thread B must complete and resolve B's codec without deadlocking");
      assertInstanceOf(DynamicStructCodec.class, codecB);
      assertNull(
          resolvedA,
          "Thread A must defer cleanly when manager write lock is contended while holding tree"
              + " write lock");
      assertFalse(
          tree.isResolved(CUSTOM_STRUCT_ID),
          "deferred decode must not permanently cache A with a null definition");

      concurrentPhase.set(false);

      DataType recoveredA = tree.getDataType(CUSTOM_STRUCT_ID);
      assertNotNull(
          recoveredA, "follow-up lookup of A must succeed without calling clearFailedResolutions");
      assertInstanceOf(StructureDefinition.class, recoveredA.getDataTypeDefinition());
      assertTrue(tree.isResolved(CUSTOM_STRUCT_ID));
    }

    // Thread A executing a tree reader (getTreeNode or isSubtypeOf) where a pre-seeded DataType's
    // getNodeId() calls manager.getCodec(...) concurrently with Thread B holding the manager write
    // lock and needing the tree write lock to resolve an uncached type must never deadlock.
    @ParameterizedTest(name = "tree reader: {0}")
    @MethodSource("treeReaderDeadlockModes")
    void treeReaderCallbackCallingManagerAndConcurrentManagerLookupDoNotDeadlock(
        String mode, Function<LazyClientDataTypeTree, @Nullable Object> readerAction)
        throws Exception {
      var fixture = new MockClientFixture();

      NodeId seededParentId = new NodeId(1, "SeededCallbackParent");
      NodeId seededChildId = new NodeId(1, "SeededCallbackChild");

      var concurrentPhase = new AtomicBoolean(false);
      var callbackTriggered = new AtomicBoolean(false);
      var threadAInGetNodeId = new CountDownLatch(1);
      var threadBInManager = new CountDownLatch(1);
      var managerRef = new AtomicReference<LazyClientDataTypeManager>();

      DataType seededParent =
          new DataType() {
            @Override
            public QualifiedName getBrowseName() {
              return new QualifiedName(1, "SeededCallbackParent");
            }

            @Override
            public NodeId getNodeId() {
              if (concurrentPhase.get() && callbackTriggered.compareAndSet(false, true)) {
                threadAInGetNodeId.countDown();
                try {
                  if (!threadBInManager.await(5, TimeUnit.SECONDS)) {
                    throw new AssertionError(
                        "Timed out waiting for Thread B to acquire manager write lock");
                  }
                } catch (InterruptedException e) {
                  Thread.currentThread().interrupt();
                }
                managerRef.get().getCodec(OTHER_STRUCT_ID);
              }
              return seededParentId;
            }

            @Override
            public @Nullable NodeId getBinaryEncodingId() {
              return null;
            }

            @Override
            public @Nullable NodeId getXmlEncodingId() {
              return null;
            }

            @Override
            public @Nullable NodeId getJsonEncodingId() {
              return null;
            }

            @Override
            public @Nullable DataTypeDefinition getDataTypeDefinition() {
              return null;
            }

            @Override
            public Boolean isAbstract() {
              return false;
            }
          };

      DataType seededChild =
          new ClientDataType(
              new QualifiedName(1, "SeededCallbackChild"),
              seededChildId,
              null,
              null,
              null,
              null,
              false);

      Tree<DataType> root =
          new Tree<>(
              null,
              new ClientDataType(
                  new QualifiedName(0, "BaseDataType"),
                  NodeIds.BaseDataType,
                  null,
                  null,
                  null,
                  null,
                  true));
      Tree<DataType> structure =
          root.addChild(
              new ClientDataType(
                  new QualifiedName(0, "Structure"),
                  NodeIds.Structure,
                  null,
                  null,
                  null,
                  null,
                  true));
      structure.addChild(seededParent).addChild(seededChild);

      LazyClientDataTypeTree tree =
          new LazyClientDataTypeTree(fixture.client, root) {
            @Override
            public @Nullable DataType getDataType(NodeId dataTypeId) {
              if (concurrentPhase.get()
                  && CUSTOM_STRUCT_ID.equals(dataTypeId)
                  && threadBInManager.getCount() > 0) {
                threadBInManager.countDown();
                try {
                  if (!threadAInGetNodeId.await(5, TimeUnit.SECONDS)) {
                    throw new AssertionError(
                        "Timed out waiting for Thread A to invoke DataType.getNodeId()");
                  }
                } catch (InterruptedException e) {
                  Thread.currentThread().interrupt();
                }
              }
              return super.getDataType(dataTypeId);
            }
          };

      var manager = new LazyClientDataTypeManager(fixture.client, fixture.namespaceTable, tree);
      managerRef.set(manager);

      concurrentPhase.set(true);

      var futureA = new CompletableFuture<@Nullable Object>();
      Thread threadA =
          new Thread(
              () -> {
                try {
                  futureA.complete(readerAction.apply(tree));
                } catch (Throwable t) {
                  futureA.completeExceptionally(t);
                }
              },
              "tree-reader-A-" + mode);
      threadA.setDaemon(true);

      var futureB = new CompletableFuture<@Nullable DataTypeCodec>();
      Thread threadB =
          new Thread(
              () -> {
                try {
                  futureB.complete(manager.getCodec(CUSTOM_STRUCT_ID));
                } catch (Throwable t) {
                  futureB.completeExceptionally(t);
                }
              },
              "manager-writer-B-" + mode);
      threadB.setDaemon(true);

      threadA.start();
      threadB.start();

      Object resultA = futureA.get(5, TimeUnit.SECONDS);
      DataTypeCodec codecB = futureB.get(5, TimeUnit.SECONDS);

      assertTrue(
          callbackTriggered.get(),
          () -> mode + " should have invoked pre-seeded DataType.getNodeId() callback");
      assertNotNull(resultA, () -> mode + " on Thread A must complete cleanly without deadlocking");
      if ("isSubtypeOf".equals(mode)) {
        assertEquals(Boolean.TRUE, resultA);
      }
      assertNotNull(
          codecB, () -> mode + ": Thread B must resolve uncached CUSTOM_STRUCT_ID cleanly");
      assertInstanceOf(DynamicStructCodec.class, codecB);

      concurrentPhase.set(false);

      // Any deferred lookup for OTHER_STRUCT_ID on Thread A must not have negatively cached it.
      DataTypeCodec otherCodec = manager.getCodec(OTHER_STRUCT_ID);
      assertNotNull(
          otherCodec,
          () -> mode + ": OTHER_STRUCT_ID must resolve cleanly after concurrent lookup");
      assertInstanceOf(DynamicStructCodec.class, otherCodec);
    }

    static Stream<Arguments> treeReaderDeadlockModes() {
      NodeId seededParentId = new NodeId(1, "SeededCallbackParent");
      NodeId seededChildId = new NodeId(1, "SeededCallbackChild");
      return Stream.of(
          Arguments.of(
              "getTreeNode",
              (Function<LazyClientDataTypeTree, @Nullable Object>)
                  tree -> tree.getTreeNode(seededParentId)),
          Arguments.of(
              "isSubtypeOf",
              (Function<LazyClientDataTypeTree, @Nullable Object>)
                  tree -> tree.isSubtypeOf(seededChildId, NodeIds.Structure)));
    }

    // If DataType A's codecFactory calls getBinaryEncodingId(B) while a temporary acquireType()
    // handle registers B without encoding IDs, A must not be negatively cached when A's factory
    // returns null or throws; closing B's handle must let A resolve cleanly on retry.
    @ParameterizedTest(name = "{0}")
    @MethodSource("dependentMissingEncodingOutcomes")
    void temporaryAcquireTypeWithoutEncodingsDoesNotPoisonDependentCodecLookup(
        String label,
        BiFunction<DataType, DataTypeTree, @Nullable DataTypeCodec> missingEncodingHandler)
        throws Exception {
      var fixture = new MockClientFixture();
      var tree = new LazyClientDataTypeTree(fixture.client);
      var managerRef = new AtomicReference<LazyClientDataTypeManager>();
      var outerFactoryCalls = new AtomicInteger();

      var manager =
          new LazyClientDataTypeManager(
              fixture.client,
              fixture.namespaceTable,
              tree,
              (dataType, dataTypeTree) -> {
                if (CUSTOM_STRUCT_ID.equals(dataType.getNodeId())) {
                  outerFactoryCalls.incrementAndGet();
                  NodeId depBinaryId = managerRef.get().getBinaryEncodingId(OTHER_STRUCT_ID);
                  if (depBinaryId == null) {
                    return missingEncodingHandler.apply(dataType, dataTypeTree);
                  }
                }
                return DynamicCodecFactory.create(dataType, dataTypeTree);
              });
      managerRef.set(manager);

      DataTypeCodec tempCodec = mock(DataTypeCodec.class);
      try (DataTypeManager.RegistrationHandle ignored =
          manager.acquireType(OTHER_STRUCT_ID, tempCodec, null, null, null)) {
        assertNull(
            manager.getCodec(CUSTOM_STRUCT_ID),
            () -> label + ": A should return null while B's temporary handle lacks encoding IDs");
        assertEquals(1, outerFactoryCalls.get());
      }

      DataTypeCodec resolved = manager.getCodec(CUSTOM_STRUCT_ID);
      assertNotNull(
          resolved,
          () ->
              label
                  + ": after closing B's temporary handle, A must resolve cleanly without calling"
                  + " clearFailedResolutions()");
      assertInstanceOf(DynamicStructCodec.class, resolved);
      assertEquals(2, outerFactoryCalls.get());
      assertEquals(OTHER_STRUCT_BINARY_ENCODING_ID, manager.getBinaryEncodingId(OTHER_STRUCT_ID));
      assertEquals(CUSTOM_STRUCT_BINARY_ENCODING_ID, manager.getBinaryEncodingId(CUSTOM_STRUCT_ID));
    }

    static Stream<Arguments> dependentMissingEncodingOutcomes() {
      return Stream.of(
          Arguments.of(
              "factory returns null",
              (BiFunction<DataType, DataTypeTree, @Nullable DataTypeCodec>)
                  (dataType, tree) -> null),
          Arguments.of(
              "factory throws NullPointerException",
              (BiFunction<DataType, DataTypeTree, @Nullable DataTypeCodec>)
                  (dataType, tree) -> {
                    throw new NullPointerException("dependency binary encoding ID missing");
                  }));
    }

    // Closing a temporary acquireType(B, codec, null, null, null) handle concurrently while
    // another thread resolves A (whose codecFactory calls getBinaryEncodingId(B)) must never
    // leave A negatively cached regardless of when handle.close() interleaves.
    @Test
    void concurrentCloseOfTemporaryRegistrationDoesNotPoisonDependentCodecLookup()
        throws Exception {
      var fixture = new MockClientFixture();
      DataTypeTree eagerTree = fixture.createEagerTree();
      DataTypeCodec tempCodec = mock(DataTypeCodec.class);

      // Deterministic slow-path interleaving: a temporary handle for B is acquired on another
      // thread while B is mid-resolution during A's codecFactory, then released after A's first
      // attempt sees null; A must not be negatively cached.
      {
        var acquireTemporary = new CountDownLatch(1);
        var handleAcquired = new CountDownLatch(1);
        var closeTemporary = new CountDownLatch(1);
        var managerRef = new AtomicReference<LazyClientDataTypeManager>();
        var outerFactoryCalls = new AtomicInteger();

        LazyClientDataTypeTree racingTree =
            new LazyClientDataTypeTree(fixture.client) {
              @Override
              public @Nullable DataType getDataType(NodeId dataTypeId) {
                DataType resolved = super.getDataType(dataTypeId);
                if (OTHER_STRUCT_ID.equals(dataTypeId) && acquireTemporary.getCount() > 0) {
                  acquireTemporary.countDown();
                  try {
                    if (!handleAcquired.await(5, TimeUnit.SECONDS)) {
                      throw new AssertionError("Timed out waiting for temporary handle acquire");
                    }
                  } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                  }
                }
                return resolved;
              }
            };

        var manager =
            new LazyClientDataTypeManager(
                fixture.client,
                fixture.namespaceTable,
                racingTree,
                (dataType, dataTypeTree) -> {
                  if (CUSTOM_STRUCT_ID.equals(dataType.getNodeId())) {
                    outerFactoryCalls.incrementAndGet();
                    NodeId depBinaryId = managerRef.get().getBinaryEncodingId(OTHER_STRUCT_ID);
                    if (depBinaryId == null) {
                      return null;
                    }
                  }
                  return DynamicCodecFactory.create(dataType, dataTypeTree);
                });
        managerRef.set(manager);

        var handleThreadDone = new CompletableFuture<Void>();
        Thread handleThread =
            new Thread(
                () -> {
                  try {
                    assertTrue(acquireTemporary.await(5, TimeUnit.SECONDS));
                    DataTypeManager.RegistrationHandle handle =
                        manager.acquireType(OTHER_STRUCT_ID, tempCodec, null, null, null);
                    handleAcquired.countDown();
                    assertTrue(closeTemporary.await(5, TimeUnit.SECONDS));
                    handle.close();
                    handleThreadDone.complete(null);
                  } catch (Throwable t) {
                    handleAcquired.countDown();
                    handleThreadDone.completeExceptionally(t);
                  }
                },
                "temp-handle-closer");
        handleThread.setDaemon(true);
        handleThread.start();

        assertNull(manager.getCodec(CUSTOM_STRUCT_ID));
        assertEquals(1, outerFactoryCalls.get());

        closeTemporary.countDown();
        handleThreadDone.get(5, TimeUnit.SECONDS);

        DataTypeCodec retry = manager.getCodec(CUSTOM_STRUCT_ID);
        assertNotNull(
            retry,
            "dependent A must not be negatively cached after slow-path temporary registration");
        assertInstanceOf(DynamicStructCodec.class, retry);
        assertEquals(2, outerFactoryCalls.get());
      }

      // Fast-path race: repeatedly close B's temporary handle concurrently with A's factory calling
      // getBinaryEncodingId(B) across a barrier so any interleaving between check and read is hit.
      for (int i = 0; i < 200; i++) {
        var managerRef = new AtomicReference<LazyClientDataTypeManager>();
        var manager =
            new LazyClientDataTypeManager(
                fixture.client,
                fixture.namespaceTable,
                eagerTree,
                (dataType, dataTypeTree) -> {
                  if (CUSTOM_STRUCT_ID.equals(dataType.getNodeId())) {
                    NodeId depBinaryId = managerRef.get().getBinaryEncodingId(OTHER_STRUCT_ID);
                    if (depBinaryId == null) {
                      return null;
                    }
                  }
                  return DynamicCodecFactory.create(dataType, dataTypeTree);
                });
        managerRef.set(manager);

        DataTypeManager.RegistrationHandle handle =
            manager.acquireType(OTHER_STRUCT_ID, tempCodec, null, null, null);
        var barrier = new CyclicBarrier(2);
        var closerDone = new CompletableFuture<Void>();
        Thread closer =
            new Thread(
                () -> {
                  try {
                    barrier.await(5, TimeUnit.SECONDS);
                    handle.close();
                    closerDone.complete(null);
                  } catch (Throwable t) {
                    closerDone.completeExceptionally(t);
                  }
                },
                "fast-path-closer-" + i);
        closer.setDaemon(true);
        closer.start();

        barrier.await(5, TimeUnit.SECONDS);
        DataTypeCodec first = manager.getCodec(CUSTOM_STRUCT_ID);
        closerDone.get(5, TimeUnit.SECONDS);

        if (first == null) {
          int iteration = i;
          DataTypeCodec retry = manager.getCodec(CUSTOM_STRUCT_ID);
          assertNotNull(
              retry,
              () ->
                  "iteration "
                      + iteration
                      + ": concurrent handle close must never negatively cache dependent A");
          assertInstanceOf(DynamicStructCodec.class, retry);
        } else {
          assertInstanceOf(DynamicStructCodec.class, first);
        }
      }
    }

    // Borrowing an already-installed permanent registerType() registration (without an XML
    // encoding ID) via acquireType() must NOT disable negative caching for dependent lookups.
    @Test
    void borrowingPermanentRegistrationWithoutXmlEncodingStillAllowsDependentNegativeCaching()
        throws Exception {
      var fixture = new MockClientFixture();
      DataTypeTree eagerTree = fixture.createEagerTree();
      var managerRef = new AtomicReference<LazyClientDataTypeManager>();
      var outerFactoryCalls = new AtomicInteger();

      var manager =
          new LazyClientDataTypeManager(
              fixture.client,
              fixture.namespaceTable,
              eagerTree,
              (dataType, dataTypeTree) -> {
                if (CUSTOM_STRUCT_ID.equals(dataType.getNodeId())) {
                  outerFactoryCalls.incrementAndGet();
                  NodeId depXmlId = managerRef.get().getXmlEncodingId(OTHER_STRUCT_ID);
                  if (depXmlId == null) {
                    return null;
                  }
                }
                return DynamicCodecFactory.create(dataType, dataTypeTree);
              });
      managerRef.set(manager);

      DataTypeCodec permanentCodecB = mock(DataTypeCodec.class);
      manager.registerType(
          OTHER_STRUCT_ID, permanentCodecB, OTHER_STRUCT_BINARY_ENCODING_ID, null, null);

      try (DataTypeManager.RegistrationHandle ignored =
          manager.acquireType(
              OTHER_STRUCT_ID, permanentCodecB, OTHER_STRUCT_BINARY_ENCODING_ID, null, null)) {

        assertNull(manager.getCodec(CUSTOM_STRUCT_ID));
        assertEquals(1, outerFactoryCalls.get());

        // Second lookup while borrowing the permanent registration must hit the negative cache.
        assertNull(manager.getCodec(CUSTOM_STRUCT_ID));
        assertEquals(
            1,
            outerFactoryCalls.get(),
            "borrowing a permanent registration via acquireType() must not disable negative"
                + " caching for dependent lookups");
      }

      // Closing the borrowing handle keeps the permanent registration and negative cache intact.
      assertSame(permanentCodecB, manager.getCodec(OTHER_STRUCT_ID));
      assertNull(manager.getCodec(CUSTOM_STRUCT_ID));
      assertEquals(1, outerFactoryCalls.get());
    }

    // Permanent registerType(T, appCodec, null, null, null) must still allow lazy discovery of
    // server-advertised encoding IDs from the type tree while retaining the application-owned
    // codec without invoking codecFactory.
    @Test
    void permanentCodecRegistrationWithoutEncodingsStillDiscoversEncodingIdsAndRetainsCodec()
        throws Exception {
      var fixture = new MockClientFixture();
      var tree = new LazyClientDataTypeTree(fixture.client);
      var factoryCalls = new AtomicInteger();

      var manager =
          new LazyClientDataTypeManager(
              fixture.client,
              fixture.namespaceTable,
              tree,
              (dataType, dataTypeTree) -> {
                factoryCalls.incrementAndGet();
                return DynamicCodecFactory.create(dataType, dataTypeTree);
              });

      DataTypeCodec appCodec = mock(DataTypeCodec.class);
      manager.registerType(CUSTOM_STRUCT_ID, appCodec, null, null, null);

      assertEquals(
          CUSTOM_STRUCT_BINARY_ENCODING_ID,
          manager.getBinaryEncodingId(CUSTOM_STRUCT_ID),
          "getBinaryEncodingId() must discover server-advertised encoding IDs for permanent"
              + " registration without encodings");
      assertEquals(CUSTOM_STRUCT_XML_ENCODING_ID, manager.getXmlEncodingId(CUSTOM_STRUCT_ID));
      assertEquals(CUSTOM_STRUCT_JSON_ENCODING_ID, manager.getJsonEncodingId(CUSTOM_STRUCT_ID));
      assertSame(
          appCodec,
          manager.getCodec(CUSTOM_STRUCT_ID),
          "application-owned codec must be retained after encoding discovery");
      assertSame(
          appCodec,
          manager.getCodec(CUSTOM_STRUCT_BINARY_ENCODING_ID),
          "discovered binary encoding ID must map to the retained application-owned codec");
      assertEquals(
          0,
          factoryCalls.get(),
          "codecFactory must not run when retaining an existing permanent application codec");

      // If a permanently registered type has no server metadata/encodings, the miss is negatively
      // cached for encoding lookups until clearFailedResolutions() while retaining the codec.
      DataTypeCodec missingTypeCodec = mock(DataTypeCodec.class);
      manager.registerType(MISSING_TYPE_ID, missingTypeCodec, null, null, null);

      assertNull(manager.getBinaryEncodingId(MISSING_TYPE_ID));
      assertEquals(1, fixture.inverseSubtypeCallsFor(MISSING_TYPE_ID));
      assertNull(manager.getBinaryEncodingId(MISSING_TYPE_ID));
      assertEquals(1, fixture.inverseSubtypeCallsFor(MISSING_TYPE_ID));
      assertSame(missingTypeCodec, manager.getCodec(MISSING_TYPE_ID));

      manager.clearFailedResolutions();
      assertNull(manager.getBinaryEncodingId(MISSING_TYPE_ID));
      assertEquals(
          1,
          fixture.inverseSubtypeCallsFor(MISSING_TYPE_ID),
          "tree negative cache still guards server browse until tree.clearFailedResolutions()");
      tree.clearFailedResolutions();
      manager.clearFailedResolutions();
      assertNull(manager.getBinaryEncodingId(MISSING_TYPE_ID));
      assertEquals(2, fixture.inverseSubtypeCallsFor(MISSING_TYPE_ID));
    }

    // A permanent registerType() encoding collision must be negatively cached so repeated
    // getCodec(T) lookups do not repeatedly construct codecs via codecFactory under the write
    // lock, while still honoring explicit clearFailedResolutions().
    @Test
    void permanentEncodingCollisionNegativelyCachesAndInvokesFactoryOnce() throws Exception {
      var fixture = new MockClientFixture();
      var tree = new LazyClientDataTypeTree(fixture.client);
      var factoryCalls = new AtomicInteger();

      var manager =
          new LazyClientDataTypeManager(
              fixture.client,
              fixture.namespaceTable,
              tree,
              (dataType, dataTypeTree) -> {
                factoryCalls.incrementAndGet();
                return DynamicCodecFactory.create(dataType, dataTypeTree);
              });

      DataTypeCodec permanentCodecU = mock(DataTypeCodec.class);
      manager.registerType(
          OTHER_STRUCT_ID, permanentCodecU, CUSTOM_STRUCT_BINARY_ENCODING_ID, null, null);

      assertNull(
          manager.getCodec(CUSTOM_STRUCT_ID),
          "lazy registration must not overwrite permanently registered encoding ID");
      assertEquals(1, factoryCalls.get());
      assertSame(permanentCodecU, manager.getCodec(CUSTOM_STRUCT_BINARY_ENCODING_ID));
      assertSame(permanentCodecU, manager.getCodec(OTHER_STRUCT_ID));

      // Second lookup must hit attemptedResolution instead of re-invoking codecFactory.
      assertNull(manager.getCodec(CUSTOM_STRUCT_ID));
      assertEquals(
          1,
          factoryCalls.get(),
          "permanent encoding collision must be negatively cached so factory runs only once");

      // Explicit clearFailedResolutions() still allows retrying.
      manager.clearFailedResolutions();
      assertNull(manager.getCodec(CUSTOM_STRUCT_ID));
      assertEquals(2, factoryCalls.get());
    }

    // If acquireType(U, tempCodec, E, null, null) is open and registerType(U, permCodec, null,
    // null, null) replaces only U's type entry, E remains owned by the still-open temporary
    // handle. A lookup for T needing E must treat the collision as temporary (retryable) rather
    // than negatively caching T, so getCodec(T) succeeds once the handle closes.
    @Test
    void temporaryEncodingCollisionRemainsRetryableAfterRegisterTypeReplacesOwnerType()
        throws Exception {
      var fixture = new MockClientFixture();
      var tree = new LazyClientDataTypeTree(fixture.client);
      var manager = new LazyClientDataTypeManager(fixture.client, fixture.namespaceTable, tree);

      DataTypeCodec tempCodecU = mock(DataTypeCodec.class);
      DataTypeCodec permCodecU = mock(DataTypeCodec.class);

      try (DataTypeManager.RegistrationHandle ignored =
          manager.acquireType(
              OTHER_STRUCT_ID, tempCodecU, CUSTOM_STRUCT_BINARY_ENCODING_ID, null, null)) {

        // Replace U's canonical type mapping permanently without replacing E.
        manager.registerType(OTHER_STRUCT_ID, permCodecU, null, null, null);
        assertSame(permCodecU, manager.getCodec(OTHER_STRUCT_ID));
        assertSame(tempCodecU, manager.getCodec(CUSTOM_STRUCT_BINARY_ENCODING_ID));

        assertNull(
            manager.getCodec(CUSTOM_STRUCT_ID),
            "T must defer while E is still owned by the open temporary handle");
        assertSame(tempCodecU, manager.getCodec(CUSTOM_STRUCT_BINARY_ENCODING_ID));
      }

      DataTypeCodec resolvedT = manager.getCodec(CUSTOM_STRUCT_ID);
      assertNotNull(
          resolvedT,
          "after closing the temporary handle that owned E, getCodec(T) must succeed without"
              + " calling clearFailedResolutions()");
      assertInstanceOf(DynamicStructCodec.class, resolvedT);
      assertSame(resolvedT, manager.getCodec(CUSTOM_STRUCT_BINARY_ENCODING_ID));
      assertEquals(CUSTOM_STRUCT_BINARY_ENCODING_ID, manager.getBinaryEncodingId(CUSTOM_STRUCT_ID));
      assertSame(permCodecU, manager.getCodec(OTHER_STRUCT_ID));
    }

    // After registerType(T, appCodec, null, xmlEncodingOrNull, null), calling getCodec(E) first
    // must complete missing encoding discovery while retaining appCodec and any existing XML
    // mapping so getDataTypeId(E) returns T and subsequent getCodec(E) calls hit the local cache
    // without re-browsing the server.
    @ParameterizedTest(name = "{0}")
    @MethodSource("permanentMissingBinaryEncodingModes")
    void encodingLookupFirstForPermanentCodecWithoutEncodingsDiscoversAndCachesEncodings(
        String modeName, @Nullable NodeId preRegisteredXmlEncodingId) throws Exception {
      var fixture = new MockClientFixture();
      var tree = new LazyClientDataTypeTree(fixture.client);
      var factoryCalls = new AtomicInteger();

      var manager =
          new LazyClientDataTypeManager(
              fixture.client,
              fixture.namespaceTable,
              tree,
              (dataType, dataTypeTree) -> {
                factoryCalls.incrementAndGet();
                return DynamicCodecFactory.create(dataType, dataTypeTree);
              });

      DataTypeCodec appCodec = mock(DataTypeCodec.class);
      manager.registerType(CUSTOM_STRUCT_ID, appCodec, null, preRegisteredXmlEncodingId, null);

      DataTypeCodec first = manager.getCodec(CUSTOM_STRUCT_BINARY_ENCODING_ID);
      assertSame(
          appCodec,
          first,
          () -> modeName + ": first getCodec(E) must return the retained application codec");
      assertEquals(1, fixture.inverseEncodingCallsFor(CUSTOM_STRUCT_BINARY_ENCODING_ID));
      assertEquals(0, factoryCalls.get(), "codecFactory must not run for retained permanent codec");
      assertEquals(
          CUSTOM_STRUCT_ID,
          manager.getDataTypeId(CUSTOM_STRUCT_BINARY_ENCODING_ID),
          () -> modeName + ": getDataTypeId(E) must return T after encoding discovery");
      assertEquals(CUSTOM_STRUCT_BINARY_ENCODING_ID, manager.getBinaryEncodingId(CUSTOM_STRUCT_ID));

      if (preRegisteredXmlEncodingId == null) {
        assertEquals(CUSTOM_STRUCT_XML_ENCODING_ID, manager.getXmlEncodingId(CUSTOM_STRUCT_ID));
        assertEquals(CUSTOM_STRUCT_JSON_ENCODING_ID, manager.getJsonEncodingId(CUSTOM_STRUCT_ID));
      } else {
        assertEquals(
            preRegisteredXmlEncodingId,
            manager.getXmlEncodingId(CUSTOM_STRUCT_ID),
            () -> modeName + ": existing XML encoding mapping must be preserved");
        assertEquals(
            CUSTOM_STRUCT_ID,
            manager.getDataTypeId(preRegisteredXmlEncodingId),
            () -> modeName + ": existing XML reverse identity must be preserved");
        assertSame(appCodec, manager.getCodec(preRegisteredXmlEncodingId));
      }

      // Second getCodec(E) must hit the installed encoding mapping without re-browsing the server.
      DataTypeCodec second = manager.getCodec(CUSTOM_STRUCT_BINARY_ENCODING_ID);
      assertSame(appCodec, second);
      assertEquals(
          1,
          fixture.inverseEncodingCallsFor(CUSTOM_STRUCT_BINARY_ENCODING_ID),
          () -> modeName + ": second getCodec(E) must not re-browse inverse HasEncoding");
      assertSame(appCodec, manager.getCodec(CUSTOM_STRUCT_ID));
    }

    static Stream<Arguments> permanentMissingBinaryEncodingModes() {
      return Stream.of(
          Arguments.of("zero encodings", null),
          Arguments.of("XML encoding only (server XML)", CUSTOM_STRUCT_XML_ENCODING_ID),
          Arguments.of(
              "XML encoding only (custom application XML)",
              new NodeId(1, "CustomAppXmlEncodingForStruct")));
    }

    // If an in-flight codecFactory reentrantly calls registerType(T, appCodec, null, null, null)
    // and returns null, the finishing lookup must not re-insert T into attemptedResolution and
    // undo registerType()'s invalidation; subsequent getBinaryEncodingId(T) must discover
    // encodings and retain appCodec.
    @Test
    void reentrantRegisterTypeInsideFailingCodecFactoryIsNotUndoneByNegativeCaching()
        throws Exception {
      var fixture = new MockClientFixture();
      var tree = new LazyClientDataTypeTree(fixture.client);
      var managerRef = new AtomicReference<LazyClientDataTypeManager>();
      var factoryCalls = new AtomicInteger();
      DataTypeCodec appCodec = mock(DataTypeCodec.class);

      var manager =
          new LazyClientDataTypeManager(
              fixture.client,
              fixture.namespaceTable,
              tree,
              (dataType, dataTypeTree) -> {
                if (CUSTOM_STRUCT_ID.equals(dataType.getNodeId())
                    && factoryCalls.incrementAndGet() == 1) {
                  managerRef.get().registerType(CUSTOM_STRUCT_ID, appCodec, null, null, null);
                  return null;
                }
                return DynamicCodecFactory.create(dataType, dataTypeTree);
              });
      managerRef.set(manager);

      assertNull(manager.getCodec(CUSTOM_STRUCT_ID));
      assertEquals(1, factoryCalls.get());
      assertSame(appCodec, manager.getCodec(CUSTOM_STRUCT_ID));

      assertEquals(
          CUSTOM_STRUCT_BINARY_ENCODING_ID,
          manager.getBinaryEncodingId(CUSTOM_STRUCT_ID),
          "in-flight failure must not re-insert T into attemptedResolution after registerType()");
      assertEquals(CUSTOM_STRUCT_XML_ENCODING_ID, manager.getXmlEncodingId(CUSTOM_STRUCT_ID));
      assertEquals(CUSTOM_STRUCT_JSON_ENCODING_ID, manager.getJsonEncodingId(CUSTOM_STRUCT_ID));
      assertSame(appCodec, manager.getCodec(CUSTOM_STRUCT_ID));
      assertSame(appCodec, manager.getCodec(CUSTOM_STRUCT_BINARY_ENCODING_ID));
      assertEquals(1, factoryCalls.get());
    }

    // When inverse HasEncoding resolves E -> T (directly or via an alias where tree.getDataType(T)
    // returns canonical C != T) and the DataType exposes no default binary, XML, or JSON encoding
    // IDs in the type tree, consecutive getCodec(E) calls must consistently return the canonical
    // codec (both for a lazily created codec and a pre-registered application codec under C)
    // without failing or negatively caching E.
    @ParameterizedTest(name = "{0}")
    @MethodSource("noDefaultEncodingModes")
    void consecutiveGetCodecByEncodingSucceedsWhenDataTypeHasNoDefaultEncodings(
        String label, boolean useAlias, boolean preRegisterAppCodec) throws Exception {
      var fixture = new MockClientFixture();
      DataType delegate = fixture.createEagerTree().getDataType(CUSTOM_STRUCT_ID);
      assertNotNull(delegate);
      StructureDefinition baseDef =
          assertInstanceOf(StructureDefinition.class, delegate.getDataTypeDefinition());
      StructureDefinition defWithoutDefaultEncoding =
          new StructureDefinition(
              NodeId.NULL_VALUE,
              baseDef.getBaseDataType(),
              baseDef.getStructureType(),
              baseDef.getFields());

      DataType noEncodingDataType =
          new ClientDataType(
              delegate.getBrowseName(),
              delegate.getNodeId(),
              null,
              null,
              null,
              defWithoutDefaultEncoding,
              delegate.isAbstract());

      NodeId lookupEncodingId =
          useAlias
              ? new NodeId(1, "AliasedNoDefaultEncoding.BinaryEncoding")
              : CUSTOM_STRUCT_BINARY_ENCODING_ID;
      NodeId discoveredDataTypeId =
          useAlias ? new NodeId(1, "AliasedNoDefaultEncodingDataType") : CUSTOM_STRUCT_ID;
      if (useAlias) {
        fixture.mapEncodingToDataType(lookupEncodingId, discoveredDataTypeId);
      }

      LazyClientDataTypeTree treeWithoutDefaultEncodings =
          new LazyClientDataTypeTree(fixture.client) {
            @Override
            public @Nullable DataType getDataType(NodeId dataTypeId) {
              if (discoveredDataTypeId.equals(dataTypeId)) {
                return noEncodingDataType;
              }
              return super.getDataType(dataTypeId);
            }
          };

      var factoryCalls = new AtomicInteger();
      var manager =
          new LazyClientDataTypeManager(
              fixture.client,
              fixture.namespaceTable,
              treeWithoutDefaultEncodings,
              (dataType, dataTypeTree) -> {
                factoryCalls.incrementAndGet();
                return DynamicCodecFactory.create(dataType, dataTypeTree);
              });

      DataTypeCodec appCodec = preRegisterAppCodec ? mock(DataTypeCodec.class) : null;
      if (appCodec != null) {
        manager.registerType(CUSTOM_STRUCT_ID, appCodec, null, null, null);
      }

      DataTypeCodec first = manager.getCodec(lookupEncodingId);
      DataTypeCodec second = manager.getCodec(lookupEncodingId);

      assertNotNull(first, () -> label + ": first getCodec(E) must return canonical codec");
      assertSame(
          first,
          second,
          () ->
              label + ": second getCodec(E) must return the same canonical codec rather than null");

      if (appCodec != null) {
        assertSame(appCodec, first);
        assertEquals(0, factoryCalls.get());
      } else {
        assertInstanceOf(DynamicStructCodec.class, first);
        assertEquals(1, factoryCalls.get());
      }
      assertSame(first, manager.getCodec(CUSTOM_STRUCT_ID));
    }

    static Stream<Arguments> noDefaultEncodingModes() {
      return Stream.of(
          Arguments.of("direct, lazily created codec", false, false),
          Arguments.of("direct, pre-registered application codec", false, true),
          Arguments.of("alias (T != C), lazily created codec", true, false),
          Arguments.of("alias (T != C), pre-registered application codec", true, true));
    }

    // While getCodec(C) or getCodec(E) (after inverse HasEncoding resolves E -> C) is inside
    // tree.getDataType(C) (so C is reserved in inProgressResolution before entering
    // inProgressCodecs), a reentrant encoding-ID getter call (getBinaryEncodingId/
    // getXmlEncodingId/getJsonEncodingId) on an alias T -> C must see that canonical C is already
    // in progress, defer without running codecFactory or registering C, and leave the outer lookup,
    // T, and C retryable when the outer lookup is interrupted.
    @ParameterizedTest(name = "{0} -> {2}")
    @MethodSource("reentrantAliasEncodingGetters")
    void reentrantEncodingIdGetterForAliasDuringInterruptedCanonicalLookupDoesNotPublishCodec(
        String outerName,
        NodeId outerLookupId,
        String methodName,
        BiFunction<LazyClientDataTypeManager, NodeId, @Nullable NodeId> encodingGetter)
        throws Exception {
      String caseLabel = outerName + " -> " + methodName;
      var fixture = new MockClientFixture();
      DataType canonicalDataType = fixture.createEagerTree().getDataType(CUSTOM_STRUCT_ID);
      assertNotNull(canonicalDataType);

      NodeId aliasId = new NodeId(1, "ReentrantAliasForCanonicalStruct");
      var managerRef = new AtomicReference<LazyClientDataTypeManager>();
      var nestedEncodingResult = new AtomicReference<@Nullable NodeId>();
      var reenteredOnce = new AtomicBoolean(false);
      var factoryCalls = new AtomicInteger();

      LazyClientDataTypeTree aliasTree =
          new LazyClientDataTypeTree(fixture.client) {
            @Override
            public @Nullable DataType getDataType(NodeId dataTypeId) {
              if (aliasId.equals(dataTypeId)) {
                return canonicalDataType;
              }
              if (CUSTOM_STRUCT_ID.equals(dataTypeId) && reenteredOnce.compareAndSet(false, true)) {
                nestedEncodingResult.set(encodingGetter.apply(managerRef.get(), aliasId));
                throw new RuntimeException(
                    new InterruptedException("outer canonical lookup interrupted"));
              }
              return super.getDataType(dataTypeId);
            }
          };

      var manager =
          new LazyClientDataTypeManager(
              fixture.client,
              fixture.namespaceTable,
              aliasTree,
              (dataType, dataTypeTree) -> {
                factoryCalls.incrementAndGet();
                return DynamicCodecFactory.create(dataType, dataTypeTree);
              });
      managerRef.set(manager);

      WorkerOutcome<DataTypeCodec> outcome =
          runOnWorkerThread(() -> manager.getCodec(outerLookupId));

      assertTrue(reenteredOnce.get(), () -> caseLabel + " reentrant callback must have executed");
      assertNull(outcome.value(), () -> caseLabel + ": interrupted outer lookup must return null");
      assertFalse(
          outcome.interrupted(),
          "wrapped InterruptedException must not interrupt healthy calling thread");
      assertNull(
          nestedEncodingResult.get(),
          () -> caseLabel + ": reentrant alias encoding lookup must defer while C is in progress");
      assertEquals(
          0,
          factoryCalls.get(),
          () ->
              caseLabel
                  + ": reentrant alias encoding lookup must not run codecFactory or publish C");

      // Healthy retry must now construct and register C cleanly, without negatively caching either
      // the outer lookup id or the alias T.
      DataTypeCodec recovered = manager.getCodec(outerLookupId);
      assertNotNull(recovered, () -> caseLabel + ": healthy retry must succeed");
      assertInstanceOf(DynamicStructCodec.class, recovered);
      assertEquals(1, factoryCalls.get());
      assertSame(recovered, manager.getCodec(aliasId));
      assertEquals(1, factoryCalls.get());
      assertEquals(CUSTOM_STRUCT_BINARY_ENCODING_ID, manager.getBinaryEncodingId(CUSTOM_STRUCT_ID));
    }

    static Stream<Arguments> reentrantAliasEncodingGetters() {
      List<Map.Entry<String, NodeId>> outerLookups =
          List.of(
              Map.entry("getCodec(C)", CUSTOM_STRUCT_ID),
              Map.entry("getCodec(E) inverse HasEncoding", CUSTOM_STRUCT_BINARY_ENCODING_ID));
      List<Map.Entry<String, BiFunction<LazyClientDataTypeManager, NodeId, @Nullable NodeId>>>
          getters =
              List.of(
                  Map.entry("getBinaryEncodingId", LazyClientDataTypeManager::getBinaryEncodingId),
                  Map.entry("getXmlEncodingId", LazyClientDataTypeManager::getXmlEncodingId),
                  Map.entry("getJsonEncodingId", LazyClientDataTypeManager::getJsonEncodingId));

      return outerLookups.stream()
          .flatMap(
              outer ->
                  getters.stream()
                      .map(
                          getter ->
                              Arguments.of(
                                  outer.getKey(),
                                  outer.getValue(),
                                  getter.getKey(),
                                  getter.getValue())));
    }

    // Calling getBinaryEncodingId(id) while a temporary acquireType(id, codec, null, null, null)
    // handle is active must not record a negative-cache entry in attemptedResolution that blocks
    // lazy resolution once the temporary handle is closed.
    @Test
    void temporaryAcquireTypeWithoutEncodingsDoesNotLeaveNegativeCacheEntryAfterClose()
        throws Exception {
      var fixture = new MockClientFixture();
      var tree = new LazyClientDataTypeTree(fixture.client);
      var manager = new LazyClientDataTypeManager(fixture.client, fixture.namespaceTable, tree);

      DataTypeCodec tempCodec = mock(DataTypeCodec.class);
      try (DataTypeManager.RegistrationHandle ignored =
          manager.acquireType(CUSTOM_STRUCT_ID, tempCodec, null, null, null)) {
        assertNull(
            manager.getBinaryEncodingId(CUSTOM_STRUCT_ID),
            "temporary registration without encodings should report null binary encoding ID");
      }

      DataTypeCodec resolved = manager.getCodec(CUSTOM_STRUCT_ID);
      assertNotNull(
          resolved, "after closing temporary handle, getCodec() must resolve cleanly from server");
      assertInstanceOf(DynamicStructCodec.class, resolved);
      assertEquals(CUSTOM_STRUCT_BINARY_ENCODING_ID, manager.getBinaryEncodingId(CUSTOM_STRUCT_ID));
    }

    // Even when a temporary acquireType(id, codec, null, null, null) registration appears
    // mid-lookup (hitting createAndRegisterCodec()'s existing-codec shortcut), releasing the
    // handle must still allow lazy resolution afterward.
    @Test
    void reentrantAcquireTypeWithoutEncodingsDoesNotLeaveNegativeCacheEntryAfterClose()
        throws Exception {
      var fixture = new MockClientFixture();
      var managerRef = new AtomicReference<LazyClientDataTypeManager>();
      var handleRef = new AtomicReference<DataTypeManager.RegistrationHandle>();
      DataTypeCodec tempCodec = mock(DataTypeCodec.class);

      LazyClientDataTypeTree acquiringTree =
          new LazyClientDataTypeTree(fixture.client) {
            @Override
            public @Nullable DataType getDataType(NodeId dataTypeId) {
              DataType resolved = super.getDataType(dataTypeId);
              if (CUSTOM_STRUCT_ID.equals(dataTypeId)
                  && resolved != null
                  && handleRef.get() == null) {
                handleRef.set(
                    managerRef.get().acquireType(CUSTOM_STRUCT_ID, tempCodec, null, null, null));
              }
              return resolved;
            }
          };

      var manager =
          new LazyClientDataTypeManager(fixture.client, fixture.namespaceTable, acquiringTree);
      managerRef.set(manager);

      assertNull(manager.getBinaryEncodingId(CUSTOM_STRUCT_ID));
      DataTypeManager.RegistrationHandle handle = handleRef.get();
      assertNotNull(handle);
      handle.close();

      DataTypeCodec resolved = manager.getCodec(CUSTOM_STRUCT_ID);
      assertNotNull(
          resolved,
          "after closing reentrantly acquired temporary handle, getCodec() must resolve cleanly");
      assertInstanceOf(DynamicStructCodec.class, resolved);
      assertEquals(CUSTOM_STRUCT_BINARY_ENCODING_ID, manager.getBinaryEncodingId(CUSTOM_STRUCT_ID));
    }

    static Stream<Arguments> encodingIdGetters() {
      return Stream.of(
          Arguments.of(
              "getBinaryEncodingId",
              (Function<LazyClientDataTypeManager, @Nullable NodeId>)
                  m -> m.getBinaryEncodingId(CUSTOM_STRUCT_ID),
              CUSTOM_STRUCT_BINARY_ENCODING_ID),
          Arguments.of(
              "getXmlEncodingId",
              (Function<LazyClientDataTypeManager, @Nullable NodeId>)
                  m -> m.getXmlEncodingId(CUSTOM_STRUCT_ID),
              CUSTOM_STRUCT_XML_ENCODING_ID),
          Arguments.of(
              "getJsonEncodingId",
              (Function<LazyClientDataTypeManager, @Nullable NodeId>)
                  m -> m.getJsonEncodingId(CUSTOM_STRUCT_ID),
              CUSTOM_STRUCT_JSON_ENCODING_ID));
    }

    // Control assertion: a genuinely unresolvable NodeId on an un-interrupted thread must still
    // be cached in attemptedResolution in both the lazy tree and the lazy manager.
    @Test
    void uninterruptedMissingDataTypeIsStillNegativelyCachedInBothTreeAndManager()
        throws Exception {

      var fixture = new MockClientFixture();
      var tree = new LazyClientDataTypeTree(fixture.client);
      var manager = new LazyClientDataTypeManager(fixture.client, fixture.namespaceTable, tree);

      assertNull(manager.getCodec(MISSING_TYPE_ID));
      assertEquals(1, fixture.inverseSubtypeCallsFor(MISSING_TYPE_ID));
      assertEquals(1, fixture.inverseEncodingCallsFor(MISSING_TYPE_ID));

      assertNull(manager.getCodec(MISSING_TYPE_ID));
      assertEquals(
          1,
          fixture.inverseSubtypeCallsFor(MISSING_TYPE_ID),
          "second getCodec() should hit manager's attemptedResolution without re-browsing tree");
      assertEquals(
          1,
          fixture.inverseEncodingCallsFor(MISSING_TYPE_ID),
          "second getCodec() should hit manager's attemptedResolution without re-browsing");

      NodeId otherMissingId = new NodeId(1, "NoSuchDataType2");
      assertNull(manager.getBinaryEncodingId(otherMissingId));
      assertEquals(1, fixture.inverseSubtypeCallsFor(otherMissingId));

      assertNull(manager.getBinaryEncodingId(otherMissingId));
      assertEquals(
          1,
          fixture.inverseSubtypeCallsFor(otherMissingId),
          "second getBinaryEncodingId() should hit attemptedResolution without re-browsing");
    }

    // If a worker thread fails codec resolution and is interrupted while blocked waiting to enter
    // synchronized (manager) before negative-caching, getCodec() must re-check interruption inside
    // the monitor so the ID is not added to attemptedResolution and healthy retries invoke the
    // factory again.
    @Test
    void interruptWhileWaitingOnManagerMonitorDoesNotNegativelyCacheFailedLookup()
        throws Exception {
      var fixture = new MockClientFixture();
      var tree = new LazyClientDataTypeTree(fixture.client);
      var factoryCalls = new AtomicInteger();
      var factoryEntered = new CountDownLatch(1);
      var monitorAcquired = new CountDownLatch(1);
      var workerResult = new AtomicReference<@Nullable DataTypeCodec>();
      var workerInterrupted = new AtomicBoolean(false);
      var workerFailure = new AtomicReference<@Nullable Throwable>();

      var manager =
          new LazyClientDataTypeManager(
              fixture.client,
              fixture.namespaceTable,
              tree,
              (dataType, dataTypeTree) -> {
                if (factoryCalls.incrementAndGet() == 1) {
                  factoryEntered.countDown();
                  try {
                    if (!monitorAcquired.await(5, TimeUnit.SECONDS)) {
                      throw new AssertionError("timed out waiting for monitor holder");
                    }
                  } catch (InterruptedException e) {
                    throw new AssertionError("unexpected interrupt inside factory", e);
                  }
                  return null;
                }
                return DynamicCodecFactory.create(dataType, dataTypeTree);
              });

      Thread worker =
          new Thread(
              () -> {
                try {
                  workerResult.set(manager.getCodec(CUSTOM_STRUCT_ID));
                  workerInterrupted.set(Thread.currentThread().isInterrupted());
                } catch (Throwable t) {
                  workerFailure.set(t);
                }
              },
              "monitor-interrupt-worker");
      worker.start();

      assertTrue(
          factoryEntered.await(5, TimeUnit.SECONDS),
          "worker should enter codecFactory on first lookup");

      synchronized (manager) {
        monitorAcquired.countDown();

        long deadlineNanos = System.nanoTime() + TimeUnit.SECONDS.toNanos(5);
        while (worker.getState() != Thread.State.BLOCKED && System.nanoTime() < deadlineNanos) {
          LockSupport.parkNanos(1_000_000L);
        }
        assertEquals(
            Thread.State.BLOCKED,
            worker.getState(),
            "worker should be blocked waiting to enter synchronized (manager)");

        worker.interrupt();
      }

      worker.join(5_000);
      assertFalse(worker.isAlive(), "worker thread should terminate cleanly");
      assertNull(workerFailure.get(), () -> "unexpected worker failure: " + workerFailure.get());
      assertNull(workerResult.get(), "failed interrupted lookup should return null");
      assertTrue(workerInterrupted.get(), "worker interrupt status should remain preserved");
      assertEquals(1, factoryCalls.get());

      // A healthy lookup must not see a stale negative-cache entry and must re-invoke the factory.
      DataTypeCodec recovered = manager.getCodec(CUSTOM_STRUCT_ID);
      assertNotNull(recovered, "healthy retry must succeed without negative-cache poisoning");
      assertInstanceOf(DynamicStructCodec.class, recovered);
      assertEquals(2, factoryCalls.get());
      assertEquals(CUSTOM_STRUCT_BINARY_ENCODING_ID, manager.getBinaryEncodingId(CUSTOM_STRUCT_ID));
    }

    // If a custom DataType.getNodeId() triggers a nested lookup that hits a wrapped
    // InterruptedException (recording abort on the active retry scope while leaving the thread's
    // interrupt flag clear), createAndRegisterCodec() must observe the abort inside its own retry
    // scope, avoid calling codecFactory or publishing a codec, and allow a healthy retry to
    // succeed.
    @ParameterizedTest(name = "{0}")
    @MethodSource("createAndRegisterEntryPoints")
    void interruptedNestedLookupInsideDataTypeGetNodeIdDoesNotPublishCodec(
        String entryName, Function<LazyClientDataTypeManager, @Nullable Object> lookupCall)
        throws Exception {
      var fixture = new MockClientFixture();
      DataType delegate = fixture.createEagerTree().getDataType(CUSTOM_STRUCT_ID);
      assertNotNull(delegate);

      NodeId nestedInterruptedId = new NodeId(1, "NestedInterruptedInsideGetNodeId");
      var managerRef = new AtomicReference<LazyClientDataTypeManager>();
      var triggeredInGetNodeId = new AtomicBoolean(false);
      var wrappedOnce = new AtomicBoolean(false);
      var factoryCalls = new AtomicInteger();

      LazyClientDataTypeTree tree =
          new LazyClientDataTypeTree(fixture.client) {
            @Override
            public @Nullable DataType getDataType(NodeId dataTypeId) {
              if (nestedInterruptedId.equals(dataTypeId)) {
                throw new RuntimeException(
                    new InterruptedException("nested lookup interrupted inside getNodeId"));
              }
              if (CUSTOM_STRUCT_ID.equals(dataTypeId) && wrappedOnce.compareAndSet(false, true)) {
                return new DataType() {
                  @Override
                  public QualifiedName getBrowseName() {
                    return delegate.getBrowseName();
                  }

                  @Override
                  public NodeId getNodeId() {
                    if (triggeredInGetNodeId.compareAndSet(false, true)) {
                      managerRef.get().getCodec(nestedInterruptedId);
                    }
                    return delegate.getNodeId();
                  }

                  @Override
                  public @Nullable NodeId getBinaryEncodingId() {
                    return delegate.getBinaryEncodingId();
                  }

                  @Override
                  public @Nullable NodeId getXmlEncodingId() {
                    return delegate.getXmlEncodingId();
                  }

                  @Override
                  public @Nullable NodeId getJsonEncodingId() {
                    return delegate.getJsonEncodingId();
                  }

                  @Override
                  public @Nullable DataTypeDefinition getDataTypeDefinition() {
                    return delegate.getDataTypeDefinition();
                  }

                  @Override
                  public Boolean isAbstract() {
                    return delegate.isAbstract();
                  }
                };
              }
              return super.getDataType(dataTypeId);
            }
          };

      var manager =
          new LazyClientDataTypeManager(
              fixture.client,
              fixture.namespaceTable,
              tree,
              (dataType, dataTypeTree) -> {
                factoryCalls.incrementAndGet();
                return DynamicCodecFactory.create(dataType, dataTypeTree);
              });
      managerRef.set(manager);

      WorkerOutcome<Object> outcome = runOnWorkerThread(() -> lookupCall.apply(manager));

      assertTrue(
          triggeredInGetNodeId.get(),
          () -> entryName + ": custom DataType.getNodeId() should have executed");
      assertNull(
          outcome.value(),
          () -> entryName + ": aborted getNodeId() lookup must not publish or return a result");
      assertFalse(
          outcome.interrupted(),
          "wrapped InterruptedException must not set the calling thread's interrupt flag");
      assertEquals(
          0,
          factoryCalls.get(),
          () -> entryName + ": codecFactory must not run after interrupted getNodeId() lookup");

      // Healthy retry must now resolve, construct, and register the codec cleanly.
      Object recovered = lookupCall.apply(manager);
      assertNotNull(recovered, () -> entryName + ": healthy retry must succeed");
      assertEquals(1, factoryCalls.get());
      assertInstanceOf(DynamicStructCodec.class, manager.getCodec(CUSTOM_STRUCT_ID));
      assertEquals(CUSTOM_STRUCT_BINARY_ENCODING_ID, manager.getBinaryEncodingId(CUSTOM_STRUCT_ID));
    }

    static Stream<Arguments> createAndRegisterEntryPoints() {
      return Stream.of(
          Arguments.of(
              "getCodec(C)",
              (Function<LazyClientDataTypeManager, @Nullable Object>)
                  m -> m.getCodec(CUSTOM_STRUCT_ID)),
          Arguments.of(
              "getCodec(E) inverse HasEncoding",
              (Function<LazyClientDataTypeManager, @Nullable Object>)
                  m -> m.getCodec(CUSTOM_STRUCT_BINARY_ENCODING_ID)),
          Arguments.of(
              "getBinaryEncodingId(C)",
              (Function<LazyClientDataTypeManager, @Nullable Object>)
                  m -> m.getBinaryEncodingId(CUSTOM_STRUCT_ID)));
    }

    // After super.registerType() commits a codec, createAndRegisterCodec() must not invoke
    // fallible DataType callbacks such as getBrowseName(); a DataType whose getBrowseName() throws
    // a wrapped InterruptedException must still succeed and return the registered codec.
    @Test
    void throwingBrowseNameAfterRegistrationDoesNotAbortCodecLookup() throws Exception {
      var fixture = new MockClientFixture();
      DataType delegate = fixture.createEagerTree().getDataType(CUSTOM_STRUCT_ID);
      assertNotNull(delegate);

      var browseNameCalls = new AtomicInteger();
      var factoryCalls = new AtomicInteger();

      LazyClientDataTypeTree tree =
          new LazyClientDataTypeTree(fixture.client) {
            @Override
            public @Nullable DataType getDataType(NodeId dataTypeId) {
              if (CUSTOM_STRUCT_ID.equals(dataTypeId)) {
                return new DataType() {
                  @Override
                  public QualifiedName getBrowseName() {
                    browseNameCalls.incrementAndGet();
                    throw new RuntimeException(
                        new InterruptedException("getBrowseName interrupted"));
                  }

                  @Override
                  public NodeId getNodeId() {
                    return delegate.getNodeId();
                  }

                  @Override
                  public @Nullable NodeId getBinaryEncodingId() {
                    return delegate.getBinaryEncodingId();
                  }

                  @Override
                  public @Nullable NodeId getXmlEncodingId() {
                    return delegate.getXmlEncodingId();
                  }

                  @Override
                  public @Nullable NodeId getJsonEncodingId() {
                    return delegate.getJsonEncodingId();
                  }

                  @Override
                  public @Nullable DataTypeDefinition getDataTypeDefinition() {
                    return delegate.getDataTypeDefinition();
                  }

                  @Override
                  public Boolean isAbstract() {
                    return delegate.isAbstract();
                  }
                };
              }
              return super.getDataType(dataTypeId);
            }
          };

      var manager =
          new LazyClientDataTypeManager(
              fixture.client,
              fixture.namespaceTable,
              tree,
              (dataType, dataTypeTree) -> {
                factoryCalls.incrementAndGet();
                return DynamicCodecFactory.create(delegate, dataTypeTree);
              });

      WorkerOutcome<DataTypeCodec> outcome =
          runOnWorkerThread(() -> manager.getCodec(CUSTOM_STRUCT_ID));

      assertNotNull(
          outcome.value(), "getCodec() must return the registered codec without falsely aborting");
      assertInstanceOf(DynamicStructCodec.class, outcome.value());
      assertFalse(outcome.interrupted());
      assertEquals(0, browseNameCalls.get(), "manager must not invoke dataType.getBrowseName()");
      assertEquals(1, factoryCalls.get());
      assertSame(outcome.value(), manager.getCodec(CUSTOM_STRUCT_ID));
      assertEquals(CUSTOM_STRUCT_BINARY_ENCODING_ID, manager.getBinaryEncodingId(CUSTOM_STRUCT_ID));
    }

    // If a worker thread is interrupted while blocked waiting to enter synchronized (manager)
    // right before super.registerType() (either for fresh lazy codec publication or for installing
    // discovered encodings onto an existing permanent codec), it must abort without publishing and
    // allow a subsequent healthy retry to register cleanly.
    @ParameterizedTest(name = "{0}")
    @MethodSource("publishMonitorContentionModes")
    void interruptWhileWaitingOnManagerMonitorBeforePublishDoesNotRegister(
        String modeName, boolean preRegisterPermanentWithoutEncodings) throws Exception {
      var fixture = new MockClientFixture();
      DataType delegate = fixture.createEagerTree().getDataType(CUSTOM_STRUCT_ID);
      assertNotNull(delegate);

      var finalGetterEntered = new CountDownLatch(1);
      var monitorAcquired = new CountDownLatch(1);
      var jsonGetterCalls = new AtomicInteger();
      var factoryCalls = new AtomicInteger();
      var workerResult = new AtomicReference<@Nullable Object>();
      var workerInterrupted = new AtomicBoolean(false);
      var workerFailure = new AtomicReference<@Nullable Throwable>();

      LazyClientDataTypeTree tree =
          new LazyClientDataTypeTree(fixture.client) {
            @Override
            public @Nullable DataType getDataType(NodeId dataTypeId) {
              if (CUSTOM_STRUCT_ID.equals(dataTypeId)) {
                return new DataType() {
                  @Override
                  public QualifiedName getBrowseName() {
                    return delegate.getBrowseName();
                  }

                  @Override
                  public NodeId getNodeId() {
                    return delegate.getNodeId();
                  }

                  @Override
                  public @Nullable NodeId getBinaryEncodingId() {
                    return delegate.getBinaryEncodingId();
                  }

                  @Override
                  public @Nullable NodeId getXmlEncodingId() {
                    return delegate.getXmlEncodingId();
                  }

                  @Override
                  public @Nullable NodeId getJsonEncodingId() {
                    if (jsonGetterCalls.incrementAndGet() == 1) {
                      finalGetterEntered.countDown();
                      try {
                        if (!monitorAcquired.await(5, TimeUnit.SECONDS)) {
                          throw new AssertionError("timed out waiting for monitor holder");
                        }
                      } catch (InterruptedException e) {
                        throw new AssertionError("unexpected interrupt inside final getter", e);
                      }
                    }
                    return delegate.getJsonEncodingId();
                  }

                  @Override
                  public @Nullable DataTypeDefinition getDataTypeDefinition() {
                    return delegate.getDataTypeDefinition();
                  }

                  @Override
                  public Boolean isAbstract() {
                    return delegate.isAbstract();
                  }
                };
              }
              return super.getDataType(dataTypeId);
            }
          };

      var manager =
          new LazyClientDataTypeManager(
              fixture.client,
              fixture.namespaceTable,
              tree,
              (dataType, dataTypeTree) -> {
                factoryCalls.incrementAndGet();
                return DynamicCodecFactory.create(dataType, dataTypeTree);
              });

      DataTypeCodec appCodec = mock(DataTypeCodec.class);
      if (preRegisterPermanentWithoutEncodings) {
        manager.registerType(CUSTOM_STRUCT_ID, appCodec, null, null, null);
      }

      Thread worker =
          new Thread(
              () -> {
                try {
                  Object result =
                      preRegisterPermanentWithoutEncodings
                          ? manager.getBinaryEncodingId(CUSTOM_STRUCT_ID)
                          : manager.getCodec(CUSTOM_STRUCT_ID);
                  workerResult.set(result);
                  workerInterrupted.set(Thread.currentThread().isInterrupted());
                } catch (Throwable t) {
                  workerFailure.set(t);
                }
              },
              "publish-monitor-interrupt-worker");
      worker.start();

      assertTrue(
          finalGetterEntered.await(5, TimeUnit.SECONDS),
          () -> modeName + ": worker should reach the final encoding getter");

      synchronized (manager) {
        monitorAcquired.countDown();

        long deadlineNanos = System.nanoTime() + TimeUnit.SECONDS.toNanos(5);
        while (worker.getState() != Thread.State.BLOCKED && System.nanoTime() < deadlineNanos) {
          LockSupport.parkNanos(1_000_000L);
        }
        assertEquals(
            Thread.State.BLOCKED,
            worker.getState(),
            () -> modeName + ": worker should block on synchronized (manager) before publish");

        worker.interrupt();
      }

      worker.join(5_000);
      assertFalse(worker.isAlive(), () -> modeName + ": worker thread should finish cleanly");
      assertNull(workerFailure.get(), () -> "unexpected worker failure: " + workerFailure.get());
      assertNull(
          workerResult.get(), () -> modeName + ": interrupted publish attempt must return null");
      assertTrue(workerInterrupted.get(), "worker interrupt status should remain preserved");
      assertEquals(1, jsonGetterCalls.get());

      if (preRegisterPermanentWithoutEncodings) {
        assertEquals(0, factoryCalls.get());
        // Healthy retry must re-run missing-encoding discovery (proving nothing was installed on
        // the interrupted attempt and nothing was negatively cached) while retaining appCodec.
        assertEquals(
            CUSTOM_STRUCT_BINARY_ENCODING_ID, manager.getBinaryEncodingId(CUSTOM_STRUCT_ID));
        assertEquals(2, jsonGetterCalls.get());
        assertSame(appCodec, manager.getCodec(CUSTOM_STRUCT_ID));
        assertSame(appCodec, manager.getCodec(CUSTOM_STRUCT_BINARY_ENCODING_ID));
      } else {
        assertEquals(1, factoryCalls.get());
        // Healthy retry must re-run codecFactory (proving nothing was published on the interrupted
        // attempt and nothing was negatively cached).
        DataTypeCodec recovered = manager.getCodec(CUSTOM_STRUCT_ID);
        assertNotNull(recovered);
        assertInstanceOf(DynamicStructCodec.class, recovered);
        assertEquals(2, factoryCalls.get());
        assertEquals(2, jsonGetterCalls.get());
        assertEquals(
            CUSTOM_STRUCT_BINARY_ENCODING_ID, manager.getBinaryEncodingId(CUSTOM_STRUCT_ID));
      }
    }

    static Stream<Arguments> publishMonitorContentionModes() {
      return Stream.of(
          Arguments.of("fresh lazy codec registration", false),
          Arguments.of("permanent codec missing-encoding discovery", true));
    }

    // If A's factory asks for B's XML encoding, and during B's lookup B is registered with only a
    // binary encoding right before B's lookup throws a wrapped InterruptedException,
    // getXmlEncodingId
    // must still propagate the abort (since the requested XML encoding is still missing) so A's
    // fallback factory cannot publish A, and a healthy retry succeeds.
    @Test
    void concurrentPartialRegistrationDoesNotHideInterruptedDependencyEncodingLookup()
        throws Exception {
      var fixture = new MockClientFixture();
      var managerRef = new AtomicReference<LazyClientDataTypeManager>();
      var registerAndInterruptOnce = new AtomicBoolean(false);
      var factoryCalls = new AtomicInteger();
      NodeId partialBinaryOnly = new NodeId(1, "PartialBinaryOnlyForCustomStruct");

      LazyClientDataTypeTree tree =
          new LazyClientDataTypeTree(fixture.client) {
            @Override
            public @Nullable DataType getDataType(NodeId dataTypeId) {
              if (CUSTOM_STRUCT_ID.equals(dataTypeId)
                  && registerAndInterruptOnce.compareAndSet(false, true)) {
                managerRef
                    .get()
                    .registerType(
                        CUSTOM_STRUCT_ID, mock(DataTypeCodec.class), partialBinaryOnly, null, null);
                throw new RuntimeException(
                    new InterruptedException("B lookup interrupted after partial registration"));
              }
              return super.getDataType(dataTypeId);
            }
          };

      var manager =
          new LazyClientDataTypeManager(
              fixture.client,
              fixture.namespaceTable,
              tree,
              (dataType, dataTypeTree) -> {
                if (OTHER_STRUCT_ID.equals(dataType.getNodeId())) {
                  factoryCalls.incrementAndGet();
                  // Ask for B's XML encoding and fall back to building A's codec even when null.
                  managerRef.get().getXmlEncodingId(CUSTOM_STRUCT_ID);
                }
                return DynamicCodecFactory.create(dataType, dataTypeTree);
              });
      managerRef.set(manager);

      WorkerOutcome<DataTypeCodec> outcome =
          runOnWorkerThread(() -> manager.getCodec(OTHER_STRUCT_ID));

      assertTrue(
          registerAndInterruptOnce.get(),
          "B's lookup callback should have registered B partially and thrown wrapped interrupt");
      assertNull(
          outcome.value(),
          "A must not be published after its dependency encoding lookup was interrupted");
      assertFalse(
          outcome.interrupted(),
          "wrapped InterruptedException must not set the calling thread's interrupt flag");
      assertEquals(1, factoryCalls.get());

      // Healthy retry must re-invoke A's factory (proving A was neither published nor negatively
      // cached) and succeed cleanly.
      DataTypeCodec recovered = manager.getCodec(OTHER_STRUCT_ID);
      assertNotNull(recovered, "healthy retry of A must succeed");
      assertInstanceOf(DynamicStructCodec.class, recovered);
      assertEquals(2, factoryCalls.get());
      assertEquals(OTHER_STRUCT_BINARY_ENCODING_ID, manager.getBinaryEncodingId(OTHER_STRUCT_ID));
    }

    // Acquire B with temporary binary encoding E, permanently register B without encodings (leaving
    // DefaultDataTypeManager's forward mapping B -> E backed by the temporary handle), then
    // permanently register another type U with E (overwriting only the reverse mapping E -> U).
    // While the temporary handle is still open, A requesting B's missing XML encoding must treat
    // the miss as retryable (not permanently unresolvable) so A is not negatively cached; after
    // closing the handle removes the temporary forward mapping B -> E, a healthy retry of A
    // discovers B's real XML encoding and succeeds.
    @Test
    void temporaryForwardEncodingAfterTypeAndReverseReplacementDoesNotNegativelyCacheDependent()
        throws Exception {
      var fixture = new MockClientFixture();
      var tree = new LazyClientDataTypeTree(fixture.client);
      var managerRef = new AtomicReference<LazyClientDataTypeManager>();
      var factoryCalls = new AtomicInteger();
      var observedXmlEncoding = new AtomicReference<@Nullable NodeId>();

      var manager =
          new LazyClientDataTypeManager(
              fixture.client,
              fixture.namespaceTable,
              tree,
              (dataType, dataTypeTree) -> {
                if (OTHER_STRUCT_ID.equals(dataType.getNodeId())) {
                  factoryCalls.incrementAndGet();
                  NodeId xmlId = managerRef.get().getXmlEncodingId(CUSTOM_STRUCT_ID);
                  observedXmlEncoding.set(xmlId);
                  if (xmlId == null) {
                    return null;
                  }
                }
                return DynamicCodecFactory.create(dataType, dataTypeTree);
              });
      managerRef.set(manager);

      NodeId sharedTempBinaryE = new NodeId(1, "SharedTemporaryBinaryEncodingE");
      NodeId permanentTypeU = new NodeId(1, "PermanentTypeUTakingOverE");
      DataTypeCodec permCodecB = mock(DataTypeCodec.class);

      try (DataTypeManager.RegistrationHandle tempHandleB =
          manager.acquireType(
              CUSTOM_STRUCT_ID, mock(DataTypeCodec.class), sharedTempBinaryE, null, null)) {
        // Permanently replace B's type entry without encodings; B's forward binary mapping still
        // belongs to tempHandleB.
        manager.registerType(CUSTOM_STRUCT_ID, permCodecB, null, null, null);
        // Permanently claim E for another type U; reverse mapping E -> U is now permanent, while
        // B's forward mapping B -> E is still owned by tempHandleB.
        manager.registerType(
            permanentTypeU, mock(DataTypeCodec.class), sharedTempBinaryE, null, null);

        assertNull(
            manager.getCodec(OTHER_STRUCT_ID),
            "A's lookup should fail while B's XML encoding is hidden behind temporary forward E");
        assertEquals(1, factoryCalls.get());
        assertNull(observedXmlEncoding.get());
      }

      // Closing tempHandleB removed the temporary forward mapping B -> E. A healthy retry of A
      // must not be blocked by negative caching, must discover B's real XML encoding from the
      // server while retaining permCodecB, and must succeed.
      DataTypeCodec recovered = manager.getCodec(OTHER_STRUCT_ID);
      assertNotNull(recovered, "healthy retry of A must succeed after temporary handle closes");
      assertInstanceOf(DynamicStructCodec.class, recovered);
      assertEquals(2, factoryCalls.get());
      assertEquals(CUSTOM_STRUCT_XML_ENCODING_ID, observedXmlEncoding.get());
      assertEquals(CUSTOM_STRUCT_XML_ENCODING_ID, manager.getXmlEncodingId(CUSTOM_STRUCT_ID));
      assertSame(permCodecB, manager.getCodec(CUSTOM_STRUCT_ID));
    }

    // Calling clearFailedResolutions() while holding synchronized (manager) concurrently with
    // another thread calling clearFailedResolutions() must never deadlock on lock ordering between
    // manager's monitor and write lock, and must still clear negative-cache entries cleanly.
    @Test
    void clearFailedResolutionsWhileHoldingManagerMonitorDoesNotDeadlock() throws Exception {
      var fixture = new MockClientFixture();
      var tree = new LazyClientDataTypeTree(fixture.client);
      var manager = new LazyClientDataTypeManager(fixture.client, fixture.namespaceTable, tree);

      // Populate negative-cache entry for MISSING_TYPE_ID.
      assertNull(manager.getCodec(MISSING_TYPE_ID));
      assertEquals(1, fixture.inverseSubtypeCallsFor(MISSING_TYPE_ID));
      assertEquals(1, fixture.inverseEncodingCallsFor(MISSING_TYPE_ID));

      var concurrentStarted = new CountDownLatch(1);
      var concurrentFailure = new AtomicReference<@Nullable Throwable>();
      var holderFinished = new CountDownLatch(1);
      var holderFailure = new AtomicReference<@Nullable Throwable>();

      Thread concurrentClearer =
          new Thread(
              () -> {
                concurrentStarted.countDown();
                try {
                  manager.clearFailedResolutions();
                } catch (Throwable t) {
                  concurrentFailure.set(t);
                }
              },
              "concurrent-clear-failed-resolutions");

      Thread monitorHolder =
          new Thread(
              () -> {
                try {
                  synchronized (manager) {
                    concurrentClearer.start();
                    assertTrue(
                        concurrentStarted.await(5, TimeUnit.SECONDS),
                        "concurrent clearer thread should start");

                    long deadlineNanos = System.nanoTime() + TimeUnit.SECONDS.toNanos(5);
                    while (concurrentClearer.getState() != Thread.State.BLOCKED
                        && System.nanoTime() < deadlineNanos) {
                      LockSupport.parkNanos(1_000_000L);
                    }
                    assertEquals(
                        Thread.State.BLOCKED,
                        concurrentClearer.getState(),
                        "concurrent clearer should block waiting on manager monitor");

                    // Must re-enter synchronized (manager) cleanly without acquiring write lock.
                    manager.clearFailedResolutions();
                  }
                } catch (Throwable t) {
                  holderFailure.set(t);
                } finally {
                  holderFinished.countDown();
                }
              },
              "monitor-holder-clear-failed-resolutions");

      monitorHolder.start();

      boolean finished = holderFinished.await(5, TimeUnit.SECONDS);
      if (!finished) {
        monitorHolder.interrupt();
        concurrentClearer.interrupt();
      }
      assertTrue(
          finished, "clearFailedResolutions() while holding manager monitor must not deadlock");

      monitorHolder.join(5_000);
      concurrentClearer.join(5_000);
      assertFalse(monitorHolder.isAlive(), "monitor holder thread should terminate cleanly");
      assertFalse(
          concurrentClearer.isAlive(), "concurrent clearer thread should terminate cleanly");
      assertNull(
          holderFailure.get(), () -> "unexpected monitor holder failure: " + holderFailure.get());
      assertNull(
          concurrentFailure.get(),
          () -> "unexpected concurrent clearer failure: " + concurrentFailure.get());

      // Verify negative cache was cleared cleanly.
      tree.clearFailedResolutions();
      assertNull(manager.getCodec(MISSING_TYPE_ID));
      assertEquals(2, fixture.inverseSubtypeCallsFor(MISSING_TYPE_ID));
      assertEquals(2, fixture.inverseEncodingCallsFor(MISSING_TYPE_ID));
    }

    // Even when the manager's write lock is initially free, calling getCodec() on a cache miss
    // while holding synchronized (manager) must defer immediately instead of entering the tree,
    // preventing deadlock with another thread holding the tree write lock and calling
    // manager.getCodec() (which blocks on synchronized (manager) inside super.getCodec()).
    @Test
    void getCodecWhileHoldingManagerMonitorDefersWithoutDeadlockingWithTreeWriter()
        throws Exception {
      var fixture = new MockClientFixture();
      var tree = new LazyClientDataTypeTree(fixture.client);
      var manager = new LazyClientDataTypeManager(fixture.client, fixture.namespaceTable, tree);

      var treeWriteLockHeld = new CountDownLatch(1);
      var monitorHeldByThreadA = new CountDownLatch(1);
      var hookedOnce = new AtomicBoolean(false);
      var threadAResult = new AtomicReference<@Nullable DataTypeCodec>();
      var threadAFailure = new AtomicReference<@Nullable Throwable>();
      var threadBFailure = new AtomicReference<@Nullable Throwable>();
      var threadADone = new CountDownLatch(1);
      var threadBDone = new CountDownLatch(1);

      fixture.singleBrowseHook =
          bd -> {
            if (OTHER_STRUCT_ID.equals(bd.getNodeId()) && hookedOnce.compareAndSet(false, true)) {
              treeWriteLockHeld.countDown();
              try {
                if (!monitorHeldByThreadA.await(5, TimeUnit.SECONDS)) {
                  throw new AssertionError(
                      "timed out waiting for Thread A to hold manager monitor");
                }
              } catch (InterruptedException e) {
                throw new AssertionError("unexpected interrupt waiting for monitor holder", e);
              }
              // While holding the tree's write lock, enter super.getCodec() which blocks on
              // synchronized (manager) held by Thread A.
              manager.getCodec(CUSTOM_STRUCT_ID);
            }
          };

      Thread threadB =
          new Thread(
              () -> {
                try {
                  tree.getDataType(OTHER_STRUCT_ID);
                } catch (Throwable t) {
                  threadBFailure.set(t);
                } finally {
                  threadBDone.countDown();
                }
              },
              "tree-writer-calling-manager");

      Thread threadA =
          new Thread(
              () -> {
                try {
                  synchronized (manager) {
                    monitorHeldByThreadA.countDown();

                    long deadlineNanos = System.nanoTime() + TimeUnit.SECONDS.toNanos(5);
                    while (threadB.getState() != Thread.State.BLOCKED
                        && System.nanoTime() < deadlineNanos) {
                      LockSupport.parkNanos(1_000_000L);
                    }
                    assertEquals(
                        Thread.State.BLOCKED,
                        threadB.getState(),
                        "Thread B should block on synchronized (manager) while holding tree lock");

                    // Manager write lock is free, but Thread.holdsLock(manager) is true: must
                    // defer cleanly without entering tree or deadlocking.
                    threadAResult.set(manager.getCodec(SUB_STRUCT_ID));
                  }
                } catch (Throwable t) {
                  threadAFailure.set(t);
                } finally {
                  threadADone.countDown();
                }
              },
              "monitor-holder-calling-getCodec");

      threadB.setDaemon(true);
      threadA.setDaemon(true);
      threadB.start();
      assertTrue(
          treeWriteLockHeld.await(5, TimeUnit.SECONDS),
          "Thread B should acquire tree write lock inside browse hook");

      threadA.start();

      boolean bothFinished =
          threadADone.await(5, TimeUnit.SECONDS) && threadBDone.await(5, TimeUnit.SECONDS);
      if (!bothFinished) {
        threadA.interrupt();
        threadB.interrupt();
      }
      assertTrue(
          bothFinished,
          "getCodec() while holding manager monitor must defer instead of deadlocking with tree"
              + " writer");

      threadA.join(5_000);
      threadB.join(5_000);
      assertFalse(threadA.isAlive(), "Thread A should terminate cleanly");
      assertFalse(threadB.isAlive(), "Thread B should terminate cleanly");
      assertNull(
          threadAFailure.get(), () -> "unexpected Thread A failure: " + threadAFailure.get());
      assertNull(
          threadBFailure.get(), () -> "unexpected Thread B failure: " + threadBFailure.get());
      assertNull(
          threadAResult.get(),
          "cache-miss getCodec() while holding manager monitor should defer and return null");

      // Subsequent healthy lookup of the deferred type must succeed without negative-cache
      // poisoning.
      DataTypeCodec recovered = manager.getCodec(SUB_STRUCT_ID);
      assertNotNull(recovered, "healthy lookup after monitor-held deferral must succeed");
      assertInstanceOf(DynamicStructCodec.class, recovered);
      assertEquals(SUB_STRUCT_BINARY_ENCODING_ID, manager.getBinaryEncodingId(SUB_STRUCT_ID));
    }

    // If type T is registered with (codecA, xml=X) and then re-registered with (codecB, null,
    // null, null), X still resolves to codecA while T resolves to codecB. Calling getCodec(E) to
    // discover and install missing binary encoding E must bind E -> codecB (with getDataTypeId(E)
    // == T) without overwriting X's existing codecA mapping.
    @Test
    void partialEncodingDiscoveryPreservesDistinctCodecOnExistingEncodingSlot() throws Exception {
      var fixture = new MockClientFixture();
      var tree = new LazyClientDataTypeTree(fixture.client);
      var factoryCalls = new AtomicInteger();

      var manager =
          new LazyClientDataTypeManager(
              fixture.client,
              fixture.namespaceTable,
              tree,
              (dataType, dataTypeTree) -> {
                factoryCalls.incrementAndGet();
                return DynamicCodecFactory.create(dataType, dataTypeTree);
              });

      DataTypeCodec codecA = mock(DataTypeCodec.class);
      DataTypeCodec codecB = mock(DataTypeCodec.class);

      manager.registerType(CUSTOM_STRUCT_ID, codecA, null, CUSTOM_STRUCT_XML_ENCODING_ID, null);
      manager.registerType(CUSTOM_STRUCT_ID, codecB, null, null, null);

      assertSame(codecB, manager.getCodec(CUSTOM_STRUCT_ID));
      assertSame(codecA, manager.getCodec(CUSTOM_STRUCT_XML_ENCODING_ID));
      assertEquals(CUSTOM_STRUCT_XML_ENCODING_ID, manager.getXmlEncodingId(CUSTOM_STRUCT_ID));

      DataTypeCodec discovered = manager.getCodec(CUSTOM_STRUCT_BINARY_ENCODING_ID);
      assertSame(codecB, discovered, "discovered binary encoding E must resolve to current codecB");
      assertEquals(0, factoryCalls.get(), "codecFactory must not run for retained permanent codec");
      assertEquals(1, fixture.inverseEncodingCallsFor(CUSTOM_STRUCT_BINARY_ENCODING_ID));
      assertEquals(
          CUSTOM_STRUCT_ID,
          manager.getDataTypeId(CUSTOM_STRUCT_BINARY_ENCODING_ID),
          "getDataTypeId(E) must return T");
      assertEquals(CUSTOM_STRUCT_BINARY_ENCODING_ID, manager.getBinaryEncodingId(CUSTOM_STRUCT_ID));
      assertSame(
          codecA,
          manager.getCodec(CUSTOM_STRUCT_XML_ENCODING_ID),
          "existing XML encoding slot X must keep its distinct codecA mapping");
      assertEquals(CUSTOM_STRUCT_XML_ENCODING_ID, manager.getXmlEncodingId(CUSTOM_STRUCT_ID));
      assertEquals(CUSTOM_STRUCT_ID, manager.getDataTypeId(CUSTOM_STRUCT_XML_ENCODING_ID));
      assertSame(codecB, manager.getCodec(CUSTOM_STRUCT_ID));

      // Subsequent getCodec(E) hits local cache without re-browsing inverse HasEncoding.
      assertSame(codecB, manager.getCodec(CUSTOM_STRUCT_BINARY_ENCODING_ID));
      assertEquals(1, fixture.inverseEncodingCallsFor(CUSTOM_STRUCT_BINARY_ENCODING_ID));
    }
  }

  // ===== Test Helpers =====

  @FunctionalInterface
  private interface InterruptStage {
    void arm(MockClientFixture fixture, InterruptGate gate);
  }

  @FunctionalInterface
  private interface BrowseHook {
    void beforeBrowse(BrowseDescription bd) throws UaException;
  }

  @FunctionalInterface
  private interface ReadHook {
    void beforeRead(List<ReadValueId> readValueIds) throws UaException;
  }

  private static Throwable createCyclicCauseChain() {
    var causeA = new IllegalStateException("cycle A");
    var causeB = new IllegalStateException("cycle B");
    causeA.initCause(causeB);
    causeB.initCause(causeA);
    return causeA;
  }

  private static int countOccurrencesInSnapshot(LazyClientDataTypeTree tree, NodeId targetId) {
    var count = new AtomicInteger();
    tree.getRoot()
        .traverse(
            dataType -> {
              if (targetId.equals(dataType.getNodeId())) {
                count.incrementAndGet();
              }
            });
    return count.get();
  }

  private record WorkerOutcome<T>(@Nullable T value, boolean interrupted) {}

  private static final class InterruptGate {
    final CountDownLatch entered = new CountDownLatch(1);
    final CountDownLatch release = new CountDownLatch(1);

    void awaitInterruption() throws UaException {
      entered.countDown();
      try {
        if (!release.await(5, TimeUnit.SECONDS)) {
          throw new AssertionError("Timed out waiting for test thread to interrupt worker");
        }
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        throw new UaException(StatusCodes.Bad_UnexpectedError, e);
      }
    }
  }

  private static <T> WorkerOutcome<T> runOnWorkerThread(Supplier<@Nullable T> action)
      throws Exception {

    var result = new CompletableFuture<WorkerOutcome<T>>();
    Thread worker =
        new Thread(
            () -> {
              try {
                T value = action.get();
                result.complete(new WorkerOutcome<>(value, Thread.currentThread().isInterrupted()));
              } catch (Throwable t) {
                result.completeExceptionally(t);
              }
            },
            "worker-thread");
    worker.setDaemon(true);
    worker.start();

    return result.get(5, TimeUnit.SECONDS);
  }

  private static <T> WorkerOutcome<T> runOnPreInterruptedThread(Supplier<@Nullable T> action)
      throws Exception {

    var result = new CompletableFuture<WorkerOutcome<T>>();
    Thread worker =
        new Thread(
            () -> {
              try {
                Thread.currentThread().interrupt();
                T value = action.get();
                result.complete(new WorkerOutcome<>(value, Thread.currentThread().isInterrupted()));
              } catch (Throwable t) {
                result.completeExceptionally(t);
              }
            },
            "pre-interrupted-worker");
    worker.setDaemon(true);
    worker.start();

    return result.get(5, TimeUnit.SECONDS);
  }

  private static <T> WorkerOutcome<T> runAndInterruptAtGate(
      InterruptGate gate, Supplier<@Nullable T> action) throws Exception {

    var result = new CompletableFuture<WorkerOutcome<T>>();
    Thread worker =
        new Thread(
            () -> {
              try {
                T value = action.get();
                result.complete(new WorkerOutcome<>(value, Thread.currentThread().isInterrupted()));
              } catch (Throwable t) {
                result.completeExceptionally(t);
              }
            },
            "gated-interrupted-worker");
    worker.setDaemon(true);
    worker.start();

    try {
      assertTrue(
          gate.entered.await(5, TimeUnit.SECONDS),
          "worker should reach the armed resolution step before interruption");
      worker.interrupt();
      return result.get(5, TimeUnit.SECONDS);
    } finally {
      gate.release.countDown();
    }
  }

  private static boolean awaitWaiting(Thread thread, long timeoutNanos) {
    long deadline = System.nanoTime() + timeoutNanos;
    while (System.nanoTime() < deadline) {
      if (thread.getState() == Thread.State.WAITING) {
        return true;
      }
      LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(1));
    }
    return thread.getState() == Thread.State.WAITING;
  }

  private static final class MockClientFixture {

    final NamespaceTable namespaceTable = new NamespaceTable();
    final OpcUaClient client;
    final OpcUaSession session;

    final AtomicInteger singleBrowseCalls = new AtomicInteger();
    final AtomicInteger batchBrowseCalls = new AtomicInteger();
    final AtomicInteger readCalls = new AtomicInteger();

    final AtomicReference<@Nullable Predicate<BrowseDescription>> singleBrowseGate =
        new AtomicReference<>();
    final AtomicReference<Boolean> readGate = new AtomicReference<>(false);
    final AtomicReference<Boolean> batchBrowseGate = new AtomicReference<>(false);
    volatile @Nullable InterruptGate gate;
    volatile @Nullable BrowseHook singleBrowseHook;
    volatile @Nullable ReadHook readHook;
    volatile boolean encodeDefinitionsAsExtensionObjects;

    private final DefaultEncodingContext encodingContext = new DefaultEncodingContext();

    private final Map<NodeId, AtomicInteger> inverseSubtypeCounts = new HashMap<>();
    private final Map<NodeId, AtomicInteger> inverseEncodingCounts = new HashMap<>();

    private final Map<NodeId, NodeId> supertypeByNodeId = new HashMap<>();
    private final Map<NodeId, NodeId> dataTypeByEncodingId = new HashMap<>();
    private final Map<NodeId, TypeFixture> typeFixtures = new HashMap<>();

    MockClientFixture() throws UaException {
      namespaceTable.add(CUSTOM_NAMESPACE_URI);

      StructureField valueField =
          new StructureField(
              "Value", LocalizedText.NULL_VALUE, NodeIds.Int32, -1, null, uint(0), false);
      StructureField subField =
          new StructureField(
              "Extra", LocalizedText.NULL_VALUE, NodeIds.String, -1, null, uint(0), false);

      StructureDefinition sampleDef =
          new StructureDefinition(
              CUSTOM_STRUCT_BINARY_ENCODING_ID,
              NodeIds.Structure,
              StructureType.Structure,
              new StructureField[] {valueField});
      StructureDefinition subDef =
          new StructureDefinition(
              SUB_STRUCT_BINARY_ENCODING_ID,
              CUSTOM_STRUCT_ID,
              StructureType.Structure,
              new StructureField[] {valueField, subField});
      StructureDefinition otherDef =
          new StructureDefinition(
              OTHER_STRUCT_BINARY_ENCODING_ID,
              NodeIds.Structure,
              StructureType.Structure,
              new StructureField[] {valueField});

      registerType(
          NodeIds.Structure,
          NodeIds.BaseDataType,
          new QualifiedName(0, "Structure"),
          true,
          null,
          null,
          null,
          null);
      registerType(
          CUSTOM_STRUCT_ID,
          NodeIds.Structure,
          new QualifiedName(1, "SampleStruct"),
          false,
          sampleDef,
          CUSTOM_STRUCT_BINARY_ENCODING_ID,
          CUSTOM_STRUCT_XML_ENCODING_ID,
          CUSTOM_STRUCT_JSON_ENCODING_ID);
      registerType(
          SUB_STRUCT_ID,
          CUSTOM_STRUCT_ID,
          new QualifiedName(1, "SubSampleStruct"),
          false,
          subDef,
          SUB_STRUCT_BINARY_ENCODING_ID,
          null,
          null);
      registerType(
          OTHER_STRUCT_ID,
          NodeIds.Structure,
          new QualifiedName(1, "OtherStruct"),
          false,
          otherDef,
          OTHER_STRUCT_BINARY_ENCODING_ID,
          null,
          null);

      client = mock(OpcUaClient.class);
      session = mock(OpcUaSession.class);
      NodeId sessionId = new NodeId(0, 1);

      when(session.getSessionId()).thenReturn(sessionId);
      when(client.getSession()).thenReturn(session);
      when(client.getSessionAsync()).thenReturn(CompletableFuture.completedFuture(session));
      when(client.getNamespaceTable()).thenReturn(namespaceTable);
      when(client.readNamespaceTable()).thenReturn(namespaceTable);
      when(client.getStaticEncodingContext()).thenReturn(encodingContext);
      when(client.getOperationLimits())
          .thenReturn(
              new OperationLimits(
                  Map.of(
                      OperationLimit.MaxNodesPerRead,
                      uint(100),
                      OperationLimit.MaxNodesPerBrowse,
                      uint(100))));

      when(client.browse(any(BrowseDescription.class)))
          .thenAnswer(invocation -> handleSingleBrowse(invocation.getArgument(0)));

      when(client.browse(anyList()))
          .thenAnswer(invocation -> handleBatchBrowse(invocation.getArgument(0)));

      when(client.read(anyDouble(), any(TimestampsToReturn.class), anyList()))
          .thenAnswer(invocation -> handleRead(invocation.getArgument(2)));
    }

    int totalClientCalls() {
      return singleBrowseCalls.get() + batchBrowseCalls.get() + readCalls.get();
    }

    int inverseSubtypeCallsFor(NodeId nodeId) {
      synchronized (inverseSubtypeCounts) {
        AtomicInteger count = inverseSubtypeCounts.get(nodeId);
        return count == null ? 0 : count.get();
      }
    }

    int inverseEncodingCallsFor(NodeId nodeId) {
      synchronized (inverseEncodingCounts) {
        AtomicInteger count = inverseEncodingCounts.get(nodeId);
        return count == null ? 0 : count.get();
      }
    }

    DataTypeTree createEagerTree() {
      Tree<DataType> root =
          new Tree<>(
              null,
              new ClientDataType(
                  new QualifiedName(0, "BaseDataType"),
                  NodeIds.BaseDataType,
                  null,
                  null,
                  null,
                  null,
                  true));

      Tree<DataType> structure = root.addChild(typeFixtures.get(NodeIds.Structure).toDataType());
      Tree<DataType> sample = structure.addChild(typeFixtures.get(CUSTOM_STRUCT_ID).toDataType());
      sample.addChild(typeFixtures.get(SUB_STRUCT_ID).toDataType());
      structure.addChild(typeFixtures.get(OTHER_STRUCT_ID).toDataType());

      return new DataTypeTree(root);
    }

    private void registerType(
        NodeId nodeId,
        NodeId supertypeId,
        QualifiedName browseName,
        boolean isAbstract,
        @Nullable DataTypeDefinition definition,
        @Nullable NodeId binaryEncodingId,
        @Nullable NodeId xmlEncodingId,
        @Nullable NodeId jsonEncodingId) {

      supertypeByNodeId.put(nodeId, supertypeId);
      typeFixtures.put(
          nodeId,
          new TypeFixture(
              nodeId,
              browseName,
              isAbstract,
              definition,
              binaryEncodingId,
              xmlEncodingId,
              jsonEncodingId));

      if (binaryEncodingId != null) {
        dataTypeByEncodingId.put(binaryEncodingId, nodeId);
      }
      if (xmlEncodingId != null) {
        dataTypeByEncodingId.put(xmlEncodingId, nodeId);
      }
      if (jsonEncodingId != null) {
        dataTypeByEncodingId.put(jsonEncodingId, nodeId);
      }
    }

    void mapEncodingToDataType(NodeId encodingId, NodeId dataTypeId) {
      dataTypeByEncodingId.put(encodingId, dataTypeId);
    }

    private BrowseResult handleSingleBrowse(BrowseDescription bd) throws UaException {
      singleBrowseCalls.incrementAndGet();

      if (bd.getBrowseDirection() == BrowseDirection.Inverse) {
        if (NodeIds.HasSubtype.equals(bd.getReferenceTypeId())) {
          synchronized (inverseSubtypeCounts) {
            inverseSubtypeCounts
                .computeIfAbsent(bd.getNodeId(), ignored -> new AtomicInteger())
                .incrementAndGet();
          }
        } else if (NodeIds.HasEncoding.equals(bd.getReferenceTypeId())) {
          synchronized (inverseEncodingCounts) {
            inverseEncodingCounts
                .computeIfAbsent(bd.getNodeId(), ignored -> new AtomicInteger())
                .incrementAndGet();
          }
        }
      }

      BrowseHook hook = singleBrowseHook;
      if (hook != null) {
        hook.beforeBrowse(bd);
      }

      Predicate<BrowseDescription> predicate = singleBrowseGate.get();
      if (predicate != null
          && predicate.test(bd)
          && singleBrowseGate.compareAndSet(predicate, null)) {
        InterruptGate activeGate = gate;
        if (activeGate != null) {
          activeGate.awaitInterruption();
        }
      }

      if (bd.getBrowseDirection() == BrowseDirection.Inverse
          && NodeIds.HasSubtype.equals(bd.getReferenceTypeId())) {
        NodeId parentId = supertypeByNodeId.get(bd.getNodeId());
        if (parentId == null) {
          return new BrowseResult(
              new StatusCode(StatusCodes.Bad_NodeIdUnknown), ByteString.NULL_VALUE, null);
        }
        return new BrowseResult(
            StatusCode.GOOD,
            ByteString.NULL_VALUE,
            new ReferenceDescription[] {
              refDesc(NodeIds.HasSubtype, false, parentId, new QualifiedName(0, "Parent"))
            });
      }

      if (bd.getBrowseDirection() == BrowseDirection.Inverse
          && NodeIds.HasEncoding.equals(bd.getReferenceTypeId())) {
        NodeId dataTypeId = dataTypeByEncodingId.get(bd.getNodeId());
        if (dataTypeId == null) {
          return new BrowseResult(
              new StatusCode(StatusCodes.Bad_NodeIdUnknown), ByteString.NULL_VALUE, null);
        }
        return new BrowseResult(
            StatusCode.GOOD,
            ByteString.NULL_VALUE,
            new ReferenceDescription[] {
              refDesc(NodeIds.HasEncoding, false, dataTypeId, new QualifiedName(1, "DataType"))
            });
      }

      return new BrowseResult(StatusCode.GOOD, ByteString.NULL_VALUE, new ReferenceDescription[0]);
    }

    private List<BrowseResult> handleBatchBrowse(List<BrowseDescription> descriptions)
        throws UaException {

      batchBrowseCalls.incrementAndGet();

      if (batchBrowseGate.compareAndSet(true, false)) {
        InterruptGate activeGate = gate;
        if (activeGate != null) {
          activeGate.awaitInterruption();
        }
      }

      var results = new ArrayList<BrowseResult>(descriptions.size());
      for (BrowseDescription bd : descriptions) {
        TypeFixture fixture = typeFixtures.get(bd.getNodeId());
        if (fixture == null) {
          results.add(
              new BrowseResult(
                  new StatusCode(StatusCodes.Bad_NodeIdUnknown), ByteString.NULL_VALUE, null));
          continue;
        }

        var refs = new ArrayList<ReferenceDescription>();
        if (fixture.binaryEncodingId != null) {
          refs.add(
              refDesc(
                  NodeIds.HasEncoding,
                  true,
                  fixture.binaryEncodingId,
                  DataTypeEncoding.BINARY_ENCODING_NAME));
        }
        if (fixture.xmlEncodingId != null) {
          refs.add(
              refDesc(
                  NodeIds.HasEncoding,
                  true,
                  fixture.xmlEncodingId,
                  DataTypeEncoding.XML_ENCODING_NAME));
        }
        if (fixture.jsonEncodingId != null) {
          refs.add(
              refDesc(
                  NodeIds.HasEncoding,
                  true,
                  fixture.jsonEncodingId,
                  DataTypeEncoding.JSON_ENCODING_NAME));
        }
        results.add(
            new BrowseResult(
                StatusCode.GOOD, ByteString.NULL_VALUE, refs.toArray(new ReferenceDescription[0])));
      }
      return results;
    }

    private ReadResponse handleRead(List<ReadValueId> readValueIds) throws UaException {
      readCalls.incrementAndGet();

      ReadHook hook = readHook;
      if (hook != null) {
        hook.beforeRead(readValueIds);
      }

      if (readGate.compareAndSet(true, false)) {
        InterruptGate activeGate = gate;
        if (activeGate != null) {
          activeGate.awaitInterruption();
        }
      }

      var values = new DataValue[readValueIds.size()];
      for (int i = 0; i < readValueIds.size(); i++) {
        ReadValueId rvid = readValueIds.get(i);
        TypeFixture fixture = typeFixtures.get(rvid.getNodeId());
        if (fixture == null) {
          values[i] = new DataValue(StatusCodes.Bad_NodeIdUnknown);
          continue;
        }

        if (AttributeId.BrowseName.uid().equals(rvid.getAttributeId())) {
          values[i] = DataValue.valueOnly(Variant.ofQualifiedName(fixture.browseName));
        } else if (AttributeId.IsAbstract.uid().equals(rvid.getAttributeId())) {
          values[i] = DataValue.valueOnly(Variant.ofBoolean(fixture.isAbstract));
        } else if (AttributeId.DataTypeDefinition.uid().equals(rvid.getAttributeId())) {
          if (fixture.definition != null) {
            Object value =
                encodeDefinitionsAsExtensionObjects
                    ? ExtensionObject.encode(encodingContext, fixture.definition)
                    : fixture.definition;
            values[i] = DataValue.valueOnly(Variant.of(value));
          } else {
            values[i] = new DataValue(StatusCodes.Bad_AttributeIdInvalid);
          }
        } else {
          values[i] = new DataValue(StatusCodes.Bad_AttributeIdInvalid);
        }
      }

      return new ReadResponse(
          new ResponseHeader(DateTime.now(), uint(1), StatusCode.GOOD, null, null, null),
          values,
          null);
    }

    private static ReferenceDescription refDesc(
        NodeId refTypeId, boolean isForward, NodeId targetId, QualifiedName browseName) {

      return new ReferenceDescription(
          refTypeId,
          isForward,
          targetId.expanded(),
          browseName,
          LocalizedText.english(browseName.name()),
          NodeClass.DataType,
          NodeId.NULL_VALUE.expanded());
    }

    private record TypeFixture(
        NodeId nodeId,
        QualifiedName browseName,
        boolean isAbstract,
        @Nullable DataTypeDefinition definition,
        @Nullable NodeId binaryEncodingId,
        @Nullable NodeId xmlEncodingId,
        @Nullable NodeId jsonEncodingId) {

      ClientDataType toDataType() {
        return new ClientDataType(
            browseName,
            nodeId,
            binaryEncodingId,
            xmlEncodingId,
            jsonEncodingId,
            definition,
            isAbstract);
      }
    }
  }
}
