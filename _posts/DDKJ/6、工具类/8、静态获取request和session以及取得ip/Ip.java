
    public static String getIp(){
        HttpServletRequest request = RequestHolder.getRequest();
        if(request==null)return null;
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if(ip.toLowerCase().contains("x-forwarded-for")){
            String ip_temp = "";
            if(ip.contains(":") && ip.split(":").length>0 && !ip.endsWith(":")){
                ip_temp = ip.split(":")[1];
                if(StringUtils.isBlank(ip_temp)) ip_temp = ip.substring(0,ip.lastIndexOf("X-Forwarded-For"));
            }else{
                ip_temp = ip.substring(0,ip.lastIndexOf("X-Forwarded-For"));
            }
            ip = ip_temp.replaceAll(" ","");
        }
        if (ip.indexOf(",") > -1) {
            String ip_temp = ip.split(",")[0];
            ip_temp = ip_temp.replaceAll(" ", "");
            if(ip_temp.startsWith("10.") && ip.split(",").length>1){
                ip = ip.split(",")[1];
                ip = ip.replaceAll(" ", "");
            }else ip = ip_temp;
        }
        return ip;
    }




    /**
     * 获取ip
     * @param request
     * @return
     */
    public static String getIp(HttpServletRequest request){
        if(request==null) {
            return null;
        }
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if(ip.toLowerCase().contains("x-forwarded-for")){
            String ip_temp = "";
            if(ip.contains(":") && ip.split(":").length>0 && !ip.endsWith(":")){
                ip_temp = ip.split(":")[1];
                if(ip_temp == null) {
                    ip_temp = ip.substring(0,ip.lastIndexOf("X-Forwarded-For"));
                }
            }else{
                ip_temp = ip.substring(0,ip.lastIndexOf("X-Forwarded-For"));
            }
            ip = ip_temp.replaceAll(" ","");
        }
        if (ip.indexOf(",") > -1) {
            String ip_temp = ip.split(",")[0];
            ip_temp = ip_temp.replaceAll(" ", "");
            if(ip_temp.startsWith("10.") && ip.split(",").length>1){
                ip = ip.split(",")[1];
                ip = ip.replaceAll(" ", "");
            }else {
                ip = ip_temp;
            }
        }
//        getIpDebug(request,ip);
        return ip;
    }

    