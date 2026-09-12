/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.model.variables;

import com.digitalpetri.opcua.uanodeset.runtime.client.ClientMembers;
import com.digitalpetri.opcua.uanodeset.runtime.client.ClientVariableView;
import com.digitalpetri.opcua.uanodeset.runtime.client.ClientVariableViewType;
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
import org.eclipse.milo.opcua.sdk.client.nodes.UaVariableNode;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.Matrix;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.EUInformation;
import org.eclipse.milo.opcua.stack.core.types.structured.NumberRange;
import org.eclipse.milo.opcua.stack.core.types.structured.Range;
import org.eclipse.milo.opcua.stack.core.util.ArrayUtil;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * https://reference.opcfoundation.org/v105/Core/docs/Part8/5.3.2/#5.3.2.4
 *
 * <p>Selected <code>AnalogUnitType</code> contract over one retained backing node. Obtain this view
 * from a ClientViews context using {@link #TYPE}; the token itself does not prove membership.
 * Access after context close fails with IllegalStateException.
 */
@NullMarked
public final class AnalogUnitTypeView extends ClientVariableView implements AnalogUnitType {
  /** Selected UA/Java contract and context-owned factory. */
  public static final ClientVariableViewType<AnalogUnitType> TYPE =
      ClientVariableViewType.of(
          ExpandedNodeId.parse("i=17497"), AnalogUnitType.class, AnalogUnitTypeView::new);

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

  private AnalogUnitTypeView(ClientViews views, UaVariableNode node) {
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
                            "Definition",
                            ExpandedNodeId.parse("i=46"),
                            true,
                            NodeClass.Variable,
                            true,
                            "http://opcfoundation.org/UA/:Definition (declaration i=2366, owner"
                                + " i=2365)"));
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
                            "ValuePrecision",
                            ExpandedNodeId.parse("i=46"),
                            true,
                            NodeClass.Variable,
                            true,
                            "http://opcfoundation.org/UA/:ValuePrecision (declaration i=2367, owner"
                                + " i=2365)"));
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
                            "InstrumentRange",
                            ExpandedNodeId.parse("i=46"),
                            true,
                            NodeClass.Variable,
                            true,
                            "http://opcfoundation.org/UA/:InstrumentRange (declaration i=17567,"
                                + " owner i=15318)"));
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
                            "InstrumentNumberRange",
                            ExpandedNodeId.parse("i=46"),
                            true,
                            NodeClass.Variable,
                            true,
                            "http://opcfoundation.org/UA/:InstrumentNumberRange (declaration"
                                + " i=23904, owner i=15318)"));
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
                            "EURange",
                            ExpandedNodeId.parse("i=46"),
                            true,
                            NodeClass.Variable,
                            true,
                            "http://opcfoundation.org/UA/:EURange (declaration i=17568, owner"
                                + " i=15318)"));
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

  private CompletableFuture<@Nullable UaVariableNode> viewMember5Async() {
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
                            "EUNumberRange",
                            ExpandedNodeId.parse("i=46"),
                            true,
                            NodeClass.Variable,
                            true,
                            "http://opcfoundation.org/UA/:EUNumberRange (declaration i=23905, owner"
                                + " i=15318)"));
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

  private @Nullable UaVariableNode viewMember5() throws UaException {
    return ViewFutures.await(viewMember5Async());
  }

  private CompletableFuture<UaVariableNode> viewMember6Async() {
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
                            "EngineeringUnits",
                            ExpandedNodeId.parse("i=46"),
                            true,
                            NodeClass.Variable,
                            false,
                            "http://opcfoundation.org/UA/:EngineeringUnits (declaration i=17502,"
                                + " owner i=17497)"));
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

  private UaVariableNode viewMember6() throws UaException {
    return ViewFutures.await(viewMember6Async());
  }

  @Override
  public @Nullable PropertyType getDefinitionNode() throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    return ViewFutures.await(getDefinitionNodeAsync());
  }

  private CompletableFuture<? extends @Nullable PropertyType>
      getDefinitionNodeAsyncImplementation() {
    return ViewFutures.compose(
        viewMember0Async(),
        child ->
            child == null
                ? CompletableFuture.completedFuture(null)
                : views.wrapVariableAsync(child.getNodeId(), PropertyTypeView.TYPE));
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyType> getDefinitionNodeAsync() {
    try {
      this.views.checkOpen();
      return this.views.ownRequest(this.getDefinitionNodeAsyncImplementation());
    } catch (RuntimeException viewFailure) {
      return CompletableFuture.failedFuture(viewFailure);
    }
  }

  @Override
  public @Nullable String getDefinition() throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    var child = viewMember0();
    if (child == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:Definition (declaration i=2366, owner i=2365)"
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
              "http://opcfoundation.org/UA/:Definition (declaration i=2366, owner i=2365)");
        }
        if (rawValue instanceof Matrix matrix) {
          if (actualRank < 2
              || rankElements == null
              || !rankElements.getClass().isArray()
              || ArrayUtil.getValueRank(rankElements) != 1) {
            throw new UaException(
                StatusCodes.Bad_TypeMismatch,
                "http://opcfoundation.org/UA/:Definition (declaration i=2366, owner i=2365)");
          }
          long elementCount = 1;
          for (int dimension : matrix.getDimensions()) {
            if (dimension < 0 || elementCount > Integer.MAX_VALUE) {
              throw new UaException(
                  StatusCodes.Bad_TypeMismatch,
                  "http://opcfoundation.org/UA/:Definition (declaration i=2366, owner i=2365)");
            }
            elementCount *= dimension;
          }
          if (elementCount != Array.getLength(rankElements)) {
            throw new UaException(
                StatusCodes.Bad_TypeMismatch,
                "http://opcfoundation.org/UA/:Definition (declaration i=2366, owner i=2365)");
          }
        } else if (actualRank > 1) {
          throw new UaException(
              StatusCodes.Bad_TypeMismatch,
              "http://opcfoundation.org/UA/:Definition (declaration i=2366, owner i=2365): use"
                  + " Matrix for multiple dimensions");
        }
      }
      Object element = rawValue;
      if (element != null && !(element instanceof String)) {
        throw new UaException(
            StatusCodes.Bad_TypeMismatch,
            "http://opcfoundation.org/UA/:Definition (declaration i=2366, owner i=2365)");
      }
      converted = (String) element;
    }
    return converted;
  }

  @Override
  public void setDefinition(@Nullable String value) throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    var child = viewMember0();
    if (child == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:Definition (declaration i=2366, owner i=2365)"
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
  public @Nullable String readDefinition() throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    return ViewFutures.await(readDefinitionAsync());
  }

  private CompletableFuture<? extends @Nullable String> readDefinitionAsyncImplementation() {
    return ViewFutures.map(
        ViewFutures.compose(
            viewMember0Async(),
            child -> {
              if (child == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:Definition (declaration i=2366, owner i=2365)"
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
                      "http://opcfoundation.org/UA/:Definition (declaration i=2366, owner i=2365)");
                }
                if (rawValue instanceof Matrix matrix) {
                  if (actualRank < 2
                      || rankElements == null
                      || !rankElements.getClass().isArray()
                      || ArrayUtil.getValueRank(rankElements) != 1) {
                    throw new UaException(
                        StatusCodes.Bad_TypeMismatch,
                        "http://opcfoundation.org/UA/:Definition (declaration i=2366, owner"
                            + " i=2365)");
                  }
                  long elementCount = 1;
                  for (int dimension : matrix.getDimensions()) {
                    if (dimension < 0 || elementCount > Integer.MAX_VALUE) {
                      throw new UaException(
                          StatusCodes.Bad_TypeMismatch,
                          "http://opcfoundation.org/UA/:Definition (declaration i=2366, owner"
                              + " i=2365)");
                    }
                    elementCount *= dimension;
                  }
                  if (elementCount != Array.getLength(rankElements)) {
                    throw new UaException(
                        StatusCodes.Bad_TypeMismatch,
                        "http://opcfoundation.org/UA/:Definition (declaration i=2366, owner"
                            + " i=2365)");
                  }
                } else if (actualRank > 1) {
                  throw new UaException(
                      StatusCodes.Bad_TypeMismatch,
                      "http://opcfoundation.org/UA/:Definition (declaration i=2366, owner i=2365):"
                          + " use Matrix for multiple dimensions");
                }
              }
              Object element = rawValue;
              if (element != null && !(element instanceof String)) {
                throw new UaException(
                    StatusCodes.Bad_TypeMismatch,
                    "http://opcfoundation.org/UA/:Definition (declaration i=2366, owner i=2365)");
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
  public CompletableFuture<? extends @Nullable String> readDefinitionAsync() {
    try {
      this.views.checkOpen();
      return this.views.ownRequest(this.readDefinitionAsyncImplementation());
    } catch (RuntimeException viewFailure) {
      return CompletableFuture.failedFuture(viewFailure);
    }
  }

  @Override
  public void writeDefinition(@Nullable String value) throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    views.checkOpen();
    StatusCode status = ViewFutures.await(writeDefinitionAsync(value));
    if (status == null) {
      throw new UaException(StatusCodes.Bad_UnexpectedError);
    }
    if (!status.isGood()) {
      throw new UaException(status);
    }
  }

  private CompletableFuture<StatusCode> writeDefinitionAsyncImplementation(@Nullable String value) {
    return ViewFutures.compose(
        viewMember0Async(),
        child -> {
          if (child == null) {
            throw new CompletionException(
                new UaException(
                    StatusCodes.Bad_NotFound,
                    "http://opcfoundation.org/UA/:Definition (declaration i=2366, owner i=2365)"
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
  public CompletableFuture<StatusCode> writeDefinitionAsync(@Nullable String value) {
    try {
      this.views.checkOpen();
      return this.views.ownRequest(this.writeDefinitionAsyncImplementation(value));
    } catch (RuntimeException viewFailure) {
      return CompletableFuture.failedFuture(viewFailure);
    }
  }

  @Override
  public @Nullable PropertyType getValuePrecisionNode() throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    return ViewFutures.await(getValuePrecisionNodeAsync());
  }

  private CompletableFuture<? extends @Nullable PropertyType>
      getValuePrecisionNodeAsyncImplementation() {
    return ViewFutures.compose(
        viewMember1Async(),
        child ->
            child == null
                ? CompletableFuture.completedFuture(null)
                : views.wrapVariableAsync(child.getNodeId(), PropertyTypeView.TYPE));
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyType> getValuePrecisionNodeAsync() {
    try {
      this.views.checkOpen();
      return this.views.ownRequest(this.getValuePrecisionNodeAsyncImplementation());
    } catch (RuntimeException viewFailure) {
      return CompletableFuture.failedFuture(viewFailure);
    }
  }

  @Override
  public @Nullable Double getValuePrecision() throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    var child = viewMember1();
    if (child == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ValuePrecision (declaration i=2367, owner i=2365)"
              + " on "
              + getNodeId());
    }
    @Nullable Double converted;
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
              "http://opcfoundation.org/UA/:ValuePrecision (declaration i=2367, owner i=2365)");
        }
        if (rawValue instanceof Matrix matrix) {
          if (actualRank < 2
              || rankElements == null
              || !rankElements.getClass().isArray()
              || ArrayUtil.getValueRank(rankElements) != 1) {
            throw new UaException(
                StatusCodes.Bad_TypeMismatch,
                "http://opcfoundation.org/UA/:ValuePrecision (declaration i=2367, owner i=2365)");
          }
          long elementCount = 1;
          for (int dimension : matrix.getDimensions()) {
            if (dimension < 0 || elementCount > Integer.MAX_VALUE) {
              throw new UaException(
                  StatusCodes.Bad_TypeMismatch,
                  "http://opcfoundation.org/UA/:ValuePrecision (declaration i=2367, owner i=2365)");
            }
            elementCount *= dimension;
          }
          if (elementCount != Array.getLength(rankElements)) {
            throw new UaException(
                StatusCodes.Bad_TypeMismatch,
                "http://opcfoundation.org/UA/:ValuePrecision (declaration i=2367, owner i=2365)");
          }
        } else if (actualRank > 1) {
          throw new UaException(
              StatusCodes.Bad_TypeMismatch,
              "http://opcfoundation.org/UA/:ValuePrecision (declaration i=2367, owner i=2365): use"
                  + " Matrix for multiple dimensions");
        }
      }
      Object element = rawValue;
      if (element != null && !(element instanceof Double)) {
        throw new UaException(
            StatusCodes.Bad_TypeMismatch,
            "http://opcfoundation.org/UA/:ValuePrecision (declaration i=2367, owner i=2365)");
      }
      converted = (Double) element;
    }
    return converted;
  }

  @Override
  public void setValuePrecision(@Nullable Double value) throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    var child = viewMember1();
    if (child == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ValuePrecision (declaration i=2367, owner i=2365)"
              + " on "
              + getNodeId());
    }
    var writeContext = views.localWriteContext(child.getNodeId(), value, KNOWN_ENUMS);
    Variant encoded =
        ValueChecks.validate(
            writeContext, value, ExpandedNodeId.parse("i=11"), -1, new long[] {}, null, false);
    views.checkOpen();
    child.setValue(encoded);
  }

  @Override
  public @Nullable Double readValuePrecision() throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    return ViewFutures.await(readValuePrecisionAsync());
  }

  private CompletableFuture<? extends @Nullable Double> readValuePrecisionAsyncImplementation() {
    return ViewFutures.map(
        ViewFutures.compose(
            viewMember1Async(),
            child -> {
              if (child == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:ValuePrecision (declaration i=2367, owner"
                            + " i=2365) on "
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
            @Nullable Double converted;
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
                      "http://opcfoundation.org/UA/:ValuePrecision (declaration i=2367, owner"
                          + " i=2365)");
                }
                if (rawValue instanceof Matrix matrix) {
                  if (actualRank < 2
                      || rankElements == null
                      || !rankElements.getClass().isArray()
                      || ArrayUtil.getValueRank(rankElements) != 1) {
                    throw new UaException(
                        StatusCodes.Bad_TypeMismatch,
                        "http://opcfoundation.org/UA/:ValuePrecision (declaration i=2367, owner"
                            + " i=2365)");
                  }
                  long elementCount = 1;
                  for (int dimension : matrix.getDimensions()) {
                    if (dimension < 0 || elementCount > Integer.MAX_VALUE) {
                      throw new UaException(
                          StatusCodes.Bad_TypeMismatch,
                          "http://opcfoundation.org/UA/:ValuePrecision (declaration i=2367, owner"
                              + " i=2365)");
                    }
                    elementCount *= dimension;
                  }
                  if (elementCount != Array.getLength(rankElements)) {
                    throw new UaException(
                        StatusCodes.Bad_TypeMismatch,
                        "http://opcfoundation.org/UA/:ValuePrecision (declaration i=2367, owner"
                            + " i=2365)");
                  }
                } else if (actualRank > 1) {
                  throw new UaException(
                      StatusCodes.Bad_TypeMismatch,
                      "http://opcfoundation.org/UA/:ValuePrecision (declaration i=2367, owner"
                          + " i=2365): use Matrix for multiple dimensions");
                }
              }
              Object element = rawValue;
              if (element != null && !(element instanceof Double)) {
                throw new UaException(
                    StatusCodes.Bad_TypeMismatch,
                    "http://opcfoundation.org/UA/:ValuePrecision (declaration i=2367, owner"
                        + " i=2365)");
              }
              converted = (Double) element;
            }
            return converted;
          } catch (UaException failure) {
            throw new CompletionException(failure);
          }
        });
  }

  @Override
  public CompletableFuture<? extends @Nullable Double> readValuePrecisionAsync() {
    try {
      this.views.checkOpen();
      return this.views.ownRequest(this.readValuePrecisionAsyncImplementation());
    } catch (RuntimeException viewFailure) {
      return CompletableFuture.failedFuture(viewFailure);
    }
  }

  @Override
  public void writeValuePrecision(@Nullable Double value) throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    views.checkOpen();
    StatusCode status = ViewFutures.await(writeValuePrecisionAsync(value));
    if (status == null) {
      throw new UaException(StatusCodes.Bad_UnexpectedError);
    }
    if (!status.isGood()) {
      throw new UaException(status);
    }
  }

  private CompletableFuture<StatusCode> writeValuePrecisionAsyncImplementation(
      @Nullable Double value) {
    return ViewFutures.compose(
        viewMember1Async(),
        child -> {
          if (child == null) {
            throw new CompletionException(
                new UaException(
                    StatusCodes.Bad_NotFound,
                    "http://opcfoundation.org/UA/:ValuePrecision (declaration i=2367, owner i=2365)"
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
                          ExpandedNodeId.parse("i=11"),
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
  public CompletableFuture<StatusCode> writeValuePrecisionAsync(@Nullable Double value) {
    try {
      this.views.checkOpen();
      return this.views.ownRequest(this.writeValuePrecisionAsyncImplementation(value));
    } catch (RuntimeException viewFailure) {
      return CompletableFuture.failedFuture(viewFailure);
    }
  }

  @Override
  public @Nullable PropertyType getInstrumentRangeNode() throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    return ViewFutures.await(getInstrumentRangeNodeAsync());
  }

  private CompletableFuture<? extends @Nullable PropertyType>
      getInstrumentRangeNodeAsyncImplementation() {
    return ViewFutures.compose(
        viewMember2Async(),
        child ->
            child == null
                ? CompletableFuture.completedFuture(null)
                : views.wrapVariableAsync(child.getNodeId(), PropertyTypeView.TYPE));
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyType> getInstrumentRangeNodeAsync() {
    try {
      this.views.checkOpen();
      return this.views.ownRequest(this.getInstrumentRangeNodeAsyncImplementation());
    } catch (RuntimeException viewFailure) {
      return CompletableFuture.failedFuture(viewFailure);
    }
  }

  @Override
  public @Nullable Range getInstrumentRange() throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    var child = viewMember2();
    if (child == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:InstrumentRange (declaration i=17567, owner i=15318)"
              + " on "
              + getNodeId());
    }
    @Nullable Range converted;
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
              "http://opcfoundation.org/UA/:InstrumentRange (declaration i=17567, owner i=15318)");
        }
        if (rawValue instanceof Matrix matrix) {
          if (actualRank < 2
              || rankElements == null
              || !rankElements.getClass().isArray()
              || ArrayUtil.getValueRank(rankElements) != 1) {
            throw new UaException(
                StatusCodes.Bad_TypeMismatch,
                "http://opcfoundation.org/UA/:InstrumentRange (declaration i=17567, owner"
                    + " i=15318)");
          }
          long elementCount = 1;
          for (int dimension : matrix.getDimensions()) {
            if (dimension < 0 || elementCount > Integer.MAX_VALUE) {
              throw new UaException(
                  StatusCodes.Bad_TypeMismatch,
                  "http://opcfoundation.org/UA/:InstrumentRange (declaration i=17567, owner"
                      + " i=15318)");
            }
            elementCount *= dimension;
          }
          if (elementCount != Array.getLength(rankElements)) {
            throw new UaException(
                StatusCodes.Bad_TypeMismatch,
                "http://opcfoundation.org/UA/:InstrumentRange (declaration i=17567, owner"
                    + " i=15318)");
          }
        } else if (actualRank > 1) {
          throw new UaException(
              StatusCodes.Bad_TypeMismatch,
              "http://opcfoundation.org/UA/:InstrumentRange (declaration i=17567, owner i=15318):"
                  + " use Matrix for multiple dimensions");
        }
      }
      try {
        rawValue = ExtensionObject.decodeValue(client.getStaticEncodingContext(), rawValue);
      } catch (UaRuntimeException failure) {
        throw new UaException(failure.getStatusCode().getValue(), failure);
      }
      Object element = rawValue;
      if (element != null && !(element instanceof Range)) {
        throw new UaException(
            StatusCodes.Bad_TypeMismatch,
            "http://opcfoundation.org/UA/:InstrumentRange (declaration i=17567, owner i=15318)");
      }
      converted = (Range) element;
    }
    return converted;
  }

  @Override
  public void setInstrumentRange(@Nullable Range value) throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    var child = viewMember2();
    if (child == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:InstrumentRange (declaration i=17567, owner i=15318)"
              + " on "
              + getNodeId());
    }
    var writeContext = views.localWriteContext(child.getNodeId(), value, KNOWN_ENUMS);
    Variant encoded =
        ValueChecks.validate(
            writeContext, value, ExpandedNodeId.parse("i=884"), -1, new long[] {}, null, false);
    views.checkOpen();
    child.setValue(encoded);
  }

  @Override
  public @Nullable Range readInstrumentRange() throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    return ViewFutures.await(readInstrumentRangeAsync());
  }

  private CompletableFuture<? extends @Nullable Range> readInstrumentRangeAsyncImplementation() {
    return ViewFutures.map(
        ViewFutures.compose(
            viewMember2Async(),
            child -> {
              if (child == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:InstrumentRange (declaration i=17567, owner"
                            + " i=15318) on "
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
            @Nullable Range converted;
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
                      "http://opcfoundation.org/UA/:InstrumentRange (declaration i=17567, owner"
                          + " i=15318)");
                }
                if (rawValue instanceof Matrix matrix) {
                  if (actualRank < 2
                      || rankElements == null
                      || !rankElements.getClass().isArray()
                      || ArrayUtil.getValueRank(rankElements) != 1) {
                    throw new UaException(
                        StatusCodes.Bad_TypeMismatch,
                        "http://opcfoundation.org/UA/:InstrumentRange (declaration i=17567, owner"
                            + " i=15318)");
                  }
                  long elementCount = 1;
                  for (int dimension : matrix.getDimensions()) {
                    if (dimension < 0 || elementCount > Integer.MAX_VALUE) {
                      throw new UaException(
                          StatusCodes.Bad_TypeMismatch,
                          "http://opcfoundation.org/UA/:InstrumentRange (declaration i=17567, owner"
                              + " i=15318)");
                    }
                    elementCount *= dimension;
                  }
                  if (elementCount != Array.getLength(rankElements)) {
                    throw new UaException(
                        StatusCodes.Bad_TypeMismatch,
                        "http://opcfoundation.org/UA/:InstrumentRange (declaration i=17567, owner"
                            + " i=15318)");
                  }
                } else if (actualRank > 1) {
                  throw new UaException(
                      StatusCodes.Bad_TypeMismatch,
                      "http://opcfoundation.org/UA/:InstrumentRange (declaration i=17567, owner"
                          + " i=15318): use Matrix for multiple dimensions");
                }
              }
              try {
                rawValue = ExtensionObject.decodeValue(client.getStaticEncodingContext(), rawValue);
              } catch (UaRuntimeException failure) {
                throw new UaException(failure.getStatusCode().getValue(), failure);
              }
              Object element = rawValue;
              if (element != null && !(element instanceof Range)) {
                throw new UaException(
                    StatusCodes.Bad_TypeMismatch,
                    "http://opcfoundation.org/UA/:InstrumentRange (declaration i=17567, owner"
                        + " i=15318)");
              }
              converted = (Range) element;
            }
            return converted;
          } catch (UaException failure) {
            throw new CompletionException(failure);
          }
        });
  }

  @Override
  public CompletableFuture<? extends @Nullable Range> readInstrumentRangeAsync() {
    try {
      this.views.checkOpen();
      return this.views.ownRequest(this.readInstrumentRangeAsyncImplementation());
    } catch (RuntimeException viewFailure) {
      return CompletableFuture.failedFuture(viewFailure);
    }
  }

  @Override
  public void writeInstrumentRange(@Nullable Range value) throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    views.checkOpen();
    StatusCode status = ViewFutures.await(writeInstrumentRangeAsync(value));
    if (status == null) {
      throw new UaException(StatusCodes.Bad_UnexpectedError);
    }
    if (!status.isGood()) {
      throw new UaException(status);
    }
  }

  private CompletableFuture<StatusCode> writeInstrumentRangeAsyncImplementation(
      @Nullable Range value) {
    return ViewFutures.compose(
        viewMember2Async(),
        child -> {
          if (child == null) {
            throw new CompletionException(
                new UaException(
                    StatusCodes.Bad_NotFound,
                    "http://opcfoundation.org/UA/:InstrumentRange (declaration i=17567, owner"
                        + " i=15318) on "
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
                          ExpandedNodeId.parse("i=884"),
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
  public CompletableFuture<StatusCode> writeInstrumentRangeAsync(@Nullable Range value) {
    try {
      this.views.checkOpen();
      return this.views.ownRequest(this.writeInstrumentRangeAsyncImplementation(value));
    } catch (RuntimeException viewFailure) {
      return CompletableFuture.failedFuture(viewFailure);
    }
  }

  @Override
  public @Nullable PropertyType getInstrumentNumberRangeNode() throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    return ViewFutures.await(getInstrumentNumberRangeNodeAsync());
  }

  private CompletableFuture<? extends @Nullable PropertyType>
      getInstrumentNumberRangeNodeAsyncImplementation() {
    return ViewFutures.compose(
        viewMember3Async(),
        child ->
            child == null
                ? CompletableFuture.completedFuture(null)
                : views.wrapVariableAsync(child.getNodeId(), PropertyTypeView.TYPE));
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyType> getInstrumentNumberRangeNodeAsync() {
    try {
      this.views.checkOpen();
      return this.views.ownRequest(this.getInstrumentNumberRangeNodeAsyncImplementation());
    } catch (RuntimeException viewFailure) {
      return CompletableFuture.failedFuture(viewFailure);
    }
  }

  @Override
  public @Nullable NumberRange getInstrumentNumberRange() throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    var child = viewMember3();
    if (child == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:InstrumentNumberRange (declaration i=23904, owner i=15318)"
              + " on "
              + getNodeId());
    }
    @Nullable NumberRange converted;
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
              "http://opcfoundation.org/UA/:InstrumentNumberRange (declaration i=23904, owner"
                  + " i=15318)");
        }
        if (rawValue instanceof Matrix matrix) {
          if (actualRank < 2
              || rankElements == null
              || !rankElements.getClass().isArray()
              || ArrayUtil.getValueRank(rankElements) != 1) {
            throw new UaException(
                StatusCodes.Bad_TypeMismatch,
                "http://opcfoundation.org/UA/:InstrumentNumberRange (declaration i=23904, owner"
                    + " i=15318)");
          }
          long elementCount = 1;
          for (int dimension : matrix.getDimensions()) {
            if (dimension < 0 || elementCount > Integer.MAX_VALUE) {
              throw new UaException(
                  StatusCodes.Bad_TypeMismatch,
                  "http://opcfoundation.org/UA/:InstrumentNumberRange (declaration i=23904, owner"
                      + " i=15318)");
            }
            elementCount *= dimension;
          }
          if (elementCount != Array.getLength(rankElements)) {
            throw new UaException(
                StatusCodes.Bad_TypeMismatch,
                "http://opcfoundation.org/UA/:InstrumentNumberRange (declaration i=23904, owner"
                    + " i=15318)");
          }
        } else if (actualRank > 1) {
          throw new UaException(
              StatusCodes.Bad_TypeMismatch,
              "http://opcfoundation.org/UA/:InstrumentNumberRange (declaration i=23904, owner"
                  + " i=15318): use Matrix for multiple dimensions");
        }
      }
      try {
        rawValue = ExtensionObject.decodeValue(client.getStaticEncodingContext(), rawValue);
      } catch (UaRuntimeException failure) {
        throw new UaException(failure.getStatusCode().getValue(), failure);
      }
      Object element = rawValue;
      if (element != null && !(element instanceof NumberRange)) {
        throw new UaException(
            StatusCodes.Bad_TypeMismatch,
            "http://opcfoundation.org/UA/:InstrumentNumberRange (declaration i=23904, owner"
                + " i=15318)");
      }
      converted = (NumberRange) element;
    }
    return converted;
  }

  @Override
  public void setInstrumentNumberRange(@Nullable NumberRange value) throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    var child = viewMember3();
    if (child == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:InstrumentNumberRange (declaration i=23904, owner i=15318)"
              + " on "
              + getNodeId());
    }
    var writeContext = views.localWriteContext(child.getNodeId(), value, KNOWN_ENUMS);
    Variant encoded =
        ValueChecks.validate(
            writeContext, value, ExpandedNodeId.parse("i=23903"), -1, new long[] {}, null, false);
    views.checkOpen();
    child.setValue(encoded);
  }

  @Override
  public @Nullable NumberRange readInstrumentNumberRange() throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    return ViewFutures.await(readInstrumentNumberRangeAsync());
  }

  private CompletableFuture<? extends @Nullable NumberRange>
      readInstrumentNumberRangeAsyncImplementation() {
    return ViewFutures.map(
        ViewFutures.compose(
            viewMember3Async(),
            child -> {
              if (child == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:InstrumentNumberRange (declaration i=23904,"
                            + " owner i=15318) on "
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
            @Nullable NumberRange converted;
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
                      "http://opcfoundation.org/UA/:InstrumentNumberRange (declaration i=23904,"
                          + " owner i=15318)");
                }
                if (rawValue instanceof Matrix matrix) {
                  if (actualRank < 2
                      || rankElements == null
                      || !rankElements.getClass().isArray()
                      || ArrayUtil.getValueRank(rankElements) != 1) {
                    throw new UaException(
                        StatusCodes.Bad_TypeMismatch,
                        "http://opcfoundation.org/UA/:InstrumentNumberRange (declaration i=23904,"
                            + " owner i=15318)");
                  }
                  long elementCount = 1;
                  for (int dimension : matrix.getDimensions()) {
                    if (dimension < 0 || elementCount > Integer.MAX_VALUE) {
                      throw new UaException(
                          StatusCodes.Bad_TypeMismatch,
                          "http://opcfoundation.org/UA/:InstrumentNumberRange (declaration i=23904,"
                              + " owner i=15318)");
                    }
                    elementCount *= dimension;
                  }
                  if (elementCount != Array.getLength(rankElements)) {
                    throw new UaException(
                        StatusCodes.Bad_TypeMismatch,
                        "http://opcfoundation.org/UA/:InstrumentNumberRange (declaration i=23904,"
                            + " owner i=15318)");
                  }
                } else if (actualRank > 1) {
                  throw new UaException(
                      StatusCodes.Bad_TypeMismatch,
                      "http://opcfoundation.org/UA/:InstrumentNumberRange (declaration i=23904,"
                          + " owner i=15318): use Matrix for multiple dimensions");
                }
              }
              try {
                rawValue = ExtensionObject.decodeValue(client.getStaticEncodingContext(), rawValue);
              } catch (UaRuntimeException failure) {
                throw new UaException(failure.getStatusCode().getValue(), failure);
              }
              Object element = rawValue;
              if (element != null && !(element instanceof NumberRange)) {
                throw new UaException(
                    StatusCodes.Bad_TypeMismatch,
                    "http://opcfoundation.org/UA/:InstrumentNumberRange (declaration i=23904, owner"
                        + " i=15318)");
              }
              converted = (NumberRange) element;
            }
            return converted;
          } catch (UaException failure) {
            throw new CompletionException(failure);
          }
        });
  }

  @Override
  public CompletableFuture<? extends @Nullable NumberRange> readInstrumentNumberRangeAsync() {
    try {
      this.views.checkOpen();
      return this.views.ownRequest(this.readInstrumentNumberRangeAsyncImplementation());
    } catch (RuntimeException viewFailure) {
      return CompletableFuture.failedFuture(viewFailure);
    }
  }

  @Override
  public void writeInstrumentNumberRange(@Nullable NumberRange value) throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    views.checkOpen();
    StatusCode status = ViewFutures.await(writeInstrumentNumberRangeAsync(value));
    if (status == null) {
      throw new UaException(StatusCodes.Bad_UnexpectedError);
    }
    if (!status.isGood()) {
      throw new UaException(status);
    }
  }

  private CompletableFuture<StatusCode> writeInstrumentNumberRangeAsyncImplementation(
      @Nullable NumberRange value) {
    return ViewFutures.compose(
        viewMember3Async(),
        child -> {
          if (child == null) {
            throw new CompletionException(
                new UaException(
                    StatusCodes.Bad_NotFound,
                    "http://opcfoundation.org/UA/:InstrumentNumberRange (declaration i=23904, owner"
                        + " i=15318) on "
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
                          ExpandedNodeId.parse("i=23903"),
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
  public CompletableFuture<StatusCode> writeInstrumentNumberRangeAsync(
      @Nullable NumberRange value) {
    try {
      this.views.checkOpen();
      return this.views.ownRequest(this.writeInstrumentNumberRangeAsyncImplementation(value));
    } catch (RuntimeException viewFailure) {
      return CompletableFuture.failedFuture(viewFailure);
    }
  }

  @Override
  public @Nullable PropertyType getEuRangeNode() throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    return ViewFutures.await(getEuRangeNodeAsync());
  }

  private CompletableFuture<? extends @Nullable PropertyType> getEuRangeNodeAsyncImplementation() {
    return ViewFutures.compose(
        viewMember4Async(),
        child ->
            child == null
                ? CompletableFuture.completedFuture(null)
                : views.wrapVariableAsync(child.getNodeId(), PropertyTypeView.TYPE));
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyType> getEuRangeNodeAsync() {
    try {
      this.views.checkOpen();
      return this.views.ownRequest(this.getEuRangeNodeAsyncImplementation());
    } catch (RuntimeException viewFailure) {
      return CompletableFuture.failedFuture(viewFailure);
    }
  }

  @Override
  public @Nullable Range getEuRange() throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    var child = viewMember4();
    if (child == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:EURange (declaration i=17568, owner i=15318)"
              + " on "
              + getNodeId());
    }
    @Nullable Range converted;
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
              "http://opcfoundation.org/UA/:EURange (declaration i=17568, owner i=15318)");
        }
        if (rawValue instanceof Matrix matrix) {
          if (actualRank < 2
              || rankElements == null
              || !rankElements.getClass().isArray()
              || ArrayUtil.getValueRank(rankElements) != 1) {
            throw new UaException(
                StatusCodes.Bad_TypeMismatch,
                "http://opcfoundation.org/UA/:EURange (declaration i=17568, owner i=15318)");
          }
          long elementCount = 1;
          for (int dimension : matrix.getDimensions()) {
            if (dimension < 0 || elementCount > Integer.MAX_VALUE) {
              throw new UaException(
                  StatusCodes.Bad_TypeMismatch,
                  "http://opcfoundation.org/UA/:EURange (declaration i=17568, owner i=15318)");
            }
            elementCount *= dimension;
          }
          if (elementCount != Array.getLength(rankElements)) {
            throw new UaException(
                StatusCodes.Bad_TypeMismatch,
                "http://opcfoundation.org/UA/:EURange (declaration i=17568, owner i=15318)");
          }
        } else if (actualRank > 1) {
          throw new UaException(
              StatusCodes.Bad_TypeMismatch,
              "http://opcfoundation.org/UA/:EURange (declaration i=17568, owner i=15318): use"
                  + " Matrix for multiple dimensions");
        }
      }
      try {
        rawValue = ExtensionObject.decodeValue(client.getStaticEncodingContext(), rawValue);
      } catch (UaRuntimeException failure) {
        throw new UaException(failure.getStatusCode().getValue(), failure);
      }
      Object element = rawValue;
      if (element != null && !(element instanceof Range)) {
        throw new UaException(
            StatusCodes.Bad_TypeMismatch,
            "http://opcfoundation.org/UA/:EURange (declaration i=17568, owner i=15318)");
      }
      converted = (Range) element;
    }
    return converted;
  }

  @Override
  public void setEuRange(@Nullable Range value) throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    var child = viewMember4();
    if (child == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:EURange (declaration i=17568, owner i=15318)"
              + " on "
              + getNodeId());
    }
    var writeContext = views.localWriteContext(child.getNodeId(), value, KNOWN_ENUMS);
    Variant encoded =
        ValueChecks.validate(
            writeContext, value, ExpandedNodeId.parse("i=884"), -1, new long[] {}, null, false);
    views.checkOpen();
    child.setValue(encoded);
  }

  @Override
  public @Nullable Range readEuRange() throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    return ViewFutures.await(readEuRangeAsync());
  }

  private CompletableFuture<? extends @Nullable Range> readEuRangeAsyncImplementation() {
    return ViewFutures.map(
        ViewFutures.compose(
            viewMember4Async(),
            child -> {
              if (child == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:EURange (declaration i=17568, owner i=15318)"
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
            @Nullable Range converted;
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
                      "http://opcfoundation.org/UA/:EURange (declaration i=17568, owner i=15318)");
                }
                if (rawValue instanceof Matrix matrix) {
                  if (actualRank < 2
                      || rankElements == null
                      || !rankElements.getClass().isArray()
                      || ArrayUtil.getValueRank(rankElements) != 1) {
                    throw new UaException(
                        StatusCodes.Bad_TypeMismatch,
                        "http://opcfoundation.org/UA/:EURange (declaration i=17568, owner"
                            + " i=15318)");
                  }
                  long elementCount = 1;
                  for (int dimension : matrix.getDimensions()) {
                    if (dimension < 0 || elementCount > Integer.MAX_VALUE) {
                      throw new UaException(
                          StatusCodes.Bad_TypeMismatch,
                          "http://opcfoundation.org/UA/:EURange (declaration i=17568, owner"
                              + " i=15318)");
                    }
                    elementCount *= dimension;
                  }
                  if (elementCount != Array.getLength(rankElements)) {
                    throw new UaException(
                        StatusCodes.Bad_TypeMismatch,
                        "http://opcfoundation.org/UA/:EURange (declaration i=17568, owner"
                            + " i=15318)");
                  }
                } else if (actualRank > 1) {
                  throw new UaException(
                      StatusCodes.Bad_TypeMismatch,
                      "http://opcfoundation.org/UA/:EURange (declaration i=17568, owner i=15318):"
                          + " use Matrix for multiple dimensions");
                }
              }
              try {
                rawValue = ExtensionObject.decodeValue(client.getStaticEncodingContext(), rawValue);
              } catch (UaRuntimeException failure) {
                throw new UaException(failure.getStatusCode().getValue(), failure);
              }
              Object element = rawValue;
              if (element != null && !(element instanceof Range)) {
                throw new UaException(
                    StatusCodes.Bad_TypeMismatch,
                    "http://opcfoundation.org/UA/:EURange (declaration i=17568, owner i=15318)");
              }
              converted = (Range) element;
            }
            return converted;
          } catch (UaException failure) {
            throw new CompletionException(failure);
          }
        });
  }

  @Override
  public CompletableFuture<? extends @Nullable Range> readEuRangeAsync() {
    try {
      this.views.checkOpen();
      return this.views.ownRequest(this.readEuRangeAsyncImplementation());
    } catch (RuntimeException viewFailure) {
      return CompletableFuture.failedFuture(viewFailure);
    }
  }

  @Override
  public void writeEuRange(@Nullable Range value) throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    views.checkOpen();
    StatusCode status = ViewFutures.await(writeEuRangeAsync(value));
    if (status == null) {
      throw new UaException(StatusCodes.Bad_UnexpectedError);
    }
    if (!status.isGood()) {
      throw new UaException(status);
    }
  }

  private CompletableFuture<StatusCode> writeEuRangeAsyncImplementation(@Nullable Range value) {
    return ViewFutures.compose(
        viewMember4Async(),
        child -> {
          if (child == null) {
            throw new CompletionException(
                new UaException(
                    StatusCodes.Bad_NotFound,
                    "http://opcfoundation.org/UA/:EURange (declaration i=17568, owner i=15318)"
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
                          ExpandedNodeId.parse("i=884"),
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
  public CompletableFuture<StatusCode> writeEuRangeAsync(@Nullable Range value) {
    try {
      this.views.checkOpen();
      return this.views.ownRequest(this.writeEuRangeAsyncImplementation(value));
    } catch (RuntimeException viewFailure) {
      return CompletableFuture.failedFuture(viewFailure);
    }
  }

  @Override
  public @Nullable PropertyType getEuNumberRangeNode() throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    return ViewFutures.await(getEuNumberRangeNodeAsync());
  }

  private CompletableFuture<? extends @Nullable PropertyType>
      getEuNumberRangeNodeAsyncImplementation() {
    return ViewFutures.compose(
        viewMember5Async(),
        child ->
            child == null
                ? CompletableFuture.completedFuture(null)
                : views.wrapVariableAsync(child.getNodeId(), PropertyTypeView.TYPE));
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyType> getEuNumberRangeNodeAsync() {
    try {
      this.views.checkOpen();
      return this.views.ownRequest(this.getEuNumberRangeNodeAsyncImplementation());
    } catch (RuntimeException viewFailure) {
      return CompletableFuture.failedFuture(viewFailure);
    }
  }

  @Override
  public @Nullable NumberRange getEuNumberRange() throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    var child = viewMember5();
    if (child == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:EUNumberRange (declaration i=23905, owner i=15318)"
              + " on "
              + getNodeId());
    }
    @Nullable NumberRange converted;
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
              "http://opcfoundation.org/UA/:EUNumberRange (declaration i=23905, owner i=15318)");
        }
        if (rawValue instanceof Matrix matrix) {
          if (actualRank < 2
              || rankElements == null
              || !rankElements.getClass().isArray()
              || ArrayUtil.getValueRank(rankElements) != 1) {
            throw new UaException(
                StatusCodes.Bad_TypeMismatch,
                "http://opcfoundation.org/UA/:EUNumberRange (declaration i=23905, owner i=15318)");
          }
          long elementCount = 1;
          for (int dimension : matrix.getDimensions()) {
            if (dimension < 0 || elementCount > Integer.MAX_VALUE) {
              throw new UaException(
                  StatusCodes.Bad_TypeMismatch,
                  "http://opcfoundation.org/UA/:EUNumberRange (declaration i=23905, owner"
                      + " i=15318)");
            }
            elementCount *= dimension;
          }
          if (elementCount != Array.getLength(rankElements)) {
            throw new UaException(
                StatusCodes.Bad_TypeMismatch,
                "http://opcfoundation.org/UA/:EUNumberRange (declaration i=23905, owner i=15318)");
          }
        } else if (actualRank > 1) {
          throw new UaException(
              StatusCodes.Bad_TypeMismatch,
              "http://opcfoundation.org/UA/:EUNumberRange (declaration i=23905, owner i=15318): use"
                  + " Matrix for multiple dimensions");
        }
      }
      try {
        rawValue = ExtensionObject.decodeValue(client.getStaticEncodingContext(), rawValue);
      } catch (UaRuntimeException failure) {
        throw new UaException(failure.getStatusCode().getValue(), failure);
      }
      Object element = rawValue;
      if (element != null && !(element instanceof NumberRange)) {
        throw new UaException(
            StatusCodes.Bad_TypeMismatch,
            "http://opcfoundation.org/UA/:EUNumberRange (declaration i=23905, owner i=15318)");
      }
      converted = (NumberRange) element;
    }
    return converted;
  }

  @Override
  public void setEuNumberRange(@Nullable NumberRange value) throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    var child = viewMember5();
    if (child == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:EUNumberRange (declaration i=23905, owner i=15318)"
              + " on "
              + getNodeId());
    }
    var writeContext = views.localWriteContext(child.getNodeId(), value, KNOWN_ENUMS);
    Variant encoded =
        ValueChecks.validate(
            writeContext, value, ExpandedNodeId.parse("i=23903"), -1, new long[] {}, null, false);
    views.checkOpen();
    child.setValue(encoded);
  }

  @Override
  public @Nullable NumberRange readEuNumberRange() throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    return ViewFutures.await(readEuNumberRangeAsync());
  }

  private CompletableFuture<? extends @Nullable NumberRange>
      readEuNumberRangeAsyncImplementation() {
    return ViewFutures.map(
        ViewFutures.compose(
            viewMember5Async(),
            child -> {
              if (child == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:EUNumberRange (declaration i=23905, owner"
                            + " i=15318) on "
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
            @Nullable NumberRange converted;
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
                      "http://opcfoundation.org/UA/:EUNumberRange (declaration i=23905, owner"
                          + " i=15318)");
                }
                if (rawValue instanceof Matrix matrix) {
                  if (actualRank < 2
                      || rankElements == null
                      || !rankElements.getClass().isArray()
                      || ArrayUtil.getValueRank(rankElements) != 1) {
                    throw new UaException(
                        StatusCodes.Bad_TypeMismatch,
                        "http://opcfoundation.org/UA/:EUNumberRange (declaration i=23905, owner"
                            + " i=15318)");
                  }
                  long elementCount = 1;
                  for (int dimension : matrix.getDimensions()) {
                    if (dimension < 0 || elementCount > Integer.MAX_VALUE) {
                      throw new UaException(
                          StatusCodes.Bad_TypeMismatch,
                          "http://opcfoundation.org/UA/:EUNumberRange (declaration i=23905, owner"
                              + " i=15318)");
                    }
                    elementCount *= dimension;
                  }
                  if (elementCount != Array.getLength(rankElements)) {
                    throw new UaException(
                        StatusCodes.Bad_TypeMismatch,
                        "http://opcfoundation.org/UA/:EUNumberRange (declaration i=23905, owner"
                            + " i=15318)");
                  }
                } else if (actualRank > 1) {
                  throw new UaException(
                      StatusCodes.Bad_TypeMismatch,
                      "http://opcfoundation.org/UA/:EUNumberRange (declaration i=23905, owner"
                          + " i=15318): use Matrix for multiple dimensions");
                }
              }
              try {
                rawValue = ExtensionObject.decodeValue(client.getStaticEncodingContext(), rawValue);
              } catch (UaRuntimeException failure) {
                throw new UaException(failure.getStatusCode().getValue(), failure);
              }
              Object element = rawValue;
              if (element != null && !(element instanceof NumberRange)) {
                throw new UaException(
                    StatusCodes.Bad_TypeMismatch,
                    "http://opcfoundation.org/UA/:EUNumberRange (declaration i=23905, owner"
                        + " i=15318)");
              }
              converted = (NumberRange) element;
            }
            return converted;
          } catch (UaException failure) {
            throw new CompletionException(failure);
          }
        });
  }

  @Override
  public CompletableFuture<? extends @Nullable NumberRange> readEuNumberRangeAsync() {
    try {
      this.views.checkOpen();
      return this.views.ownRequest(this.readEuNumberRangeAsyncImplementation());
    } catch (RuntimeException viewFailure) {
      return CompletableFuture.failedFuture(viewFailure);
    }
  }

  @Override
  public void writeEuNumberRange(@Nullable NumberRange value) throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    views.checkOpen();
    StatusCode status = ViewFutures.await(writeEuNumberRangeAsync(value));
    if (status == null) {
      throw new UaException(StatusCodes.Bad_UnexpectedError);
    }
    if (!status.isGood()) {
      throw new UaException(status);
    }
  }

  private CompletableFuture<StatusCode> writeEuNumberRangeAsyncImplementation(
      @Nullable NumberRange value) {
    return ViewFutures.compose(
        viewMember5Async(),
        child -> {
          if (child == null) {
            throw new CompletionException(
                new UaException(
                    StatusCodes.Bad_NotFound,
                    "http://opcfoundation.org/UA/:EUNumberRange (declaration i=23905, owner"
                        + " i=15318) on "
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
                          ExpandedNodeId.parse("i=23903"),
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
  public CompletableFuture<StatusCode> writeEuNumberRangeAsync(@Nullable NumberRange value) {
    try {
      this.views.checkOpen();
      return this.views.ownRequest(this.writeEuNumberRangeAsyncImplementation(value));
    } catch (RuntimeException viewFailure) {
      return CompletableFuture.failedFuture(viewFailure);
    }
  }

  @Override
  public PropertyType getEngineeringUnitsNode() throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    return ViewFutures.await(getEngineeringUnitsNodeAsync());
  }

  private CompletableFuture<? extends PropertyType> getEngineeringUnitsNodeAsyncImplementation() {
    return ViewFutures.compose(
        viewMember6Async(),
        child ->
            child == null
                ? CompletableFuture.completedFuture(null)
                : views.wrapVariableAsync(child.getNodeId(), PropertyTypeView.TYPE));
  }

  @Override
  public CompletableFuture<? extends PropertyType> getEngineeringUnitsNodeAsync() {
    try {
      this.views.checkOpen();
      return this.views.ownRequest(this.getEngineeringUnitsNodeAsyncImplementation());
    } catch (RuntimeException viewFailure) {
      return CompletableFuture.failedFuture(viewFailure);
    }
  }

  @Override
  public @Nullable EUInformation getEngineeringUnitsProperty() throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    var child = viewMember6();
    if (child == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:EngineeringUnits (declaration i=17502, owner i=17497)"
              + " on "
              + getNodeId());
    }
    @Nullable EUInformation converted;
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
              "http://opcfoundation.org/UA/:EngineeringUnits (declaration i=17502, owner i=17497)");
        }
        if (rawValue instanceof Matrix matrix) {
          if (actualRank < 2
              || rankElements == null
              || !rankElements.getClass().isArray()
              || ArrayUtil.getValueRank(rankElements) != 1) {
            throw new UaException(
                StatusCodes.Bad_TypeMismatch,
                "http://opcfoundation.org/UA/:EngineeringUnits (declaration i=17502, owner"
                    + " i=17497)");
          }
          long elementCount = 1;
          for (int dimension : matrix.getDimensions()) {
            if (dimension < 0 || elementCount > Integer.MAX_VALUE) {
              throw new UaException(
                  StatusCodes.Bad_TypeMismatch,
                  "http://opcfoundation.org/UA/:EngineeringUnits (declaration i=17502, owner"
                      + " i=17497)");
            }
            elementCount *= dimension;
          }
          if (elementCount != Array.getLength(rankElements)) {
            throw new UaException(
                StatusCodes.Bad_TypeMismatch,
                "http://opcfoundation.org/UA/:EngineeringUnits (declaration i=17502, owner"
                    + " i=17497)");
          }
        } else if (actualRank > 1) {
          throw new UaException(
              StatusCodes.Bad_TypeMismatch,
              "http://opcfoundation.org/UA/:EngineeringUnits (declaration i=17502, owner i=17497):"
                  + " use Matrix for multiple dimensions");
        }
      }
      try {
        rawValue = ExtensionObject.decodeValue(client.getStaticEncodingContext(), rawValue);
      } catch (UaRuntimeException failure) {
        throw new UaException(failure.getStatusCode().getValue(), failure);
      }
      Object element = rawValue;
      if (element != null && !(element instanceof EUInformation)) {
        throw new UaException(
            StatusCodes.Bad_TypeMismatch,
            "http://opcfoundation.org/UA/:EngineeringUnits (declaration i=17502, owner i=17497)");
      }
      converted = (EUInformation) element;
    }
    return converted;
  }

  @Override
  public void setEngineeringUnitsProperty(@Nullable EUInformation value) throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    var child = viewMember6();
    if (child == null) {
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:EngineeringUnits (declaration i=17502, owner i=17497)"
              + " on "
              + getNodeId());
    }
    var writeContext = views.localWriteContext(child.getNodeId(), value, KNOWN_ENUMS);
    Variant encoded =
        ValueChecks.validate(
            writeContext, value, ExpandedNodeId.parse("i=887"), -1, new long[] {}, null, false);
    views.checkOpen();
    child.setValue(encoded);
  }

  @Override
  public @Nullable EUInformation readEngineeringUnitsProperty() throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    return ViewFutures.await(readEngineeringUnitsPropertyAsync());
  }

  private CompletableFuture<? extends @Nullable EUInformation>
      readEngineeringUnitsPropertyAsyncImplementation() {
    return ViewFutures.map(
        ViewFutures.compose(
            viewMember6Async(),
            child -> {
              if (child == null) {
                throw new CompletionException(
                    new UaException(
                        StatusCodes.Bad_NotFound,
                        "http://opcfoundation.org/UA/:EngineeringUnits (declaration i=17502, owner"
                            + " i=17497) on "
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
            @Nullable EUInformation converted;
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
                      "http://opcfoundation.org/UA/:EngineeringUnits (declaration i=17502, owner"
                          + " i=17497)");
                }
                if (rawValue instanceof Matrix matrix) {
                  if (actualRank < 2
                      || rankElements == null
                      || !rankElements.getClass().isArray()
                      || ArrayUtil.getValueRank(rankElements) != 1) {
                    throw new UaException(
                        StatusCodes.Bad_TypeMismatch,
                        "http://opcfoundation.org/UA/:EngineeringUnits (declaration i=17502, owner"
                            + " i=17497)");
                  }
                  long elementCount = 1;
                  for (int dimension : matrix.getDimensions()) {
                    if (dimension < 0 || elementCount > Integer.MAX_VALUE) {
                      throw new UaException(
                          StatusCodes.Bad_TypeMismatch,
                          "http://opcfoundation.org/UA/:EngineeringUnits (declaration i=17502,"
                              + " owner i=17497)");
                    }
                    elementCount *= dimension;
                  }
                  if (elementCount != Array.getLength(rankElements)) {
                    throw new UaException(
                        StatusCodes.Bad_TypeMismatch,
                        "http://opcfoundation.org/UA/:EngineeringUnits (declaration i=17502, owner"
                            + " i=17497)");
                  }
                } else if (actualRank > 1) {
                  throw new UaException(
                      StatusCodes.Bad_TypeMismatch,
                      "http://opcfoundation.org/UA/:EngineeringUnits (declaration i=17502, owner"
                          + " i=17497): use Matrix for multiple dimensions");
                }
              }
              try {
                rawValue = ExtensionObject.decodeValue(client.getStaticEncodingContext(), rawValue);
              } catch (UaRuntimeException failure) {
                throw new UaException(failure.getStatusCode().getValue(), failure);
              }
              Object element = rawValue;
              if (element != null && !(element instanceof EUInformation)) {
                throw new UaException(
                    StatusCodes.Bad_TypeMismatch,
                    "http://opcfoundation.org/UA/:EngineeringUnits (declaration i=17502, owner"
                        + " i=17497)");
              }
              converted = (EUInformation) element;
            }
            return converted;
          } catch (UaException failure) {
            throw new CompletionException(failure);
          }
        });
  }

  @Override
  public CompletableFuture<? extends @Nullable EUInformation> readEngineeringUnitsPropertyAsync() {
    try {
      this.views.checkOpen();
      return this.views.ownRequest(this.readEngineeringUnitsPropertyAsyncImplementation());
    } catch (RuntimeException viewFailure) {
      return CompletableFuture.failedFuture(viewFailure);
    }
  }

  @Override
  public void writeEngineeringUnitsProperty(@Nullable EUInformation value) throws UaException {
    this.views.checkOpen();
    views.checkOpen();
    views.checkOpen();
    StatusCode status = ViewFutures.await(writeEngineeringUnitsPropertyAsync(value));
    if (status == null) {
      throw new UaException(StatusCodes.Bad_UnexpectedError);
    }
    if (!status.isGood()) {
      throw new UaException(status);
    }
  }

  private CompletableFuture<StatusCode> writeEngineeringUnitsPropertyAsyncImplementation(
      @Nullable EUInformation value) {
    return ViewFutures.compose(
        viewMember6Async(),
        child -> {
          if (child == null) {
            throw new CompletionException(
                new UaException(
                    StatusCodes.Bad_NotFound,
                    "http://opcfoundation.org/UA/:EngineeringUnits (declaration i=17502, owner"
                        + " i=17497) on "
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
                          ExpandedNodeId.parse("i=887"),
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
  public CompletableFuture<StatusCode> writeEngineeringUnitsPropertyAsync(
      @Nullable EUInformation value) {
    try {
      this.views.checkOpen();
      return this.views.ownRequest(this.writeEngineeringUnitsPropertyAsyncImplementation(value));
    } catch (RuntimeException viewFailure) {
      return CompletableFuture.failedFuture(viewFailure);
    }
  }
}
