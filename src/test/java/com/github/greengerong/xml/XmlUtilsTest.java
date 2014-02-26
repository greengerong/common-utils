package com.github.greengerong.xml;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;


public class XmlUtilsTest {
    @Test
    public void should_covert_to_xml() throws Exception {
        //given
        final InputStream stream = this.getClass().getResourceAsStream("/xml/sample.xml");
        final Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(stream);
        final NodeList node = doc.getElementsByTagName("GetOrdersHistoryResponse");

        //when
        final String xml = XmlUtils.toXml(node.item(0));

        //then
        assertThat(xml, is(containsString("<GetOrdersHistoryResponse>")));
    }
}



