var myTasksControllers = angular.module('myTasksControllers', ['ngRoute', 'ngSanitize', 'ui.bootstrap', 'angularMoment', 'myTasksServices'])

.controller('adminController', function($scope, userService) {
	function loadCurrentUser() {
		userService.getCurrent(
			function(user) { $scope.currentUser = user },
			function (response) {
				switch (response.status) {
					case 403:
						$scope.error = {code: response.statusText, detail: 'You do not have permission to access this page.'};
						break;
					default:
						// TODO handle unauthorised
						$scope.error = {code: response.statusText};
				}
			});
	}
	function loadUsers() {
		userService.list(
			function(users) { $scope.users = users },
			function (response) {
				switch (response.status) {
					case 403:
						$scope.error = {code: response.statusText, detail: 'You do not have permission to access this page.'};
						break;
					default:
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
			userService.save({}, {username: $scope.newUser.username, password: $scope.newUser.password, authorities: authorities},
				function(user) {
					$scope.users.push(user);
					$scope.errors = {};
					$scope.newUser = {};
				},
				function(response) {
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

.controller('projectsController', function($scope, $uibModal, projectService) {
	function loadProjects() {
		projectService.list(function(projects) { $scope.projects = projects });
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

.controller('projectController', function($scope, $routeParams, $location, $uibModal, projectService) {
	function loadProject() {
		projectService.get({id: $routeParams.id},
			function(project) { $scope.project = project },
			function(response) {
				switch (response.status) {
					case 404: 
						$scope.error = {code: response.statusText, detail: 'The requested project could not be found.'};
						break;
					default:
						$scope.error = {code: response.statusText};
				}
			}
		);
	}
	function loadReadMe() {
		projectService.getReadMe({id: $routeParams.id},
			function(readMe) { $scope.readMe = readMe },
			function(response) {
				switch (response.status) {
					case 404:
						$scope.readMe = {html: ''};
						break;
					default:
						//TODO handle other errors
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

.controller('projectTasksController', function($scope, $uibModal, $routeParams, projectService, taskService) {
	var filterStates = {'OPEN': ['TO_DO', 'IN_PROGRESS', 'ON_HOLD'], 'CLOSED': ['DONE']};
	var activeFilter = 'OPEN';
	function loadProject() {
		projectService.get({id: $routeParams.id},
			function(project) { $scope.project = project },
			function(response) {
				switch (response.status) {
					case 404: 
						$scope.error = {code: response.statusText, detail: 'The requested project could not be found.'};
						break;
					default:
						$scope.error = {code: response.statusText};
				}
			});
	}
	function loadTasks() {
		taskService.query({project: $routeParams.id, state: filterStates[activeFilter]},
			function(tasks) { $scope.tasks = tasks }
		);
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

.controller('taskController', function($scope, $uibModal, $routeParams, $location, taskService) {
	var transitions = {
		'TO_DO': [{'label': 'Start work', 'target': 'IN_PROGRESS'}],
	    'IN_PROGRESS': [{'label': 'Pause work', 'target': 'ON_HOLD'}, {'label': 'Done', 'target': 'DONE'}],
	    'ON_HOLD': [{'label': 'Resume work', 'target': 'IN_PROGRESS'}],
	    'DONE': []};
	function loadTask() {
		taskService.get({id: $routeParams.id},
			function(task) { $scope.task = task; $scope.transitions = transitions[$scope.task.state] },
			function(response) {
				switch (response.status) {
					case 404:
						$scope.error = {code: response.statusText, detail: 'The requested task could not be found.'};
						break;
					default:
						$scope.error = {code: response.statusText};
				}
			});
	}
	function performTransition(transition) {
		taskService.save({id: $routeParams.id}, {state: transition.target},
			function(task) { $scope.task = task; $scope.transitions = transitions[$scope.task.state] }
		);
	}
	function editTask() {
		$uibModal
			.open({templateUrl: 'modals/edit-task.html', controller: 'editTaskModalController', resolve: {task: function() {return $scope.task}}})
			.result.then(function (task) {
				$scope.task = task;
				refreshReadMe();
			});
	}
	function deleteTask() {
		$uibModal
			.open({templateUrl: 'modals/delete-task.html', controller: 'deleteTaskModalController', size: 'sm', resolve: {task: function() {return $scope.task}}})
			.result.then(function () {
				$location.path('/projects/' + $scope.task.project);
			});
	};
	function refreshReadMe() {
		taskService.getReadMe({id: $routeParams.id},
				function(readMe) { $scope.readMe = readMe },
				function() { $scope.readMe = undefined }
		);
	}

	$scope.performTransition = performTransition;
	$scope.editTask = editTask;
	$scope.deleteTask = deleteTask;
	loadTask();
	refreshReadMe();
})

// MODALS

.controller('newProjectModalController', function($scope, $uibModalInstance, projectService) {
	function submit() {
		projectService.save({}, $scope.project,
			function(project) { $uibModalInstance.close(project) },
			function (response) {
				// TODO Handle unauthorized
				// TODO Handle global errors
				$scope.errors = [];
				response.data.errors.forEach(function(error) {
					if ('field' in error) {
						$scope.errors.push({message: 'Invalid ' + error.field, explanation: error.message});
					}
				});
			});
	}
	function cancel() {
		$uibModalInstance.dismiss('cancel');
	}

	$scope.project = {};
	$scope.submit = submit;
	$scope.cancel = cancel;
})
 
.controller('editProjectModalController', function($scope, $uibModalInstance, projectService, project) {
	function submit() {
		projectService.save({id: project.id}, $scope.project,
			function(project) { $uibModalInstance.close(project) },
			function (response) {
				// TODO Handle unauthorized
				// TODO Handle global errors
				$scope.errors = [];
				response.data.errors.forEach(function(error) {
					if ('field' in error) {
						$scope.errors.push({message: 'Invalid ' + error.field, explanation: error.message});
					}
				});
			});
	}
	function cancel() {
		$uibModalInstance.dismiss('cancel');
	}

	$scope.project = {name: project.name, description: project.description};
	$scope.errors = {};
	$scope.submit = submit;
	$scope.cancel = cancel;
})
 
.controller('editReadmeModalController', function($scope, $uibModalInstance, projectService, project, readMe) {
	function submit() {
		// clean up the data - undefined means empty string in this context
		var markdown = angular.isDefined($scope.markdown) ? $scope.markdown : '';
		projectService.saveReadMe({id: project.id}, {markdown: markdown},
			function(readMe) {
				$uibModalInstance.close(readMe);
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
 
.controller('deleteProjectModalController', function($scope, $uibModalInstance, projectService, project) {
	function submit() {
		projectService.delete({id: project.id},
			function() {
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

.controller('newTaskModalController', function($scope, $uibModalInstance, taskService, projectId) {
	function submit() {
		if (angular.isUndefined($scope.task.summary)) {
			$scope.task.summary = '';
		}
		taskService.save({}, $scope.task,
			function(task) {
				$uibModalInstance.close(task);
			},
			function (response) {
				// TODO Handle unauthorized
				// TODO Handle global errors
				$scope.errors = [];
				response.data.errors.forEach(function(error) {
					if ('field' in error) {
						$scope.errors.push({message: 'Invalid ' + error.field, explanation: error.message});
					}
				});
			});
	}
	function cancel() {
		$uibModalInstance.dismiss('cancel');
	}

	$scope.task = {project: projectId, priority: 'NORMAL'};
	$scope.submit = submit;
	$scope.cancel = cancel;
})
 
.controller('editTaskModalController', function($scope, $uibModalInstance, taskService, task) {
	function submit() {
		if (angular.isUndefined($scope.task.summary)) {
			$scope.task.summary = '';
		}
		taskService.save({id: task.id}, $scope.task,
			function(task) {
				$uibModalInstance.close(task);
			},
			function (response) {
				// TODO Handle unauthorized
				// TODO Handle global errors
				$scope.errors = [];
				response.data.errors.forEach(function(error) {
					if ('field' in error) {
						$scope.errors.push({message: 'Invalid ' + error.field, explanation: error.message});
					}
				});
			});
	}
	function cancel() {
		$uibModalInstance.dismiss('cancel');
	}

	$scope.task = {summary: task.summary, description: task.description, priority: task.priority, project: task.project};
	$scope.submit = submit;
	$scope.cancel = cancel;
})

.controller('deleteTaskModalController', function($scope, $uibModalInstance, taskService, task) {
	function submit() {
		taskService.delete({id: task.id},
			function() {
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

.controller('navigationController', function($scope, $location, userService) {
	function isActive(viewLocation) {
		return viewLocation === $location.path();
	}
	$scope.isActive = isActive;
	$scope.showAdminOptions = false;
	userService.getCurrent(function(user) { $scope.showAdminOptions = contains(user.authorities, 'ROLE_ADMIN') });
});