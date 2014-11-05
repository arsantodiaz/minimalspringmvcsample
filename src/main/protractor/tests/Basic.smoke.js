describe('Basic test: ', function(){
	
	var basePage = new browser.params.pages.BasePage ();
	
	//////////////////////////////////////////
	//Test for home page
	//////////////////////////////////////////
	describe ('Home Page test:', function () {
		
		it ('Check that it loads', function () {
			basePage.getPage ("").then (function () {
				console.log ("got home page");		
			});
		});
		
		it ('and has main section', function () {
			basePage.waitOnCondition (function () {
				return element(by.css('#main')).isDisplayed();
			});
		});
		
		it ('and has "What would you like to do" section', function () {
			expect(element(by.css('h1')).getText()).toEqual("What would you like to do?");
		});
	})

	//////////////////////////////////////////
	//Test for about page
	//////////////////////////////////////////
	describe ('About Page test:', function () {
		it ('Check that it loads', function () {
			basePage.getPage ("about").then (function () {
				console.log ("got about page");		
			});
		});
		
		it ('and has main section', function () {
			basePage.waitOnCondition (function () {
				return element(by.css('#main')).isDisplayed();
			});
		});
		
		it ('and has "How it works" section', function () {
			expect(element(by.css('#team .section_header h3')).getText()).toEqual("How it works");
		});
	});
	
});
