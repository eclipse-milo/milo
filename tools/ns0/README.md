# Generate the ns0 server and client model

The server and client APIs are generated with OPC UA Model Tools from one launcher, so both bind to
the same Method descriptors. `GenerateNs0Model` runs three generation units in order: the shared
descriptors, then the server library, then the client library. Each unit writes its own output root
and compiler ownership manifest under the output directory.

Shared Method descriptors live in `milo-sdk-core` (`org.eclipse.milo.opcua.sdk.core.model.methods`)
and are generated once; both SDK libraries consume that catalog as a datatype dependency instead of
emitting their own copies. Server interfaces and node classes retain Milo's
`org.eclipse.milo.opcua.sdk.server.model.objects` and `.variables` packages in `milo-sdk-server`, and
client interfaces and node classes retain `org.eclipse.milo.opcua.sdk.client.model.objects` and
`.variables` in `milo-sdk-client`. Initializers and generated support live in
`org.eclipse.milo.opcua.sdk.server.model` and `org.eclipse.milo.opcua.sdk.client.model`. Each request
selects these exact packages with `TypePackages`. Names beginning with `3D` use `ThreeD` in Java,
preserving the existing standard model names and original UA BrowseNames.
The GDS model generator is a separate migration; its client and server types extend the generated
namespace-zero bases unchanged.

The input is OPC Foundation UA-1.05.07-2026-04-15, repository revision
`6338cced8e6cc2fa2c3816bc6b3bad5daee3f101`, as bundled in Model Tools. The source launcher rejects
any input whose SHA-256 differs from
`d29ad5eb20884935a6b7ef8f9b70a6ad72417411a3fd3e4e704dac5762d394c8`.
`model-roots.tsv` explicitly selects 342 ObjectTypes and VariableTypes for both libraries. The
ObjectType roots are also the shared descriptor roots; VariableTypes declare no Methods. On the
server BaseObjectType, BaseVariableType and BaseDataVariableType are emitted with their node classes
as part of the ancestor closure, and the generated initializers register their constructors along
with the other model types. Generated constructors resolve no children. The client compiler binds to
Milo's intrinsic client base classes instead, so those six client files stay handwritten and
`install.sh` preserves them.

Build Milo from this branch into a dedicated local Maven repository, following the repository's
Maven delegation rules. The initial compiler bootstrap needs the Method runtime APIs and
`UaArgumentConversionException` from stack core. Use JDK 17 for Milo and the Model Tools checkout's
pinned JDK 21 for generation. Commands from the Milo root:

```bash
export MILO_REPOSITORY=/absolute/path/to/local-milo-repository
mise exec -- mvn -q -DskipTests -Dmaven.repo.local="$MILO_REPOSITORY" install
./tools/ns0/generate.sh /absolute/path/to/opc-ua-model-tools /absolute/path/to/generated-ns0
./tools/ns0/install.sh /absolute/path/to/generated-ns0
mise exec -- mvn -q spotless:apply
mise exec -- mvn -q -Dmaven.repo.local="$MILO_REPOSITORY" verify
```

`generate.sh` refreshes snapshot resolution, builds the compiler checkout and records source revisions, tracked diff hashes,
source-file hashes (including untracked sources), and Milo jar/POM hashes beside the compiler ownership manifest and diagnostics. Keep the checkout
revisions and any uncommitted changes with review evidence. Set `MILO_REPOSITORY` for Model Tools
verification too; its Gradle repository is exclusive for `org.eclipse.milo`, and its independent
Maven consumers mirror the snapshots repository to this directory.

`install.sh` replaces direct Java files in the seven owned packages, including all six base interface
and node files under the server's `model.objects` and `model.variables`. Generation uses concrete
generated standard child classes, so the node loader, constructor registry and generated library must
be updated together. Initializers reject different existing registrations rather than overwriting
application constructors.

Client child accessors are `getXNode()` for the child node and `readX()`/`writeX()` for its value,
with `Async` counterparts; the removed cached `getX()` value accessors have no replacement. Method
children use `getXMethodNode()` and the typed call forms described in the Model Tools generated
Methods documentation.

Methods with instance modelling rules have typed Object-owned handlers. `setXHandler(null)` clears
one slot and restores the Method-node fallback. The shared descriptors handle raw calls as well.
Methods without an instance modelling rule (including ConditionRefresh) and Organizes references
provide descriptors and lookup only. Condition and alias managers keep their Method-node handlers
because they manage dispatch ownership, startup rollback and non-exposed Conditions; those handlers
now consume shared descriptors directly.

`SupportsFilteredRetain` has no instance modelling rule. Its capability value belongs to the
ConditionType declaration. Specialized Variable values use type-qualified accessor names where the
base API exposes Variant. Property setters operate on existing children; applications select
optional children through the instantiator before setting them. Private condition snapshot trees
resolve children in their own node manager, preserving isolation when NodeIds match live nodes.

`Out<T>` remains for the separate GDS generated model. Removing it requires regenerating that model
and migrating its consumers. This server ns0 generation emits neither Out parameters nor the old
nested Method invocation classes.
