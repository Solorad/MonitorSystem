package monitor.attribute.parser;

import monitor.attribute.Attribute;
import monitor.attribute.Catalog;
import monitor.attribute.exception.AttrNameIsNotUniqueException;
import monitor.attribute.exception.AttrTypeNotFoundException;
import monitor.attribute.exception.AttrWrongDefinitionException;
import monitor.attribute.exception.BuildCatalogException;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Interface for abstract parser.
 *
 * @author Evgenii Morenkov
 */
public interface IParser {

    /**
     * Retrieve catalog.
     *
     * @return Catalog with settled attributes
     */
    Catalog getNewCatalog() throws BuildCatalogException, AttrTypeNotFoundException, AttrWrongDefinitionException,
            AttrNameIsNotUniqueException;

    /**
     * Save catalog to destination.
     */
    void saveAttribute(Attribute attribute);

    /**
     * Get updated value of attribute by its name.
     *
     * @param name - attribute name
     * @return Attribute
     */
    Attribute getAttribute(String name) throws AttrWrongDefinitionException, AttrTypeNotFoundException;

    /**
     * Get map of attributes.
     *
     * @return attributes map
     */
    ConcurrentHashMap<String, Attribute> getAttrMap();
}
