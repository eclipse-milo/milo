/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.pubsub.config;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.DataTypeDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.EnumDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.SimpleTypeDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.StructureDefinition;
import org.eclipse.milo.opcua.stack.core.types.structured.StructureDescription;
import org.jspecify.annotations.Nullable;

/**
 * Configuration of a published dataset: a named {@link PublishedDataSetSourceConfig} plus a
 * configuration version.
 *
 * <p>The source is either a {@link PublishedDataItemsConfig} (an ordered list of {@link
 * FieldDefinition}s supplied with snapshots at runtime by a {@code PublishedDataSetSource} bound to
 * the same ref) or a {@link PublishedEventsConfig} (event fields selected from events emitted by an
 * event notifier Node). Published datasets are referenced by DataSetWriters via {@link
 * PublishedDataSetRef}. Corresponds to the Part 14 {@code PublishedDataSetDataType}.
 *
 * <p>A dataset whose fields carry custom DataTypes may additionally declare the type descriptions
 * those fields depend on ({@link Builder#structureDataType(StructureDescription)}, {@link
 * Builder#enumDataType(EnumDescription)}, {@link Builder#simpleDataType(SimpleTypeDescription)});
 * they populate the DataTypeSchemaHeader of the dataset's announced DataSetMetaData so a remote
 * subscriber can decode custom fields from the metadata alone. See {@link
 * #getStructureDataTypes()}.
 */
public final class PublishedDataSetConfig {

  private final String name;
  private final boolean enabled;
  private final PublishedDataSetSourceConfig source;
  private final UInteger configurationVersionMajor;
  private final UInteger configurationVersionMinor;
  private final Map<QualifiedName, Variant> properties;
  private final List<StructureDescription> structureDataTypes;
  private final List<EnumDescription> enumDataTypes;
  private final List<SimpleTypeDescription> simpleDataTypes;

  private PublishedDataSetConfig(Builder builder, PublishedDataSetSourceConfig source) {
    this.name = builder.name;
    this.enabled = builder.enabled;
    this.source = source;
    this.configurationVersionMajor = builder.configurationVersionMajor;
    this.configurationVersionMinor = builder.configurationVersionMinor;
    this.properties = Collections.unmodifiableMap(new LinkedHashMap<>(builder.properties));
    this.structureDataTypes = List.copyOf(builder.structureDataTypes);
    this.enumDataTypes = List.copyOf(builder.enumDataTypes);
    this.simpleDataTypes = List.copyOf(builder.simpleDataTypes);
  }

  /**
   * Get the name of this published dataset.
   *
   * @return the name of this published dataset.
   */
  public String getName() {
    return name;
  }

  /**
   * Get whether this published dataset is enabled.
   *
   * @return {@code true} if this published dataset is enabled.
   */
  public boolean isEnabled() {
    return enabled;
  }

  /**
   * Get the source of this published dataset.
   *
   * @return the {@link PublishedDataSetSourceConfig}.
   */
  public PublishedDataSetSourceConfig getSource() {
    return source;
  }

  /**
   * Get the field definitions of a data-items source, in dataset (wire) order.
   *
   * @return the field definitions when {@link #getSource()} is a {@link PublishedDataItemsConfig};
   *     an empty list for any other source kind.
   */
  public List<FieldDefinition> getFields() {
    if (source instanceof PublishedDataItemsConfig dataItems) {
      return dataItems.getFields();
    } else {
      return List.of();
    }
  }

  /**
   * Get the major configuration version.
   *
   * @return the major configuration version; defaults to 1.
   */
  public UInteger getConfigurationVersionMajor() {
    return configurationVersionMajor;
  }

  /**
   * Get the minor configuration version.
   *
   * @return the minor configuration version; defaults to 1.
   */
  public UInteger getConfigurationVersionMinor() {
    return configurationVersionMinor;
  }

  /**
   * Get the additional properties of this published dataset, in insertion order.
   *
   * @return an unmodifiable view of the additional properties.
   */
  public Map<QualifiedName, Variant> getProperties() {
    return properties;
  }

  /**
   * Get the descriptions of the Structure and Union DataTypes this dataset's fields depend on, in
   * authored order.
   *
   * <p>Together with {@link #getEnumDataTypes()} and {@link #getSimpleDataTypes()} these populate
   * the DataTypeSchemaHeader content of the dataset's DataSetMetaData (OPC UA 10000-14 §6.2.3.2.2:
   * the header "shall be populated with ... all namespaces and DataTypes that are potentially
   * contained in the associated DataSetMessages"; the descriptions themselves are defined in OPC UA
   * 10000-5 §12.31). NodeIds and names inside the descriptions are publisher-local — resolved
   * against the publisher's namespace table, the same convention as {@link
   * FieldDefinition#getDataType()} — and are remapped to metadata-local namespace indexes when the
   * metadata is derived.
   *
   * @return an unmodifiable list of the structure DataType descriptions.
   */
  public List<StructureDescription> getStructureDataTypes() {
    return structureDataTypes;
  }

  /**
   * Get the descriptions of the Enumeration and OptionSet DataTypes this dataset's fields depend
   * on, in authored order.
   *
   * @return an unmodifiable list of the enum DataType descriptions.
   * @see #getStructureDataTypes()
   */
  public List<EnumDescription> getEnumDataTypes() {
    return enumDataTypes;
  }

  /**
   * Get the descriptions of the DataTypes derived from builtin DataTypes this dataset's fields
   * depend on, in authored order.
   *
   * @return an unmodifiable list of the simple DataType descriptions.
   * @see #getStructureDataTypes()
   */
  public List<SimpleTypeDescription> getSimpleDataTypes() {
    return simpleDataTypes;
  }

  /**
   * Get a {@link PublishedDataSetRef} referencing this published dataset by name.
   *
   * @return a {@link PublishedDataSetRef} referencing this published dataset.
   */
  public PublishedDataSetRef ref() {
    return new PublishedDataSetRef(name);
  }

  /**
   * Create a {@link Builder} pre-populated with the values of this config.
   *
   * @return a new {@link Builder}.
   */
  public Builder toBuilder() {
    Builder builder = new Builder(name);
    builder.enabled = enabled;
    builder.source = source;
    builder.configurationVersionMajor = configurationVersionMajor;
    builder.configurationVersionMinor = configurationVersionMinor;
    builder.properties.putAll(properties);
    builder.structureDataTypes.addAll(structureDataTypes);
    builder.enumDataTypes.addAll(enumDataTypes);
    builder.simpleDataTypes.addAll(simpleDataTypes);
    return builder;
  }

  @Override
  public boolean equals(@Nullable Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof PublishedDataSetConfig that)) {
      return false;
    }
    return enabled == that.enabled
        && name.equals(that.name)
        && source.equals(that.source)
        && configurationVersionMajor.equals(that.configurationVersionMajor)
        && configurationVersionMinor.equals(that.configurationVersionMinor)
        && properties.equals(that.properties)
        && structureDataTypes.equals(that.structureDataTypes)
        && enumDataTypes.equals(that.enumDataTypes)
        && simpleDataTypes.equals(that.simpleDataTypes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        name,
        enabled,
        source,
        configurationVersionMajor,
        configurationVersionMinor,
        properties,
        structureDataTypes,
        enumDataTypes,
        simpleDataTypes);
  }

  @Override
  public String toString() {
    return "PublishedDataSetConfig{name='%s', enabled=%s, source=%s, configurationVersion=(%s, %s), properties=%s, structureDataTypes=%s, enumDataTypes=%s, simpleDataTypes=%s}"
        .formatted(
            name,
            enabled,
            source,
            configurationVersionMajor,
            configurationVersionMinor,
            properties,
            structureDataTypes,
            enumDataTypes,
            simpleDataTypes);
  }

  /**
   * Create a new {@link Builder}.
   *
   * @param name the name of the published dataset.
   * @return a new {@link Builder}.
   */
  public static Builder builder(String name) {
    return new Builder(name);
  }

  /** A builder of {@link PublishedDataSetConfig} instances. */
  public static final class Builder {

    private final String name;
    private boolean enabled = true;
    private @Nullable PublishedDataSetSourceConfig source;
    private final List<FieldDefinition> fields = new ArrayList<>();
    private UInteger configurationVersionMajor = uint(1);
    private UInteger configurationVersionMinor = uint(1);
    private final Map<QualifiedName, Variant> properties = new LinkedHashMap<>();
    private final List<StructureDescription> structureDataTypes = new ArrayList<>();
    private final List<EnumDescription> enumDataTypes = new ArrayList<>();
    private final List<SimpleTypeDescription> simpleDataTypes = new ArrayList<>();

    private Builder(String name) {
      this.name = name;
    }

    /**
     * Set whether the published dataset is enabled.
     *
     * @param enabled {@code true} if the published dataset is enabled.
     * @return this {@link Builder}.
     */
    public Builder enabled(boolean enabled) {
      this.enabled = enabled;
      return this;
    }

    /**
     * Add a field definition to an implicit {@link PublishedDataItemsConfig} source, built when
     * {@link #build()} is called. Field order defines wire order.
     *
     * <p>Cannot be combined with {@link #source(PublishedDataSetSourceConfig)}.
     *
     * @param field the field definition to add.
     * @return this {@link Builder}.
     */
    public Builder field(FieldDefinition field) {
      fields.add(field);
      return this;
    }

    /**
     * Set the source of the published dataset.
     *
     * <p>Cannot be combined with {@link #field(FieldDefinition)}. If no source is set, an implicit
     * {@link PublishedDataItemsConfig} is built from the added field definitions.
     *
     * @param source the {@link PublishedDataSetSourceConfig}.
     * @return this {@link Builder}.
     */
    public Builder source(PublishedDataSetSourceConfig source) {
      this.source = source;
      return this;
    }

    /**
     * Set the configuration version.
     *
     * @param major the major configuration version.
     * @param minor the minor configuration version.
     * @return this {@link Builder}.
     */
    public Builder configurationVersion(UInteger major, UInteger minor) {
      this.configurationVersionMajor = major;
      this.configurationVersionMinor = minor;
      return this;
    }

    /**
     * Add an additional property of the published dataset.
     *
     * @param name the name of the property.
     * @param value the value of the property.
     * @return this {@link Builder}.
     */
    public Builder property(QualifiedName name, Variant value) {
      properties.put(name, value);
      return this;
    }

    /**
     * Declare a Structure or Union DataType the dataset's fields depend on.
     *
     * <p>The description is announced in the {@code structureDataTypes} of the dataset's
     * DataSetMetaData, whose DataTypeSchemaHeader "shall be populated with ... all namespaces and
     * DataTypes that are potentially contained in the associated DataSetMessages" (OPC UA 10000-14
     * §6.2.3.2.2) — including nested Structures (OPC UA 10000-5 §12.31). Declaring the complete set
     * is the application's responsibility; nothing is inferred from field DataType NodeIds.
     *
     * <p>NodeIds and names inside the description are publisher-local (resolved against the
     * publisher's namespace table); metadata derivation remaps them to metadata-local namespace
     * indexes backed by the announced {@code namespaces} array.
     *
     * <p>Per OPC UA 10000-14 §6.2.3.2.6 the ConfigurationVersion {@code majorVersion} shall be
     * updated when the announced namespaces (or a field DataType) change. Like field edits, editing
     * type descriptions does not bump {@link #configurationVersion(UInteger, UInteger)}
     * automatically — the application owns the version.
     *
     * @param description the {@link StructureDescription}.
     * @return this {@link Builder}.
     */
    public Builder structureDataType(StructureDescription description) {
      structureDataTypes.add(description);
      return this;
    }

    /**
     * Declare a Structure or Union DataType from its parts — a convenience for types following the
     * Milo code-generated convention of a static {@code definition(NamespaceTable)} method:
     *
     * <pre>{@code
     * builder.structureDataType(dataTypeId, name, XVType.definition(namespaceTable));
     * }</pre>
     *
     * @param dataTypeId the publisher-local NodeId of the DataType.
     * @param name the name of the DataType; typically the DataType Node's BrowseName.
     * @param definition the {@link StructureDefinition}, e.g. {@code
     *     SomeType.definition(namespaceTable)}.
     * @return this {@link Builder}.
     * @see #structureDataType(StructureDescription)
     */
    public Builder structureDataType(
        NodeId dataTypeId, QualifiedName name, StructureDefinition definition) {
      return structureDataType(new StructureDescription(dataTypeId, name, definition));
    }

    /**
     * Declare an Enumeration or OptionSet DataType the dataset's fields depend on.
     *
     * <p>The description is announced in the {@code enumDataTypes} of the dataset's
     * DataSetMetaData; see {@link #structureDataType(StructureDescription)} for the authoring
     * contract.
     *
     * @param description the {@link EnumDescription}.
     * @return this {@link Builder}.
     */
    public Builder enumDataType(EnumDescription description) {
      enumDataTypes.add(description);
      return this;
    }

    /**
     * Declare a DataType derived from a builtin DataType that the dataset's fields depend on.
     *
     * <p>The description is announced in the {@code simpleDataTypes} of the dataset's
     * DataSetMetaData; see {@link #structureDataType(StructureDescription)} for the authoring
     * contract.
     *
     * @param description the {@link SimpleTypeDescription}.
     * @return this {@link Builder}.
     */
    public Builder simpleDataType(SimpleTypeDescription description) {
      simpleDataTypes.add(description);
      return this;
    }

    /**
     * Build a {@link PublishedDataSetConfig} from the configured values.
     *
     * @return a new {@link PublishedDataSetConfig}.
     * @throws PubSubConfigValidationException if the name is empty, both {@link
     *     #field(FieldDefinition)} and {@link #source(PublishedDataSetSourceConfig)} were used, the
     *     implicit data-items source contains two fields sharing a name or a DataSetFieldId, or two
     *     type descriptions share a DataType NodeId.
     */
    public PublishedDataSetConfig build() {
      if (name.isEmpty()) {
        throw new PubSubConfigValidationException("PublishedDataSetConfig: name must not be empty");
      }
      if (source != null && !fields.isEmpty()) {
        throw new PubSubConfigValidationException(
            "PublishedDataSetConfig '%s': field(...) and source(...) are mutually exclusive"
                .formatted(name));
      }

      Set<NodeId> describedDataTypeIds = new HashSet<>();
      for (List<? extends DataTypeDescription> descriptions :
          List.of(structureDataTypes, enumDataTypes, simpleDataTypes)) {
        for (DataTypeDescription description : descriptions) {
          if (!describedDataTypeIds.add(description.getDataTypeId())) {
            throw new PubSubConfigValidationException(
                "PublishedDataSetConfig '%s': duplicate type description for DataType %s"
                    .formatted(name, description.getDataTypeId()));
          }
        }
      }

      PublishedDataSetSourceConfig source = this.source;
      if (source == null) {
        PublishedDataItemsConfig.Builder dataItems = PublishedDataItemsConfig.builder();
        fields.forEach(dataItems::field);
        source = dataItems.build();
      }

      return new PublishedDataSetConfig(this, source);
    }
  }
}
