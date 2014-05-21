$( // loads functions on page load
function() {
	$("#getButton").click(function() {
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
	});
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
	document.location.href = document.referrer;
}
