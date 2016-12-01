package tv.puppetmaster.scraper

import tv.puppetmaster.data.PreferenceSettingsPuppet
import tv.puppetmaster.data.i.*
import tv.puppetmaster.data.i.Puppet.PuppetIterator
import tv.puppetmaster.data.i.SourcesPuppet.SourceDescription

import java.util.regex.Matcher

public class PasteyPuppet implements InstallablePuppet {

    def static final int VERSION_CODE = 5

    def static final String SETTINGS_DEFINITION =
            """
            <?xml version="1.0" encoding="utf-8"?>
            <PreferenceScreen xmlns:android="http://schemas.android.com/apk/res/android"
                android:title="#Pastey Settings">
                <EditListPreference
                    android:key="pastebin_ids"
                    android:title="M3U List"
                    android:summary="Enter pastebin.com #pastey IDs here. After saving the IDs, reenter #Pastey to refresh content." />
                <CheckBoxPreference
                    android:title="Expose to Live Channels"
                    android:key="expose_streams" />
                <CheckBoxPreference
                    android:title="Enable rtmp://"
                    android:key="enable_rtmp" />
                <CheckBoxPreference
                    android:title="Enable *.ts"
                    android:key="enable_ts" />
            </PreferenceScreen>
            """;

    def ParentPuppet mParent
    def String mName

    def transient PasteySearchesPuppet mSearchProvider
    def transient static SettingsPuppet sSettingsProvider

    PasteyPuppet() {
        this(null, "#Pastey")
    }

    PasteyPuppet(ParentPuppet parent, String name) {
        mParent = parent
        mName = name
    }

    private PasteyM3U8Puppet convertToPuppet(ParentPuppet parent, String pid) {
        String name = pid
        if (pid.contains("/")) {
            // Assume they typed out a URL instead of a pastebin ID
            name = pid.substring(pid.lastIndexOf("/") + 1)
            if (name.isEmpty()) {
                name = "Untitled"
            }
        } else {
            pid = "http://pastebin.com/raw/${pid}"
        }
        return new PasteyM3U8Puppet(parent, true, name, pid)
    }

    @Override
    PuppetIterator getChildren() {
        PuppetIterator children = new PasteyPuppetIterator()

        if (sSettingsProvider != null) {
            def SettingsPuppet.Storage storage = sSettingsProvider.getStorage()
            children.add(sSettingsProvider)
            storage.getStringList("pastebin_ids", null).each {
                children.add(convertToPuppet(this, it))
            }
        } else {
            [].each {
                children.add(convertToPuppet(this, it))
            }
        }

        return children
    }

    @Override
    boolean isTopLevel() {
        return true
    }

    @Override
    String getName() {
        return mName
    }

    @Override
    String getCategory() {
        return "Scrapey"
    }

    @Override
    String getShortDescription() {
        return "Find a popular #Pastey pastebin.com ID for an M3U playlist online"
    }

    @Override
    String getImageUrl() {
        return "http://pastebin.com/i/facebook.png"
    }

    @Override
    String getBackgroundImageUrl() {
        return "https://i.ytimg.com/vi/foCP59nBzvY/maxresdefault.jpg"
    }

    @Override
    boolean isUnavailableIn(String region) {
        return false
    }

    @Override
    String getPreferredRegion() {
        return null
    }

    @Override
    int getShieldLevel() {
        return 0
    }

    @Override
    ParentPuppet getParent() {
        return mParent
    }

    @Override
    SearchesPuppet getSearchProvider() {
        if (mSearchProvider == null) {
            mSearchProvider = new PasteySearchesPuppet(this)
        }
        return mSearchProvider
    }

    @Override
    SettingsPuppet getSettingsProvider() {
        if (sSettingsProvider == null) {
            sSettingsProvider = new PreferenceSettingsPuppet(
                    this,
                    "#Pastey Settings",
                    "Find a popular #pastey online or create your own M3U playlist on pastebin.com",
                    getImageUrl(),
                    getBackgroundImageUrl(),
                    SETTINGS_DEFINITION
            )
        }
        return sSettingsProvider
    }

    @Override
    int getFastlaneBackgroundColor() {
        return 0xFF024369
    }

    @Override
    int getSearchAffordanceBackgroundColor() {
        return 0xFF606974
    }

    @Override
    int getSelectedBackgroundColor() {
        return 0xFF024369
    }

    @Override
    int getPlayerBackgroundColor() {
        return 0xFF024369
    }

    @Override
    List<Map<String, String>> getLiveChannelsMetaData() {
        def list = []
        if (sSettingsProvider != null && sSettingsProvider.getBoolean("expose_streams", false)) {
            for (Puppet p : getChildren()) {
                if (p instanceof PasteyM3U8Puppet) {
                    for (Puppet c : p.getChildren()) {
                        def SourcesPuppet.SourceIterator sources = (SourcesPuppet.SourceIterator) ((PasteySourcesPuppet) c).getSources()
                        if (sources.hasNext()) {
                            list << [
                                    name       : c.getName(),
                                    description: c.getShortDescription(),
                                    genres     : c.getCategory(),
                                    logo       : c.getImageUrl(),
                                    url        : sources.next().url
                            ]
                        }
                    }
                }
            }
        }
        return list
    }

    @Override
    PuppetIterator getRelated() {
        return null
    }

    @Override
    public String toString() {
        return getName()
    }

    @Override
    int getVersionCode() {
        return VERSION_CODE
    }

    def static class PasteySearchesPuppet extends PasteyPuppet implements SearchesPuppet {

        def String mSearchQuery

        PasteySearchesPuppet(ParentPuppet parent) {
            super(parent, "Current #Pastey Search")
        }

        @Override
        void setSearchQuery(String searchQuery) {
            mSearchQuery = searchQuery
        }

        @Override
        PuppetIterator getChildren() {
            PuppetIterator children = new PasteyPuppetIterator()

            for (Puppet p : super.getChildren()) {
                if (p instanceof PasteyM3U8Puppet) {
                    for (Puppet c : p.getChildren()) {
                        if (c.getName().toLowerCase().contains(mSearchQuery.toLowerCase())) {
                            children.add(c)
                        }
                    }
                }
            }
            mSearchQuery = null // Reset so this search term doesn't reappear next time
            return children
        }
    }

    def static class PasteyPuppetIterator extends PuppetIterator {

        def ArrayList<Puppet> mPuppets = new ArrayList<>()
        def int currentIndex = 0

        @Override
        boolean hasNext() {
            if (currentIndex < mPuppets.size()) {
                return true
            } else {
                currentIndex = 0 // Reset for reuse
            }
            return false
        }

        @Override
        void add(Puppet puppet) {
            mPuppets.add(puppet)
        }

        @Override
        Puppet next() {
            return mPuppets.get(currentIndex++)
        }

        @Override
        void remove() {
        }
    }

    def class PasteyM3U8Puppet implements ParentPuppet {

        ParentPuppet mParent;
        boolean mIsTopLevel;
        String mName;
        String mUrl;

        PasteyM3U8Puppet(ParentPuppet parent, boolean isTopLevel, String name, String url) {
            mParent = parent
            mIsTopLevel = isTopLevel
            mName = name
            mUrl = url
        }

        @Override
        PuppetIterator getChildren() {
            PuppetIterator children = new PasteyPuppetIterator()
            def List<String> lines = new URL(mUrl).getText().trim().replaceAll("(?m)^\\s+\$", "").readLines()
            def long length = -1
            def String title
            def String imageUrl
            lines.each {
                it = it.trim()
                if (it.startsWith("#EXTINF:")) {
                    try {
                        length = Long.parseLong(it.substring(it.indexOf(":") + 1, it.indexOf(",")))
                    } catch (Exception ex) {
                        length = -1
                    }
                    title = it.substring(it.lastIndexOf(",") + 1).trim()
                    Matcher matcher = it =~ /tvg-logo="(.*?)"/
                    imageUrl = matcher.find() ? matcher.group(1) : null
                } else if (!it.isEmpty() && !it.startsWith("#")) {
                    boolean bypassSource = false
                    if (it.startsWith("rtmp://") && sSettingsProvider != null && !sSettingsProvider.getBoolean("enable_rtmp", false)) {
                        bypassSource = true
                    } else if (it.endsWith(".ts") && sSettingsProvider != null && !sSettingsProvider.getBoolean("enable_ts", false)) {
                        bypassSource = true
                    }
                    if (!bypassSource) {
                        children.add(new PasteySourcesPuppet(
                                this,
                                title,
                                length > 0 ? length * 1000L : length,
                                imageUrl,
                                it
                        ))
                    }
                }
            }
            return children
        }

        @Override
        boolean isTopLevel() {
            return mIsTopLevel
        }

        @Override
        String getName() {
            return mName
        }

        @Override
        String getCategory() {
            return null
        }

        @Override
        String getShortDescription() {
            return null
        }

        @Override
        String getImageUrl() {
            return mParent.getImageUrl()
        }

        @Override
        String getBackgroundImageUrl() {
            return mParent.getBackgroundImageUrl()
        }

        @Override
        boolean isUnavailableIn(String region) {
            return false
        }

        @Override
        String getPreferredRegion() {
            return null
        }

        @Override
        int getShieldLevel() {
            return 0
        }

        @Override
        ParentPuppet getParent() {
            return mParent
        }

        @Override
        PuppetIterator getRelated() {
            return null
        }
    }

    def static class PasteySourcesPuppet implements SourcesPuppet {

        def ParentPuppet mParent
        def String mName
        def long mDuration
        def String mImageUrl
        def String mUrl

        PasteySourcesPuppet(ParentPuppet parent, String name, long duration, String imageUrl, String url) {
            mParent = parent
            mName = name
            mDuration = duration
            mImageUrl = imageUrl
            mUrl = url
        }

        @Override
        String getPublicationDate() {
            return null
        }

        @Override
        long getDuration() {
            return mDuration
        }

        @Override
        SourcesPuppet.SourceIterator getSources() {
            return new PasteySourceIterator()
        }

        @Override
        boolean isLive() {
            return false
        }

        @Override
        List<SourcesPuppet.SubtitleDescription> getSubtitles() {
            return null
        }

        @Override
        String getName() {
            return mName
        }

        @Override
        String getCategory() {
            return null
        }

        @Override
        String getShortDescription() {
            return null
        }

        @Override
        String getImageUrl() {
            return mImageUrl != null ? mImageUrl : mParent.getImageUrl()
        }

        @Override
        String getBackgroundImageUrl() {
            return mParent.getBackgroundImageUrl()
        }

        @Override
        boolean isUnavailableIn(String region) {
            return false
        }

        @Override
        String getPreferredRegion() {
            return null
        }

        @Override
        int getShieldLevel() {
            return 0
        }

        @Override
        ParentPuppet getParent() {
            return mParent
        }

        @Override
        PuppetIterator getRelated() {
            return mParent.getChildren()
        }

        def class PasteySourceIterator implements SourcesPuppet.SourceIterator {

            def List<SourceDescription> mSources = null
            def int currentIndex = 0

            @Override
            boolean hasNext() {
                if (mSources == null) {
                    mSources = new ArrayList<SourceDescription>()
                    SourceDescription source = new SourceDescription()
                    source.url = mUrl
                    mSources.add(source)
                }
                return currentIndex < mSources.size()
            }

            @Override
            SourceDescription next() {
                return mSources.get(currentIndex++)
            }

            @Override
            void remove() {
            }
        }
    }
}