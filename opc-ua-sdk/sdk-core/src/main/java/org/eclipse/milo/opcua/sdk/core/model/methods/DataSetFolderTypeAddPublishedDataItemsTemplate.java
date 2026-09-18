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
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetMetaDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.PublishedVariableDataType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Declared arguments and typed wire conversion for the AddPublishedDataItemsTemplate Method of
 * DataSetFolderType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.5.4">Model
 *     documentation</a>
 */
public final class DataSetFolderTypeAddPublishedDataItemsTemplate {
  private DataSetFolderTypeAddPublishedDataItemsTemplate() {}

  /** Returns the BrowseName resolved in the caller's namespace table. */
  public static QualifiedName browseName(NamespaceTable namespaceTable) {
    return new QualifiedName(
        Objects.requireNonNull(
            namespaceTable.getIndex("http://opcfoundation.org/UA/"),
            "missing Method BrowseName namespace"),
        "AddPublishedDataItemsTemplate");
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
          "DataSetMetaData",
          ExpandedNodeId.of(Namespaces.OPC_UA, 14523L)
              .toNodeId(namespaceTable)
              .orElseThrow(() -> new IllegalArgumentException("missing namespace for i=14523")),
          -1,
          new UInteger[] {},
          new LocalizedText(null, "")),
      new Argument(
          "VariablesToAdd",
          ExpandedNodeId.of(Namespaces.OPC_UA, 14273L)
              .toNodeId(namespaceTable)
              .orElseThrow(() -> new IllegalArgumentException("missing namespace for i=14273")),
          1,
          new UInteger[] {UInteger.valueOf(0L)},
          new LocalizedText(null, ""))
    };
  }

  /** Returns the declared output Arguments resolved in the caller's namespace table. */
  public static Argument[] outputArguments(NamespaceTable namespaceTable) {
    return new Argument[] {
      new Argument(
          "DataSetNodeId",
          ExpandedNodeId.of(Namespaces.OPC_UA, 17L)
              .toNodeId(namespaceTable)
              .orElseThrow(() -> new IllegalArgumentException("missing namespace for i=17")),
          -1,
          new UInteger[] {},
          new LocalizedText(null, "")),
      new Argument(
          "AddResults",
          ExpandedNodeId.of(Namespaces.OPC_UA, 19L)
              .toNodeId(namespaceTable)
              .orElseThrow(() -> new IllegalArgumentException("missing namespace for i=19")),
          1,
          new UInteger[] {UInteger.valueOf(0L)},
          new LocalizedText(null, ""))
    };
  }

  /** Inputs values in declared order. */
  public static record Inputs(
      @Nullable String name,
      @Nullable DataSetMetaDataType dataSetMetaData,
      @Nullable PublishedVariableDataType @Nullable [] variablesToAdd) {
    /**
     * Encodes every declared position.
     *
     * @throws UaException if a value violates its declaration or cannot be encoded.
     */
    public Variant[] toVariants(EncodingContext context) throws UaException {
      return new Variant[] {
        MethodValues.encode(context, name, 0, String.class, -1, new long[] {}, null),
        MethodValues.encode(
            context, dataSetMetaData, 1, DataSetMetaDataType.class, -1, new long[] {}, null),
        MethodValues.encode(
            context, variablesToAdd, 2, PublishedVariableDataType.class, 1, new long[] {0L}, null)
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
          (DataSetMetaDataType)
              MethodValues.decode(
                  context, values, 1, DataSetMetaDataType.class, -1, new long[] {}, null),
          (@Nullable PublishedVariableDataType[])
              MethodValues.decode(
                  context, values, 2, PublishedVariableDataType.class, 1, new long[] {0L}, null));
    }

    @Override
    public boolean equals(@Nullable Object other) {
      return other instanceof Inputs value
          && Arrays.deepEquals(
              new Object[] {name, dataSetMetaData, variablesToAdd},
              new Object[] {value.name, value.dataSetMetaData, value.variablesToAdd});
    }

    @Override
    public int hashCode() {
      return Arrays.deepHashCode(new Object[] {name, dataSetMetaData, variablesToAdd});
    }

    @Override
    public String toString() {
      return "Inputs" + Arrays.deepToString(new Object[] {name, dataSetMetaData, variablesToAdd});
    }
  }

  /** Outputs values in declared order. */
  public static record Outputs(@Nullable NodeId dataSetNodeId, StatusCode @Nullable [] addResults) {
    /**
     * Encodes every declared position.
     *
     * @throws UaException if a value violates its declaration or cannot be encoded.
     */
    public Variant[] toVariants(EncodingContext context) throws UaException {
      return new Variant[] {
        MethodValues.encode(context, dataSetNodeId, 0, NodeId.class, -1, new long[] {}, null),
        MethodValues.encode(context, addResults, 1, StatusCode.class, 1, new long[] {0L}, null)
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
          (NodeId) MethodValues.decode(context, values, 0, NodeId.class, -1, new long[] {}, null),
          (StatusCode[])
              MethodValues.decode(context, values, 1, StatusCode.class, 1, new long[] {0L}, null));
    }

    @Override
    public boolean equals(@Nullable Object other) {
      return other instanceof Outputs value
          && Arrays.deepEquals(
              new Object[] {dataSetNodeId, addResults},
              new Object[] {value.dataSetNodeId, value.addResults});
    }

    @Override
    public int hashCode() {
      return Arrays.deepHashCode(new Object[] {dataSetNodeId, addResults});
    }

    @Override
    public String toString() {
      return "Outputs" + Arrays.deepToString(new Object[] {dataSetNodeId, addResults});
    }
  }
}
