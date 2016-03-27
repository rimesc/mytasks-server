angular.module('mytasks', ['ngRoute', 'ngSanitize', 'ui.bootstrap', 'angularMoment'])

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
	.when('/projects/:id', {
		templateUrl : 'partials/project-view.html',
		controller : 'project',
	})
	.when('/projects/:id/tasks', {
		templateUrl : 'partials/task-list.html',
		controller : 'tasks',
	})
	.when('/tasks/:id', {
		templateUrl : 'partials/task-view.html',
		controller : 'task',
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
 
.controller('edit-project', function($scope, $uibModalInstance, $http, project) {
	$scope.projectName = project.name;
	$scope.projectDescription = project.description;
	$scope.errors = {};

	$scope.submit = function() {
		$http.post('/api/projects/' + project.id, {name: $scope.projectName, description: $scope.projectDescription}).then(function(response) {
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
 
.controller('delete-project', function($scope, $uibModalInstance, $http, project) {
	$scope.submit = function() {
		$http.delete('/api/projects/' + project.id).then(function(response) {
			$uibModalInstance.close();
		},
		function (response) {
			// TODO Handle unauthorized
			// TODO Handle global errors
		});
	};

	$scope.cancel = function () {
		$uibModalInstance.dismiss('cancel');
	};
})
 
.controller('project', function($scope, $http, $uibModal, $routeParams, $location) {
	$scope.isDefined = angular.isDefined;
	$scope.isUndefined = angular.isUndefined;
	$http.get('/api/projects/' + $routeParams.id).then(function(response) {
		$scope.project = response.data;
	}, function() {
		// TODO handle 404
	});
	$http.get('/api/projects/' + $routeParams.id + "/readme").then(function(response) {
		$scope.readMe = response.data;
	}, function() {
		$scope.readMe = undefined;
	});
	$scope.openEditProjectModal = function() {
		var modalInstance = $uibModal.open({templateUrl: 'modals/edit-project.html', controller: 'edit-project', resolve: {project: function() {return $scope.project}}});
		modalInstance.result.then(function (project) {
			$scope.project = project;
		});
	};
	$scope.openDeleteProjectModal = function() {
		var modalInstance = $uibModal.open({templateUrl: 'modals/delete-project.html', controller: 'delete-project', size: 'sm', resolve: {project: function() {return $scope.project}}});
		modalInstance.result.then(function () {
			$location.path('/projects');
		});
	};
	$scope.openNewTaskModal = function() {
		var modalInstance = $uibModal.open({templateUrl: 'modals/new-task.html', controller: 'new-task', resolve: {projectId: function() { return $scope.project.id; }}});
		modalInstance.result.then(function (task) {
			$scope.project.numberOfOpenTasks += 1;
		});
	};
})

.controller('tasks', function($scope, $http, $uibModal, $routeParams) {
	$scope.isDefined = angular.isDefined;
	$scope.isUndefined = angular.isUndefined;
	var filterStates = {'OPEN': ['TO_DO', 'IN_PROGRESS', 'ON_HOLD'], 'CLOSED': ['DONE']};
	var activeFilter = 'OPEN';
	$scope.isActive = function(filter) {
		return activeFilter === filter;
	}
	function loadTasks() {
		var query = 'project=' + $routeParams.id;
		if (activeFilter in filterStates) {
			query += filterStates[activeFilter].map(function(state) { return '&state=' + state; }).join('');
		}
		$http.get('/api/tasks/?' + query).success(function(data) {
			console.log(data.tasks);
			$scope.tasks = data.tasks;
		});
	};
	$scope.activateFilter = function(filter) {
		activeFilter = filter;
		loadTasks();
	}
	$scope.openNewTaskModal = function() {
		var modalInstance = $uibModal.open({templateUrl: 'modals/new-task.html', controller: 'new-task', resolve: {projectId: function() { return $scope.project.id; }}});
		modalInstance.result.then(function (task) {
			$scope.tasks.push(task);
		});
	};
	
	$http.get('/api/projects/' + $routeParams.id).then(function(response) {
		$scope.project = response.data;
	}, function() {
		// TODO handle 404, 403
	});

	loadTasks();
})

.controller('task', function($scope, $http, $uibModal, $routeParams, $location) {
	$scope.isDefined = angular.isDefined;
	$scope.isUndefined = angular.isUndefined;
	var stateLabels = {TO_DO: 'To do', IN_PROGRESS: 'In progress', ON_HOLD: 'On hold', DONE: 'Done'};
	var transitions = {'TO_DO': [{'label': 'Start work', 'target': 'IN_PROGRESS'}],
	                   'IN_PROGRESS': [{'label': 'Pause work', 'target': 'ON_HOLD'}, {'label': 'Done', 'target': 'DONE'}],
	                   'ON_HOLD': [{'label': 'Resume work', 'target': 'IN_PROGRESS'}]};
	$scope.label = function(state) { return stateLabels[state]; };
	$scope.doTransition = function(transition) {
		console.log({state: transition.target});
		$http.post('/api/tasks/' + $scope.task.id, {state: transition.target}).then(function(response) {
			$scope.task = response.data;
			$scope.transitions = transitions[$scope.task.state];
		});
	}
	$scope.openEditTaskModal = function() {
		var modalInstance = $uibModal.open({templateUrl: 'modals/edit-task.html', controller: 'edit-task', resolve: {task: function() {return $scope.task}}});
		modalInstance.result.then(function (task) {
			$scope.task = task;
			refreshReadMe();
		});
	};
	$scope.openDeleteTaskModal = function() {
		var modalInstance = $uibModal.open({templateUrl: 'modals/delete-task.html', controller: 'delete-task', size: 'sm', resolve: {task: function() {return $scope.task}}});
		modalInstance.result.then(function () {
			$location.path('/projects/' + $scope.task.project);
		});
	};
	
	function refreshReadMe() {
		$http.get('/api/tasks/' + $routeParams.id + "/readme").then(function(response) {
			$scope.readMe = response.data;
		}, function() {
			$scope.readMe = undefined;
		});
	}
	
	$http.get('/api/tasks/' + $routeParams.id).then(function(response) {
		$scope.task = response.data;
		$scope.transitions = transitions[$scope.task.state];
	}, function() {
		// TODO handle 404, 403
	});
	refreshReadMe();
})

.controller('new-task', function($scope, $uibModalInstance, $http, projectId) {
	$scope.taskSummary = '';
	$scope.taskDescription = '';
	$scope.taskPriority = 'NORMAL';
	$scope.priorities = ['CRITICAL', 'HIGH', 'NORMAL', 'LOW'];
	$scope.errors = {};

	$scope.submit = function() {
		// clean up the data - undefined means empty string in this context
		var taskSummary = angular.isDefined($scope.taskSummary) ? $scope.taskSummary : '';
		var taskDescription = angular.isDefined($scope.taskDescription) ? $scope.taskDescription : '';
		$http.post('/api/tasks/', {project: projectId, summary: taskSummary, description: taskDescription, priority: $scope.taskPriority}).then(function(response) {
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
 
.controller('edit-task', function($scope, $uibModalInstance, $http, task) {
	$scope.isDefined = angular.isDefined;
	$scope.isUndefined = angular.isUndefined;
	$scope.taskSummary = task.summary;
	$scope.taskDescription = task.description;
	$scope.taskPriority = task.priority;
	$scope.errors = {};
	$scope.priorities = ['CRITICAL', 'HIGH', 'NORMAL', 'LOW'];

	$scope.submit = function() {
		// clean up the data - undefined means empty string in this context
		var taskSummary = angular.isDefined($scope.taskSummary) ? $scope.taskSummary : '';
		var taskDescription = angular.isDefined($scope.taskDescription) ? $scope.taskDescription : '';
		$http.post('/api/tasks/' + task.id, {summary: taskSummary, description: taskDescription, priority: $scope.taskPriority}).then(function(response) {
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
 
.controller('delete-task', function($scope, $uibModalInstance, $http, task) {
	$scope.submit = function() {
		$http.delete('/api/tasks/' + task.id).then(function(response) {
			$uibModalInstance.close();
		},
		function (response) {
			// TODO Handle unauthorized
			// TODO Handle global errors
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