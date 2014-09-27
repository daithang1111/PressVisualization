<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<!-- CSS -->
<link href="<c:url value="/resources/css/main.css" />" rel="stylesheet">

<!-- Java script -->
<script src="<c:url value="/resources/js/jquery-1.11.0.min.js" />"></script>
<script src="<c:url value="/resources/js/jquery.autocomplete.min.js" />"></script>



</head>
<body>
	<h2>Main</h2>

	<div>
		<input type="text" id="searchTerm" value=""> <span>
			<button id="search" type="button">Search</button>
		</span>
	</div>

	<!-- context of this page -->
	<script>
		var ctx = "${pageContext.request.contextPath}"
	</script>
	<script src="<c:url value="/resources/js/press.js" />"></script>
</body>
</html>
