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
import org.eclipse.milo.opcua.stack.core.types.enumerated.NegotiationStatus;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.2.3">https://reference.opcfoundation.org/v105/Core/docs/Part22/5.2.3</a>
 */
public interface IIeeeAutoNegotiationStatusType extends BaseInterfaceType {
  BaseDataVariableType getNegotiationStatusNode();

  NegotiationStatus getNegotiationStatus();

  void setNegotiationStatus(NegotiationStatus value);
}
