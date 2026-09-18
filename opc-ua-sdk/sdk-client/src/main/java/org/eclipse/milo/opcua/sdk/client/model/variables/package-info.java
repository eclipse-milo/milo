/**
 * Generated client API for OPC UA ObjectTypes and VariableTypes.
 *
 * <p>Each type has an interface and a node class. The registered constructors create node classes
 * when Milo's AddressSpace materializes a node of that type. Constructors only forward fetched
 * attributes.
 *
 * <h2>Lifecycle</h2>
 *
 * <p>Connect, then initialize dependency codecs, this library's codecs and the ObjectType and
 * VariableType initializers before obtaining nodes. Registrations last for the client's lifetime
 * and do not upgrade node instances that are already cached.
 *
 * <h2>Child resolution</h2>
 *
 * <p>Child getters resolve the declared path on every call by URI-qualified BrowseName and
 * reference type, including subtypes, and validate the expected NodeClass and Java binding. Only
 * Bad_NoMatch on an optional stage becomes null. Other failures, ambiguous targets and incompatible
 * bindings fail with {@code UaException}; asynchronous forms return failed futures instead. Nothing
 * caches member targets, so additions and removals are visible on the next call.
 *
 * <h2>Values</h2>
 *
 * <p>For a member X, {@code readX()} reads remotely, requires Good quality and decodes, and returns
 * null for an absent optional child. {@code writeX(value)} writes remotely and requires a Good
 * result; {@code writeXAsync(value)} completes with the operation status, and both fail with
 * Bad_NotFound on an absent child. Remote operations leave the cached Value unchanged and do not
 * retry. A Good null Value decodes as null. Cached raw access stays on {@code
 * getXNode().getValue()}.
 *
 * <h2>Methods</h2>
 *
 * <p>Modelled HasComponent Methods have a {@code getXMethodNode()} getter and three call families,
 * each with an asynchronous form. The convenience form requires a Good result and returns the
 * outputs. {@code callX} returns the complete result, including a Bad status. {@code callXWith}
 * adds explicit call options. Overloads omit trailing optional inputs. Methods reached through
 * Organizes are lookup-only.
 */
@NullMarked
package org.eclipse.milo.opcua.sdk.client.model.variables;

import org.jspecify.annotations.NullMarked;
