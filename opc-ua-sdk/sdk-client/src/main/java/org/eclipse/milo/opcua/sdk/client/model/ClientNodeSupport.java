package org.eclipse.milo.opcua.sdk.client.model;

import java.lang.reflect.Array;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.function.IntFunction;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallOptions;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallResult;
import org.eclipse.milo.opcua.sdk.client.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.client.nodes.UaNode;
import org.eclipse.milo.opcua.sdk.client.nodes.UaObjectNode;
import org.eclipse.milo.opcua.sdk.client.nodes.UaVariableNode;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.UaEnumeratedType;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.Matrix;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.OptionSetUInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UNumber;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowsePath;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowsePathResult;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowsePathTarget;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodResult;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId;
import org.eclipse.milo.opcua.stack.core.types.structured.RelativePath;
import org.eclipse.milo.opcua.stack.core.types.structured.RelativePathElement;
import org.eclipse.milo.opcua.stack.core.types.structured.ResponseHeader;
import org.eclipse.milo.opcua.stack.core.types.structured.WriteValue;
import org.jspecify.annotations.Nullable;

/**
 * Implementation shared by this library's generated nodes. URI-qualified lookup remains live while
 * node reuse follows Milo's cache. Typed read and write operations never mutate a node's cached
 * Value. Constructors do not call this support.
 *
 * <p>Applications serialize initialization, retain codecs for operations and subscriptions, and
 * create a new client when namespace assignments change. This support introduces no target cache,
 * retries, remote endpoint traversal or registration ownership handles.
 *
 * <p>Visibility permits calls across configured generated packages. Applications use the generated
 * node APIs instead of this implementation helper.
 */
public final class ClientNodeSupport {
  private ClientNodeSupport() {}

  @FunctionalInterface
  public interface Operation<T> {
    CompletableFuture<T> run() throws Exception;
  }

  public static <T> CompletableFuture<T> defer(Operation<T> operation) {
    try {
      return operation.run();
    } catch (Exception e) {
      return CompletableFuture.failedFuture(checked(e));
    }
  }

  /**
   * Propagates cancellation to the currently active stage and suppresses stages not yet started. A
   * service already sent to the server cannot be recalled.
   */
  public static <T, R> CompletableFuture<R> compose(
      CompletableFuture<T> source, Function<T, CompletableFuture<R>> next) {
    CompletableFuture<R> result = new CompletableFuture<>();
    AtomicReference<CompletableFuture<?>> active = new AtomicReference<>(source);
    result.whenComplete(
        (value, failure) -> {
          if (result.isCancelled()) active.get().cancel(false);
        });
    source.whenComplete(
        (value, failure) -> {
          if (result.isDone()) return;
          if (failure != null) {
            result.completeExceptionally(checked(failure));
            return;
          }
          try {
            CompletableFuture<R> stage = next.apply(value);
            active.set(stage);
            if (result.isCancelled()) stage.cancel(false);
            stage.whenComplete(
                (answer, error) -> {
                  if (error == null) result.complete(answer);
                  else result.completeExceptionally(checked(error));
                });
          } catch (Exception e) {
            result.completeExceptionally(checked(e));
          }
        });
    return result;
  }

  public static <T> T await(CompletableFuture<T> future) throws UaException {
    try {
      return future.get();
    } catch (InterruptedException e) {
      future.cancel(false);
      Thread.currentThread().interrupt();
      throw new UaException(e);
    } catch (ExecutionException | CancellationException e) {
      throw checked(e);
    }
  }

  public static CompletableFuture<MethodCallResult<Variant[]>> call(
      OpcUaClient client,
      UaObjectNode owner,
      @Nullable UaMethodNode method,
      Variant[] inputs,
      MethodCallOptions options) {
    return defer(
        () -> {
          if (method == null)
            throw new UaException(StatusCodes.Bad_NotFound, "Method node is absent");
          return compose(
              client.callAsync(
                  List.of(new CallMethodRequest(owner.getNodeId(), method.getNodeId(), inputs)),
                  options),
              response ->
                  defer(
                      () -> {
                        if (response == null)
                          throw new UaException(
                              StatusCodes.Bad_UnexpectedError, "missing Call response");
                        header(response.getResponseHeader(), "Call");
                        CallMethodResult[] results = response.getResults();
                        if (results == null
                            || results.length != 1
                            || results[0] == null
                            || results[0].getStatusCode() == null)
                          throw new UaException(
                              StatusCodes.Bad_UnexpectedError, "invalid Call result");
                        return CompletableFuture.completedFuture(
                            MethodCallResult.of(results[0], response.getResponseHeader()));
                      }));
        });
  }

  private static UaException checked(Throwable cause) {
    while ((cause instanceof ExecutionException || cause instanceof CompletionException)
        && cause.getCause() != null) cause = cause.getCause();
    return cause instanceof UaException ua ? ua : new UaException(cause);
  }

  public static void good(@Nullable StatusCode status, String context) throws UaException {
    if (status == null)
      throw new UaException(StatusCodes.Bad_UnexpectedError, context + ": missing status");
    if (!status.isGood()) throw new UaException(status, context);
  }

  private static void header(@Nullable ResponseHeader header, String context) throws UaException {
    if (header == null)
      throw new UaException(StatusCodes.Bad_UnexpectedError, context + ": missing header");
    good(header.getServiceResult(), context);
  }

  public static NodeId resolve(NamespaceTable table, ExpandedNodeId identity) {
    return identity
        .toNodeId(table)
        .orElseThrow(() -> new IllegalArgumentException("namespace absent: " + identity));
  }

  /**
   * Resolves a mandatory child by reference type and qualified BrowseName.
   *
   * @return a future that completes with the child, or fails with Bad_NotFound if it is absent.
   */
  public static <T extends UaNode> CompletableFuture<T> mandatoryChild(
      OpcUaClient client,
      UaNode parent,
      String uri,
      String name,
      ExpandedNodeId reference,
      NodeClass nodeClass,
      Class<T> binding) {
    String context = "lookup " + parent.getNodeId() + " {" + uri + "}" + name;
    return compose(
        lookup(client, parent, uri, name, reference, true, nodeClass, binding, context),
        node ->
            // A mandatory lookup fails instead of completing with null; this only types the result.
            node == null
                ? CompletableFuture.failedFuture(
                    new UaException(StatusCodes.Bad_UnexpectedError, context + ": missing node"))
                : CompletableFuture.completedFuture(node));
  }

  /**
   * Resolves an optional child by reference type and qualified BrowseName.
   *
   * @return a future that completes with the child, or with null if it is absent.
   */
  public static <T extends UaNode> CompletableFuture<@Nullable T> optionalChild(
      OpcUaClient client,
      UaNode parent,
      String uri,
      String name,
      ExpandedNodeId reference,
      NodeClass nodeClass,
      Class<T> binding) {
    String context = "lookup " + parent.getNodeId() + " {" + uri + "}" + name;
    return lookup(client, parent, uri, name, reference, false, nodeClass, binding, context);
  }

  private static <T extends UaNode> CompletableFuture<@Nullable T> lookup(
      OpcUaClient client,
      UaNode parent,
      String uri,
      String name,
      ExpandedNodeId reference,
      boolean mandatory,
      NodeClass nodeClass,
      Class<T> binding,
      String context) {
    return defer(
        () -> {
          CompletableFuture<NamespaceTable> table =
              client.getNamespaceTable().getIndex(uri) == null
                      || reference.toNodeId(client.getNamespaceTable()).isEmpty()
                  ? client.readNamespaceTableAsync()
                  : CompletableFuture.completedFuture(client.getNamespaceTable());
          return compose(
              table,
              namespaces ->
                  defer(
                      () -> {
                        if (namespaces.getIndex(uri) == null
                            || reference.toNodeId(namespaces).isEmpty())
                          throw new UaException(
                              StatusCodes.Bad_NodeIdInvalid, context + ": missing namespace");
                        BrowsePath path =
                            new BrowsePath(
                                parent.getNodeId(),
                                new RelativePath(
                                    new RelativePathElement[] {
                                      new RelativePathElement(
                                          resolve(namespaces, reference),
                                          false,
                                          true,
                                          new QualifiedName(namespaces.getIndex(uri), name))
                                    }));
                        return compose(
                            client.translateBrowsePathsAsync(List.of(path)),
                            response ->
                                defer(
                                    () -> {
                                      if (response == null)
                                        throw new UaException(
                                            StatusCodes.Bad_UnexpectedError,
                                            context + ": missing response");
                                      header(response.getResponseHeader(), context);
                                      NodeId id =
                                          target(
                                              response.getResults(),
                                              namespaces,
                                              mandatory,
                                              context);
                                      if (id == null)
                                        return CompletableFuture.completedFuture(null);
                                      return compose(
                                          client.getAddressSpace().getNodeAsync(id),
                                          node ->
                                              defer(
                                                  () -> {
                                                    if (node == null)
                                                      throw new UaException(
                                                          StatusCodes.Bad_UnexpectedError,
                                                          context + ": missing node");
                                                    if (node.getNodeClass() != nodeClass)
                                                      throw new UaException(
                                                          StatusCodes.Bad_NodeClassInvalid,
                                                          context);
                                                    if (!binding.isInstance(node))
                                                      throw new UaException(
                                                          StatusCodes.Bad_TypeMismatch,
                                                          context
                                                              + ": expected Java binding "
                                                              + binding.getName()
                                                              + ", received "
                                                              + node.getClass().getName());
                                                    return CompletableFuture.completedFuture(
                                                        binding.cast(node));
                                                  }));
                                    }));
                      }));
        });
  }

  /**
   * Validates the whole target set before choosing a node; duplicate equivalent identities
   * coalesce.
   */
  public static @Nullable NodeId target(
      @Nullable BrowsePathResult[] results, NamespaceTable table, boolean mandatory, String context)
      throws UaException {
    if (results == null || results.length != 1 || results[0] == null)
      throw new UaException(
          StatusCodes.Bad_UnexpectedError, context + ": invalid result cardinality");
    BrowsePathResult result = results[0];
    StatusCode status = result.getStatusCode();
    if (status != null && status.getValue() == StatusCodes.Bad_NoMatch) {
      if (!mandatory) return null;
      throw new UaException(StatusCodes.Bad_NotFound, context + ": missing required child");
    }
    good(status, context);
    BrowsePathTarget[] targets = result.getTargets();
    if (targets == null || targets.length == 0)
      throw new UaException(StatusCodes.Bad_UnexpectedError, context + ": missing targets");
    Set<NodeId> identities = new LinkedHashSet<>();
    for (BrowsePathTarget target : targets) {
      if (target == null
          || target.getTargetId() == null
          || target.getRemainingPathIndex() == null
          || target.getRemainingPathIndex().longValue() != 0xffffffffL)
        throw new UaException(
            StatusCodes.Bad_UnexpectedError, context + ": partial or malformed target");
      ExpandedNodeId identity = target.getTargetId();
      if (!identity.isLocal())
        throw new UaException(StatusCodes.Bad_NotSupported, context + ": remote target");
      NodeId local = identity.toNodeId(table).orElse(null);
      if (local == null || local.isNull() || table.get(local.getNamespaceIndex()) == null)
        throw new UaException(StatusCodes.Bad_NodeIdInvalid, context + ": unmappable target");
      identities.add(local);
    }
    if (identities.size() > 1)
      throw new UaException(StatusCodes.Bad_TooManyMatches, context + ": distinct targets");
    return identities.iterator().next();
  }

  public static UaVariableNode required(
      @Nullable UaVariableNode node, UaNode parent, String declaration) throws UaException {
    if (node == null)
      throw new UaException(
          StatusCodes.Bad_NotFound,
          "Value " + parent.getNodeId() + " {" + declaration + ": absent node");
    return node;
  }

  /**
   * Reads one Value remotely, matching the server's absence policy.
   *
   * @param mandatory whether the declaration requires the child; an absent optional child reads as
   *     null instead of failing.
   */
  public static CompletableFuture<@Nullable Object> read(
      OpcUaClient client,
      @Nullable UaVariableNode candidate,
      UaNode parent,
      String declaration,
      boolean mandatory,
      Class<?> type,
      int rank,
      @Nullable IntFunction<?> enumeration) {
    return defer(
        () -> {
          if (candidate == null && !mandatory) return CompletableFuture.completedFuture(null);
          UaVariableNode node = required(candidate, parent, declaration);
          String context = "read " + node.getNodeId() + " {" + declaration;
          return compose(
              client.readAsync(
                  0.0,
                  TimestampsToReturn.Both,
                  List.of(
                      new ReadValueId(
                          node.getNodeId(),
                          AttributeId.Value.uid(),
                          null,
                          QualifiedName.NULL_VALUE))),
              response ->
                  defer(
                      () -> {
                        if (response == null)
                          throw new UaException(
                              StatusCodes.Bad_UnexpectedError, context + ": missing response");
                        header(response.getResponseHeader(), context);
                        DataValue[] values = response.getResults();
                        if (values == null
                            || values.length != 1
                            || values[0] == null
                            || values[0].getValue() == null)
                          throw new UaException(
                              StatusCodes.Bad_UnexpectedError, context + ": invalid results");
                        good(values[0].getStatusCode(), context);
                        return CompletableFuture.completedFuture(
                            convert(
                                client,
                                node,
                                values[0].getValue().getValue(),
                                type,
                                rank,
                                enumeration,
                                false));
                      }));
        });
  }

  public static CompletableFuture<StatusCode> write(
      OpcUaClient client,
      @Nullable UaVariableNode candidate,
      UaNode parent,
      String declaration,
      @Nullable Object value,
      Class<?> type,
      int rank,
      @Nullable IntFunction<?> enumeration) {
    return defer(
        () -> {
          UaVariableNode node = required(candidate, parent, declaration);
          Object encoded = convert(client, node, value, type, rank, enumeration, true);
          String context = "write " + node.getNodeId() + " {" + declaration;
          return compose(
              client.writeAsync(
                  List.of(
                      new WriteValue(
                          node.getNodeId(),
                          AttributeId.Value.uid(),
                          null,
                          DataValue.valueOnly(new Variant(encoded))))),
              response ->
                  defer(
                      () -> {
                        if (response == null)
                          throw new UaException(
                              StatusCodes.Bad_UnexpectedError, context + ": missing response");
                        header(response.getResponseHeader(), context);
                        StatusCode[] results = response.getResults();
                        if (results == null || results.length != 1 || results[0] == null)
                          throw new UaException(
                              StatusCodes.Bad_UnexpectedError, context + ": invalid results");
                        return CompletableFuture.completedFuture(results[0]);
                      }));
        });
  }

  /** Checks rank and every element before mutation or a Write; errors omit value bodies. */
  public static @Nullable Object convert(
      OpcUaClient client,
      UaVariableNode node,
      @Nullable Object value,
      Class<?> type,
      int rank,
      @Nullable IntFunction<?> enumeration,
      boolean encode)
      throws UaException {
    try {
      if (value == null || value instanceof Matrix matrix && matrix.isNull()) return null;
      boolean rawVariant = type == Variant.class && rank != 1;
      Object payload = rawVariant && value instanceof Variant variant ? variant.getValue() : value;
      if (payload == null) return null;
      int actual =
          payload instanceof Matrix matrix
              ? matrix.getValueRank()
              : payload.getClass().isArray() ? 1 : -1;
      Object elements = payload instanceof Matrix matrix ? matrix.getElements() : payload;
      boolean empty = actual == 1 && Array.getLength(elements) == 0;
      boolean structured = UaStructuredType.class.isAssignableFrom(type);
      if (!(actual == rank
          || rank == -2
          || rank == -3 && (actual == -1 || actual == 1)
          || rank == 0 && actual > 0
          || structured && empty && rank > 0))
        throw new IllegalArgumentException("incompatible rank " + actual + ", expected " + rank);
      if (rawVariant) {
        if (encode && !(value instanceof Variant))
          throw new IllegalArgumentException("expected Variant");
        return encode ? payload : value instanceof Variant ? value : new Variant(value);
      }
      if (actual == -1) return scalar(client, payload, type, enumeration, encode);
      Class<?> component = encode ? wireType(type, enumeration != null) : type;
      Object converted = Array.newInstance(component, Array.getLength(elements));
      for (int i = 0; i < Array.getLength(elements); i++) {
        Object element = Array.get(elements, i);
        if (element != null && element.getClass().isArray())
          throw new IllegalArgumentException("nested Java array; use Matrix");
        Object checked = scalar(client, element, type, enumeration, encode);
        if (checked == null && !nullableElement(type))
          throw new IllegalArgumentException("null element for " + type.getName());
        Array.set(converted, i, checked);
      }
      if (payload instanceof Matrix matrix)
        return new Matrix(
            converted,
            matrix.getDimensions(),
            matrix.getDataType().orElse(null),
            matrix.getDataTypeId().orElse(null));
      return converted;
    } catch (Exception e) {
      // UaException extracts an existing codec status; local failures receive Milo's local
      // fallback.
      throw new UaException(
          new IllegalArgumentException(
              "conversion at "
                  + node.getNodeId()
                  + ": expected "
                  + type.getName()
                  + " rank "
                  + rank,
              e));
    }
  }

  private static @Nullable Object scalar(
      OpcUaClient client,
      @Nullable Object value,
      Class<?> type,
      @Nullable IntFunction<?> enumeration,
      boolean encode)
      throws Exception {
    if (value == null) return null;
    if (encode) {
      Object checked = type.cast(value);
      if (checked instanceof UaEnumeratedType e) return e.getValue();
      if (checked instanceof OptionSetUInteger<?> options) return options.getValue();
      if (checked instanceof UaStructuredType structure)
        return ExtensionObject.encode(client.getStaticEncodingContext(), structure);
      return checked;
    }
    Object decoded = value;
    if (value instanceof ExtensionObject extension && type != ExtensionObject.class)
      decoded = extension.isNull() ? null : extension.decode(client.getStaticEncodingContext());
    if (decoded == null) return null;
    if (enumeration != null && decoded instanceof Integer number) {
      Object found = enumeration.apply(number);
      if (found == null) throw new IllegalArgumentException("unknown enum number");
      return type.cast(found);
    }
    if (OptionSetUInteger.class.isAssignableFrom(type)
        && decoded instanceof UNumber
        && !type.isInstance(decoded))
      return type.getConstructor(decoded.getClass()).newInstance(decoded);
    if (type == Variant.class) return decoded instanceof Variant ? decoded : new Variant(decoded);
    return type.cast(decoded);
  }

  private static Class<?> wireType(Class<?> type, boolean enumeration) throws Exception {
    if (enumeration) return Integer.class;
    if (UaStructuredType.class.isAssignableFrom(type)) return ExtensionObject.class;
    if (OptionSetUInteger.class.isAssignableFrom(type))
      return type.getMethod("getValue").getReturnType();
    return type;
  }

  private static boolean nullableElement(Class<?> type) {
    return type == String.class
        || type == Variant.class
        || type == ExtensionObject.class
        || UaStructuredType.class.isAssignableFrom(type);
  }
}
