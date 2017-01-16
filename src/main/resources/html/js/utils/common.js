var extend = function(child, parent) { for (var key in parent) { if (hasProp.call(parent, key)) child[key] = parent[key]; } function ctor() { this.constructor = child; } ctor.prototype = parent.prototype; child.prototype = new ctor(); child.__super__ = parent.prototype; return child; },
  hasProp = {}.hasOwnProperty;

format.extend(String.prototype);
String.prototype.trim = function() {
	return this.replace(/(^\s*)|(\s*$)/g, "");
};

// String.prototype.startsWith = function(str) {
// 	return (this.match("^" + str) == str)
// };

String.prototype.endsWith = function(str) {
	return (this.match(str + "$") == str)
};
String.prototype.repeat = function(num) {
	var str = '';
	for (var i = 0; i < num; i++) {
		str += this;
	}
	return str;
};

// 对Date的扩展，将 Date 转化为指定格式的String
// 月(M)、日(d)、小时(h)、分(m)、秒(s)、季度(q) 可以用 1-2 个占位符，
// 年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字)
// 例子：
// (new Date()).Format("yyyy-MM-dd hh:mm:ss.S") ==> 2006-07-02 08:09:04.423
// (new Date()).Format("yyyy-M-d h:m:s.S")      ==> 2006-7-2 8:9:4.18
Date.prototype.Format = function (fmt) { //author: meizz
    var o = {
        "M+": this.getMonth() + 1, //月份
        "d+": this.getDate(), //日
        "h+": this.getHours(), //小时
        "m+": this.getMinutes(), //分
        "s+": this.getSeconds(), //秒
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度
        "S": this.getMilliseconds() //毫秒
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
    if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}

Array.prototype.remove = function(val) {
  var index = this.indexOf(val);
  if (index > -1) {
    this.splice(index, 1);
  }
};

function loadConfig() {
  var configName = document.title;
  if (localStorage && localStorage.getItem(configName)) {
      Config = JSON.parse(localStorage.getItem(configName));
      var index = 0;
      var inputs = $('input');
      inputs.each(function (ind, input) {
        var type = input.type;
        switch (type) {
          case "text": input.value = Config[index]; break;
          case "radio":
          case "checkbox": input.checked = Config[index]; break;
          default: console.error("unknow type");
        }
        index++;
      });
      var textareas = $('textarea');
      textareas.each(function (ind, textarea) {
        textarea.value = Config[index];
        index++;
      });
  }
}

function saveConfig() {
  var configName = document.title;
  if (!localStorage) return;
  var Config = {};
  var index = 0;
  var inputs = $('input');
  inputs.each(function (ind, input) {
    var type = input.type;
    switch (type) {
      case "text": Config[index] = input.value; break;
      case "radio":
      case "checkbox": Config[index] = input.checked; break;
      default: console.error("unknow type");
    }
    index++;
  });
  var textareas = $('textarea');
  textareas.each(function (ind, textarea) {
    Config[index] = textarea.value;
    index++;
  });
  localStorage.setItem(configName, JSON.stringify(Config));
  // console.log(configName + " - " + JSON.stringify(Config));
}
