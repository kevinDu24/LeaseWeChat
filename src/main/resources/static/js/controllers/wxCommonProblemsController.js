/**
 * Created by duyusheng on 16/11/8.
 */
'use strict';
app.controller('wxCommonProblemsController', ['$scope', '$http', '$modal', 'toaster', '$window', function ($scope, $http, $modal, toaster, $window) {

    $scope.toaster = {
        type: 'success',
        title: 'Title',
        text: 'Message'
    };
    $scope.pop = function(type,title,text){
        toaster.pop(type,'',text);
    };

    var firstPage = 1;
    var size = 10;
    $scope.page = firstPage;
    $scope.size = size;
    $scope.showPreview = true;
    $scope.showNext = true;
    $scope.loading = false;
    $scope.oneAtATime = true;

    $scope.getPagedDataAsync = function() {
        var url = '/commonProblems/commonProblemList?page=' + $scope.page + '&size=' + $scope.size;
        $http.get(url).success(function (pagedata) {

                if(pagedata.status == 'SUCCESS'){
                    $scope.commonProblemList = pagedata.data.content;
                    for(var i in $scope.commonProblemList){
                        $scope.commonProblemList[i].isOpen = false;
                    }
                    $scope.commonProblems = pagedata.data;
                    $scope.showPreview = !$scope.commonProblems.first;
                    $scope.showNext = !$scope.commonProblems.last;
                    $scope.$digest;
                }else {
                    $scope.pop('error', '', pagedata.error);
                }
                $scope.loading = false;
        }).error(function(){
            $scope.loading = false;
            $scope.pop('error', '', '加载常见问题失败，请稍后再试!');
        });
    };
    $scope.getPagedDataAsync();

    $scope.status = {
        isFirstOpen: true,
        isFirstDisabled: false
    };
    $scope.refresh = function(){
        $scope.page = firstPage;
        $scope.size = size;
        $scope.getPagedDataAsync();
    };

    $scope.preview = function(){
        if($scope.commonProblems.first || $scope.loading){
            return;
        }
        $scope.page --;
        $scope.loading = true;
        $scope.getPagedDataAsync();
    };

    $scope.next = function(){
        if($scope.commonProblems.last || $scope.loading){
            return;
        }
        $scope.page ++;
        $scope.loading = true;
        $scope.getPagedDataAsync();
    };


}])
;