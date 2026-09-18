package org.eclipse.milo.opcua.sdk.client.model;

import org.eclipse.milo.opcua.sdk.client.VariableTypeManager;
import org.eclipse.milo.opcua.sdk.client.model.variables.AlarmRateVariableType;
import org.eclipse.milo.opcua.sdk.client.model.variables.AlarmRateVariableTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.variables.AlarmStateVariableType;
import org.eclipse.milo.opcua.sdk.client.model.variables.AlarmStateVariableTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.variables.AnalogItemType;
import org.eclipse.milo.opcua.sdk.client.model.variables.AnalogItemTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.variables.AnalogNumberItemType;
import org.eclipse.milo.opcua.sdk.client.model.variables.AnalogNumberItemTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.variables.AnalogNumberUnitRangeType;
import org.eclipse.milo.opcua.sdk.client.model.variables.AnalogNumberUnitRangeTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.variables.AnalogUnitRangeType;
import org.eclipse.milo.opcua.sdk.client.model.variables.AnalogUnitRangeTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.variables.AnalogUnitType;
import org.eclipse.milo.opcua.sdk.client.model.variables.AnalogUnitTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.variables.ArrayItemType;
import org.eclipse.milo.opcua.sdk.client.model.variables.ArrayItemTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.variables.AudioVariableType;
import org.eclipse.milo.opcua.sdk.client.model.variables.AudioVariableTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.variables.BaseAnalogType;
import org.eclipse.milo.opcua.sdk.client.model.variables.BaseAnalogTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.variables.BitFieldType;
import org.eclipse.milo.opcua.sdk.client.model.variables.BitFieldTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.variables.BuildInfoType;
import org.eclipse.milo.opcua.sdk.client.model.variables.BuildInfoTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.variables.CartesianCoordinatesType;
import org.eclipse.milo.opcua.sdk.client.model.variables.CartesianCoordinatesTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.variables.ConditionVariableType;
import org.eclipse.milo.opcua.sdk.client.model.variables.ConditionVariableTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.variables.CubeItemType;
import org.eclipse.milo.opcua.sdk.client.model.variables.CubeItemTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.variables.DataItemType;
import org.eclipse.milo.opcua.sdk.client.model.variables.DataItemTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.variables.DataTypeDescriptionType;
import org.eclipse.milo.opcua.sdk.client.model.variables.DataTypeDescriptionTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.variables.DataTypeDictionaryType;
import org.eclipse.milo.opcua.sdk.client.model.variables.DataTypeDictionaryTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.variables.DiscreteItemType;
import org.eclipse.milo.opcua.sdk.client.model.variables.DiscreteItemTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.variables.ElseGuardVariableType;
import org.eclipse.milo.opcua.sdk.client.model.variables.ElseGuardVariableTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.variables.ExpressionGuardVariableType;
import org.eclipse.milo.opcua.sdk.client.model.variables.ExpressionGuardVariableTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.variables.FiniteStateVariableType;
import org.eclipse.milo.opcua.sdk.client.model.variables.FiniteStateVariableTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.variables.FiniteTransitionVariableType;
import org.eclipse.milo.opcua.sdk.client.model.variables.FiniteTransitionVariableTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.variables.FrameType;
import org.eclipse.milo.opcua.sdk.client.model.variables.FrameTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.variables.GuardVariableType;
import org.eclipse.milo.opcua.sdk.client.model.variables.GuardVariableTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.variables.ImageItemType;
import org.eclipse.milo.opcua.sdk.client.model.variables.ImageItemTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.variables.MultiStateDictionaryEntryDiscreteBaseType;
import org.eclipse.milo.opcua.sdk.client.model.variables.MultiStateDictionaryEntryDiscreteBaseTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.variables.MultiStateDictionaryEntryDiscreteType;
import org.eclipse.milo.opcua.sdk.client.model.variables.MultiStateDictionaryEntryDiscreteTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.variables.MultiStateDiscreteType;
import org.eclipse.milo.opcua.sdk.client.model.variables.MultiStateDiscreteTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.variables.MultiStateValueDiscreteType;
import org.eclipse.milo.opcua.sdk.client.model.variables.MultiStateValueDiscreteTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.variables.NDimensionArrayItemType;
import org.eclipse.milo.opcua.sdk.client.model.variables.NDimensionArrayItemTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.variables.OptionSetType;
import org.eclipse.milo.opcua.sdk.client.model.variables.OptionSetTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.variables.OrientationType;
import org.eclipse.milo.opcua.sdk.client.model.variables.OrientationTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.variables.ProgramDiagnostic2Type;
import org.eclipse.milo.opcua.sdk.client.model.variables.ProgramDiagnostic2TypeNode;
import org.eclipse.milo.opcua.sdk.client.model.variables.ProgramDiagnosticType;
import org.eclipse.milo.opcua.sdk.client.model.variables.ProgramDiagnosticTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.variables.PubSubDiagnosticsCounterType;
import org.eclipse.milo.opcua.sdk.client.model.variables.PubSubDiagnosticsCounterTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.variables.RationalNumberType;
import org.eclipse.milo.opcua.sdk.client.model.variables.RationalNumberTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.variables.ReferenceDescriptionVariableType;
import org.eclipse.milo.opcua.sdk.client.model.variables.ReferenceDescriptionVariableTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.variables.SamplingIntervalDiagnosticsArrayType;
import org.eclipse.milo.opcua.sdk.client.model.variables.SamplingIntervalDiagnosticsArrayTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.variables.SamplingIntervalDiagnosticsType;
import org.eclipse.milo.opcua.sdk.client.model.variables.SamplingIntervalDiagnosticsTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.variables.SelectionListType;
import org.eclipse.milo.opcua.sdk.client.model.variables.SelectionListTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.variables.ServerDiagnosticsSummaryType;
import org.eclipse.milo.opcua.sdk.client.model.variables.ServerDiagnosticsSummaryTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.variables.ServerStatusType;
import org.eclipse.milo.opcua.sdk.client.model.variables.ServerStatusTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.variables.ServerVendorCapabilityType;
import org.eclipse.milo.opcua.sdk.client.model.variables.ServerVendorCapabilityTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.variables.SessionDiagnosticsArrayType;
import org.eclipse.milo.opcua.sdk.client.model.variables.SessionDiagnosticsArrayTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.variables.SessionDiagnosticsVariableType;
import org.eclipse.milo.opcua.sdk.client.model.variables.SessionDiagnosticsVariableTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.variables.SessionSecurityDiagnosticsArrayType;
import org.eclipse.milo.opcua.sdk.client.model.variables.SessionSecurityDiagnosticsArrayTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.variables.SessionSecurityDiagnosticsType;
import org.eclipse.milo.opcua.sdk.client.model.variables.SessionSecurityDiagnosticsTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.variables.StateVariableType;
import org.eclipse.milo.opcua.sdk.client.model.variables.StateVariableTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.variables.SubscriptionDiagnosticsArrayType;
import org.eclipse.milo.opcua.sdk.client.model.variables.SubscriptionDiagnosticsArrayTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.variables.SubscriptionDiagnosticsType;
import org.eclipse.milo.opcua.sdk.client.model.variables.SubscriptionDiagnosticsTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.variables.ThreeDCartesianCoordinatesType;
import org.eclipse.milo.opcua.sdk.client.model.variables.ThreeDCartesianCoordinatesTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.variables.ThreeDFrameType;
import org.eclipse.milo.opcua.sdk.client.model.variables.ThreeDFrameTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.variables.ThreeDOrientationType;
import org.eclipse.milo.opcua.sdk.client.model.variables.ThreeDOrientationTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.variables.ThreeDVectorType;
import org.eclipse.milo.opcua.sdk.client.model.variables.ThreeDVectorTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.variables.TransitionVariableType;
import org.eclipse.milo.opcua.sdk.client.model.variables.TransitionVariableTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.variables.TwoStateDiscreteType;
import org.eclipse.milo.opcua.sdk.client.model.variables.TwoStateDiscreteTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.variables.TwoStateVariableType;
import org.eclipse.milo.opcua.sdk.client.model.variables.TwoStateVariableTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.variables.VectorType;
import org.eclipse.milo.opcua.sdk.client.model.variables.VectorTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.variables.XYArrayItemType;
import org.eclipse.milo.opcua.sdk.client.model.variables.XYArrayItemTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.variables.YArrayItemType;
import org.eclipse.milo.opcua.sdk.client.model.variables.YArrayItemTypeNode;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;

/**
 * Registers constructors for this client's lifetime. Serialize initialization after connecting and
 * before obtaining nodes. Existing cached instances are not upgraded. Registers this library's
 * namespace-zero constructors; initialize before any other standard registration.
 */
public final class VariableTypeInitializer {
  private static final VariableTypeManager.VariableNodeConstructor CONSTRUCTOR_0 =
      DataItemTypeNode::new;

  private static final VariableTypeManager.VariableNodeConstructor CONSTRUCTOR_1 =
      DiscreteItemTypeNode::new;

  private static final VariableTypeManager.VariableNodeConstructor CONSTRUCTOR_2 =
      MultiStateValueDiscreteTypeNode::new;

  private static final VariableTypeManager.VariableNodeConstructor CONSTRUCTOR_3 =
      OptionSetTypeNode::new;

  private static final VariableTypeManager.VariableNodeConstructor CONSTRUCTOR_4 =
      ArrayItemTypeNode::new;

  private static final VariableTypeManager.VariableNodeConstructor CONSTRUCTOR_5 =
      YArrayItemTypeNode::new;

  private static final VariableTypeManager.VariableNodeConstructor CONSTRUCTOR_6 =
      XYArrayItemTypeNode::new;

  private static final VariableTypeManager.VariableNodeConstructor CONSTRUCTOR_7 =
      ImageItemTypeNode::new;

  private static final VariableTypeManager.VariableNodeConstructor CONSTRUCTOR_8 =
      CubeItemTypeNode::new;

  private static final VariableTypeManager.VariableNodeConstructor CONSTRUCTOR_9 =
      NDimensionArrayItemTypeNode::new;

  private static final VariableTypeManager.VariableNodeConstructor CONSTRUCTOR_10 =
      GuardVariableTypeNode::new;

  private static final VariableTypeManager.VariableNodeConstructor CONSTRUCTOR_11 =
      ExpressionGuardVariableTypeNode::new;

  private static final VariableTypeManager.VariableNodeConstructor CONSTRUCTOR_12 =
      ElseGuardVariableTypeNode::new;

  private static final VariableTypeManager.VariableNodeConstructor CONSTRUCTOR_13 =
      BaseAnalogTypeNode::new;

  private static final VariableTypeManager.VariableNodeConstructor CONSTRUCTOR_14 =
      ProgramDiagnostic2TypeNode::new;

  private static final VariableTypeManager.VariableNodeConstructor CONSTRUCTOR_15 =
      SelectionListTypeNode::new;

  private static final VariableTypeManager.VariableNodeConstructor CONSTRUCTOR_16 =
      AlarmRateVariableTypeNode::new;

  private static final VariableTypeManager.VariableNodeConstructor CONSTRUCTOR_17 =
      AnalogUnitTypeNode::new;

  private static final VariableTypeManager.VariableNodeConstructor CONSTRUCTOR_18 =
      AnalogItemTypeNode::new;

  private static final VariableTypeManager.VariableNodeConstructor CONSTRUCTOR_19 =
      AnalogUnitRangeTypeNode::new;

  private static final VariableTypeManager.VariableNodeConstructor CONSTRUCTOR_20 =
      RationalNumberTypeNode::new;

  private static final VariableTypeManager.VariableNodeConstructor CONSTRUCTOR_21 =
      VectorTypeNode::new;

  private static final VariableTypeManager.VariableNodeConstructor CONSTRUCTOR_22 =
      ThreeDVectorTypeNode::new;

  private static final VariableTypeManager.VariableNodeConstructor CONSTRUCTOR_23 =
      AudioVariableTypeNode::new;

  private static final VariableTypeManager.VariableNodeConstructor CONSTRUCTOR_24 =
      CartesianCoordinatesTypeNode::new;

  private static final VariableTypeManager.VariableNodeConstructor CONSTRUCTOR_25 =
      ThreeDCartesianCoordinatesTypeNode::new;

  private static final VariableTypeManager.VariableNodeConstructor CONSTRUCTOR_26 =
      OrientationTypeNode::new;

  private static final VariableTypeManager.VariableNodeConstructor CONSTRUCTOR_27 =
      ThreeDOrientationTypeNode::new;

  private static final VariableTypeManager.VariableNodeConstructor CONSTRUCTOR_28 =
      FrameTypeNode::new;

  private static final VariableTypeManager.VariableNodeConstructor CONSTRUCTOR_29 =
      ThreeDFrameTypeNode::new;

  private static final VariableTypeManager.VariableNodeConstructor CONSTRUCTOR_30 =
      MultiStateDictionaryEntryDiscreteBaseTypeNode::new;

  private static final VariableTypeManager.VariableNodeConstructor CONSTRUCTOR_31 =
      MultiStateDictionaryEntryDiscreteTypeNode::new;

  private static final VariableTypeManager.VariableNodeConstructor CONSTRUCTOR_32 =
      PubSubDiagnosticsCounterTypeNode::new;

  private static final VariableTypeManager.VariableNodeConstructor CONSTRUCTOR_33 =
      ServerVendorCapabilityTypeNode::new;

  private static final VariableTypeManager.VariableNodeConstructor CONSTRUCTOR_34 =
      ServerStatusTypeNode::new;

  private static final VariableTypeManager.VariableNodeConstructor CONSTRUCTOR_35 =
      ServerDiagnosticsSummaryTypeNode::new;

  private static final VariableTypeManager.VariableNodeConstructor CONSTRUCTOR_36 =
      SamplingIntervalDiagnosticsArrayTypeNode::new;

  private static final VariableTypeManager.VariableNodeConstructor CONSTRUCTOR_37 =
      SamplingIntervalDiagnosticsTypeNode::new;

  private static final VariableTypeManager.VariableNodeConstructor CONSTRUCTOR_38 =
      SubscriptionDiagnosticsArrayTypeNode::new;

  private static final VariableTypeManager.VariableNodeConstructor CONSTRUCTOR_39 =
      SubscriptionDiagnosticsTypeNode::new;

  private static final VariableTypeManager.VariableNodeConstructor CONSTRUCTOR_40 =
      SessionDiagnosticsArrayTypeNode::new;

  private static final VariableTypeManager.VariableNodeConstructor CONSTRUCTOR_41 =
      SessionDiagnosticsVariableTypeNode::new;

  private static final VariableTypeManager.VariableNodeConstructor CONSTRUCTOR_42 =
      SessionSecurityDiagnosticsArrayTypeNode::new;

  private static final VariableTypeManager.VariableNodeConstructor CONSTRUCTOR_43 =
      SessionSecurityDiagnosticsTypeNode::new;

  private static final VariableTypeManager.VariableNodeConstructor CONSTRUCTOR_44 =
      TwoStateDiscreteTypeNode::new;

  private static final VariableTypeManager.VariableNodeConstructor CONSTRUCTOR_45 =
      MultiStateDiscreteTypeNode::new;

  private static final VariableTypeManager.VariableNodeConstructor CONSTRUCTOR_46 =
      ProgramDiagnosticTypeNode::new;

  private static final VariableTypeManager.VariableNodeConstructor CONSTRUCTOR_47 =
      AnalogNumberItemTypeNode::new;

  private static final VariableTypeManager.VariableNodeConstructor CONSTRUCTOR_48 =
      AnalogNumberUnitRangeTypeNode::new;

  private static final VariableTypeManager.VariableNodeConstructor CONSTRUCTOR_49 =
      StateVariableTypeNode::new;

  private static final VariableTypeManager.VariableNodeConstructor CONSTRUCTOR_50 =
      FiniteStateVariableTypeNode::new;

  private static final VariableTypeManager.VariableNodeConstructor CONSTRUCTOR_51 =
      TransitionVariableTypeNode::new;

  private static final VariableTypeManager.VariableNodeConstructor CONSTRUCTOR_52 =
      FiniteTransitionVariableTypeNode::new;

  private static final VariableTypeManager.VariableNodeConstructor CONSTRUCTOR_53 =
      BuildInfoTypeNode::new;

  private static final VariableTypeManager.VariableNodeConstructor CONSTRUCTOR_54 =
      AlarmStateVariableTypeNode::new;

  private static final VariableTypeManager.VariableNodeConstructor CONSTRUCTOR_55 =
      BitFieldTypeNode::new;

  private static final VariableTypeManager.VariableNodeConstructor CONSTRUCTOR_56 =
      ReferenceDescriptionVariableTypeNode::new;

  private static final VariableTypeManager.VariableNodeConstructor CONSTRUCTOR_57 =
      PropertyTypeNode::new;

  private static final VariableTypeManager.VariableNodeConstructor CONSTRUCTOR_58 =
      DataTypeDescriptionTypeNode::new;

  private static final VariableTypeManager.VariableNodeConstructor CONSTRUCTOR_59 =
      DataTypeDictionaryTypeNode::new;

  private static final VariableTypeManager.VariableNodeConstructor CONSTRUCTOR_60 =
      TwoStateVariableTypeNode::new;

  private static final VariableTypeManager.VariableNodeConstructor CONSTRUCTOR_61 =
      ConditionVariableTypeNode::new;

  private VariableTypeInitializer() {}

  private static void check0(NamespaceTable table, VariableTypeManager manager, NodeId[] ids) {
    ids[0] = ClientNodeSupport.resolve(table, DataItemType.TYPE_ID);
    if (manager.getNodeConstructor(ids[0]).filter(c -> c != CONSTRUCTOR_0).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + DataItemType.TYPE_ID);
    }
    ids[1] = ClientNodeSupport.resolve(table, DiscreteItemType.TYPE_ID);
    if (manager.getNodeConstructor(ids[1]).filter(c -> c != CONSTRUCTOR_1).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + DiscreteItemType.TYPE_ID);
    }
    ids[2] = ClientNodeSupport.resolve(table, MultiStateValueDiscreteType.TYPE_ID);
    if (manager.getNodeConstructor(ids[2]).filter(c -> c != CONSTRUCTOR_2).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + MultiStateValueDiscreteType.TYPE_ID);
    }
    ids[3] = ClientNodeSupport.resolve(table, OptionSetType.TYPE_ID);
    if (manager.getNodeConstructor(ids[3]).filter(c -> c != CONSTRUCTOR_3).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + OptionSetType.TYPE_ID);
    }
    ids[4] = ClientNodeSupport.resolve(table, ArrayItemType.TYPE_ID);
    if (manager.getNodeConstructor(ids[4]).filter(c -> c != CONSTRUCTOR_4).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + ArrayItemType.TYPE_ID);
    }
    ids[5] = ClientNodeSupport.resolve(table, YArrayItemType.TYPE_ID);
    if (manager.getNodeConstructor(ids[5]).filter(c -> c != CONSTRUCTOR_5).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + YArrayItemType.TYPE_ID);
    }
    ids[6] = ClientNodeSupport.resolve(table, XYArrayItemType.TYPE_ID);
    if (manager.getNodeConstructor(ids[6]).filter(c -> c != CONSTRUCTOR_6).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + XYArrayItemType.TYPE_ID);
    }
    ids[7] = ClientNodeSupport.resolve(table, ImageItemType.TYPE_ID);
    if (manager.getNodeConstructor(ids[7]).filter(c -> c != CONSTRUCTOR_7).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + ImageItemType.TYPE_ID);
    }
    ids[8] = ClientNodeSupport.resolve(table, CubeItemType.TYPE_ID);
    if (manager.getNodeConstructor(ids[8]).filter(c -> c != CONSTRUCTOR_8).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + CubeItemType.TYPE_ID);
    }
    ids[9] = ClientNodeSupport.resolve(table, NDimensionArrayItemType.TYPE_ID);
    if (manager.getNodeConstructor(ids[9]).filter(c -> c != CONSTRUCTOR_9).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + NDimensionArrayItemType.TYPE_ID);
    }
    ids[10] = ClientNodeSupport.resolve(table, GuardVariableType.TYPE_ID);
    if (manager.getNodeConstructor(ids[10]).filter(c -> c != CONSTRUCTOR_10).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + GuardVariableType.TYPE_ID);
    }
    ids[11] = ClientNodeSupport.resolve(table, ExpressionGuardVariableType.TYPE_ID);
    if (manager.getNodeConstructor(ids[11]).filter(c -> c != CONSTRUCTOR_11).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + ExpressionGuardVariableType.TYPE_ID);
    }
    ids[12] = ClientNodeSupport.resolve(table, ElseGuardVariableType.TYPE_ID);
    if (manager.getNodeConstructor(ids[12]).filter(c -> c != CONSTRUCTOR_12).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + ElseGuardVariableType.TYPE_ID);
    }
    ids[13] = ClientNodeSupport.resolve(table, BaseAnalogType.TYPE_ID);
    if (manager.getNodeConstructor(ids[13]).filter(c -> c != CONSTRUCTOR_13).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + BaseAnalogType.TYPE_ID);
    }
    ids[14] = ClientNodeSupport.resolve(table, ProgramDiagnostic2Type.TYPE_ID);
    if (manager.getNodeConstructor(ids[14]).filter(c -> c != CONSTRUCTOR_14).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + ProgramDiagnostic2Type.TYPE_ID);
    }
    ids[15] = ClientNodeSupport.resolve(table, SelectionListType.TYPE_ID);
    if (manager.getNodeConstructor(ids[15]).filter(c -> c != CONSTRUCTOR_15).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + SelectionListType.TYPE_ID);
    }
    ids[16] = ClientNodeSupport.resolve(table, AlarmRateVariableType.TYPE_ID);
    if (manager.getNodeConstructor(ids[16]).filter(c -> c != CONSTRUCTOR_16).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + AlarmRateVariableType.TYPE_ID);
    }
    ids[17] = ClientNodeSupport.resolve(table, AnalogUnitType.TYPE_ID);
    if (manager.getNodeConstructor(ids[17]).filter(c -> c != CONSTRUCTOR_17).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + AnalogUnitType.TYPE_ID);
    }
    ids[18] = ClientNodeSupport.resolve(table, AnalogItemType.TYPE_ID);
    if (manager.getNodeConstructor(ids[18]).filter(c -> c != CONSTRUCTOR_18).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + AnalogItemType.TYPE_ID);
    }
    ids[19] = ClientNodeSupport.resolve(table, AnalogUnitRangeType.TYPE_ID);
    if (manager.getNodeConstructor(ids[19]).filter(c -> c != CONSTRUCTOR_19).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + AnalogUnitRangeType.TYPE_ID);
    }
    ids[20] = ClientNodeSupport.resolve(table, RationalNumberType.TYPE_ID);
    if (manager.getNodeConstructor(ids[20]).filter(c -> c != CONSTRUCTOR_20).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + RationalNumberType.TYPE_ID);
    }
    ids[21] = ClientNodeSupport.resolve(table, VectorType.TYPE_ID);
    if (manager.getNodeConstructor(ids[21]).filter(c -> c != CONSTRUCTOR_21).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + VectorType.TYPE_ID);
    }
    ids[22] = ClientNodeSupport.resolve(table, ThreeDVectorType.TYPE_ID);
    if (manager.getNodeConstructor(ids[22]).filter(c -> c != CONSTRUCTOR_22).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + ThreeDVectorType.TYPE_ID);
    }
    ids[23] = ClientNodeSupport.resolve(table, AudioVariableType.TYPE_ID);
    if (manager.getNodeConstructor(ids[23]).filter(c -> c != CONSTRUCTOR_23).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + AudioVariableType.TYPE_ID);
    }
    ids[24] = ClientNodeSupport.resolve(table, CartesianCoordinatesType.TYPE_ID);
    if (manager.getNodeConstructor(ids[24]).filter(c -> c != CONSTRUCTOR_24).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + CartesianCoordinatesType.TYPE_ID);
    }
    ids[25] = ClientNodeSupport.resolve(table, ThreeDCartesianCoordinatesType.TYPE_ID);
    if (manager.getNodeConstructor(ids[25]).filter(c -> c != CONSTRUCTOR_25).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + ThreeDCartesianCoordinatesType.TYPE_ID);
    }
    ids[26] = ClientNodeSupport.resolve(table, OrientationType.TYPE_ID);
    if (manager.getNodeConstructor(ids[26]).filter(c -> c != CONSTRUCTOR_26).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + OrientationType.TYPE_ID);
    }
    ids[27] = ClientNodeSupport.resolve(table, ThreeDOrientationType.TYPE_ID);
    if (manager.getNodeConstructor(ids[27]).filter(c -> c != CONSTRUCTOR_27).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + ThreeDOrientationType.TYPE_ID);
    }
    ids[28] = ClientNodeSupport.resolve(table, FrameType.TYPE_ID);
    if (manager.getNodeConstructor(ids[28]).filter(c -> c != CONSTRUCTOR_28).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + FrameType.TYPE_ID);
    }
    ids[29] = ClientNodeSupport.resolve(table, ThreeDFrameType.TYPE_ID);
    if (manager.getNodeConstructor(ids[29]).filter(c -> c != CONSTRUCTOR_29).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + ThreeDFrameType.TYPE_ID);
    }
    ids[30] = ClientNodeSupport.resolve(table, MultiStateDictionaryEntryDiscreteBaseType.TYPE_ID);
    if (manager.getNodeConstructor(ids[30]).filter(c -> c != CONSTRUCTOR_30).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + MultiStateDictionaryEntryDiscreteBaseType.TYPE_ID);
    }
    ids[31] = ClientNodeSupport.resolve(table, MultiStateDictionaryEntryDiscreteType.TYPE_ID);
    if (manager.getNodeConstructor(ids[31]).filter(c -> c != CONSTRUCTOR_31).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + MultiStateDictionaryEntryDiscreteType.TYPE_ID);
    }
    ids[32] = ClientNodeSupport.resolve(table, PubSubDiagnosticsCounterType.TYPE_ID);
    if (manager.getNodeConstructor(ids[32]).filter(c -> c != CONSTRUCTOR_32).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + PubSubDiagnosticsCounterType.TYPE_ID);
    }
    ids[33] = ClientNodeSupport.resolve(table, ServerVendorCapabilityType.TYPE_ID);
    if (manager.getNodeConstructor(ids[33]).filter(c -> c != CONSTRUCTOR_33).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + ServerVendorCapabilityType.TYPE_ID);
    }
    ids[34] = ClientNodeSupport.resolve(table, ServerStatusType.TYPE_ID);
    if (manager.getNodeConstructor(ids[34]).filter(c -> c != CONSTRUCTOR_34).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + ServerStatusType.TYPE_ID);
    }
    ids[35] = ClientNodeSupport.resolve(table, ServerDiagnosticsSummaryType.TYPE_ID);
    if (manager.getNodeConstructor(ids[35]).filter(c -> c != CONSTRUCTOR_35).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + ServerDiagnosticsSummaryType.TYPE_ID);
    }
    ids[36] = ClientNodeSupport.resolve(table, SamplingIntervalDiagnosticsArrayType.TYPE_ID);
    if (manager.getNodeConstructor(ids[36]).filter(c -> c != CONSTRUCTOR_36).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + SamplingIntervalDiagnosticsArrayType.TYPE_ID);
    }
    ids[37] = ClientNodeSupport.resolve(table, SamplingIntervalDiagnosticsType.TYPE_ID);
    if (manager.getNodeConstructor(ids[37]).filter(c -> c != CONSTRUCTOR_37).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + SamplingIntervalDiagnosticsType.TYPE_ID);
    }
    ids[38] = ClientNodeSupport.resolve(table, SubscriptionDiagnosticsArrayType.TYPE_ID);
    if (manager.getNodeConstructor(ids[38]).filter(c -> c != CONSTRUCTOR_38).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + SubscriptionDiagnosticsArrayType.TYPE_ID);
    }
    ids[39] = ClientNodeSupport.resolve(table, SubscriptionDiagnosticsType.TYPE_ID);
    if (manager.getNodeConstructor(ids[39]).filter(c -> c != CONSTRUCTOR_39).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + SubscriptionDiagnosticsType.TYPE_ID);
    }
    ids[40] = ClientNodeSupport.resolve(table, SessionDiagnosticsArrayType.TYPE_ID);
    if (manager.getNodeConstructor(ids[40]).filter(c -> c != CONSTRUCTOR_40).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + SessionDiagnosticsArrayType.TYPE_ID);
    }
    ids[41] = ClientNodeSupport.resolve(table, SessionDiagnosticsVariableType.TYPE_ID);
    if (manager.getNodeConstructor(ids[41]).filter(c -> c != CONSTRUCTOR_41).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + SessionDiagnosticsVariableType.TYPE_ID);
    }
    ids[42] = ClientNodeSupport.resolve(table, SessionSecurityDiagnosticsArrayType.TYPE_ID);
    if (manager.getNodeConstructor(ids[42]).filter(c -> c != CONSTRUCTOR_42).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + SessionSecurityDiagnosticsArrayType.TYPE_ID);
    }
    ids[43] = ClientNodeSupport.resolve(table, SessionSecurityDiagnosticsType.TYPE_ID);
    if (manager.getNodeConstructor(ids[43]).filter(c -> c != CONSTRUCTOR_43).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + SessionSecurityDiagnosticsType.TYPE_ID);
    }
    ids[44] = ClientNodeSupport.resolve(table, TwoStateDiscreteType.TYPE_ID);
    if (manager.getNodeConstructor(ids[44]).filter(c -> c != CONSTRUCTOR_44).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + TwoStateDiscreteType.TYPE_ID);
    }
    ids[45] = ClientNodeSupport.resolve(table, MultiStateDiscreteType.TYPE_ID);
    if (manager.getNodeConstructor(ids[45]).filter(c -> c != CONSTRUCTOR_45).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + MultiStateDiscreteType.TYPE_ID);
    }
    ids[46] = ClientNodeSupport.resolve(table, ProgramDiagnosticType.TYPE_ID);
    if (manager.getNodeConstructor(ids[46]).filter(c -> c != CONSTRUCTOR_46).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + ProgramDiagnosticType.TYPE_ID);
    }
    ids[47] = ClientNodeSupport.resolve(table, AnalogNumberItemType.TYPE_ID);
    if (manager.getNodeConstructor(ids[47]).filter(c -> c != CONSTRUCTOR_47).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + AnalogNumberItemType.TYPE_ID);
    }
    ids[48] = ClientNodeSupport.resolve(table, AnalogNumberUnitRangeType.TYPE_ID);
    if (manager.getNodeConstructor(ids[48]).filter(c -> c != CONSTRUCTOR_48).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + AnalogNumberUnitRangeType.TYPE_ID);
    }
    ids[49] = ClientNodeSupport.resolve(table, StateVariableType.TYPE_ID);
    if (manager.getNodeConstructor(ids[49]).filter(c -> c != CONSTRUCTOR_49).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + StateVariableType.TYPE_ID);
    }
    ids[50] = ClientNodeSupport.resolve(table, FiniteStateVariableType.TYPE_ID);
    if (manager.getNodeConstructor(ids[50]).filter(c -> c != CONSTRUCTOR_50).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + FiniteStateVariableType.TYPE_ID);
    }
    ids[51] = ClientNodeSupport.resolve(table, TransitionVariableType.TYPE_ID);
    if (manager.getNodeConstructor(ids[51]).filter(c -> c != CONSTRUCTOR_51).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + TransitionVariableType.TYPE_ID);
    }
    ids[52] = ClientNodeSupport.resolve(table, FiniteTransitionVariableType.TYPE_ID);
    if (manager.getNodeConstructor(ids[52]).filter(c -> c != CONSTRUCTOR_52).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + FiniteTransitionVariableType.TYPE_ID);
    }
    ids[53] = ClientNodeSupport.resolve(table, BuildInfoType.TYPE_ID);
    if (manager.getNodeConstructor(ids[53]).filter(c -> c != CONSTRUCTOR_53).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + BuildInfoType.TYPE_ID);
    }
    ids[54] = ClientNodeSupport.resolve(table, AlarmStateVariableType.TYPE_ID);
    if (manager.getNodeConstructor(ids[54]).filter(c -> c != CONSTRUCTOR_54).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + AlarmStateVariableType.TYPE_ID);
    }
    ids[55] = ClientNodeSupport.resolve(table, BitFieldType.TYPE_ID);
    if (manager.getNodeConstructor(ids[55]).filter(c -> c != CONSTRUCTOR_55).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + BitFieldType.TYPE_ID);
    }
    ids[56] = ClientNodeSupport.resolve(table, ReferenceDescriptionVariableType.TYPE_ID);
    if (manager.getNodeConstructor(ids[56]).filter(c -> c != CONSTRUCTOR_56).isPresent()) {
      throw new IllegalStateException(
          "constructor conflict: " + ReferenceDescriptionVariableType.TYPE_ID);
    }
    ids[57] = ClientNodeSupport.resolve(table, PropertyType.TYPE_ID);
    if (manager.getNodeConstructor(ids[57]).filter(c -> c != CONSTRUCTOR_57).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + PropertyType.TYPE_ID);
    }
    ids[58] = ClientNodeSupport.resolve(table, DataTypeDescriptionType.TYPE_ID);
    if (manager.getNodeConstructor(ids[58]).filter(c -> c != CONSTRUCTOR_58).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + DataTypeDescriptionType.TYPE_ID);
    }
    ids[59] = ClientNodeSupport.resolve(table, DataTypeDictionaryType.TYPE_ID);
    if (manager.getNodeConstructor(ids[59]).filter(c -> c != CONSTRUCTOR_59).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + DataTypeDictionaryType.TYPE_ID);
    }
    ids[60] = ClientNodeSupport.resolve(table, TwoStateVariableType.TYPE_ID);
    if (manager.getNodeConstructor(ids[60]).filter(c -> c != CONSTRUCTOR_60).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + TwoStateVariableType.TYPE_ID);
    }
    ids[61] = ClientNodeSupport.resolve(table, ConditionVariableType.TYPE_ID);
    if (manager.getNodeConstructor(ids[61]).filter(c -> c != CONSTRUCTOR_61).isPresent()) {
      throw new IllegalStateException("constructor conflict: " + ConditionVariableType.TYPE_ID);
    }
  }

  private static void install0(VariableTypeManager manager, NodeId[] ids) {
    if (manager.getNodeConstructor(ids[0]).isEmpty()) {
      manager.registerVariableType(ids[0], DataItemTypeNode.class, CONSTRUCTOR_0);
    }
    if (manager.getNodeConstructor(ids[1]).isEmpty()) {
      manager.registerVariableType(ids[1], DiscreteItemTypeNode.class, CONSTRUCTOR_1);
    }
    if (manager.getNodeConstructor(ids[2]).isEmpty()) {
      manager.registerVariableType(ids[2], MultiStateValueDiscreteTypeNode.class, CONSTRUCTOR_2);
    }
    if (manager.getNodeConstructor(ids[3]).isEmpty()) {
      manager.registerVariableType(ids[3], OptionSetTypeNode.class, CONSTRUCTOR_3);
    }
    if (manager.getNodeConstructor(ids[4]).isEmpty()) {
      manager.registerVariableType(ids[4], ArrayItemTypeNode.class, CONSTRUCTOR_4);
    }
    if (manager.getNodeConstructor(ids[5]).isEmpty()) {
      manager.registerVariableType(ids[5], YArrayItemTypeNode.class, CONSTRUCTOR_5);
    }
    if (manager.getNodeConstructor(ids[6]).isEmpty()) {
      manager.registerVariableType(ids[6], XYArrayItemTypeNode.class, CONSTRUCTOR_6);
    }
    if (manager.getNodeConstructor(ids[7]).isEmpty()) {
      manager.registerVariableType(ids[7], ImageItemTypeNode.class, CONSTRUCTOR_7);
    }
    if (manager.getNodeConstructor(ids[8]).isEmpty()) {
      manager.registerVariableType(ids[8], CubeItemTypeNode.class, CONSTRUCTOR_8);
    }
    if (manager.getNodeConstructor(ids[9]).isEmpty()) {
      manager.registerVariableType(ids[9], NDimensionArrayItemTypeNode.class, CONSTRUCTOR_9);
    }
    if (manager.getNodeConstructor(ids[10]).isEmpty()) {
      manager.registerVariableType(ids[10], GuardVariableTypeNode.class, CONSTRUCTOR_10);
    }
    if (manager.getNodeConstructor(ids[11]).isEmpty()) {
      manager.registerVariableType(ids[11], ExpressionGuardVariableTypeNode.class, CONSTRUCTOR_11);
    }
    if (manager.getNodeConstructor(ids[12]).isEmpty()) {
      manager.registerVariableType(ids[12], ElseGuardVariableTypeNode.class, CONSTRUCTOR_12);
    }
    if (manager.getNodeConstructor(ids[13]).isEmpty()) {
      manager.registerVariableType(ids[13], BaseAnalogTypeNode.class, CONSTRUCTOR_13);
    }
    if (manager.getNodeConstructor(ids[14]).isEmpty()) {
      manager.registerVariableType(ids[14], ProgramDiagnostic2TypeNode.class, CONSTRUCTOR_14);
    }
    if (manager.getNodeConstructor(ids[15]).isEmpty()) {
      manager.registerVariableType(ids[15], SelectionListTypeNode.class, CONSTRUCTOR_15);
    }
    if (manager.getNodeConstructor(ids[16]).isEmpty()) {
      manager.registerVariableType(ids[16], AlarmRateVariableTypeNode.class, CONSTRUCTOR_16);
    }
    if (manager.getNodeConstructor(ids[17]).isEmpty()) {
      manager.registerVariableType(ids[17], AnalogUnitTypeNode.class, CONSTRUCTOR_17);
    }
    if (manager.getNodeConstructor(ids[18]).isEmpty()) {
      manager.registerVariableType(ids[18], AnalogItemTypeNode.class, CONSTRUCTOR_18);
    }
    if (manager.getNodeConstructor(ids[19]).isEmpty()) {
      manager.registerVariableType(ids[19], AnalogUnitRangeTypeNode.class, CONSTRUCTOR_19);
    }
    if (manager.getNodeConstructor(ids[20]).isEmpty()) {
      manager.registerVariableType(ids[20], RationalNumberTypeNode.class, CONSTRUCTOR_20);
    }
    if (manager.getNodeConstructor(ids[21]).isEmpty()) {
      manager.registerVariableType(ids[21], VectorTypeNode.class, CONSTRUCTOR_21);
    }
    if (manager.getNodeConstructor(ids[22]).isEmpty()) {
      manager.registerVariableType(ids[22], ThreeDVectorTypeNode.class, CONSTRUCTOR_22);
    }
    if (manager.getNodeConstructor(ids[23]).isEmpty()) {
      manager.registerVariableType(ids[23], AudioVariableTypeNode.class, CONSTRUCTOR_23);
    }
    if (manager.getNodeConstructor(ids[24]).isEmpty()) {
      manager.registerVariableType(ids[24], CartesianCoordinatesTypeNode.class, CONSTRUCTOR_24);
    }
    if (manager.getNodeConstructor(ids[25]).isEmpty()) {
      manager.registerVariableType(
          ids[25], ThreeDCartesianCoordinatesTypeNode.class, CONSTRUCTOR_25);
    }
    if (manager.getNodeConstructor(ids[26]).isEmpty()) {
      manager.registerVariableType(ids[26], OrientationTypeNode.class, CONSTRUCTOR_26);
    }
    if (manager.getNodeConstructor(ids[27]).isEmpty()) {
      manager.registerVariableType(ids[27], ThreeDOrientationTypeNode.class, CONSTRUCTOR_27);
    }
    if (manager.getNodeConstructor(ids[28]).isEmpty()) {
      manager.registerVariableType(ids[28], FrameTypeNode.class, CONSTRUCTOR_28);
    }
    if (manager.getNodeConstructor(ids[29]).isEmpty()) {
      manager.registerVariableType(ids[29], ThreeDFrameTypeNode.class, CONSTRUCTOR_29);
    }
    if (manager.getNodeConstructor(ids[30]).isEmpty()) {
      manager.registerVariableType(
          ids[30], MultiStateDictionaryEntryDiscreteBaseTypeNode.class, CONSTRUCTOR_30);
    }
    if (manager.getNodeConstructor(ids[31]).isEmpty()) {
      manager.registerVariableType(
          ids[31], MultiStateDictionaryEntryDiscreteTypeNode.class, CONSTRUCTOR_31);
    }
    if (manager.getNodeConstructor(ids[32]).isEmpty()) {
      manager.registerVariableType(ids[32], PubSubDiagnosticsCounterTypeNode.class, CONSTRUCTOR_32);
    }
    if (manager.getNodeConstructor(ids[33]).isEmpty()) {
      manager.registerVariableType(ids[33], ServerVendorCapabilityTypeNode.class, CONSTRUCTOR_33);
    }
    if (manager.getNodeConstructor(ids[34]).isEmpty()) {
      manager.registerVariableType(ids[34], ServerStatusTypeNode.class, CONSTRUCTOR_34);
    }
    if (manager.getNodeConstructor(ids[35]).isEmpty()) {
      manager.registerVariableType(ids[35], ServerDiagnosticsSummaryTypeNode.class, CONSTRUCTOR_35);
    }
    if (manager.getNodeConstructor(ids[36]).isEmpty()) {
      manager.registerVariableType(
          ids[36], SamplingIntervalDiagnosticsArrayTypeNode.class, CONSTRUCTOR_36);
    }
    if (manager.getNodeConstructor(ids[37]).isEmpty()) {
      manager.registerVariableType(
          ids[37], SamplingIntervalDiagnosticsTypeNode.class, CONSTRUCTOR_37);
    }
    if (manager.getNodeConstructor(ids[38]).isEmpty()) {
      manager.registerVariableType(
          ids[38], SubscriptionDiagnosticsArrayTypeNode.class, CONSTRUCTOR_38);
    }
    if (manager.getNodeConstructor(ids[39]).isEmpty()) {
      manager.registerVariableType(ids[39], SubscriptionDiagnosticsTypeNode.class, CONSTRUCTOR_39);
    }
    if (manager.getNodeConstructor(ids[40]).isEmpty()) {
      manager.registerVariableType(ids[40], SessionDiagnosticsArrayTypeNode.class, CONSTRUCTOR_40);
    }
    if (manager.getNodeConstructor(ids[41]).isEmpty()) {
      manager.registerVariableType(
          ids[41], SessionDiagnosticsVariableTypeNode.class, CONSTRUCTOR_41);
    }
    if (manager.getNodeConstructor(ids[42]).isEmpty()) {
      manager.registerVariableType(
          ids[42], SessionSecurityDiagnosticsArrayTypeNode.class, CONSTRUCTOR_42);
    }
    if (manager.getNodeConstructor(ids[43]).isEmpty()) {
      manager.registerVariableType(
          ids[43], SessionSecurityDiagnosticsTypeNode.class, CONSTRUCTOR_43);
    }
    if (manager.getNodeConstructor(ids[44]).isEmpty()) {
      manager.registerVariableType(ids[44], TwoStateDiscreteTypeNode.class, CONSTRUCTOR_44);
    }
    if (manager.getNodeConstructor(ids[45]).isEmpty()) {
      manager.registerVariableType(ids[45], MultiStateDiscreteTypeNode.class, CONSTRUCTOR_45);
    }
    if (manager.getNodeConstructor(ids[46]).isEmpty()) {
      manager.registerVariableType(ids[46], ProgramDiagnosticTypeNode.class, CONSTRUCTOR_46);
    }
    if (manager.getNodeConstructor(ids[47]).isEmpty()) {
      manager.registerVariableType(ids[47], AnalogNumberItemTypeNode.class, CONSTRUCTOR_47);
    }
    if (manager.getNodeConstructor(ids[48]).isEmpty()) {
      manager.registerVariableType(ids[48], AnalogNumberUnitRangeTypeNode.class, CONSTRUCTOR_48);
    }
    if (manager.getNodeConstructor(ids[49]).isEmpty()) {
      manager.registerVariableType(ids[49], StateVariableTypeNode.class, CONSTRUCTOR_49);
    }
    if (manager.getNodeConstructor(ids[50]).isEmpty()) {
      manager.registerVariableType(ids[50], FiniteStateVariableTypeNode.class, CONSTRUCTOR_50);
    }
    if (manager.getNodeConstructor(ids[51]).isEmpty()) {
      manager.registerVariableType(ids[51], TransitionVariableTypeNode.class, CONSTRUCTOR_51);
    }
    if (manager.getNodeConstructor(ids[52]).isEmpty()) {
      manager.registerVariableType(ids[52], FiniteTransitionVariableTypeNode.class, CONSTRUCTOR_52);
    }
    if (manager.getNodeConstructor(ids[53]).isEmpty()) {
      manager.registerVariableType(ids[53], BuildInfoTypeNode.class, CONSTRUCTOR_53);
    }
    if (manager.getNodeConstructor(ids[54]).isEmpty()) {
      manager.registerVariableType(ids[54], AlarmStateVariableTypeNode.class, CONSTRUCTOR_54);
    }
    if (manager.getNodeConstructor(ids[55]).isEmpty()) {
      manager.registerVariableType(ids[55], BitFieldTypeNode.class, CONSTRUCTOR_55);
    }
    if (manager.getNodeConstructor(ids[56]).isEmpty()) {
      manager.registerVariableType(
          ids[56], ReferenceDescriptionVariableTypeNode.class, CONSTRUCTOR_56);
    }
    if (manager.getNodeConstructor(ids[57]).isEmpty()) {
      manager.registerVariableType(ids[57], PropertyTypeNode.class, CONSTRUCTOR_57);
    }
    if (manager.getNodeConstructor(ids[58]).isEmpty()) {
      manager.registerVariableType(ids[58], DataTypeDescriptionTypeNode.class, CONSTRUCTOR_58);
    }
    if (manager.getNodeConstructor(ids[59]).isEmpty()) {
      manager.registerVariableType(ids[59], DataTypeDictionaryTypeNode.class, CONSTRUCTOR_59);
    }
    if (manager.getNodeConstructor(ids[60]).isEmpty()) {
      manager.registerVariableType(ids[60], TwoStateVariableTypeNode.class, CONSTRUCTOR_60);
    }
    if (manager.getNodeConstructor(ids[61]).isEmpty()) {
      manager.registerVariableType(ids[61], ConditionVariableTypeNode.class, CONSTRUCTOR_61);
    }
  }

  /**
   * Checks all namespaces and conflicts before mutation. Repeat calls retain identical
   * constructors.
   *
   * @param namespaceTable the connected client's namespace table.
   * @param manager the client's constructor manager.
   * @throws IllegalArgumentException if a namespace is missing.
   * @throws IllegalStateException if a different constructor is already registered.
   */
  public static void initialize(NamespaceTable namespaceTable, VariableTypeManager manager) {
    NodeId[] ids = new NodeId[62];
    check0(namespaceTable, manager, ids);
    install0(manager, ids);
  }
}
