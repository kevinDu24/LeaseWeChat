/**
 * Created by LEO on 16/10/12.
 */
app.controller('wxWzRepaymentPlanDetailController',['$scope', '$state', '$http', '$stateParams', '$localStorage',function($scope,$state, $http, $stateParams, $localStorage) {
    $localStorage.leaseUrl = 'wx.wzRepaymentPlanDetail';
    //wx.wzRepaymentPlanDetail


    (function () {
        if (typeof Object.defineProperty === 'function') {
            try {
                Object.defineProperty(Array.prototype, 'sortBy', {value: sb});
            } catch (e) {
            }
        }
        if (!Array.prototype.sortBy) Array.prototype.sortBy = sb;

        function sb(f) {
            for (var i = this.length; i;) {
                var o = this[--i];
                this[i] = [].concat(f.call(o, o, i), o);
            }
            this.sort(function (a, b) {
                for (var i = 0, len = a.length; i < len; ++i) {
                    if (a[i] != b[i]) return a[i] < b[i] ? -1 : 1;
                }
                return 0;
            });
            for (var i = this.length; i;) {
                this[--i] = this[i][this[i].length - 1];
            }
            return this;
        }
    })();

    function init(){
        $http.get('/contracts/'+$stateParams.applyNum+'/wzRepayDetail').success(function(result){
            if(result.status == 'SUCCESS'){
                $scope.contractInfo = result.data;
                $scope.contractInfo.repayplan = $scope.contractInfo.repayplan.sortBy(function(){return this.BAHKRQ});
                if($scope.contractInfo.BAYQTS == ''){
                    $scope.contractInfo.BAYQTS = '0';
                }
                if($scope.contractInfo.BAYQLX == ''){
                    $scope.contractInfo.BAYQLX = '0';
                }
            }
        });
    }
    init();
}])
;