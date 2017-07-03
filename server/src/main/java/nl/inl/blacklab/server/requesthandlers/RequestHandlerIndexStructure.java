package nl.inl.blacklab.server.requesthandlers;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import nl.inl.blacklab.search.Searcher;
import nl.inl.blacklab.search.indexstructure.ComplexFieldDesc;
import nl.inl.blacklab.search.indexstructure.IndexStructure;
import nl.inl.blacklab.search.indexstructure.IndexStructure.MetadataGroup;
import nl.inl.blacklab.search.indexstructure.MetadataFieldDesc;
import nl.inl.blacklab.search.indexstructure.PropertyDesc;
import nl.inl.blacklab.server.BlackLabServer;
import nl.inl.blacklab.server.datastream.DataStream;
import nl.inl.blacklab.server.exceptions.BlsException;
import nl.inl.blacklab.server.jobs.User;
import nl.inl.util.StringUtil;

/**
 * Get information about the structure of an index.
 */
public class RequestHandlerIndexStructure extends RequestHandler {

	public RequestHandlerIndexStructure(BlackLabServer servlet, HttpServletRequest request, User user, String indexName, String urlResource, String urlPathPart) {
		super(servlet, request, user, indexName, urlResource, urlPathPart);
	}

	@Override
	public boolean isCacheAllowed() {
		return false; // because status might change (or you might reindex)
	}

	@Override
	public int handle(DataStream ds) throws BlsException {
		Searcher searcher = getSearcher();
		IndexStructure struct = searcher.getIndexStructure();

		// Assemble response
		ds.startMap()
			.entry("indexName", indexName)
			.entry("displayName", struct.getDisplayName())
			.entry("description", struct.getDescription())
			.entry("status", indexMan.getIndexStatus(indexName))
			.entry("contentViewable", struct.contentViewable());
		String documentFormat = struct.getDocumentFormat();
		if (documentFormat != null && documentFormat.length() > 0)
			ds.entry("documentFormat", documentFormat);
		if (struct.getTokenCount() > 0)
			ds.entry("tokenCount", struct.getTokenCount());

		ds.startEntry("versionInfo").startMap()
			.entry("blackLabBuildTime", struct.getIndexBlackLabBuildTime())
			.entry("blackLabVersion", struct.getIndexBlackLabVersion())
			.entry("indexFormat", struct.getIndexFormat())
			.entry("timeCreated", struct.getTimeCreated())
			.entry("timeModified", struct.getTimeModified())
		.endMap().endEntry();

		ds.startEntry("fieldInfo").startMap()
			.entry("pidField", StringUtil.nullToEmpty(struct.pidField()))
			.entry("titleField", StringUtil.nullToEmpty(struct.titleField()))
			.entry("authorField", StringUtil.nullToEmpty(struct.authorField()))
			.entry("dateField", StringUtil.nullToEmpty(struct.dateField()))
		.endMap().endEntry();

		ds.startEntry("complexFields").startMap();
		// Complex fields
		//DataObjectMapAttribute doComplexFields = new DataObjectMapAttribute("complexField", "name");
		for (String name: struct.getComplexFields()) {
			ds.startAttrEntry("complexField", "name", name).startMap();
			ComplexFieldDesc fieldDesc = struct.getComplexFieldDesc(name);

			ds	.entry("displayName", fieldDesc.getDisplayName())
				.entry("description", fieldDesc.getDescription())
				.entry("mainProperty", fieldDesc.getMainProperty().getName());

			ds.startEntry("basicProperties").startMap();
			//DataObjectMapAttribute doProps = new DataObjectMapAttribute("property", "name");
			for (String propName: fieldDesc.getProperties()) {
				PropertyDesc propDesc = fieldDesc.getPropertyDesc(propName);
				ds.startAttrEntry("property", "name", propName).startMap()
				    .entry("displayName", propDesc.getDisplayName())
					.entry("sensitivity", propDesc.getSensitivity().toString())
                    .entry("isInternal", propDesc.isInternal())
				.endMap().endAttrEntry();
			}
			ds.endMap().endEntry();

			ds.endMap().endAttrEntry();
		}
		ds.endMap().endEntry();

		ds.startEntry("metadataFields").startMap();
		// Metadata fields
		//DataObjectMapAttribute doMetaFields = new DataObjectMapAttribute("metadataField", "name");
		for (String name: struct.getMetadataFields()) {
			MetadataFieldDesc fd = struct.getMetadataFieldDesc(name);
			ds.startAttrEntry("metadataField", "name", name).startMap()
				.entry("fieldName", fd.getName())
				.entry("displayName", fd.getDisplayName())
				.entry("type", fd.getType().toString())
				.entry("group", fd.getGroup());
			ds.endMap().endAttrEntry();
		}
		ds.endMap().endEntry();

        Map<String, MetadataGroup> metaGroups = struct.getMetaFieldGroups();
        ds.startEntry("metadataFieldGroups").startList();
        for (MetadataGroup metaGroup: metaGroups.values()) {
            ds.startItem("metadataFieldGroup").startMap();
            ds.entry("name", metaGroup.getName());
            ds.startEntry("fields").startList();
            for (String field: metaGroup.getFields()) {
                ds.item("field", field);
            }
            ds.endList().endEntry();
            ds.endMap().endItem();
        }
        ds.endList().endEntry();

		// Remove any empty settings
		//response.removeEmptyMapValues();

		ds.endMap();

		return HTTP_OK;
	}

}