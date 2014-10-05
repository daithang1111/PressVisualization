<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<title>Visualize</title>
<!-- CSS -->
<link href="<c:url value="/resources/css/visualize.css" />"
	rel="stylesheet">

<!-- Java script -->
<script src="<c:url value="/resources/js/d3.v3.min.js" />"></script>
<script src="<c:url value="/resources/js/jquery-1.11.0.min.js" />"></script>
<script src="<c:url value="/resources/js/visualize.js" />"></script>
</head>
<body>
	<h1>${title}</h1>

	<div id="#visualArea">
	</div>
	<script>
		var ctx = "${pageContext.request.contextPath}";
		var senatorName = "${senatorName}";
	</script>
</body>
</html>