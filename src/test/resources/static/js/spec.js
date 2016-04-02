describe("myTasks", function() {

	beforeEach(module('mytasks'));

	var $httpBackend, $controller;
	beforeEach(inject(function($injector) {
		$controller = $injector.get('$controller');
		$httpBackend = $injector.get('$httpBackend');
	}));

	afterEach(function() {
		$httpBackend.verifyNoOutstandingExpectation();
		$httpBackend.verifyNoOutstandingRequest();
	});
	
	describe("the projects controller", function() {
		
		var projects = [
            {id : 1, name : 'My first project', description : 'This is my first sample project.',
            	numberOfOpenTasks : 3, href : '/api/projects/1'},
            {id : 2, name : 'My second project', description : 'This is my second sample project.',
            	numberOfOpenTasks : 2, href : '/api/projects/2'},
            {id : 3, name : 'My third project', description : 'This is my third sample project.',
            	numberOfOpenTasks : 11, href : '/api/projects/3'}
    	];

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
		
		var $scope;

		beforeEach(function() {
			$scope = {};
			inject(function($uibModal) {
		        spyOn($uibModal, 'open').and.returnValue(mockModalInstance);
		    })
		});

		it("loads the list of projects from the REST API", function() {
			$httpBackend.expectGET('/api/projects/').respond(200, {'projects': projects});
			var controller = $controller('projects', {
				$scope : $scope
			});
			$httpBackend.flush();
			expect($scope.projects).toEqual(projects);
		});
		
		it("adds a new project to the list on confirm", function() {
			$httpBackend.expectGET('/api/projects/').respond(200, {'projects': projects});
			var controller = $controller('projects', {
				$scope : $scope
			});
			$httpBackend.flush();
			expect($scope.projects).toEqual(projects);
			$scope.openNewProjectModal();
			var newProject = {"id": 4, "name": "My new project", "description": "This is a new project.", "numberOfOpenTasks": 0, "href": "/api/projects/4"};
			mockModalInstance.close(newProject);
			expect($scope.projects).toEqual(projects.concat([newProject]));
		});

		it("leaves the project list unchanged on cancel", function() {
			$httpBackend.expectGET('/api/projects/').respond(200, {'projects': projects});
			var controller = $controller('projects', {
				$scope : $scope
			});
			$httpBackend.flush();
			expect($scope.projects).toEqual(projects);
			$scope.openNewProjectModal();
			mockModalInstance.dismiss();
			expect($scope.projects).toEqual(projects);
		});

	});

	describe("the new-project controller", function() {

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
			var controller = $controller('new-project', {
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
			var controller = $controller('new-project', {
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
			var controller = $controller('new-project', {
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