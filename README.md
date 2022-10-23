# Rick and Morty API Java Client
[![Java CI with Gradle](https://github.com/adrianoluis/rickandmortyapi-java/actions/workflows/build.yml/badge.svg)](https://github.com/adrianoluis/rickandmortyapi-java/actions/workflows/build.yml) [![Coverage Status](https://codecov.io/gh/adrianoluis/rickandmortyapi-java/branch/main/graph/badge.svg?token=CVOIQ4OMVZ)](https://codecov.io/gh/adrianoluis/rickandmortyapi-java)

Java Client for Rick And Morty knowledge base API: https://rickandmortyapi.com/

## Support
If you have any problem or suggestion please open an issue [here](https://github.com/adrianoluis/rickandmortyapi-java/issues).

## Usage

Simply put the following snippet into your proper build config:

#### Apache Maven

##### Repository
```
<repositories>
    <repository>
        <id>rickandmortyapi-java-mvn-repo</id>
        <url>https://raw.githubusercontent.com/adrianoluis/rickandmortyapi-java/mvn-repo/</url>
        <releases>
            <enabled>true</enabled>
            <updatePolicy>never</updatePolicy>
        </releases>
    </repository>
</repositories>
```

##### Dependency
```
<dependency>
    <groupId>net.adrianoluis</groupId>
    <artifactId>rickandmortyapi-java</artifactId>
    <version>1.1.1</version>
</dependency>
```

#### Gradle/Grails

##### Repository
```
repositories {
    maven { url 'https://raw.githubusercontent.com/adrianoluis/rickandmortyapi-java/mvn-repo' }
}
```

##### Dependency
```
compile 'net.adrianoluis:rickandmortyapi-java:1.1.1'
```

#### Apache Buildr

##### Dependency
```
'net.adrianoluis:rickandmortyapi-java:jar:1.1.1'
```

#### Apache Ivy

##### Dependency
```
<dependency org="net.adrianoluis" name="rickandmortyapi-java" rev="1.1.1">
    <artifact name="rickandmortyapi-java" type="jar" />
</dependency>
```

#### Groovy Grape

##### Dependency
```
@Grapes(
  @Grab(group='net.adrianoluis', module='rickandmortyapi-java', version='1.1.1')
)
```

#### Scala SBT

##### Dependency
```
libraryDependencies += "net.adrianoluis" % "rickandmortyapi-java" % "1.1.1"
```

#### Leiningen

##### Dependency
```
[net.adrianoluis/rickandmortyapi-java "1.1.1"]
```

## License

Check [here](LICENSE).
