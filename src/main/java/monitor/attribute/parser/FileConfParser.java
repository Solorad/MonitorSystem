package monitor.attribute.parser;

import monitor.attribute.Attribute;
import monitor.attribute.AttributeFactory;
import monitor.attribute.Catalog;
import monitor.attribute.exception.AttrNameIsNotUniqueException;
import monitor.attribute.exception.AttrTypeNotFoundException;
import monitor.attribute.exception.AttrWrongDefinitionException;
import monitor.attribute.exception.BuildCatalogException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Configuration parser for files.
 *
 * @author Evgenii Morenkov
 */
public class FileConfParser implements IParser {
    private Path sourcePath;
    /**
     * Holds information about line on which attribute with such name.
     * KeyMap is modified only in moment of catalog creation. Then, all threads just lookup in it.
     */
    private ConcurrentHashMap<String, Integer> attrLineMap;
    /**
     * Thread-safe attribute list for faster rewrite to file.
     */
    private CopyOnWriteArrayList<String> attributeStrings;

    private Catalog catalog;

    /**
     * Constructor
     *
     * @param sourcePath - file with catalog defintion address.
     */
    public FileConfParser(Path sourcePath) {
        this.sourcePath = sourcePath;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Catalog getNewCatalog() throws BuildCatalogException, AttrTypeNotFoundException,
            AttrWrongDefinitionException, AttrNameIsNotUniqueException {
        //initialize values
        catalog = new Catalog();
        ConcurrentHashMap<String, Attribute> attrMap = new ConcurrentHashMap<>();
        attrLineMap = new ConcurrentHashMap<>();
        Integer lineNumber = 0;
        try {
            // read all file in string list nad put it in concurrent array list.
            List<String> attrs = Files.readAllLines(sourcePath, Charset.defaultCharset());
            attributeStrings = new CopyOnWriteArrayList<>(attrs);
            // build attribute map
            for (String t : attrs) {
                Attribute attribute = getAttributeFromString(t);
                // check that attribute name is unique.
                String attrName = attribute.getAttrName();
                if (attrMap.containsKey(attrName)) {
                    throw new AttrNameIsNotUniqueException("There is more than one '" + attrName
                                                                   + "' attribute");
                }
                // put attribute into maps
                attrMap.put(attrName, attribute);
                attrLineMap.put(attrName, lineNumber);
                lineNumber++;
            }
            catalog.setAttrMap(attrMap);
        } catch (IOException e) {
            e.printStackTrace();
            throw new BuildCatalogException();
        }

        return catalog;
    }

    /**
     * Saves attribute to file. Rewrite all attributes with updated one
     * into new temporary file. Then replace main file with updated one.
     *
     * @param attribute - new attribute value.
     */
    @Override
    public void saveAttribute(Attribute attribute) {
        // update attributes representation in 'attributeStrings' and 'attrMap'.
        String stringFromAttr = attribute.getStringFromAttr();
        String attrName = attribute.getAttrName();
        Integer attrNumber = attrLineMap.get(attrName);
        attributeStrings.set(attrNumber, stringFromAttr);
        catalog.getAttrMap().put(attrName, attribute);

        //generate random file name for tmp file
        Double randomValue = Math.random() * 1e10;
        String randomFileName = ((Long) randomValue.longValue()).toString() + ".tmp";

        Path tmpFile = Paths.get(randomFileName);
        try {
            tmpFile = Files.createFile(tmpFile);
        } catch (IOException ex) {
            System.err.println("Error creating file");
        }
        try (BufferedWriter writer = Files.newBufferedWriter(
                tmpFile, Charset.defaultCharset())) {
            for (String attributeString : attributeStrings) {
                writer.append(attributeString);
                writer.newLine();
            }
            writer.flush();
            Files.move(tmpFile, sourcePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            // no logger available. Print stack trace to console.
            e.printStackTrace();
        }
    }

    /**
     * Retrieve attribute from file by its name.
     * Look up in map with line number and read file only for this line.
     *
     * @param attrName - attribute name.
     * @return value from file.
     * @throws AttrWrongDefinitionException
     * @throws AttrTypeNotFoundException
     */
    @Override
    public Attribute getAttribute(String attrName) throws AttrWrongDefinitionException, AttrTypeNotFoundException {
        //if file was not changed - get Attribute from attrMap.
        Integer attrNumber = attrLineMap.get(attrName);
        int lineNumber = 0;
        try (BufferedReader reader = Files.newBufferedReader(
                sourcePath, Charset.defaultCharset())) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (lineNumber == attrNumber) {
                    return getAttributeFromString(line);
                }
                lineNumber++;
            }
        } catch (IOException exception) {
            System.out.println("Error while reading file");
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ConcurrentHashMap<String, Attribute> getAttrMap() {
        return catalog == null ? null : catalog.getAttrMap();
    }

    /**
     * Unmarshall attribute from String.
     * File String format:
     * "_TYPE_ _ATTRIBUTE_INFO_".
     *
     * @param attrString - attribute string.
     * @return Attribute
     */
    private Attribute getAttributeFromString(String attrString)
            throws AttrTypeNotFoundException, AttrWrongDefinitionException {
        int spaceAttachment = attrString.indexOf(" ");
        String type = attrString.substring(0, spaceAttachment);
        String attributeInfo = attrString.substring(spaceAttachment + 1);
        return AttributeFactory.getAttributeByType(type, attributeInfo);
    }

    /**
     * Sets new sourcePath.
     *
     * @param sourcePath New value of sourcePath.
     */
    public void setSourcePath(Path sourcePath) {
        this.sourcePath = sourcePath;
    }


    /**
     * Gets sourcePath.
     *
     * @return Value of sourcePath.
     */
    public Path getSourcePath() {
        return sourcePath;
    }
}
