package com.application.domain.utils;

import com.application.domain.entity.User;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

public class UserBatchReader {
    private static final String DISALLOW_DOCTYPE_DECL = "http://apache.org/xml/features/disallow-doctype-decl";

    public static List<User> read(ByteArrayInputStream in) throws Exception {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        dbFactory.setFeature(DISALLOW_DOCTYPE_DECL, true);
        DocumentBuilder db = dbFactory.newDocumentBuilder();
        Document doc = db.parse(in);
        doc.getDocumentElement().normalize();

        List<User> users = new ArrayList<>();
        Node nodeList = doc.getElementsByTagName("users").item(0);
        for (int i = 0; i < nodeList.getChildNodes().getLength(); i++) {
            Node node = nodeList.getChildNodes().item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element eElement = (Element) node;
                User user = readUserFrom(eElement);
                users.add(user);
            }
        }

        return users;
    }

    private static User readUserFrom(Element eElement) {
        User user = new User();
        user.setId(Long.parseLong(eElement.getElementsByTagName("id").item(0).getTextContent()));
        user.setName(eElement.getElementsByTagName("name").item(0).getTextContent());
        user.setAddress(eElement.getElementsByTagName("address").item(0).getTextContent());
        user.setAge(Integer.parseInt(eElement.getElementsByTagName("age").item(0).getTextContent()));
        user.setPhone(eElement.getElementsByTagName("phone").item(0).getTextContent());
        return user;
    }
}
