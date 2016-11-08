
/*-----------------------------------------------------------------------------------*/
/* Box tools
/*-----------------------------------------------------------------------------------*/
var handleBoxTools = function() {
  //Collapse
  jQuery('.box .tools .collapse, .box .tools .expand').click(function() {
    var el = jQuery(this).parents(".box").children(".box-body");
    if (jQuery(this).hasClass("collapse")) {
      jQuery(this).removeClass("collapse").addClass("expand");
      var i = jQuery(this).children(".fa-chevron-up");
      i.removeClass("fa-chevron-up").addClass("fa-chevron-down");
      el.slideUp(200);
    } else {
      jQuery(this).removeClass("expand").addClass("collapse");
      var i = jQuery(this).children(".fa-chevron-down");
      i.removeClass("fa-chevron-down").addClass("fa-chevron-up");
      el.slideDown(200);
    }
  });

  /* Close */
  jQuery('.box .tools a.remove').click(function() {
    var removable = jQuery(this).parents(".box");
    if (removable.next().hasClass('box') || removable.prev().hasClass('box')) {
      jQuery(this).parents(".box").remove();
    } else {
      jQuery(this).parents(".box").parent().remove();
    }
  });
}

handleBoxTools();