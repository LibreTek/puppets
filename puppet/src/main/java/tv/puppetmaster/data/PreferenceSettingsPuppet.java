package tv.puppetmaster.data;

import java.util.List;
import java.util.Set;

import tv.puppetmaster.data.i.ParentPuppet;
import tv.puppetmaster.data.i.SettingsPuppet;

public class PreferenceSettingsPuppet implements SettingsPuppet {

    private String mXml;
    private Storage mStorage;

    private ParentPuppet mParent;
    private String mName;
    private String mShortDescription;
    private String mImageUrl;
    private String mBackgroundImageUrl;

    public PreferenceSettingsPuppet(ParentPuppet parent, String name, String shortDescription, String imageUrl, String backgroundImageUrl, String xml) {
        mParent = parent;
        mName = name;
        mShortDescription = shortDescription;
        mImageUrl = imageUrl;
        mBackgroundImageUrl = backgroundImageUrl;
        mXml = xml;
    }

    @Override
    public String getDefinition() {
        return mXml;
    }

    @Override
    public Storage getStorage() {
        return mStorage;
    }

    @Override
    public void setStorage(Storage storage) {
        mStorage = storage;
    }

    @Override
    public boolean getBoolean(String key, boolean defValue) {
        return mStorage == null? defValue : mStorage.getBoolean(key, defValue);
    }

    @Override
    public float getFloat(String key, float defValue) {
        return mStorage == null? defValue : mStorage.getFloat(key, defValue);
    }

    @Override
    public int getInt(String key, int defValue) {
        return mStorage == null? defValue : mStorage.getInt(key, defValue);
    }

    @Override
    public long getLong(String key, long defValue) {
        return mStorage == null? defValue : mStorage.getLong(key, defValue);
    }

    @Override
    public String getString(String key, String defValue) {
        return mStorage == null? defValue : mStorage.getString(key, defValue);
    }

    @Override
    public List<String> getStringList(String key, List<String> defValue) {
        return mStorage == null? defValue : mStorage.getStringList(key, defValue);
    }

    @Override
    public Set<String> getStringSet(String key, Set<String> defValue) {
        return mStorage == null? defValue : mStorage.getStringSet(key, defValue);
    }

    @Override
    public String getName() {
        return mName;
    }

    @Override
    public String getCategory() {
        return null;
    }

    @Override
    public String getShortDescription() {
        return mShortDescription;
    }

    @Override
    public String getImageUrl() {
        return mImageUrl;
    }

    @Override
    public String getBackgroundImageUrl() {
        return mBackgroundImageUrl;
    }

    @Override
    public boolean isUnavailableIn(String region) {
        return false;
    }

    @Override
    public String getPreferredRegion() {
        return null;
    }

    @Override
    public int getShieldLevel() {
        return 0;
    }

    @Override
    public ParentPuppet getParent() {
        return mParent;
    }

    @Override
    public PuppetIterator getRelated() {
        return null;
    }
}