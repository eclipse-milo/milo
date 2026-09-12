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
import org.eclipse.milo.opcua.sdk.client.model.variables.AnalogUnitType;
import org.eclipse.milo.opcua.sdk.client.model.variables.AnalogUnitTypeView;
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
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.ULong;
import org.eclipse.milo.opcua.stack.core.types.enumerated.InterfaceAdminStatus;
import org.eclipse.milo.opcua.stack.core.types.enumerated.InterfaceOperStatus;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.util.ArrayUtil;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * https://reference.opcfoundation.org/v105/Core/docs/Part22/5.2.1
 *
 * <p>Selected <code>IIetfBaseNetworkInterfaceType</code> contract over one retained backing node.
 * Obtain this view from a ClientViews context using {@link #TYPE}; the token itself does not prove
 * membership. Access after context close fails with IllegalStateException.
 */
@NullMarked
public final class IIetfBaseNetworkInterfaceTypeView extends ClientObjectView
    implements IIetfBaseNetworkInterfaceType {
  /** Selected UA/Java contract and context-owned factory. */
  public static final ClientViewType<IIetfBaseNetworkInterfaceType> TYPE =
      ClientViewType.of(
          ExpandedNodeId.parse("i=24148"),
          IIetfBaseNetworkInterfaceType.class,
          IIetfBaseNetworkInterfaceTypeView::new);

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

  private IIetfBaseNetworkInterfaceTypeView(ClientViews views, UaObjectNode node) {
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
                            "AdminStatus",
                            ExpandedNodeId.parse("i=47"),
                            true,
                            NodeClass.Variable,
                            false,
                            "http://opcfoundation.org/UA/:AdminStatus (declaration i=24149, owner"
                                + " i=24148)"));
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
                            "OperStatus",
                            ExpandedNodeId.parse("i=47"),
                            true,
                            NodeClass.Variable,
                            false,
                            "http://opcfoundation.org/UA/:OperStatus (declaration i=24150, owner"
                                + " i=24148)"));
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

  private CompletableFuture<@Nullable UaVariableNode> viewMember2Async() {
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
                            "PhysAddress",
                            ExpandedNodeId.parse("i=47"),
                            true,
                            NodeClass.Variable,
                            true,
                            "http://opcfoundation.org/UA/:PhysAddress (declaration i=24151, owner"
                                + " i=24148)"));
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

  private @Nullable UaVariableNode viewMember2() throws UaException {
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
                            "Speed",
                            ExpandedNodeId.parse("i=47"),
                            true,
                            NodeClass.Variable,
                            false,
                            "http://opcfoundation.org/UA/:Speed (declaration i=24152, owner"
                                + " i=24148)"));
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
  public BaseDataVariableType getAdminStatusNode() throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    return ViewFutures.await(getAdminStatusNodeAsync());
  }

  private CompletableFuture<? extends BaseDataVariableType>
      getAdminStatusNodeAsyncImplementation() {
    return ViewFutures.compose(
        viewMember0Async(),
        child ->
            child == null
                ? CompletableFuture.completedFuture(null)
                : views.wrapVariableAsync(child.getNodeId(), BaseDataVariableTypeView.TYPE));
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableType> getAdminStatusNodeAsync() {
    try {
      this.views.checkOpen();
      return this.views.ownRequest(this.getAdminStatusNodeAsyncImplementation());
    } catch (RuntimeException viewFailure) {
      return CompletableFuture.failedFuture(viewFailure);
    }
  }

  @Override
  public @Nullable InterfaceAdminStatus getAdminStatus() throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    var child = viewMember0();
    if (child == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:AdminStatus (declaration i=24149, owner i=24148)"
              + " on "
              + getNodeId());
    }
    @Nullable InterfaceAdminStatus converted;
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
              "http://opcfoundation.org/UA/:AdminStatus (declaration i=24149, owner i=24148)");
        }
        if (rawValue instanceof Matrix matrix) {
          if (actualRank < 2
              || rankElements == null
              || !rankElements.getClass().isArray()
              || ArrayUtil.getValueRank(rankElements) != 1) {
            throw new UaException(
                StatusCodes.Bad_TypeMismatch,
                "http://opcfoundation.org/UA/:AdminStatus (declaration i=24149, owner i=24148)");
          }
          long elementCount = 1;
          for (int dimension : matrix.getDimensions()) {
            if (dimension < 0 || elementCount > Integer.MAX_VALUE) {
              throw new UaException(
                  StatusCodes.Bad_TypeMismatch,
                  "http://opcfoundation.org/UA/:AdminStatus (declaration i=24149, owner i=24148)");
            }
            elementCount *= dimension;
          }
          if (elementCount != Array.getLength(rankElements)) {
            throw new UaException(
                StatusCodes.Bad_TypeMismatch,
                "http://opcfoundation.org/UA/:AdminStatus (declaration i=24149, owner i=24148)");
          }
        } else if (actualRank > 1) {
          throw new UaException(
              StatusCodes.Bad_TypeMismatch,
              "http://opcfoundation.org/UA/:AdminStatus (declaration i=24149, owner i=24148): use"
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
                "http://opcfoundation.org/UA/:AdminStatus (declaration i=24149, owner i=24148):"
                    + " ValueRank=-1 does not permit rank "
                    + rank);
          }
          if (rawValue != null && !((Object) rawValue instanceof InterfaceAdminStatus)) {
            if (!(rawValue instanceof Integer)) {
              throw new UaException(
                  StatusCodes.Bad_TypeMismatch,
                  "http://opcfoundation.org/UA/:AdminStatus (declaration i=24149, owner i=24148):"
                      + " expected"
                      + " org.eclipse.milo.opcua.stack.core.types.enumerated.InterfaceAdminStatus"
                      + " or Int32, got "
                      + rawValue);
            }
            if (InterfaceAdminStatus.from((Integer) rawValue) == null) {
              throw new UaException(
                  StatusCodes.Bad_OutOfRange,
                  "http://opcfoundation.org/UA/:AdminStatus (declaration i=24149, owner i=24148):"
                      + " unknown"
                      + " org.eclipse.milo.opcua.stack.core.types.enumerated.InterfaceAdminStatus"
                      + " value "
                      + rawValue);
            }
          }
          converted =
              rawValue == null || rawValue instanceof InterfaceAdminStatus
                  ? (InterfaceAdminStatus) rawValue
                  : InterfaceAdminStatus.from((Integer) rawValue);
        }
      }
    }
    return converted;
  }

  @Override
  public void setAdminStatus(@Nullable InterfaceAdminStatus value) throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    var child = viewMember0();
    if (child == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:AdminStatus (declaration i=24149, owner i=24148)"
              + " on "
              + getNodeId());
    }
    var writeContext = views.localWriteContext(child.getNodeId(), value, KNOWN_ENUMS);
    Variant encoded =
        ValueChecks.validate(
            writeContext,
            value,
            ExpandedNodeId.parse("i=24212"),
            -1,
            new long[] {},
            Set.of(0, 1, 2),
            false);
    views.checkOpen();
    child.setValue(encoded);
  }

  @Override
  public @Nullable InterfaceAdminStatus readAdminStatus() throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    return ViewFutures.await(readAdminStatusAsync());
  }

  private CompletableFuture<? extends @Nullable InterfaceAdminStatus>
      readAdminStatusAsyncImplementation() {
    return ViewFutures.map(
        ViewFutures.compose(
            viewMember0Async(),
            child -> {
              if (child == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:AdminStatus (declaration i=24149, owner"
                            + " i=24148) on "
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
            @Nullable InterfaceAdminStatus converted;
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
                      "http://opcfoundation.org/UA/:AdminStatus (declaration i=24149, owner"
                          + " i=24148)");
                }
                if (rawValue instanceof Matrix matrix) {
                  if (actualRank < 2
                      || rankElements == null
                      || !rankElements.getClass().isArray()
                      || ArrayUtil.getValueRank(rankElements) != 1) {
                    throw new UaException(
                        StatusCodes.Bad_TypeMismatch,
                        "http://opcfoundation.org/UA/:AdminStatus (declaration i=24149, owner"
                            + " i=24148)");
                  }
                  long elementCount = 1;
                  for (int dimension : matrix.getDimensions()) {
                    if (dimension < 0 || elementCount > Integer.MAX_VALUE) {
                      throw new UaException(
                          StatusCodes.Bad_TypeMismatch,
                          "http://opcfoundation.org/UA/:AdminStatus (declaration i=24149, owner"
                              + " i=24148)");
                    }
                    elementCount *= dimension;
                  }
                  if (elementCount != Array.getLength(rankElements)) {
                    throw new UaException(
                        StatusCodes.Bad_TypeMismatch,
                        "http://opcfoundation.org/UA/:AdminStatus (declaration i=24149, owner"
                            + " i=24148)");
                  }
                } else if (actualRank > 1) {
                  throw new UaException(
                      StatusCodes.Bad_TypeMismatch,
                      "http://opcfoundation.org/UA/:AdminStatus (declaration i=24149, owner"
                          + " i=24148): use Matrix for multiple dimensions");
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
                        "http://opcfoundation.org/UA/:AdminStatus (declaration i=24149, owner"
                            + " i=24148): ValueRank=-1 does not permit rank "
                            + rank);
                  }
                  if (rawValue != null && !((Object) rawValue instanceof InterfaceAdminStatus)) {
                    if (!(rawValue instanceof Integer)) {
                      throw new UaException(
                          StatusCodes.Bad_TypeMismatch,
                          "http://opcfoundation.org/UA/:AdminStatus (declaration i=24149, owner"
                              + " i=24148): expected"
                              + " org.eclipse.milo.opcua.stack.core.types.enumerated.InterfaceAdminStatus"
                              + " or Int32, got "
                              + rawValue);
                    }
                    if (InterfaceAdminStatus.from((Integer) rawValue) == null) {
                      throw new UaException(
                          StatusCodes.Bad_OutOfRange,
                          "http://opcfoundation.org/UA/:AdminStatus (declaration i=24149, owner"
                              + " i=24148): unknown"
                              + " org.eclipse.milo.opcua.stack.core.types.enumerated.InterfaceAdminStatus"
                              + " value "
                              + rawValue);
                    }
                  }
                  converted =
                      rawValue == null || rawValue instanceof InterfaceAdminStatus
                          ? (InterfaceAdminStatus) rawValue
                          : InterfaceAdminStatus.from((Integer) rawValue);
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
  public CompletableFuture<? extends @Nullable InterfaceAdminStatus> readAdminStatusAsync() {
    try {
      this.views.checkOpen();
      return this.views.ownRequest(this.readAdminStatusAsyncImplementation());
    } catch (RuntimeException viewFailure) {
      return CompletableFuture.failedFuture(viewFailure);
    }
  }

  @Override
  public void writeAdminStatus(@Nullable InterfaceAdminStatus value) throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    views.checkOpen();
    StatusCode status = ViewFutures.await(writeAdminStatusAsync(value));
    if (status == null) {
      throw new UaException(StatusCodes.Bad_UnexpectedError);
    }
    if (!status.isGood()) {
      throw new UaException(status);
    }
  }

  private CompletableFuture<StatusCode> writeAdminStatusAsyncImplementation(
      @Nullable InterfaceAdminStatus value) {
    return ViewFutures.compose(
        viewMember0Async(),
        child -> {
          if (child == null) {
            throw new CompletionException(
                new UaException(
                    StatusCodes.Bad_NotFound,
                    "http://opcfoundation.org/UA/:AdminStatus (declaration i=24149, owner i=24148)"
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
                          ExpandedNodeId.parse("i=24212"),
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
  public CompletableFuture<StatusCode> writeAdminStatusAsync(@Nullable InterfaceAdminStatus value) {
    try {
      this.views.checkOpen();
      return this.views.ownRequest(this.writeAdminStatusAsyncImplementation(value));
    } catch (RuntimeException viewFailure) {
      return CompletableFuture.failedFuture(viewFailure);
    }
  }

  @Override
  public BaseDataVariableType getOperStatusNode() throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    return ViewFutures.await(getOperStatusNodeAsync());
  }

  private CompletableFuture<? extends BaseDataVariableType> getOperStatusNodeAsyncImplementation() {
    return ViewFutures.compose(
        viewMember1Async(),
        child ->
            child == null
                ? CompletableFuture.completedFuture(null)
                : views.wrapVariableAsync(child.getNodeId(), BaseDataVariableTypeView.TYPE));
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableType> getOperStatusNodeAsync() {
    try {
      this.views.checkOpen();
      return this.views.ownRequest(this.getOperStatusNodeAsyncImplementation());
    } catch (RuntimeException viewFailure) {
      return CompletableFuture.failedFuture(viewFailure);
    }
  }

  @Override
  public @Nullable InterfaceOperStatus getOperStatus() throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    var child = viewMember1();
    if (child == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:OperStatus (declaration i=24150, owner i=24148)"
              + " on "
              + getNodeId());
    }
    @Nullable InterfaceOperStatus converted;
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
              "http://opcfoundation.org/UA/:OperStatus (declaration i=24150, owner i=24148)");
        }
        if (rawValue instanceof Matrix matrix) {
          if (actualRank < 2
              || rankElements == null
              || !rankElements.getClass().isArray()
              || ArrayUtil.getValueRank(rankElements) != 1) {
            throw new UaException(
                StatusCodes.Bad_TypeMismatch,
                "http://opcfoundation.org/UA/:OperStatus (declaration i=24150, owner i=24148)");
          }
          long elementCount = 1;
          for (int dimension : matrix.getDimensions()) {
            if (dimension < 0 || elementCount > Integer.MAX_VALUE) {
              throw new UaException(
                  StatusCodes.Bad_TypeMismatch,
                  "http://opcfoundation.org/UA/:OperStatus (declaration i=24150, owner i=24148)");
            }
            elementCount *= dimension;
          }
          if (elementCount != Array.getLength(rankElements)) {
            throw new UaException(
                StatusCodes.Bad_TypeMismatch,
                "http://opcfoundation.org/UA/:OperStatus (declaration i=24150, owner i=24148)");
          }
        } else if (actualRank > 1) {
          throw new UaException(
              StatusCodes.Bad_TypeMismatch,
              "http://opcfoundation.org/UA/:OperStatus (declaration i=24150, owner i=24148): use"
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
                "http://opcfoundation.org/UA/:OperStatus (declaration i=24150, owner i=24148):"
                    + " ValueRank=-1 does not permit rank "
                    + rank);
          }
          if (rawValue != null && !((Object) rawValue instanceof InterfaceOperStatus)) {
            if (!(rawValue instanceof Integer)) {
              throw new UaException(
                  StatusCodes.Bad_TypeMismatch,
                  "http://opcfoundation.org/UA/:OperStatus (declaration i=24150, owner i=24148):"
                      + " expected"
                      + " org.eclipse.milo.opcua.stack.core.types.enumerated.InterfaceOperStatus or"
                      + " Int32, got "
                      + rawValue);
            }
            if (InterfaceOperStatus.from((Integer) rawValue) == null) {
              throw new UaException(
                  StatusCodes.Bad_OutOfRange,
                  "http://opcfoundation.org/UA/:OperStatus (declaration i=24150, owner i=24148):"
                      + " unknown"
                      + " org.eclipse.milo.opcua.stack.core.types.enumerated.InterfaceOperStatus"
                      + " value "
                      + rawValue);
            }
          }
          converted =
              rawValue == null || rawValue instanceof InterfaceOperStatus
                  ? (InterfaceOperStatus) rawValue
                  : InterfaceOperStatus.from((Integer) rawValue);
        }
      }
    }
    return converted;
  }

  @Override
  public void setOperStatus(@Nullable InterfaceOperStatus value) throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    var child = viewMember1();
    if (child == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:OperStatus (declaration i=24150, owner i=24148)"
              + " on "
              + getNodeId());
    }
    var writeContext = views.localWriteContext(child.getNodeId(), value, KNOWN_ENUMS);
    Variant encoded =
        ValueChecks.validate(
            writeContext,
            value,
            ExpandedNodeId.parse("i=24214"),
            -1,
            new long[] {},
            Set.of(0, 1, 2, 3, 4, 5, 6),
            false);
    views.checkOpen();
    child.setValue(encoded);
  }

  @Override
  public @Nullable InterfaceOperStatus readOperStatus() throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    return ViewFutures.await(readOperStatusAsync());
  }

  private CompletableFuture<? extends @Nullable InterfaceOperStatus>
      readOperStatusAsyncImplementation() {
    return ViewFutures.map(
        ViewFutures.compose(
            viewMember1Async(),
            child -> {
              if (child == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:OperStatus (declaration i=24150, owner"
                            + " i=24148) on "
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
            @Nullable InterfaceOperStatus converted;
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
                      "http://opcfoundation.org/UA/:OperStatus (declaration i=24150, owner"
                          + " i=24148)");
                }
                if (rawValue instanceof Matrix matrix) {
                  if (actualRank < 2
                      || rankElements == null
                      || !rankElements.getClass().isArray()
                      || ArrayUtil.getValueRank(rankElements) != 1) {
                    throw new UaException(
                        StatusCodes.Bad_TypeMismatch,
                        "http://opcfoundation.org/UA/:OperStatus (declaration i=24150, owner"
                            + " i=24148)");
                  }
                  long elementCount = 1;
                  for (int dimension : matrix.getDimensions()) {
                    if (dimension < 0 || elementCount > Integer.MAX_VALUE) {
                      throw new UaException(
                          StatusCodes.Bad_TypeMismatch,
                          "http://opcfoundation.org/UA/:OperStatus (declaration i=24150, owner"
                              + " i=24148)");
                    }
                    elementCount *= dimension;
                  }
                  if (elementCount != Array.getLength(rankElements)) {
                    throw new UaException(
                        StatusCodes.Bad_TypeMismatch,
                        "http://opcfoundation.org/UA/:OperStatus (declaration i=24150, owner"
                            + " i=24148)");
                  }
                } else if (actualRank > 1) {
                  throw new UaException(
                      StatusCodes.Bad_TypeMismatch,
                      "http://opcfoundation.org/UA/:OperStatus (declaration i=24150, owner"
                          + " i=24148): use Matrix for multiple dimensions");
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
                        "http://opcfoundation.org/UA/:OperStatus (declaration i=24150, owner"
                            + " i=24148): ValueRank=-1 does not permit rank "
                            + rank);
                  }
                  if (rawValue != null && !((Object) rawValue instanceof InterfaceOperStatus)) {
                    if (!(rawValue instanceof Integer)) {
                      throw new UaException(
                          StatusCodes.Bad_TypeMismatch,
                          "http://opcfoundation.org/UA/:OperStatus (declaration i=24150, owner"
                              + " i=24148): expected"
                              + " org.eclipse.milo.opcua.stack.core.types.enumerated.InterfaceOperStatus"
                              + " or Int32, got "
                              + rawValue);
                    }
                    if (InterfaceOperStatus.from((Integer) rawValue) == null) {
                      throw new UaException(
                          StatusCodes.Bad_OutOfRange,
                          "http://opcfoundation.org/UA/:OperStatus (declaration i=24150, owner"
                              + " i=24148): unknown"
                              + " org.eclipse.milo.opcua.stack.core.types.enumerated.InterfaceOperStatus"
                              + " value "
                              + rawValue);
                    }
                  }
                  converted =
                      rawValue == null || rawValue instanceof InterfaceOperStatus
                          ? (InterfaceOperStatus) rawValue
                          : InterfaceOperStatus.from((Integer) rawValue);
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
  public CompletableFuture<? extends @Nullable InterfaceOperStatus> readOperStatusAsync() {
    try {
      this.views.checkOpen();
      return this.views.ownRequest(this.readOperStatusAsyncImplementation());
    } catch (RuntimeException viewFailure) {
      return CompletableFuture.failedFuture(viewFailure);
    }
  }

  @Override
  public void writeOperStatus(@Nullable InterfaceOperStatus value) throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    views.checkOpen();
    StatusCode status = ViewFutures.await(writeOperStatusAsync(value));
    if (status == null) {
      throw new UaException(StatusCodes.Bad_UnexpectedError);
    }
    if (!status.isGood()) {
      throw new UaException(status);
    }
  }

  private CompletableFuture<StatusCode> writeOperStatusAsyncImplementation(
      @Nullable InterfaceOperStatus value) {
    return ViewFutures.compose(
        viewMember1Async(),
        child -> {
          if (child == null) {
            throw new CompletionException(
                new UaException(
                    StatusCodes.Bad_NotFound,
                    "http://opcfoundation.org/UA/:OperStatus (declaration i=24150, owner i=24148)"
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
                          ExpandedNodeId.parse("i=24214"),
                          -1,
                          new long[] {},
                          Set.of(0, 1, 2, 3, 4, 5, 6),
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
  public CompletableFuture<StatusCode> writeOperStatusAsync(@Nullable InterfaceOperStatus value) {
    try {
      this.views.checkOpen();
      return this.views.ownRequest(this.writeOperStatusAsyncImplementation(value));
    } catch (RuntimeException viewFailure) {
      return CompletableFuture.failedFuture(viewFailure);
    }
  }

  @Override
  public @Nullable BaseDataVariableType getPhysAddressNode() throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    return ViewFutures.await(getPhysAddressNodeAsync());
  }

  private CompletableFuture<? extends @Nullable BaseDataVariableType>
      getPhysAddressNodeAsyncImplementation() {
    return ViewFutures.compose(
        viewMember2Async(),
        child ->
            child == null
                ? CompletableFuture.completedFuture(null)
                : views.wrapVariableAsync(child.getNodeId(), BaseDataVariableTypeView.TYPE));
  }

  @Override
  public CompletableFuture<? extends @Nullable BaseDataVariableType> getPhysAddressNodeAsync() {
    try {
      this.views.checkOpen();
      return this.views.ownRequest(this.getPhysAddressNodeAsyncImplementation());
    } catch (RuntimeException viewFailure) {
      return CompletableFuture.failedFuture(viewFailure);
    }
  }

  @Override
  public @Nullable String getPhysAddress() throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    var child = viewMember2();
    if (child == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:PhysAddress (declaration i=24151, owner i=24148)"
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
              "http://opcfoundation.org/UA/:PhysAddress (declaration i=24151, owner i=24148)");
        }
        if (rawValue instanceof Matrix matrix) {
          if (actualRank < 2
              || rankElements == null
              || !rankElements.getClass().isArray()
              || ArrayUtil.getValueRank(rankElements) != 1) {
            throw new UaException(
                StatusCodes.Bad_TypeMismatch,
                "http://opcfoundation.org/UA/:PhysAddress (declaration i=24151, owner i=24148)");
          }
          long elementCount = 1;
          for (int dimension : matrix.getDimensions()) {
            if (dimension < 0 || elementCount > Integer.MAX_VALUE) {
              throw new UaException(
                  StatusCodes.Bad_TypeMismatch,
                  "http://opcfoundation.org/UA/:PhysAddress (declaration i=24151, owner i=24148)");
            }
            elementCount *= dimension;
          }
          if (elementCount != Array.getLength(rankElements)) {
            throw new UaException(
                StatusCodes.Bad_TypeMismatch,
                "http://opcfoundation.org/UA/:PhysAddress (declaration i=24151, owner i=24148)");
          }
        } else if (actualRank > 1) {
          throw new UaException(
              StatusCodes.Bad_TypeMismatch,
              "http://opcfoundation.org/UA/:PhysAddress (declaration i=24151, owner i=24148): use"
                  + " Matrix for multiple dimensions");
        }
      }
      Object element = rawValue;
      if (element != null && !(element instanceof String)) {
        throw new UaException(
            StatusCodes.Bad_TypeMismatch,
            "http://opcfoundation.org/UA/:PhysAddress (declaration i=24151, owner i=24148)");
      }
      converted = (String) element;
    }
    return converted;
  }

  @Override
  public void setPhysAddress(@Nullable String value) throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    var child = viewMember2();
    if (child == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:PhysAddress (declaration i=24151, owner i=24148)"
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
  public @Nullable String readPhysAddress() throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    return ViewFutures.await(readPhysAddressAsync());
  }

  private CompletableFuture<? extends @Nullable String> readPhysAddressAsyncImplementation() {
    return ViewFutures.map(
        ViewFutures.compose(
            viewMember2Async(),
            child -> {
              if (child == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:PhysAddress (declaration i=24151, owner"
                            + " i=24148) on "
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
                      "http://opcfoundation.org/UA/:PhysAddress (declaration i=24151, owner"
                          + " i=24148)");
                }
                if (rawValue instanceof Matrix matrix) {
                  if (actualRank < 2
                      || rankElements == null
                      || !rankElements.getClass().isArray()
                      || ArrayUtil.getValueRank(rankElements) != 1) {
                    throw new UaException(
                        StatusCodes.Bad_TypeMismatch,
                        "http://opcfoundation.org/UA/:PhysAddress (declaration i=24151, owner"
                            + " i=24148)");
                  }
                  long elementCount = 1;
                  for (int dimension : matrix.getDimensions()) {
                    if (dimension < 0 || elementCount > Integer.MAX_VALUE) {
                      throw new UaException(
                          StatusCodes.Bad_TypeMismatch,
                          "http://opcfoundation.org/UA/:PhysAddress (declaration i=24151, owner"
                              + " i=24148)");
                    }
                    elementCount *= dimension;
                  }
                  if (elementCount != Array.getLength(rankElements)) {
                    throw new UaException(
                        StatusCodes.Bad_TypeMismatch,
                        "http://opcfoundation.org/UA/:PhysAddress (declaration i=24151, owner"
                            + " i=24148)");
                  }
                } else if (actualRank > 1) {
                  throw new UaException(
                      StatusCodes.Bad_TypeMismatch,
                      "http://opcfoundation.org/UA/:PhysAddress (declaration i=24151, owner"
                          + " i=24148): use Matrix for multiple dimensions");
                }
              }
              Object element = rawValue;
              if (element != null && !(element instanceof String)) {
                throw new UaException(
                    StatusCodes.Bad_TypeMismatch,
                    "http://opcfoundation.org/UA/:PhysAddress (declaration i=24151, owner"
                        + " i=24148)");
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
  public CompletableFuture<? extends @Nullable String> readPhysAddressAsync() {
    try {
      this.views.checkOpen();
      return this.views.ownRequest(this.readPhysAddressAsyncImplementation());
    } catch (RuntimeException viewFailure) {
      return CompletableFuture.failedFuture(viewFailure);
    }
  }

  @Override
  public void writePhysAddress(@Nullable String value) throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    views.checkOpen();
    StatusCode status = ViewFutures.await(writePhysAddressAsync(value));
    if (status == null) {
      throw new UaException(StatusCodes.Bad_UnexpectedError);
    }
    if (!status.isGood()) {
      throw new UaException(status);
    }
  }

  private CompletableFuture<StatusCode> writePhysAddressAsyncImplementation(
      @Nullable String value) {
    return ViewFutures.compose(
        viewMember2Async(),
        child -> {
          if (child == null) {
            throw new CompletionException(
                new UaException(
                    StatusCodes.Bad_NotFound,
                    "http://opcfoundation.org/UA/:PhysAddress (declaration i=24151, owner i=24148)"
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
  public CompletableFuture<StatusCode> writePhysAddressAsync(@Nullable String value) {
    try {
      this.views.checkOpen();
      return this.views.ownRequest(this.writePhysAddressAsyncImplementation(value));
    } catch (RuntimeException viewFailure) {
      return CompletableFuture.failedFuture(viewFailure);
    }
  }

  @Override
  public AnalogUnitType getSpeedNode() throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    return ViewFutures.await(getSpeedNodeAsync());
  }

  private CompletableFuture<? extends AnalogUnitType> getSpeedNodeAsyncImplementation() {
    return ViewFutures.compose(
        viewMember3Async(),
        child ->
            child == null
                ? CompletableFuture.completedFuture(null)
                : views.wrapVariableAsync(child.getNodeId(), AnalogUnitTypeView.TYPE));
  }

  @Override
  public CompletableFuture<? extends AnalogUnitType> getSpeedNodeAsync() {
    try {
      this.views.checkOpen();
      return this.views.ownRequest(this.getSpeedNodeAsyncImplementation());
    } catch (RuntimeException viewFailure) {
      return CompletableFuture.failedFuture(viewFailure);
    }
  }

  @Override
  public @Nullable ULong getSpeed() throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    var child = viewMember3();
    if (child == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:Speed (declaration i=24152, owner i=24148)"
              + " on "
              + getNodeId());
    }
    @Nullable ULong converted;
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
              "http://opcfoundation.org/UA/:Speed (declaration i=24152, owner i=24148)");
        }
        if (rawValue instanceof Matrix matrix) {
          if (actualRank < 2
              || rankElements == null
              || !rankElements.getClass().isArray()
              || ArrayUtil.getValueRank(rankElements) != 1) {
            throw new UaException(
                StatusCodes.Bad_TypeMismatch,
                "http://opcfoundation.org/UA/:Speed (declaration i=24152, owner i=24148)");
          }
          long elementCount = 1;
          for (int dimension : matrix.getDimensions()) {
            if (dimension < 0 || elementCount > Integer.MAX_VALUE) {
              throw new UaException(
                  StatusCodes.Bad_TypeMismatch,
                  "http://opcfoundation.org/UA/:Speed (declaration i=24152, owner i=24148)");
            }
            elementCount *= dimension;
          }
          if (elementCount != Array.getLength(rankElements)) {
            throw new UaException(
                StatusCodes.Bad_TypeMismatch,
                "http://opcfoundation.org/UA/:Speed (declaration i=24152, owner i=24148)");
          }
        } else if (actualRank > 1) {
          throw new UaException(
              StatusCodes.Bad_TypeMismatch,
              "http://opcfoundation.org/UA/:Speed (declaration i=24152, owner i=24148): use Matrix"
                  + " for multiple dimensions");
        }
      }
      Object element = rawValue;
      if (element != null && !(element instanceof ULong)) {
        throw new UaException(
            StatusCodes.Bad_TypeMismatch,
            "http://opcfoundation.org/UA/:Speed (declaration i=24152, owner i=24148)");
      }
      converted = (ULong) element;
    }
    return converted;
  }

  @Override
  public void setSpeed(@Nullable ULong value) throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    var child = viewMember3();
    if (child == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:Speed (declaration i=24152, owner i=24148)"
              + " on "
              + getNodeId());
    }
    var writeContext = views.localWriteContext(child.getNodeId(), value, KNOWN_ENUMS);
    Variant encoded =
        ValueChecks.validate(
            writeContext, value, ExpandedNodeId.parse("i=9"), -1, new long[] {}, null, false);
    views.checkOpen();
    child.setValue(encoded);
  }

  @Override
  public @Nullable ULong readSpeed() throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    return ViewFutures.await(readSpeedAsync());
  }

  private CompletableFuture<? extends @Nullable ULong> readSpeedAsyncImplementation() {
    return ViewFutures.map(
        ViewFutures.compose(
            viewMember3Async(),
            child -> {
              if (child == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:Speed (declaration i=24152, owner i=24148)"
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
            @Nullable ULong converted;
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
                      "http://opcfoundation.org/UA/:Speed (declaration i=24152, owner i=24148)");
                }
                if (rawValue instanceof Matrix matrix) {
                  if (actualRank < 2
                      || rankElements == null
                      || !rankElements.getClass().isArray()
                      || ArrayUtil.getValueRank(rankElements) != 1) {
                    throw new UaException(
                        StatusCodes.Bad_TypeMismatch,
                        "http://opcfoundation.org/UA/:Speed (declaration i=24152, owner i=24148)");
                  }
                  long elementCount = 1;
                  for (int dimension : matrix.getDimensions()) {
                    if (dimension < 0 || elementCount > Integer.MAX_VALUE) {
                      throw new UaException(
                          StatusCodes.Bad_TypeMismatch,
                          "http://opcfoundation.org/UA/:Speed (declaration i=24152, owner"
                              + " i=24148)");
                    }
                    elementCount *= dimension;
                  }
                  if (elementCount != Array.getLength(rankElements)) {
                    throw new UaException(
                        StatusCodes.Bad_TypeMismatch,
                        "http://opcfoundation.org/UA/:Speed (declaration i=24152, owner i=24148)");
                  }
                } else if (actualRank > 1) {
                  throw new UaException(
                      StatusCodes.Bad_TypeMismatch,
                      "http://opcfoundation.org/UA/:Speed (declaration i=24152, owner i=24148): use"
                          + " Matrix for multiple dimensions");
                }
              }
              Object element = rawValue;
              if (element != null && !(element instanceof ULong)) {
                throw new UaException(
                    StatusCodes.Bad_TypeMismatch,
                    "http://opcfoundation.org/UA/:Speed (declaration i=24152, owner i=24148)");
              }
              converted = (ULong) element;
            }
            return converted;
          } catch (UaException failure) {
            throw new CompletionException(failure);
          }
        });
  }

  @Override
  public CompletableFuture<? extends @Nullable ULong> readSpeedAsync() {
    try {
      this.views.checkOpen();
      return this.views.ownRequest(this.readSpeedAsyncImplementation());
    } catch (RuntimeException viewFailure) {
      return CompletableFuture.failedFuture(viewFailure);
    }
  }

  @Override
  public void writeSpeed(@Nullable ULong value) throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    views.checkOpen();
    StatusCode status = ViewFutures.await(writeSpeedAsync(value));
    if (status == null) {
      throw new UaException(StatusCodes.Bad_UnexpectedError);
    }
    if (!status.isGood()) {
      throw new UaException(status);
    }
  }

  private CompletableFuture<StatusCode> writeSpeedAsyncImplementation(@Nullable ULong value) {
    return ViewFutures.compose(
        viewMember3Async(),
        child -> {
          if (child == null) {
            throw new CompletionException(
                new UaException(
                    StatusCodes.Bad_NotFound,
                    "http://opcfoundation.org/UA/:Speed (declaration i=24152, owner i=24148)"
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
                          ExpandedNodeId.parse("i=9"),
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
  public CompletableFuture<StatusCode> writeSpeedAsync(@Nullable ULong value) {
    try {
      this.views.checkOpen();
      return this.views.ownRequest(this.writeSpeedAsyncImplementation(value));
    } catch (RuntimeException viewFailure) {
      return CompletableFuture.failedFuture(viewFailure);
    }
  }
}
