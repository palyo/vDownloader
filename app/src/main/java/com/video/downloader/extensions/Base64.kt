package com.video.downloader.extensions

fun getBase64StringFromBlobUrl(blobUrl: String): String {
    return if (blobUrl.startsWith("blob")) {
        "javascript: var xhr = new XMLHttpRequest();" +
                "xhr.open('GET', '" + blobUrl + "', true);" +
                "xhr.setRequestHeader('Content-type','application/pdf');" +
                "xhr.responseType = 'blob';" +
                "xhr.onload = function(e) {" +
                "    if (this.status == 200) {" +
                "        var blobPdf = this.response;" +
                "        var reader = new FileReader();" +
                "        reader.readAsDataURL(blobPdf);" +
                "        reader.onloadend = function() {" +
                "            base64data = reader.result;" +
                "            Android.getBase64FromBlobData(base64data);" +
                "        }" +
                "    }" +
                "};" +
                "xhr.send();"
    } else "javascript: console.log('It is not a Blob URL');"
}