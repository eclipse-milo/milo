/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server;

import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;

/** A Namespace is an {@link AddressSpace} that manages all nodes for a given namespace index. */
public interface Namespace extends AddressSpace {

  /**
   * @return the index of this {@link Namespace} in the server's namespace array.
   */
  UShort getNamespaceIndex();

  /**
   * @return the URI identifying this {@link Namespace}.
   */
  String getNamespaceUri();
}
