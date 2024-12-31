/*
 * Copyright (c) 2024 the Eclipse Milo Authors
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
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.13/#9.1.13.3">https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.13/#9.1.13.3</a>
 */
public interface PubSubCommunicationFailureEventType extends PubSubStatusEventType {
  QualifiedProperty<StatusCode> ERROR =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "Error",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=19"),
          -1,
          StatusCode.class);

  StatusCode getError();

  void setError(StatusCode value);

  PropertyType getErrorNode();
}
