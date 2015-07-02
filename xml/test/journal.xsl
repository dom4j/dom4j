<?xml version="1.0"?>
<xsl:stylesheet version="1.0" 
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:template match="/">
  <html>
    <xsl:apply-templates/>
  </html>
  <head><title>Journal</title></head>
</xsl:template>

<xsl:template match="series">
  <body>
    <xsl:apply-templates/>
  </body>
</xsl:template>

<xsl:template match="entry">
  <p>
    <xsl:apply-templates/>
  </p>
</xsl:template>

<xsl:template match="date">
  <b>
    <xsl:value-of select="."/>
  </b>
</xsl:template>

<xsl:template match="text">
  <xsl:value-of select="."/>
</xsl:template>

</xsl:stylesheet>
