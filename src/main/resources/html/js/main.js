function start() {
  var url = "http://www.guokr.com/article/441954/";
  var deep = 1;
  jsObj.start(url, deep);
}

function testA() {
  jsObj.testA();
}

function parseLinks(obj) {
  // alert(JSON.stringify(obj));
  var doms = $.parseHTML(obj.body);
  doms.forEach(function (d) {
    console.log(d.nodeName);
  });
  console.log('parse over...');
}

function newJsObject() {
  return {};
}

function testB() {
  jsObj.testB();
}
