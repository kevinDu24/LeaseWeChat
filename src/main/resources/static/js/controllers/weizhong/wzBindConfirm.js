
/**
 * Created by pengchao on 2018/1/4.
 */
app.controller('wxWzBindConfirmController', ['$modalInstance', '$scope', 'openId', '$state',  function($modalInstance, $scope, openId, $state) {
    $scope.openId = openId;
    $scope.ok = function(){
        $modalInstance.close();
    }
    $scope.cancel = function(){
        $state.go('access.wzBind');
    }
}]);