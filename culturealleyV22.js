var loginRedirectId = 0; 

//google analytics tracking
var _gaq = _gaq || [];
  _gaq.push(['_setAccount', 'UA-33859566-1']);
  _gaq.push(['_setDomainName', 'culturealley.com']);
  _gaq.push(['_trackPageview']);

  (function() {
    var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
    ga.src = ('https:' == document.location.protocol ? 'https://' : 'http://') + 'stats.g.doubleclick.net/dc.js';
    var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
})();
  
  $(function(){
	  
	  //header
	  if(typeof mixpanel != 'undefined'){
		  
	 mixpanel.track_links(".freeLessonHeader ul li a", "LessonInHeader Link", function(ele) { return { type: $(ele).attr('href')}});
	 mixpanel.track_links(".skypeLessonHeader a", "SkypeInHeader Link", function(ele) { return { type: $(ele).attr('href')}});
	 mixpanel.track_links(".askAQuestionDivHeaderLi ul li a", "ForumInHeader Link", function(ele) { return { type: $(ele).attr('href')}});
	 mixpanel.track_links(".dictionaryDivHeaderLi ul li a", "DictionaryInHeader Link", function(ele) { return { type: $(ele).attr('href')}});
	 mixpanel.track_links(".moreHeader li a", "FAQ/BLOG InHeader Link", function(ele) { return { type: $(ele).attr('href')}});
  
	 mixpanel.track_links(".freeLessonHeaderNavicon ul li a", "LessonInHeader Link", function(ele) { return { type: $(ele).attr('href')}});
	 mixpanel.track_links(".askAQuestionDivHeaderLiNavicon ul li a", "ForumInHeader Link", function(ele) { return { type: $(ele).attr('href')}});
	 mixpanel.track_links(".dictionaryDivHeaderLiNavicon ul li a", "DictionaryInHeader Link", function(ele) { return { type: $(ele).attr('href')}});
	 mixpanel.track_links(".naviconMenuDiv li a", "FAQ/BLOG InHeader Link", function(ele) { return { type: $(ele).attr('href')}});
  
	 //footer
	 mixpanel.track_links(".appInFooter a", "App Link", function(ele) { return { type: $(ele).attr('href')}});
	 
	 mixpanel.track_links(".CultureAlleyInFooterForAnalytics li a", "CultureAlleyInFooter Link", function(ele) { return { type: $(ele).attr('href')}});
	 mixpanel.track_links(".SkypeLessonInFooterForAnalytics li a", "SkypeLessonInFooter Link", function(ele) { return { type: $(ele).attr('href')}});
	 mixpanel.track_links(".ForumInFooterForAnalytics li a", "ForumInFooter Link", function(ele) { return { type: $(ele).attr('href')}});
	 mixpanel.track_links(".FreeLessonsInFooterForAnalytics li a", "FreeLessonsInFooter Link", function(ele) { return { type: $(ele).attr('href')}});
	 mixpanel.track_links(".AboutUsInFooterForAnalytics li a", "AboutUsInFooter Link", function(ele) { return { type: $(ele).attr('href')}});
	 mixpanel.track_links(".SpreadTheLoveInFooterForAnalytics li a", "SpreadTheLoveInFooter Link", function(ele) { return { type: $(ele).attr('href')}});
	 mixpanel.track_links(".DictionaryInFooterForAnalytics li a", "DictionaryInFooter Link", function(ele) { return { type: $(ele).attr('href')}});

	}
});
	  



$(function(){
	$("#closeNewAppNotification").click(function(){
		$(".newAppNotification").remove();
	});
	$(".opentwitterAppStoreLink").click(function(){
		
		window.open("//itunes.apple.com/us/app/language-practice/id764149495?ls=1&mt=8","_blank");
	});
});

$(function() {
	var homeinitfunc = $("#homeinitfunc").val();
	var initfuncparams = $("#initfuncparams").val();
	if (homeinitfunc != null) {
		if (homeinitfunc == "openPopUp") {

			var isUserFirstTimeVisit = getCookie("userFirstVisit");
			
			if(isUserFirstTimeVisit == "" || isUserFirstTimeVisit == null)
			{
				openPopUp("iosAppNotificattion.jsp");
				storeCookie("userFirstVisit","1",365);
			}
			else if(initfuncparams != "iosAppNotificattion.jsp")
			{
				openPopUp(initfuncparams)
			}
				
			
		} else {
			if (homeinitfunc == "showVerifiedPage") {
				showVerifiedPage(initfuncparams)
			}
		}
	}
	
	//Show Twitter banner per session for Mobile
	if( /Android|webOS|iPhone|iPod|iPad|BlackBerry|IEMobile|Opera Mini/i.test(navigator.userAgent) ) {
		var isFirstTime = getCookie("twitterBannerCookie");
		if (isFirstTime!=null && isFirstTime!="" ){
			$(".newAppNotification").css("display","none");
		}else{
			newCookie("twitterBannerCookie","1",1);
		}
	}
	
});
function doSearch(loginFlag) {
	if (val.trim() != "") {
		if (loginFlag) {
			window.location.replace("bookingstep1.action?search=" + val.trim())
		} else {
			openPopUp("Signup/signup.jsp?type=student&search=" + val.trim())
		}
	} else {
		document.getElementById("search").value = "";
		document.getElementById("search").focus()
	}
}
function setRedirectPage(id) {
	loginRedirectId = id
}
$(document).keydown(function(e) {
	if (e.keyCode == 27) {
		$("#popupwin").css("visibility", "hidden");
		$(".friend-notification").hide()
	}
});
function openPopUp(url) {
	
	$("#popupwin").css("width", "550px");
	$("#popupwin").css("margin-left", "-275px");
	$("#popupwin").css("top", "40%");
	var urlData = {
		loginurl : encodeURIComponent(window.location)
	};
	if (url.indexOf("Signup/signup.jsp?") != -1) {
		url = url + "&" + $.param(urlData)
	} else {
		if (url.indexOf("Signup/signup.jsp") != -1) {
			url = url + "?" + $.param(urlData)
		}
	}
	$("#loadPage").load(url);
	$("#popupwin").css("visibility", "visible")
}

function openPopUpCustom(url,width,height) {
	
	$("#popupwin").css("width", width);
	$("#popupwin").css("height", height);
	$("#popupwin").css("min-height", height);
	$("#popupwin").css("margin-left", -width/2);
	$("#popupwin").css("top", "40%");
	var urlData = {
		loginurl : encodeURIComponent(window.location)
	};
	if (url.indexOf("Signup/signup.jsp?") != -1) {
		url = url + "&" + $.param(urlData)
	} else {
		if (url.indexOf("Signup/signup.jsp") != -1) {
			url = url + "?" + $.param(urlData)
		}
	}
	$("#loadPage").load(url);
	$("#popupwin").css("visibility", "visible")
}

function closePopUp() {
	$("#loadingDiv").css("visibility", "hidden");
	$("#popupwin").css("visibility", "hidden");
	$("#popupwin").css("min-height", "300px");
	$("#loadPage").html("")
}
function sendFormData(type) {
	
	if (checkMail("email1",type)) {
		var email = $("#email1").val();
		var language = $("#language").val();
		if (language != 0) {
			
			if(type != "tutor")
			{
				hideEleCommon("languageerror");
				hideEleCommon("error");
			}
			$("#loadingDiv").css("visibility", "visible");
			$.ajax({
				url : "CASignupServlet?email=" + email + "&req=chksignup",
				success : function(data) {
					if (data == "1") {
						$("#error").html("Email id already registered.");
						showEleCommon("error");
						showEleCommon("emailerror");
						
						if(type == "tutor")
							_gaq.push(['_trackEvent', 'Teacher_Email_signUp ', ' Teacher_got_an_error_after_JetSetGo', 'Emails already exists']);
						else
							_gaq.push(['_trackEvent', 'Student_Email_signUp', 'Got_an_error_on_after_first_step', 'Emails already exists']);
						
						
					}
					else if(data == "2" || data=="3")
					{
						$("#error").html("Email id already registered");
						showEleCommon("error");
						showEleCommon("emailerror");
						_gaq.push(['_trackEvent', 'General_Email_signUp', 'SignUp_failed', 'Emails already exists thorugh facebook']);
						
					}
					
					else {
						closePopUp();
						openPopUp("Signup/password.jsp?type=" + type
								+ "&email=" + email + "&language=" + language);
					}
					$("#loadingDiv").css("visibility", "hidden");
				}
			})
		} else {
			$("#error").html("Please select language");
			showEleCommon("error");
			showEleCommon("languageerror");
			document.getElementById("language").focus()
		}
	}
}
function loadNewPage(url) {
	closePopUp();
	openPopUp(url)
}
function submitFormData() {
	
	if(type == "tutor")
		_gaq.push(['_trackEvent', 'Teacher_Email_signUp ', ' Teacher_clicked_on_second_step_Go', '']);
	else
		_gaq.push(['_trackEvent', 'Student_Email_signUp', 'Clicked_on_second_step_Go', '']);
	
	
	var pass1 = $("#password").val();
	var pass2 = $("#repassword").val();
	if (pass1 == "") {
		document.getElementById("password").focus();
		showEleCommon("passworderror");
		hideEleCommon("repassworderror");
		$("#error").html("Please enter password");
		showEleCommon("error")
		
		if(type == "tutor")
			_gaq.push(['_trackEvent', 'Teacher_Email_signUp ', ' Teacher_got_an_error_on_second_step_after_Go', 'enter password']);
		else
			_gaq.push(['_trackEvent', 'Student_Email_signUp', 'Got_an_error_on_second_step_after_Go', 'enter password']);
		
		
	} else {
		if (pass2 == "") {
			hideEleCommon("passworderror");
			document.getElementById("repassword").focus();
			showEleCommon("repassworderror");
			$("#error").html("Please re-enter password");
			showEleCommon("error")
		} else {
			if (pass1 != pass2) {
				hideEleCommon("repassworderror");
				hideEleCommon("passworderror");
				$("#error").html("Passwords do not match");
				showEleCommon("error")
				
				if(type == "tutor")
					_gaq.push(['_trackEvent', 'Teacher_Email_signUp ', ' Teacher_got_an_error_on_second_step_after_Go', 'Passwords do not match']);
				else
					_gaq.push(['_trackEvent', 'Student_Email_signUp', 'Got_an_error_on_second_step_after_Go', 'Passwords do not match']);
				
				
			} else {
				hideEleCommon("repassworderror");
				hideEleCommon("passworderror");
				hideEleCommon("error");
				var form = $("#sampleForm");
				var url = form.attr("action");
				var email = $("#email").val();
				var language = $("#language").val();
				var type = $("#type").val();
				$("#passwordButton").css("background-color", "#C0C0C0");
				$("#passwordButton").attr("disabled", "disabled");
				$("#loadingDiv").css("visibility", "visible");
				var date = new Date();
				var MILISEC_PRE_HOUR = 60 * 60 * 1000;
				var offset = (-(date.getTimezoneOffset() / 60) * MILISEC_PRE_HOUR);
				var tz = jstz.determine();
				var timezone = tz.name();
				$.post(url, {
					email : email,
					language : language,
					type : type,
					password : pass1,
					timeZoneOffset : offset,
					timezone : timezone
				}, function(doc) {
					
					if(type == "tutor")
						_gaq.push(['_trackEvent', 'Teacher_Email_signUp ', ' Teacher_signUp_successful', '']);
					else
						_gaq.push(['_trackEvent', 'Student_Email_signUp', 'SignUp_successful', '']);
					
					
					$("#loadPage").html(doc);
					$("#loadingDiv").css("visibility", "hidden")
				})
			}
		}
	}
}
function checkMail(objId,type) {
	var emailfilter = /^\w+[\+\.\w-]*@([\w-]+\.)*\w+[\w-]*\.([a-z]{2,4}|\d+)$/i;
	var e = document.getElementById(objId).value;
	var returnval = emailfilter.test(e);
	if (returnval == false) {
		showEleCommon("emailerror");
		if (e == null || e.trim() == "") {
			$("#error").html("Please enter email address")
		} else {
			$("#error").html("Invalid email address")
		}
		showEleCommon("error");
		document.getElementById(objId).value = "";
		document.getElementById(objId).focus()
	} else {
		
		if(type != "tutor")
		{
			hideEleCommon("emailerror");
			hideEleCommon("error")
		}
	}
	return returnval
}
function showVerifiedPage(success, studentId, tutorId, userId) {
	$("#popupwin").css("visibility", "visible");
	$("#loadingDiv").css("visibility", "visible");
	$("#loadPage").load("CultureAlley/accountVerified.jsp", {
		success : success,
		studentId : studentId,
		tutorId : tutorId,
		userId : userId
	});
	$("#loadingDiv").css("visibility", "hidden")
}
function saveAvailability() {
	var weekdays = $("#weekdays").val();
	var timeslot = $("#timeslot").val();
	var timezone = $("#timezone").val();
	var userId = $("#userId").val();
	var studentId = $("#studentId").val();
	var tutorId = $("#tutorId").val();
	if (weekdays == null) {
		showEleCommon("dayserror");
		hideEleCommon("timesloterror");
		hideEleCommon("timezoneerror");
		$("#error").html("Please select week days");
		showEleCommon("error")
	} else {
		if (timeslot == null) {
			showEleCommon("timesloterror");
			hideEleCommon("dayserror");
			hideEleCommon("timezoneerror");
			$("#error").html("Please select time slot");
			showEleCommon("error")
		} else {
			if (timezone == "") {
				hideEleCommon("timesloterror");
				hideEleCommon("dayserror");
				showEleCommon("timezoneerror");
				$("#error").html("Please select time zone");
				showEleCommon("error")
			} else {
				hideEleCommon("dayserror");
				hideEleCommon("timezoneerror");
				hideEleCommon("timesloterror");
				hideEleCommon("error");
				var week = weekdays.toString();
				var time = timeslot.toString();
				$("#availabilityBtn").css("background-color", "#C0C0C0");
				$("#availabilityBtn").attr("disabled", "disabled");
				$("#loadingDiv").css("visibility", "visible");
				$.post("saveUserAvailability.action", {
					userId : userId,
					studentId : studentId,
					tutorId : tutorId,
					weekdays : week,
					timeslot : time,
					timezone : timezone
				}, function(doc) {
					$("#loadPage").html(doc);
					$("#loadingDiv").css("visibility", "hidden")
				})
			}
		}
	}
}
var timer;
function showDiv(divId) {
// $("#popupwin").css("visibility", "hidden");
	var urlData = {
		loginurl : encodeURIComponent(window.location)
	};
	$("#loadPage").load("Signup/signup.jsp?type=student&" + $.param(urlData));
	loginRedirectId = 0;
	$("#searchlink").mousemove(function(e) {
		var pos = $("#searchlink").offset();
		$("#subcontent").css("left", pos.left + "px")
	});
	$("#" + divId).show("slow");
	timer = setInterval(function() {
		loginPopupPos()
	}, 500)
}
function loginPopupPos() {
	var remMe = document.getElementById("remember").checked;
	$("#wrapper")
			.mousemove(
					function(e) {
						var pos = $("#subcontent").offset();
						if (e.pageX < pos.left - 10 || e.pageY < pos.top - 15
								|| e.pageY > pos.top + 260
								|| e.pageX > pos.left + 190) {
							$("#subcontent").hide();
							$("#errorEmail").css("display", "none");
							$("#errorPassword").css("display", "none");
							if (remMe == false) {
								$(".input_02").val("")
							}
						}
					});
	clearInterval(timer)
}
function showEleCommon(eleId) {
	document.getElementById(eleId).style.display = "block"
}
function hideEleCommon(eleId) {
	document.getElementById(eleId).style.display = "none"
}
function doLogin() {
	
	//facebook tracking
	_gaq.push(['_trackEvent', 'Login', 'Email', 'clicked']);

	
	toMem();
	clearInterval(timer);
	var email = $("#email").val();
	var password = $("#password").val();
	var date = new Date();
	var MILISEC_PRE_HOUR = 60 * 60 * 1000;
	var offset = (-(date.getTimezoneOffset() / 60) * MILISEC_PRE_HOUR);
	var emailfilter = /^\w+[\+\.\w-]*@([\w-]+\.)*\w+[\w-]*\.([a-z]{2,4}|\d+)$/i;
	var flag = false;
	var returnval = emailfilter.test(email);
	if (returnval == false) {
		$("#errorEmail").css("display", "inline");
		flag = true
	} else {
		$("#errorEmail").css("display", "none")
	}
	if (password.length == 0) {
		$("#errorPassword").css("display", "inline");
		flag = true
	} else {
		$("#errorPassword").css("display", "none")
	}
	if (flag) {
		return
	}
	var tz = jstz.determine();
	var timezone = tz.name();
	var url = "dologin.action";
	$("#loginButton").attr("disabled", "disabled");
	$("#loginButton").css("background-color", "#C0C0C0");
	$.post(url, {
		userName : email,
		password : password,
		timeZoneOffset : offset,
		timezone : timezone
	}, function(doc) {
		parseLoginResult(doc)
	})
}
function showMoreComments() {
	showEleCommon("moreCmntDiv");
	hideEleCommon("moreCmntLink")
}
function hideMoreComments() {
	hideEleCommon("moreCmntDiv");
	showEleCommon("moreCmntLink")
}
function changePasswordReq() {
	var email = $("#emailid").val();
	var emailfilter = /^\w+[\+\.\w-]*@([\w-]+\.)*\w+[\w-]*\.([a-z]{2,4}|\d+)$/i;
	var flag = false;
	var returnval = emailfilter.test(email);
	if (returnval == false) {
		$("#errorEmailId").css("display", "inline");
		flag = true
	} else {
		$("#errorEmailId").css("display", "none")
	}
	if (flag) {
		return
	}
	var url = "changepassowdReq.action";
	$("#changePasswordButton").attr("disabled", "disabled");
	$("#changePasswordButton").css("background-color", "#C0C0C0");
	$("#loadingDiv").css("visibility", "visible");
	$.post(url, {
		email : email
	}, function(doc) {
		$("#loadPage").html(doc);
		setTimeout(function() {
			closePopUp()
		}, 3000);
		$("#loadingDiv").css("visibility", "hidden")
	})
}
function profileClosePopUp() {
	$("#profileLoadingDiv").css("visibility", "hidden");
	$("#profilepopupwin").css("visibility", "hidden")
}
function openProfilePopUp(url) {
	$("#profileLoadPage").load(url);
	$("#profilepopupwin").css("visibility", "visible")
}
function changeDivs(div1, div2) {
	hideEleCommon(div1);
	showEleCommon(div2)
}
function doDirectLogin(email) {
	
	//facebook tracking
	_gaq.push(['_trackEvent', 'Login', 'Email', 'clicked']);

	
	closePopUp();
	var url = "dodirectlogin.action";
	$.post(url, {
		userName : email
	}, function(doc) {
		parseLoginResult(doc)
	})
}
var timer1;
function showDivSubject(divId){
	// $("#popupwin").css("visibility","hidden");
	$("#subjectlink").mousemove(function(e)
	{
		var pos=$("#subjectlink").offset();
		var pos1=pos.left;$("#"+divId).css("left",pos1+"px")});
		var height=$("#"+divId).css("height");
		$("#"+divId).show("slow");
		timer1=setInterval(function(){loginSubjectPopupPos(divId,height)},500)}

function loginSubjectPopupPos(divId,height)
{
	$("#wrapper").mousemove(function(e)
	{
		var pos=$("#"+divId).offset();
		if(e.pageX<pos.left||e.pageY<pos.top-20||e.pageY>pos.top+10+height||e.pageX>pos.left+180)
		{
			$("#"+divId).hide()}});clearInterval(timer1)
}
var timer2;

function showOtherLanguages(){
				$("#popupwin").css("visibility","hidden");
				$("#otherlink").mousemove(function(e){var pos=$("#otherlink").offset();
				var pos1=pos.left;$("#otherlangs").css("left",pos1+"px")});
				$("#otherlangs").show("slow");
				var height=$("#otherlangs").css("height");
				timer2=setInterval(function(){
					otherLanguagesPopupPos(height)},500)
}

function otherLanguagesPopupPos(height){
		$("#wrapper").mousemove(function(e){
			var pos=$("#otherlangs").offset();
			if(e.pageX<pos.left||e.pageY<pos.top-20||e.pageY>pos.top+10+height||e.pageX>pos.left+90)
			{
				$("#otherlangs").hide()
			}
		});
		clearInterval(timer1)
}

function showArrow(id)
{
	$("#"+id).find("img").show();
	$("#"+id).find("span").hide()
}

function hideArrow(id)
{
	$("#"+id).find("img").hide();
	$("#"+id).find("span").show()
}

function getCookie(c_name)
{
	var c_value = document.cookie;
	var c_start = c_value.indexOf(" " + c_name + "=");
	if (c_start == -1)
	  {
	  c_start = c_value.indexOf(c_name + "=");
	  }
	if (c_start == -1)
	  {
	  c_value = null;
	  }
	else
	  {
	  c_start = c_value.indexOf("=", c_start) + 1;
	  var c_end = c_value.indexOf(";", c_start);
	  if (c_end == -1)
	  {
	c_end = c_value.length;
	}
	c_value = unescape(c_value.substring(c_start,c_end));
	}
	return c_value;
}

function parseLoginResult(doc)
{
		

	var userType=$(doc).find("userType").text();
	var accStatus=$(doc).find("accStatus").text();
	var errorType=$(doc).find("errorType").text();
	var course=$(doc).find("course").text();
	var step=$(doc).find("step").text();
	var clLink=$(doc).find("clLink").text();
	var fbLogin=$(doc).find("FBLogin").text();
	var fbSignUp=$(doc).find("FBSignUp").text();
	
	var lessonUrl=getCookie("lesson_url");
	
	if (lessonUrl!=null && lessonUrl!="" )
	{
	  	loginRedirectId =10;
	}	
	
	
	//fbLogin = -1;
	//fbSignUp = 1;
	
	
	if(accStatus=="1")
	{
		//google analytics tracking
		_gaq.push(['_trackEvent', 'Login', 'Email', 'Failed-account not verfied']);

		
		openPopUp("loginerrors.jsp?code=3");
		$("#loginButton").removeAttr("disabled");
		$("#loginButton").css("background-color","#BD1550");
		setTimeout(function(){
			closePopUp()
			},3000)}
	else{if(userType=="1"){
		if(loginRedirectId==1)
		{
			window.location.replace("growthstation.action")
		}
		
		else{
			window.location.replace("adminhome.action")
		}
	}
	else
	{
		if(userType=="2")
		{
			var wlstatus=$(doc).find("wlStatus").text();
			if(wlstatus!=""){
				alert(wlstatus)
			}
			
			if(step=="1")
			{
				var code=$(doc).find("code").text();
				window.location.replace("verifyaccountCA.action?code="+code)
			}
			
			else{if(step=="2"){
				window.location.replace("signuptutor2.action")
			}
			else{
				if(step=="3"){
					var ft=$(doc).find("ft").text();
					
					if(fbLogin == 0)
					{
						
						_gaq.push(['_trackEvent', 'Tutor_Facebook_Login', 'successful','']);
					}
					if(fbSignUp == 0)
					{
					
						
						_gaq.push(['_trackEvent', 'Tutor_Facebook_SignUp', 'successful','']);
					}

					
					if(loginRedirectId==1)
					{
						window.location.replace("growthstation.action")
					}
					else
					{
						if(ft=="1"){
							var fturl=$(doc).find("fturl").text();
							window.location=fturl;
							
						}
						else{
							if(ft=="2"){
								window.location.href=window.location.href
							}
							else{
								if(wlstatus!="")
								{
									window.location.href=nextLessonURL
								}
								else
								{
									if(clLink=="")
									{
										window.location.replace("home.action")
									}
									else{
										if(typeof mixpanel != 'undefined'){
											if(fbLogin==0 || fbSignUp==0){
												
											}else{
											mixpanel.track("LogIn clicked Successful", {"Method": "Email",version:"1.0"}, function() {});
											var courseValue="";
											if(course==3){courseValue="Spanish"}else if(course==4){courseValue="Mandarin"}else if(course==2){courseValue="Punjabi"}
											else if(course==6){courseValue="Hindi"}else if(course==5){courseValue="English"}else if(course==7){courseValue="Korean"}
											else if(course==8){courseValue="Japanese"}else if(course==14){courseValue="Italian"}else if(course==15){courseValue="French"}
											 mixpanel.register({
											        isteacher: "1" ,
											        Subject: courseValue,
											        LastLessonSeen: '',
											        FirstName:'',
											        LastName:'',
											        FBID:'',
											        EmailID:$("#username").val(),
											        version:"1.0"
											    });
											 
												mixpanel.identify($("#username").val());
												 mixpanel.people.set({
													    "$email": $("#username").val(),
												        version:"1.0"
													});
										  }
										}
										if(fbLogin==0 || fbSignUp==0){
											var courseValue="";
											var fbemailId=$(doc).find("emailId").text();
											var fbfirstName=$(doc).find("firstName").text();
											var fblastName=$(doc).find("lastName").text();
											course=$(doc).find("course").text();
											
											if(course==3){courseValue="Spanish"}else if(course==4){courseValue="Mandarin"}else if(course==2){courseValue="Punjabi"}
											else if(course==6){courseValue="Hindi"}else if(course==5){courseValue="English"}else if(course==7){courseValue="Korean"}
											else if(course==8){courseValue="Japanese"}else if(course==14){courseValue="Italian"}else if(course==15){courseValue="French"}
										


											if(fbLogin==0){
												
												setTimeout(function(){
													if(clLink.indexOf("NewCultureAlley")!= -1)
														window.location.replace("NewCultureAlley/userDashboard.jsp");
													else
														window.location.replace("success.jsp?method=Facebook&&email="+$(doc).find("emailId").text()+"&&type=old&&login_signup=login&&fbfirstName="+fbfirstName+"&&fblastName="+fblastName+"&&courseValue="+courseValue);
													//window.location.replace(clLink)
												},500);
											}else if(fbSignUp==0){
												setTimeout(function(){
													if(clLink.indexOf("NewCultureAlley")!= -1)
														window.location.replace("NewCultureAlley/userDashboard.jsp");
													else
														window.location.replace("success.jsp?method=Facebook&&email="+$(doc).find("emailId").text()+"&&type=new&&login_signup=signup&&fbfirstName="+fbfirstName+"&&fblastName="+fblastName+"&&courseValue="+courseValue);
													//window.location.replace(clLink)
												},500);
											}
										}else{
												setTimeout(function(){
													window.location.replace("success.jsp?method=Email&&email="+$("#username").val()+"&&type=old&&login_signup=login");
													//window.location.replace(clLink)
												},500);
											}
										}
									}
								}
							}
						}
					}
				else{
					if(errorType=="4")
					{
							
						openPopUp("loginerrors.jsp?code=10&course="+course);
						$("#loginButton").removeAttr("disabled");
						$("#loginButton").css("background-color","#BD1550")
					}
					else{
						if(errorType=="3")
						{
							//google analytics tracking		
							_gaq.push(['_trackEvent', 'Login', 'Email', 'Failed-blocked by admin']);

							
							openPopUp("loginerrors.jsp?code=11");
							$("#loginButton").removeAttr("disabled");
							$("#loginButton").css("background-color","#BD1550")
						}
					}
				}
			}
		}
	}
		else{if(userType=="3")
		{
			var wlstatus=$(doc).find("wlStatus").text();
			if(wlstatus!=""){
				alert(wlstatus)
			}
			if(step=="1")
			{
				var code=$(doc).find("code").text();
				window.location.replace("verifyaccountCA.action?code="+code)
			}
			
			else{if(step=="3")
			{
				var ft=$(doc).find("ft").text();
				if(fbLogin == 1)
				{
					_gaq.push(['_trackEvent', 'Student_Facebook_SignUp/Login', 'Login successful','']);
					
				}
				
				if(fbSignUp == 1)
				{

					_gaq.push(['_trackEvent', 'Student_Facebook_SignUp/Login', 'SignUp successful','']);
					
				}
				
				
				if(loginRedirectId==1)
				{
					window.location.replace("growthstation.action")
				}else{
						if(ft=="1")
						{
							var fturl=$(doc).find("fturl").text();
							window.location=fturl;
							
						}
						else{
							if(ft=="2")
								{
									window.location.href=window.location.href
								}
							 else{
								 if(wlstatus!="")
									{
										window.location.href=nextLessonURL
									}
								else{
									if(clLink==""){
									window.location.replace("home.action")
								}
					
					else{
						if(typeof mixpanel != 'undefined'){
							if(fbLogin==1 || fbSignUp==1){
								
							}else{
							mixpanel.track("LogIn clicked Successful", {"Method": "Email",version:"1.0"}, function() {});
							var courseValue="";
							if(course==3){courseValue="Spanish"}else if(course==4){courseValue="Mandarin"}else if(course==2){courseValue="Punjabi"}
							else if(course==6){courseValue="Hindi"}else if(course==5){courseValue="English"}else if(course==7){courseValue="Korean"}
							else if(course==8){courseValue="Japanese"}else if(course==14){courseValue="Italian"}else if(course==15){courseValue="French"}
							 mixpanel.register({
							        isteacher: "0" ,
							        Subject: courseValue,
							        LastLessonSeen: '',
							        FirstName:'',
							        LastName:'',
							        FBID:'',
							        EmailID:$("#username").val(),
							        version:"1.0"
							    });
							 
								mixpanel.identify($("#username").val());
								 mixpanel.people.set({
									    "$email": $("#username").val(),
								        version:"1.0"
									});
						  }
						}
						if(fbLogin==1 || fbSignUp==1){
							var courseValue="";
							var fbemailId=$(doc).find("emailId").text();
							var fbfirstName=$(doc).find("firstName").text();
							var fblastName=$(doc).find("lastName").text();
							
							course=$(doc).find("course").text();
							if(course==3){courseValue="Spanish"}else if(course==4){courseValue="Mandarin"}else if(course==2){courseValue="Punjabi"}
							else if(course==6){courseValue="Hindi"}else if(course==5){courseValue="English"}else if(course==7){courseValue="Korean"}
							else if(course==8){courseValue="Japanese"}else if(course==14){courseValue="Italian"}else if(course==15){courseValue="French"}
						
							if(fbLogin==1){
								setTimeout(function(){
									if(clLink.indexOf("NewCultureAlley")!= -1)
										window.location.replace("NewCultureAlley/userDashboard.jsp");
									else
										window.location.replace("success.jsp?method=Facebook&&email="+fbemailId+"&&type=old&&login_signup=login&&fbfirstName="+fbfirstName+"&&fblastName="+fblastName+"&&courseValue="+courseValue);
									//window.location.replace(clLink)
								},500);
							}else if(fbSignUp==1){
								setTimeout(function(){
									if(clLink.indexOf("NewCultureAlley")!= -1)
										window.location.replace("NewCultureAlley/userDashboard.jsp");
									else
										window.location.replace("success.jsp?method=Facebook&&email="+fbemailId+"&&type=new&&login_signup=signup&&fbfirstName="+fbfirstName+"&&fblastName="+fblastName+"&&courseValue="+courseValue);
									//window.location.replace(clLink)
								},500);
							}
						}else{
								setTimeout(function(){
									window.location.replace("success.jsp?method=Email&&email="+$("#username").val()+"&&type=old&&login_signup=login");
									//window.location.replace(clLink)
								},500);
							}				
					}
				}
			}
		}
					}
				}
			else{if(step=="2")
			{
				window.location.replace("signupstudent2.action")
			}
			else{
				if(errorType=="4"){
				openPopUp("loginerrors.jsp?code=10&course="+course);
				$("#loginButton").removeAttr("disabled");
				$("#loginButton").css("background-color","#BD1550")
				}
				else{
					if(errorType=="3")
					{
						openPopUp("loginerrors.jsp?code=11");
						$("#loginButton").removeAttr("disabled");
						$("#loginButton").css("background-color","#BD1550")
						}
					}
				}
			}
			}
			}
		else{if(errorType=="0")
		{
		
			_gaq.push(['_trackEvent', 'Login', 'Email', 'Failed-retry']);
			
			openPopUp("loginerrors.jsp?code=0");
			setTimeout(function(){closePopUp()},4000)
			if(typeof mixpanel != 'undefined'){
				mixpanel.track("LogIn clicked UnSuccessful", {"Method": "Email","Error":"Server Error",version:"1.0"}, function() {});
			}
		}
		else
		{
			if(errorType=="1")
			{

				_gaq.push(['_trackEvent', 'Login', 'Email', 'Failed-username-password dont match']);
				
				openPopUp("loginerrors.jsp?code=1");
				setTimeout(function(){closePopUp()},4000)
				if(typeof mixpanel != 'undefined'){
					mixpanel.track("LogIn clicked UnSuccessful", {"Method": "Email","Error":"Username Password Not Matched",version:"1.0"}, function() {});
				}
			}
			
			else if(errorType=="10")
			{
				_gaq.push(['_trackEvent', 'FacebookID_exists_email_registered', 'failed to login', 'user signup with facebook(Unable to login with email)']);
				
				openPopUp("loginerrors.jsp?code=1");
				setTimeout(function(){closePopUp()},4000);
				if(typeof mixpanel != 'undefined'){
					mixpanel.track("LogIn clicked UnSuccessful", {"Method": "Email","Error":"Username Signup With Facebook",version:"1.0"}, function() {});
				}
			}
			else
			{
				if(errorType=="2")
				{

					_gaq.push(['_trackEvent', 'Login', 'Email', 'Failed-new user']);
					openPopUp("Signup/signup.jsp?type=student&code=2");
					setTimeout(function(){$("#errormsg").css("display","none")},4000);
					if(typeof mixpanel != 'undefined'){
						mixpanel.track("LogIn clicked UnSuccessful", {"Method": "Email","Error":"Username Not Exists(New User)",version:"1.0"}, function() {});
					}
				}
			}
		}
		$("#loginButton").removeAttr("disabled");
		$("#loginButton").css("background-color","#BD1550")
		}
		}
		}
	}
}

function loginWithFB(){
	
	if(typeof mixpanel != 'undefined')
		mixpanel.track("LogIn clicked", {"Method": "Facebook","URL":window.location.href,version:"1.0"}, function() {});

	//facebook tracking
	_gaq.push(['_trackEvent', 'Student_Facebook_SignUp/Login', 'clicked', '']);
	
    var clLink = "userDashboard.jsp";
	
	var temp = window.location.href;
	var tempNew  = temp.split("culturealley.com/");
	result = tempNew[1];

	if(result != "index.jsp" && result != "index.jsp?opencl=yes" && result != null && result!="")
		clLink = result;

	var tz=jstz.determine();
	var timezone=tz.name();
	window.location.href="FacebookLoginServlet?req=fblogin&timezone="+timezone+"&clLink="+clLink;
	
}

function loginWithGoogle(){
	
	mixpanel.track("LogIn clicked", {"Method": "Google","URL":window.location.href,version:"1.0"}, function() {});
	//facebook tracking
	_gaq.push(['_trackEvent', 'Student_Google_SignUp/Login', 'clicked', '']);

    var clLink = "userDashboard.jsp";
	
	var temp = window.location.href;
	var tempNew  = temp.split("culturealley.com/");
	result = tempNew[1];

	if(result != "index.jsp" && result != "index.jsp?opencl=yes" && result != null && result!="")
		clLink = result;
	
	var tz=jstz.determine();
	var timezone=tz.name();
	
	window.location.href="GoogleLoginServlet?op=Google&clLink="+clLink+"&timezone="+timezone;
	
}

function loginWithYahoo(){
	
	mixpanel.track("LogIn clicked", {"Method": "Yahoo","URL":window.location.href,version:"1.0"}, function() {});
	//facebook tracking
	_gaq.push(['_trackEvent', 'Student_Yahoo_SignUp/Login', 'clicked', '']);

    var clLink = "userDashboard.jsp";
	
	var temp = window.location.href;
	var tempNew  = temp.split("culturealley.com/");
	result = tempNew[1];

	if(result != "index.jsp" && result != "index.jsp?opencl=yes" && result != null && result!="")
		clLink = result;
	
	var tz=jstz.determine();
	var timezone=tz.name();
	window.location.href="YahooLoginServlet?op=Yahoo&clLink="+clLink+"&timezone="+timezone;
	
}



function checkForNewNotification(){
	$.ajax({
		url:"NewCLPaymentServlet?reqType=getreferralnotification",
		dataType:"xml",
		success:function(data){var email=$(data).find("email").text().trim();var name=$(data).find("name").text().trim();if(email!=""){$(".friend-notification").show();if(name!=""){$(".friend-notification #referralName").html(name)}else{$(".friend-notification #referralName").html(email)}setTimeout(function(){setReferralNotified(email)},3000)}}})}function setReferralNotified(email){$.ajax({url:"NewCLPaymentServlet?reqType=setreferralnotified&email="+email,success:function(data){if(data=="ok"){}}})};
					
					
function showDivForum(divId){
						// $("#popupwin").css("visibility","hidden");
						$("#forumListLink").mousemove(function(e)
						{
							var pos=$("#forumListLink").offset();
							var pos1=pos.left;$("#"+divId).css("left",pos1+"px")});
							var height=$("#"+divId).css("height");
							$("#"+divId).show("slow");
							timer1=setInterval(function(){loginSubjectPopupPos(divId,height)},500)}

// show dictionary
function showDivDictionary(divId){
			// $("#popupwin").css("visibility","hidden");
						$("#dictionaryListLink").mousemove(function(e)
											{
												var pos=$("#dictionaryListLink").offset();
												var pos1=pos.left;$("#"+divId).css("left",pos1+"px")});
												var height=$("#"+divId).css("height");
												$("#"+divId).show("slow");
												timer1=setInterval(function(){loginSubjectPopupPos(divId,height)},500)}


//for google analytics event
$("#registerHeader").click(function(){
	
	_gaq.push(['_trackEvent', 'General_Email_signUp ', 'Clicked_on_Register_header', '']);
});

$("#createAccountLesson").click(function(){
	
	_gaq.push(['_trackEvent', 'General_Email_signUp ', 'Clicked_on_Create_Account_lesson', '']);
});

$("#createAccountSpellAthon").click(function(){
	
	_gaq.push(['_trackEvent', 'General_Email_signUp ', 'Clicked_on_Create_Account_spellAthon', '']);
	
});

$("#createAccountSpeedAthon").click(function(){
	
	_gaq.push(['_trackEvent', 'General_Email_signUp ', 'Clicked_on_Create_Account_speedAthon', '']);
});


$(".facebookShare").click(function(){
	
	_gaq.push(['_trackEvent', 'Sharing ', 'Shared lesson using facebook', '']);
});

$(".googleShare").click(function(){

	_gaq.push(['_trackEvent', 'Sharing ', 'Shared lesson using google+', '']);
});



function getCookie(c_name)
{
	var c_value = document.cookie;
	var c_start = c_value.indexOf(" " + c_name + "=");
	if (c_start == -1)
	  {
	  c_start = c_value.indexOf(c_name + "=");
	  }
	if (c_start == -1)
	  {
	  c_value = null;
	  }
	else
	  {
	  c_start = c_value.indexOf("=", c_start) + 1;
	  var c_end = c_value.indexOf(";", c_start);
	  if (c_end == -1)
	  {
	c_end = c_value.length;
	}
	c_value = unescape(c_value.substring(c_start,c_end));
	}
	return c_value;
}

function storeCookie(name,value,exdays)
{
	var exdate=new Date();
	exdate.setDate(exdate.getDate() + exdays);
	var value_processed=escape(value) + ((exdays==null) ? "" : "; expires="+exdate.toUTCString());
	document.cookie=name + "=" + value_processed;
}


function doLoginMain(redirectionURL,translatorParameter) {
	
	//facebook tracking
	_gaq.push(['_trackEvent', 'Login', 'Email', 'clicked']);


	toMemMain();
	clearInterval(timer);
	var email = $("#username").val();
	var password = $("#password").val();
	var date = new Date();
	var MILISEC_PRE_HOUR = 60 * 60 * 1000;
	var offset = (-(date.getTimezoneOffset() / 60) * MILISEC_PRE_HOUR);
	var emailfilter = /^\w+[\+\.\w-]*@([\w-]+\.)*\w+[\w-]*\.([a-z]{2,4}|\d+)$/i;
	var flag = false;
	var returnval = emailfilter.test(email);
	if (returnval == false) {
		$("#validateEmail").css("display", "inline");
		flag = true
	} else {
		$("#validateEmail").css("display", "none")
	}
	if (password.length == 0) {
		$("#errorPassword").css("display", "inline");
		flag = true
	} else {
		$("#errorPassword").css("display", "none")
	}
	if (flag) {
		return
	}
	var tz = jstz.determine();
	var timezone = tz.name();
	var url = "dologin.action";
	$("#loginButton").attr("disabled", "disabled");
	$("#loginButton").css("background-color", "#C0C0C0");
	
	$("#notiMessage").text("Logging You In...");
	$(".loginNotification").show();
	
	
	//redirection code 
    var clLink = "userDashboard.jsp"+translatorParameter;
	
	var temp = redirectionURL;
	var tempNew  = temp.split("culturealleytesting-env.elasticbeanstalk.com/");
	result = tempNew[1];

	if(result != "index.jsp?opencl=yes" && result != null && result!="")
		clLink = result;

	
	
	$.post(url, {
		userName : email,
		password : password,
		timeZoneOffset : offset,
		timezone : timezone
	}, function(doc) {
		$(".loginNotification").hide();
		parseLoginResultMain(doc,clLink);
		
	})
}


function parseLoginResultMain(doc,clLink)
{
	var userType=$(doc).find("userType").text();
	var accStatus=$(doc).find("accStatus").text();
	var errorType=$(doc).find("errorType").text();
	var course=$(doc).find("course").text();
	var step=$(doc).find("step").text();
	var fbLogin=$(doc).find("FBLogin").text();
	var fbSignUp=$(doc).find("FBSignUp").text();
	
	var lessonUrl=getCookie("lesson_url");
	
	if (lessonUrl!=null && lessonUrl!="" )
	{
	  	loginRedirectId =10;
	}	
	
	
	if(accStatus=="1")
	{
		//google analytics tracking
		_gaq.push(['_trackEvent', 'Login', 'Email', 'Failed-account not verfied']);

		
		openPopUp("loginerrors.jsp?code=3");
		$("#loginButton").removeAttr("disabled");
		$("#loginButton").css("background-color","#BD1550");
		setTimeout(function(){
			closePopUp()
			},3000)
	}
	else{if(userType=="1"){
		if(loginRedirectId==1)
		{
			window.location.replace("growthstation.action")
		}
		
		else{
			window.location.replace("adminhome.action")
		}
	}
	else
	{
		if(userType=="2")
		{
			var wlstatus=$(doc).find("wlStatus").text();
			if(wlstatus!=""){
				alert(wlstatus)
			}
			
			if(step=="1")
			{
				var code=$(doc).find("code").text();
				window.location.replace("verifyaccountCA.action?code="+code)
			}
			
			else{if(step=="2"){
				window.location.replace("signuptutor2.action")
			}
			else{
				if(step=="3"){
					var ft=$(doc).find("ft").text();
					
					if(fbLogin == 0)
					{
					
						_gaq.push(['_trackEvent', 'Tutor_Facebook_Login', 'successful','']);
						if(typeof mixpanel != 'undefined'){
							mixpanel.track("LogIn clicked Successful", {"Method": "Facebook",version:"1.0"}, function() {});
							
							var fbemailId=$(doc).find("emailId").text();
							var fbfirstName=$(doc).find("firstName").text();
							var fblastName=$(doc).find("lastName").text();
							
							var courseValue="";
							if(course==3){courseValue="Spanish"}else if(course==4){courseValue="Mandarin"}else if(course==2){courseValue="Punjabi"}
							else if(course==6){courseValue="Hindi"}else if(course==5){courseValue="English"}else if(course==7){courseValue="Korean"}
							else if(course==8){courseValue="Japanese"}else if(course==14){courseValue="Italian"}else if(course==15){courseValue="French"}
							 mixpanel.register({
							        isteacher: "1" ,
							        Subject: courseValue,
							        LastLessonSeen: '',
							        FirstName:fbfirstName,
							        LastName:fblastName,
							        FBID:'',
							        EmailID:fbemailId,
							        version:"1.0"
							    });
							 mixpanel.identify(fbemailId);
								mixpanel.people.set({
								    "$email": fbemailId,
							        version:"1.0"
								});
						}
					}
					if(fbSignUp == 0)
					{
						_gaq.push(['_trackEvent', 'Tutor_Facebook_SignUp', 'successful','']);
						if(typeof mixpanel != 'undefined'){
							mixpanel.track("Register clicked Successful", {"Method": "Facebook",version:"1.0"}, function() {});
							
							var fbemailId=$(doc).find("emailId").text();
							var fbfirstName=$(doc).find("firstName").text();
							var fblastName=$(doc).find("lastName").text();
							
							var courseValue="";
							if(course==3){courseValue="Spanish"}else if(course==4){courseValue="Mandarin"}else if(course==2){courseValue="Punjabi"}
							else if(course==6){courseValue="Hindi"}else if(course==5){courseValue="English"}else if(course==7){courseValue="Korean"}
							else if(course==8){courseValue="Japanese"}else if(course==14){courseValue="Italian"}else if(course==15){courseValue="French"}
							 mixpanel.register({
							        isteacher: "1" ,
							        Subject: courseValue,
							        LastLessonSeen: '',
							        FirstName:fbfirstName,
							        LastName:fblastName,
							        FBID:'',
							        EmailID:fbemailId,
							        version:"1.0"
							    });
							 mixpanel.identify(fbemailId);
								mixpanel.people.set({
								    "$email": fbemailId,
							        version:"1.0"
								});
						}
					}

						
					if(loginRedirectId==1)
					{
						window.location.replace("growthstation.action")
					}
					else
					{
						if(ft=="1"){
							var fturl=$(doc).find("fturl").text();
							window.location=fturl;
							
						}
						else{
							if(ft=="2"){
								window.location.href=window.location.href
							}
							else{
								if(wlstatus!="")
								{
									window.location.href=nextLessonURL
								}
								else
								{
									if(clLink=="")
									{
										window.location.replace("home.action")
									}
									else{
										
										if(typeof mixpanel != 'undefined'){
											if(fbLogin==0 || fbSignUp==0){
												
											}else{
											mixpanel.track("LogIn clicked Successful", {"Method": "Email",version:"1.0"}, function() {});
											
											var courseValue="";
											if(course==3){courseValue="Spanish"}else if(course==4){courseValue="Mandarin"}else if(course==2){courseValue="Punjabi"}
											else if(course==6){courseValue="Hindi"}else if(course==5){courseValue="English"}else if(course==7){courseValue="Korean"}
											else if(course==8){courseValue="Japanese"}else if(course==14){courseValue="Italian"}else if(course==15){courseValue="French"}
											 mixpanel.register({
											        isteacher: "1" ,
											        Subject: courseValue,
											        LastLessonSeen: '',
											        FirstName:'',
											        LastName:'',
											        FBID:'',
											        EmailID:$("#username").val(),
											        version:"1.0"
											    });
											 mixpanel.identify($("#username").val());
												mixpanel.people.set({
												    "$email": $("#username").val(),
											        version:"1.0"
												});
											}
										}
										
										if(fbLogin==0 || fbSignUp==0){
											
										
											
											if(fbLogin==0){
																								
												setTimeout(function(){
													if(clLink.indexOf("NewCultureAlley")!= -1)
														window.location.replace("NewCultureAlley/userDashboard.jsp");
													else
														window.location.replace("success.jsp?method=Facebook&&email="+$(doc).find("emailId").text()+"&&type=old&&login_signup=login");
													//window.location.replace(clLink)
												},500);
											}else if(fbSignUp==0){
												
												setTimeout(function(){
													if(clLink.indexOf("NewCultureAlley")!= -1)
														window.location.replace("NewCultureAlley/userDashboard.jsp");
													else
														window.location.replace("success.jsp?method=Facebook&&email="+$(doc).find("emailId").text()+"&&type=new&&login_signup=signup");
													//window.location.replace(clLink)
												},500);
											}
										}else{
											var courseValue="";
											if(course==3){courseValue="Spanish"}else if(course==4){courseValue="Mandarin"}else if(course==2){courseValue="Punjabi"}
											else if(course==6){courseValue="Hindi"}else if(course==5){courseValue="English"}else if(course==7){courseValue="Korean"}
											else if(course==8){courseValue="Japanese"}else if(course==14){courseValue="Italian"}else if(course==15){courseValue="French"}
											
												setTimeout(function(){
													window.location.replace("success.jsp?method=Email&&email="+$("#username").val()+"&&type=old&&login_signup=login");
													//window.location.replace(clLink)
												},500);
											}
										
										}
									}
								}
							}
						}
					}
				else{
					if(errorType=="4")
					{
							
						openPopUp("loginerrors.jsp?code=10&course="+course);
						$("#loginButton").removeAttr("disabled");
						$("#loginButton").css("background-color","#BD1550")
					}
					else{
						if(errorType=="3")
						{
							//google analytics tracking		
							_gaq.push(['_trackEvent', 'Login', 'Email', 'Failed-blocked by admin']);

							
							openPopUp("loginerrors.jsp?code=11");
							$("#loginButton").removeAttr("disabled");
							$("#loginButton").css("background-color","#BD1550")
						}
					}
				}
			}
		}
	}
		else{if(userType=="3")
		{
			var wlstatus=$(doc).find("wlStatus").text();
			if(wlstatus!=""){
				alert(wlstatus)
			}
			if(step=="1")
			{
				var code=$(doc).find("code").text();
				window.location.replace("verifyaccountCA.action?code="+code)
			}
			
			else{if(step=="3")
			{
				var ft=$(doc).find("ft").text();
				if(fbLogin == 1)
				{
					_gaq.push(['_trackEvent', 'Student_Facebook_SignUp/Login', 'Login successful','']);
					if(typeof mixpanel != 'undefined'){
						mixpanel.track("LogIn clicked Successful", {"Method": "Facebook",version:"1.0"}, function() {});
						var courseValue="";
						if(course==3){courseValue="Spanish"}else if(course==4){courseValue="Mandarin"}else if(course==2){courseValue="Punjabi"}
						else if(course==6){courseValue="Hindi"}else if(course==5){courseValue="English"}else if(course==7){courseValue="Korean"}
						else if(course==8){courseValue="Japanese"}else if(course==14){courseValue="Italian"}else if(course==15){courseValue="French"}
						
						var fbemailId=$(doc).find("emailId").text();
						var fbfirstName=$(doc).find("firstName").text();
						var fblastName=$(doc).find("lastName").text();
						
						mixpanel.register({
						        isteacher: "0" ,
						        Subject: courseValue,
						        LastLessonSeen: '',
						        FirstName:fbfirstName,
						        LastName:fblastName,
						        FBID:'',
						        EmailID:fbemailId,
						        version:"1.0"
						    });
						 mixpanel.identify(fbemailId);
							mixpanel.people.set({
							    "$email": fbemailId,
						        version:"1.0"
							});
					}
				}
				
				if(fbSignUp == 1)
				{


					_gaq.push(['_trackEvent', 'Student_Facebook_SignUp/Login', 'SignUp successful','']);
					if(typeof mixpanel != 'undefined'){
						mixpanel.track("Register clicked Successful", {"Method": "Facebook",version:"1.0"}, function() {});
						var courseValue="";
						var fbemailId=$(doc).find("emailId").text();
						var fbfirstName=$(doc).find("firstName").text();
						var fblastName=$(doc).find("lastName").text();
						
						if(course==3){courseValue="Spanish"}else if(course==4){courseValue="Mandarin"}else if(course==2){courseValue="Punjabi"}
						else if(course==6){courseValue="Hindi"}else if(course==5){courseValue="English"}else if(course==7){courseValue="Korean"}
						else if(course==8){courseValue="Japanese"}else if(course==14){courseValue="Italian"}else if(course==15){courseValue="French"}
					
						mixpanel.register({
						        isteacher: "0" ,
						        Subject: courseValue,
						        LastLessonSeen: '',
						        FirstName:fbfirstName,
						        LastName:fblastName,
						        FBID:'',
						        EmailID:fbemailId,
						        version:"1.0"
						    });
						 mixpanel.identify(fbemailId);
							mixpanel.people.set({
							    "$email": fbemailId,
						        version:"1.0"
							});
					}
				}
				
				if(loginRedirectId==1)
				{
					window.location.replace("growthstation.action")
				}
				
				else{
					if(ft=="1")
					{
						var fturl=$(doc).find("fturl").text();
						window.location=fturl;
						
					}
					else{if(ft=="2")
					{
						window.location.href=window.location.href
					}
					

					else{if(wlstatus!="")
					{
						window.location.href=nextLessonURL
					}
					else{if(clLink==""){
						
						window.location.replace("home.action")
					}
					
					else{
						if(typeof mixpanel != 'undefined'){
							if(fbLogin==1 || fbSignUp==1){
								
							}else{
								
								mixpanel.track("LogIn clicked Successful", {"Method": "Email",version:"1.0"}, function() {});
							var courseValue="";
							if(course==3){courseValue="Spanish"}else if(course==4){courseValue="Mandarin"}else if(course==2){courseValue="Punjabi"}
							else if(course==6){courseValue="Hindi"}else if(course==5){courseValue="English"}else if(course==7){courseValue="Korean"}
							else if(course==8){courseValue="Japanese"}else if(course==14){courseValue="Italian"}else if(course==15){courseValue="French"}
							
							 mixpanel.register({
							        isteacher: "0" ,
							        Subject: courseValue,
							        LastLessonSeen: '',
							        FirstName:'',
							        LastName:'',
							        FBID:'',
							        EmailID:$("#username").val(),
							        version:"1.0"
							    });
							 mixpanel.identify($("#username").val());
							 mixpanel.people.set({
								    "$email": $("#username").val(),
							        version:"1.0"
								});
						 }
						}
						
						if(fbLogin==1 || fbSignUp==1){
							
							
							if(fbLogin==1){
								setTimeout(function(){
									if(clLink.indexOf("NewCultureAlley")!= -1)
										window.location.replace("NewCultureAlley/userDashboard.jsp");
									else
										window.location.replace("success.jsp?method=Facebook&&email="+$(doc).find("emailId").text()+"&&type=old&&login_signup=login");
									//window.location.replace(clLink)
								},500);
							}else if(fbSignUp==1){
								setTimeout(function(){
									if(clLink.indexOf("NewCultureAlley")!= -1)
										window.location.replace("NewCultureAlley/userDashboard.jsp");
									else
										window.location.replace("success.jsp?method=Facebook&&email="+$(doc).find("emailId").text()+"&&type=new&&login_signup=signup");
									//window.location.replace(clLink)
								},500);
							}
						}else{
							
								setTimeout(function(){
									window.location.replace("success.jsp?method=Email&&email="+$("#username").val()+"&&type=old&&login_signup=login");
									//window.location.replace(clLink)
								},500);
							}
						
						
					}
				}
			}
		}
					}
				}
			else{if(step=="2")
			{
				window.location.replace("signupstudent2.action")
			}
			else{
				if(errorType=="4"){
				openPopUp("loginerrors.jsp?code=10&course="+course);
				$("#loginButton").removeAttr("disabled");
				$("#loginButton").css("background-color","#BD1550")
				}
				else{
					if(errorType=="3")
					{
						openPopUp("loginerrors.jsp?code=11");
						$("#loginButton").removeAttr("disabled");
						$("#loginButton").css("background-color","#BD1550")
						}
					}
				}
			}
			}
			}
		else{if(errorType=="0")
		{
		
			_gaq.push(['_trackEvent', 'Login', 'Email', 'Failed-retry']);
			
			openPopUp("loginerrors.jsp?code=0");
			setTimeout(function(){closePopUp()},4000)
			if(typeof mixpanel != 'undefined'){
				mixpanel.track("LogIn clicked UnSuccessful", {"Method": "Email","Error":"Server Error",version:"1.0"}, function() {});
				
			}
		}
		else
		{
			if(errorType=="1")
			{

				_gaq.push(['_trackEvent', 'Login', 'Email', 'Failed-username-password dont match']);
				
				openPopUp("loginerrors.jsp?code=1");
				setTimeout(function(){closePopUp()},4000)
				if(typeof mixpanel != 'undefined'){
					mixpanel.track("LogIn clicked UnSuccessful", {"Method": "Email","Error":"Username Password Not Matched",version:"1.0"}, function() {});
				}
			}
			
			else if(errorType=="10")
			{
				_gaq.push(['_trackEvent', 'FacebookID_exists_email_registered', 'failed to login', 'user signup with facebook(Unable to login with email)']);
				
				openPopUp("loginerrors.jsp?code=1");
				setTimeout(function(){closePopUp()},4000);
				if(typeof mixpanel != 'undefined'){
					mixpanel.track("LogIn clicked UnSuccessful", {"Method": "Email","Error":"Username Signup With Facebook",version:"1.0"}, function() {});
				}
			}
			else
			{
				if(errorType=="2")
				{

					_gaq.push(['_trackEvent', 'Login', 'Email', 'Failed-new user']);
					
					showEleCommon("notRegisteredUser");
					if(typeof mixpanel != 'undefined'){
						mixpanel.track("LogIn clicked UnSuccessful", {"Method": "Email","Error":"Username Not Exists(New User)",version:"1.0"}, function() {});
					}
				}
			}
		}
		$("#loginButton").removeAttr("disabled");
		$("#loginButton").css("background-color","#BD1550")
		}
		}
		}
	}
}


function sendFormDataMain(type) {
		
		var email = $("#email").val();
		
		if(type == "tutor")
			var language = $("#languageToTeach").val();
		else
			var language = $("#language").val();
			
			$.ajax({
				url : "CASignupServlet?email=" + email + "&req=chksignup",
				success : function(data) {
					if (data == "1") {
						$("#emailExist").html("Email id already registered. <a style='color:#bd1550;text-decoration:underline;font-size:16px;' href='loginMain.jsp?&&loginemail'>Log in</a> here");
						$("#emailExist").show();
						if(typeof mixpanel != 'undefined'){
							mixpanel.track("Register clicked UnSuccessful", {"Method": "Email","Error":"Email Already Exists",version:"1.0"}, function() {});
						}
						if(type == "tutor")
							_gaq.push(['_trackEvent', 'Teacher_Email_signUp ', ' Teacher_got_an_error_after_JetSetGo', 'Emails already exists']);
						else
							_gaq.push(['_trackEvent', 'Student_Email_signUp', 'Got_an_error_on_after_first_step', 'Emails already exists']);
						
						
					}
					else if (data == "0"){
						submitFormCompleteDataMain(type);
						
					}
					else
					{
						$("#emailExist").html("Email id already registered. <a style='color:#bd1550;text-decoration:underline;font-size:16px;' href='loginMain.jsp?&&loginemail'>Log in</a> here");
						$("#emailExist").show();
						if(typeof mixpanel != 'undefined'){
							mixpanel.track("Register clicked UnSuccessful", {"Method": "Email","Error":"Email Already Exists",version:"1.0"}, function() {});
							
						}
						_gaq.push(['_trackEvent', 'General_Email_signUp', 'SignUp_failed', 'Emails already exists thorugh facebook']);
						
					}
				}
			});
}
function submitFormCompleteDataMain(type) {
		
	
			var pass1 = $("#password").val();
			var email = $("#email").val();
			var language = $("#language").val();
			
			
			//$("#register").css("background-color", "#C0C0C0");
			//$("#register").attr("disabled", "disabled");
			//$("#loadingDiv").css("visibility", "visible");
			var date = new Date();
			var MILISEC_PRE_HOUR = 60 * 60 * 1000;
			var offset = (-(date.getTimezoneOffset() / 60) * MILISEC_PRE_HOUR);
			var tz = jstz.determine();
			var timezone = tz.name();
			
			$("#notiMessage").text("Signing You Up...");
			$(".loginNotification").show();

			
			$.post('Signup/doSignupCA.action', {
				email : email,
				language : language,
				type : type,
				password : pass1,
				timeZoneOffset : offset,
				timezone : timezone
			}, function(doc) {
					
				var languageValue="";
				if(language==3){languageValue="Spanish"}else if(language==4){languageValue="Mandarin"}else if(language==2){languageValue="Punjabi"}
				else if(language==6){languageValue="Hindi"}else if(language==5){languageValue="English"}else if(language==7){languageValue="Korean"}
				else if(language==8){languageValue="Japanese"}else if(language==14){languageValue="Italian"}else if(language==15){languageValue="French"}
				
				if(type == "tutor"){
					_gaq.push(['_trackEvent', 'Teacher_Email_signUp ', ' Teacher_signUp_successful', '']);
					if(typeof mixpanel != 'undefined'){
					mixpanel.register({
				        isteacher: "1" ,
				        Subject: languageValue,
				        LastLessonSeen: '',
				        FirstName:'',
				        LastName:'',
				        FBID:'',
				        EmailID:email,
				        version:"1.0"
				    });
				 mixpanel.identify(email);
				 mixpanel.people.set({
					    "$email": email,
					    version:"1.0"
					});
					}
				}
				else{
					_gaq.push(['_trackEvent', 'Student_Email_signUp', 'SignUp_successful', '']);
					if(typeof mixpanel != 'undefined'){
					mixpanel.register({
				        isteacher: "0" ,
				        Subject: languageValue,
				        LastLessonSeen: '',
				        FirstName:'',
				        LastName:'',
				        FBID:'',
				        EmailID:email,
				        version:"1.0"
				    });
				 mixpanel.identify(email);
				 mixpanel.people.set({
					    "$email": email,
					    version:"1.0"
					});
					}
				}
				if(typeof mixpanel != 'undefined'){
					mixpanel.track("Register clicked Successful", {"Method": "Email",version:"1.0"}, function() {});
				}
				$(".loginNotification").hide();
				$("#loadPage").html(doc);
		});
}

function doDirectLoginMain(email,course) {
	
	//google anaytics code
	_gaq.push(['_trackEvent', 'Login', 'Email', 'clicked']);

	
	closePopUp();
	var url = "dodirectlogin.action";
	$.post(url, {
		userName : email
	}, function(doc) {
		
		/*
		if(course== "Spanish" || course == "spanish")
			window.location = "//culturealley.com/spanish";
		
		else if(course== "Mandarin" || course == "mandarin")
			window.location = "//culturealley.com/mandarin";

		else if(course== "Hindi" || course == "hindi")
			window.location = "//culturealley.com/hindi";
		
		else if(course== "Punjabi" || course == "punjabi")
			window.location = "//culturealley.com/punjabi";
		
		else 
			window.location = "//culturealley.com/";
		
		*/
		window.location.replace("success.jsp?method=Email&&email="+email+"&&type=new&&login_signup=signup&&courseValue="+course);
		
		//window.location = "userDashboard.jsp#isFirstTime=1";
			
		
	})
}


function loginWithTwitterForWordList(){
		
	var tz=jstz.determine();
	var timezone=tz.name();
	window.location.href="TwitterWordListServlet?op=twitterWord&timezone="+timezone;	

}


//function for auto login
function newCookie(name, value, days) {
	var days = 30;
	if (days) {
		var date = new Date();
		date.setTime(date.getTime() + (days * 24 * 60 * 60 * 1000));
		var expires = "; expires=" + date.toGMTString();
	} else {
		var expires = "";
	}
	document.cookie = name + "=" + value + expires + "; path=/";
}
function readCookie(name) {
	var nameSG = name + "=";
	var nuller = "";
	if (document.cookie.indexOf(nameSG) == -1) {
		return nuller;
	}
	var ca = document.cookie.split(";");
	for ( var i = 0; i < ca.length; i++) {
		var c = ca[i];
		while (c.charAt(0) == " ") {
			c = c.substring(1, c.length);
		}
		if (c.indexOf(nameSG) == 0) {
			return c.substring(nameSG.length, c.length);
		}
	}
	return null;
}

function saveAutoLogIn(emailId){
	
	if(emailId != null && emailId!=""){
		newCookie("isLoggedIn","1",365);
		newCookie("emailId",emailId,365);
	}
	
}

function saveAutoLogOut(){
		newCookie("isLoggedIn","0",365);
}

function doAutoLogin(){
	//checking is user logged in or not
	var loginStatus = readCookie("isLoggedIn");
	if(loginStatus != null && loginStatus != "" && loginStatus != "0"){
		
		var emailId = readCookie("emailId");
		
		if(emailId != null && emailId != "" && emailId != "0"){
				
			var url = "dodirectlogin.action";
			$.post(url, {
				userName : emailId
			}, function(doc) {
				parseLoginResultAutoLogin(doc);
			});
		}
	}
}

function parseLoginResultAutoLogin(doc)
{
	var clLink = "userDashboard.jsp";
	var userType=$(doc).find("userType").text();
	var step=$(doc).find("step").text();
			
	if(userType=="1"){
		window.location = "home.action";
	}
	else if(userType=="2")
	{	
		if(step=="3"){
			window.location = clLink;			
		}
	}	
	else if(userType=="3")
	{			
		if(step=="3"){
			window.location = clLink;
		}
	}
}
			

