# Java To TypeScript Transpiler

This project is a transpiler able to transform your Java codebase to TypeScript and then to JavaScript to ultimatly run Java code in the browser.

### What is it for ?

The goal of J2TS is to share a codebase between Java, TypeScript and Javascript.
This is especially helpful for sharing domain classes, data structures, and complex algorithms.

### What is it not for ?

The goal of J2TS is not to develop a UI in Java. There are better projects for this.
J2TS focus on performance and readability of the generated TypeScript code.

### Getting Started

The easiest way to get started with J2TS is using the maven plugin.
Hereafter is a simple snippet of usage:

```xml
<plugin>
    <groupId>org.kevoree.modeling.j2ts</groupId>
    <artifactId>mavenplugin</artifactId>
    <version>${j2ts.version}</version>
    <executions>
        <execution>
            <id>transpile</id>
            <goals>
                <goal>java2ts</goal>
            </goals>
            <configuration>
                <name>sample</name>
                <copyJRE>true</copyJRE>
            </configuration>
        </execution>
    </executions>
</plugin>
```

By default the Java source directory is ```src/main/java``` and the generated-sources is ```target/generated-sources/java2ts```.

These values can be overriden as follows:

```xml
<source>${basedir}/../api/src/main/java</source>
<target>${basedir}/target/generated-sources</target>
```

### Sample project

A full sample project is hosted [here](https://github.com/kevoree-modeling/java2typescript/raw/master/sample.zip)

This project can be compiled through the following command
```sh
mvn clean install
```

Then go to the ```target/classes``` directory and open the ```index.html``` in you browser to see how to use the generated code.
