app.controller('wxApplyRepaymentPromptController', ['$modalInstance', '$scope', 'promptMessage', function($modalInstance, $scope, promptMessage) {
    $scope.promptMessage = promptMessage;
    $scope.ok = function(){
        $modalInstance.close();
    }
}]);