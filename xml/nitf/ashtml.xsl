<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:output method="xml"/>

<xsl:template match="nitf">
  <html>
  <table cellspacing="0" cellpadding="2" bgcolor="#000000" border="1">
    <tr bgcolor="#dddddd">
      <td>
        <xsl:apply-templates select="body/body.head"/>
      </td>
    </tr>
    <tr bgcolor="#eeeeee">
      <td>
        <xsl:apply-templates select="body/body.content"/>
        <xsl:apply-templates select="head/meta[@name='ap-company']"/>
      </td>
    </tr>
  </table>
  </html>
</xsl:template>

<xsl:template match="meta[@name='ap-company']">
  Related Stock:
  <xsl:element name="a">
    <xsl:attribute name="href">
      http://localhost/stock?ts=<xsl:value-of select="substring-before(substring-after(@content,'TS:'),';')"/>
    </xsl:attribute>
    <xsl:value-of select="substring-after(substring-before(@content,';'),':')"/>
  </xsl:element>
  
</xsl:template>

<xsl:template match="body.content">
  <xsl:for-each select="block">
    <xsl:choose>
      <xsl:when test="./hl2">
        <xsl:element name="a">
          <xsl:attribute name="href">
            <xsl:value-of select="block/media/media-reference[@data-location]"/>
          </xsl:attribute>
        <b><xsl:value-of select="hl2"/></b><br/>
        </xsl:element>
        <xsl:copy-of select="p"/>
      </xsl:when>
      <xsl:otherwise>
        <xsl:copy-of select="."/>
      </xsl:otherwise>
    </xsl:choose>     
  </xsl:for-each>  
  <hr/>
</xsl:template>

<xsl:template match="body.head">
  <xsl:apply-templates select="hedline/hl1"/> 
  <xsl:apply-templates select="byline"/>
  <xsl:value-of select="dateline/location/text()"/>
</xsl:template>

<xsl:template match="hl1">
  <h1><xsl:value-of select="."/></h1>
</xsl:template>

<xsl:template match="byline">
  <h2><b><xsl:value-of select="text()"/></b><font size="-2"><i><xsl:value-of select="byttl"/></i></font></h2>
</xsl:template>

</xsl:stylesheet>
