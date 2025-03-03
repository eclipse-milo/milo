/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.model.variables;

import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part19/7.2">https://reference.opcfoundation.org/v105/Core/docs/Part19/7.2</a>
 */
public interface MultiStateDictionaryEntryDiscreteType
    extends MultiStateDictionaryEntryDiscreteBaseType {
  QualifiedProperty<NodeId[]> VALUE_AS_DICTIONARY_ENTRIES =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ValueAsDictionaryEntries",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=17"),
          1,
          NodeId[].class);

  NodeId[] getValueAsDictionaryEntries();

  void setValueAsDictionaryEntries(NodeId[] value);

  PropertyType getValueAsDictionaryEntriesNode();
}
