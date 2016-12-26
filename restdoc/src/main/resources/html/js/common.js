$(function(){

});

//定义菜单模版
Vue.component('ui_menu', Vue.extend({
	template: '<template v-for="module in modules"><li v-if="{{}}"" + (moduleActive ? " class=\"active\"" : "") + "></template>'
}));

var meta = {
	base_url: "http://localhost:8082",
	modules: null,
	config: null,
	getModules: function() {
		var me = this;
		$.ajax({
			type: "GET",
			async: false,
			url: me.base_url + "/doc/module.json",
			success: function(msg) {
				me.modules = msg;
			}
		});
		return me.modules;
	},
	getConfig: function() {
		var me = this;
		if (me.config == null) {
			$.ajax({
				type: "GET",
				async: false,
				url: meta.base_url + "/doc/config.json",
				success: function(msg) {
					me.config = {
						product: msg["global.product"],
						company: msg["global.company"],
						description: msg["global.description"]
					}
				}
			});
			return me.config;
		}
	},
	loadClass: function(className) {
		var result;
		$.ajax({
			type: "GET",
			async: false,
			url: meta.base_url + "/doc/api/" + className + ".json",
			success: function(msg) {
				result = msg;
			}
		});
		return result;
	},
	loadModel: function(className) {
		var result;
		$.ajax({
			type: "GET",
			async: false,
			url: meta.base_url + "/doc/class/" + className + ".json",
			success: function(msg) {
				result = msg;
			}
		});
		return result;
	}
};

//获取URL参数
function GetUrlParms() {
	var args = new Object();
	var query = location.search.substring(1); //获取查询串   
	var pairs = query.split("&"); //在逗号处断开   

	for (var i = 0; i < pairs.length; i++) {
		var pos = pairs[i].indexOf('='); //查找name=value   
		if (pos == -1) continue; //如果没有找到就跳过   
		var argname = pairs[i].substring(0, pos); //提取name   
		var value = pairs[i].substring(pos + 1); //提取value   
		args[argname] = unescape(value); //存为属性   
	}

	return args;
};

var init = {
	footer: function() {
		var footerVM = new Vue({
			el: '#footer',
			data: meta.getConfig()
		})
	},
	menu: function() {
		var menuVM = new Vue({
			el: '#menu',
			data: function() {
				var className = GetUrlParms()["class"];
				if (className == undefined || className == null) {
					className = "";
				}
				className = className.toLowerCase();
				var modules = meta.getModules();
				var apiClasses;
				var module;
				var active;
				for (var mi in modules) {
					module = modules[mi];
					apiClasses = module.apiClasses;
					active = false;
					for (var ci in apiClasses) {
						var apiClass = apiClasses[ci];
						if (apiClass.type.toLowerCase() == className) active = true;
						apiClass.active = active;
						if (active) break;
					}

					module.active = active;
				}
				return {
					"modules": modules
				};
			}
		})
	}
}

