var myTasksApplication = angular.module('myTasksApplication', ['myTasksControllers', 'myTasksComponents', 'ngRoute', 'ngMessages'])

.config(function($routeProvider, $httpProvider, $resourceProvider) {
	$routeProvider
	.when('/', {
		templateUrl : 'partials/dashboard.html',
		controller : 'projectsController'
	})
	.when('/projects', {
		templateUrl : 'partials/project-list.html',
		controller : 'projectsController',
	})
	.when('/projects/:id', {
		templateUrl : 'partials/project-view.html',
		controller : 'projectController',
	})
	.when('/projects/:id/tasks', {
		templateUrl : 'partials/task-list.html',
		controller : 'projectTasksController',
	})
	.when('/tasks/:id', {
		templateUrl : 'partials/task-view.html',
		controller : 'taskController',
	})
	.when('/admin', {
		templateUrl : 'partials/admin.html',
		controller : 'adminController',
	})
	.otherwise({
		redirectTo: '/'
	});

	$httpProvider.defaults.headers.common["X-Requested-With"] = 'XMLHttpRequest';
	$resourceProvider.defaults.stripTrailingSlashes = false;

})

.run(function($rootScope) {
    $rootScope.isDefined = angular.isDefined;
    $rootScope.isUndefined = angular.isUndefined;
    
    $rootScope.priorities = priorities;
    $rootScope.states = states;

    $rootScope.labels = labels;

});