# Eclipse Milo
[![Build status](https://travis-ci.org/eclipse/milo.svg?branch=master)](https://travis-ci.org/eclipse/milo)
[![Maven Central](https://img.shields.io/maven-central/v/org.eclipse.milo/milo.svg)](https://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22org.eclipse.milo%22%20AND%20a%3A%22milo%22)
[![Join the chat at https://gitter.im/eclipse/milo](https://badges.gitter.im/eclipse/milo.svg)](https://gitter.im/eclipse/milo?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

Milo is an open-source implementation of OPC UA. It includes a high-performance stack (channels, serialization, data structures, security) as well as client and server SDKs built on top of the stack.

While this project has existed for some time, it is new to the Eclipse foundation and is therefore considered to be in [incubation](https://eclipse.org/projects/dev_process/development_process.php#6_2_3_Incubation). While in incubation it will continue to use `0.x.x` versions.

Stack Overflow tag: [milo](http://stackoverflow.com/questions/tagged/milo)

Mailing list: https://dev.eclipse.org/mailman/listinfo/milo-dev


## Maven

### Building Milo

From the project root, run `mvn clean install`.

### Releases

Releases are published to Maven Central. 

#### OPC UA Client SDK

```xml
<dependency>
    <groupId>org.eclipse.milo</groupId>
    <artifactId>sdk-client</artifactId>
    <version>0.2.1</version>
</dependency>
```

#### OPC UA Server SDK

```xml
<dependency>
    <groupId>org.eclipse.milo</groupId>
    <artifactId>sdk-server</artifactId>
    <version>0.2.1</version>
</dependency>
```

#### OPC UA Stack

```xml
<dependency>
    <groupId>org.eclipse.milo</groupId>
    <artifactId>stack-client</artifactId>
    <version>0.2.1</version>
</dependency>
```

```xml
<dependency>
    <groupId>org.eclipse.milo</groupId>
    <artifactId>stack-server</artifactId>
    <version>0.2.1</version>
</dependency>
```

If you want to reference a `SNAPSHOT` release a reference to the Sonatype snapshot repository must be added to your pom file:

```xml
<repository>
    <id>oss-sonatype</id>
    <name>oss-sonatype</name>
    <url>https://oss.sonatype.org/content/repositories/snapshots/</url>
</repository>
```

