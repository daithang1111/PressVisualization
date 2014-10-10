var svgHeight = 500;
var svgWidth = 1000;
var padding = 100;
var MAX_SENATOR = 10;

var margin;
var width;
var height;
// Dynamic, random dataset
var xScale;
var yScale;

var rScale;
var xAxis;
var yAxis;

var svg;

$(document).ready(function() {

	var data = [];
	var numDataPoints = 100;
	var numTopics = 10;
	var numSenators = 100;
	var topicPerSenator = 5;
	for (var i = 0; i < numDataPoints; i++) {
		var senator = (i % numSenators) + 1;
		var sampleTopics = Math.floor(Math.random() * topicPerSenator) + 1; // 1->5

		for (var j = 0; j < sampleTopics; j++) {
			var topic = Math.floor(Math.random() * numTopics) + 1;
			var prop = Math.random();
			data.push([ senator, topic, prop ]);
		}
	}

	init(data,0);

	var startPoint = 0;
	 $("#moveSenator").click(function() {
		 startPoint++;
		 visualizePressData(data, startPoint);
	 })

});

function init(data,startPoint) {

	margin = {
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

	xScale = d3.scale.linear();
	yScale = d3.scale.ordinal().rangePoints(
			[ svgHeight - margin.bottom, margin.top ]);

	rScale = d3.scale.linear();

	xScale.domain([ minX, maxX ]).rangeRound(
			[ margin.left, svgWidth - margin.right ]);

	yScale.domain(dataset.sort(function(a, b) {
		return d3.ascending(a.topic, b.topic);
	}).map(function(d) {
		return d.topic;
	}));
	
	xAxis = d3.svg.axis().scale(xScale).orient("bottom").ticks(MAX_SENATOR);
	yAxis = d3.svg.axis().scale(yScale).orient("left");

	svg = d3.select("#graphicArea").append("svg");

	svg.attr("width", svgWidth).attr("height", svgHeight);

	svg.append("g").attr("class", "axis").attr("transform",
			"translate(0, " + (svgHeight - margin.bottom) + ")").call(xAxis)
			.append("text").attr("class", "x label").attr("text-anchor", "end")
			.attr("x", svgWidth - margin.right).attr("y", -10).text("Senator");

	svg.append("g").attr("class", "axis").attr("transform",
			"translate(" + margin.left + ",0)").call(yAxis).append("text")
			.attr("class", "y label").attr("text-anchor", "end").attr(
					"transform", "rotate(-90)").attr("y", 16).attr("x",
					-margin.top).text("Topic");

}

function visualizePressData(data, startPoint) {
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

	xScale.domain([ minX, maxX ]).rangeRound(
			[ margin.left, svgWidth - margin.right ]);

	yScale.domain(dataset.sort(function(a, b) {
		return d3.ascending(a.topic, b.topic);
	}).map(function(d) {
		return d.topic;
	}));
	rScale.domain([ 0, maxP ]).rangeRound([ 2, 10 ]);

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
	svg.selectAll(".measureLine").data([]).exit().remove();
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
	// clear svg
	svg.selectAll("circle").data([]).exit().remove();
	circles = svg.selectAll("circle").data(dataset).enter().append("circle");

	circles.attr("cx", function(d) {
		return xScale(d.senator);
	}).attr("cy", function(d) {
		return yScale(d.topic);
	}).attr("r", function(d, i) {
		return rScale(d.prop);
	}).style("fill", "teal");

	
	svg.select(".x.axis").call(xAxis);

	svg.select(".y.axis").call(yAxis);
}

function interpolateData(inputData, startPoint) {
	// var data = inputData.slice(startPoint, startPoint + MAX_SENATOR);

	return $.map(inputData, function(d) {
		if (d[0] < startPoint + MAX_SENATOR && d[0] >= startPoint) {
			return {
				senator : d[0],
				topic : d[1],
				prop : d[2]
			};
		}
	});

}
