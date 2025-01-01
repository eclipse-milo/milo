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
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.structured.TransactionErrorType;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.11">https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.11</a>
 */
public interface TransactionDiagnosticsType extends BaseObjectType {
  QualifiedProperty<DateTime> START_TIME =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "StartTime",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=294"),
          -1,
          DateTime.class);

  QualifiedProperty<DateTime> END_TIME =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "EndTime",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=294"),
          -1,
          DateTime.class);

  QualifiedProperty<StatusCode> RESULT =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "Result",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=19"),
          -1,
          StatusCode.class);

  QualifiedProperty<NodeId[]> AFFECTED_TRUST_LISTS =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "AffectedTrustLists",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=17"),
          1,
          NodeId[].class);

  QualifiedProperty<NodeId[]> AFFECTED_CERTIFICATE_GROUPS =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "AffectedCertificateGroups",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=17"),
          1,
          NodeId[].class);

  QualifiedProperty<TransactionErrorType[]> ERRORS =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "Errors",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=32285"),
          1,
          TransactionErrorType[].class);

  DateTime getStartTime();

  void setStartTime(DateTime value);

  PropertyType getStartTimeNode();

  DateTime getEndTime();

  void setEndTime(DateTime value);

  PropertyType getEndTimeNode();

  StatusCode getResult();

  void setResult(StatusCode value);

  PropertyType getResultNode();

  NodeId[] getAffectedTrustLists();

  void setAffectedTrustLists(NodeId[] value);

  PropertyType getAffectedTrustListsNode();

  NodeId[] getAffectedCertificateGroups();

  void setAffectedCertificateGroups(NodeId[] value);

  PropertyType getAffectedCertificateGroupsNode();

  TransactionErrorType[] getErrors();

  void setErrors(TransactionErrorType[] value);

  PropertyType getErrorsNode();
}
