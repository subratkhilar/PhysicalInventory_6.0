'use strict';


angular.module('inventoryApp')
  .controller('InventoryReportCtrl', function ($scope, $timeout , $http,$stateParams) {
	  var storeNo =$stateParams.storeNo;
	  $scope.fetchReport = function() {
	   $http.get("listOfRpt?storeNo="+storeNo).then(function(response){
    	$scope.reportlist = response.data;
        });
  };
  
  $scope.fetchReport();
  });