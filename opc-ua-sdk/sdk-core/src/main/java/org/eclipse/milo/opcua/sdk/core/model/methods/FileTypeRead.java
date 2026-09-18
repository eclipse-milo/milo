package org.eclipse.milo.opcua.sdk.core.model.methods;

import java.util.Objects;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.Argument;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Declared arguments and typed wire conversion for the Read Method of FileType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part20/4.2.4">Model
 *     documentation</a>
 */
public final class FileTypeRead {
  private FileTypeRead() {}

  /** Returns the BrowseName resolved in the caller's namespace table. */
  public static QualifiedName browseName(NamespaceTable namespaceTable) {
    return new QualifiedName(
        Objects.requireNonNull(
            namespaceTable.getIndex("http://opcfoundation.org/UA/"),
            "missing Method BrowseName namespace"),
        "Read");
  }

  /** Returns the declared input Arguments resolved in the caller's namespace table. */
  public static Argument[] inputArguments(NamespaceTable namespaceTable) {
    return new Argument[] {
      new Argument(
          "FileHandle",
          ExpandedNodeId.of(Namespaces.OPC_UA, 7L)
              .toNodeId(namespaceTable)
              .orElseThrow(() -> new IllegalArgumentException("missing namespace for i=7")),
          -1,
          new UInteger[] {},
          new LocalizedText(null, "")),
      new Argument(
          "Length",
          ExpandedNodeId.of(Namespaces.OPC_UA, 6L)
              .toNodeId(namespaceTable)
              .orElseThrow(() -> new IllegalArgumentException("missing namespace for i=6")),
          -1,
          new UInteger[] {},
          new LocalizedText(null, ""))
    };
  }

  /** Returns the declared output Arguments resolved in the caller's namespace table. */
  public static Argument[] outputArguments(NamespaceTable namespaceTable) {
    return new Argument[] {
      new Argument(
          "Data",
          ExpandedNodeId.of(Namespaces.OPC_UA, 15L)
              .toNodeId(namespaceTable)
              .orElseThrow(() -> new IllegalArgumentException("missing namespace for i=15")),
          -1,
          new UInteger[] {},
          new LocalizedText(null, ""))
    };
  }

  /** Inputs values in declared order. */
  public static record Inputs(@Nullable UInteger fileHandle, @Nullable Integer length) {
    /**
     * Encodes every declared position.
     *
     * @throws UaException if a value violates its declaration or cannot be encoded.
     */
    public Variant[] toVariants(EncodingContext context) throws UaException {
      return new Variant[] {
        MethodValues.encode(context, fileHandle, 0, UInteger.class, -1, new long[] {}, null),
        MethodValues.encode(context, length, 1, Integer.class, -1, new long[] {}, null)
      };
    }

    /**
     * Decodes the declared positions.
     *
     * @throws UaException if the count, type, rank, dimensions or encoding is invalid.
     */
    public static Inputs fromVariants(EncodingContext context, Variant[] values)
        throws UaException {
      MethodValues.count(values, 2, 2);
      return new Inputs(
          (UInteger)
              MethodValues.decode(context, values, 0, UInteger.class, -1, new long[] {}, null),
          (Integer)
              MethodValues.decode(context, values, 1, Integer.class, -1, new long[] {}, null));
    }
  }

  /** Outputs values in declared order. */
  public static record Outputs(@Nullable ByteString data) {
    /**
     * Encodes every declared position.
     *
     * @throws UaException if a value violates its declaration or cannot be encoded.
     */
    public Variant[] toVariants(EncodingContext context) throws UaException {
      return new Variant[] {
        MethodValues.encode(context, data, 0, ByteString.class, -1, new long[] {}, null)
      };
    }

    /**
     * Decodes the declared positions.
     *
     * @throws UaException if the count, type, rank, dimensions or encoding is invalid.
     */
    public static Outputs fromVariants(EncodingContext context, Variant[] values)
        throws UaException {
      MethodValues.count(values, 1, 1);
      return new Outputs(
          (ByteString)
              MethodValues.decode(context, values, 0, ByteString.class, -1, new long[] {}, null));
    }
  }
}
