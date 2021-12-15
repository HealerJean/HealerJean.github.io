package com.healerjean.proj.util.mail;

import com.healerjean.proj.util.mail.dto.ExcelAttribute;
import com.healerjean.proj.util.mail.dto.ExportFiledBean;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class ListToHtmlTableUtils {
    private ListToHtmlTableUtils() {
    }


    public static StringBuffer listToTable(List<?> list) throws Exception {
        Class cls = list.get(0).getClass();
        List<Field> fields = getFields(cls);
        List<ExportFiledBean> listBean = new ArrayList();
        StringBuffer sb = new StringBuffer();
        sb.append(getSimpleCssV2());
        sb.append("<table>");
        sb.append("<tr>");
        Iterator var8 = fields.iterator();

        while (true) {
            ExportFiledBean exportFiledBean;
            Iterator var15;
            Field field;
            while (var8.hasNext()) {
                field = (Field) var8.next();
                field.setAccessible(true);
                ExcelAttribute attr = field.getAnnotation(ExcelAttribute.class);
                exportFiledBean = new ExportFiledBean(field);
                listBean.add(exportFiledBean);
                setHeadCellValue(sb, attr);
            }

            sb.append("</tr>");
            if (CollectionUtils.isEmpty(list)) {
                sb.append("<tr>");
                sb.append("<td rowspan=\"" + listBean.size() + "\">");
                sb.append("</td>");
                sb.append("</tr>");
            } else {
                label80:
                for (var8 = list.iterator(); var8.hasNext(); sb.append("</tr>")) {
                    Object t = var8.next();
                    sb.append("<tr>");
                    if (Objects.nonNull(t)) {
                        Iterator var19 = listBean.iterator();

                        while (true) {
                            List listTemp;
                            do {
                                while (true) {
                                    if (!var19.hasNext()) {
                                        continue label80;
                                    }

                                    exportFiledBean = (ExportFiledBean) var19.next();
                                    if (exportFiledBean.getListField() != null) {
                                        listTemp = (List) exportFiledBean.getField().get(t);
                                        break;
                                    }

                                    setCellValue(sb, exportFiledBean.getField().get(t), exportFiledBean.getField());
                                }
                            } while (listTemp == null);

                            Iterator var22 = listTemp.iterator();

                            while (var22.hasNext()) {
                                Object obj = var22.next();
                                var15 = exportFiledBean.getListField().iterator();

                                while (var15.hasNext()) {
                                    field = (Field) var15.next();
                                    setCellValue(sb, field.get(obj), field);
                                }
                            }
                        }
                    }
                }
            }

            sb.append("</table>");
            return sb;
        }
    }

    private static void setCellValue(StringBuffer sb, Object o, Field field) {
        sb.append("<td>");
        sb.append(o == null ? "" : String.valueOf(o));
        sb.append("</td>");
    }

    private static void setHeadCellValue(StringBuffer sb, ExcelAttribute attr) {
        sb.append("<th>");
        if (attr != null) {
            sb.append(attr.name());
        }

        sb.append("</th>");
    }


    public static List<Field> getFields(Class<?> clazz) {
        Field[] allFields = clazz.getDeclaredFields();
        List<Field> fields = new ArrayList();
        Field[] var3 = allFields;
        int var4 = allFields.length;

        for (int var5 = 0; var5 < var4; ++var5) {
            Field field = var3[var5];
            if (field.isAnnotationPresent(ExcelAttribute.class)) {
                fields.add(field);
            }
        }

        return fields;
    }

    public static String getSimpleCss() {
        return "<style type=\"text/css\">\r\n\r\n\thtml, body, div, span, object, iframe,\r\n\th1, h2, h3, h4, h5, h6, p, blockquote, pre,\r\n\tabbr, address, cite, code,\r\n\tdel, dfn, em, img, ins, kbd, q, samp,\r\n\tsmall, strong, sub, sup, var,\r\n\tb, i,\r\n\tdl, dt, dd, ol, ul, li,\r\n\tfieldset, form, label, legend,\r\n\ttable, caption, tbody, tfoot, thead, tr, th, td {\r\n\t\tmargin:0;\r\n\t\tpadding:0;\r\n\t\tborder:0;\r\n\t\toutline:0;\r\n\t\tfont-size:100%;\r\n\t\tvertical-align:baseline;\r\n\t\tbackground:transparent;\r\n\t}\r\n\t\r\n\tbody {\r\n\t\tmargin:0;\r\n\t\tpadding:0;\r\n\t\tfont:12px/15px \"Helvetica Neue\",Arial, Helvetica, sans-serif;\r\n\t\tcolor: #555;\r\n\t\tbackground:#f5f5f5 url(bg.jpg);\r\n\t}\r\n\t\r\n\ta {color:#666;}\r\n\t\r\n\t#content {width:65%; max-width:690px; margin:6% auto 0;}\r\n\t\r\n\t/*\r\n\tPretty Table Styling\r\n\tCSS Tricks also has a nice writeup: http://css-tricks.com/feature-table-design/\r\n\t*/\r\n\t\r\n\ttable {\r\n\t\toverflow:atuo;\r\n\t\tborder:1px solid #d3d3d3;\r\n\t\tbackground:#fefefe;\r\n\t\twidth:100%;\r\n\t\tmargin:5% auto 0;\r\n\t\t-moz-border-radius:5px; /* FF1+ */\r\n\t\t-webkit-border-radius:5px; /* Saf3-4 */\r\n\t\tborder-radius:5px;\r\n\t\t-moz-box-shadow: 0 0 4px rgba(0, 0, 0, 0.2);\r\n\t\t-webkit-box-shadow: 0 0 4px rgba(0, 0, 0, 0.2);\r\n\t}\r\n\t\r\n\tth, td {padding:8px 8px 8px; text-align:center; }\r\n\t\r\n\tth {padding-top:11px; text-shadow: 1px 1px 1px #fff; background:#e8eaeb;}\r\n\t\r\n\ttd {border-top:1px solid #e0e0e0; border-right:1px solid #e0e0e0;}\r\n\t\r\n\ttr.odd-row td {background:#f6f6f6;}\r\n\t\r\n\ttd.first, th.first {text-align:left}\r\n\t\r\n\ttd.last {border-right:none;}\r\n\t\r\n\t/*\r\n\tBackground gradients are completely unnecessary but a neat effect.\r\n\t*/\r\n\t\r\n\ttd {\r\n\t\tbackground: -moz-linear-gradient(100% 25% 90deg, #fefefe, #f9f9f9);\r\n\t\tbackground: -webkit-gradient(linear, 0% 0%, 0% 25%, from(#f9f9f9), to(#fefefe));\r\n\t}\r\n\t\r\n\ttr.odd-row td {\r\n\t\tbackground: -moz-linear-gradient(100% 25% 90deg, #f6f6f6, #f1f1f1);\r\n\t\tbackground: -webkit-gradient(linear, 0% 0%, 0% 25%, from(#f1f1f1), to(#f6f6f6));\r\n\t}\r\n\t\r\n\tth {\r\n\t\tbackground: -moz-linear-gradient(100% 20% 90deg, #e8eaeb, #ededed);\r\n\t\tbackground: -webkit-gradient(linear, 0% 0%, 0% 20%, from(#ededed), to(#e8eaeb));\r\n\t}\r\n\t\r\n\t/*\r\n\tI know this is annoying, but we need additional styling so webkit will recognize rounded corners on background elements.\r\n\tNice write up of this issue: http://www.onenaught.com/posts/266/css-inner-elements-breaking-border-radius\r\n\t\r\n\tAnd, since we've applied the background colors to td/th element because of IE, Gecko browsers also need it.\r\n\t*/\r\n\t\r\n\ttr:first-child th.first {\r\n\t\t-moz-border-radius-topleft:5px;\r\n\t\t-webkit-border-top-left-radius:5px; /* Saf3-4 */\r\n\t}\r\n\t\r\n\ttr:first-child th.last {\r\n\t\t-moz-border-radius-topright:5px;\r\n\t\t-webkit-border-top-right-radius:5px; /* Saf3-4 */\r\n\t}\r\n\t\r\n\ttr:last-child td.first {\r\n\t\t-moz-border-radius-bottomleft:5px;\r\n\t\t-webkit-border-bottom-left-radius:5px; /* Saf3-4 */\r\n\t}\r\n\t\r\n\ttr:last-child td.last {\r\n\t\t-moz-border-radius-bottomright:5px;\r\n\t\t-webkit-border-bottom-right-radius:5px; /* Saf3-4 */\r\n\t}\r\n\r\n</style>";
    }

    public static String getSimpleCssV2() {
        return "<style type=\"text/css\">html,body,div,span,object,iframe,h1,h2,h3,h4,h5,h6,p,blockquote,pre,abbr,address,cite,code,del,dfn,em,img,ins,kbd,q,samp,small,strong,sub,sup,var,b,i,dl,dt,dd,ol,ul,li,fieldset,form,label,legend,table,caption,tbody,tfoot,thead,tr,th,td{margin:0;padding:0;border:0;outline:0;font-size:100%;vertical-align:baseline;background:transparent;}a{color:#666;}#content{width:65%;max-width:690px;margin:6% auto 0;}table{overflow:atuo;border:1px solid #d3d3d3;background:#fefefe;width:90%;margin:5% auto 0;-moz-border-radius:5px;-webkit-border-radius:5px;border-radius:5px;-moz-box-shadow:0 0 4px rgba(0,0,0,0.2);-webkit-box-shadow:0 0 4px rgba(0,0,0,0.2);}tr.odd-row td{background:#f6f6f6;}td.first,th.first{text-align:left}td.last{border-right:none;}tr:first-child th.first{-moz-border-radius-topleft:5px;-webkit-border-top-left-radius:5px;}tr:first-child th.last{-moz-border-radius-topright:5px;-webkit-border-top-right-radius:5px;}tr:last-child td.first{-moz-border-radius-bottomleft:5px;-webkit-border-bottom-left-radius:5px;}tr:last-child td.last{-moz-border-radius-bottomright:5px;-webkit-border-bottom-right-radius:5px;}body{font-family:'trebuchet MS','Lucida sans',Arial;font-size:14px;color:#444;}table{*border-collapse:collapse;border-spacing:0;width:100%;}table{border:solid #ccc 1px;-moz-border-radius:6px;-webkit-border-radius:6px;border-radius:6px;-webkit-box-shadow:0 1px 1px #ccc;-moz-box-shadow:0 1px 1px #ccc;box-shadow:0 1px 1px #ccc;}table tr:hover{background:#fbf8e9;-o-transition:all 0.1s ease-in-out;-webkit-transition:all 0.1s ease-in-out;-moz-transition:all 0.1s ease-in-out;-ms-transition:all 0.1s ease-in-out;transition:all 0.1s ease-in-out;}table td,table th{border-left:1px solid #ccc;border-top:1px solid #ccc;}table td{padding:4px 7px 4px;text-align:center;}table th{}table thead th{width:60px;background-color:#dce9f9;background-image:-webkit-gradient(linear,left top,left bottom,from(#ebf3fc),to(#dce9f9));background-image:-webkit-linear-gradient(top,#ebf3fc,#dce9f9);background-image:-moz-linear-gradient(top,#ebf3fc,#dce9f9);background-image:-ms-linear-gradient(top,#ebf3fc,#dce9f9);background-image:-o-linear-gradient(top,#ebf3fc,#dce9f9);background-image:linear-gradient(top,#ebf3fc,#dce9f9);-webkit-box-shadow:0 1px 0 rgba(255,255,255,.8) inset;-moz-box-shadow:0 1px 0 rgba(255,255,255,.8) inset;box-shadow:0 1px 0 rgba(255,255,255,.8) inset;border-top:none;text-shadow:0 1px 0 rgba(255,255,255,.5);}table td:first-child,table th:first-child{border-left:none;}table th:first-child{-moz-border-radius:6px 0 0 0;-webkit-border-radius:6px 0 0 0;border-radius:6px 0 0 0;}table th:last-child{-moz-border-radius:0 6px 0 0;-webkit-border-radius:0 6px 0 0;border-radius:0 6px 0 0;}table th:only-child{-moz-border-radius:6px 6px 0 0;-webkit-border-radius:6px 6px 0 0;border-radius:6px 6px 0 0;}table tr:last-child td:first-child{-moz-border-radius:0 0 0 6px;-webkit-border-radius:0 0 0 6px;border-radius:0 0 0 6px;}table tr:last-child td:last-child{-moz-border-radius:0 0 6px 0;-webkit-border-radius:0 0 6px 0;border-radius:0 0 6px 0;}table td,th{border-bottom:1px solid #f2f2f2;}table tbody tr:nth-child(even){background:#f5f5f5;-webkit-box-shadow:0 1px 0 rgba(255,255,255,.8) inset;-moz-box-shadow:0 1px 0 rgba(255,255,255,.8) inset;box-shadow:0 1px 0 rgba(255,255,255,.8) inset;}table th{text-align:center;text-shadow:0 1px 0 rgba(255,255,255,.5);border-bottom:1px solid #ccc;background-color:#eee;background-image:-webkit-gradient(linear,left top,left bottom,from(#f5f5f5),to(#eee));background-image:-webkit-linear-gradient(top,#f5f5f5,#eee);background-image:-moz-linear-gradient(top,#f5f5f5,#eee);background-image:-ms-linear-gradient(top,#f5f5f5,#eee);background-image:-o-linear-gradient(top,#f5f5f5,#eee);background-image:linear-gradient(top,#f5f5f5,#eee);}table th:first-child{-moz-border-radius:6px 0 0 0;-webkit-border-radius:6px 0 0 0;border-radius:6px 0 0 0;}table th:last-child{-moz-border-radius:0 6px 0 0;-webkit-border-radius:0 6px 0 0;border-radius:0 6px 0 0;}table th:only-child{-moz-border-radius:6px 6px 0 0;-webkit-border-radius:6px 6px 0 0;border-radius:6px 6px 0 0;}table tfoot td{border-bottom:0;border-top:1px solid #fff;background-color:#f1f1f1;}table tfoot td:first-child{-moz-border-radius:0 0 0 6px;-webkit-border-radius:0 0 0 6px;border-radius:0 0 0 6px;}table tfoot td:last-child{-moz-border-radius:0 0 6px 0;-webkit-border-radius:0 0 6px 0;border-radius:0 0 6px 0;}table tfoot td:only-child{-moz-border-radius:0 0 6px 6px;-webkit-border-radius:0 0 6px 6px border-radius:0 0 6px 6px}</style>";
    }


}
