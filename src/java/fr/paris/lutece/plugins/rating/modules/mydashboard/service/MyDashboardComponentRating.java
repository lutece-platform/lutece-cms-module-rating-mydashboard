/*
 * Copyright (c) 2002-2013, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.rating.modules.mydashboard.service;

import fr.paris.lutece.plugins.document.business.Document;
import fr.paris.lutece.plugins.document.business.DocumentHome;
import fr.paris.lutece.plugins.extend.business.extender.ResourceExtenderDTO;
import fr.paris.lutece.plugins.extend.business.extender.ResourceExtenderDTOFilter;
import fr.paris.lutece.plugins.extend.business.extender.config.IExtenderConfigDAO;
import fr.paris.lutece.plugins.extend.business.extender.history.ResourceExtenderHistory;
import fr.paris.lutece.plugins.extend.business.extender.history.ResourceExtenderHistoryFilter;
import fr.paris.lutece.plugins.extend.modules.rating.business.Rating;
import fr.paris.lutece.plugins.extend.modules.rating.business.config.RatingExtenderConfig;
import fr.paris.lutece.plugins.extend.modules.rating.service.IRatingService;
import fr.paris.lutece.plugins.extend.modules.rating.service.extender.RatingResourceExtender;
import fr.paris.lutece.plugins.extend.modules.rating.service.security.IRatingSecurityService;
import fr.paris.lutece.plugins.extend.service.extender.IResourceExtenderService;
import fr.paris.lutece.plugins.extend.service.extender.history.IResourceExtenderHistoryService;
import fr.paris.lutece.plugins.mydashboard.service.MyDashboardComponent;
import fr.paris.lutece.plugins.rating.modules.mydashboard.business.RatingResource;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.security.SecurityService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.util.html.HtmlTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;


/**
 * Dashboard component to display subscriptions of the current user
 */
public class MyDashboardComponentRating extends MyDashboardComponent
{
    private static final String DASHBOARD_COMPONENT_ID = "rating.mydashboardComponentRating";

    private static final String MESSAGE_COMPONENT_DESCRIPTION = "module.rating.mydashboard.mydashboardComponentRating.description";

    private static final String MARK_LIST_RESOURCE_DTO = "list_resource_dto";
    private static final String MARK_NB_VOTES = "nb_votes";
    private static final String MARK_NB_VOTES_USER = "nb_votes_user";

    private static final String TEMPLATE_DASHBOARD_RATING = "skin/plugins/rating/modules/mydashboard/dashboardcomponent/dashboardComponentRating.html";

    @Inject
    private IResourceExtenderService _resourceExtenderService;
    @Inject
    private IResourceExtenderHistoryService _resourceExtenderHistoryService;
    @Inject
    private IRatingSecurityService _ratingSecurityService;
    @Inject
    private IRatingService _ratingService;
    @Inject
    private IExtenderConfigDAO<RatingExtenderConfig> _ratingConfigDAO;

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDashboardData( HttpServletRequest request )
    {
        if ( SecurityService.isAuthenticationEnable( ) )
        {
            LuteceUser user = SecurityService.getInstance( ).getRegisteredUser( request );
            if ( user != null )
            {
                Map<String, Object> model = new HashMap<String, Object>( );
                ResourceExtenderDTOFilter filter = new ResourceExtenderDTOFilter( );
                filter.setFilterExtendableResourceType( Document.PROPERTY_RESOURCE_TYPE );
                filter.setFilterExtenderType( RatingResourceExtender.RESOURCE_EXTENDER );
                filter.setFilterIdExtendableResource( "*" );
                ResourceExtenderHistoryFilter historyFilter = new ResourceExtenderHistoryFilter( );
                historyFilter.setUserGuid( user.getName( ) );
                historyFilter.setExtendableResourceType( Document.PROPERTY_RESOURCE_TYPE );
                historyFilter.setExtenderType( RatingResourceExtender.RESOURCE_EXTENDER );
                List<ResourceExtenderHistory> histories = _resourceExtenderHistoryService.findByFilter( historyFilter );
                List<RatingResource> ratedDocument = new ArrayList<RatingResource>( histories.size( ) );

                List<ResourceExtenderDTO> extenders = _resourceExtenderService.findByFilter( filter );

                if ( CollectionUtils.isNotEmpty( extenders ) )
                {
                    ResourceExtenderDTO extender = extenders.get( 0 );
                    RatingExtenderConfig config = _ratingConfigDAO.load( extender.getIdExtender( ) );
                    model.put( MARK_NB_VOTES, config.getNbVotePerUser( ) );
                }

                model.put( MARK_NB_VOTES_USER, histories.size( ) );

                for ( ResourceExtenderHistory history : histories )
                {
                    Document doc = DocumentHome.findByPrimaryKey( Integer.valueOf( ""
                            + history.getIdExtendableResource( ) ) );
                    RatingResource ratingResource = new RatingResource( );
                    ratingResource.setIdResource( Long.valueOf( doc.getId( ) ) );
                    ratingResource.setTitle( doc.getTitle( ) );
                    ratingResource.setTypeResource( Document.PROPERTY_RESOURCE_TYPE );
                    ratingResource.setCanDelete( _ratingSecurityService.canDeleteVote( request, "" + doc.getId( ),
                            Document.PROPERTY_RESOURCE_TYPE ) );
                    ratingResource.setSummary( doc.getSummary( ) );
                    ratingResource.setThumbnail( doc.getThumbnail( ) );

                    // Get total score of a rating
                    Rating rating = _ratingService.findByResource( "" + doc.getId( ), Document.PROPERTY_RESOURCE_TYPE );
                    ratingResource.setNote( "" + rating.getScoreValue( ) );
                    ratingResource.setVote( "" + rating.getVoteCount( ) );

                    ratedDocument.add( ratingResource );
                }

                model.put( MARK_LIST_RESOURCE_DTO, ratedDocument );
                HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_DASHBOARD_RATING,
                        request.getLocale( ), model );

                return template.getHtml( );
            }
        }
        return StringUtils.EMPTY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getComponentId( )
    {
        return DASHBOARD_COMPONENT_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getComponentDescription( Locale locale )
    {
        return I18nService.getLocalizedString( MESSAGE_COMPONENT_DESCRIPTION, locale );
    }

}
