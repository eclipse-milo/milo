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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

class FileBasedCertificateQuarantineTest extends CertificateQuarantineTest {

  @Override
  protected CertificateQuarantine newCertificateQuarantine(int maxRejectedCertificates)
      throws IOException {
    File tempDirectory = Files.createTempDirectory(getClass().getSimpleName()).toFile();
    tempDirectory.deleteOnExit();

    return new FileBasedCertificateQuarantine(tempDirectory, maxRejectedCertificates);
  }

  @Override
  protected int getMaxRejectedCertificates() {
    return 3;
  }
}
