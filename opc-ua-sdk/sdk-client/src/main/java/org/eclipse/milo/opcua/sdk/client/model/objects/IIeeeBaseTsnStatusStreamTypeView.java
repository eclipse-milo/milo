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
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TsnFailureCode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TsnListenerStatus;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TsnTalkerStatus;
import org.eclipse.milo.opcua.stack.core.util.ArrayUtil;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * https://reference.opcfoundation.org/v105/Core/docs/Part22/5.2.9
 *
 * <p>Selected <code>IIeeeBaseTsnStatusStreamType</code> contract over one retained backing node.
 * Obtain this view from a ClientViews context using {@link #TYPE}; the token itself does not prove
 * membership. Access after context close fails with IllegalStateException.
 */
@NullMarked
public final class IIeeeBaseTsnStatusStreamTypeView extends ClientObjectView
    implements IIeeeBaseTsnStatusStreamType {
  /** Selected UA/Java contract and context-owned factory. */
  public static final ClientViewType<IIeeeBaseTsnStatusStreamType> TYPE =
      ClientViewType.of(
          ExpandedNodeId.parse("i=24183"),
          IIeeeBaseTsnStatusStreamType.class,
          IIeeeBaseTsnStatusStreamTypeView::new);

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

  private IIeeeBaseTsnStatusStreamTypeView(ClientViews views, UaObjectNode node) {
    super(views, node);
  }

  private CompletableFuture<@Nullable UaVariableNode> viewMember0Async() {
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
                            "TalkerStatus",
                            ExpandedNodeId.parse("i=47"),
                            true,
                            NodeClass.Variable,
                            true,
                            "http://opcfoundation.org/UA/:TalkerStatus (declaration i=24184, owner"
                                + " i=24183)"));
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

  private @Nullable UaVariableNode viewMember0() throws UaException {
    return ViewFutures.await(viewMember0Async());
  }

  private CompletableFuture<@Nullable UaVariableNode> viewMember1Async() {
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
                            "ListenerStatus",
                            ExpandedNodeId.parse("i=47"),
                            true,
                            NodeClass.Variable,
                            true,
                            "http://opcfoundation.org/UA/:ListenerStatus (declaration i=24185,"
                                + " owner i=24183)"));
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

  private @Nullable UaVariableNode viewMember1() throws UaException {
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
                            "FailureCode",
                            ExpandedNodeId.parse("i=47"),
                            true,
                            NodeClass.Variable,
                            false,
                            "http://opcfoundation.org/UA/:FailureCode (declaration i=24186, owner"
                                + " i=24183)"));
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

  private CompletableFuture<UaVariableNode> viewMember3Async() {
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
                            "FailureSystemIdentifier",
                            ExpandedNodeId.parse("i=47"),
                            true,
                            NodeClass.Variable,
                            false,
                            "http://opcfoundation.org/UA/:FailureSystemIdentifier (declaration"
                                + " i=24187, owner i=24183)"));
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

  private UaVariableNode viewMember3() throws UaException {
    return ViewFutures.await(viewMember3Async());
  }

  @Override
  public @Nullable BaseDataVariableType getTalkerStatusNode() throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    return ViewFutures.await(getTalkerStatusNodeAsync());
  }

  private CompletableFuture<? extends @Nullable BaseDataVariableType>
      getTalkerStatusNodeAsyncImplementation() {
    return ViewFutures.compose(
        viewMember0Async(),
        child ->
            child == null
                ? CompletableFuture.completedFuture(null)
                : views.wrapVariableAsync(child.getNodeId(), BaseDataVariableTypeView.TYPE));
  }

  @Override
  public CompletableFuture<? extends @Nullable BaseDataVariableType> getTalkerStatusNodeAsync() {
    try {
      this.views.checkOpen();
      return this.views.ownRequest(this.getTalkerStatusNodeAsyncImplementation());
    } catch (RuntimeException viewFailure) {
      return CompletableFuture.failedFuture(viewFailure);
    }
  }

  @Override
  public @Nullable TsnTalkerStatus getTalkerStatus() throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    var child = viewMember0();
    if (child == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:TalkerStatus (declaration i=24184, owner i=24183)"
              + " on "
              + getNodeId());
    }
    @Nullable TsnTalkerStatus converted;
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
              "http://opcfoundation.org/UA/:TalkerStatus (declaration i=24184, owner i=24183)");
        }
        if (rawValue instanceof Matrix matrix) {
          if (actualRank < 2
              || rankElements == null
              || !rankElements.getClass().isArray()
              || ArrayUtil.getValueRank(rankElements) != 1) {
            throw new UaException(
                StatusCodes.Bad_TypeMismatch,
                "http://opcfoundation.org/UA/:TalkerStatus (declaration i=24184, owner i=24183)");
          }
          long elementCount = 1;
          for (int dimension : matrix.getDimensions()) {
            if (dimension < 0 || elementCount > Integer.MAX_VALUE) {
              throw new UaException(
                  StatusCodes.Bad_TypeMismatch,
                  "http://opcfoundation.org/UA/:TalkerStatus (declaration i=24184, owner i=24183)");
            }
            elementCount *= dimension;
          }
          if (elementCount != Array.getLength(rankElements)) {
            throw new UaException(
                StatusCodes.Bad_TypeMismatch,
                "http://opcfoundation.org/UA/:TalkerStatus (declaration i=24184, owner i=24183)");
          }
        } else if (actualRank > 1) {
          throw new UaException(
              StatusCodes.Bad_TypeMismatch,
              "http://opcfoundation.org/UA/:TalkerStatus (declaration i=24184, owner i=24183): use"
                  + " Matrix for multiple dimensions");
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
                "http://opcfoundation.org/UA/:TalkerStatus (declaration i=24184, owner i=24183):"
                    + " ValueRank=-1 does not permit rank "
                    + rank);
          }
          if (rawValue != null && !((Object) rawValue instanceof TsnTalkerStatus)) {
            if (!(rawValue instanceof Integer)) {
              throw new UaException(
                  StatusCodes.Bad_TypeMismatch,
                  "http://opcfoundation.org/UA/:TalkerStatus (declaration i=24184, owner i=24183):"
                      + " expected"
                      + " org.eclipse.milo.opcua.stack.core.types.enumerated.TsnTalkerStatus or"
                      + " Int32, got "
                      + rawValue);
            }
            if (TsnTalkerStatus.from((Integer) rawValue) == null) {
              throw new UaException(
                  StatusCodes.Bad_OutOfRange,
                  "http://opcfoundation.org/UA/:TalkerStatus (declaration i=24184, owner i=24183):"
                      + " unknown"
                      + " org.eclipse.milo.opcua.stack.core.types.enumerated.TsnTalkerStatus value "
                      + rawValue);
            }
          }
          converted =
              rawValue == null || rawValue instanceof TsnTalkerStatus
                  ? (TsnTalkerStatus) rawValue
                  : TsnTalkerStatus.from((Integer) rawValue);
        }
      }
    }
    return converted;
  }

  @Override
  public void setTalkerStatus(@Nullable TsnTalkerStatus value) throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    var child = viewMember0();
    if (child == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:TalkerStatus (declaration i=24184, owner i=24183)"
              + " on "
              + getNodeId());
    }
    var writeContext = views.localWriteContext(child.getNodeId(), value, KNOWN_ENUMS);
    Variant encoded =
        ValueChecks.validate(
            writeContext,
            value,
            ExpandedNodeId.parse("i=24222"),
            -1,
            new long[] {},
            Set.of(0, 1, 2),
            false);
    views.checkOpen();
    child.setValue(encoded);
  }

  @Override
  public @Nullable TsnTalkerStatus readTalkerStatus() throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    return ViewFutures.await(readTalkerStatusAsync());
  }

  private CompletableFuture<? extends @Nullable TsnTalkerStatus>
      readTalkerStatusAsyncImplementation() {
    return ViewFutures.map(
        ViewFutures.compose(
            viewMember0Async(),
            child -> {
              if (child == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:TalkerStatus (declaration i=24184, owner"
                            + " i=24183) on "
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
            @Nullable TsnTalkerStatus converted;
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
                      "http://opcfoundation.org/UA/:TalkerStatus (declaration i=24184, owner"
                          + " i=24183)");
                }
                if (rawValue instanceof Matrix matrix) {
                  if (actualRank < 2
                      || rankElements == null
                      || !rankElements.getClass().isArray()
                      || ArrayUtil.getValueRank(rankElements) != 1) {
                    throw new UaException(
                        StatusCodes.Bad_TypeMismatch,
                        "http://opcfoundation.org/UA/:TalkerStatus (declaration i=24184, owner"
                            + " i=24183)");
                  }
                  long elementCount = 1;
                  for (int dimension : matrix.getDimensions()) {
                    if (dimension < 0 || elementCount > Integer.MAX_VALUE) {
                      throw new UaException(
                          StatusCodes.Bad_TypeMismatch,
                          "http://opcfoundation.org/UA/:TalkerStatus (declaration i=24184, owner"
                              + " i=24183)");
                    }
                    elementCount *= dimension;
                  }
                  if (elementCount != Array.getLength(rankElements)) {
                    throw new UaException(
                        StatusCodes.Bad_TypeMismatch,
                        "http://opcfoundation.org/UA/:TalkerStatus (declaration i=24184, owner"
                            + " i=24183)");
                  }
                } else if (actualRank > 1) {
                  throw new UaException(
                      StatusCodes.Bad_TypeMismatch,
                      "http://opcfoundation.org/UA/:TalkerStatus (declaration i=24184, owner"
                          + " i=24183): use Matrix for multiple dimensions");
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
                        "http://opcfoundation.org/UA/:TalkerStatus (declaration i=24184, owner"
                            + " i=24183): ValueRank=-1 does not permit rank "
                            + rank);
                  }
                  if (rawValue != null && !((Object) rawValue instanceof TsnTalkerStatus)) {
                    if (!(rawValue instanceof Integer)) {
                      throw new UaException(
                          StatusCodes.Bad_TypeMismatch,
                          "http://opcfoundation.org/UA/:TalkerStatus (declaration i=24184, owner"
                              + " i=24183): expected"
                              + " org.eclipse.milo.opcua.stack.core.types.enumerated.TsnTalkerStatus"
                              + " or Int32, got "
                              + rawValue);
                    }
                    if (TsnTalkerStatus.from((Integer) rawValue) == null) {
                      throw new UaException(
                          StatusCodes.Bad_OutOfRange,
                          "http://opcfoundation.org/UA/:TalkerStatus (declaration i=24184, owner"
                              + " i=24183): unknown"
                              + " org.eclipse.milo.opcua.stack.core.types.enumerated.TsnTalkerStatus"
                              + " value "
                              + rawValue);
                    }
                  }
                  converted =
                      rawValue == null || rawValue instanceof TsnTalkerStatus
                          ? (TsnTalkerStatus) rawValue
                          : TsnTalkerStatus.from((Integer) rawValue);
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
  public CompletableFuture<? extends @Nullable TsnTalkerStatus> readTalkerStatusAsync() {
    try {
      this.views.checkOpen();
      return this.views.ownRequest(this.readTalkerStatusAsyncImplementation());
    } catch (RuntimeException viewFailure) {
      return CompletableFuture.failedFuture(viewFailure);
    }
  }

  @Override
  public void writeTalkerStatus(@Nullable TsnTalkerStatus value) throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    views.checkOpen();
    StatusCode status = ViewFutures.await(writeTalkerStatusAsync(value));
    if (status == null) {
      throw new UaException(StatusCodes.Bad_UnexpectedError);
    }
    if (!status.isGood()) {
      throw new UaException(status);
    }
  }

  private CompletableFuture<StatusCode> writeTalkerStatusAsyncImplementation(
      @Nullable TsnTalkerStatus value) {
    return ViewFutures.compose(
        viewMember0Async(),
        child -> {
          if (child == null) {
            throw new CompletionException(
                new UaException(
                    StatusCodes.Bad_NotFound,
                    "http://opcfoundation.org/UA/:TalkerStatus (declaration i=24184, owner i=24183)"
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
                          ExpandedNodeId.parse("i=24222"),
                          -1,
                          new long[] {},
                          Set.of(0, 1, 2),
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
  public CompletableFuture<StatusCode> writeTalkerStatusAsync(@Nullable TsnTalkerStatus value) {
    try {
      this.views.checkOpen();
      return this.views.ownRequest(this.writeTalkerStatusAsyncImplementation(value));
    } catch (RuntimeException viewFailure) {
      return CompletableFuture.failedFuture(viewFailure);
    }
  }

  @Override
  public @Nullable BaseDataVariableType getListenerStatusNode() throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    return ViewFutures.await(getListenerStatusNodeAsync());
  }

  private CompletableFuture<? extends @Nullable BaseDataVariableType>
      getListenerStatusNodeAsyncImplementation() {
    return ViewFutures.compose(
        viewMember1Async(),
        child ->
            child == null
                ? CompletableFuture.completedFuture(null)
                : views.wrapVariableAsync(child.getNodeId(), BaseDataVariableTypeView.TYPE));
  }

  @Override
  public CompletableFuture<? extends @Nullable BaseDataVariableType> getListenerStatusNodeAsync() {
    try {
      this.views.checkOpen();
      return this.views.ownRequest(this.getListenerStatusNodeAsyncImplementation());
    } catch (RuntimeException viewFailure) {
      return CompletableFuture.failedFuture(viewFailure);
    }
  }

  @Override
  public @Nullable TsnListenerStatus getListenerStatus() throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    var child = viewMember1();
    if (child == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ListenerStatus (declaration i=24185, owner i=24183)"
              + " on "
              + getNodeId());
    }
    @Nullable TsnListenerStatus converted;
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
              "http://opcfoundation.org/UA/:ListenerStatus (declaration i=24185, owner i=24183)");
        }
        if (rawValue instanceof Matrix matrix) {
          if (actualRank < 2
              || rankElements == null
              || !rankElements.getClass().isArray()
              || ArrayUtil.getValueRank(rankElements) != 1) {
            throw new UaException(
                StatusCodes.Bad_TypeMismatch,
                "http://opcfoundation.org/UA/:ListenerStatus (declaration i=24185, owner i=24183)");
          }
          long elementCount = 1;
          for (int dimension : matrix.getDimensions()) {
            if (dimension < 0 || elementCount > Integer.MAX_VALUE) {
              throw new UaException(
                  StatusCodes.Bad_TypeMismatch,
                  "http://opcfoundation.org/UA/:ListenerStatus (declaration i=24185, owner"
                      + " i=24183)");
            }
            elementCount *= dimension;
          }
          if (elementCount != Array.getLength(rankElements)) {
            throw new UaException(
                StatusCodes.Bad_TypeMismatch,
                "http://opcfoundation.org/UA/:ListenerStatus (declaration i=24185, owner i=24183)");
          }
        } else if (actualRank > 1) {
          throw new UaException(
              StatusCodes.Bad_TypeMismatch,
              "http://opcfoundation.org/UA/:ListenerStatus (declaration i=24185, owner i=24183):"
                  + " use Matrix for multiple dimensions");
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
                "http://opcfoundation.org/UA/:ListenerStatus (declaration i=24185, owner i=24183):"
                    + " ValueRank=-1 does not permit rank "
                    + rank);
          }
          if (rawValue != null && !((Object) rawValue instanceof TsnListenerStatus)) {
            if (!(rawValue instanceof Integer)) {
              throw new UaException(
                  StatusCodes.Bad_TypeMismatch,
                  "http://opcfoundation.org/UA/:ListenerStatus (declaration i=24185, owner"
                      + " i=24183): expected"
                      + " org.eclipse.milo.opcua.stack.core.types.enumerated.TsnListenerStatus or"
                      + " Int32, got "
                      + rawValue);
            }
            if (TsnListenerStatus.from((Integer) rawValue) == null) {
              throw new UaException(
                  StatusCodes.Bad_OutOfRange,
                  "http://opcfoundation.org/UA/:ListenerStatus (declaration i=24185, owner"
                      + " i=24183): unknown"
                      + " org.eclipse.milo.opcua.stack.core.types.enumerated.TsnListenerStatus"
                      + " value "
                      + rawValue);
            }
          }
          converted =
              rawValue == null || rawValue instanceof TsnListenerStatus
                  ? (TsnListenerStatus) rawValue
                  : TsnListenerStatus.from((Integer) rawValue);
        }
      }
    }
    return converted;
  }

  @Override
  public void setListenerStatus(@Nullable TsnListenerStatus value) throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    var child = viewMember1();
    if (child == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ListenerStatus (declaration i=24185, owner i=24183)"
              + " on "
              + getNodeId());
    }
    var writeContext = views.localWriteContext(child.getNodeId(), value, KNOWN_ENUMS);
    Variant encoded =
        ValueChecks.validate(
            writeContext,
            value,
            ExpandedNodeId.parse("i=24224"),
            -1,
            new long[] {},
            Set.of(0, 1, 2, 3),
            false);
    views.checkOpen();
    child.setValue(encoded);
  }

  @Override
  public @Nullable TsnListenerStatus readListenerStatus() throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    return ViewFutures.await(readListenerStatusAsync());
  }

  private CompletableFuture<? extends @Nullable TsnListenerStatus>
      readListenerStatusAsyncImplementation() {
    return ViewFutures.map(
        ViewFutures.compose(
            viewMember1Async(),
            child -> {
              if (child == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:ListenerStatus (declaration i=24185, owner"
                            + " i=24183) on "
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
            @Nullable TsnListenerStatus converted;
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
                      "http://opcfoundation.org/UA/:ListenerStatus (declaration i=24185, owner"
                          + " i=24183)");
                }
                if (rawValue instanceof Matrix matrix) {
                  if (actualRank < 2
                      || rankElements == null
                      || !rankElements.getClass().isArray()
                      || ArrayUtil.getValueRank(rankElements) != 1) {
                    throw new UaException(
                        StatusCodes.Bad_TypeMismatch,
                        "http://opcfoundation.org/UA/:ListenerStatus (declaration i=24185, owner"
                            + " i=24183)");
                  }
                  long elementCount = 1;
                  for (int dimension : matrix.getDimensions()) {
                    if (dimension < 0 || elementCount > Integer.MAX_VALUE) {
                      throw new UaException(
                          StatusCodes.Bad_TypeMismatch,
                          "http://opcfoundation.org/UA/:ListenerStatus (declaration i=24185, owner"
                              + " i=24183)");
                    }
                    elementCount *= dimension;
                  }
                  if (elementCount != Array.getLength(rankElements)) {
                    throw new UaException(
                        StatusCodes.Bad_TypeMismatch,
                        "http://opcfoundation.org/UA/:ListenerStatus (declaration i=24185, owner"
                            + " i=24183)");
                  }
                } else if (actualRank > 1) {
                  throw new UaException(
                      StatusCodes.Bad_TypeMismatch,
                      "http://opcfoundation.org/UA/:ListenerStatus (declaration i=24185, owner"
                          + " i=24183): use Matrix for multiple dimensions");
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
                        "http://opcfoundation.org/UA/:ListenerStatus (declaration i=24185, owner"
                            + " i=24183): ValueRank=-1 does not permit rank "
                            + rank);
                  }
                  if (rawValue != null && !((Object) rawValue instanceof TsnListenerStatus)) {
                    if (!(rawValue instanceof Integer)) {
                      throw new UaException(
                          StatusCodes.Bad_TypeMismatch,
                          "http://opcfoundation.org/UA/:ListenerStatus (declaration i=24185, owner"
                              + " i=24183): expected"
                              + " org.eclipse.milo.opcua.stack.core.types.enumerated.TsnListenerStatus"
                              + " or Int32, got "
                              + rawValue);
                    }
                    if (TsnListenerStatus.from((Integer) rawValue) == null) {
                      throw new UaException(
                          StatusCodes.Bad_OutOfRange,
                          "http://opcfoundation.org/UA/:ListenerStatus (declaration i=24185, owner"
                              + " i=24183): unknown"
                              + " org.eclipse.milo.opcua.stack.core.types.enumerated.TsnListenerStatus"
                              + " value "
                              + rawValue);
                    }
                  }
                  converted =
                      rawValue == null || rawValue instanceof TsnListenerStatus
                          ? (TsnListenerStatus) rawValue
                          : TsnListenerStatus.from((Integer) rawValue);
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
  public CompletableFuture<? extends @Nullable TsnListenerStatus> readListenerStatusAsync() {
    try {
      this.views.checkOpen();
      return this.views.ownRequest(this.readListenerStatusAsyncImplementation());
    } catch (RuntimeException viewFailure) {
      return CompletableFuture.failedFuture(viewFailure);
    }
  }

  @Override
  public void writeListenerStatus(@Nullable TsnListenerStatus value) throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    views.checkOpen();
    StatusCode status = ViewFutures.await(writeListenerStatusAsync(value));
    if (status == null) {
      throw new UaException(StatusCodes.Bad_UnexpectedError);
    }
    if (!status.isGood()) {
      throw new UaException(status);
    }
  }

  private CompletableFuture<StatusCode> writeListenerStatusAsyncImplementation(
      @Nullable TsnListenerStatus value) {
    return ViewFutures.compose(
        viewMember1Async(),
        child -> {
          if (child == null) {
            throw new CompletionException(
                new UaException(
                    StatusCodes.Bad_NotFound,
                    "http://opcfoundation.org/UA/:ListenerStatus (declaration i=24185, owner"
                        + " i=24183) on "
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
                          ExpandedNodeId.parse("i=24224"),
                          -1,
                          new long[] {},
                          Set.of(0, 1, 2, 3),
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
  public CompletableFuture<StatusCode> writeListenerStatusAsync(@Nullable TsnListenerStatus value) {
    try {
      this.views.checkOpen();
      return this.views.ownRequest(this.writeListenerStatusAsyncImplementation(value));
    } catch (RuntimeException viewFailure) {
      return CompletableFuture.failedFuture(viewFailure);
    }
  }

  @Override
  public BaseDataVariableType getFailureCodeNode() throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    return ViewFutures.await(getFailureCodeNodeAsync());
  }

  private CompletableFuture<? extends BaseDataVariableType>
      getFailureCodeNodeAsyncImplementation() {
    return ViewFutures.compose(
        viewMember2Async(),
        child ->
            child == null
                ? CompletableFuture.completedFuture(null)
                : views.wrapVariableAsync(child.getNodeId(), BaseDataVariableTypeView.TYPE));
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableType> getFailureCodeNodeAsync() {
    try {
      this.views.checkOpen();
      return this.views.ownRequest(this.getFailureCodeNodeAsyncImplementation());
    } catch (RuntimeException viewFailure) {
      return CompletableFuture.failedFuture(viewFailure);
    }
  }

  @Override
  public @Nullable TsnFailureCode getFailureCode() throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    var child = viewMember2();
    if (child == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:FailureCode (declaration i=24186, owner i=24183)"
              + " on "
              + getNodeId());
    }
    @Nullable TsnFailureCode converted;
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
              "http://opcfoundation.org/UA/:FailureCode (declaration i=24186, owner i=24183)");
        }
        if (rawValue instanceof Matrix matrix) {
          if (actualRank < 2
              || rankElements == null
              || !rankElements.getClass().isArray()
              || ArrayUtil.getValueRank(rankElements) != 1) {
            throw new UaException(
                StatusCodes.Bad_TypeMismatch,
                "http://opcfoundation.org/UA/:FailureCode (declaration i=24186, owner i=24183)");
          }
          long elementCount = 1;
          for (int dimension : matrix.getDimensions()) {
            if (dimension < 0 || elementCount > Integer.MAX_VALUE) {
              throw new UaException(
                  StatusCodes.Bad_TypeMismatch,
                  "http://opcfoundation.org/UA/:FailureCode (declaration i=24186, owner i=24183)");
            }
            elementCount *= dimension;
          }
          if (elementCount != Array.getLength(rankElements)) {
            throw new UaException(
                StatusCodes.Bad_TypeMismatch,
                "http://opcfoundation.org/UA/:FailureCode (declaration i=24186, owner i=24183)");
          }
        } else if (actualRank > 1) {
          throw new UaException(
              StatusCodes.Bad_TypeMismatch,
              "http://opcfoundation.org/UA/:FailureCode (declaration i=24186, owner i=24183): use"
                  + " Matrix for multiple dimensions");
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
                "http://opcfoundation.org/UA/:FailureCode (declaration i=24186, owner i=24183):"
                    + " ValueRank=-1 does not permit rank "
                    + rank);
          }
          if (rawValue != null && !((Object) rawValue instanceof TsnFailureCode)) {
            if (!(rawValue instanceof Integer)) {
              throw new UaException(
                  StatusCodes.Bad_TypeMismatch,
                  "http://opcfoundation.org/UA/:FailureCode (declaration i=24186, owner i=24183):"
                      + " expected"
                      + " org.eclipse.milo.opcua.stack.core.types.enumerated.TsnFailureCode or"
                      + " Int32, got "
                      + rawValue);
            }
            if (TsnFailureCode.from((Integer) rawValue) == null) {
              throw new UaException(
                  StatusCodes.Bad_OutOfRange,
                  "http://opcfoundation.org/UA/:FailureCode (declaration i=24186, owner i=24183):"
                      + " unknown org.eclipse.milo.opcua.stack.core.types.enumerated.TsnFailureCode"
                      + " value "
                      + rawValue);
            }
          }
          converted =
              rawValue == null || rawValue instanceof TsnFailureCode
                  ? (TsnFailureCode) rawValue
                  : TsnFailureCode.from((Integer) rawValue);
        }
      }
    }
    return converted;
  }

  @Override
  public void setFailureCode(@Nullable TsnFailureCode value) throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    var child = viewMember2();
    if (child == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:FailureCode (declaration i=24186, owner i=24183)"
              + " on "
              + getNodeId());
    }
    var writeContext = views.localWriteContext(child.getNodeId(), value, KNOWN_ENUMS);
    Variant encoded =
        ValueChecks.validate(
            writeContext,
            value,
            ExpandedNodeId.parse("i=24218"),
            -1,
            new long[] {},
            Set.of(
                0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22,
                23, 24, 25),
            false);
    views.checkOpen();
    child.setValue(encoded);
  }

  @Override
  public @Nullable TsnFailureCode readFailureCode() throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    return ViewFutures.await(readFailureCodeAsync());
  }

  private CompletableFuture<? extends @Nullable TsnFailureCode>
      readFailureCodeAsyncImplementation() {
    return ViewFutures.map(
        ViewFutures.compose(
            viewMember2Async(),
            child -> {
              if (child == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:FailureCode (declaration i=24186, owner"
                            + " i=24183) on "
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
            @Nullable TsnFailureCode converted;
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
                      "http://opcfoundation.org/UA/:FailureCode (declaration i=24186, owner"
                          + " i=24183)");
                }
                if (rawValue instanceof Matrix matrix) {
                  if (actualRank < 2
                      || rankElements == null
                      || !rankElements.getClass().isArray()
                      || ArrayUtil.getValueRank(rankElements) != 1) {
                    throw new UaException(
                        StatusCodes.Bad_TypeMismatch,
                        "http://opcfoundation.org/UA/:FailureCode (declaration i=24186, owner"
                            + " i=24183)");
                  }
                  long elementCount = 1;
                  for (int dimension : matrix.getDimensions()) {
                    if (dimension < 0 || elementCount > Integer.MAX_VALUE) {
                      throw new UaException(
                          StatusCodes.Bad_TypeMismatch,
                          "http://opcfoundation.org/UA/:FailureCode (declaration i=24186, owner"
                              + " i=24183)");
                    }
                    elementCount *= dimension;
                  }
                  if (elementCount != Array.getLength(rankElements)) {
                    throw new UaException(
                        StatusCodes.Bad_TypeMismatch,
                        "http://opcfoundation.org/UA/:FailureCode (declaration i=24186, owner"
                            + " i=24183)");
                  }
                } else if (actualRank > 1) {
                  throw new UaException(
                      StatusCodes.Bad_TypeMismatch,
                      "http://opcfoundation.org/UA/:FailureCode (declaration i=24186, owner"
                          + " i=24183): use Matrix for multiple dimensions");
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
                        "http://opcfoundation.org/UA/:FailureCode (declaration i=24186, owner"
                            + " i=24183): ValueRank=-1 does not permit rank "
                            + rank);
                  }
                  if (rawValue != null && !((Object) rawValue instanceof TsnFailureCode)) {
                    if (!(rawValue instanceof Integer)) {
                      throw new UaException(
                          StatusCodes.Bad_TypeMismatch,
                          "http://opcfoundation.org/UA/:FailureCode (declaration i=24186, owner"
                              + " i=24183): expected"
                              + " org.eclipse.milo.opcua.stack.core.types.enumerated.TsnFailureCode"
                              + " or Int32, got "
                              + rawValue);
                    }
                    if (TsnFailureCode.from((Integer) rawValue) == null) {
                      throw new UaException(
                          StatusCodes.Bad_OutOfRange,
                          "http://opcfoundation.org/UA/:FailureCode (declaration i=24186, owner"
                              + " i=24183): unknown"
                              + " org.eclipse.milo.opcua.stack.core.types.enumerated.TsnFailureCode"
                              + " value "
                              + rawValue);
                    }
                  }
                  converted =
                      rawValue == null || rawValue instanceof TsnFailureCode
                          ? (TsnFailureCode) rawValue
                          : TsnFailureCode.from((Integer) rawValue);
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
  public CompletableFuture<? extends @Nullable TsnFailureCode> readFailureCodeAsync() {
    try {
      this.views.checkOpen();
      return this.views.ownRequest(this.readFailureCodeAsyncImplementation());
    } catch (RuntimeException viewFailure) {
      return CompletableFuture.failedFuture(viewFailure);
    }
  }

  @Override
  public void writeFailureCode(@Nullable TsnFailureCode value) throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    views.checkOpen();
    StatusCode status = ViewFutures.await(writeFailureCodeAsync(value));
    if (status == null) {
      throw new UaException(StatusCodes.Bad_UnexpectedError);
    }
    if (!status.isGood()) {
      throw new UaException(status);
    }
  }

  private CompletableFuture<StatusCode> writeFailureCodeAsyncImplementation(
      @Nullable TsnFailureCode value) {
    return ViewFutures.compose(
        viewMember2Async(),
        child -> {
          if (child == null) {
            throw new CompletionException(
                new UaException(
                    StatusCodes.Bad_NotFound,
                    "http://opcfoundation.org/UA/:FailureCode (declaration i=24186, owner i=24183)"
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
                          ExpandedNodeId.parse("i=24218"),
                          -1,
                          new long[] {},
                          Set.of(
                              0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19,
                              20, 21, 22, 23, 24, 25),
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
  public CompletableFuture<StatusCode> writeFailureCodeAsync(@Nullable TsnFailureCode value) {
    try {
      this.views.checkOpen();
      return this.views.ownRequest(this.writeFailureCodeAsyncImplementation(value));
    } catch (RuntimeException viewFailure) {
      return CompletableFuture.failedFuture(viewFailure);
    }
  }

  @Override
  public BaseDataVariableType getFailureSystemIdentifierNode() throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    return ViewFutures.await(getFailureSystemIdentifierNodeAsync());
  }

  private CompletableFuture<? extends BaseDataVariableType>
      getFailureSystemIdentifierNodeAsyncImplementation() {
    return ViewFutures.compose(
        viewMember3Async(),
        child ->
            child == null
                ? CompletableFuture.completedFuture(null)
                : views.wrapVariableAsync(child.getNodeId(), BaseDataVariableTypeView.TYPE));
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableType> getFailureSystemIdentifierNodeAsync() {
    try {
      this.views.checkOpen();
      return this.views.ownRequest(this.getFailureSystemIdentifierNodeAsyncImplementation());
    } catch (RuntimeException viewFailure) {
      return CompletableFuture.failedFuture(viewFailure);
    }
  }

  @Override
  public @Nullable Object getFailureSystemIdentifier() throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    var child = viewMember3();
    if (child == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:FailureSystemIdentifier (declaration i=24187, owner"
              + " i=24183) on "
              + getNodeId());
    }
    @Nullable Object converted;
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
        if (!(actualRank == 2
            || (!(rawValue instanceof Matrix)
                && actualRank == 1
                && Array.getLength(rankElements) == 0))) {
          throw new UaException(
              StatusCodes.Bad_TypeMismatch,
              "http://opcfoundation.org/UA/:FailureSystemIdentifier (declaration i=24187, owner"
                  + " i=24183)");
        }
        if (rawValue instanceof Matrix matrix) {
          if (actualRank < 2
              || rankElements == null
              || !rankElements.getClass().isArray()
              || ArrayUtil.getValueRank(rankElements) != 1) {
            throw new UaException(
                StatusCodes.Bad_TypeMismatch,
                "http://opcfoundation.org/UA/:FailureSystemIdentifier (declaration i=24187, owner"
                    + " i=24183)");
          }
          long elementCount = 1;
          for (int dimension : matrix.getDimensions()) {
            if (dimension < 0 || elementCount > Integer.MAX_VALUE) {
              throw new UaException(
                  StatusCodes.Bad_TypeMismatch,
                  "http://opcfoundation.org/UA/:FailureSystemIdentifier (declaration i=24187, owner"
                      + " i=24183)");
            }
            elementCount *= dimension;
          }
          if (elementCount != Array.getLength(rankElements)) {
            throw new UaException(
                StatusCodes.Bad_TypeMismatch,
                "http://opcfoundation.org/UA/:FailureSystemIdentifier (declaration i=24187, owner"
                    + " i=24183)");
          }
        } else if (actualRank > 1) {
          throw new UaException(
              StatusCodes.Bad_TypeMismatch,
              "http://opcfoundation.org/UA/:FailureSystemIdentifier (declaration i=24187, owner"
                  + " i=24183): use Matrix for multiple dimensions");
        }
      }
      Object elements = rawValue instanceof Matrix matrix ? matrix.getElements() : rawValue;
      if (elements != null && elements.getClass().isArray()) {
        for (int index = 0; index < Array.getLength(elements); index++) {
          Object element = Array.get(elements, index);
          if (element != null && !(element instanceof UByte)) {
            throw new UaException(
                StatusCodes.Bad_TypeMismatch,
                "http://opcfoundation.org/UA/:FailureSystemIdentifier (declaration i=24187, owner"
                    + " i=24183)");
          }
        }
        converted = rawValue;
      } else {
        Object element = elements;
        if (element != null && !(element instanceof UByte)) {
          throw new UaException(
              StatusCodes.Bad_TypeMismatch,
              "http://opcfoundation.org/UA/:FailureSystemIdentifier (declaration i=24187, owner"
                  + " i=24183)");
        }
        converted = element;
      }
    }
    return converted;
  }

  @Override
  public void setFailureSystemIdentifier(@Nullable Object value) throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    var child = viewMember3();
    if (child == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:FailureSystemIdentifier (declaration i=24187, owner"
              + " i=24183) on "
              + getNodeId());
    }
    var writeContext = views.localWriteContext(child.getNodeId(), value, KNOWN_ENUMS);
    Variant encoded =
        ValueChecks.validate(
            writeContext, value, ExpandedNodeId.parse("i=3"), 2, new long[] {0L, 8L}, null, false);
    views.checkOpen();
    child.setValue(encoded);
  }

  @Override
  public @Nullable Object readFailureSystemIdentifier() throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    return ViewFutures.await(readFailureSystemIdentifierAsync());
  }

  private CompletableFuture<? extends @Nullable Object>
      readFailureSystemIdentifierAsyncImplementation() {
    return ViewFutures.map(
        ViewFutures.compose(
            viewMember3Async(),
            child -> {
              if (child == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:FailureSystemIdentifier (declaration i=24187,"
                            + " owner i=24183) on "
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
            @Nullable Object converted;
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
                if (!(actualRank == 2
                    || (!(rawValue instanceof Matrix)
                        && actualRank == 1
                        && Array.getLength(rankElements) == 0))) {
                  throw new UaException(
                      StatusCodes.Bad_TypeMismatch,
                      "http://opcfoundation.org/UA/:FailureSystemIdentifier (declaration i=24187,"
                          + " owner i=24183)");
                }
                if (rawValue instanceof Matrix matrix) {
                  if (actualRank < 2
                      || rankElements == null
                      || !rankElements.getClass().isArray()
                      || ArrayUtil.getValueRank(rankElements) != 1) {
                    throw new UaException(
                        StatusCodes.Bad_TypeMismatch,
                        "http://opcfoundation.org/UA/:FailureSystemIdentifier (declaration i=24187,"
                            + " owner i=24183)");
                  }
                  long elementCount = 1;
                  for (int dimension : matrix.getDimensions()) {
                    if (dimension < 0 || elementCount > Integer.MAX_VALUE) {
                      throw new UaException(
                          StatusCodes.Bad_TypeMismatch,
                          "http://opcfoundation.org/UA/:FailureSystemIdentifier (declaration"
                              + " i=24187, owner i=24183)");
                    }
                    elementCount *= dimension;
                  }
                  if (elementCount != Array.getLength(rankElements)) {
                    throw new UaException(
                        StatusCodes.Bad_TypeMismatch,
                        "http://opcfoundation.org/UA/:FailureSystemIdentifier (declaration i=24187,"
                            + " owner i=24183)");
                  }
                } else if (actualRank > 1) {
                  throw new UaException(
                      StatusCodes.Bad_TypeMismatch,
                      "http://opcfoundation.org/UA/:FailureSystemIdentifier (declaration i=24187,"
                          + " owner i=24183): use Matrix for multiple dimensions");
                }
              }
              Object elements = rawValue instanceof Matrix matrix ? matrix.getElements() : rawValue;
              if (elements != null && elements.getClass().isArray()) {
                for (int index = 0; index < Array.getLength(elements); index++) {
                  Object element = Array.get(elements, index);
                  if (element != null && !(element instanceof UByte)) {
                    throw new UaException(
                        StatusCodes.Bad_TypeMismatch,
                        "http://opcfoundation.org/UA/:FailureSystemIdentifier (declaration i=24187,"
                            + " owner i=24183)");
                  }
                }
                converted = rawValue;
              } else {
                Object element = elements;
                if (element != null && !(element instanceof UByte)) {
                  throw new UaException(
                      StatusCodes.Bad_TypeMismatch,
                      "http://opcfoundation.org/UA/:FailureSystemIdentifier (declaration i=24187,"
                          + " owner i=24183)");
                }
                converted = element;
              }
            }
            return converted;
          } catch (UaException failure) {
            throw new CompletionException(failure);
          }
        });
  }

  @Override
  public CompletableFuture<? extends @Nullable Object> readFailureSystemIdentifierAsync() {
    try {
      this.views.checkOpen();
      return this.views.ownRequest(this.readFailureSystemIdentifierAsyncImplementation());
    } catch (RuntimeException viewFailure) {
      return CompletableFuture.failedFuture(viewFailure);
    }
  }

  @Override
  public void writeFailureSystemIdentifier(@Nullable Object value) throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    views.checkOpen();
    StatusCode status = ViewFutures.await(writeFailureSystemIdentifierAsync(value));
    if (status == null) {
      throw new UaException(StatusCodes.Bad_UnexpectedError);
    }
    if (!status.isGood()) {
      throw new UaException(status);
    }
  }

  private CompletableFuture<StatusCode> writeFailureSystemIdentifierAsyncImplementation(
      @Nullable Object value) {
    return ViewFutures.compose(
        viewMember3Async(),
        child -> {
          if (child == null) {
            throw new CompletionException(
                new UaException(
                    StatusCodes.Bad_NotFound,
                    "http://opcfoundation.org/UA/:FailureSystemIdentifier (declaration i=24187,"
                        + " owner i=24183) on "
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
                          2,
                          new long[] {0L, 8L},
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
  public CompletableFuture<StatusCode> writeFailureSystemIdentifierAsync(@Nullable Object value) {
    try {
      this.views.checkOpen();
      return this.views.ownRequest(this.writeFailureSystemIdentifierAsyncImplementation(value));
    } catch (RuntimeException viewFailure) {
      return CompletableFuture.failedFuture(viewFailure);
    }
  }
}
