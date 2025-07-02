![Maven Central Version](https://img.shields.io/maven-central/v/org.dom4j/dom4j)
![GitHub Release](https://img.shields.io/github/v/release/dom4j/dom4j)
[![Javadocs](https://javadoc.io/badge/org.dom4j/dom4j.svg)](https://javadoc.io/doc/org.dom4j/dom4j)

# dom4j

`dom4j` is an open source framework for processing XML which is integrated with XPath and fully supports DOM, SAX, JAXP and the Java platform such as Java 2 Collections.

See https://dom4j.github.io for details.

## Optional dependencies
If you use some optional dependency of dom4j (for example, Jaxen, xsdlib etc.), you need to specify an explicit dependency on it in your project. They are no longer marked as a mandatory transitive dependency by dom4j.

If you are using Gradle 6+ then you may declare a dependency on these features like this:

```
// Current feature names: jaxen, xsdlib, jaxb, pullParser, xpp
implementation("org.dom4j:dom4j:${dom4jVersion}") {
  capabilities {
     requireCapability('org.dom4j:dom4j-jaxen-support')
  }
}
```
