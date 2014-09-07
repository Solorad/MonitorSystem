package monitor.attribute.parser;


import monitor.attribute.Catalog;
import monitor.attribute.exception.AttrNameIsNotUniqueException;
import monitor.attribute.exception.AttrTypeNotFoundException;
import monitor.attribute.exception.AttrWrongDefinitionException;
import monitor.attribute.exception.BuildCatalogException;

import java.nio.file.Paths;

public class FileConfParserTest {

    public static void main(String[] args)
            throws AttrWrongDefinitionException, AttrTypeNotFoundException, BuildCatalogException,
            AttrNameIsNotUniqueException {
        FileConfParser fileConfParser = new FileConfParser(Paths.get("catalog.conf"));
        Catalog catalog = fileConfParser.getNewCatalog();
        System.out.println(catalog);
    }
}