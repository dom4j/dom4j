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

<xsl:template match="programlisting|screen|synopsis">
  <xsl:param name="suppress-numbers" select="'0'"/>
  <xsl:variable name="vendor" select="system-property('xsl:vendor')"/>
  <xsl:variable name="id"><xsl:call-template name="object.id"/></xsl:variable>

  <!-- Obey the <?dbhtml linenumbering.everyNth="x"?> PI -->
  <xsl:variable name="default.linenumbering.everyNth"
                select="$linenumbering.everyNth"/>

  <xsl:variable name="pi.linenumbering.everyNth">
    <xsl:call-template name="dbhtml-attribute">
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

  <!-- Obey the <?dbhtml linenumbering.separator="x"?> PI -->
  <xsl:variable name="default.linenumbering.separator"
                select="$linenumbering.separator"/>

  <xsl:variable name="pi.linenumbering.separator">
    <xsl:call-template name="dbhtml-attribute">
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

  <xsl:if test="@id">
    <a href="{$id}"/>
  </xsl:if>

  <xsl:choose>
    <xsl:when test="$suppress-numbers = '0'
                    and @linenumbering = 'numbered'
                    and $saxon.extensions != '0'
                    and $saxon.linenumbering != '0'">
      <xsl:variable name="rtf">
        <xsl:apply-templates/>
      </xsl:variable>
      <pre class="{name(.)}">
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
      </pre>
    </xsl:when>
    <xsl:otherwise>
      <pre class="{name(.)}">
        <xsl:apply-templates/>
      </pre>
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
          <pre class="{name(.)}">
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
          </pre>
        </xsl:when>
        <xsl:otherwise>
          <div class="{name(.)}">
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
          </div>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:when>
    <xsl:otherwise>
      <xsl:choose>
        <xsl:when test="@class='monospaced'">
          <pre class="{name(.)}">
            <xsl:copy-of select="$rtf"/>
          </pre>
        </xsl:when>
        <xsl:otherwise>
          <div class="{name(.)}">
            <xsl:copy-of select="$rtf"/>
          </div>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:otherwise>
  </xsl:choose>
</xsl:template>

<xsl:template match="literallayout[not(@class)
                                   or @class != 'monospaced']//text()">
  <xsl:call-template name="make-verbatim">
    <xsl:with-param name="text" select="."/>
  </xsl:call-template>
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
      <div class="{name(.)}">
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
      </div>
    </xsl:when>
    <xsl:otherwise>
      <div class="{name(.)}">
        <xsl:apply-templates/>
      </div>
    </xsl:otherwise>
  </xsl:choose>
</xsl:template>

<xsl:template match="address//text()">
  <xsl:call-template name="make-verbatim">
    <xsl:with-param name="text" select="."/>
  </xsl:call-template>
</xsl:template>

<xsl:template name="make-verbatim">
  <xsl:param name="text" select="''"/>

  <xsl:variable name="starts-with-space"
                select="substring($text, 1, 1) = ' '"/>

  <xsl:variable name="starts-with-nl"
                select="substring($text, 1, 1) = '&#xA;'"/>

  <xsl:variable name="before-space">
    <xsl:if test="contains($text, ' ')">
      <xsl:value-of select="substring-before($text, ' ')"/>
    </xsl:if>
  </xsl:variable>

  <xsl:variable name="before-nl">
    <xsl:if test="contains($text, '&#xA;')">
      <xsl:value-of select="substring-before($text, '&#xA;')"/>
    </xsl:if>
  </xsl:variable>

  <xsl:choose>
    <xsl:when test="$starts-with-space">
      <xsl:text>&#160;</xsl:text>
      <xsl:call-template name="make-verbatim">
        <xsl:with-param name="text" select="substring($text,2)"/>
      </xsl:call-template>
    </xsl:when>

    <xsl:when test="$starts-with-nl">
      <br/><xsl:text>&#xA;</xsl:text>
      <xsl:call-template name="make-verbatim">
        <xsl:with-param name="text" select="substring($text,2)"/>
      </xsl:call-template>
    </xsl:when>

    <!-- if the string before a space is shorter than the string before
         a newline, fix the space...-->
    <xsl:when test="$before-space != ''
                    and ((string-length($before-space)
                          &lt; string-length($before-nl))
                          or $before-nl = '')">
      <xsl:value-of select="$before-space"/>
      <xsl:text>&#160;</xsl:text>
      <xsl:call-template name="make-verbatim">
        <xsl:with-param name="text" select="substring-after($text, ' ')"/>
      </xsl:call-template>
    </xsl:when>

    <!-- if the string before a newline is shorter than the string before
         a space, fix the newline...-->
    <xsl:when test="$before-nl != ''
                    and ((string-length($before-nl)
                          &lt; string-length($before-space))
                          or $before-space = '')">
      <xsl:value-of select="$before-nl"/>
      <br/><xsl:text>&#xA;</xsl:text>
      <xsl:call-template name="make-verbatim">
        <xsl:with-param name="text" select="substring-after($text, '&#xA;')"/>
      </xsl:call-template>
    </xsl:when>

    <!-- the string before the newline and the string before the
         space are the same; which means they must both be empty -->
    <xsl:otherwise>
      <xsl:value-of select="$text"/>
    </xsl:otherwise>
  </xsl:choose>
</xsl:template>

</xsl:stylesheet>
