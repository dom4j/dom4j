<?xml version="1.0" encoding="ISO-8859-1"?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

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


</xsl:stylesheet>


