package chat.onmap.chatservice.rest.handler;

import chat.onmap.chatservice.rest.BaseResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@ControllerAdvice
public class GlobalResponseHandler implements ResponseBodyAdvice {

	@Override
	public boolean supports(MethodParameter returnType, Class converterType) {
		return true;
	}

	@Override
	public Object beforeBodyWrite(Object body,
								  MethodParameter returnType,
								  MediaType selectedContentType,
								  Class selectedConverterType,
								  ServerHttpRequest request,
								  ServerHttpResponse response) {
		if (isFail((ServletServerHttpResponse) response)
				|| !AbstractJackson2HttpMessageConverter.class.isAssignableFrom(selectedConverterType)
				|| isAlreadyBaseResponse(body)
				|| !request.getURI().getPath().startsWith("/api/v1")) {
			return body;
		}
		return BaseResponse.success(body);
	}

	private boolean isFail(ServletServerHttpResponse response) {
		int status = response.getServletResponse().getStatus();
		return !(status == 0 || status == HttpStatus.OK.value());
	}

	private boolean isAlreadyBaseResponse(Object body) {
		return body != null && BaseResponse.class.isAssignableFrom(body.getClass());
	}
}
