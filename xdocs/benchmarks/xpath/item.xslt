<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="html"/>
	<xsl:template match="/">
		<html>
			<header>
				<style type="text/css">
tr.loa
{
 background-color:#FF9900;
}
tr.item
{
 background-color:lightblue;
}

</style>
			</header>
			<body>
				<table border="1">
					<xsl:apply-templates select="ItemResultSet/Item"/>
				</table>
			</body>
		</html>
	</xsl:template>
	<xsl:template match="Item">
		<tr class="item">
			<td>
				<xsl:value-of select="Attr0x0"/>
			</td>
			<td>
				<xsl:value-of select="Attr1x0"/>
			</td>
			<td>
				<xsl:value-of select="Attr2x0"/>
			</td>
			<td>
				<xsl:value-of select="Attr3x0"/>
			</td>
			<td>
				<xsl:value-of select="Attr4x0"/>
			</td>
			<td>
				<xsl:value-of select="Attr5x0"/>
			</td>
			<td>
				<xsl:value-of select="Attr6x0"/>
			</td>
			<td>
				<xsl:value-of select="Attr7x0"/>
			</td>
		</tr>
	</xsl:template>
</xsl:stylesheet>
