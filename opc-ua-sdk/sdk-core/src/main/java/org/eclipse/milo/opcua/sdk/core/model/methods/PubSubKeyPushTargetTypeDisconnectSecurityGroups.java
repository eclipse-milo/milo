package org.eclipse.milo.opcua.sdk.core.model.methods;

import java.util.Arrays;
import java.util.Objects;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.Argument;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Declared arguments and typed wire conversion for the DisconnectSecurityGroups Method of
 * PubSubKeyPushTargetType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/8.6.4">Model
 *     documentation</a>
 */
public final class PubSubKeyPushTargetTypeDisconnectSecurityGroups {
  private PubSubKeyPushTargetTypeDisconnectSecurityGroups() {}

  /** Returns the BrowseName resolved in the caller's namespace table. */
  public static QualifiedName browseName(NamespaceTable namespaceTable) {
    return new QualifiedName(
        Objects.requireNonNull(
            namespaceTable.getIndex("http://opcfoundation.org/UA/"),
            "missing Method BrowseName namespace"),
        "DisconnectSecurityGroups");
  }

  /** Returns the declared input Arguments resolved in the caller's namespace table. */
  public static Argument[] inputArguments(NamespaceTable namespaceTable) {
    return new Argument[] {
      new Argument(
          "SecurityGroupIds",
          ExpandedNodeId.of(Namespaces.OPC_UA, 17L)
              .toNodeId(namespaceTable)
              .orElseThrow(() -> new IllegalArgumentException("missing namespace for i=17")),
          1,
          new UInteger[] {UInteger.valueOf(0L)},
          new LocalizedText(null, ""))
    };
  }

  /** Returns the declared output Arguments resolved in the caller's namespace table. */
  public static Argument[] outputArguments(NamespaceTable namespaceTable) {
    return new Argument[] {
      new Argument(
          "DisconnectResults",
          ExpandedNodeId.of(Namespaces.OPC_UA, 19L)
              .toNodeId(namespaceTable)
              .orElseThrow(() -> new IllegalArgumentException("missing namespace for i=19")),
          1,
          new UInteger[] {UInteger.valueOf(0L)},
          new LocalizedText(null, ""))
    };
  }

  /** Inputs values in declared order. */
  public static record Inputs(NodeId @Nullable [] securityGroupIds) {
    /**
     * Encodes every declared position.
     *
     * @throws UaException if a value violates its declaration or cannot be encoded.
     */
    public Variant[] toVariants(EncodingContext context) throws UaException {
      return new Variant[] {
        MethodValues.encode(context, securityGroupIds, 0, NodeId.class, 1, new long[] {0L}, null)
      };
    }

    /**
     * Decodes the declared positions.
     *
     * @throws UaException if the count, type, rank, dimensions or encoding is invalid.
     */
    public static Inputs fromVariants(EncodingContext context, Variant[] values)
        throws UaException {
      MethodValues.count(values, 1, 1);
      return new Inputs(
          (NodeId[])
              MethodValues.decode(context, values, 0, NodeId.class, 1, new long[] {0L}, null));
    }

    @Override
    public boolean equals(@Nullable Object other) {
      return other instanceof Inputs value
          && Arrays.deepEquals(
              new Object[] {securityGroupIds}, new Object[] {value.securityGroupIds});
    }

    @Override
    public int hashCode() {
      return Arrays.deepHashCode(new Object[] {securityGroupIds});
    }

    @Override
    public String toString() {
      return "Inputs" + Arrays.deepToString(new Object[] {securityGroupIds});
    }
  }

  /** Outputs values in declared order. */
  public static record Outputs(StatusCode @Nullable [] disconnectResults) {
    /**
     * Encodes every declared position.
     *
     * @throws UaException if a value violates its declaration or cannot be encoded.
     */
    public Variant[] toVariants(EncodingContext context) throws UaException {
      return new Variant[] {
        MethodValues.encode(
            context, disconnectResults, 0, StatusCode.class, 1, new long[] {0L}, null)
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
          (StatusCode[])
              MethodValues.decode(context, values, 0, StatusCode.class, 1, new long[] {0L}, null));
    }

    @Override
    public boolean equals(@Nullable Object other) {
      return other instanceof Outputs value
          && Arrays.deepEquals(
              new Object[] {disconnectResults}, new Object[] {value.disconnectResults});
    }

    @Override
    public int hashCode() {
      return Arrays.deepHashCode(new Object[] {disconnectResults});
    }

    @Override
    public String toString() {
      return "Outputs" + Arrays.deepToString(new Object[] {disconnectResults});
    }
  }
}
