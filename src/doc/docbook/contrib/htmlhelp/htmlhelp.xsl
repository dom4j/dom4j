<!-- ********************************************************************
     $Id$
     ********************************************************************

     This file can be used to generate source files for HTML Help.  It
     is based on the XSL DocBook Stylesheet distribution (especially
     on JavaHelp code) from Norman Walsh.

     This stylesheet imports chunk.xsl, thus set of HTML files is
     created in usual way. Additionally is created file htmlhelp.hhp
     which is project file for HTML Help Compiler and toc.hhc which
     holds structure of your document. Language identifier of HTML
     Help is taken from top most element in your DocBook source. This
     is perfectly legal for documents in only one language. If
     language is not set, US English is defaulted. There is one
     problem - MS language codes are country sensitive, and this
     information is not available in DocBook source. If stylesheet
     selects bad code for you, edit langcodes.xml file, and remove all
     entries which have yours language code, but are located in
     inappropriate country.

     Title of whole HTML Help is taken from first title element in
     document.  Fulltext searching is automatically on. If your
     document contains index terms, they are automatically converted
     to ActiveX, which is recognized by HTML Help compiler.

     If you are generating HTML Help for non-Western Europe languages,
     you should change output encoding of your files, because HTML
     Help compiler improperly handles UTF-8 and even character
     entities in TOC file and index entries.  This can be easily done
     by "driver" file like this:

     <xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
		     version="1.0">
	<xsl:output encoding="windows-1250"/>
	<xsl:import href="<path to this file...>htmlhelp/docbook.xsl"/>
     </xsl:stylesheet>

     If you have any comments and suggestion about this file feel free
     to contact me at following address <jirka@kosek.cz>.

     Enjoy!

					Jirka Kosek

     ******************************************************************** -->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                version="1.0">

<!-- ==================================================================== -->

<xsl:template match="/">
  <xsl:apply-templates/>
  <xsl:call-template name="hhp"/>
  <xsl:call-template name="hhc"/>
</xsl:template>

<xsl:template name="header.navigation">
</xsl:template>

<xsl:template name="footer.navigation">
</xsl:template>

<!-- ==================================================================== -->

<xsl:template name="hhp">
  <xsl:call-template name="write.text.chunk">
    <xsl:with-param name="filename" select="'htmlhelp.hhp'"/>
    <xsl:with-param name="method" select="'text'"/>
    <xsl:with-param name="content">
      <xsl:call-template name="hhp-main"/>
    </xsl:with-param>
  </xsl:call-template>
</xsl:template>

<!-- ==================================================================== -->
<xsl:template name="hhp-main">
<xsl:text>[OPTIONS]
</xsl:text>
<xsl:if test="//indexterm">
<xsl:text>Auto Index=Yes
</xsl:text></xsl:if>
<xsl:text>Compatibility=1.1 or later
Compiled file=htmlhelp.chm
Contents file=toc.hhc
Default topic=index.html
Display compile progress=No
Full-text search=Yes
Language=</xsl:text>
<xsl:if test="//@lang">
  <xsl:variable name="lang" select="//@lang[1]"/>
  <xsl:value-of select="document('langcodes.xml')//gentext[@lang=string($lang)]"/>
</xsl:if>
<xsl:if test="not(//@lang)">
  <xsl:text>0x0409 English (United States)
</xsl:text></xsl:if>
<xsl:text>
Title=</xsl:text><xsl:value-of select="//title[1]"/>
<xsl:text>

[FILES]
</xsl:text>
<xsl:apply-templates mode="enumerate-files"/>
</xsl:template>
<!-- ==================================================================== -->

<xsl:template match="set|book|part|preface|chapter|appendix
                     |article
                     |reference|refentry
                     |sect1[position()>1]
                     |section[position()>1 and name(parent::*) != 'section']
                     |book/glossary|article/glossary
                     |book/bibliography|article/bibliography
                     |colophon"
              mode="enumerate-files">
  <xsl:variable name="ischunk"><xsl:call-template name="chunk"/></xsl:variable>
  <xsl:if test="$ischunk='1'">
    <xsl:apply-templates mode="chunk-filename" select="."/><xsl:text>
</xsl:text>
  </xsl:if>
  <xsl:apply-templates select="*" mode="enumerate-files"/>
</xsl:template>

<xsl:template match="text()" mode="enumerate-files">
</xsl:template>

<!-- ==================================================================== -->

<!-- Following templates are not nice. It is because MS help compiler is unable
     to process correct HTML files. We must generate following weird
     stuff instead. -->

<xsl:template name="hhc">
  <xsl:call-template name="write.text.chunk">
    <xsl:with-param name="filename" select="'toc.hhc'"/>
    <xsl:with-param name="method" select="'text'"/>
    <xsl:with-param name="content">
      <xsl:call-template name="hhc-main"/>
    </xsl:with-param>
  </xsl:call-template>
</xsl:template>

<xsl:template name="hhc-main">
    <xsl:text>&lt;HTML&gt;
&lt;HEAD&gt;
&lt;/HEAD&gt;
&lt;BODY&gt;
&lt;OBJECT type="text/site properties"&gt;
	&lt;param name="ImageType" value="Folder"&gt;
&lt;/OBJECT&gt;
&lt;UL&gt;
</xsl:text>
      <xsl:apply-templates select="." mode="hhc"/>
<xsl:text>&lt;/UL&gt;
&lt;/BODY&gt;
&lt;/HTML&gt;</xsl:text>
</xsl:template>

<xsl:template match="set" mode="hhc">
  <xsl:text>&lt;LI&gt; &lt;OBJECT type="text/sitemap"&gt;
    &lt;param name="Name" value="</xsl:text>
	<xsl:apply-templates select="." mode="title.ref">
	  <xsl:with-param name="text-only" select="true()"/>
	</xsl:apply-templates>
    <xsl:text>"&gt;
    &lt;param name="Local" value="</xsl:text>
	<xsl:call-template name="href.target"/>
    <xsl:text>"&gt;
  &lt;/OBJECT&gt;</xsl:text>
  <xsl:if test="book">
    <xsl:text>&lt;UL&gt;</xsl:text>
      <xsl:apply-templates select="book" mode="hhc"/>
    <xsl:text>&lt;/UL&gt;</xsl:text>
  </xsl:if>
</xsl:template>

<xsl:template match="book" mode="hhc">
  <xsl:text>&lt;LI&gt; &lt;OBJECT type="text/sitemap"&gt;
    &lt;param name="Name" value="</xsl:text>
	<xsl:apply-templates select="." mode="title.ref">
	  <xsl:with-param name="text-only" select="true()"/>
	</xsl:apply-templates>
    <xsl:text>"&gt;
    &lt;param name="Local" value="</xsl:text>
	<xsl:call-template name="href.target"/>
    <xsl:text>"&gt;
  &lt;/OBJECT&gt;</xsl:text>
  <xsl:if test="part|reference|preface|chapter|appendix">
    <xsl:text>&lt;UL&gt;</xsl:text>
      <xsl:apply-templates select="part|reference|preface|chapter|appendix"
			   mode="hhc"/>
    <xsl:text>&lt;/UL&gt;</xsl:text>
  </xsl:if>
</xsl:template>

<xsl:template match="part|reference|preface|chapter|appendix"
              mode="hhc">
  <xsl:text>&lt;LI&gt; &lt;OBJECT type="text/sitemap"&gt;
    &lt;param name="Name" value="</xsl:text>
	<xsl:apply-templates select="." mode="title.ref">
	  <xsl:with-param name="text-only" select="true()"/>
	</xsl:apply-templates>
    <xsl:text>"&gt;
    &lt;param name="Local" value="</xsl:text>
	<xsl:call-template name="href.target"/>
    <xsl:text>"&gt;
  &lt;/OBJECT&gt;</xsl:text>
  <xsl:if test="preface|chapter|appendix|refentry|section|sect1">
    <xsl:text>&lt;UL&gt;</xsl:text>
      <xsl:apply-templates
	select="preface|chapter|appendix|refentry|section|sect1"
	mode="hhc"/>
    <xsl:text>&lt;/UL&gt;</xsl:text>
  </xsl:if>
</xsl:template>

<xsl:template match="section" mode="hhc">
  <xsl:text>&lt;LI&gt; &lt;OBJECT type="text/sitemap"&gt;
    &lt;param name="Name" value="</xsl:text>
	<xsl:apply-templates select="." mode="title.ref">
	  <xsl:with-param name="text-only" select="true()"/>
	</xsl:apply-templates>
    <xsl:text>"&gt;
    &lt;param name="Local" value="</xsl:text>
	<xsl:call-template name="href.target"/>
    <xsl:text>"&gt;
  &lt;/OBJECT&gt;</xsl:text>
  <xsl:if test="section">
    <xsl:text>&lt;UL&gt;</xsl:text>
      <xsl:apply-templates select="section" mode="hhc"/>
    <xsl:text>&lt;/UL&gt;</xsl:text>
  </xsl:if>
</xsl:template>

<xsl:template match="sect1" mode="hhc">
  <xsl:text>&lt;LI&gt; &lt;OBJECT type="text/sitemap"&gt;
    &lt;param name="Name" value="</xsl:text>
	<xsl:apply-templates select="." mode="title.ref">
	  <xsl:with-param name="text-only" select="true()"/>
	</xsl:apply-templates>
    <xsl:text>"&gt;
    &lt;param name="Local" value="</xsl:text>
	<xsl:call-template name="href.target"/>
    <xsl:text>"&gt;
  &lt;/OBJECT&gt;</xsl:text>
  <xsl:if test="sect2">
    <xsl:text>&lt;UL&gt;</xsl:text>
      <xsl:apply-templates select="sect2"
			   mode="hhc"/>
    <xsl:text>&lt;/UL&gt;</xsl:text>
  </xsl:if>
</xsl:template>

<xsl:template match="sect2" mode="hhc">
  <xsl:text>&lt;LI&gt; &lt;OBJECT type="text/sitemap"&gt;
    &lt;param name="Name" value="</xsl:text>
	<xsl:apply-templates select="." mode="title.ref">
	  <xsl:with-param name="text-only" select="true()"/>
	</xsl:apply-templates>
    <xsl:text>"&gt;
    &lt;param name="Local" value="</xsl:text>
	<xsl:call-template name="href.target"/>
    <xsl:text>"&gt;
  &lt;/OBJECT&gt;</xsl:text>
  <xsl:if test="sect3">
    <xsl:text>&lt;UL&gt;</xsl:text>
      <xsl:apply-templates select="sect3"
			   mode="hhc"/>
    <xsl:text>&lt;/UL&gt;</xsl:text>
  </xsl:if>
</xsl:template>

<xsl:template match="sect3" mode="hhc">
  <xsl:text>&lt;LI&gt; &lt;OBJECT type="text/sitemap"&gt;
    &lt;param name="Name" value="</xsl:text>
	<xsl:apply-templates select="." mode="title.ref">
	  <xsl:with-param name="text-only" select="true()"/>
	</xsl:apply-templates>
    <xsl:text>"&gt;
    &lt;param name="Local" value="</xsl:text>
	<xsl:call-template name="href.target"/>
    <xsl:text>"&gt;
  &lt;/OBJECT&gt;</xsl:text>
  <xsl:if test="sect4">
    <xsl:text>&lt;UL&gt;</xsl:text>
      <xsl:apply-templates select="sect4"
			   mode="hhc"/>
    <xsl:text>&lt;/UL&gt;</xsl:text>
  </xsl:if>
</xsl:template>

<xsl:template match="sect4" mode="hhc">
  <xsl:text>&lt;LI&gt; &lt;OBJECT type="text/sitemap"&gt;
    &lt;param name="Name" value="</xsl:text>
	<xsl:apply-templates select="." mode="title.ref">
	  <xsl:with-param name="text-only" select="true()"/>
	</xsl:apply-templates>
    <xsl:text>"&gt;
    &lt;param name="Local" value="</xsl:text>
	<xsl:call-template name="href.target"/>
    <xsl:text>"&gt;
  &lt;/OBJECT&gt;</xsl:text>
  <xsl:if test="sect5">
    <xsl:text>&lt;UL&gt;</xsl:text>
      <xsl:apply-templates select="sect5"
			   mode="hhc"/>
    <xsl:text>&lt;/UL&gt;</xsl:text>
  </xsl:if>
</xsl:template>

<xsl:template match="sect5" mode="hhc">
  <xsl:text>&lt;LI&gt; &lt;OBJECT type="text/sitemap"&gt;
    &lt;param name="Name" value="</xsl:text>
	<xsl:apply-templates select="." mode="title.ref">
	  <xsl:with-param name="text-only" select="true()"/>
	</xsl:apply-templates>
    <xsl:text>"&gt;
    &lt;param name="Local" value="</xsl:text>
	<xsl:call-template name="href.target"/>
    <xsl:text>"&gt;
  &lt;/OBJECT&gt;</xsl:text>
</xsl:template>

<!-- ==================================================================== -->

<xsl:template match="indexterm">

  <xsl:variable name="text">
    <xsl:value-of select="primary"/>
    <xsl:if test="secondary">
      <xsl:text>, </xsl:text>
      <xsl:value-of select="secondary"/>
    </xsl:if>
    <xsl:if test="tertiary">
      <xsl:text>, </xsl:text>
      <xsl:value-of select="tertiary"/>
    </xsl:if>
  </xsl:variable>

  <OBJECT type="application/x-oleobject"
          classid="clsid:1e2a7bd0-dab9-11d0-b93a-00c04fc99f9e">
    <param name="Keyword" value="{$text}"/>
  </OBJECT>
</xsl:template>

<!-- ==================================================================== -->

</xsl:stylesheet>
