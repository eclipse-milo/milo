package org.eclipse.milo.opcua.sdk.core.model.methods;

import java.util.Arrays;
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
 * Declared arguments and typed wire conversion for the GetRejectedList Method of
 * CertificateGroupType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.3/#7.8.3.2">Model
 *     documentation</a>
 */
public final class CertificateGroupTypeGetRejectedList {
  private CertificateGroupTypeGetRejectedList() {}

  /** Returns the BrowseName resolved in the caller's namespace table. */
  public static QualifiedName browseName(NamespaceTable namespaceTable) {
    return new QualifiedName(
        Objects.requireNonNull(
            namespaceTable.getIndex("http://opcfoundation.org/UA/"),
            "missing Method BrowseName namespace"),
        "GetRejectedList");
  }

  /** Returns the declared input Arguments resolved in the caller's namespace table. */
  public static Argument[] inputArguments(NamespaceTable namespaceTable) {
    return new Argument[] {};
  }

  /** Returns the declared output Arguments resolved in the caller's namespace table. */
  public static Argument[] outputArguments(NamespaceTable namespaceTable) {
    return new Argument[] {
      new Argument(
          "Certificates",
          ExpandedNodeId.of(Namespaces.OPC_UA, 15L)
              .toNodeId(namespaceTable)
              .orElseThrow(() -> new IllegalArgumentException("missing namespace for i=15")),
          1,
          new UInteger[] {UInteger.valueOf(0L)},
          new LocalizedText(null, ""))
    };
  }

  /** Outputs values in declared order. */
  public static record Outputs(ByteString @Nullable [] certificates) {
    /**
     * Encodes every declared position.
     *
     * @throws UaException if a value violates its declaration or cannot be encoded.
     */
    public Variant[] toVariants(EncodingContext context) throws UaException {
      return new Variant[] {
        MethodValues.encode(context, certificates, 0, ByteString.class, 1, new long[] {0L}, null)
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
          (ByteString[])
              MethodValues.decode(context, values, 0, ByteString.class, 1, new long[] {0L}, null));
    }

    @Override
    public boolean equals(@Nullable Object other) {
      return other instanceof Outputs value
          && Arrays.deepEquals(new Object[] {certificates}, new Object[] {value.certificates});
    }

    @Override
    public int hashCode() {
      return Arrays.deepHashCode(new Object[] {certificates});
    }

    @Override
    public String toString() {
      return "Outputs" + Arrays.deepToString(new Object[] {certificates});
    }
  }
}
