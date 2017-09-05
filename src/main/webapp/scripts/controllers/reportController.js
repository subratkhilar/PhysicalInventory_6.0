'use strict';


angular.module('inventoryApp')
  .controller('ReportCtrl', function ($scope, $timeout , $http, $filter) {
	  $scope.recordsPerPage = 10;
	  $scope.maxSize = 30;
	  $scope.pageNo = 1;
	  $scope.fetchAllReports = function() {
		$http.get("listOfReportForDashboard").then(function(response) {
			$scope.stores = response.data;
			$scope.stores.forEach(function(s) {
				if (s.status) {
					s.isChecked = true;
				}
			});
			$scope.dispalyData($scope.recordsPerPage);
		});
	};
	$scope.fetchAllReports();
	
	$scope.dispalyData = function(size) {
		
		if ($scope.stores.length > 0) {
			$scope.searchResult = $scope.stores;
			$scope.pagedSearchResult = $scope.searchResult
					.slice(0, $scope.recordsPerPage * 5);

			$scope.startIndex = (($scope.pageNo - 1)
					* $scope.recordsPerPage * 5);
			$scope.endIndex = $scope.startIndex
					+ $scope.recordsPerPage * 5;
		} else {
			$scope.searchResult = [];
			$scope.pagedSearchResult = [];
		}
	};
	$scope.searchProcess=function(){
		$scope.stores = $scope.searchResult;
		$scope.startIndex = (($scope.pageNo - 1) * $scope.recordsPerPage * 5);
		$scope.endIndex  = $scope.startIndex + $scope.recordsPerPage * 5;
		if($scope.searchItem != ''){
			$scope.pagedSearchResult1 = $filter('filter')($scope.searchResult,$scope.searchItem);
			if ($scope.pagedSearchResult1.length > 0) {
				$scope.pagedSearchResult = $scope.pagedSearchResult1
						.slice(0, $scope.recordsPerPage*5);
				$scope.stores = $scope.pagedSearchResult1;
			} else {
				$scope.searchResult = [];
				$scope.pagedSearchResult = [];
			}
			
		}else{
			$scope.fetchAllReports();
		}
	};
  });