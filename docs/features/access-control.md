# Access control, roles, and permissions

Application policy for Milo 1.2.0-SNAPSHOT is documented in the [Server Access Control guide](https://github.com/eclipse-milo/milo/wiki/Server-Access-Control). Implementation boundaries and test entry points remain here.

<a id="table-of-contents"></a>
<a id="overview"></a>
<a id="configuration"></a>
<a id="static-access"></a>
<a id="session-dependent-access"></a>
<a id="role-mapping"></a>
<a id="custom-controller"></a>

## Application guide

See [identity, roles, and effective attributes](https://github.com/eclipse-milo/milo/wiki/Server-Access-Control#identity-roles-and-effective-attributes) for application-supplied permissions, and [default and custom decisions](https://github.com/eclipse-milo/milo/wiki/Server-Access-Control#default-and-custom-decisions) for controller integration. Roles and metadata alone do not implement effective user attributes.

<a id="service-checks"></a>
<a id="read-results-and-monitored-items"></a>

## Services and monitored items

The Wiki lists the [service authorization boundary and its exclusions](https://github.com/eclipse-milo/milo/wiki/Server-Access-Control#default-and-custom-decisions), [monitored-item decisions](https://github.com/eclipse-milo/milo/wiki/Server-Access-Control#data-monitored-items), and failure statuses. Use that boundary when implementing an address space or a custom controller.

<a id="troubleshooting"></a>

## Changing permissions at runtime

Follow [permission changes and cache invalidation](https://github.com/eclipse-milo/milo/wiki/Server-Access-Control#permission-changes-and-cache-invalidation) when application policy changes. It distinguishes direct service checks, cached sampling, queued notifications, and application-controlled revocation. [External producers](https://github.com/eclipse-milo/milo/wiki/Server-Sampling#authorization-and-external-producers) must also refresh stored decisions.

<a id="how-it-works"></a>

## Implementation

[AccessControlManager](../../opc-ua-sdk/sdk-server/src/main/java/org/eclipse/milo/opcua/sdk/server/access/AccessControlManager.java) owns the controller, cache, and invalidation listeners. The `sampling` package decides when to refresh; [MonitoredDataItem](../../opc-ua-sdk/sdk-server/src/main/java/org/eclipse/milo/opcua/sdk/server/items/MonitoredDataItem.java) gates delivery with the stored result. The access package does not collect values or store results on items.

Read the [access package documentation](../../opc-ua-sdk/sdk-server/src/main/java/org/eclipse/milo/opcua/sdk/server/access/package-info.java) for package ownership and threading, and the [sampling architecture's read-access discussion](../architecture/sampling-framework.md#read-access) for the refresh boundary. Changes to [ReadAccessCache](../../opc-ua-sdk/sdk-server/src/main/java/org/eclipse/milo/opcua/sdk/server/access/ReadAccessCache.java) must preserve invalidation during pending loads. Check the Session and access-epoch guards in item updates when changing identity or transfer handling. [DefaultSubscriptionServiceSet](../../opc-ua-sdk/sdk-server/src/main/java/org/eclipse/milo/opcua/sdk/server/servicesets/impl/DefaultSubscriptionServiceSet.java) refuses a transfer with `Bad_InternalError` if its read-access recheck throws, before moving the Subscription. Preserve that failure boundary when changing transfer logic.

[DefaultAccessController](../../opc-ua-sdk/sdk-server/src/main/java/org/eclipse/milo/opcua/sdk/server/access/DefaultAccessController.java) reads up to seven attributes per distinct Node through the filter chain: NodeClass, AccessRestrictions, UserWriteMask, AccessLevel, UserAccessLevel, UserExecutable, and UserRolePermissions. Read checks select only the attributes used by their decision, with NodeClass always included. Attribute filters must be quick and thread-safe. Keep those reads below the service authorization boundary to avoid recursive controller calls. Do not infer controller coverage for a service from the presence of an access attribute; consult the Wiki's explicit service list and that service's dispatch code.

## Testing

[DefaultAccessControllerTest](../../opc-ua-sdk/sdk-server/src/test/java/org/eclipse/milo/opcua/sdk/server/access/DefaultAccessControllerTest.java) covers controller decisions. [MonitoredItemReadAccessTest](../../opc-ua-sdk/integration-tests/src/test/java/org/eclipse/milo/opcua/sdk/server/subscriptions/MonitoredItemReadAccessTest.java) covers delivery, permission changes, identity changes, and transfer behavior. [RestrictedAccessFilter](../../milo-examples/server-examples/src/main/java/org/eclipse/milo/examples/server/RestrictedAccessFilter.java) remains a source example of Session-aware filtering.

The read-side distinction and Publish reporting arrived in [#2076](https://github.com/eclipse-milo/milo/pull/2076); the invalidation API and the sampling framework that refreshes stored results arrived in [#2089](https://github.com/eclipse-milo/milo/pull/2089).

Follow the repository's [test invocation guidance](../../.claude/docs/running-tests.md) and [test quality guidelines](../../.claude/docs/test-documentation-and-quality-guidelines.md).
