/**
 * 
 */
 	const onClickGoogleLogin = (e) => {
    	//구글 인증 서버로 인증코드 발급 요청
 		window.location.replace("https://accounts.google.com/o/oauth2/v2/auth?" 
 			+	"client_id="
 			+ "&redirect_uri=http://localhost:8080/google_api/google_token"
 			+ "&response_type=code"
 			+ "&scope=email%20profile%20openid"
 			+ "&access_type=offline")
 	}
	
	const googleLoginBtn = document.getElementById("googleLoginBtn");
	googleLoginBtn.addEventListener("click", onClickGoogleLogin);