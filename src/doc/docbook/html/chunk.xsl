<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:saxon="http://icl.com/saxon"
                xmlns:lxslt="http://xml.apache.org/xslt"
                xmlns:xalanredirect="org.apache.xalan.xslt.extensions.Redirect"
                xmlns:doc="http://nwalsh.com/xsl/documentation/1.0"
		version="1.0"
                exclude-result-prefixes="doc"
                extension-element-prefixes="saxon xalanredirect lxslt">

<!-- This stylesheet does not work with XT. Use xtchunk.xsl instead. -->

<xsl:include href="chunk-common.xsl"/>

<!-- ==================================================================== -->
<!-- This is a workaround for a XalanJ1 bug in element-available. -->

<lxslt:component prefix="xalanredirect" elements="write">
  <lxslt:script lang="javaclass"
                src="org.apache.xalan.xslt.extensions.Redirect"/>
</lxslt:component>

<!-- ==================================================================== -->

<xsl:template name="make-relative-filename">
  <xsl:param name="base.dir" select="'./'"/>
  <xsl:param name="base.name" select="''"/>

  <xsl:choose>
    <xsl:when test="element-available('xalanredirect:write')">
      <!-- Xalan doesn't make the chunks relative -->
      <xsl:value-of select="concat($base.dir,$base.name)"/>
    </xsl:when>
    <xsl:when test="element-available('saxon:output')">
      <!-- Saxon doesn't make the chunks relative -->
      <xsl:value-of select="concat($base.dir,$base.name)"/>
    </xsl:when>
    <!-- XT makes chunks relative, but it doesn't support -->
    <!-- element-available() so use xtchunk.xsl instead. -->
    <xsl:otherwise>
      <!-- it doesn't matter since we won't be making chunks... -->
      <xsl:value-of select="$base.name"/>
    </xsl:otherwise>
  </xsl:choose>
</xsl:template>

<xsl:template name="write.chunk">
  <xsl:param name="filename" select="''"/>
  <xsl:param name="method" select="'html'"/>
  <xsl:param name="encoding" select="'ISO-8859-1'"/>
  <xsl:param name="content" select="''"/>

  <xalanredirect:write file="{$filename}">
    <xsl:copy-of select="$content"/>
    <xsl:fallback>
      <saxon:output file="{$filename}"
                    method="{$method}"
                    encoding="{$encoding}">
        <xsl:copy-of select="$content"/>
        <xsl:fallback>
          <xsl:copy-of select="$content"/>
        </xsl:fallback>
      </saxon:output>
    </xsl:fallback>
  </xalanredirect:write>
</xsl:template>

</xsl:stylesheet>
