/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.encoding.xml;

import static org.junit.jupiter.api.Assertions.*;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaSerializationException;
import org.eclipse.milo.opcua.stack.core.channel.EncodingLimits;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.GenericDataTypeCodec;
import org.eclipse.milo.opcua.stack.core.encoding.UaDecoder;
import org.eclipse.milo.opcua.stack.core.encoding.UaEncoder;
import org.eclipse.milo.opcua.stack.core.types.DataTypeManager;
import org.eclipse.milo.opcua.stack.core.types.DefaultDataTypeManager;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DiagnosticInfo;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.XmlElement;
import org.eclipse.milo.opcua.stack.core.types.structured.XVType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

class OpcUaXmlDecoderArrayTest {
  private static String xml(String content) {
    return "<Items xmlns='urn:example' xmlns:u='http://opcfoundation.org/UA/2008/02/Types.xsd'"
        + " xmlns:n='http://www.w3.org/2001/XMLSchema-instance'>"
        + content
        + "</Items>";
  }

  // Part 6 §5.3.4 puts members directly in the field container, even with mixed namespaces.
  @Test
  void directStringArrayRetainsMembers() throws Exception {
    try (var decoder =
        new OpcUaXmlDecoder(
            DefaultEncodingContext.INSTANCE,
            xml("\n<!-- first --><u:String>a</u:String>\n<u:String>b</u:String>"))) {
      assertArrayEquals(new String[] {"a", "b"}, decoder.decodeStringArray("Items"));
    }
  }

  // A nil member must not make the containing array null or drop the following member.
  @Test
  void directStringArrayRetainsNilFirstMember() throws Exception {
    try (var decoder =
        new OpcUaXmlDecoder(
            DefaultEncodingContext.INSTANCE,
            xml("<u:String n:nil='true'/><u:String>b</u:String>"))) {
      assertArrayEquals(new String[] {null, "b"}, decoder.decodeStringArray("Items"));
    }
  }

  // Enum fields carry their raw numeric values, including values unknown to a Java enum.
  @Test
  void directEnumArrayRetainsRawValues() throws Exception {
    try (var decoder =
        new OpcUaXmlDecoder(
            DefaultEncodingContext.INSTANCE,
            xml("<State>Seven_7</State><!-- next --><State>Other_123</State>"))) {
      assertArrayEquals(new Integer[] {7, 123}, decoder.decodeEnumArray("Items"));
    }
  }

  // Iterating the first structure's fields would produce default structures instead of members.
  @Test
  void directStructureArrayRetainsValuesAndRuntimeType() throws Exception {
    try (var decoder =
        new OpcUaXmlDecoder(
            DefaultEncodingContext.INSTANCE,
            xml(
                "<u:XVType><u:X>2</u:X><u:Value>3</u:Value></u:XVType>\n"
                    + "<u:XVType><u:X>4</u:X><u:Value>5</u:Value></u:XVType>"))) {
      XVType[] values =
          assertInstanceOf(XVType[].class, decoder.decodeStructArray("Items", NodeIds.XVType));
      assertArrayEquals(new XVType[] {new XVType(2.0, 3.0f), new XVType(4.0, 5.0f)}, values);
    }
  }

  // Nil/empty/missing containers must remain distinct, with the next field still readable.
  @ParameterizedTest
  @MethodSource("arrayKinds")
  void arrayContainersRetainNullEmptyAndOmittedSemantics(ArrayKind kind) throws Exception {
    inStructure(
        DefaultEncodingContext.INSTANCE,
        "<Following>ok</Following>",
        input -> {
          assertNull(kind.read.apply(input));
          assertEquals("ok", input.decodeString("Following"));
          assertNull(kind.read.apply(input), "trailing omitted field");
        });
    inStructure(
        DefaultEncodingContext.INSTANCE,
        "<Items n:nil='true'/><Following>ok</Following>",
        input -> {
          assertNull(kind.read.apply(input));
          assertEquals("ok", input.decodeString("Following"));
        });
    inStructure(
        DefaultEncodingContext.INSTANCE,
        "<Items><!-- empty --></Items><Following>ok</Following>",
        input -> {
          Object[] values = kind.read.apply(input);
          assertEquals(kind.arrayType, values.getClass());
          assertEquals(0, values.length);
          assertEquals("ok", input.decodeString("Following"));
        });
  }

  // Nullable scalar members retain their position, including the first and last positions.
  @ParameterizedTest
  @ValueSource(ints = {0, 1, 2})
  void nullableMembersRetainTheirPositions(int nilIndex) throws Exception {
    String[] members = {
      "<u:String>a</u:String>", "<u:String>b</u:String>", "<u:String>c</u:String>"
    };
    String[] expected = {"a", "b", "c"};
    members[nilIndex] = "<u:String n:nil='true'/>";
    expected[nilIndex] = null;
    inStructure(
        DefaultEncodingContext.INSTANCE,
        "<Items>" + String.join("\n<!-- member -->", members) + "</Items><Following>ok</Following>",
        input -> {
          assertArrayEquals(expected, input.decodeStringArray("Items"));
          assertEquals("ok", input.decodeString("Following"));
        });
  }

  // Builtin null sentinels and empty values are preserved by the individual member decoders.
  @Test
  void directByteStringAndXmlElementArraysRetainNullAndEmptyMembers() throws Exception {
    inStructure(
        DefaultEncodingContext.INSTANCE,
        "<Bytes><u:ByteString"
            + " n:nil='true'/><u:ByteString/><u:ByteString>AQ==</u:ByteString></Bytes><Fragments><u:XmlElement"
            + " n:nil='true'/><u:XmlElement/><u:XmlElement><Payload"
            + " n:nil='true'/></u:XmlElement></Fragments><Following>ok</Following>",
        input -> {
          assertArrayEquals(
              new ByteString[] {
                ByteString.NULL_VALUE, ByteString.of(new byte[0]), ByteString.of(new byte[] {1})
              },
              input.decodeByteStringArray("Bytes"));
          XmlElement[] fragments = input.decodeXmlElementArray("Fragments");
          assertEquals(3, fragments.length);
          assertEquals(XmlElement.NULL_VALUE, fragments[0]);
          assertEquals(XmlElement.of(""), fragments[1]);
          assertTrue(fragments[2].getFragment().contains("nil=\"true\""));
          assertEquals("ok", input.decodeString("Following"));
        });
  }

  // Inline nil structures continue to use the scalar structure codec's default-field semantics.
  @ParameterizedTest
  @ValueSource(ints = {0, 1, 2})
  void nilStructureMembersKeepScalarDefaults(int nilIndex) throws Exception {
    String populated = "<u:XVType><u:X>2</u:X><u:Value>3</u:Value></u:XVType>";
    String[] members = {populated, populated, populated};
    XVType[] expected = {new XVType(2.0, 3.0f), new XVType(2.0, 3.0f), new XVType(2.0, 3.0f)};
    members[nilIndex] = "<u:XVType n:nil='true'/>";
    expected[nilIndex] = new XVType(0.0, 0.0f);
    inStructure(
        DefaultEncodingContext.INSTANCE,
        "<Items>" + String.join("", members) + "</Items><Following>ok</Following>",
        input -> {
          assertArrayEquals(expected, input.decodeStructArray("Items", NodeIds.XVType.expanded()));
          assertEquals("ok", input.decodeString("Following"));
        });
  }

  // Length checks count direct members and run before decoding the first excess member.
  @ParameterizedTest
  @MethodSource("arrayKinds")
  void arrayLengthLimitAcceptsBoundaryAndRestoresCursorOnOverflow(ArrayKind kind) throws Exception {
    EncodingContext context =
        new DefaultEncodingContext() {
          @Override
          public EncodingLimits getEncodingLimits() {
            return new EncodingLimits(8196, 1, 2, 128);
          }
        };
    inStructure(
        context,
        "<Items>" + kind.member.repeat(2) + "</Items><Following>ok</Following>",
        input -> {
          assertEquals(2, kind.read.apply(input).length);
          assertEquals("ok", input.decodeString("Following"));
        });
    inStructure(
        context,
        "<Items>"
            + kind.member.repeat(2)
            + kind.invalidMember
            + "</Items><Following>ok</Following>",
        input -> {
          UaSerializationException error =
              assertThrows(UaSerializationException.class, () -> kind.read.apply(input));
          assertEquals(StatusCodes.Bad_EncodingLimitsExceeded, error.getStatusCode().getValue());
          assertEquals("ok", input.decodeString("Following"));
        });
  }

  // Recovery must leave the member subtree even when a scalar parser or structure codec throws.
  @ParameterizedTest
  @MethodSource("arrayKinds")
  void malformedArrayMemberRestoresCursor(ArrayKind kind) throws Exception {
    inStructure(
        DefaultEncodingContext.INSTANCE,
        "<Items>" + kind.member + kind.invalidMember + "</Items><Following>ok</Following>",
        input -> {
          UaSerializationException error =
              assertThrows(UaSerializationException.class, () -> kind.read.apply(input));
          assertEquals(StatusCodes.Bad_DecodingError, error.getStatusCode().getValue());
          assertEquals("ok", input.decodeString("Following"));
        });
  }

  @ParameterizedTest
  @MethodSource("arrayKinds")
  void invalidContainerNilRestoresCursor(ArrayKind kind) throws Exception {
    inStructure(
        DefaultEncodingContext.INSTANCE,
        "<Items n:nil='invalid'/><Following>ok</Following>",
        input -> {
          UaSerializationException error =
              assertThrows(UaSerializationException.class, () -> kind.read.apply(input));
          assertEquals(StatusCodes.Bad_DecodingError, error.getStatusCode().getValue());
          assertEquals("ok", input.decodeString("Following"));
        });
  }

  // A missing codec must not strand the cursor; a nil array does not require a codec at all.
  @Test
  void missingStructureCodecRestoresCursor() throws Exception {
    NodeId unknown = new NodeId(2, "unknown");
    inStructure(
        DefaultEncodingContext.INSTANCE,
        "<Items/><Following>ok</Following>",
        input -> {
          UaSerializationException error =
              assertThrows(
                  UaSerializationException.class, () -> input.decodeStructArray("Items", unknown));
          assertEquals(StatusCodes.Bad_DecodingError, error.getStatusCode().getValue());
          assertEquals("ok", input.decodeString("Following"));
        });
    inStructure(
        DefaultEncodingContext.INSTANCE,
        "<Items n:nil='true'/><Following>ok</Following>",
        input -> {
          assertNull(input.decodeStructArray("Items", unknown));
          assertEquals("ok", input.decodeString("Following"));
        });
  }

  // Variant arrays have a separate ListOf... container, which must retain its existing behavior.
  @Test
  void variantArrayControlsRetainMembersAndContainerSemantics() throws Exception {
    inStructure(
        DefaultEncodingContext.INSTANCE,
        "<Populated><u:Value><u:ListOfString><u:String n:nil='true'/><u:String>b</u:String>"
            + "</u:ListOfString></u:Value></Populated>"
            + "<Empty><u:Value><u:ListOfString/></u:Value></Empty><Nil><u:Value><u:ListOfString"
            + " n:nil='true'/></u:Value></Nil><Following>ok</Following>",
        input -> {
          assertArrayEquals(
              new String[] {null, "b"}, (String[]) input.decodeVariant("Populated").value());
          assertArrayEquals(new String[0], (String[]) input.decodeVariant("Empty").value());
          assertNull(input.decodeVariant("Nil").value());
          assertEquals("ok", input.decodeString("Following"));
        });
  }

  // Hand-authored DI wire layout from UA-Nodeset 6338cced, DI/Opc.Ua.Di.Types.xsd:
  // ParameterDefs contains ParameterResultDataType; NodePath contains QualifiedName directly.
  @Test
  void diParameterDefinitionsRetainNestedNodePathsAndFollowingFields() throws Exception {
    EncodingContext context = parameterContext();
    inStructure(
        context,
        """
        <SequenceNumber>42</SequenceNumber><EndOfResults>true</EndOfResults>
        <ParameterDefs>
          <ParameterResultDataType>
            <NodePath>
              <u:QualifiedName><u:NamespaceIndex>2</u:NamespaceIndex><u:Name>Device</u:Name></u:QualifiedName>
              <u:QualifiedName><u:NamespaceIndex>3</u:NamespaceIndex><u:Name>Pressure</u:Name></u:QualifiedName>
            </NodePath>
            <StatusCode><u:Code>0</u:Code></StatusCode>
            <Diagnostics><u:AdditionalInfo>first</u:AdditionalInfo></Diagnostics>
          </ParameterResultDataType>
          <ParameterResultDataType>
            <NodePath><u:QualifiedName><u:NamespaceIndex>4</u:NamespaceIndex><u:Name>Temperature</u:Name></u:QualifiedName></NodePath>
            <StatusCode><u:Code>2147549184</u:Code></StatusCode>
            <Diagnostics><u:AdditionalInfo>second</u:AdditionalInfo></Diagnostics>
          </ParameterResultDataType>
        </ParameterDefs><Following>ok</Following>
        """,
        input -> {
          assertEquals(42, input.decodeInt32("SequenceNumber"));
          assertTrue(input.decodeBoolean("EndOfResults"));
          ParameterResult[] values =
              assertInstanceOf(
                  ParameterResult[].class, input.decodeStructArray("ParameterDefs", PARAMETER_ID));
          assertEquals(2, values.length);
          assertArrayEquals(
              new QualifiedName[] {
                new QualifiedName(2, "Device"), new QualifiedName(3, "Pressure")
              },
              values[0].nodePath);
          assertArrayEquals(
              new QualifiedName[] {new QualifiedName(4, "Temperature")}, values[1].nodePath);
          assertEquals(StatusCode.GOOD, values[0].statusCode);
          assertEquals(new StatusCode(StatusCodes.Bad_UnexpectedError), values[1].statusCode);
          assertEquals("first", values[0].diagnostics.additionalInfo());
          assertEquals("second", values[1].diagnostics.additionalInfo());
          assertEquals("ok", input.decodeString("Following"));
        });
  }

  // A legitimate type named List is a member, never an inferred legacy wrapper.
  @Test
  void listNamedStructureIsDecodedAsMember() throws Exception {
    inStructure(
        parameterContext(),
        """
        <Items><List><NodePath><u:QualifiedName><u:Name>Pressure</u:Name></u:QualifiedName></NodePath>
        <StatusCode><u:Code>0</u:Code></StatusCode></List></Items><Following>ok</Following>
        """,
        input -> {
          ParameterResult[] values =
              assertInstanceOf(
                  ParameterResult[].class, input.decodeStructArray("Items", PARAMETER_ID));
          assertEquals(1, values.length);
          assertArrayEquals(
              new QualifiedName[] {new QualifiedName(0, "Pressure")}, values[0].nodePath);
          assertEquals("ok", input.decodeString("Following"));
        });
  }

  private record ArrayKind(
      Function<UaDecoder, Object[]> read,
      Class<?> arrayType,
      String member,
      String invalidMember) {}

  static Stream<ArrayKind> arrayKinds() {
    return Stream.of(
        new ArrayKind(
            d -> d.decodeStringArray("Items"),
            String[].class,
            "<u:String>a</u:String>",
            "<u:String n:nil='invalid'/>"),
        new ArrayKind(
            d -> d.decodeEnumArray("Items"),
            Integer[].class,
            "<State>Seven_7</State>",
            "<State>invalid</State>"),
        new ArrayKind(
            d -> d.decodeStructArray("Items", NodeIds.XVType),
            XVType[].class,
            "<u:XVType><u:X>2</u:X><u:Value>3</u:Value></u:XVType>",
            "<u:XVType><u:X>invalid</u:X></u:XVType>"));
  }

  private static void inStructure(
      EncodingContext context, String fields, Consumer<UaDecoder> assertions) throws Exception {
    String document =
        "<Root xmlns='http://opcfoundation.org/UA/DI/Types.xsd'"
            + " xmlns:u='http://opcfoundation.org/UA/2008/02/Types.xsd'"
            + " xmlns:n='http://www.w3.org/2001/XMLSchema-instance'>"
            + fields
            + "</Root>";
    try (var decoder = new OpcUaXmlDecoder(context, document)) {
      decoder.decodeStruct(
          "Root",
          new GenericDataTypeCodec<XVType>() {
            @Override
            public Class<XVType> getType() {
              return XVType.class;
            }

            @Override
            public XVType decodeType(EncodingContext context, UaDecoder input) {
              assertions.accept(input);
              return new XVType(0.0, 0.0f);
            }

            @Override
            public void encodeType(EncodingContext context, UaEncoder encoder, XVType value) {
              throw new UnsupportedOperationException();
            }
          });
    }
  }

  private static final NodeId PARAMETER_ID = new NodeId(2, "ParameterResult");

  private static EncodingContext parameterContext() {
    var manager = new DefaultDataTypeManager();
    manager.registerType(
        PARAMETER_ID,
        new GenericDataTypeCodec<ParameterResult>() {
          @Override
          public Class<ParameterResult> getType() {
            return ParameterResult.class;
          }

          @Override
          public ParameterResult decodeType(EncodingContext context, UaDecoder input) {
            return new ParameterResult(
                input.decodeQualifiedNameArray("NodePath"),
                input.decodeStatusCode("StatusCode"),
                input.decodeDiagnosticInfo("Diagnostics"));
          }

          @Override
          public void encodeType(
              EncodingContext context, UaEncoder encoder, ParameterResult value) {
            throw new UnsupportedOperationException();
          }
        },
        null,
        null,
        null);
    return new DefaultEncodingContext() {
      @Override
      public DataTypeManager getDataTypeManager() {
        return manager;
      }
    };
  }

  private record ParameterResult(
      QualifiedName[] nodePath, StatusCode statusCode, DiagnosticInfo diagnostics)
      implements UaStructuredType {
    @Override
    public ExpandedNodeId getTypeId() {
      return PARAMETER_ID.expanded();
    }

    @Override
    public ExpandedNodeId getBinaryEncodingId() {
      return ExpandedNodeId.NULL_VALUE;
    }

    @Override
    public ExpandedNodeId getXmlEncodingId() {
      return ExpandedNodeId.NULL_VALUE;
    }

    @Override
    public ExpandedNodeId getJsonEncodingId() {
      return ExpandedNodeId.NULL_VALUE;
    }
  }
}
