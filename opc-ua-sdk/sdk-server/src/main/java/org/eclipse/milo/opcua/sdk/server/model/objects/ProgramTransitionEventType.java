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

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part10/5.2.5/#5.2.5.2">https://reference.opcfoundation.org/v105/Core/docs/Part10/5.2.5/#5.2.5.2</a>
 */
public interface ProgramTransitionEventType extends TransitionEventType {
  BaseDataVariableType getIntermediateResultNode();

  Object getIntermediateResult();

  void setIntermediateResult(Object value);
}
