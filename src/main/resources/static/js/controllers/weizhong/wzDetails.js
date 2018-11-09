/**
 * Created by pengchao on 2016/11/8.
 */
app.controller('wzDetailsController', ['toaster','$scope', '$http', '$window',function (toaster,$scope, $http, $window) {

    $scope.toaster = {
        type: 'success',
        title: 'Title',
        text: 'Message'
    };
    $scope.pop = function(type,title,text){
        toaster.pop(type,'',text);
    };

    $scope.infos = null;
    $scope.count = 0;

    function init(){
        var url = '/apply/getApplyList';
        $http.get(url).success(function (pagedata) {
            if(pagedata){
                if(pagedata.status == 'SUCCESS'){
                    $scope.infos= pagedata.data;
                    $scope.count = pagedata.data.length;
                }else {
                    $scope.pop('error', '', pagedata.error);
                    $scope.infos = null;
                    $scope.count = 0;
                }
            }
        });
    }
    init();

    $scope.search = function(){
        var url = '/apply/getApplyList';
        $http.get(url).success(function (pagedata) {
            if(pagedata){
                if(pagedata.status == 'SUCCESS'){
                    $scope.infos= pagedata.data;
                    $scope.count = pagedata.data.length;
                    $scope.pop('success', '', '刷新成功');
                }else {
                    $scope.pop('error', '', pagedata.error);
                    $scope.infos = null;
                    $scope.count = 0;
                }
            }
        });
    }
}]);
