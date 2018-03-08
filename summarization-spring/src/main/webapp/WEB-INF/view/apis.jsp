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
      <section class="content">

        <div class="col-md-12">
          <div class="box box-info collapsed-box">
            <div class="box-header with-border">
              <h3 class="box-title">api/v1/summaries</h3>
              <div class="box-tools pull-right">
                <button type="button" class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-plus"></i>
                </button>
              </div>
              <!-- /.box-tools -->
            </div>
            <!-- /.box-header -->
            <div class="box-body">
              <div class="col-md-12">
                <div>This API allows a user to list the ID's summaries which match the input parameters</div>
                <div>
                  <table id="example2" class="table table-bordered table-hover table-striped">
                    <thead>
                      <tr>
                        <th style="width:8%"">Parameter</th>
                        <th style="width:12%">Values</th>
                        <th style="width:12%">Example</th>
                        <th style="width:6%">Optional</th>
                        <th>Notes</th>
                      </tr>
                    </thead>
                    <tbody>
                      <tr>
                        <td>loaded</td>
                        <td>boolean value</td>
                        <td>loaded=true</td>
                        <td>yes</td>
                        <td>not specified value is considered as "don't care"</td>
                      </tr>
                      <tr>
                        <td>indexed</td>
                        <td>boolean value</td>
                        <td>indexed=false</td>
                        <td>yes</td>
                        <td>not specified value is considered as "don't care"</td>
                      </tr>
                    </tbody>
                  </table>
                </div>
                <div>Example 1: 
                  <a href="api/v1/summaries">http://localhost/api/v1/summaries</a>
                </div>
                <div>Example 2: 
                  <a href="api/v1/summaries?loaded=true&indexed=false">http://localhost/api/v1/summaries?loaded=true&indexed=false</a>
                </div>
              </div>
            </div>
            <!-- /.box-body -->
          </div>
          <!-- /.box -->
        </div>

        <div class="col-md-12">
          <div class="box box-success collapsed-box">
            <div class="box-header with-border">
              <h3 class="box-title">api/v1/SPO</h3>
              <font size="2" color="grey">(for stored summaries)</font>
              <div class="box-tools pull-right">
                <button type="button" class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-plus"></i>
                </button>
              </div>
              <!-- /.box-tools -->
            </div>
            <!-- /.box-header -->
            <div class="box-body">
              <div class="col-md-12">
                <div>This API allows a user to list all the subject/predicate/object of a given summary that has been previously stored</div>
                <div> 
                  <table id="example2" class="table table-bordered table-hover table-striped">
                    <thead>
                      <tr>
                        <th style="width:8%"">Parameter</th>
                        <th style="width:12%">Values</th>
                        <th style="width:12%">Example</th>
                        <th style="width:6%">Optional</th>
                        <th>Notes</th>
                      </tr>
                    </thead>
                    <tbody>
                      <tr>
                        <td>summary</td>
                        <td>(string) ID summary</td>
                        <td>summary=6d0ed511-f8c9-4b72-8c04-1f4cba9b9210</td>
                        <td>yes</td>
                        <td> - if not specified, will be considered all the stored summaries<br>
                             - IDs can be retrieved using the api/v1/summaries API or the <a href="/home">overview</a> page
                        </td>
                      </tr>
                     
                      <tr>
                        <td>position</td>
                        <td>(string) subject/predicate/object</td>
                        <td>position=predicate</td>
                        <td>no</td>
                        <td>-</td>
                      </tr>

                    </tbody>
                  </table>
                </div>
                <div>Example 1: 
                  <a href="api/v1/SPO?position=object">http://localhost/api/v1/SPO?position=object</a>
                </div>
                <div>Example 2: 
                  <a href="api/v1/SPO?summary=6d0ed511-f8c9-4b72-8c04-1f4cba9b9210&position=predicate">http://localhost/api/v1/SPO?summary=6d0ed511-f8c9-4b72-8c04-1f4cba9b9210&position=predicate</a>
                </div>
              </div>
            </div>
            <!-- /.box-body -->
          </div>
          <!-- /.box -->
        </div>


        <div class="col-md-12">
          <div class="box box-success collapsed-box">
            <div class="box-header with-border">
              <h3 class="box-title">api/v1/browse</h3>
              <font size="2" color="grey">(for stored summaries)</font>
              <div class="box-tools pull-right">
                <button type="button" class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-plus"></i>
                </button>
              </div>
              <!-- /.box-tools -->
            </div>
            <!-- /.box-header -->
            <div class="box-body">
              <div class="col-md-12">
                <div>This API allows a user to list all AKPs which match the input parameters.</div>
                <div>
                  <table id="example2" class="table table-bordered table-hover table-striped">
                    <thead>
                      <tr>
                        <th style="width:8%"">Parameter</th>
                        <th style="width:12%">Values</th>
                        <th style="width:12%">Example</th>
                        <th style="width:6%">Optional</th>
                        <th>Notes</th>
                      </tr>
                    </thead>
                    <tbody>
                      <tr>
                        <td>summary</td>
                        <td>(string) ID summary</td>
                        <td>summary=6d0ed511-f8c9-4b72-8c04-1f4cba9b9210</td>
                        <td>yes</td>
                        <td> - if not specified, will be considered all the stored summaries<br>
                             - IDs can be retrieved using the api/v1/summaries API or the <a href="/home">overview</a> page
                        </td>
                      </tr>
                      <tr>
                        <td>subj</td>
                        <td>(string) concept URI</td>
                        <td>subj=http://schema.org/Place</td>
                        <td>yes</td>
                        <td>values can be retrieved using the api/v1/SPO API</td>
                      </tr>
                      <tr>
                        <td>pred</td>
                        <td>(string) predicate URI</td>
                        <td>pred=http://xmlns.com/foaf/0.1/name</td>
                        <td>yes</td>
                        <td>values can be retrieved using the api/v1/SPO API</td>
                      </tr>
                      <tr>
                        <td>obj</td>
                        <td>(string) concept URI</td>
                        <td>obj=http://dbpedia.org/ontology/City</td>
                        <td>yes</td>
                        <td>values can be retrieved using the api/v1/SPO API</td>
                      </tr>
                      <tr>
                        <td>limit</td>
                        <td>(int) AKPs limit output</td>
                        <td>limit=20</td>
                        <td>yes</td>
                        <td>if not specified will return all the matching AKPs</td>
                      </tr>
                      <tr>
                        <td>offset</td>
                        <td>(int) AKPs offset</td>
                        <td>offset=40</td>
                        <td>yes</td>
                        <td>default value is 0</td>
                      </tr>
                      <tr>
                        <td>enrichWithSPO</td>
                        <td>boolean value</td>
                        <td>enrichWithSPO=true</td>
                        <td>yes</td>
                        <td>if true subject, predicate and object frequencies are showed</td>
                      </tr>
                    </tbody>
                  </table>
                </div>
                <div>Example 1: 
                  <a href="api/v1/browse?enrichWithSPO=true&limit=20">http://localhost/api/v1/browse?enrichWithSPO=true&limit=20</a>
                </div>
                 <div>Example 2: 
                  <a href="api/v1/browse?subj=http://dbpedia.org/ontology/Settlement">http://localhost/api/v1/browse?subj=http://dbpedia.org/ontology/Settlement</a>
                </div>
                <div>Example 3: 
                  <a href="api/v1/browse?enrichWithSPO=true&limit=20&pred=http://dbpedia.org/ontology/birthPlace&subj=http://xmlns.com/foaf/0.1/Person">http://localhost/api/v1/browse?enrichWithSPO=true&limit=20&pred=http://dbpedia.org/ontology/birthPlace&subj=http://xmlns.com/foaf/0.1/Person</a>
                </div>
                <div>Example 4: 
                  <a href="api/v1/browse?enrichWithSPO=true&limit=20&offset=40&pred=http://dbpedia.org/ontology/birthPlace&summary=6d0ed511-f8c9-4b72-8c04-1f4cba9b9210">http://localhost/api/v1/browse?enrichWithSPO=true&limit=20&offset=40&pred=http://dbpedia.org/ontology/birthPlace&summary=6d0ed511-f8c9-4b72-8c04-1f4cba9b9210</a>
                </div>
              </div>
            </div>
            <!-- /.box-body -->
          </div>
          <!-- /.box -->
        </div>


        <div class="col-md-12">
          <div class="box box-warning collapsed-box">
            <div class="box-header with-border">
              <h3 class="box-title">/api/v1/SolrSuggestions</h3>
              <font size="2" color="grey">(for indexed summaries)</font>
              <div class="box-tools pull-right">
                <button type="button" class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-plus"></i>
                </button>
              </div>
              <!-- /.box-tools -->
            </div>
            <!-- /.box-header -->
            <div class="box-body">
              <div class="col-md-12">
                <div>This API allows a user get suggestions based on the specified constraints.</div>
                <div>
                  <table id="example2" class="table table-bordered table-hover table-striped">
                    <thead>
                      <tr>
                        <th style="width:8%"">Parameter</th>
                        <th style="width:12%">Values</th>
                        <th style="width:12%">Example</th>
                        <th style="width:6%">Optional</th>
                        <th>Notes</th>
                      </tr>
                    </thead>
                    <tbody>
                      <tr>
                        <td>subj</td>
                        <td>(string) concept URI</td>
                        <td>subj=http://www.ontologydesignpatterns.org/ont/dul/DUL.owl%23Agent</td>
                        <td>yes</td>
                        <td>values can be retrieved using the api/v1/SPO API</td>
                      </tr>
                      <tr>
                        <td>pred</td>
                        <td>(string) predicate URI</td>
                        <td>pred=http://dbpedia.org/property/birthPlace</td>
                        <td>yes</td>
                        <td>values can be retrieved using the api/v1/SPO API</td>
                      </tr>
                      <tr>
                        <td>obj</td>
                        <td>(string) concept URI</td>
                        <td>obj=http://dbpedia.org/ontology/Person</td>
                        <td>yes</td>
                        <td>values can be retrieved using the api/v1/SPO API</td>
                      </tr>
                      <tr>
                        <td>qString</td>
                        <td>(string) user input</td>
                        <td>qString=per</td>
                        <td>no</td>
                        <td>If user input has 2 chars or less, the function will search for matchings which begin with the user input otherwise the matching search will be extended to central substrings</td>
                      </tr>
                      <tr>
                        <td>qPosition</td>
                        <td>(string) subj/pred/obj</td>
                        <td>qPosition=subj</td>
                        <td>no</td>
                        <td>-</td>
                      </tr>
                      <tr>
                        <td>dataset</td>
                        <td>(string) target dataset</td>
                        <td>dataset=dbpedia-2015-10</td>
                        <td>yes</td>
                        <td>If not specified the search of the query will be extended to all the datasets</td>
                      </tr>
                      <tr>
                        <td>rows</td>
                        <td>(int) number of rows to return</td>
                        <td>rows=100</td>
                        <td>yes</td>
                        <td> A smaller value implicates better performance</td>
                      </tr>
                      <tr>
                        <td>start</td>
                        <td>(int) suggestions offset</td>
                        <td>start=100</td>
                        <td>yes</td>
                        <td>-</td>
                      </tr>
                      <tr>
                        <td>group</td>
                        <td>boolean value</td>
                        <td>group=true</td>
                        <td>yes</td>
                        <td> - to be used only if you have specified sub/pred/obj<br>
                             - if true duplicates are removed
                        </td>
                      </tr>
                    </tbody>
                  </table>
                </div>
                <div>Example 1: 
                  <a href="api/v1/SolrSuggestions?qString=a&qPosition=pred">http://localhost/api/v1/SolrSuggestions?qString=a&qPosition=pred</a>
                </div>
                <div>Example 2: 
                  <a href="api/v1/SolrSuggestions?qString=a&qPosition=pred&dataset=dbpedia-2015-10">http://localhost/api/v1/SolrSuggestions?qString=a&qPosition=pred&dataset=dbpedia-2015-10</a>
                </div>
                <div>Example 3: 
                  <a href="api/v1/SolrSuggestions?qString=a&qPosition=pred&subj=http://schema.org/Place">http://localhost/api/v1/SolrSuggestions?qString=a&qPosition=pred&subj=http://schema.org/Place</a>
                </div>
                <div>Example 4: 
                  <a href="api/v1/SolrSuggestions?qString=a&qPosition=pred&subj=http://schema.org/Place&group=true">http://localhost/api/v1/SolrSuggestions?qString=a&qPosition=pred&subj=http://schema.org/Place&group=true</a>
                </div>
                <div>Example 5: 
                  <a href="api/v1/SolrSuggestions?qString=a&qPosition=pred&subj=http://schema.org/Place&obj=http://www.w3.org/2001/XMLSchema%23double&group=true">http://localhost/api/v1/SolrSuggestions?qString=a&qPosition=pred&subj=http://schema.org/Place&obj=http://www.w3.org/2001/XMLSchema%23double&group=true</a>
                </div>
              </div>
            </div>
            <!-- /.box-body -->
          </div>
          <!-- /.box -->
        </div>

      </section>
    </div>
    <!-- /.content-wrapper -->
    <!-- Main Footer -->
    <footer class="main-footer">
      <strong> ABSTAT X.X.X </strong> source code is available on <a href="https://github.com/rAlvaPrincipe/abstatSpring">GitHub</a> under the <a href="https://www.gnu.org/licenses/">GNU Affero General Public License v3.010</a> licence.
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
