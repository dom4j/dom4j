<?xml version="1.0" encoding="utf-8"?><xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

<!-- This stylesheet was created by template/titlepage.xsl; do not edit it by hand. -->

<xsl:template name="article.titlepage.recto">
  <xsl:apply-templates mode="article.titlepage.recto.auto.mode" select="(articleinfo/title|artheader/title|title)[1]"/>
  <xsl:apply-templates mode="article.titlepage.recto.auto.mode" select="(articleinfo/subtitle|artheader/subtitle|subtitle)[1]"/>
  <xsl:apply-templates mode="article.titlepage.recto.auto.mode" select="articleinfo/corpauthor|artheader/corpauthor"/>
  <xsl:apply-templates mode="article.titlepage.recto.auto.mode" select="articleinfo/authorgroup|artheader/authorgroup"/>
  <xsl:apply-templates mode="article.titlepage.recto.auto.mode" select="articleinfo/author|artheader/author"/>
  <xsl:apply-templates mode="article.titlepage.recto.auto.mode" select="articleinfo/releaseinfo|artheader/releaseinfo"/>
  <xsl:apply-templates mode="article.titlepage.recto.auto.mode" select="articleinfo/copyright|artheader/copyright"/>
  <xsl:apply-templates mode="article.titlepage.recto.auto.mode" select="articleinfo/legalnotice|artheader/legalnotice"/>
  <xsl:apply-templates mode="article.titlepage.recto.auto.mode" select="articleinfo/pubdate|artheader/pubdate"/>
  <xsl:apply-templates mode="article.titlepage.recto.auto.mode" select="articleinfo/revision|artheader/revision"/>
  <xsl:apply-templates mode="article.titlepage.recto.auto.mode" select="articleinfo/revhistory|artheader/revhistory"/>
  <xsl:apply-templates mode="article.titlepage.recto.auto.mode" select="articleinfo/abstract|artheader/abstract"/>
</xsl:template>

<xsl:template name="article.titlepage.verso">
</xsl:template>

<xsl:template name="article.titlepage.separator"><hr/>
</xsl:template>

<xsl:template name="article.titlepage.before.recto">
</xsl:template>

<xsl:template name="article.titlepage.before.verso">
</xsl:template>

<xsl:template name="article.titlepage">
  <div class="titlepage">
    <xsl:call-template name="article.titlepage.before.recto"/>
    <xsl:call-template name="article.titlepage.recto"/>
    <xsl:call-template name="article.titlepage.before.verso"/>
    <xsl:call-template name="article.titlepage.verso"/>
    <xsl:call-template name="article.titlepage.separator"/>
  </div>
</xsl:template>

<xsl:template match="*" mode="article.titlepage.recto.mode">
  <!-- if an element isn't found in this mode, -->
  <!-- try the generic titlepage.mode -->
  <xsl:apply-templates select="." mode="titlepage.mode"/>
</xsl:template>

<xsl:template match="*" mode="article.titlepage.verso.mode">
  <!-- if an element isn't found in this mode, -->
  <!-- try the generic titlepage.mode -->
  <xsl:apply-templates select="." mode="titlepage.mode"/>
</xsl:template>

<xsl:template match="title" mode="article.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="article.titlepage.recto.style">
<xsl:apply-templates select="." mode="article.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="subtitle" mode="article.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="article.titlepage.recto.style">
<xsl:apply-templates select="." mode="article.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="corpauthor" mode="article.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="article.titlepage.recto.style">
<xsl:apply-templates select="." mode="article.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="authorgroup" mode="article.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="article.titlepage.recto.style">
<xsl:apply-templates select="." mode="article.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="author" mode="article.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="article.titlepage.recto.style">
<xsl:apply-templates select="." mode="article.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="releaseinfo" mode="article.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="article.titlepage.recto.style">
<xsl:apply-templates select="." mode="article.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="copyright" mode="article.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="article.titlepage.recto.style">
<xsl:apply-templates select="." mode="article.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="legalnotice" mode="article.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="article.titlepage.recto.style">
<xsl:apply-templates select="." mode="article.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="pubdate" mode="article.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="article.titlepage.recto.style">
<xsl:apply-templates select="." mode="article.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="revision" mode="article.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="article.titlepage.recto.style">
<xsl:apply-templates select="." mode="article.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="revhistory" mode="article.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="article.titlepage.recto.style">
<xsl:apply-templates select="." mode="article.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="abstract" mode="article.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="article.titlepage.recto.style">
<xsl:apply-templates select="." mode="article.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template name="set.titlepage.recto">
  <xsl:apply-templates mode="set.titlepage.recto.auto.mode" select="(setinfo/title|title)[1]"/>
  <xsl:apply-templates mode="set.titlepage.recto.auto.mode" select="(setinfo/subtitle|subtitle)[1]"/>
  <xsl:apply-templates mode="set.titlepage.recto.auto.mode" select="setinfo/corpauthor"/>
  <xsl:apply-templates mode="set.titlepage.recto.auto.mode" select="setinfo/authorgroup"/>
  <xsl:apply-templates mode="set.titlepage.recto.auto.mode" select="setinfo/author"/>
  <xsl:apply-templates mode="set.titlepage.recto.auto.mode" select="setinfo/releaseinfo"/>
  <xsl:apply-templates mode="set.titlepage.recto.auto.mode" select="setinfo/copyright"/>
  <xsl:apply-templates mode="set.titlepage.recto.auto.mode" select="setinfo/legalnotice"/>
  <xsl:apply-templates mode="set.titlepage.recto.auto.mode" select="setinfo/pubdate"/>
  <xsl:apply-templates mode="set.titlepage.recto.auto.mode" select="setinfo/revision"/>
  <xsl:apply-templates mode="set.titlepage.recto.auto.mode" select="setinfo/revhistory"/>
  <xsl:apply-templates mode="set.titlepage.recto.auto.mode" select="setinfo/abstract"/>
</xsl:template>

<xsl:template name="set.titlepage.verso">
</xsl:template>

<xsl:template name="set.titlepage.separator"><hr/>
</xsl:template>

<xsl:template name="set.titlepage.before.recto">
</xsl:template>

<xsl:template name="set.titlepage.before.verso">
</xsl:template>

<xsl:template name="set.titlepage">
  <div class="titlepage">
    <xsl:call-template name="set.titlepage.before.recto"/>
    <xsl:call-template name="set.titlepage.recto"/>
    <xsl:call-template name="set.titlepage.before.verso"/>
    <xsl:call-template name="set.titlepage.verso"/>
    <xsl:call-template name="set.titlepage.separator"/>
  </div>
</xsl:template>

<xsl:template match="*" mode="set.titlepage.recto.mode">
  <!-- if an element isn't found in this mode, -->
  <!-- try the generic titlepage.mode -->
  <xsl:apply-templates select="." mode="titlepage.mode"/>
</xsl:template>

<xsl:template match="*" mode="set.titlepage.verso.mode">
  <!-- if an element isn't found in this mode, -->
  <!-- try the generic titlepage.mode -->
  <xsl:apply-templates select="." mode="titlepage.mode"/>
</xsl:template>

<xsl:template match="title" mode="set.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="set.titlepage.recto.style">
<xsl:apply-templates select="." mode="set.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="subtitle" mode="set.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="set.titlepage.recto.style">
<xsl:apply-templates select="." mode="set.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="corpauthor" mode="set.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="set.titlepage.recto.style">
<xsl:apply-templates select="." mode="set.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="authorgroup" mode="set.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="set.titlepage.recto.style">
<xsl:apply-templates select="." mode="set.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="author" mode="set.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="set.titlepage.recto.style">
<xsl:apply-templates select="." mode="set.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="releaseinfo" mode="set.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="set.titlepage.recto.style">
<xsl:apply-templates select="." mode="set.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="copyright" mode="set.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="set.titlepage.recto.style">
<xsl:apply-templates select="." mode="set.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="legalnotice" mode="set.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="set.titlepage.recto.style">
<xsl:apply-templates select="." mode="set.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="pubdate" mode="set.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="set.titlepage.recto.style">
<xsl:apply-templates select="." mode="set.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="revision" mode="set.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="set.titlepage.recto.style">
<xsl:apply-templates select="." mode="set.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="revhistory" mode="set.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="set.titlepage.recto.style">
<xsl:apply-templates select="." mode="set.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="abstract" mode="set.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="set.titlepage.recto.style">
<xsl:apply-templates select="." mode="set.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template name="book.titlepage.recto">
  <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="(bookinfo/title|title)[1]"/>
  <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="(bookinfo/subtitle|subtitle)[1]"/>
  <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="bookinfo/corpauthor"/>
  <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="bookinfo/authorgroup"/>
  <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="bookinfo/author"/>
  <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="bookinfo/releaseinfo"/>
  <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="bookinfo/copyright"/>
  <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="bookinfo/legalnotice"/>
  <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="bookinfo/pubdate"/>
  <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="bookinfo/revision"/>
  <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="bookinfo/revhistory"/>
  <xsl:apply-templates mode="book.titlepage.recto.auto.mode" select="bookinfo/abstract"/>
</xsl:template>

<xsl:template name="book.titlepage.verso">
</xsl:template>

<xsl:template name="book.titlepage.separator"><hr/>
</xsl:template>

<xsl:template name="book.titlepage.before.recto">
</xsl:template>

<xsl:template name="book.titlepage.before.verso">
</xsl:template>

<xsl:template name="book.titlepage">
  <div class="titlepage">
    <xsl:call-template name="book.titlepage.before.recto"/>
    <xsl:call-template name="book.titlepage.recto"/>
    <xsl:call-template name="book.titlepage.before.verso"/>
    <xsl:call-template name="book.titlepage.verso"/>
    <xsl:call-template name="book.titlepage.separator"/>
  </div>
</xsl:template>

<xsl:template match="*" mode="book.titlepage.recto.mode">
  <!-- if an element isn't found in this mode, -->
  <!-- try the generic titlepage.mode -->
  <xsl:apply-templates select="." mode="titlepage.mode"/>
</xsl:template>

<xsl:template match="*" mode="book.titlepage.verso.mode">
  <!-- if an element isn't found in this mode, -->
  <!-- try the generic titlepage.mode -->
  <xsl:apply-templates select="." mode="titlepage.mode"/>
</xsl:template>

<xsl:template match="title" mode="book.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="book.titlepage.recto.style">
<xsl:apply-templates select="." mode="book.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="subtitle" mode="book.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="book.titlepage.recto.style">
<xsl:apply-templates select="." mode="book.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="corpauthor" mode="book.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="book.titlepage.recto.style">
<xsl:apply-templates select="." mode="book.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="authorgroup" mode="book.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="book.titlepage.recto.style">
<xsl:apply-templates select="." mode="book.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="author" mode="book.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="book.titlepage.recto.style">
<xsl:apply-templates select="." mode="book.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="releaseinfo" mode="book.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="book.titlepage.recto.style">
<xsl:apply-templates select="." mode="book.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="copyright" mode="book.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="book.titlepage.recto.style">
<xsl:apply-templates select="." mode="book.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="legalnotice" mode="book.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="book.titlepage.recto.style">
<xsl:apply-templates select="." mode="book.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="pubdate" mode="book.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="book.titlepage.recto.style">
<xsl:apply-templates select="." mode="book.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="revision" mode="book.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="book.titlepage.recto.style">
<xsl:apply-templates select="." mode="book.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="revhistory" mode="book.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="book.titlepage.recto.style">
<xsl:apply-templates select="." mode="book.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="abstract" mode="book.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="book.titlepage.recto.style">
<xsl:apply-templates select="." mode="book.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template name="part.titlepage.recto">
  <xsl:apply-templates mode="part.titlepage.recto.auto.mode" select="(partinfo/title|docinfo/title|title)[1]"/>
  <xsl:apply-templates mode="part.titlepage.recto.auto.mode" select="(partinfo/subtitle|docinfo/subtitle|subtitle)[1]"/>
  <xsl:apply-templates mode="part.titlepage.recto.auto.mode" select="partinfo/corpauthor|docinfo/corpauthor"/>
  <xsl:apply-templates mode="part.titlepage.recto.auto.mode" select="partinfo/authorgroup|docinfo/authorgroup"/>
  <xsl:apply-templates mode="part.titlepage.recto.auto.mode" select="partinfo/author|docinfo/author"/>
  <xsl:apply-templates mode="part.titlepage.recto.auto.mode" select="partinfo/releaseinfo|docinfo/releaseinfo"/>
  <xsl:apply-templates mode="part.titlepage.recto.auto.mode" select="partinfo/copyright|docinfo/copyright"/>
  <xsl:apply-templates mode="part.titlepage.recto.auto.mode" select="partinfo/legalnotice|docinfo/legalnotice"/>
  <xsl:apply-templates mode="part.titlepage.recto.auto.mode" select="partinfo/pubdate|docinfo/pubdate"/>
  <xsl:apply-templates mode="part.titlepage.recto.auto.mode" select="partinfo/revision|docinfo/revision"/>
  <xsl:apply-templates mode="part.titlepage.recto.auto.mode" select="partinfo/revhistory|docinfo/revhistory"/>
  <xsl:apply-templates mode="part.titlepage.recto.auto.mode" select="partinfo/abstract|docinfo/abstract"/>
</xsl:template>

<xsl:template name="part.titlepage.verso">
</xsl:template>

<xsl:template name="part.titlepage.separator">
</xsl:template>

<xsl:template name="part.titlepage.before.recto">
</xsl:template>

<xsl:template name="part.titlepage.before.verso">
</xsl:template>

<xsl:template name="part.titlepage">
  <div class="titlepage">
    <xsl:call-template name="part.titlepage.before.recto"/>
    <xsl:call-template name="part.titlepage.recto"/>
    <xsl:call-template name="part.titlepage.before.verso"/>
    <xsl:call-template name="part.titlepage.verso"/>
    <xsl:call-template name="part.titlepage.separator"/>
  </div>
</xsl:template>

<xsl:template match="*" mode="part.titlepage.recto.mode">
  <!-- if an element isn't found in this mode, -->
  <!-- try the generic titlepage.mode -->
  <xsl:apply-templates select="." mode="titlepage.mode"/>
</xsl:template>

<xsl:template match="*" mode="part.titlepage.verso.mode">
  <!-- if an element isn't found in this mode, -->
  <!-- try the generic titlepage.mode -->
  <xsl:apply-templates select="." mode="titlepage.mode"/>
</xsl:template>

<xsl:template match="title" mode="part.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="part.titlepage.recto.style">
<xsl:apply-templates select="." mode="part.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="subtitle" mode="part.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="part.titlepage.recto.style">
<xsl:apply-templates select="." mode="part.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="corpauthor" mode="part.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="part.titlepage.recto.style">
<xsl:apply-templates select="." mode="part.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="authorgroup" mode="part.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="part.titlepage.recto.style">
<xsl:apply-templates select="." mode="part.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="author" mode="part.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="part.titlepage.recto.style">
<xsl:apply-templates select="." mode="part.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="releaseinfo" mode="part.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="part.titlepage.recto.style">
<xsl:apply-templates select="." mode="part.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="copyright" mode="part.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="part.titlepage.recto.style">
<xsl:apply-templates select="." mode="part.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="legalnotice" mode="part.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="part.titlepage.recto.style">
<xsl:apply-templates select="." mode="part.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="pubdate" mode="part.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="part.titlepage.recto.style">
<xsl:apply-templates select="." mode="part.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="revision" mode="part.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="part.titlepage.recto.style">
<xsl:apply-templates select="." mode="part.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="revhistory" mode="part.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="part.titlepage.recto.style">
<xsl:apply-templates select="." mode="part.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="abstract" mode="part.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="part.titlepage.recto.style">
<xsl:apply-templates select="." mode="part.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template name="partintro.titlepage.recto">
  <xsl:apply-templates mode="partintro.titlepage.recto.auto.mode" select="(partintroinfo/title|docinfo/title|title)[1]"/>
  <xsl:apply-templates mode="partintro.titlepage.recto.auto.mode" select="(partintroinfo/subtitle|docinfo/subtitle|subtitle)[1]"/>
  <xsl:apply-templates mode="partintro.titlepage.recto.auto.mode" select="partintroinfo/corpauthor|docinfo/corpauthor"/>
  <xsl:apply-templates mode="partintro.titlepage.recto.auto.mode" select="partintroinfo/authorgroup|docinfo/authorgroup"/>
  <xsl:apply-templates mode="partintro.titlepage.recto.auto.mode" select="partintroinfo/author|docinfo/author"/>
  <xsl:apply-templates mode="partintro.titlepage.recto.auto.mode" select="partintroinfo/releaseinfo|docinfo/releaseinfo"/>
  <xsl:apply-templates mode="partintro.titlepage.recto.auto.mode" select="partintroinfo/copyright|docinfo/copyright"/>
  <xsl:apply-templates mode="partintro.titlepage.recto.auto.mode" select="partintroinfo/legalnotice|docinfo/legalnotice"/>
  <xsl:apply-templates mode="partintro.titlepage.recto.auto.mode" select="partintroinfo/pubdate|docinfo/pubdate"/>
  <xsl:apply-templates mode="partintro.titlepage.recto.auto.mode" select="partintroinfo/revision|docinfo/revision"/>
  <xsl:apply-templates mode="partintro.titlepage.recto.auto.mode" select="partintroinfo/revhistory|docinfo/revhistory"/>
  <xsl:apply-templates mode="partintro.titlepage.recto.auto.mode" select="partintroinfo/abstract|docinfo/abstract"/>
</xsl:template>

<xsl:template name="partintro.titlepage.verso">
</xsl:template>

<xsl:template name="partintro.titlepage.separator">
</xsl:template>

<xsl:template name="partintro.titlepage.before.recto">
</xsl:template>

<xsl:template name="partintro.titlepage.before.verso">
</xsl:template>

<xsl:template name="partintro.titlepage">
  <div>
    <xsl:call-template name="partintro.titlepage.before.recto"/>
    <xsl:call-template name="partintro.titlepage.recto"/>
    <xsl:call-template name="partintro.titlepage.before.verso"/>
    <xsl:call-template name="partintro.titlepage.verso"/>
    <xsl:call-template name="partintro.titlepage.separator"/>
  </div>
</xsl:template>

<xsl:template match="*" mode="partintro.titlepage.recto.mode">
  <!-- if an element isn't found in this mode, -->
  <!-- try the generic titlepage.mode -->
  <xsl:apply-templates select="." mode="titlepage.mode"/>
</xsl:template>

<xsl:template match="*" mode="partintro.titlepage.verso.mode">
  <!-- if an element isn't found in this mode, -->
  <!-- try the generic titlepage.mode -->
  <xsl:apply-templates select="." mode="titlepage.mode"/>
</xsl:template>

<xsl:template match="title" mode="partintro.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="partintro.titlepage.recto.style">
<xsl:apply-templates select="." mode="partintro.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="subtitle" mode="partintro.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="partintro.titlepage.recto.style">
<xsl:apply-templates select="." mode="partintro.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="corpauthor" mode="partintro.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="partintro.titlepage.recto.style">
<xsl:apply-templates select="." mode="partintro.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="authorgroup" mode="partintro.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="partintro.titlepage.recto.style">
<xsl:apply-templates select="." mode="partintro.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="author" mode="partintro.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="partintro.titlepage.recto.style">
<xsl:apply-templates select="." mode="partintro.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="releaseinfo" mode="partintro.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="partintro.titlepage.recto.style">
<xsl:apply-templates select="." mode="partintro.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="copyright" mode="partintro.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="partintro.titlepage.recto.style">
<xsl:apply-templates select="." mode="partintro.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="legalnotice" mode="partintro.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="partintro.titlepage.recto.style">
<xsl:apply-templates select="." mode="partintro.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="pubdate" mode="partintro.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="partintro.titlepage.recto.style">
<xsl:apply-templates select="." mode="partintro.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="revision" mode="partintro.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="partintro.titlepage.recto.style">
<xsl:apply-templates select="." mode="partintro.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="revhistory" mode="partintro.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="partintro.titlepage.recto.style">
<xsl:apply-templates select="." mode="partintro.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="abstract" mode="partintro.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="partintro.titlepage.recto.style">
<xsl:apply-templates select="." mode="partintro.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template name="reference.titlepage.recto">
  <xsl:apply-templates mode="reference.titlepage.recto.auto.mode" select="(referenceinfo/title|docinfo/title|title)[1]"/>
  <xsl:apply-templates mode="reference.titlepage.recto.auto.mode" select="(referenceinfo/subtitle|docinfo/subtitle|subtitle)[1]"/>
  <xsl:apply-templates mode="reference.titlepage.recto.auto.mode" select="referenceinfo/corpauthor|docinfo/corpauthor"/>
  <xsl:apply-templates mode="reference.titlepage.recto.auto.mode" select="referenceinfo/authorgroup|docinfo/authorgroup"/>
  <xsl:apply-templates mode="reference.titlepage.recto.auto.mode" select="referenceinfo/author|docinfo/author"/>
  <xsl:apply-templates mode="reference.titlepage.recto.auto.mode" select="referenceinfo/releaseinfo|docinfo/releaseinfo"/>
  <xsl:apply-templates mode="reference.titlepage.recto.auto.mode" select="referenceinfo/copyright|docinfo/copyright"/>
  <xsl:apply-templates mode="reference.titlepage.recto.auto.mode" select="referenceinfo/legalnotice|docinfo/legalnotice"/>
  <xsl:apply-templates mode="reference.titlepage.recto.auto.mode" select="referenceinfo/pubdate|docinfo/pubdate"/>
  <xsl:apply-templates mode="reference.titlepage.recto.auto.mode" select="referenceinfo/revision|docinfo/revision"/>
  <xsl:apply-templates mode="reference.titlepage.recto.auto.mode" select="referenceinfo/revhistory|docinfo/revhistory"/>
  <xsl:apply-templates mode="reference.titlepage.recto.auto.mode" select="referenceinfo/abstract|docinfo/abstract"/>
</xsl:template>

<xsl:template name="reference.titlepage.verso">
</xsl:template>

<xsl:template name="reference.titlepage.separator"><hr/>
</xsl:template>

<xsl:template name="reference.titlepage.before.recto">
</xsl:template>

<xsl:template name="reference.titlepage.before.verso">
</xsl:template>

<xsl:template name="reference.titlepage">
  <div class="titlepage">
    <xsl:call-template name="reference.titlepage.before.recto"/>
    <xsl:call-template name="reference.titlepage.recto"/>
    <xsl:call-template name="reference.titlepage.before.verso"/>
    <xsl:call-template name="reference.titlepage.verso"/>
    <xsl:call-template name="reference.titlepage.separator"/>
  </div>
</xsl:template>

<xsl:template match="*" mode="reference.titlepage.recto.mode">
  <!-- if an element isn't found in this mode, -->
  <!-- try the generic titlepage.mode -->
  <xsl:apply-templates select="." mode="titlepage.mode"/>
</xsl:template>

<xsl:template match="*" mode="reference.titlepage.verso.mode">
  <!-- if an element isn't found in this mode, -->
  <!-- try the generic titlepage.mode -->
  <xsl:apply-templates select="." mode="titlepage.mode"/>
</xsl:template>

<xsl:template match="title" mode="reference.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="reference.titlepage.recto.style">
<xsl:apply-templates select="." mode="reference.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="subtitle" mode="reference.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="reference.titlepage.recto.style">
<xsl:apply-templates select="." mode="reference.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="corpauthor" mode="reference.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="reference.titlepage.recto.style">
<xsl:apply-templates select="." mode="reference.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="authorgroup" mode="reference.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="reference.titlepage.recto.style">
<xsl:apply-templates select="." mode="reference.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="author" mode="reference.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="reference.titlepage.recto.style">
<xsl:apply-templates select="." mode="reference.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="releaseinfo" mode="reference.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="reference.titlepage.recto.style">
<xsl:apply-templates select="." mode="reference.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="copyright" mode="reference.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="reference.titlepage.recto.style">
<xsl:apply-templates select="." mode="reference.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="legalnotice" mode="reference.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="reference.titlepage.recto.style">
<xsl:apply-templates select="." mode="reference.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="pubdate" mode="reference.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="reference.titlepage.recto.style">
<xsl:apply-templates select="." mode="reference.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="revision" mode="reference.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="reference.titlepage.recto.style">
<xsl:apply-templates select="." mode="reference.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="revhistory" mode="reference.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="reference.titlepage.recto.style">
<xsl:apply-templates select="." mode="reference.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="abstract" mode="reference.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="reference.titlepage.recto.style">
<xsl:apply-templates select="." mode="reference.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template name="dedication.titlepage.recto">
  <div xsl:use-attribute-sets="dedication.titlepage.recto.style">
<xsl:call-template name="component.title">
<xsl:with-param name="node" select="ancestor-or-self::dedication[1]"/>
</xsl:call-template></div>
  <xsl:apply-templates mode="dedication.titlepage.recto.auto.mode" select="(dedicationinfo/subtitle|docinfo/subtitle|subtitle)[1]"/>
</xsl:template>

<xsl:template name="dedication.titlepage.verso">
</xsl:template>

<xsl:template name="dedication.titlepage.separator">
</xsl:template>

<xsl:template name="dedication.titlepage.before.recto">
</xsl:template>

<xsl:template name="dedication.titlepage.before.verso">
</xsl:template>

<xsl:template name="dedication.titlepage">
  <div class="titlepage">
    <xsl:call-template name="dedication.titlepage.before.recto"/>
    <xsl:call-template name="dedication.titlepage.recto"/>
    <xsl:call-template name="dedication.titlepage.before.verso"/>
    <xsl:call-template name="dedication.titlepage.verso"/>
    <xsl:call-template name="dedication.titlepage.separator"/>
  </div>
</xsl:template>

<xsl:template match="*" mode="dedication.titlepage.recto.mode">
  <!-- if an element isn't found in this mode, -->
  <!-- try the generic titlepage.mode -->
  <xsl:apply-templates select="." mode="titlepage.mode"/>
</xsl:template>

<xsl:template match="*" mode="dedication.titlepage.verso.mode">
  <!-- if an element isn't found in this mode, -->
  <!-- try the generic titlepage.mode -->
  <xsl:apply-templates select="." mode="titlepage.mode"/>
</xsl:template>

<xsl:template match="subtitle" mode="dedication.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="dedication.titlepage.recto.style">
<xsl:apply-templates select="." mode="dedication.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template name="preface.titlepage.recto">
  <div xsl:use-attribute-sets="preface.titlepage.recto.style">
<xsl:call-template name="component.title">
<xsl:with-param name="node" select="ancestor-or-self::preface[1]"/>
</xsl:call-template></div>
  <xsl:apply-templates mode="preface.titlepage.recto.auto.mode" select="(prefaceinfo/subtitle|docinfo/subtitle|subtitle)[1]"/>
  <xsl:apply-templates mode="preface.titlepage.recto.auto.mode" select="prefaceinfo/corpauthor|docinfo/corpauthor"/>
  <xsl:apply-templates mode="preface.titlepage.recto.auto.mode" select="prefaceinfo/authorgroup|docinfo/authorgroup"/>
  <xsl:apply-templates mode="preface.titlepage.recto.auto.mode" select="prefaceinfo/author|docinfo/author"/>
  <xsl:apply-templates mode="preface.titlepage.recto.auto.mode" select="prefaceinfo/releaseinfo|docinfo/releaseinfo"/>
  <xsl:apply-templates mode="preface.titlepage.recto.auto.mode" select="prefaceinfo/copyright|docinfo/copyright"/>
  <xsl:apply-templates mode="preface.titlepage.recto.auto.mode" select="prefaceinfo/legalnotice|docinfo/legalnotice"/>
  <xsl:apply-templates mode="preface.titlepage.recto.auto.mode" select="prefaceinfo/pubdate|docinfo/pubdate"/>
  <xsl:apply-templates mode="preface.titlepage.recto.auto.mode" select="prefaceinfo/revision|docinfo/revision"/>
  <xsl:apply-templates mode="preface.titlepage.recto.auto.mode" select="prefaceinfo/revhistory|docinfo/revhistory"/>
  <xsl:apply-templates mode="preface.titlepage.recto.auto.mode" select="prefaceinfo/abstract|docinfo/abstract"/>
</xsl:template>

<xsl:template name="preface.titlepage.verso">
</xsl:template>

<xsl:template name="preface.titlepage.separator">
</xsl:template>

<xsl:template name="preface.titlepage.before.recto">
</xsl:template>

<xsl:template name="preface.titlepage.before.verso">
</xsl:template>

<xsl:template name="preface.titlepage">
  <div class="titlepage">
    <xsl:call-template name="preface.titlepage.before.recto"/>
    <xsl:call-template name="preface.titlepage.recto"/>
    <xsl:call-template name="preface.titlepage.before.verso"/>
    <xsl:call-template name="preface.titlepage.verso"/>
    <xsl:call-template name="preface.titlepage.separator"/>
  </div>
</xsl:template>

<xsl:template match="*" mode="preface.titlepage.recto.mode">
  <!-- if an element isn't found in this mode, -->
  <!-- try the generic titlepage.mode -->
  <xsl:apply-templates select="." mode="titlepage.mode"/>
</xsl:template>

<xsl:template match="*" mode="preface.titlepage.verso.mode">
  <!-- if an element isn't found in this mode, -->
  <!-- try the generic titlepage.mode -->
  <xsl:apply-templates select="." mode="titlepage.mode"/>
</xsl:template>

<xsl:template match="subtitle" mode="preface.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="preface.titlepage.recto.style">
<xsl:apply-templates select="." mode="preface.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="corpauthor" mode="preface.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="preface.titlepage.recto.style">
<xsl:apply-templates select="." mode="preface.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="authorgroup" mode="preface.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="preface.titlepage.recto.style">
<xsl:apply-templates select="." mode="preface.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="author" mode="preface.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="preface.titlepage.recto.style">
<xsl:apply-templates select="." mode="preface.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="releaseinfo" mode="preface.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="preface.titlepage.recto.style">
<xsl:apply-templates select="." mode="preface.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="copyright" mode="preface.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="preface.titlepage.recto.style">
<xsl:apply-templates select="." mode="preface.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="legalnotice" mode="preface.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="preface.titlepage.recto.style">
<xsl:apply-templates select="." mode="preface.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="pubdate" mode="preface.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="preface.titlepage.recto.style">
<xsl:apply-templates select="." mode="preface.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="revision" mode="preface.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="preface.titlepage.recto.style">
<xsl:apply-templates select="." mode="preface.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="revhistory" mode="preface.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="preface.titlepage.recto.style">
<xsl:apply-templates select="." mode="preface.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="abstract" mode="preface.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="preface.titlepage.recto.style">
<xsl:apply-templates select="." mode="preface.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template name="chapter.titlepage.recto">
  <xsl:apply-templates mode="chapter.titlepage.recto.auto.mode" select="(chapterinfo/title|docinfo/title|title)[1]"/>
  <xsl:apply-templates mode="chapter.titlepage.recto.auto.mode" select="(chapterinfo/subtitle|docinfo/subtitle|subtitle)[1]"/>
  <xsl:apply-templates mode="chapter.titlepage.recto.auto.mode" select="chapterinfo/corpauthor|docinfo/corpauthor"/>
  <xsl:apply-templates mode="chapter.titlepage.recto.auto.mode" select="chapterinfo/authorgroup|docinfo/authorgroup"/>
  <xsl:apply-templates mode="chapter.titlepage.recto.auto.mode" select="chapterinfo/author|docinfo/author"/>
  <xsl:apply-templates mode="chapter.titlepage.recto.auto.mode" select="chapterinfo/releaseinfo|docinfo/releaseinfo"/>
  <xsl:apply-templates mode="chapter.titlepage.recto.auto.mode" select="chapterinfo/copyright|docinfo/copyright"/>
  <xsl:apply-templates mode="chapter.titlepage.recto.auto.mode" select="chapterinfo/legalnotice|docinfo/legalnotice"/>
  <xsl:apply-templates mode="chapter.titlepage.recto.auto.mode" select="chapterinfo/pubdate|docinfo/pubdate"/>
  <xsl:apply-templates mode="chapter.titlepage.recto.auto.mode" select="chapterinfo/revision|docinfo/revision"/>
  <xsl:apply-templates mode="chapter.titlepage.recto.auto.mode" select="chapterinfo/revhistory|docinfo/revhistory"/>
  <xsl:apply-templates mode="chapter.titlepage.recto.auto.mode" select="chapterinfo/abstract|docinfo/abstract"/>
</xsl:template>

<xsl:template name="chapter.titlepage.verso">
</xsl:template>

<xsl:template name="chapter.titlepage.separator">
</xsl:template>

<xsl:template name="chapter.titlepage.before.recto">
</xsl:template>

<xsl:template name="chapter.titlepage.before.verso">
</xsl:template>

<xsl:template name="chapter.titlepage">
  <div class="titlepage">
    <xsl:call-template name="chapter.titlepage.before.recto"/>
    <xsl:call-template name="chapter.titlepage.recto"/>
    <xsl:call-template name="chapter.titlepage.before.verso"/>
    <xsl:call-template name="chapter.titlepage.verso"/>
    <xsl:call-template name="chapter.titlepage.separator"/>
  </div>
</xsl:template>

<xsl:template match="*" mode="chapter.titlepage.recto.mode">
  <!-- if an element isn't found in this mode, -->
  <!-- try the generic titlepage.mode -->
  <xsl:apply-templates select="." mode="titlepage.mode"/>
</xsl:template>

<xsl:template match="*" mode="chapter.titlepage.verso.mode">
  <!-- if an element isn't found in this mode, -->
  <!-- try the generic titlepage.mode -->
  <xsl:apply-templates select="." mode="titlepage.mode"/>
</xsl:template>

<xsl:template match="title" mode="chapter.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="chapter.titlepage.recto.style">
<xsl:apply-templates select="." mode="chapter.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="subtitle" mode="chapter.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="chapter.titlepage.recto.style">
<xsl:apply-templates select="." mode="chapter.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="corpauthor" mode="chapter.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="chapter.titlepage.recto.style">
<xsl:apply-templates select="." mode="chapter.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="authorgroup" mode="chapter.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="chapter.titlepage.recto.style">
<xsl:apply-templates select="." mode="chapter.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="author" mode="chapter.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="chapter.titlepage.recto.style">
<xsl:apply-templates select="." mode="chapter.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="releaseinfo" mode="chapter.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="chapter.titlepage.recto.style">
<xsl:apply-templates select="." mode="chapter.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="copyright" mode="chapter.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="chapter.titlepage.recto.style">
<xsl:apply-templates select="." mode="chapter.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="legalnotice" mode="chapter.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="chapter.titlepage.recto.style">
<xsl:apply-templates select="." mode="chapter.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="pubdate" mode="chapter.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="chapter.titlepage.recto.style">
<xsl:apply-templates select="." mode="chapter.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="revision" mode="chapter.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="chapter.titlepage.recto.style">
<xsl:apply-templates select="." mode="chapter.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="revhistory" mode="chapter.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="chapter.titlepage.recto.style">
<xsl:apply-templates select="." mode="chapter.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="abstract" mode="chapter.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="chapter.titlepage.recto.style">
<xsl:apply-templates select="." mode="chapter.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template name="appendix.titlepage.recto">
  <xsl:apply-templates mode="appendix.titlepage.recto.auto.mode" select="(appendixinfo/title|docinfo/title|title)[1]"/>
  <xsl:apply-templates mode="appendix.titlepage.recto.auto.mode" select="(appendixinfo/subtitle|docinfo/subtitle|subtitle)[1]"/>
  <xsl:apply-templates mode="appendix.titlepage.recto.auto.mode" select="appendixinfo/corpauthor|docinfo/corpauthor"/>
  <xsl:apply-templates mode="appendix.titlepage.recto.auto.mode" select="appendixinfo/authorgroup|docinfo/authorgroup"/>
  <xsl:apply-templates mode="appendix.titlepage.recto.auto.mode" select="appendixinfo/author|docinfo/author"/>
  <xsl:apply-templates mode="appendix.titlepage.recto.auto.mode" select="appendixinfo/releaseinfo|docinfo/releaseinfo"/>
  <xsl:apply-templates mode="appendix.titlepage.recto.auto.mode" select="appendixinfo/copyright|docinfo/copyright"/>
  <xsl:apply-templates mode="appendix.titlepage.recto.auto.mode" select="appendixinfo/legalnotice|docinfo/legalnotice"/>
  <xsl:apply-templates mode="appendix.titlepage.recto.auto.mode" select="appendixinfo/pubdate|docinfo/pubdate"/>
  <xsl:apply-templates mode="appendix.titlepage.recto.auto.mode" select="appendixinfo/revision|docinfo/revision"/>
  <xsl:apply-templates mode="appendix.titlepage.recto.auto.mode" select="appendixinfo/revhistory|docinfo/revhistory"/>
  <xsl:apply-templates mode="appendix.titlepage.recto.auto.mode" select="appendixinfo/abstract|docinfo/abstract"/>
</xsl:template>

<xsl:template name="appendix.titlepage.verso">
</xsl:template>

<xsl:template name="appendix.titlepage.separator">
</xsl:template>

<xsl:template name="appendix.titlepage.before.recto">
</xsl:template>

<xsl:template name="appendix.titlepage.before.verso">
</xsl:template>

<xsl:template name="appendix.titlepage">
  <div class="titlepage">
    <xsl:call-template name="appendix.titlepage.before.recto"/>
    <xsl:call-template name="appendix.titlepage.recto"/>
    <xsl:call-template name="appendix.titlepage.before.verso"/>
    <xsl:call-template name="appendix.titlepage.verso"/>
    <xsl:call-template name="appendix.titlepage.separator"/>
  </div>
</xsl:template>

<xsl:template match="*" mode="appendix.titlepage.recto.mode">
  <!-- if an element isn't found in this mode, -->
  <!-- try the generic titlepage.mode -->
  <xsl:apply-templates select="." mode="titlepage.mode"/>
</xsl:template>

<xsl:template match="*" mode="appendix.titlepage.verso.mode">
  <!-- if an element isn't found in this mode, -->
  <!-- try the generic titlepage.mode -->
  <xsl:apply-templates select="." mode="titlepage.mode"/>
</xsl:template>

<xsl:template match="title" mode="appendix.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="appendix.titlepage.recto.style">
<xsl:apply-templates select="." mode="appendix.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="subtitle" mode="appendix.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="appendix.titlepage.recto.style">
<xsl:apply-templates select="." mode="appendix.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="corpauthor" mode="appendix.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="appendix.titlepage.recto.style">
<xsl:apply-templates select="." mode="appendix.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="authorgroup" mode="appendix.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="appendix.titlepage.recto.style">
<xsl:apply-templates select="." mode="appendix.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="author" mode="appendix.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="appendix.titlepage.recto.style">
<xsl:apply-templates select="." mode="appendix.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="releaseinfo" mode="appendix.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="appendix.titlepage.recto.style">
<xsl:apply-templates select="." mode="appendix.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="copyright" mode="appendix.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="appendix.titlepage.recto.style">
<xsl:apply-templates select="." mode="appendix.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="legalnotice" mode="appendix.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="appendix.titlepage.recto.style">
<xsl:apply-templates select="." mode="appendix.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="pubdate" mode="appendix.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="appendix.titlepage.recto.style">
<xsl:apply-templates select="." mode="appendix.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="revision" mode="appendix.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="appendix.titlepage.recto.style">
<xsl:apply-templates select="." mode="appendix.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="revhistory" mode="appendix.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="appendix.titlepage.recto.style">
<xsl:apply-templates select="." mode="appendix.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="abstract" mode="appendix.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="appendix.titlepage.recto.style">
<xsl:apply-templates select="." mode="appendix.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template name="section.titlepage.recto">
  <xsl:apply-templates mode="section.titlepage.recto.auto.mode" select="(sectioninfo/title|title)[1]"/>
  <xsl:apply-templates mode="section.titlepage.recto.auto.mode" select="(sectioninfo/subtitle|subtitle)[1]"/>
  <xsl:apply-templates mode="section.titlepage.recto.auto.mode" select="sectioninfo/corpauthor"/>
  <xsl:apply-templates mode="section.titlepage.recto.auto.mode" select="sectioninfo/authorgroup"/>
  <xsl:apply-templates mode="section.titlepage.recto.auto.mode" select="sectioninfo/author"/>
  <xsl:apply-templates mode="section.titlepage.recto.auto.mode" select="sectioninfo/releaseinfo"/>
  <xsl:apply-templates mode="section.titlepage.recto.auto.mode" select="sectioninfo/copyright"/>
  <xsl:apply-templates mode="section.titlepage.recto.auto.mode" select="sectioninfo/legalnotice"/>
  <xsl:apply-templates mode="section.titlepage.recto.auto.mode" select="sectioninfo/pubdate"/>
  <xsl:apply-templates mode="section.titlepage.recto.auto.mode" select="sectioninfo/revision"/>
  <xsl:apply-templates mode="section.titlepage.recto.auto.mode" select="sectioninfo/revhistory"/>
  <xsl:apply-templates mode="section.titlepage.recto.auto.mode" select="sectioninfo/abstract"/>
</xsl:template>

<xsl:template name="section.titlepage.verso">
</xsl:template>

<xsl:template name="section.titlepage.separator"><xsl:if test="count(parent::*)='0'"><hr/></xsl:if>
</xsl:template>

<xsl:template name="section.titlepage.before.recto">
</xsl:template>

<xsl:template name="section.titlepage.before.verso">
</xsl:template>

<xsl:template name="section.titlepage">
  <div class="titlepage">
    <xsl:call-template name="section.titlepage.before.recto"/>
    <xsl:call-template name="section.titlepage.recto"/>
    <xsl:call-template name="section.titlepage.before.verso"/>
    <xsl:call-template name="section.titlepage.verso"/>
    <xsl:call-template name="section.titlepage.separator"/>
  </div>
</xsl:template>

<xsl:template match="*" mode="section.titlepage.recto.mode">
  <!-- if an element isn't found in this mode, -->
  <!-- try the generic titlepage.mode -->
  <xsl:apply-templates select="." mode="titlepage.mode"/>
</xsl:template>

<xsl:template match="*" mode="section.titlepage.verso.mode">
  <!-- if an element isn't found in this mode, -->
  <!-- try the generic titlepage.mode -->
  <xsl:apply-templates select="." mode="titlepage.mode"/>
</xsl:template>

<xsl:template match="title" mode="section.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="section.titlepage.recto.style">
<xsl:apply-templates select="." mode="section.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="subtitle" mode="section.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="section.titlepage.recto.style">
<xsl:apply-templates select="." mode="section.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="corpauthor" mode="section.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="section.titlepage.recto.style">
<xsl:apply-templates select="." mode="section.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="authorgroup" mode="section.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="section.titlepage.recto.style">
<xsl:apply-templates select="." mode="section.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="author" mode="section.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="section.titlepage.recto.style">
<xsl:apply-templates select="." mode="section.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="releaseinfo" mode="section.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="section.titlepage.recto.style">
<xsl:apply-templates select="." mode="section.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="copyright" mode="section.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="section.titlepage.recto.style">
<xsl:apply-templates select="." mode="section.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="legalnotice" mode="section.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="section.titlepage.recto.style">
<xsl:apply-templates select="." mode="section.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="pubdate" mode="section.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="section.titlepage.recto.style">
<xsl:apply-templates select="." mode="section.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="revision" mode="section.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="section.titlepage.recto.style">
<xsl:apply-templates select="." mode="section.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="revhistory" mode="section.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="section.titlepage.recto.style">
<xsl:apply-templates select="." mode="section.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="abstract" mode="section.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="section.titlepage.recto.style">
<xsl:apply-templates select="." mode="section.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template name="sect1.titlepage.recto">
  <xsl:apply-templates mode="sect1.titlepage.recto.auto.mode" select="(sect1info/title|title)[1]"/>
  <xsl:apply-templates mode="sect1.titlepage.recto.auto.mode" select="(sect1info/subtitle|subtitle)[1]"/>
  <xsl:apply-templates mode="sect1.titlepage.recto.auto.mode" select="sect1info/corpauthor"/>
  <xsl:apply-templates mode="sect1.titlepage.recto.auto.mode" select="sect1info/authorgroup"/>
  <xsl:apply-templates mode="sect1.titlepage.recto.auto.mode" select="sect1info/author"/>
  <xsl:apply-templates mode="sect1.titlepage.recto.auto.mode" select="sect1info/releaseinfo"/>
  <xsl:apply-templates mode="sect1.titlepage.recto.auto.mode" select="sect1info/copyright"/>
  <xsl:apply-templates mode="sect1.titlepage.recto.auto.mode" select="sect1info/legalnotice"/>
  <xsl:apply-templates mode="sect1.titlepage.recto.auto.mode" select="sect1info/pubdate"/>
  <xsl:apply-templates mode="sect1.titlepage.recto.auto.mode" select="sect1info/revision"/>
  <xsl:apply-templates mode="sect1.titlepage.recto.auto.mode" select="sect1info/revhistory"/>
  <xsl:apply-templates mode="sect1.titlepage.recto.auto.mode" select="sect1info/abstract"/>
</xsl:template>

<xsl:template name="sect1.titlepage.verso">
</xsl:template>

<xsl:template name="sect1.titlepage.separator"><xsl:if test="count(parent::*)='0'"><hr/></xsl:if>
</xsl:template>

<xsl:template name="sect1.titlepage.before.recto">
</xsl:template>

<xsl:template name="sect1.titlepage.before.verso">
</xsl:template>

<xsl:template name="sect1.titlepage">
  <div class="titlepage">
    <xsl:call-template name="sect1.titlepage.before.recto"/>
    <xsl:call-template name="sect1.titlepage.recto"/>
    <xsl:call-template name="sect1.titlepage.before.verso"/>
    <xsl:call-template name="sect1.titlepage.verso"/>
    <xsl:call-template name="sect1.titlepage.separator"/>
  </div>
</xsl:template>

<xsl:template match="*" mode="sect1.titlepage.recto.mode">
  <!-- if an element isn't found in this mode, -->
  <!-- try the generic titlepage.mode -->
  <xsl:apply-templates select="." mode="titlepage.mode"/>
</xsl:template>

<xsl:template match="*" mode="sect1.titlepage.verso.mode">
  <!-- if an element isn't found in this mode, -->
  <!-- try the generic titlepage.mode -->
  <xsl:apply-templates select="." mode="titlepage.mode"/>
</xsl:template>

<xsl:template match="title" mode="sect1.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="sect1.titlepage.recto.style">
<xsl:apply-templates select="." mode="sect1.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="subtitle" mode="sect1.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="sect1.titlepage.recto.style">
<xsl:apply-templates select="." mode="sect1.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="corpauthor" mode="sect1.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="sect1.titlepage.recto.style">
<xsl:apply-templates select="." mode="sect1.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="authorgroup" mode="sect1.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="sect1.titlepage.recto.style">
<xsl:apply-templates select="." mode="sect1.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="author" mode="sect1.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="sect1.titlepage.recto.style">
<xsl:apply-templates select="." mode="sect1.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="releaseinfo" mode="sect1.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="sect1.titlepage.recto.style">
<xsl:apply-templates select="." mode="sect1.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="copyright" mode="sect1.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="sect1.titlepage.recto.style">
<xsl:apply-templates select="." mode="sect1.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="legalnotice" mode="sect1.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="sect1.titlepage.recto.style">
<xsl:apply-templates select="." mode="sect1.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="pubdate" mode="sect1.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="sect1.titlepage.recto.style">
<xsl:apply-templates select="." mode="sect1.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="revision" mode="sect1.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="sect1.titlepage.recto.style">
<xsl:apply-templates select="." mode="sect1.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="revhistory" mode="sect1.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="sect1.titlepage.recto.style">
<xsl:apply-templates select="." mode="sect1.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="abstract" mode="sect1.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="sect1.titlepage.recto.style">
<xsl:apply-templates select="." mode="sect1.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template name="sect2.titlepage.recto">
  <xsl:apply-templates mode="sect2.titlepage.recto.auto.mode" select="(sect2info/title|title)[1]"/>
  <xsl:apply-templates mode="sect2.titlepage.recto.auto.mode" select="(sect2info/subtitle|subtitle)[1]"/>
  <xsl:apply-templates mode="sect2.titlepage.recto.auto.mode" select="sect2info/corpauthor"/>
  <xsl:apply-templates mode="sect2.titlepage.recto.auto.mode" select="sect2info/authorgroup"/>
  <xsl:apply-templates mode="sect2.titlepage.recto.auto.mode" select="sect2info/author"/>
  <xsl:apply-templates mode="sect2.titlepage.recto.auto.mode" select="sect2info/releaseinfo"/>
  <xsl:apply-templates mode="sect2.titlepage.recto.auto.mode" select="sect2info/copyright"/>
  <xsl:apply-templates mode="sect2.titlepage.recto.auto.mode" select="sect2info/legalnotice"/>
  <xsl:apply-templates mode="sect2.titlepage.recto.auto.mode" select="sect2info/pubdate"/>
  <xsl:apply-templates mode="sect2.titlepage.recto.auto.mode" select="sect2info/revision"/>
  <xsl:apply-templates mode="sect2.titlepage.recto.auto.mode" select="sect2info/revhistory"/>
  <xsl:apply-templates mode="sect2.titlepage.recto.auto.mode" select="sect2info/abstract"/>
</xsl:template>

<xsl:template name="sect2.titlepage.verso">
</xsl:template>

<xsl:template name="sect2.titlepage.separator"><xsl:if test="count(parent::*)='0'"><hr/></xsl:if>
</xsl:template>

<xsl:template name="sect2.titlepage.before.recto">
</xsl:template>

<xsl:template name="sect2.titlepage.before.verso">
</xsl:template>

<xsl:template name="sect2.titlepage">
  <div class="titlepage">
    <xsl:call-template name="sect2.titlepage.before.recto"/>
    <xsl:call-template name="sect2.titlepage.recto"/>
    <xsl:call-template name="sect2.titlepage.before.verso"/>
    <xsl:call-template name="sect2.titlepage.verso"/>
    <xsl:call-template name="sect2.titlepage.separator"/>
  </div>
</xsl:template>

<xsl:template match="*" mode="sect2.titlepage.recto.mode">
  <!-- if an element isn't found in this mode, -->
  <!-- try the generic titlepage.mode -->
  <xsl:apply-templates select="." mode="titlepage.mode"/>
</xsl:template>

<xsl:template match="*" mode="sect2.titlepage.verso.mode">
  <!-- if an element isn't found in this mode, -->
  <!-- try the generic titlepage.mode -->
  <xsl:apply-templates select="." mode="titlepage.mode"/>
</xsl:template>

<xsl:template match="title" mode="sect2.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="sect2.titlepage.recto.style">
<xsl:apply-templates select="." mode="sect2.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="subtitle" mode="sect2.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="sect2.titlepage.recto.style">
<xsl:apply-templates select="." mode="sect2.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="corpauthor" mode="sect2.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="sect2.titlepage.recto.style">
<xsl:apply-templates select="." mode="sect2.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="authorgroup" mode="sect2.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="sect2.titlepage.recto.style">
<xsl:apply-templates select="." mode="sect2.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="author" mode="sect2.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="sect2.titlepage.recto.style">
<xsl:apply-templates select="." mode="sect2.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="releaseinfo" mode="sect2.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="sect2.titlepage.recto.style">
<xsl:apply-templates select="." mode="sect2.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="copyright" mode="sect2.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="sect2.titlepage.recto.style">
<xsl:apply-templates select="." mode="sect2.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="legalnotice" mode="sect2.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="sect2.titlepage.recto.style">
<xsl:apply-templates select="." mode="sect2.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="pubdate" mode="sect2.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="sect2.titlepage.recto.style">
<xsl:apply-templates select="." mode="sect2.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="revision" mode="sect2.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="sect2.titlepage.recto.style">
<xsl:apply-templates select="." mode="sect2.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="revhistory" mode="sect2.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="sect2.titlepage.recto.style">
<xsl:apply-templates select="." mode="sect2.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="abstract" mode="sect2.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="sect2.titlepage.recto.style">
<xsl:apply-templates select="." mode="sect2.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template name="sect3.titlepage.recto">
  <xsl:apply-templates mode="sect3.titlepage.recto.auto.mode" select="(sect3info/title|title)[1]"/>
  <xsl:apply-templates mode="sect3.titlepage.recto.auto.mode" select="(sect3info/subtitle|subtitle)[1]"/>
  <xsl:apply-templates mode="sect3.titlepage.recto.auto.mode" select="sect3info/corpauthor"/>
  <xsl:apply-templates mode="sect3.titlepage.recto.auto.mode" select="sect3info/authorgroup"/>
  <xsl:apply-templates mode="sect3.titlepage.recto.auto.mode" select="sect3info/author"/>
  <xsl:apply-templates mode="sect3.titlepage.recto.auto.mode" select="sect3info/releaseinfo"/>
  <xsl:apply-templates mode="sect3.titlepage.recto.auto.mode" select="sect3info/copyright"/>
  <xsl:apply-templates mode="sect3.titlepage.recto.auto.mode" select="sect3info/legalnotice"/>
  <xsl:apply-templates mode="sect3.titlepage.recto.auto.mode" select="sect3info/pubdate"/>
  <xsl:apply-templates mode="sect3.titlepage.recto.auto.mode" select="sect3info/revision"/>
  <xsl:apply-templates mode="sect3.titlepage.recto.auto.mode" select="sect3info/revhistory"/>
  <xsl:apply-templates mode="sect3.titlepage.recto.auto.mode" select="sect3info/abstract"/>
</xsl:template>

<xsl:template name="sect3.titlepage.verso">
</xsl:template>

<xsl:template name="sect3.titlepage.separator"><xsl:if test="count(parent::*)='0'"><hr/></xsl:if>
</xsl:template>

<xsl:template name="sect3.titlepage.before.recto">
</xsl:template>

<xsl:template name="sect3.titlepage.before.verso">
</xsl:template>

<xsl:template name="sect3.titlepage">
  <div class="titlepage">
    <xsl:call-template name="sect3.titlepage.before.recto"/>
    <xsl:call-template name="sect3.titlepage.recto"/>
    <xsl:call-template name="sect3.titlepage.before.verso"/>
    <xsl:call-template name="sect3.titlepage.verso"/>
    <xsl:call-template name="sect3.titlepage.separator"/>
  </div>
</xsl:template>

<xsl:template match="*" mode="sect3.titlepage.recto.mode">
  <!-- if an element isn't found in this mode, -->
  <!-- try the generic titlepage.mode -->
  <xsl:apply-templates select="." mode="titlepage.mode"/>
</xsl:template>

<xsl:template match="*" mode="sect3.titlepage.verso.mode">
  <!-- if an element isn't found in this mode, -->
  <!-- try the generic titlepage.mode -->
  <xsl:apply-templates select="." mode="titlepage.mode"/>
</xsl:template>

<xsl:template match="title" mode="sect3.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="sect3.titlepage.recto.style">
<xsl:apply-templates select="." mode="sect3.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="subtitle" mode="sect3.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="sect3.titlepage.recto.style">
<xsl:apply-templates select="." mode="sect3.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="corpauthor" mode="sect3.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="sect3.titlepage.recto.style">
<xsl:apply-templates select="." mode="sect3.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="authorgroup" mode="sect3.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="sect3.titlepage.recto.style">
<xsl:apply-templates select="." mode="sect3.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="author" mode="sect3.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="sect3.titlepage.recto.style">
<xsl:apply-templates select="." mode="sect3.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="releaseinfo" mode="sect3.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="sect3.titlepage.recto.style">
<xsl:apply-templates select="." mode="sect3.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="copyright" mode="sect3.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="sect3.titlepage.recto.style">
<xsl:apply-templates select="." mode="sect3.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="legalnotice" mode="sect3.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="sect3.titlepage.recto.style">
<xsl:apply-templates select="." mode="sect3.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="pubdate" mode="sect3.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="sect3.titlepage.recto.style">
<xsl:apply-templates select="." mode="sect3.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="revision" mode="sect3.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="sect3.titlepage.recto.style">
<xsl:apply-templates select="." mode="sect3.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="revhistory" mode="sect3.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="sect3.titlepage.recto.style">
<xsl:apply-templates select="." mode="sect3.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="abstract" mode="sect3.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="sect3.titlepage.recto.style">
<xsl:apply-templates select="." mode="sect3.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template name="sect4.titlepage.recto">
  <xsl:apply-templates mode="sect4.titlepage.recto.auto.mode" select="(sect4info/title|title)[1]"/>
  <xsl:apply-templates mode="sect4.titlepage.recto.auto.mode" select="(sect4info/subtitle|subtitle)[1]"/>
  <xsl:apply-templates mode="sect4.titlepage.recto.auto.mode" select="sect4info/corpauthor"/>
  <xsl:apply-templates mode="sect4.titlepage.recto.auto.mode" select="sect4info/authorgroup"/>
  <xsl:apply-templates mode="sect4.titlepage.recto.auto.mode" select="sect4info/author"/>
  <xsl:apply-templates mode="sect4.titlepage.recto.auto.mode" select="sect4info/releaseinfo"/>
  <xsl:apply-templates mode="sect4.titlepage.recto.auto.mode" select="sect4info/copyright"/>
  <xsl:apply-templates mode="sect4.titlepage.recto.auto.mode" select="sect4info/legalnotice"/>
  <xsl:apply-templates mode="sect4.titlepage.recto.auto.mode" select="sect4info/pubdate"/>
  <xsl:apply-templates mode="sect4.titlepage.recto.auto.mode" select="sect4info/revision"/>
  <xsl:apply-templates mode="sect4.titlepage.recto.auto.mode" select="sect4info/revhistory"/>
  <xsl:apply-templates mode="sect4.titlepage.recto.auto.mode" select="sect4info/abstract"/>
</xsl:template>

<xsl:template name="sect4.titlepage.verso">
</xsl:template>

<xsl:template name="sect4.titlepage.separator"><xsl:if test="count(parent::*)='0'"><hr/></xsl:if>
</xsl:template>

<xsl:template name="sect4.titlepage.before.recto">
</xsl:template>

<xsl:template name="sect4.titlepage.before.verso">
</xsl:template>

<xsl:template name="sect4.titlepage">
  <div class="titlepage">
    <xsl:call-template name="sect4.titlepage.before.recto"/>
    <xsl:call-template name="sect4.titlepage.recto"/>
    <xsl:call-template name="sect4.titlepage.before.verso"/>
    <xsl:call-template name="sect4.titlepage.verso"/>
    <xsl:call-template name="sect4.titlepage.separator"/>
  </div>
</xsl:template>

<xsl:template match="*" mode="sect4.titlepage.recto.mode">
  <!-- if an element isn't found in this mode, -->
  <!-- try the generic titlepage.mode -->
  <xsl:apply-templates select="." mode="titlepage.mode"/>
</xsl:template>

<xsl:template match="*" mode="sect4.titlepage.verso.mode">
  <!-- if an element isn't found in this mode, -->
  <!-- try the generic titlepage.mode -->
  <xsl:apply-templates select="." mode="titlepage.mode"/>
</xsl:template>

<xsl:template match="title" mode="sect4.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="sect4.titlepage.recto.style">
<xsl:apply-templates select="." mode="sect4.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="subtitle" mode="sect4.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="sect4.titlepage.recto.style">
<xsl:apply-templates select="." mode="sect4.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="corpauthor" mode="sect4.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="sect4.titlepage.recto.style">
<xsl:apply-templates select="." mode="sect4.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="authorgroup" mode="sect4.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="sect4.titlepage.recto.style">
<xsl:apply-templates select="." mode="sect4.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="author" mode="sect4.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="sect4.titlepage.recto.style">
<xsl:apply-templates select="." mode="sect4.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="releaseinfo" mode="sect4.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="sect4.titlepage.recto.style">
<xsl:apply-templates select="." mode="sect4.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="copyright" mode="sect4.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="sect4.titlepage.recto.style">
<xsl:apply-templates select="." mode="sect4.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="legalnotice" mode="sect4.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="sect4.titlepage.recto.style">
<xsl:apply-templates select="." mode="sect4.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="pubdate" mode="sect4.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="sect4.titlepage.recto.style">
<xsl:apply-templates select="." mode="sect4.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="revision" mode="sect4.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="sect4.titlepage.recto.style">
<xsl:apply-templates select="." mode="sect4.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="revhistory" mode="sect4.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="sect4.titlepage.recto.style">
<xsl:apply-templates select="." mode="sect4.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="abstract" mode="sect4.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="sect4.titlepage.recto.style">
<xsl:apply-templates select="." mode="sect4.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template name="sect5.titlepage.recto">
  <xsl:apply-templates mode="sect5.titlepage.recto.auto.mode" select="(sect5info/title|title)[1]"/>
  <xsl:apply-templates mode="sect5.titlepage.recto.auto.mode" select="(sect5info/subtitle|subtitle)[1]"/>
  <xsl:apply-templates mode="sect5.titlepage.recto.auto.mode" select="sect5info/corpauthor"/>
  <xsl:apply-templates mode="sect5.titlepage.recto.auto.mode" select="sect5info/authorgroup"/>
  <xsl:apply-templates mode="sect5.titlepage.recto.auto.mode" select="sect5info/author"/>
  <xsl:apply-templates mode="sect5.titlepage.recto.auto.mode" select="sect5info/releaseinfo"/>
  <xsl:apply-templates mode="sect5.titlepage.recto.auto.mode" select="sect5info/copyright"/>
  <xsl:apply-templates mode="sect5.titlepage.recto.auto.mode" select="sect5info/legalnotice"/>
  <xsl:apply-templates mode="sect5.titlepage.recto.auto.mode" select="sect5info/pubdate"/>
  <xsl:apply-templates mode="sect5.titlepage.recto.auto.mode" select="sect5info/revision"/>
  <xsl:apply-templates mode="sect5.titlepage.recto.auto.mode" select="sect5info/revhistory"/>
  <xsl:apply-templates mode="sect5.titlepage.recto.auto.mode" select="sect5info/abstract"/>
</xsl:template>

<xsl:template name="sect5.titlepage.verso">
</xsl:template>

<xsl:template name="sect5.titlepage.separator"><xsl:if test="count(parent::*)='0'"><hr/></xsl:if>
</xsl:template>

<xsl:template name="sect5.titlepage.before.recto">
</xsl:template>

<xsl:template name="sect5.titlepage.before.verso">
</xsl:template>

<xsl:template name="sect5.titlepage">
  <div class="titlepage">
    <xsl:call-template name="sect5.titlepage.before.recto"/>
    <xsl:call-template name="sect5.titlepage.recto"/>
    <xsl:call-template name="sect5.titlepage.before.verso"/>
    <xsl:call-template name="sect5.titlepage.verso"/>
    <xsl:call-template name="sect5.titlepage.separator"/>
  </div>
</xsl:template>

<xsl:template match="*" mode="sect5.titlepage.recto.mode">
  <!-- if an element isn't found in this mode, -->
  <!-- try the generic titlepage.mode -->
  <xsl:apply-templates select="." mode="titlepage.mode"/>
</xsl:template>

<xsl:template match="*" mode="sect5.titlepage.verso.mode">
  <!-- if an element isn't found in this mode, -->
  <!-- try the generic titlepage.mode -->
  <xsl:apply-templates select="." mode="titlepage.mode"/>
</xsl:template>

<xsl:template match="title" mode="sect5.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="sect5.titlepage.recto.style">
<xsl:apply-templates select="." mode="sect5.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="subtitle" mode="sect5.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="sect5.titlepage.recto.style">
<xsl:apply-templates select="." mode="sect5.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="corpauthor" mode="sect5.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="sect5.titlepage.recto.style">
<xsl:apply-templates select="." mode="sect5.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="authorgroup" mode="sect5.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="sect5.titlepage.recto.style">
<xsl:apply-templates select="." mode="sect5.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="author" mode="sect5.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="sect5.titlepage.recto.style">
<xsl:apply-templates select="." mode="sect5.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="releaseinfo" mode="sect5.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="sect5.titlepage.recto.style">
<xsl:apply-templates select="." mode="sect5.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="copyright" mode="sect5.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="sect5.titlepage.recto.style">
<xsl:apply-templates select="." mode="sect5.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="legalnotice" mode="sect5.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="sect5.titlepage.recto.style">
<xsl:apply-templates select="." mode="sect5.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="pubdate" mode="sect5.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="sect5.titlepage.recto.style">
<xsl:apply-templates select="." mode="sect5.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="revision" mode="sect5.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="sect5.titlepage.recto.style">
<xsl:apply-templates select="." mode="sect5.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="revhistory" mode="sect5.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="sect5.titlepage.recto.style">
<xsl:apply-templates select="." mode="sect5.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="abstract" mode="sect5.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="sect5.titlepage.recto.style">
<xsl:apply-templates select="." mode="sect5.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template name="simplesect.titlepage.recto">
  <xsl:apply-templates mode="simplesect.titlepage.recto.auto.mode" select="(simplesectinfo/title|docinfo/title|title)[1]"/>
  <xsl:apply-templates mode="simplesect.titlepage.recto.auto.mode" select="(simplesectinfo/subtitle|docinfo/subtitle|subtitle)[1]"/>
  <xsl:apply-templates mode="simplesect.titlepage.recto.auto.mode" select="simplesectinfo/corpauthor|docinfo/corpauthor"/>
  <xsl:apply-templates mode="simplesect.titlepage.recto.auto.mode" select="simplesectinfo/authorgroup|docinfo/authorgroup"/>
  <xsl:apply-templates mode="simplesect.titlepage.recto.auto.mode" select="simplesectinfo/author|docinfo/author"/>
  <xsl:apply-templates mode="simplesect.titlepage.recto.auto.mode" select="simplesectinfo/releaseinfo|docinfo/releaseinfo"/>
  <xsl:apply-templates mode="simplesect.titlepage.recto.auto.mode" select="simplesectinfo/copyright|docinfo/copyright"/>
  <xsl:apply-templates mode="simplesect.titlepage.recto.auto.mode" select="simplesectinfo/legalnotice|docinfo/legalnotice"/>
  <xsl:apply-templates mode="simplesect.titlepage.recto.auto.mode" select="simplesectinfo/pubdate|docinfo/pubdate"/>
  <xsl:apply-templates mode="simplesect.titlepage.recto.auto.mode" select="simplesectinfo/revision|docinfo/revision"/>
  <xsl:apply-templates mode="simplesect.titlepage.recto.auto.mode" select="simplesectinfo/revhistory|docinfo/revhistory"/>
  <xsl:apply-templates mode="simplesect.titlepage.recto.auto.mode" select="simplesectinfo/abstract|docinfo/abstract"/>
</xsl:template>

<xsl:template name="simplesect.titlepage.verso">
</xsl:template>

<xsl:template name="simplesect.titlepage.separator"><xsl:if test="count(parent::*)='0'"><hr/></xsl:if>
</xsl:template>

<xsl:template name="simplesect.titlepage.before.recto">
</xsl:template>

<xsl:template name="simplesect.titlepage.before.verso">
</xsl:template>

<xsl:template name="simplesect.titlepage">
  <div class="titlepage">
    <xsl:call-template name="simplesect.titlepage.before.recto"/>
    <xsl:call-template name="simplesect.titlepage.recto"/>
    <xsl:call-template name="simplesect.titlepage.before.verso"/>
    <xsl:call-template name="simplesect.titlepage.verso"/>
    <xsl:call-template name="simplesect.titlepage.separator"/>
  </div>
</xsl:template>

<xsl:template match="*" mode="simplesect.titlepage.recto.mode">
  <!-- if an element isn't found in this mode, -->
  <!-- try the generic titlepage.mode -->
  <xsl:apply-templates select="." mode="titlepage.mode"/>
</xsl:template>

<xsl:template match="*" mode="simplesect.titlepage.verso.mode">
  <!-- if an element isn't found in this mode, -->
  <!-- try the generic titlepage.mode -->
  <xsl:apply-templates select="." mode="titlepage.mode"/>
</xsl:template>

<xsl:template match="title" mode="simplesect.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="simplesect.titlepage.recto.style">
<xsl:apply-templates select="." mode="simplesect.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="subtitle" mode="simplesect.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="simplesect.titlepage.recto.style">
<xsl:apply-templates select="." mode="simplesect.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="corpauthor" mode="simplesect.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="simplesect.titlepage.recto.style">
<xsl:apply-templates select="." mode="simplesect.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="authorgroup" mode="simplesect.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="simplesect.titlepage.recto.style">
<xsl:apply-templates select="." mode="simplesect.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="author" mode="simplesect.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="simplesect.titlepage.recto.style">
<xsl:apply-templates select="." mode="simplesect.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="releaseinfo" mode="simplesect.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="simplesect.titlepage.recto.style">
<xsl:apply-templates select="." mode="simplesect.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="copyright" mode="simplesect.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="simplesect.titlepage.recto.style">
<xsl:apply-templates select="." mode="simplesect.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="legalnotice" mode="simplesect.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="simplesect.titlepage.recto.style">
<xsl:apply-templates select="." mode="simplesect.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="pubdate" mode="simplesect.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="simplesect.titlepage.recto.style">
<xsl:apply-templates select="." mode="simplesect.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="revision" mode="simplesect.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="simplesect.titlepage.recto.style">
<xsl:apply-templates select="." mode="simplesect.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="revhistory" mode="simplesect.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="simplesect.titlepage.recto.style">
<xsl:apply-templates select="." mode="simplesect.titlepage.recto.mode"/>
</div>
</xsl:template>

<xsl:template match="abstract" mode="simplesect.titlepage.recto.auto.mode">
<div xsl:use-attribute-sets="simplesect.titlepage.recto.style">
<xsl:apply-templates select="." mode="simplesect.titlepage.recto.mode"/>
</div>
</xsl:template>

</xsl:stylesheet>