package fr.prcaen.externalresources.converter;

import android.support.annotation.Nullable;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import fr.prcaen.externalresources.model.Resource;
import fr.prcaen.externalresources.model.Resources;

public final class XmlConverter implements Converter {
  private static final String ATTRIBUTE_NAME = "name";

  private static final String STRING_NODE_NAME = "string";
  private static final String COLOR_NODE_NAME = "color";
  private static final String DIMEN_NODE_NAME = "dimen";
  private static final String INTEGER_NODE_NAME = "integer";
  private static final String BOOL_NODE_NAME = "bool";
  private static final String STRING_ARRAY_NODE_NAME = "string-array";
  private static final String INTEGER_ARRAY_NODE_NAME = "integer-array";

  @Override
  public Resources fromReader(Reader reader) throws IOException {
    try {
      Document document = read(reader);
      Resources resources = new Resources();

      Element root = document.getDocumentElement();

      for (int i = 0; i < root.getChildNodes().getLength(); i++) {
        Node node = root.getChildNodes().item(i);
        if (node.getNodeType() == Node.ELEMENT_NODE) {
          SimpleEntry<String, Resource> entry = get(node);
          if (entry != null) {
            resources.add(entry.getKey(), entry.getValue());
          }
        }
      }

      return resources;
    } catch (ParserConfigurationException | SAXException e) {
      throw new IOException(e);
    }
  }

  @SuppressWarnings("unused")
  public Resources fromString(String string) throws IOException {
    return fromReader(new StringReader(string));
  }

  @Nullable
  protected SimpleEntry<String, Resource> get(Node node) throws DOMException {
    final String key = node.getAttributes().getNamedItem(ATTRIBUTE_NAME).getNodeValue();
    final Resource value = getResource(node);

    if (value != null) {
      return new SimpleEntry<>(key, value);
    } else {
      return null;
    }
  }

  @Nullable
  protected Resource getResource(Node node) {
    switch (node.getNodeName()) {
      case STRING_NODE_NAME:
      case COLOR_NODE_NAME:
      case DIMEN_NODE_NAME:
        return new Resource(node.getTextContent());
      case INTEGER_NODE_NAME:
        return new Resource(Integer.valueOf(node.getTextContent()));
      case BOOL_NODE_NAME:
        return new Resource(Boolean.valueOf(node.getTextContent()));
      case STRING_ARRAY_NODE_NAME:
        return getResource(String.class, node.getChildNodes());
      case INTEGER_ARRAY_NODE_NAME:
        return getResource(Integer.class, node.getChildNodes());
      default:
        return null;
    }
  }

  @Nullable
  protected <T> Resource getResource(Class<T> clazz, Node node) {
    if (clazz.equals(Integer.class)) {
      return new Resource(Integer.valueOf(node.getTextContent()));
    } else if (clazz.equals(String.class)) {
      return new Resource(node.getTextContent());
    } else {
      return null;
    }
  }

  protected <T> Resource getResource(Class<T> clazz, NodeList nodeList) {
    ArrayList<Resource> resources = new ArrayList<>();

    for (int i = 0; i < nodeList.getLength(); i++) {
      Node node = nodeList.item(i);
      if (node.getNodeType() == Node.ELEMENT_NODE) {
        Resource resource = getResource(clazz, node);
        if (resource != null) {
          resources.add(resource);
        }
      }
    }

    return new Resource(resources.toArray(new Resource[resources.size()]));
  }

  protected static Document read(Reader xml) throws ParserConfigurationException, IOException, SAXException {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    factory.setIgnoringElementContentWhitespace(true);
    factory.setNamespaceAware(true);

    return factory.newDocumentBuilder().parse(new InputSource(xml));
  }
}
