/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.nodes.instantiation;

import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;

/**
 * Classification of an InstanceDeclaration's ModellingRule (OPC UA Part 3 §6.4.4).
 *
 * <p>Every rule is admitted by classification rather than filtering: the five standard rules map to
 * their own constants, and any other rule Object — vendor-defined rules are explicitly permitted by
 * Part 3 §6.4.4.1 — classifies as {@link #OTHER} with the raw rule {@link NodeId} preserved on the
 * {@link InstanceDeclaration}.
 */
public enum ModellingRule {

  /** Mandatory ({@code i=78}): a similar node shall exist for each existing BrowsePath. */
  MANDATORY,

  /** Optional ({@code i=80}): a similar node may exist. */
  OPTIONAL,

  /**
   * OptionalPlaceholder ({@code i=11508}): a documented extension point; never instantiated by
   * default.
   */
  OPTIONAL_PLACEHOLDER,

  /**
   * MandatoryPlaceholder ({@code i=11510}): at least one conforming member shall exist; never
   * instantiated by default.
   */
  MANDATORY_PLACEHOLDER,

  /**
   * ExposesItsArray ({@code i=83}): one member per array element of the exposing Variable; surfaced
   * in the model but never materialized by default.
   */
  EXPOSES_ITS_ARRAY,

  /**
   * Any non-standard rule Object. The raw rule id is preserved on the declaration and a model
   * diagnostic is emitted.
   */
  OTHER;

  /**
   * Classify a raw ModellingRule Object id.
   *
   * @param modellingRuleId the {@link NodeId} of the ModellingRule Object.
   * @return the classification; {@link #OTHER} for any id that is not one of the five standard
   *     rules.
   */
  public static ModellingRule of(NodeId modellingRuleId) {
    if (NodeIds.ModellingRule_Mandatory.equals(modellingRuleId)) {
      return MANDATORY;
    } else if (NodeIds.ModellingRule_Optional.equals(modellingRuleId)) {
      return OPTIONAL;
    } else if (NodeIds.ModellingRule_OptionalPlaceholder.equals(modellingRuleId)) {
      return OPTIONAL_PLACEHOLDER;
    } else if (NodeIds.ModellingRule_MandatoryPlaceholder.equals(modellingRuleId)) {
      return MANDATORY_PLACEHOLDER;
    } else if (NodeIds.ModellingRule_ExposesItsArray.equals(modellingRuleId)) {
      return EXPOSES_ITS_ARRAY;
    } else {
      return OTHER;
    }
  }

  /**
   * @return {@code true} for {@link #OPTIONAL_PLACEHOLDER} and {@link #MANDATORY_PLACEHOLDER}.
   */
  public boolean isPlaceholder() {
    return this == OPTIONAL_PLACEHOLDER || this == MANDATORY_PLACEHOLDER;
  }

  /**
   * @return {@code true} for rules recorded shallowly — placeholders and {@link #EXPOSES_ITS_ARRAY}
   *     — whose declaration subtrees are neither walked nor instantiated by default (Part 3
   *     §6.4.4.4.4–.5).
   */
  public boolean isShallow() {
    return isPlaceholder() || this == EXPOSES_ITS_ARRAY;
  }
}
