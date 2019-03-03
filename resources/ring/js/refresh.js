var ____pageLoadTime = new Date().getTime()

function ____reloadIfSourceChanged() {
  var request = new XMLHttpRequest()
  request.onreadystatechange = function() {
    if (request.readyState == 4) {
      if (request.responseText == 'true') {
        window.location.reload()
      }
      else {
        setTimeout(____reloadIfSourceChanged, <%= refresh-time %>)
      }
    }
  }
  request.open('GET', '/____source_changed?since=' + ____pageLoadTime, true)
  request.send()
}

function ____ready(fn) {
  if (document.attachEvent ? document.readyState === "complete" : document.readyState !== "loading"){
    fn();
  } else {
    document.addEventListener('DOMContentLoaded', fn);
  }
}

____ready(____reloadIfSourceChanged)
