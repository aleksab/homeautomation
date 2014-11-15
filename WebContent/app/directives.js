app.directive('header', ['$location', function($location) {
	return {
		restrict: 'E',
		scope: true,
		template: '<nav class="navbar navbar-inverse " role="navigation"><div class="container"><div class="navbar-header"><a class="navbar-brand" href="#">Kiwi</a></div><div ><ul class="nav navbar-nav"><li ng-class="{active:item.slug==activeSlug}" ng-repeat="item in items"><a href="" ng-click="activate(item)" ng-bind="item.title"></a></li></ul></div></div></nav>',
		link: function(scope, element, attrs) {
			scope.activeSlug = '';
			scope.items = [
			{
				slug: '',
				title: 'Switchboard'
			},
			{
				slug: 'rules',
				title: 'Rules'
			},
			{
				slug: 'discover',
				title: 'Discover'
			}
			];
			scope.activate = function(item) {
				$location.path(item.slug);
				scope.activeSlug = item.slug;
			};

		}
	}
}]);
