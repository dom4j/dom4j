<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:xt="http://www.jclark.com/xt"
                extension-element-prefixes="xt"
                version="1.0">

<xsl:import href="../../html/xtchunk.xsl"/>
<xsl:include href="htmlhelp.xsl"/>

<xsl:template name="write.text.chunk">
  <xsl:param name="filename" select="''"/>
  <xsl:param name="method" select="'html'"/>
  <xsl:param name="content" select="''"/>

  <!-- XT bug, if method is an AVT, it doesn't work -->
  <xt:document method="text" href="{$filename}">
    <xsl:copy-of select="$content"/>
  </xt:document>
</xsl:template>

</xsl:stylesheet>
