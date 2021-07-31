package com.adjs.gaoleng_plus.service;

import common.Response;

import java.util.Map;

public class BaseService {

    /** 定义返回 BEGIN **/
    public Response retCommonResponse(String code, String msg, Map<String, Object> obj) {
        return new Response(code, msg, obj);
    }

    public Response retCommonResponse(String code, String msg) {
        return new Response(code, msg);
    }

    public Response retSuccessResponse() {
        return new Response("000000", "操作成功");
    }

    public Response retSuccessResponse(Map<String, Object> obj) {
        return new Response("000000", "操作成功", obj);
    }

    public Response retFailedResponse() {
        return new Response("999999", "系统异常");
    }
    public Response retFailedResponse(String detail) {
        return new Response("999999", "系统异常", detail);
    }

    /** 定义返回 END **/

}
