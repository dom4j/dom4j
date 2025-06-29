[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.dom4j/dom4j/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.dom4j/dom4j)
[![codecov.io](https://codecov.io/github/dom4j/dom4j/coverage.svg?branch=master)](https://codecov.io/github/dom4j/dom4j?branch=master)
[![Javadocs](https://javadoc.io/badge/org.dom4j/dom4j.svg)](https://javadoc.io/doc/org.dom4j/dom4j)

# dom4j

`dom4j` is an open source framework for processing XML which is integrated with XPath and fully supports DOM, SAX, JAXP and the Java platform such as Java 2 Collections.

See https://dom4j.github.io for details.

## Optional dependencies
If you use some optional dependency of dom4j (for example, Jaxen, xsdlib etc.), you need to specify an explicit dependency on it in your project. They are no longer marked as a mandatory transitive dependency by dom4j.

If you are using Gradle 6+ then you may declare a dependency on these features like this:

```
// Current feature names: jaxen, stax, xsdlib, jaxb, pullParser, xpp
implementation("org.dom4j:dom4j:${dom4jVersion}") {
  capabilities {
     requireCapability('org.dom4j:dom4j-jaxen-support')
  }
}
```
