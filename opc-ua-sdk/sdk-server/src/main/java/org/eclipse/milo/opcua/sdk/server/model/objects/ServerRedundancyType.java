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
import org.eclipse.milo.opcua.stack.core.types.enumerated.RedundancySupport;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.3.7">https://reference.opcfoundation.org/v105/Core/docs/Part5/6.3.7</a>
 */
public interface ServerRedundancyType extends BaseObjectType {
  QualifiedProperty<RedundancySupport> REDUNDANCY_SUPPORT =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "RedundancySupport",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=851"),
          -1,
          RedundancySupport.class);

  RedundancySupport getRedundancySupport();

  void setRedundancySupport(RedundancySupport value);

  PropertyType getRedundancySupportNode();
}
