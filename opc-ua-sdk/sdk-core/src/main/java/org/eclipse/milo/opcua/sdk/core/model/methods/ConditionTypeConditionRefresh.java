package org.eclipse.milo.opcua.sdk.core.model.methods;

import java.util.Objects;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.Argument;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Declared arguments and typed wire conversion for the ConditionRefresh Method of ConditionType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.5.7">Model
 *     documentation</a>
 */
public final class ConditionTypeConditionRefresh {
  private ConditionTypeConditionRefresh() {}

  /** Returns the BrowseName resolved in the caller's namespace table. */
  public static QualifiedName browseName(NamespaceTable namespaceTable) {
    return new QualifiedName(
        Objects.requireNonNull(
            namespaceTable.getIndex("http://opcfoundation.org/UA/"),
            "missing Method BrowseName namespace"),
        "ConditionRefresh");
  }

  /** Returns the declared input Arguments resolved in the caller's namespace table. */
  public static Argument[] inputArguments(NamespaceTable namespaceTable) {
    return new Argument[] {
      new Argument(
          "SubscriptionId",
          ExpandedNodeId.of(Namespaces.OPC_UA, 288L)
              .toNodeId(namespaceTable)
              .orElseThrow(() -> new IllegalArgumentException("missing namespace for i=288")),
          -1,
          new UInteger[] {},
          new LocalizedText(null, "The identifier for the subscription to refresh."))
    };
  }

  /** Returns the declared output Arguments resolved in the caller's namespace table. */
  public static Argument[] outputArguments(NamespaceTable namespaceTable) {
    return new Argument[] {};
  }

  /**
   * Inputs values in declared order.
   *
   * @param subscriptionId the identifier for the subscription to refresh.
   */
  public static record Inputs(@Nullable UInteger subscriptionId) {
    /**
     * Encodes every declared position.
     *
     * @throws UaException if a value violates its declaration or cannot be encoded.
     */
    public Variant[] toVariants(EncodingContext context) throws UaException {
      return new Variant[] {
        MethodValues.encode(context, subscriptionId, 0, UInteger.class, -1, new long[] {}, null)
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
          (UInteger)
              MethodValues.decode(context, values, 0, UInteger.class, -1, new long[] {}, null));
    }
  }
}
