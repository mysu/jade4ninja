# Jade4Ninja
Jade4Ninja(beta) give support for Jade4J for Java templating

Basic functionalities

## How to use

* Add repository

```HTML
    <repositories>
      <repository>
        <id>jade4ninja-repo</id>
        <url>https://raw.github.com/emiguelt/jade4ninja/mvn-repo/</url>
        <snapshots>
          <enabled>true</enabled>
          <updatePolicy>always</updatePolicy>
        </snapshots>
      </repository>
    <repositories>
```

* Add dependency in pom.xml

```HTML
    <dependency>
      <groupId>org.ninjaframework</groupId>
        <artifactId>jade4ninja</artifactId>
        <version>0.1.0</version>
        <exclusions>
          <exclusion>
            <artifactId>commons-logging</artifactId>
            <groupId>commons-logging</groupId>
          </exclusion>
        </exclusions>
    </dependency>
```

* Add the Jade files in _views_ path in the same way that _.ftl.html_ files

* See a full usage example in _jade4ninja-demo_


## Reference

* NinjaFramework: www.ninjaframework.org
* Jade4J: https://github.com/neuland/jade4j
