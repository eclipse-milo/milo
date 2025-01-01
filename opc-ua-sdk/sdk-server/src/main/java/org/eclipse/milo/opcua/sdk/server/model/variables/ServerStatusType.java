/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.model.variables;

import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ServerState;
import org.eclipse.milo.opcua.stack.core.types.structured.BuildInfo;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.6">https://reference.opcfoundation.org/v105/Core/docs/Part5/7.6</a>
 */
public interface ServerStatusType extends BaseDataVariableType {
  BaseDataVariableType getStartTimeNode();

  DateTime getStartTime();

  void setStartTime(DateTime value);

  BaseDataVariableType getCurrentTimeNode();

  DateTime getCurrentTime();

  void setCurrentTime(DateTime value);

  BaseDataVariableType getStateNode();

  ServerState getState();

  void setState(ServerState value);

  BuildInfoType getBuildInfoNode();

  BuildInfo getBuildInfo();

  void setBuildInfo(BuildInfo value);

  BaseDataVariableType getSecondsTillShutdownNode();

  UInteger getSecondsTillShutdown();

  void setSecondsTillShutdown(UInteger value);

  BaseDataVariableType getShutdownReasonNode();

  LocalizedText getShutdownReason();

  void setShutdownReason(LocalizedText value);
}
