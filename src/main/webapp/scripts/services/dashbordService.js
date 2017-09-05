'use strict';


angular.module('inventoryApp')
  .service('DashBordService', function ($http, $q) {
    return {
    	
    	fetchAllProcess: function () {
    	 return $http({
          method: 'GET',
          url: 'tasks'
          }).then(function successCallback(response) {
               return response.data;
        }, function errorCallback(response) {
        	    return $q.reject(response);
        });
      }
    };

  });
