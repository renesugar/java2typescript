# Java To TypeScript transpiler

This project is a transpiler able to transform your Java codebase to TypeScript then to JavaScript to ultimatly run Java code in the browser.

### What is for ?

The goal of J2TS is to share codebase between Java and TypeScript and Javascript.
This is especially suitable to share domain classes that define data structures or complex algorithms that have to be maintain and shared.

### What is not for ?

The goal of J2TS is not to code UI through Java code, for this there is better project.
J2TS focus on performance and readability of the generated TypeScript code.

### Getting Started

The easiest way to get started with J2TS is the maven plugin.
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

To override this value, the value configuration help you:

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
