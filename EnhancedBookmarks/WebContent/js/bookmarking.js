$( // loads functions on page load
function() {
	/*$("#getButton").click(function() {
		$.ajax({
			type : 'get',
			url : 'http://localhost:8080/EnhancedBookmarks/BookmarkService',
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
				url : 'http://localhost:8080/EnhancedBookmarks/BookmarkService',
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
				url : 'http://localhost:8080/EnhancedBookmarks/BookmarkService',
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
				url : 'http://localhost:8080/EnhancedBookmarks/BookmarkService',
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
				url : 'http://localhost:8080/EnhancedBookmarks/BookmarkService',
				data : {"function":"logout"},
				success : function(data) {
					logoutSuccess(data);
				},
				error : function() {
					alert('error');
				}
			});
	});
	$("#addBookmarkButton").click(function() {
		var bookmark = $("#bookmarkURL").val();
		$.ajax({
			type : 'post',
			url : 'http://localhost:8080/EnhancedBookmarks/BookmarkService',
			data : {"function":"addBookmark", "bookmark": bookmark},
			success : function(data) {
				console.log(data);
			},
			error : function() {
				alert('error');
			}
		});
	});
	$("#getBookmarks").click(function() {
		$.ajax({
			type : 'post',
			url : 'http://localhost:8080/EnhancedBookmarks/BookmarkService',
			data : {"function":"getBookmarks"},
			success : function(data) {
				console.log(data);
				var tr;
				var json = $.parseJSON(data);
		        for (var i = 0; i < json.length; i++) {
		            tr = $('<tr/>');
		            tr.append("<td>" + json[i].bookmarkname + "</td>");
		            tr.append("<td>" + json[i].url + "</td>");
		            $('table').append(tr);
		        }
			},
			error : function() {
				alert('error');
			}
		});
	});
	if($("#welcomeName")){
		$.ajax({
			type : 'post',
			url : 'http://localhost:8080/EnhancedBookmarks/BookmarkService',
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
	};
	
	
	/*
	 * //setup get button click $("#getButton").click(function(){
	 * $.get("http://localhost:8080/EnhancedBookmarks/BookmarkService",function(data,status){
	 * alert("Data: " + data + "\nStatus: " + status); }); });
	 * 
	 * //setup post button click $("#postButton").click(function(){
	 * $.post("http://localhost:8080/EnhancedBookmarks/BookmarkService",{data:"testing"},function(data,status){
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
