# Eclipse Milo client examples

To run the examples in this project, do the following steps:

1. Import the project into your IDE of choice

2. Run any of the ClientExample implementations

Note: When you run the clients, it takes care of starting a local Milo server, so you do not have to have a server up and running!

Vendor-specific examples are grouped by server family:

- `org.eclipse.milo.examples.client.prosys`
- `org.eclipse.milo.examples.client.ijt`
- `org.eclipse.milo.examples.client.unifiedautomation`

Reverse Connect examples focus on discovery-first clients that start without an
`EndpointDescription` and use the SDK's discovery-first helpers:

- `ReverseConnectExample` starts a local Milo client listener and uses `DiscoveryFirstReverseConnectClient` to discover the endpoint before the production Session.
- `ReverseConnectSharedListenerExample` shows two local Milo servers sharing one listener while `ReverseConnectAcceptor` creates one production client per discovered server.
- `prosys.ProsysReverseConnectExample` waits for Prosys OPC UA Simulation Server to dial `opc.tcp://localhost:48060` and uses the same discovery-first client helper.
