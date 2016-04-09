var mockModalInstance = {
	result : {
		then : function(onClose, onDismiss) {
			this.onClose = onClose;
			this.onDismiss = onDismiss;
		}
	},
	close : function(data) {
		if (this.result.onClose)
			this.result.onClose(data);
	},
	dismiss : function() {
		if (this.result.onDismiss)
			this.result.onDismiss();
	}
};
		
describe("myTasksControllers", function() {

	beforeEach(function() {
		jasmine.addCustomEqualityTester(function(first, second) { return angular.equals(first, second) });
	});

	beforeEach(module('myTasksApplication'));
	beforeEach(module('myTasksServices'));
	beforeEach(module('myTasksControllers'));
	beforeEach(module('myTasksComponents'));

	var $httpBackend, $controller;
	beforeEach(inject(function($injector) {
		$controller = $injector.get('$controller');
		$httpBackend = $injector.get('$httpBackend');
	}));

	afterEach(function() {
		$httpBackend.verifyNoOutstandingExpectation();
		$httpBackend.verifyNoOutstandingRequest();
	});
	
	describe("projectsController", function() {
		
		var projects = [
            {id : 1, name : 'My first project', description : 'This is my first sample project.', numberOfOpenTasks : 3, href : '/api/projects/1'},
            {id : 2, name : 'My second project', description : 'This is my second sample project.', numberOfOpenTasks : 2, href : '/api/projects/2'},
            {id : 3, name : 'My third project', description : 'This is my third sample project.', numberOfOpenTasks : 11, href : '/api/projects/3'}
    	];

		var $scope;

		beforeEach(function() {
			$scope = {};
			inject(function($uibModal) {
		        spyOn($uibModal, 'open').and.returnValue(mockModalInstance);
		    })
		});

		it("loads the list of projects from the REST API", function() {
			$httpBackend.expectGET('/api/projects/').respond(200, {'projects': projects});
			var controller = $controller('projectsController', {
				$scope : $scope
			});
			$httpBackend.flush();
			expect($scope.projects).toEqual(projects);
		});
		
		it("adds a new project to the list on confirm", function() {
			$httpBackend.expectGET('/api/projects/').respond(200, {'projects': projects});
			var controller = $controller('projectsController', {
				$scope : $scope
			});
			$httpBackend.flush();
			expect($scope.projects).toEqual(projects);
			$scope.newProject();
			var newProject = {"id": 4, "name": "My new project", "description": "This is a new project.", "numberOfOpenTasks": 0, "href": "/api/projects/4"};
			mockModalInstance.close(newProject);
			expect($scope.projects).toEqual(projects.concat([newProject]));
		});

		it("leaves the project list unchanged on cancel", function() {
			$httpBackend.expectGET('/api/projects/').respond(200, {'projects': projects});
			var controller = $controller('projectsController', {
				$scope : $scope
			});
			$httpBackend.flush();
			expect($scope.projects).toEqual(projects);
			$scope.newProject();
			mockModalInstance.dismiss();
			expect($scope.projects).toEqual(projects);
		});

	});

	describe("projectController", function() {

		var project = {id : 1, name : 'My first project', description : 'This is my first sample project.', numberOfOpenTasks : 3, href : '/api/projects/1'};
		var readMe = {markdown: '#My first project', html: '<h1>My first project</h1>'}

		var $scope, $routeParams, $location;

		beforeEach(function() {
			$scope = {};
			$routeParams = { id: '1' };
			inject(function($injector) {
				$location = $injector.get('$location');
			});
			inject(function($uibModal) {
		        spyOn($uibModal, 'open').and.returnValue(mockModalInstance);
		        spyOn($location, 'path');
		    });
		});

		it("loads the project from the REST API", function() {
			$httpBackend.expectGET('/api/projects/1').respond(200, project);
			$httpBackend.expectGET('/api/projects/1/readme').respond(404, { }, { }, 'Not Found');
			var controller = $controller('projectController', {
				$scope : $scope,
				$routeParams: $routeParams
			});
			$httpBackend.flush();
			expect($scope.project).toEqual(project);
			expect($scope.readMe.html).toEqual('');
			expect($scope.error).not.toBeDefined();
		});

		it("loads the project documentation from the REST API", function() {
			$httpBackend.expectGET('/api/projects/1').respond(200, project);
			$httpBackend.expectGET('/api/projects/1/readme').respond(200, readMe);
			var controller = $controller('projectController', {
				$scope : $scope,
				$routeParams: $routeParams
			});
			$httpBackend.flush();
			expect($scope.project).toEqual(project);
			expect($scope.readMe.html).toEqual(readMe.html);
			expect($scope.error).not.toBeDefined();
		});

		it("is in an error state if the project is not found", function() {
			$httpBackend.expectGET('/api/projects/1').respond(404, { }, { }, 'Not Found');
			$httpBackend.expectGET('/api/projects/1/readme').respond(404, { }, { }, 'Not Found');
			var controller = $controller('projectController', {
				$scope : $scope,
				$routeParams: $routeParams
			});
			$httpBackend.flush();
			expect($scope.project).not.toBeDefined();
			expect($scope.readMe.html).toEqual('');
			expect($scope.error).toEqual({code: 'Not Found', detail: 'The requested project could not be found.'});
		});

		it("opens the edit project modal and updates the project on confirm", function() {
			$httpBackend.expectGET('/api/projects/1').respond(200, project);
			$httpBackend.expectGET('/api/projects/1/readme').respond(404, { }, { }, 'Not Found');
			var controller = $controller('projectController', {
				$scope : $scope,
				$routeParams: $routeParams
			});
			$httpBackend.flush();
			expect($scope.project).toEqual(project);
			$scope.editProject();
			var updatedProject = {"id": 1, "name": "My edited project", "description": "This is an edited project.", "numberOfOpenTasks": 0, "href": "/api/projects/4"};
			mockModalInstance.close(updatedProject);
			expect($scope.project).toEqual(updatedProject);
		});

		it("opens the edit project modal and leaves the project unchanged on cancel", function() {
			$httpBackend.expectGET('/api/projects/1').respond(200, project);
			$httpBackend.expectGET('/api/projects/1/readme').respond(404, { }, { }, 'Not Found');
			var controller = $controller('projectController', {
				$scope : $scope,
				$routeParams: $routeParams
			});
			$httpBackend.flush();
			expect($scope.project).toEqual(project);
			$scope.editProject();
			mockModalInstance.dismiss();
			expect($scope.project).toEqual(project);
		});

		it("opens the edit documentation modal and updates the project documentation on confirm", function() {
			$httpBackend.expectGET('/api/projects/1').respond(200, project);
			$httpBackend.expectGET('/api/projects/1/readme').respond(200, readMe);
			var controller = $controller('projectController', {
				$scope : $scope,
				$routeParams: $routeParams
			});
			$httpBackend.flush();
			expect($scope.readMe.html).toEqual(readMe.html);
			$scope.editReadMe();
			var updatedReadMe = {markdown: '# My updated project', html: '<h1>My updated project</h1>'};
			mockModalInstance.close(updatedReadMe);
			expect($scope.readMe.html).toEqual(updatedReadMe.html);
		});

		it("opens the edit documentation modal and leaves the project documentation unchanged on cancel", function() {
			$httpBackend.expectGET('/api/projects/1').respond(200, project);
			$httpBackend.expectGET('/api/projects/1/readme').respond(200, readMe);
			var controller = $controller('projectController', {
				$scope : $scope,
				$routeParams: $routeParams
			});
			$httpBackend.flush();
			expect($scope.readMe.html).toEqual(readMe.html);
			$scope.editReadMe();
			mockModalInstance.dismiss();
			expect($scope.readMe.html).toEqual(readMe.html);
		});

		it("opens the new task modal and updates the number of tasks on confirm", function() {
			$httpBackend.expectGET('/api/projects/1').respond(200, project);
			$httpBackend.expectGET('/api/projects/1/readme').respond(404, { }, { }, 'Not Found');
			var controller = $controller('projectController', {
				$scope : $scope,
				$routeParams: $routeParams
			});
			$httpBackend.flush();
			expect($scope.project.numberOfOpenTasks).toEqual(project.numberOfOpenTasks);
			$scope.newTask();
			mockModalInstance.close();
			expect($scope.project.numberOfOpenTasks).toEqual(project.numberOfOpenTasks + 1);
			$scope.newTask();
			mockModalInstance.close();
			expect($scope.project.numberOfOpenTasks).toEqual(project.numberOfOpenTasks + 2);
		});

		it("opens the new task modal and leaves the number of tasks unchanged on cancel", function() {
			$httpBackend.expectGET('/api/projects/1').respond(200, project);
			$httpBackend.expectGET('/api/projects/1/readme').respond(404, { }, { }, 'Not Found');
			var controller = $controller('projectController', {
				$scope : $scope,
				$routeParams: $routeParams
			});
			$httpBackend.flush();
			expect($scope.project.numberOfOpenTasks).toEqual(project.numberOfOpenTasks);
			$scope.newTask();
			mockModalInstance.dismiss();
			expect($scope.project.numberOfOpenTasks).toEqual(project.numberOfOpenTasks);
		});

		it("opens the delete project modal and redirects to the projects page on confirm", function() {
			$httpBackend.expectGET('/api/projects/1').respond(200, project);
			$httpBackend.expectGET('/api/projects/1/readme').respond(404, { }, { }, 'Not Found');
			var controller = $controller('projectController', {
				$scope : $scope,
				$routeParams: $routeParams
			});
			$httpBackend.flush();
			$scope.deleteProject();
			mockModalInstance.close();
			expect($location.path.calls.any()).toBe(true);
			expect($location.path.calls.argsFor(0)).toEqual(['/projects']);
		});

		it("opens the delete project modal and does nothing on cancel", function() {
			$httpBackend.expectGET('/api/projects/1').respond(200, project);
			$httpBackend.expectGET('/api/projects/1/readme').respond(404, { }, { }, 'Not Found');
			var controller = $controller('projectController', {
				$scope : $scope,
				$routeParams: $routeParams
			});
			$httpBackend.flush();
			$scope.deleteProject();
			mockModalInstance.dismiss();
			expect($location.path.calls.any()).toBe(false);
		});

	});

	describe("projectTasksController", function() {

		var project = {id : 1, name : 'My first project', description : 'This is my first sample project.', numberOfOpenTasks : 3, href : '/api/projects/1'};
		var tasks = [
             {id: '1', summary: 'First sample task', description: 'This is the first sample task.', priority: 'HIGH', state: 'TO_DO', created: '2016-04-03T19:52:00Z', updated: '2016-04-03T19:52:00Z', project: '1', href: '/api/tasks/1'},
             {id: '2', summary: 'Second sample task', description: 'This is the second sample task.', priority: 'CRITICAL', state: 'DONE', created: '2016-04-03T19:52:00Z', updated: '2016-04-03T19:52:00Z', project: '1', href: '/api/tasks/2'},
             {id: '3', summary: 'Third sample task', description: 'This is the third sample task.', priority: 'LOW', state: 'TO_DO', created: '2016-04-03T19:52:00Z', updated: '2016-04-03T19:52:00Z', project: '1', href: '/api/tasks/3'}
		];
		var $scope, $routeParams;

		beforeEach(function() {
			$scope = {};
			$routeParams = { id: '1' };
			inject(function($uibModal) {
		        spyOn($uibModal, 'open').and.returnValue(mockModalInstance);
		    });
		});

		it("loads the list of tasks for the project from the REST API", function() {
			$httpBackend.expectGET('/api/projects/1').respond(200, project);
			$httpBackend.expectGET('/api/tasks/?project=1&state=TO_DO&state=IN_PROGRESS&state=ON_HOLD').respond(200, {'tasks': tasks});
			var controller = $controller('projectTasksController', {
				$scope : $scope,
				$routeParams : $routeParams
			});
			$httpBackend.flush();
			expect($scope.project).toEqual(project);
			expect($scope.tasks).toEqual(tasks);
			expect($scope.isFilterActive('OPEN')).toBe(true);
			expect($scope.isFilterActive('CLOSED')).toBe(false);
			expect($scope.isFilterActive('ALL')).toBe(false);
		});

		it("is in an error state if the project is not found", function() {
			$httpBackend.expectGET('/api/projects/1').respond(404, { }, { }, 'Not Found');
			$httpBackend.expectGET('/api/tasks/?project=1&state=TO_DO&state=IN_PROGRESS&state=ON_HOLD').respond(200, {'tasks': []});
			var controller = $controller('projectTasksController', {
				$scope : $scope,
				$routeParams : $routeParams
			});
			$httpBackend.flush();
			expect($scope.project).not.toBeDefined();
			expect($scope.error).toEqual({code: 'Not Found', detail: 'The requested project could not be found.'});
		});
		
		it("opens the new task modal and adds the new task to the list on confirm", function() {
			$httpBackend.expectGET('/api/projects/1').respond(200, project);
			$httpBackend.expectGET('/api/tasks/?project=1&state=TO_DO&state=IN_PROGRESS&state=ON_HOLD').respond(200, {'tasks': tasks});
			var controller = $controller('projectTasksController', {
				$scope : $scope,
				$routeParams: $routeParams
			});
			$httpBackend.flush();
			expect($scope.tasks).toEqual(tasks);
			$scope.newTask();
			var newTask = {id: '4', summary: 'New task', description: 'This is a new task.', priority: 'NORMAL', state: 'TO_DO', created: '2016-04-03T19:52:00Z', updated: '2016-04-03T19:52:00Z', project: '1', href: '/api/tasks/4'};
			mockModalInstance.close(newTask);
			expect($scope.tasks).toEqual(tasks.concat([newTask]));
		});

		it("opens the new task modal and leaves the task list unchanged on cancel", function() {
			$httpBackend.expectGET('/api/projects/1').respond(200, project);
			$httpBackend.expectGET('/api/tasks/?project=1&state=TO_DO&state=IN_PROGRESS&state=ON_HOLD').respond(200, {'tasks': tasks});
			var controller = $controller('projectTasksController', {
				$scope : $scope,
				$routeParams: $routeParams
			});
			$httpBackend.flush();
			expect($scope.tasks).toEqual(tasks);
			$scope.newTask();
			mockModalInstance.dismiss();
			expect($scope.tasks).toEqual(tasks);
		});

		it("re-loads the list of tasks when the closed filter is applied", function() {
			var openTasks = tasks.filter(function(task) { return task.state == 'TO_DO' });	
			var closedTasks = tasks.filter(function(task) { return task.state == 'DONE' });

			$httpBackend.expectGET('/api/projects/1').respond(200, project);
			$httpBackend.expectGET('/api/tasks/?project=1&state=TO_DO&state=IN_PROGRESS&state=ON_HOLD').respond(200, {'tasks': openTasks});
			var controller = $controller('projectTasksController', {
				$scope : $scope,
				$routeParams: $routeParams
			});
			$httpBackend.flush();
			expect($scope.tasks).toEqual(openTasks);
			expect($scope.isFilterActive('CLOSED')).toBe(false);
			$httpBackend.expectGET('/api/tasks/?project=1&state=DONE').respond(200, {'tasks': closedTasks});
			$scope.activateFilter('CLOSED');
			$httpBackend.flush();
			expect($scope.tasks).toEqual(closedTasks);
			expect($scope.isFilterActive('CLOSED')).toBe(true);
		});

		it("re-loads the list of tasks when the all filter is applied", function() {
			var openTasks = tasks.filter(function(task) { return task.state == 'TO_DO' });	
			var closedTasks = tasks.filter(function(task) { return task.state == 'DONE' });

			$httpBackend.expectGET('/api/projects/1').respond(200, project);
			$httpBackend.expectGET('/api/tasks/?project=1&state=TO_DO&state=IN_PROGRESS&state=ON_HOLD').respond(200, {'tasks': openTasks});
			var controller = $controller('projectTasksController', {
				$scope : $scope,
				$routeParams: $routeParams
			});
			$httpBackend.flush();
			expect($scope.tasks).toEqual(openTasks);
			expect($scope.isFilterActive('ALL')).toBe(false);
			$httpBackend.expectGET('/api/tasks/?project=1').respond(200, {'tasks': tasks});
			$scope.activateFilter('ALL');
			$httpBackend.flush();
			expect($scope.tasks).toEqual(tasks);
			expect($scope.isFilterActive('ALL')).toBe(true);
		});

	});

	describe("taskController", function() {

		var task = {id: '1', summary: 'First sample task', description: 'This is the first sample task.', priority: 'HIGH', state: 'TO_DO', created: '2016-04-03T19:52:00Z', updated: '2016-04-03T19:52:00Z', project: '1', href: '/api/tasks/1'};
		var readMe = {markdown: 'This is the first sample task.', html: '<p>This is the first sample task.</p>'}
		
		var $scope, $routeParams, $location;

		beforeEach(function() {
			$scope = {};
			$routeParams = { id: '1' };
			inject(function($injector) {
				$location = $injector.get('$location');
			});
			inject(function($uibModal) {
		        spyOn($uibModal, 'open').and.returnValue(mockModalInstance);
		        spyOn($location, 'path');
		    });
		});

		it("loads the task from the REST API", function() {
			$httpBackend.expectGET('/api/tasks/1').respond(200, task);
			$httpBackend.expectGET('/api/tasks/1/readme').respond(200, readMe);
			var controller = $controller('taskController', {
				$scope : $scope,
				$routeParams: $routeParams
			});
			$httpBackend.flush();
			expect($scope.task).toEqual(task);
			expect($scope.readMe.html).toEqual(readMe.html);
			expect($scope.error).not.toBeDefined();
		});

		it("is in an error state if the task is not found", function() {
			$httpBackend.expectGET('/api/tasks/1').respond(404, {}, {}, 'Not Found');
			$httpBackend.expectGET('/api/tasks/1/readme').respond(404, {}, {}, 'Not Found');
			var controller = $controller('taskController', {
				$scope : $scope,
				$routeParams: $routeParams
			});
			$httpBackend.flush();
			expect($scope.task).not.toBeDefined();
			expect($scope.readMe).not.toBeDefined();
			expect($scope.error).toEqual({code: 'Not Found', detail: 'The requested task could not be found.'});
		});

		it("opens the edit task modal and updates the task on confirm", function() {
			$httpBackend.expectGET('/api/tasks/1').respond(200, task);
			$httpBackend.expectGET('/api/tasks/1/readme').respond(200, readMe);
			var controller = $controller('taskController', {
				$scope : $scope,
				$routeParams: $routeParams
			});
			$httpBackend.flush();
			expect($scope.task).toEqual(task);
			$scope.editTask();
			var updatedTask = {id: '1', summary: 'Edited task', description: 'This is an edited task.', priority: 'LOW', state: 'TO_DO', created: '2016-04-03T19:52:00Z', updated: '2016-04-03T19:52:00Z', project: '1', href: '/api/tasks/1'};
			$httpBackend.expectGET('/api/tasks/1/readme').respond(200, readMe);
			mockModalInstance.close(updatedTask);
			$httpBackend.flush();
			expect($scope.task).toEqual(updatedTask);
		});

		it("opens the edit task modal and leaves the task unchanged on cancel", function() {
			$httpBackend.expectGET('/api/tasks/1').respond(200, task);
			$httpBackend.expectGET('/api/tasks/1/readme').respond(200, readMe);
			var controller = $controller('taskController', {
				$scope : $scope,
				$routeParams: $routeParams
			});
			$httpBackend.flush();
			expect($scope.task).toEqual(task);
			$scope.editTask();
			mockModalInstance.dismiss();
			expect($scope.task).toEqual(task);
		});

		it("transitions the task to IN_PROGRESS", function() {
			$httpBackend.expectGET('/api/tasks/1').respond(200, task);
			$httpBackend.expectGET('/api/tasks/1/readme').respond(200, readMe);
			var controller = $controller('taskController', {
				$scope : $scope,
				$routeParams: $routeParams
			});
			$httpBackend.flush();
			expect($scope.task.state).toEqual('TO_DO');
			expect($scope.transitions.length).toEqual(1);
			expect($scope.transitions[0].target).toEqual('IN_PROGRESS');
			$httpBackend
				.expectPOST('/api/tasks/1', { state: 'IN_PROGRESS' })
				.respond(function() {
					var updatedTask = angular.copy(task)
					updatedTask.state = 'IN_PROGRESS';
					return [200, updatedTask];
				});
			$scope.performTransition({target: 'IN_PROGRESS'});
			$httpBackend.flush();
			expect($scope.task.state).toEqual('IN_PROGRESS');
			expect($scope.transitions.length).toEqual(2);
			expect($scope.transitions[0].target).toEqual('ON_HOLD');
			expect($scope.transitions[1].target).toEqual('DONE');
		});

		it("transitions the task to ON_HOLD", function() {
			$httpBackend.expectGET('/api/tasks/1').respond(200, task);
			$httpBackend.expectGET('/api/tasks/1/readme').respond(200, readMe);
			var controller = $controller('taskController', {
				$scope : $scope,
				$routeParams: $routeParams
			});
			$httpBackend.flush();
			expect($scope.task.state).toEqual('TO_DO');
			expect($scope.transitions.length).toEqual(1);
			expect($scope.transitions[0].target).toEqual('IN_PROGRESS');
			$httpBackend
				.expectPOST('/api/tasks/1', { state: 'ON_HOLD' })
				.respond(function() {
					var updatedTask = angular.copy(task)
					updatedTask.state = 'ON_HOLD';
					return [200, updatedTask];
				});
			$scope.performTransition({target: 'ON_HOLD'});
			$httpBackend.flush();
			expect($scope.task.state).toEqual('ON_HOLD');
			expect($scope.transitions.length).toEqual(1);
			expect($scope.transitions[0].target).toEqual('IN_PROGRESS');
		});

		it("transitions the task to DONE", function() {
			$httpBackend.expectGET('/api/tasks/1').respond(200, task);
			$httpBackend.expectGET('/api/tasks/1/readme').respond(200, readMe);
			var controller = $controller('taskController', {
				$scope : $scope,
				$routeParams: $routeParams
			});
			$httpBackend.flush();
			expect($scope.task.state).toEqual('TO_DO');
			expect($scope.transitions.length).toEqual(1);
			expect($scope.transitions[0].target).toEqual('IN_PROGRESS');
			$httpBackend
				.expectPOST('/api/tasks/1', { state: 'DONE' })
				.respond(function() {
					var updatedTask = angular.copy(task)
					updatedTask.state = 'DONE';
					return [200, updatedTask];
				});
			$scope.performTransition({target: 'DONE'});
			$httpBackend.flush();
			expect($scope.task.state).toEqual('DONE');
			expect($scope.transitions.length).toEqual(0);
		});

		it("opens the delete task modal and redirects to the project page on confirm", function() {
			$httpBackend.expectGET('/api/tasks/1').respond(200, task);
			$httpBackend.expectGET('/api/tasks/1/readme').respond(200, readMe);
			var controller = $controller('taskController', {
				$scope : $scope,
				$routeParams: $routeParams
			});
			$httpBackend.flush();
			$scope.deleteTask();
			mockModalInstance.close();
			expect($location.path.calls.any()).toBe(true);
			expect($location.path.calls.argsFor(0)).toEqual(['/projects/1']);
		});

		it("opens the delete task modal and does nothing on cancel", function() {
			$httpBackend.expectGET('/api/tasks/1').respond(200, task);
			$httpBackend.expectGET('/api/tasks/1/readme').respond(200, readMe);
			var controller = $controller('taskController', {
				$scope : $scope,
				$routeParams: $routeParams
			});
			$httpBackend.flush();
			$scope.deleteTask();
			mockModalInstance.dismiss();
			expect($location.path.calls.any()).toBe(false);
		});

	});

	describe("newProjectModalController", function() {

		var $scope;

		beforeEach(function() {
			$scope = {};
		});

		it("handles field errors", function() {
			var errors = [
				{'field': 'name', 'code': 'invalid', 'message': 'invalid name'},
				{'field': 'description', 'code': 'invalid', 'message': 'invalid description'}
			];
			$httpBackend.expectPOST('/api/projects/').respond(400, {'errors': errors});
			var controller = $controller('newProjectModalController', {
				$scope: $scope,
				$uibModalInstance: undefined
			});
			$scope.submit();
			$httpBackend.flush();
			expect($scope.errors).toEqual([{message: 'Invalid name', explanation: 'invalid name'}, {message: 'Invalid description', explanation: 'invalid description'}]);
		});
		
		it("resets field errors on resubmission", function() {
			var nameError = {'field': 'name', 'code': 'invalid', 'message': 'invalid name'};
			var descriptionError = {'field': 'description', 'code': 'invalid', 'message': 'invalid description'}
			$httpBackend.expectPOST('/api/projects/').respond(400, {'errors': [nameError]});
			var controller = $controller('newProjectModalController', {
				$scope: $scope,
				$uibModalInstance: undefined
			});
			$scope.submit();
			$httpBackend.flush();
			expect($scope.errors).toEqual([{message: 'Invalid name', explanation: 'invalid name'}]);
			$httpBackend.expectPOST('/api/projects/').respond(400, {'errors': [descriptionError]});
			$scope.submit();
			$httpBackend.flush();
			expect($scope.errors).toEqual([{message: 'Invalid description', explanation: 'invalid description'}]);
		});
		
		it("returns the new project on success", function() {
			var createdProject;
			var newProject = {"id": 4, "name": "My new project", "description": "This is a new project.", "numberOfOpenTasks": 0, "href": "/api/projects/4"};
			$httpBackend.expectPOST('/api/projects/').respond(200, newProject);
			var controller = $controller('newProjectModalController', {
				$scope: $scope,
				$uibModalInstance: {
					close: function(data) {
						createdProject = data
					}
				}
			});
			$scope.submit();
			$httpBackend.flush();
			expect($scope.messages).toBeUndefined();
			expect(createdProject).toEqual(newProject);
		});
		
	});

	describe("editProjectModalController", function() {

		var project = {id : 1, name : 'My first project', description : 'This is my first sample project.', numberOfOpenTasks : 3, href : '/api/projects/1'};

		var $scope;

		beforeEach(function() {
			$scope = {};
		});

		it("handles field errors", function() {
			var errors = [
				{'field': 'name', 'code': 'invalid', 'message': 'invalid name'},
				{'field': 'description', 'code': 'invalid', 'message': 'invalid description'}
			];
			$httpBackend.expectPOST('/api/projects/1').respond(400, {'errors': errors});
			var controller = $controller('editProjectModalController', {
				$scope: $scope,
				$uibModalInstance: undefined,
				project: project
			});
			$scope.submit();
			$httpBackend.flush();
			expect($scope.errors).toEqual({'name': 'invalid name', 'description': 'invalid description'});
		});
		
		it("resets field errors on resubmission", function() {
			var nameError = {'field': 'name', 'code': 'invalid', 'message': 'invalid name'};
			var descriptionError = {'field': 'description', 'code': 'invalid', 'message': 'invalid description'}
			$httpBackend.expectPOST('/api/projects/1').respond(400, {'errors': [nameError]});
			var controller = $controller('editProjectModalController', {
				$scope: $scope,
				$uibModalInstance: undefined,
				project: project
			});
			$scope.submit();
			$httpBackend.flush();
			expect($scope.errors).toEqual({'name': 'invalid name'});
			$httpBackend.expectPOST('/api/projects/1').respond(400, {'errors': [descriptionError]});
			$scope.submit();
			$httpBackend.flush();
			expect($scope.errors).toEqual({'description': 'invalid description'});
		});
		
		it("returns the updated project on success", function() {
			var savedProject;
			var updatedProject = {"id": 1, "name": "My updated project", "description": "This is the updated project.", "numberOfOpenTasks": 0, "href": "/api/projects/1"};
			$httpBackend.expectPOST('/api/projects/1').respond(200, updatedProject);
			var controller = $controller('editProjectModalController', {
				$scope: $scope,
				$uibModalInstance: {
					close: function(data) {
						savedProject = data
					}
				},
				project: project
			});
			$scope.submit();
			$httpBackend.flush();
			expect($scope.errors).toEqual({});
			expect(savedProject).toEqual(updatedProject);
		});
		
	});
	
	describe('editReadmeModalController', function() {
		
		var savedReadMe;
		var project = {id : 1, name : 'My first project', description : 'This is my first sample project.', numberOfOpenTasks : 3, href : '/api/projects/1'};
		var readMe = {markdown: '# Some documentation'};
		
		var $scope;

		beforeEach(function() {
			$scope = {};
		});

		it("returns the updated readme on success", function() {
			var updatedReadMe = {markdown: '# Some updated documentation'};
			$httpBackend.expectPOST('/api/projects/1/readme').respond(200, updatedReadMe);
			var controller = $controller('editReadmeModalController', {
				$scope: $scope,
				$uibModalInstance: {
					close: function(data) {
						savedReadMe = data
					}
				},
				project: project,
				readMe: readMe
			});
			$scope.submit();
			$httpBackend.flush();
			expect(savedReadMe).toEqual(updatedReadMe);
		});
		
	});

	describe('deleteProjectModalController', function() {

		var project = {id : 1, name : 'My first project', description : 'This is my first sample project.', numberOfOpenTasks : 3, href : '/api/projects/1'};

		var $scope;

		beforeEach(function() {
			$scope = {};
		});

		it ('deletes the project on submit', function() {
			$httpBackend.expectDELETE('/api/projects/1').respond(200);
			var controller = $controller('deleteProjectModalController', {
				$scope: $scope,
				$uibModalInstance: { close: function(data) { } },
				project: project
			});
			$scope.submit();
			$httpBackend.flush();
		});

	});

	describe('newTaskModalController', function() {

		var $scope;

		beforeEach(function() {
			$scope = {};
		});

		it("handles field errors", function() {
			var errors = [
				{'field': 'summary', 'code': 'invalid', 'message': 'invalid summary'},
				{'field': 'description', 'code': 'invalid', 'message': 'invalid description'}
			];
			$httpBackend.expectPOST('/api/tasks/').respond(400, {'errors': errors});
			var controller = $controller('newTaskModalController', {
				$scope: $scope,
				$uibModalInstance: undefined,
				projectId: 1
			});
			$scope.submit();
			$httpBackend.flush();
			expect($scope.errors).toEqual({'summary': 'invalid summary', 'description': 'invalid description'});
		});
		
		it("resets field errors on resubmission", function() {
			var summaryError = {'field': 'summary', 'code': 'invalid', 'message': 'invalid summary'};
			var descriptionError = {'field': 'description', 'code': 'invalid', 'message': 'invalid description'}
			$httpBackend.expectPOST('/api/tasks/').respond(400, {'errors': [summaryError]});
			var controller = $controller('newTaskModalController', {
				$scope: $scope,
				$uibModalInstance: undefined,
				projectId: 1
			});
			$scope.submit();
			$httpBackend.flush();
			expect($scope.errors).toEqual({'summary': 'invalid summary'});
			$httpBackend.expectPOST('/api/tasks/').respond(400, {'errors': [descriptionError]});
			$scope.submit();
			$httpBackend.flush();
			expect($scope.errors).toEqual({'description': 'invalid description'});
		});
		
		it("treats undefined summary and description as empty", function() {
			var createdTask;
			var newTask = {"project": 1, "summary": "", "description": "", "priority": "HIGH"};
			$httpBackend.expectPOST('/api/tasks/', newTask).respond(200, newTask);
			var controller = $controller('newTaskModalController', {
				$scope: $scope,
				$uibModalInstance: {
					close: function(data) {
						createdTask = data
					}
				},
				projectId: 1
			});
			$scope.taskPriority = newTask.priority;
			$scope.submit();
			$httpBackend.flush();
			expect($scope.errors).toEqual({});
			expect(createdTask).toEqual(newTask);
		});
		
		it("returns the new task on success", function() {
			var createdTask;
			var newTask = {"project": 1, "summary": "A new task", "description": "This is a new task.", "priority": "HIGH"};
			$httpBackend.expectPOST('/api/tasks/', newTask).respond(200, newTask);
			var controller = $controller('newTaskModalController', {
				$scope: $scope,
				$uibModalInstance: {
					close: function(data) {
						createdTask = data
					}
				},
				projectId: 1
			});
			$scope.taskSummary = newTask.summary;
			$scope.taskDescription = newTask.description;
			$scope.taskPriority = newTask.priority;
			$scope.submit();
			$httpBackend.flush();
			expect($scope.errors).toEqual({});
			expect(createdTask).toEqual(newTask);
		});
		
	});

	describe('editTaskModalController', function() {

		var task = {id: '1', summary: 'First sample task', description: 'This is the first sample task.', priority: 'HIGH', state: 'TO_DO', created: '2016-04-03T19:52:00Z', updated: '2016-04-03T19:52:00Z', project: '1', href: '/api/tasks/1'};

		var $scope;

		beforeEach(function() {
			$scope = {};
		});

		it("handles field errors", function() {
			var errors = [
				{'field': 'summary', 'code': 'invalid', 'message': 'invalid summary'},
				{'field': 'description', 'code': 'invalid', 'message': 'invalid description'}
			];
			$httpBackend.expectPOST('/api/tasks/1').respond(400, {'errors': errors});
			var controller = $controller('editTaskModalController', {
				$scope: $scope,
				$uibModalInstance: undefined,
				task: task
			});
			$scope.submit();
			$httpBackend.flush();
			expect($scope.errors).toEqual({'summary': 'invalid summary', 'description': 'invalid description'});
		});
		
		it("resets field errors on resubmission", function() {
			var summaryError = {'field': 'summary', 'code': 'invalid', 'message': 'invalid summary'};
			var descriptionError = {'field': 'description', 'code': 'invalid', 'message': 'invalid description'}
			$httpBackend.expectPOST('/api/tasks/1').respond(400, {'errors': [summaryError]});
			var controller = $controller('editTaskModalController', {
				$scope: $scope,
				$uibModalInstance: undefined,
				task: task
			});
			$scope.submit();
			$httpBackend.flush();
			expect($scope.errors).toEqual({'summary': 'invalid summary'});
			$httpBackend.expectPOST('/api/tasks/1').respond(400, {'errors': [descriptionError]});
			$scope.submit();
			$httpBackend.flush();
			expect($scope.errors).toEqual({'description': 'invalid description'});
		});
		
		it("returns the updated task on success", function() {
			var savedTask;
			var updatedTask = {id: '1', summary: 'Renamed task', description: 'This is an edited task.', priority: 'Low', state: 'TO_DO', created: '2016-04-03T19:52:00Z', updated: '2016-04-03T19:52:00Z', project: '1', href: '/api/tasks/1'};
			$httpBackend.expectPOST('/api/tasks/1', {summary: updatedTask.summary, description: updatedTask.description, priority: updatedTask.priority}).respond(200, updatedTask);
			var controller = $controller('editTaskModalController', {
				$scope: $scope,
				$uibModalInstance: {
					close: function(data) {
						savedTask = data
					}
				},
				task: task
			});
			$scope.taskSummary = updatedTask.summary;
			$scope.taskDescription = updatedTask.description;
			$scope.taskPriority = updatedTask.priority;
			$scope.submit();
			$httpBackend.flush();
			expect($scope.errors).toEqual({});
			expect(savedTask).toEqual(updatedTask);
		});
		
	});

	describe('deleteTaskModalController', function() {

		var task = {id: '1', summary: 'First sample task', description: 'This is the first sample task.', priority: 'HIGH', state: 'TO_DO', created: '2016-04-03T19:52:00Z', updated: '2016-04-03T19:52:00Z', project: '1', href: '/api/tasks/1'};

		var $scope;

		beforeEach(function() {
			$scope = {};
		});

		it ('deletes the task on submit', function() {
			$httpBackend.expectDELETE('/api/tasks/1').respond(200);
			var controller = $controller('deleteTaskModalController', {
				$scope: $scope,
				$uibModalInstance: { close: function(data) { } },
				task: task
			});
			$scope.submit();
			$httpBackend.flush();
		});
	});

	describe('navigationController', function() {
		
		var $scope;
		var $location;

		beforeEach(function() {
			$scope = {};
			inject(function($injector) {
				$location = $injector.get('$location');
			})
		});

		it ('can tell what the active route is', function() {
	        spyOn($location, 'path').and.returnValue('/any/path');
			$httpBackend.expectGET('/api/admin/users/current').respond(200, {"authorities": []});
			var controller = $controller('navigationController', {
				$scope: $scope
			});
			$httpBackend.flush();

			expect($scope.isActive('/any/path')).toEqual(true);
			expect($scope.isActive('/any/other/path')).toEqual(false);
		});
			
		it ('shows the admin options when the current user has the correct authorities', function() {
	        spyOn($location, 'path').and.returnValue('/any/path');
			$httpBackend.expectGET('/api/admin/users/current').respond(200, {"authorities": ['ROLE_USER', 'ROLE_ADMIN']});
			var controller = $controller('navigationController', {
				$scope: $scope
			});
			$httpBackend.flush();

			expect($scope.showAdminOptions).toEqual(true);
		});
			
		it ('hides the admin options when the current user does not have the correct authorities', function() {
	        spyOn($location, 'path').and.returnValue('/any/path');
			$httpBackend.expectGET('/api/admin/users/current').respond(200, {"authorities": ['ROLE_USER']});
			var controller = $controller('navigationController', {
				$scope: $scope
			});
			$httpBackend.flush();

			expect($scope.showAdminOptions).toEqual(false);
		});
			
	});

	describe('adminController', function() {

		var currentUser = {"username": "user", "authorities": ['ROLE_USER']};
		var otherUser = {"username": "other", "authorities": ['ROLE_USER']};
		var adminUser = {"username": "admin", "authorities": ['ROLE_USER', 'ROLE_ADMIN']};
		var users = [ currentUser, otherUser, adminUser ];
		
		var $scope;

		beforeEach(function() {
			$scope = {};
		});

		it ('loads the list of users', function() {
			$httpBackend.expectGET('/api/admin/users/current').respond(200, currentUser);
			$httpBackend.expectGET('/api/admin/users/').respond(200, {"users": users});
			var controller = $controller('adminController', {
				$scope: $scope
			});
			$httpBackend.flush();
			expect($scope.currentUser).toEqual(currentUser);
			expect($scope.users).toEqual(users);
		});
			
		it ('knows which user is the current user', function() {
			$httpBackend.expectGET('/api/admin/users/current').respond(200, currentUser);
			$httpBackend.expectGET('/api/admin/users/').respond(200, {"users": users});
			var controller = $controller('adminController', {
				$scope: $scope
			});
			$httpBackend.flush();
			expect($scope.isCurrentUser(currentUser)).toEqual(true);
			expect($scope.isCurrentUser(otherUser)).toEqual(false);
			expect($scope.isCurrentUser(adminUser)).toEqual(false);
		});
			
		it ('knows which users are admins', function() {
			$httpBackend.expectGET('/api/admin/users/current').respond(200, currentUser);
			$httpBackend.expectGET('/api/admin/users/').respond(200, {"users": users});
			var controller = $controller('adminController', {
				$scope: $scope
			});
			$httpBackend.flush();
			expect($scope.isAdmin(currentUser)).toEqual(false);
			expect($scope.isAdmin(otherUser)).toEqual(false);
			expect($scope.isAdmin(adminUser)).toEqual(true);
		});

		it ("creates a new user on submit", function() {
			$httpBackend.expectGET('/api/admin/users/current').respond(200, currentUser);
			$httpBackend.expectGET('/api/admin/users/').respond(200, {"users": users});
			var controller = $controller('adminController', {
				$scope: $scope
			});
			$httpBackend.flush();
			var newUser = {'username': 'new', 'password': 'secret', 'authorities': ['ROLE_USER']};
			$httpBackend.expectPOST('/api/admin/users/').respond(200, newUser);
			$scope.newUser = newUser;
			$scope.createUser();
			$httpBackend.flush();
		});
			
		it ("creates a new admin user and adds it to the list on submit", function() {
			$httpBackend.expectGET('/api/admin/users/current').respond(200, currentUser);
			$httpBackend.expectGET('/api/admin/users/').respond(200, {"users": users});
			var controller = $controller('adminController', {
				$scope: $scope
			});
			$httpBackend.flush();
			var newUser = {'username': 'new', 'password': 'secret', 'authorities': ['ROLE_USER', 'ROLE_ADMIN']};
			$httpBackend.expectPOST('/api/admin/users/').respond(200, newUser);
			$scope.newUser = newUser;
			$scope.createUser();
			$httpBackend.flush();
			expect($scope.users).toEqual(users.concat([newUser]));
		});
			
		it ("handles field errors", function() {
			$httpBackend.expectGET('/api/admin/users/current').respond(200, currentUser);
			$httpBackend.expectGET('/api/admin/users/').respond(200, {"users": users});
			var controller = $controller('adminController', {
				$scope: $scope
			});
			$httpBackend.flush();
			var errors = [
							{'field': 'username', 'code': 'invalid', 'message': 'invalid credentials'},
							{'field': 'password', 'code': 'invalid', 'message': 'invalid credentials'}
						];
			$scope.newUser = {'username': 'new', 'password': 'secret', 'authorities': ['ROLE_USER', 'ROLE_ADMIN']};
			$httpBackend.expectPOST('/api/admin/users/').respond(400, {'errors': errors});
			$scope.createUser();
			$httpBackend.flush();
			expect($scope.errors).toEqual({'username': 'invalid credentials', 'password': 'invalid credentials'});
		});
			
		it ("resets field errors on resubmission", function() {
			$httpBackend.expectGET('/api/admin/users/current').respond(200, currentUser);
			$httpBackend.expectGET('/api/admin/users/').respond(200, {"users": users});
			var controller = $controller('adminController', {
				$scope: $scope
			});
			$httpBackend.flush();
			var errors = [
							{'field': 'username', 'code': 'invalid', 'message': 'invalid credentials'},
							{'field': 'password', 'code': 'invalid', 'message': 'invalid credentials'}
						];
			newUser = {'username': 'new', 'password': 'secret', 'authorities': ['ROLE_USER', 'ROLE_ADMIN']};
			$scope.newUser = newUser;
			$httpBackend.expectPOST('/api/admin/users/').respond(400, {'errors': errors});
			$scope.createUser();
			$httpBackend.flush();
			expect($scope.errors).toEqual({'username': 'invalid credentials', 'password': 'invalid credentials'});
			$httpBackend.expectPOST('/api/admin/users/').respond(200, newUser);
			$scope.createUser();
			$httpBackend.flush();
			expect($scope.errors).toEqual({});
		});
			
		it ("is in an error state if the current user does not have the correct authorities", function() {
			$httpBackend.expectGET('/api/admin/users/current').respond(200, currentUser);
			$httpBackend.expectGET('/api/admin/users/').respond(403, {}, {}, 'Access Forbidden');
			var controller = $controller('adminController', {
				$scope: $scope
			});
			$httpBackend.flush();
			expect($scope.error).toEqual({'code': 'Access Forbidden', 'detail': 'You do not have permission to access this page.'});
		});
			
	});

});