<?xml version='1.0'?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:verb5="http://nwalsh.com/com.nwalsh.saxon.Verbatim"
                xmlns:verb6="http://nwalsh.com/com.nwalsh.saxon6.Verbatim"
                exclude-result-prefixes="verb5 verb6"
                version='1.0'>

<!-- ********************************************************************
     $Id$
     ********************************************************************

     This file is part of the XSL DocBook Stylesheet distribution.
     See ../README or http://nwalsh.com/docbook/xsl/ for copyright
     and other information.

     ******************************************************************** -->

<xsl:template match="programlistingco|screenco">
  <xsl:variable name="verbatim" select="programlisting|screen"/>
  <xsl:variable name="vendor" select="system-property('xsl:vendor')"/>

  <xsl:choose>
    <xsl:when test="$saxon.extensions != '0'
                    and $saxon.callouts != '0'">
      <xsl:variable name="rtf">
        <xsl:apply-templates select="$verbatim">
          <xsl:with-param name="suppress-numbers" select="'1'"/>
        </xsl:apply-templates>
      </xsl:variable>
      <xsl:variable name="rtf-with-callouts">
        <xsl:choose>
          <xsl:when test="contains($vendor, 'SAXON 6')">
            <xsl:copy-of select="verb6:insertCallouts(areaspec,$rtf)"/>
          </xsl:when>
          <xsl:when test="$callout.graphics != 0">
            <xsl:copy-of select="verb5:insertCallouts(areaspec,$rtf,
                                 $callout.defaultcolumn,
                                 $callout.graphics.path,
                                 $callout.graphics.extension,
                                 $callout.graphics.number.limit,
                                 false())"/>
          </xsl:when>
          <xsl:otherwise>
            <xsl:copy-of select="verb5:insertCallouts(areaspec,$rtf,
                                 $callout.defaultcolumn)"/>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:variable>

      <xsl:choose>
        <xsl:when test="$verbatim/@linenumbering = 'numbered'
                        and $saxon.extensions != '0'
                        and $saxon.linenumbering != '0'">
          <div class="{name(.)}">
            <xsl:choose>
              <xsl:when test="contains($vendor, 'SAXON 6')">
                <xsl:copy-of select="verb6:numberLines($rtf-with-callouts)"/>
              </xsl:when>
              <xsl:otherwise>
                <xsl:copy-of select="verb5:numberLines($rtf-with-callouts,
                                     $linenumbering.everyNth,
                                     $linenumbering.width,
                                     $linenumbering.separator)"/>
              </xsl:otherwise>
            </xsl:choose>
            <xsl:apply-templates select="calloutlist"/>
          </div>
        </xsl:when>
        <xsl:otherwise>
          <div class="{name(.)}">
            <xsl:copy-of select="$rtf-with-callouts"/>
            <xsl:apply-templates select="calloutlist"/>
          </div>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:when>
    <xsl:otherwise>
      <div class="{name(.)}">
        <xsl:apply-templates/>
      </div>
    </xsl:otherwise>
  </xsl:choose>
</xsl:template>

<xsl:template match="areaspec|areaset|area">
</xsl:template>

<xsl:template match="areaset" mode="conumber">
  <xsl:number count="area|areaset" format="1"/>
</xsl:template>

<xsl:template match="area" mode="conumber">
  <xsl:number count="area|areaset" format="1"/>
</xsl:template>

<xsl:template match="co">
  <a name="{@id}"/>
  <xsl:apply-templates select="." mode="callout-bug"/>
</xsl:template>

<xsl:template match="co" mode="callout-bug">
  <xsl:call-template name="callout-bug">
    <xsl:with-param name="conum">
      <xsl:number count="co" format="1"/>
    </xsl:with-param>
  </xsl:call-template>
</xsl:template>

<xsl:template name="callout-bug">
  <xsl:param name="conum" select='1'/>

  <xsl:choose>
    <xsl:when test="$callout.graphics = '0'
                    or $conum > $callout.graphics.number.limit">

      <xsl:text>(</xsl:text>
      <xsl:value-of select="$conum"/>
      <xsl:text>)</xsl:text>
    </xsl:when>
    <xsl:otherwise>
      <img src="{$callout.graphics.path}{$conum}{$callout.graphics.extension}"
           alt="{$conum}" border="0"/>
    </xsl:otherwise>
  </xsl:choose>
</xsl:template>

</xsl:stylesheet>
