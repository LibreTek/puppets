package tv.puppetmaster.data.i;

import java.util.List;
import java.util.Map;

public interface InstallablePuppet extends ParentPuppet {

    /*
     * The version code implies which API features are supported:
     * Live channels          >= 2
     * Branding               >= 2
     * Region handling        >= 4
     * Settings               >= 5
     * User-Agent             >= 6
     */
    int getVersionCode();

    /*
     * If the puppet provides the ability to search, this provider is passed the input
     */
    SearchesPuppet getSearchProvider();

    /*
     * Exposes settings that may be optional or required to retrieve content
     */
    SettingsPuppet getSettingsProvider();

    /*
     * Custom branding for this puppet, specifies the fastlane background color
     */
    int getFastlaneBackgroundColor();

    /*
     * Custom branding for this puppet, specifies the search affordance background color
     */
    int getSearchAffordanceBackgroundColor();

    /*
     * Custom branding for this puppet, specifies the selected item's background color
     */
    int getSelectedBackgroundColor();

    /*
     * Custom branding for this puppet, specifies the player background color
     */
    int getPlayerBackgroundColor();

    /*
     * Expose live channels through a list of maps, each declaring at least: {name, url}
     * Optionally declare {description, logo}
     */
    List<Map<String, String>> getLiveChannelsMetaData();
}