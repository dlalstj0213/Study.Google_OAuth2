package com.google.web;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.google.app.GoogleService;
import com.google.dto.GoogleOAuthResponse;
import com.google.set.OAuthPrivateInfo;

import lombok.extern.log4j.Log4j;

@Log4j
@RequestMapping("/google_api/*")
@Controller
public class GoogleController {
	@Autowired
	GoogleService service;
	@Autowired
	HttpSession session;
	
	String clientId = OAuthPrivateInfo.getInstance().getId();
	String clientSecret = OAuthPrivateInfo.getInstance().getPw();
	
	final static String GOOGLE_REDIRECT_URL = "http://localhost:8080/google_api/google_token";
	final static String GOOGLE_AUTH_BASE_URL = "https://accounts.google.com/o/oauth2/v2/auth";
	final static String GOOGLE_TOKEN_BASE_URL = "https://oauth2.googleapis.com/token";
	final static String GOOGLE_REVOKE_TOKEN_BASE_URL = "https://oauth2.googleapis.com/revoke";
	
	@RequestMapping(value="/")
	public ModelAndView googleApiJSP() {
		ModelAndView mv = new ModelAndView("google/google_api");
		return mv;
	}
	
	/**
	 * Authentication Code를 전달 받는 엔드포인트
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@ResponseBody
	@RequestMapping(value="google_token")
	public ModelAndView googleToken(@RequestParam Map<String, Object> request) throws IOException {
		ModelAndView mv = new ModelAndView();
		String authCode = (String)request.get("code");
		log.info(">> api.client_id : " +clientId);
		log.info(">> api.client_secret : " +clientSecret);
		//HTTP Request를 위한 RestTemplate
		RestTemplate restTemplate = new RestTemplate();
		
		//Google OAuth Access Token 요청을 위한 파라미터 세팅
		/* HttpHandle 오류 존재
		GoogleOAuthRequest googleOAuthRequestParam = GoogleOAuthRequest
				.builder()
				.clientId(clientId)
				.clientSecret(clientSecret)
				.code(authCode)
				.redirectUri(GOOGLE_REDIRECT_URL)
				.grantType("authorization_code").build();
		*/
		
		//JSON 파싱을 위한 기본값 세팅
		//요청시 파라미터는 스네이크 케이스로 세팅되므로 Object mapper에 미리 설정해준다.
		ObjectMapper mapper = new ObjectMapper();
		mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
		mapper.setSerializationInclusion(Include.NON_NULL);
		
		/*** HttpHandle Exception 에러 해결 방법 ***/
		MultiValueMap<String, String> mvm = new LinkedMultiValueMap<String, String>();
		mvm.add("clientId", clientId);
		mvm.add("clientSecret", clientSecret);
		mvm.add("code", authCode);
		mvm.add("redirectUri", GOOGLE_REDIRECT_URL);
		mvm.add("grantType", "authorization_code");
		
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
		HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<MultiValueMap<String, String>>(mvm, requestHeaders);
		//AccessToken 발급 요청
		ResponseEntity<String> resultEntity = restTemplate.postForEntity(GOOGLE_TOKEN_BASE_URL, requestEntity, String.class);
		
		//Token Request
		GoogleOAuthResponse result = mapper.readValue(resultEntity.getBody(), new TypeReference<GoogleOAuthResponse>() {});
		
		log.info("getBody() : "+resultEntity.getBody());
		
		//ID Token만 추출 (사용자의 정보는 jwt로 인코딩 되어있다)
		String jwtToken = result.getIdToken();
		String requestUrl = UriComponentsBuilder.fromHttpUrl("https://oauth2.googleapis.com/tokeninfo")
				.queryParam("id_token", jwtToken).encode().toUriString();
	
		String resultJson = restTemplate.getForObject(requestUrl, String.class);
		Map<String,String> userInfo = mapper.readValue(resultJson, new TypeReference<Map<String, String>>(){});
		//mv.addAllObjects(userInfo);
		mv.addObject("token", result.getAccessToken());
		log.info(userInfo);
		mv.addObject("userInfo", userInfo);
		mv.setViewName("google/token_result");
		session.setAttribute("token", result.getAccessToken());
		return mv;
	}
	
	@ResponseBody
	@GetMapping(value="revoke_token")
	public ModelAndView revokeToken(@RequestParam Map<String, Object> request){
		Map<String, Object> result = new HashMap<String, Object>();
		RestTemplate restTemplate = new RestTemplate();
		final String requestUrl = UriComponentsBuilder.fromHttpUrl(GOOGLE_REVOKE_TOKEN_BASE_URL)
				.queryParam("token", String.valueOf(session.getAttribute("token"))).encode().toUriString();
		log.info("token : "+ String.valueOf(session.getAttribute("token")));
		
		String resultJson = restTemplate.postForObject(requestUrl, null, String.class);
		result.put("result", "success");
		result.put("resultJson", resultJson);
		/*
		 * ModelAndView mv = new ModelAndView("google/revoke_result");
		 * mv.addAllObjects(result);
		 */
		session.invalidate();
		return new ModelAndView("google/revoke_result", "result", result);
	}
	
	@GetMapping(value="session_remove")
	public ModelAndView removeSession() {
		log.info(String.valueOf(session.getAttribute("token")));
		
		session.invalidate();
		return new ModelAndView("redirect:../");
	}
}
