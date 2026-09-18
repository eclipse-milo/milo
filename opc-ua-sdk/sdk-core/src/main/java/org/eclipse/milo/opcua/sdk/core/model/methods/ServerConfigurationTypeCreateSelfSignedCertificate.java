package org.eclipse.milo.opcua.sdk.core.model.methods;

import java.util.Arrays;
import java.util.Objects;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.Argument;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Declared arguments and typed wire conversion for the CreateSelfSignedCertificate Method of
 * ServerConfigurationType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.6">Model
 *     documentation</a>
 */
public final class ServerConfigurationTypeCreateSelfSignedCertificate {
  private ServerConfigurationTypeCreateSelfSignedCertificate() {}

  /** Returns the BrowseName resolved in the caller's namespace table. */
  public static QualifiedName browseName(NamespaceTable namespaceTable) {
    return new QualifiedName(
        Objects.requireNonNull(
            namespaceTable.getIndex("http://opcfoundation.org/UA/"),
            "missing Method BrowseName namespace"),
        "CreateSelfSignedCertificate");
  }

  /** Returns the declared input Arguments resolved in the caller's namespace table. */
  public static Argument[] inputArguments(NamespaceTable namespaceTable) {
    return new Argument[] {
      new Argument(
          "CertificateGroupId",
          ExpandedNodeId.of(Namespaces.OPC_UA, 17L)
              .toNodeId(namespaceTable)
              .orElseThrow(() -> new IllegalArgumentException("missing namespace for i=17")),
          -1,
          new UInteger[] {},
          new LocalizedText(null, "")),
      new Argument(
          "CertificateTypeId",
          ExpandedNodeId.of(Namespaces.OPC_UA, 17L)
              .toNodeId(namespaceTable)
              .orElseThrow(() -> new IllegalArgumentException("missing namespace for i=17")),
          -1,
          new UInteger[] {},
          new LocalizedText(null, "")),
      new Argument(
          "SubjectName",
          ExpandedNodeId.of(Namespaces.OPC_UA, 12L)
              .toNodeId(namespaceTable)
              .orElseThrow(() -> new IllegalArgumentException("missing namespace for i=12")),
          -1,
          new UInteger[] {},
          new LocalizedText(null, "")),
      new Argument(
          "DnsNames",
          ExpandedNodeId.of(Namespaces.OPC_UA, 12L)
              .toNodeId(namespaceTable)
              .orElseThrow(() -> new IllegalArgumentException("missing namespace for i=12")),
          1,
          new UInteger[] {UInteger.valueOf(0L)},
          new LocalizedText(null, "")),
      new Argument(
          "IpAddresses",
          ExpandedNodeId.of(Namespaces.OPC_UA, 12L)
              .toNodeId(namespaceTable)
              .orElseThrow(() -> new IllegalArgumentException("missing namespace for i=12")),
          1,
          new UInteger[] {UInteger.valueOf(0L)},
          new LocalizedText(null, "")),
      new Argument(
          "LifetimeInDays",
          ExpandedNodeId.of(Namespaces.OPC_UA, 5L)
              .toNodeId(namespaceTable)
              .orElseThrow(() -> new IllegalArgumentException("missing namespace for i=5")),
          -1,
          new UInteger[] {},
          new LocalizedText(null, "")),
      new Argument(
          "KeySizeInBits",
          ExpandedNodeId.of(Namespaces.OPC_UA, 5L)
              .toNodeId(namespaceTable)
              .orElseThrow(() -> new IllegalArgumentException("missing namespace for i=5")),
          -1,
          new UInteger[] {},
          new LocalizedText(null, ""))
    };
  }

  /** Returns the declared output Arguments resolved in the caller's namespace table. */
  public static Argument[] outputArguments(NamespaceTable namespaceTable) {
    return new Argument[] {
      new Argument(
          "Certificate",
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
      @Nullable NodeId certificateGroupId,
      @Nullable NodeId certificateTypeId,
      @Nullable String subjectName,
      @Nullable String @Nullable [] dnsNames,
      @Nullable String @Nullable [] ipAddresses,
      @Nullable UShort lifetimeInDays,
      @Nullable UShort keySizeInBits) {
    /**
     * Encodes every declared position.
     *
     * @throws UaException if a value violates its declaration or cannot be encoded.
     */
    public Variant[] toVariants(EncodingContext context) throws UaException {
      return new Variant[] {
        MethodValues.encode(context, certificateGroupId, 0, NodeId.class, -1, new long[] {}, null),
        MethodValues.encode(context, certificateTypeId, 1, NodeId.class, -1, new long[] {}, null),
        MethodValues.encode(context, subjectName, 2, String.class, -1, new long[] {}, null),
        MethodValues.encode(context, dnsNames, 3, String.class, 1, new long[] {0L}, null),
        MethodValues.encode(context, ipAddresses, 4, String.class, 1, new long[] {0L}, null),
        MethodValues.encode(context, lifetimeInDays, 5, UShort.class, -1, new long[] {}, null),
        MethodValues.encode(context, keySizeInBits, 6, UShort.class, -1, new long[] {}, null)
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
          (NodeId) MethodValues.decode(context, values, 0, NodeId.class, -1, new long[] {}, null),
          (NodeId) MethodValues.decode(context, values, 1, NodeId.class, -1, new long[] {}, null),
          (String) MethodValues.decode(context, values, 2, String.class, -1, new long[] {}, null),
          (@Nullable String[])
              MethodValues.decode(context, values, 3, String.class, 1, new long[] {0L}, null),
          (@Nullable String[])
              MethodValues.decode(context, values, 4, String.class, 1, new long[] {0L}, null),
          (UShort) MethodValues.decode(context, values, 5, UShort.class, -1, new long[] {}, null),
          (UShort) MethodValues.decode(context, values, 6, UShort.class, -1, new long[] {}, null));
    }

    @Override
    public boolean equals(@Nullable Object other) {
      return other instanceof Inputs value
          && Arrays.deepEquals(
              new Object[] {
                certificateGroupId,
                certificateTypeId,
                subjectName,
                dnsNames,
                ipAddresses,
                lifetimeInDays,
                keySizeInBits
              },
              new Object[] {
                value.certificateGroupId,
                value.certificateTypeId,
                value.subjectName,
                value.dnsNames,
                value.ipAddresses,
                value.lifetimeInDays,
                value.keySizeInBits
              });
    }

    @Override
    public int hashCode() {
      return Arrays.deepHashCode(
          new Object[] {
            certificateGroupId,
            certificateTypeId,
            subjectName,
            dnsNames,
            ipAddresses,
            lifetimeInDays,
            keySizeInBits
          });
    }

    @Override
    public String toString() {
      return "Inputs"
          + Arrays.deepToString(
              new Object[] {
                certificateGroupId,
                certificateTypeId,
                subjectName,
                dnsNames,
                ipAddresses,
                lifetimeInDays,
                keySizeInBits
              });
    }
  }

  /** Outputs values in declared order. */
  public static record Outputs(@Nullable ByteString certificate) {
    /**
     * Encodes every declared position.
     *
     * @throws UaException if a value violates its declaration or cannot be encoded.
     */
    public Variant[] toVariants(EncodingContext context) throws UaException {
      return new Variant[] {
        MethodValues.encode(context, certificate, 0, ByteString.class, -1, new long[] {}, null)
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
