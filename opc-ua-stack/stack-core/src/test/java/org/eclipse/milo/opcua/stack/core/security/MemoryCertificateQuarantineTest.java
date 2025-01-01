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

class MemoryCertificateQuarantineTest extends CertificateQuarantineTest {

  @Override
  protected CertificateQuarantine newCertificateQuarantine(int maxRejectedCertificates) {
    return new MemoryCertificateQuarantine(maxRejectedCertificates);
  }

  @Override
  protected int getMaxRejectedCertificates() {
    return 3;
  }
}
