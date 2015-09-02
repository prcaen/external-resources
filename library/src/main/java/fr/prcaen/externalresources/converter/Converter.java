package fr.prcaen.externalresources.converter;

import java.io.IOException;
import java.io.Reader;

import fr.prcaen.externalresources.model.Resources;

public interface Converter {
  Resources fromReader(Reader reader) throws IOException;

  Resources fromString(String string) throws IOException;
}
