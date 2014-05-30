$( // loads functions on page load
function() {
	$(".registration-submit").click(function() {
		var username = $("#username").val();
		var password = $("#password").val();
		if (username && password) {
			$.ajax({
				type : 'post',
				url : '/EnhancedBookmarks/BookmarkService',
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
	$(".login-button").click(function() {
		loginUser();
	});
	$(".logout-button").click(function() {
			$.ajax({
				type : 'post',
				url : '/EnhancedBookmarks/BookmarkService',
				data : {"function":"logout"},
				success : function(data) {
					logoutSuccess(data);
				},
				error : function() {
					alert('error');
				}
			});
	});
	$(".add-bookmark-button").click(function() {
		var bookmark = $("#bookmarkURL").val();
		if(bookmark){
			$.ajax({
				type : 'post',
				url : '/EnhancedBookmarks/BookmarkService',
				data : {"function":"addBookmark", "bookmark": bookmark},
				success : function(data) {
					console.log(data);
					updatedAddButton(data);
					getBookmarkImage(bookmark);
				},
				error : function() {
					alert('error');
				}
			});
		} else {
			$("#bookmarkURL").val("Drop a bookmark here!");
		}
	});
	$(".getBookmarks").click(function() {
		getBookmarks();
	});
	$(".get-bookmarks-button").click(function() {
		getBookmarks();
	});
	
	if($(".welcome-name")){
		$.ajax({
			type : 'post',
			url : '/EnhancedBookmarks/BookmarkService',
			data : {"function":"getUsername"},
			success : function(data) {
				if(data != null && data !== undefined && data != ""){
					//logged in
					$(".welcome-name").text(", " + data);
					$(".login-button").hide();
					$(".register-button").hide();
					$(".get-bookmarks-button").show();
					$(".add-bookmark-container").show();
				} else {
					$(".add-bookmark-container").hide();
					$(".logout-button").hide();
					$(".login-button").show();
					$(".register-button").show();
				}
			},
			error : function() {
				alert('error');
			}
		});
	};
	
	//Fixed Header	
	$(function () {
		  var stuck = false,
		      currPos = $('.fixed-header').offset().top;

		  $(window).scroll(function () {

		    var scrollTop = $(window).scrollTop();
		    //console.log(scrollTop);
		    if (!stuck && scrollTop > currPos) {
		      $('.fixed-header').css('top', 0);
		      $('.fixed-header').css('position','fixed');
		      stuck = true;
		    } else if (scrollTop < 3){
		    	console.log("header reset");
		    	$('.fixed-header').css('top', 0);
		    	$('.fixed-header').css('position','fixed');
		    } else if (stuck && scrollTop < currPos) {
		      $('.fixed-header').css('top', currPos);
		      $('.fixed-header').css('position','absolute');
		      stuck = false;
		    } 
		  });
		});
	//Dynamically adds margin-top to body. Keeping it below fixed header
//	$("#body-content-container").css("margin-top", parseInt($(".fixed-header").css("height")) + 20);

});

// Login Page 
function loginUser(){
	var username = $("#username").val();
	var password = $("#password").val();
	var rememberMe = $("rememberMe").is(':checked');
	if (username && password) {
		$.ajax({
			type : 'post',
			url : '/EnhancedBookmarks/BookmarkService',
			data : {"function":"loginUser", "username":username, "password":password, "rememberMe":rememberMe},
			success : function(data) {
				loginSuccess(data);
			},
			error : function(data) {
				alert('error ' + data);
			}
		});
	}
}

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
	window.location.replace("/EnhancedBookmarks/");
}

function logoutSuccess(data){
	alert(data);
	//Send user back to referrer
	//document.location.href = document.referrer;
	location.reload();
}

function getBookmarks(){
	$.ajax({
		type : 'post',
		url : '/EnhancedBookmarks/BookmarkService',
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
}

function getBookmarkImage(bookmark){
	$.ajax({
		type : 'post',
		url : '/EnhancedBookmarks/BookmarkService',
		data : {"function":"getBookmarkImage", "bookmark":bookmark},
		success : function(data) {
			console.log(data)
		},
		error : function() {
			alert('error');
		}
	});
}

function updatedAddButton(message){
	var oldText = $(".header-text-input").text();
	$(".header-text-input").fadeOut(700, function() {
		$(this).text(message).fadeIn(700, function(){$(".header-text-input").delay(2000).fadeOut(700, function() {
			$(this).text(oldText).fadeIn(700);
		});});
	});
	
}
