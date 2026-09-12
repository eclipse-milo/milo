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
import org.eclipse.milo.opcua.stack.core.util.ArrayUtil;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * https://reference.opcfoundation.org/v105/Core/docs/Part22/5.2.15
 *
 * <p>Selected <code>IPriorityMappingEntryType</code> contract over one retained backing node.
 * Obtain this view from a ClientViews context using {@link #TYPE}; the token itself does not prove
 * membership. Access after context close fails with IllegalStateException.
 */
@NullMarked
public final class IPriorityMappingEntryTypeView extends ClientObjectView
    implements IPriorityMappingEntryType {
  /** Selected UA/Java contract and context-owned factory. */
  public static final ClientViewType<IPriorityMappingEntryType> TYPE =
      ClientViewType.of(
          ExpandedNodeId.parse("i=24205"),
          IPriorityMappingEntryType.class,
          IPriorityMappingEntryTypeView::new);

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

  private IPriorityMappingEntryTypeView(ClientViews views, UaObjectNode node) {
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
                            "MappingUri",
                            ExpandedNodeId.parse("i=47"),
                            true,
                            NodeClass.Variable,
                            false,
                            "http://opcfoundation.org/UA/:MappingUri (declaration i=24206, owner"
                                + " i=24205)"));
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
                            "PriorityLabel",
                            ExpandedNodeId.parse("i=47"),
                            true,
                            NodeClass.Variable,
                            false,
                            "http://opcfoundation.org/UA/:PriorityLabel (declaration i=24207, owner"
                                + " i=24205)"));
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
                            "PriorityValue_PCP",
                            ExpandedNodeId.parse("i=47"),
                            true,
                            NodeClass.Variable,
                            true,
                            "http://opcfoundation.org/UA/:PriorityValue_PCP (declaration i=24208,"
                                + " owner i=24205)"));
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
                            "PriorityValue_DSCP",
                            ExpandedNodeId.parse("i=47"),
                            true,
                            NodeClass.Variable,
                            true,
                            "http://opcfoundation.org/UA/:PriorityValue_DSCP (declaration i=24209,"
                                + " owner i=24205)"));
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

  @Override
  public BaseDataVariableType getMappingUriNode() throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    return ViewFutures.await(getMappingUriNodeAsync());
  }

  private CompletableFuture<? extends BaseDataVariableType> getMappingUriNodeAsyncImplementation() {
    return ViewFutures.compose(
        viewMember0Async(),
        child ->
            child == null
                ? CompletableFuture.completedFuture(null)
                : views.wrapVariableAsync(child.getNodeId(), BaseDataVariableTypeView.TYPE));
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableType> getMappingUriNodeAsync() {
    try {
      this.views.checkOpen();
      return this.views.ownRequest(this.getMappingUriNodeAsyncImplementation());
    } catch (RuntimeException viewFailure) {
      return CompletableFuture.failedFuture(viewFailure);
    }
  }

  @Override
  public @Nullable String getMappingUri() throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    var child = viewMember0();
    if (child == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MappingUri (declaration i=24206, owner i=24205)"
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
              "http://opcfoundation.org/UA/:MappingUri (declaration i=24206, owner i=24205)");
        }
        if (rawValue instanceof Matrix matrix) {
          if (actualRank < 2
              || rankElements == null
              || !rankElements.getClass().isArray()
              || ArrayUtil.getValueRank(rankElements) != 1) {
            throw new UaException(
                StatusCodes.Bad_TypeMismatch,
                "http://opcfoundation.org/UA/:MappingUri (declaration i=24206, owner i=24205)");
          }
          long elementCount = 1;
          for (int dimension : matrix.getDimensions()) {
            if (dimension < 0 || elementCount > Integer.MAX_VALUE) {
              throw new UaException(
                  StatusCodes.Bad_TypeMismatch,
                  "http://opcfoundation.org/UA/:MappingUri (declaration i=24206, owner i=24205)");
            }
            elementCount *= dimension;
          }
          if (elementCount != Array.getLength(rankElements)) {
            throw new UaException(
                StatusCodes.Bad_TypeMismatch,
                "http://opcfoundation.org/UA/:MappingUri (declaration i=24206, owner i=24205)");
          }
        } else if (actualRank > 1) {
          throw new UaException(
              StatusCodes.Bad_TypeMismatch,
              "http://opcfoundation.org/UA/:MappingUri (declaration i=24206, owner i=24205): use"
                  + " Matrix for multiple dimensions");
        }
      }
      Object element = rawValue;
      if (element != null && !(element instanceof String)) {
        throw new UaException(
            StatusCodes.Bad_TypeMismatch,
            "http://opcfoundation.org/UA/:MappingUri (declaration i=24206, owner i=24205)");
      }
      converted = (String) element;
    }
    return converted;
  }

  @Override
  public void setMappingUri(@Nullable String value) throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    var child = viewMember0();
    if (child == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MappingUri (declaration i=24206, owner i=24205)"
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
  public @Nullable String readMappingUri() throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    return ViewFutures.await(readMappingUriAsync());
  }

  private CompletableFuture<? extends @Nullable String> readMappingUriAsyncImplementation() {
    return ViewFutures.map(
        ViewFutures.compose(
            viewMember0Async(),
            child -> {
              if (child == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:MappingUri (declaration i=24206, owner"
                            + " i=24205) on "
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
                      "http://opcfoundation.org/UA/:MappingUri (declaration i=24206, owner"
                          + " i=24205)");
                }
                if (rawValue instanceof Matrix matrix) {
                  if (actualRank < 2
                      || rankElements == null
                      || !rankElements.getClass().isArray()
                      || ArrayUtil.getValueRank(rankElements) != 1) {
                    throw new UaException(
                        StatusCodes.Bad_TypeMismatch,
                        "http://opcfoundation.org/UA/:MappingUri (declaration i=24206, owner"
                            + " i=24205)");
                  }
                  long elementCount = 1;
                  for (int dimension : matrix.getDimensions()) {
                    if (dimension < 0 || elementCount > Integer.MAX_VALUE) {
                      throw new UaException(
                          StatusCodes.Bad_TypeMismatch,
                          "http://opcfoundation.org/UA/:MappingUri (declaration i=24206, owner"
                              + " i=24205)");
                    }
                    elementCount *= dimension;
                  }
                  if (elementCount != Array.getLength(rankElements)) {
                    throw new UaException(
                        StatusCodes.Bad_TypeMismatch,
                        "http://opcfoundation.org/UA/:MappingUri (declaration i=24206, owner"
                            + " i=24205)");
                  }
                } else if (actualRank > 1) {
                  throw new UaException(
                      StatusCodes.Bad_TypeMismatch,
                      "http://opcfoundation.org/UA/:MappingUri (declaration i=24206, owner"
                          + " i=24205): use Matrix for multiple dimensions");
                }
              }
              Object element = rawValue;
              if (element != null && !(element instanceof String)) {
                throw new UaException(
                    StatusCodes.Bad_TypeMismatch,
                    "http://opcfoundation.org/UA/:MappingUri (declaration i=24206, owner i=24205)");
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
  public CompletableFuture<? extends @Nullable String> readMappingUriAsync() {
    try {
      this.views.checkOpen();
      return this.views.ownRequest(this.readMappingUriAsyncImplementation());
    } catch (RuntimeException viewFailure) {
      return CompletableFuture.failedFuture(viewFailure);
    }
  }

  @Override
  public void writeMappingUri(@Nullable String value) throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    views.checkOpen();
    StatusCode status = ViewFutures.await(writeMappingUriAsync(value));
    if (status == null) {
      throw new UaException(StatusCodes.Bad_UnexpectedError);
    }
    if (!status.isGood()) {
      throw new UaException(status);
    }
  }

  private CompletableFuture<StatusCode> writeMappingUriAsyncImplementation(@Nullable String value) {
    return ViewFutures.compose(
        viewMember0Async(),
        child -> {
          if (child == null) {
            throw new CompletionException(
                new UaException(
                    StatusCodes.Bad_NotFound,
                    "http://opcfoundation.org/UA/:MappingUri (declaration i=24206, owner i=24205)"
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
  public CompletableFuture<StatusCode> writeMappingUriAsync(@Nullable String value) {
    try {
      this.views.checkOpen();
      return this.views.ownRequest(this.writeMappingUriAsyncImplementation(value));
    } catch (RuntimeException viewFailure) {
      return CompletableFuture.failedFuture(viewFailure);
    }
  }

  @Override
  public BaseDataVariableType getPriorityLabelNode() throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    return ViewFutures.await(getPriorityLabelNodeAsync());
  }

  private CompletableFuture<? extends BaseDataVariableType>
      getPriorityLabelNodeAsyncImplementation() {
    return ViewFutures.compose(
        viewMember1Async(),
        child ->
            child == null
                ? CompletableFuture.completedFuture(null)
                : views.wrapVariableAsync(child.getNodeId(), BaseDataVariableTypeView.TYPE));
  }

  @Override
  public CompletableFuture<? extends BaseDataVariableType> getPriorityLabelNodeAsync() {
    try {
      this.views.checkOpen();
      return this.views.ownRequest(this.getPriorityLabelNodeAsyncImplementation());
    } catch (RuntimeException viewFailure) {
      return CompletableFuture.failedFuture(viewFailure);
    }
  }

  @Override
  public @Nullable String getPriorityLabel() throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    var child = viewMember1();
    if (child == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:PriorityLabel (declaration i=24207, owner i=24205)"
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
              "http://opcfoundation.org/UA/:PriorityLabel (declaration i=24207, owner i=24205)");
        }
        if (rawValue instanceof Matrix matrix) {
          if (actualRank < 2
              || rankElements == null
              || !rankElements.getClass().isArray()
              || ArrayUtil.getValueRank(rankElements) != 1) {
            throw new UaException(
                StatusCodes.Bad_TypeMismatch,
                "http://opcfoundation.org/UA/:PriorityLabel (declaration i=24207, owner i=24205)");
          }
          long elementCount = 1;
          for (int dimension : matrix.getDimensions()) {
            if (dimension < 0 || elementCount > Integer.MAX_VALUE) {
              throw new UaException(
                  StatusCodes.Bad_TypeMismatch,
                  "http://opcfoundation.org/UA/:PriorityLabel (declaration i=24207, owner"
                      + " i=24205)");
            }
            elementCount *= dimension;
          }
          if (elementCount != Array.getLength(rankElements)) {
            throw new UaException(
                StatusCodes.Bad_TypeMismatch,
                "http://opcfoundation.org/UA/:PriorityLabel (declaration i=24207, owner i=24205)");
          }
        } else if (actualRank > 1) {
          throw new UaException(
              StatusCodes.Bad_TypeMismatch,
              "http://opcfoundation.org/UA/:PriorityLabel (declaration i=24207, owner i=24205): use"
                  + " Matrix for multiple dimensions");
        }
      }
      Object element = rawValue;
      if (element != null && !(element instanceof String)) {
        throw new UaException(
            StatusCodes.Bad_TypeMismatch,
            "http://opcfoundation.org/UA/:PriorityLabel (declaration i=24207, owner i=24205)");
      }
      converted = (String) element;
    }
    return converted;
  }

  @Override
  public void setPriorityLabel(@Nullable String value) throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    var child = viewMember1();
    if (child == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:PriorityLabel (declaration i=24207, owner i=24205)"
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
  public @Nullable String readPriorityLabel() throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    return ViewFutures.await(readPriorityLabelAsync());
  }

  private CompletableFuture<? extends @Nullable String> readPriorityLabelAsyncImplementation() {
    return ViewFutures.map(
        ViewFutures.compose(
            viewMember1Async(),
            child -> {
              if (child == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:PriorityLabel (declaration i=24207, owner"
                            + " i=24205) on "
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
                      "http://opcfoundation.org/UA/:PriorityLabel (declaration i=24207, owner"
                          + " i=24205)");
                }
                if (rawValue instanceof Matrix matrix) {
                  if (actualRank < 2
                      || rankElements == null
                      || !rankElements.getClass().isArray()
                      || ArrayUtil.getValueRank(rankElements) != 1) {
                    throw new UaException(
                        StatusCodes.Bad_TypeMismatch,
                        "http://opcfoundation.org/UA/:PriorityLabel (declaration i=24207, owner"
                            + " i=24205)");
                  }
                  long elementCount = 1;
                  for (int dimension : matrix.getDimensions()) {
                    if (dimension < 0 || elementCount > Integer.MAX_VALUE) {
                      throw new UaException(
                          StatusCodes.Bad_TypeMismatch,
                          "http://opcfoundation.org/UA/:PriorityLabel (declaration i=24207, owner"
                              + " i=24205)");
                    }
                    elementCount *= dimension;
                  }
                  if (elementCount != Array.getLength(rankElements)) {
                    throw new UaException(
                        StatusCodes.Bad_TypeMismatch,
                        "http://opcfoundation.org/UA/:PriorityLabel (declaration i=24207, owner"
                            + " i=24205)");
                  }
                } else if (actualRank > 1) {
                  throw new UaException(
                      StatusCodes.Bad_TypeMismatch,
                      "http://opcfoundation.org/UA/:PriorityLabel (declaration i=24207, owner"
                          + " i=24205): use Matrix for multiple dimensions");
                }
              }
              Object element = rawValue;
              if (element != null && !(element instanceof String)) {
                throw new UaException(
                    StatusCodes.Bad_TypeMismatch,
                    "http://opcfoundation.org/UA/:PriorityLabel (declaration i=24207, owner"
                        + " i=24205)");
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
  public CompletableFuture<? extends @Nullable String> readPriorityLabelAsync() {
    try {
      this.views.checkOpen();
      return this.views.ownRequest(this.readPriorityLabelAsyncImplementation());
    } catch (RuntimeException viewFailure) {
      return CompletableFuture.failedFuture(viewFailure);
    }
  }

  @Override
  public void writePriorityLabel(@Nullable String value) throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    views.checkOpen();
    StatusCode status = ViewFutures.await(writePriorityLabelAsync(value));
    if (status == null) {
      throw new UaException(StatusCodes.Bad_UnexpectedError);
    }
    if (!status.isGood()) {
      throw new UaException(status);
    }
  }

  private CompletableFuture<StatusCode> writePriorityLabelAsyncImplementation(
      @Nullable String value) {
    return ViewFutures.compose(
        viewMember1Async(),
        child -> {
          if (child == null) {
            throw new CompletionException(
                new UaException(
                    StatusCodes.Bad_NotFound,
                    "http://opcfoundation.org/UA/:PriorityLabel (declaration i=24207, owner"
                        + " i=24205) on "
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
  public CompletableFuture<StatusCode> writePriorityLabelAsync(@Nullable String value) {
    try {
      this.views.checkOpen();
      return this.views.ownRequest(this.writePriorityLabelAsyncImplementation(value));
    } catch (RuntimeException viewFailure) {
      return CompletableFuture.failedFuture(viewFailure);
    }
  }

  @Override
  public @Nullable BaseDataVariableType getPriorityValuePcpNode() throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    return ViewFutures.await(getPriorityValuePcpNodeAsync());
  }

  private CompletableFuture<? extends @Nullable BaseDataVariableType>
      getPriorityValuePcpNodeAsyncImplementation() {
    return ViewFutures.compose(
        viewMember2Async(),
        child ->
            child == null
                ? CompletableFuture.completedFuture(null)
                : views.wrapVariableAsync(child.getNodeId(), BaseDataVariableTypeView.TYPE));
  }

  @Override
  public CompletableFuture<? extends @Nullable BaseDataVariableType>
      getPriorityValuePcpNodeAsync() {
    try {
      this.views.checkOpen();
      return this.views.ownRequest(this.getPriorityValuePcpNodeAsyncImplementation());
    } catch (RuntimeException viewFailure) {
      return CompletableFuture.failedFuture(viewFailure);
    }
  }

  @Override
  public @Nullable UByte getPriorityValuePcp() throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    var child = viewMember2();
    if (child == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:PriorityValue_PCP (declaration i=24208, owner i=24205)"
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
              "http://opcfoundation.org/UA/:PriorityValue_PCP (declaration i=24208, owner"
                  + " i=24205)");
        }
        if (rawValue instanceof Matrix matrix) {
          if (actualRank < 2
              || rankElements == null
              || !rankElements.getClass().isArray()
              || ArrayUtil.getValueRank(rankElements) != 1) {
            throw new UaException(
                StatusCodes.Bad_TypeMismatch,
                "http://opcfoundation.org/UA/:PriorityValue_PCP (declaration i=24208, owner"
                    + " i=24205)");
          }
          long elementCount = 1;
          for (int dimension : matrix.getDimensions()) {
            if (dimension < 0 || elementCount > Integer.MAX_VALUE) {
              throw new UaException(
                  StatusCodes.Bad_TypeMismatch,
                  "http://opcfoundation.org/UA/:PriorityValue_PCP (declaration i=24208, owner"
                      + " i=24205)");
            }
            elementCount *= dimension;
          }
          if (elementCount != Array.getLength(rankElements)) {
            throw new UaException(
                StatusCodes.Bad_TypeMismatch,
                "http://opcfoundation.org/UA/:PriorityValue_PCP (declaration i=24208, owner"
                    + " i=24205)");
          }
        } else if (actualRank > 1) {
          throw new UaException(
              StatusCodes.Bad_TypeMismatch,
              "http://opcfoundation.org/UA/:PriorityValue_PCP (declaration i=24208, owner i=24205):"
                  + " use Matrix for multiple dimensions");
        }
      }
      Object element = rawValue;
      if (element != null && !(element instanceof UByte)) {
        throw new UaException(
            StatusCodes.Bad_TypeMismatch,
            "http://opcfoundation.org/UA/:PriorityValue_PCP (declaration i=24208, owner i=24205)");
      }
      converted = (UByte) element;
    }
    return converted;
  }

  @Override
  public void setPriorityValuePcp(@Nullable UByte value) throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    var child = viewMember2();
    if (child == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:PriorityValue_PCP (declaration i=24208, owner i=24205)"
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
  public @Nullable UByte readPriorityValuePcp() throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    return ViewFutures.await(readPriorityValuePcpAsync());
  }

  private CompletableFuture<? extends @Nullable UByte> readPriorityValuePcpAsyncImplementation() {
    return ViewFutures.map(
        ViewFutures.compose(
            viewMember2Async(),
            child -> {
              if (child == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:PriorityValue_PCP (declaration i=24208, owner"
                            + " i=24205) on "
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
                      "http://opcfoundation.org/UA/:PriorityValue_PCP (declaration i=24208, owner"
                          + " i=24205)");
                }
                if (rawValue instanceof Matrix matrix) {
                  if (actualRank < 2
                      || rankElements == null
                      || !rankElements.getClass().isArray()
                      || ArrayUtil.getValueRank(rankElements) != 1) {
                    throw new UaException(
                        StatusCodes.Bad_TypeMismatch,
                        "http://opcfoundation.org/UA/:PriorityValue_PCP (declaration i=24208, owner"
                            + " i=24205)");
                  }
                  long elementCount = 1;
                  for (int dimension : matrix.getDimensions()) {
                    if (dimension < 0 || elementCount > Integer.MAX_VALUE) {
                      throw new UaException(
                          StatusCodes.Bad_TypeMismatch,
                          "http://opcfoundation.org/UA/:PriorityValue_PCP (declaration i=24208,"
                              + " owner i=24205)");
                    }
                    elementCount *= dimension;
                  }
                  if (elementCount != Array.getLength(rankElements)) {
                    throw new UaException(
                        StatusCodes.Bad_TypeMismatch,
                        "http://opcfoundation.org/UA/:PriorityValue_PCP (declaration i=24208, owner"
                            + " i=24205)");
                  }
                } else if (actualRank > 1) {
                  throw new UaException(
                      StatusCodes.Bad_TypeMismatch,
                      "http://opcfoundation.org/UA/:PriorityValue_PCP (declaration i=24208, owner"
                          + " i=24205): use Matrix for multiple dimensions");
                }
              }
              Object element = rawValue;
              if (element != null && !(element instanceof UByte)) {
                throw new UaException(
                    StatusCodes.Bad_TypeMismatch,
                    "http://opcfoundation.org/UA/:PriorityValue_PCP (declaration i=24208, owner"
                        + " i=24205)");
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
  public CompletableFuture<? extends @Nullable UByte> readPriorityValuePcpAsync() {
    try {
      this.views.checkOpen();
      return this.views.ownRequest(this.readPriorityValuePcpAsyncImplementation());
    } catch (RuntimeException viewFailure) {
      return CompletableFuture.failedFuture(viewFailure);
    }
  }

  @Override
  public void writePriorityValuePcp(@Nullable UByte value) throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    views.checkOpen();
    StatusCode status = ViewFutures.await(writePriorityValuePcpAsync(value));
    if (status == null) {
      throw new UaException(StatusCodes.Bad_UnexpectedError);
    }
    if (!status.isGood()) {
      throw new UaException(status);
    }
  }

  private CompletableFuture<StatusCode> writePriorityValuePcpAsyncImplementation(
      @Nullable UByte value) {
    return ViewFutures.compose(
        viewMember2Async(),
        child -> {
          if (child == null) {
            throw new CompletionException(
                new UaException(
                    StatusCodes.Bad_NotFound,
                    "http://opcfoundation.org/UA/:PriorityValue_PCP (declaration i=24208, owner"
                        + " i=24205) on "
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
  public CompletableFuture<StatusCode> writePriorityValuePcpAsync(@Nullable UByte value) {
    try {
      this.views.checkOpen();
      return this.views.ownRequest(this.writePriorityValuePcpAsyncImplementation(value));
    } catch (RuntimeException viewFailure) {
      return CompletableFuture.failedFuture(viewFailure);
    }
  }

  @Override
  public @Nullable BaseDataVariableType getPriorityValueDscpNode() throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    return ViewFutures.await(getPriorityValueDscpNodeAsync());
  }

  private CompletableFuture<? extends @Nullable BaseDataVariableType>
      getPriorityValueDscpNodeAsyncImplementation() {
    return ViewFutures.compose(
        viewMember3Async(),
        child ->
            child == null
                ? CompletableFuture.completedFuture(null)
                : views.wrapVariableAsync(child.getNodeId(), BaseDataVariableTypeView.TYPE));
  }

  @Override
  public CompletableFuture<? extends @Nullable BaseDataVariableType>
      getPriorityValueDscpNodeAsync() {
    try {
      this.views.checkOpen();
      return this.views.ownRequest(this.getPriorityValueDscpNodeAsyncImplementation());
    } catch (RuntimeException viewFailure) {
      return CompletableFuture.failedFuture(viewFailure);
    }
  }

  @Override
  public @Nullable UInteger getPriorityValueDscp() throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    var child = viewMember3();
    if (child == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:PriorityValue_DSCP (declaration i=24209, owner i=24205)"
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
              "http://opcfoundation.org/UA/:PriorityValue_DSCP (declaration i=24209, owner"
                  + " i=24205)");
        }
        if (rawValue instanceof Matrix matrix) {
          if (actualRank < 2
              || rankElements == null
              || !rankElements.getClass().isArray()
              || ArrayUtil.getValueRank(rankElements) != 1) {
            throw new UaException(
                StatusCodes.Bad_TypeMismatch,
                "http://opcfoundation.org/UA/:PriorityValue_DSCP (declaration i=24209, owner"
                    + " i=24205)");
          }
          long elementCount = 1;
          for (int dimension : matrix.getDimensions()) {
            if (dimension < 0 || elementCount > Integer.MAX_VALUE) {
              throw new UaException(
                  StatusCodes.Bad_TypeMismatch,
                  "http://opcfoundation.org/UA/:PriorityValue_DSCP (declaration i=24209, owner"
                      + " i=24205)");
            }
            elementCount *= dimension;
          }
          if (elementCount != Array.getLength(rankElements)) {
            throw new UaException(
                StatusCodes.Bad_TypeMismatch,
                "http://opcfoundation.org/UA/:PriorityValue_DSCP (declaration i=24209, owner"
                    + " i=24205)");
          }
        } else if (actualRank > 1) {
          throw new UaException(
              StatusCodes.Bad_TypeMismatch,
              "http://opcfoundation.org/UA/:PriorityValue_DSCP (declaration i=24209, owner"
                  + " i=24205): use Matrix for multiple dimensions");
        }
      }
      Object element = rawValue;
      if (element != null && !(element instanceof UInteger)) {
        throw new UaException(
            StatusCodes.Bad_TypeMismatch,
            "http://opcfoundation.org/UA/:PriorityValue_DSCP (declaration i=24209, owner i=24205)");
      }
      converted = (UInteger) element;
    }
    return converted;
  }

  @Override
  public void setPriorityValueDscp(@Nullable UInteger value) throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    var child = viewMember3();
    if (child == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:PriorityValue_DSCP (declaration i=24209, owner i=24205)"
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
  public @Nullable UInteger readPriorityValueDscp() throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    return ViewFutures.await(readPriorityValueDscpAsync());
  }

  private CompletableFuture<? extends @Nullable UInteger>
      readPriorityValueDscpAsyncImplementation() {
    return ViewFutures.map(
        ViewFutures.compose(
            viewMember3Async(),
            child -> {
              if (child == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:PriorityValue_DSCP (declaration i=24209,"
                            + " owner i=24205) on "
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
                      "http://opcfoundation.org/UA/:PriorityValue_DSCP (declaration i=24209, owner"
                          + " i=24205)");
                }
                if (rawValue instanceof Matrix matrix) {
                  if (actualRank < 2
                      || rankElements == null
                      || !rankElements.getClass().isArray()
                      || ArrayUtil.getValueRank(rankElements) != 1) {
                    throw new UaException(
                        StatusCodes.Bad_TypeMismatch,
                        "http://opcfoundation.org/UA/:PriorityValue_DSCP (declaration i=24209,"
                            + " owner i=24205)");
                  }
                  long elementCount = 1;
                  for (int dimension : matrix.getDimensions()) {
                    if (dimension < 0 || elementCount > Integer.MAX_VALUE) {
                      throw new UaException(
                          StatusCodes.Bad_TypeMismatch,
                          "http://opcfoundation.org/UA/:PriorityValue_DSCP (declaration i=24209,"
                              + " owner i=24205)");
                    }
                    elementCount *= dimension;
                  }
                  if (elementCount != Array.getLength(rankElements)) {
                    throw new UaException(
                        StatusCodes.Bad_TypeMismatch,
                        "http://opcfoundation.org/UA/:PriorityValue_DSCP (declaration i=24209,"
                            + " owner i=24205)");
                  }
                } else if (actualRank > 1) {
                  throw new UaException(
                      StatusCodes.Bad_TypeMismatch,
                      "http://opcfoundation.org/UA/:PriorityValue_DSCP (declaration i=24209, owner"
                          + " i=24205): use Matrix for multiple dimensions");
                }
              }
              Object element = rawValue;
              if (element != null && !(element instanceof UInteger)) {
                throw new UaException(
                    StatusCodes.Bad_TypeMismatch,
                    "http://opcfoundation.org/UA/:PriorityValue_DSCP (declaration i=24209, owner"
                        + " i=24205)");
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
  public CompletableFuture<? extends @Nullable UInteger> readPriorityValueDscpAsync() {
    try {
      this.views.checkOpen();
      return this.views.ownRequest(this.readPriorityValueDscpAsyncImplementation());
    } catch (RuntimeException viewFailure) {
      return CompletableFuture.failedFuture(viewFailure);
    }
  }

  @Override
  public void writePriorityValueDscp(@Nullable UInteger value) throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    views.checkOpen();
    StatusCode status = ViewFutures.await(writePriorityValueDscpAsync(value));
    if (status == null) {
      throw new UaException(StatusCodes.Bad_UnexpectedError);
    }
    if (!status.isGood()) {
      throw new UaException(status);
    }
  }

  private CompletableFuture<StatusCode> writePriorityValueDscpAsyncImplementation(
      @Nullable UInteger value) {
    return ViewFutures.compose(
        viewMember3Async(),
        child -> {
          if (child == null) {
            throw new CompletionException(
                new UaException(
                    StatusCodes.Bad_NotFound,
                    "http://opcfoundation.org/UA/:PriorityValue_DSCP (declaration i=24209, owner"
                        + " i=24205) on "
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
  public CompletableFuture<StatusCode> writePriorityValueDscpAsync(@Nullable UInteger value) {
    try {
      this.views.checkOpen();
      return this.views.ownRequest(this.writePriorityValueDscpAsyncImplementation(value));
    } catch (RuntimeException viewFailure) {
      return CompletableFuture.failedFuture(viewFailure);
    }
  }
}
