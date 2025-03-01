package org.eclipse.milo.opcua.stack.core.types.structured;

import java.util.StringJoiner;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.GenericDataTypeCodec;
import org.eclipse.milo.opcua.stack.core.encoding.UaDecoder;
import org.eclipse.milo.opcua.stack.core.encoding.UaEncoder;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.DiagnosticInfo;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.util.codegen.EqualsBuilder;
import org.eclipse.milo.opcua.stack.core.util.codegen.HashCodeBuilder;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part4/7.34">https://reference.opcfoundation.org/v105/Core/docs/Part4/7.34</a>
 */
public class ResponseHeader extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=392");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=394");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=393");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=15089");

  private final DateTime timestamp;

  private final UInteger requestHandle;

  private final StatusCode serviceResult;

  private final DiagnosticInfo serviceDiagnostics;

  private final String @Nullable [] stringTable;

  private final ExtensionObject additionalHeader;

  public ResponseHeader(
      DateTime timestamp,
      UInteger requestHandle,
      StatusCode serviceResult,
      DiagnosticInfo serviceDiagnostics,
      String @Nullable [] stringTable,
      ExtensionObject additionalHeader) {
    this.timestamp = timestamp;
    this.requestHandle = requestHandle;
    this.serviceResult = serviceResult;
    this.serviceDiagnostics = serviceDiagnostics;
    this.stringTable = stringTable;
    this.additionalHeader = additionalHeader;
  }

  @Override
  public ExpandedNodeId getTypeId() {
    return TYPE_ID;
  }

  @Override
  public ExpandedNodeId getBinaryEncodingId() {
    return BINARY_ENCODING_ID;
  }

  @Override
  public ExpandedNodeId getXmlEncodingId() {
    return XML_ENCODING_ID;
  }

  @Override
  public ExpandedNodeId getJsonEncodingId() {
    return JSON_ENCODING_ID;
  }

  public DateTime getTimestamp() {
    return timestamp;
  }

  public UInteger getRequestHandle() {
    return requestHandle;
  }

  public StatusCode getServiceResult() {
    return serviceResult;
  }

  public DiagnosticInfo getServiceDiagnostics() {
    return serviceDiagnostics;
  }

  public String @Nullable [] getStringTable() {
    return stringTable;
  }

  public ExtensionObject getAdditionalHeader() {
    return additionalHeader;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    ResponseHeader that = (ResponseHeader) object;
    var eqb = new EqualsBuilder();
    eqb.append(getTimestamp(), that.getTimestamp());
    eqb.append(getRequestHandle(), that.getRequestHandle());
    eqb.append(getServiceResult(), that.getServiceResult());
    eqb.append(getServiceDiagnostics(), that.getServiceDiagnostics());
    eqb.append(getStringTable(), that.getStringTable());
    eqb.append(getAdditionalHeader(), that.getAdditionalHeader());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getTimestamp());
    hcb.append(getRequestHandle());
    hcb.append(getServiceResult());
    hcb.append(getServiceDiagnostics());
    hcb.append(getStringTable());
    hcb.append(getAdditionalHeader());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", ResponseHeader.class.getSimpleName() + "[", "]");
    joiner.add("timestamp=" + getTimestamp());
    joiner.add("requestHandle=" + getRequestHandle());
    joiner.add("serviceResult=" + getServiceResult());
    joiner.add("serviceDiagnostics=" + getServiceDiagnostics());
    joiner.add("stringTable=" + java.util.Arrays.toString(getStringTable()));
    joiner.add("additionalHeader=" + getAdditionalHeader());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 394),
        new NodeId(0, 22),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "Timestamp",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 294),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "RequestHandle",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 288),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "ServiceResult",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 19),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "ServiceDiagnostics",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 25),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "StringTable",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12),
              1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "AdditionalHeader",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 22),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<ResponseHeader> {
    @Override
    public Class<ResponseHeader> getType() {
      return ResponseHeader.class;
    }

    @Override
    public ResponseHeader decodeType(EncodingContext context, UaDecoder decoder) {
      final DateTime timestamp;
      final UInteger requestHandle;
      final StatusCode serviceResult;
      final DiagnosticInfo serviceDiagnostics;
      final String[] stringTable;
      final ExtensionObject additionalHeader;
      timestamp = decoder.decodeDateTime("Timestamp");
      requestHandle = decoder.decodeUInt32("RequestHandle");
      serviceResult = decoder.decodeStatusCode("ServiceResult");
      serviceDiagnostics = decoder.decodeDiagnosticInfo("ServiceDiagnostics");
      stringTable = decoder.decodeStringArray("StringTable");
      additionalHeader = decoder.decodeExtensionObject("AdditionalHeader");
      return new ResponseHeader(
          timestamp,
          requestHandle,
          serviceResult,
          serviceDiagnostics,
          stringTable,
          additionalHeader);
    }

    @Override
    public void encodeType(EncodingContext context, UaEncoder encoder, ResponseHeader value) {
      encoder.encodeDateTime("Timestamp", value.getTimestamp());
      encoder.encodeUInt32("RequestHandle", value.getRequestHandle());
      encoder.encodeStatusCode("ServiceResult", value.getServiceResult());
      encoder.encodeDiagnosticInfo("ServiceDiagnostics", value.getServiceDiagnostics());
      encoder.encodeStringArray("StringTable", value.getStringTable());
      encoder.encodeExtensionObject("AdditionalHeader", value.getAdditionalHeader());
    }
  }
}
