/**
 * 控件初始化。
 */
define(['bootstrap', 'jquery-cookie', 'jquery-slimscroll', 'jquery-uniform'],
  function() {
    var collapsed = false; // sidebar collapsed
    var is_mobile = false; // is screen mobile?
    var is_mini_menu = false; // is mini-menu activated
    var is_fixed_header = false; // is fixed header activated
    var responsiveFunctions = []; // responsive function holder

    /**
     * 运行响应式函数（页面尺寸发生变化时需要调用的函数）。
     */
    var runResponsiveFunctions = function() {
      // reinitialize other subscribed elements
      for ( var i in responsiveFunctions) {
        var each = responsiveFunctions[i];
        each.call();
      }
    };

    var getViewPort = function() {
      var e = window, a = 'inner';
      if (!('innerWidth' in window)) {
        a = 'client';
        e = document.documentElement || document.body;
      }
      return {
        width: e[a + 'Width'],
        height: e[a + 'Height']
      }
    };

    var checkLayout = function() {
      // Check if sidebar has mini-menu
      is_mini_menu = $('#sidebar').hasClass('mini-menu');
      // Check if fixed header is activated
      is_fixed_header = $('#header').hasClass('navbar-fixed-top');
    };

    var updateSidebarHeight = function() {
//      var bodyHeight = $('body').height();
//      var headerHeight = $('header.navbar').height();
//      var menuHeight = bodyHeight - headerHeight;
      var body = document.body;
      var html = document.documentElement;
      var height = Math.max( body.scrollHeight, body.offsetHeight, 
                         html.clientHeight, html.scrollHeight, html.offsetHeight );
      $('.sidebar-menu').height(html.scrollHeight);
    };

    var updateCollapseIndicator = function() {
      var iconElem = $('.sidebar-indicator i');
      var iconLeft = iconElem.attr("data-icon1");
      var iconRight = iconElem.attr("data-icon2");
      if (collapsed) {
        $('.copyright').hide();
        $('.sidebar-indicator i').removeClass(iconLeft);
        $('.sidebar-indicator i').addClass(iconRight);
      } else {
        $('.copyright').show();
        $('.sidebar-indicator i').removeClass(iconRight);
        $('.sidebar-indicator i').addClass(iconLeft);
      }
      $('.sidebar-footer').show();
    };

    var handleSidebarAndContentHeight = function() {
      var content = $('#content');
      var sidebar = $('#sidebar');
      var body = $('body');
      var height;
      if (body.hasClass('sidebar-fixed')) {
        height = $(window).height() - $('#header').height() + 1;
      } else {
        height = sidebar.height();
      }
      if (height >= content.height()) {
        content.attr('style', 'min-height:' + height + 'px !important');
      }
    };

    var handleSidebar = function() {
      $('.sidebar-menu .has-sub > a').click(function() {
        var last = $('.has-sub.open', $('.sidebar-menu'));
        last.removeClass("open");
        $('.arrow', last).removeClass("open");
        $('.sub', last).slideUp(200);

        var thisElement = $(this);
        var slideOffeset = -200;
        var slideSpeed = 200;

        var sub = $(this).next();
        if (sub.is(":visible")) {
          $('.arrow', $(this)).removeClass("open");
          $(this).parent().removeClass("open");
          sub.slideUp(slideSpeed, function() {
            if ($('#sidebar').hasClass('sidebar-fixed') == false) {
              App.scrollTo(thisElement, slideOffeset);
            }
            handleSidebarAndContentHeight();
          });
        } else {
          $('.arrow', $(this)).addClass("open");
          $(this).parent().addClass("open");
          sub.slideDown(slideSpeed, function() {
            if ($('#sidebar').hasClass('sidebar-fixed') == false) {
              App.scrollTo(thisElement, slideOffeset);
            }
            handleSidebarAndContentHeight();
          });
        }
      });

      // Handle sub-sub menus
      $('.sidebar-menu .has-sub .sub .has-sub-sub > a').click(function() {
        var last = $('.has-sub-sub.open', $('.sidebar-menu'));
        last.removeClass("open");
        $('.arrow', last).removeClass("open");
        $('.sub', last).slideUp(200);

        var sub = $(this).next();
        if (sub.is(":visible")) {
          $('.arrow', $(this)).removeClass("open");
          $(this).parent().removeClass("open");
          sub.slideUp(200);
        } else {
          $('.arrow', $(this)).addClass("open");
          $(this).parent().addClass("open");
          sub.slideDown(200);
        }
      });
    };

    var collapseSidebar = function() {
      $('.copyright').hide();
      var iconElem = $('.sidebar-indicator i');
      var iconLeft = iconElem.attr("data-icon1");
      var iconRight = iconElem.attr("data-icon2");
      /* For Navbar */
      $('.navbar-brand').addClass("mini-menu");
      /* For sidebar */
      $('#sidebar').addClass("mini-menu");
      $('#main-content').addClass("margin-left-50");
      $('.sidebar-indicator i').removeClass(iconLeft);
      $('.sidebar-indicator i').addClass(iconRight);
      /* Remove placeholder from Search Bar */
      $('.search').attr('placeholder', '');
      collapsed = true;
      /* Set a cookie so that mini-sidebar persists */
      $.cookie('mini_sidebar', '1');
    }

    var responsiveSidebar = function() {
      // Handle sidebar collapse on screen width
      var width = $(window).width();
      if (width < 768) {
        is_mobile = true;
        collapseSidebar();
      } else {
        is_mobile = false;
        var menu = $('.sidebar');
        if (menu.parent('.slimScrollDiv').size() === 1) {
          menu.slimScroll({
            destroy: true
          });
          menu.removeAttr('style');
          $('#sidebar').removeAttr('style');
        }
      }
    };

    var handleSidebarCollapse = function() {
      var viewport = getViewPort();
      if ($.cookie('mini_sidebar') === '1') {
        /* For Navbar */
        $('.navbar-brand').addClass("mini-menu");
        /* For sidebar */
        $('#sidebar').addClass("mini-menu");
        $('#main-content').addClass("margin-left-50");
        collapsed = true;
      }
      // Handle sidebar collapse on user interaction
      $('.sidebar-indicator').click(function() {
        // Handle mobile sidebar toggle
        if (is_mobile && !(is_mini_menu)) {
          // If sidebar is collapsed
          if (collapsed) {
            $('.copyright').show();
            $('body').removeClass("slidebar");
            $('.sidebar').removeClass("sidebar-fixed");
            // Add fixed top nav if exists
            if (is_fixed_header) {
              $('#header').addClass("navbar-fixed-top");
              $('#main-content').addClass("margin-top-100");
            }
            collapsed = false;
            $.cookie('mini_sidebar', '0');
          } else {
            $('.copyright').hide();
            $('body').addClass("slidebar");
            $('.sidebar').addClass("sidebar-fixed");
            // Remove fixed top nav if exists
            if (is_fixed_header) {
              $('#header').removeClass("navbar-fixed-top");
              $('#main-content').removeClass("margin-top-100");
            }
            collapsed = true;
            $.cookie('mini_sidebar', '1');
            handleMobileSidebar();
          }
        } else { // Handle regular sidebar toggle
          var iconElem = $('.sidebar-indicator i');
          var iconLeft = iconElem.attr("data-icon1");
          var iconRight = iconElem.attr("data-icon2");
          // If sidebar is collapsed
          if (collapsed) {
            $('.copyright').show();
            /* For Navbar */
            $('.navbar-brand').removeClass("mini-menu");
            /* For sidebar */
            $('#sidebar').removeClass("mini-menu");
            $('#main-content').removeClass("margin-left-50");
            $('.sidebar-indicator i').removeClass(iconRight);
            $('.sidebar-indicator i').addClass(iconLeft);
            /* Add placeholder from Search Bar */
            $('.search').attr('placeholder', "Search");
            collapsed = false;
            $.cookie('mini_sidebar', '0');
          } else {
            $('.copyright').hide();
            /* For Navbar */
            $('.navbar-brand').addClass("mini-menu");
            /* For sidebar */
            $('#sidebar').addClass("mini-menu");
            $('#main-content').addClass("margin-left-50");
            $('.sidebar-indicator i').removeClass(iconLeft);
            $('.sidebar-indicator i').addClass(iconRight);
            /* Remove placeholder from Search Bar */
            $('.search').attr('placeholder', '');
            collapsed = true;
            $.cookie('mini_sidebar', '1');
          }
          $("#main-content").on('resize', function(e) {
            e.stopPropagation();
          });
        }
      });
    };

    var handleMobileSidebar = function() {
      var menu = $('.sidebar');
      if (menu.parent('.slimScrollDiv').size() === 1) {
        menu.slimScroll({
          destroy: true
        });
        menu.removeAttr('style');
        $('#sidebar').removeAttr('style');
      }
      menu.slimScroll({
        size: '7px',
        color: '#a1b2bd',
        opacity: .3,
        height: "100%",
        allowPageScroll: false,
        disableFadeOut: false
      });
    };

    var handleFixedSidebar = function() {
      var menu = $('.sidebar-menu');

      if (menu.parent('.slimScrollDiv').size() === 1) { // destroy existing
        // instance before
        // updating the height
        menu.slimScroll({
          destroy: true
        });
        menu.removeAttr('style');
        $('#sidebar').removeAttr('style');
      }

      if ($('.sidebar-fixed').size() === 0) {
        handleSidebarAndContentHeight();
        return;
      }

      var viewport = getViewPort();
      if (viewport.width >= 992) {
        var sidebarHeight = $(window).height() - $('#header').height() + 1;

        menu.slimScroll({
          size: '7px',
          color: '#a1b2bd',
          opacity: .3,
          height: sidebarHeight,
          allowPageScroll: false,
          disableFadeOut: false
        });
        handleSidebarAndContentHeight();
      }
    };

    var handleNavbarFixedTop = function() {
      if (is_mobile && is_fixed_header) {
        // Manage margin top
        $('#main-content').addClass('margin-top-100');
      }
      if (!(is_mobile) && is_fixed_header) {
        // Manage margin top
        $('#main-content').removeClass('margin-top-100').addClass(
          'margin-top-50');
      }
    };

    var handleSlimScrolls = function() {
      if (!$().slimScroll) { return; }
      $('.scroller').each(
        function() {
          $(this).slimScroll(
            {
              size: '7px',
              color: '#a1b2bd',
              height: $(this).attr("data-height"),
              alwaysVisible: ($(this).attr("data-always-visible") == "1" ? true
                : false),
              railVisible: ($(this).attr("data-rail-visible") == "1" ? true
                : false),
              railOpacity: 0.1,
              disableFadeOut: true
            });
        });
    };

    var handlePopovers = function() {
      $('.pop').popover();
      $('.pop-bottom').popover({
        placement: 'bottom'
      });
      $('.pop-left').popover({
        placement: 'left'
      });
      $('.pop-top').popover({
        placement: 'top'
      });
      $('.pop-hover').popover({
        trigger: 'hover'
      });
    };

    var handleUniform = function() {
      $(".uniform").uniform();
    };

    var handleThemeSkins = function() {
      // Handle theme colors
      var setSkin = function(color) {
        $('#skin-switcher').attr("href", "css/themes/" + color + ".css");
        $.cookie('skin_color', color);
      }
      $('ul.skins > li a').click(function() {
        var color = $(this).data("skin");
        setSkin(color);
      });

      // Check which theme skin is set
      if ($.cookie('skin_color')) {
        setSkin($.cookie('skin_color'));
      }
    };

    $(window).resize(function() {
      setTimeout(function() {
        checkLayout();
        updateSidebarHeight();
        handleSidebarAndContentHeight();
        responsiveSidebar();
        handleFixedSidebar();
        handleNavbarFixedTop();
        runResponsiveFunctions();
      }, 50); // wait 50ms until window resize finishes.
    });

    checkLayout();
    handleSidebar();
    handleSidebarCollapse();
    updateSidebarHeight();
    updateCollapseIndicator();
    handleSidebarAndContentHeight();
    responsiveSidebar();
    handleFixedSidebar();
    handleNavbarFixedTop();
    handleThemeSkins();
    handleSlimScrolls();
    handlePopovers();
    handleUniform();
  });