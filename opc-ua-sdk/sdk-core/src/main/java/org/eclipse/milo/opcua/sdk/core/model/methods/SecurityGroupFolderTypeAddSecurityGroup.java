package org.eclipse.milo.opcua.sdk.core.model.methods;

import java.util.Objects;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.Argument;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Declared arguments and typed wire conversion for the AddSecurityGroup Method of
 * SecurityGroupFolderType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/8.5.2">Model
 *     documentation</a>
 */
public final class SecurityGroupFolderTypeAddSecurityGroup {
  private SecurityGroupFolderTypeAddSecurityGroup() {}

  /** Returns the BrowseName resolved in the caller's namespace table. */
  public static QualifiedName browseName(NamespaceTable namespaceTable) {
    return new QualifiedName(
        Objects.requireNonNull(
            namespaceTable.getIndex("http://opcfoundation.org/UA/"),
            "missing Method BrowseName namespace"),
        "AddSecurityGroup");
  }

  /** Returns the declared input Arguments resolved in the caller's namespace table. */
  public static Argument[] inputArguments(NamespaceTable namespaceTable) {
    return new Argument[] {
      new Argument(
          "SecurityGroupName",
          ExpandedNodeId.of(Namespaces.OPC_UA, 12L)
              .toNodeId(namespaceTable)
              .orElseThrow(() -> new IllegalArgumentException("missing namespace for i=12")),
          -1,
          new UInteger[] {},
          new LocalizedText(null, "")),
      new Argument(
          "KeyLifetime",
          ExpandedNodeId.of(Namespaces.OPC_UA, 290L)
              .toNodeId(namespaceTable)
              .orElseThrow(() -> new IllegalArgumentException("missing namespace for i=290")),
          -1,
          new UInteger[] {},
          new LocalizedText(null, "")),
      new Argument(
          "SecurityPolicyUri",
          ExpandedNodeId.of(Namespaces.OPC_UA, 12L)
              .toNodeId(namespaceTable)
              .orElseThrow(() -> new IllegalArgumentException("missing namespace for i=12")),
          -1,
          new UInteger[] {},
          new LocalizedText(null, "")),
      new Argument(
          "MaxFutureKeyCount",
          ExpandedNodeId.of(Namespaces.OPC_UA, 7L)
              .toNodeId(namespaceTable)
              .orElseThrow(() -> new IllegalArgumentException("missing namespace for i=7")),
          -1,
          new UInteger[] {},
          new LocalizedText(null, "")),
      new Argument(
          "MaxPastKeyCount",
          ExpandedNodeId.of(Namespaces.OPC_UA, 7L)
              .toNodeId(namespaceTable)
              .orElseThrow(() -> new IllegalArgumentException("missing namespace for i=7")),
          -1,
          new UInteger[] {},
          new LocalizedText(null, ""))
    };
  }

  /** Returns the declared output Arguments resolved in the caller's namespace table. */
  public static Argument[] outputArguments(NamespaceTable namespaceTable) {
    return new Argument[] {
      new Argument(
          "SecurityGroupId",
          ExpandedNodeId.of(Namespaces.OPC_UA, 12L)
              .toNodeId(namespaceTable)
              .orElseThrow(() -> new IllegalArgumentException("missing namespace for i=12")),
          -1,
          new UInteger[] {},
          new LocalizedText(null, "")),
      new Argument(
          "SecurityGroupNodeId",
          ExpandedNodeId.of(Namespaces.OPC_UA, 17L)
              .toNodeId(namespaceTable)
              .orElseThrow(() -> new IllegalArgumentException("missing namespace for i=17")),
          -1,
          new UInteger[] {},
          new LocalizedText(null, ""))
    };
  }

  /** Inputs values in declared order. */
  public static record Inputs(
      @Nullable String securityGroupName,
      @Nullable Double keyLifetime,
      @Nullable String securityPolicyUri,
      @Nullable UInteger maxFutureKeyCount,
      @Nullable UInteger maxPastKeyCount) {
    /**
     * Encodes every declared position.
     *
     * @throws UaException if a value violates its declaration or cannot be encoded.
     */
    public Variant[] toVariants(EncodingContext context) throws UaException {
      return new Variant[] {
        MethodValues.encode(context, securityGroupName, 0, String.class, -1, new long[] {}, null),
        MethodValues.encode(context, keyLifetime, 1, Double.class, -1, new long[] {}, null),
        MethodValues.encode(context, securityPolicyUri, 2, String.class, -1, new long[] {}, null),
        MethodValues.encode(context, maxFutureKeyCount, 3, UInteger.class, -1, new long[] {}, null),
        MethodValues.encode(context, maxPastKeyCount, 4, UInteger.class, -1, new long[] {}, null)
      };
    }

    /**
     * Decodes the declared positions.
     *
     * @throws UaException if the count, type, rank, dimensions or encoding is invalid.
     */
    public static Inputs fromVariants(EncodingContext context, Variant[] values)
        throws UaException {
      MethodValues.count(values, 5, 5);
      return new Inputs(
          (String) MethodValues.decode(context, values, 0, String.class, -1, new long[] {}, null),
          (Double) MethodValues.decode(context, values, 1, Double.class, -1, new long[] {}, null),
          (String) MethodValues.decode(context, values, 2, String.class, -1, new long[] {}, null),
          (UInteger)
              MethodValues.decode(context, values, 3, UInteger.class, -1, new long[] {}, null),
          (UInteger)
              MethodValues.decode(context, values, 4, UInteger.class, -1, new long[] {}, null));
    }
  }

  /** Outputs values in declared order. */
  public static record Outputs(
      @Nullable String securityGroupId, @Nullable NodeId securityGroupNodeId) {
    /**
     * Encodes every declared position.
     *
     * @throws UaException if a value violates its declaration or cannot be encoded.
     */
    public Variant[] toVariants(EncodingContext context) throws UaException {
      return new Variant[] {
        MethodValues.encode(context, securityGroupId, 0, String.class, -1, new long[] {}, null),
        MethodValues.encode(context, securityGroupNodeId, 1, NodeId.class, -1, new long[] {}, null)
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
          (String) MethodValues.decode(context, values, 0, String.class, -1, new long[] {}, null),
          (NodeId) MethodValues.decode(context, values, 1, NodeId.class, -1, new long[] {}, null));
    }
  }
}
