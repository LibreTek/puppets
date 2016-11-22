package tv.puppetmaster.data.i;

public interface SearchesPuppet extends ParentPuppet {

    /*
     * Children are results that match the search query
     */
    void setSearchQuery(String searchQuery);
}