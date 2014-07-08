package fr.paris.lutece.plugins.rating.modules.mydashboard.business;

/**
 * Class for displaying rating info
 */
public class RatingResource
{
    /**
     * The id resource
     */
    private Long _lIdResource;

    /**
     * Type of document
     */
    private String _strTypeResource;

    /**
     * True if can delete, false otherwise
     */
    private boolean _bCanDelete;

    /**
     * The resource title displayed
     */
    private String _strTitle;

    /**
     * The resource note
     */
    private String _strNote;

    /**
     * The resource nb vote
     */
    private String _strVote;

    /**
     * @return the _lIdResource
     */
    public Long getIdResource( )
    {
        return _lIdResource;
    }

    /**
     * @param lIdResource the _lIdResource to set
     */
    public void setIdResource( Long lIdResource )
    {
        this._lIdResource = lIdResource;
    }

    /**
     * @return the _strTypeResource
     */
    public String getTypeResource( )
    {
        return _strTypeResource;
    }

    /**
     * @param strTypeResource the _strTypeResource to set
     */
    public void setTypeResource( String strTypeResource )
    {
        this._strTypeResource = strTypeResource;
    }

    /**
     * @return the _bCanDelete
     */
    public boolean isCanDelete( )
    {
        return _bCanDelete;
    }

    /**
     * @param bCanDelete the _bCanDelete to set
     */
    public void setCanDelete( boolean bCanDelete )
    {
        this._bCanDelete = bCanDelete;
    }

    /**
     * @return the _strTitle
     */
    public String getTitle( )
    {
        return _strTitle;
    }

    /**
     * @param strTitle the _strTitle to set
     */
    public void setTitle( String strTitle )
    {
        this._strTitle = strTitle;
    }

    /**
     * @return the _strNote
     */
    public String getNote( )
    {
        return _strNote;
    }

    /**
     * @param strNote the _strNote to set
     */
    public void setNote( String strNote )
    {
        this._strNote = strNote;
    }

    /**
     * @return the _strVote
     */
    public String getVote( )
    {
        return _strVote;
    }

    /**
     * @param strVote the _strVote to set
     */
    public void setVote( String strVote )
    {
        this._strVote = strVote;
    }
}
