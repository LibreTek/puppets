package tv.puppetmaster.data.i;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public interface SourcesPuppet extends Puppet {
    /*
     * The date of publication for this content
     */
    String getPublicationDate();

    /*
     * Its duration in milliseconds
     */
    long getDuration();

    /*
     * An iterator listing the actual sources of this content
     */
    SourceIterator getSources();

    /*
     * If it is live
     */
    boolean isLive();

    /*
     * Uri => Description
     */
    List<SubtitleDescription> getSubtitles();

    /*
     * We want to lazy-load sources in case the implementation requires network access to parse each
     */
    interface SourceIterator extends Iterator<SourceDescription>, Serializable {
    }

    class SourceDescription implements Serializable {
        public String url;
        public long duration;
        public String height;
        public String width;
        public String rating;
        public boolean isAudioOnly;
        public long bitrate;
        public String userAgent;
        public String referer;
        public Map<String, String> requestHeaders;
        public String drmScheme;
        public String drmLicenseUrl;
    }

    class SubtitleDescription implements Serializable {
        public String locale = Locale.ENGLISH.getLanguage();
        public String mime = "text/srt";
        public String url;
    }
}