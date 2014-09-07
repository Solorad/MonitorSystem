package main.monitor.attribute.parser;


import main.monitor.attribute.Catalog;
import main.monitor.attribute.exception.AttrNameIsNotUniqueException;
import main.monitor.attribute.exception.AttrTypeNotFoundException;
import main.monitor.attribute.exception.AttrWrongDefinitionException;
import main.monitor.attribute.exception.BuildCatalogException;

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