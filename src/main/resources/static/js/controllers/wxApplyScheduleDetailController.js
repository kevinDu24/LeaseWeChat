/**
 * Created by LEO on 16/10/12.
 */
app.controller('wxApplyScheduleDetailController',['$scope', '$state', '$http', '$stateParams', '$cookies', function($scope,$state, $http, $stateParams, $cookies) {
    function init(){
        $http.get('/contracts/'+$stateParams.applyNum+'/log').success(function(result){
            if(result.status == 'SUCCESS'){
                $scope.data = result.data;
            }
        });
    }
    init();

    $scope.convertDate = function(date){
        return date.substring(0, 4) + '-' + date.substring(4, 6) + '-' + date.substring(6, 8) + ' ' + date.substring(8, 10)+ ':' + date.substring(10, 12) + ':' + date.substring(12,14)
    };
}])
;