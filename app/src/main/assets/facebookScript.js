var imgData = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAEAAAABACAYAAACqaXHeAAAACXBIWXMAAAHYAAAB2AH6XKZyAAAAGXRFWHRTb2Z0d2FyZQB3d3cuaW5rc2NhcGUub3Jnm+48GgAABHdJREFUeJztm01sVFUUx3/nTtvptGihHxEQiAETkH4AAnGhC4sJFCWgaRhjNBAXxoUx0cTExJhQSNyYuDIuXJhYFpKWEEIjQhUEP8IGEGupSqgVtZWYCk0z/ZhpZ95xwdRMmQIP+t69Fvtbzdx359z/+efN3Hve3Cv4ZFNXa3lhRrYhWg/UAkuAeYD4jRESCgwAvwOdonzpMX7ocN3zA34+fEvxW75vvV8j7BZ4AYhOT6s1UqjsNUXpprYVz/15s443NGDtmQ8L50fn7RJ4HSgJXKIdRkTkvUR/5Z6T9fXpqTpMacC12539CBvC1WcJ4ZvMGI1HHo7351+6jm2d+xZ7RE4oLLOjzg4Cv6S9yONHVjX25rab3DfbT7XGMkQO3G3JAygsi0imbWPH3tLc9kkGJO+lGVhvVZlNhDVFpvijyU1ZtvzQ8iQih+2rso+KNhyuebYdsndAkzYZRN5xK8seovJukzYZyBpwurO6EVjtVJVd6s52PfQMZA0wojvd6rGPerIDQDZf/CwaSQ4NADHHmmwzkimeU14QGRtahYXkd95Xy2Nli331/XbwD5r/6gxZESWSHKktEI/lGvZQwNLYXOYXld66Y7avDYzqcuOJVFgZ7T+IiFdhjKfFroU4Q6XEqIjret4ZKiLm1t3ubmYNcC3ANbMGuBbgmlkDXAtwzawBrgW4ZtYA1wJc8783oCDIYAuK5tBQvpQSU5h3bUm0zHecJdEyXlm4Nq99xBvn6NUeLo8NTUtnLoEa8Nqi9awsqZx2nMrCGA3lS6e8tqKkgjd7Tkx7jAkC/QokMmNBhrMyRqAGvN93hkvJwSBDTqI3leCDvrOBxgzUgMF0ircvfRWKCb2pBG/9epKBdDLQuIHPAmGYEFbyENI0GKQJYSYPIa4DgjAh7OQh5IXQdEywkTxYWAneiQm2kgdLS+HbMcFm8mCxFvBjgu3kwXIxdDMTXCQPDqrBqUxwlTw4KodzTXCZPARcDd4Og+kUb/QcByDlZVzJcGcAuE18gtknQkEGqymtYlH0niBD5tGbSnB+OG/L7x1TIKqqAe0QeHnBGh4o9v/o6064lBzk1e7PA4klqmo8I4H9/NrYaRHoGKIjxqheCTLmTELVXClQwwW8YAJ2jw4QNeFOLN2jvk7C+MITuSDbu1qLRj2uAv72sN09DMcM5WZ/dXwMCO458wxBhWP7q+NjBkBEP3asxzqiNEN2IbS2+qeDwHdOFdmlY13Nj4cgZ1Z56nzLJlE56k6TPQxsbKuNf5F9fY3sCYoWZ6psIXwykTxcVwvEErwInLYuyhbKOUnGXsptyltYbe44sChiMieAB60Js8NFMtR/ujrel9uYVw0eWdXYO254BOG4PW3hIvB1ZpxHr08eblAOt1fHrw71VzWosAcYDl1heAyDNCX+rnpiqlOj4KO22PrzvoWajuxSZQcwU7bWJxVtTkcKdrevbLx8s46+i6unzx2cmy5MbxWPDYrWIf8en3f9UMXj2vH530SlE9Hjoynajq2L+/on5h+gHdQv8SWwkgAAAABJRU5ErkJggg==";
var videos = document.getElementsByTagName('video');

for (var i = 0; i < videos.length; i++) {
    (function(index) {
        var video = videos[index];
        var parent = video.parentElement;
        var postUrl = getParentPostUrl(parent);
        var src = parent.getAttribute('src') || parent.getAttribute('data-src') || video.getAttribute('src');
        var filename = 'video.mp4';

        if (postUrl) {
            var postId = postUrl.substring(postUrl.lastIndexOf('/') + 1);
            filename = 'video_' + postId + '.mp4';
        }

        // Check if the link already exists
        if (!parent.querySelector('.download-icon')) {
            var link = document.createElement('a');
            link.href = src;
            link.download = filename;
            link.className = 'download-icon'; // Add a class to the link for easier reference
            link.style.position = 'absolute';
            link.style.top = '16px';
            link.style.right = '16px';
            link.style.zIndex = '999';
            link.innerHTML = '<img src="' + imgData + '" style="width: 42px; height: 42px;">';
            video.parentElement.appendChild(link);
            parent.insertBefore(link, video);

            link.addEventListener('click', function(event) {
                event.preventDefault();
                event.stopPropagation();
                window.android.downloadFacebookVideo(src);
                return false;
            });
        }
    })(i);
}

function getParentPostUrl(element) {
    while (element) {
        console.log("checking element:", element);
        // Check for data-post-url attribute
        if (element.dataset.postUrl) {
            console.log("found post URL:", element.dataset.postUrl);
            return element.dataset.postUrl;
        }
        // Check for href attribute that includes "/watch/?v"
        if (element.href && element.href.includes("/watch/?v")) {
            const url = new URL(element.href);
            // Extract the post URL from the href
            console.log("found post URL:", url.href);
            return url.href;
        }
        // Traverse up the DOM
        element = element.parentElement;
    }
    console.log("could not find post URL");
    return null;
}
