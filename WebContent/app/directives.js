app.directive('showPrivacyModalBox', ['$modal', function($modal){
    return {
        restrict: 'A',
        link: function (scope, element, args){
            element.bind('click',  function(){
                $modal({scope:scope, template:'app/templates/modal/privacy.html', show:true});
            });
        }
    }
}]);

app.directive('modalBox', ['$modal', function($modal){
    return {
        restrict: 'A',
        link: function (scope, element, args){
            element.bind('click',  function(){
                $modal({scope:scope, template:args.modalBox, show:true});
            });
        }
    }
}]);

