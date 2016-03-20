angular.module('mytasks', ['ngRoute'])
	.config(function($routeProvider) {
	    $routeProvider.when('/', {
	      templateUrl : 'partials/dashboard.html',
	      controller : 'home',
	    })
	    .otherwise({
        redirectTo: '/'
	    });
  })
  .controller('home', function($scope, $http) {
	  $http.get('/api/projects/').success(function(data) {
	    $scope.projects = data.projects;
	  });
  })
  .controller('navbar', function($scope, $location) { 
    $scope.isActive = function (viewLocation) {
    	console.log(viewLocation);
    	console.log($location.path());
        return viewLocation === $location.path();
    };
  });