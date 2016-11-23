package tv.puppetmaster.data;

import java.util.ArrayList;
import java.util.List;

import tv.puppetmaster.data.i.Puppet;

public class BasePuppetIterator  extends Puppet.PuppetIterator {

    private int mCurrentIndex = 0;
    private List<Puppet> mPuppets = new ArrayList<>();

    @Override
    public void add(Puppet puppet) {
        mPuppets.add(puppet);
    }

    @Override
    public boolean hasNext() {
        boolean hasNext = mCurrentIndex < mPuppets.size();
        if (!hasNext) {
            mCurrentIndex = 0; // Reset for subsequent use
        }
        return hasNext;
    }

    @Override
    public Puppet next() {
        return mPuppets.get(mCurrentIndex++);
    }

    @Override
    public void remove() {
    }
}