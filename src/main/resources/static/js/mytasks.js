angular.module('mytasks', ['ngRoute'])

.config(function($routeProvider) {
	$routeProvider
	.when('/', {
		templateUrl : 'partials/dashboard.html',
		controller : 'projects'
	})
	.when('/projects', {
		templateUrl : 'partials/project-list.html',
		controller : 'projects',
	})
	.otherwise({
		redirectTo: '/'
	});
})

.controller('projects', function($scope, $http) {
	$http.get('/api/projects/').success(function(data) {
		$scope.projects = data.projects;
	});
})
 
.controller('navbar', function($scope, $location) { 
	$scope.isActive = function (viewLocation) {
		return viewLocation === $location.path();
	};
});