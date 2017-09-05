<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<title></title>
<meta name="description" content="">
<meta name="viewport" content="width=device-width">
<!-- bower:css -->
<link rel="stylesheet"
	href="bower_components/bootstrap/dist/css/bootstrap.css" />
<link rel="stylesheet"
	href="bower_components/angular-ui-grid/ui-grid.css" />
<link rel="stylesheet" type="text/css"
	href="styles/easyui/themes/default/easyui.css">

<link rel="stylesheet" href="styles/customCssNew.css" />
<!-- endbower -->
<link rel="stylesheet" href="bower_components/ngDialog/ngDialog.css">
<link rel="stylesheet"
	href="bower_components/ngDialog/ngDialog-theme-default.css">
<link rel="stylesheet"
	href="bower_components/ngDialog/ngDialog-theme-plain.css">
<link rel="stylesheet"
	href="bower_components/ngDialog/ngDialog-custom-width.css">
<!-- endbuild -->
<link rel="stylesheet" href="styles/main.css">
<link rel="stylesheet"
	href="http://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.6.3/css/font-awesome.min.css">

<!-- endbuild -->
</head>
<body ng-app="inventoryApp">


	<div class="" prevent-right-click >
		<div class="container-fluid"
			ng-hide="$state.includes('inventoryReport')">
			<div class="navbar-blue text-center clearfix"
				style="background-color: #005BB2 !important">
				<span class="pull-left"> <img src="images/logo.jpeg"
					alt="Logo"> </span> <span class="title">Physical Inventory</span>
			</div>
			<!-- Collect the nav links, forms, and other content for toggling -->
			<div class="text-center clearfix" id="navbarCollapse">
				<ul class="nav navbar-nav"
					style="margin-top: -1% !important; margin-bottom: -1% !important;">
					<li id="dashboardLi"
						style="padding-top: 13px; padding-bottom: 13px;"><a
						ui-sref="dashboard" style="font-size: 18px">Dashboard</a>
					</li>
				</ul>
			</div>
		</div>
		<!-- </nav> -->

		<div class="container-fluid">
			<div data-ng-if="loading" class="ajax_loader">
				<i class="fa fa-circle-o-notch fa-spin" style="font-size: 44px"></i><br>
				<span>Please wait...</span>

			</div>

			<div ui-view="" style="width: 100%; align: right;"></div>


			<div class="footer container"
				ng-hide="$state.includes('inventoryReport')">&copy; Copyright
				&copy; Sherwin Williams</div>

		</div>
	</div>


	<!-- Google Analytics: change UA-XXXXX-X to be your site's ID -->
	<script>
       !function(A,n,g,u,l,a,r){A.GoogleAnalyticsObject=l,A[l]=A[l]||function(){
       (A[l].q=A[l].q||[]).push(arguments)},A[l].l=+new Date,a=n.createElement(g),
       r=n.getElementsByTagName(g)[0],a.src=u,r.parentNode.insertBefore(a,r)
       }(window,document,'script','https://www.google-analytics.com/analytics.js','ga');

       ga('create', 'UA-XXXXX-X');
       ga('send', 'pageview');
    </script>
	<script>





</script>
	<!-- build:js(.) scripts/vendor.js -->
	<!-- bower:js -->

	<script src="bower_components/jquery/dist/jquery.js"></script>
	<script src="bower_components/angular/angular.js"></script>
	<script src="bower_components/dirPagination/dirPagination.js"></script>
	<script src="bower_components/bootstrap/dist/js/bootstrap.js"></script>
	<script
		src="bower_components/angular-ui-router/release/angular-ui-router.js"></script>
	<script src="bower_components/angular-bootstrap/ui-bootstrap-tpls.js"></script>
	<script src="bower_components/angular-ui-grid/ui-grid.js"></script>
	<script src="bower_components/alasql/alasql.min.js"></script>
	<script src="bower_components/alasql/xlsx.core.min.js"></script>
	<script src="bower_components/angular/angular-sanitize.js"></script>

	<script type="text/javascript" src="bower_components/tableExport.js"></script>
	<script type="text/javascript"
		src="bower_components/jspdf/libs/sprintf.js"></script>
	<script type="text/javascript" src="bower_components/jspdf/jspdf.js"></script>
	<script type="text/javascript"
		src="bower_components/jspdf/libs/base64.js"></script>
	<script type="text/javascript" src="bower_components/FileSaver.js"></script>
	<!-- endbower -->
	<!-- endbuild -->

	<!-- build:js({.tmp,app}) scripts/scripts.js -->
	<script src="scripts/app.js"></script>
	<script src="scripts/controllers/dashboard.js"></script>
	<script src="scripts/controllers/process.js"></script>
	<script src="scripts/controllers/obsolescence.js"></script>
	<script src="scripts/controllers/costing.js"></script>
	<script src="scripts/services/dashbordService.js"></script>
	<script src="scripts/controllers/report.js"></script>
	<script src="scripts/controllers/reportController.js"></script>
	<script src="bower_components/ngDialog/ngDialog.js"></script>
	<!-- endbuild -->
	<!-- <base href="/PhysicalInventory/index.jsp"/> -->
</body>
</html>
