/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.typetree;

import org.eclipse.milo.opcua.sdk.core.typetree.DataType;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.types.structured.EnumDefinition;
import org.eclipse.milo.opcua.stack.core.types.structured.EnumField;
import org.eclipse.milo.opcua.stack.core.types.structured.StructureDefinition;
import org.eclipse.milo.opcua.stack.core.types.structured.StructureField;
import org.eclipse.milo.opcua.stack.core.util.Tree;

public final class LazyClientDataTypeTreeSeed {
  private LazyClientDataTypeTreeSeed() {}

  @SuppressWarnings("unused")
  public static Tree<DataType> createSeedTree() {
    Tree<DataType> root =
        new Tree<>(
            null,
            new ClientDataType(
                QualifiedName.parse("0:BaseDataType"),
                NodeIds.BaseDataType,
                null,
                null,
                null,
                null,
                true));
    Tree<DataType> booleanType =
        root.addChild(
            new ClientDataType(
                QualifiedName.parse("0:Boolean"), NodeIds.Boolean, null, null, null, null, false));
    Tree<DataType> string =
        root.addChild(
            new ClientDataType(
                QualifiedName.parse("0:String"), NodeIds.String, null, null, null, null, false));
    Tree<DataType> numericRange =
        string.addChild(
            new ClientDataType(
                QualifiedName.parse("0:NumericRange"),
                NodeIds.NumericRange,
                null,
                null,
                null,
                null,
                false));
    Tree<DataType> localeId =
        string.addChild(
            new ClientDataType(
                QualifiedName.parse("0:LocaleId"),
                NodeIds.LocaleId,
                null,
                null,
                null,
                null,
                false));
    Tree<DataType> normalizedString =
        string.addChild(
            new ClientDataType(
                QualifiedName.parse("0:NormalizedString"),
                NodeIds.NormalizedString,
                null,
                null,
                null,
                null,
                false));
    Tree<DataType> decimalString =
        string.addChild(
            new ClientDataType(
                QualifiedName.parse("0:DecimalString"),
                NodeIds.DecimalString,
                null,
                null,
                null,
                null,
                false));
    Tree<DataType> durationString =
        string.addChild(
            new ClientDataType(
                QualifiedName.parse("0:DurationString"),
                NodeIds.DurationString,
                null,
                null,
                null,
                null,
                false));
    Tree<DataType> timeString =
        string.addChild(
            new ClientDataType(
                QualifiedName.parse("0:TimeString"),
                NodeIds.TimeString,
                null,
                null,
                null,
                null,
                false));
    Tree<DataType> dateString =
        string.addChild(
            new ClientDataType(
                QualifiedName.parse("0:DateString"),
                NodeIds.DateString,
                null,
                null,
                null,
                null,
                false));
    Tree<DataType> uriString =
        string.addChild(
            new ClientDataType(
                QualifiedName.parse("0:UriString"),
                NodeIds.UriString,
                null,
                null,
                null,
                null,
                false));
    Tree<DataType> semanticVersionString =
        string.addChild(
            new ClientDataType(
                QualifiedName.parse("0:SemanticVersionString"),
                NodeIds.SemanticVersionString,
                null,
                null,
                null,
                null,
                false));
    Tree<DataType> encodedTicket =
        string.addChild(
            new ClientDataType(
                QualifiedName.parse("0:EncodedTicket"),
                NodeIds.EncodedTicket,
                null,
                null,
                null,
                null,
                false));
    Tree<DataType> trimmedString =
        string.addChild(
            new ClientDataType(
                QualifiedName.parse("0:TrimmedString"),
                NodeIds.TrimmedString,
                null,
                null,
                null,
                null,
                false));
    Tree<DataType> dateTime =
        root.addChild(
            new ClientDataType(
                QualifiedName.parse("0:DateTime"),
                NodeIds.DateTime,
                null,
                null,
                null,
                null,
                false));
    Tree<DataType> utcTime =
        dateTime.addChild(
            new ClientDataType(
                QualifiedName.parse("0:UtcTime"), NodeIds.UtcTime, null, null, null, null, false));
    Tree<DataType> guid =
        root.addChild(
            new ClientDataType(
                QualifiedName.parse("0:Guid"), NodeIds.Guid, null, null, null, null, false));
    Tree<DataType> byteString =
        root.addChild(
            new ClientDataType(
                QualifiedName.parse("0:ByteString"),
                NodeIds.ByteString,
                null,
                null,
                null,
                null,
                false));
    Tree<DataType> image =
        byteString.addChild(
            new ClientDataType(
                QualifiedName.parse("0:Image"), NodeIds.Image, null, null, null, null, true));
    Tree<DataType> imageBMP =
        image.addChild(
            new ClientDataType(
                QualifiedName.parse("0:ImageBMP"),
                NodeIds.ImageBMP,
                null,
                null,
                null,
                null,
                false));
    Tree<DataType> imageGIF =
        image.addChild(
            new ClientDataType(
                QualifiedName.parse("0:ImageGIF"),
                NodeIds.ImageGIF,
                null,
                null,
                null,
                null,
                false));
    Tree<DataType> imageJPG =
        image.addChild(
            new ClientDataType(
                QualifiedName.parse("0:ImageJPG"),
                NodeIds.ImageJPG,
                null,
                null,
                null,
                null,
                false));
    Tree<DataType> imagePNG =
        image.addChild(
            new ClientDataType(
                QualifiedName.parse("0:ImagePNG"),
                NodeIds.ImagePNG,
                null,
                null,
                null,
                null,
                false));
    Tree<DataType> applicationInstanceCertificate =
        byteString.addChild(
            new ClientDataType(
                QualifiedName.parse("0:ApplicationInstanceCertificate"),
                NodeIds.ApplicationInstanceCertificate,
                null,
                null,
                null,
                null,
                false));
    Tree<DataType> continuationPoint =
        byteString.addChild(
            new ClientDataType(
                QualifiedName.parse("0:ContinuationPoint"),
                NodeIds.ContinuationPoint,
                null,
                null,
                null,
                null,
                false));
    Tree<DataType> audioDataType =
        byteString.addChild(
            new ClientDataType(
                QualifiedName.parse("0:AudioDataType"),
                NodeIds.AudioDataType,
                null,
                null,
                null,
                null,
                false));
    Tree<DataType> xmlElement =
        root.addChild(
            new ClientDataType(
                QualifiedName.parse("0:XmlElement"),
                NodeIds.XmlElement,
                null,
                null,
                null,
                null,
                false));
    Tree<DataType> nodeId =
        root.addChild(
            new ClientDataType(
                QualifiedName.parse("0:NodeId"), NodeIds.NodeId, null, null, null, null, false));
    Tree<DataType> sessionAuthenticationToken =
        nodeId.addChild(
            new ClientDataType(
                QualifiedName.parse("0:SessionAuthenticationToken"),
                NodeIds.SessionAuthenticationToken,
                null,
                null,
                null,
                null,
                false));
    Tree<DataType> expandedNodeId =
        root.addChild(
            new ClientDataType(
                QualifiedName.parse("0:ExpandedNodeId"),
                NodeIds.ExpandedNodeId,
                null,
                null,
                null,
                null,
                false));
    Tree<DataType> statusCode =
        root.addChild(
            new ClientDataType(
                QualifiedName.parse("0:StatusCode"),
                NodeIds.StatusCode,
                null,
                null,
                null,
                null,
                false));
    Tree<DataType> qualifiedName =
        root.addChild(
            new ClientDataType(
                QualifiedName.parse("0:QualifiedName"),
                NodeIds.QualifiedName,
                null,
                null,
                null,
                null,
                false));
    Tree<DataType> localizedText =
        root.addChild(
            new ClientDataType(
                QualifiedName.parse("0:LocalizedText"),
                NodeIds.LocalizedText,
                null,
                null,
                null,
                null,
                false));
    Tree<DataType> structure =
        root.addChild(
            new ClientDataType(
                QualifiedName.parse("0:Structure"),
                NodeIds.Structure,
                null,
                null,
                null,
                null,
                true));
    createStructureNodes(structure);
    Tree<DataType> dataValue =
        root.addChild(
            new ClientDataType(
                QualifiedName.parse("0:DataValue"),
                NodeIds.DataValue,
                null,
                null,
                null,
                null,
                false));
    Tree<DataType> diagnosticInfo =
        root.addChild(
            new ClientDataType(
                QualifiedName.parse("0:DiagnosticInfo"),
                NodeIds.DiagnosticInfo,
                null,
                null,
                null,
                null,
                false));
    Tree<DataType> number =
        root.addChild(
            new ClientDataType(
                QualifiedName.parse("0:Number"), NodeIds.Number, null, null, null, null, true));
    Tree<DataType> floatType =
        number.addChild(
            new ClientDataType(
                QualifiedName.parse("0:Float"), NodeIds.Float, null, null, null, null, false));
    Tree<DataType> doubleType =
        number.addChild(
            new ClientDataType(
                QualifiedName.parse("0:Double"), NodeIds.Double, null, null, null, null, false));
    Tree<DataType> duration =
        doubleType.addChild(
            new ClientDataType(
                QualifiedName.parse("0:Duration"),
                NodeIds.Duration,
                null,
                null,
                null,
                null,
                false));
    Tree<DataType> integer =
        number.addChild(
            new ClientDataType(
                QualifiedName.parse("0:Integer"), NodeIds.Integer, null, null, null, null, true));
    Tree<DataType> sByte =
        integer.addChild(
            new ClientDataType(
                QualifiedName.parse("0:SByte"), NodeIds.SByte, null, null, null, null, false));
    Tree<DataType> int16 =
        integer.addChild(
            new ClientDataType(
                QualifiedName.parse("0:Int16"), NodeIds.Int16, null, null, null, null, false));
    Tree<DataType> int32 =
        integer.addChild(
            new ClientDataType(
                QualifiedName.parse("0:Int32"), NodeIds.Int32, null, null, null, null, false));
    Tree<DataType> int64 =
        integer.addChild(
            new ClientDataType(
                QualifiedName.parse("0:Int64"), NodeIds.Int64, null, null, null, null, false));
    Tree<DataType> uInteger =
        number.addChild(
            new ClientDataType(
                QualifiedName.parse("0:UInteger"), NodeIds.UInteger, null, null, null, null, true));
    Tree<DataType> byteType =
        uInteger.addChild(
            new ClientDataType(
                QualifiedName.parse("0:Byte"), NodeIds.Byte, null, null, null, null, false));
    Tree<DataType> accessLevelType =
        byteType.addChild(
            new ClientDataType(
                QualifiedName.parse("0:AccessLevelType"),
                NodeIds.AccessLevelType,
                null,
                null,
                null,
                null,
                false));
    Tree<DataType> eventNotifierType =
        byteType.addChild(
            new ClientDataType(
                QualifiedName.parse("0:EventNotifierType"),
                NodeIds.EventNotifierType,
                null,
                null,
                null,
                null,
                false));
    Tree<DataType> uInt16 =
        uInteger.addChild(
            new ClientDataType(
                QualifiedName.parse("0:UInt16"), NodeIds.UInt16, null, null, null, null, false));
    Tree<DataType> accessRestrictionType =
        uInt16.addChild(
            new ClientDataType(
                QualifiedName.parse("0:AccessRestrictionType"),
                NodeIds.AccessRestrictionType,
                null,
                null,
                null,
                null,
                false));
    Tree<DataType> dataSetFieldFlags =
        uInt16.addChild(
            new ClientDataType(
                QualifiedName.parse("0:DataSetFieldFlags"),
                NodeIds.DataSetFieldFlags,
                null,
                null,
                null,
                null,
                false));
    Tree<DataType> alarmMask =
        uInt16.addChild(
            new ClientDataType(
                QualifiedName.parse("0:AlarmMask"),
                NodeIds.AlarmMask,
                null,
                null,
                null,
                null,
                false));
    Tree<DataType> uInt32 =
        uInteger.addChild(
            new ClientDataType(
                QualifiedName.parse("0:UInt32"), NodeIds.UInt32, null, null, null, null, false));
    Tree<DataType> permissionType =
        uInt32.addChild(
            new ClientDataType(
                QualifiedName.parse("0:PermissionType"),
                NodeIds.PermissionType,
                null,
                null,
                null,
                null,
                false));
    Tree<DataType> integerId =
        uInt32.addChild(
            new ClientDataType(
                QualifiedName.parse("0:IntegerId"),
                NodeIds.IntegerId,
                null,
                null,
                null,
                null,
                false));
    Tree<DataType> counter =
        uInt32.addChild(
            new ClientDataType(
                QualifiedName.parse("0:Counter"), NodeIds.Counter, null, null, null, null, false));
    Tree<DataType> attributeWriteMask =
        uInt32.addChild(
            new ClientDataType(
                QualifiedName.parse("0:AttributeWriteMask"),
                NodeIds.AttributeWriteMask,
                null,
                null,
                null,
                null,
                false));
    Tree<DataType> accessLevelExType =
        uInt32.addChild(
            new ClientDataType(
                QualifiedName.parse("0:AccessLevelExType"),
                NodeIds.AccessLevelExType,
                null,
                null,
                null,
                null,
                false));
    Tree<DataType> dataSetFieldContentMask =
        uInt32.addChild(
            new ClientDataType(
                QualifiedName.parse("0:DataSetFieldContentMask"),
                NodeIds.DataSetFieldContentMask,
                null,
                null,
                null,
                null,
                false));
    Tree<DataType> uadpNetworkMessageContentMask =
        uInt32.addChild(
            new ClientDataType(
                QualifiedName.parse("0:UadpNetworkMessageContentMask"),
                NodeIds.UadpNetworkMessageContentMask,
                null,
                null,
                null,
                null,
                false));
    Tree<DataType> uadpDataSetMessageContentMask =
        uInt32.addChild(
            new ClientDataType(
                QualifiedName.parse("0:UadpDataSetMessageContentMask"),
                NodeIds.UadpDataSetMessageContentMask,
                null,
                null,
                null,
                null,
                false));
    Tree<DataType> jsonNetworkMessageContentMask =
        uInt32.addChild(
            new ClientDataType(
                QualifiedName.parse("0:JsonNetworkMessageContentMask"),
                NodeIds.JsonNetworkMessageContentMask,
                null,
                null,
                null,
                null,
                false));
    Tree<DataType> jsonDataSetMessageContentMask =
        uInt32.addChild(
            new ClientDataType(
                QualifiedName.parse("0:JsonDataSetMessageContentMask"),
                NodeIds.JsonDataSetMessageContentMask,
                null,
                null,
                null,
                null,
                false));
    Tree<DataType> index =
        uInt32.addChild(
            new ClientDataType(
                QualifiedName.parse("0:Index"), NodeIds.Index, null, null, null, null, false));
    Tree<DataType> lldpSystemCapabilitiesMap =
        uInt32.addChild(
            new ClientDataType(
                QualifiedName.parse("0:LldpSystemCapabilitiesMap"),
                NodeIds.LldpSystemCapabilitiesMap,
                null,
                null,
                null,
                null,
                false));
    Tree<DataType> versionTime =
        uInt32.addChild(
            new ClientDataType(
                QualifiedName.parse("0:VersionTime"),
                NodeIds.VersionTime,
                null,
                null,
                null,
                null,
                false));
    Tree<DataType> trustListValidationOptions =
        uInt32.addChild(
            new ClientDataType(
                QualifiedName.parse("0:TrustListValidationOptions"),
                NodeIds.TrustListValidationOptions,
                null,
                null,
                null,
                null,
                false));
    Tree<DataType> passwordOptionsMask =
        uInt32.addChild(
            new ClientDataType(
                QualifiedName.parse("0:PasswordOptionsMask"),
                NodeIds.PasswordOptionsMask,
                null,
                null,
                null,
                null,
                false));
    Tree<DataType> userConfigurationMask =
        uInt32.addChild(
            new ClientDataType(
                QualifiedName.parse("0:UserConfigurationMask"),
                NodeIds.UserConfigurationMask,
                null,
                null,
                null,
                null,
                false));
    Tree<DataType> pubSubConfigurationRefMask =
        uInt32.addChild(
            new ClientDataType(
                QualifiedName.parse("0:PubSubConfigurationRefMask"),
                NodeIds.PubSubConfigurationRefMask,
                null,
                null,
                null,
                null,
                false));
    Tree<DataType> handle =
        uInt32.addChild(
            new ClientDataType(
                QualifiedName.parse("0:Handle"), NodeIds.Handle, null, null, null, null, false));
    Tree<DataType> uInt64 =
        uInteger.addChild(
            new ClientDataType(
                QualifiedName.parse("0:UInt64"), NodeIds.UInt64, null, null, null, null, false));
    Tree<DataType> bitFieldMaskDataType =
        uInt64.addChild(
            new ClientDataType(
                QualifiedName.parse("0:BitFieldMaskDataType"),
                NodeIds.BitFieldMaskDataType,
                null,
                null,
                null,
                null,
                false));
    Tree<DataType> decimal =
        number.addChild(
            new ClientDataType(
                QualifiedName.parse("0:Decimal"), NodeIds.Decimal, null, null, null, null, false));
    Tree<DataType> enumeration =
        root.addChild(
            new ClientDataType(
                QualifiedName.parse("0:Enumeration"),
                NodeIds.Enumeration,
                null,
                null,
                null,
                new EnumDefinition(new EnumField[0]),
                true));
    createEnumerationNodes(enumeration);
    return root;
  }

  @SuppressWarnings("unused")
  private static void createStructureNodes(Tree<DataType> structure) {
    Tree<DataType> rolePermissionType =
        structure.addChild(
            new ClientDataType(
                QualifiedName.parse("0:RolePermissionType"),
                NodeIds.RolePermissionType,
                NodeIds.RolePermissionType_Encoding_DefaultBinary,
                NodeIds.RolePermissionType_Encoding_DefaultXml,
                NodeIds.RolePermissionType_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.RolePermissionType_Encoding_DefaultBinary,
                    NodeIds.Structure,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "RoleId",
                          LocalizedText.NULL_VALUE,
                          NodeIds.NodeId,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "Permissions",
                          LocalizedText.NULL_VALUE,
                          NodeIds.PermissionType,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> dataTypeDefinition =
        structure.addChild(
            new ClientDataType(
                QualifiedName.parse("0:DataTypeDefinition"),
                NodeIds.DataTypeDefinition,
                NodeIds.DataTypeDefinition_Encoding_DefaultBinary,
                NodeIds.DataTypeDefinition_Encoding_DefaultXml,
                NodeIds.DataTypeDefinition_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.DataTypeDefinition_Encoding_DefaultBinary,
                    NodeIds.Structure,
                    StructureType.Structure,
                    new StructureField[0]),
                true));
    Tree<DataType> structureDefinition =
        dataTypeDefinition.addChild(
            new ClientDataType(
                QualifiedName.parse("0:StructureDefinition"),
                NodeIds.StructureDefinition,
                NodeIds.StructureDefinition_Encoding_DefaultBinary,
                NodeIds.StructureDefinition_Encoding_DefaultXml,
                NodeIds.StructureDefinition_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.StructureDefinition_Encoding_DefaultBinary,
                    NodeIds.DataTypeDefinition,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "DefaultEncodingId",
                          LocalizedText.NULL_VALUE,
                          NodeIds.NodeId,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "BaseDataType",
                          LocalizedText.NULL_VALUE,
                          NodeIds.NodeId,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "StructureType",
                          LocalizedText.NULL_VALUE,
                          NodeIds.StructureType,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "Fields",
                          LocalizedText.NULL_VALUE,
                          NodeIds.StructureField,
                          1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> enumDefinition =
        dataTypeDefinition.addChild(
            new ClientDataType(
                QualifiedName.parse("0:EnumDefinition"),
                NodeIds.EnumDefinition,
                NodeIds.EnumDefinition_Encoding_DefaultBinary,
                NodeIds.EnumDefinition_Encoding_DefaultXml,
                NodeIds.EnumDefinition_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.EnumDefinition_Encoding_DefaultBinary,
                    NodeIds.DataTypeDefinition,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "Fields",
                          LocalizedText.NULL_VALUE,
                          NodeIds.EnumField,
                          1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> structureField =
        structure.addChild(
            new ClientDataType(
                QualifiedName.parse("0:StructureField"),
                NodeIds.StructureField,
                NodeIds.StructureField_Encoding_DefaultBinary,
                NodeIds.StructureField_Encoding_DefaultXml,
                NodeIds.StructureField_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.StructureField_Encoding_DefaultBinary,
                    NodeIds.Structure,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "Name",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "Description",
                          LocalizedText.NULL_VALUE,
                          NodeIds.LocalizedText,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "DataType",
                          LocalizedText.NULL_VALUE,
                          NodeIds.NodeId,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "ValueRank",
                          LocalizedText.NULL_VALUE,
                          NodeIds.Int32,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "ArrayDimensions",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UInt32,
                          1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "MaxStringLength",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UInt32,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "IsOptional",
                          LocalizedText.NULL_VALUE,
                          NodeIds.Boolean,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> node =
        structure.addChild(
            new ClientDataType(
                QualifiedName.parse("0:Node"),
                NodeIds.Node,
                NodeIds.Node_Encoding_DefaultBinary,
                NodeIds.Node_Encoding_DefaultXml,
                NodeIds.Node_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.Node_Encoding_DefaultBinary,
                    NodeIds.Structure,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "NodeId",
                          LocalizedText.NULL_VALUE,
                          NodeIds.NodeId,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "NodeClass",
                          LocalizedText.NULL_VALUE,
                          NodeIds.NodeClass,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "BrowseName",
                          LocalizedText.NULL_VALUE,
                          NodeIds.QualifiedName,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "DisplayName",
                          LocalizedText.NULL_VALUE,
                          NodeIds.LocalizedText,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "Description",
                          LocalizedText.NULL_VALUE,
                          NodeIds.LocalizedText,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "WriteMask",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UInt32,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "UserWriteMask",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UInt32,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "RolePermissions",
                          LocalizedText.NULL_VALUE,
                          NodeIds.RolePermissionType,
                          1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "UserRolePermissions",
                          LocalizedText.NULL_VALUE,
                          NodeIds.RolePermissionType,
                          1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "AccessRestrictions",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UInt16,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "References",
                          LocalizedText.NULL_VALUE,
                          NodeIds.ReferenceNode,
                          1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> instanceNode =
        node.addChild(
            new ClientDataType(
                QualifiedName.parse("0:InstanceNode"),
                NodeIds.InstanceNode,
                NodeIds.InstanceNode_Encoding_DefaultBinary,
                NodeIds.InstanceNode_Encoding_DefaultXml,
                NodeIds.InstanceNode_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.InstanceNode_Encoding_DefaultBinary,
                    NodeIds.Node,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "References",
                          LocalizedText.NULL_VALUE,
                          NodeIds.ReferenceNode,
                          1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "AccessRestrictions",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UInt16,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "UserRolePermissions",
                          LocalizedText.NULL_VALUE,
                          NodeIds.RolePermissionType,
                          1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "RolePermissions",
                          LocalizedText.NULL_VALUE,
                          NodeIds.RolePermissionType,
                          1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "UserWriteMask",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UInt32,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "WriteMask",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UInt32,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "Description",
                          LocalizedText.NULL_VALUE,
                          NodeIds.LocalizedText,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "DisplayName",
                          LocalizedText.NULL_VALUE,
                          NodeIds.LocalizedText,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "BrowseName",
                          LocalizedText.NULL_VALUE,
                          NodeIds.QualifiedName,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "NodeClass",
                          LocalizedText.NULL_VALUE,
                          NodeIds.NodeClass,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "NodeId",
                          LocalizedText.NULL_VALUE,
                          NodeIds.NodeId,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> objectNode =
        instanceNode.addChild(
            new ClientDataType(
                QualifiedName.parse("0:ObjectNode"),
                NodeIds.ObjectNode,
                NodeIds.ObjectNode_Encoding_DefaultBinary,
                NodeIds.ObjectNode_Encoding_DefaultXml,
                NodeIds.ObjectNode_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.ObjectNode_Encoding_DefaultBinary,
                    NodeIds.InstanceNode,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "References",
                          LocalizedText.NULL_VALUE,
                          NodeIds.ReferenceNode,
                          1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "AccessRestrictions",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UInt16,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "UserRolePermissions",
                          LocalizedText.NULL_VALUE,
                          NodeIds.RolePermissionType,
                          1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "RolePermissions",
                          LocalizedText.NULL_VALUE,
                          NodeIds.RolePermissionType,
                          1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "UserWriteMask",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UInt32,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "WriteMask",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UInt32,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "Description",
                          LocalizedText.NULL_VALUE,
                          NodeIds.LocalizedText,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "DisplayName",
                          LocalizedText.NULL_VALUE,
                          NodeIds.LocalizedText,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "BrowseName",
                          LocalizedText.NULL_VALUE,
                          NodeIds.QualifiedName,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "NodeClass",
                          LocalizedText.NULL_VALUE,
                          NodeIds.NodeClass,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "NodeId",
                          LocalizedText.NULL_VALUE,
                          NodeIds.NodeId,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "EventNotifier",
                          LocalizedText.NULL_VALUE,
                          NodeIds.Byte,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> variableNode =
        instanceNode.addChild(
            new ClientDataType(
                QualifiedName.parse("0:VariableNode"),
                NodeIds.VariableNode,
                NodeIds.VariableNode_Encoding_DefaultBinary,
                NodeIds.VariableNode_Encoding_DefaultXml,
                NodeIds.VariableNode_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.VariableNode_Encoding_DefaultBinary,
                    NodeIds.InstanceNode,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "References",
                          LocalizedText.NULL_VALUE,
                          NodeIds.ReferenceNode,
                          1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "AccessRestrictions",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UInt16,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "UserRolePermissions",
                          LocalizedText.NULL_VALUE,
                          NodeIds.RolePermissionType,
                          1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "RolePermissions",
                          LocalizedText.NULL_VALUE,
                          NodeIds.RolePermissionType,
                          1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "UserWriteMask",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UInt32,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "WriteMask",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UInt32,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "Description",
                          LocalizedText.NULL_VALUE,
                          NodeIds.LocalizedText,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "DisplayName",
                          LocalizedText.NULL_VALUE,
                          NodeIds.LocalizedText,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "BrowseName",
                          LocalizedText.NULL_VALUE,
                          NodeIds.QualifiedName,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "NodeClass",
                          LocalizedText.NULL_VALUE,
                          NodeIds.NodeClass,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "NodeId",
                          LocalizedText.NULL_VALUE,
                          NodeIds.NodeId,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "Value",
                          LocalizedText.NULL_VALUE,
                          NodeIds.BaseDataType,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "DataType",
                          LocalizedText.NULL_VALUE,
                          NodeIds.NodeId,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "ValueRank",
                          LocalizedText.NULL_VALUE,
                          NodeIds.Int32,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "ArrayDimensions",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UInt32,
                          1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "AccessLevel",
                          LocalizedText.NULL_VALUE,
                          NodeIds.Byte,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "UserAccessLevel",
                          LocalizedText.NULL_VALUE,
                          NodeIds.Byte,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "MinimumSamplingInterval",
                          LocalizedText.NULL_VALUE,
                          NodeIds.Duration,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "Historizing",
                          LocalizedText.NULL_VALUE,
                          NodeIds.Boolean,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "AccessLevelEx",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UInt32,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> methodNode =
        instanceNode.addChild(
            new ClientDataType(
                QualifiedName.parse("0:MethodNode"),
                NodeIds.MethodNode,
                NodeIds.MethodNode_Encoding_DefaultBinary,
                NodeIds.MethodNode_Encoding_DefaultXml,
                NodeIds.MethodNode_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.MethodNode_Encoding_DefaultBinary,
                    NodeIds.InstanceNode,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "References",
                          LocalizedText.NULL_VALUE,
                          NodeIds.ReferenceNode,
                          1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "AccessRestrictions",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UInt16,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "UserRolePermissions",
                          LocalizedText.NULL_VALUE,
                          NodeIds.RolePermissionType,
                          1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "RolePermissions",
                          LocalizedText.NULL_VALUE,
                          NodeIds.RolePermissionType,
                          1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "UserWriteMask",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UInt32,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "WriteMask",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UInt32,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "Description",
                          LocalizedText.NULL_VALUE,
                          NodeIds.LocalizedText,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "DisplayName",
                          LocalizedText.NULL_VALUE,
                          NodeIds.LocalizedText,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "BrowseName",
                          LocalizedText.NULL_VALUE,
                          NodeIds.QualifiedName,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "NodeClass",
                          LocalizedText.NULL_VALUE,
                          NodeIds.NodeClass,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "NodeId",
                          LocalizedText.NULL_VALUE,
                          NodeIds.NodeId,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "Executable",
                          LocalizedText.NULL_VALUE,
                          NodeIds.Boolean,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "UserExecutable",
                          LocalizedText.NULL_VALUE,
                          NodeIds.Boolean,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> viewNode =
        instanceNode.addChild(
            new ClientDataType(
                QualifiedName.parse("0:ViewNode"),
                NodeIds.ViewNode,
                NodeIds.ViewNode_Encoding_DefaultBinary,
                NodeIds.ViewNode_Encoding_DefaultXml,
                NodeIds.ViewNode_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.ViewNode_Encoding_DefaultBinary,
                    NodeIds.InstanceNode,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "References",
                          LocalizedText.NULL_VALUE,
                          NodeIds.ReferenceNode,
                          1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "AccessRestrictions",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UInt16,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "UserRolePermissions",
                          LocalizedText.NULL_VALUE,
                          NodeIds.RolePermissionType,
                          1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "RolePermissions",
                          LocalizedText.NULL_VALUE,
                          NodeIds.RolePermissionType,
                          1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "UserWriteMask",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UInt32,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "WriteMask",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UInt32,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "Description",
                          LocalizedText.NULL_VALUE,
                          NodeIds.LocalizedText,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "DisplayName",
                          LocalizedText.NULL_VALUE,
                          NodeIds.LocalizedText,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "BrowseName",
                          LocalizedText.NULL_VALUE,
                          NodeIds.QualifiedName,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "NodeClass",
                          LocalizedText.NULL_VALUE,
                          NodeIds.NodeClass,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "NodeId",
                          LocalizedText.NULL_VALUE,
                          NodeIds.NodeId,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "ContainsNoLoops",
                          LocalizedText.NULL_VALUE,
                          NodeIds.Boolean,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "EventNotifier",
                          LocalizedText.NULL_VALUE,
                          NodeIds.Byte,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> typeNode =
        node.addChild(
            new ClientDataType(
                QualifiedName.parse("0:TypeNode"),
                NodeIds.TypeNode,
                NodeIds.TypeNode_Encoding_DefaultBinary,
                NodeIds.TypeNode_Encoding_DefaultXml,
                NodeIds.TypeNode_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.TypeNode_Encoding_DefaultBinary,
                    NodeIds.Node,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "References",
                          LocalizedText.NULL_VALUE,
                          NodeIds.ReferenceNode,
                          1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "AccessRestrictions",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UInt16,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "UserRolePermissions",
                          LocalizedText.NULL_VALUE,
                          NodeIds.RolePermissionType,
                          1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "RolePermissions",
                          LocalizedText.NULL_VALUE,
                          NodeIds.RolePermissionType,
                          1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "UserWriteMask",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UInt32,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "WriteMask",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UInt32,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "Description",
                          LocalizedText.NULL_VALUE,
                          NodeIds.LocalizedText,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "DisplayName",
                          LocalizedText.NULL_VALUE,
                          NodeIds.LocalizedText,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "BrowseName",
                          LocalizedText.NULL_VALUE,
                          NodeIds.QualifiedName,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "NodeClass",
                          LocalizedText.NULL_VALUE,
                          NodeIds.NodeClass,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "NodeId",
                          LocalizedText.NULL_VALUE,
                          NodeIds.NodeId,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> objectTypeNode =
        typeNode.addChild(
            new ClientDataType(
                QualifiedName.parse("0:ObjectTypeNode"),
                NodeIds.ObjectTypeNode,
                NodeIds.ObjectTypeNode_Encoding_DefaultBinary,
                NodeIds.ObjectTypeNode_Encoding_DefaultXml,
                NodeIds.ObjectTypeNode_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.ObjectTypeNode_Encoding_DefaultBinary,
                    NodeIds.TypeNode,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "References",
                          LocalizedText.NULL_VALUE,
                          NodeIds.ReferenceNode,
                          1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "AccessRestrictions",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UInt16,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "UserRolePermissions",
                          LocalizedText.NULL_VALUE,
                          NodeIds.RolePermissionType,
                          1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "RolePermissions",
                          LocalizedText.NULL_VALUE,
                          NodeIds.RolePermissionType,
                          1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "UserWriteMask",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UInt32,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "WriteMask",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UInt32,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "Description",
                          LocalizedText.NULL_VALUE,
                          NodeIds.LocalizedText,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "DisplayName",
                          LocalizedText.NULL_VALUE,
                          NodeIds.LocalizedText,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "BrowseName",
                          LocalizedText.NULL_VALUE,
                          NodeIds.QualifiedName,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "NodeClass",
                          LocalizedText.NULL_VALUE,
                          NodeIds.NodeClass,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "NodeId",
                          LocalizedText.NULL_VALUE,
                          NodeIds.NodeId,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "IsAbstract",
                          LocalizedText.NULL_VALUE,
                          NodeIds.Boolean,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> variableTypeNode =
        typeNode.addChild(
            new ClientDataType(
                QualifiedName.parse("0:VariableTypeNode"),
                NodeIds.VariableTypeNode,
                NodeIds.VariableTypeNode_Encoding_DefaultBinary,
                NodeIds.VariableTypeNode_Encoding_DefaultXml,
                NodeIds.VariableTypeNode_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.VariableTypeNode_Encoding_DefaultBinary,
                    NodeIds.TypeNode,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "References",
                          LocalizedText.NULL_VALUE,
                          NodeIds.ReferenceNode,
                          1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "AccessRestrictions",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UInt16,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "UserRolePermissions",
                          LocalizedText.NULL_VALUE,
                          NodeIds.RolePermissionType,
                          1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "RolePermissions",
                          LocalizedText.NULL_VALUE,
                          NodeIds.RolePermissionType,
                          1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "UserWriteMask",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UInt32,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "WriteMask",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UInt32,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "Description",
                          LocalizedText.NULL_VALUE,
                          NodeIds.LocalizedText,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "DisplayName",
                          LocalizedText.NULL_VALUE,
                          NodeIds.LocalizedText,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "BrowseName",
                          LocalizedText.NULL_VALUE,
                          NodeIds.QualifiedName,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "NodeClass",
                          LocalizedText.NULL_VALUE,
                          NodeIds.NodeClass,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "NodeId",
                          LocalizedText.NULL_VALUE,
                          NodeIds.NodeId,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "Value",
                          LocalizedText.NULL_VALUE,
                          NodeIds.BaseDataType,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "DataType",
                          LocalizedText.NULL_VALUE,
                          NodeIds.NodeId,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "ValueRank",
                          LocalizedText.NULL_VALUE,
                          NodeIds.Int32,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "ArrayDimensions",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UInt32,
                          1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "IsAbstract",
                          LocalizedText.NULL_VALUE,
                          NodeIds.Boolean,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> referenceTypeNode =
        typeNode.addChild(
            new ClientDataType(
                QualifiedName.parse("0:ReferenceTypeNode"),
                NodeIds.ReferenceTypeNode,
                NodeIds.ReferenceTypeNode_Encoding_DefaultBinary,
                NodeIds.ReferenceTypeNode_Encoding_DefaultXml,
                NodeIds.ReferenceTypeNode_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.ReferenceTypeNode_Encoding_DefaultBinary,
                    NodeIds.TypeNode,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "References",
                          LocalizedText.NULL_VALUE,
                          NodeIds.ReferenceNode,
                          1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "AccessRestrictions",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UInt16,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "UserRolePermissions",
                          LocalizedText.NULL_VALUE,
                          NodeIds.RolePermissionType,
                          1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "RolePermissions",
                          LocalizedText.NULL_VALUE,
                          NodeIds.RolePermissionType,
                          1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "UserWriteMask",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UInt32,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "WriteMask",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UInt32,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "Description",
                          LocalizedText.NULL_VALUE,
                          NodeIds.LocalizedText,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "DisplayName",
                          LocalizedText.NULL_VALUE,
                          NodeIds.LocalizedText,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "BrowseName",
                          LocalizedText.NULL_VALUE,
                          NodeIds.QualifiedName,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "NodeClass",
                          LocalizedText.NULL_VALUE,
                          NodeIds.NodeClass,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "NodeId",
                          LocalizedText.NULL_VALUE,
                          NodeIds.NodeId,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "IsAbstract",
                          LocalizedText.NULL_VALUE,
                          NodeIds.Boolean,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "Symmetric",
                          LocalizedText.NULL_VALUE,
                          NodeIds.Boolean,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "InverseName",
                          LocalizedText.NULL_VALUE,
                          NodeIds.LocalizedText,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> dataTypeNode =
        typeNode.addChild(
            new ClientDataType(
                QualifiedName.parse("0:DataTypeNode"),
                NodeIds.DataTypeNode,
                NodeIds.DataTypeNode_Encoding_DefaultBinary,
                NodeIds.DataTypeNode_Encoding_DefaultXml,
                NodeIds.DataTypeNode_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.DataTypeNode_Encoding_DefaultBinary,
                    NodeIds.TypeNode,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "References",
                          LocalizedText.NULL_VALUE,
                          NodeIds.ReferenceNode,
                          1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "AccessRestrictions",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UInt16,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "UserRolePermissions",
                          LocalizedText.NULL_VALUE,
                          NodeIds.RolePermissionType,
                          1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "RolePermissions",
                          LocalizedText.NULL_VALUE,
                          NodeIds.RolePermissionType,
                          1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "UserWriteMask",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UInt32,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "WriteMask",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UInt32,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "Description",
                          LocalizedText.NULL_VALUE,
                          NodeIds.LocalizedText,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "DisplayName",
                          LocalizedText.NULL_VALUE,
                          NodeIds.LocalizedText,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "BrowseName",
                          LocalizedText.NULL_VALUE,
                          NodeIds.QualifiedName,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "NodeClass",
                          LocalizedText.NULL_VALUE,
                          NodeIds.NodeClass,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "NodeId",
                          LocalizedText.NULL_VALUE,
                          NodeIds.NodeId,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "IsAbstract",
                          LocalizedText.NULL_VALUE,
                          NodeIds.Boolean,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "DataTypeDefinition",
                          LocalizedText.NULL_VALUE,
                          NodeIds.Structure,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> referenceNode =
        structure.addChild(
            new ClientDataType(
                QualifiedName.parse("0:ReferenceNode"),
                NodeIds.ReferenceNode,
                NodeIds.ReferenceNode_Encoding_DefaultBinary,
                NodeIds.ReferenceNode_Encoding_DefaultXml,
                NodeIds.ReferenceNode_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.ReferenceNode_Encoding_DefaultBinary,
                    NodeIds.Structure,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "ReferenceTypeId",
                          LocalizedText.NULL_VALUE,
                          NodeIds.NodeId,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "IsInverse",
                          LocalizedText.NULL_VALUE,
                          NodeIds.Boolean,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "TargetId",
                          LocalizedText.NULL_VALUE,
                          NodeIds.ExpandedNodeId,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> argument =
        structure.addChild(
            new ClientDataType(
                QualifiedName.parse("0:Argument"),
                NodeIds.Argument,
                NodeIds.Argument_Encoding_DefaultBinary,
                NodeIds.Argument_Encoding_DefaultXml,
                NodeIds.Argument_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.Argument_Encoding_DefaultBinary,
                    NodeIds.Structure,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "Name",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "DataType",
                          LocalizedText.NULL_VALUE,
                          NodeIds.NodeId,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "ValueRank",
                          LocalizedText.NULL_VALUE,
                          NodeIds.Int32,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "ArrayDimensions",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UInt32,
                          1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "Description",
                          LocalizedText.NULL_VALUE,
                          NodeIds.LocalizedText,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> statusResult =
        structure.addChild(
            new ClientDataType(
                QualifiedName.parse("0:StatusResult"),
                NodeIds.StatusResult,
                NodeIds.StatusResult_Encoding_DefaultBinary,
                NodeIds.StatusResult_Encoding_DefaultXml,
                NodeIds.StatusResult_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.StatusResult_Encoding_DefaultBinary,
                    NodeIds.Structure,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "StatusCode",
                          LocalizedText.NULL_VALUE,
                          NodeIds.StatusCode,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "DiagnosticInfo",
                          LocalizedText.NULL_VALUE,
                          NodeIds.DiagnosticInfo,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> userTokenPolicy =
        structure.addChild(
            new ClientDataType(
                QualifiedName.parse("0:UserTokenPolicy"),
                NodeIds.UserTokenPolicy,
                NodeIds.UserTokenPolicy_Encoding_DefaultBinary,
                NodeIds.UserTokenPolicy_Encoding_DefaultXml,
                NodeIds.UserTokenPolicy_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.UserTokenPolicy_Encoding_DefaultBinary,
                    NodeIds.Structure,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "PolicyId",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "TokenType",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UserTokenType,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "IssuedTokenType",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "IssuerEndpointUrl",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "SecurityPolicyUri",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> applicationDescription =
        structure.addChild(
            new ClientDataType(
                QualifiedName.parse("0:ApplicationDescription"),
                NodeIds.ApplicationDescription,
                NodeIds.ApplicationDescription_Encoding_DefaultBinary,
                NodeIds.ApplicationDescription_Encoding_DefaultXml,
                NodeIds.ApplicationDescription_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.ApplicationDescription_Encoding_DefaultBinary,
                    NodeIds.Structure,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "ApplicationUri",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "ProductUri",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "ApplicationName",
                          LocalizedText.NULL_VALUE,
                          NodeIds.LocalizedText,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "ApplicationType",
                          LocalizedText.NULL_VALUE,
                          NodeIds.ApplicationType,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "GatewayServerUri",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "DiscoveryProfileUri",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "DiscoveryUrls",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> endpointDescription =
        structure.addChild(
            new ClientDataType(
                QualifiedName.parse("0:EndpointDescription"),
                NodeIds.EndpointDescription,
                NodeIds.EndpointDescription_Encoding_DefaultBinary,
                NodeIds.EndpointDescription_Encoding_DefaultXml,
                NodeIds.EndpointDescription_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.EndpointDescription_Encoding_DefaultBinary,
                    NodeIds.Structure,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "EndpointUrl",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "Server",
                          LocalizedText.NULL_VALUE,
                          NodeIds.ApplicationDescription,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "ServerCertificate",
                          LocalizedText.NULL_VALUE,
                          NodeIds.ApplicationInstanceCertificate,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "SecurityMode",
                          LocalizedText.NULL_VALUE,
                          NodeIds.MessageSecurityMode,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "SecurityPolicyUri",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "UserIdentityTokens",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UserTokenPolicy,
                          1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "TransportProfileUri",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "SecurityLevel",
                          LocalizedText.NULL_VALUE,
                          NodeIds.Byte,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> userIdentityToken =
        structure.addChild(
            new ClientDataType(
                QualifiedName.parse("0:UserIdentityToken"),
                NodeIds.UserIdentityToken,
                NodeIds.UserIdentityToken_Encoding_DefaultBinary,
                NodeIds.UserIdentityToken_Encoding_DefaultXml,
                NodeIds.UserIdentityToken_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.UserIdentityToken_Encoding_DefaultBinary,
                    NodeIds.Structure,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "PolicyId",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                true));
    Tree<DataType> anonymousIdentityToken =
        userIdentityToken.addChild(
            new ClientDataType(
                QualifiedName.parse("0:AnonymousIdentityToken"),
                NodeIds.AnonymousIdentityToken,
                NodeIds.AnonymousIdentityToken_Encoding_DefaultBinary,
                NodeIds.AnonymousIdentityToken_Encoding_DefaultXml,
                NodeIds.AnonymousIdentityToken_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.AnonymousIdentityToken_Encoding_DefaultBinary,
                    NodeIds.UserIdentityToken,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "PolicyId",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> userNameIdentityToken =
        userIdentityToken.addChild(
            new ClientDataType(
                QualifiedName.parse("0:UserNameIdentityToken"),
                NodeIds.UserNameIdentityToken,
                NodeIds.UserNameIdentityToken_Encoding_DefaultBinary,
                NodeIds.UserNameIdentityToken_Encoding_DefaultXml,
                NodeIds.UserNameIdentityToken_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.UserNameIdentityToken_Encoding_DefaultBinary,
                    NodeIds.UserIdentityToken,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "PolicyId",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "UserName",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "Password",
                          LocalizedText.NULL_VALUE,
                          NodeIds.ByteString,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "EncryptionAlgorithm",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> x509IdentityToken =
        userIdentityToken.addChild(
            new ClientDataType(
                QualifiedName.parse("0:X509IdentityToken"),
                NodeIds.X509IdentityToken,
                NodeIds.X509IdentityToken_Encoding_DefaultBinary,
                NodeIds.X509IdentityToken_Encoding_DefaultXml,
                NodeIds.X509IdentityToken_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.X509IdentityToken_Encoding_DefaultBinary,
                    NodeIds.UserIdentityToken,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "PolicyId",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "CertificateData",
                          LocalizedText.NULL_VALUE,
                          NodeIds.ByteString,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> issuedIdentityToken =
        userIdentityToken.addChild(
            new ClientDataType(
                QualifiedName.parse("0:IssuedIdentityToken"),
                NodeIds.IssuedIdentityToken,
                NodeIds.IssuedIdentityToken_Encoding_DefaultBinary,
                NodeIds.IssuedIdentityToken_Encoding_DefaultXml,
                NodeIds.IssuedIdentityToken_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.IssuedIdentityToken_Encoding_DefaultBinary,
                    NodeIds.UserIdentityToken,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "PolicyId",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "TokenData",
                          LocalizedText.NULL_VALUE,
                          NodeIds.ByteString,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "EncryptionAlgorithm",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> endpointConfiguration =
        structure.addChild(
            new ClientDataType(
                QualifiedName.parse("0:EndpointConfiguration"),
                NodeIds.EndpointConfiguration,
                NodeIds.EndpointConfiguration_Encoding_DefaultBinary,
                NodeIds.EndpointConfiguration_Encoding_DefaultXml,
                NodeIds.EndpointConfiguration_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.EndpointConfiguration_Encoding_DefaultBinary,
                    NodeIds.Structure,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "OperationTimeout",
                          LocalizedText.NULL_VALUE,
                          NodeIds.Int32,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "UseBinaryEncoding",
                          LocalizedText.NULL_VALUE,
                          NodeIds.Boolean,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "MaxStringLength",
                          LocalizedText.NULL_VALUE,
                          NodeIds.Int32,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "MaxByteStringLength",
                          LocalizedText.NULL_VALUE,
                          NodeIds.Int32,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "MaxArrayLength",
                          LocalizedText.NULL_VALUE,
                          NodeIds.Int32,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "MaxMessageSize",
                          LocalizedText.NULL_VALUE,
                          NodeIds.Int32,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "MaxBufferSize",
                          LocalizedText.NULL_VALUE,
                          NodeIds.Int32,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "ChannelLifetime",
                          LocalizedText.NULL_VALUE,
                          NodeIds.Int32,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "SecurityTokenLifetime",
                          LocalizedText.NULL_VALUE,
                          NodeIds.Int32,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> buildInfo =
        structure.addChild(
            new ClientDataType(
                QualifiedName.parse("0:BuildInfo"),
                NodeIds.BuildInfo,
                NodeIds.BuildInfo_Encoding_DefaultBinary,
                NodeIds.BuildInfo_Encoding_DefaultXml,
                NodeIds.BuildInfo_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.BuildInfo_Encoding_DefaultBinary,
                    NodeIds.Structure,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "ProductUri",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "ManufacturerName",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "ProductName",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "SoftwareVersion",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "BuildNumber",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "BuildDate",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UtcTime,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> signedSoftwareCertificate =
        structure.addChild(
            new ClientDataType(
                QualifiedName.parse("0:SignedSoftwareCertificate"),
                NodeIds.SignedSoftwareCertificate,
                NodeIds.SignedSoftwareCertificate_Encoding_DefaultBinary,
                NodeIds.SignedSoftwareCertificate_Encoding_DefaultXml,
                NodeIds.SignedSoftwareCertificate_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.SignedSoftwareCertificate_Encoding_DefaultBinary,
                    NodeIds.Structure,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "CertificateData",
                          LocalizedText.NULL_VALUE,
                          NodeIds.ByteString,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "Signature",
                          LocalizedText.NULL_VALUE,
                          NodeIds.ByteString,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> addNodesItem =
        structure.addChild(
            new ClientDataType(
                QualifiedName.parse("0:AddNodesItem"),
                NodeIds.AddNodesItem,
                NodeIds.AddNodesItem_Encoding_DefaultBinary,
                NodeIds.AddNodesItem_Encoding_DefaultXml,
                NodeIds.AddNodesItem_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.AddNodesItem_Encoding_DefaultBinary,
                    NodeIds.Structure,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "ParentNodeId",
                          LocalizedText.NULL_VALUE,
                          NodeIds.ExpandedNodeId,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "ReferenceTypeId",
                          LocalizedText.NULL_VALUE,
                          NodeIds.NodeId,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "RequestedNewNodeId",
                          LocalizedText.NULL_VALUE,
                          NodeIds.ExpandedNodeId,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "BrowseName",
                          LocalizedText.NULL_VALUE,
                          NodeIds.QualifiedName,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "NodeClass",
                          LocalizedText.NULL_VALUE,
                          NodeIds.NodeClass,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "NodeAttributes",
                          LocalizedText.NULL_VALUE,
                          NodeIds.Structure,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "TypeDefinition",
                          LocalizedText.NULL_VALUE,
                          NodeIds.ExpandedNodeId,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> addReferencesItem =
        structure.addChild(
            new ClientDataType(
                QualifiedName.parse("0:AddReferencesItem"),
                NodeIds.AddReferencesItem,
                NodeIds.AddReferencesItem_Encoding_DefaultBinary,
                NodeIds.AddReferencesItem_Encoding_DefaultXml,
                NodeIds.AddReferencesItem_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.AddReferencesItem_Encoding_DefaultBinary,
                    NodeIds.Structure,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "SourceNodeId",
                          LocalizedText.NULL_VALUE,
                          NodeIds.NodeId,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "ReferenceTypeId",
                          LocalizedText.NULL_VALUE,
                          NodeIds.NodeId,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "IsForward",
                          LocalizedText.NULL_VALUE,
                          NodeIds.Boolean,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "TargetServerUri",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "TargetNodeId",
                          LocalizedText.NULL_VALUE,
                          NodeIds.ExpandedNodeId,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "TargetNodeClass",
                          LocalizedText.NULL_VALUE,
                          NodeIds.NodeClass,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> deleteNodesItem =
        structure.addChild(
            new ClientDataType(
                QualifiedName.parse("0:DeleteNodesItem"),
                NodeIds.DeleteNodesItem,
                NodeIds.DeleteNodesItem_Encoding_DefaultBinary,
                NodeIds.DeleteNodesItem_Encoding_DefaultXml,
                NodeIds.DeleteNodesItem_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.DeleteNodesItem_Encoding_DefaultBinary,
                    NodeIds.Structure,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "NodeId",
                          LocalizedText.NULL_VALUE,
                          NodeIds.NodeId,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "DeleteTargetReferences",
                          LocalizedText.NULL_VALUE,
                          NodeIds.Boolean,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> deleteReferencesItem =
        structure.addChild(
            new ClientDataType(
                QualifiedName.parse("0:DeleteReferencesItem"),
                NodeIds.DeleteReferencesItem,
                NodeIds.DeleteReferencesItem_Encoding_DefaultBinary,
                NodeIds.DeleteReferencesItem_Encoding_DefaultXml,
                NodeIds.DeleteReferencesItem_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.DeleteReferencesItem_Encoding_DefaultBinary,
                    NodeIds.Structure,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "SourceNodeId",
                          LocalizedText.NULL_VALUE,
                          NodeIds.NodeId,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "ReferenceTypeId",
                          LocalizedText.NULL_VALUE,
                          NodeIds.NodeId,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "IsForward",
                          LocalizedText.NULL_VALUE,
                          NodeIds.Boolean,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "TargetNodeId",
                          LocalizedText.NULL_VALUE,
                          NodeIds.ExpandedNodeId,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "DeleteBidirectional",
                          LocalizedText.NULL_VALUE,
                          NodeIds.Boolean,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> registeredServer =
        structure.addChild(
            new ClientDataType(
                QualifiedName.parse("0:RegisteredServer"),
                NodeIds.RegisteredServer,
                NodeIds.RegisteredServer_Encoding_DefaultBinary,
                NodeIds.RegisteredServer_Encoding_DefaultXml,
                NodeIds.RegisteredServer_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.RegisteredServer_Encoding_DefaultBinary,
                    NodeIds.Structure,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "ServerUri",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "ProductUri",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "ServerNames",
                          LocalizedText.NULL_VALUE,
                          NodeIds.LocalizedText,
                          1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "ServerType",
                          LocalizedText.NULL_VALUE,
                          NodeIds.ApplicationType,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "GatewayServerUri",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "DiscoveryUrls",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "SemaphoreFilePath",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "IsOnline",
                          LocalizedText.NULL_VALUE,
                          NodeIds.Boolean,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> relativePathElement =
        structure.addChild(
            new ClientDataType(
                QualifiedName.parse("0:RelativePathElement"),
                NodeIds.RelativePathElement,
                NodeIds.RelativePathElement_Encoding_DefaultBinary,
                NodeIds.RelativePathElement_Encoding_DefaultXml,
                NodeIds.RelativePathElement_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.RelativePathElement_Encoding_DefaultBinary,
                    NodeIds.Structure,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "ReferenceTypeId",
                          LocalizedText.NULL_VALUE,
                          NodeIds.NodeId,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "IsInverse",
                          LocalizedText.NULL_VALUE,
                          NodeIds.Boolean,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "IncludeSubtypes",
                          LocalizedText.NULL_VALUE,
                          NodeIds.Boolean,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "TargetName",
                          LocalizedText.NULL_VALUE,
                          NodeIds.QualifiedName,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> relativePath =
        structure.addChild(
            new ClientDataType(
                QualifiedName.parse("0:RelativePath"),
                NodeIds.RelativePath,
                NodeIds.RelativePath_Encoding_DefaultBinary,
                NodeIds.RelativePath_Encoding_DefaultXml,
                NodeIds.RelativePath_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.RelativePath_Encoding_DefaultBinary,
                    NodeIds.Structure,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "Elements",
                          LocalizedText.NULL_VALUE,
                          NodeIds.RelativePathElement,
                          1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> contentFilterElement =
        structure.addChild(
            new ClientDataType(
                QualifiedName.parse("0:ContentFilterElement"),
                NodeIds.ContentFilterElement,
                NodeIds.ContentFilterElement_Encoding_DefaultBinary,
                NodeIds.ContentFilterElement_Encoding_DefaultXml,
                NodeIds.ContentFilterElement_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.ContentFilterElement_Encoding_DefaultBinary,
                    NodeIds.Structure,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "FilterOperator",
                          LocalizedText.NULL_VALUE,
                          NodeIds.FilterOperator,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "FilterOperands",
                          LocalizedText.NULL_VALUE,
                          NodeIds.Structure,
                          1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> contentFilter =
        structure.addChild(
            new ClientDataType(
                QualifiedName.parse("0:ContentFilter"),
                NodeIds.ContentFilter,
                NodeIds.ContentFilter_Encoding_DefaultBinary,
                NodeIds.ContentFilter_Encoding_DefaultXml,
                NodeIds.ContentFilter_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.ContentFilter_Encoding_DefaultBinary,
                    NodeIds.Structure,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "Elements",
                          LocalizedText.NULL_VALUE,
                          NodeIds.ContentFilterElement,
                          1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> filterOperand =
        structure.addChild(
            new ClientDataType(
                QualifiedName.parse("0:FilterOperand"),
                NodeIds.FilterOperand,
                NodeIds.FilterOperand_Encoding_DefaultBinary,
                NodeIds.FilterOperand_Encoding_DefaultXml,
                NodeIds.FilterOperand_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.FilterOperand_Encoding_DefaultBinary,
                    NodeIds.Structure,
                    StructureType.Structure,
                    new StructureField[0]),
                true));
    Tree<DataType> elementOperand =
        filterOperand.addChild(
            new ClientDataType(
                QualifiedName.parse("0:ElementOperand"),
                NodeIds.ElementOperand,
                NodeIds.ElementOperand_Encoding_DefaultBinary,
                NodeIds.ElementOperand_Encoding_DefaultXml,
                NodeIds.ElementOperand_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.ElementOperand_Encoding_DefaultBinary,
                    NodeIds.FilterOperand,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "Index",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UInt32,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> literalOperand =
        filterOperand.addChild(
            new ClientDataType(
                QualifiedName.parse("0:LiteralOperand"),
                NodeIds.LiteralOperand,
                NodeIds.LiteralOperand_Encoding_DefaultBinary,
                NodeIds.LiteralOperand_Encoding_DefaultXml,
                NodeIds.LiteralOperand_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.LiteralOperand_Encoding_DefaultBinary,
                    NodeIds.FilterOperand,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "Value",
                          LocalizedText.NULL_VALUE,
                          NodeIds.BaseDataType,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> attributeOperand =
        filterOperand.addChild(
            new ClientDataType(
                QualifiedName.parse("0:AttributeOperand"),
                NodeIds.AttributeOperand,
                NodeIds.AttributeOperand_Encoding_DefaultBinary,
                NodeIds.AttributeOperand_Encoding_DefaultXml,
                NodeIds.AttributeOperand_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.AttributeOperand_Encoding_DefaultBinary,
                    NodeIds.FilterOperand,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "NodeId",
                          LocalizedText.NULL_VALUE,
                          NodeIds.NodeId,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "Alias",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "BrowsePath",
                          LocalizedText.NULL_VALUE,
                          NodeIds.RelativePath,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "AttributeId",
                          LocalizedText.NULL_VALUE,
                          NodeIds.IntegerId,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "IndexRange",
                          LocalizedText.NULL_VALUE,
                          NodeIds.NumericRange,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> simpleAttributeOperand =
        filterOperand.addChild(
            new ClientDataType(
                QualifiedName.parse("0:SimpleAttributeOperand"),
                NodeIds.SimpleAttributeOperand,
                NodeIds.SimpleAttributeOperand_Encoding_DefaultBinary,
                NodeIds.SimpleAttributeOperand_Encoding_DefaultXml,
                NodeIds.SimpleAttributeOperand_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.SimpleAttributeOperand_Encoding_DefaultBinary,
                    NodeIds.FilterOperand,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "TypeDefinitionId",
                          LocalizedText.NULL_VALUE,
                          NodeIds.NodeId,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "BrowsePath",
                          LocalizedText.NULL_VALUE,
                          NodeIds.QualifiedName,
                          1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "AttributeId",
                          LocalizedText.NULL_VALUE,
                          NodeIds.IntegerId,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "IndexRange",
                          LocalizedText.NULL_VALUE,
                          NodeIds.NumericRange,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> historyEvent =
        structure.addChild(
            new ClientDataType(
                QualifiedName.parse("0:HistoryEvent"),
                NodeIds.HistoryEvent,
                NodeIds.HistoryEvent_Encoding_DefaultBinary,
                NodeIds.HistoryEvent_Encoding_DefaultXml,
                NodeIds.HistoryEvent_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.HistoryEvent_Encoding_DefaultBinary,
                    NodeIds.Structure,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "Events",
                          LocalizedText.NULL_VALUE,
                          NodeIds.HistoryEventFieldList,
                          1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> historyModifiedEvent =
        historyEvent.addChild(
            new ClientDataType(
                QualifiedName.parse("0:HistoryModifiedEvent"),
                NodeIds.HistoryModifiedEvent,
                NodeIds.HistoryModifiedEvent_Encoding_DefaultBinary,
                NodeIds.HistoryModifiedEvent_Encoding_DefaultXml,
                NodeIds.HistoryModifiedEvent_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.HistoryModifiedEvent_Encoding_DefaultBinary,
                    NodeIds.HistoryEvent,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "Events",
                          LocalizedText.NULL_VALUE,
                          NodeIds.HistoryEventFieldList,
                          1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "ModificationInfos",
                          LocalizedText.NULL_VALUE,
                          NodeIds.ModificationInfo,
                          1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> monitoringFilter =
        structure.addChild(
            new ClientDataType(
                QualifiedName.parse("0:MonitoringFilter"),
                NodeIds.MonitoringFilter,
                NodeIds.MonitoringFilter_Encoding_DefaultBinary,
                NodeIds.MonitoringFilter_Encoding_DefaultXml,
                NodeIds.MonitoringFilter_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.MonitoringFilter_Encoding_DefaultBinary,
                    NodeIds.Structure,
                    StructureType.Structure,
                    new StructureField[0]),
                false));
    Tree<DataType> eventFilter =
        monitoringFilter.addChild(
            new ClientDataType(
                QualifiedName.parse("0:EventFilter"),
                NodeIds.EventFilter,
                NodeIds.EventFilter_Encoding_DefaultBinary,
                NodeIds.EventFilter_Encoding_DefaultXml,
                NodeIds.EventFilter_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.EventFilter_Encoding_DefaultBinary,
                    NodeIds.MonitoringFilter,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "SelectClauses",
                          LocalizedText.NULL_VALUE,
                          NodeIds.SimpleAttributeOperand,
                          1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "WhereClause",
                          LocalizedText.NULL_VALUE,
                          NodeIds.ContentFilter,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> redundantServerDataType =
        structure.addChild(
            new ClientDataType(
                QualifiedName.parse("0:RedundantServerDataType"),
                NodeIds.RedundantServerDataType,
                NodeIds.RedundantServerDataType_Encoding_DefaultBinary,
                NodeIds.RedundantServerDataType_Encoding_DefaultXml,
                NodeIds.RedundantServerDataType_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.RedundantServerDataType_Encoding_DefaultBinary,
                    NodeIds.Structure,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "ServerId",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "ServiceLevel",
                          LocalizedText.NULL_VALUE,
                          NodeIds.Byte,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "ServerState",
                          LocalizedText.NULL_VALUE,
                          NodeIds.ServerState,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> samplingIntervalDiagnosticsDataType =
        structure.addChild(
            new ClientDataType(
                QualifiedName.parse("0:SamplingIntervalDiagnosticsDataType"),
                NodeIds.SamplingIntervalDiagnosticsDataType,
                NodeIds.SamplingIntervalDiagnosticsDataType_Encoding_DefaultBinary,
                NodeIds.SamplingIntervalDiagnosticsDataType_Encoding_DefaultXml,
                NodeIds.SamplingIntervalDiagnosticsDataType_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.SamplingIntervalDiagnosticsDataType_Encoding_DefaultBinary,
                    NodeIds.Structure,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "SamplingInterval",
                          LocalizedText.NULL_VALUE,
                          NodeIds.Duration,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "MonitoredItemCount",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UInt32,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "MaxMonitoredItemCount",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UInt32,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "DisabledMonitoredItemCount",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UInt32,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> serverDiagnosticsSummaryDataType =
        structure.addChild(
            new ClientDataType(
                QualifiedName.parse("0:ServerDiagnosticsSummaryDataType"),
                NodeIds.ServerDiagnosticsSummaryDataType,
                NodeIds.ServerDiagnosticsSummaryDataType_Encoding_DefaultBinary,
                NodeIds.ServerDiagnosticsSummaryDataType_Encoding_DefaultXml,
                NodeIds.ServerDiagnosticsSummaryDataType_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.ServerDiagnosticsSummaryDataType_Encoding_DefaultBinary,
                    NodeIds.Structure,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "ServerViewCount",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UInt32,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "CurrentSessionCount",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UInt32,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "CumulatedSessionCount",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UInt32,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "SecurityRejectedSessionCount",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UInt32,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "RejectedSessionCount",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UInt32,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "SessionTimeoutCount",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UInt32,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "SessionAbortCount",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UInt32,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "CurrentSubscriptionCount",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UInt32,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "CumulatedSubscriptionCount",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UInt32,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "PublishingIntervalCount",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UInt32,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "SecurityRejectedRequestsCount",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UInt32,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "RejectedRequestsCount",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UInt32,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> serverStatusDataType =
        structure.addChild(
            new ClientDataType(
                QualifiedName.parse("0:ServerStatusDataType"),
                NodeIds.ServerStatusDataType,
                NodeIds.ServerStatusDataType_Encoding_DefaultBinary,
                NodeIds.ServerStatusDataType_Encoding_DefaultXml,
                NodeIds.ServerStatusDataType_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.ServerStatusDataType_Encoding_DefaultBinary,
                    NodeIds.Structure,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "StartTime",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UtcTime,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "CurrentTime",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UtcTime,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "State",
                          LocalizedText.NULL_VALUE,
                          NodeIds.ServerState,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "BuildInfo",
                          LocalizedText.NULL_VALUE,
                          NodeIds.BuildInfo,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "SecondsTillShutdown",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UInt32,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "ShutdownReason",
                          LocalizedText.NULL_VALUE,
                          NodeIds.LocalizedText,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> sessionDiagnosticsDataType =
        structure.addChild(
            new ClientDataType(
                QualifiedName.parse("0:SessionDiagnosticsDataType"),
                NodeIds.SessionDiagnosticsDataType,
                NodeIds.SessionDiagnosticsDataType_Encoding_DefaultBinary,
                NodeIds.SessionDiagnosticsDataType_Encoding_DefaultXml,
                NodeIds.SessionDiagnosticsDataType_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.SessionDiagnosticsDataType_Encoding_DefaultBinary,
                    NodeIds.Structure,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "SessionId",
                          LocalizedText.NULL_VALUE,
                          NodeIds.NodeId,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "SessionName",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "ClientDescription",
                          LocalizedText.NULL_VALUE,
                          NodeIds.ApplicationDescription,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "ServerUri",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "EndpointUrl",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "LocaleIds",
                          LocalizedText.NULL_VALUE,
                          NodeIds.LocaleId,
                          1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "ActualSessionTimeout",
                          LocalizedText.NULL_VALUE,
                          NodeIds.Duration,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "MaxResponseMessageSize",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UInt32,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "ClientConnectionTime",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UtcTime,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "ClientLastContactTime",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UtcTime,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "CurrentSubscriptionsCount",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UInt32,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "CurrentMonitoredItemsCount",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UInt32,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "CurrentPublishRequestsInQueue",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UInt32,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "TotalRequestCount",
                          LocalizedText.NULL_VALUE,
                          NodeIds.ServiceCounterDataType,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "UnauthorizedRequestCount",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UInt32,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "ReadCount",
                          LocalizedText.NULL_VALUE,
                          NodeIds.ServiceCounterDataType,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "HistoryReadCount",
                          LocalizedText.NULL_VALUE,
                          NodeIds.ServiceCounterDataType,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "WriteCount",
                          LocalizedText.NULL_VALUE,
                          NodeIds.ServiceCounterDataType,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "HistoryUpdateCount",
                          LocalizedText.NULL_VALUE,
                          NodeIds.ServiceCounterDataType,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "CallCount",
                          LocalizedText.NULL_VALUE,
                          NodeIds.ServiceCounterDataType,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "CreateMonitoredItemsCount",
                          LocalizedText.NULL_VALUE,
                          NodeIds.ServiceCounterDataType,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "ModifyMonitoredItemsCount",
                          LocalizedText.NULL_VALUE,
                          NodeIds.ServiceCounterDataType,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "SetMonitoringModeCount",
                          LocalizedText.NULL_VALUE,
                          NodeIds.ServiceCounterDataType,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "SetTriggeringCount",
                          LocalizedText.NULL_VALUE,
                          NodeIds.ServiceCounterDataType,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "DeleteMonitoredItemsCount",
                          LocalizedText.NULL_VALUE,
                          NodeIds.ServiceCounterDataType,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "CreateSubscriptionCount",
                          LocalizedText.NULL_VALUE,
                          NodeIds.ServiceCounterDataType,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "ModifySubscriptionCount",
                          LocalizedText.NULL_VALUE,
                          NodeIds.ServiceCounterDataType,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "SetPublishingModeCount",
                          LocalizedText.NULL_VALUE,
                          NodeIds.ServiceCounterDataType,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "PublishCount",
                          LocalizedText.NULL_VALUE,
                          NodeIds.ServiceCounterDataType,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "RepublishCount",
                          LocalizedText.NULL_VALUE,
                          NodeIds.ServiceCounterDataType,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "TransferSubscriptionsCount",
                          LocalizedText.NULL_VALUE,
                          NodeIds.ServiceCounterDataType,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "DeleteSubscriptionsCount",
                          LocalizedText.NULL_VALUE,
                          NodeIds.ServiceCounterDataType,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "AddNodesCount",
                          LocalizedText.NULL_VALUE,
                          NodeIds.ServiceCounterDataType,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "AddReferencesCount",
                          LocalizedText.NULL_VALUE,
                          NodeIds.ServiceCounterDataType,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "DeleteNodesCount",
                          LocalizedText.NULL_VALUE,
                          NodeIds.ServiceCounterDataType,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "DeleteReferencesCount",
                          LocalizedText.NULL_VALUE,
                          NodeIds.ServiceCounterDataType,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "BrowseCount",
                          LocalizedText.NULL_VALUE,
                          NodeIds.ServiceCounterDataType,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "BrowseNextCount",
                          LocalizedText.NULL_VALUE,
                          NodeIds.ServiceCounterDataType,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "TranslateBrowsePathsToNodeIdsCount",
                          LocalizedText.NULL_VALUE,
                          NodeIds.ServiceCounterDataType,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "QueryFirstCount",
                          LocalizedText.NULL_VALUE,
                          NodeIds.ServiceCounterDataType,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "QueryNextCount",
                          LocalizedText.NULL_VALUE,
                          NodeIds.ServiceCounterDataType,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "RegisterNodesCount",
                          LocalizedText.NULL_VALUE,
                          NodeIds.ServiceCounterDataType,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "UnregisterNodesCount",
                          LocalizedText.NULL_VALUE,
                          NodeIds.ServiceCounterDataType,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> sessionSecurityDiagnosticsDataType =
        structure.addChild(
            new ClientDataType(
                QualifiedName.parse("0:SessionSecurityDiagnosticsDataType"),
                NodeIds.SessionSecurityDiagnosticsDataType,
                NodeIds.SessionSecurityDiagnosticsDataType_Encoding_DefaultBinary,
                NodeIds.SessionSecurityDiagnosticsDataType_Encoding_DefaultXml,
                NodeIds.SessionSecurityDiagnosticsDataType_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.SessionSecurityDiagnosticsDataType_Encoding_DefaultBinary,
                    NodeIds.Structure,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "SessionId",
                          LocalizedText.NULL_VALUE,
                          NodeIds.NodeId,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "ClientUserIdOfSession",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "ClientUserIdHistory",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "AuthenticationMechanism",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "Encoding",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "TransportProtocol",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "SecurityMode",
                          LocalizedText.NULL_VALUE,
                          NodeIds.MessageSecurityMode,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "SecurityPolicyUri",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "ClientCertificate",
                          LocalizedText.NULL_VALUE,
                          NodeIds.ByteString,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> serviceCounterDataType =
        structure.addChild(
            new ClientDataType(
                QualifiedName.parse("0:ServiceCounterDataType"),
                NodeIds.ServiceCounterDataType,
                NodeIds.ServiceCounterDataType_Encoding_DefaultBinary,
                NodeIds.ServiceCounterDataType_Encoding_DefaultXml,
                NodeIds.ServiceCounterDataType_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.ServiceCounterDataType_Encoding_DefaultBinary,
                    NodeIds.Structure,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "TotalCount",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UInt32,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "ErrorCount",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UInt32,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> subscriptionDiagnosticsDataType =
        structure.addChild(
            new ClientDataType(
                QualifiedName.parse("0:SubscriptionDiagnosticsDataType"),
                NodeIds.SubscriptionDiagnosticsDataType,
                NodeIds.SubscriptionDiagnosticsDataType_Encoding_DefaultBinary,
                NodeIds.SubscriptionDiagnosticsDataType_Encoding_DefaultXml,
                NodeIds.SubscriptionDiagnosticsDataType_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.SubscriptionDiagnosticsDataType_Encoding_DefaultBinary,
                    NodeIds.Structure,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "SessionId",
                          LocalizedText.NULL_VALUE,
                          NodeIds.NodeId,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "SubscriptionId",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UInt32,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "Priority",
                          LocalizedText.NULL_VALUE,
                          NodeIds.Byte,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "PublishingInterval",
                          LocalizedText.NULL_VALUE,
                          NodeIds.Duration,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "MaxKeepAliveCount",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UInt32,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "MaxLifetimeCount",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UInt32,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "MaxNotificationsPerPublish",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UInt32,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "PublishingEnabled",
                          LocalizedText.NULL_VALUE,
                          NodeIds.Boolean,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "ModifyCount",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UInt32,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "EnableCount",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UInt32,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "DisableCount",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UInt32,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "RepublishRequestCount",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UInt32,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "RepublishMessageRequestCount",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UInt32,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "RepublishMessageCount",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UInt32,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "TransferRequestCount",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UInt32,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "TransferredToAltClientCount",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UInt32,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "TransferredToSameClientCount",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UInt32,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "PublishRequestCount",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UInt32,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "DataChangeNotificationsCount",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UInt32,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "EventNotificationsCount",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UInt32,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "NotificationsCount",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UInt32,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "LatePublishRequestCount",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UInt32,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "CurrentKeepAliveCount",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UInt32,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "CurrentLifetimeCount",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UInt32,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "UnacknowledgedMessageCount",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UInt32,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "DiscardedMessageCount",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UInt32,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "MonitoredItemCount",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UInt32,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "DisabledMonitoredItemCount",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UInt32,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "MonitoringQueueOverflowCount",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UInt32,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "NextSequenceNumber",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UInt32,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "EventQueueOverflowCount",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UInt32,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> modelChangeStructureDataType =
        structure.addChild(
            new ClientDataType(
                QualifiedName.parse("0:ModelChangeStructureDataType"),
                NodeIds.ModelChangeStructureDataType,
                NodeIds.ModelChangeStructureDataType_Encoding_DefaultBinary,
                NodeIds.ModelChangeStructureDataType_Encoding_DefaultXml,
                NodeIds.ModelChangeStructureDataType_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.ModelChangeStructureDataType_Encoding_DefaultBinary,
                    NodeIds.Structure,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "Affected",
                          LocalizedText.NULL_VALUE,
                          NodeIds.NodeId,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "AffectedType",
                          LocalizedText.NULL_VALUE,
                          NodeIds.NodeId,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "Verb",
                          LocalizedText.NULL_VALUE,
                          NodeIds.Byte,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> range =
        structure.addChild(
            new ClientDataType(
                QualifiedName.parse("0:Range"),
                NodeIds.Range,
                NodeIds.Range_Encoding_DefaultBinary,
                NodeIds.Range_Encoding_DefaultXml,
                NodeIds.Range_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.Range_Encoding_DefaultBinary,
                    NodeIds.Structure,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "Low",
                          LocalizedText.NULL_VALUE,
                          NodeIds.Double,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "High",
                          LocalizedText.NULL_VALUE,
                          NodeIds.Double,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> eUInformation =
        structure.addChild(
            new ClientDataType(
                QualifiedName.parse("0:EUInformation"),
                NodeIds.EUInformation,
                NodeIds.EUInformation_Encoding_DefaultBinary,
                NodeIds.EUInformation_Encoding_DefaultXml,
                NodeIds.EUInformation_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.EUInformation_Encoding_DefaultBinary,
                    NodeIds.Structure,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "NamespaceUri",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "UnitId",
                          LocalizedText.NULL_VALUE,
                          NodeIds.Int32,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "DisplayName",
                          LocalizedText.NULL_VALUE,
                          NodeIds.LocalizedText,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "Description",
                          LocalizedText.NULL_VALUE,
                          NodeIds.LocalizedText,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> annotation =
        structure.addChild(
            new ClientDataType(
                QualifiedName.parse("0:Annotation"),
                NodeIds.Annotation,
                NodeIds.Annotation_Encoding_DefaultBinary,
                NodeIds.Annotation_Encoding_DefaultXml,
                NodeIds.Annotation_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.Annotation_Encoding_DefaultBinary,
                    NodeIds.Structure,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "Message",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "UserName",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "AnnotationTime",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UtcTime,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> programDiagnosticDataType =
        structure.addChild(
            new ClientDataType(
                QualifiedName.parse("0:ProgramDiagnosticDataType"),
                NodeIds.ProgramDiagnosticDataType,
                NodeIds.ProgramDiagnosticDataType_Encoding_DefaultBinary,
                NodeIds.ProgramDiagnosticDataType_Encoding_DefaultXml,
                NodeIds.ProgramDiagnosticDataType_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.ProgramDiagnosticDataType_Encoding_DefaultBinary,
                    NodeIds.Structure,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "CreateSessionId",
                          LocalizedText.NULL_VALUE,
                          NodeIds.NodeId,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "CreateClientName",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "InvocationCreationTime",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UtcTime,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "LastTransitionTime",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UtcTime,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "LastMethodCall",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "LastMethodSessionId",
                          LocalizedText.NULL_VALUE,
                          NodeIds.NodeId,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "LastMethodInputArguments",
                          LocalizedText.NULL_VALUE,
                          NodeIds.Argument,
                          1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "LastMethodOutputArguments",
                          LocalizedText.NULL_VALUE,
                          NodeIds.Argument,
                          1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "LastMethodCallTime",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UtcTime,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "LastMethodReturnStatus",
                          LocalizedText.NULL_VALUE,
                          NodeIds.StatusResult,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> semanticChangeStructureDataType =
        structure.addChild(
            new ClientDataType(
                QualifiedName.parse("0:SemanticChangeStructureDataType"),
                NodeIds.SemanticChangeStructureDataType,
                NodeIds.SemanticChangeStructureDataType_Encoding_DefaultBinary,
                NodeIds.SemanticChangeStructureDataType_Encoding_DefaultXml,
                NodeIds.SemanticChangeStructureDataType_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.SemanticChangeStructureDataType_Encoding_DefaultBinary,
                    NodeIds.Structure,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "Affected",
                          LocalizedText.NULL_VALUE,
                          NodeIds.NodeId,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "AffectedType",
                          LocalizedText.NULL_VALUE,
                          NodeIds.NodeId,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> historyEventFieldList =
        structure.addChild(
            new ClientDataType(
                QualifiedName.parse("0:HistoryEventFieldList"),
                NodeIds.HistoryEventFieldList,
                NodeIds.HistoryEventFieldList_Encoding_DefaultBinary,
                NodeIds.HistoryEventFieldList_Encoding_DefaultXml,
                NodeIds.HistoryEventFieldList_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.HistoryEventFieldList_Encoding_DefaultBinary,
                    NodeIds.Structure,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "EventFields",
                          LocalizedText.NULL_VALUE,
                          NodeIds.BaseDataType,
                          1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> aggregateConfiguration =
        structure.addChild(
            new ClientDataType(
                QualifiedName.parse("0:AggregateConfiguration"),
                NodeIds.AggregateConfiguration,
                NodeIds.AggregateConfiguration_Encoding_DefaultBinary,
                NodeIds.AggregateConfiguration_Encoding_DefaultXml,
                NodeIds.AggregateConfiguration_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.AggregateConfiguration_Encoding_DefaultBinary,
                    NodeIds.Structure,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "UseServerCapabilitiesDefaults",
                          LocalizedText.NULL_VALUE,
                          NodeIds.Boolean,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "TreatUncertainAsBad",
                          LocalizedText.NULL_VALUE,
                          NodeIds.Boolean,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "PercentDataBad",
                          LocalizedText.NULL_VALUE,
                          NodeIds.Byte,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "PercentDataGood",
                          LocalizedText.NULL_VALUE,
                          NodeIds.Byte,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "UseSlopedExtrapolation",
                          LocalizedText.NULL_VALUE,
                          NodeIds.Boolean,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> enumValueType =
        structure.addChild(
            new ClientDataType(
                QualifiedName.parse("0:EnumValueType"),
                NodeIds.EnumValueType,
                NodeIds.EnumValueType_Encoding_DefaultBinary,
                NodeIds.EnumValueType_Encoding_DefaultXml,
                NodeIds.EnumValueType_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.EnumValueType_Encoding_DefaultBinary,
                    NodeIds.Structure,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "Value",
                          LocalizedText.NULL_VALUE,
                          NodeIds.Int64,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "DisplayName",
                          LocalizedText.NULL_VALUE,
                          NodeIds.LocalizedText,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "Description",
                          LocalizedText.NULL_VALUE,
                          NodeIds.LocalizedText,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> enumField =
        enumValueType.addChild(
            new ClientDataType(
                QualifiedName.parse("0:EnumField"),
                NodeIds.EnumField,
                NodeIds.EnumField_Encoding_DefaultBinary,
                NodeIds.EnumField_Encoding_DefaultXml,
                NodeIds.EnumField_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.EnumField_Encoding_DefaultBinary,
                    NodeIds.EnumValueType,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "Description",
                          LocalizedText.NULL_VALUE,
                          NodeIds.LocalizedText,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "DisplayName",
                          LocalizedText.NULL_VALUE,
                          NodeIds.LocalizedText,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "Value",
                          LocalizedText.NULL_VALUE,
                          NodeIds.Int64,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "Name",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> timeZoneDataType =
        structure.addChild(
            new ClientDataType(
                QualifiedName.parse("0:TimeZoneDataType"),
                NodeIds.TimeZoneDataType,
                NodeIds.TimeZoneDataType_Encoding_DefaultBinary,
                NodeIds.TimeZoneDataType_Encoding_DefaultXml,
                NodeIds.TimeZoneDataType_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.TimeZoneDataType_Encoding_DefaultBinary,
                    NodeIds.Structure,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "Offset",
                          LocalizedText.NULL_VALUE,
                          NodeIds.Int16,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "DaylightSavingInOffset",
                          LocalizedText.NULL_VALUE,
                          NodeIds.Boolean,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> modificationInfo =
        structure.addChild(
            new ClientDataType(
                QualifiedName.parse("0:ModificationInfo"),
                NodeIds.ModificationInfo,
                NodeIds.ModificationInfo_Encoding_DefaultBinary,
                NodeIds.ModificationInfo_Encoding_DefaultXml,
                NodeIds.ModificationInfo_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.ModificationInfo_Encoding_DefaultBinary,
                    NodeIds.Structure,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "ModificationTime",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UtcTime,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "UpdateType",
                          LocalizedText.NULL_VALUE,
                          NodeIds.HistoryUpdateType,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "UserName",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> endpointUrlListDataType =
        structure.addChild(
            new ClientDataType(
                QualifiedName.parse("0:EndpointUrlListDataType"),
                NodeIds.EndpointUrlListDataType,
                NodeIds.EndpointUrlListDataType_Encoding_DefaultBinary,
                NodeIds.EndpointUrlListDataType_Encoding_DefaultXml,
                NodeIds.EndpointUrlListDataType_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.EndpointUrlListDataType_Encoding_DefaultBinary,
                    NodeIds.Structure,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "EndpointUrlList",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> networkGroupDataType =
        structure.addChild(
            new ClientDataType(
                QualifiedName.parse("0:NetworkGroupDataType"),
                NodeIds.NetworkGroupDataType,
                NodeIds.NetworkGroupDataType_Encoding_DefaultBinary,
                NodeIds.NetworkGroupDataType_Encoding_DefaultXml,
                NodeIds.NetworkGroupDataType_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.NetworkGroupDataType_Encoding_DefaultBinary,
                    NodeIds.Structure,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "ServerUri",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "NetworkPaths",
                          LocalizedText.NULL_VALUE,
                          NodeIds.EndpointUrlListDataType,
                          1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> axisInformation =
        structure.addChild(
            new ClientDataType(
                QualifiedName.parse("0:AxisInformation"),
                NodeIds.AxisInformation,
                NodeIds.AxisInformation_Encoding_DefaultBinary,
                NodeIds.AxisInformation_Encoding_DefaultXml,
                NodeIds.AxisInformation_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.AxisInformation_Encoding_DefaultBinary,
                    NodeIds.Structure,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "EngineeringUnits",
                          LocalizedText.NULL_VALUE,
                          NodeIds.EUInformation,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "EURange",
                          LocalizedText.NULL_VALUE,
                          NodeIds.Range,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "Title",
                          LocalizedText.NULL_VALUE,
                          NodeIds.LocalizedText,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "AxisScaleType",
                          LocalizedText.NULL_VALUE,
                          NodeIds.AxisScaleEnumeration,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "AxisSteps",
                          LocalizedText.NULL_VALUE,
                          NodeIds.Double,
                          1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> xVType =
        structure.addChild(
            new ClientDataType(
                QualifiedName.parse("0:XVType"),
                NodeIds.XVType,
                NodeIds.XVType_Encoding_DefaultBinary,
                NodeIds.XVType_Encoding_DefaultXml,
                NodeIds.XVType_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.XVType_Encoding_DefaultBinary,
                    NodeIds.Structure,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "X",
                          LocalizedText.NULL_VALUE,
                          NodeIds.Double,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "Value",
                          LocalizedText.NULL_VALUE,
                          NodeIds.Float,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> complexNumberType =
        structure.addChild(
            new ClientDataType(
                QualifiedName.parse("0:ComplexNumberType"),
                NodeIds.ComplexNumberType,
                NodeIds.ComplexNumberType_Encoding_DefaultBinary,
                NodeIds.ComplexNumberType_Encoding_DefaultXml,
                NodeIds.ComplexNumberType_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.ComplexNumberType_Encoding_DefaultBinary,
                    NodeIds.Structure,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "Real",
                          LocalizedText.NULL_VALUE,
                          NodeIds.Float,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "Imaginary",
                          LocalizedText.NULL_VALUE,
                          NodeIds.Float,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> doubleComplexNumberType =
        structure.addChild(
            new ClientDataType(
                QualifiedName.parse("0:DoubleComplexNumberType"),
                NodeIds.DoubleComplexNumberType,
                NodeIds.DoubleComplexNumberType_Encoding_DefaultBinary,
                NodeIds.DoubleComplexNumberType_Encoding_DefaultXml,
                NodeIds.DoubleComplexNumberType_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.DoubleComplexNumberType_Encoding_DefaultBinary,
                    NodeIds.Structure,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "Real",
                          LocalizedText.NULL_VALUE,
                          NodeIds.Double,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "Imaginary",
                          LocalizedText.NULL_VALUE,
                          NodeIds.Double,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> serverOnNetwork =
        structure.addChild(
            new ClientDataType(
                QualifiedName.parse("0:ServerOnNetwork"),
                NodeIds.ServerOnNetwork,
                NodeIds.ServerOnNetwork_Encoding_DefaultBinary,
                NodeIds.ServerOnNetwork_Encoding_DefaultXml,
                NodeIds.ServerOnNetwork_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.ServerOnNetwork_Encoding_DefaultBinary,
                    NodeIds.Structure,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "RecordId",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UInt32,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "ServerName",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "DiscoveryUrl",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "ServerCapabilities",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> trustListDataType =
        structure.addChild(
            new ClientDataType(
                QualifiedName.parse("0:TrustListDataType"),
                NodeIds.TrustListDataType,
                NodeIds.TrustListDataType_Encoding_DefaultBinary,
                NodeIds.TrustListDataType_Encoding_DefaultXml,
                NodeIds.TrustListDataType_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.TrustListDataType_Encoding_DefaultBinary,
                    NodeIds.Structure,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "SpecifiedLists",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UInt32,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "TrustedCertificates",
                          LocalizedText.NULL_VALUE,
                          NodeIds.ByteString,
                          1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "TrustedCrls",
                          LocalizedText.NULL_VALUE,
                          NodeIds.ByteString,
                          1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "IssuerCertificates",
                          LocalizedText.NULL_VALUE,
                          NodeIds.ByteString,
                          1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "IssuerCrls",
                          LocalizedText.NULL_VALUE,
                          NodeIds.ByteString,
                          1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> optionSet =
        structure.addChild(
            new ClientDataType(
                QualifiedName.parse("0:OptionSet"),
                NodeIds.OptionSet,
                NodeIds.OptionSet_Encoding_DefaultBinary,
                NodeIds.OptionSet_Encoding_DefaultXml,
                NodeIds.OptionSet_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.OptionSet_Encoding_DefaultBinary,
                    NodeIds.Structure,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "Value",
                          LocalizedText.NULL_VALUE,
                          NodeIds.ByteString,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "ValidBits",
                          LocalizedText.NULL_VALUE,
                          NodeIds.ByteString,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                true));
    Tree<DataType> union =
        structure.addChild(
            new ClientDataType(
                QualifiedName.parse("0:Union"),
                NodeIds.Union,
                NodeIds.Union_Encoding_DefaultBinary,
                NodeIds.Union_Encoding_DefaultXml,
                NodeIds.Union_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.Union_Encoding_DefaultBinary,
                    NodeIds.Structure,
                    StructureType.Structure,
                    new StructureField[0]),
                true));
    Tree<DataType> discoveryConfiguration =
        structure.addChild(
            new ClientDataType(
                QualifiedName.parse("0:DiscoveryConfiguration"),
                NodeIds.DiscoveryConfiguration,
                NodeIds.DiscoveryConfiguration_Encoding_DefaultBinary,
                NodeIds.DiscoveryConfiguration_Encoding_DefaultXml,
                NodeIds.DiscoveryConfiguration_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.DiscoveryConfiguration_Encoding_DefaultBinary,
                    NodeIds.Structure,
                    StructureType.Structure,
                    new StructureField[0]),
                false));
    Tree<DataType> mdnsDiscoveryConfiguration =
        discoveryConfiguration.addChild(
            new ClientDataType(
                QualifiedName.parse("0:MdnsDiscoveryConfiguration"),
                NodeIds.MdnsDiscoveryConfiguration,
                NodeIds.MdnsDiscoveryConfiguration_Encoding_DefaultBinary,
                NodeIds.MdnsDiscoveryConfiguration_Encoding_DefaultXml,
                NodeIds.MdnsDiscoveryConfiguration_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.MdnsDiscoveryConfiguration_Encoding_DefaultBinary,
                    NodeIds.DiscoveryConfiguration,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "MdnsServerName",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "ServerCapabilities",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> publishedVariableDataType =
        structure.addChild(
            new ClientDataType(
                QualifiedName.parse("0:PublishedVariableDataType"),
                NodeIds.PublishedVariableDataType,
                NodeIds.PublishedVariableDataType_Encoding_DefaultBinary,
                NodeIds.PublishedVariableDataType_Encoding_DefaultXml,
                NodeIds.PublishedVariableDataType_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.PublishedVariableDataType_Encoding_DefaultBinary,
                    NodeIds.Structure,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "PublishedVariable",
                          LocalizedText.NULL_VALUE,
                          NodeIds.NodeId,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "AttributeId",
                          LocalizedText.NULL_VALUE,
                          NodeIds.IntegerId,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "SamplingIntervalHint",
                          LocalizedText.NULL_VALUE,
                          NodeIds.Duration,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "DeadbandType",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UInt32,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "DeadbandValue",
                          LocalizedText.NULL_VALUE,
                          NodeIds.Double,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "IndexRange",
                          LocalizedText.NULL_VALUE,
                          NodeIds.NumericRange,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "SubstituteValue",
                          LocalizedText.NULL_VALUE,
                          NodeIds.BaseDataType,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "MetaDataProperties",
                          LocalizedText.NULL_VALUE,
                          NodeIds.QualifiedName,
                          1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> fieldMetaData =
        structure.addChild(
            new ClientDataType(
                QualifiedName.parse("0:FieldMetaData"),
                NodeIds.FieldMetaData,
                NodeIds.FieldMetaData_Encoding_DefaultBinary,
                NodeIds.FieldMetaData_Encoding_DefaultXml,
                NodeIds.FieldMetaData_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.FieldMetaData_Encoding_DefaultBinary,
                    NodeIds.Structure,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "Name",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "Description",
                          LocalizedText.NULL_VALUE,
                          NodeIds.LocalizedText,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "FieldFlags",
                          LocalizedText.NULL_VALUE,
                          NodeIds.DataSetFieldFlags,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "BuiltInType",
                          LocalizedText.NULL_VALUE,
                          NodeIds.Byte,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "DataType",
                          LocalizedText.NULL_VALUE,
                          NodeIds.NodeId,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "ValueRank",
                          LocalizedText.NULL_VALUE,
                          NodeIds.Int32,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "ArrayDimensions",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UInt32,
                          1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "MaxStringLength",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UInt32,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "DataSetFieldId",
                          LocalizedText.NULL_VALUE,
                          NodeIds.Guid,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "Properties",
                          LocalizedText.NULL_VALUE,
                          NodeIds.KeyValuePair,
                          1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> dataTypeDescription =
        structure.addChild(
            new ClientDataType(
                QualifiedName.parse("0:DataTypeDescription"),
                NodeIds.DataTypeDescription,
                NodeIds.DataTypeDescription_Encoding_DefaultBinary,
                NodeIds.DataTypeDescription_Encoding_DefaultXml,
                NodeIds.DataTypeDescription_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.DataTypeDescription_Encoding_DefaultBinary,
                    NodeIds.Structure,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "DataTypeId",
                          LocalizedText.NULL_VALUE,
                          NodeIds.NodeId,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "Name",
                          LocalizedText.NULL_VALUE,
                          NodeIds.QualifiedName,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                true));
    Tree<DataType> simpleTypeDescription =
        dataTypeDescription.addChild(
            new ClientDataType(
                QualifiedName.parse("0:SimpleTypeDescription"),
                NodeIds.SimpleTypeDescription,
                NodeIds.SimpleTypeDescription_Encoding_DefaultBinary,
                NodeIds.SimpleTypeDescription_Encoding_DefaultXml,
                NodeIds.SimpleTypeDescription_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.SimpleTypeDescription_Encoding_DefaultBinary,
                    NodeIds.DataTypeDescription,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "Name",
                          LocalizedText.NULL_VALUE,
                          NodeIds.QualifiedName,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "DataTypeId",
                          LocalizedText.NULL_VALUE,
                          NodeIds.NodeId,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "BaseDataType",
                          LocalizedText.NULL_VALUE,
                          NodeIds.NodeId,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "BuiltInType",
                          LocalizedText.NULL_VALUE,
                          NodeIds.Byte,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> structureDescription =
        dataTypeDescription.addChild(
            new ClientDataType(
                QualifiedName.parse("0:StructureDescription"),
                NodeIds.StructureDescription,
                NodeIds.StructureDescription_Encoding_DefaultBinary,
                NodeIds.StructureDescription_Encoding_DefaultXml,
                NodeIds.StructureDescription_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.StructureDescription_Encoding_DefaultBinary,
                    NodeIds.DataTypeDescription,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "Name",
                          LocalizedText.NULL_VALUE,
                          NodeIds.QualifiedName,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "DataTypeId",
                          LocalizedText.NULL_VALUE,
                          NodeIds.NodeId,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "StructureDefinition",
                          LocalizedText.NULL_VALUE,
                          NodeIds.StructureDefinition,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> enumDescription =
        dataTypeDescription.addChild(
            new ClientDataType(
                QualifiedName.parse("0:EnumDescription"),
                NodeIds.EnumDescription,
                NodeIds.EnumDescription_Encoding_DefaultBinary,
                NodeIds.EnumDescription_Encoding_DefaultXml,
                NodeIds.EnumDescription_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.EnumDescription_Encoding_DefaultBinary,
                    NodeIds.DataTypeDescription,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "Name",
                          LocalizedText.NULL_VALUE,
                          NodeIds.QualifiedName,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "DataTypeId",
                          LocalizedText.NULL_VALUE,
                          NodeIds.NodeId,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "EnumDefinition",
                          LocalizedText.NULL_VALUE,
                          NodeIds.EnumDefinition,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "BuiltInType",
                          LocalizedText.NULL_VALUE,
                          NodeIds.Byte,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> keyValuePair =
        structure.addChild(
            new ClientDataType(
                QualifiedName.parse("0:KeyValuePair"),
                NodeIds.KeyValuePair,
                NodeIds.KeyValuePair_Encoding_DefaultBinary,
                NodeIds.KeyValuePair_Encoding_DefaultXml,
                NodeIds.KeyValuePair_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.KeyValuePair_Encoding_DefaultBinary,
                    NodeIds.Structure,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "Key",
                          LocalizedText.NULL_VALUE,
                          NodeIds.QualifiedName,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "Value",
                          LocalizedText.NULL_VALUE,
                          NodeIds.BaseDataType,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> configurationVersionDataType =
        structure.addChild(
            new ClientDataType(
                QualifiedName.parse("0:ConfigurationVersionDataType"),
                NodeIds.ConfigurationVersionDataType,
                NodeIds.ConfigurationVersionDataType_Encoding_DefaultBinary,
                NodeIds.ConfigurationVersionDataType_Encoding_DefaultXml,
                NodeIds.ConfigurationVersionDataType_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.ConfigurationVersionDataType_Encoding_DefaultBinary,
                    NodeIds.Structure,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "MajorVersion",
                          LocalizedText.NULL_VALUE,
                          NodeIds.VersionTime,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "MinorVersion",
                          LocalizedText.NULL_VALUE,
                          NodeIds.VersionTime,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> fieldTargetDataType =
        structure.addChild(
            new ClientDataType(
                QualifiedName.parse("0:FieldTargetDataType"),
                NodeIds.FieldTargetDataType,
                NodeIds.FieldTargetDataType_Encoding_DefaultBinary,
                NodeIds.FieldTargetDataType_Encoding_DefaultXml,
                NodeIds.FieldTargetDataType_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.FieldTargetDataType_Encoding_DefaultBinary,
                    NodeIds.Structure,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "DataSetFieldId",
                          LocalizedText.NULL_VALUE,
                          NodeIds.Guid,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "ReceiverIndexRange",
                          LocalizedText.NULL_VALUE,
                          NodeIds.NumericRange,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "TargetNodeId",
                          LocalizedText.NULL_VALUE,
                          NodeIds.NodeId,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "AttributeId",
                          LocalizedText.NULL_VALUE,
                          NodeIds.IntegerId,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "WriteIndexRange",
                          LocalizedText.NULL_VALUE,
                          NodeIds.NumericRange,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "OverrideValueHandling",
                          LocalizedText.NULL_VALUE,
                          NodeIds.OverrideValueHandling,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "OverrideValue",
                          LocalizedText.NULL_VALUE,
                          NodeIds.BaseDataType,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> networkAddressDataType =
        structure.addChild(
            new ClientDataType(
                QualifiedName.parse("0:NetworkAddressDataType"),
                NodeIds.NetworkAddressDataType,
                NodeIds.NetworkAddressDataType_Encoding_DefaultBinary,
                NodeIds.NetworkAddressDataType_Encoding_DefaultXml,
                NodeIds.NetworkAddressDataType_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.NetworkAddressDataType_Encoding_DefaultBinary,
                    NodeIds.Structure,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "NetworkInterface",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                true));
    Tree<DataType> networkAddressUrlDataType =
        networkAddressDataType.addChild(
            new ClientDataType(
                QualifiedName.parse("0:NetworkAddressUrlDataType"),
                NodeIds.NetworkAddressUrlDataType,
                NodeIds.NetworkAddressUrlDataType_Encoding_DefaultBinary,
                NodeIds.NetworkAddressUrlDataType_Encoding_DefaultXml,
                NodeIds.NetworkAddressUrlDataType_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.NetworkAddressUrlDataType_Encoding_DefaultBinary,
                    NodeIds.NetworkAddressDataType,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "NetworkInterface",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "Url",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> endpointType =
        structure.addChild(
            new ClientDataType(
                QualifiedName.parse("0:EndpointType"),
                NodeIds.EndpointType,
                NodeIds.EndpointType_Encoding_DefaultBinary,
                NodeIds.EndpointType_Encoding_DefaultXml,
                NodeIds.EndpointType_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.EndpointType_Encoding_DefaultBinary,
                    NodeIds.Structure,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "EndpointUrl",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "SecurityMode",
                          LocalizedText.NULL_VALUE,
                          NodeIds.MessageSecurityMode,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "SecurityPolicyUri",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "TransportProfileUri",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> pubSubConfigurationDataType =
        structure.addChild(
            new ClientDataType(
                QualifiedName.parse("0:PubSubConfigurationDataType"),
                NodeIds.PubSubConfigurationDataType,
                NodeIds.PubSubConfigurationDataType_Encoding_DefaultBinary,
                NodeIds.PubSubConfigurationDataType_Encoding_DefaultXml,
                NodeIds.PubSubConfigurationDataType_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.PubSubConfigurationDataType_Encoding_DefaultBinary,
                    NodeIds.Structure,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "PublishedDataSets",
                          LocalizedText.NULL_VALUE,
                          NodeIds.PublishedDataSetDataType,
                          1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "Connections",
                          LocalizedText.NULL_VALUE,
                          NodeIds.PubSubConnectionDataType,
                          1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "Enabled",
                          LocalizedText.NULL_VALUE,
                          NodeIds.Boolean,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> pubSubConfiguration2DataType =
        pubSubConfigurationDataType.addChild(
            new ClientDataType(
                QualifiedName.parse("0:PubSubConfiguration2DataType"),
                NodeIds.PubSubConfiguration2DataType,
                NodeIds.PubSubConfiguration2DataType_Encoding_DefaultBinary,
                NodeIds.PubSubConfiguration2DataType_Encoding_DefaultXml,
                NodeIds.PubSubConfiguration2DataType_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.PubSubConfiguration2DataType_Encoding_DefaultBinary,
                    NodeIds.PubSubConfigurationDataType,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "Enabled",
                          LocalizedText.NULL_VALUE,
                          NodeIds.Boolean,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "Connections",
                          LocalizedText.NULL_VALUE,
                          NodeIds.PubSubConnectionDataType,
                          1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "PublishedDataSets",
                          LocalizedText.NULL_VALUE,
                          NodeIds.PublishedDataSetDataType,
                          1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "SubscribedDataSets",
                          LocalizedText.NULL_VALUE,
                          NodeIds.StandaloneSubscribedDataSetDataType,
                          1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "DataSetClasses",
                          LocalizedText.NULL_VALUE,
                          NodeIds.DataSetMetaDataType,
                          1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "DefaultSecurityKeyServices",
                          LocalizedText.NULL_VALUE,
                          NodeIds.EndpointDescription,
                          1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "SecurityGroups",
                          LocalizedText.NULL_VALUE,
                          NodeIds.SecurityGroupDataType,
                          1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "PubSubKeyPushTargets",
                          LocalizedText.NULL_VALUE,
                          NodeIds.PubSubKeyPushTargetDataType,
                          1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "ConfigurationVersion",
                          LocalizedText.NULL_VALUE,
                          NodeIds.VersionTime,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "ConfigurationProperties",
                          LocalizedText.NULL_VALUE,
                          NodeIds.KeyValuePair,
                          1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> dataTypeSchemaHeader =
        structure.addChild(
            new ClientDataType(
                QualifiedName.parse("0:DataTypeSchemaHeader"),
                NodeIds.DataTypeSchemaHeader,
                NodeIds.DataTypeSchemaHeader_Encoding_DefaultBinary,
                NodeIds.DataTypeSchemaHeader_Encoding_DefaultXml,
                NodeIds.DataTypeSchemaHeader_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.DataTypeSchemaHeader_Encoding_DefaultBinary,
                    NodeIds.Structure,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "Namespaces",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "StructureDataTypes",
                          LocalizedText.NULL_VALUE,
                          NodeIds.StructureDescription,
                          1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "EnumDataTypes",
                          LocalizedText.NULL_VALUE,
                          NodeIds.EnumDescription,
                          1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "SimpleDataTypes",
                          LocalizedText.NULL_VALUE,
                          NodeIds.SimpleTypeDescription,
                          1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                true));
    Tree<DataType> dataSetMetaDataType =
        dataTypeSchemaHeader.addChild(
            new ClientDataType(
                QualifiedName.parse("0:DataSetMetaDataType"),
                NodeIds.DataSetMetaDataType,
                NodeIds.DataSetMetaDataType_Encoding_DefaultBinary,
                NodeIds.DataSetMetaDataType_Encoding_DefaultXml,
                NodeIds.DataSetMetaDataType_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.DataSetMetaDataType_Encoding_DefaultBinary,
                    NodeIds.DataTypeSchemaHeader,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "SimpleDataTypes",
                          LocalizedText.NULL_VALUE,
                          NodeIds.SimpleTypeDescription,
                          1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "EnumDataTypes",
                          LocalizedText.NULL_VALUE,
                          NodeIds.EnumDescription,
                          1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "StructureDataTypes",
                          LocalizedText.NULL_VALUE,
                          NodeIds.StructureDescription,
                          1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "Namespaces",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "Name",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "Description",
                          LocalizedText.NULL_VALUE,
                          NodeIds.LocalizedText,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "Fields",
                          LocalizedText.NULL_VALUE,
                          NodeIds.FieldMetaData,
                          1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "DataSetClassId",
                          LocalizedText.NULL_VALUE,
                          NodeIds.Guid,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "ConfigurationVersion",
                          LocalizedText.NULL_VALUE,
                          NodeIds.ConfigurationVersionDataType,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> uABinaryFileDataType =
        dataTypeSchemaHeader.addChild(
            new ClientDataType(
                QualifiedName.parse("0:UABinaryFileDataType"),
                NodeIds.UABinaryFileDataType,
                NodeIds.UABinaryFileDataType_Encoding_DefaultBinary,
                NodeIds.UABinaryFileDataType_Encoding_DefaultXml,
                NodeIds.UABinaryFileDataType_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.UABinaryFileDataType_Encoding_DefaultBinary,
                    NodeIds.DataTypeSchemaHeader,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "SimpleDataTypes",
                          LocalizedText.NULL_VALUE,
                          NodeIds.SimpleTypeDescription,
                          1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "EnumDataTypes",
                          LocalizedText.NULL_VALUE,
                          NodeIds.EnumDescription,
                          1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "StructureDataTypes",
                          LocalizedText.NULL_VALUE,
                          NodeIds.StructureDescription,
                          1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "Namespaces",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "SchemaLocation",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "FileHeader",
                          LocalizedText.NULL_VALUE,
                          NodeIds.KeyValuePair,
                          1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "Body",
                          LocalizedText.NULL_VALUE,
                          NodeIds.BaseDataType,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> publishedDataSetDataType =
        structure.addChild(
            new ClientDataType(
                QualifiedName.parse("0:PublishedDataSetDataType"),
                NodeIds.PublishedDataSetDataType,
                NodeIds.PublishedDataSetDataType_Encoding_DefaultBinary,
                NodeIds.PublishedDataSetDataType_Encoding_DefaultXml,
                NodeIds.PublishedDataSetDataType_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.PublishedDataSetDataType_Encoding_DefaultBinary,
                    NodeIds.Structure,
                    StructureType.StructureWithSubtypedValues,
                    new StructureField[] {
                      new StructureField(
                          "Name",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "DataSetFolder",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "DataSetMetaData",
                          LocalizedText.NULL_VALUE,
                          NodeIds.DataSetMetaDataType,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "ExtensionFields",
                          LocalizedText.NULL_VALUE,
                          NodeIds.KeyValuePair,
                          1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "DataSetSource",
                          LocalizedText.NULL_VALUE,
                          NodeIds.PublishedDataSetSourceDataType,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          true)
                    }),
                false));
    Tree<DataType> publishedDataSetSourceDataType =
        structure.addChild(
            new ClientDataType(
                QualifiedName.parse("0:PublishedDataSetSourceDataType"),
                NodeIds.PublishedDataSetSourceDataType,
                NodeIds.PublishedDataSetSourceDataType_Encoding_DefaultBinary,
                NodeIds.PublishedDataSetSourceDataType_Encoding_DefaultXml,
                NodeIds.PublishedDataSetSourceDataType_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.PublishedDataSetSourceDataType_Encoding_DefaultBinary,
                    NodeIds.Structure,
                    StructureType.Structure,
                    new StructureField[0]),
                true));
    Tree<DataType> publishedDataItemsDataType =
        publishedDataSetSourceDataType.addChild(
            new ClientDataType(
                QualifiedName.parse("0:PublishedDataItemsDataType"),
                NodeIds.PublishedDataItemsDataType,
                NodeIds.PublishedDataItemsDataType_Encoding_DefaultBinary,
                NodeIds.PublishedDataItemsDataType_Encoding_DefaultXml,
                NodeIds.PublishedDataItemsDataType_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.PublishedDataItemsDataType_Encoding_DefaultBinary,
                    NodeIds.PublishedDataSetSourceDataType,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "PublishedData",
                          LocalizedText.NULL_VALUE,
                          NodeIds.PublishedVariableDataType,
                          1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> publishedEventsDataType =
        publishedDataSetSourceDataType.addChild(
            new ClientDataType(
                QualifiedName.parse("0:PublishedEventsDataType"),
                NodeIds.PublishedEventsDataType,
                NodeIds.PublishedEventsDataType_Encoding_DefaultBinary,
                NodeIds.PublishedEventsDataType_Encoding_DefaultXml,
                NodeIds.PublishedEventsDataType_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.PublishedEventsDataType_Encoding_DefaultBinary,
                    NodeIds.PublishedDataSetSourceDataType,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "EventNotifier",
                          LocalizedText.NULL_VALUE,
                          NodeIds.NodeId,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "SelectedFields",
                          LocalizedText.NULL_VALUE,
                          NodeIds.SimpleAttributeOperand,
                          1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "Filter",
                          LocalizedText.NULL_VALUE,
                          NodeIds.ContentFilter,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> publishedActionDataType =
        publishedDataSetSourceDataType.addChild(
            new ClientDataType(
                QualifiedName.parse("0:PublishedActionDataType"),
                NodeIds.PublishedActionDataType,
                NodeIds.PublishedActionDataType_Encoding_DefaultBinary,
                NodeIds.PublishedActionDataType_Encoding_DefaultXml,
                NodeIds.PublishedActionDataType_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.PublishedActionDataType_Encoding_DefaultBinary,
                    NodeIds.PublishedDataSetSourceDataType,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "RequestDataSetMetaData",
                          LocalizedText.NULL_VALUE,
                          NodeIds.DataSetMetaDataType,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "ActionTargets",
                          LocalizedText.NULL_VALUE,
                          NodeIds.ActionTargetDataType,
                          1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> publishedActionMethodDataType =
        publishedActionDataType.addChild(
            new ClientDataType(
                QualifiedName.parse("0:PublishedActionMethodDataType"),
                NodeIds.PublishedActionMethodDataType,
                NodeIds.PublishedActionMethodDataType_Encoding_DefaultBinary,
                NodeIds.PublishedActionMethodDataType_Encoding_DefaultXml,
                NodeIds.PublishedActionMethodDataType_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.PublishedActionMethodDataType_Encoding_DefaultBinary,
                    NodeIds.PublishedActionDataType,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "ActionTargets",
                          LocalizedText.NULL_VALUE,
                          NodeIds.ActionTargetDataType,
                          1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "RequestDataSetMetaData",
                          LocalizedText.NULL_VALUE,
                          NodeIds.DataSetMetaDataType,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "ActionMethods",
                          LocalizedText.NULL_VALUE,
                          NodeIds.ActionMethodDataType,
                          1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> publishedDataSetCustomSourceDataType =
        publishedDataSetSourceDataType.addChild(
            new ClientDataType(
                QualifiedName.parse("0:PublishedDataSetCustomSourceDataType"),
                NodeIds.PublishedDataSetCustomSourceDataType,
                NodeIds.PublishedDataSetCustomSourceDataType_Encoding_DefaultBinary,
                NodeIds.PublishedDataSetCustomSourceDataType_Encoding_DefaultXml,
                NodeIds.PublishedDataSetCustomSourceDataType_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.PublishedDataSetCustomSourceDataType_Encoding_DefaultBinary,
                    NodeIds.PublishedDataSetSourceDataType,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "CyclicDataSet",
                          LocalizedText.NULL_VALUE,
                          NodeIds.Boolean,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> dataSetWriterDataType =
        structure.addChild(
            new ClientDataType(
                QualifiedName.parse("0:DataSetWriterDataType"),
                NodeIds.DataSetWriterDataType,
                NodeIds.DataSetWriterDataType_Encoding_DefaultBinary,
                NodeIds.DataSetWriterDataType_Encoding_DefaultXml,
                NodeIds.DataSetWriterDataType_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.DataSetWriterDataType_Encoding_DefaultBinary,
                    NodeIds.Structure,
                    StructureType.StructureWithSubtypedValues,
                    new StructureField[] {
                      new StructureField(
                          "Name",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "Enabled",
                          LocalizedText.NULL_VALUE,
                          NodeIds.Boolean,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "DataSetWriterId",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UInt16,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "DataSetFieldContentMask",
                          LocalizedText.NULL_VALUE,
                          NodeIds.DataSetFieldContentMask,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "KeyFrameCount",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UInt32,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "DataSetName",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "DataSetWriterProperties",
                          LocalizedText.NULL_VALUE,
                          NodeIds.KeyValuePair,
                          1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "TransportSettings",
                          LocalizedText.NULL_VALUE,
                          NodeIds.DataSetWriterTransportDataType,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          true),
                      new StructureField(
                          "MessageSettings",
                          LocalizedText.NULL_VALUE,
                          NodeIds.DataSetWriterMessageDataType,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          true)
                    }),
                false));
    Tree<DataType> dataSetWriterTransportDataType =
        structure.addChild(
            new ClientDataType(
                QualifiedName.parse("0:DataSetWriterTransportDataType"),
                NodeIds.DataSetWriterTransportDataType,
                NodeIds.DataSetWriterTransportDataType_Encoding_DefaultBinary,
                NodeIds.DataSetWriterTransportDataType_Encoding_DefaultXml,
                NodeIds.DataSetWriterTransportDataType_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.DataSetWriterTransportDataType_Encoding_DefaultBinary,
                    NodeIds.Structure,
                    StructureType.Structure,
                    new StructureField[0]),
                true));
    Tree<DataType> brokerDataSetWriterTransportDataType =
        dataSetWriterTransportDataType.addChild(
            new ClientDataType(
                QualifiedName.parse("0:BrokerDataSetWriterTransportDataType"),
                NodeIds.BrokerDataSetWriterTransportDataType,
                NodeIds.BrokerDataSetWriterTransportDataType_Encoding_DefaultBinary,
                NodeIds.BrokerDataSetWriterTransportDataType_Encoding_DefaultXml,
                NodeIds.BrokerDataSetWriterTransportDataType_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.BrokerDataSetWriterTransportDataType_Encoding_DefaultBinary,
                    NodeIds.DataSetWriterTransportDataType,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "QueueName",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "ResourceUri",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "AuthenticationProfileUri",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "RequestedDeliveryGuarantee",
                          LocalizedText.NULL_VALUE,
                          NodeIds.BrokerTransportQualityOfService,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "MetaDataQueueName",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "MetaDataUpdateTime",
                          LocalizedText.NULL_VALUE,
                          NodeIds.Duration,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> dataSetWriterMessageDataType =
        structure.addChild(
            new ClientDataType(
                QualifiedName.parse("0:DataSetWriterMessageDataType"),
                NodeIds.DataSetWriterMessageDataType,
                NodeIds.DataSetWriterMessageDataType_Encoding_DefaultBinary,
                NodeIds.DataSetWriterMessageDataType_Encoding_DefaultXml,
                NodeIds.DataSetWriterMessageDataType_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.DataSetWriterMessageDataType_Encoding_DefaultBinary,
                    NodeIds.Structure,
                    StructureType.Structure,
                    new StructureField[0]),
                true));
    Tree<DataType> uadpDataSetWriterMessageDataType =
        dataSetWriterMessageDataType.addChild(
            new ClientDataType(
                QualifiedName.parse("0:UadpDataSetWriterMessageDataType"),
                NodeIds.UadpDataSetWriterMessageDataType,
                NodeIds.UadpDataSetWriterMessageDataType_Encoding_DefaultBinary,
                NodeIds.UadpDataSetWriterMessageDataType_Encoding_DefaultXml,
                NodeIds.UadpDataSetWriterMessageDataType_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.UadpDataSetWriterMessageDataType_Encoding_DefaultBinary,
                    NodeIds.DataSetWriterMessageDataType,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "DataSetMessageContentMask",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UadpDataSetMessageContentMask,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "ConfiguredSize",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UInt16,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "NetworkMessageNumber",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UInt16,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "DataSetOffset",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UInt16,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> jsonDataSetWriterMessageDataType =
        dataSetWriterMessageDataType.addChild(
            new ClientDataType(
                QualifiedName.parse("0:JsonDataSetWriterMessageDataType"),
                NodeIds.JsonDataSetWriterMessageDataType,
                NodeIds.JsonDataSetWriterMessageDataType_Encoding_DefaultBinary,
                NodeIds.JsonDataSetWriterMessageDataType_Encoding_DefaultXml,
                NodeIds.JsonDataSetWriterMessageDataType_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.JsonDataSetWriterMessageDataType_Encoding_DefaultBinary,
                    NodeIds.DataSetWriterMessageDataType,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "DataSetMessageContentMask",
                          LocalizedText.NULL_VALUE,
                          NodeIds.JsonDataSetMessageContentMask,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> pubSubGroupDataType =
        structure.addChild(
            new ClientDataType(
                QualifiedName.parse("0:PubSubGroupDataType"),
                NodeIds.PubSubGroupDataType,
                NodeIds.PubSubGroupDataType_Encoding_DefaultBinary,
                NodeIds.PubSubGroupDataType_Encoding_DefaultXml,
                NodeIds.PubSubGroupDataType_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.PubSubGroupDataType_Encoding_DefaultBinary,
                    NodeIds.Structure,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "Name",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "Enabled",
                          LocalizedText.NULL_VALUE,
                          NodeIds.Boolean,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "SecurityMode",
                          LocalizedText.NULL_VALUE,
                          NodeIds.MessageSecurityMode,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "SecurityGroupId",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "SecurityKeyServices",
                          LocalizedText.NULL_VALUE,
                          NodeIds.EndpointDescription,
                          1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "MaxNetworkMessageSize",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UInt32,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "GroupProperties",
                          LocalizedText.NULL_VALUE,
                          NodeIds.KeyValuePair,
                          1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                true));
    Tree<DataType> writerGroupDataType =
        pubSubGroupDataType.addChild(
            new ClientDataType(
                QualifiedName.parse("0:WriterGroupDataType"),
                NodeIds.WriterGroupDataType,
                NodeIds.WriterGroupDataType_Encoding_DefaultBinary,
                NodeIds.WriterGroupDataType_Encoding_DefaultXml,
                NodeIds.WriterGroupDataType_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.WriterGroupDataType_Encoding_DefaultBinary,
                    NodeIds.PubSubGroupDataType,
                    StructureType.StructureWithSubtypedValues,
                    new StructureField[] {
                      new StructureField(
                          "GroupProperties",
                          LocalizedText.NULL_VALUE,
                          NodeIds.KeyValuePair,
                          1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "MaxNetworkMessageSize",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UInt32,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "SecurityKeyServices",
                          LocalizedText.NULL_VALUE,
                          NodeIds.EndpointDescription,
                          1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "SecurityGroupId",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "SecurityMode",
                          LocalizedText.NULL_VALUE,
                          NodeIds.MessageSecurityMode,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "Enabled",
                          LocalizedText.NULL_VALUE,
                          NodeIds.Boolean,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "Name",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "WriterGroupId",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UInt16,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "PublishingInterval",
                          LocalizedText.NULL_VALUE,
                          NodeIds.Duration,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "KeepAliveTime",
                          LocalizedText.NULL_VALUE,
                          NodeIds.Duration,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "Priority",
                          LocalizedText.NULL_VALUE,
                          NodeIds.Byte,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "LocaleIds",
                          LocalizedText.NULL_VALUE,
                          NodeIds.LocaleId,
                          1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "HeaderLayoutUri",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "TransportSettings",
                          LocalizedText.NULL_VALUE,
                          NodeIds.WriterGroupTransportDataType,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          true),
                      new StructureField(
                          "MessageSettings",
                          LocalizedText.NULL_VALUE,
                          NodeIds.WriterGroupMessageDataType,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          true),
                      new StructureField(
                          "DataSetWriters",
                          LocalizedText.NULL_VALUE,
                          NodeIds.DataSetWriterDataType,
                          1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> readerGroupDataType =
        pubSubGroupDataType.addChild(
            new ClientDataType(
                QualifiedName.parse("0:ReaderGroupDataType"),
                NodeIds.ReaderGroupDataType,
                NodeIds.ReaderGroupDataType_Encoding_DefaultBinary,
                NodeIds.ReaderGroupDataType_Encoding_DefaultXml,
                NodeIds.ReaderGroupDataType_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.ReaderGroupDataType_Encoding_DefaultBinary,
                    NodeIds.PubSubGroupDataType,
                    StructureType.StructureWithSubtypedValues,
                    new StructureField[] {
                      new StructureField(
                          "GroupProperties",
                          LocalizedText.NULL_VALUE,
                          NodeIds.KeyValuePair,
                          1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "MaxNetworkMessageSize",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UInt32,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "SecurityKeyServices",
                          LocalizedText.NULL_VALUE,
                          NodeIds.EndpointDescription,
                          1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "SecurityGroupId",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "SecurityMode",
                          LocalizedText.NULL_VALUE,
                          NodeIds.MessageSecurityMode,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "Enabled",
                          LocalizedText.NULL_VALUE,
                          NodeIds.Boolean,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "Name",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "TransportSettings",
                          LocalizedText.NULL_VALUE,
                          NodeIds.ReaderGroupTransportDataType,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          true),
                      new StructureField(
                          "MessageSettings",
                          LocalizedText.NULL_VALUE,
                          NodeIds.ReaderGroupMessageDataType,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          true),
                      new StructureField(
                          "DataSetReaders",
                          LocalizedText.NULL_VALUE,
                          NodeIds.DataSetReaderDataType,
                          1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> writerGroupTransportDataType =
        structure.addChild(
            new ClientDataType(
                QualifiedName.parse("0:WriterGroupTransportDataType"),
                NodeIds.WriterGroupTransportDataType,
                NodeIds.WriterGroupTransportDataType_Encoding_DefaultBinary,
                NodeIds.WriterGroupTransportDataType_Encoding_DefaultXml,
                NodeIds.WriterGroupTransportDataType_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.WriterGroupTransportDataType_Encoding_DefaultBinary,
                    NodeIds.Structure,
                    StructureType.Structure,
                    new StructureField[0]),
                true));
    Tree<DataType> datagramWriterGroupTransportDataType =
        writerGroupTransportDataType.addChild(
            new ClientDataType(
                QualifiedName.parse("0:DatagramWriterGroupTransportDataType"),
                NodeIds.DatagramWriterGroupTransportDataType,
                NodeIds.DatagramWriterGroupTransportDataType_Encoding_DefaultBinary,
                NodeIds.DatagramWriterGroupTransportDataType_Encoding_DefaultXml,
                NodeIds.DatagramWriterGroupTransportDataType_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.DatagramWriterGroupTransportDataType_Encoding_DefaultBinary,
                    NodeIds.WriterGroupTransportDataType,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "MessageRepeatCount",
                          LocalizedText.NULL_VALUE,
                          NodeIds.Byte,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "MessageRepeatDelay",
                          LocalizedText.NULL_VALUE,
                          NodeIds.Duration,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> datagramWriterGroupTransport2DataType =
        datagramWriterGroupTransportDataType.addChild(
            new ClientDataType(
                QualifiedName.parse("0:DatagramWriterGroupTransport2DataType"),
                NodeIds.DatagramWriterGroupTransport2DataType,
                NodeIds.DatagramWriterGroupTransport2DataType_Encoding_DefaultBinary,
                NodeIds.DatagramWriterGroupTransport2DataType_Encoding_DefaultXml,
                NodeIds.DatagramWriterGroupTransport2DataType_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.DatagramWriterGroupTransport2DataType_Encoding_DefaultBinary,
                    NodeIds.DatagramWriterGroupTransportDataType,
                    StructureType.StructureWithSubtypedValues,
                    new StructureField[] {
                      new StructureField(
                          "MessageRepeatDelay",
                          LocalizedText.NULL_VALUE,
                          NodeIds.Duration,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "MessageRepeatCount",
                          LocalizedText.NULL_VALUE,
                          NodeIds.Byte,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "Address",
                          LocalizedText.NULL_VALUE,
                          NodeIds.NetworkAddressDataType,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          true),
                      new StructureField(
                          "QosCategory",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "DatagramQos",
                          LocalizedText.NULL_VALUE,
                          NodeIds.TransmitQosDataType,
                          1,
                          null,
                          UInteger.valueOf(0),
                          true),
                      new StructureField(
                          "DiscoveryAnnounceRate",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UInt32,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "Topic",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> brokerWriterGroupTransportDataType =
        writerGroupTransportDataType.addChild(
            new ClientDataType(
                QualifiedName.parse("0:BrokerWriterGroupTransportDataType"),
                NodeIds.BrokerWriterGroupTransportDataType,
                NodeIds.BrokerWriterGroupTransportDataType_Encoding_DefaultBinary,
                NodeIds.BrokerWriterGroupTransportDataType_Encoding_DefaultXml,
                NodeIds.BrokerWriterGroupTransportDataType_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.BrokerWriterGroupTransportDataType_Encoding_DefaultBinary,
                    NodeIds.WriterGroupTransportDataType,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "QueueName",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "ResourceUri",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "AuthenticationProfileUri",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "RequestedDeliveryGuarantee",
                          LocalizedText.NULL_VALUE,
                          NodeIds.BrokerTransportQualityOfService,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> writerGroupMessageDataType =
        structure.addChild(
            new ClientDataType(
                QualifiedName.parse("0:WriterGroupMessageDataType"),
                NodeIds.WriterGroupMessageDataType,
                NodeIds.WriterGroupMessageDataType_Encoding_DefaultBinary,
                NodeIds.WriterGroupMessageDataType_Encoding_DefaultXml,
                NodeIds.WriterGroupMessageDataType_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.WriterGroupMessageDataType_Encoding_DefaultBinary,
                    NodeIds.Structure,
                    StructureType.Structure,
                    new StructureField[0]),
                true));
    Tree<DataType> uadpWriterGroupMessageDataType =
        writerGroupMessageDataType.addChild(
            new ClientDataType(
                QualifiedName.parse("0:UadpWriterGroupMessageDataType"),
                NodeIds.UadpWriterGroupMessageDataType,
                NodeIds.UadpWriterGroupMessageDataType_Encoding_DefaultBinary,
                NodeIds.UadpWriterGroupMessageDataType_Encoding_DefaultXml,
                NodeIds.UadpWriterGroupMessageDataType_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.UadpWriterGroupMessageDataType_Encoding_DefaultBinary,
                    NodeIds.WriterGroupMessageDataType,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "GroupVersion",
                          LocalizedText.NULL_VALUE,
                          NodeIds.VersionTime,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "DataSetOrdering",
                          LocalizedText.NULL_VALUE,
                          NodeIds.DataSetOrderingType,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "NetworkMessageContentMask",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UadpNetworkMessageContentMask,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "SamplingOffset",
                          LocalizedText.NULL_VALUE,
                          NodeIds.Duration,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "PublishingOffset",
                          LocalizedText.NULL_VALUE,
                          NodeIds.Duration,
                          1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> jsonWriterGroupMessageDataType =
        writerGroupMessageDataType.addChild(
            new ClientDataType(
                QualifiedName.parse("0:JsonWriterGroupMessageDataType"),
                NodeIds.JsonWriterGroupMessageDataType,
                NodeIds.JsonWriterGroupMessageDataType_Encoding_DefaultBinary,
                NodeIds.JsonWriterGroupMessageDataType_Encoding_DefaultXml,
                NodeIds.JsonWriterGroupMessageDataType_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.JsonWriterGroupMessageDataType_Encoding_DefaultBinary,
                    NodeIds.WriterGroupMessageDataType,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "NetworkMessageContentMask",
                          LocalizedText.NULL_VALUE,
                          NodeIds.JsonNetworkMessageContentMask,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> pubSubConnectionDataType =
        structure.addChild(
            new ClientDataType(
                QualifiedName.parse("0:PubSubConnectionDataType"),
                NodeIds.PubSubConnectionDataType,
                NodeIds.PubSubConnectionDataType_Encoding_DefaultBinary,
                NodeIds.PubSubConnectionDataType_Encoding_DefaultXml,
                NodeIds.PubSubConnectionDataType_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.PubSubConnectionDataType_Encoding_DefaultBinary,
                    NodeIds.Structure,
                    StructureType.StructureWithSubtypedValues,
                    new StructureField[] {
                      new StructureField(
                          "Name",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "Enabled",
                          LocalizedText.NULL_VALUE,
                          NodeIds.Boolean,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "PublisherId",
                          LocalizedText.NULL_VALUE,
                          NodeIds.BaseDataType,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "TransportProfileUri",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "Address",
                          LocalizedText.NULL_VALUE,
                          NodeIds.NetworkAddressDataType,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          true),
                      new StructureField(
                          "ConnectionProperties",
                          LocalizedText.NULL_VALUE,
                          NodeIds.KeyValuePair,
                          1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "TransportSettings",
                          LocalizedText.NULL_VALUE,
                          NodeIds.ConnectionTransportDataType,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          true),
                      new StructureField(
                          "WriterGroups",
                          LocalizedText.NULL_VALUE,
                          NodeIds.WriterGroupDataType,
                          1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "ReaderGroups",
                          LocalizedText.NULL_VALUE,
                          NodeIds.ReaderGroupDataType,
                          1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> connectionTransportDataType =
        structure.addChild(
            new ClientDataType(
                QualifiedName.parse("0:ConnectionTransportDataType"),
                NodeIds.ConnectionTransportDataType,
                NodeIds.ConnectionTransportDataType_Encoding_DefaultBinary,
                NodeIds.ConnectionTransportDataType_Encoding_DefaultXml,
                NodeIds.ConnectionTransportDataType_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.ConnectionTransportDataType_Encoding_DefaultBinary,
                    NodeIds.Structure,
                    StructureType.Structure,
                    new StructureField[0]),
                true));
    Tree<DataType> brokerConnectionTransportDataType =
        connectionTransportDataType.addChild(
            new ClientDataType(
                QualifiedName.parse("0:BrokerConnectionTransportDataType"),
                NodeIds.BrokerConnectionTransportDataType,
                NodeIds.BrokerConnectionTransportDataType_Encoding_DefaultBinary,
                NodeIds.BrokerConnectionTransportDataType_Encoding_DefaultXml,
                NodeIds.BrokerConnectionTransportDataType_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.BrokerConnectionTransportDataType_Encoding_DefaultBinary,
                    NodeIds.ConnectionTransportDataType,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "ResourceUri",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "AuthenticationProfileUri",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> datagramConnectionTransportDataType =
        connectionTransportDataType.addChild(
            new ClientDataType(
                QualifiedName.parse("0:DatagramConnectionTransportDataType"),
                NodeIds.DatagramConnectionTransportDataType,
                NodeIds.DatagramConnectionTransportDataType_Encoding_DefaultBinary,
                NodeIds.DatagramConnectionTransportDataType_Encoding_DefaultXml,
                NodeIds.DatagramConnectionTransportDataType_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.DatagramConnectionTransportDataType_Encoding_DefaultBinary,
                    NodeIds.ConnectionTransportDataType,
                    StructureType.StructureWithSubtypedValues,
                    new StructureField[] {
                      new StructureField(
                          "DiscoveryAddress",
                          LocalizedText.NULL_VALUE,
                          NodeIds.NetworkAddressDataType,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          true)
                    }),
                false));
    Tree<DataType> datagramConnectionTransport2DataType =
        datagramConnectionTransportDataType.addChild(
            new ClientDataType(
                QualifiedName.parse("0:DatagramConnectionTransport2DataType"),
                NodeIds.DatagramConnectionTransport2DataType,
                NodeIds.DatagramConnectionTransport2DataType_Encoding_DefaultBinary,
                NodeIds.DatagramConnectionTransport2DataType_Encoding_DefaultXml,
                NodeIds.DatagramConnectionTransport2DataType_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.DatagramConnectionTransport2DataType_Encoding_DefaultBinary,
                    NodeIds.DatagramConnectionTransportDataType,
                    StructureType.StructureWithSubtypedValues,
                    new StructureField[] {
                      new StructureField(
                          "DiscoveryAddress",
                          LocalizedText.NULL_VALUE,
                          NodeIds.NetworkAddressDataType,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          true),
                      new StructureField(
                          "DiscoveryAnnounceRate",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UInt32,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "DiscoveryMaxMessageSize",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UInt32,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "QosCategory",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "DatagramQos",
                          LocalizedText.NULL_VALUE,
                          NodeIds.QosDataType,
                          1,
                          null,
                          UInteger.valueOf(0),
                          true)
                    }),
                false));
    Tree<DataType> readerGroupTransportDataType =
        structure.addChild(
            new ClientDataType(
                QualifiedName.parse("0:ReaderGroupTransportDataType"),
                NodeIds.ReaderGroupTransportDataType,
                NodeIds.ReaderGroupTransportDataType_Encoding_DefaultBinary,
                NodeIds.ReaderGroupTransportDataType_Encoding_DefaultXml,
                NodeIds.ReaderGroupTransportDataType_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.ReaderGroupTransportDataType_Encoding_DefaultBinary,
                    NodeIds.Structure,
                    StructureType.Structure,
                    new StructureField[0]),
                true));
    Tree<DataType> readerGroupMessageDataType =
        structure.addChild(
            new ClientDataType(
                QualifiedName.parse("0:ReaderGroupMessageDataType"),
                NodeIds.ReaderGroupMessageDataType,
                NodeIds.ReaderGroupMessageDataType_Encoding_DefaultBinary,
                NodeIds.ReaderGroupMessageDataType_Encoding_DefaultXml,
                NodeIds.ReaderGroupMessageDataType_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.ReaderGroupMessageDataType_Encoding_DefaultBinary,
                    NodeIds.Structure,
                    StructureType.Structure,
                    new StructureField[0]),
                true));
    Tree<DataType> dataSetReaderDataType =
        structure.addChild(
            new ClientDataType(
                QualifiedName.parse("0:DataSetReaderDataType"),
                NodeIds.DataSetReaderDataType,
                NodeIds.DataSetReaderDataType_Encoding_DefaultBinary,
                NodeIds.DataSetReaderDataType_Encoding_DefaultXml,
                NodeIds.DataSetReaderDataType_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.DataSetReaderDataType_Encoding_DefaultBinary,
                    NodeIds.Structure,
                    StructureType.StructureWithSubtypedValues,
                    new StructureField[] {
                      new StructureField(
                          "Name",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "Enabled",
                          LocalizedText.NULL_VALUE,
                          NodeIds.Boolean,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "PublisherId",
                          LocalizedText.NULL_VALUE,
                          NodeIds.BaseDataType,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "WriterGroupId",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UInt16,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "DataSetWriterId",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UInt16,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "DataSetMetaData",
                          LocalizedText.NULL_VALUE,
                          NodeIds.DataSetMetaDataType,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "DataSetFieldContentMask",
                          LocalizedText.NULL_VALUE,
                          NodeIds.DataSetFieldContentMask,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "MessageReceiveTimeout",
                          LocalizedText.NULL_VALUE,
                          NodeIds.Duration,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "KeyFrameCount",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UInt32,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "HeaderLayoutUri",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "SecurityMode",
                          LocalizedText.NULL_VALUE,
                          NodeIds.MessageSecurityMode,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "SecurityGroupId",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "SecurityKeyServices",
                          LocalizedText.NULL_VALUE,
                          NodeIds.EndpointDescription,
                          1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "DataSetReaderProperties",
                          LocalizedText.NULL_VALUE,
                          NodeIds.KeyValuePair,
                          1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "TransportSettings",
                          LocalizedText.NULL_VALUE,
                          NodeIds.DataSetReaderTransportDataType,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          true),
                      new StructureField(
                          "MessageSettings",
                          LocalizedText.NULL_VALUE,
                          NodeIds.DataSetReaderMessageDataType,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          true),
                      new StructureField(
                          "SubscribedDataSet",
                          LocalizedText.NULL_VALUE,
                          NodeIds.SubscribedDataSetDataType,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          true)
                    }),
                false));
    Tree<DataType> dataSetReaderTransportDataType =
        structure.addChild(
            new ClientDataType(
                QualifiedName.parse("0:DataSetReaderTransportDataType"),
                NodeIds.DataSetReaderTransportDataType,
                NodeIds.DataSetReaderTransportDataType_Encoding_DefaultBinary,
                NodeIds.DataSetReaderTransportDataType_Encoding_DefaultXml,
                NodeIds.DataSetReaderTransportDataType_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.DataSetReaderTransportDataType_Encoding_DefaultBinary,
                    NodeIds.Structure,
                    StructureType.Structure,
                    new StructureField[0]),
                true));
    Tree<DataType> brokerDataSetReaderTransportDataType =
        dataSetReaderTransportDataType.addChild(
            new ClientDataType(
                QualifiedName.parse("0:BrokerDataSetReaderTransportDataType"),
                NodeIds.BrokerDataSetReaderTransportDataType,
                NodeIds.BrokerDataSetReaderTransportDataType_Encoding_DefaultBinary,
                NodeIds.BrokerDataSetReaderTransportDataType_Encoding_DefaultXml,
                NodeIds.BrokerDataSetReaderTransportDataType_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.BrokerDataSetReaderTransportDataType_Encoding_DefaultBinary,
                    NodeIds.DataSetReaderTransportDataType,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "QueueName",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "ResourceUri",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "AuthenticationProfileUri",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "RequestedDeliveryGuarantee",
                          LocalizedText.NULL_VALUE,
                          NodeIds.BrokerTransportQualityOfService,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "MetaDataQueueName",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> datagramDataSetReaderTransportDataType =
        dataSetReaderTransportDataType.addChild(
            new ClientDataType(
                QualifiedName.parse("0:DatagramDataSetReaderTransportDataType"),
                NodeIds.DatagramDataSetReaderTransportDataType,
                NodeIds.DatagramDataSetReaderTransportDataType_Encoding_DefaultBinary,
                NodeIds.DatagramDataSetReaderTransportDataType_Encoding_DefaultXml,
                NodeIds.DatagramDataSetReaderTransportDataType_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.DatagramDataSetReaderTransportDataType_Encoding_DefaultBinary,
                    NodeIds.DataSetReaderTransportDataType,
                    StructureType.StructureWithSubtypedValues,
                    new StructureField[] {
                      new StructureField(
                          "Address",
                          LocalizedText.NULL_VALUE,
                          NodeIds.NetworkAddressDataType,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          true),
                      new StructureField(
                          "QosCategory",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "DatagramQos",
                          LocalizedText.NULL_VALUE,
                          NodeIds.ReceiveQosDataType,
                          1,
                          null,
                          UInteger.valueOf(0),
                          true),
                      new StructureField(
                          "Topic",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> dataSetReaderMessageDataType =
        structure.addChild(
            new ClientDataType(
                QualifiedName.parse("0:DataSetReaderMessageDataType"),
                NodeIds.DataSetReaderMessageDataType,
                NodeIds.DataSetReaderMessageDataType_Encoding_DefaultBinary,
                NodeIds.DataSetReaderMessageDataType_Encoding_DefaultXml,
                NodeIds.DataSetReaderMessageDataType_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.DataSetReaderMessageDataType_Encoding_DefaultBinary,
                    NodeIds.Structure,
                    StructureType.Structure,
                    new StructureField[0]),
                true));
    Tree<DataType> uadpDataSetReaderMessageDataType =
        dataSetReaderMessageDataType.addChild(
            new ClientDataType(
                QualifiedName.parse("0:UadpDataSetReaderMessageDataType"),
                NodeIds.UadpDataSetReaderMessageDataType,
                NodeIds.UadpDataSetReaderMessageDataType_Encoding_DefaultBinary,
                NodeIds.UadpDataSetReaderMessageDataType_Encoding_DefaultXml,
                NodeIds.UadpDataSetReaderMessageDataType_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.UadpDataSetReaderMessageDataType_Encoding_DefaultBinary,
                    NodeIds.DataSetReaderMessageDataType,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "GroupVersion",
                          LocalizedText.NULL_VALUE,
                          NodeIds.VersionTime,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "NetworkMessageNumber",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UInt16,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "DataSetOffset",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UInt16,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "DataSetClassId",
                          LocalizedText.NULL_VALUE,
                          NodeIds.Guid,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "NetworkMessageContentMask",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UadpNetworkMessageContentMask,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "DataSetMessageContentMask",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UadpDataSetMessageContentMask,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "PublishingInterval",
                          LocalizedText.NULL_VALUE,
                          NodeIds.Duration,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "ReceiveOffset",
                          LocalizedText.NULL_VALUE,
                          NodeIds.Duration,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "ProcessingOffset",
                          LocalizedText.NULL_VALUE,
                          NodeIds.Duration,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> jsonDataSetReaderMessageDataType =
        dataSetReaderMessageDataType.addChild(
            new ClientDataType(
                QualifiedName.parse("0:JsonDataSetReaderMessageDataType"),
                NodeIds.JsonDataSetReaderMessageDataType,
                NodeIds.JsonDataSetReaderMessageDataType_Encoding_DefaultBinary,
                NodeIds.JsonDataSetReaderMessageDataType_Encoding_DefaultXml,
                NodeIds.JsonDataSetReaderMessageDataType_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.JsonDataSetReaderMessageDataType_Encoding_DefaultBinary,
                    NodeIds.DataSetReaderMessageDataType,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "NetworkMessageContentMask",
                          LocalizedText.NULL_VALUE,
                          NodeIds.JsonNetworkMessageContentMask,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "DataSetMessageContentMask",
                          LocalizedText.NULL_VALUE,
                          NodeIds.JsonDataSetMessageContentMask,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> subscribedDataSetDataType =
        structure.addChild(
            new ClientDataType(
                QualifiedName.parse("0:SubscribedDataSetDataType"),
                NodeIds.SubscribedDataSetDataType,
                NodeIds.SubscribedDataSetDataType_Encoding_DefaultBinary,
                NodeIds.SubscribedDataSetDataType_Encoding_DefaultXml,
                NodeIds.SubscribedDataSetDataType_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.SubscribedDataSetDataType_Encoding_DefaultBinary,
                    NodeIds.Structure,
                    StructureType.Structure,
                    new StructureField[0]),
                true));
    Tree<DataType> targetVariablesDataType =
        subscribedDataSetDataType.addChild(
            new ClientDataType(
                QualifiedName.parse("0:TargetVariablesDataType"),
                NodeIds.TargetVariablesDataType,
                NodeIds.TargetVariablesDataType_Encoding_DefaultBinary,
                NodeIds.TargetVariablesDataType_Encoding_DefaultXml,
                NodeIds.TargetVariablesDataType_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.TargetVariablesDataType_Encoding_DefaultBinary,
                    NodeIds.SubscribedDataSetDataType,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "TargetVariables",
                          LocalizedText.NULL_VALUE,
                          NodeIds.FieldTargetDataType,
                          1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> subscribedDataSetMirrorDataType =
        subscribedDataSetDataType.addChild(
            new ClientDataType(
                QualifiedName.parse("0:SubscribedDataSetMirrorDataType"),
                NodeIds.SubscribedDataSetMirrorDataType,
                NodeIds.SubscribedDataSetMirrorDataType_Encoding_DefaultBinary,
                NodeIds.SubscribedDataSetMirrorDataType_Encoding_DefaultXml,
                NodeIds.SubscribedDataSetMirrorDataType_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.SubscribedDataSetMirrorDataType_Encoding_DefaultBinary,
                    NodeIds.SubscribedDataSetDataType,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "ParentNodeName",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "RolePermissions",
                          LocalizedText.NULL_VALUE,
                          NodeIds.RolePermissionType,
                          1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> standaloneSubscribedDataSetRefDataType =
        subscribedDataSetDataType.addChild(
            new ClientDataType(
                QualifiedName.parse("0:StandaloneSubscribedDataSetRefDataType"),
                NodeIds.StandaloneSubscribedDataSetRefDataType,
                NodeIds.StandaloneSubscribedDataSetRefDataType_Encoding_DefaultBinary,
                NodeIds.StandaloneSubscribedDataSetRefDataType_Encoding_DefaultXml,
                NodeIds.StandaloneSubscribedDataSetRefDataType_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.StandaloneSubscribedDataSetRefDataType_Encoding_DefaultBinary,
                    NodeIds.SubscribedDataSetDataType,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "DataSetName",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> standaloneSubscribedDataSetDataType =
        subscribedDataSetDataType.addChild(
            new ClientDataType(
                QualifiedName.parse("0:StandaloneSubscribedDataSetDataType"),
                NodeIds.StandaloneSubscribedDataSetDataType,
                NodeIds.StandaloneSubscribedDataSetDataType_Encoding_DefaultBinary,
                NodeIds.StandaloneSubscribedDataSetDataType_Encoding_DefaultXml,
                NodeIds.StandaloneSubscribedDataSetDataType_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.StandaloneSubscribedDataSetDataType_Encoding_DefaultBinary,
                    NodeIds.SubscribedDataSetDataType,
                    StructureType.StructureWithSubtypedValues,
                    new StructureField[] {
                      new StructureField(
                          "Name",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "DataSetFolder",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "DataSetMetaData",
                          LocalizedText.NULL_VALUE,
                          NodeIds.DataSetMetaDataType,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "SubscribedDataSet",
                          LocalizedText.NULL_VALUE,
                          NodeIds.SubscribedDataSetDataType,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          true)
                    }),
                false));
    Tree<DataType> identityMappingRuleType =
        structure.addChild(
            new ClientDataType(
                QualifiedName.parse("0:IdentityMappingRuleType"),
                NodeIds.IdentityMappingRuleType,
                NodeIds.IdentityMappingRuleType_Encoding_DefaultBinary,
                NodeIds.IdentityMappingRuleType_Encoding_DefaultXml,
                NodeIds.IdentityMappingRuleType_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.IdentityMappingRuleType_Encoding_DefaultBinary,
                    NodeIds.Structure,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "CriteriaType",
                          LocalizedText.NULL_VALUE,
                          NodeIds.IdentityCriteriaType,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "Criteria",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> additionalParametersType =
        structure.addChild(
            new ClientDataType(
                QualifiedName.parse("0:AdditionalParametersType"),
                NodeIds.AdditionalParametersType,
                NodeIds.AdditionalParametersType_Encoding_DefaultBinary,
                NodeIds.AdditionalParametersType_Encoding_DefaultXml,
                NodeIds.AdditionalParametersType_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.AdditionalParametersType_Encoding_DefaultBinary,
                    NodeIds.Structure,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "Parameters",
                          LocalizedText.NULL_VALUE,
                          NodeIds.KeyValuePair,
                          1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> ephemeralKeyType =
        structure.addChild(
            new ClientDataType(
                QualifiedName.parse("0:EphemeralKeyType"),
                NodeIds.EphemeralKeyType,
                NodeIds.EphemeralKeyType_Encoding_DefaultBinary,
                NodeIds.EphemeralKeyType_Encoding_DefaultXml,
                NodeIds.EphemeralKeyType_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.EphemeralKeyType_Encoding_DefaultBinary,
                    NodeIds.Structure,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "PublicKey",
                          LocalizedText.NULL_VALUE,
                          NodeIds.ByteString,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "Signature",
                          LocalizedText.NULL_VALUE,
                          NodeIds.ByteString,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> decimalDataType =
        structure.addChild(
            new ClientDataType(
                QualifiedName.parse("0:DecimalDataType"),
                NodeIds.DecimalDataType,
                NodeIds.DecimalDataType_Encoding_DefaultBinary,
                NodeIds.DecimalDataType_Encoding_DefaultXml,
                NodeIds.DecimalDataType_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.DecimalDataType_Encoding_DefaultBinary,
                    NodeIds.Structure,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "Scale",
                          LocalizedText.NULL_VALUE,
                          NodeIds.Int16,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "Value",
                          LocalizedText.NULL_VALUE,
                          NodeIds.ByteString,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> actionTargetDataType =
        structure.addChild(
            new ClientDataType(
                QualifiedName.parse("0:ActionTargetDataType"),
                NodeIds.ActionTargetDataType,
                NodeIds.ActionTargetDataType_Encoding_DefaultBinary,
                NodeIds.ActionTargetDataType_Encoding_DefaultXml,
                NodeIds.ActionTargetDataType_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.ActionTargetDataType_Encoding_DefaultBinary,
                    NodeIds.Structure,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "ActionTargetId",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UInt16,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "Name",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "Description",
                          LocalizedText.NULL_VALUE,
                          NodeIds.LocalizedText,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> actionMethodDataType =
        structure.addChild(
            new ClientDataType(
                QualifiedName.parse("0:ActionMethodDataType"),
                NodeIds.ActionMethodDataType,
                NodeIds.ActionMethodDataType_Encoding_DefaultBinary,
                NodeIds.ActionMethodDataType_Encoding_DefaultXml,
                NodeIds.ActionMethodDataType_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.ActionMethodDataType_Encoding_DefaultBinary,
                    NodeIds.Structure,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "ObjectId",
                          LocalizedText.NULL_VALUE,
                          NodeIds.NodeId,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "MethodId",
                          LocalizedText.NULL_VALUE,
                          NodeIds.NodeId,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> dtlsPubSubConnectionDataType =
        structure.addChild(
            new ClientDataType(
                QualifiedName.parse("0:DtlsPubSubConnectionDataType"),
                NodeIds.DtlsPubSubConnectionDataType,
                NodeIds.DtlsPubSubConnectionDataType_Encoding_DefaultBinary,
                NodeIds.DtlsPubSubConnectionDataType_Encoding_DefaultXml,
                NodeIds.DtlsPubSubConnectionDataType_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.DtlsPubSubConnectionDataType_Encoding_DefaultBinary,
                    NodeIds.Structure,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "ClientCipherSuite",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "ServerCipherSuites",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "ZeroRTT",
                          LocalizedText.NULL_VALUE,
                          NodeIds.Boolean,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "CertificateGroupId",
                          LocalizedText.NULL_VALUE,
                          NodeIds.NodeId,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "VerifyClientCertificate",
                          LocalizedText.NULL_VALUE,
                          NodeIds.Boolean,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> rationalNumber =
        structure.addChild(
            new ClientDataType(
                QualifiedName.parse("0:RationalNumber"),
                NodeIds.RationalNumber,
                NodeIds.RationalNumber_Encoding_DefaultBinary,
                NodeIds.RationalNumber_Encoding_DefaultXml,
                NodeIds.RationalNumber_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.RationalNumber_Encoding_DefaultBinary,
                    NodeIds.Structure,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "Numerator",
                          LocalizedText.NULL_VALUE,
                          NodeIds.Int32,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "Denominator",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UInt32,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> vector =
        structure.addChild(
            new ClientDataType(
                QualifiedName.parse("0:Vector"),
                NodeIds.Vector,
                NodeIds.Vector_Encoding_DefaultBinary,
                NodeIds.Vector_Encoding_DefaultXml,
                NodeIds.Vector_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.Vector_Encoding_DefaultBinary,
                    NodeIds.Structure,
                    StructureType.Structure,
                    new StructureField[0]),
                true));
    Tree<DataType> _3DVector =
        vector.addChild(
            new ClientDataType(
                QualifiedName.parse("0:3DVector"),
                NodeIds.ThreeDVector,
                NodeIds.ThreeDVector_Encoding_DefaultBinary,
                NodeIds.ThreeDVector_Encoding_DefaultXml,
                NodeIds.ThreeDVector_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.ThreeDVector_Encoding_DefaultBinary,
                    NodeIds.Vector,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "X",
                          LocalizedText.NULL_VALUE,
                          NodeIds.Double,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "Y",
                          LocalizedText.NULL_VALUE,
                          NodeIds.Double,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "Z",
                          LocalizedText.NULL_VALUE,
                          NodeIds.Double,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> cartesianCoordinates =
        structure.addChild(
            new ClientDataType(
                QualifiedName.parse("0:CartesianCoordinates"),
                NodeIds.CartesianCoordinates,
                NodeIds.CartesianCoordinates_Encoding_DefaultBinary,
                NodeIds.CartesianCoordinates_Encoding_DefaultXml,
                NodeIds.CartesianCoordinates_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.CartesianCoordinates_Encoding_DefaultBinary,
                    NodeIds.Structure,
                    StructureType.Structure,
                    new StructureField[0]),
                true));
    Tree<DataType> _3DCartesianCoordinates =
        cartesianCoordinates.addChild(
            new ClientDataType(
                QualifiedName.parse("0:3DCartesianCoordinates"),
                NodeIds.ThreeDCartesianCoordinates,
                NodeIds.ThreeDCartesianCoordinates_Encoding_DefaultBinary,
                NodeIds.ThreeDCartesianCoordinates_Encoding_DefaultXml,
                NodeIds.ThreeDCartesianCoordinates_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.ThreeDCartesianCoordinates_Encoding_DefaultBinary,
                    NodeIds.CartesianCoordinates,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "X",
                          LocalizedText.NULL_VALUE,
                          NodeIds.Double,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "Y",
                          LocalizedText.NULL_VALUE,
                          NodeIds.Double,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "Z",
                          LocalizedText.NULL_VALUE,
                          NodeIds.Double,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> orientation =
        structure.addChild(
            new ClientDataType(
                QualifiedName.parse("0:Orientation"),
                NodeIds.Orientation,
                NodeIds.Orientation_Encoding_DefaultBinary,
                NodeIds.Orientation_Encoding_DefaultXml,
                NodeIds.Orientation_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.Orientation_Encoding_DefaultBinary,
                    NodeIds.Structure,
                    StructureType.Structure,
                    new StructureField[0]),
                true));
    Tree<DataType> _3DOrientation =
        orientation.addChild(
            new ClientDataType(
                QualifiedName.parse("0:3DOrientation"),
                NodeIds.ThreeDOrientation,
                NodeIds.ThreeDOrientation_Encoding_DefaultBinary,
                NodeIds.ThreeDOrientation_Encoding_DefaultXml,
                NodeIds.ThreeDOrientation_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.ThreeDOrientation_Encoding_DefaultBinary,
                    NodeIds.Orientation,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "A",
                          LocalizedText.NULL_VALUE,
                          NodeIds.Double,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "B",
                          LocalizedText.NULL_VALUE,
                          NodeIds.Double,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "C",
                          LocalizedText.NULL_VALUE,
                          NodeIds.Double,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> frame =
        structure.addChild(
            new ClientDataType(
                QualifiedName.parse("0:Frame"),
                NodeIds.Frame,
                NodeIds.Frame_Encoding_DefaultBinary,
                NodeIds.Frame_Encoding_DefaultXml,
                NodeIds.Frame_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.Frame_Encoding_DefaultBinary,
                    NodeIds.Structure,
                    StructureType.Structure,
                    new StructureField[0]),
                true));
    Tree<DataType> _3DFrame =
        frame.addChild(
            new ClientDataType(
                QualifiedName.parse("0:3DFrame"),
                NodeIds.ThreeDFrame,
                NodeIds.ThreeDFrame_Encoding_DefaultBinary,
                NodeIds.ThreeDFrame_Encoding_DefaultXml,
                NodeIds.ThreeDFrame_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.ThreeDFrame_Encoding_DefaultBinary,
                    NodeIds.Frame,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "CartesianCoordinates",
                          LocalizedText.NULL_VALUE,
                          NodeIds.ThreeDCartesianCoordinates,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "Orientation",
                          LocalizedText.NULL_VALUE,
                          NodeIds.ThreeDOrientation,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> lldpManagementAddressTxPortType =
        structure.addChild(
            new ClientDataType(
                QualifiedName.parse("0:LldpManagementAddressTxPortType"),
                NodeIds.LldpManagementAddressTxPortType,
                NodeIds.LldpManagementAddressTxPortType_Encoding_DefaultBinary,
                NodeIds.LldpManagementAddressTxPortType_Encoding_DefaultXml,
                NodeIds.LldpManagementAddressTxPortType_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.LldpManagementAddressTxPortType_Encoding_DefaultBinary,
                    NodeIds.Structure,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "AddressSubtype",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UInt32,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "ManAddress",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "TxEnable",
                          LocalizedText.NULL_VALUE,
                          NodeIds.Boolean,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "AddrLen",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UInt32,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "IfSubtype",
                          LocalizedText.NULL_VALUE,
                          NodeIds.ManAddrIfSubtype,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "IfId",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UInt32,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> lldpManagementAddressType =
        structure.addChild(
            new ClientDataType(
                QualifiedName.parse("0:LldpManagementAddressType"),
                NodeIds.LldpManagementAddressType,
                NodeIds.LldpManagementAddressType_Encoding_DefaultBinary,
                NodeIds.LldpManagementAddressType_Encoding_DefaultXml,
                NodeIds.LldpManagementAddressType_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.LldpManagementAddressType_Encoding_DefaultBinary,
                    NodeIds.Structure,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "AddressSubtype",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UInt32,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "Address",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "IfSubtype",
                          LocalizedText.NULL_VALUE,
                          NodeIds.ManAddrIfSubtype,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "IfId",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UInt32,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> lldpTlvType =
        structure.addChild(
            new ClientDataType(
                QualifiedName.parse("0:LldpTlvType"),
                NodeIds.LldpTlvType,
                NodeIds.LldpTlvType_Encoding_DefaultBinary,
                NodeIds.LldpTlvType_Encoding_DefaultXml,
                NodeIds.LldpTlvType_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.LldpTlvType_Encoding_DefaultBinary,
                    NodeIds.Structure,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "TlvType",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UInt32,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "TlvInfo",
                          LocalizedText.NULL_VALUE,
                          NodeIds.ByteString,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> aliasNameDataType =
        structure.addChild(
            new ClientDataType(
                QualifiedName.parse("0:AliasNameDataType"),
                NodeIds.AliasNameDataType,
                NodeIds.AliasNameDataType_Encoding_DefaultBinary,
                NodeIds.AliasNameDataType_Encoding_DefaultXml,
                NodeIds.AliasNameDataType_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.AliasNameDataType_Encoding_DefaultBinary,
                    NodeIds.Structure,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "AliasName",
                          LocalizedText.NULL_VALUE,
                          NodeIds.QualifiedName,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "ReferencedNodes",
                          LocalizedText.NULL_VALUE,
                          NodeIds.ExpandedNodeId,
                          1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> currencyUnitType =
        structure.addChild(
            new ClientDataType(
                QualifiedName.parse("0:CurrencyUnitType"),
                NodeIds.CurrencyUnitType,
                NodeIds.CurrencyUnitType_Encoding_DefaultBinary,
                NodeIds.CurrencyUnitType_Encoding_DefaultXml,
                NodeIds.CurrencyUnitType_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.CurrencyUnitType_Encoding_DefaultBinary,
                    NodeIds.Structure,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "NumericCode",
                          LocalizedText.NULL_VALUE,
                          NodeIds.Int16,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "Exponent",
                          LocalizedText.NULL_VALUE,
                          NodeIds.SByte,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "AlphabeticCode",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "Currency",
                          LocalizedText.NULL_VALUE,
                          NodeIds.LocalizedText,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> securityGroupDataType =
        structure.addChild(
            new ClientDataType(
                QualifiedName.parse("0:SecurityGroupDataType"),
                NodeIds.SecurityGroupDataType,
                NodeIds.SecurityGroupDataType_Encoding_DefaultBinary,
                NodeIds.SecurityGroupDataType_Encoding_DefaultXml,
                NodeIds.SecurityGroupDataType_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.SecurityGroupDataType_Encoding_DefaultBinary,
                    NodeIds.Structure,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "Name",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "SecurityGroupFolder",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "KeyLifetime",
                          LocalizedText.NULL_VALUE,
                          NodeIds.Duration,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "SecurityPolicyUri",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "MaxFutureKeyCount",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UInt32,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "MaxPastKeyCount",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UInt32,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "SecurityGroupId",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "RolePermissions",
                          LocalizedText.NULL_VALUE,
                          NodeIds.RolePermissionType,
                          1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "GroupProperties",
                          LocalizedText.NULL_VALUE,
                          NodeIds.KeyValuePair,
                          1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> qosDataType =
        structure.addChild(
            new ClientDataType(
                QualifiedName.parse("0:QosDataType"),
                NodeIds.QosDataType,
                NodeIds.QosDataType_Encoding_DefaultBinary,
                NodeIds.QosDataType_Encoding_DefaultXml,
                NodeIds.QosDataType_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.QosDataType_Encoding_DefaultBinary,
                    NodeIds.Structure,
                    StructureType.Structure,
                    new StructureField[0]),
                true));
    Tree<DataType> transmitQosDataType =
        qosDataType.addChild(
            new ClientDataType(
                QualifiedName.parse("0:TransmitQosDataType"),
                NodeIds.TransmitQosDataType,
                NodeIds.TransmitQosDataType_Encoding_DefaultBinary,
                NodeIds.TransmitQosDataType_Encoding_DefaultXml,
                NodeIds.TransmitQosDataType_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.TransmitQosDataType_Encoding_DefaultBinary,
                    NodeIds.QosDataType,
                    StructureType.Structure,
                    new StructureField[0]),
                true));
    Tree<DataType> transmitQosPriorityDataType =
        transmitQosDataType.addChild(
            new ClientDataType(
                QualifiedName.parse("0:TransmitQosPriorityDataType"),
                NodeIds.TransmitQosPriorityDataType,
                NodeIds.TransmitQosPriorityDataType_Encoding_DefaultBinary,
                NodeIds.TransmitQosPriorityDataType_Encoding_DefaultXml,
                NodeIds.TransmitQosPriorityDataType_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.TransmitQosPriorityDataType_Encoding_DefaultBinary,
                    NodeIds.TransmitQosDataType,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "PriorityLabel",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> receiveQosDataType =
        qosDataType.addChild(
            new ClientDataType(
                QualifiedName.parse("0:ReceiveQosDataType"),
                NodeIds.ReceiveQosDataType,
                NodeIds.ReceiveQosDataType_Encoding_DefaultBinary,
                NodeIds.ReceiveQosDataType_Encoding_DefaultXml,
                NodeIds.ReceiveQosDataType_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.ReceiveQosDataType_Encoding_DefaultBinary,
                    NodeIds.QosDataType,
                    StructureType.Structure,
                    new StructureField[0]),
                true));
    Tree<DataType> receiveQosPriorityDataType =
        receiveQosDataType.addChild(
            new ClientDataType(
                QualifiedName.parse("0:ReceiveQosPriorityDataType"),
                NodeIds.ReceiveQosPriorityDataType,
                NodeIds.ReceiveQosPriorityDataType_Encoding_DefaultBinary,
                NodeIds.ReceiveQosPriorityDataType_Encoding_DefaultXml,
                NodeIds.ReceiveQosPriorityDataType_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.ReceiveQosPriorityDataType_Encoding_DefaultBinary,
                    NodeIds.ReceiveQosDataType,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "PriorityLabel",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> programDiagnostic2DataType =
        structure.addChild(
            new ClientDataType(
                QualifiedName.parse("0:ProgramDiagnostic2DataType"),
                NodeIds.ProgramDiagnostic2DataType,
                NodeIds.ProgramDiagnostic2DataType_Encoding_DefaultBinary,
                NodeIds.ProgramDiagnostic2DataType_Encoding_DefaultXml,
                NodeIds.ProgramDiagnostic2DataType_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.ProgramDiagnostic2DataType_Encoding_DefaultBinary,
                    NodeIds.Structure,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "CreateSessionId",
                          LocalizedText.NULL_VALUE,
                          NodeIds.NodeId,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "CreateClientName",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "InvocationCreationTime",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UtcTime,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "LastTransitionTime",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UtcTime,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "LastMethodCall",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "LastMethodSessionId",
                          LocalizedText.NULL_VALUE,
                          NodeIds.NodeId,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "LastMethodInputArguments",
                          LocalizedText.NULL_VALUE,
                          NodeIds.Argument,
                          1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "LastMethodOutputArguments",
                          LocalizedText.NULL_VALUE,
                          NodeIds.Argument,
                          1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "LastMethodInputValues",
                          LocalizedText.NULL_VALUE,
                          NodeIds.BaseDataType,
                          1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "LastMethodOutputValues",
                          LocalizedText.NULL_VALUE,
                          NodeIds.BaseDataType,
                          1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "LastMethodCallTime",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UtcTime,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "LastMethodReturnStatus",
                          LocalizedText.NULL_VALUE,
                          NodeIds.StatusCode,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> portableQualifiedName =
        structure.addChild(
            new ClientDataType(
                QualifiedName.parse("0:PortableQualifiedName"),
                NodeIds.PortableQualifiedName,
                NodeIds.PortableQualifiedName_Encoding_DefaultBinary,
                NodeIds.PortableQualifiedName_Encoding_DefaultXml,
                NodeIds.PortableQualifiedName_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.PortableQualifiedName_Encoding_DefaultBinary,
                    NodeIds.Structure,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "NamespaceUri",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "Name",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> portableNodeId =
        structure.addChild(
            new ClientDataType(
                QualifiedName.parse("0:PortableNodeId"),
                NodeIds.PortableNodeId,
                NodeIds.PortableNodeId_Encoding_DefaultBinary,
                NodeIds.PortableNodeId_Encoding_DefaultXml,
                NodeIds.PortableNodeId_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.PortableNodeId_Encoding_DefaultBinary,
                    NodeIds.Structure,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "NamespaceUri",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "Identifier",
                          LocalizedText.NULL_VALUE,
                          NodeIds.NodeId,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> unsignedRationalNumber =
        structure.addChild(
            new ClientDataType(
                QualifiedName.parse("0:UnsignedRationalNumber"),
                NodeIds.UnsignedRationalNumber,
                NodeIds.UnsignedRationalNumber_Encoding_DefaultBinary,
                NodeIds.UnsignedRationalNumber_Encoding_DefaultXml,
                NodeIds.UnsignedRationalNumber_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.UnsignedRationalNumber_Encoding_DefaultBinary,
                    NodeIds.Structure,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "Numerator",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UInt32,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "Denominator",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UInt32,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> userManagementDataType =
        structure.addChild(
            new ClientDataType(
                QualifiedName.parse("0:UserManagementDataType"),
                NodeIds.UserManagementDataType,
                NodeIds.UserManagementDataType_Encoding_DefaultBinary,
                NodeIds.UserManagementDataType_Encoding_DefaultXml,
                NodeIds.UserManagementDataType_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.UserManagementDataType_Encoding_DefaultBinary,
                    NodeIds.Structure,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "UserName",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "UserConfiguration",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UserConfigurationMask,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "Description",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> priorityMappingEntryType =
        structure.addChild(
            new ClientDataType(
                QualifiedName.parse("0:PriorityMappingEntryType"),
                NodeIds.PriorityMappingEntryType,
                NodeIds.PriorityMappingEntryType_Encoding_DefaultBinary,
                NodeIds.PriorityMappingEntryType_Encoding_DefaultXml,
                NodeIds.PriorityMappingEntryType_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.PriorityMappingEntryType_Encoding_DefaultBinary,
                    NodeIds.Structure,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "MappingUri",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "PriorityLabel",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "PriorityValue_PCP",
                          LocalizedText.NULL_VALUE,
                          NodeIds.Byte,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "PriorityValue_DSCP",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UInt32,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> pubSubKeyPushTargetDataType =
        structure.addChild(
            new ClientDataType(
                QualifiedName.parse("0:PubSubKeyPushTargetDataType"),
                NodeIds.PubSubKeyPushTargetDataType,
                NodeIds.PubSubKeyPushTargetDataType_Encoding_DefaultBinary,
                NodeIds.PubSubKeyPushTargetDataType_Encoding_DefaultXml,
                NodeIds.PubSubKeyPushTargetDataType_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.PubSubKeyPushTargetDataType_Encoding_DefaultBinary,
                    NodeIds.Structure,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "ApplicationUri",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "PushTargetFolder",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "EndpointUrl",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "SecurityPolicyUri",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "UserTokenType",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UserTokenPolicy,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "RequestedKeyCount",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UInt16,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "RetryInterval",
                          LocalizedText.NULL_VALUE,
                          NodeIds.Duration,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "PushTargetProperties",
                          LocalizedText.NULL_VALUE,
                          NodeIds.KeyValuePair,
                          1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "SecurityGroups",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> pubSubConfigurationRefDataType =
        structure.addChild(
            new ClientDataType(
                QualifiedName.parse("0:PubSubConfigurationRefDataType"),
                NodeIds.PubSubConfigurationRefDataType,
                NodeIds.PubSubConfigurationRefDataType_Encoding_DefaultBinary,
                NodeIds.PubSubConfigurationRefDataType_Encoding_DefaultXml,
                NodeIds.PubSubConfigurationRefDataType_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.PubSubConfigurationRefDataType_Encoding_DefaultBinary,
                    NodeIds.Structure,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "ConfigurationMask",
                          LocalizedText.NULL_VALUE,
                          NodeIds.PubSubConfigurationRefMask,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "ElementIndex",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UInt16,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "ConnectionIndex",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UInt16,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "GroupIndex",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UInt16,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> pubSubConfigurationValueDataType =
        structure.addChild(
            new ClientDataType(
                QualifiedName.parse("0:PubSubConfigurationValueDataType"),
                NodeIds.PubSubConfigurationValueDataType,
                NodeIds.PubSubConfigurationValueDataType_Encoding_DefaultBinary,
                NodeIds.PubSubConfigurationValueDataType_Encoding_DefaultXml,
                NodeIds.PubSubConfigurationValueDataType_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.PubSubConfigurationValueDataType_Encoding_DefaultBinary,
                    NodeIds.Structure,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "ConfigurationElement",
                          LocalizedText.NULL_VALUE,
                          NodeIds.PubSubConfigurationRefDataType,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "Name",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "Identifier",
                          LocalizedText.NULL_VALUE,
                          NodeIds.BaseDataType,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> transactionErrorType =
        structure.addChild(
            new ClientDataType(
                QualifiedName.parse("0:TransactionErrorType"),
                NodeIds.TransactionErrorType,
                NodeIds.TransactionErrorType_Encoding_DefaultBinary,
                NodeIds.TransactionErrorType_Encoding_DefaultXml,
                NodeIds.TransactionErrorType_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.TransactionErrorType_Encoding_DefaultBinary,
                    NodeIds.Structure,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "TargetId",
                          LocalizedText.NULL_VALUE,
                          NodeIds.NodeId,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "Error",
                          LocalizedText.NULL_VALUE,
                          NodeIds.StatusCode,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "Message",
                          LocalizedText.NULL_VALUE,
                          NodeIds.LocalizedText,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> bitFieldDefinition =
        structure.addChild(
            new ClientDataType(
                QualifiedName.parse("0:BitFieldDefinition"),
                NodeIds.BitFieldDefinition,
                NodeIds.BitFieldDefinition_Encoding_DefaultBinary,
                NodeIds.BitFieldDefinition_Encoding_DefaultXml,
                NodeIds.BitFieldDefinition_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.BitFieldDefinition_Encoding_DefaultBinary,
                    NodeIds.Structure,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "Name",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "Description",
                          LocalizedText.NULL_VALUE,
                          NodeIds.LocalizedText,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "Reserved",
                          LocalizedText.NULL_VALUE,
                          NodeIds.Boolean,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "StartingBitPosition",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UInt32,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "EndingBitPosition",
                          LocalizedText.NULL_VALUE,
                          NodeIds.UInt32,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> annotationDataType =
        structure.addChild(
            new ClientDataType(
                QualifiedName.parse("0:AnnotationDataType"),
                NodeIds.AnnotationDataType,
                NodeIds.AnnotationDataType_Encoding_DefaultBinary,
                NodeIds.AnnotationDataType_Encoding_DefaultXml,
                NodeIds.AnnotationDataType_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.AnnotationDataType_Encoding_DefaultBinary,
                    NodeIds.Structure,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "Annotation",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "Discipline",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "Uri",
                          LocalizedText.NULL_VALUE,
                          NodeIds.String,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> linearConversionDataType =
        structure.addChild(
            new ClientDataType(
                QualifiedName.parse("0:LinearConversionDataType"),
                NodeIds.LinearConversionDataType,
                NodeIds.LinearConversionDataType_Encoding_DefaultBinary,
                NodeIds.LinearConversionDataType_Encoding_DefaultXml,
                NodeIds.LinearConversionDataType_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.LinearConversionDataType_Encoding_DefaultBinary,
                    NodeIds.Structure,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "InitialAddend",
                          LocalizedText.NULL_VALUE,
                          NodeIds.Float,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "Multiplicand",
                          LocalizedText.NULL_VALUE,
                          NodeIds.Float,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "Divisor",
                          LocalizedText.NULL_VALUE,
                          NodeIds.Float,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "FinalAddend",
                          LocalizedText.NULL_VALUE,
                          NodeIds.Float,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> quantityDimension =
        structure.addChild(
            new ClientDataType(
                QualifiedName.parse("0:QuantityDimension"),
                NodeIds.QuantityDimension,
                NodeIds.QuantityDimension_Encoding_DefaultBinary,
                NodeIds.QuantityDimension_Encoding_DefaultXml,
                NodeIds.QuantityDimension_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.QuantityDimension_Encoding_DefaultBinary,
                    NodeIds.Structure,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "MassExponent",
                          LocalizedText.NULL_VALUE,
                          NodeIds.SByte,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "LengthExponent",
                          LocalizedText.NULL_VALUE,
                          NodeIds.SByte,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "TimeExponent",
                          LocalizedText.NULL_VALUE,
                          NodeIds.SByte,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "ElectricCurrentExponent",
                          LocalizedText.NULL_VALUE,
                          NodeIds.SByte,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "AmountOfSubstanceExponent",
                          LocalizedText.NULL_VALUE,
                          NodeIds.SByte,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "LuminousIntensityExponent",
                          LocalizedText.NULL_VALUE,
                          NodeIds.SByte,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "AbsoluteTemperatureExponent",
                          LocalizedText.NULL_VALUE,
                          NodeIds.SByte,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "DimensionlessExponent",
                          LocalizedText.NULL_VALUE,
                          NodeIds.SByte,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> referenceDescriptionDataType =
        structure.addChild(
            new ClientDataType(
                QualifiedName.parse("0:ReferenceDescriptionDataType"),
                NodeIds.ReferenceDescriptionDataType,
                NodeIds.ReferenceDescriptionDataType_Encoding_DefaultBinary,
                NodeIds.ReferenceDescriptionDataType_Encoding_DefaultXml,
                NodeIds.ReferenceDescriptionDataType_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.ReferenceDescriptionDataType_Encoding_DefaultBinary,
                    NodeIds.Structure,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "SourceNode",
                          LocalizedText.NULL_VALUE,
                          NodeIds.NodeId,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "ReferenceType",
                          LocalizedText.NULL_VALUE,
                          NodeIds.NodeId,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "IsForward",
                          LocalizedText.NULL_VALUE,
                          NodeIds.Boolean,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "TargetNode",
                          LocalizedText.NULL_VALUE,
                          NodeIds.ExpandedNodeId,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
    Tree<DataType> referenceListEntryDataType =
        structure.addChild(
            new ClientDataType(
                QualifiedName.parse("0:ReferenceListEntryDataType"),
                NodeIds.ReferenceListEntryDataType,
                NodeIds.ReferenceListEntryDataType_Encoding_DefaultBinary,
                NodeIds.ReferenceListEntryDataType_Encoding_DefaultXml,
                NodeIds.ReferenceListEntryDataType_Encoding_DefaultJson,
                new StructureDefinition(
                    NodeIds.ReferenceListEntryDataType_Encoding_DefaultBinary,
                    NodeIds.Structure,
                    StructureType.Structure,
                    new StructureField[] {
                      new StructureField(
                          "ReferenceType",
                          LocalizedText.NULL_VALUE,
                          NodeIds.NodeId,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "IsForward",
                          LocalizedText.NULL_VALUE,
                          NodeIds.Boolean,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false),
                      new StructureField(
                          "TargetNode",
                          LocalizedText.NULL_VALUE,
                          NodeIds.ExpandedNodeId,
                          -1,
                          null,
                          UInteger.valueOf(0),
                          false)
                    }),
                false));
  }

  @SuppressWarnings("unused")
  private static void createEnumerationNodes(Tree<DataType> enumeration) {
    Tree<DataType> structureType =
        enumeration.addChild(
            new ClientDataType(
                QualifiedName.parse("0:StructureType"),
                NodeIds.StructureType,
                null,
                null,
                null,
                new EnumDefinition(
                    new EnumField[] {
                      new EnumField(
                          0L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "Structure"),
                      new EnumField(
                          1L,
                          LocalizedText.NULL_VALUE,
                          LocalizedText.NULL_VALUE,
                          "StructureWithOptionalFields"),
                      new EnumField(
                          2L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "Union"),
                      new EnumField(
                          3L,
                          LocalizedText.NULL_VALUE,
                          LocalizedText.NULL_VALUE,
                          "StructureWithSubtypedValues"),
                      new EnumField(
                          4L,
                          LocalizedText.NULL_VALUE,
                          LocalizedText.NULL_VALUE,
                          "UnionWithSubtypedValues")
                    }),
                false));
    Tree<DataType> namingRuleType =
        enumeration.addChild(
            new ClientDataType(
                QualifiedName.parse("0:NamingRuleType"),
                NodeIds.NamingRuleType,
                null,
                null,
                null,
                new EnumDefinition(
                    new EnumField[] {
                      new EnumField(
                          1L,
                          LocalizedText.NULL_VALUE,
                          new LocalizedText(
                              "", "The BrowseName must appear in all instances of the type."),
                          "Mandatory"),
                      new EnumField(
                          2L,
                          LocalizedText.NULL_VALUE,
                          new LocalizedText(
                              "", "The BrowseName may appear in an instance of the type."),
                          "Optional"),
                      new EnumField(
                          3L,
                          LocalizedText.NULL_VALUE,
                          new LocalizedText(
                              "",
                              "The modelling rule defines a constraint and the BrowseName is not"
                                  + " used in an instance of the type."),
                          "Constraint")
                    }),
                false));
    Tree<DataType> idType =
        enumeration.addChild(
            new ClientDataType(
                QualifiedName.parse("0:IdType"),
                NodeIds.IdType,
                null,
                null,
                null,
                new EnumDefinition(
                    new EnumField[] {
                      new EnumField(
                          0L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "Numeric"),
                      new EnumField(
                          1L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "String"),
                      new EnumField(2L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "Guid"),
                      new EnumField(
                          3L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "Opaque")
                    }),
                false));
    Tree<DataType> nodeClass =
        enumeration.addChild(
            new ClientDataType(
                QualifiedName.parse("0:NodeClass"),
                NodeIds.NodeClass,
                null,
                null,
                null,
                new EnumDefinition(
                    new EnumField[] {
                      new EnumField(
                          0L,
                          LocalizedText.NULL_VALUE,
                          new LocalizedText("", "No value is specified."),
                          "Unspecified"),
                      new EnumField(
                          1L,
                          LocalizedText.NULL_VALUE,
                          new LocalizedText("", "The Node is an Object."),
                          "Object"),
                      new EnumField(
                          2L,
                          LocalizedText.NULL_VALUE,
                          new LocalizedText("", "The Node is a Variable."),
                          "Variable"),
                      new EnumField(
                          4L,
                          LocalizedText.NULL_VALUE,
                          new LocalizedText("", "The Node is a Method."),
                          "Method"),
                      new EnumField(
                          8L,
                          LocalizedText.NULL_VALUE,
                          new LocalizedText("", "The Node is an ObjectType."),
                          "ObjectType"),
                      new EnumField(
                          16L,
                          LocalizedText.NULL_VALUE,
                          new LocalizedText("", "The Node is a VariableType."),
                          "VariableType"),
                      new EnumField(
                          32L,
                          LocalizedText.NULL_VALUE,
                          new LocalizedText("", "The Node is a ReferenceType."),
                          "ReferenceType"),
                      new EnumField(
                          64L,
                          LocalizedText.NULL_VALUE,
                          new LocalizedText("", "The Node is a DataType."),
                          "DataType"),
                      new EnumField(
                          128L,
                          LocalizedText.NULL_VALUE,
                          new LocalizedText("", "The Node is a View."),
                          "View")
                    }),
                false));
    Tree<DataType> messageSecurityMode =
        enumeration.addChild(
            new ClientDataType(
                QualifiedName.parse("0:MessageSecurityMode"),
                NodeIds.MessageSecurityMode,
                null,
                null,
                null,
                new EnumDefinition(
                    new EnumField[] {
                      new EnumField(
                          0L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "Invalid"),
                      new EnumField(1L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "None"),
                      new EnumField(2L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "Sign"),
                      new EnumField(
                          3L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "SignAndEncrypt")
                    }),
                false));
    Tree<DataType> userTokenType =
        enumeration.addChild(
            new ClientDataType(
                QualifiedName.parse("0:UserTokenType"),
                NodeIds.UserTokenType,
                null,
                null,
                null,
                new EnumDefinition(
                    new EnumField[] {
                      new EnumField(
                          0L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "Anonymous"),
                      new EnumField(
                          1L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "UserName"),
                      new EnumField(
                          2L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "Certificate"),
                      new EnumField(
                          3L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "IssuedToken")
                    }),
                false));
    Tree<DataType> applicationType =
        enumeration.addChild(
            new ClientDataType(
                QualifiedName.parse("0:ApplicationType"),
                NodeIds.ApplicationType,
                null,
                null,
                null,
                new EnumDefinition(
                    new EnumField[] {
                      new EnumField(
                          0L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "Server"),
                      new EnumField(
                          1L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "Client"),
                      new EnumField(
                          2L,
                          LocalizedText.NULL_VALUE,
                          LocalizedText.NULL_VALUE,
                          "ClientAndServer"),
                      new EnumField(
                          3L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "DiscoveryServer")
                    }),
                false));
    Tree<DataType> securityTokenRequestType =
        enumeration.addChild(
            new ClientDataType(
                QualifiedName.parse("0:SecurityTokenRequestType"),
                NodeIds.SecurityTokenRequestType,
                null,
                null,
                null,
                new EnumDefinition(
                    new EnumField[] {
                      new EnumField(
                          0L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "Issue"),
                      new EnumField(1L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "Renew")
                    }),
                false));
    Tree<DataType> nodeAttributesMask =
        enumeration.addChild(
            new ClientDataType(
                QualifiedName.parse("0:NodeAttributesMask"),
                NodeIds.NodeAttributesMask,
                null,
                null,
                null,
                new EnumDefinition(
                    new EnumField[] {
                      new EnumField(0L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "None"),
                      new EnumField(
                          1L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "AccessLevel"),
                      new EnumField(
                          2L,
                          LocalizedText.NULL_VALUE,
                          LocalizedText.NULL_VALUE,
                          "ArrayDimensions"),
                      new EnumField(
                          4L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "BrowseName"),
                      new EnumField(
                          8L,
                          LocalizedText.NULL_VALUE,
                          LocalizedText.NULL_VALUE,
                          "ContainsNoLoops"),
                      new EnumField(
                          16L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "DataType"),
                      new EnumField(
                          32L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "Description"),
                      new EnumField(
                          64L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "DisplayName"),
                      new EnumField(
                          128L,
                          LocalizedText.NULL_VALUE,
                          LocalizedText.NULL_VALUE,
                          "EventNotifier"),
                      new EnumField(
                          256L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "Executable"),
                      new EnumField(
                          512L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "Historizing"),
                      new EnumField(
                          1024L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "InverseName"),
                      new EnumField(
                          2048L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "IsAbstract"),
                      new EnumField(
                          4096L,
                          LocalizedText.NULL_VALUE,
                          LocalizedText.NULL_VALUE,
                          "MinimumSamplingInterval"),
                      new EnumField(
                          8192L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "NodeClass"),
                      new EnumField(
                          16384L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "NodeId"),
                      new EnumField(
                          32768L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "Symmetric"),
                      new EnumField(
                          65536L,
                          LocalizedText.NULL_VALUE,
                          LocalizedText.NULL_VALUE,
                          "UserAccessLevel"),
                      new EnumField(
                          131072L,
                          LocalizedText.NULL_VALUE,
                          LocalizedText.NULL_VALUE,
                          "UserExecutable"),
                      new EnumField(
                          262144L,
                          LocalizedText.NULL_VALUE,
                          LocalizedText.NULL_VALUE,
                          "UserWriteMask"),
                      new EnumField(
                          524288L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "ValueRank"),
                      new EnumField(
                          1048576L,
                          LocalizedText.NULL_VALUE,
                          LocalizedText.NULL_VALUE,
                          "WriteMask"),
                      new EnumField(
                          2097152L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "Value"),
                      new EnumField(
                          4194304L,
                          LocalizedText.NULL_VALUE,
                          LocalizedText.NULL_VALUE,
                          "DataTypeDefinition"),
                      new EnumField(
                          8388608L,
                          LocalizedText.NULL_VALUE,
                          LocalizedText.NULL_VALUE,
                          "RolePermissions"),
                      new EnumField(
                          16777216L,
                          LocalizedText.NULL_VALUE,
                          LocalizedText.NULL_VALUE,
                          "AccessRestrictions"),
                      new EnumField(
                          33554431L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "All"),
                      new EnumField(
                          26501220L,
                          LocalizedText.NULL_VALUE,
                          LocalizedText.NULL_VALUE,
                          "BaseNode"),
                      new EnumField(
                          26501348L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "Object"),
                      new EnumField(
                          26503268L,
                          LocalizedText.NULL_VALUE,
                          LocalizedText.NULL_VALUE,
                          "ObjectType"),
                      new EnumField(
                          26571383L,
                          LocalizedText.NULL_VALUE,
                          LocalizedText.NULL_VALUE,
                          "Variable"),
                      new EnumField(
                          28600438L,
                          LocalizedText.NULL_VALUE,
                          LocalizedText.NULL_VALUE,
                          "VariableType"),
                      new EnumField(
                          26632548L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "Method"),
                      new EnumField(
                          26537060L,
                          LocalizedText.NULL_VALUE,
                          LocalizedText.NULL_VALUE,
                          "ReferenceType"),
                      new EnumField(
                          26501356L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "View")
                    }),
                false));
    Tree<DataType> filterOperator =
        enumeration.addChild(
            new ClientDataType(
                QualifiedName.parse("0:FilterOperator"),
                NodeIds.FilterOperator,
                null,
                null,
                null,
                new EnumDefinition(
                    new EnumField[] {
                      new EnumField(
                          0L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "Equals"),
                      new EnumField(
                          1L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "IsNull"),
                      new EnumField(
                          2L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "GreaterThan"),
                      new EnumField(
                          3L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "LessThan"),
                      new EnumField(
                          4L,
                          LocalizedText.NULL_VALUE,
                          LocalizedText.NULL_VALUE,
                          "GreaterThanOrEqual"),
                      new EnumField(
                          5L,
                          LocalizedText.NULL_VALUE,
                          LocalizedText.NULL_VALUE,
                          "LessThanOrEqual"),
                      new EnumField(6L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "Like"),
                      new EnumField(7L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "Not"),
                      new EnumField(
                          8L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "Between"),
                      new EnumField(
                          9L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "InList"),
                      new EnumField(10L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "And"),
                      new EnumField(11L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "Or"),
                      new EnumField(
                          12L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "Cast"),
                      new EnumField(
                          13L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "InView"),
                      new EnumField(
                          14L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "OfType"),
                      new EnumField(
                          15L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "RelatedTo"),
                      new EnumField(
                          16L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "BitwiseAnd"),
                      new EnumField(
                          17L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "BitwiseOr")
                    }),
                false));
    Tree<DataType> redundancySupport =
        enumeration.addChild(
            new ClientDataType(
                QualifiedName.parse("0:RedundancySupport"),
                NodeIds.RedundancySupport,
                null,
                null,
                null,
                new EnumDefinition(
                    new EnumField[] {
                      new EnumField(0L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "None"),
                      new EnumField(1L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "Cold"),
                      new EnumField(2L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "Warm"),
                      new EnumField(3L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "Hot"),
                      new EnumField(
                          4L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "Transparent"),
                      new EnumField(
                          5L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "HotAndMirrored")
                    }),
                false));
    Tree<DataType> serverState =
        enumeration.addChild(
            new ClientDataType(
                QualifiedName.parse("0:ServerState"),
                NodeIds.ServerState,
                null,
                null,
                null,
                new EnumDefinition(
                    new EnumField[] {
                      new EnumField(
                          0L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "Running"),
                      new EnumField(
                          1L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "Failed"),
                      new EnumField(
                          2L,
                          LocalizedText.NULL_VALUE,
                          LocalizedText.NULL_VALUE,
                          "NoConfiguration"),
                      new EnumField(
                          3L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "Suspended"),
                      new EnumField(
                          4L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "Shutdown"),
                      new EnumField(5L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "Test"),
                      new EnumField(
                          6L,
                          LocalizedText.NULL_VALUE,
                          LocalizedText.NULL_VALUE,
                          "CommunicationFault"),
                      new EnumField(
                          7L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "Unknown")
                    }),
                false));
    Tree<DataType> exceptionDeviationFormat =
        enumeration.addChild(
            new ClientDataType(
                QualifiedName.parse("0:ExceptionDeviationFormat"),
                NodeIds.ExceptionDeviationFormat,
                null,
                null,
                null,
                new EnumDefinition(
                    new EnumField[] {
                      new EnumField(
                          0L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "AbsoluteValue"),
                      new EnumField(
                          1L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "PercentOfValue"),
                      new EnumField(
                          2L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "PercentOfRange"),
                      new EnumField(
                          3L,
                          LocalizedText.NULL_VALUE,
                          LocalizedText.NULL_VALUE,
                          "PercentOfEURange"),
                      new EnumField(
                          4L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "Unknown")
                    }),
                false));
    Tree<DataType> historyUpdateType =
        enumeration.addChild(
            new ClientDataType(
                QualifiedName.parse("0:HistoryUpdateType"),
                NodeIds.HistoryUpdateType,
                null,
                null,
                null,
                new EnumDefinition(
                    new EnumField[] {
                      new EnumField(
                          1L,
                          LocalizedText.NULL_VALUE,
                          new LocalizedText("", "Data was inserted."),
                          "Insert"),
                      new EnumField(
                          2L,
                          LocalizedText.NULL_VALUE,
                          new LocalizedText("", "Data was replaced."),
                          "Replace"),
                      new EnumField(
                          3L,
                          LocalizedText.NULL_VALUE,
                          new LocalizedText("", "Data was inserted or replaced."),
                          "Update"),
                      new EnumField(
                          4L,
                          LocalizedText.NULL_VALUE,
                          new LocalizedText("", "Data was deleted."),
                          "Delete")
                    }),
                false));
    Tree<DataType> performUpdateType =
        enumeration.addChild(
            new ClientDataType(
                QualifiedName.parse("0:PerformUpdateType"),
                NodeIds.PerformUpdateType,
                null,
                null,
                null,
                new EnumDefinition(
                    new EnumField[] {
                      new EnumField(
                          1L,
                          LocalizedText.NULL_VALUE,
                          new LocalizedText("", "Data was inserted."),
                          "Insert"),
                      new EnumField(
                          2L,
                          LocalizedText.NULL_VALUE,
                          new LocalizedText("", "Data was replaced."),
                          "Replace"),
                      new EnumField(
                          3L,
                          LocalizedText.NULL_VALUE,
                          new LocalizedText("", "Data was inserted or replaced."),
                          "Update"),
                      new EnumField(
                          4L,
                          LocalizedText.NULL_VALUE,
                          new LocalizedText("", "Data was deleted."),
                          "Remove")
                    }),
                false));
    Tree<DataType> openFileMode =
        enumeration.addChild(
            new ClientDataType(
                QualifiedName.parse("0:OpenFileMode"),
                NodeIds.OpenFileMode,
                null,
                null,
                null,
                new EnumDefinition(
                    new EnumField[] {
                      new EnumField(1L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "Read"),
                      new EnumField(
                          2L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "Write"),
                      new EnumField(
                          4L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "EraseExisting"),
                      new EnumField(
                          8L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "Append")
                    }),
                false));
    Tree<DataType> axisScaleEnumeration =
        enumeration.addChild(
            new ClientDataType(
                QualifiedName.parse("0:AxisScaleEnumeration"),
                NodeIds.AxisScaleEnumeration,
                null,
                null,
                null,
                new EnumDefinition(
                    new EnumField[] {
                      new EnumField(
                          0L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "Linear"),
                      new EnumField(1L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "Log"),
                      new EnumField(2L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "Ln")
                    }),
                false));
    Tree<DataType> trustListMasks =
        enumeration.addChild(
            new ClientDataType(
                QualifiedName.parse("0:TrustListMasks"),
                NodeIds.TrustListMasks,
                null,
                null,
                null,
                new EnumDefinition(
                    new EnumField[] {
                      new EnumField(
                          0L,
                          LocalizedText.NULL_VALUE,
                          new LocalizedText("", "No fields are provided."),
                          "None"),
                      new EnumField(
                          1L,
                          LocalizedText.NULL_VALUE,
                          new LocalizedText("", "The TrustedCertificates are provided."),
                          "TrustedCertificates"),
                      new EnumField(
                          2L,
                          LocalizedText.NULL_VALUE,
                          new LocalizedText("", "The TrustedCrls are provided."),
                          "TrustedCrls"),
                      new EnumField(
                          4L,
                          LocalizedText.NULL_VALUE,
                          new LocalizedText("", "The IssuerCertificates are provided."),
                          "IssuerCertificates"),
                      new EnumField(
                          8L,
                          LocalizedText.NULL_VALUE,
                          new LocalizedText("", "The IssuerCrls are provided."),
                          "IssuerCrls"),
                      new EnumField(
                          15L,
                          LocalizedText.NULL_VALUE,
                          new LocalizedText("", "All fields are provided."),
                          "All")
                    }),
                false));
    Tree<DataType> pubSubState =
        enumeration.addChild(
            new ClientDataType(
                QualifiedName.parse("0:PubSubState"),
                NodeIds.PubSubState,
                null,
                null,
                null,
                new EnumDefinition(
                    new EnumField[] {
                      new EnumField(
                          0L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "Disabled"),
                      new EnumField(
                          1L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "Paused"),
                      new EnumField(
                          2L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "Operational"),
                      new EnumField(
                          3L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "Error"),
                      new EnumField(
                          4L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "PreOperational")
                    }),
                false));
    Tree<DataType> brokerTransportQualityOfService =
        enumeration.addChild(
            new ClientDataType(
                QualifiedName.parse("0:BrokerTransportQualityOfService"),
                NodeIds.BrokerTransportQualityOfService,
                null,
                null,
                null,
                new EnumDefinition(
                    new EnumField[] {
                      new EnumField(
                          0L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "NotSpecified"),
                      new EnumField(
                          1L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "BestEffort"),
                      new EnumField(
                          2L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "AtLeastOnce"),
                      new EnumField(
                          3L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "AtMostOnce"),
                      new EnumField(
                          4L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "ExactlyOnce")
                    }),
                false));
    Tree<DataType> identityCriteriaType =
        enumeration.addChild(
            new ClientDataType(
                QualifiedName.parse("0:IdentityCriteriaType"),
                NodeIds.IdentityCriteriaType,
                null,
                null,
                null,
                new EnumDefinition(
                    new EnumField[] {
                      new EnumField(
                          1L,
                          LocalizedText.NULL_VALUE,
                          new LocalizedText(
                              "", "The rule specifies a UserName from a UserNameIdentityToken."),
                          "UserName"),
                      new EnumField(
                          2L,
                          LocalizedText.NULL_VALUE,
                          new LocalizedText(
                              "", "The rule specifies the Thumbprint of a user or CA Certificate."),
                          "Thumbprint"),
                      new EnumField(
                          3L,
                          LocalizedText.NULL_VALUE,
                          new LocalizedText("", "The rule is a Role specified in an Access Token."),
                          "Role"),
                      new EnumField(
                          4L,
                          LocalizedText.NULL_VALUE,
                          new LocalizedText(
                              "", "The rule is a user group specified in the Access Token."),
                          "GroupId"),
                      new EnumField(
                          5L,
                          LocalizedText.NULL_VALUE,
                          new LocalizedText("", "The rule specifies Anonymous UserIdentityToken."),
                          "Anonymous"),
                      new EnumField(
                          6L,
                          LocalizedText.NULL_VALUE,
                          new LocalizedText(
                              "", "The rule specifies any non Anonymous UserIdentityToken."),
                          "AuthenticatedUser"),
                      new EnumField(
                          7L,
                          LocalizedText.NULL_VALUE,
                          new LocalizedText(
                              "",
                              "The rule specifies the combination of an application identity and an"
                                  + " Anonymous UserIdentityToken."),
                          "Application"),
                      new EnumField(
                          8L,
                          LocalizedText.NULL_VALUE,
                          new LocalizedText(
                              "",
                              "The rule specifies the X509 subject name of a user or CA"
                                  + " Certificate."),
                          "X509Subject"),
                      new EnumField(
                          9L,
                          LocalizedText.NULL_VALUE,
                          new LocalizedText(
                              "",
                              "The rule specifies any trusted application that has been"
                                  + " authenticated with a trusted ApplicationInstance"
                                  + " Certificate."),
                          "TrustedApplication")
                    }),
                false));
    Tree<DataType> overrideValueHandling =
        enumeration.addChild(
            new ClientDataType(
                QualifiedName.parse("0:OverrideValueHandling"),
                NodeIds.OverrideValueHandling,
                null,
                null,
                null,
                new EnumDefinition(
                    new EnumField[] {
                      new EnumField(
                          0L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "Disabled"),
                      new EnumField(
                          1L,
                          LocalizedText.NULL_VALUE,
                          LocalizedText.NULL_VALUE,
                          "LastUsableValue"),
                      new EnumField(
                          2L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "OverrideValue")
                    }),
                false));
    Tree<DataType> actionState =
        enumeration.addChild(
            new ClientDataType(
                QualifiedName.parse("0:ActionState"),
                NodeIds.ActionState,
                null,
                null,
                null,
                new EnumDefinition(
                    new EnumField[] {
                      new EnumField(0L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "Idle"),
                      new EnumField(
                          1L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "Executing"),
                      new EnumField(2L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "Done")
                    }),
                false));
    Tree<DataType> chassisIdSubtype =
        enumeration.addChild(
            new ClientDataType(
                QualifiedName.parse("0:ChassisIdSubtype"),
                NodeIds.ChassisIdSubtype,
                null,
                null,
                null,
                new EnumDefinition(
                    new EnumField[] {
                      new EnumField(
                          1L,
                          LocalizedText.NULL_VALUE,
                          new LocalizedText(
                              "",
                              "Represents a chassis identifier based on the value of"
                                  + " entPhysicalAlias object (defined in IETF RFC 2737) for a"
                                  + " chassis component (i.e., an entPhysicalClass value of"
                                  + " chassis(3))"),
                          "ChassisComponent"),
                      new EnumField(
                          2L,
                          LocalizedText.NULL_VALUE,
                          new LocalizedText(
                              "",
                              "Represents a chassis identifier based on the value of ifAlias object"
                                  + " (defined in IETF RFC 2863) for an interface on the containing"
                                  + " chassis."),
                          "InterfaceAlias"),
                      new EnumField(
                          3L,
                          LocalizedText.NULL_VALUE,
                          new LocalizedText(
                              "",
                              "Represents a chassis identifier based on the value of"
                                  + " entPhysicalAlias object (defined in IETF RFC 2737) for a port"
                                  + " or backplane component (i.e., entPhysicalClass has a value of"
                                  + " port(10), or backplane(4)), within the containing chassis."),
                          "PortComponent"),
                      new EnumField(
                          4L,
                          LocalizedText.NULL_VALUE,
                          new LocalizedText(
                              "",
                              "Represents a chassis identifier based on the value of a unicast"
                                  + " source address (encoded in network byte order and IEEE 802.3"
                                  + " canonical bit order) of a port on the containing chassis as"
                                  + " defined in IEEE Std 802-2014."),
                          "MacAddress"),
                      new EnumField(
                          5L,
                          LocalizedText.NULL_VALUE,
                          new LocalizedText(
                              "",
                              "Represents a chassis identifier based on a network address"
                                  + " associated with a particular chassis. The encoded address is"
                                  + " actually composed of two fields. The first field is a single"
                                  + " octet, representing the IANA AddressFamilyNumbers value for"
                                  + " the specific address type, and the second field is the"
                                  + " network address value."),
                          "NetworkAddress"),
                      new EnumField(
                          6L,
                          LocalizedText.NULL_VALUE,
                          new LocalizedText(
                              "",
                              "Represents a chassis identifier based on the value of ifName object"
                                  + " (defined in IETF RFC 2863) for an interface on the containing"
                                  + " chassis."),
                          "InterfaceName"),
                      new EnumField(
                          7L,
                          LocalizedText.NULL_VALUE,
                          new LocalizedText(
                              "",
                              "Represents a chassis identifier based on a locally defined value."),
                          "Local")
                    }),
                false));
    Tree<DataType> portIdSubtype =
        enumeration.addChild(
            new ClientDataType(
                QualifiedName.parse("0:PortIdSubtype"),
                NodeIds.PortIdSubtype,
                null,
                null,
                null,
                new EnumDefinition(
                    new EnumField[] {
                      new EnumField(
                          1L,
                          LocalizedText.NULL_VALUE,
                          new LocalizedText(
                              "",
                              "Represents a port identifier based on the ifAlias MIB object defined"
                                  + " in IETF RFC 2863."),
                          "InterfaceAlias"),
                      new EnumField(
                          2L,
                          LocalizedText.NULL_VALUE,
                          new LocalizedText(
                              "",
                              "Represents a port identifier based on the value of entPhysicalAlias"
                                  + " (defined in IETF RFC 2737) for a port component (i.e.,"
                                  + " entPhysicalClass value of port(10) or backplane(4)), within"
                                  + " the containing chassis."),
                          "PortComponent"),
                      new EnumField(
                          3L,
                          LocalizedText.NULL_VALUE,
                          new LocalizedText(
                              "",
                              "Represents a port identifier based on a unicast source address"
                                  + " (encoded in network byte order and IEEE 802.3 canonical bit"
                                  + " order) which has been detected by the agent and associated"
                                  + " with a particular port (IEEE Std 802-2014)."),
                          "MacAddress"),
                      new EnumField(
                          4L,
                          LocalizedText.NULL_VALUE,
                          new LocalizedText(
                              "",
                              "Represents a port identifier based on a network address, detected by"
                                  + " the agent and associated with a particular port."),
                          "NetworkAddress"),
                      new EnumField(
                          5L,
                          LocalizedText.NULL_VALUE,
                          new LocalizedText(
                              "",
                              "Represents a port identifier based on the ifName MIB object, defined"
                                  + " in IETF RFC 2863."),
                          "InterfaceName"),
                      new EnumField(
                          6L,
                          LocalizedText.NULL_VALUE,
                          new LocalizedText(
                              "",
                              "Represents a port identifier based on the agent-local identifier of"
                                  + " the circuit (defined in IETF RFC 3046), detected by the agent"
                                  + " and associated with a particular port."),
                          "AgentCircuitId"),
                      new EnumField(
                          7L,
                          LocalizedText.NULL_VALUE,
                          new LocalizedText(
                              "",
                              "Represents a port identifier based on a value locally assigned."),
                          "Local")
                    }),
                false));
    Tree<DataType> manAddrIfSubtype =
        enumeration.addChild(
            new ClientDataType(
                QualifiedName.parse("0:ManAddrIfSubtype"),
                NodeIds.ManAddrIfSubtype,
                null,
                null,
                null,
                new EnumDefinition(
                    new EnumField[] {
                      new EnumField(
                          0L,
                          LocalizedText.NULL_VALUE,
                          new LocalizedText("", "Optional variable is not set."),
                          "None"),
                      new EnumField(
                          1L,
                          LocalizedText.NULL_VALUE,
                          new LocalizedText("", "Interface is not known."),
                          "Unknown"),
                      new EnumField(
                          2L,
                          LocalizedText.NULL_VALUE,
                          new LocalizedText("", "Interface based on the port-ref MIB object."),
                          "PortRef"),
                      new EnumField(
                          3L,
                          LocalizedText.NULL_VALUE,
                          new LocalizedText("", "Interface based on the system port number."),
                          "SystemPortNumber")
                    }),
                false));
    Tree<DataType> diagnosticsLevel =
        enumeration.addChild(
            new ClientDataType(
                QualifiedName.parse("0:DiagnosticsLevel"),
                NodeIds.DiagnosticsLevel,
                null,
                null,
                null,
                new EnumDefinition(
                    new EnumField[] {
                      new EnumField(
                          0L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "Basic"),
                      new EnumField(
                          1L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "Advanced"),
                      new EnumField(2L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "Info"),
                      new EnumField(3L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "Log"),
                      new EnumField(4L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "Debug")
                    }),
                false));
    Tree<DataType> pubSubDiagnosticsCounterClassification =
        enumeration.addChild(
            new ClientDataType(
                QualifiedName.parse("0:PubSubDiagnosticsCounterClassification"),
                NodeIds.PubSubDiagnosticsCounterClassification,
                null,
                null,
                null,
                new EnumDefinition(
                    new EnumField[] {
                      new EnumField(
                          0L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "Information"),
                      new EnumField(1L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "Error")
                    }),
                false));
    Tree<DataType> dataSetOrderingType =
        enumeration.addChild(
            new ClientDataType(
                QualifiedName.parse("0:DataSetOrderingType"),
                NodeIds.DataSetOrderingType,
                null,
                null,
                null,
                new EnumDefinition(
                    new EnumField[] {
                      new EnumField(
                          0L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "Undefined"),
                      new EnumField(
                          1L,
                          LocalizedText.NULL_VALUE,
                          LocalizedText.NULL_VALUE,
                          "AscendingWriterId"),
                      new EnumField(
                          2L,
                          LocalizedText.NULL_VALUE,
                          LocalizedText.NULL_VALUE,
                          "AscendingWriterIdSingle")
                    }),
                false));
    Tree<DataType> duplex =
        enumeration.addChild(
            new ClientDataType(
                QualifiedName.parse("0:Duplex"),
                NodeIds.Duplex,
                null,
                null,
                null,
                new EnumDefinition(
                    new EnumField[] {
                      new EnumField(
                          0L,
                          LocalizedText.NULL_VALUE,
                          new LocalizedText("", "Full duplex."),
                          "Full"),
                      new EnumField(
                          1L,
                          LocalizedText.NULL_VALUE,
                          new LocalizedText("", "Half duplex."),
                          "Half"),
                      new EnumField(
                          2L,
                          LocalizedText.NULL_VALUE,
                          new LocalizedText("", "Link is currently disconnected or initializing."),
                          "Unknown")
                    }),
                false));
    Tree<DataType> interfaceAdminStatus =
        enumeration.addChild(
            new ClientDataType(
                QualifiedName.parse("0:InterfaceAdminStatus"),
                NodeIds.InterfaceAdminStatus,
                null,
                null,
                null,
                new EnumDefinition(
                    new EnumField[] {
                      new EnumField(
                          0L,
                          LocalizedText.NULL_VALUE,
                          new LocalizedText("", "Ready to pass packets."),
                          "Up"),
                      new EnumField(
                          1L,
                          LocalizedText.NULL_VALUE,
                          new LocalizedText(
                              "", "Not ready to pass packets and not in some test mode."),
                          "Down"),
                      new EnumField(
                          2L,
                          LocalizedText.NULL_VALUE,
                          new LocalizedText("", "In some test mode."),
                          "Testing")
                    }),
                false));
    Tree<DataType> interfaceOperStatus =
        enumeration.addChild(
            new ClientDataType(
                QualifiedName.parse("0:InterfaceOperStatus"),
                NodeIds.InterfaceOperStatus,
                null,
                null,
                null,
                new EnumDefinition(
                    new EnumField[] {
                      new EnumField(
                          0L,
                          LocalizedText.NULL_VALUE,
                          new LocalizedText("", "Ready to pass packets."),
                          "Up"),
                      new EnumField(
                          1L,
                          LocalizedText.NULL_VALUE,
                          new LocalizedText("", "The interface does not pass any packets."),
                          "Down"),
                      new EnumField(
                          2L,
                          LocalizedText.NULL_VALUE,
                          new LocalizedText(
                              "", "In some test mode. No operational packets can be passed."),
                          "Testing"),
                      new EnumField(
                          3L,
                          LocalizedText.NULL_VALUE,
                          new LocalizedText("", "Status cannot be determined for some reason."),
                          "Unknown"),
                      new EnumField(
                          4L,
                          LocalizedText.NULL_VALUE,
                          new LocalizedText("", "Waiting for some external event."),
                          "Dormant"),
                      new EnumField(
                          5L,
                          LocalizedText.NULL_VALUE,
                          new LocalizedText("", "Some component (typically hardware) is missing."),
                          "NotPresent"),
                      new EnumField(
                          6L,
                          LocalizedText.NULL_VALUE,
                          new LocalizedText("", "Down due to state of lower-layer interface(s)."),
                          "LowerLayerDown")
                    }),
                false));
    Tree<DataType> negotiationStatus =
        enumeration.addChild(
            new ClientDataType(
                QualifiedName.parse("0:NegotiationStatus"),
                NodeIds.NegotiationStatus,
                null,
                null,
                null,
                new EnumDefinition(
                    new EnumField[] {
                      new EnumField(
                          0L,
                          LocalizedText.NULL_VALUE,
                          new LocalizedText(
                              "",
                              "The auto-negotiation protocol is running and negotiation is"
                                  + " currently in-progress."),
                          "InProgress"),
                      new EnumField(
                          1L,
                          LocalizedText.NULL_VALUE,
                          new LocalizedText(
                              "", "The auto-negotiation protocol has completed successfully."),
                          "Complete"),
                      new EnumField(
                          2L,
                          LocalizedText.NULL_VALUE,
                          new LocalizedText("", "The auto-negotiation protocol has failed."),
                          "Failed"),
                      new EnumField(
                          3L,
                          LocalizedText.NULL_VALUE,
                          new LocalizedText(
                              "",
                              "The auto-negotiation status is not currently known, this could be"
                                  + " because it is still negotiating or the protocol cannot run"
                                  + " (e.g., if no medium is present)."),
                          "Unknown"),
                      new EnumField(
                          4L,
                          LocalizedText.NULL_VALUE,
                          new LocalizedText(
                              "",
                              "No auto-negotiation is executed. The auto-negotiation function is"
                                  + " either not supported on this interface or has not been"
                                  + " enabled."),
                          "NoNegotiation")
                    }),
                false));
    Tree<DataType> tsnFailureCode =
        enumeration.addChild(
            new ClientDataType(
                QualifiedName.parse("0:TsnFailureCode"),
                NodeIds.TsnFailureCode,
                null,
                null,
                null,
                new EnumDefinition(
                    new EnumField[] {
                      new EnumField(
                          0L,
                          LocalizedText.NULL_VALUE,
                          new LocalizedText("", "No failure"),
                          "NoFailure"),
                      new EnumField(
                          1L,
                          LocalizedText.NULL_VALUE,
                          new LocalizedText("", "Insufficient bandwidth"),
                          "InsufficientBandwidth"),
                      new EnumField(
                          2L,
                          LocalizedText.NULL_VALUE,
                          new LocalizedText("", "Insufficient bridge resources"),
                          "InsufficientResources"),
                      new EnumField(
                          3L,
                          LocalizedText.NULL_VALUE,
                          new LocalizedText("", "Insufficient bandwidth for Traffic Class"),
                          "InsufficientTrafficClassBandwidth"),
                      new EnumField(
                          4L,
                          LocalizedText.NULL_VALUE,
                          new LocalizedText("", "StreamID in use by another Talker"),
                          "StreamIdInUse"),
                      new EnumField(
                          5L,
                          LocalizedText.NULL_VALUE,
                          new LocalizedText("", "Stream destination address already in use"),
                          "StreamDestinationAddressInUse"),
                      new EnumField(
                          6L,
                          LocalizedText.NULL_VALUE,
                          new LocalizedText("", "Stream pre-empted by higher rank"),
                          "StreamPreemptedByHigherRank"),
                      new EnumField(
                          7L,
                          LocalizedText.NULL_VALUE,
                          new LocalizedText("", "Reported latency has changed"),
                          "LatencyHasChanged"),
                      new EnumField(
                          8L,
                          LocalizedText.NULL_VALUE,
                          new LocalizedText("", "Egress port is not AVBCapable"),
                          "EgressPortNotAvbCapable"),
                      new EnumField(
                          9L,
                          LocalizedText.NULL_VALUE,
                          new LocalizedText("", "Use a different destination address"),
                          "UseDifferentDestinationAddress"),
                      new EnumField(
                          10L,
                          LocalizedText.NULL_VALUE,
                          new LocalizedText("", "Out of MSRP resources"),
                          "OutOfMsrpResources"),
                      new EnumField(
                          11L,
                          LocalizedText.NULL_VALUE,
                          new LocalizedText("", "Out of MMRP resources"),
                          "OutOfMmrpResources"),
                      new EnumField(
                          12L,
                          LocalizedText.NULL_VALUE,
                          new LocalizedText("", "Cannot store destination address"),
                          "CannotStoreDestinationAddress"),
                      new EnumField(
                          13L,
                          LocalizedText.NULL_VALUE,
                          new LocalizedText("", "Requested priority is not an SR Class priority"),
                          "PriorityIsNotAnSrcClass"),
                      new EnumField(
                          14L,
                          LocalizedText.NULL_VALUE,
                          new LocalizedText("", "MaxFrameSize is too large for media"),
                          "MaxFrameSizeTooLarge"),
                      new EnumField(
                          15L,
                          LocalizedText.NULL_VALUE,
                          new LocalizedText("", "MaxFanInPorts limit has been reached"),
                          "MaxFanInPortsLimitReached"),
                      new EnumField(
                          16L,
                          LocalizedText.NULL_VALUE,
                          new LocalizedText("", "Changes in FirstValue for a registered StreamID"),
                          "FirstValueChangedForStreamId"),
                      new EnumField(
                          17L,
                          LocalizedText.NULL_VALUE,
                          new LocalizedText(
                              "", "VLAN is blocked on this egress port (Registration Forbidden)"),
                          "VlanBlockedOnEgress"),
                      new EnumField(
                          18L,
                          LocalizedText.NULL_VALUE,
                          new LocalizedText(
                              "", "VLAN tagging is disabled on this egress port (untagged set)"),
                          "VlanTaggingDisabledOnEgress"),
                      new EnumField(
                          19L,
                          LocalizedText.NULL_VALUE,
                          new LocalizedText("", "SR class priority mismatch"),
                          "SrClassPriorityMismatch"),
                      new EnumField(
                          20L,
                          LocalizedText.NULL_VALUE,
                          new LocalizedText(
                              "", "Enhanced feature cannot be propagated to original Port"),
                          "FeatureNotPropagated"),
                      new EnumField(
                          21L,
                          LocalizedText.NULL_VALUE,
                          new LocalizedText("", "MaxLatency exceeded"),
                          "MaxLatencyExceeded"),
                      new EnumField(
                          22L,
                          LocalizedText.NULL_VALUE,
                          new LocalizedText(
                              "",
                              "Nearest Bridge cannot provide network identification for stream"
                                  + " transformation"),
                          "BridgeDoesNotProvideNetworkId"),
                      new EnumField(
                          23L,
                          LocalizedText.NULL_VALUE,
                          new LocalizedText("", "Stream transformation not supported"),
                          "StreamTransformNotSupported"),
                      new EnumField(
                          24L,
                          LocalizedText.NULL_VALUE,
                          new LocalizedText(
                              "",
                              "Stream identification type not supported for stream transformation"),
                          "StreamIdTypeNotSupported"),
                      new EnumField(
                          25L,
                          LocalizedText.NULL_VALUE,
                          new LocalizedText(
                              "", "Enhanced feature cannot be supported without a CNC"),
                          "FeatureNotSupported")
                    }),
                false));
    Tree<DataType> tsnStreamState =
        enumeration.addChild(
            new ClientDataType(
                QualifiedName.parse("0:TsnStreamState"),
                NodeIds.TsnStreamState,
                null,
                null,
                null,
                new EnumDefinition(
                    new EnumField[] {
                      new EnumField(
                          0L,
                          LocalizedText.NULL_VALUE,
                          new LocalizedText("", "The related TSN Stream is currently disabled."),
                          "Disabled"),
                      new EnumField(
                          1L,
                          LocalizedText.NULL_VALUE,
                          new LocalizedText(
                              "",
                              "The related TSN Stream is in the process of receiving configuration"
                                  + " parameters from the TSN Control Layer."),
                          "Configuring"),
                      new EnumField(
                          2L,
                          LocalizedText.NULL_VALUE,
                          new LocalizedText(
                              "",
                              "The related TSN Stream has successfully received and applied the"
                                  + " configuration from the TSN Control Layer. The related TSN"
                                  + " Stream is not fully operational as long as local"
                                  + " preconditions (e.g. synchronization state) are not valid."),
                          "Ready"),
                      new EnumField(
                          3L,
                          LocalizedText.NULL_VALUE,
                          new LocalizedText(
                              "",
                              "The related TSN Stream object is configured and all other required"
                                  + " preconditions (e.g. synchronization state) for sending /"
                                  + " receiving data are valid."),
                          "Operational"),
                      new EnumField(
                          4L,
                          LocalizedText.NULL_VALUE,
                          new LocalizedText(
                              "", "The related TSN Stream object is in an error state."),
                          "Error")
                    }),
                false));
    Tree<DataType> tsnTalkerStatus =
        enumeration.addChild(
            new ClientDataType(
                QualifiedName.parse("0:TsnTalkerStatus"),
                NodeIds.TsnTalkerStatus,
                null,
                null,
                null,
                new EnumDefinition(
                    new EnumField[] {
                      new EnumField(
                          0L,
                          LocalizedText.NULL_VALUE,
                          new LocalizedText("", "No Talker detected."),
                          "None"),
                      new EnumField(
                          1L,
                          LocalizedText.NULL_VALUE,
                          new LocalizedText("", "Talker ready (configured)."),
                          "Ready"),
                      new EnumField(
                          2L,
                          LocalizedText.NULL_VALUE,
                          new LocalizedText("", "Talker failed."),
                          "Failed")
                    }),
                false));
    Tree<DataType> tsnListenerStatus =
        enumeration.addChild(
            new ClientDataType(
                QualifiedName.parse("0:TsnListenerStatus"),
                NodeIds.TsnListenerStatus,
                null,
                null,
                null,
                new EnumDefinition(
                    new EnumField[] {
                      new EnumField(
                          0L,
                          LocalizedText.NULL_VALUE,
                          new LocalizedText("", "No Listener detected."),
                          "None"),
                      new EnumField(
                          1L,
                          LocalizedText.NULL_VALUE,
                          new LocalizedText("", "Listener ready (configured)."),
                          "Ready"),
                      new EnumField(
                          2L,
                          LocalizedText.NULL_VALUE,
                          new LocalizedText(
                              "", "One or more Listeners ready, and one or more Listeners failed."),
                          "PartialFailed"),
                      new EnumField(
                          3L,
                          LocalizedText.NULL_VALUE,
                          new LocalizedText("", "Listener failed."),
                          "Failed")
                    }),
                false));
    Tree<DataType> redundantServerMode =
        enumeration.addChild(
            new ClientDataType(
                QualifiedName.parse("0:RedundantServerMode"),
                NodeIds.RedundantServerMode,
                null,
                null,
                null,
                new EnumDefinition(
                    new EnumField[] {
                      new EnumField(
                          0L,
                          LocalizedText.NULL_VALUE,
                          LocalizedText.NULL_VALUE,
                          "PrimaryWithBackup"),
                      new EnumField(
                          1L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "PrimaryOnly"),
                      new EnumField(
                          2L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "BackupReady"),
                      new EnumField(
                          3L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "BackupNotReady")
                    }),
                false));
    Tree<DataType> conversionLimitEnum =
        enumeration.addChild(
            new ClientDataType(
                QualifiedName.parse("0:ConversionLimitEnum"),
                NodeIds.ConversionLimitEnum,
                null,
                null,
                null,
                new EnumDefinition(
                    new EnumField[] {
                      new EnumField(
                          0L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "NoConversion"),
                      new EnumField(
                          1L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "Limited"),
                      new EnumField(
                          2L, LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE, "Unlimited")
                    }),
                false));
  }
}
