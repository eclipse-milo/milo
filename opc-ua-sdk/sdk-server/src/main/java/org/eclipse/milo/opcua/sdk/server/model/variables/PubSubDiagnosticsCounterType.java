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
import org.eclipse.milo.opcua.stack.core.types.enumerated.DiagnosticsLevel;
import org.eclipse.milo.opcua.stack.core.types.enumerated.PubSubDiagnosticsCounterClassification;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.11/#9.1.11.5">https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.11/#9.1.11.5</a>
 */
public interface PubSubDiagnosticsCounterType extends BaseDataVariableType {
  QualifiedProperty<Boolean> ACTIVE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "Active",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          -1,
          Boolean.class);

  QualifiedProperty<PubSubDiagnosticsCounterClassification> CLASSIFICATION =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "Classification",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=19730"),
          -1,
          PubSubDiagnosticsCounterClassification.class);

  QualifiedProperty<DiagnosticsLevel> DIAGNOSTICS_LEVEL =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "DiagnosticsLevel",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=19723"),
          -1,
          DiagnosticsLevel.class);

  QualifiedProperty<DateTime> TIME_FIRST_CHANGE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "TimeFirstChange",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=13"),
          -1,
          DateTime.class);

  Boolean getActive();

  void setActive(Boolean value);

  PropertyType getActiveNode();

  PubSubDiagnosticsCounterClassification getClassification();

  void setClassification(PubSubDiagnosticsCounterClassification value);

  PropertyType getClassificationNode();

  DiagnosticsLevel getDiagnosticsLevel();

  void setDiagnosticsLevel(DiagnosticsLevel value);

  PropertyType getDiagnosticsLevelNode();

  DateTime getTimeFirstChange();

  void setTimeFirstChange(DateTime value);

  PropertyType getTimeFirstChangeNode();
}
