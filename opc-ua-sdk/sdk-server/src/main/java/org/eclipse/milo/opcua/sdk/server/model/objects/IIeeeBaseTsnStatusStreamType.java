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

import org.eclipse.milo.opcua.sdk.server.model.variables.BaseDataVariableType;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TsnFailureCode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TsnListenerStatus;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TsnTalkerStatus;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.2.9">https://reference.opcfoundation.org/v105/Core/docs/Part22/5.2.9</a>
 */
public interface IIeeeBaseTsnStatusStreamType extends BaseInterfaceType {
  BaseDataVariableType getTalkerStatusNode();

  TsnTalkerStatus getTalkerStatus();

  void setTalkerStatus(TsnTalkerStatus value);

  BaseDataVariableType getListenerStatusNode();

  TsnListenerStatus getListenerStatus();

  void setListenerStatus(TsnListenerStatus value);

  BaseDataVariableType getFailureCodeNode();

  TsnFailureCode getFailureCode();

  void setFailureCode(TsnFailureCode value);

  BaseDataVariableType getFailureSystemIdentifierNode();

  Object getFailureSystemIdentifier();

  void setFailureSystemIdentifier(Object value);
}
