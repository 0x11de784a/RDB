package persistence;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Defines necessary database interactions in order to store object of a given type T persistently in a database
 */
public abstract class PersistenceInterface<T> {
    /** Stores PersistenceMetaData for each Object that is under control of the PersistenceInterface */
    private Map<T, PersistenceMetaData> object2PersistenceMetaData = new HashMap<T, PersistenceMetaData>();
    
    /** Stores each object that is under control of the PersistenceInterface. The key corresponds to the respective key in the database. */
    private Map<Object, T> registeredInstances = new HashMap<Object, T>();
    
    
    /**
     * Implement this method and execute necessary SQL Statements that save an instance of the given type
     */
    abstract public void _save(T t) throws SQLException;
    
    /**
     * Saves an instance of the given type
     */
    public void save(T t) throws SQLException {
        PersistenceMetaData pmd = object2PersistenceMetaData.get(t);
        
        if(pmd == null) {
            pmd = new PersistenceMetaData();
            object2PersistenceMetaData.put(t, pmd);
        }
        
        if(pmd != null && !pmd.isSaved()) {
            _save(t);
            pmd.instanceSaved();
            registeredInstances.put(key(t), t);
        }
    }
    
    
    /**
     * Implement this method and execute necessary SQL Statements that update an instance of the given type
     */
    abstract public void _update(T t) throws SQLException;
    
    /**
     * Updates an instance of the given type
     */
    public void update(T t) throws SQLException {
        PersistenceMetaData pmd = object2PersistenceMetaData.get(t);
        if(pmd == null || pmd != null && !pmd.isSaved()) {
            throw new RuntimeException("Object cannot be updated because it does not exist in the DB");
        }
        _update(t);
        pmd.instanceUpdated();
    }
    
    
    /**
     * Implement this method and execute necessary SQL Statements that fetch an instance of the given type
     */
    abstract public T _fetch(Object o) throws SQLException;
    
    /**
     * Fetches an instance of the given type
     */
    public T fetch(Object o) throws SQLException {
        T t = registeredInstances.get(o);
        if(t == null) {
            T t2 = _fetch(o);
            PersistenceMetaData pmd = new PersistenceMetaData();
            pmd.instanceSaved();
            object2PersistenceMetaData.put(t2, pmd);
            registeredInstances.put(o, t2);
        }
        return registeredInstances.get(o);
    }
    
    /**
     * Implement this method and execute necessary SQL Statements that fetches all instances of the given type
     */
    abstract public List<T> _all() throws SQLException;
    
    /**
     * Fetches all instances of the given type
     */
    public List<T> all() throws SQLException {
        List<T> ts = _all();
        for(T t: ts) {
            Object key = key(t);
            if(registeredInstances.get(key) == null) {
                PersistenceMetaData pmd = new PersistenceMetaData();
                pmd.instanceSaved();
                object2PersistenceMetaData.put(t, pmd);
                registeredInstances.put(key, t);
            }
        }
        return ts;
    }
    
    
    /**
     * Implement this method and return the key that identifies an instance of the given type uniquely in the database
     */
    abstract public Object key(T t);
}