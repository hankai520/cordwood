require.config({
  paths: {
    'jquery': 'libs/jquery-2.0.3.min'
  }
});

requirejs('jquery', function() {
  var seconds = 5;
  $('#seconds').text(seconds);
  var timer = setInterval('countdown()', 1000);
  function countdown() {
    if (seconds == 1) {
      window.history.back();
      clearInterval(timer);
    } else {
      seconds--;
      $('#seconds').text(seconds);
    }
  }
});