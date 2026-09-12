/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.model.variables;

import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.2">https://reference.opcfoundation.org/v105/Core/docs/Part9/5.2</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface TwoStateVariableType extends StateVariableType {
  QualifiedProperty<Boolean> ID =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "Id",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          -1,
          Boolean.class);

  QualifiedProperty<DateTime> TRANSITION_TIME =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "TransitionTime",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=294"),
          -1,
          DateTime.class);

  QualifiedProperty<DateTime> EFFECTIVE_TRANSITION_TIME =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "EffectiveTransitionTime",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=294"),
          -1,
          DateTime.class);

  QualifiedProperty<LocalizedText> TRUE_STATE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "TrueState",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=21"),
          -1,
          LocalizedText.class);

  QualifiedProperty<LocalizedText> FALSE_STATE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "FalseState",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=21"),
          -1,
          LocalizedText.class);

  /** Gets the existing node's local value. */
  @Nullable Boolean getId();

  /** Sets the existing node's local value. */
  void setId(@Nullable Boolean value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getIdNode();

  /** Gets the existing node's local value. */
  @Nullable DateTime getTransitionTime();

  /** Sets the existing node's local value. */
  void setTransitionTime(@Nullable DateTime value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getTransitionTimeNode();

  /** Gets the existing node's local value. */
  @Nullable DateTime getEffectiveTransitionTime();

  /** Sets the existing node's local value. */
  void setEffectiveTransitionTime(@Nullable DateTime value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getEffectiveTransitionTimeNode();

  /** Gets the existing node's local value. */
  @Nullable LocalizedText getTrueState();

  /** Sets the existing node's local value. */
  void setTrueState(@Nullable LocalizedText value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getTrueStateNode();

  /** Gets the existing node's local value. */
  @Nullable LocalizedText getFalseState();

  /** Sets the existing node's local value. */
  void setFalseState(@Nullable LocalizedText value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getFalseStateNode();
}
