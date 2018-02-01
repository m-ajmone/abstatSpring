<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%><!DOCTYPE html>
<!--
  This is a starter template page. Use this page to start your new project from
  scratch. This page gets rid of all links and provides the needed markup only.
  -->
<html>
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>ABSTAT</title>
    <!-- Tell the browser to be responsive to screen width -->
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
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
      <!-- Main content -->
      <section class="content">
        <div class="row">
          <div class="col-xs-12">
            <div class="box box-info">
              
              <div class="box-header">
                <h3 class="box-title">Cached Summaries</h3>
              </div>

              <!-- /.box-header -->
              <form:form action = "indexingLoading" modelAttribute = "indexingReq" method = "POST" >
                <div class="box-body">
                  <table id="example2" class="table table-bordered table-hover table-striped">
                   
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
                        <th>Stored</th>
                        <th>Indexed</th>
                      </tr>
                    </thead>

                    <tbody>
                      <c:forEach var="summary" items="${listSummaries}">
                        <tr>
                          <td class = "text-center">
                            <form:radiobutton name="readAnswer" path="idSummary" value="${summary.id}" />
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
                          <td class = "text-center">
                            <c:choose>
                              <c:when test = "${summary.loadedMongoDB == true}">
                                <span class="glyphicon glyphicon-ok text-success" style="color:green"></span>
                              </c:when>
                              <c:otherwise>
                                <span class="glyphicon glyphicon-remove text-danger" style="color:red"></span>
                              </c:otherwise>
                            </c:choose>
                          </td>
                          <td class = "text-center">
                            <c:choose>
                              <c:when test = "${summary.indexedSolr == true}">
                                <span class="glyphicon glyphicon-ok text-success" style="color:green"></span>
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

                </div>

                <div class="box-body">
                  <table class="table table-bordered table-hover">
                    <thead>
                      <tr>
                        <th>Actions</th>
                      </tr>
                    </thead>
                    <tbody>
                      <div class="form-group">
                        <tr>
                          <div class="checkbox">
                            <td>
                              <form:checkbox path="loadOnMongoDB"/>
                              &nbsp Store on the document database
                            </td>
                          </div>
                        </tr>
                        <tr>
                          <div class="checkbox">
                            <td>
                              <form:checkbox path="indexOnSolr"/>
                              &nbsp Index on the search engine
                            </td>
                          </div>
                        </tr>
                      </div>
                    </tbody>
                  </table>
                </div>

                <div class="box-footer">
                  <input type="text" name="domain" placeholder="Insert a domain" />
                  <button type="submit" class="btn btn-primary">Submit</button>
                </div>

              </form:form>

              <!-- /.box-body -->
            </div>
            <!-- /.box -->
          </div>
          <!-- /.col -->
        </div>
        <!-- /.row -->
      </section>
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
