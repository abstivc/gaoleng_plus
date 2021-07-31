package common;

import java.util.Map;

public class Response {

    String respCode;
    String respDesc;
    String detail;
    Map<String, Object> data;

    public Response(String respCode, String respDesc) {
        this.respCode = respCode;
        this.respDesc = respDesc;
    }

    public Response(String respCode, String respDesc, String detail) {
        this.respCode = respCode;
        this.respDesc = respDesc;
        this.detail = detail;
    }

    public Response(String respCode, String respDesc, String detail, Map<String, Object> data) {
        this.respCode = respCode;
        this.respDesc = respDesc;
        this.detail = detail;
        this.data = data;
    }

    public Response(String respCode, String respDesc, Map<String, Object> data) {
        this.respCode = respCode;
        this.respDesc = respDesc;
        this.data = data;
    }

    public String getRespCode() {
        return respCode;
    }

    public void setRespCode(String respCode) {
        this.respCode = respCode;
    }

    public String getRespDesc() {
        return respDesc;
    }

    public void setRespDesc(String respDesc) {
        this.respDesc = respDesc;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
