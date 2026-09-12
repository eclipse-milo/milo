/*
 * Copyright (c) 2026 the Eclipse Milo Authors
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
import org.eclipse.milo.opcua.sdk.server.model.variables.SessionDiagnosticsVariableType;
import org.eclipse.milo.opcua.sdk.server.model.variables.SessionSecurityDiagnosticsType;
import org.eclipse.milo.opcua.sdk.server.model.variables.SubscriptionDiagnosticsArrayType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.structured.SessionDiagnosticsDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.SessionSecurityDiagnosticsDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.SubscriptionDiagnosticsDataType;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.3.5">https://reference.opcfoundation.org/v105/Core/docs/Part5/6.3.5</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface SessionDiagnosticsObjectType extends BaseObjectType {
  QualifiedProperty<NodeId[]> CURRENT_ROLE_IDS =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "CurrentRoleIds",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=17"),
          1,
          NodeId[].class);

  /** Gets the existing node's local value. */
  @Nullable NodeId @Nullable [] getCurrentRoleIds();

  /** Sets the existing node's local value. */
  void setCurrentRoleIds(@Nullable NodeId @Nullable [] value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getCurrentRoleIdsNode();

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  SessionDiagnosticsVariableType getSessionDiagnosticsNode();

  /** Gets the existing node's local value. */
  @Nullable SessionDiagnosticsDataType getSessionDiagnostics();

  /** Sets the existing node's local value. */
  void setSessionDiagnostics(@Nullable SessionDiagnosticsDataType value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  SessionSecurityDiagnosticsType getSessionSecurityDiagnosticsNode();

  /** Gets the existing node's local value. */
  @Nullable SessionSecurityDiagnosticsDataType getSessionSecurityDiagnostics();

  /** Sets the existing node's local value. */
  void setSessionSecurityDiagnostics(@Nullable SessionSecurityDiagnosticsDataType value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  SubscriptionDiagnosticsArrayType getSubscriptionDiagnosticsArrayNode();

  /** Gets the existing node's local value. */
  @Nullable SubscriptionDiagnosticsDataType @Nullable [] getSubscriptionDiagnosticsArray();

  /** Sets the existing node's local value. */
  void setSubscriptionDiagnosticsArray(
      @Nullable SubscriptionDiagnosticsDataType @Nullable [] value);
}
