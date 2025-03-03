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

import org.eclipse.milo.opcua.sdk.server.model.variables.BaseDataVariableType;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.5.4">https://reference.opcfoundation.org/v105/Core/docs/Part22/5.5.4</a>
 */
public interface LldpRemoteStatisticsType extends BaseObjectType {
  BaseDataVariableType getLastChangeTimeNode();

  UInteger getLastChangeTime();

  void setLastChangeTime(UInteger value);

  BaseDataVariableType getRemoteInsertsNode();

  UInteger getRemoteInserts();

  void setRemoteInserts(UInteger value);

  BaseDataVariableType getRemoteDeletesNode();

  UInteger getRemoteDeletes();

  void setRemoteDeletes(UInteger value);

  BaseDataVariableType getRemoteDropsNode();

  UInteger getRemoteDrops();

  void setRemoteDrops(UInteger value);

  BaseDataVariableType getRemoteAgeoutsNode();

  UInteger getRemoteAgeouts();

  void setRemoteAgeouts(UInteger value);
}
