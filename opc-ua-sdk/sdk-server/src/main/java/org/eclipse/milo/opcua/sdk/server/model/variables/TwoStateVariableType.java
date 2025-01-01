/*
 * Copyright (c) 2024 the Eclipse Milo Authors
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

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.2">https://reference.opcfoundation.org/v105/Core/docs/Part9/5.2</a>
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

  Boolean getId();

  void setId(Boolean value);

  PropertyType getIdNode();

  DateTime getTransitionTime();

  void setTransitionTime(DateTime value);

  PropertyType getTransitionTimeNode();

  DateTime getEffectiveTransitionTime();

  void setEffectiveTransitionTime(DateTime value);

  PropertyType getEffectiveTransitionTimeNode();

  LocalizedText getTrueState();

  void setTrueState(LocalizedText value);

  PropertyType getTrueStateNode();

  LocalizedText getFalseState();

  void setFalseState(LocalizedText value);

  PropertyType getFalseStateNode();
}
