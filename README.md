[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.dom4j/dom4j/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.dom4j/dom4j)
[![codecov.io](https://codecov.io/github/dom4j/dom4j/coverage.svg?branch=master)](https://codecov.io/github/dom4j/dom4j?branch=master)
[![Build Status](https://travis-ci.org/dom4j/dom4j.svg?branch=master)](https://travis-ci.org/dom4j/dom4j)
[![Javadocs](https://javadoc.io/badge/org.dom4j/dom4j.svg)](https://javadoc.io/doc/org.dom4j/dom4j)

# dom4j

`dom4j` is an open source framework for processing XML which is integrated with XPath and fully supports DOM, SAX, JAXP and the Java platform such as Java 2 Collections.

# News

## Version 2.0.3 and 2.1.3 released

(Version 2.1.2 has been skipped.)

### Improvements
* Added new factory method `org.dom4j.io.SAXReader.createDefault()`. It hase more secure defaults than `new SAXReader()`, which uses system
 `XMLReaderFactory.createXMLReader()` or `SAXParserFactory.newInstance().newSAXParser()`. `SAXReader.createDefault()` disable parsing of external entities
  in the SAX parser.

## Version 2.1.1 released
Bug fix release.

### Potential breaking changes
* If you use some optional dependency of dom4j (for example Jaxen, xsdlib etc.), you need to specify an explicit dependency on it in your project. They are no longer marked as a mandatory transitive dependency by dom4j.

### Fixed issues
* #28 Possible vulnerability of `DocumentHelper.parseText()` to XML injection (reported by @s0m30ne)
* #34 CVS directories left in the source tree (reported by @ebourg)
* #38 XMLWriter does not escape supplementary unicode characters correctly (reported by @abenkovskii)
* #39 writer.writeOpen(x) doesn't write namespaces (reported by @borissmidt)
* #40 concurrency problem with `QNameCache`  (@jbennett2091)
* #43 and #46 all dependencies are optional (reported by @Zardoz89 and @vmassol)
* #44 SAXReader: hardcoded namespace features (reported by @philippeu)
* #48 validate `QName`s (reported by @mario-areias)
