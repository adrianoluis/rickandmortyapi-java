# Rick and Morty API Java Client
[![Build Status](https://travis-ci.org/adrianoluis/rickandmortyapi-java.svg?branch=master)](https://travis-ci.org/adrianoluis/rickandmortyapi-java) [![Coverage Status](https://coveralls.io/repos/github/adrianoluis/rickandmortyapi-java/badge.svg?branch=master)](https://coveralls.io/github/adrianoluis/rickandmortyapi-java?branch=master)

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
    <version>1.0.0</version>
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
compile 'net.adrianoluis:rickandmortyapi-java:1.0.0'
```

#### Apache Buildr

##### Dependency
```
'net.adrianoluis:rickandmortyapi-java:jar:1.0.0'
```

#### Apache Ivy

##### Dependency
```
<dependency org="net.adrianoluis" name="rickandmortyapi-java" rev="1.0.0">
    <artifact name="rickandmortyapi-java" type="jar" />
</dependency>
```

#### Groovy Grape

##### Dependency
```
@Grapes(
  @Grab(group='net.adrianoluis', module='rickandmortyapi-java', version='1.0.0')
)
```

#### Scala SBT

##### Dependency
```
libraryDependencies += "net.adrianoluis" % "rickandmortyapi-java" % "1.0.0"
```

#### Leiningen

##### Dependency
```
[net.adrianoluis/rickandmortyapi-java "1.0.0"]
```

## License

Check [here](LICENSE).
