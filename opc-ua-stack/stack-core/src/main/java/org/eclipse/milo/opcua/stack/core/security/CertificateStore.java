/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.security;

import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.jspecify.annotations.Nullable;

public interface CertificateStore {

  /**
   * Check if this {@link CertificateStore} has an entry for {@code certificateTypeId}.
   *
   * @param certificateTypeId the id to check.
   * @return {@code true} if this {@link CertificateStore} is managing an entry for {@code
   *     certificateTypeId}.
   * @throws Exception if an error occurs while checking.
   */
  boolean contains(NodeId certificateTypeId) throws Exception;

  /**
   * Get the {@link Entry} for {@code certificateTypeId}.
   *
   * @param certificateTypeId the id of the certificate type to get the {@link Entry} for.
   * @return the {@link Entry} for {@code certificateTypeId}, or {@code null} if no entry exists for
   *     {@code certificateTypeId}.
   * @throws Exception if an error occurs while getting the {@link Entry}.
   */
  @Nullable Entry get(NodeId certificateTypeId) throws Exception;

  /**
   * Remove the {@link Entry} for {@code certificateTypeId}.
   *
   * @param certificateTypeId the id of the certificate type to remove the {@link Entry} for.
   * @return the {@link Entry} that was removed, or {@code null} if no entry exists for {@code
   *     certificateTypeId}.
   * @throws Exception if an error occurs while removing the {@link Entry}.
   */
  @Nullable Entry remove(NodeId certificateTypeId) throws Exception;

  /**
   * Set the {@link Entry} for {@code certificateTypeId}.
   *
   * @param certificateTypeId the alias to set the {@link Entry} for.
   * @param entry the {@link Entry} to set.
   * @throws Exception if an error occurs while setting the {@link Entry}.
   */
  void set(NodeId certificateTypeId, Entry entry) throws Exception;

  class Entry {
    public final PrivateKey privateKey;
    public final X509Certificate[] certificateChain;

    public Entry(PrivateKey privateKey, X509Certificate[] certificateChain) {
      this.privateKey = privateKey;
      this.certificateChain = certificateChain;
    }
  }
}
