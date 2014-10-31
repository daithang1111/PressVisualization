var cache = {};
var tooltip;
$(document).ready(
		function() {
			
			// for document search
			$("#showDocumentVis").click(function() {
				var selectedDocId = $("#documentId").val();
				showDocumentVis(selectedDocId);
			});
			// for doc search

			$("#documentId").autocomplete({
				delay : 0,
				minLength : 4,
				select : function(event, ui) {
					if (ui.item) {
						// do something with ui.item.value
						showDocumentVis(ui.item.value);
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
						url : ctx + '/getDocumentId',
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
									desc : item.description
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

function showDocumentVis(documentId) {
	$("#topicDistArea").html("");
	// show document content
//	$("#contentOfDocument").html(getDocumentContent(documentId));

	// visualize topic distribution for codebook, and seeded lda
	// first, get topics assigned by different algorithm
	$.ajax({
		url : ctx + '/getAlgorithmTopic',
		data : {
			docId : documentId
		},
		dataType : "json",
		type : 'get',
		async : false,
		success : function(data) {
			for (var t = 0; t < data.length; t++) {
				addVisualization(data[t].algorithmName, data[t].topicName,t);
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
	
	$("#topicDistArea").append("<div style='position:absolute;left:700px;text-align:left;'>"+getDocumentContent(documentId)+"</div>");

}

function addVisualization(algorithmName, topicName,aIndex) {

	var distWidth = 250;
	var distHeight = 400;
	var dmargin = {
		dtop : 60,
		dright : 20,
		dbottom : 20,
		dleft : 100
	}, dwidth = distWidth - dmargin.dleft - dmargin.dright, dheight = distHeight
			- dmargin.dtop - dmargin.dbottom;

	var svgDist = d3.select("#topicDistArea").append("svg")
			.attr("class", "dist").attr("width",
					dwidth + dmargin.dleft + dmargin.dright).attr("height",
					dheight + dmargin.dtop + dmargin.dbottom).style("left", (100+ (100+distWidth)*aIndex)+"px") // .style("top",
																				// 20)
			.append("g").attr("transform",
					"translate(" + dmargin.dleft + "," + dmargin.dtop + ")");

	dyScale = d3.scale.ordinal().rangeRoundBands([ dheight, 0 ], .1);

	dxScale = d3.scale.linear().range([ 0, dwidth ]);

	dxAxis = d3.svg.axis().scale(dxScale).orient("bottom").ticks(3);

	dyAxis = d3.svg.axis().scale(dyScale).orient("left");

	svgDist.append("g").attr("class", "dx axis").attr("transform",
			"translate(0," + dheight + ")").call(dxAxis);

	svgDist.append("g").attr("class", "dy axis").call(dyAxis);

	svgDist.append("text").attr("class", "dtitle").attr("x",
			(dwidth - dmargin.dleft) / 2).attr("y", 0 - (dmargin.dtop / 2))
			.attr("text-anchor", "middle").style("font-size", "14px").text(
					"Topic");
	
	//not good, but test for now
	tooltip = d3.select("body").append("div").attr("class",
	"tooltip").style("opacity", 0);
	
	$.ajax({
		url : ctx + '/getTopic',
		data : {
			topicName : topicName
		},
		dataType : "json",
		type : 'get',
		async : false,
		success : function(data) {
			// var uTopicContent = data.topicContent;
			// var topicWords = uTopicContent.split(" ");

			var word_prop_list = data.wp;

			word_prop_list.sort(function(a, b) {
				return d3.descending(a.word, b.word);
			});

			var topicData = [];
			var sum = 0;
			for (var i = 0; i < word_prop_list.length; i++) {
				// var p = Math.random();
				var p = parseFloat(word_prop_list[i].prop);
				sum += p;
				topicData.push([ word_prop_list[i].word, p ]);
			}

			var topicWordDist = $.map(topicData, function(d, i) {
				return {
					word : d[0],
					prop : d[1] / sum
				};
			});

			svgDist.style("display", "inline");

			// set title for dist
			svgDist.select(".dtitle").text(
					"Alg:" + algorithmName + ", Topic:" + topicName);

			// generate bar chart
			// create a new SVG for topic distribution
			dyScale.domain(topicWordDist.map(function(d) {
				return d.word;
			}));
			dxScale.domain([ 0, d3.max(topicWordDist, function(d) {
				return d.prop;
			}) ]);

			svgDist.selectAll(".bar").data([]).exit().remove();

			svgDist.selectAll("g.dx.axis").call(dxAxis);
			svgDist.selectAll("g.dy.axis").call(dyAxis);
			var bars = svgDist.selectAll(".bar").data(topicWordDist).enter()
					.append("rect").attr("class", "bar").attr("x", function(d) {
						return 0;
					}).attr("height", dyScale.rangeBand()).attr(
							"y",
							function(d) {
								return dheight - dyScale(d.word)
										- dyScale.rangeBand();
							}).attr("width", function(d) {
						return dxScale(d.prop);
					}).style("fill", "#ccc").on(
							"mouseover",
							function(d) {
								d3.select(this).style("fill", "red");

								tooltip.transition().duration(200).style(
										"opacity", 100);
								tooltip.html(d.prop).style("left",
										(d3.event.pageX) + "px").style("top",
										(d3.event.pageY - 28) + "px");

							}).on("mouseout", function(d) {
						d3.select(this).style("fill", "#ccc");
						tooltip.transition().duration(200).style("opacity", 0);
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
