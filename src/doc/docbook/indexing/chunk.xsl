<?xml version='1.0'?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:doc="http://nwalsh.com/xsl/documentation/1.0"
                exclude-result-prefixes="doc"
                version='1.0'>

<xsl:import href="../html/chunk.xsl"/>

<xsl:output method="text"/>

<!-- ********************************************************************
     $Id$
     ********************************************************************

     This file is part of the XSL DocBook Stylesheet distribution.
     See ../README or http://nwalsh.com/docbook/xsl/ for copyright
     and other information.

     ******************************************************************** -->

<!-- ==================================================================== -->

<xsl:template match="text()">
  <!-- suppress -->
</xsl:template>

<xsl:template match="*">
  <xsl:apply-templates/>
</xsl:template>

<!-- ==================================================================== -->

<xsl:template match="indexterm">
  <xsl:variable name="id">
    <xsl:call-template name="object.id"/>
  </xsl:variable>

  <xsl:variable name="chunk-file">
    <xsl:apply-templates select="." mode="chunk-filename"/>
  </xsl:variable>

  <xsl:variable name="target-section"
                select="(ancestor::appendix
                         |ancestor::article
                         |ancestor::bibliography
                         |ancestor::book
                         |ancestor::chapter
                         |ancestor::colophon
                         |ancestor::glossary
                         |ancestor::index
                         |ancestor::part
                         |ancestor::preface
                         |ancestor::refentry
                         |ancestor::reference
                         |ancestor::refsect1
                         |ancestor::refsect2
                         |ancestor::refsect3
                         |ancestor::sect1
                         |ancestor::sect2
                         |ancestor::sect3
                         |ancestor::sect4
                         |ancestor::sect5
                         |ancestor::section
                         |ancestor::setindex
                         |ancestor::simplesect)[last()]"/>

  <xsl:text>INDEXTERM </xsl:text>
  <xsl:value-of select="$chunk-file"/>
  <xsl:text>&#13;&#10;</xsl:text>

  <xsl:text>INDEXPOINT </xsl:text>
  <xsl:value-of select="$chunk-file"/>
  <xsl:text>#</xsl:text>
  <xsl:value-of select="$id"/>
  <xsl:text>&#13;&#10;</xsl:text>

  <xsl:text>TITLE </xsl:text>
  <xsl:apply-templates select="$target-section" mode="title.content">
    <xsl:with-param name="text-only" select="true()"/>
  </xsl:apply-templates>
  <xsl:text>&#13;&#10;</xsl:text>

  <xsl:text>significance </xsl:text>
  <xsl:value-of select="@significance"/>
  <xsl:text>&#13;&#10;</xsl:text>

  <xsl:if test="@zone">
    <xsl:call-template name="process-zones">
      <xsl:with-param name="zones" select="@zone"/>
    </xsl:call-template>
  </xsl:if>

  <xsl:apply-templates/>

  <xsl:text>/INDEXTERM</xsl:text>
  <xsl:text>&#13;&#10;</xsl:text>
</xsl:template>

<xsl:template match="primary|secondary|tertiary|see|seealso">
  <xsl:value-of select="local-name(.)"/>
  <xsl:if test="@sortas">
    <xsl:text>[</xsl:text>
    <xsl:value-of select="@sortas"/>
    <xsl:text>]</xsl:text>
  </xsl:if>
  <xsl:text> </xsl:text>
  <xsl:value-of select="text()"/>
  <xsl:text>&#13;&#10;</xsl:text>
</xsl:template>

<xsl:template name="process-zones">
  <xsl:param name="zones"></xsl:param>

  <xsl:choose>
    <xsl:when test="$zones = ''"></xsl:when>
    <xsl:when test="substring-before($zones, ' ') != ''">
      <xsl:call-template name="process-zone">
        <xsl:with-param name="zone" select="substring-before($zones, ' ')"/>
      </xsl:call-template>
      <xsl:call-template name="process-zones">
        <xsl:with-param name="zones" select="substring-after($zones, ' ')"/>
      </xsl:call-template>
    </xsl:when>
    <xsl:otherwise>
      <xsl:call-template name="process-zone">
        <xsl:with-param name="zone" select="$zones"/>
      </xsl:call-template>
    </xsl:otherwise>
  </xsl:choose>
</xsl:template>

<xsl:template name="process-zone">
  <xsl:param name="zone"></xsl:param>
  <xsl:variable name="target" select="id($zone)"/>
  <xsl:variable name="chunk-file">
    <xsl:apply-templates select="$target" mode="chunk-filename"/>
  </xsl:variable>

  <xsl:variable name="target-section"
                select="($target/ancestor-or-self::appendix
                         |$target/ancestor-or-self::article
                         |$target/ancestor-or-self::bibliography
                         |$target/ancestor-or-self::book
                         |$target/ancestor-or-self::chapter
                         |$target/ancestor-or-self::colophon
                         |$target/ancestor-or-self::glossary
                         |$target/ancestor-or-self::index
                         |$target/ancestor-or-self::part
                         |$target/ancestor-or-self::preface
                         |$target/ancestor-or-self::refentry
                         |$target/ancestor-or-self::reference
                         |$target/ancestor-or-self::refsect1
                         |$target/ancestor-or-self::refsect2
                         |$target/ancestor-or-self::refsect3
                         |$target/ancestor-or-self::sect1
                         |$target/ancestor-or-self::sect2
                         |$target/ancestor-or-self::sect3
                         |$target/ancestor-or-self::sect4
                         |$target/ancestor-or-self::sect5
                         |$target/ancestor-or-self::section
                         |$target/ancestor-or-self::setindex
                         |$target/ancestor-or-self::simplesect)[last()]"/>

  <xsl:text>ZONE </xsl:text>
  <xsl:value-of select="$chunk-file"/>
  <xsl:text>&#13;&#10;</xsl:text>
  <xsl:text>TITLE </xsl:text>
  <xsl:apply-templates select="$target-section" mode="title.content">
    <xsl:with-param name="text-only" select="true()"/>
  </xsl:apply-templates>
  <xsl:text>&#13;&#10;</xsl:text>
</xsl:template>

<!-- ==================================================================== -->

</xsl:stylesheet>
