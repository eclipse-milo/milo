/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.security;

import java.util.Comparator;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.jspecify.annotations.NullMarked;

@NullMarked
final class CertificateIdentityOrdering {

  static final Comparator<CertificateIdentity> STABLE =
      Comparator.comparing(CertificateIdentityOrdering::certificateGroupSortKey)
          .thenComparing(CertificateIdentityOrdering::certificateTypeSortKey);

  private CertificateIdentityOrdering() {}

  private static String certificateGroupSortKey(CertificateIdentity identity) {
    return nodeIdSortKey(identity.certificateGroupId());
  }

  private static String certificateTypeSortKey(CertificateIdentity identity) {
    return nodeIdSortKey(identity.certificateTypeId());
  }

  private static String nodeIdSortKey(NodeId nodeId) {
    return nodeId.toParseableString();
  }
}
