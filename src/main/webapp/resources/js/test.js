$(document).ready(function() {
	
	var test =[];
	
	for(var i=0;i<10;i++){
		test.push([i%4,2*i]);
	}
	
		 var data = d3.nest()
		  .key(function(d) { return d[0];})
		  .rollup(function(d) { 
		   return d3.sum(d, function(g) {return g[1]; });
		  }).entries(test);
		
		 var map = {};
		 for (var i = 0; i < data.length; i++) {
		     map[data[i].key] = data[i].values;
		 }
		 
		 
		var finalData = $.map(test, function(d) {

				return {
					key : d[0],
					prop : d[1] / map[d[0]],
				};

			});
		
		alert(JSON.stringify(finalData));
		 
		 
		 
		 
//			alert(JSON.stringify(data));
});
