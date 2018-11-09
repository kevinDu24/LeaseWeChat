app.controller('wxDefaultController',['$scope', '$state',function($scope,$state) {
    $scope.gotoPage = function(page){
        $state.go(page);
    };
}])
;
