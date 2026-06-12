/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.pubsub.config;

import java.util.UUID;

/**
 * Selects a field of a received dataset, e.g. when mapping fields to target variables in a {@link
 * TargetVariablesConfig}.
 *
 * <p>Fields can be selected by their DataSetFieldId ({@link FieldIdSelector}), their name ({@link
 * FieldNameSelector}), or their positional index in the dataset ({@link FieldIndexSelector}).
 *
 * <p>This interface is sealed, but new permitted subtypes may be added in future versions. Adding a
 * permit is source-compatible only for callers that include a {@code default} branch when switching
 * over instances of this type; always include one.
 */
public sealed interface FieldSelector
    permits FieldIdSelector, FieldNameSelector, FieldIndexSelector {

  /**
   * Select a field by its DataSetFieldId.
   *
   * @param fieldId the DataSetFieldId of the field.
   * @return a new {@link FieldIdSelector}.
   */
  static FieldSelector byId(UUID fieldId) {
    return new FieldIdSelector(fieldId);
  }

  /**
   * Select a field by its name.
   *
   * @param fieldName the name of the field.
   * @return a new {@link FieldNameSelector}.
   */
  static FieldSelector byName(String fieldName) {
    return new FieldNameSelector(fieldName);
  }

  /**
   * Select a field by its positional index in the dataset.
   *
   * @param index the zero-based index of the field.
   * @return a new {@link FieldIndexSelector}.
   */
  static FieldSelector byIndex(int index) {
    return new FieldIndexSelector(index);
  }
}
