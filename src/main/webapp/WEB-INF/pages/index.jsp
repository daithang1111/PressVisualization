<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<!-- CSS -->
<link href="<c:url value="/resources/css/main.css" />" rel="stylesheet">

<!-- Java script -->
<script src="<c:url value="/resources/js/jquery-1.11.0.min.js" />"></script>

</head>
<body>
<h1>Welcome to my Press Visualization prototype</h1>
	<button id="goToPress" type="button">Click</button>
	<!-- context of this page -->
	<script>
		var ctx = "${pageContext.request.contextPath}"
	</script>
	<script src="<c:url value="/resources/js/main.js" />"></script>
</body>
</html>
