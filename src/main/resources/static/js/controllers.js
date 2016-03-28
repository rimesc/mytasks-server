var myTasksControllers = angular.module('myTasksControllers', ['ngRoute', 'ngSanitize', 'ui.bootstrap', 'angularMoment'])

.controller('projectsController', function($scope, $http, $uibModal) {
	function loadProjects() {
		$http.get('/api/projects/').then(function(response) {
			$scope.projects = response.data.projects;
		});
	}
	function newProject() {
		var modalInstance = $uibModal.open({templateUrl: 'modals/new-project.html', controller: 'newProjectModalController'});
		modalInstance.result.then(function (project) {
			$scope.projects.push(project);
		});
	}

	$scope.newProject = newProject;
	loadProjects();
})

.controller('projectController', function($scope, $routeParams, $http, $location, $uibModal) {
	function loadProject() {
		$http.get('/api/projects/' + $routeParams.id).then(function(response) {
			$scope.messages = [];
			$scope.project = response.data;
		}, function(response) {
			if (response.status == 404) {
				$scope.error = {code: response.statusText, detail: 'The requested project could not be found.'};
			}
			else {
				$scope.error = {code: response.statusText};
			}
		});
	}
	function loadReadMe() {
		$http.get('/api/projects/' + $routeParams.id + "/readme").then(function(response) {
			$scope.readMe = response.data;
		}, function(response) {
			if (response.status == 404) {
				$scope.readMe = '';
			}
		});
	}
	function editProject() {
		$uibModal
			.open({templateUrl: 'modals/edit-project.html', controller: 'editProjectModalController', resolve: {project: function() { return $scope.project }}})
			.result.then(function (project) {
				$scope.project = project;
			});
	}
	function editReadMe() {
		$uibModal
			.open({templateUrl: 'modals/edit-readme.html', controller: 'editReadmeModalController', resolve: {project: function() { return $scope.project }, readMe: function() { return $scope.readMe }}})
			.result.then(function (readMe) {
				$scope.readMe = readMe;
			});
	}
	function deleteProject() {
		$uibModal
			.open({templateUrl: 'modals/delete-project.html', controller: 'deleteProjectModalController', size: 'sm', resolve: {project: function() { return $scope.project }}})
			.result.then(function () {
				$location.path('/projects');
			});
	}
	function newTask() {
		$uibModal
			.open({templateUrl: 'modals/new-task.html', controller: 'newTaskModalController', resolve: {projectId: function() { return $scope.project.id }}})
			.result.then(function (task) {
				$scope.project.numberOfOpenTasks += 1;
			});
	}

	$scope.editProject = editProject;
	$scope.editReadMe = editReadMe;
	$scope.deleteProject = deleteProject;
	$scope.newTask = newTask;
	$scope.error = {};
	loadProject();
	loadReadMe();
})

.controller('projectTasksController', function($scope, $http, $uibModal, $routeParams) {
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
			$scope.tasks = data.tasks;
		});
	};
	$scope.activateFilter = function(filter) {
		activeFilter = filter;
		loadTasks();
	}
	$scope.openNewTaskModal = function() {
		var modalInstance = $uibModal.open({templateUrl: 'modals/new-task.html', controller: 'newTaskModalController', resolve: {projectId: function() { return $scope.project.id; }}});
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

.controller('taskController', function($scope, $http, $uibModal, $routeParams, $location) {
	var stateLabels = {TO_DO: 'To do', IN_PROGRESS: 'In progress', ON_HOLD: 'On hold', DONE: 'Done'};
	var transitions = {'TO_DO': [{'label': 'Start work', 'target': 'IN_PROGRESS'}],
	                   'IN_PROGRESS': [{'label': 'Pause work', 'target': 'ON_HOLD'}, {'label': 'Done', 'target': 'DONE'}],
	                   'ON_HOLD': [{'label': 'Resume work', 'target': 'IN_PROGRESS'}]};
	$scope.label = function(state) { return stateLabels[state]; };
	$scope.doTransition = function(transition) {
		$http.post('/api/tasks/' + $scope.task.id, {state: transition.target}).then(function(response) {
			$scope.task = response.data;
			$scope.transitions = transitions[$scope.task.state];
		});
	}
	$scope.openEditTaskModal = function() {
		var modalInstance = $uibModal.open({templateUrl: 'modals/edit-task.html', controller: 'editTaskModalController', resolve: {task: function() {return $scope.task}}});
		modalInstance.result.then(function (task) {
			$scope.task = task;
			refreshReadMe();
		});
	};
	$scope.openDeleteTaskModal = function() {
		var modalInstance = $uibModal.open({templateUrl: 'modals/delete-task.html', controller: 'deleteTaskModalController', size: 'sm', resolve: {task: function() {return $scope.task}}});
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

// MODALS

.controller('newProjectModalController', function($scope, $uibModalInstance, $http) {
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
 
.controller('editProjectModalController', function($scope, $uibModalInstance, $http, project) {
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
 
.controller('editReadmeModalController', function($scope, $uibModalInstance, $http, project, readMe) {
	$scope.markdown = readMe.markdown;
	$scope.errors = {};

	$scope.submit = function() {
		// clean up the data - undefined means empty string in this context
		var markdown = angular.isDefined($scope.markdown) ? $scope.markdown : '';
		$http.post('/api/projects/' + project.id + '/readme', {markdown: markdown}).then(function(response) {
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
 
.controller('deleteProjectModalController', function($scope, $uibModalInstance, $http, project) {
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

.controller('newTaskModalController', function($scope, $uibModalInstance, $http, projectId) {
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
 
.controller('editTaskModalController', function($scope, $uibModalInstance, $http, task) {
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

.controller('deleteTaskModalController', function($scope, $uibModalInstance, $http, task) {
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

// NAVIGATION

.controller('navigationController', function($scope, $location) { 
	$scope.isActive = function (viewLocation) {
		return viewLocation === $location.path();
	};
});