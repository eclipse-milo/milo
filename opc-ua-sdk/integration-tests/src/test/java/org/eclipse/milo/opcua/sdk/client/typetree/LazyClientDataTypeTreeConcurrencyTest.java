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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.LockSupport;
import java.util.function.Function;
import java.util.stream.Stream;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.OpcUaSession;
import org.eclipse.milo.opcua.sdk.client.OperationLimits;
import org.eclipse.milo.opcua.sdk.core.typetree.DataType;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.OpcUaDataType;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UNumber;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseResult;
import org.eclipse.milo.opcua.stack.core.util.Tree;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class LazyClientDataTypeTreeConcurrencyTest {

  private static final NodeId MISSING_TYPE_ID = new NodeId(1, "NoSuchDataType");

  // A failed resolution followed immediately by clearFailedResolutions() must not deadlock
  // the querying thread by trying to upgrade a held read lock to a write lock, and must not
  // trigger a second resolution inside the same query.
  @ParameterizedTest(name = "{0}")
  @MethodSource("queryMethods")
  void failedQueryDoesNotDeadlockWhenClearFailedResolutionsRaces(
      String methodName, Function<LazyClientDataTypeTree, Object> query, Object expectedResult)
      throws Exception {

    var browseEntered = new CountDownLatch(1);
    var allowBrowseReturn = new CountDownLatch(1);
    var clearStarted = new CountDownLatch(1);
    var browseCalls = new AtomicInteger();

    OpcUaClient client = mockFailingClient(browseEntered, allowBrowseReturn, browseCalls);
    var tree = new LazyClientDataTypeTree(client);

    var lookupResult = new CompletableFuture<Object>();
    Thread lookupThread =
        new Thread(
            () -> {
              try {
                lookupResult.complete(query.apply(tree));
              } catch (Throwable t) {
                lookupResult.completeExceptionally(t);
              }
            },
            "lookup-" + methodName);
    lookupThread.setDaemon(true);
    lookupThread.start();

    assertTrue(
        browseEntered.await(5, TimeUnit.SECONDS),
        () -> methodName + " should trigger lazy browse for unresolved NodeId");

    // Start clearFailedResolutions() while lookupThread still holds the write lock so it queues
    // directly behind lookupThread and runs before lookupThread's subsequent readLock() acquire.
    var clearDone = new CompletableFuture<Void>();
    Thread clearThread =
        new Thread(
            () -> {
              try {
                clearStarted.countDown();
                tree.clearFailedResolutions();
                clearDone.complete(null);
              } catch (Throwable t) {
                clearDone.completeExceptionally(t);
              }
            },
            "clear-" + methodName);
    clearThread.setDaemon(true);
    clearThread.start();

    assertTrue(clearStarted.await(5, TimeUnit.SECONDS));
    assertTrue(
        awaitWaiting(clearThread, TimeUnit.SECONDS.toNanos(5)),
        "clearFailedResolutions() thread should queue on the write lock");

    allowBrowseReturn.countDown();

    Object actualResult = lookupResult.get(5, TimeUnit.SECONDS);
    clearDone.get(5, TimeUnit.SECONDS);

    assertEquals(expectedResult, actualResult, () -> methodName + " returned unexpected result");
    assertEquals(
        1,
        browseCalls.get(),
        () -> methodName + " should resolve once without re-entering ensureResolved()");

    // Verify the read lock is not wedged behind a stuck writer.
    var readerResult = new CompletableFuture<Boolean>();
    Thread readerThread =
        new Thread(() -> readerResult.complete(tree.isResolved(NodeIds.BaseDataType)), "reader");
    readerThread.setDaemon(true);
    readerThread.start();

    assertTrue(readerResult.get(5, TimeUnit.SECONDS));
  }

  static Stream<Arguments> queryMethods() {
    return Stream.of(
        Arguments.of(
            "getDataType",
            (Function<LazyClientDataTypeTree, Object>) tree -> tree.getDataType(MISSING_TYPE_ID),
            null),
        Arguments.of(
            "getBinaryEncodingId",
            (Function<LazyClientDataTypeTree, Object>)
                tree -> tree.getBinaryEncodingId(MISSING_TYPE_ID),
            null),
        Arguments.of(
            "getXmlEncodingId",
            (Function<LazyClientDataTypeTree, Object>)
                tree -> tree.getXmlEncodingId(MISSING_TYPE_ID),
            null),
        Arguments.of(
            "getJsonEncodingId",
            (Function<LazyClientDataTypeTree, Object>)
                tree -> tree.getJsonEncodingId(MISSING_TYPE_ID),
            null),
        Arguments.of(
            "getDataTypeDefinition",
            (Function<LazyClientDataTypeTree, Object>)
                tree -> tree.getDataTypeDefinition(MISSING_TYPE_ID),
            null),
        Arguments.of(
            "isEnumType",
            (Function<LazyClientDataTypeTree, Object>) tree -> tree.isEnumType(MISSING_TYPE_ID),
            false),
        Arguments.of(
            "isStructType",
            (Function<LazyClientDataTypeTree, Object>) tree -> tree.isStructType(MISSING_TYPE_ID),
            false),
        Arguments.of(
            "getType",
            (Function<LazyClientDataTypeTree, Object>) tree -> tree.getType(MISSING_TYPE_ID),
            null),
        Arguments.of(
            "containsType",
            (Function<LazyClientDataTypeTree, Object>) tree -> tree.containsType(MISSING_TYPE_ID),
            false),
        Arguments.of(
            "getTreeNode",
            (Function<LazyClientDataTypeTree, Object>) tree -> tree.getTreeNode(MISSING_TYPE_ID),
            null),
        Arguments.of(
            "isSubtypeOf",
            (Function<LazyClientDataTypeTree, Object>)
                tree -> tree.isSubtypeOf(MISSING_TYPE_ID, NodeIds.Structure),
            false),
        Arguments.of(
            "getBackingClass",
            (Function<LazyClientDataTypeTree, Object>)
                tree -> tree.getBackingClass(MISSING_TYPE_ID),
            Object.class),
        Arguments.of(
            "getBuiltinType",
            (Function<LazyClientDataTypeTree, Object>) tree -> tree.getBuiltinType(MISSING_TYPE_ID),
            OpcUaDataType.Variant),
        Arguments.of(
            "isAssignable",
            (Function<LazyClientDataTypeTree, Object>)
                tree -> tree.isAssignable(MISSING_TYPE_ID, String.class),
            true));
  }

  // Walking parents directly under one read lock must preserve DataTypeTree's backing-class and
  // builtin-type semantics for multi-hop hierarchies without re-entering overridden methods.
  @Test
  void getBackingClassAndBuiltinTypeWalkParentsWithoutVirtualReentry() {
    var client = mock(OpcUaClient.class);

    NodeId directCustom = new NodeId(1, "DirectCustom");
    NodeId customString = new NodeId(1, "CustomString");
    NodeId subCustomString = new NodeId(1, "SubCustomString");
    NodeId customEnum = new NodeId(1, "CustomEnum");
    NodeId subCustomEnum = new NodeId(1, "SubCustomEnum");
    NodeId customNumber = new NodeId(1, "CustomNumber");
    NodeId customInteger = new NodeId(1, "CustomInteger");
    NodeId customUInteger = new NodeId(1, "CustomUInteger");
    NodeId customStruct = new NodeId(1, "CustomStruct");
    NodeId subCustomStruct = new NodeId(1, "SubCustomStruct");

    Tree<DataType> root = new Tree<>(null, dataType(NodeIds.BaseDataType, "BaseDataType", true));
    root.addChild(dataType(directCustom, "DirectCustom", false));

    Tree<DataType> stringNode = root.addChild(dataType(NodeIds.String, "String", false));
    stringNode
        .addChild(dataType(customString, "CustomString", false))
        .addChild(dataType(subCustomString, "SubCustomString", false));

    Tree<DataType> enumNode = root.addChild(dataType(NodeIds.Enumeration, "Enumeration", true));
    enumNode
        .addChild(dataType(customEnum, "CustomEnum", false))
        .addChild(dataType(subCustomEnum, "SubCustomEnum", false));

    Tree<DataType> numberNode = root.addChild(dataType(NodeIds.Number, "Number", true));
    numberNode.addChild(dataType(customNumber, "CustomNumber", false));
    numberNode
        .addChild(dataType(NodeIds.Integer, "Integer", true))
        .addChild(dataType(customInteger, "CustomInteger", false));
    numberNode
        .addChild(dataType(NodeIds.UInteger, "UInteger", true))
        .addChild(dataType(customUInteger, "CustomUInteger", false));

    Tree<DataType> structNode = root.addChild(dataType(NodeIds.Structure, "Structure", true));
    structNode
        .addChild(dataType(customStruct, "CustomStruct", false))
        .addChild(dataType(subCustomStruct, "SubCustomStruct", false));

    var backingClassCalls = new AtomicInteger();
    var builtinTypeCalls = new AtomicInteger();

    var tree =
        new LazyClientDataTypeTree(client, root) {
          @Override
          public Class<?> getBackingClass(NodeId dataTypeId) {
            backingClassCalls.incrementAndGet();
            return super.getBackingClass(dataTypeId);
          }

          @Override
          public OpcUaDataType getBuiltinType(NodeId dataTypeId) {
            builtinTypeCalls.incrementAndGet();
            return super.getBuiltinType(dataTypeId);
          }
        };

    assertBackingClass(tree, backingClassCalls, NodeIds.BaseDataType, Variant.class);
    assertBackingClass(tree, backingClassCalls, directCustom, Variant.class);
    assertBackingClass(tree, backingClassCalls, subCustomString, String.class);
    assertBackingClass(tree, backingClassCalls, subCustomEnum, Integer.class);
    assertBackingClass(tree, backingClassCalls, customNumber, Number.class);
    assertBackingClass(tree, backingClassCalls, customInteger, Number.class);
    assertBackingClass(tree, backingClassCalls, customUInteger, UNumber.class);
    assertBackingClass(tree, backingClassCalls, subCustomStruct, ExtensionObject.class);

    assertBuiltinType(tree, builtinTypeCalls, NodeIds.BaseDataType, OpcUaDataType.Variant);
    assertBuiltinType(tree, builtinTypeCalls, directCustom, OpcUaDataType.Variant);
    assertBuiltinType(tree, builtinTypeCalls, subCustomString, OpcUaDataType.String);
    assertBuiltinType(tree, builtinTypeCalls, subCustomEnum, OpcUaDataType.Variant);
    assertBuiltinType(tree, builtinTypeCalls, customNumber, OpcUaDataType.Variant);
    assertBuiltinType(tree, builtinTypeCalls, customInteger, OpcUaDataType.Variant);
    assertBuiltinType(tree, builtinTypeCalls, customUInteger, OpcUaDataType.Variant);
    assertBuiltinType(tree, builtinTypeCalls, subCustomStruct, OpcUaDataType.ExtensionObject);

    assertFalse(tree.isEnumType(subCustomStruct));
    assertTrue(tree.isEnumType(subCustomEnum));
    assertTrue(tree.isStructType(subCustomStruct));
    assertFalse(tree.isStructType(subCustomEnum));

    verifyNoInteractions(client);
  }

  private static void assertBackingClass(
      LazyClientDataTypeTree tree, AtomicInteger calls, NodeId dataTypeId, Class<?> expected) {

    calls.set(0);
    assertEquals(expected, tree.getBackingClass(dataTypeId));
    assertEquals(
        1,
        calls.get(),
        () -> "getBackingClass(" + dataTypeId + ") must not re-enter virtually while walking");
  }

  private static void assertBuiltinType(
      LazyClientDataTypeTree tree, AtomicInteger calls, NodeId dataTypeId, OpcUaDataType expected) {

    calls.set(0);
    assertEquals(expected, tree.getBuiltinType(dataTypeId));
    assertEquals(
        1,
        calls.get(),
        () -> "getBuiltinType(" + dataTypeId + ") must not re-enter virtually while walking");
  }

  private static DataType dataType(NodeId nodeId, String browseName, boolean isAbstract) {
    return new ClientDataType(
        new QualifiedName(nodeId.getNamespaceIndex(), browseName),
        nodeId,
        null,
        null,
        null,
        null,
        isAbstract);
  }

  private static OpcUaClient mockFailingClient(
      CountDownLatch browseEntered, CountDownLatch allowBrowseReturn, AtomicInteger browseCalls)
      throws UaException {

    var client = mock(OpcUaClient.class);
    var session = mock(OpcUaSession.class);

    when(session.getSessionId()).thenReturn(new NodeId(0, 1));
    when(client.getSession()).thenReturn(session);
    when(client.readNamespaceTable()).thenReturn(new NamespaceTable());
    when(client.getOperationLimits()).thenReturn(new OperationLimits(Map.of()));
    when(client.browse(any(BrowseDescription.class)))
        .thenAnswer(
            invocation -> {
              if (browseCalls.incrementAndGet() == 1) {
                browseEntered.countDown();
                if (!allowBrowseReturn.await(5, TimeUnit.SECONDS)) {
                  throw new AssertionError("Timed out waiting to resume browse");
                }
              }
              return new BrowseResult(
                  new StatusCode(StatusCodes.Bad_NodeIdUnknown), ByteString.NULL_VALUE, null);
            });

    return client;
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
}
