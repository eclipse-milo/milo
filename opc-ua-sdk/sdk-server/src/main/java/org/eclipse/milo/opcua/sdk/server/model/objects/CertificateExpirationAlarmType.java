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

import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.24/#5.8.24.7">https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.24/#5.8.24.7</a>
 */
public interface CertificateExpirationAlarmType extends SystemOffNormalAlarmType {
  QualifiedProperty<DateTime> EXPIRATION_DATE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ExpirationDate",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=13"),
          -1,
          DateTime.class);

  QualifiedProperty<Double> EXPIRATION_LIMIT =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ExpirationLimit",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=290"),
          -1,
          Double.class);

  QualifiedProperty<NodeId> CERTIFICATE_TYPE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "CertificateType",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=17"),
          -1,
          NodeId.class);

  QualifiedProperty<ByteString> CERTIFICATE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "Certificate",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=15"),
          -1,
          ByteString.class);

  DateTime getExpirationDate();

  void setExpirationDate(DateTime value);

  PropertyType getExpirationDateNode();

  Double getExpirationLimit();

  void setExpirationLimit(Double value);

  PropertyType getExpirationLimitNode();

  NodeId getCertificateType();

  void setCertificateType(NodeId value);

  PropertyType getCertificateTypeNode();

  ByteString getCertificate();

  void setCertificate(ByteString value);

  PropertyType getCertificateNode();
}
