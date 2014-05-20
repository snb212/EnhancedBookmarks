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
		var username = $("#username").val();
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
