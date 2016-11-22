package tv.puppetmaster.data.i;

public interface ParentPuppet extends Puppet {

    /*
     * A parent puppet acts as a directory and getChildren lists its items
     */
    PuppetIterator getChildren();

    /*
     * If top-level, it gets its own row in the main screen with its children listed immediately,
     * otherwise it appears within an existing row and children are exposed when clicked upon
     */
    boolean isTopLevel();
}