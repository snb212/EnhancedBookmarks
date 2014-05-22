$( // loads functions on page load
function() {
	/*$("#getButton").click(function() {
		$.ajax({
			type : 'get',
			url : 'http://localhost:8080/EnhancedBookmarks/HelloWorld',
			data : "getTest",
			success : function(data) {
				alert(data);
			},
			error : function() {
				alert('error');
			}
		});
	});
	$("#postButton").click(function() {
		var username = $("#username-book").val();
		var bookmark = $("#bookmark").val();
		if (username && bookmark) {
			$.ajax({
				type : 'post',
				url : 'http://localhost:8080/EnhancedBookmarks/HelloWorld',
				data : {"username":username, "bookmark":bookmark},
				success : function(data) {
					alert(data);
				},
				error : function() {
					alert('error');
				}
			});
		}
	});*/
	$("#submit").click(function() {
		var username = $("#username").val();
		var password = $("#password").val();
		if (username && password) {
			$.ajax({
				type : 'post',
				url : 'http://localhost:8080/EnhancedBookmarks/HelloWorld',
				data : {"function":"registerUser", "username":username, "password":password},
				success : function(data) {
					registerSuccess(data);
				},
				error : function() {
					alert('error');
				}
			});
		}
	});
	$("#loginButton").click(function() {
		var username = $("#username").val();
		var password = $("#password").val();
		var rememberMe = $("rememberMe").is(':checked');
		if (username && password) {
			$.ajax({
				type : 'post',
				url : 'http://localhost:8080/EnhancedBookmarks/HelloWorld',
				data : {"function":"loginUser", "username":username, "password":password, "rememberMe":rememberMe},
				success : function(data) {
					loginSuccess(data);
				},
				error : function(data) {
					alert('error ' + data);
				}
			});
		}
	});
	$("#logoutButton").click(function() {
			$.ajax({
				type : 'post',
				url : 'http://localhost:8080/EnhancedBookmarks/HelloWorld',
				data : {"function":"logout"},
				success : function(data) {
					logoutSuccess(data);
				},
				error : function() {
					alert('error');
				}
			});
	});
	if($("#welcomeName")){
		$.ajax({
			type : 'post',
			url : 'http://localhost:8080/EnhancedBookmarks/HelloWorld',
			data : {"function":"getUsername"},
			success : function(data) {
				if(data != null && data !== undefined && data != ""){
					$("#welcomeName").text(", " + data);
				}
			},
			error : function() {
				alert('error');
			}
		});
	}
	/*
	 * //setup get button click $("#getButton").click(function(){
	 * $.get("http://localhost:8080/EnhancedBookmarks/HelloWorld",function(data,status){
	 * alert("Data: " + data + "\nStatus: " + status); }); });
	 * 
	 * //setup post button click $("#postButton").click(function(){
	 * $.post("http://localhost:8080/EnhancedBookmarks/HelloWorld",{data:"testing"},function(data,status){
	 * alert("Data: " + data + "\nStatus: " + status); }); });
	 */
});

function registerSuccess(data){
	alert("Registration Successful! " + data);
	//Send user back to referrer
	//document.location.href = document.referrer;
	location.reload();
}

function loginSuccess(data){
	alert(data);
	//Send user back to referrer
	//document.location.href = document.referrer;
	location.reload();
}

function logoutSuccess(data){
	alert(data);
	//Send user back to referrer
	//document.location.href = document.referrer;
	location.reload();
}
