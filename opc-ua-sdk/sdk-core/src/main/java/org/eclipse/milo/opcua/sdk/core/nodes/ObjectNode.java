/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.core.nodes;

import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;

public interface ObjectNode extends Node {

  /**
   * The EventNotifier attribute identifies whether the Object can be used to subscribe to Events or
   * to read and write the history of the Events.
   *
   * @return the EventNotifier attribute of this Object.
   */
  UByte getEventNotifier();

  /**
   * Set the EventNotifier attribute of this Object.
   *
   * @param eventNotifier the EventNotifier attribute to set.
   */
  void setEventNotifier(UByte eventNotifier);
}
