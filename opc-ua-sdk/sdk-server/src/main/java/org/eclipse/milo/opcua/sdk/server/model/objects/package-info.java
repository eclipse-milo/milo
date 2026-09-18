/**
 * Generated server API for OPC UA ObjectTypes and VariableTypes.
 *
 * <p>Each type has an interface and a node class. The interface declares the type identity, child
 * accessors, typed Value accessors and Method handlers. The node class extends the Milo node class
 * of the UA supertype and implements the interface. Its constructors only forward attributes.
 *
 * <h2>Lifecycle</h2>
 *
 * <p>Register namespace URIs, dependency codecs and this library's codecs, then run the ObjectType
 * and VariableType initializers before loading instances. Constructors create no children and
 * resolve nothing. Access and validate children after the instance is installed in the address
 * space. {@code validateChildren()} checks the supported immediate children, including inherited
 * requirements, without recursing into them.
 *
 * <h2>Child resolution</h2>
 *
 * <p>Child getters look up the live address space on every call by reference type and URI-qualified
 * BrowseName, so adding or removing a child changes the next result. An absent optional child
 * returns null. Absence of a mandatory child fails with {@code UaRuntimeException} and
 * Bad_NotFound, several distinct targets with Bad_TooManyMatches, and an unsatisfied UA type,
 * DataType, rank or Java class with Bad_TypeMismatch. A present child of the wrong Java class
 * usually means the initializer ran after the nodes were loaded. Declarations nested below a child
 * are outside validation.
 *
 * <h2>Typed values</h2>
 *
 * <p>Value getters convert the current Value through Milo's codecs. A null payload or an absent
 * optional child returns null. A Bad status throws {@code UaRuntimeException} carrying that status;
 * a conversion failure, including an unknown enum number, uses Bad_DecodingError. An Uncertain
 * status still converts. Setters validate and convert before mutating, store a Good status with
 * cleared timestamps, and never create children: a setter on an absent child fails with
 * Bad_NotFound. Milo refreshes the server timestamp on reads. The inherited DataValue accessors
 * expose status, source time and picoseconds.
 *
 * <h2>Methods</h2>
 *
 * <p>A handler setter resolves the existing Method node and replaces one slot owned by this
 * instance; it creates no nodes, and an absent Method node fails with Bad_NotFound. Null clears the
 * slot and restores fallback to the Method node's own invocation handler. A {@code Methods}
 * implementation installs every slot, and its unimplemented defaults report Bad_NotImplemented.
 * Inputs are validated against the effective declaration before the handler runs; outputs are
 * validated before the response is sent.
 */
@NullMarked
package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.jspecify.annotations.NullMarked;
