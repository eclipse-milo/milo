/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.encoding;

import java.util.Map;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.ServerTable;
import org.eclipse.milo.opcua.stack.core.channel.EncodingLimits;
import org.eclipse.milo.opcua.stack.core.types.DataTypeManager;

public interface EncodingContext {

  /**
   * Get the {@link DataTypeManager}.
   *
   * @return the {@link DataTypeManager}.
   */
  DataTypeManager getDataTypeManager();

  /**
   * Get the {@link EncodingManager}.
   *
   * @return the {@link EncodingManager}.
   */
  EncodingManager getEncodingManager();

  /**
   * Get the {@link EncodingLimits}.
   *
   * @return the {@link EncodingLimits}.
   */
  EncodingLimits getEncodingLimits();

  /**
   * Get the {@link NamespaceTable}.
   *
   * @return the {@link NamespaceTable}.
   */
  NamespaceTable getNamespaceTable();

  /**
   * Get the {@link ServerTable}.
   *
   * @return the {@link ServerTable}.
   */
  ServerTable getServerTable();

  /**
   * Returns model URI to XML schema namespace mappings for this context.
   *
   * <p>Keys identify models in the namespace table; values identify XML elements, as specified by
   * Part 6 F.2 {@code XmlSchemaUri}. These mappings never change NodeId or encoding ID identities.
   * Missing entries preserve the model URI as the XML namespace. Namespace zero always uses the
   * standard UA Types XML namespace. Implementations must return an immutable, non-null map with
   * nonblank keys and values, stable for the duration of an encoding operation.
   *
   * @return the XML namespace mappings, empty by default for compatibility.
   */
  default Map<String, String> getXmlNamespaceUris() {
    return Map.of();
  }

  /**
   * Returns a view of this context with additional XML schema namespace mappings.
   *
   * <p>The view snapshots the combined mappings and shares this context's managers, limits, and
   * namespace and server tables. It can be passed to encoders and Default XML ExtensionObject
   * encoding. Supply mappings from model metadata or an authoritative schema's target namespace; no
   * schema URI suffix is inferred. Resolve conflicting metadata before creating the view.
   *
   * <pre>{@code
   * EncodingContext xmlContext = context.withXmlNamespaceUris(
   *     Map.of("http://opcfoundation.org/UA/DI/", "http://opcfoundation.org/UA/DI/Types.xsd"));
   * }</pre>
   *
   * @param mappings the model URI to XML schema namespace mappings to add.
   * @return a context view with immutable mappings.
   * @throws IllegalArgumentException if a key or value is blank, a mapping conflicts with an
   *     existing mapping, or a mapping changes the standard UA XML namespace.
   * @throws NullPointerException if the map or any key or value is null.
   */
  default EncodingContext withXmlNamespaceUris(Map<String, String> mappings) {
    return new XmlNamespaceEncodingContext(this, mappings);
  }
}
