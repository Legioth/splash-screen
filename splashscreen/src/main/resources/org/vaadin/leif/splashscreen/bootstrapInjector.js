(function() {
	var hook = function() {
		var splash = document.getElementById("splash");
		if (splash) {
			splash.parentElement.removeChild(splash);
		}
		
		var hooks = window.vaadin.postRequestHooks;
		for(var i = 0; i < hooks.length; i++) {
			if (hooks[i] == hook) {
				hooks.splice(i, 1);
				break;
			}
		}
		if (hooks.length == 0) {
			delete window.vaadin.postRequestHooks;
		}
	}
	
	if (!window.vaadin.postRequestHooks) {
		window.vaadin.postRequestHooks = [hook];		
	} else {
		window.vaadin.postRequestHooks.push(hook);
	}
})();