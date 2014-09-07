package main.monitor.attribute.parser;

import main.monitor.attribute.Attribute;
import main.monitor.attribute.Catalog;
import main.monitor.attribute.exception.AttrNameIsNotUniqueException;
import main.monitor.attribute.exception.AttrTypeNotFoundException;
import main.monitor.attribute.exception.AttrWrongDefinitionException;
import main.monitor.attribute.exception.BuildCatalogException;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Interface for abstract parser.
 *
 * @author Evgenii Morenkov
 */
public interface IParser {

    /**
     * Retrieve catalog.
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
