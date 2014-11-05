browser.driver.manage().window().maximize();

// JASMINE SETTINGS
require('jasmine-reporters');

// UTILITY FUNCTIONS
var fs = require('fs');

browser.params.pages={}
browser.params.baseUrl = '@APP_URL@';
browser.ignoreSynchronization = true;//do not to wait for angular

// SET UP PAGE OBJECTS
require('build/protractor/pages/BasePage.js');
require('build/protractor/pages/SearchPage.js');

