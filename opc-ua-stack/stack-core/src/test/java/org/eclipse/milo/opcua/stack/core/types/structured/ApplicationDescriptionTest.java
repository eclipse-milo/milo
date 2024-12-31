/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.types.structured;

import static org.testng.Assert.assertEquals;

import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ApplicationType;
import org.junit.jupiter.api.Test;

public class ApplicationDescriptionTest {

  @Test
  public void testEqualsAndHashCode() {
    ApplicationDescription applicationDescription1 =
        new ApplicationDescription(
            "applicationUri",
            "productUri",
            LocalizedText.english("test"),
            ApplicationType.Server,
            null,
            null,
            new String[] {"a", "b", "c"});

    ApplicationDescription applicationDescription2 =
        new ApplicationDescription(
            "applicationUri",
            "productUri",
            LocalizedText.english("test"),
            ApplicationType.Server,
            null,
            null,
            new String[] {"a", "b", "c"});

    assertEquals(applicationDescription1, applicationDescription2);
    assertEquals(applicationDescription1.hashCode(), applicationDescription2.hashCode());
  }
}
