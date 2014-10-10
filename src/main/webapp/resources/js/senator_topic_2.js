var svgHeight = 500;
var svgWidth = 1000;
var padding = 100;
var MAX_SENATOR = 10;

$(document).ready(function() {

	var data = [];
	var numDataPoints = 500;
	var numTopics = 10;
	var numSenators = 100;

	for (var i = 0; i < numDataPoints; i++) {
		var senator = (i % numSenators) + 1;
		var topic = 0;
		if (i < numTopics) {
			topic = i + 1;
		} else {
			topic = Math.floor(Math.random() * numTopics) + 1;
		}
		var prop = Math.random();
		data.push([ senator, topic, prop ]);
	}

	var startPoint = 0;
	visualizePressData(data, startPoint);
	$("#moveSenator").click(function() {
		startPoint++;
		$("#graphicArea").html("");

		visualizePressData(data, startPoint);
	})

});

function visualizePressData(data, startPoint) {

	var margin = {
		top : 20,
		right : 200,
		bottom : 30,
		left : 260
	}, width = svgWidth - margin.left - margin.right, height = svgHeight
			- margin.top - margin.bottom;
	// Dynamic, random dataset
	var dataset = interpolateData(data, startPoint);

	var maxX = d3.max(dataset, function(d) {
		return d.senator;
	});

	var minX = d3.min(dataset, function(d) {
		return d.senator;
	});

	var maxP = d3.max(dataset, function(d) {
		return d.prop;
	});

	var xScale = d3.scale.linear();
	var yScale = d3.scale.ordinal().rangePoints(
			[ svgHeight - margin.bottom, margin.top ]);

	var rScale = d3.scale.linear();

	var c = d3.scale.category20c();
	
	xScale.domain([ minX, maxX ]).rangeRound(
			[ margin.left, svgWidth - margin.right ]);
	
	
	yScale.domain(dataset.sort(function(a, b) {
		return d3.ascending(a.topic,b.topic);
	}).map(function(d){
		return d.topic;
	}));

	rScale.domain([ 0, maxP ]).rangeRound([ 2, 10 ]);

	var xAxis = d3.svg.axis().scale(xScale).orient("bottom").ticks(MAX_SENATOR);
	var yAxis = d3.svg.axis().scale(yScale).orient("left");

	var svg = d3.select("#graphicArea").append("svg").on(
			"click", click);

	svg.attr("width", svgWidth).attr("height", svgHeight);

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
				"line" : minX,
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
		return xScale(d.senator);
	}).attr("cy", function(d) {
		return yScale(d.topic);
	}).attr("r", function(d, i) {
		return rScale(d.prop);
	}).style("fill", function(d,i) {
		return c(i);
	});

	svg.append("g").attr("class", "axis").attr("transform",
			"translate(0, " + (svgHeight - margin.bottom) + ")").call(xAxis)
			.append("text").attr("class", "x label").attr("text-anchor", "end")
			.attr("x", svgWidth - margin.right).attr("y", -10).text("Document");

	svg.append("g").attr("class", "axis").attr("transform",
			"translate(" + margin.left + ",0)").call(yAxis).append("text")
			.attr("class", "y label").attr("text-anchor", "end").attr(
					"transform", "rotate(-90)").attr("y", 16).attr("x",
					-margin.top).text("Topic");

	function click() {alert(maxX);
		xScale.domain([ minX+1, maxX-1 ]);
		var t = svg.transition().duration(750);
		t.select(".x.axis").call(xAxis);
	}
}

function interpolateData(inputData, startPoint) {
	var data = inputData.slice(startPoint, startPoint + MAX_SENATOR);

	return $.map(data, function(d) {

		return {
			senator : d[0],
			topic : d[1],
			prop : d[2]
		};

	});

}
