/**
 * Global variable
 */
var svgWidth = 980;
var svgHeight = 500;
var FIX_MOVE = 30;
var MAX_DOCUMENT = 20; // show 20 senator at a time
var startDocPos = 0;
var stepSen = 20;// move

var leftRightWidth = 40;
var topBottomHeight = 40;
var distWidth = 160;
var c = d3.scale.category20();

var svg, svgLeft, svgTop, svgRight, svgBottom, svgDist;

var xScale, yScale, rScale, dxScal, dyScale;

var xAxis, yAxis, dxAxis, dyAxis;

var circles;
var lineFunction, leftLine, topLine, rightLine, bottomLine;

var dataset;
var allDataset;// store all data

var xDomain;// 20
var yDomain;// 100
var topicDomain;
var documentDomain;

var margin = {
	top : 80,
	right : 80,
	bottom : 100,
	left : 120
}, width = svgWidth - margin.left - margin.right, height = svgHeight
		- margin.top - margin.bottom;

var dmargin = {
	dtop : 60,
	dright : 20,
	dbottom : 20,
	dleft : 80
}, dwidth = distWidth - dmargin.dleft - dmargin.dright, dheight = svgHeight
		- dmargin.dtop - dmargin.dbottom;

// status variables
var selectedTopic = null;
var selectedDocument = null;
var uDocContent="";//store doc content

$(document).ready(function() {
	// 1. init svgs
	init();
	drawOtherSvgs();
	// 2. get data
	updateData();

	// 3. visualize()
	visualize();
});

/**
 * Methods
 */

function init() {

	// main svg
	svg = d3.select("body").append("svg").attr("class", "main").style("left",
			leftRightWidth).style("top", topBottomHeight).attr("width",
			svgWidth).attr("height", svgHeight).append("g").attr("transform",
			"translate(" + margin.left + "," + margin.top + ")");

	xScale = d3.scale.ordinal().rangePoints([ FIX_MOVE, FIX_MOVE + width ]);
	yScale = d3.scale.ordinal().rangePoints([ height + FIX_MOVE, FIX_MOVE ]);

	rScale = d3.scale.linear();

	xAxis = d3.svg.axis().scale(xScale).orient("top");
	yAxis = d3.svg.axis().scale(yScale).orient("left");

	svg.append("g").attr("class", "x axis").call(xAxis);

	svg.append("g").attr("class", "y axis").call(yAxis);

	// svg.selectAll("circle").data([1]).enter()
	// .append("circle");

	// dist svg
	svgDist = d3.select("body").append("svg").attr("class", "dist").attr(
			"width", dwidth + dmargin.dleft + dmargin.dright).attr("height",
			dheight + dmargin.dtop + dmargin.dbottom).style("left",
			2 * leftRightWidth + svgWidth).style("top", topBottomHeight)
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

	hideTopicDistribution();
	// tooltip
	// define 'div' for tooltips
	tooltip = d3.select("body").append("div").attr("class", "tooltip").style(
			"opacity", 0);

}
function drawOtherSvgs() {
	// line function
	lineFunction = d3.svg.line().x(function(d) {
		return d.x;
	}).y(function(d) {
		return d.y;
	}).interpolate("linear");
	// other svgs

	svgLeft = d3.select("body").append("svg").attr("class", "left").attr(
			"width", leftRightWidth).attr("height", svgHeight).style("left",
			"0").style("top", topBottomHeight);

	svgTop = d3.select("body").append("svg").attr("class", "top").attr("width",
			svgWidth).attr("height", topBottomHeight).style("left",
			leftRightWidth).style("top", "0");

	svgRight = d3.select("body").append("svg").attr("class", "right").attr(
			"width", leftRightWidth).attr("height", svgHeight).style("left",
			svgWidth + leftRightWidth).style("top", topBottomHeight);

	svgBottom = d3.select("body").append("svg").attr("class", "bottom").attr(
			"width", svgWidth).attr("height", topBottomHeight).style("left",
			leftRightWidth).style("top", topBottomHeight + svgHeight);

	// draw left arrow
	leftLine = [ {
		"x" : leftRightWidth,
		"y" : 0
	}, {
		"x" : 0,
		"y" : svgHeight / 2
	}, {
		"x" : leftRightWidth,
		"y" : svgHeight
	} ];

	svgLeft.append("path").attr("d", lineFunction(leftLine)).attr("stroke",
			"#ccc").attr("stroke-width", 1).attr("fill", "#ccc");

	// draw top arrow
	topLine = [ {
		"x" : 0,
		"y" : topBottomHeight
	}, {
		"x" : svgWidth / 2,
		"y" : 0
	}, {
		"x" : svgWidth,
		"y" : topBottomHeight
	} ];

	svgTop.append("path").attr("d", lineFunction(topLine)).attr("stroke",
			"#ccc").attr("stroke-width", 1).attr("fill", "#ccc");

	// draw right arrow
	rightLine = [ {
		"x" : 0,
		"y" : 0
	}, {
		"x" : leftRightWidth,
		"y" : svgHeight / 2
	}, {
		"x" : 0,
		"y" : svgHeight
	} ];

	svgRight.append("path").attr("d", lineFunction(rightLine)).attr("stroke",
			"#ccc").attr("stroke-width", 1).attr("fill", "#ccc");
	// draw bottom line
	bottomLine = [ {
		"x" : 0,
		"y" : 0
	}, {
		"x" : svgWidth / 2,
		"y" : topBottomHeight
	}, {
		"x" : svgWidth,
		"y" : 0
	} ];

	svgBottom.append("path").attr("d", lineFunction(bottomLine)).attr("stroke",
			"#ccc").attr("stroke-width", 1).attr("fill", "#ccc");

	/**
	 * SET ACTIONS ON ARROWS
	 */
	svgLeft.on("mouseover", function() {
		setcursor("pointer");
	}).on("mouseout", function() {
		setcursor("default");
	});

	svgTop.on("mouseover", function() {
		setcursor("pointer");
	}).on("mouseout", function() {
		setcursor("default");
	});

	svgRight.on("mouseover", function() {
		setcursor("pointer");
	}).on("mouseout", function() {
		setcursor("default");
	});

	svgBottom.on("mouseover", function() {
		setcursor("pointer");
	}).on("mouseout", function() {
		setcursor("default");
	});

	svgRight.on("click", function() {
		// TODO
	});

	svgLeft.on("click", function() {
		// TODO
	});

	svgTop.on("click", function() {
		sliceData(-stepSen);
		visualize();
	});

	svgBottom.on("click", function() {
		sliceData(stepSen);
		visualize();
	});

}

function setWidth(w) {
	svgWidth = w;
}
function setHeight(h) {
	svgHeight = h;
}
function resize(newH, newW) {
	setHeight(newH);
	setWidth(newW);

	svg.attr("width", svgWidth);
	svg.attr("height", svgHeight);
}

function setcursor(cursor) {
	d3.select("body").style("cursor", cursor);
}

function updateData() {
	$.ajax({
		url : ctx + '/getDocTopic',
		data : {
			senatorName: senatorName,
			algorithmName:algorithmName
		},
		dataType : "json",
		type : 'get',
		async : false,
		success : function(data) {
			interpolateData(data);
			sliceData(0);
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

function interpolateData(data) {
	allDataset = $.map(data, function(d) {
				return {
				doc : parseInt(d.docIndex),
				topic : d.topicName,
				topicId:d.topicId,
				prop : parseFloat(d.prop),
				docId : d.docId
			};
	});

	// topic domain

	topicDomain = d3.set(allDataset.sort(function(a, b) {
		return d3.ascending(a.topic, b.topic);
	}).map(function(d) {
		return d.topic;
	})).values();

	// doc domain
	documentDomain = d3.set(allDataset.sort(function(a, b) {
		return d3.ascending(a.doc, b.doc);
	}).map(function(d) {
		return d.doc;
	})).values();
}

function sliceData(step) {

	startDocPos += step;
	if (startDocPos < 0) {
		startDocPos = 0;
	}

	if (startDocPos > documentDomain.length - MAX_DOCUMENT) {

		startDocPos = documentDomain.length - MAX_DOCUMENT;
	}

	// There are two pivotal points, at startDocPos and at startDocPos
	// +MAX_DOCUMENT
	var endDocPos = startDocPos + MAX_DOCUMENT - 1;

	dataset = $.map(allDataset, function(d) {
		if (d.doc >= documentDomain[startDocPos]
				&& d.doc <= documentDomain[endDocPos]) {
			return d;
		}
	});

}

function visualize() {

	// 1. set domain for scales
//	xScale.domain(dataset.sort(function(a, b) {
//		return d3.ascending(a.topic, b.topic);
//	}).map(function(d) {
//		return d.topic;
//	}));

	xScale.domain(topicDomain); //always use the same domain
	xDomain = xScale.domain();

	yScale.domain(dataset.sort(function(a, b) {
		return d3.descending(a.doc, b.doc);
	}).map(function(d) {
		return d.doc;
	}));

	yDomain = yScale.domain();

	var rMin = d3.min(dataset, function(d) {
		return d.prop;
	});

	var rMax = d3.max(dataset, function(d) {
		return d.prop;
	});

	rScale.domain([ rMin, rMax ]).rangeRound([ 10, 20 ]);
	// 2. update xAxis, yAxis
	xAxis.tickFormat(function(d) {
		return d;
	});
	yAxis.tickFormat(function(d) {
		return "Doc "+d;
	});
	// 3. update svg with new axis

	// y axis
	svg.selectAll("g.y.axis").call(yAxis).selectAll("text").on(
			"mouseover",
			function(d) {
				selectedDocument = d;
				setcursor("pointer");
				setyAxisColor();
				// d3.select(this).style("fill", "red");
				tooltip.transition().duration(200).style("opacity", 100);
				tooltip.html("Click for more information about this document " + d)
						.style("left", (d3.event.pageX) + "px").style("top",
								(d3.event.pageY - 28) + "px");

				setCirclesColor();
				// svg.selectAll("circle").style("fill", function(dCircle, i) {
				// if (dCircle.senator == d) {
				// return "red";
				// } else {
				// return c(dCircle.topic);
				// }
				// });

			}).on("mouseout", function(d) {
		setcursor("default");
		// d3.select(this).style("fill", yDefaultColor());
		tooltip.transition().duration(200).style("opacity", 0);
		// svg.selectAll("circle").style("fill", function(dCircle, i) {
		// return c(dCircle.topic);
		// });

	}).on("click", function(d) {
		showDocumentInformation(d);
	});
	svg.selectAll("g.y.axis .tick").style("fill", function(d, i) {
		return yDefaultColor();
	});

	// x axis
	svg.selectAll("g.x.axis").call(xAxis).selectAll("text").style("font-size",
			"12px").style("text-anchor", "end").attr("dx", "40")
			.attr("dy", "0").attr("transform", function(d) {
				return "rotate(-50)"
			}).on("mouseover", function(d) {
				selectedTopic = d;
				setcursor("pointer");

				setxAxisColor();

				// d3.select(this).style("fill", "red");
				setCirclesColor();
				// svg.selectAll("circle").style("fill", function(dCircle, i) {
				// if (dCircle.topic == d) {
				// return "red";
				// } else {
				// return c(dCircle.topic);
				// }
				// });

				// show topic distribution
				showTopicDistribution(d);
			}).on("mouseout", function(d) {
				setcursor("default");
				// d3.select(this).style("fill", function(d) {
				// return c(d);
				// });
				tooltip.transition().duration(200).style("opacity", 0);
				// svg.selectAll("circle").style("fill", function(dCircle, i) {
				//
				// return c(dCircle.topic);
				// });
			});

	setxAxisColor();
	// 4. draw measure line

	d3.select("path .measureLine").remove();

	var measureLine = d3.svg.line().defined(function(d) {
		return d.measure != null;
	}).x(function(d) {
		return xScale(d.line);
	}).y(function(d) {
		return yScale(d.measure);
	});

	for (var i = 0; i < yDomain.length; i++) {

		var lineData = [ {
			"line" : xDomain[0],
			"measure" : yDomain[i]
		}, {
			"line" : xDomain[xDomain.length - 1],
			"measure" : yDomain[i]
		} ];

		svg.append("path").attr("class", "measureLine").attr("d",
				measureLine(lineData));
	}
	// 5. add circles
	svg.selectAll("circle").data([]).exit().remove();
	circles = svg.selectAll("circle").data(dataset).enter().append("circle");

	circles.attr("cx", function(d) {
		return xScale(d.topic);
	}).attr("cy", function(d) {
		return yScale(d.doc);
	}).attr("r", function(d, i) {
		return rScale(d.prop);
	}).style("fill", function(d, i) {
		return c(d.topic);
	}).style("fill-opacity", ".5").on(
			"mouseover",
			function(d) {
				tooltip.transition().duration(200).style("opacity", 100);
				getDoc(d.docId);
				setcursor("pointer");
				tooltip.html("<strong>"+ d.docId + "</strong><br>" + uDocContent).style("left",
						(d3.event.pageX) + "px").style("top",
						(d3.event.pageY - 28) + "px");
			}).on("mouseout", function(d) {
		setcursor("default");
		tooltip.transition().duration(500).style("opacity", 0);
	});

	// 6. show move actions
	svg.on("mousemove", function() {

		// console.log(d3.event);
		if (d3.event.pageY > (topBottomHeight + svgHeight / 2)) {
			// if (d3.event.pageY > svgHeight ) {
			$(".bottom").fadeIn(400);
			$(".top").fadeOut(400);

		} else {
			$(".top").fadeIn(400);
			$(".bottom").fadeOut(400);
		}

		if (d3.event.pageX > (leftRightWidth + svgWidth / 2)) {
			// if (d3.event.pageX > svgWidth) {
			$(".right").fadeIn(400);
			$(".left").fadeOut(400);
		} else {
			$(".left").fadeIn(400);
			$(".right").fadeOut(400);
		}

	});
}

function showDocumentInformation(senatorName) {
	// TODO
	// window.location = ctx + '/visualize?senatorName='+senatorName;
}

function hideTopicDistribution() {

	svgDist.style("display", "none");

}

function showTopicDistribution(topicName) {
	$.ajax({
		url : ctx + '/getTopic',
		data : {
			topicName : topicName
		},
		dataType : "json",
		type : 'get',
		async : false,
		success : function(data) {
//			var uTopicContent = data.topicContent;
//			var topicWords = uTopicContent.split(" ");
			
			var word_prop_list = data.wp;
						
			word_prop_list.sort(function(a, b) {
				return d3.descending(a.word, b.word);
			});
			
			var topicData = [];
			var sum = 0;
			for (var i = 0; i < word_prop_list.length; i++) {
//				var p = Math.random();
				var p =parseFloat(word_prop_list[i].prop);
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
			d3.select(".dtitle").text("Topic " + topicName);

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

function yDefaultColor() {
	return "#787878";
}

function setxAxisColor() {
	svg.selectAll("g.x.axis .tick text").style("fill", function(d) {
		if (d == selectedTopic) {
			return "red";
		} else {
			return c(d);
		}

	});

}

function setyAxisColor() {
	svg.selectAll("g.y.axis .tick text").style("fill", function(d) {
		if (d == selectedDocument) {
			return "red";
		} else {
			return yDefaultColor();
		}

	});

}

function setCirclesColor() {
	svg.selectAll("circle").style("fill", function(d, i) {
		if (d.topic == selectedTopic || d.doc == selectedDocument) {
			return "red";
		} else {
			return c(d.topic);
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