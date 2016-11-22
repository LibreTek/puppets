package tv.puppetmaster.data.i;

import java.util.List;
import java.util.Set;

public interface SettingsPuppet extends Puppet {

    String              getDefinition();

    Storage             getStorage();
    void                setStorage(Storage storage);

    boolean             getBoolean(String key, boolean defValue);
    float               getFloat(String key, float defValue);
    int                 getInt(String key, int defValue);
    long                getLong(String key, long defValue);
    String              getString(String key, String defValue);
    List<String>        getStringList(String key, List<String> defValue);
    Set<String>         getStringSet(String key, Set<String> defValue);

    interface Storage {
        boolean         getBoolean(String key, boolean defValue);
        void            setBoolean(String key, boolean value);
        float           getFloat(String key, float defValue);
        void            setFloat(String key, float value);
        int             getInt(String key, int defValue);
        void            setInt(String key, int value);
        long            getLong(String key, long defValue);
        void            setLong(String key, long value);
        String          getString(String key, String defValue);
        void            setString(String key, String value);
        List<String>    getStringList(String key, List<String> defValue);
        void            setStringList(String key, List<String> value);
        Set<String>     getStringSet(String key, Set<String> defValue);
        void            setStringSet(String key, Set<String> value);
    }
}