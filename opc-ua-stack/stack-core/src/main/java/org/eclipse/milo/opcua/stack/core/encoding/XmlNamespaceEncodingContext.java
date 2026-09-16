/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.encoding;

import java.util.HashMap;
import java.util.Map;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.ServerTable;
import org.eclipse.milo.opcua.stack.core.channel.EncodingLimits;
import org.eclipse.milo.opcua.stack.core.types.DataTypeManager;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;

final class XmlNamespaceEncodingContext implements EncodingContext {
  private final EncodingContext delegate;
  private final Map<String, String> mappings;

  XmlNamespaceEncodingContext(EncodingContext delegate, Map<String, String> mappings) {
    this.delegate = delegate;
    Map<String, String> combined = new HashMap<>();
    delegate.getXmlNamespaceUris().forEach((model, schema) -> add(combined, model, schema));
    mappings.forEach((model, schema) -> add(combined, model, schema));
    this.mappings = Map.copyOf(combined);
  }

  private static void add(Map<String, String> mappings, String model, String schema) {
    if (model.isBlank() || schema.isBlank()) {
      throw new IllegalArgumentException("XML namespace mappings must not be blank");
    }
    if (Namespaces.OPC_UA.equals(model) && !Namespaces.OPC_UA_XSD.equals(schema)) {
      throw new IllegalArgumentException("The standard UA XML namespace cannot be changed");
    }
    String previous = mappings.putIfAbsent(model, schema);
    if (previous != null && !previous.equals(schema)) {
      throw new IllegalArgumentException("Conflicting XML namespace mapping for " + model);
    }
  }

  @Override
  public Map<String, String> getXmlNamespaceUris() {
    return mappings;
  }

  @Override
  public DataTypeManager getDataTypeManager() {
    return delegate.getDataTypeManager();
  }

  @Override
  public EncodingManager getEncodingManager() {
    return delegate.getEncodingManager();
  }

  @Override
  public EncodingLimits getEncodingLimits() {
    return delegate.getEncodingLimits();
  }

  @Override
  public NamespaceTable getNamespaceTable() {
    return delegate.getNamespaceTable();
  }

  @Override
  public ServerTable getServerTable() {
    return delegate.getServerTable();
  }
}
