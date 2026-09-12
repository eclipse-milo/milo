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

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part13/4.2.1/#4.2.1.2">https://reference.opcfoundation.org/v105/Core/docs/Part13/4.2.1/#4.2.1.2</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface AggregateConfigurationType extends BaseObjectType {
  QualifiedProperty<Boolean> TREAT_UNCERTAIN_AS_BAD =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "TreatUncertainAsBad",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          -1,
          Boolean.class);

  QualifiedProperty<UByte> PERCENT_DATA_BAD =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "PercentDataBad",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=3"),
          -1,
          UByte.class);

  QualifiedProperty<UByte> PERCENT_DATA_GOOD =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "PercentDataGood",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=3"),
          -1,
          UByte.class);

  QualifiedProperty<Boolean> USE_SLOPED_EXTRAPOLATION =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "UseSlopedExtrapolation",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          -1,
          Boolean.class);

  /** Gets the existing node's local value. */
  @Nullable Boolean getTreatUncertainAsBad() throws UaException;

  /** Sets the existing node's local value. */
  void setTreatUncertainAsBad(@Nullable Boolean value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Boolean readTreatUncertainAsBad() throws UaException;

  /** Writes the value remotely. */
  void writeTreatUncertainAsBad(@Nullable Boolean value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Boolean> readTreatUncertainAsBadAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeTreatUncertainAsBadAsync(@Nullable Boolean value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getTreatUncertainAsBadNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getTreatUncertainAsBadNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UByte getPercentDataBad() throws UaException;

  /** Sets the existing node's local value. */
  void setPercentDataBad(@Nullable UByte value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UByte readPercentDataBad() throws UaException;

  /** Writes the value remotely. */
  void writePercentDataBad(@Nullable UByte value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UByte> readPercentDataBadAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writePercentDataBadAsync(@Nullable UByte value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getPercentDataBadNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getPercentDataBadNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UByte getPercentDataGood() throws UaException;

  /** Sets the existing node's local value. */
  void setPercentDataGood(@Nullable UByte value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UByte readPercentDataGood() throws UaException;

  /** Writes the value remotely. */
  void writePercentDataGood(@Nullable UByte value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UByte> readPercentDataGoodAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writePercentDataGoodAsync(@Nullable UByte value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getPercentDataGoodNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getPercentDataGoodNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable Boolean getUseSlopedExtrapolation() throws UaException;

  /** Sets the existing node's local value. */
  void setUseSlopedExtrapolation(@Nullable Boolean value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Boolean readUseSlopedExtrapolation() throws UaException;

  /** Writes the value remotely. */
  void writeUseSlopedExtrapolation(@Nullable Boolean value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Boolean> readUseSlopedExtrapolationAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeUseSlopedExtrapolationAsync(@Nullable Boolean value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getUseSlopedExtrapolationNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getUseSlopedExtrapolationNodeAsync();
}
