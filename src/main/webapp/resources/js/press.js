$(document).ready(function() {
	var searchedTerm = "";
	$('#searchTerm').autocomplete({
		serviceUrl : ctx + '/getSenatorName',
		paramName : "searchTerm",
		delimiter : ",",
		transformResult : function(response) {

			return {
				// must convert json to javascript object before process
				suggestions : $.map($.parseJSON(response), function(item) {

					return {
						value : item.name,
						data : item.id
					};
				})

			};

		},
		select : function(event, ui) {
			searchedTerm = ui.item.value
		}

	});

	$('#search').click(function() {
		alert(searchedTerm);
		$.ajax({
			url : ctx + "/getPressData",
			data : {
				senatorName : "akaka"
			},
			dataType : "json",
			type : 'get',
			async : true,
			success : function(data) {
				var finalData = $.map($.parseJSON(data), function(item) {
					return {
						time : item.time,
						senatorName : item.senatorName,
						topicName : item.topicName,
						topicProp : item.topicProp
					};
				})

				alert(finalData);
			},
			beforeSend : function(data) {
			},
			error : function(jq, status, errorMsg) {
				alert("Status: " + status + " Error: " + errorMsg);
			},
			complete : function(jq, status) {
			}
		});
	})

});