package com.github.larryr1.verilock.data.importing;

public class IdentityImportResult {
    private final int identitiesAdded;
    private final int identitiesSkipped;
    private final long msDuration;

    public IdentityImportResult(int identitiesAdded, int identitiesSkipped, long msDuration) {
        this.identitiesAdded = identitiesAdded;
        this.identitiesSkipped = identitiesSkipped;
        this.msDuration = msDuration;
    }

    public int getIdentitiesAdded() {
        return this.identitiesAdded;
    }

    public int getIdentitiesSkipped() {
        return this.identitiesSkipped;
    }

    public long getMsDuration() {
        return msDuration;
    }
}
