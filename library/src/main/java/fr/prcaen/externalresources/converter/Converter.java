package fr.prcaen.externalresources.converter;

import fr.prcaen.externalresources.model.Resources;
import java.io.IOException;
import java.io.Reader;

public interface Converter {
  Resources fromReader(Reader reader) throws IOException;
}
