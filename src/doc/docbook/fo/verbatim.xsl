<?xml version='1.0'?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:fo="http://www.w3.org/1999/XSL/Format"
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

<xsl:template match="programlisting|screen">
  <xsl:param name="suppress-numbers" select="'0'"/>
  <xsl:variable name="vendor" select="system-property('xsl:vendor')"/>

  <!-- Obey the <?dbfo linenumbering.everyNth="x"?> PI -->
  <xsl:variable name="default.linenumbering.everyNth"
                select="$linenumbering.everyNth"/>

  <xsl:variable name="pi.linenumbering.everyNth">
    <xsl:call-template name="dbfo-attribute">
      <xsl:with-param name="attribute" select="'everyNth'"/>
    </xsl:call-template>
  </xsl:variable>

  <xsl:variable name="linenumbering.everyNth">
    <xsl:choose>
      <xsl:when test="$pi.linenumbering.everyNth != ''">
        <xsl:value-of select="$pi.linenumbering.everyNth"/>
      </xsl:when>
      <xsl:otherwise>
        <xsl:value-of select="$default.linenumbering.everyNth"/>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:variable>

  <!-- Obey the <?dbfo linenumbering.separator="x"?> PI -->
  <xsl:variable name="default.linenumbering.separator"
                select="$linenumbering.separator"/>

  <xsl:variable name="pi.linenumbering.separator">
    <xsl:call-template name="dbfo-attribute">
      <xsl:with-param name="attribute" select="'linenumbering.separator'"/>
    </xsl:call-template>
  </xsl:variable>

  <xsl:variable name="linenumbering.separator">
    <xsl:choose>
      <xsl:when test="$pi.linenumbering.separator != ''">
        <xsl:value-of select="$pi.linenumbering.separator"/>
      </xsl:when>
      <xsl:otherwise>
        <xsl:value-of select="$default.linenumbering.separator"/>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:variable>

  <xsl:choose>
    <xsl:when test="$suppress-numbers = '0'
                    and @linenumbering = 'numbered'
                    and $saxon.extensions != '0'
                    and $saxon.linenumbering != '0'">
      <xsl:variable name="rtf">
        <xsl:apply-templates/>
      </xsl:variable>
      <fo:block wrap-option='no-wrap'
                text-align='start'
                white-space-collapse='false'
                linefeed-treatment="preserve"
                font-family='{$monospace.font.family}'
                space-before.minimum="0.8em"
                space-before.optimum="1em"
                space-before.maximum="1.2em">
        <xsl:choose>
          <xsl:when test="contains($vendor, 'SAXON 6')">
            <xsl:copy-of select="verb6:numberLines($rtf)"/>
          </xsl:when>
          <xsl:otherwise>
            <xsl:copy-of select="verb5:numberLines($rtf,
                                 $linenumbering.everyNth,
                                 $linenumbering.width,
                                 $linenumbering.separator)"/>
          </xsl:otherwise>
        </xsl:choose>
      </fo:block>
    </xsl:when>
    <xsl:otherwise>
      <fo:block wrap-option='no-wrap'
                text-align='start'
                white-space-collapse='false'
                linefeed-treatment="preserve"
                font-family='{$monospace.font.family}'
                space-before.minimum="0.8em"
                space-before.optimum="1em"
                space-before.maximum="1.2em">
        <xsl:apply-templates/>
      </fo:block>
    </xsl:otherwise>
  </xsl:choose>
</xsl:template>

<xsl:template match="literallayout">
  <xsl:param name="suppress-numbers" select="'0'"/>
  <xsl:variable name="vendor" select="system-property('xsl:vendor')"/>

  <xsl:variable name="rtf">
    <xsl:apply-templates/>
  </xsl:variable>

  <xsl:choose>
    <xsl:when test="$suppress-numbers = '0'
                    and @linenumbering = 'numbered'
                    and $saxon.extensions != '0'
                    and $saxon.linenumbering != '0'">
      <xsl:choose>
        <xsl:when test="@class='monospaced'">
          <fo:block wrap-option='no-wrap'
                    text-align='start'
                    linefeed-treatment="preserve"
                    font-family='{$monospace.font.family}'
                    white-space-collapse='false'
                    space-before.minimum="0.8em"
                    space-before.optimum="1em"
                    space-before.maximum="1.2em">
            <xsl:choose>
              <xsl:when test="contains($vendor, 'SAXON 6')">
                <xsl:copy-of select="verb6:numberLines($rtf)"/>
              </xsl:when>
              <xsl:otherwise>
                <xsl:copy-of select="verb5:numberLines($rtf,
                                     $linenumbering.everyNth,
                                     $linenumbering.width,
                                     $linenumbering.separator)"/>
              </xsl:otherwise>
            </xsl:choose>
          </fo:block>
        </xsl:when>
        <xsl:otherwise>
          <fo:block wrap-option='no-wrap'
                    text-align='start'
                    linefeed-treatment="preserve"
                    white-space-collapse='false'
                    space-before.minimum="0.8em"
                    space-before.optimum="1em"
                    space-before.maximum="1.2em">
            <xsl:choose>
              <xsl:when test="contains($vendor, 'SAXON 6')">
                <xsl:copy-of select="verb6:numberLines($rtf)"/>
              </xsl:when>
              <xsl:otherwise>
                <xsl:copy-of select="verb5:numberLines($rtf,
                                     $linenumbering.everyNth,
                                     $linenumbering.width,
                                     $linenumbering.separator)"/>
              </xsl:otherwise>
            </xsl:choose>
          </fo:block>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:when>
    <xsl:otherwise>
      <xsl:choose>
        <xsl:when test="@class='monospaced'">
          <fo:block wrap-option='no-wrap'
                    text-align='start'
                    font-family='{$monospace.font.family}'
                    linefeed-treatment="preserve"
                    white-space-collapse='false'
                    space-before.minimum="0.8em"
                    space-before.optimum="1em"
                    space-before.maximum="1.2em">
            <xsl:copy-of select="$rtf"/>
          </fo:block>
        </xsl:when>
        <xsl:otherwise>
          <fo:block wrap-option='no-wrap'
                    text-align='start'
                    linefeed-treatment="preserve"
                    white-space-collapse='false'
                    space-before.minimum="0.8em"
                    space-before.optimum="1em"
                    space-before.maximum="1.2em">
            <xsl:copy-of select="$rtf"/>
          </fo:block>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:otherwise>
  </xsl:choose>
</xsl:template>

<xsl:template match="address">
  <xsl:param name="suppress-numbers" select="'0'"/>
  <xsl:variable name="vendor" select="system-property('xsl:vendor')"/>

  <xsl:variable name="rtf">
    <xsl:apply-templates/>
  </xsl:variable>

  <xsl:choose>
    <xsl:when test="$suppress-numbers = '0'
                    and @linenumbering = 'numbered'
                    and $saxon.extensions != '0'
                    and $saxon.linenumbering != '0'">
      <fo:block wrap-option='no-wrap'
                text-align='start'
                white-space-collapse='false'
                linefeed-treatment="preserve"
                space-before.minimum="0.8em"
                space-before.optimum="1em"
                space-before.maximum="1.2em">
        <xsl:choose>
          <xsl:when test="contains($vendor, 'SAXON 6')">
            <xsl:copy-of select="verb6:numberLines($rtf)"/>
          </xsl:when>
          <xsl:otherwise>
            <xsl:copy-of select="verb5:numberLines($rtf,
                                 $linenumbering.everyNth,
                                 $linenumbering.width,
                                 $linenumbering.separator)"/>
          </xsl:otherwise>
        </xsl:choose>
      </fo:block>
    </xsl:when>
    <xsl:otherwise>
      <fo:block wrap-option='no-wrap'
                text-align='start'
                linefeed-treatment="preserve"
                white-space-collapse='false'
                space-before.minimum="0.8em"
                space-before.optimum="1em"
                space-before.maximum="1.2em">
        <xsl:apply-templates/>
      </fo:block>
    </xsl:otherwise>
  </xsl:choose>
</xsl:template>

</xsl:stylesheet>
