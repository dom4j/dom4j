<?xml version='1.0'?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:fo="http://www.w3.org/1999/XSL/Format"
                version='1.0'>

<!-- ********************************************************************
     $Id$
     ********************************************************************

     This file is part of the XSL DocBook Stylesheet distribution.
     See ../README or http://nwalsh.com/docbook/xsl/ for copyright
     and other information.

     ******************************************************************** -->

<xsl:template name="division.toc">
  <xsl:variable name="nodes"
                select="part|reference|preface
                        |chapter|appendix
                        |bibliography|glossary|index"/>
  <xsl:if test="$nodes">
    <fo:block>
      <xsl:call-template name="table.of.contents.titlepage"/>
      <xsl:apply-templates select="$nodes" mode="toc"/>
    </fo:block>
  </xsl:if>
</xsl:template>

<xsl:template name="component.toc">
  <xsl:variable name="nodes" select="section|sect1"/>
  <xsl:if test="$nodes">
    <fo:block>
      <fo:block>
         <fo:inline font-weight="bold">
           <xsl:call-template name="gentext.element.name">
             <xsl:with-param name="element.name">TableofContents</xsl:with-param>
           </xsl:call-template>
         </fo:inline>
       </fo:block>
      <xsl:apply-templates select="$nodes" mode="toc"/>
    </fo:block>
  </xsl:if>
</xsl:template>

<xsl:template match="part" mode="toc">
  <xsl:variable name="id">
    <xsl:call-template name="object.id"/>
  </xsl:variable>

  <fo:block>
    <xsl:apply-templates select="." mode="label.content"/>
    <xsl:apply-templates select="." mode="title.content"/>
    <fo:leader leader-pattern="dots" text-align-last="justify"/>
    <fo:page-number-citation ref-id="{$id}"/>
  </fo:block>

  <xsl:if test="chapter|appendix|preface|reference">
    <fo:block start-indent="2pc">
      <xsl:apply-templates select="chapter|appendix|preface|reference"
                           mode="toc"/>
    </fo:block>
  </xsl:if>
</xsl:template>

<xsl:template match="reference" mode="toc">
  <xsl:variable name="id">
    <xsl:call-template name="object.id"/>
  </xsl:variable>

  <fo:block>
    <xsl:apply-templates select="." mode="label.content"/>
    <xsl:apply-templates select="." mode="title.content"/>
    <fo:leader leader-pattern="dots" text-align-last="justify"/>
    <fo:page-number-citation ref-id="{$id}"/>
  </fo:block>

  <xsl:if test="refentry">
    <fo:block start-indent="2pc">
      <xsl:apply-templates select="refentry" mode="toc"/>
    </fo:block>
  </xsl:if>
</xsl:template>

<xsl:template match="preface|chapter|appendix"
              mode="toc">
  <xsl:variable name="id">
    <xsl:call-template name="object.id"/>
  </xsl:variable>

  <fo:block>
    <xsl:apply-templates select="." mode="label.content"/>
    <xsl:apply-templates select="." mode="title.content"/>
    <fo:leader leader-pattern="dots" text-align-last="justify"/>
    <fo:page-number-citation ref-id="{$id}"/>
  </fo:block>

  <xsl:if test="section|sect1">
    <fo:block start-indent="2pc">
      <xsl:apply-templates select="section|sect1"
                           mode="toc"/>
    </fo:block>
  </xsl:if>
</xsl:template>

<xsl:template match="section|sect1|sect2|sect3|sect4|sect5"
              mode="toc">
  <xsl:variable name="id">
    <xsl:call-template name="object.id"/>
  </xsl:variable>

  <fo:block>
    <xsl:apply-templates select="." mode="label.content"/>
    <xsl:apply-templates select="." mode="title.content"/>
    <fo:leader leader-pattern="dots" text-align-last="justify"/>
    <fo:page-number-citation ref-id="{$id}"/>
  </fo:block>

  <xsl:if test="section|sect2|sect3|sect4|sect5">
    <fo:block start-indent="2pc">
      <xsl:apply-templates select="section|sect2|sect3|sect4|sect5"
                           mode="toc"/>
    </fo:block>
  </xsl:if>
</xsl:template>

<xsl:template match="bibliography|glossary"
              mode="toc">
  <xsl:variable name="id">
    <xsl:call-template name="object.id"/>
  </xsl:variable>

  <fo:block>
    <xsl:apply-templates select="." mode="label.content"/>
    <xsl:apply-templates select="." mode="title.content"/>
    <fo:leader leader-pattern="dots" text-align-last="justify"/>
    <fo:page-number-citation ref-id="{$id}"/>
  </fo:block>
</xsl:template>

<xsl:template match="index"
              mode="toc">
  <xsl:variable name="id">
    <xsl:call-template name="object.id"/>
  </xsl:variable>

  <xsl:if test="* or $generate.index">
    <fo:block>
      <xsl:apply-templates select="." mode="label.content"/>
      <xsl:apply-templates select="." mode="title.content"/>
      <fo:leader leader-pattern="dots" text-align-last="justify"/>
      <fo:page-number-citation ref-id="{$id}"/>
    </fo:block>
  </xsl:if>
</xsl:template>

<xsl:template match="title" mode="toc">
  <xsl:apply-templates/>
</xsl:template>

<!-- ==================================================================== -->

<xsl:template name="list.of.titles">
  <xsl:param name="titles" select="'table'"/>
  <xsl:param name="nodes" select=".//table"/>

  <xsl:if test="$nodes">
    <fo:block>
      <xsl:choose>
        <xsl:when test="$titles='table'">
          <xsl:call-template name="list.of.tables.titlepage"/>
        </xsl:when>
        <xsl:when test="$titles='figure'">
          <xsl:call-template name="list.of.figures.titlepage"/>
        </xsl:when>
        <xsl:when test="$titles='equation'">
          <xsl:call-template name="list.of.equations.titlepage"/>
        </xsl:when>
        <xsl:when test="$titles='example'">
          <xsl:call-template name="list.of.examples.titlepage"/>
        </xsl:when>
        <xsl:otherwise>
          <xsl:call-template name="list.of.unknowns.titlepage"/>
        </xsl:otherwise>
      </xsl:choose>
      <xsl:apply-templates select="$nodes" mode="toc"/>
    </fo:block>
  </xsl:if>
</xsl:template>

<xsl:template match="figure|table|example|equation" mode="toc">
  <xsl:variable name="id">
    <xsl:call-template name="object.id"/>
  </xsl:variable>

  <fo:block>
    <xsl:apply-templates select="." mode="label.content"/>
    <xsl:apply-templates select="." mode="title.content"/>
    <fo:leader leader-pattern="dots" text-align-last="justify"/>
    <fo:page-number-citation ref-id="{$id}"/>
  </fo:block>
</xsl:template>

<!-- ==================================================================== -->

</xsl:stylesheet>

