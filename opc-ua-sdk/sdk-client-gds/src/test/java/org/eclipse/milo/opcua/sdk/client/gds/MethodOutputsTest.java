/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.gds;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ApplicationType;
import org.eclipse.milo.opcua.stack.core.types.structured.ApplicationDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.ServerOnNetwork;
import org.junit.jupiter.api.Test;

/**
 * A GDS that deviates from the spec's output argument list must produce a diagnosable {@code
 * Bad_UnexpectedError} rather than a {@link ClassCastException} escaping from a wrapper.
 */
public class MethodOutputsTest {

  @Test
  void wrongOutputTypeIsReportedAsBadUnexpectedErrorNamingMethodAndTypes() throws Exception {
    MethodOutputs outputs =
        MethodOutputs.of("GetTrustList", new Variant[] {Variant.ofString("not a NodeId")}, 1);

    UaException e = assertThrows(UaException.class, () -> outputs.scalar(0, NodeId.class));

    assertEquals(StatusCodes.Bad_UnexpectedError, e.getStatusCode().value());
    assertTrue(e.getMessage().contains("GetTrustList"), e.getMessage());
    assertTrue(e.getMessage().contains("NodeId"), e.getMessage());
    assertTrue(e.getMessage().contains("String"), e.getMessage());
  }

  @Test
  void wrongOutputCountIsReportedAsBadUnexpectedError() {
    UaException e =
        assertThrows(
            UaException.class, () -> MethodOutputs.of("GetCertificateStatus", new Variant[0], 1));

    assertEquals(StatusCodes.Bad_UnexpectedError, e.getStatusCode().value());
    assertTrue(e.getMessage().contains("GetCertificateStatus"), e.getMessage());
  }

  @Test
  void missingRequiredScalarIsReportedAsBadUnexpectedError() throws Exception {
    MethodOutputs outputs =
        MethodOutputs.of("RegisterApplication", new Variant[] {Variant.ofNull()}, 1);

    UaException e = assertThrows(UaException.class, () -> outputs.scalar(0, NodeId.class));

    assertEquals(StatusCodes.Bad_UnexpectedError, e.getStatusCode().value());
  }

  // Servers encode an empty array as null on the wire; callers should not have to null-check.
  @Test
  void nullArrayOutputDecodesAsEmptyArray() throws Exception {
    MethodOutputs outputs =
        MethodOutputs.of("GetCertificateGroups", new Variant[] {Variant.ofNull()}, 1);

    assertArrayEquals(new NodeId[0], outputs.array(0, NodeId.class));
  }

  // Structure outputs arrive as ExtensionObjects and must be decoded through the encoding context
  // into the typed class the wrapper promises.
  @Test
  void extensionObjectArrayDecodesToTypedStructures() throws Exception {
    var description =
        new ApplicationDescription(
            "urn:app",
            "urn:product",
            LocalizedText.english("App"),
            ApplicationType.Server,
            null,
            null,
            new String[] {"opc.tcp://localhost:4840"});
    ExtensionObject encoded = ExtensionObject.encode(DefaultEncodingContext.INSTANCE, description);

    MethodOutputs outputs =
        MethodOutputs.of(
            "QueryApplications", new Variant[] {new Variant(new ExtensionObject[] {encoded})}, 1);

    ApplicationDescription[] decoded =
        outputs.structArray(0, ApplicationDescription.class, DefaultEncodingContext.INSTANCE);

    assertArrayEquals(new ApplicationDescription[] {description}, decoded);
  }

  @Test
  void structureOfWrongTypeIsReportedAsBadUnexpectedError() {
    var description =
        new ApplicationDescription(
            "urn:app", null, LocalizedText.NULL_VALUE, ApplicationType.Server, null, null, null);
    ExtensionObject encoded = ExtensionObject.encode(DefaultEncodingContext.INSTANCE, description);

    UaException e =
        assertThrows(
            UaException.class,
            () ->
                MethodOutputs.of("GetApplication", new Variant[] {new Variant(encoded)}, 1)
                    .struct(0, ServerOnNetwork.class, DefaultEncodingContext.INSTANCE));

    assertEquals(StatusCodes.Bad_UnexpectedError, e.getStatusCode().value());
  }
}
