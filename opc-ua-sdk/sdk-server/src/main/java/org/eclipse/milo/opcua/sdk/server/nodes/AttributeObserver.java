/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.nodes;

import org.eclipse.milo.opcua.stack.core.AttributeId;

public interface AttributeObserver {

  /**
   * The Attribute indicated by {@code attributeId} on {@code node} changed.
   *
   * @param node the {@link UaNode} the change originated from.
   * @param attributeId the {@link AttributeId} that changed.
   * @param value the new value of the attribute.
   */
  void attributeChanged(UaNode node, AttributeId attributeId, Object value);
}
