var myTasksComponents = angular.module('myTasksComponents', ['ui.bootstrap'])

.component('myMessages', {
	templateUrl: 'components/my-messages.html',
	controller: MyMessagesController,
	bindings: {
    	messages: '='
    }
});

function MyMessagesController($scope, $element, $attrs) {
	var ctrl = this;
	$scope.type = $attrs['type'] ? $attrs['type'] : 'info';
	$scope.close = function(index) { ctrl.messages.splice(index, 1) }
}