package org.eclipse.milo.opcua.sdk.core.model.methods;

import java.util.Objects;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.Argument;
import org.eclipse.milo.opcua.stack.core.types.structured.LogRecordMask;
import org.eclipse.milo.opcua.stack.core.types.structured.LogRecordsDataType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Declared arguments and typed wire conversion for the GetRecords Method of LogObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part26/5.3">Model
 *     documentation</a>
 */
public final class LogObjectTypeGetRecords {
  private LogObjectTypeGetRecords() {}

  /** Returns the BrowseName resolved in the caller's namespace table. */
  public static QualifiedName browseName(NamespaceTable namespaceTable) {
    return new QualifiedName(
        Objects.requireNonNull(
            namespaceTable.getIndex("http://opcfoundation.org/UA/"),
            "missing Method BrowseName namespace"),
        "GetRecords");
  }

  /** Returns the declared input Arguments resolved in the caller's namespace table. */
  public static Argument[] inputArguments(NamespaceTable namespaceTable) {
    return new Argument[] {
      new Argument(
          "StartTime",
          ExpandedNodeId.of(Namespaces.OPC_UA, 13L)
              .toNodeId(namespaceTable)
              .orElseThrow(() -> new IllegalArgumentException("missing namespace for i=13")),
          -1,
          new UInteger[] {},
          new LocalizedText(null, "")),
      new Argument(
          "EndTime",
          ExpandedNodeId.of(Namespaces.OPC_UA, 13L)
              .toNodeId(namespaceTable)
              .orElseThrow(() -> new IllegalArgumentException("missing namespace for i=13")),
          -1,
          new UInteger[] {},
          new LocalizedText(null, "")),
      new Argument(
          "MaxReturnRecords",
          ExpandedNodeId.of(Namespaces.OPC_UA, 7L)
              .toNodeId(namespaceTable)
              .orElseThrow(() -> new IllegalArgumentException("missing namespace for i=7")),
          -1,
          new UInteger[] {},
          new LocalizedText(null, "")),
      new Argument(
          "MinimumSeverity",
          ExpandedNodeId.of(Namespaces.OPC_UA, 5L)
              .toNodeId(namespaceTable)
              .orElseThrow(() -> new IllegalArgumentException("missing namespace for i=5")),
          -1,
          new UInteger[] {},
          new LocalizedText(null, "")),
      new Argument(
          "RequestMask",
          ExpandedNodeId.of(Namespaces.OPC_UA, 19749L)
              .toNodeId(namespaceTable)
              .orElseThrow(() -> new IllegalArgumentException("missing namespace for i=19749")),
          -1,
          new UInteger[] {},
          new LocalizedText(null, "")),
      new Argument(
          "ContinuationPointIn",
          ExpandedNodeId.of(Namespaces.OPC_UA, 15L)
              .toNodeId(namespaceTable)
              .orElseThrow(() -> new IllegalArgumentException("missing namespace for i=15")),
          -1,
          new UInteger[] {},
          new LocalizedText(null, ""))
    };
  }

  /** Returns the declared output Arguments resolved in the caller's namespace table. */
  public static Argument[] outputArguments(NamespaceTable namespaceTable) {
    return new Argument[] {
      new Argument(
          "Results",
          ExpandedNodeId.of(Namespaces.OPC_UA, 19745L)
              .toNodeId(namespaceTable)
              .orElseThrow(() -> new IllegalArgumentException("missing namespace for i=19745")),
          -1,
          new UInteger[] {},
          new LocalizedText(null, "")),
      new Argument(
          "ContinuationPointOut",
          ExpandedNodeId.of(Namespaces.OPC_UA, 15L)
              .toNodeId(namespaceTable)
              .orElseThrow(() -> new IllegalArgumentException("missing namespace for i=15")),
          -1,
          new UInteger[] {},
          new LocalizedText(null, ""))
    };
  }

  /** Inputs values in declared order. */
  public static record Inputs(
      @Nullable DateTime startTime,
      @Nullable DateTime endTime,
      @Nullable UInteger maxReturnRecords,
      @Nullable UShort minimumSeverity,
      @Nullable LogRecordMask requestMask,
      @Nullable ByteString continuationPointIn) {
    /**
     * Encodes every declared position.
     *
     * @throws UaException if a value violates its declaration or cannot be encoded.
     */
    public Variant[] toVariants(EncodingContext context) throws UaException {
      return new Variant[] {
        MethodValues.encode(context, startTime, 0, DateTime.class, -1, new long[] {}, null),
        MethodValues.encode(context, endTime, 1, DateTime.class, -1, new long[] {}, null),
        MethodValues.encode(context, maxReturnRecords, 2, UInteger.class, -1, new long[] {}, null),
        MethodValues.encode(context, minimumSeverity, 3, UShort.class, -1, new long[] {}, null),
        MethodValues.encode(context, requestMask, 4, LogRecordMask.class, -1, new long[] {}, null),
        MethodValues.encode(
            context, continuationPointIn, 5, ByteString.class, -1, new long[] {}, null)
      };
    }

    /**
     * Decodes the declared positions.
     *
     * @throws UaException if the count, type, rank, dimensions or encoding is invalid.
     */
    public static Inputs fromVariants(EncodingContext context, Variant[] values)
        throws UaException {
      MethodValues.count(values, 6, 6);
      return new Inputs(
          (DateTime)
              MethodValues.decode(context, values, 0, DateTime.class, -1, new long[] {}, null),
          (DateTime)
              MethodValues.decode(context, values, 1, DateTime.class, -1, new long[] {}, null),
          (UInteger)
              MethodValues.decode(context, values, 2, UInteger.class, -1, new long[] {}, null),
          (UShort) MethodValues.decode(context, values, 3, UShort.class, -1, new long[] {}, null),
          (LogRecordMask)
              MethodValues.decode(context, values, 4, LogRecordMask.class, -1, new long[] {}, null),
          (ByteString)
              MethodValues.decode(context, values, 5, ByteString.class, -1, new long[] {}, null));
    }
  }

  /** Outputs values in declared order. */
  public static record Outputs(
      @Nullable LogRecordsDataType results, @Nullable ByteString continuationPointOut) {
    /**
     * Encodes every declared position.
     *
     * @throws UaException if a value violates its declaration or cannot be encoded.
     */
    public Variant[] toVariants(EncodingContext context) throws UaException {
      return new Variant[] {
        MethodValues.encode(context, results, 0, LogRecordsDataType.class, -1, new long[] {}, null),
        MethodValues.encode(
            context, continuationPointOut, 1, ByteString.class, -1, new long[] {}, null)
      };
    }

    /**
     * Decodes the declared positions.
     *
     * @throws UaException if the count, type, rank, dimensions or encoding is invalid.
     */
    public static Outputs fromVariants(EncodingContext context, Variant[] values)
        throws UaException {
      MethodValues.count(values, 2, 2);
      return new Outputs(
          (LogRecordsDataType)
              MethodValues.decode(
                  context, values, 0, LogRecordsDataType.class, -1, new long[] {}, null),
          (ByteString)
              MethodValues.decode(context, values, 1, ByteString.class, -1, new long[] {}, null));
    }
  }
}
