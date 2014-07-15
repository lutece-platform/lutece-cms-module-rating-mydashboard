<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:template match="page">
	<div>
		<xsl:apply-templates select="list-document"/>
	</div>
</xsl:template>

<xsl:template match="list-document">
	Récapitulatif de mes votes : <xsl:value-of select="nb-vote" /> / <xsl:value-of select="nb-max-vote" />
	<div class="row">
    <xsl:for-each select="document-content">
		<div class="col-md-4" >
			<div class="thumbnail">
				<div class="caption">
					<div class="thumbnail">
						<img src="document?id={doc-id}" /> 
					</div>
					<div style="height:30px;">
					<b> <xsl:value-of select="doc-title" /> </b>
					</div>
					<br />
					<xsl:value-of select="doc-rate" /> votes
					<hr />
					<xsl:choose>
						<xsl:when test="string-length(doc-summary) &gt; 100" >
							<div class="dashboard_summary"><xsl:value-of select="substring(doc-summary, 0, 100)" /> [...] </div>
						</xsl:when>
						<xsl:otherwise>
							<div class="dashboard_summary"><xsl:value-of select="doc-summary" /> </div>
						</xsl:otherwise>
					</xsl:choose>
					<xsl:if test="doc-cancel-vote = 'true'">
						<div>
							<a href="jsp/site/plugins/extend/modules/rating/DoCancelVote.jsp?idExtendableResource={doc-id}&amp;extendableResourceType=document">
								J'annule mon vote
							</a>
						</div>
					</xsl:if>
				</div>
			</div>
		</div>
    </xsl:for-each>
	</div>
</xsl:template>

</xsl:stylesheet>
