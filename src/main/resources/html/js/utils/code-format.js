var CodeFormat = {};


/*
 *对齐注释
 */
CodeFormat.alignComment = function (input) {
    console.log('input:' + input);
    var maxLength = 0;
    var lines = input.split('\n');
    lines.forEach(function (item) {
        item = item.replace('//', '#sp#//');
        var items = item.split('#sp#');
        if (items.length == 2) {
            maxLength = Math.max(maxLength, items[0].length);
        }
    });
    console.log('max length : ' + maxLength);
    var newLines = [];
    var m = /http.?:\/\//;
    lines.forEach(function (item) {
        if(!m.test(item)){
            item = item.replace('//', '#sp#//');   
        }
        var items = item.split('#sp#');
        var newLine = items[0];
        if (items.length == 2) {
            if (items[0].trim().length == 0) {
                newLine += items[1];
            } else {
                var space = maxLength - items[0].length;
                newLine += ' '.repeat(space) + items[1];
            }
        }
        newLines.push(newLine);
    });
    return newLines.join('\n');
};