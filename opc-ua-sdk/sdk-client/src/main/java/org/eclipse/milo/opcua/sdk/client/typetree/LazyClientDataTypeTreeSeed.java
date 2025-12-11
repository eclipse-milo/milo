package org.eclipse.milo.opcua.sdk.client.typetree;

import org.eclipse.milo.opcua.sdk.core.typetree.DataType;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.util.Tree;

public final class LazyClientDataTypeTreeSeed {
  private LazyClientDataTypeTreeSeed() {}

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
    root.addChild(
        new ClientDataType(
            QualifiedName.parse("0:Boolean"), NodeIds.Boolean, null, null, null, null, false));
    Tree<DataType> stringType =
        root.addChild(
            new ClientDataType(
                QualifiedName.parse("0:String"), NodeIds.String, null, null, null, null, false));
    stringType.addChild(
        new ClientDataType(
            QualifiedName.parse("0:NumericRange"),
            NodeIds.NumericRange,
            null,
            null,
            null,
            null,
            false));
    stringType.addChild(
        new ClientDataType(
            QualifiedName.parse("0:LocaleId"), NodeIds.LocaleId, null, null, null, null, false));
    stringType.addChild(
        new ClientDataType(
            QualifiedName.parse("0:NormalizedString"),
            NodeIds.NormalizedString,
            null,
            null,
            null,
            null,
            false));
    stringType.addChild(
        new ClientDataType(
            QualifiedName.parse("0:DecimalString"),
            NodeIds.DecimalString,
            null,
            null,
            null,
            null,
            false));
    stringType.addChild(
        new ClientDataType(
            QualifiedName.parse("0:DurationString"),
            NodeIds.DurationString,
            null,
            null,
            null,
            null,
            false));
    stringType.addChild(
        new ClientDataType(
            QualifiedName.parse("0:TimeString"),
            NodeIds.TimeString,
            null,
            null,
            null,
            null,
            false));
    stringType.addChild(
        new ClientDataType(
            QualifiedName.parse("0:DateString"),
            NodeIds.DateString,
            null,
            null,
            null,
            null,
            false));
    stringType.addChild(
        new ClientDataType(
            QualifiedName.parse("0:UriString"), NodeIds.UriString, null, null, null, null, false));
    stringType.addChild(
        new ClientDataType(
            QualifiedName.parse("0:SemanticVersionString"),
            NodeIds.SemanticVersionString,
            null,
            null,
            null,
            null,
            false));
    stringType.addChild(
        new ClientDataType(
            QualifiedName.parse("0:EncodedTicket"),
            NodeIds.EncodedTicket,
            null,
            null,
            null,
            null,
            false));
    stringType.addChild(
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
    dateTime.addChild(
        new ClientDataType(
            QualifiedName.parse("0:UtcTime"), NodeIds.UtcTime, null, null, null, null, false));
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
    image.addChild(
        new ClientDataType(
            QualifiedName.parse("0:ImageBMP"), NodeIds.ImageBMP, null, null, null, null, false));
    image.addChild(
        new ClientDataType(
            QualifiedName.parse("0:ImageGIF"), NodeIds.ImageGIF, null, null, null, null, false));
    image.addChild(
        new ClientDataType(
            QualifiedName.parse("0:ImageJPG"), NodeIds.ImageJPG, null, null, null, null, false));
    image.addChild(
        new ClientDataType(
            QualifiedName.parse("0:ImagePNG"), NodeIds.ImagePNG, null, null, null, null, false));
    byteString.addChild(
        new ClientDataType(
            QualifiedName.parse("0:ApplicationInstanceCertificate"),
            NodeIds.ApplicationInstanceCertificate,
            null,
            null,
            null,
            null,
            false));
    byteString.addChild(
        new ClientDataType(
            QualifiedName.parse("0:ContinuationPoint"),
            NodeIds.ContinuationPoint,
            null,
            null,
            null,
            null,
            false));
    byteString.addChild(
        new ClientDataType(
            QualifiedName.parse("0:AudioDataType"),
            NodeIds.AudioDataType,
            null,
            null,
            null,
            null,
            false));
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
    nodeId.addChild(
        new ClientDataType(
            QualifiedName.parse("0:SessionAuthenticationToken"),
            NodeIds.SessionAuthenticationToken,
            null,
            null,
            null,
            null,
            false));
    root.addChild(
        new ClientDataType(
            QualifiedName.parse("0:ExpandedNodeId"),
            NodeIds.ExpandedNodeId,
            null,
            null,
            null,
            null,
            false));
    root.addChild(
        new ClientDataType(
            QualifiedName.parse("0:StatusCode"),
            NodeIds.StatusCode,
            null,
            null,
            null,
            null,
            false));
    root.addChild(
        new ClientDataType(
            QualifiedName.parse("0:QualifiedName"),
            NodeIds.QualifiedName,
            null,
            null,
            null,
            null,
            false));
    root.addChild(
        new ClientDataType(
            QualifiedName.parse("0:LocalizedText"),
            NodeIds.LocalizedText,
            null,
            null,
            null,
            null,
            false));
    root.addChild(
        new ClientDataType(
            QualifiedName.parse("0:Structure"), NodeIds.Structure, null, null, null, null, true));
    root.addChild(
        new ClientDataType(
            QualifiedName.parse("0:DataValue"), NodeIds.DataValue, null, null, null, null, false));
    root.addChild(
        new ClientDataType(
            QualifiedName.parse("0:DiagnosticInfo"),
            NodeIds.DiagnosticInfo,
            null,
            null,
            null,
            null,
            false));
    Tree<DataType> numberType =
        root.addChild(
            new ClientDataType(
                QualifiedName.parse("0:Number"), NodeIds.Number, null, null, null, null, true));
    numberType.addChild(
        new ClientDataType(
            QualifiedName.parse("0:Float"), NodeIds.Float, null, null, null, null, false));
    Tree<DataType> doubleType =
        numberType.addChild(
            new ClientDataType(
                QualifiedName.parse("0:Double"), NodeIds.Double, null, null, null, null, false));
    doubleType.addChild(
        new ClientDataType(
            QualifiedName.parse("0:Duration"), NodeIds.Duration, null, null, null, null, false));
    Tree<DataType> integerType =
        numberType.addChild(
            new ClientDataType(
                QualifiedName.parse("0:Integer"), NodeIds.Integer, null, null, null, null, true));
    integerType.addChild(
        new ClientDataType(
            QualifiedName.parse("0:SByte"), NodeIds.SByte, null, null, null, null, false));
    integerType.addChild(
        new ClientDataType(
            QualifiedName.parse("0:Int16"), NodeIds.Int16, null, null, null, null, false));
    integerType.addChild(
        new ClientDataType(
            QualifiedName.parse("0:Int32"), NodeIds.Int32, null, null, null, null, false));
    integerType.addChild(
        new ClientDataType(
            QualifiedName.parse("0:Int64"), NodeIds.Int64, null, null, null, null, false));
    Tree<DataType> uInteger =
        numberType.addChild(
            new ClientDataType(
                QualifiedName.parse("0:UInteger"), NodeIds.UInteger, null, null, null, null, true));
    Tree<DataType> byteType =
        uInteger.addChild(
            new ClientDataType(
                QualifiedName.parse("0:Byte"), NodeIds.Byte, null, null, null, null, false));
    byteType.addChild(
        new ClientDataType(
            QualifiedName.parse("0:AccessLevelType"),
            NodeIds.AccessLevelType,
            null,
            null,
            null,
            null,
            false));
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
    uInt16.addChild(
        new ClientDataType(
            QualifiedName.parse("0:AccessRestrictionType"),
            NodeIds.AccessRestrictionType,
            null,
            null,
            null,
            null,
            false));
    uInt16.addChild(
        new ClientDataType(
            QualifiedName.parse("0:DataSetFieldFlags"),
            NodeIds.DataSetFieldFlags,
            null,
            null,
            null,
            null,
            false));
    uInt16.addChild(
        new ClientDataType(
            QualifiedName.parse("0:AlarmMask"), NodeIds.AlarmMask, null, null, null, null, false));
    Tree<DataType> uInt32 =
        uInteger.addChild(
            new ClientDataType(
                QualifiedName.parse("0:UInt32"), NodeIds.UInt32, null, null, null, null, false));
    uInt32.addChild(
        new ClientDataType(
            QualifiedName.parse("0:PermissionType"),
            NodeIds.PermissionType,
            null,
            null,
            null,
            null,
            false));
    uInt32.addChild(
        new ClientDataType(
            QualifiedName.parse("0:IntegerId"), NodeIds.IntegerId, null, null, null, null, false));
    uInt32.addChild(
        new ClientDataType(
            QualifiedName.parse("0:Counter"), NodeIds.Counter, null, null, null, null, false));
    uInt32.addChild(
        new ClientDataType(
            QualifiedName.parse("0:AttributeWriteMask"),
            NodeIds.AttributeWriteMask,
            null,
            null,
            null,
            null,
            false));
    uInt32.addChild(
        new ClientDataType(
            QualifiedName.parse("0:AccessLevelExType"),
            NodeIds.AccessLevelExType,
            null,
            null,
            null,
            null,
            false));
    uInt32.addChild(
        new ClientDataType(
            QualifiedName.parse("0:DataSetFieldContentMask"),
            NodeIds.DataSetFieldContentMask,
            null,
            null,
            null,
            null,
            false));
    uInt32.addChild(
        new ClientDataType(
            QualifiedName.parse("0:UadpNetworkMessageContentMask"),
            NodeIds.UadpNetworkMessageContentMask,
            null,
            null,
            null,
            null,
            false));
    uInt32.addChild(
        new ClientDataType(
            QualifiedName.parse("0:UadpDataSetMessageContentMask"),
            NodeIds.UadpDataSetMessageContentMask,
            null,
            null,
            null,
            null,
            false));
    uInt32.addChild(
        new ClientDataType(
            QualifiedName.parse("0:JsonNetworkMessageContentMask"),
            NodeIds.JsonNetworkMessageContentMask,
            null,
            null,
            null,
            null,
            false));
    uInt32.addChild(
        new ClientDataType(
            QualifiedName.parse("0:JsonDataSetMessageContentMask"),
            NodeIds.JsonDataSetMessageContentMask,
            null,
            null,
            null,
            null,
            false));
    uInt32.addChild(
        new ClientDataType(
            QualifiedName.parse("0:Index"), NodeIds.Index, null, null, null, null, false));
    uInt32.addChild(
        new ClientDataType(
            QualifiedName.parse("0:LldpSystemCapabilitiesMap"),
            NodeIds.LldpSystemCapabilitiesMap,
            null,
            null,
            null,
            null,
            false));
    uInt32.addChild(
        new ClientDataType(
            QualifiedName.parse("0:VersionTime"),
            NodeIds.VersionTime,
            null,
            null,
            null,
            null,
            false));
    uInt32.addChild(
        new ClientDataType(
            QualifiedName.parse("0:TrustListValidationOptions"),
            NodeIds.TrustListValidationOptions,
            null,
            null,
            null,
            null,
            false));
    uInt32.addChild(
        new ClientDataType(
            QualifiedName.parse("0:PasswordOptionsMask"),
            NodeIds.PasswordOptionsMask,
            null,
            null,
            null,
            null,
            false));
    uInt32.addChild(
        new ClientDataType(
            QualifiedName.parse("0:UserConfigurationMask"),
            NodeIds.UserConfigurationMask,
            null,
            null,
            null,
            null,
            false));
    uInt32.addChild(
        new ClientDataType(
            QualifiedName.parse("0:PubSubConfigurationRefMask"),
            NodeIds.PubSubConfigurationRefMask,
            null,
            null,
            null,
            null,
            false));
    uInt32.addChild(
        new ClientDataType(
            QualifiedName.parse("0:Handle"), NodeIds.Handle, null, null, null, null, false));
    Tree<DataType> uInt64 =
        uInteger.addChild(
            new ClientDataType(
                QualifiedName.parse("0:UInt64"), NodeIds.UInt64, null, null, null, null, false));
    uInt64.addChild(
        new ClientDataType(
            QualifiedName.parse("0:BitFieldMaskDataType"),
            NodeIds.BitFieldMaskDataType,
            null,
            null,
            null,
            null,
            false));
    numberType.addChild(
        new ClientDataType(
            QualifiedName.parse("0:Decimal"), NodeIds.Decimal, null, null, null, null, false));
    root.addChild(
        new ClientDataType(
            QualifiedName.parse("0:Enumeration"),
            NodeIds.Enumeration,
            null,
            null,
            null,
            null,
            true));
    return root;
  }
}
