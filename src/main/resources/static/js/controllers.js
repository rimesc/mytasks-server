var myTasksControllers = angular.module('myTasksControllers', ['ngRoute', 'ngSanitize', 'ui.bootstrap', 'angularMoment'])

.controller('adminController', function($scope, $http) {
	function loadCurrentUser() {
		$http.get('/api/admin/users/current').then(function(response) {
			$scope.currentUser = response.data;
		}, function (response) {
			if (response.status == 403) {
				$scope.error = {code: response.statusText, detail: 'You do not have permission to access this page.'};
			}
			else {
				// TODO handle unauthorised
				$scope.error = {code: response.statusText};
			}
		});
	}
	function loadUsers() {
		$http.get('/api/admin/users/').then(function(response) {
			$scope.users = response.data.users;
		}, function (response) {
			if (response.status == 403) {
				$scope.error = {code: response.statusText, detail: 'You do not have permission to access this page.'};
			}
			else {
				// TODO handle unauthorised
				$scope.error = {code: response.statusText};
			}
		});
	}
	function isCurrentUser(user) {
		return $scope.currentUser.username == user.username;
	}
	function isAdmin(user) {
		return contains(user.authorities, 'ROLE_ADMIN');
	}
	function createUser() {
		if (angular.isDefined($scope.newUser.username)) {
			var authorities = ['ROLE_USER'];
			if ($scope.newUser.isAdmin) {
				authorities.push('ROLE_ADMIN');
			}
			$http
				.post('/api/admin/users/', {username: $scope.newUser.username, password: $scope.newUser.password, authorities: authorities})
				.then(function(response) {
					$scope.users.push(response.data);
					$scope.errors = {};
					$scope.newUser = {};
				}, function(response) {
					// TODO handle unauthorised
					$scope.errors = {};
					response.data.errors.forEach(function(error) {
						if ('field' in error) {
							$scope.errors[error.field] = error.message;
						}
					});
				});
		}
	}

	$scope.errors = {};
	$scope.newUser = {};
	$scope.isCurrentUser = isCurrentUser;
	$scope.isAdmin = isAdmin;
	$scope.createUser = createUser;
	loadCurrentUser();
	loadUsers();
})

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
			$scope.error = undefined;
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
				$scope.readMe = {html: ''};
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
	loadProject();
	loadReadMe();
})

.controller('projectTasksController', function($scope, $http, $uibModal, $routeParams) {
	var filterStates = {'OPEN': ['TO_DO', 'IN_PROGRESS', 'ON_HOLD'], 'CLOSED': ['DONE']};
	var activeFilter = 'OPEN';
	function loadProject() {
		$http.get('/api/projects/' + $routeParams.id).then(function(response) {
			$scope.error = undefined;
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
	function loadTasks() {
		var query = 'project=' + $routeParams.id;
		if (activeFilter in filterStates) {
			query += filterStates[activeFilter].map(function(state) { return '&state=' + state; }).join('');
		}
		$http.get('/api/tasks/?' + query).then(function(response) {
			$scope.tasks = response.data.tasks;
		});
	};
	function isFilterActive(filter) {
		return activeFilter === filter;
	}
	function activateFilter(filter) {
		activeFilter = filter;
		loadTasks();
	}
	function newTask() {
		$uibModal
			.open({templateUrl: 'modals/new-task.html', controller: 'newTaskModalController', resolve: {projectId: function() { return $scope.project.id }}})
			.result.then(function (task) {
				$scope.tasks.push(task);
				$scope.project.numberOfOpenTasks += 1;
			});
	}
	
	$scope.isFilterActive = isFilterActive;
	$scope.activateFilter = activateFilter;
	$scope.newTask = newTask;
	loadProject();
	loadTasks();
})

.controller('taskController', function($scope, $http, $uibModal, $routeParams, $location) {
	var transitions = {
		'TO_DO': [{'label': 'Start work', 'target': 'IN_PROGRESS'}],
	    'IN_PROGRESS': [{'label': 'Pause work', 'target': 'ON_HOLD'}, {'label': 'Done', 'target': 'DONE'}],
	    'ON_HOLD': [{'label': 'Resume work', 'target': 'IN_PROGRESS'}],
	    'DONE': []};
	function loadTask() {
		$http.get('/api/tasks/' + $routeParams.id).then(function(response) {
			$scope.error = undefined;
			$scope.task = response.data;
			$scope.transitions = transitions[$scope.task.state];
		}, function(response) {
			if (response.status == 404) {
				$scope.error = {code: response.statusText, detail: 'The requested task could not be found.'};
			}
			else {
				$scope.error = {code: response.statusText};
			}
		});
	}
	function performTransition(transition) {
		$http.post('/api/tasks/' + $scope.task.id, {state: transition.target}).then(function(response) {
			$scope.task = response.data;
			$scope.transitions = transitions[$scope.task.state];
		});
	}
	function editTask() {
		$uibModal
			.open({templateUrl: 'modals/edit-task.html', controller: 'editTaskModalController', resolve: {task: function() {return $scope.task}}})
			.result.then(function (response) {
				$scope.task = response.data;
				refreshReadMe();
			});
	}
	function deleteTask() {
		$uibModal
			.open({templateUrl: 'modals/delete-task.html', controller: 'deleteTaskModalController', size: 'sm', resolve: {task: function() {return $scope.task}}})
			.result.then(function (response) {
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

	$scope.performTransition = performTransition;
	$scope.editTask = editTask;
	$scope.deleteTask = deleteTask;
	loadTask();
	refreshReadMe();
})

// MODALS

.controller('newProjectModalController', function($scope, $uibModalInstance, $http) {
	function submit() {
		$http
			.post('/api/projects/', {name: $scope.projectName, description: $scope.projectDescription})
			.then(function(response) {
				$uibModalInstance.close(response.data);
			}, function (response) {
				// TODO Handle unauthorized
				// TODO Handle global errors
				$scope.errors = {};
				response.data.errors.forEach(function(error) {
					if ('field' in error) {
						$scope.errors[error.field] = error.message;
					}
				});
			});
	}
	function cancel() {
		$uibModalInstance.dismiss('cancel');
	}

	$scope.projectName = "";
	$scope.projectDescription = "";
	$scope.errors = {};
	$scope.submit = submit;
	$scope.cancel = cancel;
})
 
.controller('editProjectModalController', function($scope, $uibModalInstance, $http, project) {
	function submit() {
		$http
			.post('/api/projects/' + project.id, {name: $scope.projectName, description: $scope.projectDescription})
			.then(function(response) {
				$uibModalInstance.close(response.data);
			}, function (response) {
				// TODO Handle unauthorized
				// TODO Handle global errors
				$scope.errors = {};
				response.data.errors.forEach(function(error) {
					if ('field' in error) {
						$scope.errors[error.field] = error.message;
					}
				});
			});
	}
	function cancel() {
		$uibModalInstance.dismiss('cancel');
	}

	$scope.projectName = project.name;
	$scope.projectDescription = project.description;
	$scope.errors = {};
	$scope.submit = submit;
	$scope.cancel = cancel;
})
 
.controller('editReadmeModalController', function($scope, $uibModalInstance, $http, project, readMe) {
	function submit() {
		// clean up the data - undefined means empty string in this context
		var markdown = angular.isDefined($scope.markdown) ? $scope.markdown : '';
		$http
			.post('/api/projects/' + project.id + '/readme', {markdown: markdown})
			.then(function(response) {
				$uibModalInstance.close(response.data);
			}, function (response) {
				// TODO Handle unauthorized
				// TODO Handle global errors
				$scope.errors = {};
				response.data.errors.forEach(function(error) {
					if ('field' in error) {
						$scope.errors[error.field] = error.message;
					}
				});
			});
	}
	function cancel() {
		$uibModalInstance.dismiss('cancel');
	}

	$scope.markdown = readMe.markdown;
	$scope.errors = {};
	$scope.submit = submit;
	$scope.cancel = cancel;
})
 
.controller('deleteProjectModalController', function($scope, $uibModalInstance, $http, project) {
	function submit() {
		$http
			.delete('/api/projects/' + project.id)
			.then(function(response) {
				$uibModalInstance.close();
			}, function (response) {
				// TODO Handle unauthorized
				// TODO Handle global errors
			});
		
	}
	function cancel() {
		$uibModalInstance.dismiss('cancel');
	}

	$scope.submit = submit;
	$scope.cancel = cancel;
})

.controller('newTaskModalController', function($scope, $uibModalInstance, $http, projectId) {
	function submit() {
		// clean up the data - undefined means empty string in this context
		var taskSummary = angular.isDefined($scope.taskSummary) ? $scope.taskSummary : '';
		var taskDescription = angular.isDefined($scope.taskDescription) ? $scope.taskDescription : '';
		$http
			.post('/api/tasks/', {project: projectId, summary: taskSummary, description: taskDescription, priority: $scope.taskPriority})
			.then(function(response) {
				$uibModalInstance.close(response.data);
			}, function (response) {
				// TODO Handle unauthorized
				// TODO Handle global errors
				$scope.errors = {};
				response.data.errors.forEach(function(error) {
					if ('field' in error) {
						$scope.errors[error.field] = error.message;
					}
				});
			});
	}
	function cancel() {
		$uibModalInstance.dismiss('cancel');
	}

	$scope.taskSummary = '';
	$scope.taskDescription = '';
	$scope.taskPriority = 'NORMAL';
	$scope.errors = {};
	$scope.submit = submit;
	$scope.cancel = cancel;
})
 
.controller('editTaskModalController', function($scope, $uibModalInstance, $http, task) {
	function submit() {
		// clean up the data - undefined means empty string in this context
		var taskSummary = angular.isDefined($scope.taskSummary) ? $scope.taskSummary : '';
		var taskDescription = angular.isDefined($scope.taskDescription) ? $scope.taskDescription : '';
		$http
			.post('/api/tasks/' + task.id, {summary: taskSummary, description: taskDescription, priority: $scope.taskPriority})
			.then(function(response) {
				$uibModalInstance.close(response.data);
			}, function (response) {
				// TODO Handle unauthorized
				// TODO Handle global errors
				$scope.errors = {};
				response.data.errors.forEach(function(error) {
					if ('field' in error) {
						$scope.errors[error.field] = error.message;
					}
				});
			});
	}
	function cancel() {
		$uibModalInstance.dismiss('cancel');
	}

	$scope.taskSummary = task.summary;
	$scope.taskDescription = task.description;
	$scope.taskPriority = task.priority;
	$scope.errors = {};
	$scope.submit = submit;
	$scope.cancel = cancel;
})

.controller('deleteTaskModalController', function($scope, $uibModalInstance, $http, task) {
	function submit() {
		$http
			.delete('/api/tasks/' + task.id)
			.then(function(response) {
				$uibModalInstance.close();
			}, function (response) {
				// TODO Handle unauthorized
				// TODO Handle global errors
			});
	}
	function cancel() {
		$uibModalInstance.dismiss('cancel');
	}

	$scope.submit = submit;
	$scope.cancel = cancel;
})

// NAVIGATION

.controller('navigationController', function($scope, $location, $http) {
	function isActive(viewLocation) {
		return viewLocation === $location.path();
	}
	$scope.isActive = isActive;
	$scope.showAdminOptions = false;
	$http.get('/api/admin/users/current').then(function(response) {
		$scope.showAdminOptions = contains(response.data.authorities, 'ROLE_ADMIN');
		console.log($scope.showAdminOptions);
	});
});