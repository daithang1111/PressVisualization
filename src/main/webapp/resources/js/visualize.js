var svgHeight = 500;
var svgWidth = 1000;
var padding = 100;
var uDocContent = "N/A";
var uTopicContent = "N/A";
$(document).ready(function() {
	$.ajax({
		url : ctx + '/getPressData',
		data : {
			senatorName : senatorName
		},
		dataType : "json",
		type : 'get',
		async : true,
		success : function(data) {
			visualizePressData(data);
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

function visualizePressData(data) {
	// define 'div' for tooltips
	var div = d3.select("body").append("div").attr("class", "tooltip").style(
			"opacity", 0);

	var margin = {
		top : 20,
		right : 200,
		bottom : 30,
		left : 260
	}, width = svgWidth - margin.left - margin.right, height = svgHeight
			- margin.top - margin.bottom;
	// Dynamic, random dataset
	var dataset = interpolateData(data);

	var maxX = d3.max(dataset, function(d) {
		return d.doc;
	});

	var maxP = d3.max(dataset, function(d) {
		return d.prop;
	});

	var xScale = d3.scale.linear();
	var yScale = d3.scale.ordinal().rangePoints(
			[ svgHeight - margin.bottom, margin.top ]);

	var rScale = d3.scale.linear();

	xScale.domain([ 1, maxX ]).rangeRound(
			[ margin.left, svgWidth - margin.right ]);
	yScale.domain(dataset.map(function(d) {
		return d.topic;
	}));

	rScale.domain([ 0, maxP ]).rangeRound([ 2, 10 ]);

	var xAxis = d3.svg.axis().scale(xScale).orient("bottom").ticks(10);
	var yAxis = d3.svg.axis().scale(yScale).orient("left").ticks(20);

	var svg = d3.select("body").append("svg");

	svg.attr("width", svgWidth).attr("height", svgHeight);
	// .attr("transform",
	// "translate(" + 150+ ",-50)");

	// measure line
	// define the measure line
	var measureLine = d3.svg.line().defined(function(d) {
		return d.measure != null;
	}).x(function(d) {
		return xScale(d.line);
	}).y(function(d) {
		return yScale(d.measure);
	});

	var uniqTopics = [];

	$.each(dataset, function(index, d) {

		if ($.inArray(d.topic, uniqTopics) === -1) {
			uniqTopics.push(d.topic);
			var measure = d.topic;
			var lineData = [ {
				"line" : 1,
				"measure" : measure
			}, {
				"line" : maxX,
				"measure" : measure
			} ];

			svg.append("path").attr("class", "measureLine").attr("d",
					measureLine(lineData));
		}
	});

	var circles = svg.selectAll("circle").data(dataset).enter()
			.append("circle");

	circles.attr("cx", function(d) {
		return xScale(d.doc);
	}).attr("cy", function(d) {
		return yScale(d.topic);
	}).attr("r", function(d, i) {
		return rScale(d.prop);
	}).style("fill", "teal").on(
			"mouseover",
			function(d) {
				div.transition().duration(200).style("opacity", 100);

				getDoc(d.docId);
				div.html(d.docId + "<br>" + uDocContent).style("left",
						(d3.event.pageX) + "px").style("top",
						(d3.event.pageY - 28) + "px");
			}).on("mouseout", function(d) {
		div.transition().duration(500).style("opacity", 0);
	});

	svg.append("g").attr("class", "axis").attr("transform",
			"translate(0, " + (svgHeight - margin.bottom) + ")").call(xAxis).

	append("text").attr("class", "x label").attr("text-anchor", "end").attr(
			"x", svgWidth - margin.right).attr("y", -10).text("Document");

	svg.append("g").attr("class", "axis").attr("transform",
			"translate(" + margin.left + ",0)").call(yAxis).selectAll("text")
			.on(
					"mouseover",
					function(d) {
						div.transition().duration(200).style("opacity", 100);
						getTopic(d);
						div.html(d + "<br>" + uTopicContent).style("left",
								(d3.event.pageX) + "px").style("top",
								(d3.event.pageY - 28) + "px");
					}).on("mouseout", function(d) {
				div.transition().duration(500).style("opacity", 0);
			}).append("text").attr("class", "y label").attr("text-anchor",
					"end").attr("transform", "rotate(-90)").attr("y", 16).attr(
					"x", -margin.top).text("Topic");

}

function interpolateData(data) {
	return $.map(data, function(d) {
		var docIndex = parseInt(d.docIndex);
		// only show some documents
		if (docIndex % 5 == 0) {
			return {
				doc : parseInt(d.docIndex),
				topic : d.topicId,
				prop : parseFloat(d.prop),
				docId : d.docId
			};
		}
	});
}

function getDoc(docId) {

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
}

function getTopic(topicId) {
	$.ajax({
		url : ctx + '/getTopic',
		data : {
			topicId : topicId
		},
		dataType : "json",
		type : 'get',
		async : false,
		success : function(data) {
			uTopicContent = data.topicContent;
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
