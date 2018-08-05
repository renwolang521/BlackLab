package nl.inl.blacklab.interfaces.struct;

import nl.inl.blacklab.index.config.ConfigCorpus.TextDirection;

/** Used to write information about a BlackLab index, including its fields structure. */
public interface IndexMetadataWriter {
    
    /**
     * Return the accompanying reading interface
     * @return metadata reader
     */
    IndexMetadata reader();
    
	/**
	 * Save the index structure file.
	 */
	void save();

    /**
	 * Indicate that the index was modified, so that fact
	 * will be recorded in the metadata file.
	 */
	void updateLastModified();

	/**
	 * Set the display name for this index. Only makes
	 * sense in index mode where the change will be saved.
	 * Usually called when creating an index.
	 *
	 * @param displayName the display name to set.
	 */
	void setDisplayName(String displayName);

	/**
	 * Set a document format (or formats) for this index.
	 *
	 * This should be a format identifier as understood by the
	 * DocumentFormats class (either an abbreviation or a
	 * (qualified) class name).
	 *
	 * It only makes sense to call this in index mode, where
	 * this change will be saved.
	 *
	 * @param documentFormat the document format to store
	 */
	void setDocumentFormat(String documentFormat);

	/**
	 * Add some tokens to the total number of tokens in the index.
	 * 
	 * @param tokensProcessed number of tokens to add
	 */
	void addToTokenCount(long tokensProcessed);

    /**
     * Used when creating an index to initialize contentViewable setting. Do not use otherwise.
     *
     * It is also used to support a deprecated configuration setting in BlackLab Server, but
     * this use will eventually be removed.
     *
     * @param contentViewable whether content may be freely viewed
     */
	void setContentViewable(boolean contentViewable);

    /**
     * Used when creating an index to initialize textDirection setting. Do not use otherwise.
     *
     * @param textDirection text direction
     */
    void setTextDirection(TextDirection textDirection);
}
