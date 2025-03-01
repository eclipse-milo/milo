package org.eclipse.milo.opcua.stack.core.types.structured;

import java.util.StringJoiner;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.GenericDataTypeCodec;
import org.eclipse.milo.opcua.stack.core.encoding.UaDecoder;
import org.eclipse.milo.opcua.stack.core.encoding.UaEncoder;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.util.codegen.EqualsBuilder;
import org.eclipse.milo.opcua.stack.core.util.codegen.HashCodeBuilder;
import org.jspecify.annotations.Nullable;

public class ProgramDiagnosticDataType extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=894");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=896");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=895");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=15381");

  private final NodeId createSessionId;

  private final @Nullable String createClientName;

  private final DateTime invocationCreationTime;

  private final DateTime lastTransitionTime;

  private final @Nullable String lastMethodCall;

  private final NodeId lastMethodSessionId;

  private final Argument @Nullable [] lastMethodInputArguments;

  private final Argument @Nullable [] lastMethodOutputArguments;

  private final DateTime lastMethodCallTime;

  private final StatusResult lastMethodReturnStatus;

  public ProgramDiagnosticDataType(
      NodeId createSessionId,
      @Nullable String createClientName,
      DateTime invocationCreationTime,
      DateTime lastTransitionTime,
      @Nullable String lastMethodCall,
      NodeId lastMethodSessionId,
      Argument @Nullable [] lastMethodInputArguments,
      Argument @Nullable [] lastMethodOutputArguments,
      DateTime lastMethodCallTime,
      StatusResult lastMethodReturnStatus) {
    this.createSessionId = createSessionId;
    this.createClientName = createClientName;
    this.invocationCreationTime = invocationCreationTime;
    this.lastTransitionTime = lastTransitionTime;
    this.lastMethodCall = lastMethodCall;
    this.lastMethodSessionId = lastMethodSessionId;
    this.lastMethodInputArguments = lastMethodInputArguments;
    this.lastMethodOutputArguments = lastMethodOutputArguments;
    this.lastMethodCallTime = lastMethodCallTime;
    this.lastMethodReturnStatus = lastMethodReturnStatus;
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

  public NodeId getCreateSessionId() {
    return createSessionId;
  }

  public @Nullable String getCreateClientName() {
    return createClientName;
  }

  public DateTime getInvocationCreationTime() {
    return invocationCreationTime;
  }

  public DateTime getLastTransitionTime() {
    return lastTransitionTime;
  }

  public @Nullable String getLastMethodCall() {
    return lastMethodCall;
  }

  public NodeId getLastMethodSessionId() {
    return lastMethodSessionId;
  }

  public Argument @Nullable [] getLastMethodInputArguments() {
    return lastMethodInputArguments;
  }

  public Argument @Nullable [] getLastMethodOutputArguments() {
    return lastMethodOutputArguments;
  }

  public DateTime getLastMethodCallTime() {
    return lastMethodCallTime;
  }

  public StatusResult getLastMethodReturnStatus() {
    return lastMethodReturnStatus;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    ProgramDiagnosticDataType that = (ProgramDiagnosticDataType) object;
    var eqb = new EqualsBuilder();
    eqb.append(getCreateSessionId(), that.getCreateSessionId());
    eqb.append(getCreateClientName(), that.getCreateClientName());
    eqb.append(getInvocationCreationTime(), that.getInvocationCreationTime());
    eqb.append(getLastTransitionTime(), that.getLastTransitionTime());
    eqb.append(getLastMethodCall(), that.getLastMethodCall());
    eqb.append(getLastMethodSessionId(), that.getLastMethodSessionId());
    eqb.append(getLastMethodInputArguments(), that.getLastMethodInputArguments());
    eqb.append(getLastMethodOutputArguments(), that.getLastMethodOutputArguments());
    eqb.append(getLastMethodCallTime(), that.getLastMethodCallTime());
    eqb.append(getLastMethodReturnStatus(), that.getLastMethodReturnStatus());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getCreateSessionId());
    hcb.append(getCreateClientName());
    hcb.append(getInvocationCreationTime());
    hcb.append(getLastTransitionTime());
    hcb.append(getLastMethodCall());
    hcb.append(getLastMethodSessionId());
    hcb.append(getLastMethodInputArguments());
    hcb.append(getLastMethodOutputArguments());
    hcb.append(getLastMethodCallTime());
    hcb.append(getLastMethodReturnStatus());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", ProgramDiagnosticDataType.class.getSimpleName() + "[", "]");
    joiner.add("createSessionId=" + getCreateSessionId());
    joiner.add("createClientName='" + getCreateClientName() + "'");
    joiner.add("invocationCreationTime=" + getInvocationCreationTime());
    joiner.add("lastTransitionTime=" + getLastTransitionTime());
    joiner.add("lastMethodCall='" + getLastMethodCall() + "'");
    joiner.add("lastMethodSessionId=" + getLastMethodSessionId());
    joiner.add(
        "lastMethodInputArguments=" + java.util.Arrays.toString(getLastMethodInputArguments()));
    joiner.add(
        "lastMethodOutputArguments=" + java.util.Arrays.toString(getLastMethodOutputArguments()));
    joiner.add("lastMethodCallTime=" + getLastMethodCallTime());
    joiner.add("lastMethodReturnStatus=" + getLastMethodReturnStatus());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 896),
        new NodeId(0, 22),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "CreateSessionId",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 17),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "CreateClientName",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "InvocationCreationTime",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 294),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "LastTransitionTime",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 294),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "LastMethodCall",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "LastMethodSessionId",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 17),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "LastMethodInputArguments",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 296),
              1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "LastMethodOutputArguments",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 296),
              1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "LastMethodCallTime",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 294),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "LastMethodReturnStatus",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 299),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<ProgramDiagnosticDataType> {
    @Override
    public Class<ProgramDiagnosticDataType> getType() {
      return ProgramDiagnosticDataType.class;
    }

    @Override
    public ProgramDiagnosticDataType decodeType(EncodingContext context, UaDecoder decoder) {
      final NodeId createSessionId;
      final String createClientName;
      final DateTime invocationCreationTime;
      final DateTime lastTransitionTime;
      final String lastMethodCall;
      final NodeId lastMethodSessionId;
      final Argument[] lastMethodInputArguments;
      final Argument[] lastMethodOutputArguments;
      final DateTime lastMethodCallTime;
      final StatusResult lastMethodReturnStatus;
      createSessionId = decoder.decodeNodeId("CreateSessionId");
      createClientName = decoder.decodeString("CreateClientName");
      invocationCreationTime = decoder.decodeDateTime("InvocationCreationTime");
      lastTransitionTime = decoder.decodeDateTime("LastTransitionTime");
      lastMethodCall = decoder.decodeString("LastMethodCall");
      lastMethodSessionId = decoder.decodeNodeId("LastMethodSessionId");
      lastMethodInputArguments =
          (Argument[]) decoder.decodeStructArray("LastMethodInputArguments", Argument.TYPE_ID);
      lastMethodOutputArguments =
          (Argument[]) decoder.decodeStructArray("LastMethodOutputArguments", Argument.TYPE_ID);
      lastMethodCallTime = decoder.decodeDateTime("LastMethodCallTime");
      lastMethodReturnStatus =
          (StatusResult) decoder.decodeStruct("LastMethodReturnStatus", StatusResult.TYPE_ID);
      return new ProgramDiagnosticDataType(
          createSessionId,
          createClientName,
          invocationCreationTime,
          lastTransitionTime,
          lastMethodCall,
          lastMethodSessionId,
          lastMethodInputArguments,
          lastMethodOutputArguments,
          lastMethodCallTime,
          lastMethodReturnStatus);
    }

    @Override
    public void encodeType(
        EncodingContext context, UaEncoder encoder, ProgramDiagnosticDataType value) {
      encoder.encodeNodeId("CreateSessionId", value.getCreateSessionId());
      encoder.encodeString("CreateClientName", value.getCreateClientName());
      encoder.encodeDateTime("InvocationCreationTime", value.getInvocationCreationTime());
      encoder.encodeDateTime("LastTransitionTime", value.getLastTransitionTime());
      encoder.encodeString("LastMethodCall", value.getLastMethodCall());
      encoder.encodeNodeId("LastMethodSessionId", value.getLastMethodSessionId());
      encoder.encodeStructArray(
          "LastMethodInputArguments", value.getLastMethodInputArguments(), Argument.TYPE_ID);
      encoder.encodeStructArray(
          "LastMethodOutputArguments", value.getLastMethodOutputArguments(), Argument.TYPE_ID);
      encoder.encodeDateTime("LastMethodCallTime", value.getLastMethodCallTime());
      encoder.encodeStruct(
          "LastMethodReturnStatus", value.getLastMethodReturnStatus(), StatusResult.TYPE_ID);
    }
  }
}
