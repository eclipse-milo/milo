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
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.Argument;
import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Declared arguments and typed wire conversion for the AddPushTarget Method of
 * PubSubKeyPushTargetFolderType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/8.7.2">Model
 *     documentation</a>
 */
public final class PubSubKeyPushTargetFolderTypeAddPushTarget {
  private PubSubKeyPushTargetFolderTypeAddPushTarget() {}

  /** Returns the BrowseName resolved in the caller's namespace table. */
  public static QualifiedName browseName(NamespaceTable namespaceTable) {
    return new QualifiedName(
        Objects.requireNonNull(
            namespaceTable.getIndex("http://opcfoundation.org/UA/"),
            "missing Method BrowseName namespace"),
        "AddPushTarget");
  }

  /** Returns the declared input Arguments resolved in the caller's namespace table. */
  public static Argument[] inputArguments(NamespaceTable namespaceTable) {
    return new Argument[] {
      new Argument(
          "ApplicationUri",
          ExpandedNodeId.of(Namespaces.OPC_UA, 12L)
              .toNodeId(namespaceTable)
              .orElseThrow(() -> new IllegalArgumentException("missing namespace for i=12")),
          -1,
          new UInteger[] {},
          new LocalizedText(null, "")),
      new Argument(
          "EndpointUrl",
          ExpandedNodeId.of(Namespaces.OPC_UA, 12L)
              .toNodeId(namespaceTable)
              .orElseThrow(() -> new IllegalArgumentException("missing namespace for i=12")),
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
          "UserTokenType",
          ExpandedNodeId.of(Namespaces.OPC_UA, 304L)
              .toNodeId(namespaceTable)
              .orElseThrow(() -> new IllegalArgumentException("missing namespace for i=304")),
          -1,
          new UInteger[] {},
          new LocalizedText(null, "")),
      new Argument(
          "RequestedKeyCount",
          ExpandedNodeId.of(Namespaces.OPC_UA, 5L)
              .toNodeId(namespaceTable)
              .orElseThrow(() -> new IllegalArgumentException("missing namespace for i=5")),
          -1,
          new UInteger[] {},
          new LocalizedText(null, "")),
      new Argument(
          "RetryInterval",
          ExpandedNodeId.of(Namespaces.OPC_UA, 290L)
              .toNodeId(namespaceTable)
              .orElseThrow(() -> new IllegalArgumentException("missing namespace for i=290")),
          -1,
          new UInteger[] {},
          new LocalizedText(null, ""))
    };
  }

  /** Returns the declared output Arguments resolved in the caller's namespace table. */
  public static Argument[] outputArguments(NamespaceTable namespaceTable) {
    return new Argument[] {
      new Argument(
          "PushTargetId",
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
      @Nullable String applicationUri,
      @Nullable String endpointUrl,
      @Nullable String securityPolicyUri,
      @Nullable UserTokenPolicy userTokenType,
      @Nullable UShort requestedKeyCount,
      @Nullable Double retryInterval) {
    /**
     * Encodes every declared position.
     *
     * @throws UaException if a value violates its declaration or cannot be encoded.
     */
    public Variant[] toVariants(EncodingContext context) throws UaException {
      return new Variant[] {
        MethodValues.encode(context, applicationUri, 0, String.class, -1, new long[] {}, null),
        MethodValues.encode(context, endpointUrl, 1, String.class, -1, new long[] {}, null),
        MethodValues.encode(context, securityPolicyUri, 2, String.class, -1, new long[] {}, null),
        MethodValues.encode(
            context, userTokenType, 3, UserTokenPolicy.class, -1, new long[] {}, null),
        MethodValues.encode(context, requestedKeyCount, 4, UShort.class, -1, new long[] {}, null),
        MethodValues.encode(context, retryInterval, 5, Double.class, -1, new long[] {}, null)
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
          (String) MethodValues.decode(context, values, 0, String.class, -1, new long[] {}, null),
          (String) MethodValues.decode(context, values, 1, String.class, -1, new long[] {}, null),
          (String) MethodValues.decode(context, values, 2, String.class, -1, new long[] {}, null),
          (UserTokenPolicy)
              MethodValues.decode(
                  context, values, 3, UserTokenPolicy.class, -1, new long[] {}, null),
          (UShort) MethodValues.decode(context, values, 4, UShort.class, -1, new long[] {}, null),
          (Double) MethodValues.decode(context, values, 5, Double.class, -1, new long[] {}, null));
    }
  }

  /** Outputs values in declared order. */
  public static record Outputs(@Nullable NodeId pushTargetId) {
    /**
     * Encodes every declared position.
     *
     * @throws UaException if a value violates its declaration or cannot be encoded.
     */
    public Variant[] toVariants(EncodingContext context) throws UaException {
      return new Variant[] {
        MethodValues.encode(context, pushTargetId, 0, NodeId.class, -1, new long[] {}, null)
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
          (NodeId) MethodValues.decode(context, values, 0, NodeId.class, -1, new long[] {}, null));
    }
  }
}
