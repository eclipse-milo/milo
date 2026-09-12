/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
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
  @Nullable Boolean getTreatUncertainAsBad();

  /** Sets the existing node's local value. */
  void setTreatUncertainAsBad(@Nullable Boolean value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getTreatUncertainAsBadNode();

  /** Gets the existing node's local value. */
  @Nullable UByte getPercentDataBad();

  /** Sets the existing node's local value. */
  void setPercentDataBad(@Nullable UByte value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getPercentDataBadNode();

  /** Gets the existing node's local value. */
  @Nullable UByte getPercentDataGood();

  /** Sets the existing node's local value. */
  void setPercentDataGood(@Nullable UByte value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getPercentDataGoodNode();

  /** Gets the existing node's local value. */
  @Nullable Boolean getUseSlopedExtrapolation();

  /** Sets the existing node's local value. */
  void setUseSlopedExtrapolation(@Nullable Boolean value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getUseSlopedExtrapolationNode();
}
