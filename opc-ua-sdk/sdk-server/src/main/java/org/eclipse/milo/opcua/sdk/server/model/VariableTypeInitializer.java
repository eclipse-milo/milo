/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.model;

import org.eclipse.milo.opcua.sdk.server.VariableTypeManager;
import org.eclipse.milo.opcua.sdk.server.model.variables.AlarmRateVariableTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.AlarmStateVariableTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.AnalogItemTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.AnalogUnitRangeTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.AnalogUnitTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.ArrayItemTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.AudioVariableTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.BaseAnalogTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.BaseDataVariableTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.BaseVariableTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.BitFieldTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.BuildInfoTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.CartesianCoordinatesTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.ConditionVariableTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.CubeItemTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.DataItemTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.DataTypeDescriptionTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.DataTypeDictionaryTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.DiscreteItemTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.ElseGuardVariableTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.ExpressionGuardVariableTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.FiniteStateVariableTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.FiniteTransitionVariableTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.FrameTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.GuardVariableTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.ImageItemTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.MultiStateDictionaryEntryDiscreteBaseTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.MultiStateDictionaryEntryDiscreteTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.MultiStateDiscreteTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.MultiStateValueDiscreteTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.NDimensionArrayItemTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.OptionSetTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.OrientationTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.ProgramDiagnostic2TypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.ProgramDiagnosticTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.PubSubDiagnosticsCounterTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.RationalNumberTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.ReferenceDescriptionVariableTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.SamplingIntervalDiagnosticsArrayTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.SamplingIntervalDiagnosticsTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.SelectionListTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.ServerDiagnosticsSummaryTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.ServerStatusTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.ServerVendorCapabilityTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.SessionDiagnosticsArrayTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.SessionDiagnosticsVariableTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.SessionSecurityDiagnosticsArrayTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.SessionSecurityDiagnosticsTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.StateVariableTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.SubscriptionDiagnosticsArrayTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.SubscriptionDiagnosticsTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.ThreeDCartesianCoordinatesTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.ThreeDFrameTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.ThreeDOrientationTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.ThreeDVectorTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.TransitionVariableTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.TwoStateDiscreteTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.TwoStateVariableTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.VectorTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.XYArrayItemTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.YArrayItemTypeNode;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;

public class VariableTypeInitializer {
  public static void initialize(
      NamespaceTable namespaceTable, VariableTypeManager variableTypeManager) {
    variableTypeManager.registerVariableType(
        NodeId.parse("i=62").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        BaseVariableTypeNode.class,
        BaseVariableTypeNode::new);
    variableTypeManager.registerVariableType(
        NodeId.parse("i=63").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        BaseDataVariableTypeNode.class,
        BaseDataVariableTypeNode::new);
    variableTypeManager.registerVariableType(
        NodeId.parse("i=69").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        DataTypeDescriptionTypeNode.class,
        DataTypeDescriptionTypeNode::new);
    variableTypeManager.registerVariableType(
        NodeId.parse("i=72").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        DataTypeDictionaryTypeNode.class,
        DataTypeDictionaryTypeNode::new);
    variableTypeManager.registerVariableType(
        NodeId.parse("i=2137").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        ServerVendorCapabilityTypeNode.class,
        ServerVendorCapabilityTypeNode::new);
    variableTypeManager.registerVariableType(
        NodeId.parse("i=2138").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        ServerStatusTypeNode.class,
        ServerStatusTypeNode::new);
    variableTypeManager.registerVariableType(
        NodeId.parse("i=3051").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        BuildInfoTypeNode.class,
        BuildInfoTypeNode::new);
    variableTypeManager.registerVariableType(
        NodeId.parse("i=2150").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        ServerDiagnosticsSummaryTypeNode.class,
        ServerDiagnosticsSummaryTypeNode::new);
    variableTypeManager.registerVariableType(
        NodeId.parse("i=2164").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        SamplingIntervalDiagnosticsArrayTypeNode.class,
        SamplingIntervalDiagnosticsArrayTypeNode::new);
    variableTypeManager.registerVariableType(
        NodeId.parse("i=2165").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        SamplingIntervalDiagnosticsTypeNode.class,
        SamplingIntervalDiagnosticsTypeNode::new);
    variableTypeManager.registerVariableType(
        NodeId.parse("i=2171").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        SubscriptionDiagnosticsArrayTypeNode.class,
        SubscriptionDiagnosticsArrayTypeNode::new);
    variableTypeManager.registerVariableType(
        NodeId.parse("i=2172").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        SubscriptionDiagnosticsTypeNode.class,
        SubscriptionDiagnosticsTypeNode::new);
    variableTypeManager.registerVariableType(
        NodeId.parse("i=2196").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        SessionDiagnosticsArrayTypeNode.class,
        SessionDiagnosticsArrayTypeNode::new);
    variableTypeManager.registerVariableType(
        NodeId.parse("i=2197").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        SessionDiagnosticsVariableTypeNode.class,
        SessionDiagnosticsVariableTypeNode::new);
    variableTypeManager.registerVariableType(
        NodeId.parse("i=2243").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        SessionSecurityDiagnosticsArrayTypeNode.class,
        SessionSecurityDiagnosticsArrayTypeNode::new);
    variableTypeManager.registerVariableType(
        NodeId.parse("i=2244").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        SessionSecurityDiagnosticsTypeNode.class,
        SessionSecurityDiagnosticsTypeNode::new);
    variableTypeManager.registerVariableType(
        NodeId.parse("i=11487").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        OptionSetTypeNode.class,
        OptionSetTypeNode::new);
    variableTypeManager.registerVariableType(
        NodeId.parse("i=16309").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        SelectionListTypeNode.class,
        SelectionListTypeNode::new);
    variableTypeManager.registerVariableType(
        NodeId.parse("i=17986").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        AudioVariableTypeNode.class,
        AudioVariableTypeNode::new);
    variableTypeManager.registerVariableType(
        NodeId.parse("i=2755").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        StateVariableTypeNode.class,
        StateVariableTypeNode::new);
    variableTypeManager.registerVariableType(
        NodeId.parse("i=2760").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        FiniteStateVariableTypeNode.class,
        FiniteStateVariableTypeNode::new);
    variableTypeManager.registerVariableType(
        NodeId.parse("i=8995").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        TwoStateVariableTypeNode.class,
        TwoStateVariableTypeNode::new);
    variableTypeManager.registerVariableType(
        NodeId.parse("i=2762").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        TransitionVariableTypeNode.class,
        TransitionVariableTypeNode::new);
    variableTypeManager.registerVariableType(
        NodeId.parse("i=2767").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        FiniteTransitionVariableTypeNode.class,
        FiniteTransitionVariableTypeNode::new);
    variableTypeManager.registerVariableType(
        NodeId.parse("i=15113").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        GuardVariableTypeNode.class,
        GuardVariableTypeNode::new);
    variableTypeManager.registerVariableType(
        NodeId.parse("i=15128").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        ExpressionGuardVariableTypeNode.class,
        ExpressionGuardVariableTypeNode::new);
    variableTypeManager.registerVariableType(
        NodeId.parse("i=15317").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        ElseGuardVariableTypeNode.class,
        ElseGuardVariableTypeNode::new);
    variableTypeManager.registerVariableType(
        NodeId.parse("i=17709").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        RationalNumberTypeNode.class,
        RationalNumberTypeNode::new);
    variableTypeManager.registerVariableType(
        NodeId.parse("i=17714").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        VectorTypeNode.class,
        VectorTypeNode::new);
    variableTypeManager.registerVariableType(
        NodeId.parse("i=17716").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        ThreeDVectorTypeNode.class,
        ThreeDVectorTypeNode::new);
    variableTypeManager.registerVariableType(
        NodeId.parse("i=18772").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        CartesianCoordinatesTypeNode.class,
        CartesianCoordinatesTypeNode::new);
    variableTypeManager.registerVariableType(
        NodeId.parse("i=18774").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        ThreeDCartesianCoordinatesTypeNode.class,
        ThreeDCartesianCoordinatesTypeNode::new);
    variableTypeManager.registerVariableType(
        NodeId.parse("i=18779").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        OrientationTypeNode.class,
        OrientationTypeNode::new);
    variableTypeManager.registerVariableType(
        NodeId.parse("i=18781").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        ThreeDOrientationTypeNode.class,
        ThreeDOrientationTypeNode::new);
    variableTypeManager.registerVariableType(
        NodeId.parse("i=18786").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        FrameTypeNode.class,
        FrameTypeNode::new);
    variableTypeManager.registerVariableType(
        NodeId.parse("i=18791").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        ThreeDFrameTypeNode.class,
        ThreeDFrameTypeNode::new);
    variableTypeManager.registerVariableType(
        NodeId.parse("i=32431").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        BitFieldTypeNode.class,
        BitFieldTypeNode::new);
    variableTypeManager.registerVariableType(
        NodeId.parse("i=2365").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        DataItemTypeNode.class,
        DataItemTypeNode::new);
    variableTypeManager.registerVariableType(
        NodeId.parse("i=15318").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        BaseAnalogTypeNode.class,
        BaseAnalogTypeNode::new);
    variableTypeManager.registerVariableType(
        NodeId.parse("i=2368").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        AnalogItemTypeNode.class,
        AnalogItemTypeNode::new);
    variableTypeManager.registerVariableType(
        NodeId.parse("i=17570").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        AnalogUnitRangeTypeNode.class,
        AnalogUnitRangeTypeNode::new);
    variableTypeManager.registerVariableType(
        NodeId.parse("i=17497").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        AnalogUnitTypeNode.class,
        AnalogUnitTypeNode::new);
    variableTypeManager.registerVariableType(
        NodeId.parse("i=2372").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        DiscreteItemTypeNode.class,
        DiscreteItemTypeNode::new);
    variableTypeManager.registerVariableType(
        NodeId.parse("i=2373").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        TwoStateDiscreteTypeNode.class,
        TwoStateDiscreteTypeNode::new);
    variableTypeManager.registerVariableType(
        NodeId.parse("i=2376").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        MultiStateDiscreteTypeNode.class,
        MultiStateDiscreteTypeNode::new);
    variableTypeManager.registerVariableType(
        NodeId.parse("i=11238").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        MultiStateValueDiscreteTypeNode.class,
        MultiStateValueDiscreteTypeNode::new);
    variableTypeManager.registerVariableType(
        NodeId.parse("i=19077").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        MultiStateDictionaryEntryDiscreteBaseTypeNode.class,
        MultiStateDictionaryEntryDiscreteBaseTypeNode::new);
    variableTypeManager.registerVariableType(
        NodeId.parse("i=19084").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        MultiStateDictionaryEntryDiscreteTypeNode.class,
        MultiStateDictionaryEntryDiscreteTypeNode::new);
    variableTypeManager.registerVariableType(
        NodeId.parse("i=12021").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        ArrayItemTypeNode.class,
        ArrayItemTypeNode::new);
    variableTypeManager.registerVariableType(
        NodeId.parse("i=12029").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        YArrayItemTypeNode.class,
        YArrayItemTypeNode::new);
    variableTypeManager.registerVariableType(
        NodeId.parse("i=12038").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        XYArrayItemTypeNode.class,
        XYArrayItemTypeNode::new);
    variableTypeManager.registerVariableType(
        NodeId.parse("i=12047").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        ImageItemTypeNode.class,
        ImageItemTypeNode::new);
    variableTypeManager.registerVariableType(
        NodeId.parse("i=12057").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        CubeItemTypeNode.class,
        CubeItemTypeNode::new);
    variableTypeManager.registerVariableType(
        NodeId.parse("i=12068").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        NDimensionArrayItemTypeNode.class,
        NDimensionArrayItemTypeNode::new);
    variableTypeManager.registerVariableType(
        NodeId.parse("i=9002").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        ConditionVariableTypeNode.class,
        ConditionVariableTypeNode::new);
    variableTypeManager.registerVariableType(
        NodeId.parse("i=17277").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        AlarmRateVariableTypeNode.class,
        AlarmRateVariableTypeNode::new);
    variableTypeManager.registerVariableType(
        NodeId.parse("i=32244").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        AlarmStateVariableTypeNode.class,
        AlarmStateVariableTypeNode::new);
    variableTypeManager.registerVariableType(
        NodeId.parse("i=2380").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        ProgramDiagnosticTypeNode.class,
        ProgramDiagnosticTypeNode::new);
    variableTypeManager.registerVariableType(
        NodeId.parse("i=15383").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        ProgramDiagnostic2TypeNode.class,
        ProgramDiagnostic2TypeNode::new);
    variableTypeManager.registerVariableType(
        NodeId.parse("i=19725").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        PubSubDiagnosticsCounterTypeNode.class,
        PubSubDiagnosticsCounterTypeNode::new);
    variableTypeManager.registerVariableType(
        NodeId.parse("i=32657").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        ReferenceDescriptionVariableTypeNode.class,
        ReferenceDescriptionVariableTypeNode::new);
    variableTypeManager.registerVariableType(
        NodeId.parse("i=68").reindex(namespaceTable, "http://opcfoundation.org/UA/"),
        PropertyTypeNode.class,
        PropertyTypeNode::new);
  }
}
