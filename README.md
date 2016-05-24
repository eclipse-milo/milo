# Eclipse Milo

[![Join the chat at https://gitter.im/eclipse/milo](https://badges.gitter.im/eclipse/milo.svg)](https://gitter.im/eclipse/milo?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)
Milo is an open-source implementation of OPC-UA. It includes a high-performance stack (channels, serialization, data structures, security) as well as client and server SDKs built on top of the stack.

While this project has existed for some time, it is new to the Eclipse foundation and is therefore considered to be in [incubation](https://eclipse.org/projects/dev_process/development_process.php#6_2_3_Incubation). While in incubation it will continue to use `0.x.x` versions. An initial release and graduation from the incubation phase is planned for the end of this year, after the OPC interop event in Nuremberg.

## Maven

Snapshots and releases are available using Maven. In order to use snapshots a reference to the Sonatype snapshot repository must be added to your pom file:

```xml
<repository>
    <id>oss-sonatype</id>
    <name>oss-sonatype</name>
    <url>https://oss.sonatype.org/content/repositories/snapshots/</url>
</repository>
```

### OPC-UA Client SDK

```xml
<dependency>
    <groupId>org.eclipse.milo</groupId>
    <artifactId>sdk-client</artifactId>
    <version>0.1.0-SNAPSHOT</version>
</dependency>
```

### OPC-UA Server SDK

```xml
<dependency>
    <groupId>org.eclipse.milo</groupId>
    <artifactId>sdk-server</artifactId>
    <version>0.1.0-SNAPSHOT</version>
</dependency>
```

### OPC-UA Stack

```xml
<dependency>
    <groupId>org.eclipse.milo</groupId>
    <artifactId>stack-client</artifactId>
    <version>0.1.0-SNAPSHOT</version>
</dependency>
```

```xml
<dependency>
    <groupId>org.eclipse.milo</groupId>
    <artifactId>stack-server</artifactId>
    <version>0.1.0-SNAPSHOT</version>
</dependency>
```
