'use strict';

angular
		.module('inventoryApp')
		.controller(
				'ProcessCtrl',
				function($scope, $timeout, $http, $filter) {
					$scope.recordsPerPage = 10;
					$scope.maxSize = 30;
					$scope.pageNo = 1;

					$scope.fetchAllProcess = function() {
						$http.get("listOfReport").then(function(response) {
							$scope.stores = response.data;
							$scope.stores.forEach(function(s) {
								if (s.status) {
									s.isChecked = true;
								}
							});
							$scope.dispalyData($scope.recordsPerPage);
						});
					};
					$scope.fetchAllProcess();
					
					$scope.listOfReport = function() {
						$http.get("listOfReport").then(function(response) {
							$scope.stores = response.data;
							$scope.stores.forEach(function(s) {
								if (s.status) {
									s.isChecked = true;
								}
							});
							$scope.dispalyData($scope.recordsPerPage);
						});
					};
					$scope.listOfReport();

					$scope.idSelectedCosting = null;
					$scope.setSelectedCosting = function(idSelectedCosting) {
						$scope.idSelectedCosting = idSelectedCosting;
					};
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

					$scope.searchProcess = function() {
						$scope.stores = $scope.searchResult;
						$scope.pageNo = 1;
						$scope.startIndex = (($scope.pageNo - 1)
								* $scope.recordsPerPage * 5);
						$scope.endIndex = $scope.startIndex
								+ $scope.recordsPerPage * 5;
						if ($scope.searchItem != '') {
							$scope.pagedSearchResult1 = $filter('filter')(
									$scope.searchResult, $scope.searchItem);
							if ($scope.pagedSearchResult1.length > 0) {
								$scope.pagedSearchResult = $scope.pagedSearchResult1
										.slice(0, $scope.recordsPerPage * 5);
								$scope.stores = $scope.pagedSearchResult1;
							} else {
								$scope.searchResult = [];
								$scope.pagedSearchResult = [];
							}
						} else {
							$scope.fetchAllProcess();
						}
					}

					$scope.searchPageChange = function(pagenum) {
						var begin = ((pagenum - 1) * $scope.recordsPerPage * 5), end = begin
								+ $scope.recordsPerPage * 5;
						$scope.pagedSearchResult = $scope.stores.slice(begin,
								end);

						$scope.startIndex = (($scope.pageNo - 1)
								* $scope.recordsPerPage * 5);
						$scope.endIndex = $scope.startIndex
								+ $scope.recordsPerPage * 5;
					};

					$scope.datepickerOptions = {
						formatYear : 'yy',
						maxDate : new Date(2020, 5, 22),
						minDate : new Date(),
						startingDay : 1
					};

					$scope.selectedData = [];
					$scope.totalData = [];
					$scope.uncheckList = [];

					$scope.selectAll = function(check) {
						if (check) {
							$scope.stores.forEach(function(s) {
								s.isChecked = true;
								$scope.selectedData.push(s.storeNo);
								$scope.totalData.push(s.storeNo);
								$scope.removeCheck1 = true;
								$scope.removeCheck2 = false;
							});
						} else {
							$scope.stores.forEach(function(s) {
								s.isChecked = false;
								$scope.csvStoreList = '';
								$scope.selectedData = [];
								$scope.totalData.push(s.storeNo);
								$scope.uncheckList.push(s.storeNo);
							});
						}
					};
					
					$scope.removeAll = function(check) {
						if (check) {
							$scope.stores.forEach(function(s) {
								s.isChecked = false;
								$scope.csvStoreList = '';
								$scope.removeCheck2 = true;
								$scope.removeCheck1 = false;
								$scope.uncheckList.push(s.storeNo);

							});
						}
					};

					$scope.exportData = function() {
						alasql(
								'SELECT storeNo,status INTO XLSX("Process.xlsx",{headers:true}) FROM ?',
								[ $scope.stores ]);
					};

					Array.prototype.remove = function(set) {
						return this.filter(function(e, i, a) {
							return set.indexOf(e) < 0
						});
					};
					$scope.approve = function() {
						
						var processDate = $filter('date')($scope.date,'yyyy-MM-dd');
						$scope.selectedData = [];
						$scope.uncheckList = [];
						$scope.stores.forEach(function(s) {

							if (s.isChecked == true) {
								$scope.selectedData.push(s.storeNo);
							}

						});
						console.log($scope.selectedData);
						$http({
							url : 'approve',
							method : 'POST',
							data : {
								storeNoList : $scope.selectedData,
								processDate : processDate
							}
						}).success(function (data, status) {
							$scope.fetchAllProcess();
							$scope.success_option = data.message;	
							
						    alert(data.message);
			            })
			            .error(function (data, status) {
							$scope.selected_option = data.message;	 
			            	$scope.fetchAllProcess();
			            });
					
					};

					$scope.validateDate = function() {
						if ($scope.date) {
							$scope.isvalidDate = false;
						} else {
							$scope.isvalidDate = true;
						}
					}
					$scope.saveStores = function(csv) {
						var textAreaLists =[];
						textAreaLists = $scope.csvSkuList;
						var processDate = $filter('date')($scope.date,'yyyy-MM-dd');
						alert("processDate "+processDate);
						$scope.isvalidDate = false;
						if ($scope.date) {
							$scope.selectedData = [];
							$scope.uncheckList = [];
							$scope.stores.forEach(function(s) {
								$scope.totalData.push(s.storeNo);
								if (s.isChecked == true) {
									$scope.selectedData.push(s.storeNo);
								} else {
									$scope.uncheckList.push(s.storeNo);
								}
							});

							if (csv != null && csv.length > 0) {
								var stores = csv.split(',');
								$scope.selectedData = [];
								$scope.uncheckList = [];
								$scope.selectedData = $scope.totalData;
								$scope.selectedData = $scope.selectedData.remove(stores);
								$scope.uncheckList = stores;
								$scope.stores.forEach(function(s) {
									if (stores.indexOf(s.storeNo) != -1) {
										s.isChecked = false;
									}
								});
							}
							$scope.csvStoreList = '';
							$http({
								url : 'createStr',
								method : 'POST',
								data : {
									storeNoList : $scope.selectedData,
									uncheckList : $scope.uncheckList,
									processDate : processDate
									//textAreaList :textAreaLists
								}
							}).success(function (data, status) {
								
								$scope.success_option = data.message;	
								$scope.fetchAllProcess();
								 $scope.csvSkuList='';
							    //alert(data.message);
				            })
				            .error(function (data, status) {
								$scope.selected_option = data.message;	 
				            	$scope.fetchAllProcess();
				            });
						
						} else {
							$scope.isvalidDate = true;
							$scope.msg = "Please enter  date .";
						}
					};
				});
