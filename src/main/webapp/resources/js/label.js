var cache = {};
var tooltip;
$(document).ready(
		function() {
			tooltip = d3.select("body").append("div").attr("class", "tooltip")
					.style("opacity", 0);

			$("#showDocuments").click(function() {
				var selectedId = $("#senatorId").val();
				showDocuments(selectedId);
			});

			$("#senatorId").autocomplete({
				delay : 0,
				minLength : 1,
				select : function(event, ui) {
					if (ui.item) {
						// alert("Y");
						// do something with ui.item.value
						showDocuments(ui.item.value);
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
						url : ctx + '/getSenator',
						data : {
							searchTerm : request.term
						},
						dataType : "json",
						type : 'get',
						async : true,
						success : function(data) {
							var jsonArr = $.map(data, function(item) {
								return {
									value : item.id,
									desc : item.name
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

function showDocuments(senatorId) {

	$("#documentLabelArea").html("");
	$.ajax({
		url : ctx + '/getDocuments',
		data : {
			senatorId : senatorId
		},
		dataType : "json",
		type : 'get',
		async : false,
		success : function(data) {
			// alert(data.length);
			$("#documentLabelArea").html(genLabelTable(data));

			d3.selectAll(".summary").on(
					"mouseover",
					function() {

						tooltip.transition().duration(200)
								.style("opacity", 100);

						var selectedDocId = $(this).closest('tr').children(
								'td:eq(0)').text();
						// alert(getDocumentContent(selectedDocId));
						tooltip.html(getDocumentContent(selectedDocId)).style(
								"left", (d3.event.pageX) + "px").style("top",
								(d3.event.pageY - 28) + "px");

					}).on("mouseout", function() {
				tooltip.transition().duration(200).style("opacity", 0);

			});

			// TODO:
			$(".docLabel").mouseenter(
					function() {
						var selectedDocId = $(this).closest('tr').children(
								'td:eq(0)').text();
						$(this).append(
								"<div class ='allLabels'>"
										+ getAllLabels(selectedDocId)
										+ "</div>");

						$(this).mouseleave(function() {
							$(".allLabels").remove();
						});
					});

			// update doc label table
			$(".submitLabel").click(
					function() {
						var selectedDocId = $(this).closest('tr').children(
								'td:eq(0)').text();
						var newLabel = $("input[id='" + selectedDocId + "']")
								.val();
						updateLabel(selectedDocId, newLabel);
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

}
function getAllLabels(docId) {
	var htmlLabels = "";
	$.ajax({
		url : ctx + '/getAllLabels',
		data : {},
		dataType : "json",
		type : 'get',
		async : false,
		success : function(data) {

			for (var t = 0; t < data.length; t++) {
				htmlLabels += "<a href='javascript:;' onclick='updateLabel(\"" + docId
						+ "\",\"" + data[t] + "\");'>" + data[t] + "</a><br>";
			}
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
	return htmlLabels;
}
function updateLabel(docId, label) {
	
	$.ajax({
		url : ctx + '/updateLabel',
		data : {
			docId : docId,
			label : label
		},
		dataType : "json",
		type : 'get',
		async : false,
		success : function(data) {
			var selectedDocId = $("input[id='"+docId+"']").closest('tr').children(
			'td:eq(4)').html(label);
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

function genLabelTable(sDocuments) {
	var tableContent = "<table class='table table-striped'><thead><tr>"
			+ "<th>docid</th>" + "<th>summary</th>" + "<th>label</th>"
			+ "<th>submit</th><th>labeled?</th>" + "</tr></thead><tbody>";

	for (var i = 0; i < sDocuments.length; i++) {
		tableContent += "<tr>";

		tableContent += "<td>" + sDocuments[i].docId + "</td>";

		tableContent += "<td class='summary'>" + sDocuments[i].summary
				+ "</td>";

		tableContent += "<td class='docLabel'><label for='"
				+ sDocuments[i].docId + "'>Label: </label> <input id='"
				+ sDocuments[i].docId + "'></td>";

		tableContent += "<td><button type='button' class='btn submitLabel'>Submit</button></td><td></td>";

		tableContent += "</tr>";

	}
	tableContent += "</tbody></table>";

	return tableContent;

}

function getDocumentContent(docId) {
	var uDocContent = "";
	$.ajax({
		url : ctx + '/getDoc',
		data : {
			docId : docId
		},
		dataType : "json",
		type : 'get',
		async : false,
		success : function(data) {
			uDocContent = data.docContent;
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
	return uDocContent;
}
