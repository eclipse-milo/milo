/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.events;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.core.ValueRanks;
import org.eclipse.milo.opcua.sdk.core.nodes.Node;
import org.eclipse.milo.opcua.sdk.core.nodes.ObjectTypeNode;
import org.eclipse.milo.opcua.sdk.core.nodes.VariableNode;
import org.eclipse.milo.opcua.sdk.core.typetree.ReferenceTypeTree;
import org.eclipse.milo.opcua.sdk.server.AddressSpaceManager;
import org.eclipse.milo.opcua.sdk.server.AttributeReader;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.Session;
import org.eclipse.milo.opcua.sdk.server.events.operators.Operator;
import org.eclipse.milo.opcua.sdk.server.events.operators.Operators;
import org.eclipse.milo.opcua.sdk.server.model.objects.BaseEventTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DiagnosticInfo;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.FilterOperator;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.AttributeOperand;
import org.eclipse.milo.opcua.stack.core.types.structured.ContentFilter;
import org.eclipse.milo.opcua.stack.core.types.structured.ContentFilterElement;
import org.eclipse.milo.opcua.stack.core.types.structured.ContentFilterElementResult;
import org.eclipse.milo.opcua.stack.core.types.structured.ContentFilterResult;
import org.eclipse.milo.opcua.stack.core.types.structured.ElementOperand;
import org.eclipse.milo.opcua.stack.core.types.structured.EventFilter;
import org.eclipse.milo.opcua.stack.core.types.structured.EventFilterResult;
import org.eclipse.milo.opcua.stack.core.types.structured.FilterOperand;
import org.eclipse.milo.opcua.stack.core.types.structured.LiteralOperand;
import org.eclipse.milo.opcua.stack.core.types.structured.SimpleAttributeOperand;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventContentFilter {

  private static final Logger LOGGER = LoggerFactory.getLogger(EventContentFilter.class);

  public static EventFilterResult validate(FilterContext context, EventFilter filter)
      throws UaException {
    SimpleAttributeOperand[] selectClauses = filter.getSelectClauses();

    if (selectClauses == null || selectClauses.length == 0) {
      // A valid filter has at least one select clause
      throw new UaException(StatusCodes.Bad_EventFilterInvalid);
    }

    SelectClauseValidationResult selectClauseResults =
        validateSelectClauses(context, selectClauses);

    ContentFilterResult whereClauseResult = validateWhereClause(context, filter.getWhereClause());

    return new EventFilterResult(
        selectClauseResults.statusCodes, selectClauseResults.diagnosticInfos, whereClauseResult);
  }

  private static SelectClauseValidationResult validateSelectClauses(
      FilterContext context, SimpleAttributeOperand[] selectClauses) {

    List<StatusCode> statusCodes = new ArrayList<>();
    List<DiagnosticInfo> diagnosticInfos = new ArrayList<>();

    for (SimpleAttributeOperand select : selectClauses) {
      try {
        validateSimpleOperand(context, select);

        statusCodes.add(StatusCode.GOOD);
        diagnosticInfos.add(DiagnosticInfo.NULL_VALUE);
      } catch (ValidationException e) {
        statusCodes.add(e.getStatusCode());
        diagnosticInfos.add(e.getDiagnosticInfo());
      } catch (Throwable t) {
        LOGGER.error("Unexpected error validating operand: {}", select, t);
        statusCodes.add(new StatusCode(StatusCodes.Bad_InternalError));
        diagnosticInfos.add(DiagnosticInfo.NULL_VALUE);
      }
    }

    return new SelectClauseValidationResult(
        statusCodes.toArray(new StatusCode[0]), diagnosticInfos.toArray(new DiagnosticInfo[0]));
  }

  private static void validateSimpleOperand(FilterContext context, SimpleAttributeOperand select)
      throws ValidationException {

    NodeId eventTypeId = select.getTypeDefinitionId();

    if (eventTypeId != null && !eventTypeId.equals(NodeIds.BaseEventType)) {
      UaNode node =
          context.getServer().getAddressSpaceManager().getManagedNode(eventTypeId).orElse(null);

      if (node == null || node.getNodeClass() != NodeClass.ObjectType) {
        throw new ValidationException(StatusCodes.Bad_TypeDefinitionInvalid);
      }

      QualifiedName[] browsePath = select.getBrowsePath();

      if (browsePath == null || Arrays.stream(browsePath).anyMatch(Objects::isNull)) {
        throw new ValidationException(StatusCodes.Bad_BrowseNameInvalid);
      }

      Node relativeNode = getRelativeNode(context, node, browsePath);

      if (relativeNode == null) {
        throw new ValidationException(StatusCodes.Bad_NodeIdUnknown);
      }

      UInteger attributeId = select.getAttributeId();

      Set<AttributeId> validAttributes = AttributeId.getAttributes(relativeNode.getNodeClass());

      boolean validAttribute =
          AttributeId.from(attributeId).map(validAttributes::contains).orElse(false);

      if (!validAttribute) {
        throw new ValidationException(StatusCodes.Bad_AttributeIdInvalid);
      }

      String indexRange = select.getIndexRange();
      if (indexRange != null && !indexRange.isEmpty()) {
        if (relativeNode instanceof VariableNode) {
          int valueRank = ((VariableNode) relativeNode).getValueRank();

          if (valueRank == ValueRanks.Scalar) {
            throw new ValidationException(StatusCodes.Bad_IndexRangeInvalid);
          }
        } else {
          throw new ValidationException(StatusCodes.Bad_IndexRangeInvalid);
        }
      }
    }
  }

  @Nullable
  private static Node getRelativeNode(
      FilterContext context, @NonNull UaNode startingNode, @NonNull QualifiedName[] browsePath) {

    UaNode relativeNode = startingNode;

    Predicate<UaNode> nodePredicate =
        n -> n.getNodeClass() == NodeClass.Object || n.getNodeClass() == NodeClass.Variable;

    ReferenceTypeTree referenceTypeTree =
        startingNode.getNodeContext().getServer().getReferenceTypeTree();

    Predicate<Reference> referencePredicate =
        r ->
            r.isForward()
                && referenceTypeTree.isSubtypeOf(
                    r.getReferenceTypeId(), NodeIds.HierarchicalReferences);

    // find the Node relative to eventNode using browsePath.
    for (QualifiedName targetBrowsePath : browsePath) {
      relativeNode =
          relativeNode.findNode(targetBrowsePath, nodePredicate, referencePredicate).orElse(null);

      if (relativeNode == null) break;
    }

    return relativeNode;
  }

  private static ContentFilterResult validateWhereClause(
      FilterContext context, ContentFilter whereClause) {

    ContentFilterElement[] filterElements = whereClause.getElements();

    if (filterElements == null) {
      return new ContentFilterResult(new ContentFilterElementResult[0], new DiagnosticInfo[0]);
    }

    ContentFilterElementResult[] elementResults =
        Arrays.stream(filterElements)
            .map(e -> validateFilterElement(context, e))
            .toArray(ContentFilterElementResult[]::new);

    return new ContentFilterResult(elementResults, new DiagnosticInfo[0]);
  }

  private static ContentFilterElementResult validateFilterElement(
      @NonNull FilterContext context, @NonNull ContentFilterElement filterElement) {

    FilterOperator filterOperator = filterElement.getFilterOperator();

    if (!Operators.SUPPORTED_OPERATORS.contains(filterOperator)) {
      return new ContentFilterElementResult(
          new StatusCode(StatusCodes.Bad_FilterOperatorUnsupported),
          new StatusCode[0],
          new DiagnosticInfo[0]);
    }

    ExtensionObject[] xos = filterElement.getFilterOperands();

    if (xos == null || xos.length == 0) {
      return new ContentFilterElementResult(
          new StatusCode(StatusCodes.Bad_FilterOperandCountMismatch),
          new StatusCode[0],
          new DiagnosticInfo[0]);
    }

    FilterOperand[] operands = new FilterOperand[xos.length];
    StatusCode[] operandStatusCodes = new StatusCode[xos.length];

    for (int i = 0; i < xos.length; i++) {
      Object operand;
      try {
        operand = xos[i].decode(context.getServer().getStaticEncodingContext());
      } catch (Exception ignored) {
        operand = null;
      }

      if (operand instanceof FilterOperand) {
        operands[i] = (FilterOperand) operand;

        if (operand instanceof SimpleAttributeOperand) {
          try {
            validateSimpleOperand(context, (SimpleAttributeOperand) operand);

            operandStatusCodes[i] = StatusCode.GOOD;
          } catch (ValidationException e) {
            operandStatusCodes[i] = e.getStatusCode();
          }
        } else if (operand instanceof ElementOperand) {
          operandStatusCodes[i] = StatusCode.GOOD;
        } else if (operand instanceof LiteralOperand) {
          operandStatusCodes[i] = StatusCode.GOOD;
        } else {
          // includes AttributeOperand and any unknown/unhandle subclasses
          operandStatusCodes[i] = new StatusCode(StatusCodes.Bad_FilterOperandInvalid);
        }
      } else {
        operandStatusCodes[i] = new StatusCode(StatusCodes.Bad_FilterOperandInvalid);
      }
    }

    StatusCode operatorStatus = StatusCode.GOOD;

    try {
      Operator<?> operator = getOperator(filterOperator);
      operator.validate(context, operands);
    } catch (ValidationException e) {
      operatorStatus = e.getStatusCode();
    }

    return new ContentFilterElementResult(
        operatorStatus, operandStatusCodes, new DiagnosticInfo[0]);
  }

  public static Variant[] select(
      @NonNull FilterContext context,
      @NonNull SimpleAttributeOperand[] selectClauses,
      @NonNull BaseEventTypeNode eventNode) {

    return Arrays.stream(selectClauses)
        .map(
            operand -> {
              try {
                return new Variant(getSimpleAttribute(context, operand, eventNode));
              } catch (UaException e) {
                return Variant.NULL_VALUE;
              }
            })
        .toArray(Variant[]::new);
  }

  public static boolean evaluate(
      @NonNull FilterContext context,
      @NonNull ContentFilter whereClause,
      @NonNull BaseEventTypeNode eventNode)
      throws UaException {

    if (whereClause.getElements() == null || whereClause.getElements().length == 0) {
      return true;
    }

    ContentFilterElement[] elements = whereClause.getElements();

    OperatorContext operatorContext = new DefaultOperatorContext(context, elements);

    Object result = evaluate(operatorContext, eventNode, elements[0]);

    if (result == null) {
      return false;
    } else if (result instanceof Boolean) {
      return (Boolean) result;
    } else {
      throw new UaException(StatusCodes.Bad_ContentFilterInvalid);
    }
  }

  @Nullable
  private static Object evaluate(
      @NonNull OperatorContext context,
      @NonNull BaseEventTypeNode eventNode,
      @NonNull ContentFilterElement element)
      throws UaException {

    FilterOperator filterOperator = element.getFilterOperator();
    if (filterOperator == null) {
      throw new UaException(StatusCodes.Bad_FilterOperatorInvalid);
    }

    FilterOperand[] filterOperands =
        decodeOperands(context.getServer().getStaticEncodingContext(), element.getFilterOperands());

    Operator<?> operator = getOperator(filterOperator);

    return operator.apply(context, eventNode, filterOperands);
  }

  @NonNull
  private static FilterOperand[] decodeOperands(
      EncodingContext context, ExtensionObject @Nullable [] operandXos) {

    if (operandXos == null) {
      return new FilterOperand[0];
    } else {
      return Arrays.stream(operandXos)
          .map(xo -> (FilterOperand) xo.decode(context))
          .toArray(FilterOperand[]::new);
    }
  }

  @NonNull
  private static Operator<?> getOperator(@NonNull FilterOperator filterOperator) {
    return switch (filterOperator) {
      // Basic FilterOperators
      case Equals -> Operators.EQUALS;
      case IsNull -> Operators.IS_NULL;
      case GreaterThan -> Operators.GREATER_THAN;
      case LessThan -> Operators.LESS_THAN;
      case GreaterThanOrEqual -> Operators.GREATER_THAN_OR_EQUAL;
      case LessThanOrEqual -> Operators.LESS_THAN_OR_EQUAL;
      case Like -> Operators.UNSUPPORTED;
      case Not -> Operators.NOT;
      case Between -> Operators.UNSUPPORTED;
      case InList -> Operators.UNSUPPORTED;
      case And -> Operators.UNSUPPORTED;
      case Or -> Operators.UNSUPPORTED;
      case Cast -> Operators.CAST;
      case BitwiseAnd -> Operators.UNSUPPORTED;
      case BitwiseOr -> Operators.UNSUPPORTED;

      // Complex FilterOperators
      case InView -> Operators.UNSUPPORTED;
      case OfType -> Operators.OF_TYPE;
      case RelatedTo -> Operators.UNSUPPORTED;
    };
  }

  @SuppressWarnings("unused")
  private static Object getAttribute(
      @NonNull FilterContext context,
      @NonNull AttributeOperand operand,
      @NonNull BaseEventTypeNode eventNode)
      throws UaException {

    // AttributeOperand is not allowed to be used in EventFilters... it's for the Query services.
    // Right now Query services are unsupported and this class exists to handle the application
    // of the whereClause of a ContentFilter to an event.
    throw new UaException(StatusCodes.Bad_EventFilterInvalid);
  }

  private static Object getSimpleAttribute(
      @NonNull FilterContext context,
      @NonNull SimpleAttributeOperand operand,
      @NonNull BaseEventTypeNode eventNode)
      throws UaException {

    NodeId typeDefinitionId = operand.getTypeDefinitionId();

    if (typeDefinitionId != null && !typeDefinitionId.equals(NodeIds.BaseEventType)) {
      NodeId eventTypeDefinitionId = eventNode.getTypeDefinitionNode().getNodeId();

      boolean sameOrSubtype =
          typeDefinitionId.equals(eventTypeDefinitionId)
              || subtypeOf(eventTypeDefinitionId, typeDefinitionId, context.getServer());

      if (!sameOrSubtype) {
        return null;
      }
    }

    QualifiedName[] browsePath = operand.getBrowsePath();

    UaNode targetNode = eventNode;

    if (browsePath != null) {
      Predicate<UaNode> nodePredicate =
          n -> n.getNodeClass() == NodeClass.Object || n.getNodeClass() == NodeClass.Variable;

      ReferenceTypeTree referenceTypeTree =
          eventNode.getNodeContext().getServer().getReferenceTypeTree();

      Predicate<Reference> referencePredicate =
          r ->
              r.isForward()
                  && referenceTypeTree.isSubtypeOf(
                      r.getReferenceTypeId(), NodeIds.HierarchicalReferences);

      // find the Node relative to eventNode using browsePath.
      for (QualifiedName targetBrowsePath : browsePath) {
        targetNode =
            targetNode.findNode(targetBrowsePath, nodePredicate, referencePredicate).orElse(null);

        if (targetNode == null) break;
      }
    }

    if (targetNode != null) {
      // read the attribute
      AttributeId attributeId =
          AttributeId.from(operand.getAttributeId())
              .orElseThrow(() -> new UaException(StatusCodes.Bad_AttributeIdInvalid));

      String indexRange = operand.getIndexRange();

      DataValue value =
          AttributeReader.readAttribute(
              context,
              targetNode,
              attributeId,
              TimestampsToReturn.Neither,
              indexRange,
              QualifiedName.NULL_VALUE);

      return value.value().value();
    } else {
      return null;
    }
  }

  public static boolean subtypeOf(NodeId typeId, NodeId superTypeId, OpcUaServer server) {
    UaNode node = server.getAddressSpaceManager().getManagedNode(typeId).orElse(null);

    if (node instanceof ObjectTypeNode) {
      return getParentTypeDefinition(node, server)
          .map(Node::getNodeId)
          .map(id -> id.equals(superTypeId) || subtypeOf(id, superTypeId, server))
          .orElse(false);
    } else {
      return false;
    }
  }

  private static Optional<UaNode> getParentTypeDefinition(UaNode node, OpcUaServer server) {
    AddressSpaceManager addressSpaceManager = server.getAddressSpaceManager();
    NamespaceTable namespaceTable = server.getNamespaceTable();

    return addressSpaceManager.getManagedReferences(node.getNodeId()).stream()
        .filter(Reference.SUBTYPE_OF)
        .flatMap(r -> r.getTargetNodeId().toNodeId(namespaceTable).stream())
        .findFirst()
        .flatMap(addressSpaceManager::getManagedNode);
  }

  static class DefaultOperatorContext implements OperatorContext {

    private final FilterContext filterContext;
    private final ContentFilterElement[] elements;

    DefaultOperatorContext(FilterContext filterContext, ContentFilterElement[] elements) {
      this.filterContext = filterContext;
      this.elements = elements;
    }

    @Override
    public Optional<Session> getSession() {
      return filterContext.getSession();
    }

    @Override
    public OpcUaServer getServer() {
      return filterContext.getServer();
    }

    @Override
    public ContentFilterElement[] getElements() {
      return elements;
    }

    @Nullable
    @Override
    public Object resolve(FilterOperand operand, BaseEventTypeNode eventNode) throws UaException {
      if (operand instanceof LiteralOperand) {
        return ((LiteralOperand) operand).getValue().value();
      } else if (operand instanceof ElementOperand) {
        UInteger index = ((ElementOperand) operand).getIndex();

        ContentFilterElement element = elements[index.intValue()];

        return evaluate(this, eventNode, element);
      } else if (operand instanceof AttributeOperand ao) {
        return getAttribute(filterContext, ao, eventNode);
      } else if (operand instanceof SimpleAttributeOperand sao) {
        return getSimpleAttribute(filterContext, sao, eventNode);
      } else {
        throw new UaException(StatusCodes.Bad_FilterOperandInvalid);
      }
    }
  }

  static class SelectClauseValidationResult {
    private final StatusCode[] statusCodes;
    private final DiagnosticInfo[] diagnosticInfos;

    public SelectClauseValidationResult(
        StatusCode[] statusCodes, DiagnosticInfo[] diagnosticInfos) {
      this.statusCodes = statusCodes;
      this.diagnosticInfos = diagnosticInfos;
    }
  }
}
