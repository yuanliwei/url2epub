function parse(body){
	var b = $(body);
	var result = b.find('p');
	return result.html();
}