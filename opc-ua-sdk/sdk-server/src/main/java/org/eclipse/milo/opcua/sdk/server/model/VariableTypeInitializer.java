package org.eclipse.milo.opcua.sdk.server.model;

import org.eclipse.milo.opcua.sdk.server.VariableTypeManager;
import org.eclipse.milo.opcua.sdk.server.model.variables.AlarmRateVariableType;
import org.eclipse.milo.opcua.sdk.server.model.variables.AlarmRateVariableTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.AlarmStateVariableType;
import org.eclipse.milo.opcua.sdk.server.model.variables.AlarmStateVariableTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.AnalogItemType;
import org.eclipse.milo.opcua.sdk.server.model.variables.AnalogItemTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.AnalogNumberItemType;
import org.eclipse.milo.opcua.sdk.server.model.variables.AnalogNumberItemTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.AnalogNumberUnitRangeType;
import org.eclipse.milo.opcua.sdk.server.model.variables.AnalogNumberUnitRangeTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.AnalogUnitRangeType;
import org.eclipse.milo.opcua.sdk.server.model.variables.AnalogUnitRangeTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.AnalogUnitType;
import org.eclipse.milo.opcua.sdk.server.model.variables.AnalogUnitTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.ArrayItemType;
import org.eclipse.milo.opcua.sdk.server.model.variables.ArrayItemTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.AudioVariableType;
import org.eclipse.milo.opcua.sdk.server.model.variables.AudioVariableTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.BaseAnalogType;
import org.eclipse.milo.opcua.sdk.server.model.variables.BaseAnalogTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.BaseDataVariableType;
import org.eclipse.milo.opcua.sdk.server.model.variables.BaseDataVariableTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.BaseVariableType;
import org.eclipse.milo.opcua.sdk.server.model.variables.BaseVariableTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.BitFieldType;
import org.eclipse.milo.opcua.sdk.server.model.variables.BitFieldTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.BuildInfoType;
import org.eclipse.milo.opcua.sdk.server.model.variables.BuildInfoTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.CartesianCoordinatesType;
import org.eclipse.milo.opcua.sdk.server.model.variables.CartesianCoordinatesTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.ConditionVariableType;
import org.eclipse.milo.opcua.sdk.server.model.variables.ConditionVariableTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.CubeItemType;
import org.eclipse.milo.opcua.sdk.server.model.variables.CubeItemTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.DataItemType;
import org.eclipse.milo.opcua.sdk.server.model.variables.DataItemTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.DataTypeDescriptionType;
import org.eclipse.milo.opcua.sdk.server.model.variables.DataTypeDescriptionTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.DataTypeDictionaryType;
import org.eclipse.milo.opcua.sdk.server.model.variables.DataTypeDictionaryTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.DiscreteItemType;
import org.eclipse.milo.opcua.sdk.server.model.variables.DiscreteItemTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.ElseGuardVariableType;
import org.eclipse.milo.opcua.sdk.server.model.variables.ElseGuardVariableTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.ExpressionGuardVariableType;
import org.eclipse.milo.opcua.sdk.server.model.variables.ExpressionGuardVariableTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.FiniteStateVariableType;
import org.eclipse.milo.opcua.sdk.server.model.variables.FiniteStateVariableTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.FiniteTransitionVariableType;
import org.eclipse.milo.opcua.sdk.server.model.variables.FiniteTransitionVariableTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.FrameType;
import org.eclipse.milo.opcua.sdk.server.model.variables.FrameTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.GuardVariableType;
import org.eclipse.milo.opcua.sdk.server.model.variables.GuardVariableTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.ImageItemType;
import org.eclipse.milo.opcua.sdk.server.model.variables.ImageItemTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.MultiStateDictionaryEntryDiscreteBaseType;
import org.eclipse.milo.opcua.sdk.server.model.variables.MultiStateDictionaryEntryDiscreteBaseTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.MultiStateDictionaryEntryDiscreteType;
import org.eclipse.milo.opcua.sdk.server.model.variables.MultiStateDictionaryEntryDiscreteTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.MultiStateDiscreteType;
import org.eclipse.milo.opcua.sdk.server.model.variables.MultiStateDiscreteTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.MultiStateValueDiscreteType;
import org.eclipse.milo.opcua.sdk.server.model.variables.MultiStateValueDiscreteTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.NDimensionArrayItemType;
import org.eclipse.milo.opcua.sdk.server.model.variables.NDimensionArrayItemTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.OptionSetType;
import org.eclipse.milo.opcua.sdk.server.model.variables.OptionSetTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.OrientationType;
import org.eclipse.milo.opcua.sdk.server.model.variables.OrientationTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.ProgramDiagnostic2Type;
import org.eclipse.milo.opcua.sdk.server.model.variables.ProgramDiagnostic2TypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.ProgramDiagnosticType;
import org.eclipse.milo.opcua.sdk.server.model.variables.ProgramDiagnosticTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.PubSubDiagnosticsCounterType;
import org.eclipse.milo.opcua.sdk.server.model.variables.PubSubDiagnosticsCounterTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.RationalNumberType;
import org.eclipse.milo.opcua.sdk.server.model.variables.RationalNumberTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.ReferenceDescriptionVariableType;
import org.eclipse.milo.opcua.sdk.server.model.variables.ReferenceDescriptionVariableTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.SamplingIntervalDiagnosticsArrayType;
import org.eclipse.milo.opcua.sdk.server.model.variables.SamplingIntervalDiagnosticsArrayTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.SamplingIntervalDiagnosticsType;
import org.eclipse.milo.opcua.sdk.server.model.variables.SamplingIntervalDiagnosticsTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.SelectionListType;
import org.eclipse.milo.opcua.sdk.server.model.variables.SelectionListTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.ServerDiagnosticsSummaryType;
import org.eclipse.milo.opcua.sdk.server.model.variables.ServerDiagnosticsSummaryTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.ServerStatusType;
import org.eclipse.milo.opcua.sdk.server.model.variables.ServerStatusTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.ServerVendorCapabilityType;
import org.eclipse.milo.opcua.sdk.server.model.variables.ServerVendorCapabilityTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.SessionDiagnosticsArrayType;
import org.eclipse.milo.opcua.sdk.server.model.variables.SessionDiagnosticsArrayTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.SessionDiagnosticsVariableType;
import org.eclipse.milo.opcua.sdk.server.model.variables.SessionDiagnosticsVariableTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.SessionSecurityDiagnosticsArrayType;
import org.eclipse.milo.opcua.sdk.server.model.variables.SessionSecurityDiagnosticsArrayTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.SessionSecurityDiagnosticsType;
import org.eclipse.milo.opcua.sdk.server.model.variables.SessionSecurityDiagnosticsTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.StateVariableType;
import org.eclipse.milo.opcua.sdk.server.model.variables.StateVariableTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.SubscriptionDiagnosticsArrayType;
import org.eclipse.milo.opcua.sdk.server.model.variables.SubscriptionDiagnosticsArrayTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.SubscriptionDiagnosticsType;
import org.eclipse.milo.opcua.sdk.server.model.variables.SubscriptionDiagnosticsTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.ThreeDCartesianCoordinatesType;
import org.eclipse.milo.opcua.sdk.server.model.variables.ThreeDCartesianCoordinatesTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.ThreeDFrameType;
import org.eclipse.milo.opcua.sdk.server.model.variables.ThreeDFrameTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.ThreeDOrientationType;
import org.eclipse.milo.opcua.sdk.server.model.variables.ThreeDOrientationTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.ThreeDVectorType;
import org.eclipse.milo.opcua.sdk.server.model.variables.ThreeDVectorTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.TransitionVariableType;
import org.eclipse.milo.opcua.sdk.server.model.variables.TransitionVariableTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.TwoStateDiscreteType;
import org.eclipse.milo.opcua.sdk.server.model.variables.TwoStateDiscreteTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.TwoStateVariableType;
import org.eclipse.milo.opcua.sdk.server.model.variables.TwoStateVariableTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.VectorType;
import org.eclipse.milo.opcua.sdk.server.model.variables.VectorTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.XYArrayItemType;
import org.eclipse.milo.opcua.sdk.server.model.variables.XYArrayItemTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.YArrayItemType;
import org.eclipse.milo.opcua.sdk.server.model.variables.YArrayItemTypeNode;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;

/**
 * Registers this library's VariableType constructors for the server's lifetime. Serialize
 * initialization before loading.
 */
public final class VariableTypeInitializer {
  private static final VariableTypeManager.VariableNodeConstructor CONSTRUCTOR_0 =
      BaseVariableTypeNode::new;

  private static final VariableTypeManager.VariableNodeConstructor CONSTRUCTOR_1 =
      BaseDataVariableTypeNode::new;

  private static final VariableTypeManager.VariableNodeConstructor CONSTRUCTOR_2 =
      DataItemTypeNode::new;

  private static final VariableTypeManager.VariableNodeConstructor CONSTRUCTOR_3 =
      DiscreteItemTypeNode::new;

  private static final VariableTypeManager.VariableNodeConstructor CONSTRUCTOR_4 =
      MultiStateValueDiscreteTypeNode::new;

  private static final VariableTypeManager.VariableNodeConstructor CONSTRUCTOR_5 =
      OptionSetTypeNode::new;

  private static final VariableTypeManager.VariableNodeConstructor CONSTRUCTOR_6 =
      ArrayItemTypeNode::new;

  private static final VariableTypeManager.VariableNodeConstructor CONSTRUCTOR_7 =
      YArrayItemTypeNode::new;

  private static final VariableTypeManager.VariableNodeConstructor CONSTRUCTOR_8 =
      XYArrayItemTypeNode::new;

  private static final VariableTypeManager.VariableNodeConstructor CONSTRUCTOR_9 =
      ImageItemTypeNode::new;

  private static final VariableTypeManager.VariableNodeConstructor CONSTRUCTOR_10 =
      CubeItemTypeNode::new;

  private static final VariableTypeManager.VariableNodeConstructor CONSTRUCTOR_11 =
      NDimensionArrayItemTypeNode::new;

  private static final VariableTypeManager.VariableNodeConstructor CONSTRUCTOR_12 =
      GuardVariableTypeNode::new;

  private static final VariableTypeManager.VariableNodeConstructor CONSTRUCTOR_13 =
      ExpressionGuardVariableTypeNode::new;

  private static final VariableTypeManager.VariableNodeConstructor CONSTRUCTOR_14 =
      ElseGuardVariableTypeNode::new;

  private static final VariableTypeManager.VariableNodeConstructor CONSTRUCTOR_15 =
      BaseAnalogTypeNode::new;

  private static final VariableTypeManager.VariableNodeConstructor CONSTRUCTOR_16 =
      ProgramDiagnostic2TypeNode::new;

  private static final VariableTypeManager.VariableNodeConstructor CONSTRUCTOR_17 =
      SelectionListTypeNode::new;

  private static final VariableTypeManager.VariableNodeConstructor CONSTRUCTOR_18 =
      AlarmRateVariableTypeNode::new;

  private static final VariableTypeManager.VariableNodeConstructor CONSTRUCTOR_19 =
      AnalogUnitTypeNode::new;

  private static final VariableTypeManager.VariableNodeConstructor CONSTRUCTOR_20 =
      AnalogItemTypeNode::new;

  private static final VariableTypeManager.VariableNodeConstructor CONSTRUCTOR_21 =
      AnalogUnitRangeTypeNode::new;

  private static final VariableTypeManager.VariableNodeConstructor CONSTRUCTOR_22 =
      RationalNumberTypeNode::new;

  private static final VariableTypeManager.VariableNodeConstructor CONSTRUCTOR_23 =
      VectorTypeNode::new;

  private static final VariableTypeManager.VariableNodeConstructor CONSTRUCTOR_24 =
      ThreeDVectorTypeNode::new;

  private static final VariableTypeManager.VariableNodeConstructor CONSTRUCTOR_25 =
      AudioVariableTypeNode::new;

  private static final VariableTypeManager.VariableNodeConstructor CONSTRUCTOR_26 =
      CartesianCoordinatesTypeNode::new;

  private static final VariableTypeManager.VariableNodeConstructor CONSTRUCTOR_27 =
      ThreeDCartesianCoordinatesTypeNode::new;

  private static final VariableTypeManager.VariableNodeConstructor CONSTRUCTOR_28 =
      OrientationTypeNode::new;

  private static final VariableTypeManager.VariableNodeConstructor CONSTRUCTOR_29 =
      ThreeDOrientationTypeNode::new;

  private static final VariableTypeManager.VariableNodeConstructor CONSTRUCTOR_30 =
      FrameTypeNode::new;

  private static final VariableTypeManager.VariableNodeConstructor CONSTRUCTOR_31 =
      ThreeDFrameTypeNode::new;

  private static final VariableTypeManager.VariableNodeConstructor CONSTRUCTOR_32 =
      MultiStateDictionaryEntryDiscreteBaseTypeNode::new;

  private static final VariableTypeManager.VariableNodeConstructor CONSTRUCTOR_33 =
      MultiStateDictionaryEntryDiscreteTypeNode::new;

  private static final VariableTypeManager.VariableNodeConstructor CONSTRUCTOR_34 =
      PubSubDiagnosticsCounterTypeNode::new;

  private static final VariableTypeManager.VariableNodeConstructor CONSTRUCTOR_35 =
      ServerVendorCapabilityTypeNode::new;

  private static final VariableTypeManager.VariableNodeConstructor CONSTRUCTOR_36 =
      ServerStatusTypeNode::new;

  private static final VariableTypeManager.VariableNodeConstructor CONSTRUCTOR_37 =
      ServerDiagnosticsSummaryTypeNode::new;

  private static final VariableTypeManager.VariableNodeConstructor CONSTRUCTOR_38 =
      SamplingIntervalDiagnosticsArrayTypeNode::new;

  private static final VariableTypeManager.VariableNodeConstructor CONSTRUCTOR_39 =
      SamplingIntervalDiagnosticsTypeNode::new;

  private static final VariableTypeManager.VariableNodeConstructor CONSTRUCTOR_40 =
      SubscriptionDiagnosticsArrayTypeNode::new;

  private static final VariableTypeManager.VariableNodeConstructor CONSTRUCTOR_41 =
      SubscriptionDiagnosticsTypeNode::new;

  private static final VariableTypeManager.VariableNodeConstructor CONSTRUCTOR_42 =
      SessionDiagnosticsArrayTypeNode::new;

  private static final VariableTypeManager.VariableNodeConstructor CONSTRUCTOR_43 =
      SessionDiagnosticsVariableTypeNode::new;

  private static final VariableTypeManager.VariableNodeConstructor CONSTRUCTOR_44 =
      SessionSecurityDiagnosticsArrayTypeNode::new;

  private static final VariableTypeManager.VariableNodeConstructor CONSTRUCTOR_45 =
      SessionSecurityDiagnosticsTypeNode::new;

  private static final VariableTypeManager.VariableNodeConstructor CONSTRUCTOR_46 =
      TwoStateDiscreteTypeNode::new;

  private static final VariableTypeManager.VariableNodeConstructor CONSTRUCTOR_47 =
      MultiStateDiscreteTypeNode::new;

  private static final VariableTypeManager.VariableNodeConstructor CONSTRUCTOR_48 =
      ProgramDiagnosticTypeNode::new;

  private static final VariableTypeManager.VariableNodeConstructor CONSTRUCTOR_49 =
      AnalogNumberItemTypeNode::new;

  private static final VariableTypeManager.VariableNodeConstructor CONSTRUCTOR_50 =
      AnalogNumberUnitRangeTypeNode::new;

  private static final VariableTypeManager.VariableNodeConstructor CONSTRUCTOR_51 =
      StateVariableTypeNode::new;

  private static final VariableTypeManager.VariableNodeConstructor CONSTRUCTOR_52 =
      FiniteStateVariableTypeNode::new;

  private static final VariableTypeManager.VariableNodeConstructor CONSTRUCTOR_53 =
      TransitionVariableTypeNode::new;

  private static final VariableTypeManager.VariableNodeConstructor CONSTRUCTOR_54 =
      FiniteTransitionVariableTypeNode::new;

  private static final VariableTypeManager.VariableNodeConstructor CONSTRUCTOR_55 =
      BuildInfoTypeNode::new;

  private static final VariableTypeManager.VariableNodeConstructor CONSTRUCTOR_56 =
      AlarmStateVariableTypeNode::new;

  private static final VariableTypeManager.VariableNodeConstructor CONSTRUCTOR_57 =
      BitFieldTypeNode::new;

  private static final VariableTypeManager.VariableNodeConstructor CONSTRUCTOR_58 =
      ReferenceDescriptionVariableTypeNode::new;

  private static final VariableTypeManager.VariableNodeConstructor CONSTRUCTOR_59 =
      PropertyTypeNode::new;

  private static final VariableTypeManager.VariableNodeConstructor CONSTRUCTOR_60 =
      DataTypeDescriptionTypeNode::new;

  private static final VariableTypeManager.VariableNodeConstructor CONSTRUCTOR_61 =
      DataTypeDictionaryTypeNode::new;

  private static final VariableTypeManager.VariableNodeConstructor CONSTRUCTOR_62 =
      TwoStateVariableTypeNode::new;

  private static final VariableTypeManager.VariableNodeConstructor CONSTRUCTOR_63 =
      ConditionVariableTypeNode::new;

  private VariableTypeInitializer() {}

  /**
   * Prevalidates every namespace and registration before installing constructors. Repeated calls
   * preserve identical registrations.
   *
   * @param namespaceTable the server namespace table with required URIs already registered.
   * @param manager the server's type manager.
   * @throws IllegalStateException if any existing registration differs.
   * @throws IllegalArgumentException if a namespace is absent.
   */
  public static void initialize(NamespaceTable namespaceTable, VariableTypeManager manager) {
    NodeId id0 = ServerNodeSupport.resolve(namespaceTable, BaseVariableType.TYPE_ID);
    VariableTypeManager.RegisteredVariableType registered0 =
        manager.getRegisteredType(id0).orElse(null);
    if (registered0 != null
        && (registered0.nodeClass() != BaseVariableTypeNode.class
            || registered0.nodeConstructor() != CONSTRUCTOR_0
            || registered0.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + BaseVariableType.TYPE_ID
              + " expected "
              + BaseVariableTypeNode.class.getName());
    }
    NodeId id1 = ServerNodeSupport.resolve(namespaceTable, BaseDataVariableType.TYPE_ID);
    VariableTypeManager.RegisteredVariableType registered1 =
        manager.getRegisteredType(id1).orElse(null);
    if (registered1 != null
        && (registered1.nodeClass() != BaseDataVariableTypeNode.class
            || registered1.nodeConstructor() != CONSTRUCTOR_1
            || registered1.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + BaseDataVariableType.TYPE_ID
              + " expected "
              + BaseDataVariableTypeNode.class.getName());
    }
    NodeId id2 = ServerNodeSupport.resolve(namespaceTable, DataItemType.TYPE_ID);
    VariableTypeManager.RegisteredVariableType registered2 =
        manager.getRegisteredType(id2).orElse(null);
    if (registered2 != null
        && (registered2.nodeClass() != DataItemTypeNode.class
            || registered2.nodeConstructor() != CONSTRUCTOR_2
            || registered2.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + DataItemType.TYPE_ID
              + " expected "
              + DataItemTypeNode.class.getName());
    }
    NodeId id3 = ServerNodeSupport.resolve(namespaceTable, DiscreteItemType.TYPE_ID);
    VariableTypeManager.RegisteredVariableType registered3 =
        manager.getRegisteredType(id3).orElse(null);
    if (registered3 != null
        && (registered3.nodeClass() != DiscreteItemTypeNode.class
            || registered3.nodeConstructor() != CONSTRUCTOR_3
            || registered3.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + DiscreteItemType.TYPE_ID
              + " expected "
              + DiscreteItemTypeNode.class.getName());
    }
    NodeId id4 = ServerNodeSupport.resolve(namespaceTable, MultiStateValueDiscreteType.TYPE_ID);
    VariableTypeManager.RegisteredVariableType registered4 =
        manager.getRegisteredType(id4).orElse(null);
    if (registered4 != null
        && (registered4.nodeClass() != MultiStateValueDiscreteTypeNode.class
            || registered4.nodeConstructor() != CONSTRUCTOR_4
            || registered4.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + MultiStateValueDiscreteType.TYPE_ID
              + " expected "
              + MultiStateValueDiscreteTypeNode.class.getName());
    }
    NodeId id5 = ServerNodeSupport.resolve(namespaceTable, OptionSetType.TYPE_ID);
    VariableTypeManager.RegisteredVariableType registered5 =
        manager.getRegisteredType(id5).orElse(null);
    if (registered5 != null
        && (registered5.nodeClass() != OptionSetTypeNode.class
            || registered5.nodeConstructor() != CONSTRUCTOR_5
            || registered5.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + OptionSetType.TYPE_ID
              + " expected "
              + OptionSetTypeNode.class.getName());
    }
    NodeId id6 = ServerNodeSupport.resolve(namespaceTable, ArrayItemType.TYPE_ID);
    VariableTypeManager.RegisteredVariableType registered6 =
        manager.getRegisteredType(id6).orElse(null);
    if (registered6 != null
        && (registered6.nodeClass() != ArrayItemTypeNode.class
            || registered6.nodeConstructor() != CONSTRUCTOR_6
            || registered6.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + ArrayItemType.TYPE_ID
              + " expected "
              + ArrayItemTypeNode.class.getName());
    }
    NodeId id7 = ServerNodeSupport.resolve(namespaceTable, YArrayItemType.TYPE_ID);
    VariableTypeManager.RegisteredVariableType registered7 =
        manager.getRegisteredType(id7).orElse(null);
    if (registered7 != null
        && (registered7.nodeClass() != YArrayItemTypeNode.class
            || registered7.nodeConstructor() != CONSTRUCTOR_7
            || registered7.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + YArrayItemType.TYPE_ID
              + " expected "
              + YArrayItemTypeNode.class.getName());
    }
    NodeId id8 = ServerNodeSupport.resolve(namespaceTable, XYArrayItemType.TYPE_ID);
    VariableTypeManager.RegisteredVariableType registered8 =
        manager.getRegisteredType(id8).orElse(null);
    if (registered8 != null
        && (registered8.nodeClass() != XYArrayItemTypeNode.class
            || registered8.nodeConstructor() != CONSTRUCTOR_8
            || registered8.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + XYArrayItemType.TYPE_ID
              + " expected "
              + XYArrayItemTypeNode.class.getName());
    }
    NodeId id9 = ServerNodeSupport.resolve(namespaceTable, ImageItemType.TYPE_ID);
    VariableTypeManager.RegisteredVariableType registered9 =
        manager.getRegisteredType(id9).orElse(null);
    if (registered9 != null
        && (registered9.nodeClass() != ImageItemTypeNode.class
            || registered9.nodeConstructor() != CONSTRUCTOR_9
            || registered9.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + ImageItemType.TYPE_ID
              + " expected "
              + ImageItemTypeNode.class.getName());
    }
    NodeId id10 = ServerNodeSupport.resolve(namespaceTable, CubeItemType.TYPE_ID);
    VariableTypeManager.RegisteredVariableType registered10 =
        manager.getRegisteredType(id10).orElse(null);
    if (registered10 != null
        && (registered10.nodeClass() != CubeItemTypeNode.class
            || registered10.nodeConstructor() != CONSTRUCTOR_10
            || registered10.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + CubeItemType.TYPE_ID
              + " expected "
              + CubeItemTypeNode.class.getName());
    }
    NodeId id11 = ServerNodeSupport.resolve(namespaceTable, NDimensionArrayItemType.TYPE_ID);
    VariableTypeManager.RegisteredVariableType registered11 =
        manager.getRegisteredType(id11).orElse(null);
    if (registered11 != null
        && (registered11.nodeClass() != NDimensionArrayItemTypeNode.class
            || registered11.nodeConstructor() != CONSTRUCTOR_11
            || registered11.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + NDimensionArrayItemType.TYPE_ID
              + " expected "
              + NDimensionArrayItemTypeNode.class.getName());
    }
    NodeId id12 = ServerNodeSupport.resolve(namespaceTable, GuardVariableType.TYPE_ID);
    VariableTypeManager.RegisteredVariableType registered12 =
        manager.getRegisteredType(id12).orElse(null);
    if (registered12 != null
        && (registered12.nodeClass() != GuardVariableTypeNode.class
            || registered12.nodeConstructor() != CONSTRUCTOR_12
            || registered12.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + GuardVariableType.TYPE_ID
              + " expected "
              + GuardVariableTypeNode.class.getName());
    }
    NodeId id13 = ServerNodeSupport.resolve(namespaceTable, ExpressionGuardVariableType.TYPE_ID);
    VariableTypeManager.RegisteredVariableType registered13 =
        manager.getRegisteredType(id13).orElse(null);
    if (registered13 != null
        && (registered13.nodeClass() != ExpressionGuardVariableTypeNode.class
            || registered13.nodeConstructor() != CONSTRUCTOR_13
            || registered13.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + ExpressionGuardVariableType.TYPE_ID
              + " expected "
              + ExpressionGuardVariableTypeNode.class.getName());
    }
    NodeId id14 = ServerNodeSupport.resolve(namespaceTable, ElseGuardVariableType.TYPE_ID);
    VariableTypeManager.RegisteredVariableType registered14 =
        manager.getRegisteredType(id14).orElse(null);
    if (registered14 != null
        && (registered14.nodeClass() != ElseGuardVariableTypeNode.class
            || registered14.nodeConstructor() != CONSTRUCTOR_14
            || registered14.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + ElseGuardVariableType.TYPE_ID
              + " expected "
              + ElseGuardVariableTypeNode.class.getName());
    }
    NodeId id15 = ServerNodeSupport.resolve(namespaceTable, BaseAnalogType.TYPE_ID);
    VariableTypeManager.RegisteredVariableType registered15 =
        manager.getRegisteredType(id15).orElse(null);
    if (registered15 != null
        && (registered15.nodeClass() != BaseAnalogTypeNode.class
            || registered15.nodeConstructor() != CONSTRUCTOR_15
            || registered15.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + BaseAnalogType.TYPE_ID
              + " expected "
              + BaseAnalogTypeNode.class.getName());
    }
    NodeId id16 = ServerNodeSupport.resolve(namespaceTable, ProgramDiagnostic2Type.TYPE_ID);
    VariableTypeManager.RegisteredVariableType registered16 =
        manager.getRegisteredType(id16).orElse(null);
    if (registered16 != null
        && (registered16.nodeClass() != ProgramDiagnostic2TypeNode.class
            || registered16.nodeConstructor() != CONSTRUCTOR_16
            || registered16.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + ProgramDiagnostic2Type.TYPE_ID
              + " expected "
              + ProgramDiagnostic2TypeNode.class.getName());
    }
    NodeId id17 = ServerNodeSupport.resolve(namespaceTable, SelectionListType.TYPE_ID);
    VariableTypeManager.RegisteredVariableType registered17 =
        manager.getRegisteredType(id17).orElse(null);
    if (registered17 != null
        && (registered17.nodeClass() != SelectionListTypeNode.class
            || registered17.nodeConstructor() != CONSTRUCTOR_17
            || registered17.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + SelectionListType.TYPE_ID
              + " expected "
              + SelectionListTypeNode.class.getName());
    }
    NodeId id18 = ServerNodeSupport.resolve(namespaceTable, AlarmRateVariableType.TYPE_ID);
    VariableTypeManager.RegisteredVariableType registered18 =
        manager.getRegisteredType(id18).orElse(null);
    if (registered18 != null
        && (registered18.nodeClass() != AlarmRateVariableTypeNode.class
            || registered18.nodeConstructor() != CONSTRUCTOR_18
            || registered18.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + AlarmRateVariableType.TYPE_ID
              + " expected "
              + AlarmRateVariableTypeNode.class.getName());
    }
    NodeId id19 = ServerNodeSupport.resolve(namespaceTable, AnalogUnitType.TYPE_ID);
    VariableTypeManager.RegisteredVariableType registered19 =
        manager.getRegisteredType(id19).orElse(null);
    if (registered19 != null
        && (registered19.nodeClass() != AnalogUnitTypeNode.class
            || registered19.nodeConstructor() != CONSTRUCTOR_19
            || registered19.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + AnalogUnitType.TYPE_ID
              + " expected "
              + AnalogUnitTypeNode.class.getName());
    }
    NodeId id20 = ServerNodeSupport.resolve(namespaceTable, AnalogItemType.TYPE_ID);
    VariableTypeManager.RegisteredVariableType registered20 =
        manager.getRegisteredType(id20).orElse(null);
    if (registered20 != null
        && (registered20.nodeClass() != AnalogItemTypeNode.class
            || registered20.nodeConstructor() != CONSTRUCTOR_20
            || registered20.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + AnalogItemType.TYPE_ID
              + " expected "
              + AnalogItemTypeNode.class.getName());
    }
    NodeId id21 = ServerNodeSupport.resolve(namespaceTable, AnalogUnitRangeType.TYPE_ID);
    VariableTypeManager.RegisteredVariableType registered21 =
        manager.getRegisteredType(id21).orElse(null);
    if (registered21 != null
        && (registered21.nodeClass() != AnalogUnitRangeTypeNode.class
            || registered21.nodeConstructor() != CONSTRUCTOR_21
            || registered21.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + AnalogUnitRangeType.TYPE_ID
              + " expected "
              + AnalogUnitRangeTypeNode.class.getName());
    }
    NodeId id22 = ServerNodeSupport.resolve(namespaceTable, RationalNumberType.TYPE_ID);
    VariableTypeManager.RegisteredVariableType registered22 =
        manager.getRegisteredType(id22).orElse(null);
    if (registered22 != null
        && (registered22.nodeClass() != RationalNumberTypeNode.class
            || registered22.nodeConstructor() != CONSTRUCTOR_22
            || registered22.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + RationalNumberType.TYPE_ID
              + " expected "
              + RationalNumberTypeNode.class.getName());
    }
    NodeId id23 = ServerNodeSupport.resolve(namespaceTable, VectorType.TYPE_ID);
    VariableTypeManager.RegisteredVariableType registered23 =
        manager.getRegisteredType(id23).orElse(null);
    if (registered23 != null
        && (registered23.nodeClass() != VectorTypeNode.class
            || registered23.nodeConstructor() != CONSTRUCTOR_23
            || registered23.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + VectorType.TYPE_ID
              + " expected "
              + VectorTypeNode.class.getName());
    }
    NodeId id24 = ServerNodeSupport.resolve(namespaceTable, ThreeDVectorType.TYPE_ID);
    VariableTypeManager.RegisteredVariableType registered24 =
        manager.getRegisteredType(id24).orElse(null);
    if (registered24 != null
        && (registered24.nodeClass() != ThreeDVectorTypeNode.class
            || registered24.nodeConstructor() != CONSTRUCTOR_24
            || registered24.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + ThreeDVectorType.TYPE_ID
              + " expected "
              + ThreeDVectorTypeNode.class.getName());
    }
    NodeId id25 = ServerNodeSupport.resolve(namespaceTable, AudioVariableType.TYPE_ID);
    VariableTypeManager.RegisteredVariableType registered25 =
        manager.getRegisteredType(id25).orElse(null);
    if (registered25 != null
        && (registered25.nodeClass() != AudioVariableTypeNode.class
            || registered25.nodeConstructor() != CONSTRUCTOR_25
            || registered25.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + AudioVariableType.TYPE_ID
              + " expected "
              + AudioVariableTypeNode.class.getName());
    }
    NodeId id26 = ServerNodeSupport.resolve(namespaceTable, CartesianCoordinatesType.TYPE_ID);
    VariableTypeManager.RegisteredVariableType registered26 =
        manager.getRegisteredType(id26).orElse(null);
    if (registered26 != null
        && (registered26.nodeClass() != CartesianCoordinatesTypeNode.class
            || registered26.nodeConstructor() != CONSTRUCTOR_26
            || registered26.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + CartesianCoordinatesType.TYPE_ID
              + " expected "
              + CartesianCoordinatesTypeNode.class.getName());
    }
    NodeId id27 = ServerNodeSupport.resolve(namespaceTable, ThreeDCartesianCoordinatesType.TYPE_ID);
    VariableTypeManager.RegisteredVariableType registered27 =
        manager.getRegisteredType(id27).orElse(null);
    if (registered27 != null
        && (registered27.nodeClass() != ThreeDCartesianCoordinatesTypeNode.class
            || registered27.nodeConstructor() != CONSTRUCTOR_27
            || registered27.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + ThreeDCartesianCoordinatesType.TYPE_ID
              + " expected "
              + ThreeDCartesianCoordinatesTypeNode.class.getName());
    }
    NodeId id28 = ServerNodeSupport.resolve(namespaceTable, OrientationType.TYPE_ID);
    VariableTypeManager.RegisteredVariableType registered28 =
        manager.getRegisteredType(id28).orElse(null);
    if (registered28 != null
        && (registered28.nodeClass() != OrientationTypeNode.class
            || registered28.nodeConstructor() != CONSTRUCTOR_28
            || registered28.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + OrientationType.TYPE_ID
              + " expected "
              + OrientationTypeNode.class.getName());
    }
    NodeId id29 = ServerNodeSupport.resolve(namespaceTable, ThreeDOrientationType.TYPE_ID);
    VariableTypeManager.RegisteredVariableType registered29 =
        manager.getRegisteredType(id29).orElse(null);
    if (registered29 != null
        && (registered29.nodeClass() != ThreeDOrientationTypeNode.class
            || registered29.nodeConstructor() != CONSTRUCTOR_29
            || registered29.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + ThreeDOrientationType.TYPE_ID
              + " expected "
              + ThreeDOrientationTypeNode.class.getName());
    }
    NodeId id30 = ServerNodeSupport.resolve(namespaceTable, FrameType.TYPE_ID);
    VariableTypeManager.RegisteredVariableType registered30 =
        manager.getRegisteredType(id30).orElse(null);
    if (registered30 != null
        && (registered30.nodeClass() != FrameTypeNode.class
            || registered30.nodeConstructor() != CONSTRUCTOR_30
            || registered30.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + FrameType.TYPE_ID
              + " expected "
              + FrameTypeNode.class.getName());
    }
    NodeId id31 = ServerNodeSupport.resolve(namespaceTable, ThreeDFrameType.TYPE_ID);
    VariableTypeManager.RegisteredVariableType registered31 =
        manager.getRegisteredType(id31).orElse(null);
    if (registered31 != null
        && (registered31.nodeClass() != ThreeDFrameTypeNode.class
            || registered31.nodeConstructor() != CONSTRUCTOR_31
            || registered31.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + ThreeDFrameType.TYPE_ID
              + " expected "
              + ThreeDFrameTypeNode.class.getName());
    }
    NodeId id32 =
        ServerNodeSupport.resolve(
            namespaceTable, MultiStateDictionaryEntryDiscreteBaseType.TYPE_ID);
    VariableTypeManager.RegisteredVariableType registered32 =
        manager.getRegisteredType(id32).orElse(null);
    if (registered32 != null
        && (registered32.nodeClass() != MultiStateDictionaryEntryDiscreteBaseTypeNode.class
            || registered32.nodeConstructor() != CONSTRUCTOR_32
            || registered32.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + MultiStateDictionaryEntryDiscreteBaseType.TYPE_ID
              + " expected "
              + MultiStateDictionaryEntryDiscreteBaseTypeNode.class.getName());
    }
    NodeId id33 =
        ServerNodeSupport.resolve(namespaceTable, MultiStateDictionaryEntryDiscreteType.TYPE_ID);
    VariableTypeManager.RegisteredVariableType registered33 =
        manager.getRegisteredType(id33).orElse(null);
    if (registered33 != null
        && (registered33.nodeClass() != MultiStateDictionaryEntryDiscreteTypeNode.class
            || registered33.nodeConstructor() != CONSTRUCTOR_33
            || registered33.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + MultiStateDictionaryEntryDiscreteType.TYPE_ID
              + " expected "
              + MultiStateDictionaryEntryDiscreteTypeNode.class.getName());
    }
    NodeId id34 = ServerNodeSupport.resolve(namespaceTable, PubSubDiagnosticsCounterType.TYPE_ID);
    VariableTypeManager.RegisteredVariableType registered34 =
        manager.getRegisteredType(id34).orElse(null);
    if (registered34 != null
        && (registered34.nodeClass() != PubSubDiagnosticsCounterTypeNode.class
            || registered34.nodeConstructor() != CONSTRUCTOR_34
            || registered34.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + PubSubDiagnosticsCounterType.TYPE_ID
              + " expected "
              + PubSubDiagnosticsCounterTypeNode.class.getName());
    }
    NodeId id35 = ServerNodeSupport.resolve(namespaceTable, ServerVendorCapabilityType.TYPE_ID);
    VariableTypeManager.RegisteredVariableType registered35 =
        manager.getRegisteredType(id35).orElse(null);
    if (registered35 != null
        && (registered35.nodeClass() != ServerVendorCapabilityTypeNode.class
            || registered35.nodeConstructor() != CONSTRUCTOR_35
            || registered35.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + ServerVendorCapabilityType.TYPE_ID
              + " expected "
              + ServerVendorCapabilityTypeNode.class.getName());
    }
    NodeId id36 = ServerNodeSupport.resolve(namespaceTable, ServerStatusType.TYPE_ID);
    VariableTypeManager.RegisteredVariableType registered36 =
        manager.getRegisteredType(id36).orElse(null);
    if (registered36 != null
        && (registered36.nodeClass() != ServerStatusTypeNode.class
            || registered36.nodeConstructor() != CONSTRUCTOR_36
            || registered36.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + ServerStatusType.TYPE_ID
              + " expected "
              + ServerStatusTypeNode.class.getName());
    }
    NodeId id37 = ServerNodeSupport.resolve(namespaceTable, ServerDiagnosticsSummaryType.TYPE_ID);
    VariableTypeManager.RegisteredVariableType registered37 =
        manager.getRegisteredType(id37).orElse(null);
    if (registered37 != null
        && (registered37.nodeClass() != ServerDiagnosticsSummaryTypeNode.class
            || registered37.nodeConstructor() != CONSTRUCTOR_37
            || registered37.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + ServerDiagnosticsSummaryType.TYPE_ID
              + " expected "
              + ServerDiagnosticsSummaryTypeNode.class.getName());
    }
    NodeId id38 =
        ServerNodeSupport.resolve(namespaceTable, SamplingIntervalDiagnosticsArrayType.TYPE_ID);
    VariableTypeManager.RegisteredVariableType registered38 =
        manager.getRegisteredType(id38).orElse(null);
    if (registered38 != null
        && (registered38.nodeClass() != SamplingIntervalDiagnosticsArrayTypeNode.class
            || registered38.nodeConstructor() != CONSTRUCTOR_38
            || registered38.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + SamplingIntervalDiagnosticsArrayType.TYPE_ID
              + " expected "
              + SamplingIntervalDiagnosticsArrayTypeNode.class.getName());
    }
    NodeId id39 =
        ServerNodeSupport.resolve(namespaceTable, SamplingIntervalDiagnosticsType.TYPE_ID);
    VariableTypeManager.RegisteredVariableType registered39 =
        manager.getRegisteredType(id39).orElse(null);
    if (registered39 != null
        && (registered39.nodeClass() != SamplingIntervalDiagnosticsTypeNode.class
            || registered39.nodeConstructor() != CONSTRUCTOR_39
            || registered39.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + SamplingIntervalDiagnosticsType.TYPE_ID
              + " expected "
              + SamplingIntervalDiagnosticsTypeNode.class.getName());
    }
    NodeId id40 =
        ServerNodeSupport.resolve(namespaceTable, SubscriptionDiagnosticsArrayType.TYPE_ID);
    VariableTypeManager.RegisteredVariableType registered40 =
        manager.getRegisteredType(id40).orElse(null);
    if (registered40 != null
        && (registered40.nodeClass() != SubscriptionDiagnosticsArrayTypeNode.class
            || registered40.nodeConstructor() != CONSTRUCTOR_40
            || registered40.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + SubscriptionDiagnosticsArrayType.TYPE_ID
              + " expected "
              + SubscriptionDiagnosticsArrayTypeNode.class.getName());
    }
    NodeId id41 = ServerNodeSupport.resolve(namespaceTable, SubscriptionDiagnosticsType.TYPE_ID);
    VariableTypeManager.RegisteredVariableType registered41 =
        manager.getRegisteredType(id41).orElse(null);
    if (registered41 != null
        && (registered41.nodeClass() != SubscriptionDiagnosticsTypeNode.class
            || registered41.nodeConstructor() != CONSTRUCTOR_41
            || registered41.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + SubscriptionDiagnosticsType.TYPE_ID
              + " expected "
              + SubscriptionDiagnosticsTypeNode.class.getName());
    }
    NodeId id42 = ServerNodeSupport.resolve(namespaceTable, SessionDiagnosticsArrayType.TYPE_ID);
    VariableTypeManager.RegisteredVariableType registered42 =
        manager.getRegisteredType(id42).orElse(null);
    if (registered42 != null
        && (registered42.nodeClass() != SessionDiagnosticsArrayTypeNode.class
            || registered42.nodeConstructor() != CONSTRUCTOR_42
            || registered42.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + SessionDiagnosticsArrayType.TYPE_ID
              + " expected "
              + SessionDiagnosticsArrayTypeNode.class.getName());
    }
    NodeId id43 = ServerNodeSupport.resolve(namespaceTable, SessionDiagnosticsVariableType.TYPE_ID);
    VariableTypeManager.RegisteredVariableType registered43 =
        manager.getRegisteredType(id43).orElse(null);
    if (registered43 != null
        && (registered43.nodeClass() != SessionDiagnosticsVariableTypeNode.class
            || registered43.nodeConstructor() != CONSTRUCTOR_43
            || registered43.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + SessionDiagnosticsVariableType.TYPE_ID
              + " expected "
              + SessionDiagnosticsVariableTypeNode.class.getName());
    }
    NodeId id44 =
        ServerNodeSupport.resolve(namespaceTable, SessionSecurityDiagnosticsArrayType.TYPE_ID);
    VariableTypeManager.RegisteredVariableType registered44 =
        manager.getRegisteredType(id44).orElse(null);
    if (registered44 != null
        && (registered44.nodeClass() != SessionSecurityDiagnosticsArrayTypeNode.class
            || registered44.nodeConstructor() != CONSTRUCTOR_44
            || registered44.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + SessionSecurityDiagnosticsArrayType.TYPE_ID
              + " expected "
              + SessionSecurityDiagnosticsArrayTypeNode.class.getName());
    }
    NodeId id45 = ServerNodeSupport.resolve(namespaceTable, SessionSecurityDiagnosticsType.TYPE_ID);
    VariableTypeManager.RegisteredVariableType registered45 =
        manager.getRegisteredType(id45).orElse(null);
    if (registered45 != null
        && (registered45.nodeClass() != SessionSecurityDiagnosticsTypeNode.class
            || registered45.nodeConstructor() != CONSTRUCTOR_45
            || registered45.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + SessionSecurityDiagnosticsType.TYPE_ID
              + " expected "
              + SessionSecurityDiagnosticsTypeNode.class.getName());
    }
    NodeId id46 = ServerNodeSupport.resolve(namespaceTable, TwoStateDiscreteType.TYPE_ID);
    VariableTypeManager.RegisteredVariableType registered46 =
        manager.getRegisteredType(id46).orElse(null);
    if (registered46 != null
        && (registered46.nodeClass() != TwoStateDiscreteTypeNode.class
            || registered46.nodeConstructor() != CONSTRUCTOR_46
            || registered46.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + TwoStateDiscreteType.TYPE_ID
              + " expected "
              + TwoStateDiscreteTypeNode.class.getName());
    }
    NodeId id47 = ServerNodeSupport.resolve(namespaceTable, MultiStateDiscreteType.TYPE_ID);
    VariableTypeManager.RegisteredVariableType registered47 =
        manager.getRegisteredType(id47).orElse(null);
    if (registered47 != null
        && (registered47.nodeClass() != MultiStateDiscreteTypeNode.class
            || registered47.nodeConstructor() != CONSTRUCTOR_47
            || registered47.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + MultiStateDiscreteType.TYPE_ID
              + " expected "
              + MultiStateDiscreteTypeNode.class.getName());
    }
    NodeId id48 = ServerNodeSupport.resolve(namespaceTable, ProgramDiagnosticType.TYPE_ID);
    VariableTypeManager.RegisteredVariableType registered48 =
        manager.getRegisteredType(id48).orElse(null);
    if (registered48 != null
        && (registered48.nodeClass() != ProgramDiagnosticTypeNode.class
            || registered48.nodeConstructor() != CONSTRUCTOR_48
            || registered48.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + ProgramDiagnosticType.TYPE_ID
              + " expected "
              + ProgramDiagnosticTypeNode.class.getName());
    }
    NodeId id49 = ServerNodeSupport.resolve(namespaceTable, AnalogNumberItemType.TYPE_ID);
    VariableTypeManager.RegisteredVariableType registered49 =
        manager.getRegisteredType(id49).orElse(null);
    if (registered49 != null
        && (registered49.nodeClass() != AnalogNumberItemTypeNode.class
            || registered49.nodeConstructor() != CONSTRUCTOR_49
            || registered49.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + AnalogNumberItemType.TYPE_ID
              + " expected "
              + AnalogNumberItemTypeNode.class.getName());
    }
    NodeId id50 = ServerNodeSupport.resolve(namespaceTable, AnalogNumberUnitRangeType.TYPE_ID);
    VariableTypeManager.RegisteredVariableType registered50 =
        manager.getRegisteredType(id50).orElse(null);
    if (registered50 != null
        && (registered50.nodeClass() != AnalogNumberUnitRangeTypeNode.class
            || registered50.nodeConstructor() != CONSTRUCTOR_50
            || registered50.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + AnalogNumberUnitRangeType.TYPE_ID
              + " expected "
              + AnalogNumberUnitRangeTypeNode.class.getName());
    }
    NodeId id51 = ServerNodeSupport.resolve(namespaceTable, StateVariableType.TYPE_ID);
    VariableTypeManager.RegisteredVariableType registered51 =
        manager.getRegisteredType(id51).orElse(null);
    if (registered51 != null
        && (registered51.nodeClass() != StateVariableTypeNode.class
            || registered51.nodeConstructor() != CONSTRUCTOR_51
            || registered51.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + StateVariableType.TYPE_ID
              + " expected "
              + StateVariableTypeNode.class.getName());
    }
    NodeId id52 = ServerNodeSupport.resolve(namespaceTable, FiniteStateVariableType.TYPE_ID);
    VariableTypeManager.RegisteredVariableType registered52 =
        manager.getRegisteredType(id52).orElse(null);
    if (registered52 != null
        && (registered52.nodeClass() != FiniteStateVariableTypeNode.class
            || registered52.nodeConstructor() != CONSTRUCTOR_52
            || registered52.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + FiniteStateVariableType.TYPE_ID
              + " expected "
              + FiniteStateVariableTypeNode.class.getName());
    }
    NodeId id53 = ServerNodeSupport.resolve(namespaceTable, TransitionVariableType.TYPE_ID);
    VariableTypeManager.RegisteredVariableType registered53 =
        manager.getRegisteredType(id53).orElse(null);
    if (registered53 != null
        && (registered53.nodeClass() != TransitionVariableTypeNode.class
            || registered53.nodeConstructor() != CONSTRUCTOR_53
            || registered53.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + TransitionVariableType.TYPE_ID
              + " expected "
              + TransitionVariableTypeNode.class.getName());
    }
    NodeId id54 = ServerNodeSupport.resolve(namespaceTable, FiniteTransitionVariableType.TYPE_ID);
    VariableTypeManager.RegisteredVariableType registered54 =
        manager.getRegisteredType(id54).orElse(null);
    if (registered54 != null
        && (registered54.nodeClass() != FiniteTransitionVariableTypeNode.class
            || registered54.nodeConstructor() != CONSTRUCTOR_54
            || registered54.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + FiniteTransitionVariableType.TYPE_ID
              + " expected "
              + FiniteTransitionVariableTypeNode.class.getName());
    }
    NodeId id55 = ServerNodeSupport.resolve(namespaceTable, BuildInfoType.TYPE_ID);
    VariableTypeManager.RegisteredVariableType registered55 =
        manager.getRegisteredType(id55).orElse(null);
    if (registered55 != null
        && (registered55.nodeClass() != BuildInfoTypeNode.class
            || registered55.nodeConstructor() != CONSTRUCTOR_55
            || registered55.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + BuildInfoType.TYPE_ID
              + " expected "
              + BuildInfoTypeNode.class.getName());
    }
    NodeId id56 = ServerNodeSupport.resolve(namespaceTable, AlarmStateVariableType.TYPE_ID);
    VariableTypeManager.RegisteredVariableType registered56 =
        manager.getRegisteredType(id56).orElse(null);
    if (registered56 != null
        && (registered56.nodeClass() != AlarmStateVariableTypeNode.class
            || registered56.nodeConstructor() != CONSTRUCTOR_56
            || registered56.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + AlarmStateVariableType.TYPE_ID
              + " expected "
              + AlarmStateVariableTypeNode.class.getName());
    }
    NodeId id57 = ServerNodeSupport.resolve(namespaceTable, BitFieldType.TYPE_ID);
    VariableTypeManager.RegisteredVariableType registered57 =
        manager.getRegisteredType(id57).orElse(null);
    if (registered57 != null
        && (registered57.nodeClass() != BitFieldTypeNode.class
            || registered57.nodeConstructor() != CONSTRUCTOR_57
            || registered57.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + BitFieldType.TYPE_ID
              + " expected "
              + BitFieldTypeNode.class.getName());
    }
    NodeId id58 =
        ServerNodeSupport.resolve(namespaceTable, ReferenceDescriptionVariableType.TYPE_ID);
    VariableTypeManager.RegisteredVariableType registered58 =
        manager.getRegisteredType(id58).orElse(null);
    if (registered58 != null
        && (registered58.nodeClass() != ReferenceDescriptionVariableTypeNode.class
            || registered58.nodeConstructor() != CONSTRUCTOR_58
            || registered58.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + ReferenceDescriptionVariableType.TYPE_ID
              + " expected "
              + ReferenceDescriptionVariableTypeNode.class.getName());
    }
    NodeId id59 = ServerNodeSupport.resolve(namespaceTable, PropertyType.TYPE_ID);
    VariableTypeManager.RegisteredVariableType registered59 =
        manager.getRegisteredType(id59).orElse(null);
    if (registered59 != null
        && (registered59.nodeClass() != PropertyTypeNode.class
            || registered59.nodeConstructor() != CONSTRUCTOR_59
            || registered59.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + PropertyType.TYPE_ID
              + " expected "
              + PropertyTypeNode.class.getName());
    }
    NodeId id60 = ServerNodeSupport.resolve(namespaceTable, DataTypeDescriptionType.TYPE_ID);
    VariableTypeManager.RegisteredVariableType registered60 =
        manager.getRegisteredType(id60).orElse(null);
    if (registered60 != null
        && (registered60.nodeClass() != DataTypeDescriptionTypeNode.class
            || registered60.nodeConstructor() != CONSTRUCTOR_60
            || registered60.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + DataTypeDescriptionType.TYPE_ID
              + " expected "
              + DataTypeDescriptionTypeNode.class.getName());
    }
    NodeId id61 = ServerNodeSupport.resolve(namespaceTable, DataTypeDictionaryType.TYPE_ID);
    VariableTypeManager.RegisteredVariableType registered61 =
        manager.getRegisteredType(id61).orElse(null);
    if (registered61 != null
        && (registered61.nodeClass() != DataTypeDictionaryTypeNode.class
            || registered61.nodeConstructor() != CONSTRUCTOR_61
            || registered61.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + DataTypeDictionaryType.TYPE_ID
              + " expected "
              + DataTypeDictionaryTypeNode.class.getName());
    }
    NodeId id62 = ServerNodeSupport.resolve(namespaceTable, TwoStateVariableType.TYPE_ID);
    VariableTypeManager.RegisteredVariableType registered62 =
        manager.getRegisteredType(id62).orElse(null);
    if (registered62 != null
        && (registered62.nodeClass() != TwoStateVariableTypeNode.class
            || registered62.nodeConstructor() != CONSTRUCTOR_62
            || registered62.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + TwoStateVariableType.TYPE_ID
              + " expected "
              + TwoStateVariableTypeNode.class.getName());
    }
    NodeId id63 = ServerNodeSupport.resolve(namespaceTable, ConditionVariableType.TYPE_ID);
    VariableTypeManager.RegisteredVariableType registered63 =
        manager.getRegisteredType(id63).orElse(null);
    if (registered63 != null
        && (registered63.nodeClass() != ConditionVariableTypeNode.class
            || registered63.nodeConstructor() != CONSTRUCTOR_63
            || registered63.snapshotConstructor() != null)) {
      throw new IllegalStateException(
          "constructor registration conflict for "
              + ConditionVariableType.TYPE_ID
              + " expected "
              + ConditionVariableTypeNode.class.getName());
    }
    if (registered0 == null) {
      manager.registerVariableType(id0, BaseVariableTypeNode.class, CONSTRUCTOR_0);
    }
    if (registered1 == null) {
      manager.registerVariableType(id1, BaseDataVariableTypeNode.class, CONSTRUCTOR_1);
    }
    if (registered2 == null) {
      manager.registerVariableType(id2, DataItemTypeNode.class, CONSTRUCTOR_2);
    }
    if (registered3 == null) {
      manager.registerVariableType(id3, DiscreteItemTypeNode.class, CONSTRUCTOR_3);
    }
    if (registered4 == null) {
      manager.registerVariableType(id4, MultiStateValueDiscreteTypeNode.class, CONSTRUCTOR_4);
    }
    if (registered5 == null) {
      manager.registerVariableType(id5, OptionSetTypeNode.class, CONSTRUCTOR_5);
    }
    if (registered6 == null) {
      manager.registerVariableType(id6, ArrayItemTypeNode.class, CONSTRUCTOR_6);
    }
    if (registered7 == null) {
      manager.registerVariableType(id7, YArrayItemTypeNode.class, CONSTRUCTOR_7);
    }
    if (registered8 == null) {
      manager.registerVariableType(id8, XYArrayItemTypeNode.class, CONSTRUCTOR_8);
    }
    if (registered9 == null) {
      manager.registerVariableType(id9, ImageItemTypeNode.class, CONSTRUCTOR_9);
    }
    if (registered10 == null) {
      manager.registerVariableType(id10, CubeItemTypeNode.class, CONSTRUCTOR_10);
    }
    if (registered11 == null) {
      manager.registerVariableType(id11, NDimensionArrayItemTypeNode.class, CONSTRUCTOR_11);
    }
    if (registered12 == null) {
      manager.registerVariableType(id12, GuardVariableTypeNode.class, CONSTRUCTOR_12);
    }
    if (registered13 == null) {
      manager.registerVariableType(id13, ExpressionGuardVariableTypeNode.class, CONSTRUCTOR_13);
    }
    if (registered14 == null) {
      manager.registerVariableType(id14, ElseGuardVariableTypeNode.class, CONSTRUCTOR_14);
    }
    if (registered15 == null) {
      manager.registerVariableType(id15, BaseAnalogTypeNode.class, CONSTRUCTOR_15);
    }
    if (registered16 == null) {
      manager.registerVariableType(id16, ProgramDiagnostic2TypeNode.class, CONSTRUCTOR_16);
    }
    if (registered17 == null) {
      manager.registerVariableType(id17, SelectionListTypeNode.class, CONSTRUCTOR_17);
    }
    if (registered18 == null) {
      manager.registerVariableType(id18, AlarmRateVariableTypeNode.class, CONSTRUCTOR_18);
    }
    if (registered19 == null) {
      manager.registerVariableType(id19, AnalogUnitTypeNode.class, CONSTRUCTOR_19);
    }
    if (registered20 == null) {
      manager.registerVariableType(id20, AnalogItemTypeNode.class, CONSTRUCTOR_20);
    }
    if (registered21 == null) {
      manager.registerVariableType(id21, AnalogUnitRangeTypeNode.class, CONSTRUCTOR_21);
    }
    if (registered22 == null) {
      manager.registerVariableType(id22, RationalNumberTypeNode.class, CONSTRUCTOR_22);
    }
    if (registered23 == null) {
      manager.registerVariableType(id23, VectorTypeNode.class, CONSTRUCTOR_23);
    }
    if (registered24 == null) {
      manager.registerVariableType(id24, ThreeDVectorTypeNode.class, CONSTRUCTOR_24);
    }
    if (registered25 == null) {
      manager.registerVariableType(id25, AudioVariableTypeNode.class, CONSTRUCTOR_25);
    }
    if (registered26 == null) {
      manager.registerVariableType(id26, CartesianCoordinatesTypeNode.class, CONSTRUCTOR_26);
    }
    if (registered27 == null) {
      manager.registerVariableType(id27, ThreeDCartesianCoordinatesTypeNode.class, CONSTRUCTOR_27);
    }
    if (registered28 == null) {
      manager.registerVariableType(id28, OrientationTypeNode.class, CONSTRUCTOR_28);
    }
    if (registered29 == null) {
      manager.registerVariableType(id29, ThreeDOrientationTypeNode.class, CONSTRUCTOR_29);
    }
    if (registered30 == null) {
      manager.registerVariableType(id30, FrameTypeNode.class, CONSTRUCTOR_30);
    }
    if (registered31 == null) {
      manager.registerVariableType(id31, ThreeDFrameTypeNode.class, CONSTRUCTOR_31);
    }
    if (registered32 == null) {
      manager.registerVariableType(
          id32, MultiStateDictionaryEntryDiscreteBaseTypeNode.class, CONSTRUCTOR_32);
    }
    if (registered33 == null) {
      manager.registerVariableType(
          id33, MultiStateDictionaryEntryDiscreteTypeNode.class, CONSTRUCTOR_33);
    }
    if (registered34 == null) {
      manager.registerVariableType(id34, PubSubDiagnosticsCounterTypeNode.class, CONSTRUCTOR_34);
    }
    if (registered35 == null) {
      manager.registerVariableType(id35, ServerVendorCapabilityTypeNode.class, CONSTRUCTOR_35);
    }
    if (registered36 == null) {
      manager.registerVariableType(id36, ServerStatusTypeNode.class, CONSTRUCTOR_36);
    }
    if (registered37 == null) {
      manager.registerVariableType(id37, ServerDiagnosticsSummaryTypeNode.class, CONSTRUCTOR_37);
    }
    if (registered38 == null) {
      manager.registerVariableType(
          id38, SamplingIntervalDiagnosticsArrayTypeNode.class, CONSTRUCTOR_38);
    }
    if (registered39 == null) {
      manager.registerVariableType(id39, SamplingIntervalDiagnosticsTypeNode.class, CONSTRUCTOR_39);
    }
    if (registered40 == null) {
      manager.registerVariableType(
          id40, SubscriptionDiagnosticsArrayTypeNode.class, CONSTRUCTOR_40);
    }
    if (registered41 == null) {
      manager.registerVariableType(id41, SubscriptionDiagnosticsTypeNode.class, CONSTRUCTOR_41);
    }
    if (registered42 == null) {
      manager.registerVariableType(id42, SessionDiagnosticsArrayTypeNode.class, CONSTRUCTOR_42);
    }
    if (registered43 == null) {
      manager.registerVariableType(id43, SessionDiagnosticsVariableTypeNode.class, CONSTRUCTOR_43);
    }
    if (registered44 == null) {
      manager.registerVariableType(
          id44, SessionSecurityDiagnosticsArrayTypeNode.class, CONSTRUCTOR_44);
    }
    if (registered45 == null) {
      manager.registerVariableType(id45, SessionSecurityDiagnosticsTypeNode.class, CONSTRUCTOR_45);
    }
    if (registered46 == null) {
      manager.registerVariableType(id46, TwoStateDiscreteTypeNode.class, CONSTRUCTOR_46);
    }
    if (registered47 == null) {
      manager.registerVariableType(id47, MultiStateDiscreteTypeNode.class, CONSTRUCTOR_47);
    }
    if (registered48 == null) {
      manager.registerVariableType(id48, ProgramDiagnosticTypeNode.class, CONSTRUCTOR_48);
    }
    if (registered49 == null) {
      manager.registerVariableType(id49, AnalogNumberItemTypeNode.class, CONSTRUCTOR_49);
    }
    if (registered50 == null) {
      manager.registerVariableType(id50, AnalogNumberUnitRangeTypeNode.class, CONSTRUCTOR_50);
    }
    if (registered51 == null) {
      manager.registerVariableType(id51, StateVariableTypeNode.class, CONSTRUCTOR_51);
    }
    if (registered52 == null) {
      manager.registerVariableType(id52, FiniteStateVariableTypeNode.class, CONSTRUCTOR_52);
    }
    if (registered53 == null) {
      manager.registerVariableType(id53, TransitionVariableTypeNode.class, CONSTRUCTOR_53);
    }
    if (registered54 == null) {
      manager.registerVariableType(id54, FiniteTransitionVariableTypeNode.class, CONSTRUCTOR_54);
    }
    if (registered55 == null) {
      manager.registerVariableType(id55, BuildInfoTypeNode.class, CONSTRUCTOR_55);
    }
    if (registered56 == null) {
      manager.registerVariableType(id56, AlarmStateVariableTypeNode.class, CONSTRUCTOR_56);
    }
    if (registered57 == null) {
      manager.registerVariableType(id57, BitFieldTypeNode.class, CONSTRUCTOR_57);
    }
    if (registered58 == null) {
      manager.registerVariableType(
          id58, ReferenceDescriptionVariableTypeNode.class, CONSTRUCTOR_58);
    }
    if (registered59 == null) {
      manager.registerVariableType(id59, PropertyTypeNode.class, CONSTRUCTOR_59);
    }
    if (registered60 == null) {
      manager.registerVariableType(id60, DataTypeDescriptionTypeNode.class, CONSTRUCTOR_60);
    }
    if (registered61 == null) {
      manager.registerVariableType(id61, DataTypeDictionaryTypeNode.class, CONSTRUCTOR_61);
    }
    if (registered62 == null) {
      manager.registerVariableType(id62, TwoStateVariableTypeNode.class, CONSTRUCTOR_62);
    }
    if (registered63 == null) {
      manager.registerVariableType(id63, ConditionVariableTypeNode.class, CONSTRUCTOR_63);
    }
  }
}
