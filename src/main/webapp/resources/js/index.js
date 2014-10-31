$(document).ready(
		function() {

			$("#gotoCompare").click(function() {

				window.location = ctx + '/compare';
			});
			$("#gotoLabel").click(function() {

				window.location = ctx + '/label';
			});

			$.ajax({
				url : ctx + '/getAlgorithms',
				data : {},
				dataType : "json",
				type : 'get',
				async : true,
				success : function(data) {
					for (var i = 0; i < data.length; i++) {
						$(".algorithmArea").append(
								"<button class='algorithmButton btn' id='" + data[i].name
										+ "'>" + data[i].name
										+ "</button><hr>")
					}
					
					$(".algorithmButton").click(function(){
						showVisualization($(this).attr("id"));
					});
				},
				beforeSend : function(data) {
				},
				error : function(jq, status, errorMsg) {
					alert("Status: " + status + " Error: " + errorMsg);
				},
				complete : function(jq, status) {
					// alert(status);
				}
			});
		});

function showVisualization(algorithmName) {
	window.location = ctx + '/main?algorithmName=' + algorithmName;
}