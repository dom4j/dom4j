<?xml version="1.0" encoding="iso-8859-1"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

  <xsl:output method="html" indent="yes"/>

  <xsl:include href="document.xsl"/>

  <xsl:template match="/">
    <xsl:variable name="project" select="document('../project.xml')/project"/>
    <html lang="en">
    <head>
      <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
      <meta name="author" content="{document/properties/author/.}"/>
      <link rel="stylesheet" type="text/css" href="default.css"/>
      <xsl:choose>
        <xsl:when test="/document/properties/title"><title><xsl:value-of select="/document/body/title"/></title></xsl:when>
        <xsl:when test="/document/body/title"><title><xsl:value-of select="/document/body/title"/></title></xsl:when>
        <xsl:otherwise><title><xsl:value-of select="$project/title"/></title></xsl:otherwise>
      </xsl:choose>
    </head>

    <body>

      <table border="0" cellpadding="0" cellspacing="0" width="100%">
        <tr>
          <td valign="top">          
            <xsl:apply-templates select="$project/links"/>
            <hr/>
          </td>
        </tr>

        <tr>
          <td>
            <h1>
              <xsl:choose>
                <xsl:when test="/document/body/title"><xsl:value-of select="/document/body/title"/></xsl:when>
                <xsl:otherwise><xsl:value-of select="$project/title"/></xsl:otherwise>
              </xsl:choose>
            </h1>
           </td>
        </tr>

        <tr>
          <td>
             <xsl:apply-templates select="document/body"/>
             <br/>
             <hr/>
          </td>
        </tr>

        <tr>
          <td valign="top">
            <xsl:apply-templates select="$project/links"/>
            <hr/>
          </td>
        </tr>

        <xsl:if test="$project/notice">
          <tr>
            <td align="center">
              <xsl:for-each select="$project/notice">
                <small><xsl:copy-of select="."/><br/>&#xA0;<br/></small>
              </xsl:for-each>
            </td>
          </tr>
        </xsl:if>

      </table>

    </body>
    </html>
  </xsl:template>


  <!-- UL is processed into a table using graphical bullets -->
  <xsl:template match="ul">
    <table border="0" cellpadding="2" cellspacing="2">
      <tr><td colspan="2" height="5"></td></tr>
      <xsl:apply-templates/>
    </table>
  </xsl:template>

  <xsl:template match="ul/li">
    <tr>
      <td align="left" valign="top">
        <img src="style/images/blueball.gif" alt="*"/>
     </td>
      <td align="left" valign="top"><xsl:apply-templates/></td>
    </tr>
  </xsl:template>

  <xsl:template match="section">
    <br />
  </xsl:template>

  <xsl:template match="br">
    <br />
  </xsl:template>

  <xsl:template match='@* | node()'>
    <xsl:copy>
      <xsl:apply-templates select='@* | node()'/>
    </xsl:copy>
  </xsl:template>


</xsl:stylesheet>


