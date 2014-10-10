<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html lang="en">
<head>
<link rel="shortcut icon"
	href="<c:url value="/resources/images/favicon.ico"/>">
<title>Press Visualization</title>
<!-- CSS -->

<link href="<c:url value="/resources/css/main.css"/>" rel="stylesheet">

</head>
<body>
	<script>
		var ctx = "${pageContext.request.contextPath}"
		var algorithmName =  "${algorithmName}";
	</script>

	<!-- Java script -->
	<script src="<c:url value="/resources/js/d3.v3.min.js"/>"></script>
	<script src="<c:url value="/resources/js/jquery-1.11.0.min.js"/>"></script>
	<script src="<c:url value="/resources/js/main.js"/>"></script>
</body>
</html>
