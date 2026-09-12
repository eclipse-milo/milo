/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.types.structured;

import java.util.StringJoiner;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaSerializationException;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.GenericDataTypeCodec;
import org.eclipse.milo.opcua.stack.core.encoding.UaDecoder;
import org.eclipse.milo.opcua.stack.core.encoding.UaEncoder;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.IdentityCriteriaType;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.util.codegen.EqualsBuilder;
import org.eclipse.milo.opcua.stack.core.util.codegen.HashCodeBuilder;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part18/4.4.3">https://reference.opcfoundation.org/v105/Core/docs/Part18/4.4.3</a>
 */
public class IdentityMappingRuleType extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("i=15634");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=15736");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=15728");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=15042");

  private final IdentityCriteriaType criteriaType;

  private final @Nullable String criteria;

  public IdentityMappingRuleType(IdentityCriteriaType criteriaType, @Nullable String criteria) {
    this.criteriaType = criteriaType;
    this.criteria = criteria;
  }

  @Override
  public ExpandedNodeId getTypeId() {
    return TYPE_ID;
  }

  @Override
  public ExpandedNodeId getBinaryEncodingId() {
    return BINARY_ENCODING_ID;
  }

  @Override
  public ExpandedNodeId getXmlEncodingId() {
    return XML_ENCODING_ID;
  }

  @Override
  public ExpandedNodeId getJsonEncodingId() {
    return JSON_ENCODING_ID;
  }

  public IdentityCriteriaType getCriteriaType() {
    return criteriaType;
  }

  public @Nullable String getCriteria() {
    return criteria;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    IdentityMappingRuleType that = (IdentityMappingRuleType) object;
    var eqb = new EqualsBuilder();
    eqb.append(getCriteriaType(), that.getCriteriaType());
    eqb.append(getCriteria(), that.getCriteria());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getCriteriaType());
    hcb.append(getCriteria());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", IdentityMappingRuleType.class.getSimpleName() + "[", "]");
    joiner.add("criteriaType=" + getCriteriaType());
    joiner.add("criteria='" + getCriteria() + "'");
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        NodeId.parse("i=15736"),
        NodeId.parse("i=22"),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "CriteriaType",
              LocalizedText.NULL_VALUE,
              NodeId.parse("i=15632"),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "Criteria",
              LocalizedText.NULL_VALUE,
              NodeId.parse("i=12"),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<IdentityMappingRuleType> {
    @Override
    public Class<IdentityMappingRuleType> getType() {
      return IdentityMappingRuleType.class;
    }

    @Override
    public IdentityMappingRuleType decodeType(EncodingContext context, UaDecoder decoder) {
      final IdentityCriteriaType criteriaType;
      final String criteria;
      {
        Integer enumValue = decoder.decodeEnum("CriteriaType");
        if (enumValue != null && !((Object) enumValue instanceof IdentityCriteriaType)) {
          if (!(enumValue instanceof Integer)) {
            throw new UaSerializationException(
                StatusCodes.Bad_TypeMismatch,
                "CriteriaType: expected"
                    + " org.eclipse.milo.opcua.stack.core.types.enumerated.IdentityCriteriaType or"
                    + " Int32, got "
                    + enumValue);
          }
          if (IdentityCriteriaType.from((Integer) enumValue) == null) {
            throw new UaSerializationException(
                StatusCodes.Bad_OutOfRange,
                "CriteriaType: unknown"
                    + " org.eclipse.milo.opcua.stack.core.types.enumerated.IdentityCriteriaType"
                    + " value "
                    + enumValue);
          }
        }
        criteriaType = enumValue == null ? null : IdentityCriteriaType.from(enumValue);
      }
      criteria = decoder.decodeString("Criteria");
      return new IdentityMappingRuleType(criteriaType, criteria);
    }

    @Override
    public void encodeType(
        EncodingContext context, UaEncoder encoder, IdentityMappingRuleType value) {
      encoder.encodeEnum("CriteriaType", value.getCriteriaType());
      encoder.encodeString("Criteria", value.getCriteria());
    }
  }
}
