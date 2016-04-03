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

	beforeEach(module('myTasksControllers'));

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
			var updatedProject = {"id": 4, "name": "My edited project", "description": "This is an edited project.", "numberOfOpenTasks": 0, "href": "/api/projects/4"};
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
			expect($scope.errors).toEqual({'name': 'invalid name', 'description': 'invalid description'});
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
			expect($scope.errors).toEqual({'name': 'invalid name'});
			$httpBackend.expectPOST('/api/projects/').respond(400, {'errors': [descriptionError]});
			$scope.submit();
			$httpBackend.flush();
			expect($scope.errors).toEqual({'description': 'invalid description'});
		});
		
		it("returns the new project on success", function() {
			var createdProject;
			var newProject = {"id": 4, "name": "My new project", "description": "This is a new project.", "numberOfOpenTasks": 0, "href": "/api/projects/4"};
			$httpBackend.expectPOST('/api/projects/').respond(201, newProject);
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
			expect($scope.errors).toEqual({});
			expect(createdProject).toEqual(newProject);
		});
		
	});

});