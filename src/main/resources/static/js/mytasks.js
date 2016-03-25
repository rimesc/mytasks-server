angular.module('mytasks', ['ngRoute', 'ui.bootstrap'])

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

.controller('projects', function($scope, $http, $uibModal) {
	$http.get('/api/projects/').success(function(data) {
		$scope.projects = data.projects;
	});
	$scope.openNewProjectModal = function() {
		var modalInstance = $uibModal.open({templateUrl: 'modals/new-project.html', controller: 'new-project'});
		modalInstance.result.then(function (project) {
			$scope.projects.push(project);
		});
	};
})

.controller('new-project', function($scope, $uibModalInstance, $http) {
	$scope.projectName = "";
	$scope.projectDescription = "";
	$scope.errors = {};

	$scope.submit = function() {
		$http.post('/api/projects/', {name: $scope.projectName, description: $scope.projectDescription}).then(function(response) {
			$uibModalInstance.close(response.data);
		},
		function (response) {
			// TODO Handle unauthorized
			// TODO Handle global errors
			$scope.errors = {};
			response.data.errors.forEach(function(error) {
				if ('field' in error) {
					$scope.errors[error.field] = error.message;
				}
			});
		});
	};

	$scope.cancel = function () {
		$uibModalInstance.dismiss('cancel');
	};
})
 
.controller('navbar', function($scope, $location) { 
	$scope.isActive = function (viewLocation) {
		return viewLocation === $location.path();
	};
});