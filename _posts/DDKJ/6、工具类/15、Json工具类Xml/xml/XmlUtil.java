package com.duodian.youhui.admin.utils;

import com.duodian.youhui.data.message.NewsArticle;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * @Desc: 微信消息格式以及xml和对象的转化
 * @Date:  2018/5/24 下午5:54.
 */

public class WeChatMessageUtil {

    /**
     * 将xml转化为Map集合
     *
     * @param request
     * @return
     */
    public static Map<String, String> xmlToMap(HttpServletRequest request) {
        Map<String, String> map = new HashMap<String, String>();
        SAXReader reader = new SAXReader();
        InputStream ins = null;
        try {
            ins = request.getInputStream();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        Document doc = null;
        try {
            doc = reader.read(ins);
        } catch (DocumentException e1) {
            e1.printStackTrace();
        }
        Element root = doc.getRootElement();
        @SuppressWarnings("unchecked")
        List<Element> list = root.elements();
        for (Element e : list) {
            map.put(e.getName(), e.getText());
        }
        try {
            ins.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return map;
    }

    /**
     * 文本消息转化为xml
     *
     * @param object
     * @return
     */
    public static String textMessageToXml(Object object) {
        XStream xstream = new XStream(new DomDriver("utf-8"));
        //默认是包名加类名为标签，这里改为xml为标签
        xstream.alias("xml", object.getClass());
        //默认是包名加类名为标签，这里改为item为标签
        xstream.alias("item", new NewsArticle().getClass());

        return xstream.toXML(object);

    }

}