var cache = {};
$(document).ready(function() {
	
	
	$("#showVisualization").click(function(){
		var selectedSenator = $("#senators").val();
		showVisualization(selectedSenator);
	});
	
	
	$("#senators").autocomplete({
		delay : 0,
		minLength : 1,
		select : function(event, ui) {
			if (ui.item) {
				// do something with ui.item.value
				showVisualization(ui.item.value);
			}

		},
		open : function(event, ui) {
		},
		source : function(request, response) {

			var term = request.term.toLowerCase();
			if (term in cache) {
				response(cache[term]);
				return;
			}

			$.ajax({
				url : ctx + '/getSenatorName',
				data : {
					searchTerm : request.term
				},
				dataType : "json",
				type : 'get',
				async : true,
				success : function(data) {
					var jsonArr = $.map(data, function(item) {
						return {
							value : item.name,
							id : item.id
						};
					})
					cache[term] = jsonArr;
					response(jsonArr);

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
		}
	});
});


function showVisualization(senatorName){
	window.location = ctx + '/visualize?senatorName='+senatorName;
}
