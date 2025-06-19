package vn.buivandat.TestApiDTS.util;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import jakarta.servlet.http.HttpServletResponse;
import vn.buivandat.TestApiDTS.domain.respone.ResResponse;
import vn.buivandat.TestApiDTS.util.annotation.ApiMessage;

@ControllerAdvice
public class FormatRestResponse implements ResponseBodyAdvice<Object> {
    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body,
        MethodParameter returnType, 
        MediaType arg2,
        Class arg3, 
        ServerHttpRequest arg4,
        ServerHttpResponse arg5) {
    HttpServletResponse servletResponse = ((ServletServerHttpResponse) arg5).getServletResponse();
    int status = servletResponse.getStatus();

    ResResponse<Object> res = new ResResponse<Object>();
        res.setStatusCode(status);

        //     if (body instanceof String || body instanceof Resource) {
        // return body;
        // }
            if (status >= 400) {
            // case error
            return body;

        } else {
            // case success
            res.setData(body);
            ApiMessage message = returnType.getMethodAnnotation(ApiMessage.class);
            res.setMessage(message != null ? message.value() : "Call API success");
        }

        return res;
    }

   
    
}
