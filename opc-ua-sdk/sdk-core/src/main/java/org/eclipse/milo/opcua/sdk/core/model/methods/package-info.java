/**
 * Shared Method descriptors: declared arguments and typed wire conversion for both SDKs.
 *
 * <p>Each descriptor is named for its declaring ObjectType and Method. Its BrowseName and Argument
 * accessors return fresh metadata resolved through the caller's namespace table. Nonempty sides
 * have {@code Inputs} and {@code Outputs} records in declared order. The records have no OPC UA
 * identity and convert through the caller's encoding context.
 *
 * <p>{@code toVariants} emits every declared position and supplies null for an omitted optional
 * input. {@code fromVariants} validates count, type, rank and dimensions, and fills an omitted
 * trailing optional suffix with null. Null and empty arrays remain distinct. Structures use
 * registered codecs, enums use their Int32 values, and flexible or matrix ranks keep wire-facing
 * Variants.
 */
@org.jspecify.annotations.NullMarked
package org.eclipse.milo.opcua.sdk.core.model.methods;
