package com.example.springaop;

import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Aspect
@Component
public class AopAspectImplementation {
	@Around(value = "@within(com.example.springaop.AopAspects) || @annotation(com.example.springaop.AopAspects)")
	public ResponseEntity<String> aroundAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
				.getRequest();
		HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
				.getResponse();

		StringBuilder test = new StringBuilder();
		
		
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();

		AopAspects aopmethod = method.getAnnotation(AopAspects.class);

		long startTime = System.currentTimeMillis();
		Object result = joinPoint.proceed();
		long endTime = System.currentTimeMillis();

		String methodName = method.getName();
		String serverName = request.getServerName();
		String requestType = request.getMethod();
		// String requestUrl= request.getRequestURL().toString();
		String requestUrl = getRequestUrl(request);
		int status = response.getStatus();
		String queryParams= request.getQueryString();
		
		/*
		 * ContentCachingResponseWrapper responseWrapper = new
		 * ContentCachingResponseWrapper(response); String test1= new
		 * String(responseWrapper.getContentAsByteArray()); System.out.println("content"
		 * +test1);
		 */
		if (aopmethod.allow()) {
			test.append("Target Method Name :" + methodName + "\n");
			test.append("Request Method Type :" + requestType + "\n");
			test.append("Server Name :" + serverName + "\n");
			test.append("Request Url :" + requestUrl + "\n");
			test.append("Status Code : " + status + "\n");
			test.append("Time Taken : " + (endTime - startTime) + " milliSeconds" + "\n");

			if (joinPoint.getArgs() != null && joinPoint.getArgs().length > 0) {
				for (int i = 0; i < joinPoint.getArgs().length; i++) {
					if (joinPoint.getArgs()[i] instanceof OptionHeader) {
						test.append("Request Body :" + getJsonStringWithISODate(joinPoint.getArgs()[i]));

					}

				}
			}
		}
		return new ResponseEntity<String>(test.toString(), HttpStatus.OK);

	}

	private String getJsonStringWithISODate(Object object) {
		ObjectMapper mapper = new ObjectMapper();
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSZ");
		format.setTimeZone(TimeZone.getTimeZone("GMT+5:30"));
		mapper.setDateFormat(format);
		
		String json = "";
		try {
			json = mapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
	}

	private String getRequestUrl(HttpServletRequest request) {
		UriComponentsBuilder builder = UriComponentsBuilder.newInstance().scheme(request.getScheme())
				.host(request.getServerName()).port(request.getServerPort()).path("json");
		return builder.toUriString();
	}

//	private String getRequestUrl(HttpServletRequest request) {
//		UriComponentsBuilder builer= UriComponentsBuilder.newInstance().scheme(request.getScheme()).host(request.getServerName())
//				.port(request.getServerPort())
//				.path(request.geta)
//		return null;
//	}

}
