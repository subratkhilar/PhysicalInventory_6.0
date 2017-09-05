'use strict';

/**
 * @ngdoc overview
 * @name inventoryApp
 * @description
 * # inventoryApp
 *
 * Main module of the application.
 */
angular
  .module('inventoryApp', [


    //Third-Party modules
    'ui.router',
    'ui.bootstrap',

    'ui.grid',
    'ui.grid.pagination',
    'ngDialog',
    'ngSanitize'
  ])
  .config(function ($urlRouterProvider, $stateProvider) {

    $urlRouterProvider.otherwise('/dashboard');


    $stateProvider
      .state('dashboard', {
        url: '/dashboard',
        templateUrl: 'views/dashbord.html',
        controller: 'DashboardCtrl'
      })
      .state('process', {
        url: '/process',
        templateUrl: 'views/process.html',
        controller: 'ProcessCtrl'
      })
      .state('obsolescence', {
        url: '/obsolescence',
        templateUrl: 'views/obsolescence.html',
        controller: 'ObsolescenceCtrl'
      })
      .state('costing', {
        url: '/costing',
        templateUrl: 'views/costing.html',
        controller: 'CostingCtrl'
      })
      .state('inventoryReport', {
        url: '/inventoryReport/:storeNo',
        templateUrl: 'views/inventoryReport.html',
        controller: 'InventoryReportCtrl'
      })        
      .state('obsUploadprocess', {
        url: '/obsUploadprocess',
        templateUrl: 'views/obsUploadprocess.html',
        controller: 'ObsolescenceCtrl'
      })
      .state('storeReport', {
          url: '/storeReport',
          templateUrl: 'views/storeReport.html',
          controller: 'ReportCtrl'
       });
    
  })
  .run(function ($rootScope, $state){
    $rootScope.$state = $state
  });
//disable right click start
angular.module('inventoryApp').directive('preventRightClick', [
function() {
	return {
		restrict : 'A',
		link : function($scope, $ele) {
			$ele.bind("contextmenu", function(e) {
				e.preventDefault();
				//alert("Right click is disabled");
			});
		}
	};
} ])//end
