<?xml version='1.0'?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                version='1.0'>

<!-- ********************************************************************
     $Id$
     ********************************************************************

     This file is part of the XSL DocBook Stylesheet distribution.
     See ../README or http://nwalsh.com/docbook/xsl/ for copyright
     and other information.

     ******************************************************************** -->

<!-- ==================================================================== -->

<xsl:template match="section">
  <xsl:variable name="id">
    <xsl:call-template name="object.id"/>
  </xsl:variable>

  <div class="{name(.)}">
    <a name="{$id}"/>
    <xsl:call-template name="section.titlepage"/>
    <xsl:if test="$generate.section.toc != '0'
                  or refentry">
      <xsl:call-template name="section.toc"/>
    </xsl:if>
    <xsl:apply-templates/>
  </div>
</xsl:template>

<xsl:template match="title" mode="section.titlepage.recto.mode">
  <xsl:variable name="section" select="(ancestor::section
                                        |ancestor::simplesect
                                        |ancestor::sect1
                                        |ancestor::sect2
                                        |ancestor::sect3
                                        |ancestor::sect4
                                        |ancestor::sect5)[last()]"/>
  <xsl:call-template name="section.heading">
    <xsl:with-param name="section" select="$section"/>
    <xsl:with-param name="level">
      <xsl:call-template name="section.level">
        <xsl:with-param name="node" select="$section"/>
      </xsl:call-template>
    </xsl:with-param>
    <xsl:with-param name="title">
      <xsl:apply-templates select="$section" mode="title.ref">
        <xsl:with-param name="label-wrapper" select="'span'"/>
        <xsl:with-param name="label-wrapper-class" select="'label'"/>
        <xsl:with-param name="title-wrapper" select="'span'"/>
        <xsl:with-param name="title-wrapper-class" select="'title'"/>
        <xsl:with-param name="allow-anchors" select="'1'"/>
      </xsl:apply-templates>
    </xsl:with-param>
  </xsl:call-template>
</xsl:template>

<xsl:template match="sect1">
  <xsl:variable name="id">
    <xsl:call-template name="object.id"/>
  </xsl:variable>

  <div class="{name(.)}">
    <a name="{$id}"/>
    <xsl:call-template name="sect1.titlepage"/>
    <xsl:if test="$generate.section.toc != '0'
                  or refentry">
      <xsl:call-template name="section.toc"/>
    </xsl:if>
    <xsl:apply-templates/>
  </div>
</xsl:template>

<xsl:template match="title" mode="sect1.titlepage.recto.mode">
  <xsl:apply-templates select="." mode="section.titlepage.recto.mode"/>
</xsl:template>

<xsl:template match="sect2">
  <xsl:variable name="id">
    <xsl:call-template name="object.id"/>
  </xsl:variable>

  <div class="{name(.)}">
    <a name="{$id}"/>
    <xsl:call-template name="sect2.titlepage"/>
    <xsl:if test="$generate.section.toc != '0'
                  or refentry">
      <xsl:call-template name="section.toc"/>
    </xsl:if>
    <xsl:apply-templates/>
  </div>
</xsl:template>

<xsl:template match="title" mode="sect2.titlepage.recto.mode">
  <xsl:apply-templates select="." mode="section.titlepage.recto.mode"/>
</xsl:template>

<xsl:template match="sect3">
  <xsl:variable name="id">
    <xsl:call-template name="object.id"/>
  </xsl:variable>

  <div class="{name(.)}">
    <a name="{$id}"/>
    <xsl:call-template name="sect3.titlepage"/>

    <xsl:if test="$generate.section.toc != '0'
                  or refentry">
      <xsl:call-template name="section.toc"/>
    </xsl:if>
    <xsl:apply-templates/>
  </div>
</xsl:template>

<xsl:template match="title" mode="sect3.titlepage.recto.mode">
  <xsl:apply-templates select="." mode="section.titlepage.recto.mode"/>
</xsl:template>

<xsl:template match="sect4">
  <xsl:variable name="id">
    <xsl:call-template name="object.id"/>
  </xsl:variable>

  <div class="{name(.)}">
    <a name="{$id}"/>
    <xsl:call-template name="sect4.titlepage"/>
    <xsl:if test="$generate.section.toc != '0'
                  or refentry">
      <xsl:call-template name="section.toc"/>
    </xsl:if>
    <xsl:apply-templates/>
  </div>
</xsl:template>

<xsl:template match="title" mode="sect4.titlepage.recto.mode">
  <xsl:apply-templates select="." mode="section.titlepage.recto.mode"/>
</xsl:template>

<xsl:template match="sect5">
  <xsl:variable name="id">
    <xsl:call-template name="object.id"/>
  </xsl:variable>

  <div class="{name(.)}">
    <a name="{$id}"/>
    <xsl:call-template name="sect5.titlepage"/>
    <xsl:if test="$generate.section.toc != '0'
                  or refentry">
      <xsl:call-template name="section.toc"/>
    </xsl:if>
    <xsl:apply-templates/>
  </div>
</xsl:template>

<xsl:template match="title" mode="sect5.titlepage.recto.mode">
  <xsl:apply-templates select="." mode="section.titlepage.recto.mode"/>
</xsl:template>

<xsl:template match="simplesect">
  <xsl:variable name="id">
    <xsl:call-template name="object.id"/>
  </xsl:variable>

  <div class="{name(.)}">
    <a name="{$id}"/>
    <xsl:call-template name="simplesect.titlepage"/>
    <xsl:apply-templates/>
  </div>
</xsl:template>

<xsl:template match="title" mode="simplesect.titlepage.recto.mode">
  <xsl:apply-templates select="." mode="section.titlepage.recto.mode"/>
</xsl:template>

<xsl:template match="section/title"></xsl:template>
<xsl:template match="section/subtitle"></xsl:template>
<xsl:template match="sectioninfo"></xsl:template>

<xsl:template match="sect1/title"></xsl:template>
<xsl:template match="sect1/subtitle"></xsl:template>
<xsl:template match="sect1info"></xsl:template>

<xsl:template match="sect2/title"></xsl:template>
<xsl:template match="sect2/subtitle"></xsl:template>
<xsl:template match="sect2info"></xsl:template>

<xsl:template match="sect3/title"></xsl:template>
<xsl:template match="sect3/subtitle"></xsl:template>
<xsl:template match="sect3info"></xsl:template>

<xsl:template match="sect4/title"></xsl:template>
<xsl:template match="sect4/subtitle"></xsl:template>
<xsl:template match="sect4info"></xsl:template>

<xsl:template match="sect5/title"></xsl:template>
<xsl:template match="sect5/subtitle"></xsl:template>
<xsl:template match="sect5info"></xsl:template>

<xsl:template match="simplesect/title"></xsl:template>
<xsl:template match="simplesect/subtitle"></xsl:template>

<!-- ==================================================================== -->

<xsl:template name="section.heading">
  <xsl:param name="section" select="."/>
  <xsl:param name="level" select="'1'"/>
  <xsl:param name="title"/>
  <xsl:element name="h{$level}">
    <xsl:attribute name="class">title</xsl:attribute>
    <xsl:if test="$css.decoration != '0'">
      <xsl:if test="$level&lt;3">
        <xsl:attribute name="style">clear: all</xsl:attribute>
      </xsl:if>
    </xsl:if>
    <a>
      <xsl:attribute name="name">
        <xsl:call-template name="object.id">
          <xsl:with-param name="object" select="$section"/>
        </xsl:call-template>
      </xsl:attribute>
    </a>
    <xsl:copy-of select="$title"/>
  </xsl:element>
</xsl:template>

<!-- ==================================================================== -->

<xsl:template match="bridgehead">
  <!-- need to calculate depth! -->
  <h3><xsl:apply-templates/></h3>
</xsl:template>

</xsl:stylesheet>

