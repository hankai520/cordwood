<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="t" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<t:admin>
    <jsp:attribute name="linkResources">
        
    </jsp:attribute>
    <jsp:attribute name="linkScripts">
        
        <!-- FLOT CHARTS -->
        <script src="/js/flot/jquery.flot.min.js"></script>
        <script src="/js/flot/jquery.flot.time.min.js"></script>
        <script src="/js/flot/jquery.flot.selection.min.js"></script>
        <script src="/js/flot/jquery.flot.resize.min.js"></script>
        <script src="/js/flot/jquery.flot.pie.min.js"></script>
        <script src="/js/flot/jquery.flot.stack.min.js"></script>
        <script src="/js/flot/jquery.flot.crosshair.min.js"></script>
        <!-- BLOCK UI -->
        <script type="text/javascript" src="/js/jQuery-BlockUI/jquery.blockUI.min.js"></script>
    </jsp:attribute>
    <jsp:attribute name="pageDidLoad">
        <script>
            jQuery(document).ready(function() {     
                App.setPage("dashboard");  //Set current page
                App.init(); //Initialise plugins and elements
            });
        </script>
    </jsp:attribute>

    <jsp:body>
        <!-- SAMPLE BOX CONFIGURATION MODAL FORM-->
        <div class="modal fade" id="box-config" tabindex="-1" role="dialog"
                aria-labelledby="myModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                        <h4 class="modal-title">Box Settings</h4>
                    </div>
                    <div class="modal-body">Here goes box setting content.</div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                        <button type="button" class="btn btn-primary">Save changes</button>
                    </div>
                </div>
            </div>
        </div>
        <!-- /SAMPLE BOX CONFIGURATION MODAL FORM-->
        <div class="container">
            <div class="row">
                <div id="content" class="col-lg-12">
                    <!-- PAGE HEADER-->
                    <div class="row">
                        <div class="col-sm-12">
                            <div class="page-header">
                                <!-- STYLER -->
    
                                <!-- /STYLER -->
                                <!-- BREADCRUMBS -->
                                <ul class="breadcrumb">
                                    <li><i class="fa fa-home"></i> <a href="index.html">Home</a></li>
                                    <li>Dashboard - shared on weidea.net</li>
                                </ul>
                                <div class="description">Overview, Statistics and more</div>
                            </div>
                        </div>
                    </div>
                    <!-- /PAGE HEADER -->
                    <!-- DASHBOARD CONTENT -->
                    <div class="row">
                        <!-- COLUMN 1 -->
                        <div class="col-md-6">
                            <div class="box solid grey">
                                <div class="box-title">
                                    <h4>
                                        <i class="fa fa-dollar"></i>Revenue
                                    </h4>
                                    <div class="tools">
                                        <span class="label label-danger"> 20% <i
                                                class="fa fa-arrow-up"></i>
                                        </span> <a href="#box-config" data-toggle="modal" class="config"> <i
                                                class="fa fa-cog"></i>
                                        </a> <a href="javascript:;" class="reload"> <i
                                                class="fa fa-refresh"></i>
                                        </a> <a href="javascript:;" class="collapse"> <i
                                                class="fa fa-chevron-up"></i>
                                        </a> <a href="javascript:;" class="remove"> <i
                                                class="fa fa-times"></i>
                                        </a>
                                    </div>
                                </div>
                                <div class="box-body">
                                    <div id="chart-revenue" style="height: 240px"></div>
                                </div>
                            </div>
                        </div>
                        <!-- /COLUMN 2 -->
                    </div>
                </div>
                <!-- /CONTENT-->
            </div>
        </div>
    </jsp:body>
</t:admin>