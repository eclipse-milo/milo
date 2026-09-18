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
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.Argument;
import org.eclipse.milo.opcua.stack.core.types.structured.ConfigurationVersionDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.ContentFilter;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetFieldFlags;
import org.eclipse.milo.opcua.stack.core.types.structured.SimpleAttributeOperand;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Declared arguments and typed wire conversion for the AddPublishedEvents Method of
 * DataSetFolderType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.5.3">Model
 *     documentation</a>
 */
public final class DataSetFolderTypeAddPublishedEvents {
  private DataSetFolderTypeAddPublishedEvents() {}

  /** Returns the BrowseName resolved in the caller's namespace table. */
  public static QualifiedName browseName(NamespaceTable namespaceTable) {
    return new QualifiedName(
        Objects.requireNonNull(
            namespaceTable.getIndex("http://opcfoundation.org/UA/"),
            "missing Method BrowseName namespace"),
        "AddPublishedEvents");
  }

  /** Returns the declared input Arguments resolved in the caller's namespace table. */
  public static Argument[] inputArguments(NamespaceTable namespaceTable) {
    return new Argument[] {
      new Argument(
          "Name",
          ExpandedNodeId.of(Namespaces.OPC_UA, 12L)
              .toNodeId(namespaceTable)
              .orElseThrow(() -> new IllegalArgumentException("missing namespace for i=12")),
          -1,
          new UInteger[] {},
          new LocalizedText(null, "")),
      new Argument(
          "EventNotifier",
          ExpandedNodeId.of(Namespaces.OPC_UA, 17L)
              .toNodeId(namespaceTable)
              .orElseThrow(() -> new IllegalArgumentException("missing namespace for i=17")),
          -1,
          new UInteger[] {},
          new LocalizedText(null, "")),
      new Argument(
          "FieldNameAliases",
          ExpandedNodeId.of(Namespaces.OPC_UA, 12L)
              .toNodeId(namespaceTable)
              .orElseThrow(() -> new IllegalArgumentException("missing namespace for i=12")),
          1,
          new UInteger[] {UInteger.valueOf(0L)},
          new LocalizedText(null, "")),
      new Argument(
          "FieldFlags",
          ExpandedNodeId.of(Namespaces.OPC_UA, 15904L)
              .toNodeId(namespaceTable)
              .orElseThrow(() -> new IllegalArgumentException("missing namespace for i=15904")),
          1,
          new UInteger[] {UInteger.valueOf(0L)},
          new LocalizedText(null, "")),
      new Argument(
          "SelectedFields",
          ExpandedNodeId.of(Namespaces.OPC_UA, 601L)
              .toNodeId(namespaceTable)
              .orElseThrow(() -> new IllegalArgumentException("missing namespace for i=601")),
          1,
          new UInteger[] {UInteger.valueOf(0L)},
          new LocalizedText(null, "")),
      new Argument(
          "Filter",
          ExpandedNodeId.of(Namespaces.OPC_UA, 586L)
              .toNodeId(namespaceTable)
              .orElseThrow(() -> new IllegalArgumentException("missing namespace for i=586")),
          -1,
          new UInteger[] {},
          new LocalizedText(null, ""))
    };
  }

  /** Returns the declared output Arguments resolved in the caller's namespace table. */
  public static Argument[] outputArguments(NamespaceTable namespaceTable) {
    return new Argument[] {
      new Argument(
          "ConfigurationVersion",
          ExpandedNodeId.of(Namespaces.OPC_UA, 14593L)
              .toNodeId(namespaceTable)
              .orElseThrow(() -> new IllegalArgumentException("missing namespace for i=14593")),
          -1,
          new UInteger[] {},
          new LocalizedText(null, "")),
      new Argument(
          "DataSetNodeId",
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
      @Nullable String name,
      @Nullable NodeId eventNotifier,
      @Nullable String @Nullable [] fieldNameAliases,
      DataSetFieldFlags @Nullable [] fieldFlags,
      @Nullable SimpleAttributeOperand @Nullable [] selectedFields,
      @Nullable ContentFilter filter) {
    /**
     * Encodes every declared position.
     *
     * @throws UaException if a value violates its declaration or cannot be encoded.
     */
    public Variant[] toVariants(EncodingContext context) throws UaException {
      return new Variant[] {
        MethodValues.encode(context, name, 0, String.class, -1, new long[] {}, null),
        MethodValues.encode(context, eventNotifier, 1, NodeId.class, -1, new long[] {}, null),
        MethodValues.encode(context, fieldNameAliases, 2, String.class, 1, new long[] {0L}, null),
        MethodValues.encode(
            context, fieldFlags, 3, DataSetFieldFlags.class, 1, new long[] {0L}, null),
        MethodValues.encode(
            context, selectedFields, 4, SimpleAttributeOperand.class, 1, new long[] {0L}, null),
        MethodValues.encode(context, filter, 5, ContentFilter.class, -1, new long[] {}, null)
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
          (NodeId) MethodValues.decode(context, values, 1, NodeId.class, -1, new long[] {}, null),
          (@Nullable String[])
              MethodValues.decode(context, values, 2, String.class, 1, new long[] {0L}, null),
          (DataSetFieldFlags[])
              MethodValues.decode(
                  context, values, 3, DataSetFieldFlags.class, 1, new long[] {0L}, null),
          (@Nullable SimpleAttributeOperand[])
              MethodValues.decode(
                  context, values, 4, SimpleAttributeOperand.class, 1, new long[] {0L}, null),
          (ContentFilter)
              MethodValues.decode(
                  context, values, 5, ContentFilter.class, -1, new long[] {}, null));
    }

    @Override
    public boolean equals(@Nullable Object other) {
      return other instanceof Inputs value
          && Arrays.deepEquals(
              new Object[] {
                name, eventNotifier, fieldNameAliases, fieldFlags, selectedFields, filter
              },
              new Object[] {
                value.name,
                value.eventNotifier,
                value.fieldNameAliases,
                value.fieldFlags,
                value.selectedFields,
                value.filter
              });
    }

    @Override
    public int hashCode() {
      return Arrays.deepHashCode(
          new Object[] {name, eventNotifier, fieldNameAliases, fieldFlags, selectedFields, filter});
    }

    @Override
    public String toString() {
      return "Inputs"
          + Arrays.deepToString(
              new Object[] {
                name, eventNotifier, fieldNameAliases, fieldFlags, selectedFields, filter
              });
    }
  }

  /** Outputs values in declared order. */
  public static record Outputs(
      @Nullable ConfigurationVersionDataType configurationVersion, @Nullable NodeId dataSetNodeId) {
    /**
     * Encodes every declared position.
     *
     * @throws UaException if a value violates its declaration or cannot be encoded.
     */
    public Variant[] toVariants(EncodingContext context) throws UaException {
      return new Variant[] {
        MethodValues.encode(
            context,
            configurationVersion,
            0,
            ConfigurationVersionDataType.class,
            -1,
            new long[] {},
            null),
        MethodValues.encode(context, dataSetNodeId, 1, NodeId.class, -1, new long[] {}, null)
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
          (ConfigurationVersionDataType)
              MethodValues.decode(
                  context, values, 0, ConfigurationVersionDataType.class, -1, new long[] {}, null),
          (NodeId) MethodValues.decode(context, values, 1, NodeId.class, -1, new long[] {}, null));
    }
  }
}
