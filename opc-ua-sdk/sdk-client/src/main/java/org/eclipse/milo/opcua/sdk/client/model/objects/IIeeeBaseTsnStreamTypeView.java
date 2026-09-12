/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.model.objects;

import com.digitalpetri.opcua.uanodeset.runtime.client.ClientMembers;
import com.digitalpetri.opcua.uanodeset.runtime.client.ClientObjectView;
import com.digitalpetri.opcua.uanodeset.runtime.client.ClientViewType;
import com.digitalpetri.opcua.uanodeset.runtime.client.ClientViews;
import com.digitalpetri.opcua.uanodeset.runtime.members.MemberDeclaration;
import com.digitalpetri.opcua.uanodeset.runtime.values.ValueChecks;
import com.digitalpetri.opcua.uanodeset.runtime.views.ViewFutures;
import java.lang.reflect.Array;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.function.Supplier;
import org.eclipse.milo.opcua.sdk.client.model.variables.BaseDataVariableType;
import org.eclipse.milo.opcua.sdk.client.model.variables.BaseDataVariableTypeView;
import org.eclipse.milo.opcua.sdk.client.nodes.UaObjectNode;
import org.eclipse.milo.opcua.sdk.client.nodes.UaVariableNode;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Matrix;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TsnStreamState;
import org.eclipse.milo.opcua.stack.core.util.ArrayUtil;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * https://reference.opcfoundation.org/v105/Core/docs/Part22/5.2.7
 *
 * <p>Selected <code>IIeeeBaseTsnStreamType</code> contract over one retained backing node. Obtain
 * this view from a ClientViews context using {@link #TYPE}; the token itself does not prove
 * membership. Access after context close fails with IllegalStateException.
 */
@NullMarked
public final class IIeeeBaseTsnStreamTypeView extends ClientObjectView
    implements IIeeeBaseTsnStreamType {
  /** Selected UA/Java contract and context-owned factory. */
  public static final ClientViewType<IIeeeBaseTsnStreamType> TYPE =
      ClientViewType.of(
          ExpandedNodeId.parse("i=24173"),
          IIeeeBaseTsnStreamType.class,
          IIeeeBaseTsnStreamTypeView::new);

  private static final Map<ExpandedNodeId, Set<Integer>> KNOWN_ENUMS =
      Map.ofEntries(
          Map.entry(ExpandedNodeId.parse("i=120"), Set.of(1, 2, 3)),
          Map.entry(ExpandedNodeId.parse("i=32417"), Set.of(0, 1, 2, 3)),
          Map.entry(ExpandedNodeId.parse("i=11939"), Set.of(1, 2, 4, 8)),
          Map.entry(ExpandedNodeId.parse("i=15632"), Set.of(1, 2, 3, 4, 5, 6, 7, 8, 9)),
          Map.entry(ExpandedNodeId.parse("i=32436"), Set.of(0, 1, 2)),
          Map.entry(ExpandedNodeId.parse("i=12552"), Set.of(0, 1, 2, 4, 8, 15)),
          Map.entry(ExpandedNodeId.parse("i=15539"), Set.of(1, 2, 3, 4)),
          Map.entry(ExpandedNodeId.parse("i=14647"), Set.of(0, 1, 2, 3, 4)),
          Map.entry(ExpandedNodeId.parse("i=18595"), Set.of(0, 1, 2)),
          Map.entry(ExpandedNodeId.parse("i=15874"), Set.of(0, 1, 2)),
          Map.entry(ExpandedNodeId.parse("i=20408"), Set.of(0, 1, 2)),
          Map.entry(ExpandedNodeId.parse("i=15008"), Set.of(0, 1, 2, 3, 4)),
          Map.entry(ExpandedNodeId.parse("i=19723"), Set.of(0, 1, 2, 3, 4)),
          Map.entry(ExpandedNodeId.parse("i=19730"), Set.of(0, 1)),
          Map.entry(ExpandedNodeId.parse("i=24210"), Set.of(0, 1, 2)),
          Map.entry(ExpandedNodeId.parse("i=24212"), Set.of(0, 1, 2)),
          Map.entry(ExpandedNodeId.parse("i=24214"), Set.of(0, 1, 2, 3, 4, 5, 6)),
          Map.entry(ExpandedNodeId.parse("i=24216"), Set.of(0, 1, 2, 3, 4)),
          Map.entry(
              ExpandedNodeId.parse("i=24218"),
              Set.of(
                  0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22,
                  23, 24, 25)),
          Map.entry(ExpandedNodeId.parse("i=24220"), Set.of(0, 1, 2, 3, 4)),
          Map.entry(ExpandedNodeId.parse("i=24222"), Set.of(0, 1, 2)),
          Map.entry(ExpandedNodeId.parse("i=24224"), Set.of(0, 1, 2, 3)),
          Map.entry(ExpandedNodeId.parse("i=18947"), Set.of(1, 2, 3, 4, 5, 6, 7)),
          Map.entry(ExpandedNodeId.parse("i=18949"), Set.of(1, 2, 3, 4, 5, 6, 7)),
          Map.entry(ExpandedNodeId.parse("i=18951"), Set.of(0, 1, 2, 3)),
          Map.entry(ExpandedNodeId.parse("i=256"), Set.of(0, 1, 2, 3)),
          Map.entry(ExpandedNodeId.parse("i=257"), Set.of(0, 1, 2, 4, 8, 16, 32, 64, 128)),
          Map.entry(ExpandedNodeId.parse("i=98"), Set.of(0, 1, 2, 3, 4)),
          Map.entry(ExpandedNodeId.parse("i=307"), Set.of(0, 1, 2, 3)),
          Map.entry(ExpandedNodeId.parse("i=302"), Set.of(0, 1, 2, 3)),
          Map.entry(ExpandedNodeId.parse("i=303"), Set.of(0, 1, 2, 3)),
          Map.entry(ExpandedNodeId.parse("i=315"), Set.of(0, 1)),
          Map.entry(
              ExpandedNodeId.parse("i=348"),
              Set.of(
                  0, 1, 2, 4, 8, 16, 32, 64, 128, 256, 512, 1024, 2048, 4096, 8192, 16384, 32768,
                  65536, 131072, 262144, 524288, 1048576, 2097152, 4194304, 8388608, 16777216,
                  26501220, 26501348, 26501356, 26503268, 26537060, 26571383, 26632548, 28600438,
                  33554431)),
          Map.entry(
              ExpandedNodeId.parse("i=576"),
              Set.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17)),
          Map.entry(ExpandedNodeId.parse("i=11234"), Set.of(1, 2, 3, 4)),
          Map.entry(ExpandedNodeId.parse("i=11293"), Set.of(1, 2, 3, 4)),
          Map.entry(ExpandedNodeId.parse("i=851"), Set.of(0, 1, 2, 3, 4, 5)),
          Map.entry(ExpandedNodeId.parse("i=852"), Set.of(0, 1, 2, 3, 4, 5, 6, 7)),
          Map.entry(ExpandedNodeId.parse("i=12077"), Set.of(0, 1, 2)),
          Map.entry(ExpandedNodeId.parse("i=890"), Set.of(0, 1, 2, 3, 4)));

  private IIeeeBaseTsnStreamTypeView(ClientViews views, UaObjectNode node) {
    super(views, node);
  }

  private CompletableFuture<UaVariableNode> viewMember0Async() {
    try {
      views.checkOpen();
      var memberRequest =
          ((Supplier<CompletableFuture<UaVariableNode>>)
                  () -> {
                    return ClientMembers.lookup(
                        client,
                        getNodeId(),
                        UaVariableNode.class,
                        new MemberDeclaration(
                            "http://opcfoundation.org/UA/",
                            "StreamId",
                            ExpandedNodeId.parse("i=47"),
                            true,
                            NodeClass.Variable,
                            false,
                            "http://opcfoundation.org/UA/:StreamId (declaration i=24174, owner"
                                + " i=24173)"));
                  })
              .get();
      return ViewFutures.compose(
          memberRequest,
          found ->
              found == null
                  ? CompletableFuture.completedFuture(null)
                  : views.variableNodeAsync(found.getNodeId()));
    } catch (RuntimeException failure) {
      return CompletableFuture.failedFuture(failure);
    }
  }

  private UaVariableNode viewMember0() throws UaException {
    return ViewFutures.await(viewMember0Async());
  }

  private CompletableFuture<UaVariableNode> viewMember1Async() {
    try {
      views.checkOpen();
      var memberRequest =
          ((Supplier<CompletableFuture<UaVariableNode>>)
                  () -> {
                    return ClientMembers.lookup(
                        client,
                        getNodeId(),
                        UaVariableNode.class,
                        new MemberDeclaration(
                            "http://opcfoundation.org/UA/",
                            "StreamName",
                            ExpandedNodeId.parse("i=47"),
                            true,
                            NodeClass.Variable,
                            false,
                            "http://opcfoundation.org/UA/:StreamName (declaration i=24175, owner"
                                + " i=24173)"));
                  })
              .get();
      return ViewFutures.compose(
          memberRequest,
          found ->
              found == null
                  ? CompletableFuture.completedFuture(null)
                  : views.variableNodeAsync(found.getNodeId()));
    } catch (RuntimeException failure) {
      return CompletableFuture.failedFuture(failure);
    }
  }

  private UaVariableNode viewMember1() throws UaException {
    return ViewFutures.await(viewMember1Async());
  }

  private CompletableFuture<UaVariableNode> viewMember2Async() {
    try {
      views.checkOpen();
      var memberRequest =
          ((Supplier<CompletableFuture<UaVariableNode>>)
                  () -> {
                    return ClientMembers.lookup(
                        client,
                        getNodeId(),
                        UaVariableNode.class,
                        new MemberDeclaration(
                            "http://opcfoundation.org/UA/",
                            "State",
                            ExpandedNodeId.parse("i=47"),
                            true,
                            NodeClass.Variable,
                            false,
                            "http://opcfoundation.org/UA/:State (declaration i=24176, owner"
                                + " i=24173)"));
                  })
              .get();
      return ViewFutures.compose(
          memberRequest,
          found ->
              found == null
                  ? CompletableFuture.completedFuture(null)
                  : views.variableNodeAsync(found.getNodeId()));
    } catch (RuntimeException failure) {
      return CompletableFuture.failedFuture(failure);
    }
  }

  private UaVariableNode viewMember2() throws UaException {
    return ViewFutures.await(viewMember2Async());
  }

  private CompletableFuture<@Nullable UaVariableNode> viewMember3Async() {
    try {
      views.checkOpen();
      var memberRequest =
          ((Supplier<CompletableFuture<@Nullable UaVariableNode>>)
                  () -> {
                    return ClientMembers.lookup(
                        client,
                        getNodeId(),
                        UaVariableNode.class,
                        new MemberDeclaration(
                            "http://opcfoundation.org/UA/",
                            "AccumulatedLatency",
                            ExpandedNodeId.parse("i=47"),
                            true,
                            NodeClass.Variable,
                            true,
                            "http://opcfoundation.org/UA/:AccumulatedLatency (declaration i=24177,"
                                + " owner i=24173)"));
                  })
              .get();
      return ViewFutures.compose(
          memberRequest,
          found ->
              found == null
                  ? CompletableFuture.completedFuture(null)
                  : views.variableNodeAsync(found.getNodeId()));
    } catch (RuntimeException failure) {
      return CompletableFuture.failedFuture(failure);
    }
  }

  private @Nullable UaVariableNode viewMember3() throws UaException {
    return ViewFutures.await(viewMember3Async());
  }

  private CompletableFuture<@Nullable UaVariableNode> viewMember4Async() {
    try {
      views.checkOpen();
      var memberRequest =
          ((Supplier<CompletableFuture<@Nullable UaVariableNode>>)
                  () -> {
                    return ClientMembers.lookup(
                        client,
                        getNodeId(),
                        UaVariableNode.class,
                        new MemberDeclaration(
                            "http://opcfoundation.org/UA/",
                            "SrClassId",
                            ExpandedNodeId.parse("i=47"),
                            true,
                            NodeClass.Variable,
                            true,
                            "http://opcfoundation.org/UA/:SrClassId (declaration i=24178, owner"
                                + " i=24173)"));
                  })
              .get();
      return ViewFutures.compose(
          memberRequest,
          found ->
              found == null
                  ? CompletableFuture.completedFuture(null)
                  : views.variableNodeAsync(found.getNodeId()));
    } catch (RuntimeException failure) {
      return CompletableFuture.failedFuture(failure);
    }
  }

  private @Nullable UaVariableNode viewMember4() throws UaException {
    return ViewFutures.await(viewMember4Async());
  }

  @Override
  public BaseDataVariableType getStreamIdNode() throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    return ViewFutures.await(getStreamIdNodeAsync());
  }

  private CompletableFuture<? extends BaseDataVariableType> getStreamIdNodeAsyncImplementation() {
    return ViewFutures.compose(
        viewMember0Async(),
        child ->
            child == null
                ? CompletableFuture.completedFuture(null)
                : views.wrapVariableAsync(child.getNodeId(), BaseDataVariableTypeView.TYPE));
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableType> getStreamIdNodeAsync() {
    try {
      this.views.checkOpen();
      return this.views.ownRequest(this.getStreamIdNodeAsyncImplementation());
    } catch (RuntimeException viewFailure) {
      return CompletableFuture.failedFuture(viewFailure);
    }
  }

  @Override
  public @Nullable UByte @Nullable [] getStreamId() throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    var child = viewMember0();
    if (child == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:StreamId (declaration i=24174, owner i=24173)"
              + " on "
              + getNodeId());
    }
    @Nullable UByte @Nullable [] converted;
    {
      Object rawValue = child.getValue().getValue().getValue();
      if (rawValue instanceof Matrix matrix && matrix.isNull()) {
        rawValue = null;
      }
      if (rawValue != null) {
        int actualRank =
            rawValue instanceof Matrix matrix
                ? matrix.getValueRank()
                : ArrayUtil.getValueRank(rawValue);
        Object rankElements = rawValue instanceof Matrix matrix ? matrix.getElements() : rawValue;
        if (!(actualRank == 1
            || (!(rawValue instanceof Matrix)
                && actualRank == 1
                && Array.getLength(rankElements) == 0))) {
          throw new UaException(
              StatusCodes.Bad_TypeMismatch,
              "http://opcfoundation.org/UA/:StreamId (declaration i=24174, owner i=24173)");
        }
        if (rawValue instanceof Matrix matrix) {
          if (actualRank < 2
              || rankElements == null
              || !rankElements.getClass().isArray()
              || ArrayUtil.getValueRank(rankElements) != 1) {
            throw new UaException(
                StatusCodes.Bad_TypeMismatch,
                "http://opcfoundation.org/UA/:StreamId (declaration i=24174, owner i=24173)");
          }
          long elementCount = 1;
          for (int dimension : matrix.getDimensions()) {
            if (dimension < 0 || elementCount > Integer.MAX_VALUE) {
              throw new UaException(
                  StatusCodes.Bad_TypeMismatch,
                  "http://opcfoundation.org/UA/:StreamId (declaration i=24174, owner i=24173)");
            }
            elementCount *= dimension;
          }
          if (elementCount != Array.getLength(rankElements)) {
            throw new UaException(
                StatusCodes.Bad_TypeMismatch,
                "http://opcfoundation.org/UA/:StreamId (declaration i=24174, owner i=24173)");
          }
        } else if (actualRank > 1) {
          throw new UaException(
              StatusCodes.Bad_TypeMismatch,
              "http://opcfoundation.org/UA/:StreamId (declaration i=24174, owner i=24173): use"
                  + " Matrix for multiple dimensions");
        }
      }
      if (rawValue == null) {
        converted = null;
      } else {
        Object arrayElements = rawValue instanceof Matrix matrix ? matrix.getElements() : rawValue;
        if (!arrayElements.getClass().isArray()) {
          throw new UaException(
              StatusCodes.Bad_TypeMismatch,
              "http://opcfoundation.org/UA/:StreamId (declaration i=24174, owner i=24173)");
        }
        UByte[] values = new UByte[Array.getLength(arrayElements)];
        for (int index = 0; index < values.length; index++) {
          Object element = Array.get(arrayElements, index);
          if (element != null && !(element instanceof UByte)) {
            throw new UaException(
                StatusCodes.Bad_TypeMismatch,
                "http://opcfoundation.org/UA/:StreamId (declaration i=24174, owner i=24173)");
          }
          values[index] = (UByte) element;
        }
        converted = values;
      }
    }
    return converted;
  }

  @Override
  public void setStreamId(@Nullable UByte @Nullable [] value) throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    var child = viewMember0();
    if (child == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:StreamId (declaration i=24174, owner i=24173)"
              + " on "
              + getNodeId());
    }
    var writeContext = views.localWriteContext(child.getNodeId(), value, KNOWN_ENUMS);
    Variant encoded =
        ValueChecks.validate(
            writeContext, value, ExpandedNodeId.parse("i=3"), 1, new long[] {8L}, null, false);
    views.checkOpen();
    child.setValue(encoded);
  }

  @Override
  public @Nullable UByte @Nullable [] readStreamId() throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    return ViewFutures.await(readStreamIdAsync());
  }

  private CompletableFuture<? extends @Nullable UByte @Nullable []>
      readStreamIdAsyncImplementation() {
    return ViewFutures.map(
        ViewFutures.compose(
            viewMember0Async(),
            child -> {
              if (child == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:StreamId (declaration i=24174, owner i=24173)"
                            + " on "
                            + getNodeId()));
              }
              views.checkOpen();
              return child.readAttributeAsync(AttributeId.Value).thenApply(response -> response);
            }),
        value -> {
          views.checkOpen();
          if (value == null || value.getStatusCode() == null) {
            throw new CompletionException(new UaException(StatusCodes.Bad_UnexpectedError));
          }
          if (!value.getStatusCode().isGood()) {
            throw new CompletionException(new UaException(value.getStatusCode()));
          }
          try {
            @Nullable UByte @Nullable [] converted;
            {
              Object rawValue = value.getValue().getValue();
              if (rawValue instanceof Matrix matrix && matrix.isNull()) {
                rawValue = null;
              }
              if (rawValue != null) {
                int actualRank =
                    rawValue instanceof Matrix matrix
                        ? matrix.getValueRank()
                        : ArrayUtil.getValueRank(rawValue);
                Object rankElements =
                    rawValue instanceof Matrix matrix ? matrix.getElements() : rawValue;
                if (!(actualRank == 1
                    || (!(rawValue instanceof Matrix)
                        && actualRank == 1
                        && Array.getLength(rankElements) == 0))) {
                  throw new UaException(
                      StatusCodes.Bad_TypeMismatch,
                      "http://opcfoundation.org/UA/:StreamId (declaration i=24174, owner i=24173)");
                }
                if (rawValue instanceof Matrix matrix) {
                  if (actualRank < 2
                      || rankElements == null
                      || !rankElements.getClass().isArray()
                      || ArrayUtil.getValueRank(rankElements) != 1) {
                    throw new UaException(
                        StatusCodes.Bad_TypeMismatch,
                        "http://opcfoundation.org/UA/:StreamId (declaration i=24174, owner"
                            + " i=24173)");
                  }
                  long elementCount = 1;
                  for (int dimension : matrix.getDimensions()) {
                    if (dimension < 0 || elementCount > Integer.MAX_VALUE) {
                      throw new UaException(
                          StatusCodes.Bad_TypeMismatch,
                          "http://opcfoundation.org/UA/:StreamId (declaration i=24174, owner"
                              + " i=24173)");
                    }
                    elementCount *= dimension;
                  }
                  if (elementCount != Array.getLength(rankElements)) {
                    throw new UaException(
                        StatusCodes.Bad_TypeMismatch,
                        "http://opcfoundation.org/UA/:StreamId (declaration i=24174, owner"
                            + " i=24173)");
                  }
                } else if (actualRank > 1) {
                  throw new UaException(
                      StatusCodes.Bad_TypeMismatch,
                      "http://opcfoundation.org/UA/:StreamId (declaration i=24174, owner i=24173):"
                          + " use Matrix for multiple dimensions");
                }
              }
              if (rawValue == null) {
                converted = null;
              } else {
                Object arrayElements =
                    rawValue instanceof Matrix matrix ? matrix.getElements() : rawValue;
                if (!arrayElements.getClass().isArray()) {
                  throw new UaException(
                      StatusCodes.Bad_TypeMismatch,
                      "http://opcfoundation.org/UA/:StreamId (declaration i=24174, owner i=24173)");
                }
                UByte[] values = new UByte[Array.getLength(arrayElements)];
                for (int index = 0; index < values.length; index++) {
                  Object element = Array.get(arrayElements, index);
                  if (element != null && !(element instanceof UByte)) {
                    throw new UaException(
                        StatusCodes.Bad_TypeMismatch,
                        "http://opcfoundation.org/UA/:StreamId (declaration i=24174, owner"
                            + " i=24173)");
                  }
                  values[index] = (UByte) element;
                }
                converted = values;
              }
            }
            return converted;
          } catch (UaException failure) {
            throw new CompletionException(failure);
          }
        });
  }

  @Override
  public CompletableFuture<? extends @Nullable UByte @Nullable []> readStreamIdAsync() {
    try {
      this.views.checkOpen();
      return this.views.ownRequest(this.readStreamIdAsyncImplementation());
    } catch (RuntimeException viewFailure) {
      return CompletableFuture.failedFuture(viewFailure);
    }
  }

  @Override
  public void writeStreamId(@Nullable UByte @Nullable [] value) throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    views.checkOpen();
    StatusCode status = ViewFutures.await(writeStreamIdAsync(value));
    if (status == null) {
      throw new UaException(StatusCodes.Bad_UnexpectedError);
    }
    if (!status.isGood()) {
      throw new UaException(status);
    }
  }

  private CompletableFuture<StatusCode> writeStreamIdAsyncImplementation(
      @Nullable UByte @Nullable [] value) {
    return ViewFutures.compose(
        viewMember0Async(),
        child -> {
          if (child == null) {
            throw new CompletionException(
                new UaException(
                    StatusCodes.Bad_NotFound,
                    "http://opcfoundation.org/UA/:StreamId (declaration i=24174, owner i=24173)"
                        + " on "
                        + getNodeId()));
          }
          return ViewFutures.compose(
              views.readWriteContextAsync(child.getNodeId(), value, KNOWN_ENUMS),
              writeContext -> {
                try {
                  Variant encoded =
                      ValueChecks.validate(
                          writeContext,
                          value,
                          ExpandedNodeId.parse("i=3"),
                          1,
                          new long[] {8L},
                          null,
                          true);
                  views.checkOpen();
                  return child
                      .writeAttributeAsync(AttributeId.Value, DataValue.valueOnly(encoded))
                      .thenApply(response -> response);
                } catch (Exception failure) {
                  return CompletableFuture.failedFuture(failure);
                }
              });
        });
  }

  @Override
  public CompletableFuture<StatusCode> writeStreamIdAsync(@Nullable UByte @Nullable [] value) {
    try {
      this.views.checkOpen();
      return this.views.ownRequest(this.writeStreamIdAsyncImplementation(value));
    } catch (RuntimeException viewFailure) {
      return CompletableFuture.failedFuture(viewFailure);
    }
  }

  @Override
  public BaseDataVariableType getStreamNameNode() throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    return ViewFutures.await(getStreamNameNodeAsync());
  }

  private CompletableFuture<? extends BaseDataVariableType> getStreamNameNodeAsyncImplementation() {
    return ViewFutures.compose(
        viewMember1Async(),
        child ->
            child == null
                ? CompletableFuture.completedFuture(null)
                : views.wrapVariableAsync(child.getNodeId(), BaseDataVariableTypeView.TYPE));
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableType> getStreamNameNodeAsync() {
    try {
      this.views.checkOpen();
      return this.views.ownRequest(this.getStreamNameNodeAsyncImplementation());
    } catch (RuntimeException viewFailure) {
      return CompletableFuture.failedFuture(viewFailure);
    }
  }

  @Override
  public @Nullable String getStreamName() throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    var child = viewMember1();
    if (child == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:StreamName (declaration i=24175, owner i=24173)"
              + " on "
              + getNodeId());
    }
    @Nullable String converted;
    {
      Object rawValue = child.getValue().getValue().getValue();
      if (rawValue instanceof Matrix matrix && matrix.isNull()) {
        rawValue = null;
      }
      if (rawValue != null) {
        int actualRank =
            rawValue instanceof Matrix matrix
                ? matrix.getValueRank()
                : ArrayUtil.getValueRank(rawValue);
        Object rankElements = rawValue instanceof Matrix matrix ? matrix.getElements() : rawValue;
        if (!(actualRank == -1)) {
          throw new UaException(
              StatusCodes.Bad_TypeMismatch,
              "http://opcfoundation.org/UA/:StreamName (declaration i=24175, owner i=24173)");
        }
        if (rawValue instanceof Matrix matrix) {
          if (actualRank < 2
              || rankElements == null
              || !rankElements.getClass().isArray()
              || ArrayUtil.getValueRank(rankElements) != 1) {
            throw new UaException(
                StatusCodes.Bad_TypeMismatch,
                "http://opcfoundation.org/UA/:StreamName (declaration i=24175, owner i=24173)");
          }
          long elementCount = 1;
          for (int dimension : matrix.getDimensions()) {
            if (dimension < 0 || elementCount > Integer.MAX_VALUE) {
              throw new UaException(
                  StatusCodes.Bad_TypeMismatch,
                  "http://opcfoundation.org/UA/:StreamName (declaration i=24175, owner i=24173)");
            }
            elementCount *= dimension;
          }
          if (elementCount != Array.getLength(rankElements)) {
            throw new UaException(
                StatusCodes.Bad_TypeMismatch,
                "http://opcfoundation.org/UA/:StreamName (declaration i=24175, owner i=24173)");
          }
        } else if (actualRank > 1) {
          throw new UaException(
              StatusCodes.Bad_TypeMismatch,
              "http://opcfoundation.org/UA/:StreamName (declaration i=24175, owner i=24173): use"
                  + " Matrix for multiple dimensions");
        }
      }
      Object element = rawValue;
      if (element != null && !(element instanceof String)) {
        throw new UaException(
            StatusCodes.Bad_TypeMismatch,
            "http://opcfoundation.org/UA/:StreamName (declaration i=24175, owner i=24173)");
      }
      converted = (String) element;
    }
    return converted;
  }

  @Override
  public void setStreamName(@Nullable String value) throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    var child = viewMember1();
    if (child == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:StreamName (declaration i=24175, owner i=24173)"
              + " on "
              + getNodeId());
    }
    var writeContext = views.localWriteContext(child.getNodeId(), value, KNOWN_ENUMS);
    Variant encoded =
        ValueChecks.validate(
            writeContext, value, ExpandedNodeId.parse("i=12"), -1, new long[] {}, null, false);
    views.checkOpen();
    child.setValue(encoded);
  }

  @Override
  public @Nullable String readStreamName() throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    return ViewFutures.await(readStreamNameAsync());
  }

  private CompletableFuture<? extends @Nullable String> readStreamNameAsyncImplementation() {
    return ViewFutures.map(
        ViewFutures.compose(
            viewMember1Async(),
            child -> {
              if (child == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:StreamName (declaration i=24175, owner"
                            + " i=24173) on "
                            + getNodeId()));
              }
              views.checkOpen();
              return child.readAttributeAsync(AttributeId.Value).thenApply(response -> response);
            }),
        value -> {
          views.checkOpen();
          if (value == null || value.getStatusCode() == null) {
            throw new CompletionException(new UaException(StatusCodes.Bad_UnexpectedError));
          }
          if (!value.getStatusCode().isGood()) {
            throw new CompletionException(new UaException(value.getStatusCode()));
          }
          try {
            @Nullable String converted;
            {
              Object rawValue = value.getValue().getValue();
              if (rawValue instanceof Matrix matrix && matrix.isNull()) {
                rawValue = null;
              }
              if (rawValue != null) {
                int actualRank =
                    rawValue instanceof Matrix matrix
                        ? matrix.getValueRank()
                        : ArrayUtil.getValueRank(rawValue);
                Object rankElements =
                    rawValue instanceof Matrix matrix ? matrix.getElements() : rawValue;
                if (!(actualRank == -1)) {
                  throw new UaException(
                      StatusCodes.Bad_TypeMismatch,
                      "http://opcfoundation.org/UA/:StreamName (declaration i=24175, owner"
                          + " i=24173)");
                }
                if (rawValue instanceof Matrix matrix) {
                  if (actualRank < 2
                      || rankElements == null
                      || !rankElements.getClass().isArray()
                      || ArrayUtil.getValueRank(rankElements) != 1) {
                    throw new UaException(
                        StatusCodes.Bad_TypeMismatch,
                        "http://opcfoundation.org/UA/:StreamName (declaration i=24175, owner"
                            + " i=24173)");
                  }
                  long elementCount = 1;
                  for (int dimension : matrix.getDimensions()) {
                    if (dimension < 0 || elementCount > Integer.MAX_VALUE) {
                      throw new UaException(
                          StatusCodes.Bad_TypeMismatch,
                          "http://opcfoundation.org/UA/:StreamName (declaration i=24175, owner"
                              + " i=24173)");
                    }
                    elementCount *= dimension;
                  }
                  if (elementCount != Array.getLength(rankElements)) {
                    throw new UaException(
                        StatusCodes.Bad_TypeMismatch,
                        "http://opcfoundation.org/UA/:StreamName (declaration i=24175, owner"
                            + " i=24173)");
                  }
                } else if (actualRank > 1) {
                  throw new UaException(
                      StatusCodes.Bad_TypeMismatch,
                      "http://opcfoundation.org/UA/:StreamName (declaration i=24175, owner"
                          + " i=24173): use Matrix for multiple dimensions");
                }
              }
              Object element = rawValue;
              if (element != null && !(element instanceof String)) {
                throw new UaException(
                    StatusCodes.Bad_TypeMismatch,
                    "http://opcfoundation.org/UA/:StreamName (declaration i=24175, owner i=24173)");
              }
              converted = (String) element;
            }
            return converted;
          } catch (UaException failure) {
            throw new CompletionException(failure);
          }
        });
  }

  @Override
  public CompletableFuture<? extends @Nullable String> readStreamNameAsync() {
    try {
      this.views.checkOpen();
      return this.views.ownRequest(this.readStreamNameAsyncImplementation());
    } catch (RuntimeException viewFailure) {
      return CompletableFuture.failedFuture(viewFailure);
    }
  }

  @Override
  public void writeStreamName(@Nullable String value) throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    views.checkOpen();
    StatusCode status = ViewFutures.await(writeStreamNameAsync(value));
    if (status == null) {
      throw new UaException(StatusCodes.Bad_UnexpectedError);
    }
    if (!status.isGood()) {
      throw new UaException(status);
    }
  }

  private CompletableFuture<StatusCode> writeStreamNameAsyncImplementation(@Nullable String value) {
    return ViewFutures.compose(
        viewMember1Async(),
        child -> {
          if (child == null) {
            throw new CompletionException(
                new UaException(
                    StatusCodes.Bad_NotFound,
                    "http://opcfoundation.org/UA/:StreamName (declaration i=24175, owner i=24173)"
                        + " on "
                        + getNodeId()));
          }
          return ViewFutures.compose(
              views.readWriteContextAsync(child.getNodeId(), value, KNOWN_ENUMS),
              writeContext -> {
                try {
                  Variant encoded =
                      ValueChecks.validate(
                          writeContext,
                          value,
                          ExpandedNodeId.parse("i=12"),
                          -1,
                          new long[] {},
                          null,
                          true);
                  views.checkOpen();
                  return child
                      .writeAttributeAsync(AttributeId.Value, DataValue.valueOnly(encoded))
                      .thenApply(response -> response);
                } catch (Exception failure) {
                  return CompletableFuture.failedFuture(failure);
                }
              });
        });
  }

  @Override
  public CompletableFuture<StatusCode> writeStreamNameAsync(@Nullable String value) {
    try {
      this.views.checkOpen();
      return this.views.ownRequest(this.writeStreamNameAsyncImplementation(value));
    } catch (RuntimeException viewFailure) {
      return CompletableFuture.failedFuture(viewFailure);
    }
  }

  @Override
  public BaseDataVariableType getStateNode() throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    return ViewFutures.await(getStateNodeAsync());
  }

  private CompletableFuture<? extends BaseDataVariableType> getStateNodeAsyncImplementation() {
    return ViewFutures.compose(
        viewMember2Async(),
        child ->
            child == null
                ? CompletableFuture.completedFuture(null)
                : views.wrapVariableAsync(child.getNodeId(), BaseDataVariableTypeView.TYPE));
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableType> getStateNodeAsync() {
    try {
      this.views.checkOpen();
      return this.views.ownRequest(this.getStateNodeAsyncImplementation());
    } catch (RuntimeException viewFailure) {
      return CompletableFuture.failedFuture(viewFailure);
    }
  }

  @Override
  public @Nullable TsnStreamState getState() throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    var child = viewMember2();
    if (child == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:State (declaration i=24176, owner i=24173)"
              + " on "
              + getNodeId());
    }
    @Nullable TsnStreamState converted;
    {
      Object rawValue = child.getValue().getValue().getValue();
      if (rawValue instanceof Matrix matrix && matrix.isNull()) {
        rawValue = null;
      }
      if (rawValue != null) {
        int actualRank =
            rawValue instanceof Matrix matrix
                ? matrix.getValueRank()
                : ArrayUtil.getValueRank(rawValue);
        Object rankElements = rawValue instanceof Matrix matrix ? matrix.getElements() : rawValue;
        if (!(actualRank == -1)) {
          throw new UaException(
              StatusCodes.Bad_TypeMismatch,
              "http://opcfoundation.org/UA/:State (declaration i=24176, owner i=24173)");
        }
        if (rawValue instanceof Matrix matrix) {
          if (actualRank < 2
              || rankElements == null
              || !rankElements.getClass().isArray()
              || ArrayUtil.getValueRank(rankElements) != 1) {
            throw new UaException(
                StatusCodes.Bad_TypeMismatch,
                "http://opcfoundation.org/UA/:State (declaration i=24176, owner i=24173)");
          }
          long elementCount = 1;
          for (int dimension : matrix.getDimensions()) {
            if (dimension < 0 || elementCount > Integer.MAX_VALUE) {
              throw new UaException(
                  StatusCodes.Bad_TypeMismatch,
                  "http://opcfoundation.org/UA/:State (declaration i=24176, owner i=24173)");
            }
            elementCount *= dimension;
          }
          if (elementCount != Array.getLength(rankElements)) {
            throw new UaException(
                StatusCodes.Bad_TypeMismatch,
                "http://opcfoundation.org/UA/:State (declaration i=24176, owner i=24173)");
          }
        } else if (actualRank > 1) {
          throw new UaException(
              StatusCodes.Bad_TypeMismatch,
              "http://opcfoundation.org/UA/:State (declaration i=24176, owner i=24173): use Matrix"
                  + " for multiple dimensions");
        }
      }
      {
        if (rawValue == null || rawValue instanceof Matrix && ((Matrix) rawValue).isNull()) {
          converted = null;
        } else {
          Object elements =
              rawValue instanceof Matrix ? ((Matrix) rawValue).getElements() : rawValue;
          int rank =
              rawValue instanceof Matrix
                  ? ((Matrix) rawValue).getValueRank()
                  : ArrayUtil.getValueRank(rawValue);
          boolean permitted = rank == -1;
          if (!permitted) {
            throw new UaException(
                StatusCodes.Bad_TypeMismatch,
                "http://opcfoundation.org/UA/:State (declaration i=24176, owner i=24173):"
                    + " ValueRank=-1 does not permit rank "
                    + rank);
          }
          if (rawValue != null && !((Object) rawValue instanceof TsnStreamState)) {
            if (!(rawValue instanceof Integer)) {
              throw new UaException(
                  StatusCodes.Bad_TypeMismatch,
                  "http://opcfoundation.org/UA/:State (declaration i=24176, owner i=24173):"
                      + " expected"
                      + " org.eclipse.milo.opcua.stack.core.types.enumerated.TsnStreamState or"
                      + " Int32, got "
                      + rawValue);
            }
            if (TsnStreamState.from((Integer) rawValue) == null) {
              throw new UaException(
                  StatusCodes.Bad_OutOfRange,
                  "http://opcfoundation.org/UA/:State (declaration i=24176, owner i=24173): unknown"
                      + " org.eclipse.milo.opcua.stack.core.types.enumerated.TsnStreamState value "
                      + rawValue);
            }
          }
          converted =
              rawValue == null || rawValue instanceof TsnStreamState
                  ? (TsnStreamState) rawValue
                  : TsnStreamState.from((Integer) rawValue);
        }
      }
    }
    return converted;
  }

  @Override
  public void setState(@Nullable TsnStreamState value) throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    var child = viewMember2();
    if (child == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:State (declaration i=24176, owner i=24173)"
              + " on "
              + getNodeId());
    }
    var writeContext = views.localWriteContext(child.getNodeId(), value, KNOWN_ENUMS);
    Variant encoded =
        ValueChecks.validate(
            writeContext,
            value,
            ExpandedNodeId.parse("i=24220"),
            -1,
            new long[] {},
            Set.of(0, 1, 2, 3, 4),
            false);
    views.checkOpen();
    child.setValue(encoded);
  }

  @Override
  public @Nullable TsnStreamState readState() throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    return ViewFutures.await(readStateAsync());
  }

  private CompletableFuture<? extends @Nullable TsnStreamState> readStateAsyncImplementation() {
    return ViewFutures.map(
        ViewFutures.compose(
            viewMember2Async(),
            child -> {
              if (child == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:State (declaration i=24176, owner i=24173)"
                            + " on "
                            + getNodeId()));
              }
              views.checkOpen();
              return child.readAttributeAsync(AttributeId.Value).thenApply(response -> response);
            }),
        value -> {
          views.checkOpen();
          if (value == null || value.getStatusCode() == null) {
            throw new CompletionException(new UaException(StatusCodes.Bad_UnexpectedError));
          }
          if (!value.getStatusCode().isGood()) {
            throw new CompletionException(new UaException(value.getStatusCode()));
          }
          try {
            @Nullable TsnStreamState converted;
            {
              Object rawValue = value.getValue().getValue();
              if (rawValue instanceof Matrix matrix && matrix.isNull()) {
                rawValue = null;
              }
              if (rawValue != null) {
                int actualRank =
                    rawValue instanceof Matrix matrix
                        ? matrix.getValueRank()
                        : ArrayUtil.getValueRank(rawValue);
                Object rankElements =
                    rawValue instanceof Matrix matrix ? matrix.getElements() : rawValue;
                if (!(actualRank == -1)) {
                  throw new UaException(
                      StatusCodes.Bad_TypeMismatch,
                      "http://opcfoundation.org/UA/:State (declaration i=24176, owner i=24173)");
                }
                if (rawValue instanceof Matrix matrix) {
                  if (actualRank < 2
                      || rankElements == null
                      || !rankElements.getClass().isArray()
                      || ArrayUtil.getValueRank(rankElements) != 1) {
                    throw new UaException(
                        StatusCodes.Bad_TypeMismatch,
                        "http://opcfoundation.org/UA/:State (declaration i=24176, owner i=24173)");
                  }
                  long elementCount = 1;
                  for (int dimension : matrix.getDimensions()) {
                    if (dimension < 0 || elementCount > Integer.MAX_VALUE) {
                      throw new UaException(
                          StatusCodes.Bad_TypeMismatch,
                          "http://opcfoundation.org/UA/:State (declaration i=24176, owner"
                              + " i=24173)");
                    }
                    elementCount *= dimension;
                  }
                  if (elementCount != Array.getLength(rankElements)) {
                    throw new UaException(
                        StatusCodes.Bad_TypeMismatch,
                        "http://opcfoundation.org/UA/:State (declaration i=24176, owner i=24173)");
                  }
                } else if (actualRank > 1) {
                  throw new UaException(
                      StatusCodes.Bad_TypeMismatch,
                      "http://opcfoundation.org/UA/:State (declaration i=24176, owner i=24173): use"
                          + " Matrix for multiple dimensions");
                }
              }
              {
                if (rawValue == null
                    || rawValue instanceof Matrix && ((Matrix) rawValue).isNull()) {
                  converted = null;
                } else {
                  Object elements =
                      rawValue instanceof Matrix ? ((Matrix) rawValue).getElements() : rawValue;
                  int rank =
                      rawValue instanceof Matrix
                          ? ((Matrix) rawValue).getValueRank()
                          : ArrayUtil.getValueRank(rawValue);
                  boolean permitted = rank == -1;
                  if (!permitted) {
                    throw new UaException(
                        StatusCodes.Bad_TypeMismatch,
                        "http://opcfoundation.org/UA/:State (declaration i=24176, owner i=24173):"
                            + " ValueRank=-1 does not permit rank "
                            + rank);
                  }
                  if (rawValue != null && !((Object) rawValue instanceof TsnStreamState)) {
                    if (!(rawValue instanceof Integer)) {
                      throw new UaException(
                          StatusCodes.Bad_TypeMismatch,
                          "http://opcfoundation.org/UA/:State (declaration i=24176, owner i=24173):"
                              + " expected"
                              + " org.eclipse.milo.opcua.stack.core.types.enumerated.TsnStreamState"
                              + " or Int32, got "
                              + rawValue);
                    }
                    if (TsnStreamState.from((Integer) rawValue) == null) {
                      throw new UaException(
                          StatusCodes.Bad_OutOfRange,
                          "http://opcfoundation.org/UA/:State (declaration i=24176, owner i=24173):"
                              + " unknown"
                              + " org.eclipse.milo.opcua.stack.core.types.enumerated.TsnStreamState"
                              + " value "
                              + rawValue);
                    }
                  }
                  converted =
                      rawValue == null || rawValue instanceof TsnStreamState
                          ? (TsnStreamState) rawValue
                          : TsnStreamState.from((Integer) rawValue);
                }
              }
            }
            return converted;
          } catch (UaException failure) {
            throw new CompletionException(failure);
          }
        });
  }

  @Override
  public CompletableFuture<? extends @Nullable TsnStreamState> readStateAsync() {
    try {
      this.views.checkOpen();
      return this.views.ownRequest(this.readStateAsyncImplementation());
    } catch (RuntimeException viewFailure) {
      return CompletableFuture.failedFuture(viewFailure);
    }
  }

  @Override
  public void writeState(@Nullable TsnStreamState value) throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    views.checkOpen();
    StatusCode status = ViewFutures.await(writeStateAsync(value));
    if (status == null) {
      throw new UaException(StatusCodes.Bad_UnexpectedError);
    }
    if (!status.isGood()) {
      throw new UaException(status);
    }
  }

  private CompletableFuture<StatusCode> writeStateAsyncImplementation(
      @Nullable TsnStreamState value) {
    return ViewFutures.compose(
        viewMember2Async(),
        child -> {
          if (child == null) {
            throw new CompletionException(
                new UaException(
                    StatusCodes.Bad_NotFound,
                    "http://opcfoundation.org/UA/:State (declaration i=24176, owner i=24173)"
                        + " on "
                        + getNodeId()));
          }
          return ViewFutures.compose(
              views.readWriteContextAsync(child.getNodeId(), value, KNOWN_ENUMS),
              writeContext -> {
                try {
                  Variant encoded =
                      ValueChecks.validate(
                          writeContext,
                          value,
                          ExpandedNodeId.parse("i=24220"),
                          -1,
                          new long[] {},
                          Set.of(0, 1, 2, 3, 4),
                          true);
                  views.checkOpen();
                  return child
                      .writeAttributeAsync(AttributeId.Value, DataValue.valueOnly(encoded))
                      .thenApply(response -> response);
                } catch (Exception failure) {
                  return CompletableFuture.failedFuture(failure);
                }
              });
        });
  }

  @Override
  public CompletableFuture<StatusCode> writeStateAsync(@Nullable TsnStreamState value) {
    try {
      this.views.checkOpen();
      return this.views.ownRequest(this.writeStateAsyncImplementation(value));
    } catch (RuntimeException viewFailure) {
      return CompletableFuture.failedFuture(viewFailure);
    }
  }

  @Override
  public @Nullable BaseDataVariableType getAccumulatedLatencyNode() throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    return ViewFutures.await(getAccumulatedLatencyNodeAsync());
  }

  private CompletableFuture<? extends @Nullable BaseDataVariableType>
      getAccumulatedLatencyNodeAsyncImplementation() {
    return ViewFutures.compose(
        viewMember3Async(),
        child ->
            child == null
                ? CompletableFuture.completedFuture(null)
                : views.wrapVariableAsync(child.getNodeId(), BaseDataVariableTypeView.TYPE));
  }

  @Override
  public CompletableFuture<? extends @Nullable BaseDataVariableType>
      getAccumulatedLatencyNodeAsync() {
    try {
      this.views.checkOpen();
      return this.views.ownRequest(this.getAccumulatedLatencyNodeAsyncImplementation());
    } catch (RuntimeException viewFailure) {
      return CompletableFuture.failedFuture(viewFailure);
    }
  }

  @Override
  public @Nullable UInteger getAccumulatedLatency() throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    var child = viewMember3();
    if (child == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:AccumulatedLatency (declaration i=24177, owner i=24173)"
              + " on "
              + getNodeId());
    }
    @Nullable UInteger converted;
    {
      Object rawValue = child.getValue().getValue().getValue();
      if (rawValue instanceof Matrix matrix && matrix.isNull()) {
        rawValue = null;
      }
      if (rawValue != null) {
        int actualRank =
            rawValue instanceof Matrix matrix
                ? matrix.getValueRank()
                : ArrayUtil.getValueRank(rawValue);
        Object rankElements = rawValue instanceof Matrix matrix ? matrix.getElements() : rawValue;
        if (!(actualRank == -1)) {
          throw new UaException(
              StatusCodes.Bad_TypeMismatch,
              "http://opcfoundation.org/UA/:AccumulatedLatency (declaration i=24177, owner"
                  + " i=24173)");
        }
        if (rawValue instanceof Matrix matrix) {
          if (actualRank < 2
              || rankElements == null
              || !rankElements.getClass().isArray()
              || ArrayUtil.getValueRank(rankElements) != 1) {
            throw new UaException(
                StatusCodes.Bad_TypeMismatch,
                "http://opcfoundation.org/UA/:AccumulatedLatency (declaration i=24177, owner"
                    + " i=24173)");
          }
          long elementCount = 1;
          for (int dimension : matrix.getDimensions()) {
            if (dimension < 0 || elementCount > Integer.MAX_VALUE) {
              throw new UaException(
                  StatusCodes.Bad_TypeMismatch,
                  "http://opcfoundation.org/UA/:AccumulatedLatency (declaration i=24177, owner"
                      + " i=24173)");
            }
            elementCount *= dimension;
          }
          if (elementCount != Array.getLength(rankElements)) {
            throw new UaException(
                StatusCodes.Bad_TypeMismatch,
                "http://opcfoundation.org/UA/:AccumulatedLatency (declaration i=24177, owner"
                    + " i=24173)");
          }
        } else if (actualRank > 1) {
          throw new UaException(
              StatusCodes.Bad_TypeMismatch,
              "http://opcfoundation.org/UA/:AccumulatedLatency (declaration i=24177, owner"
                  + " i=24173): use Matrix for multiple dimensions");
        }
      }
      Object element = rawValue;
      if (element != null && !(element instanceof UInteger)) {
        throw new UaException(
            StatusCodes.Bad_TypeMismatch,
            "http://opcfoundation.org/UA/:AccumulatedLatency (declaration i=24177, owner i=24173)");
      }
      converted = (UInteger) element;
    }
    return converted;
  }

  @Override
  public void setAccumulatedLatency(@Nullable UInteger value) throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    var child = viewMember3();
    if (child == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:AccumulatedLatency (declaration i=24177, owner i=24173)"
              + " on "
              + getNodeId());
    }
    var writeContext = views.localWriteContext(child.getNodeId(), value, KNOWN_ENUMS);
    Variant encoded =
        ValueChecks.validate(
            writeContext, value, ExpandedNodeId.parse("i=7"), -1, new long[] {}, null, false);
    views.checkOpen();
    child.setValue(encoded);
  }

  @Override
  public @Nullable UInteger readAccumulatedLatency() throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    return ViewFutures.await(readAccumulatedLatencyAsync());
  }

  private CompletableFuture<? extends @Nullable UInteger>
      readAccumulatedLatencyAsyncImplementation() {
    return ViewFutures.map(
        ViewFutures.compose(
            viewMember3Async(),
            child -> {
              if (child == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:AccumulatedLatency (declaration i=24177,"
                            + " owner i=24173) on "
                            + getNodeId()));
              }
              views.checkOpen();
              return child.readAttributeAsync(AttributeId.Value).thenApply(response -> response);
            }),
        value -> {
          views.checkOpen();
          if (value == null || value.getStatusCode() == null) {
            throw new CompletionException(new UaException(StatusCodes.Bad_UnexpectedError));
          }
          if (!value.getStatusCode().isGood()) {
            throw new CompletionException(new UaException(value.getStatusCode()));
          }
          try {
            @Nullable UInteger converted;
            {
              Object rawValue = value.getValue().getValue();
              if (rawValue instanceof Matrix matrix && matrix.isNull()) {
                rawValue = null;
              }
              if (rawValue != null) {
                int actualRank =
                    rawValue instanceof Matrix matrix
                        ? matrix.getValueRank()
                        : ArrayUtil.getValueRank(rawValue);
                Object rankElements =
                    rawValue instanceof Matrix matrix ? matrix.getElements() : rawValue;
                if (!(actualRank == -1)) {
                  throw new UaException(
                      StatusCodes.Bad_TypeMismatch,
                      "http://opcfoundation.org/UA/:AccumulatedLatency (declaration i=24177, owner"
                          + " i=24173)");
                }
                if (rawValue instanceof Matrix matrix) {
                  if (actualRank < 2
                      || rankElements == null
                      || !rankElements.getClass().isArray()
                      || ArrayUtil.getValueRank(rankElements) != 1) {
                    throw new UaException(
                        StatusCodes.Bad_TypeMismatch,
                        "http://opcfoundation.org/UA/:AccumulatedLatency (declaration i=24177,"
                            + " owner i=24173)");
                  }
                  long elementCount = 1;
                  for (int dimension : matrix.getDimensions()) {
                    if (dimension < 0 || elementCount > Integer.MAX_VALUE) {
                      throw new UaException(
                          StatusCodes.Bad_TypeMismatch,
                          "http://opcfoundation.org/UA/:AccumulatedLatency (declaration i=24177,"
                              + " owner i=24173)");
                    }
                    elementCount *= dimension;
                  }
                  if (elementCount != Array.getLength(rankElements)) {
                    throw new UaException(
                        StatusCodes.Bad_TypeMismatch,
                        "http://opcfoundation.org/UA/:AccumulatedLatency (declaration i=24177,"
                            + " owner i=24173)");
                  }
                } else if (actualRank > 1) {
                  throw new UaException(
                      StatusCodes.Bad_TypeMismatch,
                      "http://opcfoundation.org/UA/:AccumulatedLatency (declaration i=24177, owner"
                          + " i=24173): use Matrix for multiple dimensions");
                }
              }
              Object element = rawValue;
              if (element != null && !(element instanceof UInteger)) {
                throw new UaException(
                    StatusCodes.Bad_TypeMismatch,
                    "http://opcfoundation.org/UA/:AccumulatedLatency (declaration i=24177, owner"
                        + " i=24173)");
              }
              converted = (UInteger) element;
            }
            return converted;
          } catch (UaException failure) {
            throw new CompletionException(failure);
          }
        });
  }

  @Override
  public CompletableFuture<? extends @Nullable UInteger> readAccumulatedLatencyAsync() {
    try {
      this.views.checkOpen();
      return this.views.ownRequest(this.readAccumulatedLatencyAsyncImplementation());
    } catch (RuntimeException viewFailure) {
      return CompletableFuture.failedFuture(viewFailure);
    }
  }

  @Override
  public void writeAccumulatedLatency(@Nullable UInteger value) throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    views.checkOpen();
    StatusCode status = ViewFutures.await(writeAccumulatedLatencyAsync(value));
    if (status == null) {
      throw new UaException(StatusCodes.Bad_UnexpectedError);
    }
    if (!status.isGood()) {
      throw new UaException(status);
    }
  }

  private CompletableFuture<StatusCode> writeAccumulatedLatencyAsyncImplementation(
      @Nullable UInteger value) {
    return ViewFutures.compose(
        viewMember3Async(),
        child -> {
          if (child == null) {
            throw new CompletionException(
                new UaException(
                    StatusCodes.Bad_NotFound,
                    "http://opcfoundation.org/UA/:AccumulatedLatency (declaration i=24177, owner"
                        + " i=24173) on "
                        + getNodeId()));
          }
          return ViewFutures.compose(
              views.readWriteContextAsync(child.getNodeId(), value, KNOWN_ENUMS),
              writeContext -> {
                try {
                  Variant encoded =
                      ValueChecks.validate(
                          writeContext,
                          value,
                          ExpandedNodeId.parse("i=7"),
                          -1,
                          new long[] {},
                          null,
                          true);
                  views.checkOpen();
                  return child
                      .writeAttributeAsync(AttributeId.Value, DataValue.valueOnly(encoded))
                      .thenApply(response -> response);
                } catch (Exception failure) {
                  return CompletableFuture.failedFuture(failure);
                }
              });
        });
  }

  @Override
  public CompletableFuture<StatusCode> writeAccumulatedLatencyAsync(@Nullable UInteger value) {
    try {
      this.views.checkOpen();
      return this.views.ownRequest(this.writeAccumulatedLatencyAsyncImplementation(value));
    } catch (RuntimeException viewFailure) {
      return CompletableFuture.failedFuture(viewFailure);
    }
  }

  @Override
  public @Nullable BaseDataVariableType getSrClassIdNode() throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    return ViewFutures.await(getSrClassIdNodeAsync());
  }

  private CompletableFuture<? extends @Nullable BaseDataVariableType>
      getSrClassIdNodeAsyncImplementation() {
    return ViewFutures.compose(
        viewMember4Async(),
        child ->
            child == null
                ? CompletableFuture.completedFuture(null)
                : views.wrapVariableAsync(child.getNodeId(), BaseDataVariableTypeView.TYPE));
  }

  @Override
  public CompletableFuture<? extends @Nullable BaseDataVariableType> getSrClassIdNodeAsync() {
    try {
      this.views.checkOpen();
      return this.views.ownRequest(this.getSrClassIdNodeAsyncImplementation());
    } catch (RuntimeException viewFailure) {
      return CompletableFuture.failedFuture(viewFailure);
    }
  }

  @Override
  public @Nullable UByte getSrClassId() throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    var child = viewMember4();
    if (child == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:SrClassId (declaration i=24178, owner i=24173)"
              + " on "
              + getNodeId());
    }
    @Nullable UByte converted;
    {
      Object rawValue = child.getValue().getValue().getValue();
      if (rawValue instanceof Matrix matrix && matrix.isNull()) {
        rawValue = null;
      }
      if (rawValue != null) {
        int actualRank =
            rawValue instanceof Matrix matrix
                ? matrix.getValueRank()
                : ArrayUtil.getValueRank(rawValue);
        Object rankElements = rawValue instanceof Matrix matrix ? matrix.getElements() : rawValue;
        if (!(actualRank == -1)) {
          throw new UaException(
              StatusCodes.Bad_TypeMismatch,
              "http://opcfoundation.org/UA/:SrClassId (declaration i=24178, owner i=24173)");
        }
        if (rawValue instanceof Matrix matrix) {
          if (actualRank < 2
              || rankElements == null
              || !rankElements.getClass().isArray()
              || ArrayUtil.getValueRank(rankElements) != 1) {
            throw new UaException(
                StatusCodes.Bad_TypeMismatch,
                "http://opcfoundation.org/UA/:SrClassId (declaration i=24178, owner i=24173)");
          }
          long elementCount = 1;
          for (int dimension : matrix.getDimensions()) {
            if (dimension < 0 || elementCount > Integer.MAX_VALUE) {
              throw new UaException(
                  StatusCodes.Bad_TypeMismatch,
                  "http://opcfoundation.org/UA/:SrClassId (declaration i=24178, owner i=24173)");
            }
            elementCount *= dimension;
          }
          if (elementCount != Array.getLength(rankElements)) {
            throw new UaException(
                StatusCodes.Bad_TypeMismatch,
                "http://opcfoundation.org/UA/:SrClassId (declaration i=24178, owner i=24173)");
          }
        } else if (actualRank > 1) {
          throw new UaException(
              StatusCodes.Bad_TypeMismatch,
              "http://opcfoundation.org/UA/:SrClassId (declaration i=24178, owner i=24173): use"
                  + " Matrix for multiple dimensions");
        }
      }
      Object element = rawValue;
      if (element != null && !(element instanceof UByte)) {
        throw new UaException(
            StatusCodes.Bad_TypeMismatch,
            "http://opcfoundation.org/UA/:SrClassId (declaration i=24178, owner i=24173)");
      }
      converted = (UByte) element;
    }
    return converted;
  }

  @Override
  public void setSrClassId(@Nullable UByte value) throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    var child = viewMember4();
    if (child == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:SrClassId (declaration i=24178, owner i=24173)"
              + " on "
              + getNodeId());
    }
    var writeContext = views.localWriteContext(child.getNodeId(), value, KNOWN_ENUMS);
    Variant encoded =
        ValueChecks.validate(
            writeContext, value, ExpandedNodeId.parse("i=3"), -1, new long[] {}, null, false);
    views.checkOpen();
    child.setValue(encoded);
  }

  @Override
  public @Nullable UByte readSrClassId() throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    return ViewFutures.await(readSrClassIdAsync());
  }

  private CompletableFuture<? extends @Nullable UByte> readSrClassIdAsyncImplementation() {
    return ViewFutures.map(
        ViewFutures.compose(
            viewMember4Async(),
            child -> {
              if (child == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:SrClassId (declaration i=24178, owner"
                            + " i=24173) on "
                            + getNodeId()));
              }
              views.checkOpen();
              return child.readAttributeAsync(AttributeId.Value).thenApply(response -> response);
            }),
        value -> {
          views.checkOpen();
          if (value == null || value.getStatusCode() == null) {
            throw new CompletionException(new UaException(StatusCodes.Bad_UnexpectedError));
          }
          if (!value.getStatusCode().isGood()) {
            throw new CompletionException(new UaException(value.getStatusCode()));
          }
          try {
            @Nullable UByte converted;
            {
              Object rawValue = value.getValue().getValue();
              if (rawValue instanceof Matrix matrix && matrix.isNull()) {
                rawValue = null;
              }
              if (rawValue != null) {
                int actualRank =
                    rawValue instanceof Matrix matrix
                        ? matrix.getValueRank()
                        : ArrayUtil.getValueRank(rawValue);
                Object rankElements =
                    rawValue instanceof Matrix matrix ? matrix.getElements() : rawValue;
                if (!(actualRank == -1)) {
                  throw new UaException(
                      StatusCodes.Bad_TypeMismatch,
                      "http://opcfoundation.org/UA/:SrClassId (declaration i=24178, owner"
                          + " i=24173)");
                }
                if (rawValue instanceof Matrix matrix) {
                  if (actualRank < 2
                      || rankElements == null
                      || !rankElements.getClass().isArray()
                      || ArrayUtil.getValueRank(rankElements) != 1) {
                    throw new UaException(
                        StatusCodes.Bad_TypeMismatch,
                        "http://opcfoundation.org/UA/:SrClassId (declaration i=24178, owner"
                            + " i=24173)");
                  }
                  long elementCount = 1;
                  for (int dimension : matrix.getDimensions()) {
                    if (dimension < 0 || elementCount > Integer.MAX_VALUE) {
                      throw new UaException(
                          StatusCodes.Bad_TypeMismatch,
                          "http://opcfoundation.org/UA/:SrClassId (declaration i=24178, owner"
                              + " i=24173)");
                    }
                    elementCount *= dimension;
                  }
                  if (elementCount != Array.getLength(rankElements)) {
                    throw new UaException(
                        StatusCodes.Bad_TypeMismatch,
                        "http://opcfoundation.org/UA/:SrClassId (declaration i=24178, owner"
                            + " i=24173)");
                  }
                } else if (actualRank > 1) {
                  throw new UaException(
                      StatusCodes.Bad_TypeMismatch,
                      "http://opcfoundation.org/UA/:SrClassId (declaration i=24178, owner i=24173):"
                          + " use Matrix for multiple dimensions");
                }
              }
              Object element = rawValue;
              if (element != null && !(element instanceof UByte)) {
                throw new UaException(
                    StatusCodes.Bad_TypeMismatch,
                    "http://opcfoundation.org/UA/:SrClassId (declaration i=24178, owner i=24173)");
              }
              converted = (UByte) element;
            }
            return converted;
          } catch (UaException failure) {
            throw new CompletionException(failure);
          }
        });
  }

  @Override
  public CompletableFuture<? extends @Nullable UByte> readSrClassIdAsync() {
    try {
      this.views.checkOpen();
      return this.views.ownRequest(this.readSrClassIdAsyncImplementation());
    } catch (RuntimeException viewFailure) {
      return CompletableFuture.failedFuture(viewFailure);
    }
  }

  @Override
  public void writeSrClassId(@Nullable UByte value) throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    views.checkOpen();
    StatusCode status = ViewFutures.await(writeSrClassIdAsync(value));
    if (status == null) {
      throw new UaException(StatusCodes.Bad_UnexpectedError);
    }
    if (!status.isGood()) {
      throw new UaException(status);
    }
  }

  private CompletableFuture<StatusCode> writeSrClassIdAsyncImplementation(@Nullable UByte value) {
    return ViewFutures.compose(
        viewMember4Async(),
        child -> {
          if (child == null) {
            throw new CompletionException(
                new UaException(
                    StatusCodes.Bad_NotFound,
                    "http://opcfoundation.org/UA/:SrClassId (declaration i=24178, owner i=24173)"
                        + " on "
                        + getNodeId()));
          }
          return ViewFutures.compose(
              views.readWriteContextAsync(child.getNodeId(), value, KNOWN_ENUMS),
              writeContext -> {
                try {
                  Variant encoded =
                      ValueChecks.validate(
                          writeContext,
                          value,
                          ExpandedNodeId.parse("i=3"),
                          -1,
                          new long[] {},
                          null,
                          true);
                  views.checkOpen();
                  return child
                      .writeAttributeAsync(AttributeId.Value, DataValue.valueOnly(encoded))
                      .thenApply(response -> response);
                } catch (Exception failure) {
                  return CompletableFuture.failedFuture(failure);
                }
              });
        });
  }

  @Override
  public CompletableFuture<StatusCode> writeSrClassIdAsync(@Nullable UByte value) {
    try {
      this.views.checkOpen();
      return this.views.ownRequest(this.writeSrClassIdAsyncImplementation(value));
    } catch (RuntimeException viewFailure) {
      return CompletableFuture.failedFuture(viewFailure);
    }
  }
}
