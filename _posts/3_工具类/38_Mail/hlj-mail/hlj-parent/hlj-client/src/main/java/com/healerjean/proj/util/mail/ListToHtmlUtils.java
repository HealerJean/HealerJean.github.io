package com.healerjean.proj.util.mail;

import com.healerjean.proj.util.mail.dto.MailField;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class ListToHtmlUtils {
    private ListToHtmlUtils() {
    }

    /**
     * 转换HTML
     *
     * @param list 数据
     * @param <E>  E
     * @return HTML
     */
    public static <E> String convertHtml(List<E> list) {
        return convertHtml(list, null, null);
    }

    /**
     * 转换HTML
     *
     * @param list  数据
     * @param title 自定义内容
     * @param <E>   E
     * @return HTML
     */
    public static <E> String convertHtml(List<E> list, String title, String bottom) {
        Class<?> clazz = list.get(0).getClass();
        List<Field> fieldList = getFieldList(clazz);
        StringBuilder builder = new StringBuilder();
        builder.append(defaultStyle());
        if (StringUtils.isNotBlank(title)) {
            builder.append("<h2>").append(title).append("</h2>");
        }
        builder.append("<table>").append("<tr>");
        List<Field> tableFieldList = new ArrayList<>();
        for (Field field : fieldList) {
            MailField mailField = field.getAnnotation(MailField.class);
            if (null == mailField) {
                continue;
            }
            builder.append("<th>").append(mailField.value()).append("</th>");
            tableFieldList.add(field);
        }
        builder.append("</tr>");
        list.forEach(data -> {
            builder.append("</tr>");
            for (Field field : tableFieldList) {
                Object value = null;
                try {
                    PropertyDescriptor descriptor = new PropertyDescriptor(field.getName(), clazz);
                    value = descriptor.getReadMethod().invoke(data);
                } catch (Exception e) {
                    throw new RuntimeException(e.getMessage(), e);
                }
                builder.append("<td>").append(null == value ? StringUtils.EMPTY : value.toString()).append("</td>");
            }
            builder.append("</tr>");
        });
        builder.append("</table>");

        if (StringUtils.isNotBlank(bottom)) {
            builder.append("<h2>").append(bottom).append("</h2>");
        }

        return builder.toString();
    }



    /**
     * 获取class所有Field对象
     *
     * @param clazz Class
     * @return Field列表
     */
    public static List<Field> getFieldList(Class<?> clazz) {
        List<Field> fieldList = new ArrayList<>();
        while (null != clazz) {
            Field[] fieldArr = clazz.getDeclaredFields();
            fieldList.addAll(Arrays.asList(fieldArr));
            clazz = clazz.getSuperclass();
        }
        return fieldList;
    }


    /**
     * Style
     *
     * @return Style
     */
    private static String defaultStyle() {
        return "<style type=\"text/css\">html,body,div,span,object,iframe,h1,h2,h3,h4,h5,h6,p,blockquote,pre,abbr,address,cite,code,del,dfn,em,img,ins,kbd,q,samp,small,strong,sub,sup,var,b,i,dl,dt,dd,ol,ul,li,fieldset,form,label,legend,table,caption,tbody,tfoot,thead,tr,th,td{margin:0;padding:0;border:0;outline:0;font-size:100%;vertical-align:baseline;background:transparent;}a{color:#666;}#content{width:65%;max-width:690px;margin:6% auto 0;}table{overflow:atuo;border:1px solid #d3d3d3;background:#fefefe;width:90%;margin:5% auto 0;-moz-border-radius:5px;-webkit-border-radius:5px;border-radius:5px;-moz-box-shadow:0 0 4px rgba(0,0,0,0.2);-webkit-box-shadow:0 0 4px rgba(0,0,0,0.2);}tr.odd-row td{background:#f6f6f6;}td.first,th.first{text-align:left}td.last{border-right:none;}tr:first-child th.first{-moz-border-radius-topleft:5px;-webkit-border-top-left-radius:5px;}tr:first-child th.last{-moz-border-radius-topright:5px;-webkit-border-top-right-radius:5px;}tr:last-child td.first{-moz-border-radius-bottomleft:5px;-webkit-border-bottom-left-radius:5px;}tr:last-child td.last{-moz-border-radius-bottomright:5px;-webkit-border-bottom-right-radius:5px;}body{font-family:'JDLangZhengTi','Lucida sans',Arial;font-size:14px;color:#444;}table{*border-collapse:collapse;border-spacing:0;width:100%;}table{border:solid #ccc 1px;-moz-border-radius:6px;-webkit-border-radius:6px;border-radius:6px;-webkit-box-shadow:0 1px 1px #ccc;-moz-box-shadow:0 1px 1px #ccc;box-shadow:0 1px 1px #ccc;}table tr:hover{background:#fbf8e9;-o-transition:all 0.1s ease-in-out;-webkit-transition:all 0.1s ease-in-out;-moz-transition:all 0.1s ease-in-out;-ms-transition:all 0.1s ease-in-out;transition:all 0.1s ease-in-out;}table td,table th{border-left:1px solid #ccc;border-top:1px solid #ccc;}table td{padding:4px 7px 4px;text-align:center;}table th{}table thead th{width:60px;background-color:#dce9f9;background-image:-webkit-gradient(linear,left top,left bottom,from(#ebf3fc),to(#dce9f9));background-image:-webkit-linear-gradient(top,#ebf3fc,#dce9f9);background-image:-moz-linear-gradient(top,#ebf3fc,#dce9f9);background-image:-ms-linear-gradient(top,#ebf3fc,#dce9f9);background-image:-o-linear-gradient(top,#ebf3fc,#dce9f9);background-image:linear-gradient(top,#ebf3fc,#dce9f9);-webkit-box-shadow:0 1px 0 rgba(255,255,255,.8) inset;-moz-box-shadow:0 1px 0 rgba(255,255,255,.8) inset;box-shadow:0 1px 0 rgba(255,255,255,.8) inset;border-top:none;text-shadow:0 1px 0 rgba(255,255,255,.5);}table td:first-child,table th:first-child{border-left:none;}table th:first-child{-moz-border-radius:6px 0 0 0;-webkit-border-radius:6px 0 0 0;border-radius:6px 0 0 0;}table th:last-child{-moz-border-radius:0 6px 0 0;-webkit-border-radius:0 6px 0 0;border-radius:0 6px 0 0;}table th:only-child{-moz-border-radius:6px 6px 0 0;-webkit-border-radius:6px 6px 0 0;border-radius:6px 6px 0 0;}table tr:last-child td:first-child{-moz-border-radius:0 0 0 6px;-webkit-border-radius:0 0 0 6px;border-radius:0 0 0 6px;}table tr:last-child td:last-child{-moz-border-radius:0 0 6px 0;-webkit-border-radius:0 0 6px 0;border-radius:0 0 6px 0;}table td,th{border-bottom:1px solid #f2f2f2;}table tbody tr:nth-child(even){background:#f5f5f5;-webkit-box-shadow:0 1px 0 rgba(255,255,255,.8) inset;-moz-box-shadow:0 1px 0 rgba(255,255,255,.8) inset;box-shadow:0 1px 0 rgba(255,255,255,.8) inset;}table th{text-align:center;text-shadow:0 1px 0 rgba(255,255,255,.5);border-bottom:1px solid #ccc;background-color:#eee;background-image:-webkit-gradient(linear,left top,left bottom,from(#f5f5f5),to(#eee));background-image:-webkit-linear-gradient(top,#f5f5f5,#eee);background-image:-moz-linear-gradient(top,#f5f5f5,#eee);background-image:-ms-linear-gradient(top,#f5f5f5,#eee);background-image:-o-linear-gradient(top,#f5f5f5,#eee);background-image:linear-gradient(top,#f5f5f5,#eee);}table th:first-child{-moz-border-radius:6px 0 0 0;-webkit-border-radius:6px 0 0 0;border-radius:6px 0 0 0;}table th:last-child{-moz-border-radius:0 6px 0 0;-webkit-border-radius:0 6px 0 0;border-radius:0 6px 0 0;}table th:only-child{-moz-border-radius:6px 6px 0 0;-webkit-border-radius:6px 6px 0 0;border-radius:6px 6px 0 0;}table tfoot td{border-bottom:0;border-top:1px solid #fff;background-color:#f1f1f1;}table tfoot td:first-child{-moz-border-radius:0 0 0 6px;-webkit-border-radius:0 0 0 6px;border-radius:0 0 0 6px;}table tfoot td:last-child{-moz-border-radius:0 0 6px 0;-webkit-border-radius:0 0 6px 0;border-radius:0 0 6px 0;}table tfoot td:only-child{-moz-border-radius:0 0 6px 6px;-webkit-border-radius:0 0 6px 6px border-radius:0 0 6px 6px}</style>";
    }

    public static String getSimpleCss() {
        return "<style type=\"text/css\">\r\n\r\n\thtml, body, div, span, object, iframe,\r\n\th1, h2, h3, h4, h5, h6, p, blockquote, pre,\r\n\tabbr, address, cite, code,\r\n\tdel, dfn, em, img, ins, kbd, q, samp,\r\n\tsmall, strong, sub, sup, var,\r\n\tb, i,\r\n\tdl, dt, dd, ol, ul, li,\r\n\tfieldset, form, label, legend,\r\n\ttable, caption, tbody, tfoot, thead, tr, th, td {\r\n\t\tmargin:0;\r\n\t\tpadding:0;\r\n\t\tborder:0;\r\n\t\toutline:0;\r\n\t\tfont-size:100%;\r\n\t\tvertical-align:baseline;\r\n\t\tbackground:transparent;\r\n\t}\r\n\t\r\n\tbody {\r\n\t\tmargin:0;\r\n\t\tpadding:0;\r\n\t\tfont:12px/15px \"Helvetica Neue\",Arial, Helvetica, sans-serif;\r\n\t\tcolor: #555;\r\n\t\tbackground:#f5f5f5 url(bg.jpg);\r\n\t}\r\n\t\r\n\ta {color:#666;}\r\n\t\r\n\t#content {width:65%; max-width:690px; margin:6% auto 0;}\r\n\t\r\n\t/*\r\n\tPretty Table Styling\r\n\tCSS Tricks also has a nice writeup: http://css-tricks.com/feature-table-design/\r\n\t*/\r\n\t\r\n\ttable {\r\n\t\toverflow:atuo;\r\n\t\tborder:1px solid #d3d3d3;\r\n\t\tbackground:#fefefe;\r\n\t\twidth:100%;\r\n\t\tmargin:5% auto 0;\r\n\t\t-moz-border-radius:5px; /* FF1+ */\r\n\t\t-webkit-border-radius:5px; /* Saf3-4 */\r\n\t\tborder-radius:5px;\r\n\t\t-moz-box-shadow: 0 0 4px rgba(0, 0, 0, 0.2);\r\n\t\t-webkit-box-shadow: 0 0 4px rgba(0, 0, 0, 0.2);\r\n\t}\r\n\t\r\n\tth, td {padding:8px 8px 8px; text-align:center; }\r\n\t\r\n\tth {padding-top:11px; text-shadow: 1px 1px 1px #fff; background:#e8eaeb;}\r\n\t\r\n\ttd {border-top:1px solid #e0e0e0; border-right:1px solid #e0e0e0;}\r\n\t\r\n\ttr.odd-row td {background:#f6f6f6;}\r\n\t\r\n\ttd.first, th.first {text-align:left}\r\n\t\r\n\ttd.last {border-right:none;}\r\n\t\r\n\t/*\r\n\tBackground gradients are completely unnecessary but a neat effect.\r\n\t*/\r\n\t\r\n\ttd {\r\n\t\tbackground: -moz-linear-gradient(100% 25% 90deg, #fefefe, #f9f9f9);\r\n\t\tbackground: -webkit-gradient(linear, 0% 0%, 0% 25%, from(#f9f9f9), to(#fefefe));\r\n\t}\r\n\t\r\n\ttr.odd-row td {\r\n\t\tbackground: -moz-linear-gradient(100% 25% 90deg, #f6f6f6, #f1f1f1);\r\n\t\tbackground: -webkit-gradient(linear, 0% 0%, 0% 25%, from(#f1f1f1), to(#f6f6f6));\r\n\t}\r\n\t\r\n\tth {\r\n\t\tbackground: -moz-linear-gradient(100% 20% 90deg, #e8eaeb, #ededed);\r\n\t\tbackground: -webkit-gradient(linear, 0% 0%, 0% 20%, from(#ededed), to(#e8eaeb));\r\n\t}\r\n\t\r\n\t/*\r\n\tI know this is annoying, but we need additional styling so webkit will recognize rounded corners on background elements.\r\n\tNice write up of this issue: http://www.onenaught.com/posts/266/css-inner-elements-breaking-border-radius\r\n\t\r\n\tAnd, since we've applied the background colors to td/th element because of IE, Gecko browsers also need it.\r\n\t*/\r\n\t\r\n\ttr:first-child th.first {\r\n\t\t-moz-border-radius-topleft:5px;\r\n\t\t-webkit-border-top-left-radius:5px; /* Saf3-4 */\r\n\t}\r\n\t\r\n\ttr:first-child th.last {\r\n\t\t-moz-border-radius-topright:5px;\r\n\t\t-webkit-border-top-right-radius:5px; /* Saf3-4 */\r\n\t}\r\n\t\r\n\ttr:last-child td.first {\r\n\t\t-moz-border-radius-bottomleft:5px;\r\n\t\t-webkit-border-bottom-left-radius:5px; /* Saf3-4 */\r\n\t}\r\n\t\r\n\ttr:last-child td.last {\r\n\t\t-moz-border-radius-bottomright:5px;\r\n\t\t-webkit-border-bottom-right-radius:5px; /* Saf3-4 */\r\n\t}\r\n\r\n</style>";
    }


}
