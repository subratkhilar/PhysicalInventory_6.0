'use strict';

angular
		.module('inventoryApp')
		.controller(
				'ObsolescenceCtrl',
				function($scope, $timeout, $http, $filter, ngDialog, $window,$location, $rootScope) {

					$scope.recordsPerPage = 10;
					$scope.maxSize = 30;
					$scope.pageNo = 1;
					$scope.disDataDiv = true;
					
					$scope.fetchAllProcess = function() {
						$http.get("getAllObs").then(function(response) {
							$scope.skus = response.data;
							$scope.dispalyData($scope.recordsPerPage);
						});

					};
					$scope.fetchAllProcess();
					$scope.dispalyData = function(size) {
						if ($scope.skus.length == 0) {
							$scope.disDataDiv = false;

						}
						
						if ($scope.skus.length > 0) {

							$scope.searchResult = $scope.skus;
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
						$scope.skus = $scope.searchResult;
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
								$scope.skus = $scope.pagedSearchResult1;

								$scope.startIndex = (($scope.pageNo - 1)
										* $scope.recordsPerPage * 5);
								$scope.endIndex = $scope.startIndex
										+ $scope.recordsPerPage * 5;
								

							} else {
								$scope.searchResult = [];
								$scope.pagedSearchResult = [];
							}

						} else {
							$scope.fetchAllProcess();
						}

					}

					$scope.idSelectedObsolescence = null;
					$scope.setSelectedObsolescence = function(
							idSelectedObsolescence) {
						$scope.idSelectedObsolescence = idSelectedObsolescence;
					
					}
					$scope.searchPageChange = function(pagenum) {
						
						var begin = ((pagenum - 1) * $scope.recordsPerPage * 5), end = begin
								+ $scope.recordsPerPage * 5;
						$scope.pagedSearchResult = $scope.skus
								.slice(begin, end);
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

					$scope.exportData = function() {
						alasql(
								'SELECT * INTO XLSX("Obsolescence.xlsx",{headers:true}) FROM ?',
								[ $scope.searchResult ]);
					};

					$scope.tableExport = function() {
						var doc = new jsPDF();
						var employees = $scope.searchResult;
						employees.forEach(function(employee, i) {
							doc.text(20, 10 + (i * 10), "First Name: "
									+ employee.skuNo);
						});
						
						doc.saveAs('Test.pdf');
					};

					$scope.validateDate = function() {
						
						if ($scope.date) {
							$scope.isvalidDate = false;
						} else {
							$scope.isvalidDate = true;

						}
					}
					$scope.saveSkus = function(csv) {
						$scope.isvalidDate = false;
						if ($scope.date) {
							var skus = "";
							
							$scope.skus.forEach(function(s) {
								if (skus.indexOf(s.skuNo) > -1) {
									s.isChecked = true;
								}
							});
							$scope.csvSkuList = '';
						} else {
							$scope.isvalidDate = true;
							$scope.msg = "Please enter  date .";
						}
					};

					$scope.selectedData = [];
					$scope.selectAll = function(check) {
						if (check) {
							$scope.skus.forEach(function(s) {
								s.isChecked = true;
								$scope.selectedData.push(s.skuNo);
								$scope.removeCheck1 = true;
								$scope.removeCheck2 = false;
							});
							
						} else {
							$scope.skus.forEach(function(s) {
								s.isChecked = false;
								$scope.csvSkuList = '';
								$scope.selectedData = [];
							});
						}

					};

					$scope.removeAll = function(check) {
						if (check) {
							$scope.skus.forEach(function(s) {
								s.isChecked = false;
								$scope.csvSkuList = '';
							});
							$scope.removeCheck2 = true;
							$scope.removeCheck1 = false;
							var ss = $scope.removeCheck1;
						}

					};

					$scope.deleteObso = function() {
						$scope.selectedData = [];
						$scope.skus.forEach(function(s) {
							if (s.isChecked == true) {
								$scope.selectedData.push(s.skuNo);
							}
						});
						$http({
							url : 'deleteObso',
							method : 'POST',
							data : {
								skuNoList : $scope.selectedData
							}
						})
								.success(
										function(data, status) {
											 $scope.divHtmlFileUploadVar = '<div class="alert alert-success fade in"><strong>'
													+ data.message
													+ '</strong></div>';
											$scope.fetchAllProcess();
											
										})
								.error(
										function(data, status) {
											 $scope.divHtmlFileUploadVar = '<div class="alert alert-danger fade in"><strong>'
													+ data.message
													+ '</strong></div>';
											$scope.fetchAllProcess();
										});
						
					};

					// creating add pop up
					$scope.openCreateObsPop = function() {
						ngDialog.open({
							template : 'views/createObs.html',
							controller : 'ObsolescenceCtrl',
							className : 'ngdialog-theme-default'
						});

					}

					$scope.saveObsData = function() {

						$http({
							url : 'createObso',
							method : 'POST',
							data : {
								oId : $scope.oId,
								skuNo : $scope.skuNo
							}
						}).success(function(data, status) {
							
							$window.alert(data.message);
							$scope.closeSecond();
							$window.location.reload();

						}).error(function(data, status) {
							alert(data.message);
						});

					}

					$scope.closeSecond = function() {
						ngDialog.close();
					};
					$scope.uploadFileToUrl = function(file, uploadUrl) {
						$rootScope.loading = true;
						var fd = new FormData();
						fd.append('file', file);

						$http.post(uploadUrl, fd, {
							transformRequest : angular.identity,
							headers : {
								'Content-Type' : undefined
							}
						})

						.success(function(data, status) {
							 $scope.divHtmlFileUploadVar= '<div class="alert  alert-success fade in"><strong>'+data.message+'</strong></div>';
							 angular.element("input[type='file']").val(null);
							 $scope.fetchAllProcess();
							 $rootScope.loading = false;
							 
						}).error(function(data, status) {
							$scope.divHtmlFileUploadVar = '<div class="alert alert-danger fade in"><strong>'
								+ data.message
								+ '</strong></div>';
							
							 $rootScope.loading = false;
							
						});
					}

					$scope.uploadFile = function() {
						var file = $scope.myFile;
						if(file==null || file=='undefined'){
							$scope.divHtmlFileUploadVar = '<div class="alert alert-danger fade in"><strong>Please choose a file </strong></div>';
							return true;
						}
						var uploadUrl = "obsFileUpload";
						$scope.uploadFileToUrl(file, uploadUrl);
					};

				});

				angular.module('inventoryApp').directive('fileModel',
						[ '$parse', function($parse) {
							return {
								restrict : 'A',
								link : function(scope, element, attrs) {
									var model = $parse(attrs.fileModel);
									var modelSetter = model.assign;
				
									element.bind('change', function() {
										scope.$apply(function() {
											modelSetter(scope, element[0].files[0]);
										});
									});
								}
							};
						} ]);