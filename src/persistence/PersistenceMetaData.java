package persistence;

/**
 *  Stores meta information related to the persistence of an entity
 */
class PersistenceMetaData {
    private boolean saved;
    private Long timeSinceLastUpdate;

    public PersistenceMetaData() {
        this.saved = false;
        timeSinceLastUpdate = null;
    }
    
    /**
     * Call this method when the respective entity was saved in the DB
     */
    public void instanceSaved() {
        saved = true;
    }
    
    /**
     * Call this method when the respective entity was updated in the DB
     */
    public void instanceUpdated() {
        timeSinceLastUpdate = System.currentTimeMillis();
    }
    
    /**
     * Returns the time stamp of the last update  
     */
    public Long getTimeSinceLastUpdate() {
        return timeSinceLastUpdate;
    }
    
    /**
     * Returns true, if the respective instance is saved in the DB
     */
    public boolean isSaved() {
        return saved;
    }
}