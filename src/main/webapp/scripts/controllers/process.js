'use strict';

angular
		.module('inventoryApp')
		.controller(
				'ProcessCtrl',
				function($scope, $timeout, $http, $filter, ngDialog, $window,
						$location,$rootScope) {
					$scope.recordsPerPage = 10;
					$scope.maxSize = 30;
					$scope.pageNo = 1;	
					$scope.disDataDiv = true;

					$scope.fetchAllProcess = function() {
						$http
								.get("listOsProcess")
								.then(
										function(response) {
											$scope.stores = response.data.storeList;
											if (response.data.processDate != null
													&& !response.data.processDate == "") {
												$scope.date = new Date(
														response.data.processDate);
											}
											$scope.stores.forEach(function(s) {
												if (s.status) {
													s.isChecked = true;
												}
											});
											$scope
													.dispalyData($scope.recordsPerPage);
										});
					};
					
					$scope.fetchAllProcess();
					$scope.idSelectedCosting = null;
					$scope.setSelectedCosting = function(idSelectedCosting) {
						$scope.idSelectedCosting = idSelectedCosting;
					};
					$scope.dispalyData = function(size) {
						if ($scope.stores.length == 0) {
							$scope.disDataDiv = false;
							// return true;
						}

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
						$scope.divHtmlVar = '';
						var processDate = $filter('date')($scope.date,
								'yyyy-MM-dd');
						$scope.selectedData = [];
						$scope.uncheckList = [];
						$scope.isvalidDate = false;
						if ($scope.date) {
							$rootScope.loading = true;
							$scope.stores.forEach(function(s) {

								if (s.isChecked == true) {
									$scope.selectedData.push(s.storeNo);
								}

							});
							
							$http({
								url : 'approve',
								method : 'POST',
								data : {
									storeNoList : $scope.selectedData,
									processDate : processDate
								}
							})
									.success(
											function(data, status) {
												$scope.fetchAllProcess();
												 $scope.divHtmlFileUploadVar = '<div class="alert alert-success fade in"><strong>'
														+ data.message
														+ '</strong></div>';
												$rootScope.loading = false;
												
											})
									.error(
											function(data, status) {
												 $scope.divHtmlFileUploadVar = ' <div class="alert alert-danger fade in"><span style="color: black !important; ">'
														+ data.message
														+ '</span></div>';
												$scope.fetchAllProcess();
												$rootScope.loading = false;
											});
						} else {
							$scope.isvalidDate = true;
							$scope.msg = "Please enter date";
						}

					};

					$scope.validateDate = function() {
						if ($scope.date) {
							$scope.isvalidDate = false;
						} else {
							$scope.isvalidDate = true;
						}
					};
					$scope.saveStores = function(csv) {
						$scope.divHtmlVar = '';
						var textAreaLists = [];
						textAreaLists = $scope.csvSkuList;
						var processDate = $filter('date')($scope.date,
								'yyyy-MM-dd');
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
								$scope.selectedData = $scope.selectedData
										.remove(stores);
								$scope.uncheckList = stores;
								$scope.stores.forEach(function(s) {
									if (stores.indexOf(s.storeNo) != -1) {
										s.isChecked = false;
									}
								});
							}
							$scope.csvStoreList = '';
							var flag = true;
							if ($scope.uncheckList.length > 0) {
								
								for ( var i = 0; i < $scope.uncheckList.length; i++) {
									var sno = $scope.uncheckList[i];
									if ($scope.totalData.indexOf(sno) == -1) {
										flag = false;
										$scope.msg = "invalid store number ."
												+ sno;
										 $scope.divHtmlFileUploadVar = ' <div class="alert alert-danger fade in"><span style="color: black !important; ">Invalid store number'
												+ sno + '</span></div>';
										break;
									}
								}
							}
							if (flag) {
								 $rootScope.loading = true;
								$http({
									url : 'createStr',
									method : 'POST',
									data : {
										storeNoList : $scope.selectedData,
										uncheckList : $scope.uncheckList,
										processDate : processDate
									}
								})
										.success(
												function(data, status) {
													 $scope.divHtmlFileUploadVar = '<div class="alert alert-success fade in"><strong>'
															+ data.message
															+ '</strong></div>';
													$scope.fetchAllProcess();
													$scope.csvSkuList = '';
													 $rootScope.loading = false;
												})
										.error(
												function(data, status) {
													 $scope.divHtmlFileUploadVar = ' <div class="alert alert-danger fade in"><span style="color: black !important; ">'
															+ data.message
															+ '</span></div>';
													$scope.fetchAllProcess();
													 $rootScope.loading = false;
												});
							}

						} else {
							$scope.isvalidDate = true;
							$scope.msg = "Please enter date";
						}
					};
					
					$scope.uploadFileToUrl = function(file, uploadUrl) {
						 $rootScope.loading = true;
						var fd = new FormData();
						fd.append('file', file);

						$http
								.post(uploadUrl, fd, {
									transformRequest : angular.identity,
									headers : {
										'Content-Type' : undefined
									}
								})
								.success(
										function(data, status) {
											$scope.divHtmlFileUploadVar = '<div class="alert alert-success fade in"><strong>'
													+ data.message
													+ '</strong></div>';
											angular.element("input[type='file']").val(null);
											$scope.fetchAllProcess();
											 $rootScope.loading = false;
									})
								.error(
										function(data, status) {
											$scope.divHtmlFileUploadVar = ' <div class="alert alert-danger fade in"><span style="color: black !important; ">'
													+ data.message
													+ '</span></div>';
											 $rootScope.loading = false;
										});
					}

					$scope.uploadFile = function() {
						var file = $scope.myFile;
						if (file == null || file == 'undefined') {
							$scope.divHtmlFileUploadVar = ' <div class="alert alert-danger fade in"><span style="color: black !important; ">Please choose a file </span></div>';
							return true;
						}
						var uploadUrl = "invFileUpload";
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
