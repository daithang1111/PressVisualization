var svgHeight = 500;
var svgWidth = 500;
var padding = 30;

$(document).ready(
		function($) {
			// Dynamic, random dataset
			var dataset = [];
			var numDataPoints = 50;
			var xRange = Math.random() * 1000;
			var yRange = Math.random() * 1000;
			for ( var i = 0; i < numDataPoints; i++) {
				var newNumber1 = Math.round(Math.random() * xRange);
				var newNumber2 = Math.round(Math.random() * yRange);
				dataset.push([ newNumber1, newNumber2 ]);
			}

			var maxX = d3.max(dataset, function(d) {
				return d[0];
			})

			var maxY = d3.max(dataset, function(d) {
				return d[1];
			})

			var xScale = d3.scale.linear();
			var yScale = d3.scale.linear();
			var rScale = d3.scale.linear();
			xScale.domain([ 0, maxX ]).rangeRound(
					[ padding, svgWidth - padding * 2 ]);
			yScale.domain([ 0, maxY ]).rangeRound(
					[ svgHeight - padding, padding ]);
			rScale.domain([ 0, maxY ]).rangeRound([ 2, 10 ]);

			var xAxis = d3.svg.axis().scale(xScale).orient("bottom").ticks(5);
			var yAxis = d3.svg.axis().scale(yScale).orient("left").ticks(5);

			var svg = d3.select("body").append("svg");

			svg.attr("width", svgWidth).attr("height", svgHeight);

			var circles = svg.selectAll("circle").data(dataset).enter().append(
					"circle");

			circles.transition().duration(3000).attr("cx", function(d) {
				return xScale(d[0]);
			}).attr("cy", function(d) {
				return yScale(d[1]);
			}).attr("r", function(d) {
				return rScale(d[1]);
			}).style("fill", "teal");

			svg.append("g").attr("class", "axis").attr("transform",
					"translate(0, " + (svgHeight - padding) + ")").call(xAxis);

			svg.append("g").attr("class", "axis").attr("transform",
					"translate(" + padding + ",0)").call(yAxis);

		});
