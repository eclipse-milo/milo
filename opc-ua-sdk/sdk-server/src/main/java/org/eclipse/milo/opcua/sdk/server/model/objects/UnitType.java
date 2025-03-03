/*
 * Copyright (c) 2025 the Eclipse Milo Authors
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
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part8/6.4.2/#6.4.2.2">https://reference.opcfoundation.org/v105/Core/docs/Part8/6.4.2/#6.4.2.2</a>
 */
public interface UnitType extends BaseObjectType {
  QualifiedProperty<LocalizedText> SYMBOL =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "Symbol",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=21"),
          -1,
          LocalizedText.class);

  QualifiedProperty<String> UNIT_SYSTEM =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "UnitSystem",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  QualifiedProperty<String> DISCIPLINE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "Discipline",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  LocalizedText getSymbol();

  void setSymbol(LocalizedText value);

  PropertyType getSymbolNode();

  String getUnitSystem();

  void setUnitSystem(String value);

  PropertyType getUnitSystemNode();

  String getDiscipline();

  void setDiscipline(String value);

  PropertyType getDisciplineNode();
}
