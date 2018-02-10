<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE html>
<!--
  This is a starter template page. Use this page to start your new project from
  scratch. This page gets rid of all links and provides the needed markup only.
  -->
<html lang="en" ng-app="schemasummaries">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>ABSTAT</title>
    <!-- Tell the browser to be responsive to screen width -->
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
    <!-- OLD -->
    <script src="https://ajax.googleapis.com/ajax/libs/angularjs/1.3.14/angular.min.js"></script>
    <script src="old/js/controllers.js"></script>
    <script src="old/js/ui-bootstrap-tpls-0.12.1.min.js"></script>
    <link rel="stylesheet" href="css/bootstrap.min.css" type = "text/css">
    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" type = "text/css">
    <!-- Ionicons -->
    <link rel="stylesheet" href="css/ionicons.min.css" type = "text/css">
    <!-- Theme style -->
    <link rel="stylesheet" href="css/AdminLTE.min.css">
    <!-- AdminLTE Skins. We have chosen the skin-blue for this starter
      page. However, you can choose any other skin. Make sure you
      apply the skin class to the body tag so the changes take effect. -->
    <link rel="stylesheet" href="css/skinblue.min.css" type = "text/css">
    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
    <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
    <!-- Google Font -->
    <link rel="stylesheet"
      href="https://fonts.googleapis.com/css?family=Source+Sans+Pro:300,400,600,700,300italic,400italic,600italic">
  </head>
  <!--
    BODY TAG OPTIONS:
    =================
    Apply one or more of the following classes to get the
    desired effect
    |---------------------------------------------------------|
    | SKINS         | skin-blue                               |
    |               | skin-black                              |
    |               | skin-purple                             |
    |               | skin-yellow                             |
    |               | skin-red                                |
    |               | skin-green                              |
    |---------------------------------------------------------|
    |LAYOUT OPTIONS | fixed                                   |
    |               | layout-boxed                            |
    |               | layout-top-nav                          |
    |               | sidebar-collapse                        |
    |               | sidebar-mini                            |
    |---------------------------------------------------------|
    -->
  <body class="hold-transition skin-blue fixed sidebar-mini">
    <div class="wrapper">
    <!-- Main Header -->
    <header class="main-header">
      <!-- Logo -->
      <a href="home" class="logo">
        <!-- mini logo for sidebar mini 50x50 pixels -->
        <span class="logo-mini"> <img src="img/bicocca.png" width="50" height="50"> </span>
        <!-- logo for regular state and mobile devices -->
        <span class="logo-lg"><b>ABSTAT</b></span>
      </a>
      <!-- Header Navbar -->
      <nav class="navbar navbar-static-top" role="navigation">
        <!-- Sidebar toggle button-->
        <a href="#" class="sidebar-toggle" data-toggle="push-menu" role="button">
        <span class="sr-only">Toggle navigation</span>
        </a>
      </nav>
    </header>
    <!-- Left side column. contains the logo and sidebar -->
    <aside class="main-sidebar">
      <!-- sidebar: style can be found in sidebar.less -->
      <section class="sidebar">
        <!-- Sidebar Menu -->
        <ul class="sidebar-menu" data-widget="tree">
          <li class="header">ABSTAT</li>
          <!-- Optionally, you can add icons to the links -->
          <li class="active"><a href="home"><i class="fa fa-home"></i> <span>Overview</span></a></li>
          <li class="active"><a href="summarize"><i class="fa fa-gears"></i> <span>Summarize</span></a></li>
          <li class="active"><a href="dataLoading"><i class="fa fa-database"></i> <span>Consolidate</span></a></li>
          <li class="active"><a href="browse"><i class="fa fa-filter"></i> <span>Browse</span></a></li>
          <li class="active"><a href="search"><i class="fa fa-search"></i> <span>Search</span></a></li>
          <li class="active"><a href="management"><i class="fa fa-folder"></i> <span>Manage</span></a></li>
          <li class="active"><a href="apis"><i class="fa fa-link"></i> <span>APIs</span></a></li>
        </ul>
        <!-- /.sidebar-menu -->
      </section>
      <!-- /.sidebar -->
    </aside>
    <!-- Content Wrapper. Contains page content -->
    <div class="content-wrapper">
      <!-- Content Header (Page header) -->
      <section class="content-header">
        <div ng-controller="browse">
          
          <div class="row">
            
            <div class="col-md-12">
              <div class="box box-info">

                <div class="box-header">
                  <h3 class="box-title">Stored Summaries</h3>
                </div>

                <div class="box-body">
                  <form>
                    <table id="example2" class="table table-bordered table-hover">
                      <thead>
                        <tr>
                          <th>Select</th>
                          <th>Dataset</th>
                          <th>Ontology</th>
                          <th>Timestamp</th>
                          <th>Concept Min.</th>
                          <th>Inference</th>
                          <th>Cardinality</th>
                          <th>Property Min.</th>
                        </tr>
                      </thead>
                      <tbody>
                        <c:forEach var="summary" items="${listSummaries}">
                          <tr>
                            <td class = "text-center">
                              <input type="radio" ng-model="selected_graph" name="readAnswer" value="${summary.id}" />
                            </td>
                            <td>${summary.dsName}</td>
                            <td>${summary.listOntNames.get(0)}</td>
                            <td>${summary.timestamp}</td>
                            <td class = "text-center">
                              <c:choose>
                                <c:when test = "${summary.tipoMinimo == true}">
                                  <span class="glyphicon glyphicon-ok text-success" style="color:green"></span>
                                </c:when>
                                <c:otherwise>
                                  <span class="glyphicon glyphicon-remove text-danger" style="color:red"></span>
                                </c:otherwise>
                              </c:choose>
                            </td>
                            <td class = "text-center">
                              <c:choose>
                                <c:when test = "${summary.inferences == true}">
                                  <span class="glyphicon glyphicon-ok text-success" style="color:green"></span>
                                </c:when>
                                <c:otherwise>
                                  <span class="glyphicon glyphicon-remove text-danger" style="color:red"></span>
                                </c:otherwise>
                              </c:choose>
                            </td>
                            <td class = "text-center">
                              <c:choose>
                                <c:when test = "${summary.cardinalita == true}">
                                  <span class="glyphicon glyphicon-ok text-success" style="color:green"></span>
                                </c:when>
                                <c:otherwise>
                                  <span class="glyphicon glyphicon-remove text-danger" style="color:red"></span>
                                </c:otherwise>
                              </c:choose>
                            </td>
                            <td class = "text-center">
                              <c:choose>
                                <c:when test = "${summary.propertyMinimaliz == true}">
                                  <span class="glyphicon glyphicon-ok text-success" style="color:green"> </span>
                                </c:when>
                                <c:otherwise>
                                  <span class="glyphicon glyphicon-remove text-danger" style="color:red"></span>
                                </c:otherwise>
                              </c:choose>
                            </td>
                          </tr>
                        </c:forEach>
                      </tbody>
                    </table>
                  </form>
                </div>

                <div class="box-footer">
                  <form class="form-inline">
                    <div class="form-group">
                      <button type="button" ng-click='loadPatterns()' ng-disabled="loadingSummary" class="btn btn-primary">
                      <span ng-hide="loadingSummary">view</span>
                      <span ng-show="loadingSummary">view  <i class="fa fa-spinner fa-spin"></i></span>
                      </button>
                    </div>
                  </form>
                </div>
              </div>

            </div>

            <br><br>
            <div ng-show="graph_was_selected">
              <div class="col-md-12">
                <div class="box box-info">
                  <div class="box-body">
                    <div style="margin-top:0.0cm" class="row">
                      <div class="col-md-12">
                        <table class="table table-bordered table-hover table-striped">
                          <thead>
                            <tr>
                              <th></th>
                              <th>subject type <small>(occurrences)</small></th>
                              <th>predicate <small>(occurrences)</small></th>
                              <th>object type <small>(occurrences)</small></th>
                              <th>frequency</th>
                              <th>instances</th>
                              <th>Max subjs-obj</th>
                              <th>Avg subjs-obj</th>
                              <th>Min subjs-obj</th>
                              <th>Max subj-objs</th>
                              <th>Avg subj-objs</th>
                              <th>Min subj-objs</th>
                            </tr>
                          </thead>
                          <tbody>
                            <tr>
                              <td class="text-center" style="width:0cm"><button type="submit" ng-click='filterPatterns()' ng-disabled="loadingSummary" class="btn btn-primary">
                                <span ng-hide="loadingSummary">filter</span>
                                <span ng-show="loadingSummary">filter  <i class="fa fa-spinner fa-spin"></i></span>
                                </button>
                              </td>
                              <script type="text/ng-template" id="autocomplete-template.html">
                                <a>
                                  {{match.model.local | isDatatype}}{{match.model.local | isObject}}
                                            <span bind-html-unsafe="match.label | typeaheadHighlight:query"></span>
                                    </a>
                              </script>
                              <td><input type="text" typeahead="subject as subject.global for subject in autocomplete.subject | filter:$viewValue | limitTo:7" typeahead-template-url="autocomplete-template.html" ng-model="subject" class="form-control" placeholder="subject"></td>
                              <td><input type="text" typeahead="predicate as predicate.global for predicate in autocomplete.predicate | filter:$viewValue | limitTo:7" typeahead-template-url="autocomplete-template.html" ng-model="predicate" class="form-control" placeholder="predicate"></td>
                              <td><input type="text" typeahead="object as object.global for object in autocomplete.object | filter:$viewValue | limitTo:7" typeahead-template-url="autocomplete-template.html" ng-model="object" class="form-control" placeholder="object"></td>
                            </tr>
                            <tr ng-repeat="summary in summaries">
                              <td class = "text-center"><span class="fa fa-{{summary.subType | asIcon}}" aria-hidden="true"></span></td>
                              <td>
                                <a target="_blank" ">{{summary.subject.globalURL | prefixed}}</a>
                                <small ng-show="summary.subject.frequency">({{summary.subject.frequency}})</small>
                              </td>
                              <td>
                                {{summary.type | isDatatype}}{{summary.type | isObject}}
                                <a target="_blank" ">{{summary.predicate.globalURL  | prefixed}}</a>
                                <small ng-show="summary.predicate.frequency">({{summary.predicate.frequency}})</small>
                              </td>
                              <td>
                                <a target="_blank" ">{{summary.object.globalURL  | prefixed}}</a>
                                <small ng-show="summary.object.frequency">({{summary.object.frequency}})</small>
                              </td>
                              <td>{{summary.frequency}}</td>
                              <td>{{summary.numberOfInstances != null && summary.numberOfInstances || '-' }}</td>
                              <td>{{summary.cardinality1 != null && summary.cardinality1 || '-' }}</td>
                              <td>{{summary.cardinality2 != null && summary.cardinality2 || '-' }}</td>
                              <td>{{summary.cardinality3 != null && summary.cardinality3 || '-' }}</td>
                              <td>{{summary.cardinality4 != null && summary.cardinality4 || '-' }}</td>
                              <td>{{summary.cardinality5 != null && summary.cardinality5 || '-' }}</td>
                              <td>{{summary.cardinality6 != null && summary.cardinality6 || '-' }}</td>
                            </tr>
                          </tbody>
                        </table>
                        <button ng-click="loadMore()" type="button" ng-disabled="loadingSummary" class="btn btn-deafult btn-block">
                        <span ng-hide="loadingSummary"><strong>{{summaries.length}}</strong> patterns found - get more</span>
                        <span ng-show="loadingSummary"><strong>{{summaries.length}}</strong> patterns found - get more  <i class="fa fa-spinner fa-spin"></i></span>
                        </button>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <br><br>

          </div>
        </div>
      </section>
      <!-- /.content -->
    </div>
    <!-- /.content-wrapper -->
    <!-- Main Footer -->
    <footer class="main-footer">
      <!-- To the right -->
      <div class="pull-right hidden-xs">
        Anything you want
      </div>
      <!-- Default to the left -->
      <strong>Copyright &copy; 2016 <a href="#">Company</a>.</strong> All rights reserved.
    </footer>
    <!-- Control Sidebar -->
    <aside class="control-sidebar control-sidebar-dark">
      <!-- Create the tabs -->
      <ul class="nav nav-tabs nav-justified control-sidebar-tabs">
        <li class="active"><a href="#control-sidebar-home-tab" data-toggle="tab"><i class="fa fa-home"></i></a></li>
        <li><a href="#control-sidebar-settings-tab" data-toggle="tab"><i class="fa fa-gears"></i></a></li>
      </ul>
    </aside>
    <!-- /.control-sidebar -->
    <!-- Add the sidebar's background. This div must be placed
      immediately after the control sidebar -->
    <div class="control-sidebar-bg"></div>
    <!-- ./wrapper -->
    <!-- REQUIRED JS SCRIPTS -->
    <!-- jQuery 3 -->
    <script src="jquery/jquery.min.js"></script>
    <!-- Bootstrap 3.3.7 -->
    <script src="jquery/bootstrap.min.js"></script>
    <!-- AdminLTE App -->
    <script src="jquery/adminlte.min.js"></script>
    <!-- Optionally, you can add Slimscroll and FastClick plugins.
      Both of these plugins are recommended to enhance the
      user experience. -->
  </body>
</html>
