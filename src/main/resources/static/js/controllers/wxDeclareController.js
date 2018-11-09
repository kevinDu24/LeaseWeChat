/**
 * Created by LEO on 16/10/27.
 */
app.controller('wxDeclareController',['$scope', '$state', '$cookies', '$http', '$localStorage', '$modalInstance', function($scope,$state, $cookies, $http, $localStorage, $modalInstance) {
    $scope.close = function(){
        $modalInstance.close();
    };
}])
;