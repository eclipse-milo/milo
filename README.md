# Eclipse Milo
[![Actions](https://img.shields.io/github/actions/workflow/status/eclipse-milo/milo/.github%2Fworkflows%2Fmaven-verify.yml
)](https://github.com/eclipse-milo/milo/actions/workflows/maven-verify.yml)
[![Maven Central](https://img.shields.io/maven-central/v/org.eclipse.milo/milo.svg)](https://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22org.eclipse.milo%22%20AND%20a%3A%22milo%22)

Milo is an open-source implementation of OPC UA (currently targeting 1.05). It includes a high-performance stack (channels, serialization, data structures, security) as well as client and server SDKs built on top of the stack.

Stack Overflow tag: [milo](http://stackoverflow.com/questions/tagged/milo)

Mailing list: https://dev.eclipse.org/mailman/listinfo/milo-dev

## Documentation

The [Milo Wiki][wiki] is the user guide. If you are new to Milo, start with
[Getting started][getting-started], then build the tutorial [server][first-server] and a
[client][first-client] that talks to it. The [client][client-guide] and [server][server-guide]
guides continue from there, and the [1.2.0 migration guide][migration] covers upgrading from 1.1.
[Examples][examples] lists the runnable programs in `milo-examples`.

[wiki]: https://github.com/eclipse-milo/milo/wiki
[getting-started]: https://github.com/eclipse-milo/milo/wiki/Getting-Started
[first-server]: https://github.com/eclipse-milo/milo/wiki/First-Server
[first-client]: https://github.com/eclipse-milo/milo/wiki/First-Client
[client-guide]: https://github.com/eclipse-milo/milo/wiki/Client
[server-guide]: https://github.com/eclipse-milo/milo/wiki/Server
[migration]: https://github.com/eclipse-milo/milo/wiki/Release-Notes-1.2.0
[examples]: https://github.com/eclipse-milo/milo/wiki/Examples

## Requirements

The repository pins its Java and Maven toolchain with `mise`:

```shell
mise install
```

If `mise` reports that the config is not trusted, review `.mise.toml` and run
`mise trust .mise.toml` once. After installation, run Maven through `mise exec --` so
the pinned Java 17 and Maven versions are used.

## Maven

### Building Milo

**Using JDK 17**, run this from the project root:

```shell
mise exec -- mvn clean install
```

To maintain compatibility with Java 17 it is recommended that you build using JDK 17, however the library is runtime compatible with versions 17 and later (e.g. JDK 21, JDK 24).

### Releases

Releases are published to Maven Central and snapshots to Sonatype.

#### OPC UA Client SDK

```xml
<dependency>
    <groupId>org.eclipse.milo</groupId>
    <artifactId>milo-sdk-client</artifactId>
    <version>1.1.6</version>
</dependency>
```

#### OPC UA Server SDK

```xml
<dependency>
    <groupId>org.eclipse.milo</groupId>
    <artifactId>milo-sdk-server</artifactId>
    <version>1.1.6</version>
</dependency>
```

The Wiki's server guides cover the [sampling framework](https://github.com/eclipse-milo/milo/wiki/Server-Sampling) and
[access control, roles, and permissions](https://github.com/eclipse-milo/milo/wiki/Server-Access-Control)
(1.2.0 and later).

#### GDS Client (1.2.0 and later)

Registers an application with a Global Discovery Server, requests certificates through the Pull
Model, and reads GDS trust lists. See the [GDS client guide](https://github.com/eclipse-milo/milo/wiki/GDS-Client).

```xml
<dependency>
    <groupId>org.eclipse.milo</groupId>
    <artifactId>milo-sdk-client-gds</artifactId>
    <version>1.2.0-SNAPSHOT</version>
</dependency>
```

Referencing a `SNAPSHOT` release requires the Sonatype snapshot repository be added to your pom file:

```xml
<repository>
    <id>sonatype-snapshots</id>
    <url>https://central.sonatype.com/repository/maven-snapshots/</url>
    <releases><enabled>false</enabled></releases>
    <snapshots><enabled>true</enabled></snapshots>
</repository>
```

## Public Demo Server

An internet-facing instance of this demo server is accessible at
`opc.tcp://milo.digitalpetri.com:62541/milo`.

It accepts both unsecured and secured connections. All incoming client certificates are automatically trusted.

Authenticate anonymously or with one of the following credential pairs:

- `User` / `password`
    - roles: `WellKnownRole_AuthenticatedUser`
- `UserA` / `password`
    - roles: `SiteA_Read`, `SiteA_Write`
- `UserB` / `password`
    - roles: `SiteB_Read`, `SiteB_Write`
- `SiteAdmin` / `password`
    - roles: `SiteA_Read`, `SiteB_Read`
- `SecurityAdmin` / `password`
    - roles: `WellKnownRole_SecurityAdmin`

The code powering the demo server is available here: https://github.com/digitalpetri/opc-ua-demo-server
