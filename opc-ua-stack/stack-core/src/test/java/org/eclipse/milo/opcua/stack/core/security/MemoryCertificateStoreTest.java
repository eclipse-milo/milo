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

class MemoryCertificateStoreTest extends CertificateStoreTest {

  @Override
  protected CertificateStore newCertificateStore() {
    return new MemoryCertificateStore();
  }
}
