/// <reference path="../../../d.ts/angularjs/angular.d.ts"/>

var directiveModule = angular.module ('tonyGridDirectiveModule', []);

directiveModule.directive ('tonyGrid', function () {
	
	return {
		restrict:'E',
		transclusion:false,
		templateUrl:'resources/module/common/html/grid.html',
		scope:{
            template:"=",
            rowsPerPage:"=?"
		},
		controller:function ($scope) {
			
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
			
		},
		link:function ($scope, $element, $attrs, controller) {
            $scope.$on('$includeContentLoaded',function(event,data){
                controller.templateLoaded=true;
            });
			
			
		}
	}
});