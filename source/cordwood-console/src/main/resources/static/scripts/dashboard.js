/**
 * 应用初始化。
 */
requirejs(['main'], function(app) {
  app.loadSidebar('dashboard');
  /**
   * 加载访问次数和流量图表。
   */
  requirejs(['jquery-flot-time', 'datejs'], function() {
    var chartOptions = {
      series: {
        lines: {
          show: true,
          lineWidth: 2,
          fill: true,
          fillColor: {
            colors: [{
              opacity: 0.05
            }, {
              opacity: 0.01
            }]
          }
        },
        points: {
          show: true
        },
        shadowSize: 2
      },
      grid: {
        hoverable: true,
        clickable: false,
        tickColor: "#eee",
        borderWidth: 0
      },
      colors: ["#DB5E8C", "#F0AD4E"],
      xaxis: {
        mode: 'time',
        ticks: 7,
        tickDecimals: 0
      },
      yaxis: {
        ticks: 6,
        tickDecimals: 0
      }
    };
    var requestCharts = $.plot('#request-charts', [], chartOptions);
    var showTooltip = function(charts, x, y, contents) {
      var chartW = charts.width(), chartH = charts.height();
      var chartX = charts.offset().left;
      var chartY = charts.offset().top;
      var minW = 200, minH = 40, padding = 20;
      var minX = chartX + padding, maxX = chartX + chartW - minW - padding;
      var minY = chartY + padding, maxY = chartY + chartH - minH - padding;
      if (x < minX) {
        x = minX;
      } else if (x > maxX) {
        x = maxX - padding;
      }
      if (y < minY) {
        y = minY;
      } else if (y > maxY) {
        y = maxY - padding;
      }
      $('<div class="chart-tooltip">' + contents + '</div>').css({
        position: 'absolute',
        top: y,
        left: x,
        border: '1px solid #333',
        padding: '4px',
        color: '#fff',
        'border-radius': '3px',
        'background-color': '#333',
        opacity: 0.80
      }).appendTo("body");
    };

    $("#request-charts").bind("plothover", function(event, pos, item) {
      var previousPoint = null;
      if (item) {
        if (previousPoint != item.dataIndex) {
          previousPoint = item.dataIndex;
          $(".chart-tooltip").remove();
          var date = new Date(item.datapoint[0]).toString('yyyy-MM-dd');
          var contents = null;
          if (item.seriesIndex == 0) {// 访问次数
            contents = date + ': ' + item.datapoint[1] + ' 次插件访问';
          } else if (item.seriesIndex == 1) {// 数据流量
            contents = date + ': ' + item.datapoint[1].toFixed(2) + 'kb 数据被传输';
          }
          if (contents) {
            showTooltip($(this), item.pageX, item.pageY, contents);
          }
        }
      } else {
        $(".chart-tooltip").remove();
        previousPoint = null;
      }
    });

    // 加载访问次数及流量数据
    var loadData = function(container) {
      app.blockUI(container);
      $.get('/admin/dashboard/request_charts.json',
        function(data, status, xhr) {
          if (xhr.status == 200 && data) {
            var requestCount = [], requestVolume = [];
            for (var i = 0; i < data.length; i++) {
              var item = data[i];
              requestCount.push([item.createDate, item.count]);
              requestVolume.push([item.createDate, item.bytes / 1024]);
            }
            requestCharts.setData([{
              data: requestCount,
              label: '访问次数'
            }, {
              data: requestVolume,
              label: '数据流量 (kb)'
            }]);
            requestCharts.setupGrid();
            requestCharts.draw();
          }
          app.unblockUI(container);
        });
    };
    var container = $('#request-charts').parents(".box");
    loadData(container);
    // 处刷新
    $('#reloadRequestChart').click(function() {
      loadData(container);
    });
  });

  /**
   * 初始化服务器资源仪表。
   * */
  requirejs(['raphael'], function(Raphael) {
    window.Raphael = Raphael;
    requirejs(['justgage'], function() {
      var responseTime = new JustGage({
        id: "responseTime",
        value: $('#responseTime').attr('value'),
        min: 10,
        max: 100,
        title: "响应时间",
        textRenderer: function() {
          return $('#responseTime').attr('value-desc');
        },
        pointer: true,
        pointerOptions: {
          toplength: -15,
          bottomlength: 10,
          bottomwidth: 12,
          color: '#8e8e93',
          stroke: '#ffffff',
          stroke_width: 3,
          stroke_linecap: 'round'
        },
        gaugeWidthScale: 0.3
      });
      var faultRateValue = $('#faultRate').attr('value');
      var faultRate = new JustGage({
        id: "faultRate",
        value: faultRateValue,
        min: 0,
        max: 100,
        title: "故障率",
        symbol: '%',
        pointer: true,
        pointerOptions: {
          toplength: -15,
          bottomlength: 10,
          bottomwidth: 12,
          color: '#8e8e93',
          stroke: '#ffffff',
          stroke_width: 3,
          stroke_linecap: 'round'
        },
        gaugeWidthScale: 0.3
      });
    });

    requirejs(['bootstrap']);
  });
});
