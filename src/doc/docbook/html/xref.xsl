<?xml version='1.0'?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:doc="http://nwalsh.com/xsl/documentation/1.0"
                exclude-result-prefixes="doc"
                version='1.0'>

<!-- ********************************************************************
     $Id$
     ********************************************************************

     This file is part of the XSL DocBook Stylesheet distribution.
     See ../README or http://nwalsh.com/docbook/xsl/ for copyright
     and other information.

     ******************************************************************** -->

<!-- ==================================================================== -->

<xsl:template match="anchor">
  <a>
    <xsl:attribute name="name">
      <xsl:call-template name="object.id"/>
    </xsl:attribute>
  </a>
</xsl:template>

<!-- ==================================================================== -->

<xsl:template match="xref">
  <xsl:variable name="targets" select="id(@linkend)"/>
  <xsl:variable name="target" select="$targets[1]"/>
  <xsl:variable name="refelem" select="local-name($target)"/>

  <xsl:call-template name="check.id.unique">
    <xsl:with-param name="linkend" select="@linkend"/>
  </xsl:call-template>

  <xsl:if test="@id">
    <a name="{@id}"/>
  </xsl:if>

  <xsl:choose>
    <xsl:when test="$refelem=''">
      <xsl:message>
	<xsl:text>XRef to nonexistent id: </xsl:text>
	<xsl:value-of select="@linkend"/>
      </xsl:message>
      <xsl:text>???</xsl:text>
    </xsl:when>

    <xsl:when test="$target/@xreflabel">
      <a>
        <xsl:attribute name="href">
          <xsl:call-template name="href.target">
            <xsl:with-param name="object" select="$target"/>
          </xsl:call-template>
        </xsl:attribute>
        <xsl:call-template name="xref.xreflabel">
          <xsl:with-param name="target" select="$target"/>
        </xsl:call-template>
      </a>
    </xsl:when>

    <xsl:otherwise>
      <a>
        <xsl:attribute name="href">
          <xsl:call-template name="href.target">
            <xsl:with-param name="object" select="$target"/>
          </xsl:call-template>
        </xsl:attribute>

        <xsl:choose>
	  <xsl:when test="@endterm">
	    <xsl:variable name="etargets" select="id(@endterm)"/>
	    <xsl:variable name="etarget" select="$etargets[1]"/>
	    <xsl:choose>
	      <xsl:when test="count($etarget) = 0">
		<xsl:message>
		  <xsl:value-of select="count($etargets)"/>
		  <xsl:text>Endterm points to nonexistent ID: </xsl:text>
		  <xsl:value-of select="@endterm"/>
		</xsl:message>
		<xsl:text>???</xsl:text>
	      </xsl:when>
	      <xsl:otherwise>
		<xsl:apply-templates select="$etarget" mode="xref.text"/>
	      </xsl:otherwise>
	    </xsl:choose>
	  </xsl:when>

          <xsl:otherwise>
            <xsl:apply-templates select="$target" mode="xref-to"/>
          </xsl:otherwise>
<!--
          <xsl:when test="$refelem='figure'">
            <xsl:call-template name="xref.figure">
              <xsl:with-param name="target" select="$target"/>
            </xsl:call-template>
          </xsl:when>

          <xsl:when test="$refelem='example'">
            <xsl:call-template name="xref.example">
              <xsl:with-param name="target" select="$target"/>
            </xsl:call-template>
          </xsl:when>

          <xsl:when test="$refelem='table'">
            <xsl:call-template name="xref.table">
              <xsl:with-param name="target" select="$target"/>
            </xsl:call-template>
          </xsl:when>

          <xsl:when test="$refelem='equation'">
            <xsl:call-template name="xref.equation">
              <xsl:with-param name="target" select="$target"/>
            </xsl:call-template>
          </xsl:when>

          <xsl:when test="$refelem='cmdsynopsis'">
            <xsl:call-template name="xref.cmdsynopsis">
              <xsl:with-param name="target" select="$target"/>
            </xsl:call-template>
          </xsl:when>

          <xsl:when test="$refelem='funcsynopsis'">
            <xsl:call-template name="xref.funcsynopsis">
              <xsl:with-param name="target" select="$target"/>
            </xsl:call-template>
          </xsl:when>

          <xsl:when test="$refelem='dedication'">
            <xsl:call-template name="xref.dedication">
              <xsl:with-param name="target" select="$target"/>
            </xsl:call-template>
          </xsl:when>

          <xsl:when test="$refelem='preface'">
            <xsl:call-template name="xref.preface">
              <xsl:with-param name="target" select="$target"/>
            </xsl:call-template>
          </xsl:when>

          <xsl:when test="$refelem='chapter'">
            <xsl:call-template name="xref.chapter">
              <xsl:with-param name="target" select="$target"/>
            </xsl:call-template>
          </xsl:when>

          <xsl:when test="$refelem='appendix'">
            <xsl:call-template name="xref.appendix">
              <xsl:with-param name="target" select="$target"/>
            </xsl:call-template>
          </xsl:when>

          <xsl:when test="$refelem='bibliography'">
            <xsl:call-template name="xref.bibliography">
              <xsl:with-param name="target" select="$target"/>
            </xsl:call-template>
          </xsl:when>

          <xsl:when test="$refelem='biblioentry'">
            <xsl:call-template name="xref.biblioentry">
              <xsl:with-param name="target" select="$target"/>
            </xsl:call-template>
          </xsl:when>

          <xsl:when test="$refelem='bibliomixed'">
            <xsl:call-template name="xref.biblioentry">
              <xsl:with-param name="target" select="$target"/>
            </xsl:call-template>
          </xsl:when>

          <xsl:when test="$refelem='glossary'">
            <xsl:call-template name="xref.glossary">
              <xsl:with-param name="target" select="$target"/>
            </xsl:call-template>
          </xsl:when>

          <xsl:when test="$refelem='index'">
            <xsl:call-template name="xref.index">
              <xsl:with-param name="target" select="$target"/>
            </xsl:call-template>
          </xsl:when>

          <xsl:when test="$refelem='section'
                          or $refelem='simplesect'
                          or $refelem='sect1'
                          or $refelem='sect2'
                          or $refelem='sect3'
                          or $refelem='sect4'
                          or $refelem='sect5'
                          or $refelem='refsect1'
                          or $refelem='refsect2'
                          or $refelem='refsect3'">
            <xsl:call-template name="xref.section">
              <xsl:with-param name="target" select="$target"/>
            </xsl:call-template>
          </xsl:when>

          <xsl:when test="$refelem='listitem'
                          and local-name($target/..)='orderedlist'">
            <xsl:apply-templates select="$target" mode="xref"/>
          </xsl:when>

          <xsl:when test="$refelem='question'">
            <xsl:call-template name="xref.question">
              <xsl:with-param name="target" select="$target"/>
            </xsl:call-template>
          </xsl:when>

          <xsl:when test="$refelem='answer'">
            <xsl:call-template name="xref.answer">
              <xsl:with-param name="target" select="$target"/>
            </xsl:call-template>
          </xsl:when>

          <xsl:when test="$refelem='part'">
            <xsl:call-template name="xref.part">
              <xsl:with-param name="target" select="$target"/>
            </xsl:call-template>
          </xsl:when>

          <xsl:when test="$refelem='reference'">
            <xsl:call-template name="xref.reference">
              <xsl:with-param name="target" select="$target"/>
            </xsl:call-template>
          </xsl:when>

          <xsl:when test="$refelem='co'">
            <xsl:call-template name="xref.co">
              <xsl:with-param name="target" select="$target"/>
            </xsl:call-template>
          </xsl:when>

          <xsl:when test="$refelem='book'">
            <xsl:call-template name="xref.book">
              <xsl:with-param name="target" select="$target"/>
            </xsl:call-template>
          </xsl:when>

          <xsl:otherwise>
	    <xsl:message>
	      <xsl:text>[Don't know what gentext to create for xref to: "</xsl:text>
	      <xsl:value-of select="$refelem"/>
	      <xsl:text>"]</xsl:text>
	    </xsl:message>
            <xsl:text>???</xsl:text>
          </xsl:otherwise>
-->
        </xsl:choose>
      </a>
    </xsl:otherwise>
  </xsl:choose>
</xsl:template>

<!-- ==================================================================== -->

<xsl:template name="cross-reference">
  <xsl:param name="target" select="."/>
  <xsl:param name="refelem" select="local-name($target)"/>
  <xsl:param name="xref.text">
    <xsl:call-template name="gentext.xref.text">
      <xsl:with-param name="element.name" select="$refelem"/>
      <xsl:with-param name="default">%g %n</xsl:with-param>
    </xsl:call-template>
  </xsl:param>

  <xsl:call-template name="subst.xref.text">
    <xsl:with-param name="xref.text" select="$xref.text"/>
    <xsl:with-param name="target" select="$target"/>
  </xsl:call-template>
</xsl:template>

<xsl:template match="*" mode="xref-to">
  <xsl:param name="target" select="."/>
  <xsl:param name="refelem" select="local-name($target)"/>

  <xsl:message>
    <xsl:text>[Don't know what gentext to create for xref to: "</xsl:text>
    <xsl:value-of select="$refelem"/>
    <xsl:text>"]</xsl:text>
  </xsl:message>
  <xsl:text>???</xsl:text>
</xsl:template>

<xsl:template match="author" mode="xref-to">
  <xsl:param name="target" select="."/>
  <xsl:param name="refelem" select="local-name($target)"/>

  <xsl:call-template name="person.name"/>
</xsl:template>

<xsl:template match="figure" mode="xref-to">
  <xsl:param name="target" select="."/>
  <xsl:param name="refelem" select="local-name($target)"/>

  <xsl:call-template name="cross-reference">
    <xsl:with-param name="target" select="$target"/>
  </xsl:call-template>
</xsl:template>

<xsl:template match="example" mode="xref-to">
  <xsl:param name="target" select="."/>
  <xsl:param name="refelem" select="local-name($target)"/>

  <xsl:call-template name="cross-reference">
    <xsl:with-param name="target" select="$target"/>
  </xsl:call-template>
</xsl:template>

<xsl:template match="table" mode="xref-to">
  <xsl:param name="target" select="."/>
  <xsl:param name="refelem" select="local-name($target)"/>

  <xsl:call-template name="cross-reference">
    <xsl:with-param name="target" select="$target"/>
  </xsl:call-template>
</xsl:template>

<xsl:template match="equation" mode="xref-to">
  <xsl:param name="target" select="."/>
  <xsl:param name="refelem" select="local-name($target)"/>

  <xsl:call-template name="cross-reference">
    <xsl:with-param name="target" select="$target"/>
  </xsl:call-template>
</xsl:template>

<xsl:template match="cmdsynopsis" mode="xref-to">
  <xsl:param name="target" select="."/>
  <xsl:param name="refelem" select="local-name($target)"/>

  <xsl:variable name="command" select="($target//command)[1]"/>

  <xsl:apply-templates select="$command" mode="xref"/>
</xsl:template>

<xsl:template match="funcsynopsis" mode="xref-to">
  <xsl:param name="target" select="."/>
  <xsl:param name="refelem" select="local-name($target)"/>

  <xsl:variable name="func" select="($target//function)[1]"/>

  <xsl:apply-templates select="$func" mode="xref"/>
</xsl:template>

<xsl:template match="dedication" mode="xref-to">
  <xsl:param name="target" select="."/>
  <xsl:param name="refelem" select="local-name($target)"/>

  <xsl:call-template name="cross-reference">
    <xsl:with-param name="target" select="$target"/>
  </xsl:call-template>
</xsl:template>

<xsl:template match="preface" mode="xref-to">
  <xsl:param name="target" select="."/>
  <xsl:param name="refelem" select="local-name($target)"/>

  <xsl:call-template name="cross-reference">
    <xsl:with-param name="target" select="$target"/>
  </xsl:call-template>
</xsl:template>

<xsl:template match="chapter" mode="xref-to">
  <xsl:param name="target" select="."/>
  <xsl:param name="refelem" select="local-name($target)"/>

  <xsl:call-template name="cross-reference">
    <xsl:with-param name="target" select="$target"/>
  </xsl:call-template>
</xsl:template>

<xsl:template match="appendix" mode="xref-to">
  <xsl:param name="target" select="."/>
  <xsl:param name="refelem" select="local-name($target)"/>

  <xsl:call-template name="cross-reference">
    <xsl:with-param name="target" select="$target"/>
  </xsl:call-template>
</xsl:template>

<xsl:template match="bibliography" mode="xref-to">
  <xsl:param name="target" select="."/>
  <xsl:param name="refelem" select="local-name($target)"/>

  <xsl:call-template name="cross-reference">
    <xsl:with-param name="target" select="$target"/>
  </xsl:call-template>
</xsl:template>

<xsl:template match="biblioentry|bibliomixed" mode="xref-to">
  <!-- handles both biblioentry and bibliomixed -->
  <xsl:param name="target" select="."/>
  <xsl:param name="refelem" select="local-name($target)"/>

  <xsl:text>[</xsl:text>
  <xsl:choose>
    <xsl:when test="local-name($target/*[1]) = 'abbrev'">
      <xsl:apply-templates select="$target/*[1]"/>
    </xsl:when>
    <xsl:otherwise>
      <xsl:value-of select="@id"/>
    </xsl:otherwise>
  </xsl:choose>
  <xsl:text>]</xsl:text>
</xsl:template>

<xsl:template match="glossary" mode="xref-to">
  <xsl:param name="target" select="."/>
  <xsl:param name="refelem" select="local-name($target)"/>

  <xsl:call-template name="cross-reference">
    <xsl:with-param name="target" select="$target"/>
  </xsl:call-template>
</xsl:template>

<xsl:template match="index" mode="xref-to">
  <xsl:param name="target" select="."/>
  <xsl:param name="refelem" select="local-name($target)"/>

  <xsl:call-template name="cross-reference">
    <xsl:with-param name="target" select="$target"/>
  </xsl:call-template>
</xsl:template>

<xsl:template match="section|simplesect
                     |sect1|sect2|sect3|sect4|sect5
                     |refsect1|refsect2|refsect3" mode="xref-to">
  <xsl:param name="target" select="."/>
  <xsl:param name="refelem" select="local-name($target)"/>

  <xsl:choose>
    <xsl:when test="$section.autolabel">
      <xsl:call-template name="gentext.element.name">
        <xsl:with-param name="element.name" select="$refelem"/>
      </xsl:call-template>
      <xsl:text> </xsl:text>
      <xsl:apply-templates select="$target" mode="label.content"/>

    </xsl:when>
    <xsl:otherwise>
      <xsl:call-template name="gentext.element.name">
        <xsl:with-param name="element.name">the section called</xsl:with-param>
      </xsl:call-template>
      <xsl:text> </xsl:text>
      <xsl:call-template name="gentext.startquote"/>
      <xsl:apply-templates select="$target" mode="title.content"/>
      <xsl:call-template name="gentext.endquote"/>
    </xsl:otherwise>
  </xsl:choose>
</xsl:template>

<xsl:template match="question" mode="xref-to">
  <xsl:param name="target" select="."/>
  <xsl:param name="refelem" select="local-name($target)"/>

  <xsl:call-template name="cross-reference">
    <xsl:with-param name="target" select="$target"/>
  </xsl:call-template>
</xsl:template>

<xsl:template match="answer" mode="xref-to">
  <xsl:param name="target" select="."/>
  <xsl:param name="refelem" select="local-name($target)"/>

  <xsl:call-template name="cross-reference">
    <xsl:with-param name="target" select="$target"/>
  </xsl:call-template>
</xsl:template>

<xsl:template match="part" mode="xref-to">
  <xsl:param name="target" select="."/>
  <xsl:param name="refelem" select="local-name($target)"/>

  <xsl:call-template name="cross-reference">
    <xsl:with-param name="target" select="$target"/>
  </xsl:call-template>
</xsl:template>

<xsl:template match="reference" mode="xref-to">
  <xsl:param name="target" select="."/>
  <xsl:param name="refelem" select="local-name($target)"/>

  <xsl:call-template name="cross-reference">
    <xsl:with-param name="target" select="$target"/>
  </xsl:call-template>
</xsl:template>

<xsl:template match="co" mode="xref-to">
  <xsl:param name="target" select="."/>
  <xsl:param name="refelem" select="local-name($target)"/>
  <xsl:apply-templates select="$target" mode="callout-bug"/>
</xsl:template>

<xsl:template match="co" mode="conumber">
  <xsl:number from="literallayout|programlisting|screen|synopsis"
              level="single"
              format="1"/>
</xsl:template>

<xsl:template match="book" mode="xref-to">
  <xsl:param name="target" select="."/>
  <xsl:param name="refelem" select="local-name($target)"/>

  <xsl:variable name="title">
    <xsl:choose>
      <xsl:when test="$target/title">
        <xsl:apply-templates select="$target/title" mode="xref"/>
      </xsl:when>
      <xsl:otherwise>
        <xsl:apply-templates select="$target/bookinfo/title"
                             mode="xref"/>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:variable>
  <i>
    <xsl:copy-of select="$title"/>
  </i>
</xsl:template>

<!-- ==================================================================== -->

<xsl:template match="link">
  <xsl:variable name="targets" select="id(@linkend)"/>
  <xsl:variable name="target" select="$targets[1]"/>

  <xsl:call-template name="check.id.unique">
    <xsl:with-param name="linkend" select="@linkend"/>
  </xsl:call-template>

  <a>
    <xsl:if test="@id">
      <xsl:attribute name="name"><xsl:value-of select="@id"/></xsl:attribute>
    </xsl:if>

    <xsl:attribute name="href">
      <xsl:call-template name="href.target">
        <xsl:with-param name="object" select="$target"/>
      </xsl:call-template>
    </xsl:attribute>

    <xsl:apply-templates/>
  </a>
</xsl:template>

<xsl:template match="ulink">
  <a>
    <xsl:if test="@id">
      <xsl:attribute name="name"><xsl:value-of select="@id"/></xsl:attribute>
    </xsl:if>
    <xsl:attribute name="href"><xsl:value-of select="@url"/></xsl:attribute>
    <xsl:if test="$ulink.target != ''">
      <xsl:attribute name="target">
        <xsl:value-of select="$ulink.target"/>
      </xsl:attribute>
    </xsl:if>
    <xsl:choose>
      <xsl:when test="count(child::node())=0">
	<xsl:value-of select="@url"/>
      </xsl:when>
      <xsl:otherwise>
	<xsl:apply-templates/>
      </xsl:otherwise>
    </xsl:choose>
  </a>
</xsl:template>

<xsl:template match="olink">
  <xsl:if test="@id">
    <a name="{@id}"/>
  </xsl:if>
  <xsl:apply-templates/>
</xsl:template>

<!-- ==================================================================== -->

<xsl:template name="title.xref">
  <xsl:param name="target" select="."/>
  <xsl:choose>
    <xsl:when test="local-name($target) = 'figure'
                    or local-name($target) = 'example'
                    or local-name($target) = 'equation'
                    or local-name($target) = 'table'
                    or local-name($target) = 'dedication'
                    or local-name($target) = 'preface'
                    or local-name($target) = 'bibliography'
                    or local-name($target) = 'glossary'
                    or local-name($target) = 'index'
                    or local-name($target) = 'setindex'
                    or local-name($target) = 'colophon'">
      <xsl:call-template name="gentext.startquote"/>
      <xsl:apply-templates select="$target" mode="title.content"/>
      <xsl:call-template name="gentext.endquote"/>
    </xsl:when>
    <xsl:otherwise>
      <i>
        <xsl:apply-templates select="$target" mode="title.content"/>
      </i>
    </xsl:otherwise>
  </xsl:choose>
</xsl:template>

<xsl:template name="number.xref">
  <xsl:param name="target" select="."/>
  <xsl:apply-templates select="$target" mode="label.content"/>
</xsl:template>

<!-- ==================================================================== -->

<xsl:template name="xref.xreflabel">
  <!-- called to process an xreflabel...you might use this to make  -->
  <!-- xreflabels come out in the right font for different targets, -->
  <!-- for example. -->
  <xsl:param name="target" select="."/>
  <xsl:value-of select="$target/@xreflabel"/>
</xsl:template>

<!-- ==================================================================== -->

<xsl:template match="title" mode="xref">
  <xsl:apply-templates/>
</xsl:template>

<xsl:template match="command" mode="xref">
  <xsl:call-template name="inline.boldseq"/>
</xsl:template>

<xsl:template match="function" mode="xref">
  <xsl:call-template name="inline.monoseq"/>
</xsl:template>

</xsl:stylesheet>
