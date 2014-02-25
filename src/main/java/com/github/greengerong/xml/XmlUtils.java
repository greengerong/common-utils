package com.github.greengerong.xml;


import com.github.greengerong.exception.CommonUtilsRuntimeException;
import org.w3c.dom.Node;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;

public class XmlUtils {

    public static String toXml(final Node node) {
        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            final StringWriter sw = new StringWriter();
            transformer.transform(new DOMSource(node), new StreamResult(sw));
            return sw.toString();
        } catch (Exception e) {
            throw new CommonUtilsRuntimeException(e);
        }
    }

    public static <T> T deserialization(final Node node, final Class<T> type) {
        try {
            JAXBContext jc = JAXBContext.newInstance(type);
            Unmarshaller u = jc.createUnmarshaller();
            return (T) u.unmarshal(node);
        } catch (JAXBException e) {
            throw new CommonUtilsRuntimeException(e);
        }
    }

    public static <T> T deserialization(final String xml, final Class<T> type) {
        try {
            JAXBContext jc = JAXBContext.newInstance(type);
            Unmarshaller u = jc.createUnmarshaller();
            return (T) u.unmarshal(new StringReader(xml));
        } catch (JAXBException e) {
            throw new CommonUtilsRuntimeException(e);
        }
    }

    public static <T> T deserialization(final InputStream inputStream, final Class<T> type) {
        try {
            JAXBContext jc = JAXBContext.newInstance(type);
            Unmarshaller u = jc.createUnmarshaller();
            return (T) u.unmarshal(inputStream);
        } catch (JAXBException e) {
            throw new CommonUtilsRuntimeException(e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    throw new CommonUtilsRuntimeException(e);
                }
            }
        }
    }

    public static <T> String serialization(final T body, final Class<T> type) {
        try {
            JAXBContext jc = JAXBContext.newInstance(type);
            Marshaller u = jc.createMarshaller();
            final StringWriter sw = new StringWriter();
            u.marshal(body, sw);
            return sw.toString();
        } catch (JAXBException e) {
            throw new CommonUtilsRuntimeException(e);
        }
    }

    public static <T> void serialization(final T body, final Class<T> type, final OutputStream outputStream) {
        try {
            JAXBContext jc = JAXBContext.newInstance(type);
            Marshaller u = jc.createMarshaller();
            u.marshal(body, outputStream);
        } catch (JAXBException e) {
            throw new CommonUtilsRuntimeException(e);
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.flush();
                    outputStream.close();
                } catch (IOException e) {
                    throw new CommonUtilsRuntimeException(e);
                }
            }
        }
    }
}
