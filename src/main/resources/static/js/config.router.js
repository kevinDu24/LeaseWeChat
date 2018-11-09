'use strict';

/**
 * Config for the router
 */
angular.module('app')
  .run(
    [          '$rootScope', '$state', '$stateParams',
      function ($rootScope,   $state,   $stateParams) {
          $rootScope.$state = $state;
          $rootScope.$stateParams = $stateParams;        
      }
    ]
  )
  .config(
    [          '$stateProvider', '$urlRouterProvider', 'JQ_CONFIG', 'MODULE_CONFIG', '$compileProvider',
      function ($stateProvider,   $urlRouterProvider, JQ_CONFIG, MODULE_CONFIG, $compileProvider) {
          var layout = "tpl/app.html";
          $compileProvider.imgSrcSanitizationWhitelist(/^\s*(http|https|data|wxlocalresource|weixin):/);
          if(window.location.href.indexOf("material") > 0){
            layout = "tpl/blocks/material.layout.html";
            $urlRouterProvider
              .otherwise('/app/dashboard-v3');
          }
          
          $stateProvider
              .state('app', {
                  abstract: true,
                  url: '/app',
                  templateUrl: layout
              })
              .state('access', {
                  url: '/access',
                  template: '<div ui-view></div>'
              })
              //绑定用户
              .state('access.bind', {
                  url: '/bind',
                  templateUrl: 'tpl/wx_binding.html',
                  resolve: load(['toaster', 'js/controllers/weChatBindController.js'])
              })
              //微众绑定
              .state('access.wzBind', {
                    url: '/wzBind',
                    templateUrl: 'tpl/weizhong/wx_wz_bind.html',
                    resolve: load(['toaster', 'js/controllers/weizhong/wzWeChatBindController.js','js/controllers/weizhong/wzBindConfirm.js'])
               })
              .state('wx', {
                  url: '/wx',
                  template: '<div ui-view></div>'
              })
              //微信默认页面
              .state('wx.defaultPage', {
                  url: '/defaultPage',
                  templateUrl: 'tpl/wx_default.html',
                  resolve: load( ['toaster', 'js/controllers/wxDefaultController.js'] )
              })
              //个人申请
              .state('wx.personalCredit', {
                  url: '/personalCredit',
                  templateUrl: 'tpl/wx_personalCredit.html',
                  resolve: load( ['toaster', 'js/controllers/wxPersonalCreditController.js','js/controllers/wxApplyFinanceController.js'] )
              })
              //企业申请
              .state('wx.companyCredit', {
                  url: '/companyCredit',
                  templateUrl: 'tpl/wx_companyCredit.html',
                  resolve: load( ['toaster', 'js/controllers/wxCompanyCreditController.js','js/controllers/wxApplyFinanceController.js'] )
              })
              //申请进度查询列表
              .state('wx.applyScheduleQuery', {
                  url: '/applyScheduleQuery',
                  templateUrl: 'tpl/wx_applyScheduleQuery.html',
                  resolve: load( ['js/controllers/wxApplyScheduleQueryController.js'] )
              })
              //申请提前还款列表
              .state('wx.applyRepaymentQuery', {
                  url: '/applyRepaymentQuery',
                  templateUrl: 'tpl/wx_applyRepaymentQuery.html',
                  resolve: load( ['js/controllers/wxApplyRepaymentQueryController.js', 'js/controllers/wxApplyRepaymentPromptController.js'] )
              })
              .state('wx.applyRepaymentCheckError', {
                  url: '/applyRepaymentCheckError',
                  templateUrl: 'tpl/wx_applyRepaymentCheckError.html'
              })
              //申请提前还款详情
              .state('wx.applyRepaymentDetails', {
                  url: '/applyRepaymentDetails/:contractId',
                  templateUrl: 'tpl/wx_applyRepaymentDetails.html',
                  resolve: load( ['js/controllers/wxApplyRepaymentDetailsController.js'] )
              })
              //还款计划列表
              .state('wx.repaymentPlanQuery', {
                  url: '/repaymentPlanQuery',
                  templateUrl: 'tpl/wx_repaymentPlanQuery.html',
                  resolve: load( ['js/controllers/wxRepaymentPlanQueryController.js'] )
              })
              .state('wx.applyPrepayment', {
                  url: '/applyPrepayment',
                  templateUrl: 'tpl/wx_applyPrepayment.html',
                  resolve: load( ['js/controllers/wxApplyPrepaymentController.js'] )
              })
              //还款卡变更
              .state('wx.repaymentCardModify', {
                  url: '/repaymentCardModify',
                  templateUrl: 'tpl/wx_repaymentCardModify.html',
                  resolve: load( ['js/controllers/wxRepaymentCardModifyController.js'] )
              })
              //还款卡变更详情
              .state('wx.repaymentCardModifyDetails', {
                  url: '/repaymentCardModifyDetails/:contractNum',
                  templateUrl: 'tpl/wx_repaymentCardModifyDetails.html',
                  resolve: load( ['toaster', 'js/controllers/wxRepaymentCardModifyDetailsController.js', "js/directives/fileModel.js", "/js/controllers/wxDeclareController.js"] )
              })
             .state('wx.wzRepaymentCardModifyDetails', {
                url: '/wzRepaymentCardModifyDetails/:contractNum/:applyNum',
                templateUrl: 'tpl/weizhong/wx_wzRepaymentCardModifyDetails.html',
                resolve: load( ['toaster', 'js/controllers/weizhong/wxWzRepaymentCardModifyDetailsController.js', "js/directives/fileModel.js", "/js/controllers/wxDeclareController.js","js/controllers/wxApplyFinanceController.js"] )
             })
              //产品列表
              .state('wx.productList', {
                  url: '/productList',
                  templateUrl: 'tpl/wx_productList.html',
                  resolve: load( ['js/controllers/wxProductListController.js'] )
              })
              //产品详情
              .state('wx.productDetails', {
                  url: '/productDetails',
                  templateUrl: 'tpl/wx_productDetails.html',
                  resolve: load( ['js/controllers/wxProductDetailsController.js'] )
              })
              .state('wx.applyScheduleDetail', {
                  url: '/applyScheduleDetail/:applyNum',
                  templateUrl: 'tpl/wx_applyScheduleDetail.html',
                  resolve: load( ['js/controllers/wxApplyScheduleDetailController.js'] )
              })
              .state('wx.repaymentPlanDetail', {
                  url: '/repaymentPlanDetail/:applyNum',
                  templateUrl: 'tpl/wx_repaymentPlanDetail.html',
                  resolve: load( ['js/controllers/wxRepaymentPlanDetailController.js'] )
              })
              .state('wx.wzRepaymentPlanDetail', {
                  url: '/wzRepaymentPlanDetail/:applyNum',
                  templateUrl: 'tpl/weizhong/wx_wz_repaymentPlanDetail.html',
                  resolve: load( ['js/controllers/weizhong/wxWzRepaymentPlanDetailController.js'] )
               })
              .state('wx.temp', {
                  url: '/temp',
                  templateUrl: 'tpl/wx_temp.html',
                  resolve: load( ['js/controllers/wxTempController.js'] )
              })
              .state('wx.commonProblem', {
                  url: '/commonProblem',
                  templateUrl: 'tpl/wx_commonProblems.html',
                  resolve: load( ['toaster','js/controllers/wxCommonProblemsController.js'] )
              })
              .state('wx.calculator', {
                  url: '/calculator',
                  templateUrl: 'tpl/wx_calculator.html',//计算器
                  resolve: load( ['toaster','js/controllers/wxCalculatorController.js'] )
              })
              .state('wx.calculatorResult', {
                  url: '/calculatorResult',
                  templateUrl: 'tpl/wx_calculatorResult.html',//计算结果
                  resolve: load( ['toaster','js/controllers/wxCalculatorResultController.js'] )
              })
              //微众预审
              .state('wx.wzApply', {
                  url: '/wzApply',
                  templateUrl: 'tpl/weizhong/wx_wzApply.html',
                  resolve: load( ['toaster','js/controllers/weizhong/wzApply.js', "/js/controllers/wxDeclareController.js", "js/controllers/wxApplyFinanceController.js","js/controllers/weizhong/wxStatementController.js"] )
              })
                .state('wx.wzCalculator', {
                    url: '/wzCalculator',
                    templateUrl: 'tpl/wx_wzCalculator.html',
                    resolve: load( ['toaster','js/controllers/wzCalculator.js'] )
                })
              .state('wx.wzSign', {
                  url: '/wzSign',
                  templateUrl: 'tpl/weizhong/wx_wzSign.html',
                  resolve: load( ['toaster','js/controllers/weizhong/wzSign.js', "/js/controllers/wxDeclareController.js", "js/controllers/wxApplyFinanceController.js"] )
              })
              .state('wx.wzResult', {
                  url: '/wzResult',
                  templateUrl: 'tpl/weizhong/wx_wzResult.html',
                  resolve: load( ['toaster','js/controllers/weizhong/wzResult.js'] )
              })
              .state('wx.wzRepayCardChangeResult', {
                  url: '/wzRepayCardChangeResult',
                  templateUrl: 'tpl/weizhong/wx_wzRepayCardChangeResult.html',
                  resolve: load( ['toaster','js/controllers/weizhong/wzRepayCardChangeController.js'] )
              })
              .state('wx.wzDetails', {
                  url: '/wzDetails',
                  templateUrl: 'tpl/weizhong/wzDetails.html',
                  resolve: load( ['toaster','js/controllers/weizhong/wzDetails.js'] )
              })
              .state('wx.factorVerification', {
                 url: '/factorVerification',
                 templateUrl: 'tpl/wx_factorVerification.html',
                 resolve: load( ['toaster','js/controllers/wxFactorVerificationController.js'] )
              });
          function load(srcs, callback) {
            return {
                deps: ['$ocLazyLoad', '$q',
                  function( $ocLazyLoad, $q ){
                    var deferred = $q.defer();
                    var promise  = false;
                    srcs = angular.isArray(srcs) ? srcs : srcs.split(/\s+/);
                    if(!promise){
                      promise = deferred.promise;
                    }
                    angular.forEach(srcs, function(src) {
                      promise = promise.then( function(){
                        if(JQ_CONFIG[src]){
                          return $ocLazyLoad.load(JQ_CONFIG[src]);
                        }
                        angular.forEach(MODULE_CONFIG, function(module) {
                          if( module.name == src){
                            name = module.name;
                          }else{
                            name = src;
                          }
                        });
                        return $ocLazyLoad.load(name);
                      } );
                    });
                    deferred.resolve();
                    return callback ? promise.then(function(){ return callback(); }) : promise;
                }]
            }
          }


      }
    ]
  ).config(['$httpProvider', function($httpProvider) {
        //Handle 401 Error
        $httpProvider.interceptors.push(function($q, $injector) {
            return {
                response: function(response){
                    return response || $q.when(response);
                },
                responseError: function(rejection){
                    if(rejection.status === 401){
                        var $window = $injector.get('$window');
                        $window.location.href = 'https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx743de4a4fa762293&redirect_uri=http%3A%2F%2Ffwh.xftm.com%2F%23%2Faccess%2Fbind&response_type=code&scope=snsapi_userinfo&state=123#wechat_redirect';
                    }
                    return $q.reject(rejection);
                }
            };
        });
    }]);
