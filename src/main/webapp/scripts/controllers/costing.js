'use strict';


angular.module('inventoryApp')
  .controller('CostingCtrl', function ($scope, $timeout , $http,$filter) {
  $scope.recordsPerPage = 10;
  $scope.maxSize = 30;
  $scope.pageNo = 1;
  
  $scope.fetchAllProcess = function() {
    $http.get("getAllStores").then(function(response){
      $scope.stores = response.data;
      
       $scope.dispalyData($scope.recordsPerPage);

    });
  };
  
  
  
  $scope.fetchReport = function() {
	 
    $http.get("listOfRpt").then(function(response){
      $scope.reportlist = response.data;
       });
  };
  $scope.fetchReport();
  
  $scope.fetchAllProcess();
    $scope.idSelectedCosting = null;
	 $scope.setSelectedCosting = function(idSelectedCosting) {
	       $scope.idSelectedCosting = idSelectedCosting;
	    }

    $scope.dispalyData = function(size){
    
      if ($scope.stores.length > 0) {

      $scope.searchResult = $scope.stores;
      $scope.pagedSearchResult = $scope.searchResult.slice(0, $scope.recordsPerPage*5);
      $scope.startIndex = (($scope.pageNo - 1) * $scope.recordsPerPage * 5);
		$scope.endIndex  = $scope.startIndex + $scope.recordsPerPage * 5;
		console.log("hi:"+$scope.startIndex+":"+$scope.endIndex );
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
			$scope.fetchAllProcess();
		}
		
	}
    
    $scope.searchPageChange = function(pagenum) {
    var begin = ((pagenum - 1) * $scope.recordsPerPage*5), end = begin
        + $scope.recordsPerPage*5;
       $scope.pagedSearchResult = $scope.stores.slice(begin,
        end);
    $scope.startIndex = (($scope.pageNo - 1) * $scope.recordsPerPage * 5);
	$scope.endIndex  = $scope.startIndex + $scope.recordsPerPage * 5;
  };

    $scope.datepickerOptions = {
      formatYear: 'yy',
      maxDate: new Date(2020, 5, 22),
      minDate: new Date(),
      startingDay: 1
    };

    
    $scope.exportData = function () {
        alasql('SELECT * INTO XLSX("Costing.xlsx",{headers:true}) FROM ?',[$scope.stores]);
    };
    

    $scope.saveStores = function (csv) {
      var stores = csv.split(',');

        $scope.stores.forEach(function (s) {
          if (stores.indexOf(s.storeNo) != -1) {
            s.isChecked = true;
          }
        });
   
      $scope.csvStoreList = '';
    };

    $scope.selectedData = [];
    $scope.selectAll = function(check){
      if(check){
        $scope.stores.forEach(function (s) {
               s.isChecked = true;
               $scope.selectedData.push(s.storeNo);
               $scope.removeCheck1 = true;
               $scope.removeCheck2 = false;
         });

      }else{
         $scope.stores.forEach(function (s) {
               s.isChecked = false;
               $scope.csvStoreList = '';
               $scope.selectedData = [];
         });
      }

    };

    $scope.removeAll = function(check){
      if(check){
       $scope.stores.forEach(function (s) {
               s.isChecked = false;
               $scope.csvStoreList = '';
               $scope.removeCheck2 = true;
               $scope.removeCheck1 = false;	
         });
     }
     };
     $scope.validateDate = function(){
			if($scope.date){
				$scope.isvalidDate = false;
			}else{
				$scope.isvalidDate = true;
				
			}
		}

  });
