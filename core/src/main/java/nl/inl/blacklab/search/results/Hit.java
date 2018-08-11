package nl.inl.blacklab.search.results;

/**
 * Interface for a hit. Normally, hits are iterated over in a Lucene Spans object,
 * but in some places, it makes sense to place hits in separate objects: when
 * caching or sorting hits, or just for convenience in client code.
 */
public interface Hit extends Comparable<Hit> {
    
    /**
     * Create a hit.
     * 
     * @param doc Lucene document id
     * @param start position of first word of the hit
     * @param end first word position after the hit
     * @return the hit
     */
    static Hit create(int doc, int start, int end) {
        return HitStored.create(doc, start, end);
    }
    
    @Override
    boolean equals(Object with);

    @Override
    default int compareTo(Hit o) {
        if (this == o)
            return 0;
        if (doc() == o.doc()) {
            if (start() == o.start()) {
                return end() - o.end();
            }
            return start() - o.start();
        }
        return doc() - o.doc();
    }

    @Override
    String toString();

    @Override
    int hashCode();

    /**
     * Get the document this hit occurs in.
     * @return Lucene document id
     */
    int doc();

    /**
     * Get the start of this hit.
     * @return position of first word of the hit
     */
    int start();

    /**
     * Get the end of this hit.
     * @return position of first word after the hit
     */
    int end();

}
