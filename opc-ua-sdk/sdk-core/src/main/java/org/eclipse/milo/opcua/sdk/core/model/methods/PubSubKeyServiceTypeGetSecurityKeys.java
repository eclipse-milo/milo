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
 * Declared arguments and typed wire conversion for the GetSecurityKeys Method of
 * PubSubKeyServiceType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/8.3.2">Model
 *     documentation</a>
 */
public final class PubSubKeyServiceTypeGetSecurityKeys {
  private PubSubKeyServiceTypeGetSecurityKeys() {}

  /** Returns the BrowseName resolved in the caller's namespace table. */
  public static QualifiedName browseName(NamespaceTable namespaceTable) {
    return new QualifiedName(
        Objects.requireNonNull(
            namespaceTable.getIndex("http://opcfoundation.org/UA/"),
            "missing Method BrowseName namespace"),
        "GetSecurityKeys");
  }

  /** Returns the declared input Arguments resolved in the caller's namespace table. */
  public static Argument[] inputArguments(NamespaceTable namespaceTable) {
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
          "StartingTokenId",
          ExpandedNodeId.of(Namespaces.OPC_UA, 288L)
              .toNodeId(namespaceTable)
              .orElseThrow(() -> new IllegalArgumentException("missing namespace for i=288")),
          -1,
          new UInteger[] {},
          new LocalizedText(null, "")),
      new Argument(
          "RequestedKeyCount",
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
          "SecurityPolicyUri",
          ExpandedNodeId.of(Namespaces.OPC_UA, 12L)
              .toNodeId(namespaceTable)
              .orElseThrow(() -> new IllegalArgumentException("missing namespace for i=12")),
          -1,
          new UInteger[] {},
          new LocalizedText(null, "")),
      new Argument(
          "FirstTokenId",
          ExpandedNodeId.of(Namespaces.OPC_UA, 288L)
              .toNodeId(namespaceTable)
              .orElseThrow(() -> new IllegalArgumentException("missing namespace for i=288")),
          -1,
          new UInteger[] {},
          new LocalizedText(null, "")),
      new Argument(
          "Keys",
          ExpandedNodeId.of(Namespaces.OPC_UA, 15L)
              .toNodeId(namespaceTable)
              .orElseThrow(() -> new IllegalArgumentException("missing namespace for i=15")),
          1,
          new UInteger[] {UInteger.valueOf(0L)},
          new LocalizedText(null, "")),
      new Argument(
          "TimeToNextKey",
          ExpandedNodeId.of(Namespaces.OPC_UA, 290L)
              .toNodeId(namespaceTable)
              .orElseThrow(() -> new IllegalArgumentException("missing namespace for i=290")),
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
          new LocalizedText(null, ""))
    };
  }

  /** Inputs values in declared order. */
  public static record Inputs(
      @Nullable String securityGroupId,
      @Nullable UInteger startingTokenId,
      @Nullable UInteger requestedKeyCount) {
    /**
     * Encodes every declared position.
     *
     * @throws UaException if a value violates its declaration or cannot be encoded.
     */
    public Variant[] toVariants(EncodingContext context) throws UaException {
      return new Variant[] {
        MethodValues.encode(context, securityGroupId, 0, String.class, -1, new long[] {}, null),
        MethodValues.encode(context, startingTokenId, 1, UInteger.class, -1, new long[] {}, null),
        MethodValues.encode(context, requestedKeyCount, 2, UInteger.class, -1, new long[] {}, null)
      };
    }

    /**
     * Decodes the declared positions.
     *
     * @throws UaException if the count, type, rank, dimensions or encoding is invalid.
     */
    public static Inputs fromVariants(EncodingContext context, Variant[] values)
        throws UaException {
      MethodValues.count(values, 3, 3);
      return new Inputs(
          (String) MethodValues.decode(context, values, 0, String.class, -1, new long[] {}, null),
          (UInteger)
              MethodValues.decode(context, values, 1, UInteger.class, -1, new long[] {}, null),
          (UInteger)
              MethodValues.decode(context, values, 2, UInteger.class, -1, new long[] {}, null));
    }
  }

  /** Outputs values in declared order. */
  public static record Outputs(
      @Nullable String securityPolicyUri,
      @Nullable UInteger firstTokenId,
      ByteString @Nullable [] keys,
      @Nullable Double timeToNextKey,
      @Nullable Double keyLifetime) {
    /**
     * Encodes every declared position.
     *
     * @throws UaException if a value violates its declaration or cannot be encoded.
     */
    public Variant[] toVariants(EncodingContext context) throws UaException {
      return new Variant[] {
        MethodValues.encode(context, securityPolicyUri, 0, String.class, -1, new long[] {}, null),
        MethodValues.encode(context, firstTokenId, 1, UInteger.class, -1, new long[] {}, null),
        MethodValues.encode(context, keys, 2, ByteString.class, 1, new long[] {0L}, null),
        MethodValues.encode(context, timeToNextKey, 3, Double.class, -1, new long[] {}, null),
        MethodValues.encode(context, keyLifetime, 4, Double.class, -1, new long[] {}, null)
      };
    }

    /**
     * Decodes the declared positions.
     *
     * @throws UaException if the count, type, rank, dimensions or encoding is invalid.
     */
    public static Outputs fromVariants(EncodingContext context, Variant[] values)
        throws UaException {
      MethodValues.count(values, 5, 5);
      return new Outputs(
          (String) MethodValues.decode(context, values, 0, String.class, -1, new long[] {}, null),
          (UInteger)
              MethodValues.decode(context, values, 1, UInteger.class, -1, new long[] {}, null),
          (ByteString[])
              MethodValues.decode(context, values, 2, ByteString.class, 1, new long[] {0L}, null),
          (Double) MethodValues.decode(context, values, 3, Double.class, -1, new long[] {}, null),
          (Double) MethodValues.decode(context, values, 4, Double.class, -1, new long[] {}, null));
    }

    @Override
    public boolean equals(@Nullable Object other) {
      return other instanceof Outputs value
          && Arrays.deepEquals(
              new Object[] {securityPolicyUri, firstTokenId, keys, timeToNextKey, keyLifetime},
              new Object[] {
                value.securityPolicyUri,
                value.firstTokenId,
                value.keys,
                value.timeToNextKey,
                value.keyLifetime
              });
    }

    @Override
    public int hashCode() {
      return Arrays.deepHashCode(
          new Object[] {securityPolicyUri, firstTokenId, keys, timeToNextKey, keyLifetime});
    }

    @Override
    public String toString() {
      return "Outputs"
          + Arrays.deepToString(
              new Object[] {securityPolicyUri, firstTokenId, keys, timeToNextKey, keyLifetime});
    }
  }
}
