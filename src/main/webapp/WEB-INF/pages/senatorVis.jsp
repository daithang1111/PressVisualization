<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<title>Senator and documents</title>
<!-- CSS -->
<link href="<c:url value="/resources/css/senatorVis.css" />"
	rel="stylesheet">


</head>
<body>
	<script>
		var ctx = "${pageContext.request.contextPath}";
		var senatorName = "${senatorName}";
		var algorithmName = "${algorithmName}";
	</script>

	<!-- Java script -->
	<script src="<c:url value="/resources/js/d3.v3.min.js" />"></script>
	<script src="<c:url value="/resources/js/jquery-1.11.0.min.js" />"></script>
	<script src="<c:url value="/resources/js/senatorVis.js" />"></script>
</body>
</html>