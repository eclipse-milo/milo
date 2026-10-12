# GDS client

The [GDS Client Wiki guide](https://github.com/eclipse-milo/milo/wiki/GDS-Client) owns the Milo 1.2.0-SNAPSHOT application workflow, dependencies, failure handling, and deployment limitations. Model generation and module ownership remain documented here.

<a id="table-of-contents"></a>
<a id="usage"></a>
<a id="limitations"></a>

## Application guide

Follow the Wiki to [register an application](https://github.com/eclipse-milo/milo/wiki/GDS-Client#register-or-find-the-application), [request and install a certificate](https://github.com/eclipse-milo/milo/wiki/GDS-Client#request-poll-and-install-a-certificate), and [read and apply trust lists](https://github.com/eclipse-milo/milo/wiki/GDS-Client#read-and-apply-a-trust-list). Its [workflow and deployment policy section](https://github.com/eclipse-milo/milo/wiki/GDS-Client#own-the-workflow-and-deployment-policy) records application-owned scheduling, persistence, enrollment policy, and peer compatibility. Local fake-GDS tests do not establish vendor interoperability.

<a id="overview"></a>

## Module ownership

Generated model classes live in the existing stack, dictionary, client, and server modules. The handwritten client API lives in [sdk-client-gds](../../opc-ua-sdk/sdk-client-gds), under `org.eclipse.milo.opcua.sdk.client.gds`; generated client initializers and nodes live under `.gds.model` in `sdk-client`. Keep these packages distinct so the jars can resolve together on the module path.

[FakeGdsNamespace](../../opc-ua-sdk/sdk-client-gds-testing/src/main/java/org/eclipse/milo/opcua/sdk/client/gds/testing/FakeGdsNamespace.java) lives in the separate `sdk-client-gds-testing` module. Keep its server dependencies out of the runtime client module. See the [handwritten API package documentation](../../opc-ua-sdk/sdk-client-gds/src/main/java/org/eclipse/milo/opcua/sdk/client/gds/package-info.java) and the generated model package documentation linked below.

<a id="how-it-works"></a>
<a id="namespace-resolution-and-type-registration"></a>
<a id="method-invocation"></a>
<a id="trustlist-files"></a>

## Registration and protocol boundaries

[GdsClient](../../opc-ua-sdk/sdk-client-gds/src/main/java/org/eclipse/milo/opcua/sdk/client/gds/GdsClient.java) resolves GDS identifiers through the connected server's namespace table and validates Method outputs. [TrustListReader](../../opc-ua-sdk/sdk-client-gds/src/main/java/org/eclipse/milo/opcua/sdk/client/gds/TrustListReader.java) owns its FileType handle while reading and decoding; [TrustListApplier](../../opc-ua-sdk/sdk-client-gds/src/main/java/org/eclipse/milo/opcua/sdk/client/gds/TrustListApplier.java) updates the supplied trust manager. The Wiki documents the caller's connection, error, and resource responsibilities.

The GDS `DataTypeInitializer` and both `ObjectTypeInitializer`s take a `NamespaceTable`. Keep them out of `DefaultDataTypeManager.createAndInitialize` and the client/server namespace 0 initializers. `GdsClient.create` performs client-side registration; a server hosting the GDS namespace registers its types after adding the GDS URI to its table. The [stack model package](../../opc-ua-stack/stack-core/src/main/java/org/eclipse/milo/opcua/stack/core/gds/package-info.java), [client model package](../../opc-ua-sdk/sdk-client/src/main/java/org/eclipse/milo/opcua/sdk/client/gds/model/package-info.java), and [server model package](../../opc-ua-sdk/sdk-server/src/main/java/org/eclipse/milo/opcua/sdk/server/gds/model/package-info.java) describe these boundaries.

## Generated model

The model is generated and checked in; the Maven build does not generate it. It comes from GDS NodeSet2 1.05.07 on a 1.05.07 base, using the same external generator as Milo's namespace 0 model. Import generated files with rewritten packages and the Eclipse Milo license header; do not hand-edit their implementation.

The generator roots are `com.digitalpetri.opcua.gds.client.model` and `com.digitalpetri.opcua.gds.server.model`. Initializers live directly in those packages; typed nodes use their `.objects` and `.variables` subpackages. Each generated package belongs to one module, while handwritten APIs use the parent packages. `PackageMap.create(...)` uses prefixes as supplied, so new companion model generators must supply dedicated model roots.

Older output placed initializers in `.client` and `.server` and typed nodes in `.client.objects` and `.server.objects`. Map those packages to the same Milo model destinations below when importing older output.

| Generated source package | Milo package | Destination module |
| --- | --- | --- |
| `com.digitalpetri.opcua.gds` | `org.eclipse.milo.opcua.stack.core.gds` | `stack-core` |
| `com.digitalpetri.opcua.gds.types` | `org.eclipse.milo.opcua.stack.core.gds.types` | `stack-core` |
| `com.digitalpetri.opcua.gds` (`BinaryDataTypeDictionaryInitializer` only) | `org.eclipse.milo.opcua.sdk.core.dtd.gds` | `dtd-core` |
| `com.digitalpetri.opcua.gds.client.model`, including subpackages | `org.eclipse.milo.opcua.sdk.client.gds.model`, including subpackages | `sdk-client` |
| `com.digitalpetri.opcua.gds.server.model`, including subpackages | `org.eclipse.milo.opcua.sdk.server.gds.model`, including subpackages | `sdk-server` |

To import a new GDS NodeSet release:

1. Regenerate into a clean output directory with `.client.model` and `.server.model` roots. Remove obsolete generated files from previous package layouts during import; generation does not delete them.
2. Copy the generated core, client, and server modules' Java sources into the destinations above. Rewrite package and import prefixes from most specific to least specific: `.client.model`, `.server.model`, and `.types` before the bare prefix. Put `BinaryDataTypeDictionaryInitializer` in `dtd-core` because its superclass, `DataTypeDictionaryInitializer`, belongs to that module.
3. Prepend the EPL-2.0 header. Follow [repository verification guidance](../../AGENTS.md#verification) to format and compile, including the required Maven command runner.
4. Update the NodeSet version in the affected `package-info.java` files and this document.
5. Diff the imported files against generator output with the same prefix rewrites. Only the license header and import order should differ.
6. Run [GdsModulePathTest](../../opc-ua-sdk/integration-tests/src/test/java/org/eclipse/milo/opcua/sdk/client/gds/GdsModulePathTest.java) and [GdsServerModelTest](../../opc-ua-sdk/integration-tests/src/test/java/org/eclipse/milo/opcua/sdk/server/gds/GdsServerModelTest.java) using the [integration-test invocation guidance](../../.claude/docs/running-tests.md). They check module package ownership and server model registration.

The shared property-writer generator must preserve the blocking writer's `UaException` for a non-Good operation result and the asynchronous writer's returned `StatusCode`. This also affects namespace 0 generated writers. Review both model families when changing that generator.
