/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack;

import org.eclipse.milo.opcua.stack.core.channel.headers.SequenceHeader;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class SequenceHeaderTest extends SerializationFixture2 {

  @DataProvider(name = "parameters")
  public Object[][] getParameters() {
    return new Object[][] {
      {0, 0},
      {Integer.MAX_VALUE - 1, Integer.MAX_VALUE - 1},
      {Integer.MAX_VALUE, Integer.MAX_VALUE},
      {Integer.MAX_VALUE + 1L, Integer.MAX_VALUE + 1L},
      {UInteger.MAX_VALUE, UInteger.MAX_VALUE}
    };
  }

  @Test(dataProvider = "parameters", description = "SequenceHeader is serializable.")
  public void testSerialization(long sequenceNumber, long requestId) {
    SequenceHeader header = new SequenceHeader(sequenceNumber, requestId);

    assertSerializable(header, SequenceHeader::encode, SequenceHeader::decode);
  }
}
