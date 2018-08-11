/*******************************************************************************
 * Copyright (c) 2010, 2012 Institute for Dutch Lexicology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package nl.inl.blacklab.search.results;

import java.util.List;

import nl.inl.blacklab.resultproperty.HitPropValue;
import nl.inl.blacklab.search.BlackLabIndex;
import nl.inl.blacklab.search.indexmetadata.AnnotatedField;

/**
 * A group of results, with its group identity and the results themselves, that
 * you can access randomly (i.e. you can obtain a list of Hit objects)
 */
public class HitGroup extends Group {
    Hits results;

    HitGroup(BlackLabIndex index, HitPropValue groupIdentity, AnnotatedField field, HitsSettings settings) {
        super(groupIdentity);
        results = Hits.emptyList(index, field, settings);
    }

    /**
     * Wraps a list of Hit objects with the HitGroup interface.
     *
     * NOTE: the list is not copied!
     *
     * @param index the searcher that produced the hits
     * @param groupIdentity grouping identity of this group of hits
     * @param field concordance field
     * @param hits the hits
     */
    HitGroup(BlackLabIndex index, HitPropValue groupIdentity, AnnotatedField field, List<Hit> hits, HitsSettings settings) {
        super(groupIdentity);
        results = Hits.fromList(index, field, hits, settings);
    }

    public Hits getHits() {
        return results;
    }

    public int size() {
        return results.size();
    }

    @Override
    public String toString() {
        return "GroupOfHits, identity = " + groupIdentity + ", size = " + results.size();
    }
}
