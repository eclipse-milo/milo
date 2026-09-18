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
 * Declared arguments and typed wire conversion for the SetSecurityKeys Method of
 * PublishSubscribeType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.3/#9.1.3.3">Model
 *     documentation</a>
 */
public final class PublishSubscribeTypeSetSecurityKeys {
  private PublishSubscribeTypeSetSecurityKeys() {}

  /** Returns the BrowseName resolved in the caller's namespace table. */
  public static QualifiedName browseName(NamespaceTable namespaceTable) {
    return new QualifiedName(
        Objects.requireNonNull(
            namespaceTable.getIndex("http://opcfoundation.org/UA/"),
            "missing Method BrowseName namespace"),
        "SetSecurityKeys");
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
          "SecurityPolicyUri",
          ExpandedNodeId.of(Namespaces.OPC_UA, 12L)
              .toNodeId(namespaceTable)
              .orElseThrow(() -> new IllegalArgumentException("missing namespace for i=12")),
          -1,
          new UInteger[] {},
          new LocalizedText(null, "")),
      new Argument(
          "CurrentTokenId",
          ExpandedNodeId.of(Namespaces.OPC_UA, 288L)
              .toNodeId(namespaceTable)
              .orElseThrow(() -> new IllegalArgumentException("missing namespace for i=288")),
          -1,
          new UInteger[] {},
          new LocalizedText(null, "")),
      new Argument(
          "CurrentKey",
          ExpandedNodeId.of(Namespaces.OPC_UA, 15L)
              .toNodeId(namespaceTable)
              .orElseThrow(() -> new IllegalArgumentException("missing namespace for i=15")),
          -1,
          new UInteger[] {},
          new LocalizedText(null, "")),
      new Argument(
          "FutureKeys",
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

  /** Returns the declared output Arguments resolved in the caller's namespace table. */
  public static Argument[] outputArguments(NamespaceTable namespaceTable) {
    return new Argument[] {};
  }

  /** Inputs values in declared order. */
  public static record Inputs(
      @Nullable String securityGroupId,
      @Nullable String securityPolicyUri,
      @Nullable UInteger currentTokenId,
      @Nullable ByteString currentKey,
      ByteString @Nullable [] futureKeys,
      @Nullable Double timeToNextKey,
      @Nullable Double keyLifetime) {
    /**
     * Encodes every declared position.
     *
     * @throws UaException if a value violates its declaration or cannot be encoded.
     */
    public Variant[] toVariants(EncodingContext context) throws UaException {
      return new Variant[] {
        MethodValues.encode(context, securityGroupId, 0, String.class, -1, new long[] {}, null),
        MethodValues.encode(context, securityPolicyUri, 1, String.class, -1, new long[] {}, null),
        MethodValues.encode(context, currentTokenId, 2, UInteger.class, -1, new long[] {}, null),
        MethodValues.encode(context, currentKey, 3, ByteString.class, -1, new long[] {}, null),
        MethodValues.encode(context, futureKeys, 4, ByteString.class, 1, new long[] {0L}, null),
        MethodValues.encode(context, timeToNextKey, 5, Double.class, -1, new long[] {}, null),
        MethodValues.encode(context, keyLifetime, 6, Double.class, -1, new long[] {}, null)
      };
    }

    /**
     * Decodes the declared positions.
     *
     * @throws UaException if the count, type, rank, dimensions or encoding is invalid.
     */
    public static Inputs fromVariants(EncodingContext context, Variant[] values)
        throws UaException {
      MethodValues.count(values, 7, 7);
      return new Inputs(
          (String) MethodValues.decode(context, values, 0, String.class, -1, new long[] {}, null),
          (String) MethodValues.decode(context, values, 1, String.class, -1, new long[] {}, null),
          (UInteger)
              MethodValues.decode(context, values, 2, UInteger.class, -1, new long[] {}, null),
          (ByteString)
              MethodValues.decode(context, values, 3, ByteString.class, -1, new long[] {}, null),
          (ByteString[])
              MethodValues.decode(context, values, 4, ByteString.class, 1, new long[] {0L}, null),
          (Double) MethodValues.decode(context, values, 5, Double.class, -1, new long[] {}, null),
          (Double) MethodValues.decode(context, values, 6, Double.class, -1, new long[] {}, null));
    }

    @Override
    public boolean equals(@Nullable Object other) {
      return other instanceof Inputs value
          && Arrays.deepEquals(
              new Object[] {
                securityGroupId,
                securityPolicyUri,
                currentTokenId,
                currentKey,
                futureKeys,
                timeToNextKey,
                keyLifetime
              },
              new Object[] {
                value.securityGroupId,
                value.securityPolicyUri,
                value.currentTokenId,
                value.currentKey,
                value.futureKeys,
                value.timeToNextKey,
                value.keyLifetime
              });
    }

    @Override
    public int hashCode() {
      return Arrays.deepHashCode(
          new Object[] {
            securityGroupId,
            securityPolicyUri,
            currentTokenId,
            currentKey,
            futureKeys,
            timeToNextKey,
            keyLifetime
          });
    }

    @Override
    public String toString() {
      return "Inputs"
          + Arrays.deepToString(
              new Object[] {
                securityGroupId,
                securityPolicyUri,
                currentTokenId,
                currentKey,
                futureKeys,
                timeToNextKey,
                keyLifetime
              });
    }
  }
}
