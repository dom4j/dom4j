<?xml version="1.0" encoding="iso-8859-1"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

  <xsl:output method="html" indent="yes"/>

  <xsl:template match="document">
    <xsl:apply-templates/>
  </xsl:template>

  <xsl:template match="document/properties">

    <table border="0" cellpadding="4" cellspacing="2">

      <xsl:for-each select="author">

        <tr>

          <td valign="top"><b>Author:</b></td>

          <td valign="top">

            <xsl:value-of select="."/>&#xA0;

            <xsl:if test="@email">

              [ <a href="mailto:{@email}"><xsl:value-of select="@email"/></a> ]

            </xsl:if>

<!--

            <xsl:if test="@ldap">

              [ <a href="ldap://{@ldap}">LDAP</a> ]<br/>

            </xsl:if>

 -->

          </td>

        </tr>

      </xsl:for-each>

      <xsl:if test="abstract">

        <tr>

          <td valign="top"><b>Abstract:</b></td>

          <td valign="top"><xsl:value-of select="abstract"/></td>

        </tr>

      </xsl:if>

      <xsl:if test="status">

        <tr>

          <td valign="top"><b>Status:</b></td>

          <td valign="top"><xsl:value-of select="status"/></td>

        </tr>

      </xsl:if>

    </table><br/>

  </xsl:template>
  <!-- Process the document body -->

  <xsl:template match="document/body">

    <xsl:if test="/document/properties/title">

      <br/>

      <h1><xsl:value-of select="/document/properties/title"/></h1>

    </xsl:if>

    <xsl:if test="header">

      <xsl:apply-templates select="header"/>

    </xsl:if>
    <xsl:for-each select=".//section">

      <small>

      <xsl:if test="@title">

        <xsl:variable name="level" select="count(ancestor::*)"/>

        <xsl:choose>

          <xsl:when test='$level=2'>

            <a href="#{@title}"><xsl:value-of select="@title"/></a><br/>

          </xsl:when>

          <xsl:when test='$level=3'>

            &#xA0;&#xA0;&#xA0;&#xA0;<a href="#{@title}"><xsl:value-of select="@title"/></a><br/>

          </xsl:when>

        </xsl:choose>

      </xsl:if>

      </small>

    </xsl:for-each>

    <br/>
    <xsl:apply-templates select="section"/>
    <xsl:if test="footer">

      <br/>

      <xsl:apply-templates select="footer"/>

    </xsl:if>

  </xsl:template>


  <!-- Process a section in the document. Nested sections are supported -->

  <xsl:template match="document//section">

    <xsl:variable name="level" select="count(ancestor::*)"/>

    <xsl:choose>

      <xsl:when test='$level=2'>

        <a name="{@title}"><h2><xsl:value-of select="@title"/></h2></a>

      </xsl:when>

      <xsl:when test='$level=3'>

        <a name="{@title}"><h3><xsl:value-of select="@title"/></h3></a>

      </xsl:when>

      <xsl:when test='$level=4'>

        <a name="{@title}"><h4><xsl:value-of select="@title"/></h4></a>

      </xsl:when>

      <xsl:when test='$level>=5'>

        <h5><xsl:copy-of select="@title"/></h5>

      </xsl:when>

    </xsl:choose>

    <blockquote>

      <xsl:apply-templates/>

    </blockquote>

  </xsl:template>
  <!-- Paragraphs are separated with one empty line -->

  <xsl:template match="p">

    <p><xsl:apply-templates/><br/></p>

  </xsl:template>
  <!-- Paragraphs are separated with one empty line -->

  <xsl:template match="body-note">

    <blockquote><hr size="1" noshadow=""/><xsl:apply-templates/><hr size="1" noshadow=""/></blockquote>

  </xsl:template>
  <xsl:template match="nbsp">

    &#xA0;

  </xsl:template>

  <xsl:template match="url">
    <a href="{.}"><xsl:copy-of select="."/></a>
  </xsl:template>

  <xsl:template match="email">
    <a href="mailto:{.}"><xsl:copy-of select="."/></a>
  </xsl:template>

  <xsl:template match="/">
    <xsl:variable name="project" select="document('../project.xml')/project"/>
    <html lang="en">
    <head>
      <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
      <xsl:element name="META">
        <xsl:attribute name="NAME">Keywords</xsl:attribute>
        <xsl:attribute name="CONTENT">DOM4J, XML, Java, XML and Java, Open Source, 
            XML software, Java software, XML API, Java API, XML parser, 
            DOM, Document Object Model, SAX, XML Library, XPath, 
            Java 2 Collections
        </xsl:attribute>
      </xsl:element>
      <xsl:element name="META">
        <xsl:attribute name="NAME">Description</xsl:attribute>
        <xsl:attribute name="CONTENT">DOM4J is an Open Source XML framework 
            for the Java Platform that combines the best of DOM and SAX 
            together with integrated  XPath and Java 2 Collections support
        </xsl:attribute>
      </xsl:element>
      <xsl:element name="META">
        <xsl:attribute name="NAME">Copyright</xsl:attribute>
        <xsl:attribute name="CONTENT">(c) 2001 MetaStuff Ltd.</xsl:attribute>
      </xsl:element>

      <xsl:choose>
        <xsl:when test="/document/properties/title"><title><xsl:value-of select="/document/body/title"/></title></xsl:when>
        <xsl:when test="/document/body/title"><title><xsl:value-of select="/document/body/title"/></title></xsl:when>
        <xsl:otherwise><title><xsl:value-of select="$project/title"/></title></xsl:otherwise>
      </xsl:choose>
      <link rel="stylesheet" type="text/css" href="default.css"/>
    </head>

    <body>

      <table border="0" cellpadding="0" cellspacing="0" width="100%">
        <tr>
          <td valign="top">          
            <xsl:apply-templates select="$project/links"/>
            <hr/>
          </td>
        </tr>

        <tr>
          <td>
            <h1>
              <xsl:choose>
                <xsl:when test="/document/body/title"><xsl:value-of select="/document/body/title"/></xsl:when>
                <xsl:otherwise><xsl:value-of select="$project/title"/></xsl:otherwise>
              </xsl:choose>
            </h1>
           </td>
        </tr>

        <tr>
          <td>
             <xsl:apply-templates select="document/body"/>
             <br/>
             <hr/>
          </td>
        </tr>

        <tr>
          <td valign="top">
            <xsl:apply-templates select="$project/links"/>
            <hr/>
          </td>
        </tr>

        <xsl:if test="$project/notice">
          <tr>
            <td align="center">
              <xsl:for-each select="$project/notice">
                <small><xsl:copy-of select="."/><br/>&#xA0;<br/></small>
              </xsl:for-each>
            </td>
          </tr>
        </xsl:if>

      </table>

    </body>
    </html>
  </xsl:template>


  <!-- UL is processed into a table using graphical bullets -->
  <xsl:template match="ul">
    <table border="0" cellpadding="2" cellspacing="2">
      <tr><td colspan="2" height="5"></td></tr>
      <xsl:apply-templates/>
    </table>
  </xsl:template>

  <xsl:template match="ul/li">
    <tr>
      <td align="left" valign="top">
        <img src="style/images/blueball.gif" alt="*"/>
     </td>
      <td align="left" valign="top"><xsl:apply-templates/></td>
    </tr>
  </xsl:template>

  <xsl:template match="section">
    <br />
  </xsl:template>

  <xsl:template match="br">
    <br />
  </xsl:template>

  <xsl:template match='@* | node()'>
    <xsl:copy>
      <xsl:apply-templates select='@* | node()'/>
    </xsl:copy>
  </xsl:template>


</xsl:stylesheet>


