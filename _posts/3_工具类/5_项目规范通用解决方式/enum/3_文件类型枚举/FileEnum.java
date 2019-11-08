package com.hlj.util.Z017_文件ContentType;

/**
 * @author HealerJean
 * @ClassName FileEnum
 * @date 2019/11/7  19:58.
 * @Description
 */
public interface FileEnum {

    /**
     * 文件类型枚举
     */
    enum FileContentTypeEnum {

        doc(".doc","application/msword"),
        docx(".docx","application/vnd.openxmlformats-officedocument.wordprocessingml.document"),
        xls(".xls","application/vnd.ms-excel"),
        xlsx(".xlsx","application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"),
        csv(".csv","text/csv"),
        ppt(".ppt","application/vnd.ms-powerpoint"),


        ttf(".ttf","font/ttf"),
        js(".js","text/javascript"),
        css(".css","text/css"),
        xml(".xml","text/xml"),
        html(".html","text/html"),
        htm(".htm","text/html"),
        json(".json","application/json"),
        xhtml(".xhtml","application/xhtml+xml"),

        jpeg(".jpeg","image/jpeg"),
        jpg(".jpg","image/jpeg"),
        png(".png","image/png"),
        gif(".gif","image/gif"),
        ico(".ico","image/vnd.microsoft.icon"),
        tif(".tif","image/tiff"),
        tiff(".tiff","image/tiff"),

        jar(".jar","application/java-archive"),
        zip(".zip","application/zip"),
        tar(".tar","application/x-tar"),
        sh(".sh","application/x-sh"),

        mp3(".mp3","audio/mpeg"),


        ;
        private String code;
        private String mime;

        FileContentTypeEnum(String code, String mime) {
            this.code = code;
            this.mime = mime;
        }

        public String getCode() {
            return code;
        }
        public void setCode(String code) {
            this.code = code;
        }
        public String getMime() {
            return mime;
        }
        public void setMime(String mime) {
            this.mime = mime;
        }

        public static FileContentTypeEnum toEnum(String code) {
            for (FileContentTypeEnum item : FileContentTypeEnum.values()) {
                if (item.getCode().equals(code)) {
                    return item;
                }
            }
            return null;
        }
    }

}
