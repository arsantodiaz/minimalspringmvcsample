/// <reference path="../../../../../../3rdparty/d.ts/angularjs/angular.d.ts"/>
module TonyKopettoSampleWebApp.Directive {

    /////////////////////////////////////////////////////
    //Define which attributes will be created on scope
    /////////////////////////////////////////////////////
    export class TonyGridDirectiveScopeDefinition implements IBaseDirectiveScopeDefinition {
        template:string;
        rowsPerPage:string;
    };

    /////////////////////////////////////////////////////
    //Actual scope object created by angular
    /////////////////////////////////////////////////////
    export interface ITonyGridDirectiveScope extends IBaseDirectiveScope {
        template:string;
        rowsPerPage:number;
        
        //methods
        page: (pageNum:number) => void;
        url:string;
    };
    
    /////////////////////////////////////////////////////
    //return DDO object
    /////////////////////////////////////////////////////
    export class TonyGridDirective extends BaseDirective {

        constructor (
            private $compile:ng.ICompileService, 
            private $logger:ng.ILogService, 
            private $timeout:ng.ITimeoutService 
        ){
        
            super ();
            this.template="resources/module/common/html/grid.html";
            this.restrict="E";
            
            this.scope=<TonyGridDirectiveScopeDefinition>{
                template:"=",
                rowsPerPage:"=?"
            };
            
            this.controller=function ($scope: ITonyGridDirectiveScope, $element:ng.IAugmentedJQuery, $attrs:any, controllers:any) {
                var _that=this;
    
                if (!$scope.rowsPerPage)
                    $scope.rowsPerPage=20;
                
                this.pageNum=0;
                this.templateLoaded=false;
                
                $scope.page = function (pageNum) {
                    _that.pageNum=pageNum;
                    
                     $scope.url = $scope.template + "?page.pageSize=" + 
                     $scope.rowsPerPage + "&page.page=" + pageNum +
                         "&ut=" + (new Date ().getTime ());
                };
                
                $scope.page (this.pageNum);
                
            };
            
            this.link=function ($scope: ITonyGridDirectiveScope, $element:ng.IAugmentedJQuery, $attrs:any, controller:any) {
                $scope.$on('$includeContentLoaded',function(event,data){
                    controller.templateLoaded=true;
                });
            }

        }
        
    }

}

/////////////////////////////////////////////////////
//Let angular know about the directive
/////////////////////////////////////////////////////
var tonyGridDirective = angular.module('tonyGridDirectiveModule', [], function ($compileProvider) {
    $compileProvider.directive ('tonyGrid', function ($compile, $log, $timeout) {
        return new TonyKopettoSampleWebApp.Directive.TonyGridDirective ($compile, $log, $timeout);
    });
});

